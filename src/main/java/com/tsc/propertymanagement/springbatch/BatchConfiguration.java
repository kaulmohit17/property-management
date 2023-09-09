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
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.data.MongoItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;

@Configuration
@EnableBatchProcessing
public class BatchConfiguration {

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private EquipmentInformationProcessor equipmentInformationProcessor;

    @Autowired
    private EquipmentKeyDetailsProcessor equipmentKeyDetailsProcessor;

    @Autowired
    private EquipmentTechnicalDetailsProcessor equipmentTechnicalDetailsProcessor;

    @Autowired
    private ReleasedEquipmentProcessor releasedEquipmentProcessor;

    @Bean
    public MongoItemWriter<Equipment> writer() {
        MongoItemWriter<Equipment> writer = new MongoItemWriter<>();
        writer.setTemplate(mongoTemplate);
        writer.setCollection("equipment");
        return writer;
    }

    @Bean
    public Job importEquipmentJob(JobCompletionNotificationListener listener) {
        return jobBuilderFactory.get("importEquipmentJob")
                .incrementer(new RunIdIncrementer())
                .listener(listener)
                .start(equipmentInformationStep())
                .next(equipmentKeyDetailStep())
                .next(equipmentTechnicalDetailStep())
                .next(releaseEquipmentStep())
                .build();
    }

    @Bean
    public Step equipmentInformationStep() {
        return stepBuilderFactory.get("equipmentInformationStep")
                .<Equipment, Equipment>chunk(10)
                .reader(equipmentInformationProcessor)
                .writer(writer())
                .build();
    }

    @Bean
    public Step equipmentKeyDetailStep() {
        return stepBuilderFactory.get("equipmentKeyDetailStep")
                .<Equipment, Equipment>chunk(10)
                .reader(equipmentKeyDetailsProcessor)
                .writer(writer())
                .build();
    }

    @Bean
    public Step equipmentTechnicalDetailStep() {
        return stepBuilderFactory.get("equipmentTechnicalDetailStep")
                .<Equipment, Equipment>chunk(10)
                .reader(equipmentTechnicalDetailsProcessor)
                .writer(writer())
                .build();
    }

    @Bean
    public Step releaseEquipmentStep() {
        return stepBuilderFactory.get("releaseEquipmentStep")
                .<Equipment, Equipment>chunk(10)
                .reader(releasedEquipmentProcessor)
                .writer(writer())
                .build();
    }

}
