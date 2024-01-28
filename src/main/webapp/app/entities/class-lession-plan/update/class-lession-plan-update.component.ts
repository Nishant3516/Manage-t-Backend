import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IClassLessionPlan, ClassLessionPlan } from '../class-lession-plan.model';
import { ClassLessionPlanService } from '../service/class-lession-plan.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { IChapterSection } from 'app/entities/chapter-section/chapter-section.model';
import { ChapterSectionService } from 'app/entities/chapter-section/service/chapter-section.service';
import { ISchoolClass } from 'app/entities/school-class/school-class.model';
import { SchoolClassService } from 'app/entities/school-class/service/school-class.service';
import { IClassSubject } from 'app/entities/class-subject/class-subject.model';
import { ClassSubjectService } from 'app/entities/class-subject/service/class-subject.service';
import { ISubjectChapter } from 'app/entities/subject-chapter/subject-chapter.model';
import { SubjectChapterService } from 'app/entities/subject-chapter/service/subject-chapter.service';

@Component({
  selector: 'jhi-class-lession-plan-update',
  templateUrl: './class-lession-plan-update.component.html',
})
export class ClassLessionPlanUpdateComponent implements OnInit {
  isSaving = false;
 isLoading = false;

  chapterSectionsSharedCollection: IChapterSection[] = [];
  schoolClassesSharedCollection: ISchoolClass[] = [];
  classSubjectsSharedCollection: IClassSubject[] = [];
  subjectChaptersSharedCollection: ISubjectChapter[] = [];

  editForm = this.fb.group({
    id: [],
    schoolDate: [null, [Validators.required]],
    classWorkText: [null, [Validators.required, Validators.maxLength(1000)]],
    homeWorkText: [null, [Validators.required, Validators.maxLength(1000)]],
//    workStatus: [null, [Validators.required]],
    lesionPlanFile: [],
    lesionPlanFileContentType: [],
    lessionPlanFileLink: [],
    createDate: [],
    lastModified: [],
    cancelDate: [],
    chapterSection: [],
    schoolClass: [],
    classSubject: [],
    subjectChapter: [],
  });

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected classLessionPlanService: ClassLessionPlanService,
    protected chapterSectionService: ChapterSectionService,
    protected schoolClassService: SchoolClassService,
    protected classSubjectService: ClassSubjectService,
    protected subjectChapterService: SubjectChapterService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ classLessionPlan }) => {
      this.updateForm(classLessionPlan);

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

	if(this.editForm.get(['chapterSection'])!.value&&this.editForm.get(['schoolClass'])!.value&&this.editForm.get(['classSubject'])!.value&&this.editForm.get(['subjectChapter'])!.value){
   	 this.isSaving = true;
		    const classLessionPlan = this.createFromForm();
		    if (classLessionPlan.id !== undefined) {
		      this.subscribeToSaveResponse(this.classLessionPlanService.update(classLessionPlan));
		    } else {
		      this.subscribeToSaveResponse(this.classLessionPlanService.create(classLessionPlan));
		    }
	}else{
		alert("Mandatory field missing")
	}
  }

  trackChapterSectionById(index: number, item: IChapterSection): number {
    return item.id!;
  }

  trackSchoolClassById(index: number, item: ISchoolClass): number {
    return item.id!;
  }

  trackClassSubjectById(index: number, item: IClassSubject): number {
    return item.id!;
  }

  trackSubjectChapterById(index: number, item: ISubjectChapter): number {
    return item.id!;
  }


