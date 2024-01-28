package com.ssik.manageit.service.mapper;

import com.ssik.manageit.domain.*;
import com.ssik.manageit.service.dto.SchoolPictureGalleryDTO;
import java.util.Set;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link SchoolPictureGallery} and its DTO {@link SchoolPictureGalleryDTO}.
 */
@Mapper(componentModel = "spring", uses = { SchoolClassMapper.class })
public interface SchoolPictureGalleryMapper extends EntityMapper<SchoolPictureGalleryDTO, SchoolPictureGallery> {
    @Mapping(target = "schoolClasses", source = "schoolClasses", qualifiedByName = "classNameSet")
    SchoolPictureGalleryDTO toDto(SchoolPictureGallery s);

    @Mapping(target = "removeSchoolClass", ignore = true)
    SchoolPictureGallery toEntity(SchoolPictureGalleryDTO schoolPictureGalleryDTO);
}
