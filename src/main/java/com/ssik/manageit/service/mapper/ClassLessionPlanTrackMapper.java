package com.ssik.manageit.service.mapper;

import com.ssik.manageit.domain.*;
import com.ssik.manageit.service.dto.ClassLessionPlanTrackDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ClassLessionPlanTrack} and its DTO {@link ClassLessionPlanTrackDTO}.
 */
@Mapper(componentModel = "spring", uses = { ClassLessionPlanMapper.class })
public interface ClassLessionPlanTrackMapper extends EntityMapper<ClassLessionPlanTrackDTO, ClassLessionPlanTrack> {
    @Mapping(target = "classLessionPlan", source = "classLessionPlan", qualifiedByName = "id")
    ClassLessionPlanTrackDTO toDto(ClassLessionPlanTrack s);
}
