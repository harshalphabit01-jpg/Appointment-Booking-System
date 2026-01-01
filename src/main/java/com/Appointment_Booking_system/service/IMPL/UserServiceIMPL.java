package com.Appointment_Booking_system.service.IMPL;

import com.Appointment_Booking_system.DTO.*;
import com.Appointment_Booking_system.config.JwtUtil;
import com.Appointment_Booking_system.model.*;
import com.Appointment_Booking_system.repository.*;
import com.Appointment_Booking_system.service.UserService;
import com.Appointment_Booking_system.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

@Service
public class UserServiceIMPL implements UserService {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private DeviceRepo deviceRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EmailService emailService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private PatientProfileRepo patientProfileRepo;

    @Autowired
    private DoctorProfileRepo doctorProfileRepo;

    @Autowired
    private PatientAddressRepo patientAddressRepo;

    @Autowired
    private DoctorAddressRepo doctorAddressRepo;

    @Autowired
    private AppointmentRepository appointmentRepository;

    // Generate 6 digit OTP
    private String generateOtp() {
        return String.valueOf(100000 + new Random().nextInt(900000));
    }

    // Entity → ResponseDTO
    private RegisterResponseDTO mapToResponse(User doctor) {
        RegisterResponseDTO response = new RegisterResponseDTO();
        response.setFirstName(doctor.getFirstName());
        response.setLastName(doctor.getLastName());
        response.setEmail(doctor.getEmail());
        response.setPhoneNumber(doctor.getPhoneNumber());
        response.setRole(doctor.getRole());
        return response;
    }

    // RequestDTO → Entity
    private User mapToEntity(RegisterResponseDTO requestDTO) {
        User doctor = new User();
        doctor.setFirstName(requestDTO.getFirstName());
        doctor.setLastName(requestDTO.getLastName());
        doctor.setEmail(requestDTO.getEmail());
        doctor.setPhoneNumber(requestDTO.getPhoneNumber());
        doctor.setPassword(requestDTO.getPassword());
        doctor.setRole(requestDTO.getRole());
        return doctor;
    }

    // Entity → ResponseDTO
    private PatientProfileResponseDTO mapToPatientProfileResponse(PatientProfile patientProfile){
        PatientProfileResponseDTO patientProfileResponseDTO = new PatientProfileResponseDTO();
        patientProfileResponseDTO.setId(patientProfile.getId());
        patientProfileResponseDTO.setPhoto(patientProfile.getPhoto());
        patientProfileResponseDTO.setDateOfBirth(
                patientProfile.getDateOfBirth());
        patientProfileResponseDTO.setBloodGroup(
                patientProfile.getBloodGroup());
        patientProfileResponseDTO.setGender(
                patientProfile.getGender());

        if (patientProfile.getUser() != null) {
            patientProfileResponseDTO.setUserId(
                    Math.toIntExact(patientProfile.getUser().getId()));
        }
        return patientProfileResponseDTO;
    }

    // RequestDTO → Entity
    private PatientProfile mapToPatientProfileEntity(PatientProfileRequestDTO requestDTO, User user) {
        PatientProfile patientProfile = new PatientProfile();
        patientProfile.setPhoto(requestDTO.getPhoto());
        patientProfile.setDateOfBirth(requestDTO.getDateOfBirth());
        patientProfile.setBloodGroup(requestDTO.getBloodGroup());
        patientProfile.setGender(requestDTO.getGender());
        patientProfile.setUser(user);
        return patientProfile;
    }

    private PatientAddressResponseDTO mapToPatientAddressResponse(PatientAddress address) {

        PatientAddressResponseDTO responseDTO = new PatientAddressResponseDTO();

        responseDTO.setId(address.getId());
        responseDTO.setAddressLine(address.getAddressLine());
        responseDTO.setCity(address.getCity());
        responseDTO.setState(address.getState());
        responseDTO.setPinCode(address.getPinCode());
        responseDTO.setCountry(address.getCountry());

        return responseDTO;
    }

