package com.coffeecode.core.model;

import java.util.UUID;

public interface ILocation {

    UUID getId();

    String getName();

    double getLongitude();

    double getLatitude();

    double distanceTo(ILocation other);

    void validate();
}
