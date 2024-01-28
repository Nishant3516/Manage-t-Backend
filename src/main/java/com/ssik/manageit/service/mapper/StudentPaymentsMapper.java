package com.ssik.manageit.service.mapper;

import com.ssik.manageit.domain.*;
import com.ssik.manageit.service.dto.StudentPaymentsDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link StudentPayments} and its DTO {@link StudentPaymentsDTO}.
 */
@Mapper(componentModel = "spring", uses = { ClassStudentMapper.class })
public interface StudentPaymentsMapper extends EntityMapper<StudentPaymentsDTO, StudentPayments> {
    @Mapping(target = "classStudent", source = "classStudent", qualifiedByName = "id")
    StudentPaymentsDTO toDto(StudentPayments s);
}
