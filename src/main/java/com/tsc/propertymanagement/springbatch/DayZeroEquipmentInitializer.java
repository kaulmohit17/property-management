package com.tsc.propertymanagement.springbatch;

import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.domain.Sort;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.index.Index;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.UUID;

@Component
@ConditionalOnProperty(name = "app.seed.enabled", havingValue = "true")
public class DayZeroEquipmentInitializer implements ApplicationRunner {
    public static final String IMPORT_ID = "equipment-day-zero-v1";
    public static final String COLLECTION = "dataInitializations";
    private static final String LOCK_ID = IMPORT_ID + "-lock";
    private static final Logger log = LoggerFactory.getLogger(DayZeroEquipmentInitializer.class);
    private final Object initializationLock = new Object();
    private final MongoTemplate mongo;
    private final JobLauncher launcher;
    private final Job importEquipmentJob;

    public DayZeroEquipmentInitializer(MongoTemplate mongo, JobLauncher launcher, Job importEquipmentJob) {
        this.mongo = mongo;
        this.launcher = launcher;
        this.importEquipmentJob = importEquipmentJob;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        synchronized (initializationLock) {
            if (mongo.findById(IMPORT_ID, Document.class, COLLECTION) != null) {
                log.info("Day-zero equipment import already completed; preserving existing data.");
                return;
            }

            try {
                mongo.insert(new Document("_id", LOCK_ID).append("startedAt", new Date()), COLLECTION);
            } catch (DuplicateKeyException e) {
                log.info("Day-zero equipment import already in progress; skipping duplicate startup.");
                return;
            }

            try {
                mongo.indexOps("equipment").ensureIndex(new Index("tagNumber", Sort.Direction.ASC).unique().sparse());
                JobExecution execution = launcher.run(importEquipmentJob, new JobParametersBuilder()
                        .addString("initialization", IMPORT_ID)
                        .addString("attempt", UUID.randomUUID().toString())
                        .toJobParameters());
                if (execution.getStatus() != BatchStatus.COMPLETED) {
                    throw new IllegalStateException("Day-zero equipment import failed; no completion marker was saved. "
                            + execution.getAllFailureExceptions());
                }
                mongo.save(new Document("_id", IMPORT_ID)
                        .append("completedAt", new Date())
                        .append("jobExecutionId", execution.getId()), COLLECTION);
                log.info("Day-zero equipment import completed successfully.");
            } finally {
                mongo.remove(new Document("_id", LOCK_ID), COLLECTION);
            }
        }
    }
}
