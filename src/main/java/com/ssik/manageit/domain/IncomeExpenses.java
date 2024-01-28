package com.ssik.manageit.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.ssik.manageit.domain.enumeration.ModeOfPayment;
import com.ssik.manageit.domain.enumeration.TransactionType;
import java.io.Serializable;
import java.time.LocalDate;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A IncomeExpenses.
 */
@Entity
@Table(name = "income_expenses")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class IncomeExpenses implements Serializable {

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
	@JsonIgnoreProperties(value = { "incomeExpenses", "sTIncomeExpenses" }, allowSetters = true)
	private Vendors vendor;

	@ManyToOne
	private School school;

	@ManyToOne
	private Tenant tenant;

	@ManyToOne
	@JsonIgnoreProperties(value = { "incomeExpenses", "classFees", "studentDiscounts", "studentAdditionalCharges",
			"studentChargesSummaries", "school" }, allowSetters = true)
	private SchoolLedgerHead ledgerHead;

	// jhipster-needle-entity-add-field - JHipster will add fields here

	public Long getId() {
		return this.id;
	}

	public IncomeExpenses id(Long id) {
		this.setId(id);
		return this;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Double getAmountPaid() {
		return this.amountPaid;
	}

	public IncomeExpenses amountPaid(Double amountPaid) {
		this.setAmountPaid(amountPaid);
		return this;
	}

	public void setAmountPaid(Double amountPaid) {
		this.amountPaid = amountPaid;
	}

	public ModeOfPayment getModeOfPay() {
		return this.modeOfPay;
	}

	public IncomeExpenses modeOfPay(ModeOfPayment modeOfPay) {
		this.setModeOfPay(modeOfPay);
		return this;
	}

	public void setModeOfPay(ModeOfPayment modeOfPay) {
		this.modeOfPay = modeOfPay;
	}

	public String getNoteNumbers() {
		return this.noteNumbers;
	}

	public IncomeExpenses noteNumbers(String noteNumbers) {
		this.setNoteNumbers(noteNumbers);
		return this;
	}

	public void setNoteNumbers(String noteNumbers) {
		this.noteNumbers = noteNumbers;
	}

	public String getUpiId() {
		return this.upiId;
	}

	public IncomeExpenses upiId(String upiId) {
		this.setUpiId(upiId);
		return this;
	}

	public void setUpiId(String upiId) {
		this.upiId = upiId;
	}

	public String getRemarks() {
		return this.remarks;
	}

	public IncomeExpenses remarks(String remarks) {
		this.setRemarks(remarks);
		return this;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public LocalDate getPaymentDate() {
		return this.paymentDate;
	}

	public IncomeExpenses paymentDate(LocalDate paymentDate) {
		this.setPaymentDate(paymentDate);
		return this;
	}

	public void setPaymentDate(LocalDate paymentDate) {
		this.paymentDate = paymentDate;
	}

	public String getReceiptId() {
		return this.receiptId;
	}

	public IncomeExpenses receiptId(String receiptId) {
		this.setReceiptId(receiptId);
		return this;
	}

	public void setReceiptId(String receiptId) {
		this.receiptId = receiptId;
	}

	public LocalDate getCreateDate() {
		return this.createDate;
	}

	public IncomeExpenses createDate(LocalDate createDate) {
		this.setCreateDate(createDate);
		return this;
	}

	public void setCreateDate(LocalDate createDate) {
		this.createDate = createDate;
	}

	public LocalDate getLastModified() {
		return this.lastModified;
	}

	public IncomeExpenses lastModified(LocalDate lastModified) {
		this.setLastModified(lastModified);
		return this;
	}

	public void setLastModified(LocalDate lastModified) {
		this.lastModified = lastModified;
	}

	public LocalDate getCancelDate() {
		return this.cancelDate;
	}

	public IncomeExpenses cancelDate(LocalDate cancelDate) {
		this.setCancelDate(cancelDate);
		return this;
	}

	public void setCancelDate(LocalDate cancelDate) {
		this.cancelDate = cancelDate;
	}

	public TransactionType getTransactionType() {
		return this.transactionType;
	}

	public IncomeExpenses transactionType(TransactionType transactionType) {
		this.setTransactionType(transactionType);
		return this;
	}

	public void setTransactionType(TransactionType transactionType) {
		this.transactionType = transactionType;
	}

	public Vendors getVendor() {
		return this.vendor;
	}

	public void setVendor(Vendors vendors) {
		this.vendor = vendors;
	}

	public IncomeExpenses vendor(Vendors vendors) {
		this.setVendor(vendors);
		return this;
	}

	public SchoolLedgerHead getLedgerHead() {
		return this.ledgerHead;
	}

	public void setLedgerHead(SchoolLedgerHead schoolLedgerHead) {
		this.ledgerHead = schoolLedgerHead;
	}

	public IncomeExpenses ledgerHead(SchoolLedgerHead schoolLedgerHead) {
		this.setLedgerHead(schoolLedgerHead);
		return this;
	}

	// jhipster-needle-entity-add-getters-setters - JHipster will add getters and
	// setters here

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

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof IncomeExpenses)) {
			return false;
		}
		return id != null && id.equals(((IncomeExpenses) o).id);
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
		return "IncomeExpenses{" + "id=" + getId() + ", amountPaid=" + getAmountPaid() + ", modeOfPay='"
				+ getModeOfPay() + "'" + ", noteNumbers='" + getNoteNumbers() + "'" + ", upiId='" + getUpiId() + "'"
				+ ", remarks='" + getRemarks() + "'" + ", paymentDate='" + getPaymentDate() + "'" + ", receiptId='"
				+ getReceiptId() + "'" + ", createDate='" + getCreateDate() + "'" + ", lastModified='"
				+ getLastModified() + "'" + ", cancelDate='" + getCancelDate() + "'" + ", transactionType='"
				+ getTransactionType() + "'" + "}";
	}
}
