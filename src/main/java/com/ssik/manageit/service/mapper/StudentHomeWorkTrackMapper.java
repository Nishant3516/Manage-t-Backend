package com.ssik.manageit.service.mapper;

import com.ssik.manageit.domain.*;
import com.ssik.manageit.service.dto.StudentHomeWorkTrackDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link StudentHomeWorkTrack} and its DTO {@link StudentHomeWorkTrackDTO}.
 */
@Mapper(componentModel = "spring", uses = { ClassStudentMapper.class, ClassHomeWorkMapper.class })
public interface StudentHomeWorkTrackMapper extends EntityMapper<StudentHomeWorkTrackDTO, StudentHomeWorkTrack> {
    @Mapping(target = "classStudent", source = "classStudent", qualifiedByName = "id")
    @Mapping(target = "classHomeWork", source = "classHomeWork", qualifiedByName = "id")
    StudentHomeWorkTrackDTO toDto(StudentHomeWorkTrack s);
}
