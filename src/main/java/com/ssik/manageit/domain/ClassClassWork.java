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
import com.ssik.manageit.domain.enumeration.StudentAssignmentType;

/**
 * A ClassClassWork.
 */
@Entity
@Table(name = "class_class_work")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ClassClassWork implements Serializable {

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
	@Column(name = "class_work_text", length = 1000, nullable = false)
	private String classWorkText;

	@Lob
	@Column(name = "class_work_file")
	private byte[] classWorkFile;

	@Column(name = "class_work_file_content_type")
	private String classWorkFileContentType;

	@Column(name = "class_work_file_link")
	private String classWorkFileLink;

	@Column(name = "assign")
	private Boolean assign;

	@Column(name = "create_date")
	private LocalDate createDate;

	@Column(name = "last_modified")
	private LocalDate lastModified;

	@Column(name = "cancel_date")
	private LocalDate cancelDate;

	@OneToMany(mappedBy = "classClassWork")
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
	@JsonIgnoreProperties(value = { "classStudent", "classClassWork" }, allowSetters = true)
	private Set<StudentClassWorkTrack> studentClassWorkTracks = new HashSet<>();

	@ManyToOne(fetch = FetchType.EAGER)
	@JsonIgnoreProperties(value = { "classHomeWorks", "classClassWorks", "classLessionPlans",
			"subjectChapter" }, allowSetters = true)
	private ChapterSection chapterSection;

	// jhipster-needle-entity-add-field - JHipster will add fields here
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public ClassClassWork id(Long id) {
		this.id = id;
		return this;
	}

	public LocalDate getSchoolDate() {
		return this.schoolDate;
	}

	public ClassClassWork schoolDate(LocalDate schoolDate) {
		this.schoolDate = schoolDate;
		return this;
	}

	public void setSchoolDate(LocalDate schoolDate) {
		this.schoolDate = schoolDate;
	}

	public StudentAssignmentType getStudentAssignmentType() {
		return this.studentAssignmentType;
	}

	public ClassClassWork studentAssignmentType(StudentAssignmentType studentAssignmentType) {
		this.studentAssignmentType = studentAssignmentType;
		return this;
	}

	public void setStudentAssignmentType(StudentAssignmentType studentAssignmentType) {
		this.studentAssignmentType = studentAssignmentType;
	}

	public String getClassWorkText() {
		return this.classWorkText;
	}

	public ClassClassWork classWorkText(String classWorkText) {
		this.classWorkText = classWorkText;
		return this;
	}

	public void setClassWorkText(String classWorkText) {
		this.classWorkText = classWorkText;
	}

	public byte[] getClassWorkFile() {
		return this.classWorkFile;
	}

	public ClassClassWork classWorkFile(byte[] classWorkFile) {
		this.classWorkFile = classWorkFile;
		return this;
	}

	public void setClassWorkFile(byte[] classWorkFile) {
		this.classWorkFile = classWorkFile;
	}

	public String getClassWorkFileContentType() {
		return this.classWorkFileContentType;
	}

	public ClassClassWork classWorkFileContentType(String classWorkFileContentType) {
		this.classWorkFileContentType = classWorkFileContentType;
		return this;
	}

	public void setClassWorkFileContentType(String classWorkFileContentType) {
		this.classWorkFileContentType = classWorkFileContentType;
	}

	public String getClassWorkFileLink() {
		return this.classWorkFileLink;
	}

	public ClassClassWork classWorkFileLink(String classWorkFileLink) {
		this.classWorkFileLink = classWorkFileLink;
		return this;
	}

	public void setClassWorkFileLink(String classWorkFileLink) {
		this.classWorkFileLink = classWorkFileLink;
	}

	public Boolean getAssign() {
		return this.assign;
	}

	public ClassClassWork assign(Boolean assign) {
		this.assign = assign;
		return this;
	}

	public void setAssign(Boolean assign) {
		this.assign = assign;
	}

	public LocalDate getCreateDate() {
		return this.createDate;
	}

	public ClassClassWork createDate(LocalDate createDate) {
		this.createDate = createDate;
		return this;
	}

	public void setCreateDate(LocalDate createDate) {
		this.createDate = createDate;
	}

	public LocalDate getLastModified() {
		return this.lastModified;
	}

	public ClassClassWork lastModified(LocalDate lastModified) {
		this.lastModified = lastModified;
		return this;
	}

	public void setLastModified(LocalDate lastModified) {
		this.lastModified = lastModified;
	}

	public LocalDate getCancelDate() {
		return this.cancelDate;
	}

	public ClassClassWork cancelDate(LocalDate cancelDate) {
		this.cancelDate = cancelDate;
		return this;
	}

	public void setCancelDate(LocalDate cancelDate) {
		this.cancelDate = cancelDate;
	}

	public Set<StudentClassWorkTrack> getStudentClassWorkTracks() {
		return this.studentClassWorkTracks;
	}

	public ClassClassWork studentClassWorkTracks(Set<StudentClassWorkTrack> studentClassWorkTracks) {
		this.setStudentClassWorkTracks(studentClassWorkTracks);
		return this;
	}

	public ClassClassWork addStudentClassWorkTrack(StudentClassWorkTrack studentClassWorkTrack) {
		this.studentClassWorkTracks.add(studentClassWorkTrack);
		studentClassWorkTrack.setClassClassWork(this);
		return this;
	}

	public ClassClassWork removeStudentClassWorkTrack(StudentClassWorkTrack studentClassWorkTrack) {
		this.studentClassWorkTracks.remove(studentClassWorkTrack);
		studentClassWorkTrack.setClassClassWork(null);
		return this;
	}

	public void setStudentClassWorkTracks(Set<StudentClassWorkTrack> studentClassWorkTracks) {
		if (this.studentClassWorkTracks != null) {
			this.studentClassWorkTracks.forEach(i -> i.setClassClassWork(null));
		}
		if (studentClassWorkTracks != null) {
			studentClassWorkTracks.forEach(i -> i.setClassClassWork(this));
		}
		this.studentClassWorkTracks = studentClassWorkTracks;
	}

	public ChapterSection getChapterSection() {
		return this.chapterSection;
	}

	public ClassClassWork chapterSection(ChapterSection chapterSection) {
		this.setChapterSection(chapterSection);
		return this;
	}

	public void setChapterSection(ChapterSection chapterSection) {
		this.chapterSection = chapterSection;
	}

	// jhipster-needle-entity-add-getters-setters - JHipster will add getters and
	// setters here

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof ClassClassWork)) {
			return false;
		}
		return id != null && id.equals(((ClassClassWork) o).id);
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
		return "ClassClassWork{" + "id=" + getId() + ", schoolDate='" + getSchoolDate() + "'"
				+ ", studentAssignmentType='" + getStudentAssignmentType() + "'" + ", classWorkText='"
				+ getClassWorkText() + "'" + ", classWorkFile='" + getClassWorkFile() + "'"
				+ ", classWorkFileContentType='" + getClassWorkFileContentType() + "'" + ", classWorkFileLink='"
				+ getClassWorkFileLink() + "'" + ", assign='" + getAssign() + "'" + ", createDate='" + getCreateDate()
				+ "'" + ", lastModified='" + getLastModified() + "'" + ", cancelDate='" + getCancelDate() + "'" + "}";
	}
}
