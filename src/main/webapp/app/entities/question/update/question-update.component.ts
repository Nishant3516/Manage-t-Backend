import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IQuestion, Question } from '../question.model';
import { QuestionService } from '../service/question.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { ITag } from 'app/entities/tag/tag.model';
import { TagService } from 'app/entities/tag/service/tag.service';
import { IQuestionType } from 'app/entities/question-type/question-type.model';
import { QuestionTypeService } from 'app/entities/question-type/service/question-type.service';
import { ITenant } from 'app/entities/tenant/tenant.model';
import { TenantService } from 'app/entities/tenant/service/tenant.service';
import { ISchoolClass } from 'app/entities/school-class/school-class.model';
import { SchoolClassService } from 'app/entities/school-class/service/school-class.service';
import { IClassSubject } from 'app/entities/class-subject/class-subject.model';
import { ClassSubjectService } from 'app/entities/class-subject/service/class-subject.service';
import { ISubjectChapter } from 'app/entities/subject-chapter/subject-chapter.model';
import { SubjectChapterService } from 'app/entities/subject-chapter/service/subject-chapter.service';
import { Status } from 'app/entities/enumerations/status.model';
import { Difficulty } from 'app/entities/enumerations/difficulty.model';

@Component({
  selector: 'jhi-question-update',
  templateUrl: './question-update.component.html',
})
export class QuestionUpdateComponent implements OnInit {
  isSaving = false;
  statusValues = Object.keys(Status);
  difficultyValues = Object.keys(Difficulty);

  tagsSharedCollection: ITag[] = [];
  questionTypesSharedCollection: IQuestionType[] = [];
  tenantsSharedCollection: ITenant[] = [];
  schoolClassesSharedCollection: ISchoolClass[] = [];
  classSubjectsSharedCollection: IClassSubject[] = [];
  subjectChaptersSharedCollection: ISubjectChapter[] = [];

