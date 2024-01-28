package com.ssik.manageit.service.mapper;

import com.ssik.manageit.domain.*;
import com.ssik.manageit.service.dto.ClassFeeDTO;
import java.util.Set;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ClassFee} and its DTO {@link ClassFeeDTO}.
 */
@Mapper(componentModel = "spring", uses = { SchoolClassMapper.class, SchoolLedgerHeadMapper.class })
public interface ClassFeeMapper extends EntityMapper<ClassFeeDTO, ClassFee> {
    @Mapping(target = "schoolClasses", source = "schoolClasses", qualifiedByName = "classNameSet")
    @Mapping(target = "schoolLedgerHead", source = "schoolLedgerHead", qualifiedByName = "id")
    ClassFeeDTO toDto(ClassFee s);

    @Mapping(target = "removeSchoolClass", ignore = true)
    ClassFee toEntity(ClassFeeDTO classFeeDTO);
}
