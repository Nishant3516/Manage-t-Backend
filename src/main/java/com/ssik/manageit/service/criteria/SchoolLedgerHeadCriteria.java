package com.ssik.manageit.service.criteria;

import com.ssik.manageit.domain.enumeration.SchoolLedgerHeadType;
import java.io.Serializable;
import java.util.Objects;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.BooleanFilter;
import tech.jhipster.service.filter.DoubleFilter;
import tech.jhipster.service.filter.Filter;
import tech.jhipster.service.filter.FloatFilter;
import tech.jhipster.service.filter.IntegerFilter;
import tech.jhipster.service.filter.LocalDateFilter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link com.ssik.manageit.domain.SchoolLedgerHead}
 * entity. This class is used in
 * {@link com.ssik.manageit.web.rest.SchoolLedgerHeadResource} to receive all
 * the possible filtering options from the Http GET request parameters. For
 * example the following could be a valid request:
 * {@code /school-ledger-heads?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific
 * {@link Filter} class are used, we need to use fix type specific filters.
 */
public class SchoolLedgerHeadCriteria implements Serializable, Criteria {

	/**
	 * Class for filtering SchoolLedgerHeadType
	 */
	public static class SchoolLedgerHeadTypeFilter extends Filter<SchoolLedgerHeadType> {

		public SchoolLedgerHeadTypeFilter() {
		}

		public SchoolLedgerHeadTypeFilter(SchoolLedgerHeadTypeFilter filter) {
			super(filter);
		}

		@Override
		public SchoolLedgerHeadTypeFilter copy() {
			return new SchoolLedgerHeadTypeFilter(this);
		}
	}

	private static final long serialVersionUID = 1L;

	private LongFilter id;

	private SchoolLedgerHeadTypeFilter studentLedgerHeadType;

	private StringFilter ledgerHeadName;

	private StringFilter ledgerHeadLongName;

	private LocalDateFilter createDate;

	private LocalDateFilter lastModified;

	private LocalDateFilter cancelDate;

	private LongFilter classFeeId;

	private LongFilter studentDiscountId;

	private LongFilter studentAdditionalChargesId;

	private LongFilter studentChargesSummaryId;

	private LongFilter schoolId;

//	private LongFilter tenantId;

	public SchoolLedgerHeadCriteria() {
	}

	public SchoolLedgerHeadCriteria(SchoolLedgerHeadCriteria other) {
		this.id = other.id == null ? null : other.id.copy();
		this.studentLedgerHeadType = other.studentLedgerHeadType == null ? null : other.studentLedgerHeadType.copy();
		this.ledgerHeadName = other.ledgerHeadName == null ? null : other.ledgerHeadName.copy();
		this.ledgerHeadLongName = other.ledgerHeadLongName == null ? null : other.ledgerHeadLongName.copy();
		this.createDate = other.createDate == null ? null : other.createDate.copy();
		this.lastModified = other.lastModified == null ? null : other.lastModified.copy();
		this.cancelDate = other.cancelDate == null ? null : other.cancelDate.copy();
		this.classFeeId = other.classFeeId == null ? null : other.classFeeId.copy();
		this.studentDiscountId = other.studentDiscountId == null ? null : other.studentDiscountId.copy();
		this.studentAdditionalChargesId = other.studentAdditionalChargesId == null ? null
				: other.studentAdditionalChargesId.copy();
		this.studentChargesSummaryId = other.studentChargesSummaryId == null ? null
				: other.studentChargesSummaryId.copy();
		this.schoolId = other.schoolId == null ? null : other.schoolId.copy();
		// this.tenantId = other.tenantId == null ? null : other.tenantId.copy();
	}

	@Override
	public SchoolLedgerHeadCriteria copy() {
		return new SchoolLedgerHeadCriteria(this);
	}

	public LongFilter getId() {
		return id;
	}

	public LongFilter id() {
		if (id == null) {
			id = new LongFilter();
		}
		return id;
	}

	public void setId(LongFilter id) {
		this.id = id;
	}

	public SchoolLedgerHeadTypeFilter getStudentLedgerHeadType() {
		return studentLedgerHeadType;
	}

	public SchoolLedgerHeadTypeFilter studentLedgerHeadType() {
		if (studentLedgerHeadType == null) {
			studentLedgerHeadType = new SchoolLedgerHeadTypeFilter();
		}
		return studentLedgerHeadType;
	}

	public void setStudentLedgerHeadType(SchoolLedgerHeadTypeFilter studentLedgerHeadType) {
		this.studentLedgerHeadType = studentLedgerHeadType;
	}

	public StringFilter getLedgerHeadName() {
		return ledgerHeadName;
	}

	public StringFilter ledgerHeadName() {
		if (ledgerHeadName == null) {
			ledgerHeadName = new StringFilter();
		}
		return ledgerHeadName;
	}

	public void setLedgerHeadName(StringFilter ledgerHeadName) {
		this.ledgerHeadName = ledgerHeadName;
	}

	public StringFilter getLedgerHeadLongName() {
		return ledgerHeadLongName;
	}

	public StringFilter ledgerHeadLongName() {
		if (ledgerHeadLongName == null) {
			ledgerHeadLongName = new StringFilter();
		}
		return ledgerHeadLongName;
	}

	public void setLedgerHeadLongName(StringFilter ledgerHeadLongName) {
		this.ledgerHeadLongName = ledgerHeadLongName;
	}

	public LocalDateFilter getCreateDate() {
		return createDate;
	}

	public LocalDateFilter createDate() {
		if (createDate == null) {
			createDate = new LocalDateFilter();
		}
		return createDate;
	}

	public void setCreateDate(LocalDateFilter createDate) {
		this.createDate = createDate;
	}

	public LocalDateFilter getLastModified() {
		return lastModified;
	}

	public LocalDateFilter lastModified() {
		if (lastModified == null) {
			lastModified = new LocalDateFilter();
		}
		return lastModified;
	}

	public void setLastModified(LocalDateFilter lastModified) {
		this.lastModified = lastModified;
	}

	public LocalDateFilter getCancelDate() {
		return cancelDate;
	}

	public LocalDateFilter cancelDate() {
		if (cancelDate == null) {
			cancelDate = new LocalDateFilter();
		}
		return cancelDate;
	}

	public void setCancelDate(LocalDateFilter cancelDate) {
		this.cancelDate = cancelDate;
	}

	public LongFilter getClassFeeId() {
		return classFeeId;
	}

	public LongFilter classFeeId() {
		if (classFeeId == null) {
			classFeeId = new LongFilter();
		}
		return classFeeId;
	}

	public void setClassFeeId(LongFilter classFeeId) {
		this.classFeeId = classFeeId;
	}

	public LongFilter getStudentDiscountId() {
		return studentDiscountId;
	}

	public LongFilter studentDiscountId() {
		if (studentDiscountId == null) {
			studentDiscountId = new LongFilter();
		}
		return studentDiscountId;
	}

	public void setStudentDiscountId(LongFilter studentDiscountId) {
		this.studentDiscountId = studentDiscountId;
	}

	public LongFilter getStudentAdditionalChargesId() {
		return studentAdditionalChargesId;
	}

	public LongFilter studentAdditionalChargesId() {
		if (studentAdditionalChargesId == null) {
			studentAdditionalChargesId = new LongFilter();
		}
		return studentAdditionalChargesId;
	}

	public void setStudentAdditionalChargesId(LongFilter studentAdditionalChargesId) {
		this.studentAdditionalChargesId = studentAdditionalChargesId;
	}

	public LongFilter getStudentChargesSummaryId() {
		return studentChargesSummaryId;
	}

	public LongFilter studentChargesSummaryId() {
		if (studentChargesSummaryId == null) {
			studentChargesSummaryId = new LongFilter();
		}
		return studentChargesSummaryId;
	}

	public void setStudentChargesSummaryId(LongFilter studentChargesSummaryId) {
		this.studentChargesSummaryId = studentChargesSummaryId;
	}

	public LongFilter getSchoolId() {
		return schoolId;
	}

	public LongFilter schoolId() {
		if (schoolId == null) {
			schoolId = new LongFilter();
		}
		return schoolId;
	}

	public void setSchoolId(LongFilter schoolId) {
		this.schoolId = schoolId;
	}

//	public LongFilter getTenantId() {
//		return tenantId;
//	}
//
//	public void setTenantId(LongFilter tenantId) {
//		this.tenantId = tenantId;
//	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		final SchoolLedgerHeadCriteria that = (SchoolLedgerHeadCriteria) o;
		return (Objects.equals(id, that.id) && Objects.equals(studentLedgerHeadType, that.studentLedgerHeadType)
				&& Objects.equals(ledgerHeadName, that.ledgerHeadName)
				&& Objects.equals(ledgerHeadLongName, that.ledgerHeadLongName)
				&& Objects.equals(createDate, that.createDate) && Objects.equals(lastModified, that.lastModified)
				&& Objects.equals(cancelDate, that.cancelDate) && Objects.equals(classFeeId, that.classFeeId)
				&& Objects.equals(studentDiscountId, that.studentDiscountId)
				&& Objects.equals(studentAdditionalChargesId, that.studentAdditionalChargesId)
				&& Objects.equals(studentChargesSummaryId, that.studentChargesSummaryId)
		// && Objects.equals(schoolId, that.schoolId) && Objects.equals(tenantId,
		// that.tenantId)

		);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, studentLedgerHeadType, ledgerHeadName, ledgerHeadLongName, createDate, lastModified,
				cancelDate, classFeeId, studentDiscountId, studentAdditionalChargesId, studentChargesSummaryId,
				schoolId);// tenantId);
	}

	// prettier-ignore
	@Override
	public String toString() {
		return "SchoolLedgerHeadCriteria{" + (id != null ? "id=" + id + ", " : "")
				+ (studentLedgerHeadType != null ? "studentLedgerHeadType=" + studentLedgerHeadType + ", " : "")
				+ (ledgerHeadName != null ? "ledgerHeadName=" + ledgerHeadName + ", " : "")
				+ (ledgerHeadLongName != null ? "ledgerHeadLongName=" + ledgerHeadLongName + ", " : "")
				+ (createDate != null ? "createDate=" + createDate + ", " : "")
				+ (lastModified != null ? "lastModified=" + lastModified + ", " : "")
				+ (cancelDate != null ? "cancelDate=" + cancelDate + ", " : "")
				+ (classFeeId != null ? "classFeeId=" + classFeeId + ", " : "")
				+ (studentDiscountId != null ? "studentDiscountId=" + studentDiscountId + ", " : "")
				+ (studentAdditionalChargesId != null
						? "studentAdditionalChargesId=" + studentAdditionalChargesId + ", "
						: "")
				+ (studentChargesSummaryId != null ? "studentChargesSummaryId=" + studentChargesSummaryId + ", " : "")
				+ (schoolId != null ? "schoolId=" + schoolId + ", " : "")
				// + (tenantId != null ? "tenantId=" + tenantId + ", " : "") + "}"
				+ "}"

		;
	}
}
