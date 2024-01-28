import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IClassHomeWork, ClassHomeWork } from '../class-home-work.model';
import { ClassHomeWorkService } from '../service/class-home-work.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { IChapterSection } from 'app/entities/chapter-section/chapter-section.model';
import { ISubjectChapter } from 'app/entities/subject-chapter/subject-chapter.model';
import { ChapterSectionService } from 'app/entities/chapter-section/service/chapter-section.service';

@Component({
  selector: 'jhi-class-home-work-update',
  templateUrl: './class-home-work-update.component.html',
})
export class ClassHomeWorkUpdateComponent implements OnInit {
  isSaving = false;
	selectedChapter:any;
	isLoading = false;

  chapterSectionsSharedCollection: IChapterSection[] = [];
	subjectChapters:ISubjectChapter[]=[];
	
  editForm = this.fb.group({
    id: [],
    schoolDate: [null, [Validators.required]],
    studentAssignmentType: [null, [Validators.required]],
    homeWorkText: [null, [Validators.required, Validators.maxLength(1000)]],
    homeWorkFile: [],
    homeWorkFileContentType: [],
    homeWorkFileLink: [],
    assign: [],
    createDate: [],
    lastModified: [],
    cancelDate: [],
    chapterSection: [],
  });

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected classHomeWorkService: ClassHomeWorkService,
    protected chapterSectionService: ChapterSectionService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ classHomeWork }) => {
      this.updateForm(classHomeWork);

     // this.loadRelationshipsOptions();
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
  
 loadAllSectionForAChapter(): void {
        this.isLoading = true;
    this.chapterSectionService.query({ 'subjectChapterId.equals': this.selectedChapter }).subscribe(
      (res: HttpResponse<IChapterSection[]>) => {
        this.isLoading = false;
        this.chapterSectionsSharedCollection = res.body ?? [];
        },
      () => {
      	alert("Error occured while fetching the class subjects ");
        this.isLoading = false;
      }
    );
  }
   loadAllSectionForAChapterCallBack(chapterSectionsSharedCollection:IChapterSection[]): void {
   	this.chapterSectionsSharedCollection=chapterSectionsSharedCollection;
   	
   }
  save(): void {
		if(this.editForm.get(['chapterSection'])!.value){
		    this.isSaving = true;
		    const classHomeWork = this.createFromForm();
		    if (classHomeWork.id !== undefined) {
		      this.subscribeToSaveResponse(this.classHomeWorkService.update(classHomeWork));
		    } else {
		      this.subscribeToSaveResponse(this.classHomeWorkService.create(classHomeWork));
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
  protected subscribeToSaveResponse(result: Observable<HttpResponse<IClassHomeWork>>): void {
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

  protected updateForm(classHomeWork: IClassHomeWork): void {
    this.editForm.patchValue({
      id: classHomeWork.id,
      schoolDate: classHomeWork.schoolDate,
      studentAssignmentType: classHomeWork.studentAssignmentType,
      homeWorkText: classHomeWork.homeWorkText,
      homeWorkFile: classHomeWork.homeWorkFile,
      homeWorkFileContentType: classHomeWork.homeWorkFileContentType,
      homeWorkFileLink: classHomeWork.homeWorkFileLink,
      assign: classHomeWork.assign,
      createDate: classHomeWork.createDate,
      lastModified: classHomeWork.lastModified,
      cancelDate: classHomeWork.cancelDate,
      chapterSection: classHomeWork.chapterSection,
    });

    this.chapterSectionsSharedCollection = this.chapterSectionService.addChapterSectionToCollectionIfMissing(
      this.chapterSectionsSharedCollection,
      classHomeWork.chapterSection
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

  protected createFromForm(): IClassHomeWork {
    return {
      ...new ClassHomeWork(),
      id: this.editForm.get(['id'])!.value,
      schoolDate: this.editForm.get(['schoolDate'])!.value,
      studentAssignmentType: this.editForm.get(['studentAssignmentType'])!.value,
      homeWorkText: this.editForm.get(['homeWorkText'])!.value,
      homeWorkFileContentType: this.editForm.get(['homeWorkFileContentType'])!.value,
      homeWorkFile: this.editForm.get(['homeWorkFile'])!.value,
      homeWorkFileLink: this.editForm.get(['homeWorkFileLink'])!.value,
      assign: this.editForm.get(['assign'])!.value,
      createDate: this.editForm.get(['createDate'])!.value,
      lastModified: this.editForm.get(['lastModified'])!.value,
      cancelDate: this.editForm.get(['cancelDate'])!.value,
      chapterSection: this.editForm.get(['chapterSection'])!.value,
    };
  }
}
