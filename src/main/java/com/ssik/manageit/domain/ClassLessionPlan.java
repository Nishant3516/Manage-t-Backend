package com.ssik.manageit.domain;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.ssik.manageit.domain.enumeration.TaskStatus;

/**
 * A ClassLessionPlan.
 */
@Entity
@Table(name = "class_lession_plan")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ClassLessionPlan implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
	@SequenceGenerator(name = "sequenceGenerator")
	private Long id;

	@NotNull
	@Column(name = "school_date", nullable = false)
	private LocalDate schoolDate;

	@NotNull
	@Size(max = 1000)
	@Column(name = "class_work_text", length = 1000, nullable = false)
	private String classWorkText;

	@NotNull
	@Size(max = 1000)
	@Column(name = "home_work_text", length = 1000, nullable = false)
	private String homeWorkText;

	@NotNull
	@Enumerated(EnumType.STRING)
	@Column(name = "work_status", nullable = false)
	private TaskStatus workStatus;

	@Lob
	@Column(name = "lesion_plan_file")
	private byte[] lesionPlanFile;

	@Column(name = "lesion_plan_file_content_type")
	private String lesionPlanFileContentType;

	@Column(name = "lession_plan_file_link")
	private String lessionPlanFileLink;

	@Column(name = "create_date")
	private LocalDate createDate;

	@Column(name = "last_modified")
	private LocalDate lastModified;

	@Column(name = "cancel_date")
	private LocalDate cancelDate;

	@OneToMany(mappedBy = "classLessionPlan")
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
	@JsonIgnoreProperties(value = { "classLessionPlan" }, allowSetters = true)
	private Set<ClassLessionPlanTrack> classLessionPlanTracks = new HashSet<>();

	@ManyToOne(fetch = FetchType.EAGER)
	@JsonIgnoreProperties(value = { "classHomeWorks", "classClassWorks", "classLessionPlans",
			"subjectChapter" }, allowSetters = true)
	private ChapterSection chapterSection;

	@ManyToOne(fetch = FetchType.EAGER)
	@JsonIgnoreProperties(value = { "classStudents", "classLessionPlans", "school", "schoolNotifications", "classFees",
			"classSubjects", "schoolUsers", "schoolDaysOffs", "schoolEvents", "schoolPictureGalleries",
			"vchoolVideoGalleries", "schoolReports", }, allowSetters = true)
	private SchoolClass schoolClass;

	@ManyToOne(fetch = FetchType.EAGER)
	@JsonIgnoreProperties(value = { "subjectChapters", "classLessionPlans", "schoolClasses",
			"schoolUsers" }, allowSetters = true)
	private ClassSubject classSubject;

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

	public ClassLessionPlan id(Long id) {
		this.id = id;
		return this;
	}

	public LocalDate getSchoolDate() {
		return this.schoolDate;
	}

	public ClassLessionPlan schoolDate(LocalDate schoolDate) {
		this.schoolDate = schoolDate;
		return this;
	}

	public void setSchoolDate(LocalDate schoolDate) {
		this.schoolDate = schoolDate;
	}

	public String getClassWorkText() {
		return this.classWorkText;
	}

	public ClassLessionPlan classWorkText(String classWorkText) {
		this.classWorkText = classWorkText;
		return this;
	}

	public void setClassWorkText(String classWorkText) {
		this.classWorkText = classWorkText;
	}

	public String getHomeWorkText() {
		return this.homeWorkText;
	}

	public ClassLessionPlan homeWorkText(String homeWorkText) {
		this.homeWorkText = homeWorkText;
		return this;
	}

	public void setHomeWorkText(String homeWorkText) {
		this.homeWorkText = homeWorkText;
	}

	public TaskStatus getWorkStatus() {
		return this.workStatus;
	}

	public ClassLessionPlan workStatus(TaskStatus workStatus) {
		this.workStatus = workStatus;
		return this;
	}

	public void setWorkStatus(TaskStatus workStatus) {
		this.workStatus = workStatus;
	}

	public byte[] getLesionPlanFile() {
		return this.lesionPlanFile;
	}

	public ClassLessionPlan lesionPlanFile(byte[] lesionPlanFile) {
		this.lesionPlanFile = lesionPlanFile;
		return this;
	}

	public void setLesionPlanFile(byte[] lesionPlanFile) {
		this.lesionPlanFile = lesionPlanFile;
	}

	public String getLesionPlanFileContentType() {
		return this.lesionPlanFileContentType;
	}

	public ClassLessionPlan lesionPlanFileContentType(String lesionPlanFileContentType) {
		this.lesionPlanFileContentType = lesionPlanFileContentType;
		return this;
	}

	public void setLesionPlanFileContentType(String lesionPlanFileContentType) {
		this.lesionPlanFileContentType = lesionPlanFileContentType;
	}

	public String getLessionPlanFileLink() {
		return this.lessionPlanFileLink;
	}

	public ClassLessionPlan lessionPlanFileLink(String lessionPlanFileLink) {
		this.lessionPlanFileLink = lessionPlanFileLink;
		return this;
	}

	public void setLessionPlanFileLink(String lessionPlanFileLink) {
		this.lessionPlanFileLink = lessionPlanFileLink;
	}

	public LocalDate getCreateDate() {
		return this.createDate;
	}

	public ClassLessionPlan createDate(LocalDate createDate) {
		this.createDate = createDate;
		return this;
	}

	public void setCreateDate(LocalDate createDate) {
		this.createDate = createDate;
	}

	public LocalDate getLastModified() {
		return this.lastModified;
	}

	public ClassLessionPlan lastModified(LocalDate lastModified) {
		this.lastModified = lastModified;
		return this;
	}

	public void setLastModified(LocalDate lastModified) {
		this.lastModified = lastModified;
	}

	public LocalDate getCancelDate() {
		return this.cancelDate;
	}

	public ClassLessionPlan cancelDate(LocalDate cancelDate) {
		this.cancelDate = cancelDate;
		return this;
	}

	public void setCancelDate(LocalDate cancelDate) {
		this.cancelDate = cancelDate;
	}

	public Set<ClassLessionPlanTrack> getClassLessionPlanTracks() {
		return this.classLessionPlanTracks;
	}

	public ClassLessionPlan classLessionPlanTracks(Set<ClassLessionPlanTrack> classLessionPlanTracks) {
		this.setClassLessionPlanTracks(classLessionPlanTracks);
		return this;
	}

	public ClassLessionPlan addClassLessionPlanTrack(ClassLessionPlanTrack classLessionPlanTrack) {
		this.classLessionPlanTracks.add(classLessionPlanTrack);
		classLessionPlanTrack.setClassLessionPlan(this);
		return this;
	}

	public ClassLessionPlan removeClassLessionPlanTrack(ClassLessionPlanTrack classLessionPlanTrack) {
		this.classLessionPlanTracks.remove(classLessionPlanTrack);
		classLessionPlanTrack.setClassLessionPlan(null);
		return this;
	}

	public void setClassLessionPlanTracks(Set<ClassLessionPlanTrack> classLessionPlanTracks) {
		if (this.classLessionPlanTracks != null) {
			this.classLessionPlanTracks.forEach(i -> i.setClassLessionPlan(null));
		}
		if (classLessionPlanTracks != null) {
			classLessionPlanTracks.forEach(i -> i.setClassLessionPlan(this));
		}
		this.classLessionPlanTracks = classLessionPlanTracks;
	}

	public ChapterSection getChapterSection() {
		return this.chapterSection;
	}

	public ClassLessionPlan chapterSection(ChapterSection chapterSection) {
		this.setChapterSection(chapterSection);
		return this;
	}

	public void setChapterSection(ChapterSection chapterSection) {
		this.chapterSection = chapterSection;
	}

	public SchoolClass getSchoolClass() {
		return this.schoolClass;
	}

	public ClassLessionPlan schoolClass(SchoolClass schoolClass) {
		this.setSchoolClass(schoolClass);
		return this;
	}

	public void setSchoolClass(SchoolClass schoolClass) {
		this.schoolClass = schoolClass;
	}

	public ClassSubject getClassSubject() {
		return this.classSubject;
	}

	public ClassLessionPlan classSubject(ClassSubject classSubject) {
		this.setClassSubject(classSubject);
		return this;
	}

	public void setClassSubject(ClassSubject classSubject) {
		this.classSubject = classSubject;
	}

	public SubjectChapter getSubjectChapter() {
		return this.subjectChapter;
	}

	public ClassLessionPlan subjectChapter(SubjectChapter subjectChapter) {
		this.setSubjectChapter(subjectChapter);
		return this;
	}

	public void setSubjectChapter(SubjectChapter subjectChapter) {
		this.subjectChapter = subjectChapter;
	}

	// jhipster-needle-entity-add-getters-setters - JHipster will add getters and
	// setters here

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof ClassLessionPlan)) {
			return false;
		}
		return id != null && id.equals(((ClassLessionPlan) o).id);
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
		return "ClassLessionPlan{" + "id=" + getId() + ", schoolDate='" + getSchoolDate() + "'" + ", classWorkText='"
				+ getClassWorkText() + "'" + ", homeWorkText='" + getHomeWorkText() + "'" + ", workStatus='"
				+ getWorkStatus() + "'" + ", lesionPlanFile='" + getLesionPlanFile() + "'"
				+ ", lesionPlanFileContentType='" + getLesionPlanFileContentType() + "'" + ", lessionPlanFileLink='"
				+ getLessionPlanFileLink() + "'" + ", createDate='" + getCreateDate() + "'" + ", lastModified='"
				+ getLastModified() + "'" + ", cancelDate='" + getCancelDate() + "'" + "}";
	}
}
