package com.ssik.manageit.service.mapper;

import com.ssik.manageit.domain.*;
import com.ssik.manageit.service.dto.SchoolClassDTO;
import java.util.Set;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link SchoolClass} and its DTO {@link SchoolClassDTO}.
 */
@Mapper(componentModel = "spring", uses = { SchoolMapper.class })
public interface SchoolClassMapper extends EntityMapper<SchoolClassDTO, SchoolClass> {
    @Mapping(target = "school", source = "school", qualifiedByName = "id")
    SchoolClassDTO toDto(SchoolClass s);

    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    SchoolClassDTO toDtoId(SchoolClass schoolClass);

    @Named("classNameSet")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "className", source = "className")
    Set<SchoolClassDTO> toDtoClassNameSet(Set<SchoolClass> schoolClass);
}
