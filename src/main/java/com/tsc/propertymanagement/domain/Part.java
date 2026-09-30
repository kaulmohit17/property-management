package com.tsc.propertymanagement.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "part")
public class Part implements Serializable {

    @Id
    private String id;
    private String serialNumber;
    private String supplierName;
    private double partCost;
    private Address supplierAddress;
    private Set<String> equipmentTagNumbers;
}