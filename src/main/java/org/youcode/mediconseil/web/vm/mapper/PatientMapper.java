package org.youcode.mediconseil.web.vm.mapper;

import org.mapstruct.Mapper;
import org.youcode.mediconseil.domain.User;
import org.youcode.mediconseil.web.vm.response.PatientResponseVm;

@Mapper(componentModel = "spring")

public interface PatientMapper {
        PatientResponseVm toVM(User user);
}
