package com.ssik.manageit.service.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.ssik.manageit.domain.SchoolClass} entity.
 */
public class SchoolClassDTO implements Serializable {

    private Long id;

    @NotNull
    private String className;

    private String classLongName;

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

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getClassLongName() {
        return classLongName;
    }

    public void setClassLongName(String classLongName) {
        this.classLongName = classLongName;
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
        if (!(o instanceof SchoolClassDTO)) {
            return false;
        }

        SchoolClassDTO schoolClassDTO = (SchoolClassDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, schoolClassDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SchoolClassDTO{" +
            "id=" + getId() +
            ", className='" + getClassName() + "'" +
            ", classLongName='" + getClassLongName() + "'" +
            ", createDate='" + getCreateDate() + "'" +
            ", lastModified='" + getLastModified() + "'" +
            ", cancelDate='" + getCancelDate() + "'" +
            ", school=" + getSchool() +
            "}";
    }
}