    private PatientAddress mapToPatientAddressEntity(
            PatientAddressRequestDTO requestDTO,
            PatientProfile patientProfile
    ) {

        PatientAddress address = new PatientAddress();

        address.setAddressLine(requestDTO.getAddressLine());
        address.setCity(requestDTO.getCity());
        address.setState(requestDTO.getState());
        address.setPinCode(requestDTO.getPinCode());
        address.setCountry(requestDTO.getCountry());

        // IMPORTANT: relationship is set in service layer
        address.setPatientProfile(patientProfile);

        return address;
    }


    //  DOCTOR ENTITY AND DTO
    // RequestDTO → Entity
    private DoctorProfile mapToDoctorProfileEntity(DoctorProfileRequestDTO requestDTO, User user) {
        DoctorProfile doctorProfile = new DoctorProfile();
        doctorProfile.setImage(requestDTO.getImage());
        doctorProfile.setSpecialities(requestDTO.getSpecialities());
        doctorProfile.setAvailableDays(requestDTO.getAvailableDays()); // comma-separated string
        doctorProfile.setAvailable_start_time(requestDTO.getAvailableStartTime());
        doctorProfile.setAvailable_end_time(requestDTO.getAvailableEndTime());
        doctorProfile.setState_medical_council(requestDTO.getStateMedicalCouncil());
        doctorProfile.setPlace_of_exp(requestDTO.getPlaceOfExp());
        doctorProfile.setExperience_year(requestDTO.getExperienceYear());
        doctorProfile.setYear_of_graduation(requestDTO.getYearOfGraduation());
        doctorProfile.setPayment_charge(requestDTO.getPaymentCharge());
        doctorProfile.setDetails(requestDTO.getDetails());
        doctorProfile.setUser(user); // associate the User entity
        return doctorProfile;
    }

    // Entity → ResponseDTO
    private DoctorProfileResponseDTO mapToDoctorProfileResponse(DoctorProfile doctorProfile) {
        DoctorProfileResponseDTO responseDTO = new DoctorProfileResponseDTO();
        responseDTO.setId(doctorProfile.getId());
        responseDTO.setImage(doctorProfile.getImage());
        responseDTO.setSpecialities(doctorProfile.getSpecialities());
        responseDTO.setAvailableDays(doctorProfile.getAvailableDays());
        responseDTO.setAvailableStartTime(doctorProfile.getAvailable_start_time());
        responseDTO.setAvailableEndTime(doctorProfile.getAvailable_end_time());
        responseDTO.setStateMedicalCouncil(doctorProfile.getState_medical_council());
        responseDTO.setPlaceOfExp(doctorProfile.getPlace_of_exp());
        responseDTO.setExperienceYear(doctorProfile.getExperience_year());
        responseDTO.setYearOfGraduation(doctorProfile.getYear_of_graduation());
        responseDTO.setPaymentCharge(doctorProfile.getPayment_charge());
        responseDTO.setDetails(doctorProfile.getDetails());
        if (doctorProfile.getUser() != null) {
            responseDTO.setUserId(doctorProfile.getUser().getId());
        }
        return responseDTO;
    }

    // Entity → ResponseDTO  for a patientAddress
    private DoctorAddressResponseDTO mapToDoctorAddressResponse(DoctorAddress address) {

        DoctorAddressResponseDTO response = new DoctorAddressResponseDTO();

        response.setId(address.getId());
        response.setClinicName(address.getClinicName());
        response.setAddressLine(address.getAddressLine());
        response.setCity(address.getCity());
        response.setState(address.getState());
        response.setPincode(address.getPincode());
        response.setCountry(address.getCountry());
        response.setLatitude(address.getLatitude());
        response.setLongitude(address.getLongitude());
        response.setLandmark(address.getLandmark());

        if (address.getDoctorProfile() != null) {
            response.setDoctorProfileId(address.getDoctorProfile().getId());
        }

        return response;
    }

    // RequestDTO → Entity
    private DoctorAddress mapToDoctorAddressEntity(
            DoctorAddressRequestDTO requestDTO,
            DoctorProfile doctorProfile
    ) {

        DoctorAddress address = new DoctorAddress();

        address.setClinicName(requestDTO.getClinicName());
        address.setAddressLine(requestDTO.getAddressLine());
        address.setCity(requestDTO.getCity());
        address.setState(requestDTO.getState());
        address.setPincode(requestDTO.getPincode());
        address.setCountry(requestDTO.getCountry());
        address.setLatitude(requestDTO.getLatitude());
        address.setLongitude(requestDTO.getLongitude());
        address.setLandmark(requestDTO.getLandmark());

        // IMPORTANT: set relationship from service, not request
        address.setDoctorProfile(doctorProfile);

        return address;
    }



