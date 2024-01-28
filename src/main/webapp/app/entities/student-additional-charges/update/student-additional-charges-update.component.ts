import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IStudentAdditionalCharges, StudentAdditionalCharges } from '../student-additional-charges.model';
import { StudentAdditionalChargesService } from '../service/student-additional-charges.service';
import { ISchoolLedgerHead } from 'app/entities/school-ledger-head/school-ledger-head.model';
import { SchoolLedgerHeadService } from 'app/entities/school-ledger-head/service/school-ledger-head.service';
import { IClassStudent } from 'app/entities/class-student/class-student.model';
import { ClassStudentService } from 'app/entities/class-student/service/class-student.service';
import { SchoolClassService } from 'app/entities/school-class/service/school-class.service';
import { SchoolClassFilter } from '../../../shared/filters/class.filter';

@Component({
  selector: 'jhi-student-additional-charges-update',
  templateUrl: './student-additional-charges-update.component.html',
})
export class StudentAdditionalChargesUpdateComponent extends SchoolClassFilter implements OnInit {
  isSaving = false;

  schoolLedgerHeadsSharedCollection: ISchoolLedgerHead[] = [];
  classStudentsSharedCollection: IClassStudent[] = [];
  selectedClassIdtest: any;
  editForm = this.fb.group({
    id: [],
    feeYear: [null, [Validators.required]],
    dueDate: [null, [Validators.required]],
    janAddChrg: [],
    febAddChrgc: [],
    marAddChrg: [],
    aprAddChrg: [],
    mayAddChrg: [],
    junAddChrg: [],
    julAddChrg: [],
    augAddChrg: [],
    sepAddChrg: [],
    octAddChrg: [],
    novAddChrg: [],
    decAddChrg: [],
    createDate: [],
    lastModified: [],
    cancelDate: [],
    schoolLedgerHead: [],
    classStudent: [],
  });

  constructor(
    protected studentAdditionalChargesService: StudentAdditionalChargesService,
    protected schoolLedgerHeadService: SchoolLedgerHeadService,
    protected classStudentService: ClassStudentService,
    protected schoolClassService: SchoolClassService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {
    super(schoolClassService);
  }

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ studentAdditionalCharges }) => {
      this.updateForm(studentAdditionalCharges);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const studentAdditionalCharges = this.createFromForm();
    if (studentAdditionalCharges.id !== undefined) {
      this.subscribeToSaveResponse(this.studentAdditionalChargesService.update(studentAdditionalCharges));
    } else {
      this.subscribeToSaveResponse(this.studentAdditionalChargesService.create(studentAdditionalCharges));
    }
  }
  loadAllStudentsForAClass(): void {
    this.isLoading = true;
    this.classStudentService.query({ 'schoolClassId.equals': this.selectedClassId }).subscribe(
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

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IStudentAdditionalCharges>>): void {
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

  protected updateForm(studentAdditionalCharges: IStudentAdditionalCharges): void {
    this.editForm.patchValue({
      id: studentAdditionalCharges.id,
      feeYear: studentAdditionalCharges.feeYear,
      dueDate: studentAdditionalCharges.dueDate,
      janAddChrg: studentAdditionalCharges.janAddChrg,
      febAddChrgc: studentAdditionalCharges.febAddChrgc,
      marAddChrg: studentAdditionalCharges.marAddChrg,
      aprAddChrg: studentAdditionalCharges.aprAddChrg,
      mayAddChrg: studentAdditionalCharges.mayAddChrg,
      junAddChrg: studentAdditionalCharges.junAddChrg,
      julAddChrg: studentAdditionalCharges.julAddChrg,
      augAddChrg: studentAdditionalCharges.augAddChrg,
      sepAddChrg: studentAdditionalCharges.sepAddChrg,
      octAddChrg: studentAdditionalCharges.octAddChrg,
      novAddChrg: studentAdditionalCharges.novAddChrg,
      decAddChrg: studentAdditionalCharges.decAddChrg,
      createDate: studentAdditionalCharges.createDate,
      lastModified: studentAdditionalCharges.lastModified,
      cancelDate: studentAdditionalCharges.cancelDate,
      schoolLedgerHead: studentAdditionalCharges.schoolLedgerHead,
      classStudent: studentAdditionalCharges.classStudent,
    });

    this.schoolLedgerHeadsSharedCollection = this.schoolLedgerHeadService.addSchoolLedgerHeadToCollectionIfMissing(
      this.schoolLedgerHeadsSharedCollection,
      studentAdditionalCharges.schoolLedgerHead
    );
    this.classStudentsSharedCollection = this.classStudentService.addClassStudentToCollectionIfMissing(
      this.classStudentsSharedCollection,
      studentAdditionalCharges.classStudent
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

  protected createFromForm(): IStudentAdditionalCharges {
    return {
      ...new StudentAdditionalCharges(),
      id: this.editForm.get(['id'])!.value,
      feeYear: this.editForm.get(['feeYear'])!.value,
      dueDate: this.editForm.get(['dueDate'])!.value,
      janAddChrg: this.editForm.get(['janAddChrg'])!.value,
      febAddChrgc: this.editForm.get(['febAddChrgc'])!.value,
      marAddChrg: this.editForm.get(['marAddChrg'])!.value,
      aprAddChrg: this.editForm.get(['aprAddChrg'])!.value,
      mayAddChrg: this.editForm.get(['mayAddChrg'])!.value,
      junAddChrg: this.editForm.get(['junAddChrg'])!.value,
      julAddChrg: this.editForm.get(['julAddChrg'])!.value,
      augAddChrg: this.editForm.get(['augAddChrg'])!.value,
      sepAddChrg: this.editForm.get(['sepAddChrg'])!.value,
      octAddChrg: this.editForm.get(['octAddChrg'])!.value,
      novAddChrg: this.editForm.get(['novAddChrg'])!.value,
      decAddChrg: this.editForm.get(['decAddChrg'])!.value,
      createDate: this.editForm.get(['createDate'])!.value,
      lastModified: this.editForm.get(['lastModified'])!.value,
      cancelDate: this.editForm.get(['cancelDate'])!.value,
      schoolLedgerHead: this.editForm.get(['schoolLedgerHead'])!.value,
      classStudent: this.editForm.get(['classStudent'])!.value,
    };
  }
}
