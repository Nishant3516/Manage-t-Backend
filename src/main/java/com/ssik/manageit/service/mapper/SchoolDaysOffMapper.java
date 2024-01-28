package com.ssik.manageit.service.mapper;

import com.ssik.manageit.domain.*;
import com.ssik.manageit.service.dto.SchoolDaysOffDTO;
import java.util.Set;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link SchoolDaysOff} and its DTO {@link SchoolDaysOffDTO}.
 */
@Mapper(componentModel = "spring", uses = { SchoolClassMapper.class })
public interface SchoolDaysOffMapper extends EntityMapper<SchoolDaysOffDTO, SchoolDaysOff> {
    @Mapping(target = "schoolClasses", source = "schoolClasses", qualifiedByName = "classNameSet")
    SchoolDaysOffDTO toDto(SchoolDaysOff s);

    @Mapping(target = "removeSchoolClass", ignore = true)
    SchoolDaysOff toEntity(SchoolDaysOffDTO schoolDaysOffDTO);
}