    //-----------------------------------------------------------------------------------------------------------------------------

    @Override
    public RegisterResponseDTO register(RegisterResponseDTO registerRequestDTO) {

        // Check if email already exists
        Optional<User> existingDoctor =
                userRepo.findByEmail(registerRequestDTO.getEmail());

        if (existingDoctor.isPresent()) {
            throw new RuntimeException("Email already registered!");
        }

        // DTO → Entity
        User doctor = mapToEntity(registerRequestDTO);

        // Encode password
        doctor.setPassword(passwordEncoder.encode(doctor.getPassword()));

        // Save Doctor (ID generated here)
        User savedDoctor = userRepo.save(doctor);

        // Generate OTP
        String otp = generateOtp();

        // Generate JWT
        String token = jwtUtil.generateToken(
                Map.of(
                        "id", savedDoctor.getId(),
                        "email", savedDoctor.getEmail(),
                        "role", savedDoctor.getRole().name()
                )
        );

        // Save DeviceINFO with Doctor
        DeviceINFO deviceINFO = new DeviceINFO();
        deviceINFO.setOtp(Integer.parseInt(otp));
        deviceINFO.setToken(null);
        deviceINFO.setFlag(false);
        deviceINFO.setDoctor(savedDoctor);

        deviceRepo.save(deviceINFO);

        // Send OTP email
        emailService.sendOtpEmail(
                savedDoctor.getEmail(),
                otp
        );

        // Return Response
        return mapToResponse(savedDoctor);
    }

    @Override
    public OtpResponseDTO verifyOtp(OtpRequestDTO otpRequestDTO) {

        Optional<User> userOptional =
                userRepo.findByEmail(otpRequestDTO.getEmail());

        if (userOptional.isEmpty()) {
            return new OtpResponseDTO("Email not found", null, false);
        }

        User user = userOptional.get();

        Optional<DeviceINFO> deviceINFOOptional =
                deviceRepo.findByDoctor(user);

        if (deviceINFOOptional.isEmpty()) {
            return new OtpResponseDTO("OTP not found", null, false);
        }

        DeviceINFO deviceINFO = deviceINFOOptional.get();

        if (deviceINFO.getOtp() == null ||
                deviceINFO.getOtp().intValue() != otpRequestDTO.getOtp().intValue()) {

            return new OtpResponseDTO("Invalid OTP", null, false);
        }

        deviceINFO.setFlag(true);
        deviceINFO.setOtp(null);

        String token = jwtUtil.generateToken(
                Map.of(
                        "id", user.getId(),
                        "email", user.getEmail(),
                        "role", user.getRole().name()
                )
        );

        deviceINFO.setToken(token);
        deviceRepo.save(deviceINFO);

        return new OtpResponseDTO(
                "User verified successfully",
                token,
                true
        );
    }


    // ---------- LOGOUT METHOD ----------
    @Override
    public LogoutResponseDTO logout(String token) {
        if (token == null || token.isBlank()){
            return new LogoutResponseDTO("Token is required", false);
        }

        boolean valid;
        try{
            valid = jwtUtil.validateToken(token);
        }catch (Exception e){
            valid = false;
        }

        if (!valid){
            return new LogoutResponseDTO("Invalid or expired token", false);
        }

        String email;
        try{
            email = jwtUtil.extractEmail(token);
            if (email == null){
                return new LogoutResponseDTO("Token missing email claim", false);
            }
        } catch (Exception e) {
            return  new LogoutResponseDTO("Failed to parse token", false);
        }

        Optional<User> userOptional = userRepo.findByEmail(email);
        if (userOptional.isEmpty()){
            return new LogoutResponseDTO("User not found", false);
        }

        User user = userOptional.get();

        Optional<DeviceINFO> deviceINFOOptional = deviceRepo.findByDoctor(user);
        if (deviceINFOOptional.isEmpty()){
            return new LogoutResponseDTO("DeviceInFo not found",false);
        }

        DeviceINFO deviceINFO = deviceINFOOptional.get();
        if (deviceINFO.getToken() == null || !deviceINFO.getToken().equals(token)){
            return new LogoutResponseDTO("Token does not match the user's active session", false);
        }

//        if (!passwordEncoder.matches(logoutRequestDTO.getPassword(), user.getPassword())){
//            return new LogoutResponseDTO("Incorrect password", false);
//        }
        deviceINFO.setToken(null);
        deviceRepo.save(deviceINFO);

        return new LogoutResponseDTO("Logged out successfully", true);

    }

