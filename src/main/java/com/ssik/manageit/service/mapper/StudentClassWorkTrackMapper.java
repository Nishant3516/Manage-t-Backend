package com.ssik.manageit.service.mapper;

import com.ssik.manageit.domain.*;
import com.ssik.manageit.service.dto.StudentClassWorkTrackDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link StudentClassWorkTrack} and its DTO {@link StudentClassWorkTrackDTO}.
 */
@Mapper(componentModel = "spring", uses = { ClassStudentMapper.class, ClassClassWorkMapper.class })
public interface StudentClassWorkTrackMapper extends EntityMapper<StudentClassWorkTrackDTO, StudentClassWorkTrack> {
    @Mapping(target = "classStudent", source = "classStudent", qualifiedByName = "id")
    @Mapping(target = "classClassWork", source = "classClassWork", qualifiedByName = "id")
    StudentClassWorkTrackDTO toDto(StudentClassWorkTrack s);
}
