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
public class EquipmentInformationItemReader {

    @Value("classpath:equipment-info.csv")
    private Resource equipmentInfoResource;

    @Bean
    public FlatFileItemReader<FieldSet> equipmentInformationFlatFileItemReader() {
        FlatFileItemReader<FieldSet> flatFileItemReader = new FlatFileItemReader<>();
        flatFileItemReader.setResource(equipmentInfoResource);
        DefaultLineMapper<FieldSet> defaultLineMapper = new DefaultLineMapper<>();
        defaultLineMapper.setLineTokenizer(equipmentInformationTokenizer());
        defaultLineMapper.setFieldSetMapper(new PassThroughFieldSetMapper());
        flatFileItemReader.setLineMapper(defaultLineMapper);
        return flatFileItemReader;
    }

    private LineTokenizer equipmentInformationTokenizer() {
        DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer();
        String[] names = {"equipmentType", "manufacturer", "equipmentModel", "tagNumber"};
        tokenizer.setNames(names);
        return tokenizer;
    }
}
