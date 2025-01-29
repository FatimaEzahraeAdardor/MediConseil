package org.youcode.mediconseil.web.vm.mapper;

import org.mapstruct.Mapper;
import org.youcode.mediconseil.domain.City;
import org.youcode.mediconseil.web.vm.request.CityRequestVM;
import org.youcode.mediconseil.web.vm.response.CityResponseVM;

@Mapper(componentModel = "spring")

public interface CityMapper {
        CityResponseVM toVM(City city);
        City toEntity(CityRequestVM cityRequestVM);
}