  editForm = this.fb.group({
    id: [],
    questionImportFile: [],
    questionImportFileContentType: [],
    questionText: [],
    questionImage: [],
    questionImageContentType: [],
    answerImage: [],
    answerImageContentType: [],
    imageSideBySide: [],
    option1: [],
    option2: [],
    option3: [],
    option4: [],
    option5: [],
    status: [],
    weightage: [],
    difficultyLevel: [],
    createDate: [],
    lastModified: [],
    cancelDate: [],
    tags: [],
    questionType: [],
    tenant: [],
    schoolClass: [],
    classSubject: [],
    subjectChapter: [],
  });

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected questionService: QuestionService,
    protected tagService: TagService,
    protected questionTypeService: QuestionTypeService,
    protected tenantService: TenantService,
    protected schoolClassService: SchoolClassService,
    protected classSubjectService: ClassSubjectService,
    protected subjectChapterService: SubjectChapterService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ question }) => {
      this.updateForm(question);

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
        this.eventManager.broadcast(new EventWithContent<AlertError>('manageitApp.error', { ...err, key: 'error.file.' + err.key })),
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
	if(this.editForm.get(['questionType'])!.value&&this.editForm.get(['schoolClass'])!.value&&this.editForm.get(['classSubject'])!.value&&this.editForm.get(['subjectChapter'])!.value){
	    this.isSaving = true;
	    const question = this.createFromForm();
	    if (question.id !== undefined) {
	      this.subscribeToSaveResponse(this.questionService.update(question));
	    } else {
	      this.subscribeToSaveResponse(this.questionService.create(question));
	    }
	}else{
		alert("Mandatory fields missing")
	}
  }

  trackTagById(index: number, item: ITag): number {
    return item.id!;
  }

  trackQuestionTypeById(index: number, item: IQuestionType): number {
    return item.id!;
  }

  trackTenantById(index: number, item: ITenant): number {
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
	loadAllSubjectsForSelectedClass():void{
		this.editForm.patchValue({
      		classSubject: "",
			subjectChapter: "",
    	});
		   this.classSubjectService
		      .query({ 'schoolClassId.equals': this.editForm.get(['schoolClass'])!.value.id })
		      .pipe(map((res: HttpResponse<IClassSubject[]>) => res.body ?? []))
		      .pipe(
		        map((classSubjects: IClassSubject[]) =>
		          this.classSubjectService.addClassSubjectToCollectionIfMissing(classSubjects, this.editForm.get('classSubject')!.value)
		        )
		      )
		      .subscribe((classSubjects: IClassSubject[]) => (this.classSubjectsSharedCollection = classSubjects));
		
	}
	loadAllChaptersFroASelectedSubject():void{
		this.editForm.patchValue({
			subjectChapter: "",
    	});
		    this.subjectChapterService
		      .query({ 'classSubjectId.equals': this.editForm.get(['classSubject'])!.value.id })
		      .pipe(map((res: HttpResponse<ISubjectChapter[]>) => res.body ?? []))
		      .pipe(
		        map((subjectChapters: ISubjectChapter[]) =>
		          this.subjectChapterService.addSubjectChapterToCollectionIfMissing(subjectChapters, this.editForm.get('subjectChapter')!.value)
		        )
		      )
		      .subscribe((subjectChapters: ISubjectChapter[]) => (this.subjectChaptersSharedCollection = subjectChapters));

	}
  getSelectedTag(option: ITag, selectedVals?: ITag[]): ITag {
    if (selectedVals) {
      for (const selectedVal of selectedVals) {
        if (option.id === selectedVal.id) {
          return selectedVal;
        }
      }
    }
    return option;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IQuestion>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    // this.previousState();
 	this.editForm.patchValue({
      questionText: "",
    });
	alert("Added successfully ")
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(question: IQuestion): void {
    this.editForm.patchValue({
      id: question.id,
      questionImportFile: question.questionImportFile,
      questionImportFileContentType: question.questionImportFileContentType,
      questionText: question.questionText,
      questionImage: question.questionImage,
      questionImageContentType: question.questionImageContentType,
      answerImage: question.answerImage,
      answerImageContentType: question.answerImageContentType,
      imageSideBySide: question.imageSideBySide,
      option1: question.option1,
      option2: question.option2,
      option3: question.option3,
      option4: question.option4,
      option5: question.option5,
      status: question.status,
      weightage: question.weightage,
      difficultyLevel: question.difficultyLevel,
      createDate: question.createDate,
      lastModified: question.lastModified,
      cancelDate: question.cancelDate,
      tags: question.tags,
      questionType: question.questionType,
      tenant: question.tenant,
      schoolClass: question.schoolClass,
      classSubject: question.classSubject,
      subjectChapter: question.subjectChapter,
    });

    this.tagsSharedCollection = this.tagService.addTagToCollectionIfMissing(this.tagsSharedCollection, ...(question.tags ?? []));
    this.questionTypesSharedCollection = this.questionTypeService.addQuestionTypeToCollectionIfMissing(
      this.questionTypesSharedCollection,
      question.questionType
    );
    this.tenantsSharedCollection = this.tenantService.addTenantToCollectionIfMissing(this.tenantsSharedCollection, question.tenant);
    this.schoolClassesSharedCollection = this.schoolClassService.addSchoolClassToCollectionIfMissing(
      this.schoolClassesSharedCollection,
      question.schoolClass
    );
    this.classSubjectsSharedCollection = this.classSubjectService.addClassSubjectToCollectionIfMissing(
      this.classSubjectsSharedCollection,
      question.classSubject
    );
    this.subjectChaptersSharedCollection = this.subjectChapterService.addSubjectChapterToCollectionIfMissing(
      this.subjectChaptersSharedCollection,
      question.subjectChapter
    );
  }

  protected loadRelationshipsOptions(): void {
    this.tagService
      .query()
      .pipe(map((res: HttpResponse<ITag[]>) => res.body ?? []))
      .pipe(map((tags: ITag[]) => this.tagService.addTagToCollectionIfMissing(tags, ...(this.editForm.get('tags')!.value ?? []))))
      .subscribe((tags: ITag[]) => (this.tagsSharedCollection = tags));

    this.questionTypeService
      .query()
      .pipe(map((res: HttpResponse<IQuestionType[]>) => res.body ?? []))
      .pipe(
        map((questionTypes: IQuestionType[]) =>
          this.questionTypeService.addQuestionTypeToCollectionIfMissing(questionTypes, this.editForm.get('questionType')!.value)
        )
      )
      .subscribe((questionTypes: IQuestionType[]) => (this.questionTypesSharedCollection = questionTypes));

//    this.tenantService
//      .query()
//      .pipe(map((res: HttpResponse<ITenant[]>) => res.body ?? []))
//      .pipe(map((tenants: ITenant[]) => this.tenantService.addTenantToCollectionIfMissing(tenants, this.editForm.get('tenant')!.value)))
//      .subscribe((tenants: ITenant[]) => (this.tenantsSharedCollection = tenants));

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



  protected createFromForm(): IQuestion {
    return {
      ...new Question(),
      id: this.editForm.get(['id'])!.value,
      questionImportFileContentType: this.editForm.get(['questionImportFileContentType'])!.value,
      questionImportFile: this.editForm.get(['questionImportFile'])!.value,
      questionText: this.editForm.get(['questionText'])!.value,
      questionImageContentType: this.editForm.get(['questionImageContentType'])!.value,
      questionImage: this.editForm.get(['questionImage'])!.value,
      answerImageContentType: this.editForm.get(['answerImageContentType'])!.value,
      answerImage: this.editForm.get(['answerImage'])!.value,
      imageSideBySide: this.editForm.get(['imageSideBySide'])!.value,
      option1: this.editForm.get(['option1'])!.value,
      option2: this.editForm.get(['option2'])!.value,
      option3: this.editForm.get(['option3'])!.value,
      option4: this.editForm.get(['option4'])!.value,
      option5: this.editForm.get(['option5'])!.value,
      status: this.editForm.get(['status'])!.value,
      weightage: this.editForm.get(['weightage'])!.value,
      difficultyLevel: this.editForm.get(['difficultyLevel'])!.value,
      createDate: this.editForm.get(['createDate'])!.value,
      lastModified: this.editForm.get(['lastModified'])!.value,
      cancelDate: this.editForm.get(['cancelDate'])!.value,
      tags: this.editForm.get(['tags'])!.value,
      questionType: this.editForm.get(['questionType'])!.value,
      tenant: this.editForm.get(['tenant'])!.value,
      schoolClass: this.editForm.get(['schoolClass'])!.value,
      classSubject: this.editForm.get(['classSubject'])!.value,
      subjectChapter: this.editForm.get(['subjectChapter'])!.value,
    };
  }
}
