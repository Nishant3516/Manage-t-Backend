import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { ISchoolUser, SchoolUser } from '../school-user.model';
import { SchoolUserService } from '../service/school-user.service';
import { ISchoolClass } from 'app/entities/school-class/school-class.model';
import { SchoolClassService } from 'app/entities/school-class/service/school-class.service';
import { IClassSubject } from 'app/entities/class-subject/class-subject.model';
import { ClassSubjectService } from 'app/entities/class-subject/service/class-subject.service';

@Component({
  selector: 'jhi-school-user-update',
  templateUrl: './school-user-update.component.html',
})
export class SchoolUserUpdateComponent implements OnInit {
  isSaving = false;

  schoolClassesSharedCollection: ISchoolClass[] = [];
  classSubjectsSharedCollection: IClassSubject[] = [];

  editForm = this.fb.group({
    id: [],
    loginName: [null, [Validators.required]],
    password: [null, [Validators.required]],
    userEmail: [null, [Validators.required]],
    extraInfo: [],
    activated: [],
    userType: [],
    createDate: [],
    lastModified: [],
    cancelDate: [],
    schoolClasses: [],
    classSubjects: [],
  });

  constructor(
    protected schoolUserService: SchoolUserService,
    protected schoolClassService: SchoolClassService,
    protected classSubjectService: ClassSubjectService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ schoolUser }) => {
      this.updateForm(schoolUser);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const schoolUser = this.createFromForm();
    if (schoolUser.id !== undefined) {
      this.subscribeToSaveResponse(this.schoolUserService.update(schoolUser));
    } else {
      this.subscribeToSaveResponse(this.schoolUserService.create(schoolUser));
    }
  }

  trackSchoolClassById(index: number, item: ISchoolClass): number {
    return item.id!;
  }

  trackClassSubjectById(index: number, item: IClassSubject): number {
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

  getSelectedClassSubject(option: IClassSubject, selectedVals?: IClassSubject[]): IClassSubject {
    if (selectedVals) {
      for (const selectedVal of selectedVals) {
        if (option.id === selectedVal.id) {
          return selectedVal;
        }
      }
    }
    return option;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ISchoolUser>>): void {
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

  protected updateForm(schoolUser: ISchoolUser): void {
    this.editForm.patchValue({
      id: schoolUser.id,
      loginName: schoolUser.loginName,
      password: schoolUser.password,
      userEmail: schoolUser.userEmail,
      extraInfo: schoolUser.extraInfo,
      activated: schoolUser.activated,
      userType: schoolUser.userType,
      createDate: schoolUser.createDate,
      lastModified: schoolUser.lastModified,
      cancelDate: schoolUser.cancelDate,
      schoolClasses: schoolUser.schoolClasses,
      classSubjects: schoolUser.classSubjects,
    });

    this.schoolClassesSharedCollection = this.schoolClassService.addSchoolClassToCollectionIfMissing(
      this.schoolClassesSharedCollection,
      ...(schoolUser.schoolClasses ?? [])
    );
    this.classSubjectsSharedCollection = this.classSubjectService.addClassSubjectToCollectionIfMissing(
      this.classSubjectsSharedCollection,
      ...(schoolUser.classSubjects ?? [])
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

    this.classSubjectService
      .query()
      .pipe(map((res: HttpResponse<IClassSubject[]>) => res.body ?? []))
      .pipe(
        map((classSubjects: IClassSubject[]) =>
          this.classSubjectService.addClassSubjectToCollectionIfMissing(classSubjects, ...(this.editForm.get('classSubjects')!.value ?? []))
        )
      )
      .subscribe((classSubjects: IClassSubject[]) => (this.classSubjectsSharedCollection = classSubjects));
  }

  protected createFromForm(): ISchoolUser {
    return {
      ...new SchoolUser(),
      id: this.editForm.get(['id'])!.value,
      loginName: this.editForm.get(['loginName'])!.value,
      password: this.editForm.get(['password'])!.value,
      userEmail: this.editForm.get(['userEmail'])!.value,
      extraInfo: this.editForm.get(['extraInfo'])!.value,
      activated: this.editForm.get(['activated'])!.value,
      userType: this.editForm.get(['userType'])!.value,
      createDate: this.editForm.get(['createDate'])!.value,
      lastModified: this.editForm.get(['lastModified'])!.value,
      cancelDate: this.editForm.get(['cancelDate'])!.value,
      schoolClasses: this.editForm.get(['schoolClasses'])!.value,
      classSubjects: this.editForm.get(['classSubjects'])!.value,
    };
  }
}
