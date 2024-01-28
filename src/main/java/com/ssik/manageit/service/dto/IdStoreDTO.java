package com.ssik.manageit.service.dto;

import com.ssik.manageit.domain.enumeration.IdType;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.ssik.manageit.domain.IdStore} entity.
 */
public class IdStoreDTO implements Serializable {

    private Long id;

    @NotNull
    private IdType entrytype;

    @NotNull
    private Long lastGeneratedId;

    private Long startId;

    private LocalDate createDate;

    private LocalDate lastModified;

    private LocalDate cancelDate;

    private SchoolDTO school;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public IdType getEntrytype() {
        return entrytype;
    }

    public void setEntrytype(IdType entrytype) {
        this.entrytype = entrytype;
    }

    public Long getLastGeneratedId() {
        return lastGeneratedId;
    }

    public void setLastGeneratedId(Long lastGeneratedId) {
        this.lastGeneratedId = lastGeneratedId;
    }

    public Long getStartId() {
        return startId;
    }

    public void setStartId(Long startId) {
        this.startId = startId;
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

    public SchoolDTO getSchool() {
        return school;
    }

    public void setSchool(SchoolDTO school) {
        this.school = school;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof IdStoreDTO)) {
            return false;
        }

        IdStoreDTO idStoreDTO = (IdStoreDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, idStoreDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "IdStoreDTO{" +
            "id=" + getId() +
            ", entrytype='" + getEntrytype() + "'" +
            ", lastGeneratedId=" + getLastGeneratedId() +
            ", startId=" + getStartId() +
            ", createDate='" + getCreateDate() + "'" +
            ", lastModified='" + getLastModified() + "'" +
            ", cancelDate='" + getCancelDate() + "'" +
            ", school=" + getSchool() +
            "}";
    }
}
