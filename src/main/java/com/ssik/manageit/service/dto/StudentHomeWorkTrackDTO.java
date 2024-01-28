package com.ssik.manageit.service.dto;

import com.ssik.manageit.domain.enumeration.WorkStatus;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.ssik.manageit.domain.StudentHomeWorkTrack} entity.
 */
public class StudentHomeWorkTrackDTO implements Serializable {

    private Long id;

    @NotNull
    private WorkStatus workStatus;

    private String remarks;

    private LocalDate createDate;

    private LocalDate lastModified;

    private LocalDate cancelDate;

    private ClassStudentDTO classStudent;

    private ClassHomeWorkDTO classHomeWork;

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

    public ClassHomeWorkDTO getClassHomeWork() {
        return classHomeWork;
    }

    public void setClassHomeWork(ClassHomeWorkDTO classHomeWork) {
        this.classHomeWork = classHomeWork;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof StudentHomeWorkTrackDTO)) {
            return false;
        }

        StudentHomeWorkTrackDTO studentHomeWorkTrackDTO = (StudentHomeWorkTrackDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, studentHomeWorkTrackDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "StudentHomeWorkTrackDTO{" +
            "id=" + getId() +
            ", workStatus='" + getWorkStatus() + "'" +
            ", remarks='" + getRemarks() + "'" +
            ", createDate='" + getCreateDate() + "'" +
            ", lastModified='" + getLastModified() + "'" +
            ", cancelDate='" + getCancelDate() + "'" +
            ", classStudent=" + getClassStudent() +
            ", classHomeWork=" + getClassHomeWork() +
            "}";
    }
}
