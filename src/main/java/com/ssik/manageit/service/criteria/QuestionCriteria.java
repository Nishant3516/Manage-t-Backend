package com.ssik.manageit.service.criteria;

import com.ssik.manageit.domain.enumeration.Difficulty;
import com.ssik.manageit.domain.enumeration.Status;
import java.io.Serializable;
import java.util.Objects;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.BooleanFilter;
import tech.jhipster.service.filter.DoubleFilter;
import tech.jhipster.service.filter.Filter;
import tech.jhipster.service.filter.FloatFilter;
import tech.jhipster.service.filter.IntegerFilter;
import tech.jhipster.service.filter.LocalDateFilter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link com.ssik.manageit.domain.Question} entity. This class is used
 * in {@link com.ssik.manageit.web.rest.QuestionResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /questions?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class QuestionCriteria implements Serializable, Criteria {

    /**
     * Class for filtering Status
     */
    public static class StatusFilter extends Filter<Status> {

        public StatusFilter() {}

        public StatusFilter(StatusFilter filter) {
            super(filter);
        }

        @Override
        public StatusFilter copy() {
            return new StatusFilter(this);
        }
    }

    /**
     * Class for filtering Difficulty
     */
    public static class DifficultyFilter extends Filter<Difficulty> {

        public DifficultyFilter() {}

        public DifficultyFilter(DifficultyFilter filter) {
            super(filter);
        }

        @Override
        public DifficultyFilter copy() {
            return new DifficultyFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private BooleanFilter imageSideBySide;

    private StringFilter option1;

    private StringFilter option2;

    private StringFilter option3;

    private StringFilter option4;

    private StringFilter option5;

    private StatusFilter status;

    private IntegerFilter weightage;

    private DifficultyFilter difficultyLevel;

    private LocalDateFilter createDate;

    private LocalDateFilter lastModified;

    private LocalDateFilter cancelDate;

    private LongFilter tagId;

    private LongFilter questionTypeId;

    private LongFilter tenantId;

    private LongFilter schoolClassId;

    private LongFilter classSubjectId;

    private LongFilter subjectChapterId;

    private LongFilter questionPaperId;

    private Boolean distinct;

    public QuestionCriteria() {}

    public QuestionCriteria(QuestionCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.imageSideBySide = other.imageSideBySide == null ? null : other.imageSideBySide.copy();
        this.option1 = other.option1 == null ? null : other.option1.copy();
        this.option2 = other.option2 == null ? null : other.option2.copy();
        this.option3 = other.option3 == null ? null : other.option3.copy();
        this.option4 = other.option4 == null ? null : other.option4.copy();
        this.option5 = other.option5 == null ? null : other.option5.copy();
        this.status = other.status == null ? null : other.status.copy();
        this.weightage = other.weightage == null ? null : other.weightage.copy();
        this.difficultyLevel = other.difficultyLevel == null ? null : other.difficultyLevel.copy();
        this.createDate = other.createDate == null ? null : other.createDate.copy();
        this.lastModified = other.lastModified == null ? null : other.lastModified.copy();
        this.cancelDate = other.cancelDate == null ? null : other.cancelDate.copy();
        this.tagId = other.tagId == null ? null : other.tagId.copy();
        this.questionTypeId = other.questionTypeId == null ? null : other.questionTypeId.copy();
        this.tenantId = other.tenantId == null ? null : other.tenantId.copy();
        this.schoolClassId = other.schoolClassId == null ? null : other.schoolClassId.copy();
        this.classSubjectId = other.classSubjectId == null ? null : other.classSubjectId.copy();
        this.subjectChapterId = other.subjectChapterId == null ? null : other.subjectChapterId.copy();
        this.questionPaperId = other.questionPaperId == null ? null : other.questionPaperId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public QuestionCriteria copy() {
        return new QuestionCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public LongFilter id() {
        if (id == null) {
            id = new LongFilter();
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public BooleanFilter getImageSideBySide() {
        return imageSideBySide;
    }

    public BooleanFilter imageSideBySide() {
        if (imageSideBySide == null) {
            imageSideBySide = new BooleanFilter();
        }
        return imageSideBySide;
    }

    public void setImageSideBySide(BooleanFilter imageSideBySide) {
        this.imageSideBySide = imageSideBySide;
    }

    public StringFilter getOption1() {
        return option1;
    }

    public StringFilter option1() {
        if (option1 == null) {
            option1 = new StringFilter();
        }
        return option1;
    }

    public void setOption1(StringFilter option1) {
        this.option1 = option1;
    }

    public StringFilter getOption2() {
        return option2;
    }

    public StringFilter option2() {
        if (option2 == null) {
            option2 = new StringFilter();
        }
        return option2;
    }

    public void setOption2(StringFilter option2) {
        this.option2 = option2;
    }

    public StringFilter getOption3() {
        return option3;
    }

    public StringFilter option3() {
        if (option3 == null) {
            option3 = new StringFilter();
        }
        return option3;
    }

    public void setOption3(StringFilter option3) {
        this.option3 = option3;
    }

    public StringFilter getOption4() {
        return option4;
    }

    public StringFilter option4() {
        if (option4 == null) {
            option4 = new StringFilter();
        }
        return option4;
    }

    public void setOption4(StringFilter option4) {
        this.option4 = option4;
    }

    public StringFilter getOption5() {
        return option5;
    }

    public StringFilter option5() {
        if (option5 == null) {
            option5 = new StringFilter();
        }
        return option5;
    }

    public void setOption5(StringFilter option5) {
        this.option5 = option5;
    }

    public StatusFilter getStatus() {
        return status;
    }

    public StatusFilter status() {
        if (status == null) {
            status = new StatusFilter();
        }
        return status;
    }

    public void setStatus(StatusFilter status) {
        this.status = status;
    }

    public IntegerFilter getWeightage() {
        return weightage;
    }

    public IntegerFilter weightage() {
        if (weightage == null) {
            weightage = new IntegerFilter();
        }
        return weightage;
    }

    public void setWeightage(IntegerFilter weightage) {
        this.weightage = weightage;
    }

    public DifficultyFilter getDifficultyLevel() {
        return difficultyLevel;
    }

    public DifficultyFilter difficultyLevel() {
        if (difficultyLevel == null) {
            difficultyLevel = new DifficultyFilter();
        }
        return difficultyLevel;
    }

    public void setDifficultyLevel(DifficultyFilter difficultyLevel) {
        this.difficultyLevel = difficultyLevel;
    }

    public LocalDateFilter getCreateDate() {
        return createDate;
    }

    public LocalDateFilter createDate() {
        if (createDate == null) {
            createDate = new LocalDateFilter();
        }
        return createDate;
    }

    public void setCreateDate(LocalDateFilter createDate) {
        this.createDate = createDate;
    }

    public LocalDateFilter getLastModified() {
        return lastModified;
    }

    public LocalDateFilter lastModified() {
        if (lastModified == null) {
            lastModified = new LocalDateFilter();
        }
        return lastModified;
    }

    public void setLastModified(LocalDateFilter lastModified) {
        this.lastModified = lastModified;
    }

    public LocalDateFilter getCancelDate() {
        return cancelDate;
    }

    public LocalDateFilter cancelDate() {
        if (cancelDate == null) {
            cancelDate = new LocalDateFilter();
        }
        return cancelDate;
    }

    public void setCancelDate(LocalDateFilter cancelDate) {
        this.cancelDate = cancelDate;
    }

    public LongFilter getTagId() {
        return tagId;
    }

    public LongFilter tagId() {
        if (tagId == null) {
            tagId = new LongFilter();
        }
        return tagId;
    }

    public void setTagId(LongFilter tagId) {
        this.tagId = tagId;
    }

    public LongFilter getQuestionTypeId() {
        return questionTypeId;
    }

    public LongFilter questionTypeId() {
        if (questionTypeId == null) {
            questionTypeId = new LongFilter();
        }
        return questionTypeId;
    }

    public void setQuestionTypeId(LongFilter questionTypeId) {
        this.questionTypeId = questionTypeId;
    }

    public LongFilter getTenantId() {
        return tenantId;
    }

    public LongFilter tenantId() {
        if (tenantId == null) {
            tenantId = new LongFilter();
        }
        return tenantId;
    }

    public void setTenantId(LongFilter tenantId) {
        this.tenantId = tenantId;
    }

    public LongFilter getSchoolClassId() {
        return schoolClassId;
    }

    public LongFilter schoolClassId() {
        if (schoolClassId == null) {
            schoolClassId = new LongFilter();
        }
        return schoolClassId;
    }

    public void setSchoolClassId(LongFilter schoolClassId) {
        this.schoolClassId = schoolClassId;
    }

    public LongFilter getClassSubjectId() {
        return classSubjectId;
    }

    public LongFilter classSubjectId() {
        if (classSubjectId == null) {
            classSubjectId = new LongFilter();
        }
        return classSubjectId;
    }

    public void setClassSubjectId(LongFilter classSubjectId) {
        this.classSubjectId = classSubjectId;
    }

    public LongFilter getSubjectChapterId() {
        return subjectChapterId;
    }

    public LongFilter subjectChapterId() {
        if (subjectChapterId == null) {
            subjectChapterId = new LongFilter();
        }
        return subjectChapterId;
    }

    public void setSubjectChapterId(LongFilter subjectChapterId) {
        this.subjectChapterId = subjectChapterId;
    }

    public LongFilter getQuestionPaperId() {
        return questionPaperId;
    }

    public LongFilter questionPaperId() {
        if (questionPaperId == null) {
            questionPaperId = new LongFilter();
        }
        return questionPaperId;
    }

    public void setQuestionPaperId(LongFilter questionPaperId) {
        this.questionPaperId = questionPaperId;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final QuestionCriteria that = (QuestionCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(imageSideBySide, that.imageSideBySide) &&
            Objects.equals(option1, that.option1) &&
            Objects.equals(option2, that.option2) &&
            Objects.equals(option3, that.option3) &&
            Objects.equals(option4, that.option4) &&
            Objects.equals(option5, that.option5) &&
            Objects.equals(status, that.status) &&
            Objects.equals(weightage, that.weightage) &&
            Objects.equals(difficultyLevel, that.difficultyLevel) &&
            Objects.equals(createDate, that.createDate) &&
            Objects.equals(lastModified, that.lastModified) &&
            Objects.equals(cancelDate, that.cancelDate) &&
            Objects.equals(tagId, that.tagId) &&
            Objects.equals(questionTypeId, that.questionTypeId) &&
            Objects.equals(tenantId, that.tenantId) &&
            Objects.equals(schoolClassId, that.schoolClassId) &&
            Objects.equals(classSubjectId, that.classSubjectId) &&
            Objects.equals(subjectChapterId, that.subjectChapterId) &&
            Objects.equals(questionPaperId, that.questionPaperId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            imageSideBySide,
            option1,
            option2,
            option3,
            option4,
            option5,
            status,
            weightage,
            difficultyLevel,
            createDate,
            lastModified,
            cancelDate,
            tagId,
            questionTypeId,
            tenantId,
            schoolClassId,
            classSubjectId,
            subjectChapterId,
            questionPaperId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "QuestionCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (imageSideBySide != null ? "imageSideBySide=" + imageSideBySide + ", " : "") +
            (option1 != null ? "option1=" + option1 + ", " : "") +
            (option2 != null ? "option2=" + option2 + ", " : "") +
            (option3 != null ? "option3=" + option3 + ", " : "") +
            (option4 != null ? "option4=" + option4 + ", " : "") +
            (option5 != null ? "option5=" + option5 + ", " : "") +
            (status != null ? "status=" + status + ", " : "") +
            (weightage != null ? "weightage=" + weightage + ", " : "") +
            (difficultyLevel != null ? "difficultyLevel=" + difficultyLevel + ", " : "") +
            (createDate != null ? "createDate=" + createDate + ", " : "") +
            (lastModified != null ? "lastModified=" + lastModified + ", " : "") +
            (cancelDate != null ? "cancelDate=" + cancelDate + ", " : "") +
            (tagId != null ? "tagId=" + tagId + ", " : "") +
            (questionTypeId != null ? "questionTypeId=" + questionTypeId + ", " : "") +
            (tenantId != null ? "tenantId=" + tenantId + ", " : "") +
            (schoolClassId != null ? "schoolClassId=" + schoolClassId + ", " : "") +
            (classSubjectId != null ? "classSubjectId=" + classSubjectId + ", " : "") +
            (subjectChapterId != null ? "subjectChapterId=" + subjectChapterId + ", " : "") +
            (questionPaperId != null ? "questionPaperId=" + questionPaperId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
