package com.ssik.manageit.service.dto;

import com.ssik.manageit.domain.enumeration.StudentAssignmentType;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;
import javax.persistence.Lob;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.ssik.manageit.domain.ClassClassWork} entity.
 */
public class ClassClassWorkDTO implements Serializable {

    private Long id;

    @NotNull
    private LocalDate schoolDate;

    @NotNull
    private StudentAssignmentType studentAssignmentType;

    @NotNull
    @Size(max = 1000)
    private String classWorkText;

    @Lob
    private byte[] classWorkFile;

    private String classWorkFileContentType;
    private String classWorkFileLink;

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

    public String getClassWorkText() {
        return classWorkText;
    }

    public void setClassWorkText(String classWorkText) {
        this.classWorkText = classWorkText;
    }

    public byte[] getClassWorkFile() {
        return classWorkFile;
    }

    public void setClassWorkFile(byte[] classWorkFile) {
        this.classWorkFile = classWorkFile;
    }

    public String getClassWorkFileContentType() {
        return classWorkFileContentType;
    }

    public void setClassWorkFileContentType(String classWorkFileContentType) {
        this.classWorkFileContentType = classWorkFileContentType;
    }

    public String getClassWorkFileLink() {
        return classWorkFileLink;
    }

    public void setClassWorkFileLink(String classWorkFileLink) {
        this.classWorkFileLink = classWorkFileLink;
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
        if (!(o instanceof ClassClassWorkDTO)) {
            return false;
        }

        ClassClassWorkDTO classClassWorkDTO = (ClassClassWorkDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, classClassWorkDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ClassClassWorkDTO{" +
            "id=" + getId() +
            ", schoolDate='" + getSchoolDate() + "'" +
            ", studentAssignmentType='" + getStudentAssignmentType() + "'" +
            ", classWorkText='" + getClassWorkText() + "'" +
            ", classWorkFile='" + getClassWorkFile() + "'" +
            ", classWorkFileLink='" + getClassWorkFileLink() + "'" +
            ", assign='" + getAssign() + "'" +
            ", createDate='" + getCreateDate() + "'" +
            ", lastModified='" + getLastModified() + "'" +
            ", cancelDate='" + getCancelDate() + "'" +
            ", chapterSection=" + getChapterSection() +
            "}";
    }
}
