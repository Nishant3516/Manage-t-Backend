package com.ssik.manageit.service.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.ssik.manageit.domain.StudentAttendence} entity.
 */
public class StudentAttendenceDTO implements Serializable {

    private Long id;

    @NotNull
    private LocalDate schoolDate;

    @NotNull
    private Boolean attendence;

    private LocalDate createDate;

    private LocalDate lastModified;

    private LocalDate cancelDate;

    private ClassStudentDTO classStudent;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getSchoolDate() {
        return schoolDate;
    }

    public void setSchoolDate(LocalDate schoolDate) {
        this.schoolDate = schoolDate;
    }

    public Boolean getAttendence() {
        return attendence;
    }

    public void setAttendence(Boolean attendence) {
        this.attendence = attendence;
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

    public ClassStudentDTO getClassStudent() {
        return classStudent;
    }

    public void setClassStudent(ClassStudentDTO classStudent) {
        this.classStudent = classStudent;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof StudentAttendenceDTO)) {
            return false;
        }

        StudentAttendenceDTO studentAttendenceDTO = (StudentAttendenceDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, studentAttendenceDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "StudentAttendenceDTO{" +
            "id=" + getId() +
            ", schoolDate='" + getSchoolDate() + "'" +
            ", attendence='" + getAttendence() + "'" +
            ", createDate='" + getCreateDate() + "'" +
            ", lastModified='" + getLastModified() + "'" +
            ", cancelDate='" + getCancelDate() + "'" +
            ", classStudent=" + getClassStudent() +
            "}";
    }
}
