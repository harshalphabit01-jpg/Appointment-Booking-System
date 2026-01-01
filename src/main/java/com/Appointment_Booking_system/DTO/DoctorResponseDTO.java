package com.Appointment_Booking_system.DTO;

import lombok.Data;
import java.util.List;

@Data
public class DoctorResponseDTO {

    private DoctorProfileResponseDTO doctorProfile;

    private UserResponseDTO user;

    private List<DoctorAddressResponseDTO> addresses;

}
