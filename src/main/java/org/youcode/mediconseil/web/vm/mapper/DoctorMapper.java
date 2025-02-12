package org.youcode.mediconseil.web.vm.mapper;

import org.mapstruct.Mapper;
import org.youcode.mediconseil.domain.City;
import org.youcode.mediconseil.domain.Doctor;
import org.youcode.mediconseil.domain.Speciality;
import org.youcode.mediconseil.domain.enums.Role;
import org.youcode.mediconseil.web.vm.request.DoctorRequestVm;

@Mapper(componentModel = "spring")

public interface DoctorMapper {

        default Doctor toEntity(DoctorRequestVm doctorRequestVm) {
                if (doctorRequestVm == null) {
                        return null;
                }

                // Create a new Doctor instance
                Doctor doctor = new Doctor();

                // Map User (Superclass) fields
                doctor.setUserName(doctorRequestVm.getUserName());
                doctor.setPassword(doctorRequestVm.getPassword());
                doctor.setFirstName(doctorRequestVm.getFirstName());
                doctor.setLastName(doctorRequestVm.getLastName());
                doctor.setEmail(doctorRequestVm.getEmail());
                doctor.setPhoneNumber(doctorRequestVm.getPhoneNumber());
                doctor.setRole(Role.DOCTOR); // Assuming Role is an ENUM

                // Map Doctor-specific fields
                doctor.setAddress(doctorRequestVm.getAddress());
                doctor.setExperiences(doctorRequestVm.getExperiences());
                doctor.setDiploma(doctorRequestVm.getDiploma());
                doctor.setDescription(doctorRequestVm.getDescription());
                doctor.setPrice(doctorRequestVm.getPrice());
                 if (doctorRequestVm.getCityId() != null) {
                         City city = new City();
                         city.setId(doctorRequestVm.getCityId());
                         doctor.setCity(city);
                 }

                // Specialty Mapping (if Specialty is an entity)
                if (doctorRequestVm.getSpecialtyId() != null) {
                        Speciality specialty = new Speciality();
                        specialty.setId(doctorRequestVm.getSpecialtyId());
                        doctor.setSpeciality(specialty);
                }

                return doctor;
        }
}
