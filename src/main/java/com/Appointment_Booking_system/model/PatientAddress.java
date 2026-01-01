package com.Appointment_Booking_system.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PatientAddress {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private String addressLine;
    private String city;
    private String state;
    private String pinCode;
    private String country;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_profile_id", nullable = false)
    @JsonBackReference
    private PatientProfile patientProfile;


}
