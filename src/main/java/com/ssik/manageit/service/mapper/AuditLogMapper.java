package com.ssik.manageit.service.mapper;

import com.ssik.manageit.domain.AuditLog;
import com.ssik.manageit.service.dto.AuditLogDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link AuditLog} and its DTO {@link AuditLogDTO}.
 */
@Mapper(componentModel = "spring", uses = { SchoolMapper.class, SchoolUserMapper.class })
public interface AuditLogMapper extends EntityMapper<AuditLogDTO, AuditLog> {
    @Mapping(target = "school", source = "school", qualifiedByName = "id")
    @Mapping(target = "schoolUser", source = "schoolUser", qualifiedByName = "id")
    AuditLogDTO toDto(AuditLog s);
}
