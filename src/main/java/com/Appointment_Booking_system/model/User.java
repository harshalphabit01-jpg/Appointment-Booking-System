package com.Appointment_Booking_system.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;

import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = true)
    private String lastName;

    @Column(nullable = true, unique = true)
    private String phoneNumber;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = true)
    private String password;

    private String provider;
    private String providerId;

    @Enumerated(EnumType.STRING)
    private RoleStatus role;

    // One Doctor -> Many DeviceINFO
    @OneToMany(
            mappedBy = "doctor",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    @JsonIgnore
    private List<DeviceINFO> deviceInfos;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
//    @JsonIgnore
    @JsonManagedReference
    private PatientProfile patientProfile;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
//    @JsonIgnore
    @JsonManagedReference
    private DoctorProfile doctorProfile;

    // Helper method to check authentication type
    public boolean isOAuth2User() {
        return provider != null && providerId != null;
    }

    public boolean isManualUser() {
        return password != null && (provider == null || provider.isEmpty());
    }

}
