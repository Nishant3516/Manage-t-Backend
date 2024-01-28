import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { ISchoolClass, SchoolClass } from '../school-class.model';
import { SchoolClassService } from '../service/school-class.service';
import { ISchool } from 'app/entities/school/school.model';
import { SchoolService } from 'app/entities/school/service/school.service';

@Component({
  selector: 'jhi-school-class-update',
  templateUrl: './school-class-update.component.html',
})
export class SchoolClassUpdateComponent implements OnInit {
  isSaving = false;

  schoolsSharedCollection: ISchool[] = [];

  editForm = this.fb.group({
    id: [],
    className: [null, [Validators.required]],
    classLongName: [],
    createDate: [],
    lastModified: [],
    cancelDate: [],
    school: [],
  });

  constructor(
    protected schoolClassService: SchoolClassService,
    protected schoolService: SchoolService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ schoolClass }) => {
      this.updateForm(schoolClass);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const schoolClass = this.createFromForm();
    if (schoolClass.id !== undefined) {
      this.subscribeToSaveResponse(this.schoolClassService.update(schoolClass));
    } else {
      this.subscribeToSaveResponse(this.schoolClassService.create(schoolClass));
    }
  }

  trackSchoolById(index: number, item: ISchool): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ISchoolClass>>): void {
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

  protected updateForm(schoolClass: ISchoolClass): void {
    this.editForm.patchValue({
      id: schoolClass.id,
      className: schoolClass.className,
      classLongName: schoolClass.classLongName,
      createDate: schoolClass.createDate,
      lastModified: schoolClass.lastModified,
      cancelDate: schoolClass.cancelDate,
      school: schoolClass.school,
    });

    this.schoolsSharedCollection = this.schoolService.addSchoolToCollectionIfMissing(this.schoolsSharedCollection, schoolClass.school);
  }

  protected loadRelationshipsOptions(): void {
    this.schoolService
      .query()
      .pipe(map((res: HttpResponse<ISchool[]>) => res.body ?? []))
      .pipe(map((schools: ISchool[]) => this.schoolService.addSchoolToCollectionIfMissing(schools, this.editForm.get('school')!.value)))
      .subscribe((schools: ISchool[]) => (this.schoolsSharedCollection = schools));
  }

  protected createFromForm(): ISchoolClass {
    return {
      ...new SchoolClass(),
      id: this.editForm.get(['id'])!.value,
      className: this.editForm.get(['className'])!.value,
      classLongName: this.editForm.get(['classLongName'])!.value,
      createDate: this.editForm.get(['createDate'])!.value,
      lastModified: this.editForm.get(['lastModified'])!.value,
      cancelDate: this.editForm.get(['cancelDate'])!.value,
      school: this.editForm.get(['school'])!.value,
    };
  }
}
