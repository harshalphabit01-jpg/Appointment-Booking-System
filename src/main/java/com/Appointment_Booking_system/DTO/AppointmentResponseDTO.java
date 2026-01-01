package com.Appointment_Booking_system.DTO;

import com.Appointment_Booking_system.model.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AppointmentResponseDTO {

    private Long id;
    private LocalDate appointmentDate;
    private LocalTime appointmentTime;

    // Include patient profile with user
    private PatientProfileResponseDTO patientProfile;

    private PatientAddressResponseDTO patientAddressResponseDTO;

    @JsonIgnoreProperties({"password","deviceInfos","doctorProfile",
            "patientProfile","role","provider",
            "providerId","OAuth2User","manualUser"})
    private User user;

    // Doctor profile is optional, null for security
}
