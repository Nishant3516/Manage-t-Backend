import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { ISchoolPictureGallery, SchoolPictureGallery } from '../school-picture-gallery.model';
import { SchoolPictureGalleryService } from '../service/school-picture-gallery.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { ISchoolClass } from 'app/entities/school-class/school-class.model';
import { SchoolClassService } from 'app/entities/school-class/service/school-class.service';

@Component({
  selector: 'jhi-school-picture-gallery-update',
  templateUrl: './school-picture-gallery-update.component.html',
})
export class SchoolPictureGalleryUpdateComponent implements OnInit {
  isSaving = false;

  schoolClassesSharedCollection: ISchoolClass[] = [];

  editForm = this.fb.group({
    id: [],
    pictureTitle: [null, [Validators.required]],
    pictureDescription: [],
    pictureFile: [null, [Validators.required]],
    pictureFileContentType: [],
    pictureLink: [],
    createDate: [],
    lastModified: [],
    cancelDate: [],
    schoolClasses: [],
  });

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected schoolPictureGalleryService: SchoolPictureGalleryService,
    protected schoolClassService: SchoolClassService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ schoolPictureGallery }) => {
      this.updateForm(schoolPictureGallery);

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
    const schoolPictureGallery = this.createFromForm();
    if (schoolPictureGallery.id !== undefined) {
      this.subscribeToSaveResponse(this.schoolPictureGalleryService.update(schoolPictureGallery));
    } else {
      this.subscribeToSaveResponse(this.schoolPictureGalleryService.create(schoolPictureGallery));
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

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ISchoolPictureGallery>>): void {
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

  protected updateForm(schoolPictureGallery: ISchoolPictureGallery): void {
    this.editForm.patchValue({
      id: schoolPictureGallery.id,
      pictureTitle: schoolPictureGallery.pictureTitle,
      pictureDescription: schoolPictureGallery.pictureDescription,
      pictureFile: schoolPictureGallery.pictureFile,
      pictureFileContentType: schoolPictureGallery.pictureFileContentType,
      pictureLink: schoolPictureGallery.pictureLink,
      createDate: schoolPictureGallery.createDate,
      lastModified: schoolPictureGallery.lastModified,
      cancelDate: schoolPictureGallery.cancelDate,
      schoolClasses: schoolPictureGallery.schoolClasses,
    });

    this.schoolClassesSharedCollection = this.schoolClassService.addSchoolClassToCollectionIfMissing(
      this.schoolClassesSharedCollection,
      ...(schoolPictureGallery.schoolClasses ?? [])
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

  protected createFromForm(): ISchoolPictureGallery {
    return {
      ...new SchoolPictureGallery(),
      id: this.editForm.get(['id'])!.value,
      pictureTitle: this.editForm.get(['pictureTitle'])!.value,
      pictureDescription: this.editForm.get(['pictureDescription'])!.value,
      pictureFileContentType: this.editForm.get(['pictureFileContentType'])!.value,
      pictureFile: this.editForm.get(['pictureFile'])!.value,
      pictureLink: this.editForm.get(['pictureLink'])!.value,
      createDate: this.editForm.get(['createDate'])!.value,
      lastModified: this.editForm.get(['lastModified'])!.value,
      cancelDate: this.editForm.get(['cancelDate'])!.value,
      schoolClasses: this.editForm.get(['schoolClasses'])!.value,
    };
  }
}
