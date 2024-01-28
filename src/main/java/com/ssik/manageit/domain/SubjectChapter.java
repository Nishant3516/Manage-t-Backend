package com.ssik.manageit.domain;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * A SubjectChapter.
 */
@Entity
@Table(name = "subject_chapter")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class SubjectChapter implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
	@SequenceGenerator(name = "sequenceGenerator")
	private Long id;

	@NotNull
	@Column(name = "chapter_name", nullable = false)
	private String chapterName;

	@Column(name = "chapter_number")
	private Integer chapterNumber;

	@Column(name = "create_date")
	private LocalDate createDate;

	@Column(name = "last_modified")
	private LocalDate lastModified;

	@Column(name = "cancel_date")
	private LocalDate cancelDate;

	@OneToMany(mappedBy = "subjectChapter")
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
	@JsonIgnoreProperties(value = { "classHomeWorks", "classClassWorks", "classLessionPlans",
			"subjectChapter" }, allowSetters = true)
	private Set<ChapterSection> chapterSections = new HashSet<>();

	@OneToMany(mappedBy = "subjectChapter")
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
	@JsonIgnoreProperties(value = { "classLessionPlanTracks", "chapterSection", "schoolClass", "classSubject",
			"subjectChapter" }, allowSetters = true)
	private Set<ClassLessionPlan> classLessionPlans = new HashSet<>();

	@ManyToOne(fetch = FetchType.EAGER)
	@JsonIgnoreProperties(value = { "subjectChapters", "classLessionPlans", "schoolClasses",
			"schoolUsers" }, allowSetters = true)
	private ClassSubject classSubject;

	// jhipster-needle-entity-add-field - JHipster will add fields here
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public SubjectChapter id(Long id) {
		this.id = id;
		return this;
	}

	public String getChapterName() {
		return this.chapterName;
	}

	public SubjectChapter chapterName(String chapterName) {
		this.chapterName = chapterName;
		return this;
	}

	public void setChapterName(String chapterName) {
		this.chapterName = chapterName;
	}

	public Integer getChapterNumber() {
		return this.chapterNumber;
	}

	public SubjectChapter chapterNumber(Integer chapterNumber) {
		this.chapterNumber = chapterNumber;
		return this;
	}

	public void setChapterNumber(Integer chapterNumber) {
		this.chapterNumber = chapterNumber;
	}

	public LocalDate getCreateDate() {
		return this.createDate;
	}

	public SubjectChapter createDate(LocalDate createDate) {
		this.createDate = createDate;
		return this;
	}

	public void setCreateDate(LocalDate createDate) {
		this.createDate = createDate;
	}

	public LocalDate getLastModified() {
		return this.lastModified;
	}

	public SubjectChapter lastModified(LocalDate lastModified) {
		this.lastModified = lastModified;
		return this;
	}

	public void setLastModified(LocalDate lastModified) {
		this.lastModified = lastModified;
	}

	public LocalDate getCancelDate() {
		return this.cancelDate;
	}

	public SubjectChapter cancelDate(LocalDate cancelDate) {
		this.cancelDate = cancelDate;
		return this;
	}

	public void setCancelDate(LocalDate cancelDate) {
		this.cancelDate = cancelDate;
	}

	public Set<ChapterSection> getChapterSections() {
		return this.chapterSections;
	}

	public SubjectChapter chapterSections(Set<ChapterSection> chapterSections) {
		this.setChapterSections(chapterSections);
		return this;
	}

	public SubjectChapter addChapterSection(ChapterSection chapterSection) {
		this.chapterSections.add(chapterSection);
		chapterSection.setSubjectChapter(this);
		return this;
	}

	public SubjectChapter removeChapterSection(ChapterSection chapterSection) {
		this.chapterSections.remove(chapterSection);
		chapterSection.setSubjectChapter(null);
		return this;
	}

	public void setChapterSections(Set<ChapterSection> chapterSections) {
		if (this.chapterSections != null) {
			this.chapterSections.forEach(i -> i.setSubjectChapter(null));
		}
		if (chapterSections != null) {
			chapterSections.forEach(i -> i.setSubjectChapter(this));
		}
		this.chapterSections = chapterSections;
	}

	public Set<ClassLessionPlan> getClassLessionPlans() {
		return this.classLessionPlans;
	}

	public SubjectChapter classLessionPlans(Set<ClassLessionPlan> classLessionPlans) {
		this.setClassLessionPlans(classLessionPlans);
		return this;
	}

	public SubjectChapter addClassLessionPlan(ClassLessionPlan classLessionPlan) {
		this.classLessionPlans.add(classLessionPlan);
		classLessionPlan.setSubjectChapter(this);
		return this;
	}

	public SubjectChapter removeClassLessionPlan(ClassLessionPlan classLessionPlan) {
		this.classLessionPlans.remove(classLessionPlan);
		classLessionPlan.setSubjectChapter(null);
		return this;
	}

	public void setClassLessionPlans(Set<ClassLessionPlan> classLessionPlans) {
		if (this.classLessionPlans != null) {
			this.classLessionPlans.forEach(i -> i.setSubjectChapter(null));
		}
		if (classLessionPlans != null) {
			classLessionPlans.forEach(i -> i.setSubjectChapter(this));
		}
		this.classLessionPlans = classLessionPlans;
	}

	public ClassSubject getClassSubject() {
		return this.classSubject;
	}

	public SubjectChapter classSubject(ClassSubject classSubject) {
		this.setClassSubject(classSubject);
		return this;
	}

	public void setClassSubject(ClassSubject classSubject) {
		this.classSubject = classSubject;
	}

	// jhipster-needle-entity-add-getters-setters - JHipster will add getters and
	// setters here

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof SubjectChapter)) {
			return false;
		}
		return id != null && id.equals(((SubjectChapter) o).id);
	}

	@Override
	public int hashCode() {
		// see
		// https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
		return getClass().hashCode();
	}

	// prettier-ignore
	@Override
	public String toString() {
		return "SubjectChapter{" + "id=" + getId() + ", chapterName='" + getChapterName() + "'" + ", chapterNumber="
				+ getChapterNumber() + ", createDate='" + getCreateDate() + "'" + ", lastModified='" + getLastModified()
				+ "'" + ", cancelDate='" + getCancelDate() + "'" + "}";
	}
}
