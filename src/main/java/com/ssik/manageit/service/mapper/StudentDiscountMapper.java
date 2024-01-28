package com.ssik.manageit.service.mapper;

import com.ssik.manageit.domain.*;
import com.ssik.manageit.service.dto.StudentDiscountDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link StudentDiscount} and its DTO {@link StudentDiscountDTO}.
 */
@Mapper(componentModel = "spring", uses = { SchoolLedgerHeadMapper.class, ClassStudentMapper.class })
public interface StudentDiscountMapper extends EntityMapper<StudentDiscountDTO, StudentDiscount> {
    @Mapping(target = "schoolLedgerHead", source = "schoolLedgerHead", qualifiedByName = "id")
    @Mapping(target = "classStudent", source = "classStudent", qualifiedByName = "id")
    StudentDiscountDTO toDto(StudentDiscount s);
}
