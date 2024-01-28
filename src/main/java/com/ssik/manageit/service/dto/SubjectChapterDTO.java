package com.ssik.manageit.service.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.ssik.manageit.domain.SubjectChapter} entity.
 */
public class SubjectChapterDTO implements Serializable {

    private Long id;

    @NotNull
    private String chapterName;

    private Integer chapterNumber;

    private LocalDate createDate;

    private LocalDate lastModified;

    private LocalDate cancelDate;

    private ClassSubjectDTO classSubject;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getChapterName() {
        return chapterName;
    }

    public void setChapterName(String chapterName) {
        this.chapterName = chapterName;
    }

    public Integer getChapterNumber() {
        return chapterNumber;
    }

    public void setChapterNumber(Integer chapterNumber) {
        this.chapterNumber = chapterNumber;
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

    public ClassSubjectDTO getClassSubject() {
        return classSubject;
    }

    public void setClassSubject(ClassSubjectDTO classSubject) {
        this.classSubject = classSubject;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SubjectChapterDTO)) {
            return false;
        }

        SubjectChapterDTO subjectChapterDTO = (SubjectChapterDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, subjectChapterDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SubjectChapterDTO{" +
            "id=" + getId() +
            ", chapterName='" + getChapterName() + "'" +
            ", chapterNumber=" + getChapterNumber() +
            ", createDate='" + getCreateDate() + "'" +
            ", lastModified='" + getLastModified() + "'" +
            ", cancelDate='" + getCancelDate() + "'" +
            ", classSubject=" + getClassSubject() +
            "}";
    }
}
