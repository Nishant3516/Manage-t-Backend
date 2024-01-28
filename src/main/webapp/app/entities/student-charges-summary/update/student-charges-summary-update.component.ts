import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IStudentChargesSummary, StudentChargesSummary } from '../student-charges-summary.model';
import { StudentChargesSummaryService } from '../service/student-charges-summary.service';
import { ISchoolLedgerHead } from 'app/entities/school-ledger-head/school-ledger-head.model';
import { SchoolLedgerHeadService } from 'app/entities/school-ledger-head/service/school-ledger-head.service';
import { IClassStudent } from 'app/entities/class-student/class-student.model';
import { ClassStudentService } from 'app/entities/class-student/service/class-student.service';

@Component({
  selector: 'jhi-student-charges-summary-update',
  templateUrl: './student-charges-summary-update.component.html',
})
export class StudentChargesSummaryUpdateComponent implements OnInit {
  isSaving = false;

  schoolLedgerHeadsSharedCollection: ISchoolLedgerHead[] = [];
  classStudentsSharedCollection: IClassStudent[] = [];

  editForm = this.fb.group({
    id: [],
    summaryType: [],
    feeYear: [],
    dueDate: [],
    aprSummary: [],
    maySummary: [],
    junSummary: [],
    julSummary: [],
    augSummary: [],
    sepSummary: [],
    octSummary: [],
    novSummary: [],
    decSummary: [],
    janSummary: [],
    febSummary: [],
    marSummary: [],
    additionalInfo1: [],
    additionalInfo2: [],
    createDate: [],
    lastModified: [],
    cancelDate: [],
    schoolLedgerHead: [],
    classStudent: [],
  });

  constructor(
    protected studentChargesSummaryService: StudentChargesSummaryService,
    protected schoolLedgerHeadService: SchoolLedgerHeadService,
    protected classStudentService: ClassStudentService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ studentChargesSummary }) => {
      this.updateForm(studentChargesSummary);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const studentChargesSummary = this.createFromForm();
    if (studentChargesSummary.id !== undefined) {
      this.subscribeToSaveResponse(this.studentChargesSummaryService.update(studentChargesSummary));
    } else {
      this.subscribeToSaveResponse(this.studentChargesSummaryService.create(studentChargesSummary));
    }
  }

  trackSchoolLedgerHeadById(index: number, item: ISchoolLedgerHead): number {
    return item.id!;
  }

  trackClassStudentById(index: number, item: IClassStudent): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IStudentChargesSummary>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(studentChargesSummary: IStudentChargesSummary): void {
    this.editForm.patchValue({
      id: studentChargesSummary.id,
      summaryType: studentChargesSummary.summaryType,
      feeYear: studentChargesSummary.feeYear,
      dueDate: studentChargesSummary.dueDate,
      aprSummary: studentChargesSummary.aprSummary,
      maySummary: studentChargesSummary.maySummary,
      junSummary: studentChargesSummary.junSummary,
      julSummary: studentChargesSummary.julSummary,
      augSummary: studentChargesSummary.augSummary,
      sepSummary: studentChargesSummary.sepSummary,
      octSummary: studentChargesSummary.octSummary,
      novSummary: studentChargesSummary.novSummary,
      decSummary: studentChargesSummary.decSummary,
      janSummary: studentChargesSummary.janSummary,
      febSummary: studentChargesSummary.febSummary,
      marSummary: studentChargesSummary.marSummary,
      additionalInfo1: studentChargesSummary.additionalInfo1,
      additionalInfo2: studentChargesSummary.additionalInfo2,
      createDate: studentChargesSummary.createDate,
      lastModified: studentChargesSummary.lastModified,
      cancelDate: studentChargesSummary.cancelDate,
      schoolLedgerHead: studentChargesSummary.schoolLedgerHead,
      classStudent: studentChargesSummary.classStudent,
    });

    this.schoolLedgerHeadsSharedCollection = this.schoolLedgerHeadService.addSchoolLedgerHeadToCollectionIfMissing(
      this.schoolLedgerHeadsSharedCollection,
      studentChargesSummary.schoolLedgerHead
    );
    this.classStudentsSharedCollection = this.classStudentService.addClassStudentToCollectionIfMissing(
      this.classStudentsSharedCollection,
      studentChargesSummary.classStudent
    );
  }

  protected loadRelationshipsOptions(): void {
    this.schoolLedgerHeadService
      .query()
      .pipe(map((res: HttpResponse<ISchoolLedgerHead[]>) => res.body ?? []))
      .pipe(
        map((schoolLedgerHeads: ISchoolLedgerHead[]) =>
          this.schoolLedgerHeadService.addSchoolLedgerHeadToCollectionIfMissing(
            schoolLedgerHeads,
            this.editForm.get('schoolLedgerHead')!.value
          )
        )
      )
      .subscribe((schoolLedgerHeads: ISchoolLedgerHead[]) => (this.schoolLedgerHeadsSharedCollection = schoolLedgerHeads));

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

  protected createFromForm(): IStudentChargesSummary {
    return {
      ...new StudentChargesSummary(),
      id: this.editForm.get(['id'])!.value,
      summaryType: this.editForm.get(['summaryType'])!.value,
      feeYear: this.editForm.get(['feeYear'])!.value,
      dueDate: this.editForm.get(['dueDate'])!.value,
      aprSummary: this.editForm.get(['aprSummary'])!.value,
      maySummary: this.editForm.get(['maySummary'])!.value,
      junSummary: this.editForm.get(['junSummary'])!.value,
      julSummary: this.editForm.get(['julSummary'])!.value,
      augSummary: this.editForm.get(['augSummary'])!.value,
      sepSummary: this.editForm.get(['sepSummary'])!.value,
      octSummary: this.editForm.get(['octSummary'])!.value,
      novSummary: this.editForm.get(['novSummary'])!.value,
      decSummary: this.editForm.get(['decSummary'])!.value,
      janSummary: this.editForm.get(['janSummary'])!.value,
      febSummary: this.editForm.get(['febSummary'])!.value,
      marSummary: this.editForm.get(['marSummary'])!.value,
      additionalInfo1: this.editForm.get(['additionalInfo1'])!.value,
      additionalInfo2: this.editForm.get(['additionalInfo2'])!.value,
      createDate: this.editForm.get(['createDate'])!.value,
      lastModified: this.editForm.get(['lastModified'])!.value,
      cancelDate: this.editForm.get(['cancelDate'])!.value,
      schoolLedgerHead: this.editForm.get(['schoolLedgerHead'])!.value,
      classStudent: this.editForm.get(['classStudent'])!.value,
    };
  }
}
