package com.coffeecode.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;


@Configuration
@ComponentScan(basePackages = {
    "com.coffeecode.application.service",
    "com.coffeecode.infrastructure.persistance",
    "com.coffeecode.infrastructure.distance"
})
public class BackendConfig {
    // Remove locationService bean if defined here
}
