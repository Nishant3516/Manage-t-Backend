package com.ssik.manageit.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.ssik.manageit.domain.enumeration.FeeYear;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A ClassFee.
 */
@Entity
@Table(name = "class_fee")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ClassFee implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "fee_year", nullable = false)
    private FeeYear feeYear;

    @NotNull
    @Column(name = "due_date", nullable = false)
    private Integer dueDate;

    @Column(name = "jan_fee", columnDefinition = "default 0.0")
    private Double janFee;

    @Column(name = "feb_fee", columnDefinition = "default 0.0")
    private Double febFee;

    @Column(name = "mar_fee", columnDefinition = "default 0.0")
    private Double marFee;

    @Column(name = "apr_fee", columnDefinition = "default 0.0")
    private Double aprFee;

    @Column(name = "may_fee", columnDefinition = "default 0.0")
    private Double mayFee;

    @Column(name = "jun_fee", columnDefinition = "default 0.0")
    private Double junFee;

    @Column(name = "jul_fee", columnDefinition = "default 0.0")
    private Double julFee;

    @Column(name = "aug_fee", columnDefinition = "default 0.0")
    private Double augFee;

    @Column(name = "sep_fee", columnDefinition = "default 0.0")
    private Double sepFee;

    @Column(name = "oct_fee", columnDefinition = "default 0.0")
    private Double octFee;

    @Column(name = "nov_fee", columnDefinition = "default 0.0")
    private Double novFee;

    @Column(name = "dec_fee", columnDefinition = "default 0.0")
    private Double decFee;

    @Column(name = "pay_by_date")
    private LocalDate payByDate;

    @Column(name = "create_date")
    private LocalDate createDate;

    @Column(name = "last_modified")
    private LocalDate lastModified;

    @Column(name = "cancel_date")
    private LocalDate cancelDate;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JoinTable(
        name = "rel_class_fee__school_class",
        joinColumns = @JoinColumn(name = "class_fee_id"),
        inverseJoinColumns = @JoinColumn(name = "school_class_id")
    )
    @JsonIgnoreProperties(
        value = {
            "classStudents",
            "classLessionPlans",
            "school",
            "schoolNotifications",
            "classFees",
            "classSubjects",
            "schoolUsers",
            "schoolDaysOffs",
            "schoolEvents",
            "schoolPictureGalleries",
            "vchoolVideoGalleries",
            "schoolReports",
        },
        allowSetters = true
    )
    private Set<SchoolClass> schoolClasses = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(
        value = { "classFees", "studentDiscounts", "studentAdditionalCharges", "studentChargesSummaries", "school" },
        allowSetters = true
    )
    private SchoolLedgerHead schoolLedgerHead;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ClassFee id(Long id) {
        this.id = id;
        return this;
    }

    public FeeYear getFeeYear() {
        return this.feeYear;
    }

    public ClassFee feeYear(FeeYear feeYear) {
        this.feeYear = feeYear;
        return this;
    }

    public void setFeeYear(FeeYear feeYear) {
        this.feeYear = feeYear;
    }

    public Integer getDueDate() {
        return this.dueDate;
    }

    public ClassFee dueDate(Integer dueDate) {
        this.dueDate = dueDate;
        return this;
    }

    public void setDueDate(Integer dueDate) {
        this.dueDate = dueDate;
    }

    public Double getJanFee() {
        return this.janFee;
    }

    public ClassFee janFee(Double janFee) {
        this.janFee = janFee;
        return this;
    }

    public void setJanFee(Double janFee) {
        this.janFee = janFee;
    }

    public Double getFebFee() {
        return this.febFee;
    }

    public ClassFee febFee(Double febFee) {
        this.febFee = febFee;
        return this;
    }

    public void setFebFee(Double febFee) {
        this.febFee = febFee;
    }

    public Double getMarFee() {
        return this.marFee;
    }

    public ClassFee marFee(Double marFee) {
        this.marFee = marFee;
        return this;
    }

    public void setMarFee(Double marFee) {
        this.marFee = marFee;
    }

    public Double getAprFee() {
        return this.aprFee;
    }

    public ClassFee aprFee(Double aprFee) {
        this.aprFee = aprFee;
        return this;
    }

    public void setAprFee(Double aprFee) {
        this.aprFee = aprFee;
    }

    public Double getMayFee() {
        return this.mayFee;
    }

    public ClassFee mayFee(Double mayFee) {
        this.mayFee = mayFee;
        return this;
    }

    public void setMayFee(Double mayFee) {
        this.mayFee = mayFee;
    }

    public Double getJunFee() {
        return this.junFee;
    }

    public ClassFee junFee(Double junFee) {
        this.junFee = junFee;
        return this;
    }

    public void setJunFee(Double junFee) {
        this.junFee = junFee;
    }

    public Double getJulFee() {
        return this.julFee;
    }

    public ClassFee julFee(Double julFee) {
        this.julFee = julFee;
        return this;
    }

    public void setJulFee(Double julFee) {
        this.julFee = julFee;
    }

    public Double getAugFee() {
        return this.augFee;
    }

    public ClassFee augFee(Double augFee) {
        this.augFee = augFee;
        return this;
    }

    public void setAugFee(Double augFee) {
        this.augFee = augFee;
    }

    public Double getSepFee() {
        return this.sepFee;
    }

    public ClassFee sepFee(Double sepFee) {
        this.sepFee = sepFee;
        return this;
    }

    public void setSepFee(Double sepFee) {
        this.sepFee = sepFee;
    }

    public Double getOctFee() {
        return this.octFee;
    }

    public ClassFee octFee(Double octFee) {
        this.octFee = octFee;
        return this;
    }

    public void setOctFee(Double octFee) {
        this.octFee = octFee;
    }

    public Double getNovFee() {
        return this.novFee;
    }

    public ClassFee novFee(Double novFee) {
        this.novFee = novFee;
        return this;
    }

    public void setNovFee(Double novFee) {
        this.novFee = novFee;
    }

    public Double getDecFee() {
        return this.decFee;
    }

    public ClassFee decFee(Double decFee) {
        this.decFee = decFee;
        return this;
    }

    public void setDecFee(Double decFee) {
        this.decFee = decFee;
    }

    public LocalDate getPayByDate() {
        return this.payByDate;
    }

    public ClassFee payByDate(LocalDate payByDate) {
        this.payByDate = payByDate;
        return this;
    }

    public void setPayByDate(LocalDate payByDate) {
        this.payByDate = payByDate;
    }

    public LocalDate getCreateDate() {
        return this.createDate;
    }

    public ClassFee createDate(LocalDate createDate) {
        this.createDate = createDate;
        return this;
    }

    public void setCreateDate(LocalDate createDate) {
        this.createDate = createDate;
    }

    public LocalDate getLastModified() {
        return this.lastModified;
    }

    public ClassFee lastModified(LocalDate lastModified) {
        this.lastModified = lastModified;
        return this;
    }

    public void setLastModified(LocalDate lastModified) {
        this.lastModified = lastModified;
    }

    public LocalDate getCancelDate() {
        return this.cancelDate;
    }

    public ClassFee cancelDate(LocalDate cancelDate) {
        this.cancelDate = cancelDate;
        return this;
    }

    public void setCancelDate(LocalDate cancelDate) {
        this.cancelDate = cancelDate;
    }

    public Set<SchoolClass> getSchoolClasses() {
        return this.schoolClasses;
    }

    public ClassFee schoolClasses(Set<SchoolClass> schoolClasses) {
        this.setSchoolClasses(schoolClasses);
        return this;
    }

    public ClassFee addSchoolClass(SchoolClass schoolClass) {
        this.schoolClasses.add(schoolClass);
        schoolClass.getClassFees().add(this);
        return this;
    }

    public ClassFee removeSchoolClass(SchoolClass schoolClass) {
        this.schoolClasses.remove(schoolClass);
        schoolClass.getClassFees().remove(this);
        return this;
    }

    public void setSchoolClasses(Set<SchoolClass> schoolClasses) {
        this.schoolClasses = schoolClasses;
    }

    public SchoolLedgerHead getSchoolLedgerHead() {
        return this.schoolLedgerHead;
    }

    public ClassFee schoolLedgerHead(SchoolLedgerHead schoolLedgerHead) {
        this.setSchoolLedgerHead(schoolLedgerHead);
        return this;
    }

    public void setSchoolLedgerHead(SchoolLedgerHead schoolLedgerHead) {
        this.schoolLedgerHead = schoolLedgerHead;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ClassFee)) {
            return false;
        }
        return id != null && id.equals(((ClassFee) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ClassFee{" +
            "id=" + getId() +
            ", feeYear='" + getFeeYear() + "'" +
            ", dueDate=" + getDueDate() +
            ", janFee=" + getJanFee() +
            ", febFee=" + getFebFee() +
            ", marFee=" + getMarFee() +
            ", aprFee=" + getAprFee() +
            ", mayFee=" + getMayFee() +
            ", junFee=" + getJunFee() +
            ", julFee=" + getJulFee() +
            ", augFee=" + getAugFee() +
            ", sepFee=" + getSepFee() +
            ", octFee=" + getOctFee() +
            ", novFee=" + getNovFee() +
            ", decFee=" + getDecFee() +
            ", payByDate='" + getPayByDate() + "'" +
            ", createDate='" + getCreateDate() + "'" +
            ", lastModified='" + getLastModified() + "'" +
            ", cancelDate='" + getCancelDate() + "'" +
            "}";
    }
}
