package com.tsc.propertymanagement.springbatch.flatfileitemreader;

import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.mapping.PassThroughFieldSetMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.batch.item.file.transform.LineTokenizer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

@Configuration
public class EquipmentTechnicalItemReader {

    @Value("classpath:equipment-techicalDetails.csv")
    private Resource equipmentTechnicalDetailsResource;

    @Bean
    public FlatFileItemReader<FieldSet> equipmentTechnicalDetailsFlatFileItemReader() {
        FlatFileItemReader<FieldSet> flatFileItemReader = new FlatFileItemReader<>();
        flatFileItemReader.setResource(equipmentTechnicalDetailsResource);
        DefaultLineMapper<FieldSet> defaultLineMapper = new DefaultLineMapper<>();
        defaultLineMapper.setLineTokenizer(equipmentTechnicalDetailsTokenizer());
        defaultLineMapper.setFieldSetMapper(new PassThroughFieldSetMapper());
        flatFileItemReader.setLineMapper(defaultLineMapper);
        return flatFileItemReader;
    }

    private LineTokenizer equipmentTechnicalDetailsTokenizer() {
        DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();
        lineTokenizer.setDelimiter(",");
        lineTokenizer.setNames("equipmentType", "manufacturer", "tagNumber", "equipmentModel", "serialNumber", "frontTire", "rearTire", "remarks");
        lineTokenizer.setIncludedFields(0, 1, 2, 3, 4, 5, 6, 7);
        return lineTokenizer;
    }

}