    @Override
    public LoginResponseDTO login(LoginRequestDTO loginRequestDTO) {
        Optional<User> userOptional = userRepo.findByEmail(loginRequestDTO.getEmail());

        if (userOptional.isEmpty()){
            return new LoginResponseDTO("User does not exist", false, null);
        }

        User user = userOptional.get();

        // Check if user registered via OAuth2
        if (user.getProvider() != null && user.getPassword() == null) {
            return new LoginResponseDTO(
                    "This account was created using " + user.getProvider() +
                            ". Please use 'Login with " + user.getProvider() + "' option.",
                    false,
                    null
            );
        }

        Optional<DeviceINFO> deviceINFOOptional = deviceRepo.findByDoctor(user);


        if (deviceINFOOptional.isEmpty()){
            return new LoginResponseDTO("User DeviceInfo does not exist", false, null);
        }

        DeviceINFO deviceINFO = deviceINFOOptional.get();

        if (!deviceINFO.getFlag() == true) {
            return new LoginResponseDTO(
                    "OTP not verified. Please verify OTP first",
                    false,
                    null
            );
        }


        if (deviceINFO.getToken() != null){
            return new LoginResponseDTO("User already logged in", false, deviceINFO.getToken() );
        }
        if (!passwordEncoder.matches(loginRequestDTO.getPassword(), user.getPassword())){
            return new LoginResponseDTO("Invalid password", false, null);
        }

        // Generate JWT token
        Map<String, Object> payload = Map.of(
                "email", user.getEmail(),
                "role", user.getRole()
        );
        String token = jwtUtil.generateToken(payload);

        deviceINFO.setToken(token);
        deviceRepo.save(deviceINFO);

        return new LoginResponseDTO("Login successful", true, token);
    }

    @Override
    public PatientProfileResponseDTO createProfile(PatientProfileRequestDTO patientProfileRequestDTO, String token) {
        if (token == null || token.isBlank()) {
            throw new RuntimeException("Token is required");
        }

        // Validate JWT
        boolean valid;
        try {
            valid = jwtUtil.validateToken(token);
        } catch (Exception e) {
            valid = false;
        }

        if (!valid) {
            throw new RuntimeException("Invalid or expired token");
        }

        String email = jwtUtil.extractEmail(token);
        String role = jwtUtil.extractRole(token);

        if (!role.equalsIgnoreCase("PATIENT")) {
            throw new RuntimeException("Only patients can create a profile");
        }

        Optional<User> userOptional = userRepo.findByEmail(email);
        if (userOptional.isEmpty()) {
            throw new RuntimeException("User not found");
        }

        User user = userOptional.get();

        // Map RequestDTO → Entity using mapper method
        PatientProfile patientProfile = mapToPatientProfileEntity(patientProfileRequestDTO, user);

        // Save entity
        PatientProfile savedProfile = patientProfileRepo.save(patientProfile);

        // Map Entity → ResponseDTO
        return mapToPatientProfileResponse(savedProfile);
    }

