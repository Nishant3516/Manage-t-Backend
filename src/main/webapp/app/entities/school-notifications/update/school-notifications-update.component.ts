import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { ISchoolNotifications, SchoolNotifications } from '../school-notifications.model';
import { SchoolNotificationsService } from '../service/school-notifications.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { ISchoolClass } from 'app/entities/school-class/school-class.model';
import { SchoolClassService } from 'app/entities/school-class/service/school-class.service';

@Component({
  selector: 'jhi-school-notifications-update',
  templateUrl: './school-notifications-update.component.html',
})
export class SchoolNotificationsUpdateComponent implements OnInit {
  isSaving = false;

  schoolClassesSharedCollection: ISchoolClass[] = [];

  editForm = this.fb.group({
    id: [],
    notificationTitle: [null, [Validators.required]],
    notificationDetails: [null, [Validators.required]],
    notificationFile: [],
    notificationFileContentType: [],
    notificationFileLink: [],
    showTillDate: [],
    createDate: [],
    lastModified: [],
    cancelDate: [],
    schoolClasses: [],
  });

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected schoolNotificationsService: SchoolNotificationsService,
    protected schoolClassService: SchoolClassService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ schoolNotifications }) => {
      this.updateForm(schoolNotifications);

      this.loadRelationshipsOptions();
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }

  setFileData(event: Event, field: string, isImage: boolean): void {
    this.dataUtils.loadFileToForm(event, this.editForm, field, isImage).subscribe({
      error: (err: FileLoadError) =>
        this.eventManager.broadcast(
          new EventWithContent<AlertError>('manageitApp.error', { ...err, key: 'error.file.' + err.key })
        ),
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const schoolNotifications = this.createFromForm();
    if (schoolNotifications.id !== undefined) {
      this.subscribeToSaveResponse(this.schoolNotificationsService.update(schoolNotifications));
    } else {
      this.subscribeToSaveResponse(this.schoolNotificationsService.create(schoolNotifications));
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

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ISchoolNotifications>>): void {
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

  protected updateForm(schoolNotifications: ISchoolNotifications): void {
    this.editForm.patchValue({
      id: schoolNotifications.id,
      notificationTitle: schoolNotifications.notificationTitle,
      notificationDetails: schoolNotifications.notificationDetails,
      notificationFile: schoolNotifications.notificationFile,
      notificationFileContentType: schoolNotifications.notificationFileContentType,
      notificationFileLink: schoolNotifications.notificationFileLink,
      showTillDate: schoolNotifications.showTillDate,
      createDate: schoolNotifications.createDate,
      lastModified: schoolNotifications.lastModified,
      cancelDate: schoolNotifications.cancelDate,
      schoolClasses: schoolNotifications.schoolClasses,
    });

    this.schoolClassesSharedCollection = this.schoolClassService.addSchoolClassToCollectionIfMissing(
      this.schoolClassesSharedCollection,
      ...(schoolNotifications.schoolClasses ?? [])
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

  protected createFromForm(): ISchoolNotifications {
    return {
      ...new SchoolNotifications(),
      id: this.editForm.get(['id'])!.value,
      notificationTitle: this.editForm.get(['notificationTitle'])!.value,
      notificationDetails: this.editForm.get(['notificationDetails'])!.value,
      notificationFileContentType: this.editForm.get(['notificationFileContentType'])!.value,
      notificationFile: this.editForm.get(['notificationFile'])!.value,
      notificationFileLink: this.editForm.get(['notificationFileLink'])!.value,
      showTillDate: this.editForm.get(['showTillDate'])!.value,
      createDate: this.editForm.get(['createDate'])!.value,
      lastModified: this.editForm.get(['lastModified'])!.value,
      cancelDate: this.editForm.get(['cancelDate'])!.value,
      schoolClasses: this.editForm.get(['schoolClasses'])!.value,
    };
  }
}
