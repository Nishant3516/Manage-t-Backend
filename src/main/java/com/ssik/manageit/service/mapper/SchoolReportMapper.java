package com.ssik.manageit.service.mapper;

import com.ssik.manageit.domain.*;
import com.ssik.manageit.service.dto.SchoolReportDTO;
import java.util.Set;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link SchoolReport} and its DTO {@link SchoolReportDTO}.
 */
@Mapper(componentModel = "spring", uses = { SchoolClassMapper.class })
public interface SchoolReportMapper extends EntityMapper<SchoolReportDTO, SchoolReport> {
    @Mapping(target = "schoolClasses", source = "schoolClasses", qualifiedByName = "classNameSet")
    SchoolReportDTO toDto(SchoolReport s);

    @Mapping(target = "removeSchoolClass", ignore = true)
    SchoolReport toEntity(SchoolReportDTO schoolReportDTO);
}
