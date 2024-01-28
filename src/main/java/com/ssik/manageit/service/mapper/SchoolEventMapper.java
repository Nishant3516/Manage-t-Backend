package com.ssik.manageit.service.mapper;

import com.ssik.manageit.domain.*;
import com.ssik.manageit.service.dto.SchoolEventDTO;
import java.util.Set;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link SchoolEvent} and its DTO {@link SchoolEventDTO}.
 */
@Mapper(componentModel = "spring", uses = { SchoolClassMapper.class })
public interface SchoolEventMapper extends EntityMapper<SchoolEventDTO, SchoolEvent> {
    @Mapping(target = "schoolClasses", source = "schoolClasses", qualifiedByName = "classNameSet")
    SchoolEventDTO toDto(SchoolEvent s);

    @Mapping(target = "removeSchoolClass", ignore = true)
    SchoolEvent toEntity(SchoolEventDTO schoolEventDTO);
}
