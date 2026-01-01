package com.Appointment_Booking_system.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserResponseDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private String role;

    // If doctor, include doctor profile info
    private DoctorProfileResponseDTO doctorProfile;

    // If patient, include patient profile info
    private PatientProfileResponseDTO patientProfile;

    private DoctorAddressResponseDTO doctorAddressResponseDTO;

    private PatientAddressResponseDTO patientAddressResponseDTO;
}
