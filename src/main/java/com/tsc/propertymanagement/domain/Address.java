package com.tsc.propertymanagement.domain;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Address implements Serializable {

    private String unitNumber;
    private String streetName;
    private String city;
    private String state;
    private String country;
    private String postalCode;
}
