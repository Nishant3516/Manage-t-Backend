package com.ssik.manageit.service.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import javax.persistence.Lob;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.ssik.manageit.domain.SchoolNotifications} entity.
 */
public class SchoolNotificationsDTO implements Serializable {

    private Long id;

    @NotNull
    private String notificationTitle;

    @NotNull
    private String notificationDetails;

    @Lob
    private byte[] notificationFile;

    private String notificationFileContentType;
    private String notificationFileLink;

    private LocalDate showTillDate;

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

    public String getNotificationTitle() {
        return notificationTitle;
    }

    public void setNotificationTitle(String notificationTitle) {
        this.notificationTitle = notificationTitle;
    }

    public String getNotificationDetails() {
        return notificationDetails;
    }

    public void setNotificationDetails(String notificationDetails) {
        this.notificationDetails = notificationDetails;
    }

    public byte[] getNotificationFile() {
        return notificationFile;
    }

    public void setNotificationFile(byte[] notificationFile) {
        this.notificationFile = notificationFile;
    }

    public String getNotificationFileContentType() {
        return notificationFileContentType;
    }

    public void setNotificationFileContentType(String notificationFileContentType) {
        this.notificationFileContentType = notificationFileContentType;
    }

    public String getNotificationFileLink() {
        return notificationFileLink;
    }

    public void setNotificationFileLink(String notificationFileLink) {
        this.notificationFileLink = notificationFileLink;
    }

    public LocalDate getShowTillDate() {
        return showTillDate;
    }

    public void setShowTillDate(LocalDate showTillDate) {
        this.showTillDate = showTillDate;
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
        if (!(o instanceof SchoolNotificationsDTO)) {
            return false;
        }

        SchoolNotificationsDTO schoolNotificationsDTO = (SchoolNotificationsDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, schoolNotificationsDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SchoolNotificationsDTO{" +
            "id=" + getId() +
            ", notificationTitle='" + getNotificationTitle() + "'" +
            ", notificationDetails='" + getNotificationDetails() + "'" +
            ", notificationFile='" + getNotificationFile() + "'" +
            ", notificationFileLink='" + getNotificationFileLink() + "'" +
            ", showTillDate='" + getShowTillDate() + "'" +
            ", createDate='" + getCreateDate() + "'" +
            ", lastModified='" + getLastModified() + "'" +
            ", cancelDate='" + getCancelDate() + "'" +
            ", schoolClasses=" + getSchoolClasses() +
            "}";
    }
}
