package com.ssik.manageit.service.criteria;

import com.ssik.manageit.domain.enumeration.TagLevel;
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
 * Criteria class for the {@link com.ssik.manageit.domain.Tag} entity. This class is used
 * in {@link com.ssik.manageit.web.rest.TagResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /tags?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class TagCriteria implements Serializable, Criteria {

    /**
     * Class for filtering TagLevel
     */
    public static class TagLevelFilter extends Filter<TagLevel> {

        public TagLevelFilter() {}

        public TagLevelFilter(TagLevelFilter filter) {
            super(filter);
        }

        @Override
        public TagLevelFilter copy() {
            return new TagLevelFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter tagText;

    private TagLevelFilter tagLevel;

    private LocalDateFilter createDate;

    private LocalDateFilter lastModified;

    private LocalDateFilter cancelDate;

    private LongFilter tenantId;

    private LongFilter questionPaperId;

    private LongFilter questionId;

    private Boolean distinct;

    public TagCriteria() {}

    public TagCriteria(TagCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.tagText = other.tagText == null ? null : other.tagText.copy();
        this.tagLevel = other.tagLevel == null ? null : other.tagLevel.copy();
        this.createDate = other.createDate == null ? null : other.createDate.copy();
        this.lastModified = other.lastModified == null ? null : other.lastModified.copy();
        this.cancelDate = other.cancelDate == null ? null : other.cancelDate.copy();
        this.tenantId = other.tenantId == null ? null : other.tenantId.copy();
        this.questionPaperId = other.questionPaperId == null ? null : other.questionPaperId.copy();
        this.questionId = other.questionId == null ? null : other.questionId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public TagCriteria copy() {
        return new TagCriteria(this);
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

    public StringFilter getTagText() {
        return tagText;
    }

    public StringFilter tagText() {
        if (tagText == null) {
            tagText = new StringFilter();
        }
        return tagText;
    }

    public void setTagText(StringFilter tagText) {
        this.tagText = tagText;
    }

    public TagLevelFilter getTagLevel() {
        return tagLevel;
    }

    public TagLevelFilter tagLevel() {
        if (tagLevel == null) {
            tagLevel = new TagLevelFilter();
        }
        return tagLevel;
    }

    public void setTagLevel(TagLevelFilter tagLevel) {
        this.tagLevel = tagLevel;
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
        final TagCriteria that = (TagCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(tagText, that.tagText) &&
            Objects.equals(tagLevel, that.tagLevel) &&
            Objects.equals(createDate, that.createDate) &&
            Objects.equals(lastModified, that.lastModified) &&
            Objects.equals(cancelDate, that.cancelDate) &&
            Objects.equals(tenantId, that.tenantId) &&
            Objects.equals(questionPaperId, that.questionPaperId) &&
            Objects.equals(questionId, that.questionId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, tagText, tagLevel, createDate, lastModified, cancelDate, tenantId, questionPaperId, questionId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TagCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (tagText != null ? "tagText=" + tagText + ", " : "") +
            (tagLevel != null ? "tagLevel=" + tagLevel + ", " : "") +
            (createDate != null ? "createDate=" + createDate + ", " : "") +
            (lastModified != null ? "lastModified=" + lastModified + ", " : "") +
            (cancelDate != null ? "cancelDate=" + cancelDate + ", " : "") +
            (tenantId != null ? "tenantId=" + tenantId + ", " : "") +
            (questionPaperId != null ? "questionPaperId=" + questionPaperId + ", " : "") +
            (questionId != null ? "questionId=" + questionId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
