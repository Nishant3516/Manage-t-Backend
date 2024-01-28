package com.ssik.manageit.service.mapper;

import com.ssik.manageit.domain.*;
import com.ssik.manageit.service.dto.SubjectChapterDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link SubjectChapter} and its DTO {@link SubjectChapterDTO}.
 */
@Mapper(componentModel = "spring", uses = { ClassSubjectMapper.class })
public interface SubjectChapterMapper extends EntityMapper<SubjectChapterDTO, SubjectChapter> {
    @Mapping(target = "classSubject", source = "classSubject", qualifiedByName = "id")
    SubjectChapterDTO toDto(SubjectChapter s);

    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    SubjectChapterDTO toDtoId(SubjectChapter subjectChapter);
}
