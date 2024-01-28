package com.ssik.manageit.service.criteria;

import com.ssik.manageit.domain.enumeration.UserType;
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
 * Criteria class for the {@link com.ssik.manageit.domain.SchoolUser} entity. This class is used
 * in {@link com.ssik.manageit.web.rest.SchoolUserResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /school-users?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class SchoolUserCriteria implements Serializable, Criteria {

    /**
     * Class for filtering UserType
     */
    public static class UserTypeFilter extends Filter<UserType> {

        public UserTypeFilter() {}

        public UserTypeFilter(UserTypeFilter filter) {
            super(filter);
        }

        @Override
        public UserTypeFilter copy() {
            return new UserTypeFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter loginName;

    private StringFilter password;

    private StringFilter userEmail;

    private StringFilter extraInfo;

    private BooleanFilter activated;

    private UserTypeFilter userType;

    private LocalDateFilter createDate;

    private LocalDateFilter lastModified;

    private LocalDateFilter cancelDate;

    private LongFilter auditLogId;

    private LongFilter schoolClassId;

    private LongFilter classSubjectId;

    public SchoolUserCriteria() {}

    public SchoolUserCriteria(SchoolUserCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.loginName = other.loginName == null ? null : other.loginName.copy();
        this.password = other.password == null ? null : other.password.copy();
        this.userEmail = other.userEmail == null ? null : other.userEmail.copy();
        this.extraInfo = other.extraInfo == null ? null : other.extraInfo.copy();
        this.activated = other.activated == null ? null : other.activated.copy();
        this.userType = other.userType == null ? null : other.userType.copy();
        this.createDate = other.createDate == null ? null : other.createDate.copy();
        this.lastModified = other.lastModified == null ? null : other.lastModified.copy();
        this.cancelDate = other.cancelDate == null ? null : other.cancelDate.copy();
        this.auditLogId = other.auditLogId == null ? null : other.auditLogId.copy();
        this.schoolClassId = other.schoolClassId == null ? null : other.schoolClassId.copy();
        this.classSubjectId = other.classSubjectId == null ? null : other.classSubjectId.copy();
    }

    @Override
    public SchoolUserCriteria copy() {
        return new SchoolUserCriteria(this);
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

    public StringFilter getLoginName() {
        return loginName;
    }

    public StringFilter loginName() {
        if (loginName == null) {
            loginName = new StringFilter();
        }
        return loginName;
    }

    public void setLoginName(StringFilter loginName) {
        this.loginName = loginName;
    }

    public StringFilter getPassword() {
        return password;
    }

    public StringFilter password() {
        if (password == null) {
            password = new StringFilter();
        }
        return password;
    }

    public void setPassword(StringFilter password) {
        this.password = password;
    }

    public StringFilter getUserEmail() {
        return userEmail;
    }

    public StringFilter userEmail() {
        if (userEmail == null) {
            userEmail = new StringFilter();
        }
        return userEmail;
    }

    public void setUserEmail(StringFilter userEmail) {
        this.userEmail = userEmail;
    }

    public StringFilter getExtraInfo() {
        return extraInfo;
    }

    public StringFilter extraInfo() {
        if (extraInfo == null) {
            extraInfo = new StringFilter();
        }
        return extraInfo;
    }

    public void setExtraInfo(StringFilter extraInfo) {
        this.extraInfo = extraInfo;
    }

    public BooleanFilter getActivated() {
        return activated;
    }

    public BooleanFilter activated() {
        if (activated == null) {
            activated = new BooleanFilter();
        }
        return activated;
    }

    public void setActivated(BooleanFilter activated) {
        this.activated = activated;
    }

    public UserTypeFilter getUserType() {
        return userType;
    }

    public UserTypeFilter userType() {
        if (userType == null) {
            userType = new UserTypeFilter();
        }
        return userType;
    }

    public void setUserType(UserTypeFilter userType) {
        this.userType = userType;
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

    public LongFilter getAuditLogId() {
        return auditLogId;
    }

    public LongFilter auditLogId() {
        if (auditLogId == null) {
            auditLogId = new LongFilter();
        }
        return auditLogId;
    }

    public void setAuditLogId(LongFilter auditLogId) {
        this.auditLogId = auditLogId;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final SchoolUserCriteria that = (SchoolUserCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(loginName, that.loginName) &&
            Objects.equals(password, that.password) &&
            Objects.equals(userEmail, that.userEmail) &&
            Objects.equals(extraInfo, that.extraInfo) &&
            Objects.equals(activated, that.activated) &&
            Objects.equals(userType, that.userType) &&
            Objects.equals(createDate, that.createDate) &&
            Objects.equals(lastModified, that.lastModified) &&
            Objects.equals(cancelDate, that.cancelDate) &&
            Objects.equals(auditLogId, that.auditLogId) &&
            Objects.equals(schoolClassId, that.schoolClassId) &&
            Objects.equals(classSubjectId, that.classSubjectId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            loginName,
            password,
            userEmail,
            extraInfo,
            activated,
            userType,
            createDate,
            lastModified,
            cancelDate,
            auditLogId,
            schoolClassId,
            classSubjectId
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SchoolUserCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (loginName != null ? "loginName=" + loginName + ", " : "") +
            (password != null ? "password=" + password + ", " : "") +
            (userEmail != null ? "userEmail=" + userEmail + ", " : "") +
            (extraInfo != null ? "extraInfo=" + extraInfo + ", " : "") +
            (activated != null ? "activated=" + activated + ", " : "") +
            (userType != null ? "userType=" + userType + ", " : "") +
            (createDate != null ? "createDate=" + createDate + ", " : "") +
            (lastModified != null ? "lastModified=" + lastModified + ", " : "") +
            (cancelDate != null ? "cancelDate=" + cancelDate + ", " : "") +
            (auditLogId != null ? "auditLogId=" + auditLogId + ", " : "") +
            (schoolClassId != null ? "schoolClassId=" + schoolClassId + ", " : "") +
            (classSubjectId != null ? "classSubjectId=" + classSubjectId + ", " : "") +
            "}";
    }
}
