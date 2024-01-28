package com.ssik.manageit.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A ClassSubject.
 */
@Entity
@Table(name = "class_subject")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ClassSubject implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "subject_name", nullable = false)
    private String subjectName;

    @Column(name = "create_date")
    private LocalDate createDate;

    @Column(name = "last_modified")
    private LocalDate lastModified;

    @Column(name = "cancel_date")
    private LocalDate cancelDate;

    @OneToMany(mappedBy = "classSubject")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "chapterSections", "classLessionPlans", "classSubject" }, allowSetters = true)
    private Set<SubjectChapter> subjectChapters = new HashSet<>();

    @OneToMany(mappedBy = "classSubject")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(
        value = { "classLessionPlanTracks", "chapterSection", "schoolClass", "classSubject", "subjectChapter" },
        allowSetters = true
    )
    private Set<ClassLessionPlan> classLessionPlans = new HashSet<>();

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JoinTable(
        name = "rel_class_subject__school_class",
        joinColumns = @JoinColumn(name = "class_subject_id"),
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

    @ManyToMany(mappedBy = "classSubjects")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "auditLogs", "schoolClasses", "classSubjects" }, allowSetters = true)
    private Set<SchoolUser> schoolUsers = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ClassSubject id(Long id) {
        this.id = id;
        return this;
    }

    public String getSubjectName() {
        return this.subjectName;
    }

    public ClassSubject subjectName(String subjectName) {
        this.subjectName = subjectName;
        return this;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public LocalDate getCreateDate() {
        return this.createDate;
    }

    public ClassSubject createDate(LocalDate createDate) {
        this.createDate = createDate;
        return this;
    }

    public void setCreateDate(LocalDate createDate) {
        this.createDate = createDate;
    }

    public LocalDate getLastModified() {
        return this.lastModified;
    }

    public ClassSubject lastModified(LocalDate lastModified) {
        this.lastModified = lastModified;
        return this;
    }

    public void setLastModified(LocalDate lastModified) {
        this.lastModified = lastModified;
    }

    public LocalDate getCancelDate() {
        return this.cancelDate;
    }

    public ClassSubject cancelDate(LocalDate cancelDate) {
        this.cancelDate = cancelDate;
        return this;
    }

    public void setCancelDate(LocalDate cancelDate) {
        this.cancelDate = cancelDate;
    }

    public Set<SubjectChapter> getSubjectChapters() {
        return this.subjectChapters;
    }

    public ClassSubject subjectChapters(Set<SubjectChapter> subjectChapters) {
        this.setSubjectChapters(subjectChapters);
        return this;
    }

    public ClassSubject addSubjectChapter(SubjectChapter subjectChapter) {
        this.subjectChapters.add(subjectChapter);
        subjectChapter.setClassSubject(this);
        return this;
    }

    public ClassSubject removeSubjectChapter(SubjectChapter subjectChapter) {
        this.subjectChapters.remove(subjectChapter);
        subjectChapter.setClassSubject(null);
        return this;
    }

    public void setSubjectChapters(Set<SubjectChapter> subjectChapters) {
        if (this.subjectChapters != null) {
            this.subjectChapters.forEach(i -> i.setClassSubject(null));
        }
        if (subjectChapters != null) {
            subjectChapters.forEach(i -> i.setClassSubject(this));
        }
        this.subjectChapters = subjectChapters;
    }

    public Set<ClassLessionPlan> getClassLessionPlans() {
        return this.classLessionPlans;
    }

    public ClassSubject classLessionPlans(Set<ClassLessionPlan> classLessionPlans) {
        this.setClassLessionPlans(classLessionPlans);
        return this;
    }

    public ClassSubject addClassLessionPlan(ClassLessionPlan classLessionPlan) {
        this.classLessionPlans.add(classLessionPlan);
        classLessionPlan.setClassSubject(this);
        return this;
    }

    public ClassSubject removeClassLessionPlan(ClassLessionPlan classLessionPlan) {
        this.classLessionPlans.remove(classLessionPlan);
        classLessionPlan.setClassSubject(null);
        return this;
    }

    public void setClassLessionPlans(Set<ClassLessionPlan> classLessionPlans) {
        if (this.classLessionPlans != null) {
            this.classLessionPlans.forEach(i -> i.setClassSubject(null));
        }
        if (classLessionPlans != null) {
            classLessionPlans.forEach(i -> i.setClassSubject(this));
        }
        this.classLessionPlans = classLessionPlans;
    }

    public Set<SchoolClass> getSchoolClasses() {
        return this.schoolClasses;
    }

    public ClassSubject schoolClasses(Set<SchoolClass> schoolClasses) {
        this.setSchoolClasses(schoolClasses);
        return this;
    }

    public ClassSubject addSchoolClass(SchoolClass schoolClass) {
        this.schoolClasses.add(schoolClass);
        schoolClass.getClassSubjects().add(this);
        return this;
    }

    public ClassSubject removeSchoolClass(SchoolClass schoolClass) {
        this.schoolClasses.remove(schoolClass);
        schoolClass.getClassSubjects().remove(this);
        return this;
    }

    public void setSchoolClasses(Set<SchoolClass> schoolClasses) {
        this.schoolClasses = schoolClasses;
    }

    public Set<SchoolUser> getSchoolUsers() {
        return this.schoolUsers;
    }

    public ClassSubject schoolUsers(Set<SchoolUser> schoolUsers) {
        this.setSchoolUsers(schoolUsers);
        return this;
    }

    public ClassSubject addSchoolUser(SchoolUser schoolUser) {
        this.schoolUsers.add(schoolUser);
        schoolUser.getClassSubjects().add(this);
        return this;
    }

    public ClassSubject removeSchoolUser(SchoolUser schoolUser) {
        this.schoolUsers.remove(schoolUser);
        schoolUser.getClassSubjects().remove(this);
        return this;
    }

    public void setSchoolUsers(Set<SchoolUser> schoolUsers) {
        if (this.schoolUsers != null) {
            this.schoolUsers.forEach(i -> i.removeClassSubject(this));
        }
        if (schoolUsers != null) {
            schoolUsers.forEach(i -> i.addClassSubject(this));
        }
        this.schoolUsers = schoolUsers;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ClassSubject)) {
            return false;
        }
        return id != null && id.equals(((ClassSubject) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ClassSubject{" +
            "id=" + getId() +
            ", subjectName='" + getSubjectName() + "'" +
            ", createDate='" + getCreateDate() + "'" +
            ", lastModified='" + getLastModified() + "'" +
            ", cancelDate='" + getCancelDate() + "'" +
            "}";
    }
}
