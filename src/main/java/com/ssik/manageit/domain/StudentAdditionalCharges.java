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
 * A StudentAdditionalCharges.
 */
@Entity
@Table(name = "student_additional_charges")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class StudentAdditionalCharges implements Serializable {

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

	@Column(name = "jan_add_chrg", columnDefinition = "default 0.0")
	private Double janAddChrg;

	@Column(name = "feb_add_chrgc", columnDefinition = "default 0.0")
	private Double febAddChrgc;

	@Column(name = "mar_add_chrg", columnDefinition = "default 0.0")
	private Double marAddChrg;

	@Column(name = "apr_add_chrg", columnDefinition = "default 0.0")
	private Double aprAddChrg;

	@Column(name = "may_add_chrg", columnDefinition = "default 0.0")
	private Double mayAddChrg;

	@Column(name = "jun_add_chrg", columnDefinition = "default 0.0")
	private Double junAddChrg;

	@Column(name = "jul_add_chrg", columnDefinition = "default 0.0")
	private Double julAddChrg;

	@Column(name = "aug_add_chrg", columnDefinition = "default 0.0")
	private Double augAddChrg;

	@Column(name = "sep_add_chrg", columnDefinition = "default 0.0")
	private Double sepAddChrg;

	@Column(name = "oct_add_chrg", columnDefinition = "default 0.0")
	private Double octAddChrg;

	@Column(name = "nov_add_chrg", columnDefinition = "default 0.0")
	private Double novAddChrg;

	@Column(name = "dec_add_chrg", columnDefinition = "default 0.0")
	private Double decAddChrg;

	@Column(name = "create_date")
	private LocalDate createDate;

	@Column(name = "last_modified")
	private LocalDate lastModified;

	@Column(name = "cancel_date")
	private LocalDate cancelDate;
	@ManyToOne
	private School school;

	@ManyToOne
	private Tenant tenant;

	@ManyToOne
	@JsonIgnoreProperties(value = { "classFees", "studentDiscounts", "studentAdditionalCharges",
			"studentChargesSummaries", "school" }, allowSetters = true)
	private SchoolLedgerHead schoolLedgerHead;

	@ManyToOne
	@JsonIgnoreProperties(value = { "studentDiscounts", "studentAdditionalCharges", "studentChargesSummaries",
			"studentPayments", "studentAttendences", "studentHomeWorkTracks", "studentClassWorkTracks",
			"schoolClass", }, allowSetters = true)
	private ClassStudent classStudent;

	// jhipster-needle-entity-add-field - JHipster will add fields here
	public StudentAdditionalCharges() {
		// TODO Auto-generated constructor stub
	}

	public StudentAdditionalCharges(FeeYear feeYear, Integer dueDate) {
		this.feeYear = feeYear;
		this.dueDate = dueDate;
	}

	public Long getId() {
		return id;
	}

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

	public void setId(Long id) {
		this.id = id;
	}

	public StudentAdditionalCharges id(Long id) {
		this.id = id;
		return this;
	}

	public FeeYear getFeeYear() {
		return this.feeYear;
	}

	public StudentAdditionalCharges feeYear(FeeYear feeYear) {
		this.feeYear = feeYear;
		return this;
	}

	public void setFeeYear(FeeYear feeYear) {
		this.feeYear = feeYear;
	}

	public Integer getDueDate() {
		return this.dueDate;
	}

	public StudentAdditionalCharges dueDate(Integer dueDate) {
		this.dueDate = dueDate;
		return this;
	}

	public void setDueDate(Integer dueDate) {
		this.dueDate = dueDate;
	}

	public Double getJanAddChrg() {
		return this.janAddChrg;
	}

	public StudentAdditionalCharges janAddChrg(Double janAddChrg) {
		this.janAddChrg = janAddChrg;
		return this;
	}

	public void setJanAddChrg(Double janAddChrg) {
		this.janAddChrg = janAddChrg;
	}

	public Double getFebAddChrgc() {
		return this.febAddChrgc;
	}

	public StudentAdditionalCharges febAddChrgc(Double febAddChrgc) {
		this.febAddChrgc = febAddChrgc;
		return this;
	}

	public void setFebAddChrgc(Double febAddChrgc) {
		this.febAddChrgc = febAddChrgc;
	}

	public Double getMarAddChrg() {
		return this.marAddChrg;
	}

	public StudentAdditionalCharges marAddChrg(Double marAddChrg) {
		this.marAddChrg = marAddChrg;
		return this;
	}

	public void setMarAddChrg(Double marAddChrg) {
		this.marAddChrg = marAddChrg;
	}

	public Double getAprAddChrg() {
		return this.aprAddChrg;
	}

	public StudentAdditionalCharges aprAddChrg(Double aprAddChrg) {
		this.aprAddChrg = aprAddChrg;
		return this;
	}

	public void setAprAddChrg(Double aprAddChrg) {
		this.aprAddChrg = aprAddChrg;
	}

	public Double getMayAddChrg() {
		return this.mayAddChrg;
	}

	public StudentAdditionalCharges mayAddChrg(Double mayAddChrg) {
		this.mayAddChrg = mayAddChrg;
		return this;
	}

	public void setMayAddChrg(Double mayAddChrg) {
		this.mayAddChrg = mayAddChrg;
	}

	public Double getJunAddChrg() {
		return this.junAddChrg;
	}

	public StudentAdditionalCharges junAddChrg(Double junAddChrg) {
		this.junAddChrg = junAddChrg;
		return this;
	}

	public void setJunAddChrg(Double junAddChrg) {
		this.junAddChrg = junAddChrg;
	}

	public Double getJulAddChrg() {
		return this.julAddChrg;
	}

	public StudentAdditionalCharges julAddChrg(Double julAddChrg) {
		this.julAddChrg = julAddChrg;
		return this;
	}

	public void setJulAddChrg(Double julAddChrg) {
		this.julAddChrg = julAddChrg;
	}

	public Double getAugAddChrg() {
		return this.augAddChrg;
	}

	public StudentAdditionalCharges augAddChrg(Double augAddChrg) {
		this.augAddChrg = augAddChrg;
		return this;
	}

	public void setAugAddChrg(Double augAddChrg) {
		this.augAddChrg = augAddChrg;
	}

	public Double getSepAddChrg() {
		return this.sepAddChrg;
	}

	public StudentAdditionalCharges sepAddChrg(Double sepAddChrg) {
		this.sepAddChrg = sepAddChrg;
		return this;
	}

	public void setSepAddChrg(Double sepAddChrg) {
		this.sepAddChrg = sepAddChrg;
	}

	public Double getOctAddChrg() {
		return this.octAddChrg;
	}

	public StudentAdditionalCharges octAddChrg(Double octAddChrg) {
		this.octAddChrg = octAddChrg;
		return this;
	}

	public void setOctAddChrg(Double octAddChrg) {
		this.octAddChrg = octAddChrg;
	}

	public Double getNovAddChrg() {
		return this.novAddChrg;
	}

	public StudentAdditionalCharges novAddChrg(Double novAddChrg) {
		this.novAddChrg = novAddChrg;
		return this;
	}

	public void setNovAddChrg(Double novAddChrg) {
		this.novAddChrg = novAddChrg;
	}

	public Double getDecAddChrg() {
		return this.decAddChrg;
	}

	public StudentAdditionalCharges decAddChrg(Double decAddChrg) {
		this.decAddChrg = decAddChrg;
		return this;
	}

	public void setDecAddChrg(Double decAddChrg) {
		this.decAddChrg = decAddChrg;
	}

	public LocalDate getCreateDate() {
		return this.createDate;
	}

	public StudentAdditionalCharges createDate(LocalDate createDate) {
		this.createDate = createDate;
		return this;
	}

	public void setCreateDate(LocalDate createDate) {
		this.createDate = createDate;
	}

	public LocalDate getLastModified() {
		return this.lastModified;
	}

	public StudentAdditionalCharges lastModified(LocalDate lastModified) {
		this.lastModified = lastModified;
		return this;
	}

	public void setLastModified(LocalDate lastModified) {
		this.lastModified = lastModified;
	}

	public LocalDate getCancelDate() {
		return this.cancelDate;
	}

	public StudentAdditionalCharges cancelDate(LocalDate cancelDate) {
		this.cancelDate = cancelDate;
		return this;
	}

	public void setCancelDate(LocalDate cancelDate) {
		this.cancelDate = cancelDate;
	}

	public SchoolLedgerHead getSchoolLedgerHead() {
		return this.schoolLedgerHead;
	}

	public StudentAdditionalCharges schoolLedgerHead(SchoolLedgerHead schoolLedgerHead) {
		this.setSchoolLedgerHead(schoolLedgerHead);
		return this;
	}

	public void setSchoolLedgerHead(SchoolLedgerHead schoolLedgerHead) {
		this.schoolLedgerHead = schoolLedgerHead;
	}

	public ClassStudent getClassStudent() {
		return this.classStudent;
	}

	public StudentAdditionalCharges classStudent(ClassStudent classStudent) {
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
		if (!(o instanceof StudentAdditionalCharges)) {
			return false;
		}
		return id != null && id.equals(((StudentAdditionalCharges) o).id);
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
		return "StudentAdditionalCharges{" + "id=" + getId() + ", feeYear='" + getFeeYear() + "'" + ", dueDate="
				+ getDueDate() + ", janAddChrg=" + getJanAddChrg() + ", febAddChrgc=" + getFebAddChrgc()
				+ ", marAddChrg=" + getMarAddChrg() + ", aprAddChrg=" + getAprAddChrg() + ", mayAddChrg="
				+ getMayAddChrg() + ", junAddChrg=" + getJunAddChrg() + ", julAddChrg=" + getJulAddChrg()
				+ ", augAddChrg=" + getAugAddChrg() + ", sepAddChrg=" + getSepAddChrg() + ", octAddChrg="
				+ getOctAddChrg() + ", novAddChrg=" + getNovAddChrg() + ", decAddChrg=" + getDecAddChrg()
				+ ", createDate='" + getCreateDate() + "'" + ", lastModified='" + getLastModified() + "'"
				+ ", cancelDate='" + getCancelDate() + "'" + "}";
	}
}
