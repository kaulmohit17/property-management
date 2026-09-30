package com.tsc.propertymanagement.mapper;

import com.tsc.propertymanagement.constants.enumtype.MaintenanceType;
import com.tsc.propertymanagement.domain.MaintenanceRecord;
import com.tsc.propertymanagement.dto.MaintenanceRecordDto;
import org.apache.logging.log4j.util.Strings;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
@Mapper(componentModel = "spring")
public interface MaintenanceRecordMapper {

    @Mapping(target = "id", ignore = true)
    MaintenanceRecord toMaintenanceRecord(MaintenanceRecordDto maintenanceRecordDto);

    @Mapping(target = "id", ignore = true)
    MaintenanceRecord toMaintenanceRecordForUpdate(MaintenanceRecordDto maintenanceRecordDto, @MappingTarget MaintenanceRecord maintenanceRecord);

    @Mapping(target = "maintenanceRecordId", source = "id")
    MaintenanceRecordDto toMaintenanceRequestDto(MaintenanceRecord maintenanceRecord);

    default LocalDate toLocalDate(String localDate) {
        return Strings.isBlank(localDate) ? null : LocalDate.parse(localDate, DateTimeFormatter.ofPattern("MM/dd/yyyy"));
    }

    default MaintenanceType toMaintenanceType(String maintenanceType) {
        return Enum.valueOf(MaintenanceType.class, maintenanceType.toUpperCase());
    }
}
