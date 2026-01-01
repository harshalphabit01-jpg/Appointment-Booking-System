package com.Appointment_Booking_system.repository;

import com.Appointment_Booking_system.model.PatientProfile;
import com.Appointment_Booking_system.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PatientProfileRepo extends JpaRepository<PatientProfile, Integer> {
    Optional<PatientProfile> findByUser(User user);


}
