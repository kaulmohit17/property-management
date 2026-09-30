package com.tsc.propertymanagement.springbatch.fieldsetmapper;

import com.tsc.propertymanagement.constants.enumtype.EquipmentStatus;
import com.tsc.propertymanagement.domain.Equipment;
import com.tsc.propertymanagement.domain.EquipmentDetail;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindException;

@Service(ReleasedEquipmentMapper.BEAN_ID)
public class ReleasedEquipmentMapper implements FieldSetMapper<Equipment> {

    public static final String BEAN_ID = "ReleasedEquipmentFieldSetMapper";

    @Override
    public Equipment mapFieldSet(FieldSet fieldSet) throws BindException {
        return Equipment.builder()
                .equipmentType(fieldSet.readString("equipmentType"))
                .manufacturer(fieldSet.readString("manufacturer"))
                .tagNumber(fieldSet.readString("tagNumber"))
                .equipmentModel(fieldSet.readString("equipmentModel"))
                .serialNumber(fieldSet.readString("serialNumber"))
                .equipmentDetail(EquipmentDetail.builder()
                        .equipmentRemarks(fieldSet.readString("remarks"))
                        .equipmentStatus(EquipmentStatus.RELEASED)
                        .build())
                .build();
    }
}
