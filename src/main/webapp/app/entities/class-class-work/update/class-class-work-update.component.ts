import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IClassClassWork, ClassClassWork } from '../class-class-work.model';
import { ClassClassWorkService } from '../service/class-class-work.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { IChapterSection } from 'app/entities/chapter-section/chapter-section.model';
import { ChapterSectionService } from 'app/entities/chapter-section/service/chapter-section.service';
import { ISubjectChapter } from 'app/entities/subject-chapter/subject-chapter.model';

@Component({
  selector: 'jhi-class-class-work-update',
  templateUrl: './class-class-work-update.component.html',
})
export class ClassClassWorkUpdateComponent implements OnInit {
  isSaving = false;
	subjectChapters:ISubjectChapter[]=[];
	selectedChapter:any;
	isLoading = false;

  chapterSectionsSharedCollection: IChapterSection[] = [];

  editForm = this.fb.group({
    id: [],
    schoolDate: [null, [Validators.required]],
    studentAssignmentType: [null, [Validators.required]],
    classWorkText: [null, [Validators.required, Validators.maxLength(1000)]],
    classWorkFile: [],
    classWorkFileContentType: [],
    classWorkFileLink: [],
    assign: [],
    createDate: [],
    lastModified: [],
    cancelDate: [],
    chapterSection: [],
  });

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected classClassWorkService: ClassClassWorkService,
    protected chapterSectionService: ChapterSectionService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ classClassWork }) => {
      this.updateForm(classClassWork);

      // this.loadRelationshipsOptions();
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }
 loadAllSectionForAChapter(): void {
        this.isLoading = true;
    this.chapterSectionService.query({ 'subjectChapterId.equals': this.selectedChapter }).subscribe(
      (res: HttpResponse<IChapterSection[]>) => {
        this.isLoading = false;
        this.chapterSectionsSharedCollection = res.body ?? [];
        },
      () => {
      	alert("Error occured while fetching the class subjects sections");
        this.isLoading = false;
      }
    );
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
	if(this.editForm.get(['chapterSection'])!.value){

		    this.isSaving = true;
		    const classClassWork = this.createFromForm();
		    if (classClassWork.id !== undefined) {
		      this.subscribeToSaveResponse(this.classClassWorkService.update(classClassWork));
		    } else {
		      this.subscribeToSaveResponse(this.classClassWorkService.create(classClassWork));
		    }
	}else{
				alert("Select Section")
			}
  }
loadAllChapterForASubjectCallback = (subjectChaptersSharedCollection: ISubjectChapter[]): void => {
  // callback code here
   this.subjectChapters=subjectChaptersSharedCollection;
  }
  trackChapterSectionById(index: number, item: IChapterSection): number {
    return item.id!;
  }
trackFilterChapterByIdCommon(index: number, item: ISubjectChapter): number {
    return item.id!;
  }
  protected subscribeToSaveResponse(result: Observable<HttpResponse<IClassClassWork>>): void {
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

  protected updateForm(classClassWork: IClassClassWork): void {
    this.editForm.patchValue({
      id: classClassWork.id,
      schoolDate: classClassWork.schoolDate,
      studentAssignmentType: classClassWork.studentAssignmentType,
      classWorkText: classClassWork.classWorkText,
      classWorkFile: classClassWork.classWorkFile,
      classWorkFileContentType: classClassWork.classWorkFileContentType,
      classWorkFileLink: classClassWork.classWorkFileLink,
      assign: classClassWork.assign,
      createDate: classClassWork.createDate,
      lastModified: classClassWork.lastModified,
      cancelDate: classClassWork.cancelDate,
      chapterSection: classClassWork.chapterSection,
    });

    this.chapterSectionsSharedCollection = this.chapterSectionService.addChapterSectionToCollectionIfMissing(
      this.chapterSectionsSharedCollection,
      classClassWork.chapterSection
    );
  }

  protected loadRelationshipsOptions(): void {
    this.chapterSectionService
      .query()
      .pipe(map((res: HttpResponse<IChapterSection[]>) => res.body ?? []))
      .pipe(
        map((chapterSections: IChapterSection[]) =>
          this.chapterSectionService.addChapterSectionToCollectionIfMissing(chapterSections, this.editForm.get('chapterSection')!.value)
        )
      )
      .subscribe((chapterSections: IChapterSection[]) => (this.chapterSectionsSharedCollection = chapterSections));
  }

  protected createFromForm(): IClassClassWork {
    return {
      ...new ClassClassWork(),
      id: this.editForm.get(['id'])!.value,
      schoolDate: this.editForm.get(['schoolDate'])!.value,
      studentAssignmentType: this.editForm.get(['studentAssignmentType'])!.value,
      classWorkText: this.editForm.get(['classWorkText'])!.value,
      classWorkFileContentType: this.editForm.get(['classWorkFileContentType'])!.value,
      classWorkFile: this.editForm.get(['classWorkFile'])!.value,
      classWorkFileLink: this.editForm.get(['classWorkFileLink'])!.value,
      assign: this.editForm.get(['assign'])!.value,
      createDate: this.editForm.get(['createDate'])!.value,
      lastModified: this.editForm.get(['lastModified'])!.value,
      cancelDate: this.editForm.get(['cancelDate'])!.value,
      chapterSection: this.editForm.get(['chapterSection'])!.value,
    };
  }
}
