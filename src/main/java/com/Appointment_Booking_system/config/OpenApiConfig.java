package com.Appointment_Booking_system.config;


import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Appointment Booking System API",
                version = "1.0",
                description = "Swagger documentation for Appointment Booking System"
        )
)
public class OpenApiConfig {
}