package com.ssik.manageit.service.mapper;

import com.ssik.manageit.domain.*;
import com.ssik.manageit.service.dto.ClassClassWorkDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ClassClassWork} and its DTO {@link ClassClassWorkDTO}.
 */
@Mapper(componentModel = "spring", uses = { ChapterSectionMapper.class })
public interface ClassClassWorkMapper extends EntityMapper<ClassClassWorkDTO, ClassClassWork> {
    @Mapping(target = "chapterSection", source = "chapterSection", qualifiedByName = "id")
    ClassClassWorkDTO toDto(ClassClassWork s);

    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ClassClassWorkDTO toDtoId(ClassClassWork classClassWork);
}
