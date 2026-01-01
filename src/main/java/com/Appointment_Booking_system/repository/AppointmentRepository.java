package com.Appointment_Booking_system.repository;

import com.Appointment_Booking_system.model.Appointment;
import com.Appointment_Booking_system.model.DoctorProfile;
import com.Appointment_Booking_system.model.PatientProfile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    boolean existsByDoctorProfileAndAppointmentDateAndAppointmentTime(
            DoctorProfile doctorProfile,
            LocalDate appointmentDate,
            LocalTime appointmentTime
    );

    List<Appointment> findByDoctorProfile(DoctorProfile doctorProfile);

    List<Appointment> findByPatientProfile(PatientProfile patientProfile);

}
