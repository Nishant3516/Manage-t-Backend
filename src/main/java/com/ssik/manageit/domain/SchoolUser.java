package com.ssik.manageit.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.ssik.manageit.domain.enumeration.UserType;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A SchoolUser.
 */
@Entity
@Table(name = "school_user")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class SchoolUser implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "login_name", nullable = false)
    private String loginName;

    @NotNull
    @Column(name = "password", nullable = false)
    private String password;

    @NotNull
    @Column(name = "user_email", nullable = false)
    private String userEmail;

    @Column(name = "extra_info")
    private String extraInfo;

    @Column(name = "activated")
    private Boolean activated;

    @Enumerated(EnumType.STRING)
    @Column(name = "user_type")
    private UserType userType;

    @Column(name = "create_date")
    private LocalDate createDate;

    @Column(name = "last_modified")
    private LocalDate lastModified;

    @Column(name = "cancel_date")
    private LocalDate cancelDate;

    @OneToMany(mappedBy = "schoolUser")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "school", "schoolUser" }, allowSetters = true)
    private Set<AuditLog> auditLogs = new HashSet<>();

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JoinTable(
        name = "rel_school_user__school_class",
        joinColumns = @JoinColumn(name = "school_user_id"),
        inverseJoinColumns = @JoinColumn(name = "school_class_id")
    )
    @JsonIgnoreProperties(
        value = {
            "classStudents",
            "classLessionPlans",
            "school",
            "schoolNotifications",
            "classFees",
            "classSubjects",
            "schoolUsers",
            "schoolDaysOffs",
            "schoolEvents",
            "schoolPictureGalleries",
            "vchoolVideoGalleries",
            "schoolReports",
        },
        allowSetters = true
    )
    private Set<SchoolClass> schoolClasses = new HashSet<>();

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JoinTable(
        name = "rel_school_user__class_subject",
        joinColumns = @JoinColumn(name = "school_user_id"),
        inverseJoinColumns = @JoinColumn(name = "class_subject_id")
    )
    @JsonIgnoreProperties(value = { "subjectChapters", "classLessionPlans", "schoolClasses", "schoolUsers" }, allowSetters = true)
    private Set<ClassSubject> classSubjects = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public SchoolUser id(Long id) {
        this.id = id;
        return this;
    }

    public String getLoginName() {
        return this.loginName;
    }

    public SchoolUser loginName(String loginName) {
        this.loginName = loginName;
        return this;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getPassword() {
        return this.password;
    }

    public SchoolUser password(String password) {
        this.password = password;
        return this;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserEmail() {
        return this.userEmail;
    }

    public SchoolUser userEmail(String userEmail) {
        this.userEmail = userEmail;
        return this;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getExtraInfo() {
        return this.extraInfo;
    }

    public SchoolUser extraInfo(String extraInfo) {
        this.extraInfo = extraInfo;
        return this;
    }

    public void setExtraInfo(String extraInfo) {
        this.extraInfo = extraInfo;
    }

    public Boolean getActivated() {
        return this.activated;
    }

    public SchoolUser activated(Boolean activated) {
        this.activated = activated;
        return this;
    }

    public void setActivated(Boolean activated) {
        this.activated = activated;
    }

    public UserType getUserType() {
        return this.userType;
    }

    public SchoolUser userType(UserType userType) {
        this.userType = userType;
        return this;
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }

    public LocalDate getCreateDate() {
        return this.createDate;
    }

    public SchoolUser createDate(LocalDate createDate) {
        this.createDate = createDate;
        return this;
    }

    public void setCreateDate(LocalDate createDate) {
        this.createDate = createDate;
    }

    public LocalDate getLastModified() {
        return this.lastModified;
    }

    public SchoolUser lastModified(LocalDate lastModified) {
        this.lastModified = lastModified;
        return this;
    }

    public void setLastModified(LocalDate lastModified) {
        this.lastModified = lastModified;
    }

    public LocalDate getCancelDate() {
        return this.cancelDate;
    }

    public SchoolUser cancelDate(LocalDate cancelDate) {
        this.cancelDate = cancelDate;
        return this;
    }

    public void setCancelDate(LocalDate cancelDate) {
        this.cancelDate = cancelDate;
    }

    public Set<AuditLog> getAuditLogs() {
        return this.auditLogs;
    }

    public SchoolUser auditLogs(Set<AuditLog> auditLogs) {
        this.setAuditLogs(auditLogs);
        return this;
    }

    public SchoolUser addAuditLog(AuditLog auditLog) {
        this.auditLogs.add(auditLog);
        auditLog.setSchoolUser(this);
        return this;
    }

    public SchoolUser removeAuditLog(AuditLog auditLog) {
        this.auditLogs.remove(auditLog);
        auditLog.setSchoolUser(null);
        return this;
    }

    public void setAuditLogs(Set<AuditLog> auditLogs) {
        if (this.auditLogs != null) {
            this.auditLogs.forEach(i -> i.setSchoolUser(null));
        }
        if (auditLogs != null) {
            auditLogs.forEach(i -> i.setSchoolUser(this));
        }
        this.auditLogs = auditLogs;
    }

    public Set<SchoolClass> getSchoolClasses() {
        return this.schoolClasses;
    }

    public SchoolUser schoolClasses(Set<SchoolClass> schoolClasses) {
        this.setSchoolClasses(schoolClasses);
        return this;
    }

    public SchoolUser addSchoolClass(SchoolClass schoolClass) {
        this.schoolClasses.add(schoolClass);
        schoolClass.getSchoolUsers().add(this);
        return this;
    }

    public SchoolUser removeSchoolClass(SchoolClass schoolClass) {
        this.schoolClasses.remove(schoolClass);
        schoolClass.getSchoolUsers().remove(this);
        return this;
    }

    public void setSchoolClasses(Set<SchoolClass> schoolClasses) {
        this.schoolClasses = schoolClasses;
    }

    public Set<ClassSubject> getClassSubjects() {
        return this.classSubjects;
    }

    public SchoolUser classSubjects(Set<ClassSubject> classSubjects) {
        this.setClassSubjects(classSubjects);
        return this;
    }

    public SchoolUser addClassSubject(ClassSubject classSubject) {
        this.classSubjects.add(classSubject);
        classSubject.getSchoolUsers().add(this);
        return this;
    }

    public SchoolUser removeClassSubject(ClassSubject classSubject) {
        this.classSubjects.remove(classSubject);
        classSubject.getSchoolUsers().remove(this);
        return this;
    }

    public void setClassSubjects(Set<ClassSubject> classSubjects) {
        this.classSubjects = classSubjects;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SchoolUser)) {
            return false;
        }
        return id != null && id.equals(((SchoolUser) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SchoolUser{" +
            "id=" + getId() +
            ", loginName='" + getLoginName() + "'" +
            ", password='" + getPassword() + "'" +
            ", userEmail='" + getUserEmail() + "'" +
            ", extraInfo='" + getExtraInfo() + "'" +
            ", activated='" + getActivated() + "'" +
            ", userType='" + getUserType() + "'" +
            ", createDate='" + getCreateDate() + "'" +
            ", lastModified='" + getLastModified() + "'" +
            ", cancelDate='" + getCancelDate() + "'" +
            "}";
    }
}
