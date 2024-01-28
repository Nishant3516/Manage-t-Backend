package com.ssik.manageit.service.dto;

import com.ssik.manageit.domain.enumeration.StudentAssignmentType;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;
import javax.persistence.Lob;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.ssik.manageit.domain.ClassHomeWork} entity.
 */
public class ClassHomeWorkDTO implements Serializable {

    private Long id;

    @NotNull
    private LocalDate schoolDate;

    @NotNull
    private StudentAssignmentType studentAssignmentType;

    @NotNull
    @Size(max = 1000)
    private String homeWorkText;

    @Lob
    private byte[] homeWorkFile;

    private String homeWorkFileContentType;
    private String homeWorkFileLink;

    private Boolean assign;

    private LocalDate createDate;

    private LocalDate lastModified;

    private LocalDate cancelDate;

    private ChapterSectionDTO chapterSection;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getSchoolDate() {
        return schoolDate;
    }

    public void setSchoolDate(LocalDate schoolDate) {
        this.schoolDate = schoolDate;
    }

    public StudentAssignmentType getStudentAssignmentType() {
        return studentAssignmentType;
    }

    public void setStudentAssignmentType(StudentAssignmentType studentAssignmentType) {
        this.studentAssignmentType = studentAssignmentType;
    }

    public String getHomeWorkText() {
        return homeWorkText;
    }

    public void setHomeWorkText(String homeWorkText) {
        this.homeWorkText = homeWorkText;
    }

    public byte[] getHomeWorkFile() {
        return homeWorkFile;
    }

    public void setHomeWorkFile(byte[] homeWorkFile) {
        this.homeWorkFile = homeWorkFile;
    }

    public String getHomeWorkFileContentType() {
        return homeWorkFileContentType;
    }

    public void setHomeWorkFileContentType(String homeWorkFileContentType) {
        this.homeWorkFileContentType = homeWorkFileContentType;
    }

    public String getHomeWorkFileLink() {
        return homeWorkFileLink;
    }

    public void setHomeWorkFileLink(String homeWorkFileLink) {
        this.homeWorkFileLink = homeWorkFileLink;
    }

    public Boolean getAssign() {
        return assign;
    }

    public void setAssign(Boolean assign) {
        this.assign = assign;
    }

    public LocalDate getCreateDate() {
        return createDate;
    }

    public void setCreateDate(LocalDate createDate) {
        this.createDate = createDate;
    }

    public LocalDate getLastModified() {
        return lastModified;
    }

    public void setLastModified(LocalDate lastModified) {
        this.lastModified = lastModified;
    }

    public LocalDate getCancelDate() {
        return cancelDate;
    }

    public void setCancelDate(LocalDate cancelDate) {
        this.cancelDate = cancelDate;
    }

    public ChapterSectionDTO getChapterSection() {
        return chapterSection;
    }

    public void setChapterSection(ChapterSectionDTO chapterSection) {
        this.chapterSection = chapterSection;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ClassHomeWorkDTO)) {
            return false;
        }

        ClassHomeWorkDTO classHomeWorkDTO = (ClassHomeWorkDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, classHomeWorkDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ClassHomeWorkDTO{" +
            "id=" + getId() +
            ", schoolDate='" + getSchoolDate() + "'" +
            ", studentAssignmentType='" + getStudentAssignmentType() + "'" +
            ", homeWorkText='" + getHomeWorkText() + "'" +
            ", homeWorkFile='" + getHomeWorkFile() + "'" +
            ", homeWorkFileLink='" + getHomeWorkFileLink() + "'" +
            ", assign='" + getAssign() + "'" +
            ", createDate='" + getCreateDate() + "'" +
            ", lastModified='" + getLastModified() + "'" +
            ", cancelDate='" + getCancelDate() + "'" +
            ", chapterSection=" + getChapterSection() +
            "}";
    }
}
