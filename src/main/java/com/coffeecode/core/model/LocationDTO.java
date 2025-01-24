package com.coffeecode.core.model;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LocationDTO {

    private UUID id;
    private String name;
    private double longitude;
    private double latitude;
}