    @Override
    public PatientAddressResponseDTO addPatientAddress(PatientAddressRequestDTO patientAddressRequestDTO, String token) {
        if (token == null || token.isBlank()) {
            throw new RuntimeException("Token is required");
        }

        boolean valid;
        try {
            valid = jwtUtil.validateToken(token);
        } catch (Exception e) {
            valid = false;
        }

        if (!valid) {
            throw new RuntimeException("Invalid or expired token");
        }

        String email = jwtUtil.extractEmail(token);
        String role = jwtUtil.extractRole(token);

        if (!"PATIENT".equalsIgnoreCase(role)) {
            throw new RuntimeException("Only patients can add address");
        }
        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        PatientProfile patientProfile = patientProfileRepo.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Patient profile not found"));

//        Map RequestDTO → Entity
        PatientAddress address = mapToPatientAddressEntity(patientAddressRequestDTO, patientProfile);

//        Save address
        PatientAddress savedAddress = patientAddressRepo.save(address);

//        Entity → ResponseDTO
        return mapToPatientAddressResponse(savedAddress);

    }

    @Override
    public DoctorProfileResponseDTO createDoctorProfile(DoctorProfileRequestDTO doctorProfileRequestDTO, String token) {
        if (token == null || token.isBlank()){
            throw new RuntimeException("Token is required");
        }

        boolean valid;
        try{
            valid = jwtUtil.validateToken(token);
        } catch (Exception e) {
            valid = false;
        }

        if (!valid) {
            throw new RuntimeException("Invalid or expired token");
        }

        String email = jwtUtil.extractEmail(token);
        String role = jwtUtil.extractRole(token);

        if (!role.equalsIgnoreCase("DOCTOR")) {
            throw new RuntimeException("Only doctors can create a profile");
        }

        Optional<User> userOptional = userRepo.findByEmail(email);
        if (userOptional.isEmpty()) {
            throw new RuntimeException("User not found");
        }

        User user = userOptional.get();

        // Map DTO → Entity
        DoctorProfile doctorProfile = mapToDoctorProfileEntity(doctorProfileRequestDTO, user);

        // Save entity
        DoctorProfile savedProfile = doctorProfileRepo.save(doctorProfile);

        // Map Entity → ResponseDTO
        return mapToDoctorProfileResponse(savedProfile);
    }

    @Override
    public DoctorAddressResponseDTO addDoctorAddress(DoctorAddressRequestDTO doctorAddressRequestDTO, String token) {
        if (token == null || token.isBlank()){
            throw new RuntimeException("Toke is required");
        }

        boolean valid;
        try{
            valid = jwtUtil.validateToken(token);
        } catch (Exception e) {
            valid = false;
        }

        if (!valid) {
            throw new RuntimeException("Invalid token");
        }


        String email = jwtUtil.extractEmail(token);
        String role = jwtUtil.extractRole(token);

        if (!"DOCTOR".equalsIgnoreCase(role)){
            throw new RuntimeException("Only DOCTOR can add address");
        }

        User user = userRepo.findByEmail(email).orElseThrow(()->new RuntimeException("User not found"));

        DoctorProfile doctorProfile = doctorProfileRepo.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Doctor profile not found"));

//      Map RequestDTO → Entity
        DoctorAddress address = mapToDoctorAddressEntity(doctorAddressRequestDTO, doctorProfile);

        // Save address
        DoctorAddress savedAddress = doctorAddressRepo.save(address);

//        Entity → ResponseDTO
        return mapToDoctorAddressResponse(savedAddress);

    }

