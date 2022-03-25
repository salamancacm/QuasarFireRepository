package com.rebel.quasarfireoperation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan("com.rebel.quasarfireoperation.config")
public class QuasarFireOperationApplication {

    public static void main(String[] args) {
        SpringApplication.run(QuasarFireOperationApplication.class, args);
    }

}
