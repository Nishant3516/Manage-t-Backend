package com.ssik.manageit.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.ssik.manageit.domain.enumeration.TagLevel;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Tag.
 */
@Entity
@Table(name = "tag")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Tag implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "tag_text")
    private String tagText;

    @Enumerated(EnumType.STRING)
    @Column(name = "tag_level")
    private TagLevel tagLevel;

    @Column(name = "create_date")
    private LocalDate createDate;

    @Column(name = "last_modified")
    private LocalDate lastModified;

    @Column(name = "cancel_date")
    private LocalDate cancelDate;

    @ManyToOne
    @JsonIgnoreProperties(value = { "questions", "questionTypes", "questionPapers", "tags" }, allowSetters = true)
    private Tenant tenant;

    @ManyToMany(mappedBy = "tags")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "questions", "tags", "tenant", "schoolClass", "classSubject" }, allowSetters = true)
    private Set<QuestionPaper> questionPapers = new HashSet<>();

    @ManyToMany(mappedBy = "tags")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(
        value = { "tags", "questionType", "tenant", "schoolClass", "classSubject", "subjectChapter", "questionPapers" },
        allowSetters = true
    )
    private Set<Question> questions = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Tag id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTagText() {
        return this.tagText;
    }

    public Tag tagText(String tagText) {
        this.setTagText(tagText);
        return this;
    }

    public void setTagText(String tagText) {
        this.tagText = tagText;
    }

    public TagLevel getTagLevel() {
        return this.tagLevel;
    }

    public Tag tagLevel(TagLevel tagLevel) {
        this.setTagLevel(tagLevel);
        return this;
    }

    public void setTagLevel(TagLevel tagLevel) {
        this.tagLevel = tagLevel;
    }

    public LocalDate getCreateDate() {
        return this.createDate;
    }

    public Tag createDate(LocalDate createDate) {
        this.setCreateDate(createDate);
        return this;
    }

    public void setCreateDate(LocalDate createDate) {
        this.createDate = createDate;
    }

    public LocalDate getLastModified() {
        return this.lastModified;
    }

    public Tag lastModified(LocalDate lastModified) {
        this.setLastModified(lastModified);
        return this;
    }

    public void setLastModified(LocalDate lastModified) {
        this.lastModified = lastModified;
    }

    public LocalDate getCancelDate() {
        return this.cancelDate;
    }

    public Tag cancelDate(LocalDate cancelDate) {
        this.setCancelDate(cancelDate);
        return this;
    }

    public void setCancelDate(LocalDate cancelDate) {
        this.cancelDate = cancelDate;
    }

    public Tenant getTenant() {
        return this.tenant;
    }

    public void setTenant(Tenant tenant) {
        this.tenant = tenant;
    }

    public Tag tenant(Tenant tenant) {
        this.setTenant(tenant);
        return this;
    }

    public Set<QuestionPaper> getQuestionPapers() {
        return this.questionPapers;
    }

    public void setQuestionPapers(Set<QuestionPaper> questionPapers) {
        if (this.questionPapers != null) {
            this.questionPapers.forEach(i -> i.removeTag(this));
        }
        if (questionPapers != null) {
            questionPapers.forEach(i -> i.addTag(this));
        }
        this.questionPapers = questionPapers;
    }

    public Tag questionPapers(Set<QuestionPaper> questionPapers) {
        this.setQuestionPapers(questionPapers);
        return this;
    }

    public Tag addQuestionPaper(QuestionPaper questionPaper) {
        this.questionPapers.add(questionPaper);
        questionPaper.getTags().add(this);
        return this;
    }

    public Tag removeQuestionPaper(QuestionPaper questionPaper) {
        this.questionPapers.remove(questionPaper);
        questionPaper.getTags().remove(this);
        return this;
    }

    public Set<Question> getQuestions() {
        return this.questions;
    }

    public void setQuestions(Set<Question> questions) {
        if (this.questions != null) {
            this.questions.forEach(i -> i.removeTag(this));
        }
        if (questions != null) {
            questions.forEach(i -> i.addTag(this));
        }
        this.questions = questions;
    }

    public Tag questions(Set<Question> questions) {
        this.setQuestions(questions);
        return this;
    }

    public Tag addQuestion(Question question) {
        this.questions.add(question);
        question.getTags().add(this);
        return this;
    }

    public Tag removeQuestion(Question question) {
        this.questions.remove(question);
        question.getTags().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Tag)) {
            return false;
        }
        return id != null && id.equals(((Tag) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Tag{" +
            "id=" + getId() +
            ", tagText='" + getTagText() + "'" +
            ", tagLevel='" + getTagLevel() + "'" +
            ", createDate='" + getCreateDate() + "'" +
            ", lastModified='" + getLastModified() + "'" +
            ", cancelDate='" + getCancelDate() + "'" +
            "}";
    }
}
