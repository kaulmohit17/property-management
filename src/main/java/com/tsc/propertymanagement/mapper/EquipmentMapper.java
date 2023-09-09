package com.tsc.propertymanagement.mapper;

import com.tsc.propertymanagement.domain.Equipment;
import com.tsc.propertymanagement.dto.EquipmentDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface EquipmentMapper {

    @Mapping(target = "id", ignore = true)
    Equipment toEquipment(EquipmentDto equipmentDto);

    EquipmentDto toEquipmentDto(Equipment equipment);
}
