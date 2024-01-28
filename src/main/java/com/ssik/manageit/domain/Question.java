package com.ssik.manageit.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.ssik.manageit.domain.enumeration.Difficulty;
import com.ssik.manageit.domain.enumeration.Status;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;

/**
 * A Question.
 */
@Entity
@Table(name = "question")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Question implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
	@SequenceGenerator(name = "sequenceGenerator")
	@Column(name = "id")
	private Long id;

	@Lob
	@Column(name = "question_import_file")
	private byte[] questionImportFile;

	@Column(name = "question_import_file_content_type")
	private String questionImportFileContentType;

	@Lob
	@Type(type = "org.hibernate.type.TextType")
	@Column(name = "question_text")
	private String questionText;

	@Lob
	@Column(name = "question_image")
	private byte[] questionImage;

	@Column(name = "question_image_content_type")
	private String questionImageContentType;

	@Lob
	@Column(name = "answer_image")
	private byte[] answerImage;

	@Column(name = "answer_image_content_type")
	private String answerImageContentType;

	@Column(name = "image_side_by_side")
	private Boolean imageSideBySide;

	@Column(name = "option_1")
	private String option1;

	@Column(name = "option_2")
	private String option2;

	@Column(name = "option_3")
	private String option3;

	@Column(name = "option_4")
	private String option4;

	@Column(name = "option_5")
	private String option5;

	@Enumerated(EnumType.STRING)
	@Column(name = "status")
	private Status status;

	@Column(name = "weightage")
	private Integer weightage;

	@Enumerated(EnumType.STRING)
	@Column(name = "difficulty_level")
	private Difficulty difficultyLevel;

	@Column(name = "create_date")
	private LocalDate createDate;

	@Column(name = "last_modified")
	private LocalDate lastModified;

	@Column(name = "cancel_date")
	private LocalDate cancelDate;

	@ManyToMany
	@JoinTable(name = "rel_question__tag", joinColumns = @JoinColumn(name = "question_id"), inverseJoinColumns = @JoinColumn(name = "tag_id"))
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
	@JsonIgnoreProperties(value = { "tenant", "questionPapers", "questions" }, allowSetters = true)
	private Set<Tag> tags = new HashSet<>();

	@ManyToOne(fetch = FetchType.EAGER)
	@JsonIgnoreProperties(value = { "questions", "tenant" }, allowSetters = true)
	private QuestionType questionType;

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

	@ManyToOne
	@JsonIgnoreProperties(value = { "chapterSections", "classLessionPlans", "questions",
			"classSubject" }, allowSetters = true)
	private SubjectChapter subjectChapter;

	@ManyToMany(mappedBy = "questions", fetch = FetchType.EAGER)
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
	@JsonIgnoreProperties(value = { "questions", "tags", "tenant", "schoolClass", "classSubject" }, allowSetters = true)
	private Set<QuestionPaper> questionPapers = new HashSet<>();

	// jhipster-needle-entity-add-field - JHipster will add fields here

	public Long getId() {
		return this.id;
	}

	public Question id(Long id) {
		this.setId(id);
		return this;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public byte[] getQuestionImportFile() {
		return this.questionImportFile;
	}

	public Question questionImportFile(byte[] questionImportFile) {
		this.setQuestionImportFile(questionImportFile);
		return this;
	}

	public void setQuestionImportFile(byte[] questionImportFile) {
		this.questionImportFile = questionImportFile;
	}

	public String getQuestionImportFileContentType() {
		return this.questionImportFileContentType;
	}

	public Question questionImportFileContentType(String questionImportFileContentType) {
		this.questionImportFileContentType = questionImportFileContentType;
		return this;
	}

	public void setQuestionImportFileContentType(String questionImportFileContentType) {
		this.questionImportFileContentType = questionImportFileContentType;
	}

	public String getQuestionText() {
		return this.questionText;
	}

	public Question questionText(String questionText) {
		this.setQuestionText(questionText);
		return this;
	}

	public void setQuestionText(String questionText) {
		this.questionText = questionText;
	}

	public byte[] getQuestionImage() {
		return this.questionImage;
	}

	public Question questionImage(byte[] questionImage) {
		this.setQuestionImage(questionImage);
		return this;
	}

	public void setQuestionImage(byte[] questionImage) {
		this.questionImage = questionImage;
	}

	public String getQuestionImageContentType() {
		return this.questionImageContentType;
	}

	public Question questionImageContentType(String questionImageContentType) {
		this.questionImageContentType = questionImageContentType;
		return this;
	}

	public void setQuestionImageContentType(String questionImageContentType) {
		this.questionImageContentType = questionImageContentType;
	}

	public byte[] getAnswerImage() {
		return this.answerImage;
	}

	public Question answerImage(byte[] answerImage) {
		this.setAnswerImage(answerImage);
		return this;
	}

	public void setAnswerImage(byte[] answerImage) {
		this.answerImage = answerImage;
	}

	public String getAnswerImageContentType() {
		return this.answerImageContentType;
	}

	public Question answerImageContentType(String answerImageContentType) {
		this.answerImageContentType = answerImageContentType;
		return this;
	}

	public void setAnswerImageContentType(String answerImageContentType) {
		this.answerImageContentType = answerImageContentType;
	}

	public Boolean getImageSideBySide() {
		return this.imageSideBySide;
	}

	public Question imageSideBySide(Boolean imageSideBySide) {
		this.setImageSideBySide(imageSideBySide);
		return this;
	}

	public void setImageSideBySide(Boolean imageSideBySide) {
		this.imageSideBySide = imageSideBySide;
	}

	public String getOption1() {
		return this.option1;
	}

	public Question option1(String option1) {
		this.setOption1(option1);
		return this;
	}

	public void setOption1(String option1) {
		this.option1 = option1;
	}

	public String getOption2() {
		return this.option2;
	}

	public Question option2(String option2) {
		this.setOption2(option2);
		return this;
	}

	public void setOption2(String option2) {
		this.option2 = option2;
	}

	public String getOption3() {
		return this.option3;
	}

	public Question option3(String option3) {
		this.setOption3(option3);
		return this;
	}

	public void setOption3(String option3) {
		this.option3 = option3;
	}

	public String getOption4() {
		return this.option4;
	}

	public Question option4(String option4) {
		this.setOption4(option4);
		return this;
	}

	public void setOption4(String option4) {
		this.option4 = option4;
	}

	public String getOption5() {
		return this.option5;
	}

	public Question option5(String option5) {
		this.setOption5(option5);
		return this;
	}

	public void setOption5(String option5) {
		this.option5 = option5;
	}

	public Status getStatus() {
		return this.status;
	}

	public Question status(Status status) {
		this.setStatus(status);
		return this;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public Integer getWeightage() {
		return this.weightage;
	}

	public Question weightage(Integer weightage) {
		this.setWeightage(weightage);
		return this;
	}

	public void setWeightage(Integer weightage) {
		this.weightage = weightage;
	}

	public Difficulty getDifficultyLevel() {
		return this.difficultyLevel;
	}

	public Question difficultyLevel(Difficulty difficultyLevel) {
		this.setDifficultyLevel(difficultyLevel);
		return this;
	}

	public void setDifficultyLevel(Difficulty difficultyLevel) {
		this.difficultyLevel = difficultyLevel;
	}

	public LocalDate getCreateDate() {
		return this.createDate;
	}

	public Question createDate(LocalDate createDate) {
		this.setCreateDate(createDate);
		return this;
	}

	public void setCreateDate(LocalDate createDate) {
		this.createDate = createDate;
	}

	public LocalDate getLastModified() {
		return this.lastModified;
	}

	public Question lastModified(LocalDate lastModified) {
		this.setLastModified(lastModified);
		return this;
	}

	public void setLastModified(LocalDate lastModified) {
		this.lastModified = lastModified;
	}

	public LocalDate getCancelDate() {
		return this.cancelDate;
	}

	public Question cancelDate(LocalDate cancelDate) {
		this.setCancelDate(cancelDate);
		return this;
	}

	public void setCancelDate(LocalDate cancelDate) {
		this.cancelDate = cancelDate;
	}

	public Set<Tag> getTags() {
		return this.tags;
	}

	public void setTags(Set<Tag> tags) {
		this.tags = tags;
	}

	public Question tags(Set<Tag> tags) {
		this.setTags(tags);
		return this;
	}

	public Question addTag(Tag tag) {
		this.tags.add(tag);
		tag.getQuestions().add(this);
		return this;
	}

	public Question removeTag(Tag tag) {
		this.tags.remove(tag);
		tag.getQuestions().remove(this);
		return this;
	}

	public QuestionType getQuestionType() {
		return this.questionType;
	}

	public void setQuestionType(QuestionType questionType) {
		this.questionType = questionType;
	}

	public Question questionType(QuestionType questionType) {
		this.setQuestionType(questionType);
		return this;
	}

	public Tenant getTenant() {
		return this.tenant;
	}

	public void setTenant(Tenant tenant) {
		this.tenant = tenant;
	}

	public Question tenant(Tenant tenant) {
		this.setTenant(tenant);
		return this;
	}

	public SchoolClass getSchoolClass() {
		return this.schoolClass;
	}

	public void setSchoolClass(SchoolClass schoolClass) {
		this.schoolClass = schoolClass;
	}

	public Question schoolClass(SchoolClass schoolClass) {
		this.setSchoolClass(schoolClass);
		return this;
	}

	public ClassSubject getClassSubject() {
		return this.classSubject;
	}

	public void setClassSubject(ClassSubject classSubject) {
		this.classSubject = classSubject;
	}

	public Question classSubject(ClassSubject classSubject) {
		this.setClassSubject(classSubject);
		return this;
	}

	public SubjectChapter getSubjectChapter() {
		return this.subjectChapter;
	}

	public void setSubjectChapter(SubjectChapter subjectChapter) {
		this.subjectChapter = subjectChapter;
	}

	public Question subjectChapter(SubjectChapter subjectChapter) {
		this.setSubjectChapter(subjectChapter);
		return this;
	}

	public Set<QuestionPaper> getQuestionPapers() {
		return this.questionPapers;
	}

	public void setQuestionPapers(Set<QuestionPaper> questionPapers) {
		if (this.questionPapers != null) {
			this.questionPapers.forEach(i -> i.removeQuestion(this));
		}
		if (questionPapers != null) {
			questionPapers.forEach(i -> i.addQuestion(this));
		}
		this.questionPapers = questionPapers;
	}

	public Question questionPapers(Set<QuestionPaper> questionPapers) {
		this.setQuestionPapers(questionPapers);
		return this;
	}

	public Question addQuestionPaper(QuestionPaper questionPaper) {
		this.questionPapers.add(questionPaper);
		questionPaper.getQuestions().add(this);
		return this;
	}

	public Question removeQuestionPaper(QuestionPaper questionPaper) {
		this.questionPapers.remove(questionPaper);
		questionPaper.getQuestions().remove(this);
		return this;
	}

	// jhipster-needle-entity-add-getters-setters - JHipster will add getters and
	// setters here

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof Question)) {
			return false;
		}
		return id != null && id.equals(((Question) o).id);
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
		return "Question{" + "id=" + getId() + ", questionImportFile='" + getQuestionImportFile() + "'"
				+ ", questionImportFileContentType='" + getQuestionImportFileContentType() + "'" + ", questionText='"
				+ getQuestionText() + "'" + ", questionImage='" + getQuestionImage() + "'"
				+ ", questionImageContentType='" + getQuestionImageContentType() + "'" + ", answerImage='"
				+ getAnswerImage() + "'" + ", answerImageContentType='" + getAnswerImageContentType() + "'"
				+ ", imageSideBySide='" + getImageSideBySide() + "'" + ", option1='" + getOption1() + "'"
				+ ", option2='" + getOption2() + "'" + ", option3='" + getOption3() + "'" + ", option4='" + getOption4()
				+ "'" + ", option5='" + getOption5() + "'" + ", status='" + getStatus() + "'" + ", weightage="
				+ getWeightage() + ", difficultyLevel='" + getDifficultyLevel() + "'" + ", createDate='"
				+ getCreateDate() + "'" + ", lastModified='" + getLastModified() + "'" + ", cancelDate='"
				+ getCancelDate() + "'" + "}";
	}
}
