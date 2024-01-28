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
 * A ChapterSection.
 */
@Entity
@Table(name = "chapter_section")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ChapterSection implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "section_name", nullable = false)
    private String sectionName;

    @Column(name = "section_number")
    private String sectionNumber;

    @Column(name = "create_date")
    private LocalDate createDate;

    @Column(name = "last_modified")
    private LocalDate lastModified;

    @Column(name = "cancel_date")
    private LocalDate cancelDate;

    @OneToMany(mappedBy = "chapterSection")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "studentHomeWorkTracks", "chapterSection" }, allowSetters = true)
    private Set<ClassHomeWork> classHomeWorks = new HashSet<>();

    @OneToMany(mappedBy = "chapterSection")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "studentClassWorkTracks", "chapterSection" }, allowSetters = true)
    private Set<ClassClassWork> classClassWorks = new HashSet<>();

    @OneToMany(mappedBy = "chapterSection")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(
        value = { "classLessionPlanTracks", "chapterSection", "schoolClass", "classSubject", "subjectChapter" },
        allowSetters = true
    )
    private Set<ClassLessionPlan> classLessionPlans = new HashSet<>();

    @ManyToOne(fetch = FetchType.EAGER)
    @JsonIgnoreProperties(value = { "chapterSections", "classLessionPlans", "classSubject" }, allowSetters = true)
    private SubjectChapter subjectChapter;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ChapterSection id(Long id) {
        this.id = id;
        return this;
    }

    public String getSectionName() {
        return this.sectionName;
    }

    public ChapterSection sectionName(String sectionName) {
        this.sectionName = sectionName;
        return this;
    }

    public void setSectionName(String sectionName) {
        this.sectionName = sectionName;
    }

    public String getSectionNumber() {
        return this.sectionNumber;
    }

    public ChapterSection sectionNumber(String sectionNumber) {
        this.sectionNumber = sectionNumber;
        return this;
    }

    public void setSectionNumber(String sectionNumber) {
        this.sectionNumber = sectionNumber;
    }

    public LocalDate getCreateDate() {
        return this.createDate;
    }

    public ChapterSection createDate(LocalDate createDate) {
        this.createDate = createDate;
        return this;
    }

    public void setCreateDate(LocalDate createDate) {
        this.createDate = createDate;
    }

    public LocalDate getLastModified() {
        return this.lastModified;
    }

    public ChapterSection lastModified(LocalDate lastModified) {
        this.lastModified = lastModified;
        return this;
    }

    public void setLastModified(LocalDate lastModified) {
        this.lastModified = lastModified;
    }

    public LocalDate getCancelDate() {
        return this.cancelDate;
    }

    public ChapterSection cancelDate(LocalDate cancelDate) {
        this.cancelDate = cancelDate;
        return this;
    }

    public void setCancelDate(LocalDate cancelDate) {
        this.cancelDate = cancelDate;
    }

    public Set<ClassHomeWork> getClassHomeWorks() {
        return this.classHomeWorks;
    }

    public ChapterSection classHomeWorks(Set<ClassHomeWork> classHomeWorks) {
        this.setClassHomeWorks(classHomeWorks);
        return this;
    }

    public ChapterSection addClassHomeWork(ClassHomeWork classHomeWork) {
        this.classHomeWorks.add(classHomeWork);
        classHomeWork.setChapterSection(this);
        return this;
    }

    public ChapterSection removeClassHomeWork(ClassHomeWork classHomeWork) {
        this.classHomeWorks.remove(classHomeWork);
        classHomeWork.setChapterSection(null);
        return this;
    }

    public void setClassHomeWorks(Set<ClassHomeWork> classHomeWorks) {
        if (this.classHomeWorks != null) {
            this.classHomeWorks.forEach(i -> i.setChapterSection(null));
        }
        if (classHomeWorks != null) {
            classHomeWorks.forEach(i -> i.setChapterSection(this));
        }
        this.classHomeWorks = classHomeWorks;
    }

    public Set<ClassClassWork> getClassClassWorks() {
        return this.classClassWorks;
    }

    public ChapterSection classClassWorks(Set<ClassClassWork> classClassWorks) {
        this.setClassClassWorks(classClassWorks);
        return this;
    }

    public ChapterSection addClassClassWork(ClassClassWork classClassWork) {
        this.classClassWorks.add(classClassWork);
        classClassWork.setChapterSection(this);
        return this;
    }

    public ChapterSection removeClassClassWork(ClassClassWork classClassWork) {
        this.classClassWorks.remove(classClassWork);
        classClassWork.setChapterSection(null);
        return this;
    }

    public void setClassClassWorks(Set<ClassClassWork> classClassWorks) {
        if (this.classClassWorks != null) {
            this.classClassWorks.forEach(i -> i.setChapterSection(null));
        }
        if (classClassWorks != null) {
            classClassWorks.forEach(i -> i.setChapterSection(this));
        }
        this.classClassWorks = classClassWorks;
    }

    public Set<ClassLessionPlan> getClassLessionPlans() {
        return this.classLessionPlans;
    }

    public ChapterSection classLessionPlans(Set<ClassLessionPlan> classLessionPlans) {
        this.setClassLessionPlans(classLessionPlans);
        return this;
    }

    public ChapterSection addClassLessionPlan(ClassLessionPlan classLessionPlan) {
        this.classLessionPlans.add(classLessionPlan);
        classLessionPlan.setChapterSection(this);
        return this;
    }

    public ChapterSection removeClassLessionPlan(ClassLessionPlan classLessionPlan) {
        this.classLessionPlans.remove(classLessionPlan);
        classLessionPlan.setChapterSection(null);
        return this;
    }

    public void setClassLessionPlans(Set<ClassLessionPlan> classLessionPlans) {
        if (this.classLessionPlans != null) {
            this.classLessionPlans.forEach(i -> i.setChapterSection(null));
        }
        if (classLessionPlans != null) {
            classLessionPlans.forEach(i -> i.setChapterSection(this));
        }
        this.classLessionPlans = classLessionPlans;
    }

    public SubjectChapter getSubjectChapter() {
        return this.subjectChapter;
    }

    public ChapterSection subjectChapter(SubjectChapter subjectChapter) {
        this.setSubjectChapter(subjectChapter);
        return this;
    }

    public void setSubjectChapter(SubjectChapter subjectChapter) {
        this.subjectChapter = subjectChapter;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ChapterSection)) {
            return false;
        }
        return id != null && id.equals(((ChapterSection) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ChapterSection{" +
            "id=" + getId() +
            ", sectionName='" + getSectionName() + "'" +
            ", sectionNumber='" + getSectionNumber() + "'" +
            ", createDate='" + getCreateDate() + "'" +
            ", lastModified='" + getLastModified() + "'" +
            ", cancelDate='" + getCancelDate() + "'" +
            "}";
    }
}
