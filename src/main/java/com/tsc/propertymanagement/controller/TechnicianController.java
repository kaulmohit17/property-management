package com.tsc.propertymanagement.controller;

import com.tsc.propertymanagement.dto.TechnicianDto;
import com.tsc.propertymanagement.service.TechnicianService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.ResponseEntity.status;

/**
 * controller for accessing technicians in the system
 */
@RestController
@RequestMapping(value = "/api/v1/technicians")
@RequiredArgsConstructor
public class TechnicianController {
    private final TechnicianService technicianService;


    /**
     * operation to add technicians bulk to the mongo db
     */

    @PostMapping()
    public ResponseEntity<Void> addTechnicians(@RequestBody List<TechnicianDto> technicianDto) {
        technicianService.addTechnician(technicianDto);
        return status(HttpStatus.CREATED)
                .build();
    }

    @GetMapping()
    public ResponseEntity<List<TechnicianDto>> getTechnicians() {
        return status(HttpStatus.OK)
                .body(technicianService.getTechnician());
    }
}
