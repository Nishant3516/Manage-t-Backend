package com.ssik.manageit.service.dto;

import com.ssik.manageit.domain.enumeration.UserType;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.ssik.manageit.domain.SchoolUser} entity.
 */
public class SchoolUserDTO implements Serializable {

    private Long id;

    @NotNull
    private String loginName;

    @NotNull
    private String password;

    @NotNull
    private String userEmail;

    private String extraInfo;

    private Boolean activated;

    private UserType userType;

    private LocalDate createDate;

    private LocalDate lastModified;

    private LocalDate cancelDate;

    private Set<SchoolClassDTO> schoolClasses = new HashSet<>();

    private Set<ClassSubjectDTO> classSubjects = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getExtraInfo() {
        return extraInfo;
    }

    public void setExtraInfo(String extraInfo) {
        this.extraInfo = extraInfo;
    }

    public Boolean getActivated() {
        return activated;
    }

    public void setActivated(Boolean activated) {
        this.activated = activated;
    }

    public UserType getUserType() {
        return userType;
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
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

    public Set<ClassSubjectDTO> getClassSubjects() {
        return classSubjects;
    }

    public void setClassSubjects(Set<ClassSubjectDTO> classSubjects) {
        this.classSubjects = classSubjects;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SchoolUserDTO)) {
            return false;
        }

        SchoolUserDTO schoolUserDTO = (SchoolUserDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, schoolUserDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SchoolUserDTO{" +
            "id=" + getId() +
            ", loginName='" + getLoginName() + "'" +
            ", password='" + getPassword() + "'" +
            ", userEmail='" + getUserEmail() + "'" +
            ", extraInfo='" + getExtraInfo() + "'" +
            ", activated='" + getActivated() + "'" +
            ", userType='" + getUserType() + "'" +
            ", createDate='" + getCreateDate() + "'" +
            ", lastModified='" + getLastModified() + "'" +
            ", cancelDate='" + getCancelDate() + "'" +
            ", schoolClasses=" + getSchoolClasses() +
            ", classSubjects=" + getClassSubjects() +
            "}";
    }
}
