package com.coffeecode.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({BackendConfig.class, FrontEndConfig.class})
public class AppConfig {
    // Empty as it just combines other configs
}
