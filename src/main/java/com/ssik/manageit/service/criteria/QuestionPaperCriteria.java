package com.ssik.manageit.service.criteria;

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
 * Criteria class for the {@link com.ssik.manageit.domain.QuestionPaper} entity. This class is used
 * in {@link com.ssik.manageit.web.rest.QuestionPaperResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /question-papers?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class QuestionPaperCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter questionPaperName;

    private StringFilter mainTitle;

    private StringFilter subTitle;

    private StringFilter leftSubHeading1;

    private StringFilter leftSubHeading2;

    private StringFilter rightSubHeading1;

    private StringFilter rightSubHeading2;

    private StringFilter instructions;

    private StringFilter footerText;

    private IntegerFilter totalMarks;

    private LocalDateFilter createDate;

    private LocalDateFilter lastModified;

    private LocalDateFilter cancelDate;

    private LongFilter questionId;

    private LongFilter tagId;

    private LongFilter tenantId;

    private LongFilter schoolClassId;

    private LongFilter classSubjectId;

    private Boolean distinct;

    public QuestionPaperCriteria() {}

    public QuestionPaperCriteria(QuestionPaperCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.questionPaperName = other.questionPaperName == null ? null : other.questionPaperName.copy();
        this.mainTitle = other.mainTitle == null ? null : other.mainTitle.copy();
        this.subTitle = other.subTitle == null ? null : other.subTitle.copy();
        this.leftSubHeading1 = other.leftSubHeading1 == null ? null : other.leftSubHeading1.copy();
        this.leftSubHeading2 = other.leftSubHeading2 == null ? null : other.leftSubHeading2.copy();
        this.rightSubHeading1 = other.rightSubHeading1 == null ? null : other.rightSubHeading1.copy();
        this.rightSubHeading2 = other.rightSubHeading2 == null ? null : other.rightSubHeading2.copy();
        this.instructions = other.instructions == null ? null : other.instructions.copy();
        this.footerText = other.footerText == null ? null : other.footerText.copy();
        this.totalMarks = other.totalMarks == null ? null : other.totalMarks.copy();
        this.createDate = other.createDate == null ? null : other.createDate.copy();
        this.lastModified = other.lastModified == null ? null : other.lastModified.copy();
        this.cancelDate = other.cancelDate == null ? null : other.cancelDate.copy();
        this.questionId = other.questionId == null ? null : other.questionId.copy();
        this.tagId = other.tagId == null ? null : other.tagId.copy();
        this.tenantId = other.tenantId == null ? null : other.tenantId.copy();
        this.schoolClassId = other.schoolClassId == null ? null : other.schoolClassId.copy();
        this.classSubjectId = other.classSubjectId == null ? null : other.classSubjectId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public QuestionPaperCriteria copy() {
        return new QuestionPaperCriteria(this);
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

    public StringFilter getQuestionPaperName() {
        return questionPaperName;
    }

    public StringFilter questionPaperName() {
        if (questionPaperName == null) {
            questionPaperName = new StringFilter();
        }
        return questionPaperName;
    }

    public void setQuestionPaperName(StringFilter questionPaperName) {
        this.questionPaperName = questionPaperName;
    }

    public StringFilter getMainTitle() {
        return mainTitle;
    }

    public StringFilter mainTitle() {
        if (mainTitle == null) {
            mainTitle = new StringFilter();
        }
        return mainTitle;
    }

    public void setMainTitle(StringFilter mainTitle) {
        this.mainTitle = mainTitle;
    }

    public StringFilter getSubTitle() {
        return subTitle;
    }

    public StringFilter subTitle() {
        if (subTitle == null) {
            subTitle = new StringFilter();
        }
        return subTitle;
    }

    public void setSubTitle(StringFilter subTitle) {
        this.subTitle = subTitle;
    }

    public StringFilter getLeftSubHeading1() {
        return leftSubHeading1;
    }

    public StringFilter leftSubHeading1() {
        if (leftSubHeading1 == null) {
            leftSubHeading1 = new StringFilter();
        }
        return leftSubHeading1;
    }

    public void setLeftSubHeading1(StringFilter leftSubHeading1) {
        this.leftSubHeading1 = leftSubHeading1;
    }

    public StringFilter getLeftSubHeading2() {
        return leftSubHeading2;
    }

    public StringFilter leftSubHeading2() {
        if (leftSubHeading2 == null) {
            leftSubHeading2 = new StringFilter();
        }
        return leftSubHeading2;
    }

    public void setLeftSubHeading2(StringFilter leftSubHeading2) {
        this.leftSubHeading2 = leftSubHeading2;
    }

    public StringFilter getRightSubHeading1() {
        return rightSubHeading1;
    }

    public StringFilter rightSubHeading1() {
        if (rightSubHeading1 == null) {
            rightSubHeading1 = new StringFilter();
        }
        return rightSubHeading1;
    }

    public void setRightSubHeading1(StringFilter rightSubHeading1) {
        this.rightSubHeading1 = rightSubHeading1;
    }

    public StringFilter getRightSubHeading2() {
        return rightSubHeading2;
    }

    public StringFilter rightSubHeading2() {
        if (rightSubHeading2 == null) {
            rightSubHeading2 = new StringFilter();
        }
        return rightSubHeading2;
    }

    public void setRightSubHeading2(StringFilter rightSubHeading2) {
        this.rightSubHeading2 = rightSubHeading2;
    }

    public StringFilter getInstructions() {
        return instructions;
    }

    public StringFilter instructions() {
        if (instructions == null) {
            instructions = new StringFilter();
        }
        return instructions;
    }

    public void setInstructions(StringFilter instructions) {
        this.instructions = instructions;
    }

    public StringFilter getFooterText() {
        return footerText;
    }

    public StringFilter footerText() {
        if (footerText == null) {
            footerText = new StringFilter();
        }
        return footerText;
    }

    public void setFooterText(StringFilter footerText) {
        this.footerText = footerText;
    }

    public IntegerFilter getTotalMarks() {
        return totalMarks;
    }

    public IntegerFilter totalMarks() {
        if (totalMarks == null) {
            totalMarks = new IntegerFilter();
        }
        return totalMarks;
    }

    public void setTotalMarks(IntegerFilter totalMarks) {
        this.totalMarks = totalMarks;
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

    public LongFilter getQuestionId() {
        return questionId;
    }

    public LongFilter questionId() {
        if (questionId == null) {
            questionId = new LongFilter();
        }
        return questionId;
    }

    public void setQuestionId(LongFilter questionId) {
        this.questionId = questionId;
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
        final QuestionPaperCriteria that = (QuestionPaperCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(questionPaperName, that.questionPaperName) &&
            Objects.equals(mainTitle, that.mainTitle) &&
            Objects.equals(subTitle, that.subTitle) &&
            Objects.equals(leftSubHeading1, that.leftSubHeading1) &&
            Objects.equals(leftSubHeading2, that.leftSubHeading2) &&
            Objects.equals(rightSubHeading1, that.rightSubHeading1) &&
            Objects.equals(rightSubHeading2, that.rightSubHeading2) &&
            Objects.equals(instructions, that.instructions) &&
            Objects.equals(footerText, that.footerText) &&
            Objects.equals(totalMarks, that.totalMarks) &&
            Objects.equals(createDate, that.createDate) &&
            Objects.equals(lastModified, that.lastModified) &&
            Objects.equals(cancelDate, that.cancelDate) &&
            Objects.equals(questionId, that.questionId) &&
            Objects.equals(tagId, that.tagId) &&
            Objects.equals(tenantId, that.tenantId) &&
            Objects.equals(schoolClassId, that.schoolClassId) &&
            Objects.equals(classSubjectId, that.classSubjectId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            questionPaperName,
            mainTitle,
            subTitle,
            leftSubHeading1,
            leftSubHeading2,
            rightSubHeading1,
            rightSubHeading2,
            instructions,
            footerText,
            totalMarks,
            createDate,
            lastModified,
            cancelDate,
            questionId,
            tagId,
            tenantId,
            schoolClassId,
            classSubjectId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "QuestionPaperCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (questionPaperName != null ? "questionPaperName=" + questionPaperName + ", " : "") +
            (mainTitle != null ? "mainTitle=" + mainTitle + ", " : "") +
            (subTitle != null ? "subTitle=" + subTitle + ", " : "") +
            (leftSubHeading1 != null ? "leftSubHeading1=" + leftSubHeading1 + ", " : "") +
            (leftSubHeading2 != null ? "leftSubHeading2=" + leftSubHeading2 + ", " : "") +
            (rightSubHeading1 != null ? "rightSubHeading1=" + rightSubHeading1 + ", " : "") +
            (rightSubHeading2 != null ? "rightSubHeading2=" + rightSubHeading2 + ", " : "") +
            (instructions != null ? "instructions=" + instructions + ", " : "") +
            (footerText != null ? "footerText=" + footerText + ", " : "") +
            (totalMarks != null ? "totalMarks=" + totalMarks + ", " : "") +
            (createDate != null ? "createDate=" + createDate + ", " : "") +
            (lastModified != null ? "lastModified=" + lastModified + ", " : "") +
            (cancelDate != null ? "cancelDate=" + cancelDate + ", " : "") +
            (questionId != null ? "questionId=" + questionId + ", " : "") +
            (tagId != null ? "tagId=" + tagId + ", " : "") +
            (tenantId != null ? "tenantId=" + tenantId + ", " : "") +
            (schoolClassId != null ? "schoolClassId=" + schoolClassId + ", " : "") +
            (classSubjectId != null ? "classSubjectId=" + classSubjectId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
