import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { ISchoolVideoGallery, SchoolVideoGallery } from '../school-video-gallery.model';
import { SchoolVideoGalleryService } from '../service/school-video-gallery.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { ISchoolClass } from 'app/entities/school-class/school-class.model';
import { SchoolClassService } from 'app/entities/school-class/service/school-class.service';

@Component({
  selector: 'jhi-school-video-gallery-update',
  templateUrl: './school-video-gallery-update.component.html',
})
export class SchoolVideoGalleryUpdateComponent implements OnInit {
  isSaving = false;

  schoolClassesSharedCollection: ISchoolClass[] = [];

  editForm = this.fb.group({
    id: [],
    videoTitle: [null, [Validators.required]],
    videoDescription: [],
    videoFile: [null, [Validators.required]],
    videoFileContentType: [],
    videoLink: [],
    createDate: [],
    lastModified: [],
    cancelDate: [],
    schoolClasses: [],
  });

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected schoolVideoGalleryService: SchoolVideoGalleryService,
    protected schoolClassService: SchoolClassService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ schoolVideoGallery }) => {
      this.updateForm(schoolVideoGallery);

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
    const schoolVideoGallery = this.createFromForm();
    if (schoolVideoGallery.id !== undefined) {
      this.subscribeToSaveResponse(this.schoolVideoGalleryService.update(schoolVideoGallery));
    } else {
      this.subscribeToSaveResponse(this.schoolVideoGalleryService.create(schoolVideoGallery));
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

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ISchoolVideoGallery>>): void {
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

  protected updateForm(schoolVideoGallery: ISchoolVideoGallery): void {
    this.editForm.patchValue({
      id: schoolVideoGallery.id,
      videoTitle: schoolVideoGallery.videoTitle,
      videoDescription: schoolVideoGallery.videoDescription,
      videoFile: schoolVideoGallery.videoFile,
      videoFileContentType: schoolVideoGallery.videoFileContentType,
      videoLink: schoolVideoGallery.videoLink,
      createDate: schoolVideoGallery.createDate,
      lastModified: schoolVideoGallery.lastModified,
      cancelDate: schoolVideoGallery.cancelDate,
      schoolClasses: schoolVideoGallery.schoolClasses,
    });

    this.schoolClassesSharedCollection = this.schoolClassService.addSchoolClassToCollectionIfMissing(
      this.schoolClassesSharedCollection,
      ...(schoolVideoGallery.schoolClasses ?? [])
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

  protected createFromForm(): ISchoolVideoGallery {
    return {
      ...new SchoolVideoGallery(),
      id: this.editForm.get(['id'])!.value,
      videoTitle: this.editForm.get(['videoTitle'])!.value,
      videoDescription: this.editForm.get(['videoDescription'])!.value,
      videoFileContentType: this.editForm.get(['videoFileContentType'])!.value,
      videoFile: this.editForm.get(['videoFile'])!.value,
      videoLink: this.editForm.get(['videoLink'])!.value,
      createDate: this.editForm.get(['createDate'])!.value,
      lastModified: this.editForm.get(['lastModified'])!.value,
      cancelDate: this.editForm.get(['cancelDate'])!.value,
      schoolClasses: this.editForm.get(['schoolClasses'])!.value,
    };
  }
}
