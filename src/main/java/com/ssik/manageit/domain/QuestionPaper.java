package com.ssik.manageit.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A QuestionPaper.
 */
@Entity
@Table(name = "question_paper")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class QuestionPaper implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
	@SequenceGenerator(name = "sequenceGenerator")
	@Column(name = "id")
	private Long id;

	@Lob
	@Column(name = "tenat_logo")
	private byte[] tenatLogo;

	@Column(name = "tenat_logo_content_type")
	private String tenatLogoContentType;

	@Lob
	@Column(name = "question_paper_file")
	private byte[] questionPaperFile;

	@Column(name = "question_paper_file_content_type")
	private String questionPaperFileContentType;

	@Column(name = "question_paper_name")
	private String questionPaperName;

	@Column(name = "main_title")
	private String mainTitle;

	@Column(name = "sub_title")
	private String subTitle;

	@Column(name = "left_sub_heading_1")
	private String leftSubHeading1;

	@Column(name = "left_sub_heading_2")
	private String leftSubHeading2;

	@Column(name = "right_sub_heading_1")
	private String rightSubHeading1;

	@Column(name = "right_sub_heading_2")
	private String rightSubHeading2;

	@Column(name = "instructions")
	private String instructions;

	@Column(name = "footer_text")
	private String footerText;

	@Column(name = "total_marks")
	private Integer totalMarks;

	@Column(name = "create_date")
	private LocalDate createDate;

	@Column(name = "last_modified")
	private LocalDate lastModified;

	@Column(name = "cancel_date")
	private LocalDate cancelDate;

	// @ManyToMany(fetch = FetchType.EAGER)
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "rel_question_paper__question", joinColumns = @JoinColumn(name = "question_paper_id"), inverseJoinColumns = @JoinColumn(name = "question_id"))
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
	@JsonIgnoreProperties(value = { "tags", "questionType", "tenant", "schoolClass", "classSubject", "subjectChapter",
			"questionPapers" }, allowSetters = true)
	private Set<Question> questions = new HashSet<>();

	@ManyToMany
	@JoinTable(name = "rel_question_paper__tag", joinColumns = @JoinColumn(name = "question_paper_id"), inverseJoinColumns = @JoinColumn(name = "tag_id"))
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
	@JsonIgnoreProperties(value = { "tenant", "questionPapers", "questions" }, allowSetters = true)
	private Set<Tag> tags = new HashSet<>();

	@ManyToOne
	@JsonIgnoreProperties(value = { "questions", "questionTypes", "questionPapers", "tags" }, allowSetters = true)
	private Tenant tenant;

	@ManyToOne
	@JsonIgnoreProperties(value = { "classStudents", "classLessionPlans", "questions", "questionPapers", "school",
			"schoolNotifications", "classFees", "classSubjects", "schoolUsers", "schoolDaysOffs", "schoolEvents",
			"schoolPictureGalleries", "vchoolVideoGalleries", "schoolReports", }, allowSetters = true)
	private SchoolClass schoolClass;

	@ManyToOne
	@JsonIgnoreProperties(value = { "subjectChapters", "classLessionPlans", "questions", "questionPapers",
			"schoolClasses", "schoolUsers" }, allowSetters = true)
	private ClassSubject classSubject;

	// jhipster-needle-entity-add-field - JHipster will add fields here

	public Long getId() {
		return this.id;
	}

	public QuestionPaper id(Long id) {
		this.setId(id);
		return this;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public byte[] getTenatLogo() {
		return this.tenatLogo;
	}

	public QuestionPaper tenatLogo(byte[] tenatLogo) {
		this.setTenatLogo(tenatLogo);
		return this;
	}

	public void setTenatLogo(byte[] tenatLogo) {
		this.tenatLogo = tenatLogo;
	}

	public String getTenatLogoContentType() {
		return this.tenatLogoContentType;
	}

	public QuestionPaper tenatLogoContentType(String tenatLogoContentType) {
		this.tenatLogoContentType = tenatLogoContentType;
		return this;
	}

	public void setTenatLogoContentType(String tenatLogoContentType) {
		this.tenatLogoContentType = tenatLogoContentType;
	}

	public byte[] getQuestionPaperFile() {
		return this.questionPaperFile;
	}

	public QuestionPaper questionPaperFile(byte[] questionPaperFile) {
		this.setQuestionPaperFile(questionPaperFile);
		return this;
	}

	public void setQuestionPaperFile(byte[] questionPaperFile) {
		this.questionPaperFile = questionPaperFile;
	}

	public String getQuestionPaperFileContentType() {
		return this.questionPaperFileContentType;
	}

	public QuestionPaper questionPaperFileContentType(String questionPaperFileContentType) {
		this.questionPaperFileContentType = questionPaperFileContentType;
		return this;
	}

	public void setQuestionPaperFileContentType(String questionPaperFileContentType) {
		this.questionPaperFileContentType = questionPaperFileContentType;
	}

	public String getQuestionPaperName() {
		return this.questionPaperName;
	}

	public QuestionPaper questionPaperName(String questionPaperName) {
		this.setQuestionPaperName(questionPaperName);
		return this;
	}

	public void setQuestionPaperName(String questionPaperName) {
		this.questionPaperName = questionPaperName;
	}

	public String getMainTitle() {
		return this.mainTitle;
	}

	public QuestionPaper mainTitle(String mainTitle) {
		this.setMainTitle(mainTitle);
		return this;
	}

	public void setMainTitle(String mainTitle) {
		this.mainTitle = mainTitle;
	}

	public String getSubTitle() {
		return this.subTitle;
	}

	public QuestionPaper subTitle(String subTitle) {
		this.setSubTitle(subTitle);
		return this;
	}

	public void setSubTitle(String subTitle) {
		this.subTitle = subTitle;
	}

	public String getLeftSubHeading1() {
		return this.leftSubHeading1;
	}

	public QuestionPaper leftSubHeading1(String leftSubHeading1) {
		this.setLeftSubHeading1(leftSubHeading1);
		return this;
	}

	public void setLeftSubHeading1(String leftSubHeading1) {
		this.leftSubHeading1 = leftSubHeading1;
	}

	public String getLeftSubHeading2() {
		return this.leftSubHeading2;
	}

	public QuestionPaper leftSubHeading2(String leftSubHeading2) {
		this.setLeftSubHeading2(leftSubHeading2);
		return this;
	}

	public void setLeftSubHeading2(String leftSubHeading2) {
		this.leftSubHeading2 = leftSubHeading2;
	}

	public String getRightSubHeading1() {
		return this.rightSubHeading1;
	}

	public QuestionPaper rightSubHeading1(String rightSubHeading1) {
		this.setRightSubHeading1(rightSubHeading1);
		return this;
	}

	public void setRightSubHeading1(String rightSubHeading1) {
		this.rightSubHeading1 = rightSubHeading1;
	}

	public String getRightSubHeading2() {
		return this.rightSubHeading2;
	}

	public QuestionPaper rightSubHeading2(String rightSubHeading2) {
		this.setRightSubHeading2(rightSubHeading2);
		return this;
	}

	public void setRightSubHeading2(String rightSubHeading2) {
		this.rightSubHeading2 = rightSubHeading2;
	}

	public String getInstructions() {
		return this.instructions;
	}

	public QuestionPaper instructions(String instructions) {
		this.setInstructions(instructions);
		return this;
	}

	public void setInstructions(String instructions) {
		this.instructions = instructions;
	}

	public String getFooterText() {
		return this.footerText;
	}

	public QuestionPaper footerText(String footerText) {
		this.setFooterText(footerText);
		return this;
	}

	public void setFooterText(String footerText) {
		this.footerText = footerText;
	}

	public Integer getTotalMarks() {
		return this.totalMarks;
	}

	public QuestionPaper totalMarks(Integer totalMarks) {
		this.setTotalMarks(totalMarks);
		return this;
	}

	public void setTotalMarks(Integer totalMarks) {
		this.totalMarks = totalMarks;
	}

	public LocalDate getCreateDate() {
		return this.createDate;
	}

	public QuestionPaper createDate(LocalDate createDate) {
		this.setCreateDate(createDate);
		return this;
	}

	public void setCreateDate(LocalDate createDate) {
		this.createDate = createDate;
	}

	public LocalDate getLastModified() {
		return this.lastModified;
	}

	public QuestionPaper lastModified(LocalDate lastModified) {
		this.setLastModified(lastModified);
		return this;
	}

	public void setLastModified(LocalDate lastModified) {
		this.lastModified = lastModified;
	}

	public LocalDate getCancelDate() {
		return this.cancelDate;
	}

	public QuestionPaper cancelDate(LocalDate cancelDate) {
		this.setCancelDate(cancelDate);
		return this;
	}

	public void setCancelDate(LocalDate cancelDate) {
		this.cancelDate = cancelDate;
	}

	public Set<Question> getQuestions() {
		return this.questions;
	}

	public void setQuestions(Set<Question> questions) {
		this.questions = questions;
	}

	public QuestionPaper questions(Set<Question> questions) {
		this.setQuestions(questions);
		return this;
	}

	public QuestionPaper addQuestion(Question question) {
		this.questions.add(question);
		question.getQuestionPapers().add(this);
		return this;
	}

	public QuestionPaper removeQuestion(Question question) {
		this.questions.remove(question);
		question.getQuestionPapers().remove(this);
		return this;
	}

	public Set<Tag> getTags() {
		return this.tags;
	}

	public void setTags(Set<Tag> tags) {
		this.tags = tags;
	}

	public QuestionPaper tags(Set<Tag> tags) {
		this.setTags(tags);
		return this;
	}

	public QuestionPaper addTag(Tag tag) {
		this.tags.add(tag);
		tag.getQuestionPapers().add(this);
		return this;
	}

	public QuestionPaper removeTag(Tag tag) {
		this.tags.remove(tag);
		tag.getQuestionPapers().remove(this);
		return this;
	}

	public Tenant getTenant() {
		return this.tenant;
	}

	public void setTenant(Tenant tenant) {
		this.tenant = tenant;
	}

	public QuestionPaper tenant(Tenant tenant) {
		this.setTenant(tenant);
		return this;
	}

	public SchoolClass getSchoolClass() {
		return this.schoolClass;
	}

	public void setSchoolClass(SchoolClass schoolClass) {
		this.schoolClass = schoolClass;
	}

	public QuestionPaper schoolClass(SchoolClass schoolClass) {
		this.setSchoolClass(schoolClass);
		return this;
	}

	public ClassSubject getClassSubject() {
		return this.classSubject;
	}

	public void setClassSubject(ClassSubject classSubject) {
		this.classSubject = classSubject;
	}

	public QuestionPaper classSubject(ClassSubject classSubject) {
		this.setClassSubject(classSubject);
		return this;
	}

	// jhipster-needle-entity-add-getters-setters - JHipster will add getters and
	// setters here

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof QuestionPaper)) {
			return false;
		}
		return id != null && id.equals(((QuestionPaper) o).id);
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
		return "QuestionPaper{" + "id=" + getId() + ", tenatLogo='" + getTenatLogo() + "'" + ", tenatLogoContentType='"
				+ getTenatLogoContentType() + "'" + ", questionPaperFile='" + getQuestionPaperFile() + "'"
				+ ", questionPaperFileContentType='" + getQuestionPaperFileContentType() + "'" + ", questionPaperName='"
				+ getQuestionPaperName() + "'" + ", mainTitle='" + getMainTitle() + "'" + ", subTitle='" + getSubTitle()
				+ "'" + ", leftSubHeading1='" + getLeftSubHeading1() + "'" + ", leftSubHeading2='"
				+ getLeftSubHeading2() + "'" + ", rightSubHeading1='" + getRightSubHeading1() + "'"
				+ ", rightSubHeading2='" + getRightSubHeading2() + "'" + ", instructions='" + getInstructions() + "'"
				+ ", footerText='" + getFooterText() + "'" + ", totalMarks=" + getTotalMarks() + ", createDate='"
				+ getCreateDate() + "'" + ", lastModified='" + getLastModified() + "'" + ", cancelDate='"
				+ getCancelDate() + "'" + "}";
	}
}
