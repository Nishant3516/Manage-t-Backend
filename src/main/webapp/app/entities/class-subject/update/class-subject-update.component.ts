import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IClassSubject, ClassSubject } from '../class-subject.model';
import { ClassSubjectService } from '../service/class-subject.service';
import { ISchoolClass } from 'app/entities/school-class/school-class.model';
import { SchoolClassService } from 'app/entities/school-class/service/school-class.service';

@Component({
  selector: 'jhi-class-subject-update',
  templateUrl: './class-subject-update.component.html',
})
export class ClassSubjectUpdateComponent implements OnInit {
  isSaving = false;

  schoolClassesSharedCollection: ISchoolClass[] = [];

  editForm = this.fb.group({
    id: [],
    subjectName: [null, [Validators.required]],
    createDate: [],
    lastModified: [],
    cancelDate: [],
    schoolClasses: [],
  });

  constructor(
    protected classSubjectService: ClassSubjectService,
    protected schoolClassService: SchoolClassService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ classSubject }) => {
      this.updateForm(classSubject);

      this.loadRelationshipsOptions();
      
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
   const classesSelected =this.editForm.get(['schoolClasses'])!.value;
   if(classesSelected&&classesSelected.length>1){
   	alert(`Only one class can be selected `);
   	return;
   }
   
    this.isSaving = true;
    const classSubject = this.createFromForm();
    if (classSubject.id !== undefined) {
      this.subscribeToSaveResponse(this.classSubjectService.update(classSubject));
    } else {
      this.subscribeToSaveResponse(this.classSubjectService.create(classSubject));
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

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IClassSubject>>): void {
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

  protected updateForm(classSubject: IClassSubject): void {
    this.editForm.patchValue({
      id: classSubject.id,
      subjectName: classSubject.subjectName,
      createDate: classSubject.createDate,
      lastModified: classSubject.lastModified,
      cancelDate: classSubject.cancelDate,
      schoolClasses: classSubject.schoolClasses,
    });

    this.schoolClassesSharedCollection = this.schoolClassService.addSchoolClassToCollectionIfMissing(
      this.schoolClassesSharedCollection,
      ...(classSubject.schoolClasses ?? [])
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

  protected createFromForm(): IClassSubject {
  
    return {
   
      ...new ClassSubject(),
      id: this.editForm.get(['id'])!.value,
      subjectName: this.editForm.get(['subjectName'])!.value,
      createDate: this.editForm.get(['createDate'])!.value,
      lastModified: this.editForm.get(['lastModified'])!.value,
      cancelDate: this.editForm.get(['cancelDate'])!.value,
      schoolClasses: this.editForm.get(['schoolClasses'])!.value,
    };
  }
}
