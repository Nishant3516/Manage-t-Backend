import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { ISchoolDaysOff, SchoolDaysOff } from '../school-days-off.model';
import { SchoolDaysOffService } from '../service/school-days-off.service';
import { ISchoolClass } from 'app/entities/school-class/school-class.model';
import { SchoolClassService } from 'app/entities/school-class/service/school-class.service';

@Component({
  selector: 'jhi-school-days-off-update',
  templateUrl: './school-days-off-update.component.html',
})
export class SchoolDaysOffUpdateComponent implements OnInit {
  isSaving = false;

  schoolClassesSharedCollection: ISchoolClass[] = [];

  editForm = this.fb.group({
    id: [],
    dayOffType: [null, [Validators.required]],
    dayOffName: [null, [Validators.required]],
    dayOffDetails: [],
    startDate: [null, [Validators.required]],
    endDate: [null, [Validators.required]],
    createDate: [],
    lastModified: [],
    cancelDate: [],
    schoolClasses: [],
  });

  constructor(
    protected schoolDaysOffService: SchoolDaysOffService,
    protected schoolClassService: SchoolClassService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ schoolDaysOff }) => {
      this.updateForm(schoolDaysOff);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const schoolDaysOff = this.createFromForm();
    if (schoolDaysOff.id !== undefined) {
      this.subscribeToSaveResponse(this.schoolDaysOffService.update(schoolDaysOff));
    } else {
      this.subscribeToSaveResponse(this.schoolDaysOffService.create(schoolDaysOff));
    }
  }

  trackSchoolClassById(index: number, item: ISchoolClass): number {
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

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ISchoolDaysOff>>): void {
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

  protected updateForm(schoolDaysOff: ISchoolDaysOff): void {
    this.editForm.patchValue({
      id: schoolDaysOff.id,
      dayOffType: schoolDaysOff.dayOffType,
      dayOffName: schoolDaysOff.dayOffName,
      dayOffDetails: schoolDaysOff.dayOffDetails,
      startDate: schoolDaysOff.startDate,
      endDate: schoolDaysOff.endDate,
      createDate: schoolDaysOff.createDate,
      lastModified: schoolDaysOff.lastModified,
      cancelDate: schoolDaysOff.cancelDate,
      schoolClasses: schoolDaysOff.schoolClasses,
    });

    this.schoolClassesSharedCollection = this.schoolClassService.addSchoolClassToCollectionIfMissing(
      this.schoolClassesSharedCollection,
      ...(schoolDaysOff.schoolClasses ?? [])
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
  }

  protected createFromForm(): ISchoolDaysOff {
    return {
      ...new SchoolDaysOff(),
      id: this.editForm.get(['id'])!.value,
      dayOffType: this.editForm.get(['dayOffType'])!.value,
      dayOffName: this.editForm.get(['dayOffName'])!.value,
      dayOffDetails: this.editForm.get(['dayOffDetails'])!.value,
      startDate: this.editForm.get(['startDate'])!.value,
      endDate: this.editForm.get(['endDate'])!.value,
      createDate: this.editForm.get(['createDate'])!.value,
      lastModified: this.editForm.get(['lastModified'])!.value,
      cancelDate: this.editForm.get(['cancelDate'])!.value,
      schoolClasses: this.editForm.get(['schoolClasses'])!.value,
    };
  }
}
