package com.tsc.propertymanagement.domain;


import com.tsc.propertymanagement.constants.enumtype.MaintenanceType;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "maintenanceRecord")
public class MaintenanceRecord implements Serializable {

    @Id
    private String id;
    private double maintenanceCost;
    private String maintenanceFacilityName;
    private String maintenanceAdditionalNotes;
    private String equipmentIssue;
    private Technician requestedBy;
    private MaintenanceType maintenanceType;
    private LocalDate requestedDate;
    private LocalDate startDate;
    private LocalDate completionDate;
    @DBRef
    private Equipment equipment;
    @Builder.Default
    private List<Technician> technicians = new ArrayList<>(); // these are the techs who worked on the machine to complete the task
    @Builder.Default
    private List<Part> parts = new ArrayList<>();
}
