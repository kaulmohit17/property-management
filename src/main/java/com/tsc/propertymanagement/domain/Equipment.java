package com.tsc.propertymanagement.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "equipment")
public class Equipment implements Serializable {

    @Id
    private String id;
    private String tagNumber;
    private String serialNumber;
    private String manufacturer;
    private String equipmentType;
    private String equipmentModel;
    private EquipmentDetail equipmentDetail;
}
