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
 * A SchoolClass.
 */
@Entity
@Table(name = "school_class")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class SchoolClass implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "class_name", nullable = false)
    private String className;

    @Column(name = "class_long_name")
    private String classLongName;

    @Column(name = "create_date")
    private LocalDate createDate;

    @Column(name = "last_modified")
    private LocalDate lastModified;

    @Column(name = "cancel_date")
    private LocalDate cancelDate;

    @OneToMany(mappedBy = "schoolClass")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(
        value = {
            "studentDiscounts",
            "studentAdditionalCharges",
            "studentChargesSummaries",
            "studentPayments",
            "studentAttendences",
            "studentHomeWorkTracks",
            "studentClassWorkTracks",
            "schoolClass",
        },
        allowSetters = true
    )
    private Set<ClassStudent> classStudents = new HashSet<>();

    @OneToMany(mappedBy = "schoolClass")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(
        value = { "classLessionPlanTracks", "chapterSection", "schoolClass", "classSubject", "subjectChapter" },
        allowSetters = true
    )
    private Set<ClassLessionPlan> classLessionPlans = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "schoolClasses", "schoolLedgerHeads", "idStores", "auditLogs" }, allowSetters = true)
    private School school;

    @ManyToMany(mappedBy = "schoolClasses")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "schoolClasses" }, allowSetters = true)
    private Set<SchoolNotifications> schoolNotifications = new HashSet<>();

    @ManyToMany(mappedBy = "schoolClasses")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "schoolClasses", "schoolLedgerHead" }, allowSetters = true)
    private Set<ClassFee> classFees = new HashSet<>();

    @ManyToMany(mappedBy = "schoolClasses")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "subjectChapters", "classLessionPlans", "schoolClasses", "schoolUsers" }, allowSetters = true)
    private Set<ClassSubject> classSubjects = new HashSet<>();

    @ManyToMany(mappedBy = "schoolClasses")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "auditLogs", "schoolClasses", "classSubjects" }, allowSetters = true)
    private Set<SchoolUser> schoolUsers = new HashSet<>();

    @ManyToMany(mappedBy = "schoolClasses")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "schoolClasses" }, allowSetters = true)
    private Set<SchoolDaysOff> schoolDaysOffs = new HashSet<>();

    @ManyToMany(mappedBy = "schoolClasses")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "schoolClasses" }, allowSetters = true)
    private Set<SchoolEvent> schoolEvents = new HashSet<>();

    @ManyToMany(mappedBy = "schoolClasses")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "schoolClasses" }, allowSetters = true)
    private Set<SchoolPictureGallery> schoolPictureGalleries = new HashSet<>();

    @ManyToMany(mappedBy = "schoolClasses")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "schoolClasses" }, allowSetters = true)
    private Set<SchoolVideoGallery> vchoolVideoGalleries = new HashSet<>();

    @ManyToMany(mappedBy = "schoolClasses")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "schoolClasses" }, allowSetters = true)
    private Set<SchoolReport> schoolReports = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public SchoolClass id(Long id) {
        this.id = id;
        return this;
    }

    public String getClassName() {
        return this.className;
    }

    public SchoolClass className(String className) {
        this.className = className;
        return this;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getClassLongName() {
        return this.classLongName;
    }

    public SchoolClass classLongName(String classLongName) {
        this.classLongName = classLongName;
        return this;
    }

    public void setClassLongName(String classLongName) {
        this.classLongName = classLongName;
    }

    public LocalDate getCreateDate() {
        return this.createDate;
    }

    public SchoolClass createDate(LocalDate createDate) {
        this.createDate = createDate;
        return this;
    }

    public void setCreateDate(LocalDate createDate) {
        this.createDate = createDate;
    }

    public LocalDate getLastModified() {
        return this.lastModified;
    }

    public SchoolClass lastModified(LocalDate lastModified) {
        this.lastModified = lastModified;
        return this;
    }

    public void setLastModified(LocalDate lastModified) {
        this.lastModified = lastModified;
    }

    public LocalDate getCancelDate() {
        return this.cancelDate;
    }

    public SchoolClass cancelDate(LocalDate cancelDate) {
        this.cancelDate = cancelDate;
        return this;
    }

    public void setCancelDate(LocalDate cancelDate) {
        this.cancelDate = cancelDate;
    }

    public Set<ClassStudent> getClassStudents() {
        return this.classStudents;
    }

    public SchoolClass classStudents(Set<ClassStudent> classStudents) {
        this.setClassStudents(classStudents);
        return this;
    }

    public SchoolClass addClassStudent(ClassStudent classStudent) {
        this.classStudents.add(classStudent);
        classStudent.setSchoolClass(this);
        return this;
    }

    public SchoolClass removeClassStudent(ClassStudent classStudent) {
        this.classStudents.remove(classStudent);
        classStudent.setSchoolClass(null);
        return this;
    }

    public void setClassStudents(Set<ClassStudent> classStudents) {
        if (this.classStudents != null) {
            this.classStudents.forEach(i -> i.setSchoolClass(null));
        }
        if (classStudents != null) {
            classStudents.forEach(i -> i.setSchoolClass(this));
        }
        this.classStudents = classStudents;
    }

    public Set<ClassLessionPlan> getClassLessionPlans() {
        return this.classLessionPlans;
    }

    public SchoolClass classLessionPlans(Set<ClassLessionPlan> classLessionPlans) {
        this.setClassLessionPlans(classLessionPlans);
        return this;
    }

    public SchoolClass addClassLessionPlan(ClassLessionPlan classLessionPlan) {
        this.classLessionPlans.add(classLessionPlan);
        classLessionPlan.setSchoolClass(this);
        return this;
    }

    public SchoolClass removeClassLessionPlan(ClassLessionPlan classLessionPlan) {
        this.classLessionPlans.remove(classLessionPlan);
        classLessionPlan.setSchoolClass(null);
        return this;
    }

    public void setClassLessionPlans(Set<ClassLessionPlan> classLessionPlans) {
        if (this.classLessionPlans != null) {
            this.classLessionPlans.forEach(i -> i.setSchoolClass(null));
        }
        if (classLessionPlans != null) {
            classLessionPlans.forEach(i -> i.setSchoolClass(this));
        }
        this.classLessionPlans = classLessionPlans;
    }

    public School getSchool() {
        return this.school;
    }

    public SchoolClass school(School school) {
        this.setSchool(school);
        return this;
    }

    public void setSchool(School school) {
        this.school = school;
    }

    public Set<SchoolNotifications> getSchoolNotifications() {
        return this.schoolNotifications;
    }

    public SchoolClass schoolNotifications(Set<SchoolNotifications> schoolNotifications) {
        this.setSchoolNotifications(schoolNotifications);
        return this;
    }

    public SchoolClass addSchoolNotifications(SchoolNotifications schoolNotifications) {
        this.schoolNotifications.add(schoolNotifications);
        schoolNotifications.getSchoolClasses().add(this);
        return this;
    }

    public SchoolClass removeSchoolNotifications(SchoolNotifications schoolNotifications) {
        this.schoolNotifications.remove(schoolNotifications);
        schoolNotifications.getSchoolClasses().remove(this);
        return this;
    }

    public void setSchoolNotifications(Set<SchoolNotifications> schoolNotifications) {
        if (this.schoolNotifications != null) {
            this.schoolNotifications.forEach(i -> i.removeSchoolClass(this));
        }
        if (schoolNotifications != null) {
            schoolNotifications.forEach(i -> i.addSchoolClass(this));
        }
        this.schoolNotifications = schoolNotifications;
    }

    public Set<ClassFee> getClassFees() {
        return this.classFees;
    }

    public SchoolClass classFees(Set<ClassFee> classFees) {
        this.setClassFees(classFees);
        return this;
    }

    public SchoolClass addClassFee(ClassFee classFee) {
        this.classFees.add(classFee);
        classFee.getSchoolClasses().add(this);
        return this;
    }

    public SchoolClass removeClassFee(ClassFee classFee) {
        this.classFees.remove(classFee);
        classFee.getSchoolClasses().remove(this);
        return this;
    }

    public void setClassFees(Set<ClassFee> classFees) {
        if (this.classFees != null) {
            this.classFees.forEach(i -> i.removeSchoolClass(this));
        }
        if (classFees != null) {
            classFees.forEach(i -> i.addSchoolClass(this));
        }
        this.classFees = classFees;
    }

    public Set<ClassSubject> getClassSubjects() {
        return this.classSubjects;
    }

    public SchoolClass classSubjects(Set<ClassSubject> classSubjects) {
        this.setClassSubjects(classSubjects);
        return this;
    }

    public SchoolClass addClassSubject(ClassSubject classSubject) {
        this.classSubjects.add(classSubject);
        classSubject.getSchoolClasses().add(this);
        return this;
    }

    public SchoolClass removeClassSubject(ClassSubject classSubject) {
        this.classSubjects.remove(classSubject);
        classSubject.getSchoolClasses().remove(this);
        return this;
    }

    public void setClassSubjects(Set<ClassSubject> classSubjects) {
        if (this.classSubjects != null) {
            this.classSubjects.forEach(i -> i.removeSchoolClass(this));
        }
        if (classSubjects != null) {
            classSubjects.forEach(i -> i.addSchoolClass(this));
        }
        this.classSubjects = classSubjects;
    }

    public Set<SchoolUser> getSchoolUsers() {
        return this.schoolUsers;
    }

    public SchoolClass schoolUsers(Set<SchoolUser> schoolUsers) {
        this.setSchoolUsers(schoolUsers);
        return this;
    }

    public SchoolClass addSchoolUser(SchoolUser schoolUser) {
        this.schoolUsers.add(schoolUser);
        schoolUser.getSchoolClasses().add(this);
        return this;
    }

    public SchoolClass removeSchoolUser(SchoolUser schoolUser) {
        this.schoolUsers.remove(schoolUser);
        schoolUser.getSchoolClasses().remove(this);
        return this;
    }

    public void setSchoolUsers(Set<SchoolUser> schoolUsers) {
        if (this.schoolUsers != null) {
            this.schoolUsers.forEach(i -> i.removeSchoolClass(this));
        }
        if (schoolUsers != null) {
            schoolUsers.forEach(i -> i.addSchoolClass(this));
        }
        this.schoolUsers = schoolUsers;
    }

    public Set<SchoolDaysOff> getSchoolDaysOffs() {
        return this.schoolDaysOffs;
    }

    public SchoolClass schoolDaysOffs(Set<SchoolDaysOff> schoolDaysOffs) {
        this.setSchoolDaysOffs(schoolDaysOffs);
        return this;
    }

    public SchoolClass addSchoolDaysOff(SchoolDaysOff schoolDaysOff) {
        this.schoolDaysOffs.add(schoolDaysOff);
        schoolDaysOff.getSchoolClasses().add(this);
        return this;
    }

    public SchoolClass removeSchoolDaysOff(SchoolDaysOff schoolDaysOff) {
        this.schoolDaysOffs.remove(schoolDaysOff);
        schoolDaysOff.getSchoolClasses().remove(this);
        return this;
    }

    public void setSchoolDaysOffs(Set<SchoolDaysOff> schoolDaysOffs) {
        if (this.schoolDaysOffs != null) {
            this.schoolDaysOffs.forEach(i -> i.removeSchoolClass(this));
        }
        if (schoolDaysOffs != null) {
            schoolDaysOffs.forEach(i -> i.addSchoolClass(this));
        }
        this.schoolDaysOffs = schoolDaysOffs;
    }

    public Set<SchoolEvent> getSchoolEvents() {
        return this.schoolEvents;
    }

    public SchoolClass schoolEvents(Set<SchoolEvent> schoolEvents) {
        this.setSchoolEvents(schoolEvents);
        return this;
    }

    public SchoolClass addSchoolEvent(SchoolEvent schoolEvent) {
        this.schoolEvents.add(schoolEvent);
        schoolEvent.getSchoolClasses().add(this);
        return this;
    }

    public SchoolClass removeSchoolEvent(SchoolEvent schoolEvent) {
        this.schoolEvents.remove(schoolEvent);
        schoolEvent.getSchoolClasses().remove(this);
        return this;
    }

    public void setSchoolEvents(Set<SchoolEvent> schoolEvents) {
        if (this.schoolEvents != null) {
            this.schoolEvents.forEach(i -> i.removeSchoolClass(this));
        }
        if (schoolEvents != null) {
            schoolEvents.forEach(i -> i.addSchoolClass(this));
        }
        this.schoolEvents = schoolEvents;
    }

    public Set<SchoolPictureGallery> getSchoolPictureGalleries() {
        return this.schoolPictureGalleries;
    }

    public SchoolClass schoolPictureGalleries(Set<SchoolPictureGallery> schoolPictureGalleries) {
        this.setSchoolPictureGalleries(schoolPictureGalleries);
        return this;
    }

    public SchoolClass addSchoolPictureGallery(SchoolPictureGallery schoolPictureGallery) {
        this.schoolPictureGalleries.add(schoolPictureGallery);
        schoolPictureGallery.getSchoolClasses().add(this);
        return this;
    }

    public SchoolClass removeSchoolPictureGallery(SchoolPictureGallery schoolPictureGallery) {
        this.schoolPictureGalleries.remove(schoolPictureGallery);
        schoolPictureGallery.getSchoolClasses().remove(this);
        return this;
    }

    public void setSchoolPictureGalleries(Set<SchoolPictureGallery> schoolPictureGalleries) {
        if (this.schoolPictureGalleries != null) {
            this.schoolPictureGalleries.forEach(i -> i.removeSchoolClass(this));
        }
        if (schoolPictureGalleries != null) {
            schoolPictureGalleries.forEach(i -> i.addSchoolClass(this));
        }
        this.schoolPictureGalleries = schoolPictureGalleries;
    }

    public Set<SchoolVideoGallery> getVchoolVideoGalleries() {
        return this.vchoolVideoGalleries;
    }

    public SchoolClass vchoolVideoGalleries(Set<SchoolVideoGallery> schoolVideoGalleries) {
        this.setVchoolVideoGalleries(schoolVideoGalleries);
        return this;
    }

    public SchoolClass addVchoolVideoGallery(SchoolVideoGallery schoolVideoGallery) {
        this.vchoolVideoGalleries.add(schoolVideoGallery);
        schoolVideoGallery.getSchoolClasses().add(this);
        return this;
    }

    public SchoolClass removeVchoolVideoGallery(SchoolVideoGallery schoolVideoGallery) {
        this.vchoolVideoGalleries.remove(schoolVideoGallery);
        schoolVideoGallery.getSchoolClasses().remove(this);
        return this;
    }

    public void setVchoolVideoGalleries(Set<SchoolVideoGallery> schoolVideoGalleries) {
        if (this.vchoolVideoGalleries != null) {
            this.vchoolVideoGalleries.forEach(i -> i.removeSchoolClass(this));
        }
        if (schoolVideoGalleries != null) {
            schoolVideoGalleries.forEach(i -> i.addSchoolClass(this));
        }
        this.vchoolVideoGalleries = schoolVideoGalleries;
    }

    public Set<SchoolReport> getSchoolReports() {
        return this.schoolReports;
    }

    public SchoolClass schoolReports(Set<SchoolReport> schoolReports) {
        this.setSchoolReports(schoolReports);
        return this;
    }

    public SchoolClass addSchoolReport(SchoolReport schoolReport) {
        this.schoolReports.add(schoolReport);
        schoolReport.getSchoolClasses().add(this);
        return this;
    }

    public SchoolClass removeSchoolReport(SchoolReport schoolReport) {
        this.schoolReports.remove(schoolReport);
        schoolReport.getSchoolClasses().remove(this);
        return this;
    }

    public void setSchoolReports(Set<SchoolReport> schoolReports) {
        if (this.schoolReports != null) {
            this.schoolReports.forEach(i -> i.removeSchoolClass(this));
        }
        if (schoolReports != null) {
            schoolReports.forEach(i -> i.addSchoolClass(this));
        }
        this.schoolReports = schoolReports;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SchoolClass)) {
            return false;
        }
        return id != null && id.equals(((SchoolClass) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SchoolClass{" +
            "id=" + getId() +
            ", className='" + getClassName() + "'" +
            ", classLongName='" + getClassLongName() + "'" +
            ", createDate='" + getCreateDate() + "'" +
            ", lastModified='" + getLastModified() + "'" +
            ", cancelDate='" + getCancelDate() + "'" +
            "}";
    }
}
