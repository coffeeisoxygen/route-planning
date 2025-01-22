package com.coffeecode.presentation.viewmodel.listener;

import java.util.List;

import com.coffeecode.domain.model.Locations;

public interface RouteListener {

    void onRouteCalculated(List<Locations> route);
}
