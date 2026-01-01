package com.Appointment_Booking_system.service;


import com.Appointment_Booking_system.DTO.*;
import com.Appointment_Booking_system.model.Appointment;
import com.Appointment_Booking_system.model.DoctorProfile;
import com.Appointment_Booking_system.model.PatientProfile;
import com.Appointment_Booking_system.model.User;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface UserService {

    RegisterResponseDTO register(RegisterResponseDTO registerRequestDTO);


    OtpResponseDTO verifyOtp(OtpRequestDTO otpRequestDTO);

    LogoutResponseDTO logout(String token);

    LoginResponseDTO login(LoginRequestDTO loginRequestDTO);

    PatientProfileResponseDTO createProfile(PatientProfileRequestDTO patientProfileRequestDTO, String token);

    PatientAddressResponseDTO addPatientAddress(PatientAddressRequestDTO patientAddressRequestDTO, String token);

    DoctorProfileResponseDTO createDoctorProfile(DoctorProfileRequestDTO doctorProfileRequestDTO, String token);

    DoctorAddressResponseDTO addDoctorAddress(DoctorAddressRequestDTO doctorAddressRequestDTO, String token);

    List<DoctorResponseDTO> getAllDoctors(String token);

    DoctorProfile findDoctorById(Long doctorProfileId,String token);

    Appointment addAppoinment(String token,Appointment appointment);

    List<AppointmentResponseDTO> getAllAppointment(String token);

    Appointment getAppointmentById(Long appointmenId,String token);

    Appointment deleteAppointment(Long appointmentId, String token);

//    User getDetails(String token);

    UserResponseDTO getDetails(String token);
}
