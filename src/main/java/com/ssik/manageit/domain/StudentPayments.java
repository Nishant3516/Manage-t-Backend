package com.ssik.manageit.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.ssik.manageit.domain.enumeration.ModeOfPayment;
import java.io.Serializable;
import java.time.LocalDate;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A StudentPayments.
 */
@Entity
@Table(name = "student_payments")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class StudentPayments implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
	@SequenceGenerator(name = "sequenceGenerator")
	private Long id;

	@NotNull
	@Column(name = "amount_paid", nullable = false)
	private Double amountPaid;

	@Enumerated(EnumType.STRING)
	@Column(name = "mode_of_pay")
	private ModeOfPayment modeOfPay;

	@Column(name = "note_numbers")
	private String noteNumbers;

	@Column(name = "upi_id")
	private String upiId;

	@Column(name = "remarks")
	private String remarks;

	@Column(name = "payment_date")
	private LocalDate paymentDate;

	@Column(name = "receipt_id")
	private String receiptId;

	@Column(name = "create_date")
	private LocalDate createDate;

	@Column(name = "last_modified")
	private LocalDate lastModified;

	@Column(name = "cancel_date")
	private LocalDate cancelDate;

	@ManyToOne
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

	public StudentPayments id(Long id) {
		this.id = id;
		return this;
	}

	public Double getAmountPaid() {
		return this.amountPaid;
	}

	public StudentPayments amountPaid(Double amountPaid) {
		this.amountPaid = amountPaid;
		return this;
	}

	public void setAmountPaid(Double amountPaid) {
		this.amountPaid = amountPaid;
	}

	public ModeOfPayment getModeOfPay() {
		return this.modeOfPay;
	}

	public StudentPayments modeOfPay(ModeOfPayment modeOfPay) {
		this.modeOfPay = modeOfPay;
		return this;
	}

	public void setModeOfPay(ModeOfPayment modeOfPay) {
		this.modeOfPay = modeOfPay;
	}

	public String getNoteNumbers() {
		return this.noteNumbers;
	}

	public StudentPayments noteNumbers(String noteNumbers) {
		this.noteNumbers = noteNumbers;
		return this;
	}

	public void setNoteNumbers(String noteNumbers) {
		this.noteNumbers = noteNumbers;
	}

	public String getUpiId() {
		return this.upiId;
	}

	public StudentPayments upiId(String upiId) {
		this.upiId = upiId;
		return this;
	}

	public void setUpiId(String upiId) {
		this.upiId = upiId;
	}

	public String getRemarks() {
		return this.remarks;
	}

	public StudentPayments remarks(String remarks) {
		this.remarks = remarks;
		return this;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public LocalDate getPaymentDate() {
		return this.paymentDate;
	}

	public StudentPayments paymentDate(LocalDate paymentDate) {
		this.paymentDate = paymentDate;
		return this;
	}

	public void setPaymentDate(LocalDate paymentDate) {
		this.paymentDate = paymentDate;
	}

	public String getReceiptId() {
		return this.receiptId;
	}

	public StudentPayments receiptId(String receiptId) {
		this.receiptId = receiptId;
		return this;
	}

	public void setReceiptId(String receiptId) {
		this.receiptId = receiptId;
	}

	public LocalDate getCreateDate() {
		return this.createDate;
	}

	public StudentPayments createDate(LocalDate createDate) {
		this.createDate = createDate;
		return this;
	}

	public void setCreateDate(LocalDate createDate) {
		this.createDate = createDate;
	}

	public LocalDate getLastModified() {
		return this.lastModified;
	}

	public StudentPayments lastModified(LocalDate lastModified) {
		this.lastModified = lastModified;
		return this;
	}

	public void setLastModified(LocalDate lastModified) {
		this.lastModified = lastModified;
	}

	public LocalDate getCancelDate() {
		return this.cancelDate;
	}

	public StudentPayments cancelDate(LocalDate cancelDate) {
		this.cancelDate = cancelDate;
		return this;
	}

	public void setCancelDate(LocalDate cancelDate) {
		this.cancelDate = cancelDate;
	}

	public ClassStudent getClassStudent() {
		return this.classStudent;
	}

	public StudentPayments classStudent(ClassStudent classStudent) {
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
		if (!(o instanceof StudentPayments)) {
			return false;
		}
		return id != null && id.equals(((StudentPayments) o).id);
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
		return "StudentPayments{" + "id=" + getId() + ", amountPaid=" + getAmountPaid() + ", modeOfPay='"
				+ getModeOfPay() + "'" + ", noteNumbers='" + getNoteNumbers() + "'" + ", upiId='" + getUpiId() + "'"
				+ ", remarks='" + getRemarks() + "'" + ", paymentDate='" + getPaymentDate() + "'" + ", receiptId='"
				+ getReceiptId() + "'" + ", createDate='" + getCreateDate() + "'" + ", lastModified='"
				+ getLastModified() + "'" + ", cancelDate='" + getCancelDate() + "'" + "}";
	}
}
