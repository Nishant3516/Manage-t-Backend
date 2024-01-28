package com.ssik.manageit.service.mapper;

import com.ssik.manageit.domain.*;
import com.ssik.manageit.service.dto.ClassSubjectDTO;
import java.util.Set;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ClassSubject} and its DTO {@link ClassSubjectDTO}.
 */
@Mapper(componentModel = "spring", uses = { SchoolClassMapper.class })
public interface ClassSubjectMapper extends EntityMapper<ClassSubjectDTO, ClassSubject> {
    @Mapping(target = "schoolClasses", source = "schoolClasses", qualifiedByName = "classNameSet")
    ClassSubjectDTO toDto(ClassSubject s);

    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ClassSubjectDTO toDtoId(ClassSubject classSubject);

    @Mapping(target = "removeSchoolClass", ignore = true)
    ClassSubject toEntity(ClassSubjectDTO classSubjectDTO);

    @Named("subjectNameSet")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "subjectName", source = "subjectName")
    Set<ClassSubjectDTO> toDtoSubjectNameSet(Set<ClassSubject> classSubject);
}
