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
public class EquipmentKeyDetailsItemReader {

    @Value("classpath:equipment-keylist.csv")
    private Resource equipmentKeyListResource;

    @Bean
    public FlatFileItemReader<FieldSet> equipmentKeyDetailsFlatFileItemReader() {
        FlatFileItemReader<FieldSet> flatFileItemReader = new FlatFileItemReader<>();
        flatFileItemReader.setResource(equipmentKeyListResource);
        DefaultLineMapper<FieldSet> defaultLineMapper = new DefaultLineMapper<>();
        defaultLineMapper.setLineTokenizer(equipmentKeyDetailsTokenizer());
        defaultLineMapper.setFieldSetMapper(new PassThroughFieldSetMapper());
        flatFileItemReader.setLineMapper(defaultLineMapper);
        return flatFileItemReader;
    }

    private LineTokenizer equipmentKeyDetailsTokenizer() {
        DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();
        lineTokenizer.setDelimiter(",");
        lineTokenizer.setNames("equipmentType", "manufacturer","equipmentModel", "tagNumber", "equipmentDetail");
        lineTokenizer.setIncludedFields(0, 1, 2, 3, 4);
        return lineTokenizer;
    }
}

