package com.tsc.propertymanagement.springbatch;

import com.tsc.propertymanagement.domain.Equipment;
import com.tsc.propertymanagement.springbatch.equipmentprocessor.EquipmentInformationProcessor;
import com.tsc.propertymanagement.springbatch.equipmentprocessor.EquipmentKeyDetailsProcessor;
import com.tsc.propertymanagement.springbatch.equipmentprocessor.EquipmentTechnicalDetailsProcessor;
import com.tsc.propertymanagement.springbatch.equipmentprocessor.ReleasedEquipmentProcessor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableBatchProcessing
@ConditionalOnProperty(name = "app.seed.enabled", havingValue = "true")
public class BatchConfiguration {

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Autowired
    private EquipmentSeedWriter equipmentSeedWriter;

    @Autowired
    private EquipmentQuantityImport equipmentQuantityImport;

    @Autowired
    private EquipmentInformationProcessor equipmentInformationProcessor;

    @Autowired
    private EquipmentKeyDetailsProcessor equipmentKeyDetailsProcessor;

    @Autowired
    private EquipmentTechnicalDetailsProcessor equipmentTechnicalDetailsProcessor;

    @Autowired
    private ReleasedEquipmentProcessor releasedEquipmentProcessor;

    @Bean
    public Job importEquipmentJob(JobCompletionNotificationListener listener) {
        return jobBuilderFactory.get("importEquipmentJob")
                .listener(listener)
                .start(equipmentInformationStep())
                .next(equipmentKeyDetailStep())
                .next(equipmentTechnicalDetailStep())
                .next(releaseEquipmentStep())
                .next(equipmentQuantityStep())
                .build();
    }

    @Bean
    public Step equipmentInformationStep() {
        return stepBuilderFactory.get("equipmentInformationStep")
                .<Equipment, Equipment>chunk(10)
                .reader(equipmentInformationProcessor)
                .writer(equipmentSeedWriter)
                .build();
    }

    @Bean
    public Step equipmentKeyDetailStep() {
        return stepBuilderFactory.get("equipmentKeyDetailStep")
                .<Equipment, Equipment>chunk(10)
                .reader(equipmentKeyDetailsProcessor)
                .writer(equipmentSeedWriter)
                .build();
    }

    @Bean
    public Step equipmentTechnicalDetailStep() {
        return stepBuilderFactory.get("equipmentTechnicalDetailStep")
                .<Equipment, Equipment>chunk(10)
                .reader(equipmentTechnicalDetailsProcessor)
                .writer(equipmentSeedWriter)
                .build();
    }

    @Bean
    public Step releaseEquipmentStep() {
        return stepBuilderFactory.get("releaseEquipmentStep")
                .<Equipment, Equipment>chunk(10)
                .reader(releasedEquipmentProcessor)
                .writer(equipmentSeedWriter)
                .build();
    }

    @Bean
    public Step equipmentQuantityStep() {
        return stepBuilderFactory.get("equipmentQuantityStep")
                .tasklet((contribution, context) -> {
                    equipmentQuantityImport.run();
                    return RepeatStatus.FINISHED;
                }).build();
    }

}
