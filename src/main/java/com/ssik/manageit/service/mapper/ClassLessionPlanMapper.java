package com.ssik.manageit.service.mapper;

import com.ssik.manageit.domain.*;
import com.ssik.manageit.service.dto.ClassLessionPlanDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ClassLessionPlan} and its DTO {@link ClassLessionPlanDTO}.
 */
@Mapper(
    componentModel = "spring",
    uses = { ChapterSectionMapper.class, SchoolClassMapper.class, ClassSubjectMapper.class, SubjectChapterMapper.class }
)
public interface ClassLessionPlanMapper extends EntityMapper<ClassLessionPlanDTO, ClassLessionPlan> {
    @Mapping(target = "chapterSection", source = "chapterSection", qualifiedByName = "id")
    @Mapping(target = "schoolClass", source = "schoolClass", qualifiedByName = "id")
    @Mapping(target = "classSubject", source = "classSubject", qualifiedByName = "id")
    @Mapping(target = "subjectChapter", source = "subjectChapter", qualifiedByName = "id")
    ClassLessionPlanDTO toDto(ClassLessionPlan s);

    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ClassLessionPlanDTO toDtoId(ClassLessionPlan classLessionPlan);
}
