package com.ssik.manageit.service.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

import javax.persistence.Lob;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.ssik.manageit.domain.enumeration.TaskStatus;

/**
 * A DTO for the {@link com.ssik.manageit.domain.ClassLessionPlan} entity.
 */
public class ClassLessionPlanDTO implements Serializable {

	private Long id;

	@NotNull
	private LocalDate schoolDate;

	@NotNull
	@Size(max = 1000)
	private String classWorkText;

	@NotNull
	@Size(max = 1000)
	private String homeWorkText;

	// @NotNull
	private TaskStatus workStatus;

	@Lob
	private byte[] lesionPlanFile;

	private String lesionPlanFileContentType;
	private String lessionPlanFileLink;

	private LocalDate createDate;

	private LocalDate lastModified;

	private LocalDate cancelDate;

	private ChapterSectionDTO chapterSection;

	private SchoolClassDTO schoolClass;

	private ClassSubjectDTO classSubject;

	private SubjectChapterDTO subjectChapter;

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

	public String getClassWorkText() {
		return classWorkText;
	}

	public void setClassWorkText(String classWorkText) {
		this.classWorkText = classWorkText;
	}

	public String getHomeWorkText() {
		return homeWorkText;
	}

	public void setHomeWorkText(String homeWorkText) {
		this.homeWorkText = homeWorkText;
	}

	public TaskStatus getWorkStatus() {
		return workStatus;
	}

	public void setWorkStatus(TaskStatus workStatus) {
		this.workStatus = workStatus;
	}

	public byte[] getLesionPlanFile() {
		return lesionPlanFile;
	}

	public void setLesionPlanFile(byte[] lesionPlanFile) {
		this.lesionPlanFile = lesionPlanFile;
	}

	public String getLesionPlanFileContentType() {
		return lesionPlanFileContentType;
	}

	public void setLesionPlanFileContentType(String lesionPlanFileContentType) {
		this.lesionPlanFileContentType = lesionPlanFileContentType;
	}

	public String getLessionPlanFileLink() {
		return lessionPlanFileLink;
	}

	public void setLessionPlanFileLink(String lessionPlanFileLink) {
		this.lessionPlanFileLink = lessionPlanFileLink;
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

	public SchoolClassDTO getSchoolClass() {
		return schoolClass;
	}

	public void setSchoolClass(SchoolClassDTO schoolClass) {
		this.schoolClass = schoolClass;
	}

	public ClassSubjectDTO getClassSubject() {
		return classSubject;
	}

	public void setClassSubject(ClassSubjectDTO classSubject) {
		this.classSubject = classSubject;
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
		if (!(o instanceof ClassLessionPlanDTO)) {
			return false;
		}

		ClassLessionPlanDTO classLessionPlanDTO = (ClassLessionPlanDTO) o;
		if (this.id == null) {
			return false;
		}
		return Objects.equals(this.id, classLessionPlanDTO.id);
	}

	@Override
	public int hashCode() {
		return Objects.hash(this.id);
	}

	// prettier-ignore
	@Override
	public String toString() {
		return "ClassLessionPlanDTO{" + "id=" + getId() + ", schoolDate='" + getSchoolDate() + "'" + ", classWorkText='"
				+ getClassWorkText() + "'" + ", homeWorkText='" + getHomeWorkText() + "'" + ", workStatus='"
				+ getWorkStatus() + "'" + ", lesionPlanFile='" + getLesionPlanFile() + "'" + ", lessionPlanFileLink='"
				+ getLessionPlanFileLink() + "'" + ", createDate='" + getCreateDate() + "'" + ", lastModified='"
				+ getLastModified() + "'" + ", cancelDate='" + getCancelDate() + "'" + ", chapterSection="
				+ getChapterSection() + ", schoolClass=" + getSchoolClass() + ", classSubject=" + getClassSubject()
				+ ", subjectChapter=" + getSubjectChapter() + "}";
	}
}
