package com.tsc.propertymanagement.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Id;
import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "technicians")
public class Technician implements Serializable {

    @Id
    private String id;
    private String technicianId;
    private String firstName;
    private String lastName;
    private String contactNumber;
}
