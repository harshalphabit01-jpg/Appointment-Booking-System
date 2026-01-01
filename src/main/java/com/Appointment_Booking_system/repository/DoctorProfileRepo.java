package com.Appointment_Booking_system.repository;

import com.Appointment_Booking_system.model.DoctorProfile;
import com.Appointment_Booking_system.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DoctorProfileRepo extends JpaRepository<DoctorProfile, Long> {

    Optional<DoctorProfile> findByUser(User user);

}
