package com.ssik.manageit.service.dto;

import com.ssik.manageit.domain.enumeration.TaskStatus;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.ssik.manageit.domain.ClassLessionPlanTrack} entity.
 */
public class ClassLessionPlanTrackDTO implements Serializable {

    private Long id;

    @NotNull
    private TaskStatus workStatus;

    private String remarks;

    private LocalDate createDate;

    private LocalDate lastModified;

    private LocalDate cancelDate;

    private ClassLessionPlanDTO classLessionPlan;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TaskStatus getWorkStatus() {
        return workStatus;
    }

    public void setWorkStatus(TaskStatus workStatus) {
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

    public ClassLessionPlanDTO getClassLessionPlan() {
        return classLessionPlan;
    }

    public void setClassLessionPlan(ClassLessionPlanDTO classLessionPlan) {
        this.classLessionPlan = classLessionPlan;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ClassLessionPlanTrackDTO)) {
            return false;
        }

        ClassLessionPlanTrackDTO classLessionPlanTrackDTO = (ClassLessionPlanTrackDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, classLessionPlanTrackDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ClassLessionPlanTrackDTO{" +
            "id=" + getId() +
            ", workStatus='" + getWorkStatus() + "'" +
            ", remarks='" + getRemarks() + "'" +
            ", createDate='" + getCreateDate() + "'" +
            ", lastModified='" + getLastModified() + "'" +
            ", cancelDate='" + getCancelDate() + "'" +
            ", classLessionPlan=" + getClassLessionPlan() +
            "}";
    }
}
