package com.ssik.manageit.service.mapper;

import com.ssik.manageit.domain.*;
import com.ssik.manageit.service.dto.IdStoreDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link IdStore} and its DTO {@link IdStoreDTO}.
 */
@Mapper(componentModel = "spring", uses = { SchoolMapper.class })
public interface IdStoreMapper extends EntityMapper<IdStoreDTO, IdStore> {
    @Mapping(target = "school", source = "school", qualifiedByName = "id")
    IdStoreDTO toDto(IdStore s);
}
