package com.tsc.propertymanagement.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MaintenanceRecordDto {

    private String maintenanceType;
    private String equipmentIssue;
    private String maintenanceCost;
    private String maintenanceFacilityName;
    private TechnicianDto requestedBy;
    private String requestedDate;
    private String startDate;
    private String completionDate;
    @Builder.Default
    private List<TechnicianDto> technicians = new ArrayList<>();
    @Builder.Default
    private List<PartDto> parts = new ArrayList<>();
    private String maintenanceAdditionalNotes;
    private String maintenanceRecordId;
    @JsonProperty("tagNumber")
    private String equipmentTagNumber;
}