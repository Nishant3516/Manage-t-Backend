package com.ssik.manageit.service.mapper;

import com.ssik.manageit.domain.*;
import com.ssik.manageit.service.dto.ClassHomeWorkDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ClassHomeWork} and its DTO {@link ClassHomeWorkDTO}.
 */
@Mapper(componentModel = "spring", uses = { ChapterSectionMapper.class })
public interface ClassHomeWorkMapper extends EntityMapper<ClassHomeWorkDTO, ClassHomeWork> {
    @Mapping(target = "chapterSection", source = "chapterSection", qualifiedByName = "id")
    ClassHomeWorkDTO toDto(ClassHomeWork s);

    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ClassHomeWorkDTO toDtoId(ClassHomeWork classHomeWork);
}
