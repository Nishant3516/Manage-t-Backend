import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IStudentPayments, StudentPayments } from '../student-payments.model';
import { StudentPaymentsService } from '../service/student-payments.service';
import { IClassStudent } from 'app/entities/class-student/class-student.model';
import { ClassStudentService } from 'app/entities/class-student/service/class-student.service';
import { SchoolClassService } from 'app/entities/school-class/service/school-class.service';
import { SchoolClassFilter } from '../../../shared/filters/class.filter';
import { IStudentChargesSummary } from 'app/entities/student-charges-summary/student-charges-summary.model';
import { StudentChargesSummaryService } from 'app/entities/student-charges-summary/service/student-charges-summary.service';

@Component({
  selector: 'jhi-student-payments-update',
  templateUrl: './student-payments-update.component.html',
})
export class StudentPaymentsUpdateComponent extends SchoolClassFilter implements OnInit {
  isSaving = false;
  selectedStudentId: any;
  classStudentsSharedCollection: IClassStudent[] = [];
  studentChargesSummaries?: IStudentChargesSummary[];
  editForm = this.fb.group({
    id: [],
    amountPaid: [null, [Validators.required]],
    modeOfPay: [],
    noteNumbers: [],
    upiId: [],
    remarks: [],
    paymentDate: [],
    receiptId: [],
    createDate: [],
    lastModified: [],
    cancelDate: [],
    classStudent: [],
  });

  constructor(
    protected studentChargesSummaryService: StudentChargesSummaryService,
    protected studentPaymentsService: StudentPaymentsService,
    protected classStudentService: ClassStudentService,
    protected schoolClassService: SchoolClassService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {
    super(schoolClassService);
this.selectedStudentId=1;
  }
  trackId(index: number, item: IStudentChargesSummary): number {
    return item.id!;
  }
  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ studentPayments }) => {
      this.updateForm(studentPayments);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    if (typeof this.studentChargesSummaries !== 'undefined') {
      for (const studentChargesSummary of this.studentChargesSummaries) {
        if (studentChargesSummary.summaryType === 'Closing') {
          const due = Number(studentChargesSummary.marSummary);
          if (this.editForm.get(['amountPaid'])!.value > due) {
            alert('Amount greater than balance');
          } else {
            this.isSaving = true;
			this.editForm.patchValue({
				classStudent: studentChargesSummary.classStudent,
			})
            const studentPayments = this.createFromForm();
			studentPayments.classStudent=studentChargesSummary.classStudent;

            this.subscribeToSaveResponse(this.studentPaymentsService.create(studentPayments));
          }
        }
      }
    }
  }
  loadAllStudentsForAClass(): void {
    this.isLoading = true;
    this.classStudentService.query({ 'schoolClassId.equals': this.selectedClassId, size: 60 }).subscribe(
      (res: HttpResponse<IClassStudent[]>) => {
        this.isLoading = false;
        this.classStudentsSharedCollection = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }
  loadAStudent(): void {
    this.classStudentsSharedCollection = [];
    this.fetchAllFinancialSummariesForAStudent(this.selectedStudentId);
    this.selectedStudentId = '';
  }
  loadAllFinancialSummariesForAStudent(): void {
    this.isLoading = true;
    this.fetchAllFinancialSummariesForAStudent(this.editForm.get(['classStudent'])!.value.studentId);
  }
  fetchAllFinancialSummariesForAStudent(studentId: any): void {
    this.isLoading = true;
    this.studentChargesSummaryService.query({ studentIds: studentId, onlyDues: false }).subscribe(
      (res: HttpResponse<IStudentChargesSummary[]>) => {
        this.isLoading = false;
        this.studentChargesSummaries = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  trackClassStudentById(index: number, item: IClassStudent): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IStudentPayments>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.loadAllFinancialSummariesForAStudent();
    this.editForm.patchValue({ amountPaid: '' });
  }

  protected onSaveError(): void {
    alert('Payment failed');
    this.editForm.patchValue({ amountPaid: '' });
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(studentPayments: IStudentPayments): void {
    this.editForm.patchValue({
      id: studentPayments.id,
      amountPaid: studentPayments.amountPaid,
      modeOfPay: studentPayments.modeOfPay,
      noteNumbers: studentPayments.noteNumbers,
      upiId: studentPayments.upiId,
      remarks: studentPayments.remarks,
      paymentDate: studentPayments.paymentDate,
      receiptId: studentPayments.receiptId,
      createDate: studentPayments.createDate,
      lastModified: studentPayments.lastModified,
      cancelDate: studentPayments.cancelDate,
      classStudent: studentPayments.classStudent,
    });

    this.classStudentsSharedCollection = this.classStudentService.addClassStudentToCollectionIfMissing(
      this.classStudentsSharedCollection,
      studentPayments.classStudent
    );
  }

  protected loadRelationshipsOptions(): void {
    this.loadAllClasses();
    this.classStudentService
      .query()
      .pipe(map((res: HttpResponse<IClassStudent[]>) => res.body ?? []))
      .pipe(
        map((classStudents: IClassStudent[]) =>
          this.classStudentService.addClassStudentToCollectionIfMissing(classStudents, this.editForm.get('classStudent')!.value)
        )
      )
      .subscribe((classStudents: IClassStudent[]) => (this.classStudentsSharedCollection = classStudents));
  }

  protected createFromForm(): IStudentPayments {
    return {
      ...new StudentPayments(),
      id: this.editForm.get(['id'])!.value,
      amountPaid: this.editForm.get(['amountPaid'])!.value,
      modeOfPay: this.editForm.get(['modeOfPay'])!.value,
      noteNumbers: this.editForm.get(['noteNumbers'])!.value,
      upiId: this.editForm.get(['upiId'])!.value,
      remarks: this.editForm.get(['remarks'])!.value,
      paymentDate: this.editForm.get(['paymentDate'])!.value,
      receiptId: this.editForm.get(['receiptId'])!.value,
      createDate: this.editForm.get(['createDate'])!.value,
      lastModified: this.editForm.get(['lastModified'])!.value,
      cancelDate: this.editForm.get(['cancelDate'])!.value,
      classStudent: this.editForm.get(['classStudent'])!.value,
    };
  }
}
