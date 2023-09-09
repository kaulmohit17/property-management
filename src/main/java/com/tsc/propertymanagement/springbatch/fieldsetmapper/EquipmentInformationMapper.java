package com.tsc.propertymanagement.springbatch.fieldsetmapper;

import com.tsc.propertymanagement.domain.Equipment;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindException;

@Service(EquipmentInformationMapper.BEAN_ID)
public class EquipmentInformationMapper implements FieldSetMapper<Equipment> {

    public static final String BEAN_ID = "equipmentInformationFieldSetMapper";

    @Override
    public Equipment mapFieldSet(FieldSet fieldSet) throws BindException {
        return Equipment.builder()
                .equipmentType(fieldSet.readString("equipmentType"))
                .manufacturer(fieldSet.readString("manufacturer"))
                .equipmentModel(fieldSet.readString("equipmentModel"))
                .tagNumber(fieldSet.readString("tagNumber"))
                .build();
    }
}
