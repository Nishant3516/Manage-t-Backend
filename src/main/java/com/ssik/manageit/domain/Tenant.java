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
 * A Tenant.
 */
@Entity
@Table(name = "tenant")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Tenant implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "tenant_name")
    private String tenantName;

    @Lob
    @Column(name = "tenant_logo")
    private byte[] tenantLogo;

    @Column(name = "tenant_logo_content_type")
    private String tenantLogoContentType;

    @Column(name = "create_date")
    private LocalDate createDate;

    @Column(name = "last_modified")
    private LocalDate lastModified;

    @Column(name = "cancel_date")
    private LocalDate cancelDate;

    @OneToMany(mappedBy = "tenant")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(
        value = { "tags", "questionType", "tenant", "schoolClass", "classSubject", "subjectChapter", "questionPapers" },
        allowSetters = true
    )
    private Set<Question> questions = new HashSet<>();

    @OneToMany(mappedBy = "tenant")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "questions", "tenant" }, allowSetters = true)
    private Set<QuestionType> questionTypes = new HashSet<>();

    @OneToMany(mappedBy = "tenant")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "questions", "tags", "tenant", "schoolClass", "classSubject" }, allowSetters = true)
    private Set<QuestionPaper> questionPapers = new HashSet<>();

    @OneToMany(mappedBy = "tenant")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "tenant", "questionPapers", "questions" }, allowSetters = true)
    private Set<Tag> tags = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Tenant id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTenantName() {
        return this.tenantName;
    }

    public Tenant tenantName(String tenantName) {
        this.setTenantName(tenantName);
        return this;
    }

    public void setTenantName(String tenantName) {
        this.tenantName = tenantName;
    }

    public byte[] getTenantLogo() {
        return this.tenantLogo;
    }

    public Tenant tenantLogo(byte[] tenantLogo) {
        this.setTenantLogo(tenantLogo);
        return this;
    }

    public void setTenantLogo(byte[] tenantLogo) {
        this.tenantLogo = tenantLogo;
    }

    public String getTenantLogoContentType() {
        return this.tenantLogoContentType;
    }

    public Tenant tenantLogoContentType(String tenantLogoContentType) {
        this.tenantLogoContentType = tenantLogoContentType;
        return this;
    }

    public void setTenantLogoContentType(String tenantLogoContentType) {
        this.tenantLogoContentType = tenantLogoContentType;
    }

    public LocalDate getCreateDate() {
        return this.createDate;
    }

    public Tenant createDate(LocalDate createDate) {
        this.setCreateDate(createDate);
        return this;
    }

    public void setCreateDate(LocalDate createDate) {
        this.createDate = createDate;
    }

    public LocalDate getLastModified() {
        return this.lastModified;
    }

    public Tenant lastModified(LocalDate lastModified) {
        this.setLastModified(lastModified);
        return this;
    }

    public void setLastModified(LocalDate lastModified) {
        this.lastModified = lastModified;
    }

    public LocalDate getCancelDate() {
        return this.cancelDate;
    }

    public Tenant cancelDate(LocalDate cancelDate) {
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
            this.questions.forEach(i -> i.setTenant(null));
        }
        if (questions != null) {
            questions.forEach(i -> i.setTenant(this));
        }
        this.questions = questions;
    }

    public Tenant questions(Set<Question> questions) {
        this.setQuestions(questions);
        return this;
    }

    public Tenant addQuestion(Question question) {
        this.questions.add(question);
        question.setTenant(this);
        return this;
    }

    public Tenant removeQuestion(Question question) {
        this.questions.remove(question);
        question.setTenant(null);
        return this;
    }

    public Set<QuestionType> getQuestionTypes() {
        return this.questionTypes;
    }

    public void setQuestionTypes(Set<QuestionType> questionTypes) {
        if (this.questionTypes != null) {
            this.questionTypes.forEach(i -> i.setTenant(null));
        }
        if (questionTypes != null) {
            questionTypes.forEach(i -> i.setTenant(this));
        }
        this.questionTypes = questionTypes;
    }

    public Tenant questionTypes(Set<QuestionType> questionTypes) {
        this.setQuestionTypes(questionTypes);
        return this;
    }

    public Tenant addQuestionType(QuestionType questionType) {
        this.questionTypes.add(questionType);
        questionType.setTenant(this);
        return this;
    }

    public Tenant removeQuestionType(QuestionType questionType) {
        this.questionTypes.remove(questionType);
        questionType.setTenant(null);
        return this;
    }

    public Set<QuestionPaper> getQuestionPapers() {
        return this.questionPapers;
    }

    public void setQuestionPapers(Set<QuestionPaper> questionPapers) {
        if (this.questionPapers != null) {
            this.questionPapers.forEach(i -> i.setTenant(null));
        }
        if (questionPapers != null) {
            questionPapers.forEach(i -> i.setTenant(this));
        }
        this.questionPapers = questionPapers;
    }

    public Tenant questionPapers(Set<QuestionPaper> questionPapers) {
        this.setQuestionPapers(questionPapers);
        return this;
    }

    public Tenant addQuestionPaper(QuestionPaper questionPaper) {
        this.questionPapers.add(questionPaper);
        questionPaper.setTenant(this);
        return this;
    }

    public Tenant removeQuestionPaper(QuestionPaper questionPaper) {
        this.questionPapers.remove(questionPaper);
        questionPaper.setTenant(null);
        return this;
    }

    public Set<Tag> getTags() {
        return this.tags;
    }

    public void setTags(Set<Tag> tags) {
        if (this.tags != null) {
            this.tags.forEach(i -> i.setTenant(null));
        }
        if (tags != null) {
            tags.forEach(i -> i.setTenant(this));
        }
        this.tags = tags;
    }

    public Tenant tags(Set<Tag> tags) {
        this.setTags(tags);
        return this;
    }

    public Tenant addTag(Tag tag) {
        this.tags.add(tag);
        tag.setTenant(this);
        return this;
    }

    public Tenant removeTag(Tag tag) {
        this.tags.remove(tag);
        tag.setTenant(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Tenant)) {
            return false;
        }
        return id != null && id.equals(((Tenant) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Tenant{" +
            "id=" + getId() +
            ", tenantName='" + getTenantName() + "'" +
            ", tenantLogo='" + getTenantLogo() + "'" +
            ", tenantLogoContentType='" + getTenantLogoContentType() + "'" +
            ", createDate='" + getCreateDate() + "'" +
            ", lastModified='" + getLastModified() + "'" +
            ", cancelDate='" + getCancelDate() + "'" +
            "}";
    }
}
