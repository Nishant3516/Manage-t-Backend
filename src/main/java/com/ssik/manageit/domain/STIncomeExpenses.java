package com.ssik.manageit.domain;

import java.io.Serializable;
import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.ssik.manageit.domain.enumeration.ModeOfPayment;
import com.ssik.manageit.domain.enumeration.TransactionType;

/**
 * A STIncomeExpenses.
 */
@Entity
@Table(name = "st_income_expenses")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class STIncomeExpenses implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
	@SequenceGenerator(name = "sequenceGenerator")
	@Column(name = "id")
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

	@Enumerated(EnumType.STRING)
	@Column(name = "transaction_type")
	private TransactionType transactionType;

	@ManyToOne
	@JsonIgnoreProperties(value = { "sTIncomeExpenses", "studentDiscounts", "studentAdditionalCharges",
			"studentChargesSummaries", "studentPayments", "studentAttendences", "studentHomeWorkTracks",
			"studentClassWorkTracks", "schoolClass", }, allowSetters = true)
	private ClassStudent classStudent;

	@ManyToOne
	@JsonIgnoreProperties(value = { "sTIncomeExpenses" }, allowSetters = true)
	private STRoute stRoute;

	@ManyToOne
	@JsonIgnoreProperties(value = { "incomeExpenses", "sTIncomeExpenses" }, allowSetters = true)
	private Vendors operatedBy;
	@ManyToOne
	private School school;

	@ManyToOne
	private Tenant tenant;

	// jhipster-needle-entity-add-field - JHipster will add fields here

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

	public Long getId() {
		return this.id;
	}

	public STIncomeExpenses id(Long id) {
		this.setId(id);
		return this;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Double getAmountPaid() {
		return this.amountPaid;
	}

	public STIncomeExpenses amountPaid(Double amountPaid) {
		this.setAmountPaid(amountPaid);
		return this;
	}

	public void setAmountPaid(Double amountPaid) {
		this.amountPaid = amountPaid;
	}

	public ModeOfPayment getModeOfPay() {
		return this.modeOfPay;
	}

	public STIncomeExpenses modeOfPay(ModeOfPayment modeOfPay) {
		this.setModeOfPay(modeOfPay);
		return this;
	}

	public void setModeOfPay(ModeOfPayment modeOfPay) {
		this.modeOfPay = modeOfPay;
	}

	public String getNoteNumbers() {
		return this.noteNumbers;
	}

	public STIncomeExpenses noteNumbers(String noteNumbers) {
		this.setNoteNumbers(noteNumbers);
		return this;
	}

	public void setNoteNumbers(String noteNumbers) {
		this.noteNumbers = noteNumbers;
	}

	public String getUpiId() {
		return this.upiId;
	}

	public STIncomeExpenses upiId(String upiId) {
		this.setUpiId(upiId);
		return this;
	}

	public void setUpiId(String upiId) {
		this.upiId = upiId;
	}

	public String getRemarks() {
		return this.remarks;
	}

	public STIncomeExpenses remarks(String remarks) {
		this.setRemarks(remarks);
		return this;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public LocalDate getPaymentDate() {
		return this.paymentDate;
	}

	public STIncomeExpenses paymentDate(LocalDate paymentDate) {
		this.setPaymentDate(paymentDate);
		return this;
	}

	public void setPaymentDate(LocalDate paymentDate) {
		this.paymentDate = paymentDate;
	}

	public String getReceiptId() {
		return this.receiptId;
	}

	public STIncomeExpenses receiptId(String receiptId) {
		this.setReceiptId(receiptId);
		return this;
	}

	public void setReceiptId(String receiptId) {
		this.receiptId = receiptId;
	}

	public LocalDate getCreateDate() {
		return this.createDate;
	}

	public STIncomeExpenses createDate(LocalDate createDate) {
		this.setCreateDate(createDate);
		return this;
	}

	public void setCreateDate(LocalDate createDate) {
		this.createDate = createDate;
	}

	public LocalDate getLastModified() {
		return this.lastModified;
	}

	public STIncomeExpenses lastModified(LocalDate lastModified) {
		this.setLastModified(lastModified);
		return this;
	}

	public void setLastModified(LocalDate lastModified) {
		this.lastModified = lastModified;
	}

	public LocalDate getCancelDate() {
		return this.cancelDate;
	}

	public STIncomeExpenses cancelDate(LocalDate cancelDate) {
		this.setCancelDate(cancelDate);
		return this;
	}

	public void setCancelDate(LocalDate cancelDate) {
		this.cancelDate = cancelDate;
	}

	public TransactionType getTransactionType() {
		return this.transactionType;
	}

	public STIncomeExpenses transactionType(TransactionType transactionType) {
		this.setTransactionType(transactionType);
		return this;
	}

	public void setTransactionType(TransactionType transactionType) {
		this.transactionType = transactionType;
	}

	public ClassStudent getClassStudent() {
		return this.classStudent;
	}

	public void setClassStudent(ClassStudent classStudent) {
		this.classStudent = classStudent;
	}

	public STIncomeExpenses classStudent(ClassStudent classStudent) {
		this.setClassStudent(classStudent);
		return this;
	}

	public STRoute getStRoute() {
		return this.stRoute;
	}

	public void setStRoute(STRoute sTRoute) {
		this.stRoute = sTRoute;
	}

	public STIncomeExpenses stRoute(STRoute sTRoute) {
		this.setStRoute(sTRoute);
		return this;
	}

	public Vendors getOperatedBy() {
		return this.operatedBy;
	}

	public void setOperatedBy(Vendors vendors) {
		this.operatedBy = vendors;
	}

	public STIncomeExpenses operatedBy(Vendors vendors) {
		this.setOperatedBy(vendors);
		return this;
	}

	// jhipster-needle-entity-add-getters-setters - JHipster will add getters and
	// setters here

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof STIncomeExpenses)) {
			return false;
		}
		return id != null && id.equals(((STIncomeExpenses) o).id);
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
		return "STIncomeExpenses{" + "id=" + getId() + ", amountPaid=" + getAmountPaid() + ", modeOfPay='"
				+ getModeOfPay() + "'" + ", noteNumbers='" + getNoteNumbers() + "'" + ", upiId='" + getUpiId() + "'"
				+ ", remarks='" + getRemarks() + "'" + ", paymentDate='" + getPaymentDate() + "'" + ", receiptId='"
				+ getReceiptId() + "'" + ", createDate='" + getCreateDate() + "'" + ", lastModified='"
				+ getLastModified() + "'" + ", cancelDate='" + getCancelDate() + "'" + ", transactionType='"
				+ getTransactionType() + "'" + "}";
	}
}
