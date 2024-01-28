import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IStudentDiscount, StudentDiscount } from '../student-discount.model';
import { StudentDiscountService } from '../service/student-discount.service';
import { ISchoolLedgerHead } from 'app/entities/school-ledger-head/school-ledger-head.model';
import { SchoolLedgerHeadService } from 'app/entities/school-ledger-head/service/school-ledger-head.service';
import { IClassStudent } from 'app/entities/class-student/class-student.model';
import { ClassStudentService } from 'app/entities/class-student/service/class-student.service';
import { SchoolClassService } from 'app/entities/school-class/service/school-class.service';
import { SchoolClassFilter } from '../../../shared/filters/class.filter';

@Component({
  selector: 'jhi-student-discount-update',
  templateUrl: './student-discount-update.component.html',
})
export class StudentDiscountUpdateComponent extends SchoolClassFilter implements OnInit {
  isSaving = false;
  schoolLedgerHeadsSharedCollection: ISchoolLedgerHead[] = [];
  classStudentsSharedCollection: IClassStudent[] = [];

  editForm = this.fb.group({
    id: [],
    feeYear: [null, [Validators.required]],
    dueDate: [],
    janFeeDisc: [],
    febFeeDisc: [],
    marFeeDisc: [],
    aprFeeDisc: [],
    mayFeeDisc: [],
    junFeeDisc: [],
    julFeeDisc: [],
    augFeeDisc: [],
    sepFeeDisc: [],
    octFeeDisc: [],
    novFeeDisc: [],
    decFeeDisc: [],
    createDate: [],
    lastModified: [],
    cancelDate: [],
    schoolLedgerHead: [],
    classStudent: [],
  });

  constructor(
    protected studentDiscountService: StudentDiscountService,
    protected schoolLedgerHeadService: SchoolLedgerHeadService,
    protected classStudentService: ClassStudentService,
    protected schoolClassService: SchoolClassService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {
    super(schoolClassService);
  }

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ studentDiscount }) => {
      this.updateForm(studentDiscount);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const studentDiscount = this.createFromForm();
    if (studentDiscount.id !== undefined) {
      this.subscribeToSaveResponse(this.studentDiscountService.update(studentDiscount));
    } else {
      this.subscribeToSaveResponse(this.studentDiscountService.create(studentDiscount));
    }
  }
  loadAllStudentsForAClass(): void {
    this.isLoading = true;
    this.classStudentService.query({ 'schoolClassId.equals': this.selectedClassId , size: 60}).subscribe(
      (res: HttpResponse<IClassStudent[]>) => {
        this.isLoading = false;
        this.classStudentsSharedCollection = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }
  trackSchoolLedgerHeadById(index: number, item: ISchoolLedgerHead): number {
    return item.id!;
  }

  trackClassStudentById(index: number, item: IClassStudent): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IStudentDiscount>>): void {
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

  protected updateForm(studentDiscount: IStudentDiscount): void {
    this.editForm.patchValue({
      id: studentDiscount.id,
      feeYear: studentDiscount.feeYear,
      dueDate: studentDiscount.dueDate,
      janFeeDisc: studentDiscount.janFeeDisc,
      febFeeDisc: studentDiscount.febFeeDisc,
      marFeeDisc: studentDiscount.marFeeDisc,
      aprFeeDisc: studentDiscount.aprFeeDisc,
      mayFeeDisc: studentDiscount.mayFeeDisc,
      junFeeDisc: studentDiscount.junFeeDisc,
      julFeeDisc: studentDiscount.julFeeDisc,
      augFeeDisc: studentDiscount.augFeeDisc,
      sepFeeDisc: studentDiscount.sepFeeDisc,
      octFeeDisc: studentDiscount.octFeeDisc,
      novFeeDisc: studentDiscount.novFeeDisc,
      decFeeDisc: studentDiscount.decFeeDisc,
      createDate: studentDiscount.createDate,
      lastModified: studentDiscount.lastModified,
      cancelDate: studentDiscount.cancelDate,
      schoolLedgerHead: studentDiscount.schoolLedgerHead,
      classStudent: studentDiscount.classStudent,
    });

    this.schoolLedgerHeadsSharedCollection = this.schoolLedgerHeadService.addSchoolLedgerHeadToCollectionIfMissing(
      this.schoolLedgerHeadsSharedCollection,
      studentDiscount.schoolLedgerHead
    );
    this.classStudentsSharedCollection = this.classStudentService.addClassStudentToCollectionIfMissing(
      this.classStudentsSharedCollection,
      studentDiscount.classStudent
    );
  }

  protected loadRelationshipsOptions(): void {
    this.loadAllClasses();
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

  protected createFromForm(): IStudentDiscount {
    return {
      ...new StudentDiscount(),
      id: this.editForm.get(['id'])!.value,
      feeYear: this.editForm.get(['feeYear'])!.value,
      dueDate: this.editForm.get(['dueDate'])!.value,
      janFeeDisc: this.editForm.get(['janFeeDisc'])!.value,
      febFeeDisc: this.editForm.get(['febFeeDisc'])!.value,
      marFeeDisc: this.editForm.get(['marFeeDisc'])!.value,
      aprFeeDisc: this.editForm.get(['aprFeeDisc'])!.value,
      mayFeeDisc: this.editForm.get(['mayFeeDisc'])!.value,
      junFeeDisc: this.editForm.get(['junFeeDisc'])!.value,
      julFeeDisc: this.editForm.get(['julFeeDisc'])!.value,
      augFeeDisc: this.editForm.get(['augFeeDisc'])!.value,
      sepFeeDisc: this.editForm.get(['sepFeeDisc'])!.value,
      octFeeDisc: this.editForm.get(['octFeeDisc'])!.value,
      novFeeDisc: this.editForm.get(['novFeeDisc'])!.value,
      decFeeDisc: this.editForm.get(['decFeeDisc'])!.value,
      createDate: this.editForm.get(['createDate'])!.value,
      lastModified: this.editForm.get(['lastModified'])!.value,
      cancelDate: this.editForm.get(['cancelDate'])!.value,
      schoolLedgerHead: this.editForm.get(['schoolLedgerHead'])!.value,
      classStudent: this.editForm.get(['classStudent'])!.value,
    };
  }
}
