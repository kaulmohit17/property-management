package com.tsc.propertymanagement.controller;

import com.tsc.propertymanagement.dto.EquipmentDto;
import com.tsc.propertymanagement.dto.TechnicianDto;
import com.tsc.propertymanagement.service.equipmentService.EquipmentService;
import com.tsc.propertymanagement.service.technicianservice.TechnicianService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.ResponseEntity.status;

/**
 * controller for accessing properties of different equipments used in the property
 */
@RestController
@RequestMapping(value = "/api/v1/equipments")
@RequiredArgsConstructor
public class EquipmentController {

    private final EquipmentService equipmentService;
    private final TechnicianService technicianService;

    /**
     * method is used for posting new equipment to the database
     * @param equipmentDto equipment information that needs to be stored in the database
     * @return no resource is returned
     */
    @PostMapping()
    public ResponseEntity<List<EquipmentDto>> addEquipment(@RequestBody EquipmentDto equipmentDto) {
        equipmentService.addEquipment(equipmentDto);
        return status(HttpStatus.NO_CONTENT)
                .build();
    }

    /**
     * operation used for getting list of equipments saved in our database
     * @return list of all equipments registered in the database
     */
    @GetMapping()
    public ResponseEntity<List<EquipmentDto>> getEquipments() {
        return status(HttpStatus.OK)
                .body(equipmentService.getEquipments());
    }

    /**
     * operation is used to get equipments using the equipment id
     * @param equipmentId the unique identifier for the equipment
     * @return Equipment associated with unique equipment id
     */
    @GetMapping("/{equipmentId}")
    public ResponseEntity<EquipmentDto> getEquipment(@PathVariable String equipmentId) {
        return status(HttpStatus.OK)
                        .body(equipmentService.getEquipmentById(equipmentId));
    }

    /**
     * operation will be used for updating the equipment information in the database
     * @param equipmentId unique identifier for the equipment
     * @param equipmentDto equipment information that needs to be updated
     * @return no resource is returned regarding the updated information
     */
    @PutMapping("/{equipmentId}")
    public ResponseEntity<Void> updateEquipmentById(@PathVariable String equipmentId, @RequestBody EquipmentDto equipmentDto) {
        equipmentService.updateEquipmentById(equipmentId, equipmentDto);
        return status(HttpStatus.OK)
                .build();
    }

    /**
     * this is just a test operation to add technicians bulk to the mongo db
     */

    @PostMapping("/technicians")
    public ResponseEntity<Void> addTechnicians(@RequestBody List<TechnicianDto> technicianDto) {
        technicianService.addTechnician(technicianDto);
        return status(HttpStatus.CREATED)
                .build();
    }
}
