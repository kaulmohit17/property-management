package com.tsc.propertymanagement.springbatch;

import org.bson.Document;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.*;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.boot.DefaultApplicationArguments;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.index.IndexOperations;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class DayZeroEquipmentInitializerTests {
    private final MongoTemplate mongo = mock(MongoTemplate.class);
    private final JobLauncher launcher = mock(JobLauncher.class);
    private final Job job = mock(Job.class);
    private final DayZeroEquipmentInitializer initializer = new DayZeroEquipmentInitializer(mongo, launcher, job);

    @Test
    void completedImportDoesNotRunAgain() throws Exception {
        when(mongo.findById(DayZeroEquipmentInitializer.IMPORT_ID, Document.class,
                DayZeroEquipmentInitializer.COLLECTION)).thenReturn(new Document("completedAt", "already done"));
        initializer.run(new DefaultApplicationArguments());
        verifyNoInteractions(launcher);
        verify(mongo, never()).save(any(), anyString());
    }

    @Test
    void failureDoesNotMarkDatabaseInitialized() throws Exception {
        when(mongo.indexOps("equipment")).thenReturn(mock(IndexOperations.class));
        JobExecution execution = new JobExecution(1L);
        execution.setStatus(BatchStatus.FAILED);
        when(launcher.run(eq(job), any())).thenReturn(execution);
        assertThatThrownBy(() -> initializer.run(new DefaultApplicationArguments()))
                .isInstanceOf(IllegalStateException.class).hasMessageContaining("no completion marker");
        verify(mongo, never()).save(any(), anyString());
    }

    @Test
    void concurrentStartupSkipsDuplicateInitialization() throws Exception {
        when(mongo.findById(DayZeroEquipmentInitializer.IMPORT_ID, Document.class,
                DayZeroEquipmentInitializer.COLLECTION)).thenReturn(null);
        doThrow(new DuplicateKeyException("duplicate lock")).when(mongo)
                .insert(any(Document.class), eq(DayZeroEquipmentInitializer.COLLECTION));

        initializer.run(new DefaultApplicationArguments());

        verifyNoInteractions(launcher);
        verify(mongo, never()).save(any(), anyString());
    }

    @Test
    void completionIsPersistedOnlyAfterSuccessfulJob() throws Exception {
        when(mongo.indexOps("equipment")).thenReturn(mock(IndexOperations.class));
        JobExecution execution = new JobExecution(1L);
        execution.setStatus(BatchStatus.COMPLETED);
        when(launcher.run(eq(job), any())).thenReturn(execution);
        initializer.run(new DefaultApplicationArguments());
        verify(mongo).save(argThat((Document d) -> DayZeroEquipmentInitializer.IMPORT_ID.equals(d.getString("_id"))
                && d.getDate("completedAt") != null), eq(DayZeroEquipmentInitializer.COLLECTION));
    }
}
