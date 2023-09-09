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
public class KeyDetail implements Serializable {

    private String primaryKey;
    private String secondaryKey;
    private String keyRemarks;

}
