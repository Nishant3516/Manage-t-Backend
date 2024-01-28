package com.ssik.manageit.service.mapper;

import com.ssik.manageit.domain.*;
import com.ssik.manageit.service.dto.SchoolUserDTO;
import java.util.Set;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link SchoolUser} and its DTO {@link SchoolUserDTO}.
 */
@Mapper(componentModel = "spring", uses = { SchoolClassMapper.class, ClassSubjectMapper.class })
public interface SchoolUserMapper extends EntityMapper<SchoolUserDTO, SchoolUser> {
    @Mapping(target = "schoolClasses", source = "schoolClasses", qualifiedByName = "classNameSet")
    @Mapping(target = "classSubjects", source = "classSubjects", qualifiedByName = "subjectNameSet")
    SchoolUserDTO toDto(SchoolUser s);

    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    SchoolUserDTO toDtoId(SchoolUser schoolUser);

    @Mapping(target = "removeSchoolClass", ignore = true)
    @Mapping(target = "removeClassSubject", ignore = true)
    SchoolUser toEntity(SchoolUserDTO schoolUserDTO);
}