    @Override
    public List<DoctorResponseDTO> getAllDoctors(String token) {

        String role = jwtUtil.extractRole(token);

        if (!"PATIENT".equalsIgnoreCase(role)) {
            throw new RuntimeException("Only patients can view doctors");
        }

        List<DoctorProfile> doctors = doctorProfileRepo.findAll();

        return doctors.stream().map(doctor -> {

            // Doctor Profile DTO
            DoctorProfileResponseDTO profileDTO =
                    new DoctorProfileResponseDTO(
                            doctor.getId(),
                            doctor.getImage(),
                            doctor.getSpecialities(),
                            doctor.getAvailableDays(),
                            doctor.getAvailable_start_time(),
                            doctor.getAvailable_end_time(),
                            doctor.getState_medical_council(),
                            doctor.getPlace_of_exp(),
                            doctor.getExperience_year(),
                            doctor.getYear_of_graduation(),
                            doctor.getPayment_charge(),
                            doctor.getDetails(),
                            doctor.getUser().getId()
                    );

            // User DTO
            UserResponseDTO userDTO = new UserResponseDTO();
            userDTO.setId(doctor.getUser().getId());
            userDTO.setFirstName(doctor.getUser().getFirstName());
            userDTO.setLastName(doctor.getUser().getLastName());
            userDTO.setEmail(doctor.getUser().getEmail());
            userDTO.setPhoneNumber(doctor.getUser().getPhoneNumber());

            // 🔥 Addresses using DoctorAddressRepo
            List<DoctorAddressResponseDTO> addressDTOs =
                    doctorAddressRepo.findAllByDoctorProfile(doctor)
                            .stream()
                            .map(address -> {
                                DoctorAddressResponseDTO dto = new DoctorAddressResponseDTO();
                                dto.setId(address.getId());
                                dto.setClinicName(address.getClinicName());
                                dto.setAddressLine(address.getAddressLine());
                                dto.setCity(address.getCity());
                                dto.setState(address.getState());
                                dto.setCountry(address.getCountry());
                                dto.setPincode(address.getPincode());
                                dto.setLatitude(address.getLatitude());
                                dto.setLongitude(address.getLongitude());
                                dto.setLandmark(address.getLandmark());
                                dto.setDoctorProfileId(doctor.getId());
                                return dto;
                            })
                            .toList();

            DoctorResponseDTO response = new DoctorResponseDTO();
            response.setDoctorProfile(profileDTO);
            response.setUser(userDTO);
            response.setAddresses(addressDTOs);

            return response;

        }).toList();
    }


    @Override
    public DoctorProfile findDoctorById(Long doctorProfileId, String token) {
        return userRepo.findById(Math.toIntExact(doctorProfileId)).orElseThrow(() ->
                new RuntimeException("Doctor not found with id : " + doctorProfileId)
        ).getDoctorProfile();
    }

    @Override
    public Appointment addAppoinment(String token, Appointment appointment) {

        if (token == null || token.isBlank()) {
            throw new RuntimeException("Token is required");
        }

        boolean valid;
        try {
            valid = jwtUtil.validateToken(token);
        } catch (Exception e) {
            valid = false;
        }

        if (!valid) {
            throw new RuntimeException("Invalid token");
        }

        String email = jwtUtil.extractEmail(token);
        String role = jwtUtil.extractRole(token);

        // mistake tha yaha
        if (!"PATIENT".equalsIgnoreCase(role)) {
            throw new RuntimeException("Only PATIENT can add appointment");
        }

        if (appointment.getDoctorProfile() == null
                || appointment.getDoctorProfile().getId() == null) {
            throw new RuntimeException("Doctor profile id is required");
        }

        // User fetch
        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Patient profile fetch
        PatientProfile patientProfile = patientProfileRepo.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Patient profile not found"));

        // Doctor profile must come from request
//        Long doctorProfileId = appointment.getDoctorProfile().getId();

        DoctorProfile doctorProfile = doctorProfileRepo
                .findById(appointment.getDoctorProfile().getId())
                .orElseThrow(() -> new RuntimeException("Doctor profile not found"));

        // Check appointment date/time is in the future
        LocalDate appointmentDate = appointment.getAppointmentDate();
        LocalTime appointmentTime = appointment.getAppointmentTime();
        if (appointmentDate.isBefore(LocalDate.now()) ||
                (appointmentDate.isEqual(LocalDate.now()) && appointmentTime.isBefore(LocalTime.now()))) {
            throw new RuntimeException("Appointment must be in the future");
        }

        // Check if doctor is available on that day
        DayOfWeek dayOfWeek = appointmentDate.getDayOfWeek();
        Set<String> availableDays = Set.of(doctorProfile.getAvailableDays().split(","));
        if (!availableDays.contains(dayOfWeek.name())) {
            throw new RuntimeException("Doctor is not available on " + dayOfWeek);
        }

        // Check if appointment time is within doctor's available time
        if (appointmentTime.isBefore(doctorProfile.getAvailable_start_time()) ||
                appointmentTime.isAfter(doctorProfile.getAvailable_end_time())) {
            throw new RuntimeException("Doctor is not available at this time");
        }

        boolean slotTaken = appointmentRepository
                .existsByDoctorProfileAndAppointmentDateAndAppointmentTime(
                        doctorProfile, appointmentDate, appointmentTime
                );

        if (slotTaken) {
            throw new RuntimeException("This time slot is already booked. Please choose another time.");
        }


        // New appointment
        Appointment newAppointment = new Appointment();
        newAppointment.setAppointmentDate(appointment.getAppointmentDate());
        newAppointment.setAppointmentTime(appointment.getAppointmentTime());
        newAppointment.setDoctorProfile(doctorProfile);
        newAppointment.setPatientProfile(patientProfile);

        return appointmentRepository.save(newAppointment);
    }

