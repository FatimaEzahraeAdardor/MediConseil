package org.youcode.mediconseil.web.vm.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;
import org.youcode.mediconseil.domain.Consultation;
import org.youcode.mediconseil.domain.Doctor;
import org.youcode.mediconseil.domain.User;
import org.youcode.mediconseil.repository.DoctorRepository;
import org.youcode.mediconseil.repository.UserRepository;
import org.youcode.mediconseil.web.vm.request.ConsultationRequestVm;
import org.youcode.mediconseil.web.vm.response.ConsultationResponseVm;

import java.util.UUID;

@Mapper(componentModel = "spring")
public abstract class ConsultationMapper {
    @Autowired
    protected UserRepository userRepository;

    @Autowired
    protected DoctorRepository doctorRepository;

    @Mapping(target = "doctor", source = "doctorId", qualifiedByName = "toDoctorById")
    @Mapping(target = "patient", source = "patientId", qualifiedByName = "toUserById")
    public abstract Consultation toEntity(ConsultationRequestVm request);

    @Named("toUserById")
    protected User toUserById(UUID userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Patient not found with ID: " + userId));
    }

    @Named("toDoctorById")
    protected Doctor toDoctorById(UUID doctorId) {
        return doctorRepository.findById(doctorId)
                .orElseThrow(() -> new RuntimeException("Doctor not found with ID: " + doctorId));
    }

    @Mapping(target = "doctorId", source = "doctor.id")
    @Mapping(target = "patientId", source = "patient.id")
    public abstract ConsultationResponseVm toVm(Consultation consultation);
}