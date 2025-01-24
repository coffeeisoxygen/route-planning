package com.coffeecode.core.model;

import java.util.UUID;

import org.jxmapviewer.viewer.GeoPosition;

import lombok.Data;

@Data
public class Location implements ILocation {
    private UUID id;
    private String name;
    private double longitude; 
    private double latitude;

    public Location(LocationDTO dto) {
        this.id = dto.getId() != null ? dto.getId() : UUID.randomUUID();
        this.name = dto.getName();
        this.longitude = dto.getLongitude();
        this.latitude = dto.getLatitude();
        validate();
    }

    // For map integration
    public Location(String name, GeoPosition position) {
        this(LocationDTO.builder()
            .id(UUID.randomUUID())
            .name(name)
            .longitude(position.getLongitude())
            .latitude(position.getLatitude())
            .build());
    }

    @Override
    public void validate() {
        // ...existing validation code...
    }

    @Override
    public double distanceTo(ILocation location) {
        // ...existing distance calculation...
    }
}