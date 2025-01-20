package com.coffeecode.util.distance;

import java.math.BigDecimal;
import java.math.RoundingMode;

public interface DistanceCalculator {

    double calculate(double lat1, double lon1, double lat2, double lon2);

    String getStrategyName();

    default double roundToTwoDecimals(double value) {
        BigDecimal bd = BigDecimal.valueOf(value);
        bd = bd.setScale(2, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

}
