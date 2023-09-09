package com.tsc.propertymanagement.springbatch.fieldsetmapper;

import com.tsc.propertymanagement.domain.Equipment;
import com.tsc.propertymanagement.domain.EquipmentDetail;
import com.tsc.propertymanagement.domain.KeyDetail;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindException;

@Service(EquipmentKeyDetailsMapper.BEAN_ID)
public class EquipmentKeyDetailsMapper implements FieldSetMapper<Equipment> {

    public static final String BEAN_ID = "equipmentKeyDetailsFieldSetMapper";

    @Override
    public Equipment mapFieldSet(FieldSet fieldSet) throws BindException {
        return Equipment.builder()
                .equipmentType(fieldSet.readString("equipmentType"))
                .manufacturer(fieldSet.readString("manufacturer"))
                .equipmentModel(fieldSet.readString("equipmentModel"))
                .tagNumber(fieldSet.readString("tagNumber"))
                .equipmentDetail(EquipmentDetail.builder()
                        .keyDetail(KeyDetail.builder()
                                .primaryKey("equipmentDetail".split(",")[0])
                                .build())
                        .equipmentRemarks("equipmentDetail".split(",")[1])
                        .build())
                .build();
    }
}