    @Override
    public List<AppointmentResponseDTO> getAllAppointment(String token) {

        if (token == null || token.isBlank()) {
            throw new RuntimeException("Token is required");
        }

        boolean valid;
        try {
            valid = jwtUtil.validateToken(token);
        } catch (Exception e) {
            valid = false;
        }

        if (!valid) {
            throw new RuntimeException("Invalid token");
        }

        String email = jwtUtil.extractEmail(token);
        String role = jwtUtil.extractRole(token);

        if (!"DOCTOR".equalsIgnoreCase(role)) {
            throw new RuntimeException("Only DOCTOR can view appointments");
        }

        // Get doctor user and profile
        User doctorUser = userRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Doctor user not found"));
        DoctorProfile doctorProfile = doctorProfileRepo.findByUser(doctorUser)
                .orElseThrow(() -> new RuntimeException("Doctor profile not found"));

        // Get appointments
        List<Appointment> appointments = appointmentRepository.findByDoctorProfile(doctorProfile);

        // Map to DTO
        List<AppointmentResponseDTO> appointmentDTOs = appointments.stream().map(app -> {
            AppointmentResponseDTO dto = new AppointmentResponseDTO();
            dto.setId(app.getId());
            dto.setAppointmentDate(app.getAppointmentDate());
            dto.setAppointmentTime(app.getAppointmentTime());

            // PatientProfile
            PatientProfile patientProfile = app.getPatientProfile();
            PatientProfileResponseDTO patientProfileDTO = new PatientProfileResponseDTO();
            patientProfileDTO.setId(patientProfile.getId());
            patientProfileDTO.setPhoto(patientProfile.getPhoto());
            patientProfileDTO.setDateOfBirth(patientProfile.getDateOfBirth());
            patientProfileDTO.setBloodGroup(patientProfile.getBloodGroup());
            patientProfileDTO.setGender(patientProfile.getGender());
            if (patientProfile.getUser() != null) {
                patientProfileDTO.setUserId(Math.toIntExact(patientProfile.getUser().getId()));
            } else {
                User patientUser = userRepo.findByPatientProfile_Id(Long.valueOf(patientProfile.getId())).orElse(null);
                if (patientUser != null) {
                    patientProfileDTO.setUserId(Math.toIntExact(patientUser.getId()));
                }
            }
            dto.setPatientProfile(patientProfileDTO);

            // PatientAddress (take first address as example, can be extended to list)
            if (patientProfile.getAddresses() != null && !patientProfile.getAddresses().isEmpty()) {
                PatientAddressResponseDTO addressDTO = new PatientAddressResponseDTO();
                addressDTO.setId(patientProfile.getAddresses().get(0).getId());
                addressDTO.setAddressLine(patientProfile.getAddresses().get(0).getAddressLine());
                addressDTO.setCity(patientProfile.getAddresses().get(0).getCity());
                addressDTO.setState(patientProfile.getAddresses().get(0).getState());
                addressDTO.setPinCode(patientProfile.getAddresses().get(0).getPinCode());
                addressDTO.setCountry(patientProfile.getAddresses().get(0).getCountry());
                dto.setPatientAddressResponseDTO(addressDTO);
            }

            // User — only required fields manually
            User patientUser = patientProfile.getUser();
            if (patientUser == null) {
                patientUser = userRepo.findByPatientProfile_Id(Long.valueOf(patientProfile.getId())).orElse(null);
            }
            if (patientUser != null) {
                User limitedUser = new User();
                limitedUser.setFirstName(patientUser.getFirstName());
                limitedUser.setLastName(patientUser.getLastName());
                limitedUser.setPhoneNumber(patientUser.getPhoneNumber());
                limitedUser.setEmail(patientUser.getEmail());
                dto.setUser(limitedUser);
            }

            return dto;
        }).toList();

        return appointmentDTOs;
    }

