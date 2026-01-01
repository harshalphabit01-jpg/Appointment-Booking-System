package com.Appointment_Booking_system.DTO;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PatientAddressResponseDTO {
    private int id;
    private String addressLine;
    private String city;
    private String state;
    private String pinCode;
    private String country;

}
