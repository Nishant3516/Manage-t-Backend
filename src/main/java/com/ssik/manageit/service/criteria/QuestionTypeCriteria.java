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
 * Criteria class for the {@link com.ssik.manageit.domain.QuestionType} entity. This class is used
 * in {@link com.ssik.manageit.web.rest.QuestionTypeResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /question-types?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class QuestionTypeCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter questionType;

    private IntegerFilter marks;

    private LocalDateFilter createDate;

    private LocalDateFilter lastModified;

    private LocalDateFilter cancelDate;

    private LongFilter questionId;

    private LongFilter tenantId;

    private Boolean distinct;

    public QuestionTypeCriteria() {}

    public QuestionTypeCriteria(QuestionTypeCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.questionType = other.questionType == null ? null : other.questionType.copy();
        this.marks = other.marks == null ? null : other.marks.copy();
        this.createDate = other.createDate == null ? null : other.createDate.copy();
        this.lastModified = other.lastModified == null ? null : other.lastModified.copy();
        this.cancelDate = other.cancelDate == null ? null : other.cancelDate.copy();
        this.questionId = other.questionId == null ? null : other.questionId.copy();
        this.tenantId = other.tenantId == null ? null : other.tenantId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public QuestionTypeCriteria copy() {
        return new QuestionTypeCriteria(this);
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

    public StringFilter getQuestionType() {
        return questionType;
    }

    public StringFilter questionType() {
        if (questionType == null) {
            questionType = new StringFilter();
        }
        return questionType;
    }

    public void setQuestionType(StringFilter questionType) {
        this.questionType = questionType;
    }

    public IntegerFilter getMarks() {
        return marks;
    }

    public IntegerFilter marks() {
        if (marks == null) {
            marks = new IntegerFilter();
        }
        return marks;
    }

    public void setMarks(IntegerFilter marks) {
        this.marks = marks;
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
        final QuestionTypeCriteria that = (QuestionTypeCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(questionType, that.questionType) &&
            Objects.equals(marks, that.marks) &&
            Objects.equals(createDate, that.createDate) &&
            Objects.equals(lastModified, that.lastModified) &&
            Objects.equals(cancelDate, that.cancelDate) &&
            Objects.equals(questionId, that.questionId) &&
            Objects.equals(tenantId, that.tenantId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, questionType, marks, createDate, lastModified, cancelDate, questionId, tenantId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "QuestionTypeCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (questionType != null ? "questionType=" + questionType + ", " : "") +
            (marks != null ? "marks=" + marks + ", " : "") +
            (createDate != null ? "createDate=" + createDate + ", " : "") +
            (lastModified != null ? "lastModified=" + lastModified + ", " : "") +
            (cancelDate != null ? "cancelDate=" + cancelDate + ", " : "") +
            (questionId != null ? "questionId=" + questionId + ", " : "") +
            (tenantId != null ? "tenantId=" + tenantId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
