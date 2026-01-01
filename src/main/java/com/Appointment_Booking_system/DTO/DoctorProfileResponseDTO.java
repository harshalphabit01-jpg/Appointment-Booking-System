package com.Appointment_Booking_system.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class DoctorProfileResponseDTO {

    private Long id;

    private String image;

    private String specialities;

    private String availableDays;

    private LocalTime availableStartTime;

    private LocalTime availableEndTime;

    private String stateMedicalCouncil;

    private String placeOfExp;

    private int experienceYear;

    private String yearOfGraduation;

    private Double paymentCharge;

    private String details;

    private Long userId;

}
