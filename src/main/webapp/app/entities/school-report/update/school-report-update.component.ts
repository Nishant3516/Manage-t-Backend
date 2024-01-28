import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { ISchoolReport, SchoolReport } from '../school-report.model';
import { SchoolReportService } from '../service/school-report.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { ISchoolClass } from 'app/entities/school-class/school-class.model';
import { SchoolClassService } from 'app/entities/school-class/service/school-class.service';

@Component({
  selector: 'jhi-school-report-update',
  templateUrl: './school-report-update.component.html',
})
export class SchoolReportUpdateComponent implements OnInit {
  isSaving = false;
  isLoading = false;

  schoolClassesSharedCollection: ISchoolClass[] = [];
  schoolReportGenerated: ISchoolReport = {};

  editForm = this.fb.group({
    id: [],
    reportType: [null, [Validators.required]],
    startDate: [null, [Validators.required]],
    endDate: [null, [Validators.required]],
    reportFile: [],
    reportFileContentType: [],
    createDate: [],
    lastModified: [],
    cancelDate: [],
    schoolClasses: [],
  });

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected schoolReportService: SchoolReportService,
    protected schoolClassService: SchoolClassService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ schoolReport }) => {
      this.updateForm(schoolReport);

      this.loadRelationshipsOptions();
    });
  }
  loadAllStudentsForAClass(): void {
    this.schoolReportGenerated = {};
    const schoolReport = this.createFromForm();
    this.schoolReportService.create(schoolReport).subscribe(
      (res: HttpResponse<ISchoolReport>) => {
        this.schoolReportGenerated = res.body ?? {};
      },
      () => {
        alert('Failed to get reports');
      }
    );
  }
  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }
  trackId(index: number, item: ISchoolReport): number {
    return item.id!;
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
	this.isLoading =true;
    const schoolReport = this.createFromForm();
    if (schoolReport.id !== undefined) {
      this.subscribeToSaveResponse(this.schoolReportService.update(schoolReport));
    } else {
      this.subscribeToSaveResponse(this.schoolReportService.create(schoolReport));
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

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ISchoolReport>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    // this.previousState();
    //console.log("")
	this.isLoading=false;
	this.isSaving=false;
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(schoolReport: ISchoolReport): void {
    this.editForm.patchValue({
      id: schoolReport.id,
      reportType: schoolReport.reportType,
      startDate: schoolReport.startDate,
      endDate: schoolReport.endDate,
      reportFile: schoolReport.reportFile,
      reportFileContentType: schoolReport.reportFileContentType,
      createDate: schoolReport.createDate,
      lastModified: schoolReport.lastModified,
      cancelDate: schoolReport.cancelDate,
      schoolClasses: schoolReport.schoolClasses,
    });

    this.schoolClassesSharedCollection = this.schoolClassService.addSchoolClassToCollectionIfMissing(
      this.schoolClassesSharedCollection,
      ...(schoolReport.schoolClasses ?? [])
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

  protected createFromForm(): ISchoolReport {
    return {
      ...new SchoolReport(),
      id: this.editForm.get(['id'])!.value,
      reportType: this.editForm.get(['reportType'])!.value,
      startDate: this.editForm.get(['startDate'])!.value,
      endDate: this.editForm.get(['endDate'])!.value,
      reportFileContentType: this.editForm.get(['reportFileContentType'])!.value,
      reportFile: this.editForm.get(['reportFile'])!.value,
      createDate: this.editForm.get(['createDate'])!.value,
      lastModified: this.editForm.get(['lastModified'])!.value,
      cancelDate: this.editForm.get(['cancelDate'])!.value,
      schoolClasses: this.editForm.get(['schoolClasses'])!.value,
    };
  }
}
