import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IIdStore, IdStore } from '../id-store.model';
import { IdStoreService } from '../service/id-store.service';
import { ISchool } from 'app/entities/school/school.model';
import { SchoolService } from 'app/entities/school/service/school.service';

@Component({
  selector: 'jhi-id-store-update',
  templateUrl: './id-store-update.component.html',
})
export class IdStoreUpdateComponent implements OnInit {
  isSaving = false;

  schoolsSharedCollection: ISchool[] = [];

  editForm = this.fb.group({
    id: [],
    entrytype: [null, [Validators.required]],
    lastGeneratedId: [null, [Validators.required]],
    startId: [],
    createDate: [],
    lastModified: [],
    cancelDate: [],
    school: [],
  });

  constructor(
    protected idStoreService: IdStoreService,
    protected schoolService: SchoolService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ idStore }) => {
      this.updateForm(idStore);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const idStore = this.createFromForm();
    if (idStore.id !== undefined) {
      this.subscribeToSaveResponse(this.idStoreService.update(idStore));
    } else {
      this.subscribeToSaveResponse(this.idStoreService.create(idStore));
    }
  }

  trackSchoolById(index: number, item: ISchool): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IIdStore>>): void {
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

  protected updateForm(idStore: IIdStore): void {
    this.editForm.patchValue({
      id: idStore.id,
      entrytype: idStore.entrytype,
      lastGeneratedId: idStore.lastGeneratedId,
      startId: idStore.startId,
      createDate: idStore.createDate,
      lastModified: idStore.lastModified,
      cancelDate: idStore.cancelDate,
      school: idStore.school,
    });

    this.schoolsSharedCollection = this.schoolService.addSchoolToCollectionIfMissing(this.schoolsSharedCollection, idStore.school);
  }

  protected loadRelationshipsOptions(): void {
    this.schoolService
      .query()
      .pipe(map((res: HttpResponse<ISchool[]>) => res.body ?? []))
      .pipe(map((schools: ISchool[]) => this.schoolService.addSchoolToCollectionIfMissing(schools, this.editForm.get('school')!.value)))
      .subscribe((schools: ISchool[]) => (this.schoolsSharedCollection = schools));
  }

  protected createFromForm(): IIdStore {
    return {
      ...new IdStore(),
      id: this.editForm.get(['id'])!.value,
      entrytype: this.editForm.get(['entrytype'])!.value,
      lastGeneratedId: this.editForm.get(['lastGeneratedId'])!.value,
      startId: this.editForm.get(['startId'])!.value,
      createDate: this.editForm.get(['createDate'])!.value,
      lastModified: this.editForm.get(['lastModified'])!.value,
      cancelDate: this.editForm.get(['cancelDate'])!.value,
      school: this.editForm.get(['school'])!.value,
    };
  }
}
