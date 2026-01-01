package com.Appointment_Booking_system.DTO;

import com.Appointment_Booking_system.model.GenderStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class PatientProfileResponseDTO {

    private Integer  id;
    private String photo;
    private LocalDate dateOfBirth;
    private String bloodGroup;
    private GenderStatus gender;
    private Integer  userId;


}
