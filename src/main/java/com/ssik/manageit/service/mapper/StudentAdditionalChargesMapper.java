package com.ssik.manageit.service.mapper;

import com.ssik.manageit.domain.*;
import com.ssik.manageit.service.dto.StudentAdditionalChargesDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link StudentAdditionalCharges} and its DTO {@link StudentAdditionalChargesDTO}.
 */
@Mapper(componentModel = "spring", uses = { SchoolLedgerHeadMapper.class, ClassStudentMapper.class })
public interface StudentAdditionalChargesMapper extends EntityMapper<StudentAdditionalChargesDTO, StudentAdditionalCharges> {
    @Mapping(target = "schoolLedgerHead", source = "schoolLedgerHead", qualifiedByName = "id")
    @Mapping(target = "classStudent", source = "classStudent", qualifiedByName = "id")
    StudentAdditionalChargesDTO toDto(StudentAdditionalCharges s);
}
