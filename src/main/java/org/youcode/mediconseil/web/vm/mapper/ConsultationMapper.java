package org.youcode.mediconseil.web.vm.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.youcode.mediconseil.domain.Consultation;
import org.youcode.mediconseil.domain.Doctor;
import org.youcode.mediconseil.domain.User;
import org.youcode.mediconseil.web.vm.request.ConsultationRequestVm;

@Mapper(componentModel = "spring")
public interface ConsultationMapper {

    @Mapping(target = "doctor", expression = "java(mapDoctor(request.getDoctorId()))")
    @Mapping(target = "patient", expression = "java(mapUser(request.getUserId()))")
    Consultation toEntity(ConsultationRequestVm request);

    default Doctor mapDoctor(java.util.UUID doctorId) {
        if (doctorId == null) {
            return null;
        }
        Doctor doctor = new Doctor();
        doctor.setId(doctorId);
        return doctor;
    }

    default User mapUser(java.util.UUID userId) {
        if (userId == null) {
            return null;
        }
        User user = new User();
        user.setId(userId);
        return user;
    }
}
