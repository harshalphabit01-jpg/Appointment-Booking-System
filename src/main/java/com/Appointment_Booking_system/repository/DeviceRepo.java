package com.Appointment_Booking_system.repository;

import com.Appointment_Booking_system.model.DeviceINFO;
import com.Appointment_Booking_system.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DeviceRepo extends JpaRepository<DeviceINFO, Integer> {

    Optional<DeviceINFO> findByDoctor(User doctor);

}
