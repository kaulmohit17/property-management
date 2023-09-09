package com.tsc.propertymanagement.controller;

import com.tsc.propertymanagement.dto.MaintenanceRecordDto;
import com.tsc.propertymanagement.service.maintenanceRecordService.MaintenanceRecordService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.ResponseEntity.status;

/**
 * controller will be used to access the information about the equipment maintenance service logs
 */
@RestController
@RequestMapping(value = "/api/v1/maintenanceRecords")
@RequiredArgsConstructor
public class MaintenanceLogController {

    private final MaintenanceRecordService maintenanceRecordService;

    /**
     * operation is used to add maintenance Record to the equipment
     * @param maintenanceRecordDto maintenance record that will be associated to an equipment
     * @return no resource is returned except the HTTP Status
     */
    @PostMapping()
    public ResponseEntity<Void> addMaintenanceRecord(@RequestBody MaintenanceRecordDto maintenanceRecordDto) {
        maintenanceRecordService.addMaintenanceRecord(maintenanceRecordDto);
        return status(HttpStatus.NO_CONTENT)
                .build();
    }

    /**
     * operation is used to get the maintenance using the maintenanceRecordId
     * @param maintenanceRecordId unique identifier for a maintenance Record
     * @return maintenance Record that is previously added for an equipment
     */
    @GetMapping("/{maintenanceRecordId}")
    public ResponseEntity<MaintenanceRecordDto> getMaintenanceRecordById(@PathVariable String maintenanceRecordId) {
        return status(HttpStatus.OK)
                .body(maintenanceRecordService.getMaintenanceRecord(maintenanceRecordId));
    }

    /**
     * operation is used to delete a maintenance record
     * @param maintenanceRecordId unique identifier for a maintenance record
     * @return no resource is returned except the HTTP Status
     */
    @DeleteMapping("/{maintenanceRecordId}")
    public ResponseEntity<Void> deleteMaintenanceRecord(@PathVariable String maintenanceRecordId) {
        maintenanceRecordService.deleteMaintenanceRecordById(maintenanceRecordId);
        return status(HttpStatus.OK)
                .build();
    }

    /**
     * operation is used to update an existing maintenance record
     * @param maintenanceRecordDto updated information for the maintenance record
     * @param maintenanceRecordId unique identifer for the maintenance record
     * @return no resource is returned except the HTTP Status
     */
    @PutMapping("/{maintenanceRecordId}")
    public ResponseEntity<Void> updateEquipment(@RequestBody MaintenanceRecordDto maintenanceRecordDto, @PathVariable String maintenanceRecordId) {
        maintenanceRecordService.updateMaintenanceRecord(maintenanceRecordDto, maintenanceRecordId);
        return status(HttpStatus.OK)
                .build();
    }

    /**
     * operation is used to get maintenance records for an equipment
     * @param equipmentTagNumber
     * @return
     */
    @GetMapping("/equipments/{equipmentTagNumber}")
    public ResponseEntity<List<MaintenanceRecordDto>> getMaintenanceRecordByEquipmentTagNumber(@PathVariable String equipmentTagNumber) {
        return status(HttpStatus.OK)
                .body(maintenanceRecordService.getMaintenanceRecordByEquipmentTagNumber(equipmentTagNumber));
    }

}