loadAllSubjects(): void {
        const classId = this.editForm.get('schoolClass')!.value?.id ? this.editForm.get('schoolClass')!.value.id : "null"
        this.isLoading = true;
    this.classSubjectService.query({ 'schoolClassId.equals': classId }).subscribe(
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
  loadAllChaptersForASubject(): void {
  
          const subjectId = this.editForm.get('classSubject')!.value?.id ? this.editForm.get('classSubject')!.value.id : "null"

        this.isLoading = true;
	    this.subjectChapterService.query({ 'classSubjectId.equals': subjectId }).subscribe(
      (res: HttpResponse<ISubjectChapter[]>) => {
        this.isLoading = false;
        this.subjectChaptersSharedCollection = res.body ?? [];
       },
      () => {
      	alert("Error occured while fetching the class subjects ");
        this.isLoading = false;
      }
    );
  }
  
  loadAllSectionForAChapter(): void {
              const chapterId = this.editForm.get('subjectChapter')!.value?.id ? this.editForm.get('subjectChapter')!.value.id : "null"

        this.isLoading = true;
    this.chapterSectionService.query({ 'subjectChapterId.equals': chapterId }).subscribe(
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
  protected subscribeToSaveResponse(result: Observable<HttpResponse<IClassLessionPlan>>): void {
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

  protected updateForm(classLessionPlan: IClassLessionPlan): void {
    this.editForm.patchValue({
      id: classLessionPlan.id,
      schoolDate: classLessionPlan.schoolDate,
      classWorkText: classLessionPlan.classWorkText,
      homeWorkText: classLessionPlan.homeWorkText,
//      workStatus: classLessionPlan.workStatus,
      lesionPlanFile: classLessionPlan.lesionPlanFile,
      lesionPlanFileContentType: classLessionPlan.lesionPlanFileContentType,
      lessionPlanFileLink: classLessionPlan.lessionPlanFileLink,
      createDate: classLessionPlan.createDate,
      lastModified: classLessionPlan.lastModified,
      cancelDate: classLessionPlan.cancelDate,
      chapterSection: classLessionPlan.chapterSection,
      schoolClass: classLessionPlan.schoolClass,
      classSubject: classLessionPlan.classSubject,
      subjectChapter: classLessionPlan.subjectChapter,
    });

    this.chapterSectionsSharedCollection = this.chapterSectionService.addChapterSectionToCollectionIfMissing(
      this.chapterSectionsSharedCollection,
      classLessionPlan.chapterSection
    );
    this.schoolClassesSharedCollection = this.schoolClassService.addSchoolClassToCollectionIfMissing(
      this.schoolClassesSharedCollection,
      classLessionPlan.schoolClass
    );
    this.classSubjectsSharedCollection = this.classSubjectService.addClassSubjectToCollectionIfMissing(
      this.classSubjectsSharedCollection,
      classLessionPlan.classSubject
    );
    this.subjectChaptersSharedCollection = this.subjectChapterService.addSubjectChapterToCollectionIfMissing(
      this.subjectChaptersSharedCollection,
      classLessionPlan.subjectChapter
    );
  }

  protected loadRelationshipsOptions(): void {

    this.schoolClassService
      .query()
      .pipe(map((res: HttpResponse<ISchoolClass[]>) => res.body ?? []))
      .pipe(
        map((schoolClasses: ISchoolClass[]) =>
          this.schoolClassService.addSchoolClassToCollectionIfMissing(schoolClasses, this.editForm.get('schoolClass')!.value)
        )
      )
      .subscribe((schoolClasses: ISchoolClass[]) => (this.schoolClassesSharedCollection = schoolClasses));

   }

  protected createFromForm(): IClassLessionPlan {
    return {
      ...new ClassLessionPlan(),
      id: this.editForm.get(['id'])!.value,
      schoolDate: this.editForm.get(['schoolDate'])!.value,
      classWorkText: this.editForm.get(['classWorkText'])!.value,
      homeWorkText: this.editForm.get(['homeWorkText'])!.value,
//      workStatus: this.editForm.get(['workStatus'])!.value,
      lesionPlanFileContentType: this.editForm.get(['lesionPlanFileContentType'])!.value,
      lesionPlanFile: this.editForm.get(['lesionPlanFile'])!.value,
      lessionPlanFileLink: this.editForm.get(['lessionPlanFileLink'])!.value,
      createDate: this.editForm.get(['createDate'])!.value,
      lastModified: this.editForm.get(['lastModified'])!.value,
      cancelDate: this.editForm.get(['cancelDate'])!.value,
      chapterSection: this.editForm.get(['chapterSection'])!.value,
      schoolClass: this.editForm.get(['schoolClass'])!.value,
      classSubject: this.editForm.get(['classSubject'])!.value,
      subjectChapter: this.editForm.get(['subjectChapter'])!.value,
    };
  }
}
