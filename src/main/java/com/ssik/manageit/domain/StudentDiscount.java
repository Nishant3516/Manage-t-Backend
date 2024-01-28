package com.ssik.manageit.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.ssik.manageit.domain.enumeration.FeeYear;
import java.io.Serializable;
import java.time.LocalDate;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A StudentDiscount.
 */
@Entity
@Table(name = "student_discount")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class StudentDiscount implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
	@SequenceGenerator(name = "sequenceGenerator")
	private Long id;

	@NotNull
	@Enumerated(EnumType.STRING)
	@Column(name = "fee_year", nullable = false)
	private FeeYear feeYear;

	@Column(name = "due_date")
	private Integer dueDate;

	@Column(name = "jan_fee_disc", columnDefinition = "default 0.0")
	private Double janFeeDisc;

	@Column(name = "feb_fee_disc", columnDefinition = "default 0.0")
	private Double febFeeDisc;

	@Column(name = "mar_fee_disc", columnDefinition = "default 0.0")
	private Double marFeeDisc;

	@Column(name = "apr_fee_disc", columnDefinition = "default 0.0")
	private Double aprFeeDisc;

	@Column(name = "may_fee_disc", columnDefinition = "default 0.0")
	private Double mayFeeDisc;

	@Column(name = "jun_fee_disc", columnDefinition = "default 0.0")
	private Double junFeeDisc;

	@Column(name = "jul_fee_disc", columnDefinition = "default 0.0")
	private Double julFeeDisc;

	@Column(name = "aug_fee_disc", columnDefinition = "default 0.0")
	private Double augFeeDisc;

	@Column(name = "sep_fee_disc", columnDefinition = "default 0.0")
	private Double sepFeeDisc;

	@Column(name = "oct_fee_disc", columnDefinition = "default 0.0")
	private Double octFeeDisc;

	@Column(name = "nov_fee_disc", columnDefinition = "default 0.0")
	private Double novFeeDisc;

	@Column(name = "dec_fee_disc", columnDefinition = "default 0.0")
	private Double decFeeDisc;

	@Column(name = "create_date")
	private LocalDate createDate;

	@Column(name = "last_modified")
	private LocalDate lastModified;

	@Column(name = "cancel_date")
	private LocalDate cancelDate;

	@NotNull
	@ManyToOne(fetch = FetchType.EAGER)
	@JsonIgnoreProperties(value = { "classFees", "studentDiscounts", "studentAdditionalCharges",
			"studentChargesSummaries", "school" }, allowSetters = true)
	private SchoolLedgerHead schoolLedgerHead;

	@NotNull
	@ManyToOne(fetch = FetchType.EAGER)
	@JsonIgnoreProperties(value = { "studentDiscounts", "studentAdditionalCharges", "studentChargesSummaries",
			"studentPayments", "studentAttendences", "studentHomeWorkTracks", "studentClassWorkTracks",
			"schoolClass", }, allowSetters = true)
	private ClassStudent classStudent;

	@ManyToOne
	private School school;

	@ManyToOne
	private Tenant tenant;

	public School getSchool() {
		return school;
	}

	public void setSchool(School school) {
		this.school = school;
	}

	public Tenant getTenant() {
		return tenant;
	}

	public void setTenant(Tenant tenant) {
		this.tenant = tenant;
	}

	// jhipster-needle-entity-add-field - JHipster will add fields here

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public StudentDiscount id(Long id) {
		this.id = id;
		return this;
	}

	public FeeYear getFeeYear() {
		return this.feeYear;
	}

	public StudentDiscount feeYear(FeeYear feeYear) {
		this.feeYear = feeYear;
		return this;
	}

	public void setFeeYear(FeeYear feeYear) {
		this.feeYear = feeYear;
	}

	public Integer getDueDate() {
		return this.dueDate;
	}

	public StudentDiscount dueDate(Integer dueDate) {
		this.dueDate = dueDate;
		return this;
	}

	public void setDueDate(Integer dueDate) {
		this.dueDate = dueDate;
	}

	public Double getJanFeeDisc() {
		return this.janFeeDisc;
	}

	public StudentDiscount janFeeDisc(Double janFeeDisc) {
		this.janFeeDisc = janFeeDisc;
		return this;
	}

	public void setJanFeeDisc(Double janFeeDisc) {
		this.janFeeDisc = janFeeDisc;
	}

	public Double getFebFeeDisc() {
		return this.febFeeDisc;
	}

	public StudentDiscount febFeeDisc(Double febFeeDisc) {
		this.febFeeDisc = febFeeDisc;
		return this;
	}

	public void setFebFeeDisc(Double febFeeDisc) {
		this.febFeeDisc = febFeeDisc;
	}

	public Double getMarFeeDisc() {
		return this.marFeeDisc;
	}

	public StudentDiscount marFeeDisc(Double marFeeDisc) {
		this.marFeeDisc = marFeeDisc;
		return this;
	}

	public void setMarFeeDisc(Double marFeeDisc) {
		this.marFeeDisc = marFeeDisc;
	}

	public Double getAprFeeDisc() {
		return this.aprFeeDisc;
	}

	public StudentDiscount aprFeeDisc(Double aprFeeDisc) {
		this.aprFeeDisc = aprFeeDisc;
		return this;
	}

	public void setAprFeeDisc(Double aprFeeDisc) {
		this.aprFeeDisc = aprFeeDisc;
	}

	public Double getMayFeeDisc() {
		return this.mayFeeDisc;
	}

	public StudentDiscount mayFeeDisc(Double mayFeeDisc) {
		this.mayFeeDisc = mayFeeDisc;
		return this;
	}

	public void setMayFeeDisc(Double mayFeeDisc) {
		this.mayFeeDisc = mayFeeDisc;
	}

	public Double getJunFeeDisc() {
		return this.junFeeDisc;
	}

	public StudentDiscount junFeeDisc(Double junFeeDisc) {
		this.junFeeDisc = junFeeDisc;
		return this;
	}

	public void setJunFeeDisc(Double junFeeDisc) {
		this.junFeeDisc = junFeeDisc;
	}

	public Double getJulFeeDisc() {
		return this.julFeeDisc;
	}

	public StudentDiscount julFeeDisc(Double julFeeDisc) {
		this.julFeeDisc = julFeeDisc;
		return this;
	}

	public void setJulFeeDisc(Double julFeeDisc) {
		this.julFeeDisc = julFeeDisc;
	}

	public Double getAugFeeDisc() {
		return this.augFeeDisc;
	}

	public StudentDiscount augFeeDisc(Double augFeeDisc) {
		this.augFeeDisc = augFeeDisc;
		return this;
	}

	public void setAugFeeDisc(Double augFeeDisc) {
		this.augFeeDisc = augFeeDisc;
	}

	public Double getSepFeeDisc() {
		return this.sepFeeDisc;
	}

	public StudentDiscount sepFeeDisc(Double sepFeeDisc) {
		this.sepFeeDisc = sepFeeDisc;
		return this;
	}

	public void setSepFeeDisc(Double sepFeeDisc) {
		this.sepFeeDisc = sepFeeDisc;
	}

	public Double getOctFeeDisc() {
		return this.octFeeDisc;
	}

	public StudentDiscount octFeeDisc(Double octFeeDisc) {
		this.octFeeDisc = octFeeDisc;
		return this;
	}

	public void setOctFeeDisc(Double octFeeDisc) {
		this.octFeeDisc = octFeeDisc;
	}

	public Double getNovFeeDisc() {
		return this.novFeeDisc;
	}

	public StudentDiscount novFeeDisc(Double novFeeDisc) {
		this.novFeeDisc = novFeeDisc;
		return this;
	}

	public void setNovFeeDisc(Double novFeeDisc) {
		this.novFeeDisc = novFeeDisc;
	}

	public Double getDecFeeDisc() {
		return this.decFeeDisc;
	}

	public StudentDiscount decFeeDisc(Double decFeeDisc) {
		this.decFeeDisc = decFeeDisc;
		return this;
	}

	public void setDecFeeDisc(Double decFeeDisc) {
		this.decFeeDisc = decFeeDisc;
	}

	public LocalDate getCreateDate() {
		return this.createDate;
	}

	public StudentDiscount createDate(LocalDate createDate) {
		this.createDate = createDate;
		return this;
	}

	public void setCreateDate(LocalDate createDate) {
		this.createDate = createDate;
	}

	public LocalDate getLastModified() {
		return this.lastModified;
	}

	public StudentDiscount lastModified(LocalDate lastModified) {
		this.lastModified = lastModified;
		return this;
	}

	public void setLastModified(LocalDate lastModified) {
		this.lastModified = lastModified;
	}

	public LocalDate getCancelDate() {
		return this.cancelDate;
	}

	public StudentDiscount cancelDate(LocalDate cancelDate) {
		this.cancelDate = cancelDate;
		return this;
	}

	public void setCancelDate(LocalDate cancelDate) {
		this.cancelDate = cancelDate;
	}

	public SchoolLedgerHead getSchoolLedgerHead() {
		return this.schoolLedgerHead;
	}

	public StudentDiscount schoolLedgerHead(SchoolLedgerHead schoolLedgerHead) {
		this.setSchoolLedgerHead(schoolLedgerHead);
		return this;
	}

	public void setSchoolLedgerHead(SchoolLedgerHead schoolLedgerHead) {
		this.schoolLedgerHead = schoolLedgerHead;
	}

	public ClassStudent getClassStudent() {
		return this.classStudent;
	}

	public StudentDiscount classStudent(ClassStudent classStudent) {
		this.setClassStudent(classStudent);
		return this;
	}

	public void setClassStudent(ClassStudent classStudent) {
		this.classStudent = classStudent;
	}

	// jhipster-needle-entity-add-getters-setters - JHipster will add getters and
	// setters here

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof StudentDiscount)) {
			return false;
		}
		return id != null && id.equals(((StudentDiscount) o).id);
	}

	@Override
	public int hashCode() {
		// see
		// https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
		return getClass().hashCode();
	}

	// prettier-ignore
	@Override
	public String toString() {
		return "StudentDiscount{" + "id=" + getId() + ", feeYear='" + getFeeYear() + "'" + ", dueDate=" + getDueDate()
				+ ", janFeeDisc=" + getJanFeeDisc() + ", febFeeDisc=" + getFebFeeDisc() + ", marFeeDisc="
				+ getMarFeeDisc() + ", aprFeeDisc=" + getAprFeeDisc() + ", mayFeeDisc=" + getMayFeeDisc()
				+ ", junFeeDisc=" + getJunFeeDisc() + ", julFeeDisc=" + getJulFeeDisc() + ", augFeeDisc="
				+ getAugFeeDisc() + ", sepFeeDisc=" + getSepFeeDisc() + ", octFeeDisc=" + getOctFeeDisc()
				+ ", novFeeDisc=" + getNovFeeDisc() + ", decFeeDisc=" + getDecFeeDisc() + ", createDate='"
				+ getCreateDate() + "'" + ", lastModified='" + getLastModified() + "'" + ", cancelDate='"
				+ getCancelDate() + "'" + "}";
	}
}
