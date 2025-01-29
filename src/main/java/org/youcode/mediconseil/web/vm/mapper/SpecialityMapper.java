package org.youcode.mediconseil.web.vm.mapper;

import org.mapstruct.Mapper;
import org.youcode.mediconseil.domain.Speciality;
import org.youcode.mediconseil.web.vm.request.SpecialityRequest;
import org.youcode.mediconseil.web.vm.response.SpecialityResponseVm;

@Mapper(componentModel = "spring")

public interface SpecialityMapper {
        SpecialityResponseVm toVM(Speciality speciality);
        Speciality toEntity(SpecialityRequest specialityRequest);
}
