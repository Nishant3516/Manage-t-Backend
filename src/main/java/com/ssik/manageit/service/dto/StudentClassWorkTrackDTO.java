package com.ssik.manageit.service.dto;

import com.ssik.manageit.domain.enumeration.WorkStatus;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.ssik.manageit.domain.StudentClassWorkTrack} entity.
 */
public class StudentClassWorkTrackDTO implements Serializable {

    private Long id;

    @NotNull
    private WorkStatus workStatus;

    private String remarks;

    private LocalDate createDate;

    private LocalDate lastModified;

    private LocalDate cancelDate;

    private ClassStudentDTO classStudent;

    private ClassClassWorkDTO classClassWork;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public WorkStatus getWorkStatus() {
        return workStatus;
    }

    public void setWorkStatus(WorkStatus workStatus) {
        this.workStatus = workStatus;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
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

    public ClassClassWorkDTO getClassClassWork() {
        return classClassWork;
    }

    public void setClassClassWork(ClassClassWorkDTO classClassWork) {
        this.classClassWork = classClassWork;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof StudentClassWorkTrackDTO)) {
            return false;
        }

        StudentClassWorkTrackDTO studentClassWorkTrackDTO = (StudentClassWorkTrackDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, studentClassWorkTrackDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "StudentClassWorkTrackDTO{" +
            "id=" + getId() +
            ", workStatus='" + getWorkStatus() + "'" +
            ", remarks='" + getRemarks() + "'" +
            ", createDate='" + getCreateDate() + "'" +
            ", lastModified='" + getLastModified() + "'" +
            ", cancelDate='" + getCancelDate() + "'" +
            ", classStudent=" + getClassStudent() +
            ", classClassWork=" + getClassClassWork() +
            "}";
    }
}
