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
public class ReleasedEquipmentItemReader {

    @Value("classpath:released-equipment.csv")
    private Resource releasedEquipmentResource;

    @Bean
    public FlatFileItemReader<FieldSet> releaseEquipmentFlatFileItemReader() {
        FlatFileItemReader<FieldSet> flatFileItemReader = new FlatFileItemReader<>();
        flatFileItemReader.setResource(releasedEquipmentResource);
        DefaultLineMapper<FieldSet> defaultLineMapper = new DefaultLineMapper<>();
        defaultLineMapper.setLineTokenizer(releasedEquipmentTokenizer());
        defaultLineMapper.setFieldSetMapper(new PassThroughFieldSetMapper());
        flatFileItemReader.setLineMapper(defaultLineMapper);
        return flatFileItemReader;
    }

    private LineTokenizer releasedEquipmentTokenizer() {
        DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();
        lineTokenizer.setDelimiter(",");
        lineTokenizer.setNames("equipmentType", "manufacturer", "tagNumber", "equipmentModel", "serialNumber", "equipmentDetail");
        lineTokenizer.setIncludedFields(0, 1, 2, 3, 4);
        return lineTokenizer;
    }
}
