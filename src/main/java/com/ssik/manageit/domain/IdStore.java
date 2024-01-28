package com.ssik.manageit.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.ssik.manageit.domain.enumeration.IdType;
import java.io.Serializable;
import java.time.LocalDate;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A IdStore.
 */
@Entity
@Table(name = "id_store")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class IdStore implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "entrytype", nullable = false)
    private IdType entrytype;

    @NotNull
    @Column(name = "last_generated_id", nullable = false)
    private Long lastGeneratedId;

    @Column(name = "start_id")
    private Long startId;

    @Column(name = "create_date")
    private LocalDate createDate;

    @Column(name = "last_modified")
    private LocalDate lastModified;

    @Column(name = "cancel_date")
    private LocalDate cancelDate;

    @ManyToOne
    @JsonIgnoreProperties(value = { "schoolClasses", "schoolLedgerHeads", "idStores", "auditLogs" }, allowSetters = true)
    private School school;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public IdStore id(Long id) {
        this.id = id;
        return this;
    }

    public IdType getEntrytype() {
        return this.entrytype;
    }

    public IdStore entrytype(IdType entrytype) {
        this.entrytype = entrytype;
        return this;
    }

    public void setEntrytype(IdType entrytype) {
        this.entrytype = entrytype;
    }

    public Long getLastGeneratedId() {
        return this.lastGeneratedId;
    }

    public IdStore lastGeneratedId(Long lastGeneratedId) {
        this.lastGeneratedId = lastGeneratedId;
        return this;
    }

    public void setLastGeneratedId(Long lastGeneratedId) {
        this.lastGeneratedId = lastGeneratedId;
    }

    public Long getStartId() {
        return this.startId;
    }

    public IdStore startId(Long startId) {
        this.startId = startId;
        return this;
    }

    public void setStartId(Long startId) {
        this.startId = startId;
    }

    public LocalDate getCreateDate() {
        return this.createDate;
    }

    public IdStore createDate(LocalDate createDate) {
        this.createDate = createDate;
        return this;
    }

    public void setCreateDate(LocalDate createDate) {
        this.createDate = createDate;
    }

    public LocalDate getLastModified() {
        return this.lastModified;
    }

    public IdStore lastModified(LocalDate lastModified) {
        this.lastModified = lastModified;
        return this;
    }

    public void setLastModified(LocalDate lastModified) {
        this.lastModified = lastModified;
    }

    public LocalDate getCancelDate() {
        return this.cancelDate;
    }

    public IdStore cancelDate(LocalDate cancelDate) {
        this.cancelDate = cancelDate;
        return this;
    }

    public void setCancelDate(LocalDate cancelDate) {
        this.cancelDate = cancelDate;
    }

    public School getSchool() {
        return this.school;
    }

    public IdStore school(School school) {
        this.setSchool(school);
        return this;
    }

    public void setSchool(School school) {
        this.school = school;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof IdStore)) {
            return false;
        }
        return id != null && id.equals(((IdStore) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "IdStore{" +
            "id=" + getId() +
            ", entrytype='" + getEntrytype() + "'" +
            ", lastGeneratedId=" + getLastGeneratedId() +
            ", startId=" + getStartId() +
            ", createDate='" + getCreateDate() + "'" +
            ", lastModified='" + getLastModified() + "'" +
            ", cancelDate='" + getCancelDate() + "'" +
            "}";
    }
}
