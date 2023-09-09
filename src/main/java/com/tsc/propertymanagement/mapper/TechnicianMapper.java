package com.tsc.propertymanagement.mapper;

import com.tsc.propertymanagement.domain.Technician;
import com.tsc.propertymanagement.dto.TechnicianDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TechnicianMapper {

    Technician toTechnician(TechnicianDto technicianDto);

    TechnicianDto toTechnicianDto(Technician technician);
}
