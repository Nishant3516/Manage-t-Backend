package com.ssik.manageit.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.ssik.manageit.domain.enumeration.StudentAssignmentType;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A ClassHomeWork.
 */
@Entity
@Table(name = "class_home_work")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ClassHomeWork implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "school_date", nullable = false)
    private LocalDate schoolDate;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "student_assignment_type", nullable = false)
    private StudentAssignmentType studentAssignmentType;

    @NotNull
    @Size(max = 1000)
    @Column(name = "home_work_text", length = 1000, nullable = false)
    private String homeWorkText;

    @Lob
    @Column(name = "home_work_file")
    private byte[] homeWorkFile;

    @Column(name = "home_work_file_content_type")
    private String homeWorkFileContentType;

    @Column(name = "home_work_file_link")
    private String homeWorkFileLink;

    @Column(name = "assign")
    private Boolean assign;

    @Column(name = "create_date")
    private LocalDate createDate;

    @Column(name = "last_modified")
    private LocalDate lastModified;

    @Column(name = "cancel_date")
    private LocalDate cancelDate;

    @OneToMany(mappedBy = "classHomeWork")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "classStudent", "classHomeWork" }, allowSetters = true)
    private Set<StudentHomeWorkTrack> studentHomeWorkTracks = new HashSet<>();

    @ManyToOne(fetch = FetchType.EAGER)
    @JsonIgnoreProperties(value = { "classHomeWorks", "classClassWorks", "classLessionPlans", "subjectChapter" }, allowSetters = true)
    private ChapterSection chapterSection;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ClassHomeWork id(Long id) {
        this.id = id;
        return this;
    }

    public LocalDate getSchoolDate() {
        return this.schoolDate;
    }

    public ClassHomeWork schoolDate(LocalDate schoolDate) {
        this.schoolDate = schoolDate;
        return this;
    }

    public void setSchoolDate(LocalDate schoolDate) {
        this.schoolDate = schoolDate;
    }

    public StudentAssignmentType getStudentAssignmentType() {
        return this.studentAssignmentType;
    }

    public ClassHomeWork studentAssignmentType(StudentAssignmentType studentAssignmentType) {
        this.studentAssignmentType = studentAssignmentType;
        return this;
    }

    public void setStudentAssignmentType(StudentAssignmentType studentAssignmentType) {
        this.studentAssignmentType = studentAssignmentType;
    }

    public String getHomeWorkText() {
        return this.homeWorkText;
    }

    public ClassHomeWork homeWorkText(String homeWorkText) {
        this.homeWorkText = homeWorkText;
        return this;
    }

    public void setHomeWorkText(String homeWorkText) {
        this.homeWorkText = homeWorkText;
    }

    public byte[] getHomeWorkFile() {
        return this.homeWorkFile;
    }

    public ClassHomeWork homeWorkFile(byte[] homeWorkFile) {
        this.homeWorkFile = homeWorkFile;
        return this;
    }

    public void setHomeWorkFile(byte[] homeWorkFile) {
        this.homeWorkFile = homeWorkFile;
    }

    public String getHomeWorkFileContentType() {
        return this.homeWorkFileContentType;
    }

    public ClassHomeWork homeWorkFileContentType(String homeWorkFileContentType) {
        this.homeWorkFileContentType = homeWorkFileContentType;
        return this;
    }

    public void setHomeWorkFileContentType(String homeWorkFileContentType) {
        this.homeWorkFileContentType = homeWorkFileContentType;
    }

    public String getHomeWorkFileLink() {
        return this.homeWorkFileLink;
    }

    public ClassHomeWork homeWorkFileLink(String homeWorkFileLink) {
        this.homeWorkFileLink = homeWorkFileLink;
        return this;
    }

    public void setHomeWorkFileLink(String homeWorkFileLink) {
        this.homeWorkFileLink = homeWorkFileLink;
    }

    public Boolean getAssign() {
        return this.assign;
    }

    public ClassHomeWork assign(Boolean assign) {
        this.assign = assign;
        return this;
    }

    public void setAssign(Boolean assign) {
        this.assign = assign;
    }

    public LocalDate getCreateDate() {
        return this.createDate;
    }

    public ClassHomeWork createDate(LocalDate createDate) {
        this.createDate = createDate;
        return this;
    }

    public void setCreateDate(LocalDate createDate) {
        this.createDate = createDate;
    }

    public LocalDate getLastModified() {
        return this.lastModified;
    }

    public ClassHomeWork lastModified(LocalDate lastModified) {
        this.lastModified = lastModified;
        return this;
    }

    public void setLastModified(LocalDate lastModified) {
        this.lastModified = lastModified;
    }

    public LocalDate getCancelDate() {
        return this.cancelDate;
    }

    public ClassHomeWork cancelDate(LocalDate cancelDate) {
        this.cancelDate = cancelDate;
        return this;
    }

    public void setCancelDate(LocalDate cancelDate) {
        this.cancelDate = cancelDate;
    }

    public Set<StudentHomeWorkTrack> getStudentHomeWorkTracks() {
        return this.studentHomeWorkTracks;
    }

    public ClassHomeWork studentHomeWorkTracks(Set<StudentHomeWorkTrack> studentHomeWorkTracks) {
        this.setStudentHomeWorkTracks(studentHomeWorkTracks);
        return this;
    }

    public ClassHomeWork addStudentHomeWorkTrack(StudentHomeWorkTrack studentHomeWorkTrack) {
        this.studentHomeWorkTracks.add(studentHomeWorkTrack);
        studentHomeWorkTrack.setClassHomeWork(this);
        return this;
    }

    public ClassHomeWork removeStudentHomeWorkTrack(StudentHomeWorkTrack studentHomeWorkTrack) {
        this.studentHomeWorkTracks.remove(studentHomeWorkTrack);
        studentHomeWorkTrack.setClassHomeWork(null);
        return this;
    }

    public void setStudentHomeWorkTracks(Set<StudentHomeWorkTrack> studentHomeWorkTracks) {
        if (this.studentHomeWorkTracks != null) {
            this.studentHomeWorkTracks.forEach(i -> i.setClassHomeWork(null));
        }
        if (studentHomeWorkTracks != null) {
            studentHomeWorkTracks.forEach(i -> i.setClassHomeWork(this));
        }
        this.studentHomeWorkTracks = studentHomeWorkTracks;
    }

    public ChapterSection getChapterSection() {
        return this.chapterSection;
    }

    public ClassHomeWork chapterSection(ChapterSection chapterSection) {
        this.setChapterSection(chapterSection);
        return this;
    }

    public void setChapterSection(ChapterSection chapterSection) {
        this.chapterSection = chapterSection;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ClassHomeWork)) {
            return false;
        }
        return id != null && id.equals(((ClassHomeWork) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ClassHomeWork{" +
            "id=" + getId() +
            ", schoolDate='" + getSchoolDate() + "'" +
            ", studentAssignmentType='" + getStudentAssignmentType() + "'" +
            ", homeWorkText='" + getHomeWorkText() + "'" +
            ", homeWorkFile='" + getHomeWorkFile() + "'" +
            ", homeWorkFileContentType='" + getHomeWorkFileContentType() + "'" +
            ", homeWorkFileLink='" + getHomeWorkFileLink() + "'" +
            ", assign='" + getAssign() + "'" +
            ", createDate='" + getCreateDate() + "'" +
            ", lastModified='" + getLastModified() + "'" +
            ", cancelDate='" + getCancelDate() + "'" +
            "}";
    }
}
