import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IChapterSection, ChapterSection } from '../chapter-section.model';
import { ChapterSectionService } from '../service/chapter-section.service';
import { ISubjectChapter } from 'app/entities/subject-chapter/subject-chapter.model';
import { SubjectChapterService } from 'app/entities/subject-chapter/service/subject-chapter.service';

@Component({
  selector: 'jhi-chapter-section-update',
  templateUrl: './chapter-section-update.component.html',
})
export class ChapterSectionUpdateComponent implements OnInit {
  isSaving = false;
  isLoading = false;
  selectedSubjectId: any;

  subjectChaptersSharedCollection: ISubjectChapter[] = [];

  editForm = this.fb.group({
    id: [],
    sectionName: [null, [Validators.required]],
    sectionNumber: [],
    createDate: [],
    lastModified: [],
    cancelDate: [],
    subjectChapter: [],
  });

  constructor(
    protected chapterSectionService: ChapterSectionService,
    protected subjectChapterService: SubjectChapterService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ chapterSection }) => {
      this.updateForm(chapterSection);

     // this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
	if(this.editForm.get(['subjectChapter'])!.value){
    this.isSaving = true;
    const chapterSection = this.createFromForm();
    if (chapterSection.id !== undefined) {
      this.subscribeToSaveResponse(this.chapterSectionService.update(chapterSection));
    } else {
      this.subscribeToSaveResponse(this.chapterSectionService.create(chapterSection));
    }
}else{
	alert("Select Chapter");
}
  }

 
  loadAllChapterForASubjectCallback = (subjectChaptersSharedCollection: ISubjectChapter[]): void => {
  // callback code here
   this.subjectChaptersSharedCollection=subjectChaptersSharedCollection;
  }
  
  trackSubjectChapterById(index: number, item: ISubjectChapter): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IChapterSection>>): void {
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

  protected updateForm(chapterSection: IChapterSection): void {
    this.editForm.patchValue({
      id: chapterSection.id,
      sectionName: chapterSection.sectionName,
      sectionNumber: chapterSection.sectionNumber,
      createDate: chapterSection.createDate,
      lastModified: chapterSection.lastModified,
      cancelDate: chapterSection.cancelDate,
      subjectChapter: chapterSection.subjectChapter,
    });

    this.subjectChaptersSharedCollection = this.subjectChapterService.addSubjectChapterToCollectionIfMissing(
      this.subjectChaptersSharedCollection,
      chapterSection.subjectChapter
    );
  }

  protected loadRelationshipsOptions(): void {
    this.subjectChapterService
      .query()
      .pipe(map((res: HttpResponse<ISubjectChapter[]>) => res.body ?? []))
      .pipe(
        map((subjectChapters: ISubjectChapter[]) =>
          this.subjectChapterService.addSubjectChapterToCollectionIfMissing(subjectChapters, this.editForm.get('subjectChapter')!.value)
        )
      )
      .subscribe((subjectChapters: ISubjectChapter[]) => (this.subjectChaptersSharedCollection = subjectChapters));
  }

  protected createFromForm(): IChapterSection {
    return {
      ...new ChapterSection(),
      id: this.editForm.get(['id'])!.value,
      sectionName: this.editForm.get(['sectionName'])!.value,
      sectionNumber: this.editForm.get(['sectionNumber'])!.value,
      createDate: this.editForm.get(['createDate'])!.value,
      lastModified: this.editForm.get(['lastModified'])!.value,
      cancelDate: this.editForm.get(['cancelDate'])!.value,
      subjectChapter: this.editForm.get(['subjectChapter'])!.value,
    };
  }
}
