package com.ssik.manageit.service.mapper;

import com.ssik.manageit.domain.*;
import com.ssik.manageit.service.dto.StudentAttendenceDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link StudentAttendence} and its DTO {@link StudentAttendenceDTO}.
 */
@Mapper(componentModel = "spring", uses = { ClassStudentMapper.class })
public interface StudentAttendenceMapper extends EntityMapper<StudentAttendenceDTO, StudentAttendence> {
    @Mapping(target = "classStudent", source = "classStudent", qualifiedByName = "id")
    StudentAttendenceDTO toDto(StudentAttendence s);
}
