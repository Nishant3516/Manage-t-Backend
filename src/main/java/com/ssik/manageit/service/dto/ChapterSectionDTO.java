package com.ssik.manageit.service.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.ssik.manageit.domain.ChapterSection} entity.
 */
public class ChapterSectionDTO implements Serializable {

    private Long id;

    @NotNull
    private String sectionName;

    private String sectionNumber;

    private LocalDate createDate;

    private LocalDate lastModified;

    private LocalDate cancelDate;

    private SubjectChapterDTO subjectChapter;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSectionName() {
        return sectionName;
    }

    public void setSectionName(String sectionName) {
        this.sectionName = sectionName;
    }

    public String getSectionNumber() {
        return sectionNumber;
    }

    public void setSectionNumber(String sectionNumber) {
        this.sectionNumber = sectionNumber;
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

    public SubjectChapterDTO getSubjectChapter() {
        return subjectChapter;
    }

    public void setSubjectChapter(SubjectChapterDTO subjectChapter) {
        this.subjectChapter = subjectChapter;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ChapterSectionDTO)) {
            return false;
        }

        ChapterSectionDTO chapterSectionDTO = (ChapterSectionDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, chapterSectionDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ChapterSectionDTO{" +
            "id=" + getId() +
            ", sectionName='" + getSectionName() + "'" +
            ", sectionNumber='" + getSectionNumber() + "'" +
            ", createDate='" + getCreateDate() + "'" +
            ", lastModified='" + getLastModified() + "'" +
            ", cancelDate='" + getCancelDate() + "'" +
            ", subjectChapter=" + getSubjectChapter() +
            "}";
    }
}
