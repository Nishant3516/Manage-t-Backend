import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { ISchoolEvent, SchoolEvent } from '../school-event.model';
import { SchoolEventService } from '../service/school-event.service';
import { ISchoolClass } from 'app/entities/school-class/school-class.model';
import { SchoolClassService } from 'app/entities/school-class/service/school-class.service';

@Component({
  selector: 'jhi-school-event-update',
  templateUrl: './school-event-update.component.html',
})
export class SchoolEventUpdateComponent implements OnInit {
  isSaving = false;

  schoolClassesSharedCollection: ISchoolClass[] = [];

  editForm = this.fb.group({
    id: [],
    eventName: [null, [Validators.required]],
    eventDetails: [],
    startDate: [null, [Validators.required]],
    endDate: [null, [Validators.required]],
    createDate: [],
    lastModified: [],
    cancelDate: [],
    schoolClasses: [],
  });

  constructor(
    protected schoolEventService: SchoolEventService,
    protected schoolClassService: SchoolClassService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ schoolEvent }) => {
      this.updateForm(schoolEvent);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const schoolEvent = this.createFromForm();
    if (schoolEvent.id !== undefined) {
      this.subscribeToSaveResponse(this.schoolEventService.update(schoolEvent));
    } else {
      this.subscribeToSaveResponse(this.schoolEventService.create(schoolEvent));
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

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ISchoolEvent>>): void {
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

  protected updateForm(schoolEvent: ISchoolEvent): void {
    this.editForm.patchValue({
      id: schoolEvent.id,
      eventName: schoolEvent.eventName,
      eventDetails: schoolEvent.eventDetails,
      startDate: schoolEvent.startDate,
      endDate: schoolEvent.endDate,
      createDate: schoolEvent.createDate,
      lastModified: schoolEvent.lastModified,
      cancelDate: schoolEvent.cancelDate,
      schoolClasses: schoolEvent.schoolClasses,
    });

    this.schoolClassesSharedCollection = this.schoolClassService.addSchoolClassToCollectionIfMissing(
      this.schoolClassesSharedCollection,
      ...(schoolEvent.schoolClasses ?? [])
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

  protected createFromForm(): ISchoolEvent {
    return {
      ...new SchoolEvent(),
      id: this.editForm.get(['id'])!.value,
      eventName: this.editForm.get(['eventName'])!.value,
      eventDetails: this.editForm.get(['eventDetails'])!.value,
      startDate: this.editForm.get(['startDate'])!.value,
      endDate: this.editForm.get(['endDate'])!.value,
      createDate: this.editForm.get(['createDate'])!.value,
      lastModified: this.editForm.get(['lastModified'])!.value,
      cancelDate: this.editForm.get(['cancelDate'])!.value,
      schoolClasses: this.editForm.get(['schoolClasses'])!.value,
    };
  }
}
