package org.youcode.mediconseil.web.vm.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.youcode.mediconseil.domain.Article;
import org.youcode.mediconseil.domain.Doctor;
import org.youcode.mediconseil.web.vm.request.ArticleRequestVm;
import org.youcode.mediconseil.web.vm.response.ArticleResponseVm;

import java.util.UUID;

@Mapper(componentModel = "spring", uses = {CategoryMapper.class})
public interface ArticleMapper {
    @Mapping(target = "category.id", source = "categoryId")
    @Mapping(target = "doctor", expression = "java(mapDoctor(request.getDoctorId()))")
    Article toEntity(ArticleRequestVm request);

    ArticleResponseVm toVm(Article article);

    // Add this method to explicitly map doctor
    default Doctor mapDoctor(UUID doctorId) {
        if (doctorId == null) {
            return null;
        }
        Doctor doctor = new Doctor();
        doctor.setId(doctorId);
        return doctor;
    }
}