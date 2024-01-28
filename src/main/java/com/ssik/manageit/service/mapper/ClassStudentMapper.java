package com.ssik.manageit.service.mapper;

import com.ssik.manageit.domain.*;
import com.ssik.manageit.service.dto.ClassStudentDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ClassStudent} and its DTO {@link ClassStudentDTO}.
 */
@Mapper(componentModel = "spring", uses = { SchoolClassMapper.class })
public interface ClassStudentMapper extends EntityMapper<ClassStudentDTO, ClassStudent> {
    @Mapping(target = "schoolClass", source = "schoolClass", qualifiedByName = "id")
    ClassStudentDTO toDto(ClassStudent s);

    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ClassStudentDTO toDtoId(ClassStudent classStudent);
}
