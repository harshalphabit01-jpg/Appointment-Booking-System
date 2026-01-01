package com.Appointment_Booking_system.controllers;

import com.Appointment_Booking_system.DTO.*;
import com.Appointment_Booking_system.model.Appointment;
import com.Appointment_Booking_system.model.DoctorProfile;
import com.Appointment_Booking_system.model.GenderStatus;
import com.Appointment_Booking_system.model.RoleStatus;
import com.Appointment_Booking_system.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Tag(name = "User APIs", description = "All user related APIs")
@RestController
@RequestMapping("/user")
@CrossOrigin(origins = "http://localhost:5173")
public class UserController {

    private final UserService userService;

    // Constructor Injection (BEST PRACTICE)
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public RegisterResponseDTO addUser(@RequestBody RegisterResponseDTO registerResponseDTO){
        return userService.register(registerResponseDTO);
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<OtpResponseDTO> verifyOtp(
            @RequestBody @Valid OtpRequestDTO otpRequestDTO) {

        OtpResponseDTO response = userService.verifyOtp(otpRequestDTO);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/logout")
    public ResponseEntity<LogoutResponseDTO> logOut(@RequestHeader(value = "Authorization", required = true) String authorizationHeader
                                                   ){
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")){
            return ResponseEntity.badRequest().body(new LogoutResponseDTO("Missing or invalid Authorization header", false));
        }

        String token = authorizationHeader.substring(7);
        LogoutResponseDTO responseDTO = userService.logout(token );
        if (responseDTO.isSuccess()){
            return ResponseEntity.ok(responseDTO);
        }else{
            return ResponseEntity.status(400).body(responseDTO);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequestDTO loginRequestDTO){
        LoginResponseDTO responseDTO = userService.login(loginRequestDTO);

        if (responseDTO.isSuccess()){
            return ResponseEntity.ok(responseDTO);
        }else{
            return ResponseEntity.status(400).body(responseDTO);
        }
    }

    @PostMapping("/profile")
    public ResponseEntity<PatientProfileResponseDTO> createProfile(
            @RequestBody PatientProfileRequestDTO patientProfileRequestDTO,
            @RequestHeader("Authorization") String authHeader){

        if (authHeader == null || !authHeader.startsWith("Bearer ")){
            return ResponseEntity.badRequest().build();
        }
        String token = authHeader.substring(7);

        PatientProfileResponseDTO responseDTO = userService.createProfile(patientProfileRequestDTO,token);

        return ResponseEntity.ok(responseDTO);
    }

    @PostMapping("/patient/address")
    public ResponseEntity<PatientAddressResponseDTO> addPatientAddress(
            @RequestBody @Valid PatientAddressRequestDTO requestDTO,
            @RequestHeader("Authorization") String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")){
            return ResponseEntity.badRequest().build();
        }
        String token = authHeader.substring(7);


        return ResponseEntity.ok(
                userService.addPatientAddress(requestDTO, token)
        );
    }


    @PostMapping("/doctor/profile")
    public ResponseEntity<DoctorProfileResponseDTO> createDoctorProfile(
            @RequestBody DoctorProfileRequestDTO doctorProfileRequestDTO,
            @RequestHeader("Authorization") String authHeader){

        if (authHeader == null || !authHeader.startsWith("Bearer ")){
            return ResponseEntity.badRequest().build();
        }
        String token = authHeader.substring(7);

        DoctorProfileResponseDTO responseDTO = userService.createDoctorProfile(doctorProfileRequestDTO, token);

        return ResponseEntity.ok(responseDTO);
    }

    @PostMapping("/doctor/address")
    public ResponseEntity<DoctorAddressResponseDTO> createDoctorAddress(
           @RequestBody @Valid DoctorAddressRequestDTO requestDTO,
           @RequestHeader("Authorization") String authHeader){

        if (authHeader == null || !authHeader.startsWith("Bearer ")){
            return ResponseEntity.badRequest().build();
        }
        String token = authHeader.substring(7);

        return ResponseEntity.ok(userService.addDoctorAddress(requestDTO, token));
    }

    @GetMapping("/doctors")
    public ResponseEntity<?> getDoctors(
            @RequestHeader("Authorization") String authorization
    ) {
        String token = authorization.replace("Bearer ", "").trim();
        return ResponseEntity.ok(userService.getAllDoctors(token));
    }

    @GetMapping("/doctor/{id}")
    public ResponseEntity<?> getDoctorById(
            @PathVariable Long id,
            @RequestHeader("Authorization") String authorization
    ) {
        if (authorization == null || !authorization.startsWith("Bearer ")) {
            return ResponseEntity.badRequest().body("Invalid Authorization header");
        }

        String token = authorization.substring(7).trim();

        DoctorProfile doctorProfile = userService.findDoctorById(id, token);
        return ResponseEntity.ok(doctorProfile);
    }


    @PostMapping("/book")
    public ResponseEntity<Appointment> bookAppointment(
            @RequestHeader("Authorization") String authorization,
            @RequestBody Appointment appointment
    ) {
        if (authorization == null || !authorization.startsWith("Bearer ")) {
            return ResponseEntity.badRequest().build();
        }
        String token = authorization.substring(7);

        Appointment savedAppointment = userService.addAppoinment(token, appointment);
        return ResponseEntity.ok(savedAppointment);
    }

    @GetMapping("/appointments")
    public ResponseEntity<List<AppointmentResponseDTO>> getAllAppointments(@RequestHeader("Authorization") String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.badRequest().build();
        }
        String token = authHeader.substring(7);
        List<AppointmentResponseDTO> appointments = userService.getAllAppointment(token);
        return ResponseEntity.ok(appointments);
    }

    @GetMapping("/appointment/{id}")
    public ResponseEntity<Appointment> getAppointmentById(
            @PathVariable Long id,
            @RequestHeader("Authorization") String authorization
    ) {
        if (authorization == null || !authorization.startsWith("Bearer ")) {
            return ResponseEntity.badRequest().build();
        }

        String token = authorization.substring(7).trim();
        Appointment appointment = userService.getAppointmentById(id, token);

        return ResponseEntity.ok(appointment);
    }

    @DeleteMapping("/appointment/{id}")
    public ResponseEntity<Appointment> deleteAppointment(
            @PathVariable Long id,
            @RequestHeader("Authorization") String authorization
    ) {
        if (authorization == null || !authorization.startsWith("Bearer ")) {
            return ResponseEntity.badRequest().build();
        }

        String token = authorization.substring(7).trim();
        Appointment deletedAppointment = userService.deleteAppointment(id, token);

        return ResponseEntity.ok(deletedAppointment);
    }

    @GetMapping("/roles")
    public RoleStatus[] getRoles() {
        return RoleStatus.values();
    }

    @GetMapping("/gender")
    public GenderStatus[] getGender() {
        return GenderStatus.values();
    }

    @GetMapping("/user")
    public ResponseEntity<?> getUserDetails(
            @RequestHeader(value = "Authorization", required = true) String authorization
    ) {
        try {
            if (authorization == null || !authorization.startsWith("Bearer ")) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Map.of("error", "Authorization header missing or invalid"));
            }

            // Extract token
            String token = authorization.substring(7).trim();

            // Fetch user details from service
            UserResponseDTO userDetails = userService.getDetails(token);

            return ResponseEntity.ok(userDetails);

        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Something went wrong"));
        }
    }

}
