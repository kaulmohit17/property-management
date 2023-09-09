package com.tsc.propertymanagement.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EquipmentDto {

    private String id;
    private String tagNumber;
    private String serialNumber;
    private String manufacturer;
    private String equipmentType;
    private String equipmentModel;
}
