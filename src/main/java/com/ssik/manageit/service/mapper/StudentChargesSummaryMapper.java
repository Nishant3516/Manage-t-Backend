package com.ssik.manageit.service.mapper;

import com.ssik.manageit.domain.*;
import com.ssik.manageit.service.dto.StudentChargesSummaryDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link StudentChargesSummary} and its DTO {@link StudentChargesSummaryDTO}.
 */
@Mapper(componentModel = "spring", uses = { SchoolLedgerHeadMapper.class, ClassStudentMapper.class })
public interface StudentChargesSummaryMapper extends EntityMapper<StudentChargesSummaryDTO, StudentChargesSummary> {
    @Mapping(target = "schoolLedgerHead", source = "schoolLedgerHead", qualifiedByName = "id")
    @Mapping(target = "classStudent", source = "classStudent", qualifiedByName = "id")
    StudentChargesSummaryDTO toDto(StudentChargesSummary s);
}