    @Override
    public Appointment getAppointmentById(Long appointmentId, String token) {
        if (token == null || token.isBlank()) {
            throw new RuntimeException("Token is required");
        }

        boolean valid;
        try {
            valid = jwtUtil.validateToken(token);
        } catch (Exception e) {
            valid = false;
        }

        if (!valid) {
            throw new RuntimeException("Invalid token");
        }

        String email = jwtUtil.extractEmail(token);
        String role = jwtUtil.extractRole(token);

        if (!"DOCTOR".equalsIgnoreCase(role)) {
            throw new RuntimeException("Only doctors can view their appointments");
        }

        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new RuntimeException("Appointment not found"));

        // Corrected check: compare with User email
        if (!appointment.getDoctorProfile().getUser().getEmail().equalsIgnoreCase(email)) {
            throw new RuntimeException("You are not authorized to view this appointment");
        }
        appointment.setDoctorProfile(null);

        return appointment;
    }

    @Override
    public Appointment deleteAppointment(Long appointmentId, String token) {
        if (token == null || token.isBlank()) {
            throw new RuntimeException("Token is required");
        }

        boolean valid;
        try {
            valid = jwtUtil.validateToken(token);
        } catch (Exception e) {
            valid = false;
        }

        if (!valid) {
            throw new RuntimeException("Invalid token");
        }

        String email = jwtUtil.extractEmail(token);
        String role = jwtUtil.extractRole(token);

        if (!"DOCTOR".equalsIgnoreCase(role)) {
            throw new RuntimeException("Only doctors can delete their appointments");
        }

        // Fetch the appointment
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new RuntimeException("Appointment not found"));

        // Ensure the appointment belongs to the logged-in doctor
        if (!appointment.getDoctorProfile().getUser().getEmail().equalsIgnoreCase(email)) {
            throw new RuntimeException("You are not authorized to delete this appointment");
        }

        // Delete the appointment
        appointmentRepository.delete(appointment);

        appointment.setDoctorProfile(null); // hide doctor info
        return appointment;
    }

    @Override
    public UserResponseDTO getDetails(String token) {
        if (token == null || token.isBlank()) {
            throw new RuntimeException("Token is required");
        }

        if (!jwtUtil.validateToken(token)) {
            throw new RuntimeException("Invalid or expired token");
        }

        String email = jwtUtil.extractEmail(token);
        String role = jwtUtil.extractRole(token);

        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User Not Found"));




        UserResponseDTO responseDTO = new UserResponseDTO();
        responseDTO.setId(user.getId());
        responseDTO.setFirstName(user.getFirstName());
        responseDTO.setLastName(user.getLastName());
        responseDTO.setEmail(user.getEmail());
        responseDTO.setPhoneNumber(user.getPhoneNumber());
        responseDTO.setRole(user.getRole().name());

        // Include doctor profile if exists
        if (user.getDoctorProfile() != null) {
            responseDTO.setDoctorProfile(mapToDoctorProfileResponse(user.getDoctorProfile()));

            // Include doctor address if exists
            DoctorAddress doctorAddress = doctorAddressRepo.findByDoctorProfile(user.getDoctorProfile()).orElse(null);
            if (doctorAddress != null) {
                responseDTO.setDoctorAddressResponseDTO(mapToDoctorAddressResponse(doctorAddress));
            }
        }

        // Include patient profile if exists
        if (user.getPatientProfile() != null) {
            responseDTO.setPatientProfile(mapToPatientProfileResponse(user.getPatientProfile()));

            // Include patient address if exists
            PatientAddress patientAddress = patientAddressRepo.findByPatientProfile(user.getPatientProfile()).orElse(null);
            if (patientAddress != null) {
                responseDTO.setPatientAddressResponseDTO(mapToPatientAddressResponse(patientAddress));
            }
        }

        return responseDTO;
    }

}
