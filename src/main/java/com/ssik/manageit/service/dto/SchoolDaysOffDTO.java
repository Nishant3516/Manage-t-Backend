package com.ssik.manageit.service.dto;

import com.ssik.manageit.domain.enumeration.DayOffType;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.ssik.manageit.domain.SchoolDaysOff} entity.
 */
public class SchoolDaysOffDTO implements Serializable {

    private Long id;

    @NotNull
    private DayOffType dayOffType;

    @NotNull
    private String dayOffName;

    private String dayOffDetails;

    @NotNull
    private LocalDate startDate;

    @NotNull
    private LocalDate endDate;

    private LocalDate createDate;

    private LocalDate lastModified;

    private LocalDate cancelDate;

    private Set<SchoolClassDTO> schoolClasses = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public DayOffType getDayOffType() {
        return dayOffType;
    }

    public void setDayOffType(DayOffType dayOffType) {
        this.dayOffType = dayOffType;
    }

    public String getDayOffName() {
        return dayOffName;
    }

    public void setDayOffName(String dayOffName) {
        this.dayOffName = dayOffName;
    }

    public String getDayOffDetails() {
        return dayOffDetails;
    }

    public void setDayOffDetails(String dayOffDetails) {
        this.dayOffDetails = dayOffDetails;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
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

    public Set<SchoolClassDTO> getSchoolClasses() {
        return schoolClasses;
    }

    public void setSchoolClasses(Set<SchoolClassDTO> schoolClasses) {
        this.schoolClasses = schoolClasses;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SchoolDaysOffDTO)) {
            return false;
        }

        SchoolDaysOffDTO schoolDaysOffDTO = (SchoolDaysOffDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, schoolDaysOffDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SchoolDaysOffDTO{" +
            "id=" + getId() +
            ", dayOffType='" + getDayOffType() + "'" +
            ", dayOffName='" + getDayOffName() + "'" +
            ", dayOffDetails='" + getDayOffDetails() + "'" +
            ", startDate='" + getStartDate() + "'" +
            ", endDate='" + getEndDate() + "'" +
            ", createDate='" + getCreateDate() + "'" +
            ", lastModified='" + getLastModified() + "'" +
            ", cancelDate='" + getCancelDate() + "'" +
            ", schoolClasses=" + getSchoolClasses() +
            "}";
    }
}
