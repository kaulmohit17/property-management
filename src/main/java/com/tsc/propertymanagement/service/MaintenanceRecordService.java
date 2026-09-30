package com.tsc.propertymanagement.service;

import com.tsc.propertymanagement.domain.Equipment;
import com.tsc.propertymanagement.domain.MaintenanceRecord;
import com.tsc.propertymanagement.dto.MaintenanceRecordDto;
import com.tsc.propertymanagement.exception.ErrorCode;
import com.tsc.propertymanagement.exception.ServiceException;
import com.tsc.propertymanagement.mapper.MaintenanceRecordMapper;
import com.tsc.propertymanagement.repository.MaintenanceRecordRepository;
import com.tsc.propertymanagement.service.EquipmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class MaintenanceRecordService {

    private final MaintenanceRecordRepository maintenanceRecordRepository;
    private final MaintenanceRecordMapper maintenanceRecordMapper;
    private final EquipmentService equipmentService;

    public void addMaintenanceRecord(MaintenanceRecordDto maintenanceRecordDto) {
        MaintenanceRecord maintenanceRecord = maintenanceRecordMapper.toMaintenanceRecord(maintenanceRecordDto);
        Equipment equipment = equipmentService.getEquipmentByEquipmentTagNumber(maintenanceRecordDto.getEquipmentTagNumber());
        maintenanceRecord.setEquipment(equipment);
        maintenanceRecordRepository.save(maintenanceRecord);
    }

    public MaintenanceRecordDto getMaintenanceRecord(String maintenanceRecordId) {
        return maintenanceRecordRepository.findById(maintenanceRecordId)
                .map(maintenanceRecordMapper::toMaintenanceRequestDto)
                .orElseThrow(() -> new ServiceException(ErrorCode.MAINTENANCE_RECORD_NOT_FOUND));
    }

    public void deleteMaintenanceRecordById(String maintenanceRecordId) {
        maintenanceRecordRepository.deleteById(maintenanceRecordId);
    }

    public MaintenanceRecordDto updateMaintenanceRecord(MaintenanceRecordDto maintenanceRecordDto, String maintenanceRecordId) {
        MaintenanceRecord maintenanceRecord = maintenanceRecordRepository.findById(maintenanceRecordId)
                .orElseThrow(() -> new ServiceException(ErrorCode.MAINTENANCE_RECORD_NOT_FOUND));
        MaintenanceRecord updatedRecord = maintenanceRecordMapper.toMaintenanceRecordForUpdate(
                maintenanceRecordDto, maintenanceRecord);
        return maintenanceRecordMapper.toMaintenanceRequestDto(maintenanceRecordRepository.save(updatedRecord));
    }

    public List<MaintenanceRecordDto> getMaintenanceRecordByEquipmentTagNumber(String equipmentTagNumber) {
        String equipmentId = equipmentService.getEquipmentByEquipmentTagNumber(equipmentTagNumber).getId();
        return maintenanceRecordRepository.findByEquipmentId(equipmentId).stream()
                .map(maintenanceRecordMapper::toMaintenanceRequestDto)
                .collect(Collectors.toList());
    }
}