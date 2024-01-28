package com.ssik.manageit.service.mapper;

import com.ssik.manageit.domain.*;
import com.ssik.manageit.service.dto.SchoolLedgerHeadDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link SchoolLedgerHead} and its DTO
 * {@link SchoolLedgerHeadDTO}.
 */
@Mapper(componentModel = "spring", uses = { SchoolMapper.class })
public interface SchoolLedgerHeadMapper extends EntityMapper<SchoolLedgerHeadDTO, SchoolLedgerHead> {
	@Override
	@Mapping(target = "school", source = "school", qualifiedByName = "id")
	SchoolLedgerHeadDTO toDto(SchoolLedgerHead s);

	@Named("id")
	@BeanMapping(ignoreByDefault = true)
	@Mapping(target = "id", source = "id")
	SchoolLedgerHeadDTO toDtoId(SchoolLedgerHead schoolLedgerHead);
}
