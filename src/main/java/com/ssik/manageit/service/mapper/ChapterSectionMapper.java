package com.ssik.manageit.service.mapper;

import com.ssik.manageit.domain.*;
import com.ssik.manageit.service.dto.ChapterSectionDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ChapterSection} and its DTO {@link ChapterSectionDTO}.
 */
@Mapper(componentModel = "spring", uses = { SubjectChapterMapper.class })
public interface ChapterSectionMapper extends EntityMapper<ChapterSectionDTO, ChapterSection> {
    @Mapping(target = "subjectChapter", source = "subjectChapter", qualifiedByName = "id")
    ChapterSectionDTO toDto(ChapterSection s);

    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ChapterSectionDTO toDtoId(ChapterSection chapterSection);
}
