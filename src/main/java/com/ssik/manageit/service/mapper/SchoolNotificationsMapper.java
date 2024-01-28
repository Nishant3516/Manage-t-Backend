package com.ssik.manageit.service.mapper;

import com.ssik.manageit.domain.*;
import com.ssik.manageit.service.dto.SchoolNotificationsDTO;
import java.util.Set;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link SchoolNotifications} and its DTO {@link SchoolNotificationsDTO}.
 */
@Mapper(componentModel = "spring", uses = { SchoolClassMapper.class })
public interface SchoolNotificationsMapper extends EntityMapper<SchoolNotificationsDTO, SchoolNotifications> {
    @Mapping(target = "schoolClasses", source = "schoolClasses", qualifiedByName = "classNameSet")
    SchoolNotificationsDTO toDto(SchoolNotifications s);

    @Mapping(target = "removeSchoolClass", ignore = true)
    SchoolNotifications toEntity(SchoolNotificationsDTO schoolNotificationsDTO);
}
