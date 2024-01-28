import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { ISubjectChapter, SubjectChapter } from '../subject-chapter.model';
import { SubjectChapterService } from '../service/subject-chapter.service';
import { IClassSubject } from 'app/entities/class-subject/class-subject.model';
import { ClassSubjectService } from 'app/entities/class-subject/service/class-subject.service';

@Component({
  selector: 'jhi-subject-chapter-update',
  templateUrl: './subject-chapter-update.component.html',
})
export class SubjectChapterUpdateComponent  implements OnInit {
  isSaving = false;
  classSubjectsSharedCollection: IClassSubject[] = [];
  isLoading = false;
 

  editForm = this.fb.group({
    id: [],
    chapterName: [null, [Validators.required]],
    chapterNumber: [],
    createDate: [],
    lastModified: [],
    cancelDate: [],
    classSubject: [],
  });

  constructor(
    protected subjectChapterService: SubjectChapterService,
    protected classSubjectService: ClassSubjectService,
     protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {
  }

 loadAllSubjectsForAClassCallback = (selectedClassId:any): void => {
  // callback code here
   this.isLoading = true;
    this.classSubjectService.query({ 'schoolClassId.equals': selectedClassId }).subscribe(
      (res: HttpResponse<IClassSubject[]>) => {
        this.isLoading = false;
        this.classSubjectsSharedCollection = res.body ?? [];
      },
      () => {
      	alert("Error occured while fetching the class subjects ");
        this.isLoading = false;
      }
    );
  }
  
  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ subjectChapter }) => {
      this.updateForm(subjectChapter);

      // this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
	if(this.editForm.get(['classSubject'])!.value){
    this.isSaving = true;
    const subjectChapter = this.createFromForm();
    if (subjectChapter.id !== undefined) {
      this.subscribeToSaveResponse(this.subjectChapterService.update(subjectChapter));
    } else {
      this.subscribeToSaveResponse(this.subjectChapterService.create(subjectChapter));
    }
}else{
	alert("Select Subject");
}
  }

  trackClassSubjectById(index: number, item: IClassSubject): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ISubjectChapter>>): void {
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

  protected updateForm(subjectChapter: ISubjectChapter): void {
    this.editForm.patchValue({
      id: subjectChapter.id,
      chapterName: subjectChapter.chapterName,
      chapterNumber: subjectChapter.chapterNumber,
      createDate: subjectChapter.createDate,
      lastModified: subjectChapter.lastModified,
      cancelDate: subjectChapter.cancelDate,
      classSubject: subjectChapter.classSubject,
    });

    this.classSubjectsSharedCollection = this.classSubjectService.addClassSubjectToCollectionIfMissing(
      this.classSubjectsSharedCollection,
      subjectChapter.classSubject
    );
  }

  protected loadRelationshipsOptions(): void {
    this.classSubjectService
      .query()
      .pipe(map((res: HttpResponse<IClassSubject[]>) => res.body ?? []))
      .pipe(
        map((classSubjects: IClassSubject[]) =>
          this.classSubjectService.addClassSubjectToCollectionIfMissing(classSubjects, this.editForm.get('classSubject')!.value)
        )
      )
      .subscribe((classSubjects: IClassSubject[]) => (this.classSubjectsSharedCollection = classSubjects));
  }

  protected createFromForm(): ISubjectChapter {
    return {
      ...new SubjectChapter(),
      id: this.editForm.get(['id'])!.value,
      chapterName: this.editForm.get(['chapterName'])!.value,
      chapterNumber: this.editForm.get(['chapterNumber'])!.value,
      createDate: this.editForm.get(['createDate'])!.value,
      lastModified: this.editForm.get(['lastModified'])!.value,
      cancelDate: this.editForm.get(['cancelDate'])!.value,
      classSubject: this.editForm.get(['classSubject'])!.value,
    };
  }
}
