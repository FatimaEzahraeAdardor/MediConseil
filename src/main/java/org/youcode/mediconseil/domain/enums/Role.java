package org.youcode.mediconseil.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.youcode.mediconseil.domain.enums.Permission.*;

@RequiredArgsConstructor
public enum Role {
    PATIENT(
            Set.of(
                    USER_BOOK_APPOINTMENT,
                    USER_VIEW_APPOINTMENT_DETAILS,
                    USER_CANCEL_APPOINTMENT,
                    USER_RESCHEDULE_APPOINTMENT,
                    USER_VIEW_DOCTOR_PROFILES,
                    USER_RECEIVE_CONSULTATION_DOCUMENT,
                    USER_READ_HEALTH_ARTICLES,
                    USER_MANAGE_PERSONAL_INFO,
                    USER_VIEW_NOTIFICATIONS,
                    USER_VIEW_CONSULTATION_HISTORY
            )
    ),

    DOCTOR(
            Set.of(
                    DOCTOR_VIEW_APPOINTMENTS,
                    DOCTOR_CONFIRM_APPOINTMENT,
                    DOCTOR_CANCEL_APPOINTMENT,
                    DOCTOR_RESCHEDULE_APPOINTMENT,
                    DOCTOR_VIEW_PATIENT_DETAILS,
                    DOCTOR_PROVIDE_CONSULTATION_DOCUMENT,
                    ADMIN_MANAGE_HEALTH_ARTICLES,
                    DOCTOR_MANAGE_AVAILABILITY,
                    DOCTOR_VIEW_SCHEDULE,
                    DOCTOR_MANAGE_PROFILE,
                    DOCTOR_VIEW_STATISTICS
            )
    ),

    ADMIN(
            Set.of(
                    ADMIN_MANAGE_APPOINTMENTS,
                    ADMIN_VIEW_APPOINTMENT_DETAILS,
                    ADMIN_CANCEL_APPOINTMENT,
                    ADMIN_RESCHEDULE_APPOINTMENT,
                    ADMIN_VIEW_DOCTOR_SCHEDULES,
                    ADMIN_MANAGE_DOCTOR_AVAILABILITY,
                    ADMIN_MANAGE_HEALTH_ARTICLES,
                    ADMIN_PUBLISH_HEALTH_ARTICLES,
                    ADMIN_MANAGE_USERS,
                    ADMIN_MANAGE_DOCTORS,
                    ADMIN_VIEW_STATISTICS,
                    ADMIN_MANAGE_NOTIFICATIONS
            )
    );

    @Getter
    private final Set<Permission> permissions;

    public List<SimpleGrantedAuthority> getAuthorities() {
        var authorities = getPermissions().stream()
                .map(permission -> new SimpleGrantedAuthority(permission.name()))
                .collect(Collectors.toList());
        authorities.add(new SimpleGrantedAuthority("ROLE_" + name()));
        return authorities;
    }

}