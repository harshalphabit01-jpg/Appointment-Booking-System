package com.Appointment_Booking_system.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;
import java.util.Set;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DoctorProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String image;

    private String specialities;

    @Column(name = "available_days")
    private String availableDays;

    private LocalTime available_start_time;

    private LocalTime available_end_time;

    private String state_medical_council;

    private String Place_of_exp;

    private Integer  experience_year;

    private String year_of_graduation;

    private Double payment_charge;

    private String details;

    @OneToOne
    @JoinColumn(name = "doctor_id", nullable = false, unique = true)
    @JsonIgnoreProperties({"password","deviceInfos","doctorProfile","patientProfile","role"})
    @JsonBackReference
    private User user;

    @OneToMany(mappedBy = "doctorProfile", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DoctorAddress> addresses;


}
