package com.Appointment_Booking_system.repository;

import com.Appointment_Booking_system.model.DoctorAddress;
import com.Appointment_Booking_system.model.DoctorProfile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DoctorAddressRepo extends JpaRepository<DoctorAddress, Long> {

    Optional<DoctorAddress> findByDoctorProfile(DoctorProfile doctorProfile);

    List<DoctorAddress> findAllByDoctorProfile(DoctorProfile doctorProfile);


}
