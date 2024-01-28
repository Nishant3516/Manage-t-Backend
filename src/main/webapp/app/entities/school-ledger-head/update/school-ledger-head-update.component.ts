import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { ISchoolLedgerHead, SchoolLedgerHead } from '../school-ledger-head.model';
import { SchoolLedgerHeadService } from '../service/school-ledger-head.service';
import { ISchool } from 'app/entities/school/school.model';
import { SchoolService } from 'app/entities/school/service/school.service';

@Component({
  selector: 'jhi-school-ledger-head-update',
  templateUrl: './school-ledger-head-update.component.html',
})
export class SchoolLedgerHeadUpdateComponent implements OnInit {
  isSaving = false;

  schoolsSharedCollection: ISchool[] = [];

  editForm = this.fb.group({
    id: [],
    studentLedgerHeadType: [null, [Validators.required]],
    ledgerHeadName: [null, [Validators.required]],
    ledgerHeadLongName: [],
    createDate: [],
    lastModified: [],
    cancelDate: [],
    school: [],
  });

  constructor(
    protected schoolLedgerHeadService: SchoolLedgerHeadService,
    protected schoolService: SchoolService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ schoolLedgerHead }) => {
      this.updateForm(schoolLedgerHead);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const schoolLedgerHead = this.createFromForm();
    if (schoolLedgerHead.id !== undefined) {
      this.subscribeToSaveResponse(this.schoolLedgerHeadService.update(schoolLedgerHead));
    } else {
      this.subscribeToSaveResponse(this.schoolLedgerHeadService.create(schoolLedgerHead));
    }
  }

  trackSchoolById(index: number, item: ISchool): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ISchoolLedgerHead>>): void {
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

  protected updateForm(schoolLedgerHead: ISchoolLedgerHead): void {
    this.editForm.patchValue({
      id: schoolLedgerHead.id,
      studentLedgerHeadType: schoolLedgerHead.studentLedgerHeadType,
      ledgerHeadName: schoolLedgerHead.ledgerHeadName,
      ledgerHeadLongName: schoolLedgerHead.ledgerHeadLongName,
      createDate: schoolLedgerHead.createDate,
      lastModified: schoolLedgerHead.lastModified,
      cancelDate: schoolLedgerHead.cancelDate,
      school: schoolLedgerHead.school,
    });

    this.schoolsSharedCollection = this.schoolService.addSchoolToCollectionIfMissing(this.schoolsSharedCollection, schoolLedgerHead.school);
  }

  protected loadRelationshipsOptions(): void {
    this.schoolService
      .query()
      .pipe(map((res: HttpResponse<ISchool[]>) => res.body ?? []))
      .pipe(map((schools: ISchool[]) => this.schoolService.addSchoolToCollectionIfMissing(schools, this.editForm.get('school')!.value)))
      .subscribe((schools: ISchool[]) => (this.schoolsSharedCollection = schools));
  }

  protected createFromForm(): ISchoolLedgerHead {
    return {
      ...new SchoolLedgerHead(),
      id: this.editForm.get(['id'])!.value,
      studentLedgerHeadType: this.editForm.get(['studentLedgerHeadType'])!.value,
      ledgerHeadName: this.editForm.get(['ledgerHeadName'])!.value,
      ledgerHeadLongName: this.editForm.get(['ledgerHeadLongName'])!.value,
      createDate: this.editForm.get(['createDate'])!.value,
      lastModified: this.editForm.get(['lastModified'])!.value,
      cancelDate: this.editForm.get(['cancelDate'])!.value,
      school: this.editForm.get(['school'])!.value,
    };
  }
}
