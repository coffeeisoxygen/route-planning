package com.coffeecode.presentation.viewmodel.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.coffeecode.application.port.input.RouteCalculationUseCase;
import com.coffeecode.domain.model.Locations;
import com.coffeecode.presentation.viewmodel.base.AbstractViewModel;

@Component
public class RouteViewModel extends AbstractViewModel {

    private static final Logger logger = LoggerFactory.getLogger(RouteViewModel.class);

    private final RouteCalculationUseCase routeUseCase;
    private List<Locations> currentRoute;

    public RouteViewModel(RouteCalculationUseCase routeUseCase) {
        this.routeUseCase = routeUseCase;
    }

    public void calculateRoute(Locations start, Locations end) {
        try {
            currentRoute = routeUseCase.calculateRoute(start.id(), end.id());
            notifyRouteCalculated();
        } catch (Exception e) {
            handleError(e);
            throw e;
        }
    }

    private void notifyRouteCalculated() {
        notifyObservers(() -> observers.forEach(o
                -> o.onRouteCalculated(currentRoute)));
    }

    @Override
    protected void handleError(Exception e) {
        logger.error("Error calculating route", e);
    }

    @Override
    public void notifyObservers() {
        if (currentRoute != null) {
            notifyRouteCalculated();
        }
    }
}
