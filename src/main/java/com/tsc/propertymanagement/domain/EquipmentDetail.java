package com.tsc.propertymanagement.domain;

import com.tsc.propertymanagement.constants.enumtype.EquipmentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EquipmentDetail implements Serializable {

    private EquipmentStatus equipmentStatus;
    private KeyDetail keyDetail;
    private String equipmentRemarks;
    private String frontTirePressure;
    private String backTirePressure;
    private String equipmentLocation;
    @Builder.Default
    private List<Part> parts = new ArrayList<>();

}