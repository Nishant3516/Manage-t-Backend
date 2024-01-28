package com.ssik.manageit.service.mapper;

import com.ssik.manageit.domain.*;
import com.ssik.manageit.service.dto.SchoolVideoGalleryDTO;
import java.util.Set;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link SchoolVideoGallery} and its DTO {@link SchoolVideoGalleryDTO}.
 */
@Mapper(componentModel = "spring", uses = { SchoolClassMapper.class })
public interface SchoolVideoGalleryMapper extends EntityMapper<SchoolVideoGalleryDTO, SchoolVideoGallery> {
    @Mapping(target = "schoolClasses", source = "schoolClasses", qualifiedByName = "classNameSet")
    SchoolVideoGalleryDTO toDto(SchoolVideoGallery s);

    @Mapping(target = "removeSchoolClass", ignore = true)
    SchoolVideoGallery toEntity(SchoolVideoGalleryDTO schoolVideoGalleryDTO);
}
