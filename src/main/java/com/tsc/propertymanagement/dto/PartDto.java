package com.tsc.propertymanagement.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PartDto {

    private String serialNumber;
    private String supplierName;
    private String partCost;
    private AddressDto supplierAddress;
    private Set<String> equipmentTagNumbers;
}
