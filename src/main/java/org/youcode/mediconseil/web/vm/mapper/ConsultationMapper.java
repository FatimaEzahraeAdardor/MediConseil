package org.youcode.mediconseil.web.vm.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;
import org.youcode.mediconseil.domain.Consultation;
import org.youcode.mediconseil.domain.Doctor;
import org.youcode.mediconseil.domain.User;
import org.youcode.mediconseil.domain.enums.ConsultationStatus;
import org.youcode.mediconseil.repository.DoctorRepository;
import org.youcode.mediconseil.repository.UserRepository;
import org.youcode.mediconseil.service.AvailabilityService;
import org.youcode.mediconseil.web.exception.ResourceNotFoundException;
import org.youcode.mediconseil.web.vm.request.ConsultationRequestVm;
import org.youcode.mediconseil.web.vm.response.ConsultationResponseVm;

import java.util.UUID;

@Mapper(componentModel = "spring", imports = {ConsultationStatus.class})
public abstract class ConsultationMapper {
    @Autowired
    protected UserRepository userRepository;

    @Autowired
    protected DoctorRepository doctorRepository;

    @Autowired
    protected AvailabilityService availabilityService;

    @Mapping(target = "doctor", source = "doctorId", qualifiedByName = "toDoctorById")
    @Mapping(target = "patient", source = "patientId", qualifiedByName = "toUserById")
    @Mapping(target = "dateConsultation", source = "dateConsultation")
    @Mapping(target = "status", expression = "java(ConsultationStatus.PENDING)")
    @Mapping(target = "id", ignore = true)
    public abstract Consultation toEntity(ConsultationRequestVm request);


    public abstract ConsultationResponseVm toResponseVm(Consultation consultation);

    @Named("toUserById")
    protected User toUserById(UUID userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found with ID: " + userId));
    }

    @Named("toDoctorById")
    protected Doctor toDoctorById(UUID doctorId) {
        return doctorRepository.findById(doctorId)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found with ID: " + doctorId));
    }
}