package com.Appointment_Booking_system.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PatientProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private String photo;
    private LocalDate dateOfBirth;
    private String bloodGroup;
    @Enumerated(EnumType.STRING)
    private GenderStatus gender;

    @OneToOne
    @JoinColumn(name = "patient_id", nullable = false, unique = true)
    @JsonBackReference
    @JsonIgnoreProperties({"password","deviceInfos","doctorProfile","patientProfile","role"})
    private User user;

    @OneToMany(mappedBy = "patientProfile", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<PatientAddress> addresses;


}
