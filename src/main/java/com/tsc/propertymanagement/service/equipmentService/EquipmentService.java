package com.tsc.propertymanagement.service.equipmentService;

import com.tsc.propertymanagement.domain.Equipment;
import com.tsc.propertymanagement.dto.EquipmentDto;
import com.tsc.propertymanagement.exception.ErrorCode;
import com.tsc.propertymanagement.exception.ServiceException;
import com.tsc.propertymanagement.mapper.EquipmentMapper;
import com.tsc.propertymanagement.repository.EquipmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class EquipmentService {

    private final EquipmentRepository equipmentRepository;
    private final EquipmentMapper equipmentMapper;

    public void addEquipment(EquipmentDto equipmentDto) {
        equipmentRepository.save(equipmentMapper.toEquipment(equipmentDto));
    }

    public List<EquipmentDto> getEquipments() {return equipmentRepository.findAll().stream()
                .map(equipmentMapper::toEquipmentDto)
                .collect(Collectors.toList());
    }

    public EquipmentDto getEquipmentById(String equipmentId) {
        return equipmentRepository.findById(equipmentId).stream()
                .findAny()
                .map(equipmentMapper::toEquipmentDto)
                .orElseThrow(() -> new ServiceException(ErrorCode.EQUIPMENT_NOT_FOUND));
    }

    public Equipment getEquipmentByEquipmentTagNumber(String equipmentNumber) {
        Optional<List<Equipment>> byTagNumber = equipmentRepository.findByTagNumber(equipmentNumber);
        // TODO: 2023-03-17 need to fix this query
        return byTagNumber
                .map(item -> item.get(0))
                .orElseThrow(() -> new ServiceException(ErrorCode.EQUIPMENT_NOT_FOUND));
    }

    public void updateEquipmentById(String equipmentId, EquipmentDto equipmentDto) {
        equipmentRepository.findById(equipmentId)
                .ifPresent(equipment -> equipmentRepository.save(equipmentMapper.toEquipment(equipmentDto)));
    }
}
