package com.Appointment_Booking_system.repository;

import com.Appointment_Booking_system.model.PatientAddress;
import com.Appointment_Booking_system.model.PatientProfile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PatientAddressRepo extends JpaRepository<PatientAddress,Integer> {
    Optional<PatientAddress> findByPatientProfile(PatientProfile patientProfile);


}
