package com.coffeecode.presentation.viewmodel.state;

import java.util.List;

import com.coffeecode.domain.model.Locations;

public record LocationState(
        List<Locations> locations,
        Locations selectedLocation) {

}
