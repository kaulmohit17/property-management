package com.tsc.propertymanagement.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TechnicianDto {

    private String technicianId;
    private String firstName;
    private String lastName;
    private String contactNumber;
}
