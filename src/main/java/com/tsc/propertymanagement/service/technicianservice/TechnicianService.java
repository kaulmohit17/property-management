package com.tsc.propertymanagement.service.technicianservice;

import com.tsc.propertymanagement.domain.Technician;
import com.tsc.propertymanagement.dto.TechnicianDto;
import com.tsc.propertymanagement.mapper.TechnicianMapper;
import com.tsc.propertymanagement.repository.TechnicianRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TechnicianService {

    private final TechnicianRepository technicianRepository;
    private final TechnicianMapper technicianMapper;

    public void addTechnician(List<TechnicianDto> technicians) {
        technicianRepository.insert(technicians.stream()
                .map(technicianMapper::toTechnician)
                .collect(Collectors.toList()));
    }

    public List<TechnicianDto> getTechnician() {
        return technicianRepository.findAll().stream()
                .map(technicianMapper::toTechnicianDto)
                .collect(Collectors.toList());
    }
}
