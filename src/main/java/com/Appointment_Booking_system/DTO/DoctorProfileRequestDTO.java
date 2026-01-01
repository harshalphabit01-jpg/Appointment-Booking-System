package com.Appointment_Booking_system.DTO;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DoctorProfileRequestDTO {


    @NotBlank(message = "Image URL cannot be empty")
    private String image;

    @NotBlank(message = "Specialities cannot be empty")
    private String specialities;

    @NotBlank(message = "Available days cannot be empty")
    private String availableDays; // e.g., "MONDAY,WEDNESDAY,FRIDAY"

    @NotNull(message = "Available start time cannot be null")
    private LocalTime availableStartTime;

    @NotNull(message = "Available end time cannot be null")
    private LocalTime availableEndTime;

    @NotBlank(message = "State medical council cannot be empty")
    private String stateMedicalCouncil;

    @NotBlank(message = "Place of experience cannot be empty")
    private String placeOfExp;

    @Min(value = 0, message = "Experience year cannot be negative")
    private int experienceYear;

    @NotBlank(message = "Year of graduation cannot be empty")
    private String yearOfGraduation;

    @DecimalMin(value = "0.0", inclusive = false, message = "Payment charge must be positive")
    private Double paymentCharge;

    private String details; // Optional field

    @NotNull(message = "User ID cannot be null")
    private Long userId; // to link with the User entity




}
