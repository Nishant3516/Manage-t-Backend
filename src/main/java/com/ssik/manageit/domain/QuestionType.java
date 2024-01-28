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
 * A QuestionType.
 */
@Entity
@Table(name = "question_type")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class QuestionType implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
	@SequenceGenerator(name = "sequenceGenerator")
	@Column(name = "id")
	private Long id;

	@Column(name = "question_type")
	private String questionType;

	@Column(name = "marks")
	private Integer marks;

	@Column(name = "create_date")
	private LocalDate createDate;

	@Column(name = "last_modified")
	private LocalDate lastModified;

	@Column(name = "cancel_date")
	private LocalDate cancelDate;

	@OneToMany(mappedBy = "questionType")
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
	@JsonIgnoreProperties(value = { "tags", "questionType", "tenant", "schoolClass", "classSubject", "subjectChapter",
			"questionPapers" }, allowSetters = true)
	private Set<Question> questions = new HashSet<>();

	@ManyToOne(fetch = FetchType.EAGER)
	@JsonIgnoreProperties(value = { "questions", "questionTypes", "questionPapers", "tags" }, allowSetters = true)
	private Tenant tenant;

	// jhipster-needle-entity-add-field - JHipster will add fields here

	public Long getId() {
		return this.id;
	}

	public QuestionType id(Long id) {
		this.setId(id);
		return this;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getQuestionType() {
		return this.questionType;
	}

	public QuestionType questionType(String questionType) {
		this.setQuestionType(questionType);
		return this;
	}

	public void setQuestionType(String questionType) {
		this.questionType = questionType;
	}

	public Integer getMarks() {
		return this.marks;
	}

	public QuestionType marks(Integer marks) {
		this.setMarks(marks);
		return this;
	}

	public void setMarks(Integer marks) {
		this.marks = marks;
	}

	public LocalDate getCreateDate() {
		return this.createDate;
	}

	public QuestionType createDate(LocalDate createDate) {
		this.setCreateDate(createDate);
		return this;
	}

	public void setCreateDate(LocalDate createDate) {
		this.createDate = createDate;
	}

	public LocalDate getLastModified() {
		return this.lastModified;
	}

	public QuestionType lastModified(LocalDate lastModified) {
		this.setLastModified(lastModified);
		return this;
	}

	public void setLastModified(LocalDate lastModified) {
		this.lastModified = lastModified;
	}

	public LocalDate getCancelDate() {
		return this.cancelDate;
	}

	public QuestionType cancelDate(LocalDate cancelDate) {
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
		if (this.questions != null) {
			this.questions.forEach(i -> i.setQuestionType(null));
		}
		if (questions != null) {
			questions.forEach(i -> i.setQuestionType(this));
		}
		this.questions = questions;
	}

	public QuestionType questions(Set<Question> questions) {
		this.setQuestions(questions);
		return this;
	}

	public QuestionType addQuestion(Question question) {
		this.questions.add(question);
		question.setQuestionType(this);
		return this;
	}

	public QuestionType removeQuestion(Question question) {
		this.questions.remove(question);
		question.setQuestionType(null);
		return this;
	}

	public Tenant getTenant() {
		return this.tenant;
	}

	public void setTenant(Tenant tenant) {
		this.tenant = tenant;
	}

	public QuestionType tenant(Tenant tenant) {
		this.setTenant(tenant);
		return this;
	}

	// jhipster-needle-entity-add-getters-setters - JHipster will add getters and
	// setters here

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof QuestionType)) {
			return false;
		}
		return id != null && id.equals(((QuestionType) o).id);
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
		return "QuestionType{" + "id=" + getId() + ", questionType='" + getQuestionType() + "'" + ", marks="
				+ getMarks() + ", createDate='" + getCreateDate() + "'" + ", lastModified='" + getLastModified() + "'"
				+ ", cancelDate='" + getCancelDate() + "'" + "}";
	}
}
