package com.tsc.propertymanagement.springbatch.fieldsetmapper;

import com.tsc.propertymanagement.domain.Equipment;
import com.tsc.propertymanagement.domain.EquipmentDetail;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindException;

@Service(EquipmentTechnicalDetailsMapper.BEAN_ID)
public class EquipmentTechnicalDetailsMapper implements FieldSetMapper<Equipment> {

    public static final String BEAN_ID = "equipmentTechnicalDetailsFieldSetMapper";

    @Override
    public Equipment mapFieldSet(FieldSet fieldSet) throws BindException {
        return Equipment.builder()
                .equipmentType(fieldSet.readString("equipmentType"))
                .manufacturer(fieldSet.readString("manufacturer"))
                .equipmentModel(fieldSet.readString("equipmentModel"))
                .tagNumber(fieldSet.readString("tagNumber"))
                .serialNumber(fieldSet.readString("serialNumber"))
                .equipmentDetail(EquipmentDetail.builder()
                        .frontTirePressure(fieldSet.readString("equipmentDetail").split(",")[0])
                        .backTirePressure(fieldSet.readString("equipmentDetail").split(",")[1])
                        .build())
                .build();
    }
}