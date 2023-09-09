package com.tsc.propertymanagement.springbatch.equipmentprocessor;

import com.tsc.propertymanagement.domain.Equipment;
import com.tsc.propertymanagement.springbatch.fieldsetmapper.EquipmentKeyDetailsMapper;
import org.springframework.batch.item.*;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class EquipmentKeyDetailsProcessor implements ItemReader<Equipment>, ItemStream {

    @Autowired
    @Qualifier("equipmentKeyDetailsFlatFileItemReader")
    private FlatFileItemReader<FieldSet> fieldSetFlatFileItemReader;

    @Autowired
    @Qualifier(EquipmentKeyDetailsMapper.BEAN_ID)
    private FieldSetMapper<Equipment> equipmentKeyDetailsFieldSetMapper;

    @Override
    public Equipment read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
        FieldSet fieldSet = fieldSetFlatFileItemReader.read();
                return equipmentKeyDetailsFieldSetMapper.mapFieldSet(fieldSet);
    }

    @Override
    public void open(ExecutionContext executionContext) throws ItemStreamException {
        fieldSetFlatFileItemReader.open(executionContext);
    }

    @Override
    public void update(ExecutionContext executionContext) throws ItemStreamException {
        fieldSetFlatFileItemReader.update(executionContext);
    }

    @Override
    public void close() throws ItemStreamException {
        fieldSetFlatFileItemReader.close();
    }
}
