import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IClassLessionPlanTrack, ClassLessionPlanTrack } from '../class-lession-plan-track.model';
import { ClassLessionPlanTrackService } from '../service/class-lession-plan-track.service';
import { IClassLessionPlan } from 'app/entities/class-lession-plan/class-lession-plan.model';
import { ClassLessionPlanService } from 'app/entities/class-lession-plan/service/class-lession-plan.service';

@Component({
  selector: 'jhi-class-lession-plan-track-update',
  templateUrl: './class-lession-plan-track-update.component.html',
})
export class ClassLessionPlanTrackUpdateComponent implements OnInit {
  isSaving = false;

  classLessionPlansSharedCollection: IClassLessionPlan[] = [];

  editForm = this.fb.group({
    id: [],
    workStatus: [null, [Validators.required]],
    remarks: [],
    createDate: [],
    lastModified: [],
    cancelDate: [],
    classLessionPlan: [],
  });

  constructor(
    protected classLessionPlanTrackService: ClassLessionPlanTrackService,
    protected classLessionPlanService: ClassLessionPlanService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ classLessionPlanTrack }) => {
      this.updateForm(classLessionPlanTrack);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const classLessionPlanTrack = this.createFromForm();
    if (classLessionPlanTrack.id !== undefined) {
      this.subscribeToSaveResponse(this.classLessionPlanTrackService.update(classLessionPlanTrack));
    } else {
      this.subscribeToSaveResponse(this.classLessionPlanTrackService.create(classLessionPlanTrack));
    }
  }

  trackClassLessionPlanById(index: number, item: IClassLessionPlan): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IClassLessionPlanTrack>>): void {
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

  protected updateForm(classLessionPlanTrack: IClassLessionPlanTrack): void {
    this.editForm.patchValue({
      id: classLessionPlanTrack.id,
      workStatus: classLessionPlanTrack.workStatus,
      remarks: classLessionPlanTrack.remarks,
      createDate: classLessionPlanTrack.createDate,
      lastModified: classLessionPlanTrack.lastModified,
      cancelDate: classLessionPlanTrack.cancelDate,
      classLessionPlan: classLessionPlanTrack.classLessionPlan,
    });

    this.classLessionPlansSharedCollection = this.classLessionPlanService.addClassLessionPlanToCollectionIfMissing(
      this.classLessionPlansSharedCollection,
      classLessionPlanTrack.classLessionPlan
    );
  }

  protected loadRelationshipsOptions(): void {
    this.classLessionPlanService
      .query()
      .pipe(map((res: HttpResponse<IClassLessionPlan[]>) => res.body ?? []))
      .pipe(
        map((classLessionPlans: IClassLessionPlan[]) =>
          this.classLessionPlanService.addClassLessionPlanToCollectionIfMissing(
            classLessionPlans,
            this.editForm.get('classLessionPlan')!.value
          )
        )
      )
      .subscribe((classLessionPlans: IClassLessionPlan[]) => (this.classLessionPlansSharedCollection = classLessionPlans));
  }

  protected createFromForm(): IClassLessionPlanTrack {
    return {
      ...new ClassLessionPlanTrack(),
      id: this.editForm.get(['id'])!.value,
      workStatus: this.editForm.get(['workStatus'])!.value,
      remarks: this.editForm.get(['remarks'])!.value,
      createDate: this.editForm.get(['createDate'])!.value,
      lastModified: this.editForm.get(['lastModified'])!.value,
      cancelDate: this.editForm.get(['cancelDate'])!.value,
      classLessionPlan: this.editForm.get(['classLessionPlan'])!.value,
    };
  }
}
