package com.ssik.manageit.service.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.ssik.manageit.domain.AuditLog} entity.
 */
public class AuditLogDTO implements Serializable {

    private Long id;

    @NotNull
    private String userName;

    @NotNull
    private String userDeviceDetails;

    private String action;

    private String data1;

    private String data2;

    private String data3;

    private LocalDate createDate;

    private LocalDate lastModified;

    private LocalDate cancelDate;

    private SchoolDTO school;

    private SchoolUserDTO schoolUser;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserDeviceDetails() {
        return userDeviceDetails;
    }

    public void setUserDeviceDetails(String userDeviceDetails) {
        this.userDeviceDetails = userDeviceDetails;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getData1() {
        return data1;
    }

    public void setData1(String data1) {
        this.data1 = data1;
    }

    public String getData2() {
        return data2;
    }

    public void setData2(String data2) {
        this.data2 = data2;
    }

    public String getData3() {
        return data3;
    }

    public void setData3(String data3) {
        this.data3 = data3;
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

    public SchoolUserDTO getSchoolUser() {
        return schoolUser;
    }

    public void setSchoolUser(SchoolUserDTO schoolUser) {
        this.schoolUser = schoolUser;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AuditLogDTO)) {
            return false;
        }

        AuditLogDTO auditLogDTO = (AuditLogDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, auditLogDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AuditLogDTO{" +
            "id=" + getId() +
            ", userName='" + getUserName() + "'" +
            ", userDeviceDetails='" + getUserDeviceDetails() + "'" +
            ", action='" + getAction() + "'" +
            ", data1='" + getData1() + "'" +
            ", data2='" + getData2() + "'" +
            ", data3='" + getData3() + "'" +
            ", createDate='" + getCreateDate() + "'" +
            ", lastModified='" + getLastModified() + "'" +
            ", cancelDate='" + getCancelDate() + "'" +
            ", school=" + getSchool() +
            ", schoolUser=" + getSchoolUser() +
            "}";
    }
}
