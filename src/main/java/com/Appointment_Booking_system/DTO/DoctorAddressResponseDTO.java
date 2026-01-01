package com.Appointment_Booking_system.DTO;

import lombok.Data;

@Data
public class DoctorAddressResponseDTO {

    private Long id;

    private String clinicName;

    private String addressLine;

    private String city;
    private String state;
    private String pincode;
    private String country;

    private Double latitude;
    private Double longitude;

    private String landmark;

    private Long doctorProfileId;
}
