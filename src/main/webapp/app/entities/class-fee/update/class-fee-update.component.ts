import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IClassFee, ClassFee } from '../class-fee.model';
import { ClassFeeService } from '../service/class-fee.service';
import { ISchoolClass } from 'app/entities/school-class/school-class.model';
import { SchoolClassService } from 'app/entities/school-class/service/school-class.service';
import { ISchoolLedgerHead } from 'app/entities/school-ledger-head/school-ledger-head.model';
import { SchoolLedgerHeadService } from 'app/entities/school-ledger-head/service/school-ledger-head.service';

@Component({
  selector: 'jhi-class-fee-update',
  templateUrl: './class-fee-update.component.html',
})
export class ClassFeeUpdateComponent implements OnInit {
  isSaving = false;

  schoolClassesSharedCollection: ISchoolClass[] = [];
  schoolLedgerHeadsSharedCollection: ISchoolLedgerHead[] = [];

  editForm = this.fb.group({
    id: [],
    feeYear: [null, [Validators.required]],
    dueDate: [null, [Validators.required]],
    janFee: [],
    febFee: [],
    marFee: [],
    aprFee: [],
    mayFee: [],
    junFee: [],
    julFee: [],
    augFee: [],
    sepFee: [],
    octFee: [],
    novFee: [],
    decFee: [],
    payByDate: [],
    createDate: [],
    lastModified: [],
    cancelDate: [],
    schoolClasses: [],
    schoolLedgerHead: [],
  });

  constructor(
    protected classFeeService: ClassFeeService,
    protected schoolClassService: SchoolClassService,
    protected schoolLedgerHeadService: SchoolLedgerHeadService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ classFee }) => {
      this.updateForm(classFee);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const classFee = this.createFromForm();
    if (classFee.id !== undefined) {
      this.subscribeToSaveResponse(this.classFeeService.update(classFee));
    } else {
      this.subscribeToSaveResponse(this.classFeeService.create(classFee));
    }
  }

  trackSchoolClassById(index: number, item: ISchoolClass): number {
    return item.id!;
  }

  trackSchoolLedgerHeadById(index: number, item: ISchoolLedgerHead): number {
    return item.id!;
  }

  getSelectedSchoolClass(option: ISchoolClass, selectedVals?: ISchoolClass[]): ISchoolClass {
    if (selectedVals) {
      for (const selectedVal of selectedVals) {
        if (option.id === selectedVal.id) {
          return selectedVal;
        }
      }
    }
    return option;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IClassFee>>): void {
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

  protected updateForm(classFee: IClassFee): void {
    this.editForm.patchValue({
      id: classFee.id,
      feeYear: classFee.feeYear,
      dueDate: classFee.dueDate,
      janFee: classFee.janFee,
      febFee: classFee.febFee,
      marFee: classFee.marFee,
      aprFee: classFee.aprFee,
      mayFee: classFee.mayFee,
      junFee: classFee.junFee,
      julFee: classFee.julFee,
      augFee: classFee.augFee,
      sepFee: classFee.sepFee,
      octFee: classFee.octFee,
      novFee: classFee.novFee,
      decFee: classFee.decFee,
      payByDate: classFee.payByDate,
      createDate: classFee.createDate,
      lastModified: classFee.lastModified,
      cancelDate: classFee.cancelDate,
      schoolClasses: classFee.schoolClasses,
      schoolLedgerHead: classFee.schoolLedgerHead,
    });

    this.schoolClassesSharedCollection = this.schoolClassService.addSchoolClassToCollectionIfMissing(
      this.schoolClassesSharedCollection,
      ...(classFee.schoolClasses ?? [])
    );
    this.schoolLedgerHeadsSharedCollection = this.schoolLedgerHeadService.addSchoolLedgerHeadToCollectionIfMissing(
      this.schoolLedgerHeadsSharedCollection,
      classFee.schoolLedgerHead
    );
  }

  protected loadRelationshipsOptions(): void {
    this.schoolClassService
      .query()
      .pipe(map((res: HttpResponse<ISchoolClass[]>) => res.body ?? []))
      .pipe(
        map((schoolClasses: ISchoolClass[]) =>
          this.schoolClassService.addSchoolClassToCollectionIfMissing(schoolClasses, ...(this.editForm.get('schoolClasses')!.value ?? []))
        )
      )
      .subscribe((schoolClasses: ISchoolClass[]) => (this.schoolClassesSharedCollection = schoolClasses));

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
  }

  protected createFromForm(): IClassFee {
    return {
      ...new ClassFee(),
      id: this.editForm.get(['id'])!.value,
      feeYear: this.editForm.get(['feeYear'])!.value,
      dueDate: this.editForm.get(['dueDate'])!.value,
      janFee: this.editForm.get(['janFee'])!.value,
      febFee: this.editForm.get(['febFee'])!.value,
      marFee: this.editForm.get(['marFee'])!.value,
      aprFee: this.editForm.get(['aprFee'])!.value,
      mayFee: this.editForm.get(['mayFee'])!.value,
      junFee: this.editForm.get(['junFee'])!.value,
      julFee: this.editForm.get(['julFee'])!.value,
      augFee: this.editForm.get(['augFee'])!.value,
      sepFee: this.editForm.get(['sepFee'])!.value,
      octFee: this.editForm.get(['octFee'])!.value,
      novFee: this.editForm.get(['novFee'])!.value,
      decFee: this.editForm.get(['decFee'])!.value,
      payByDate: this.editForm.get(['payByDate'])!.value,
      createDate: this.editForm.get(['createDate'])!.value,
      lastModified: this.editForm.get(['lastModified'])!.value,
      cancelDate: this.editForm.get(['cancelDate'])!.value,
      schoolClasses: this.editForm.get(['schoolClasses'])!.value,
      schoolLedgerHead: this.editForm.get(['schoolLedgerHead'])!.value,
    };
  }
}
