package org.youcode.mediconseil.web.vm.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;
import org.youcode.mediconseil.domain.Availability;
import org.youcode.mediconseil.domain.Doctor;
import org.youcode.mediconseil.repository.DoctorRepository;
import org.youcode.mediconseil.web.vm.request.AvailabilityRequestVm;
import org.youcode.mediconseil.web.vm.response.AvailabilityResponseVm;

import java.util.UUID;

@Mapper(componentModel = "spring")
public abstract class AvailabilityMapper {
    @Autowired
    protected DoctorRepository doctorRepository;

    @Mapping(target = "doctor", source = "doctorId", qualifiedByName = "toDoctorById")
    @Mapping(target = "isBooked", constant = "false")
    public abstract Availability toEntity(AvailabilityRequestVm request);

    @Named("toDoctorById")
    protected Doctor toDoctorById(UUID doctorId) {
        return doctorRepository.findById(doctorId)
                .orElseThrow(() -> new RuntimeException("Doctor not found with ID: " + doctorId));
    }
    public abstract AvailabilityResponseVm toVm(Availability availability);

}