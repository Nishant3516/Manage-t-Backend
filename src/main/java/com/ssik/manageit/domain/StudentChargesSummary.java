package com.ssik.manageit.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.LocalDate;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A StudentChargesSummary.
 */
@Entity
@Table(name = "student_charges_summary")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class StudentChargesSummary implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "summary_type")
    private String summaryType;

    @Column(name = "fee_year")
    private String feeYear;

    @Column(name = "due_date")
    private Integer dueDate;

    @Column(name = "apr_summary")
    private String aprSummary;

    @Column(name = "may_summary")
    private String maySummary;

    @Column(name = "jun_summary")
    private String junSummary;

    @Column(name = "jul_summary")
    private String julSummary;

    @Column(name = "aug_summary")
    private String augSummary;

    @Column(name = "sep_summary")
    private String sepSummary;

    @Column(name = "oct_summary")
    private String octSummary;

    @Column(name = "nov_summary")
    private String novSummary;

    @Column(name = "dec_summary")
    private String decSummary;

    @Column(name = "jan_summary")
    private String janSummary;

    @Column(name = "feb_summary")
    private String febSummary;

    @Column(name = "mar_summary")
    private String marSummary;

    @Column(name = "additional_info_1")
    private String additionalInfo1;

    @Column(name = "additional_info_2")
    private String additionalInfo2;

    @Column(name = "create_date")
    private LocalDate createDate;

    @Column(name = "last_modified")
    private LocalDate lastModified;

    @Column(name = "cancel_date")
    private LocalDate cancelDate;

    @ManyToOne
    @JsonIgnoreProperties(
        value = { "classFees", "studentDiscounts", "studentAdditionalCharges", "studentChargesSummaries", "school" },
        allowSetters = true
    )
    private SchoolLedgerHead schoolLedgerHead;

    @ManyToOne
    @JsonIgnoreProperties(
        value = {
            "studentDiscounts",
            "studentAdditionalCharges",
            "studentChargesSummaries",
            "studentPayments",
            "studentAttendences",
            "studentHomeWorkTracks",
            "studentClassWorkTracks",
            "schoolClass",
        },
        allowSetters = true
    )
    private ClassStudent classStudent;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public StudentChargesSummary id(Long id) {
        this.id = id;
        return this;
    }

    public String getSummaryType() {
        return this.summaryType;
    }

    public StudentChargesSummary summaryType(String summaryType) {
        this.summaryType = summaryType;
        return this;
    }

    public void setSummaryType(String summaryType) {
        this.summaryType = summaryType;
    }

    public String getFeeYear() {
        return this.feeYear;
    }

    public StudentChargesSummary feeYear(String feeYear) {
        this.feeYear = feeYear;
        return this;
    }

    public void setFeeYear(String feeYear) {
        this.feeYear = feeYear;
    }

    public Integer getDueDate() {
        return this.dueDate;
    }

    public StudentChargesSummary dueDate(Integer dueDate) {
        this.dueDate = dueDate;
        return this;
    }

    public void setDueDate(Integer dueDate) {
        this.dueDate = dueDate;
    }

    public String getAprSummary() {
        return this.aprSummary;
    }

    public StudentChargesSummary aprSummary(String aprSummary) {
        this.aprSummary = aprSummary;
        return this;
    }

    public void setAprSummary(String aprSummary) {
        this.aprSummary = aprSummary;
    }

    public String getMaySummary() {
        return this.maySummary;
    }

    public StudentChargesSummary maySummary(String maySummary) {
        this.maySummary = maySummary;
        return this;
    }

    public void setMaySummary(String maySummary) {
        this.maySummary = maySummary;
    }

    public String getJunSummary() {
        return this.junSummary;
    }

    public StudentChargesSummary junSummary(String junSummary) {
        this.junSummary = junSummary;
        return this;
    }

    public void setJunSummary(String junSummary) {
        this.junSummary = junSummary;
    }

    public String getJulSummary() {
        return this.julSummary;
    }

    public StudentChargesSummary julSummary(String julSummary) {
        this.julSummary = julSummary;
        return this;
    }

    public void setJulSummary(String julSummary) {
        this.julSummary = julSummary;
    }

    public String getAugSummary() {
        return this.augSummary;
    }

    public StudentChargesSummary augSummary(String augSummary) {
        this.augSummary = augSummary;
        return this;
    }

    public void setAugSummary(String augSummary) {
        this.augSummary = augSummary;
    }

    public String getSepSummary() {
        return this.sepSummary;
    }

    public StudentChargesSummary sepSummary(String sepSummary) {
        this.sepSummary = sepSummary;
        return this;
    }

    public void setSepSummary(String sepSummary) {
        this.sepSummary = sepSummary;
    }

    public String getOctSummary() {
        return this.octSummary;
    }

    public StudentChargesSummary octSummary(String octSummary) {
        this.octSummary = octSummary;
        return this;
    }

    public void setOctSummary(String octSummary) {
        this.octSummary = octSummary;
    }

    public String getNovSummary() {
        return this.novSummary;
    }

    public StudentChargesSummary novSummary(String novSummary) {
        this.novSummary = novSummary;
        return this;
    }

    public void setNovSummary(String novSummary) {
        this.novSummary = novSummary;
    }

    public String getDecSummary() {
        return this.decSummary;
    }

    public StudentChargesSummary decSummary(String decSummary) {
        this.decSummary = decSummary;
        return this;
    }

    public void setDecSummary(String decSummary) {
        this.decSummary = decSummary;
    }

    public String getJanSummary() {
        return this.janSummary;
    }

    public StudentChargesSummary janSummary(String janSummary) {
        this.janSummary = janSummary;
        return this;
    }

    public void setJanSummary(String janSummary) {
        this.janSummary = janSummary;
    }

    public String getFebSummary() {
        return this.febSummary;
    }

    public StudentChargesSummary febSummary(String febSummary) {
        this.febSummary = febSummary;
        return this;
    }

    public void setFebSummary(String febSummary) {
        this.febSummary = febSummary;
    }

    public String getMarSummary() {
        return this.marSummary;
    }

    public StudentChargesSummary marSummary(String marSummary) {
        this.marSummary = marSummary;
        return this;
    }

    public void setMarSummary(String marSummary) {
        this.marSummary = marSummary;
    }

    public String getAdditionalInfo1() {
        return this.additionalInfo1;
    }

    public StudentChargesSummary additionalInfo1(String additionalInfo1) {
        this.additionalInfo1 = additionalInfo1;
        return this;
    }

    public void setAdditionalInfo1(String additionalInfo1) {
        this.additionalInfo1 = additionalInfo1;
    }

    public String getAdditionalInfo2() {
        return this.additionalInfo2;
    }

    public StudentChargesSummary additionalInfo2(String additionalInfo2) {
        this.additionalInfo2 = additionalInfo2;
        return this;
    }

    public void setAdditionalInfo2(String additionalInfo2) {
        this.additionalInfo2 = additionalInfo2;
    }

    public LocalDate getCreateDate() {
        return this.createDate;
    }

    public StudentChargesSummary createDate(LocalDate createDate) {
        this.createDate = createDate;
        return this;
    }

    public void setCreateDate(LocalDate createDate) {
        this.createDate = createDate;
    }

    public LocalDate getLastModified() {
        return this.lastModified;
    }

    public StudentChargesSummary lastModified(LocalDate lastModified) {
        this.lastModified = lastModified;
        return this;
    }

    public void setLastModified(LocalDate lastModified) {
        this.lastModified = lastModified;
    }

    public LocalDate getCancelDate() {
        return this.cancelDate;
    }

    public StudentChargesSummary cancelDate(LocalDate cancelDate) {
        this.cancelDate = cancelDate;
        return this;
    }

    public void setCancelDate(LocalDate cancelDate) {
        this.cancelDate = cancelDate;
    }

    public SchoolLedgerHead getSchoolLedgerHead() {
        return this.schoolLedgerHead;
    }

    public StudentChargesSummary schoolLedgerHead(SchoolLedgerHead schoolLedgerHead) {
        this.setSchoolLedgerHead(schoolLedgerHead);
        return this;
    }

    public void setSchoolLedgerHead(SchoolLedgerHead schoolLedgerHead) {
        this.schoolLedgerHead = schoolLedgerHead;
    }

    public ClassStudent getClassStudent() {
        return this.classStudent;
    }

    public StudentChargesSummary classStudent(ClassStudent classStudent) {
        this.setClassStudent(classStudent);
        return this;
    }

    public void setClassStudent(ClassStudent classStudent) {
        this.classStudent = classStudent;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof StudentChargesSummary)) {
            return false;
        }
        return id != null && id.equals(((StudentChargesSummary) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "StudentChargesSummary{" +
            "id=" + getId() +
            ", summaryType='" + getSummaryType() + "'" +
            ", feeYear='" + getFeeYear() + "'" +
            ", dueDate=" + getDueDate() +
            ", aprSummary='" + getAprSummary() + "'" +
            ", maySummary='" + getMaySummary() + "'" +
            ", junSummary='" + getJunSummary() + "'" +
            ", julSummary='" + getJulSummary() + "'" +
            ", augSummary='" + getAugSummary() + "'" +
            ", sepSummary='" + getSepSummary() + "'" +
            ", octSummary='" + getOctSummary() + "'" +
            ", novSummary='" + getNovSummary() + "'" +
            ", decSummary='" + getDecSummary() + "'" +
            ", janSummary='" + getJanSummary() + "'" +
            ", febSummary='" + getFebSummary() + "'" +
            ", marSummary='" + getMarSummary() + "'" +
            ", additionalInfo1='" + getAdditionalInfo1() + "'" +
            ", additionalInfo2='" + getAdditionalInfo2() + "'" +
            ", createDate='" + getCreateDate() + "'" +
            ", lastModified='" + getLastModified() + "'" +
            ", cancelDate='" + getCancelDate() + "'" +
            "}";
    }
}
