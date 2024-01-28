import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IQuestionPaper, QuestionPaper } from '../question-paper.model';
import { QuestionPaperService } from '../service/question-paper.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { IQuestion } from 'app/entities/question/question.model';
import { QuestionService } from 'app/entities/question/service/question.service';
import { ITag } from 'app/entities/tag/tag.model';
import { TagService } from 'app/entities/tag/service/tag.service';
import { ITenant } from 'app/entities/tenant/tenant.model';
import { TenantService } from 'app/entities/tenant/service/tenant.service';
import { ISchoolClass } from 'app/entities/school-class/school-class.model';
import { SchoolClassService } from 'app/entities/school-class/service/school-class.service';
import { IClassSubject } from 'app/entities/class-subject/class-subject.model';
import { ClassSubjectService } from 'app/entities/class-subject/service/class-subject.service';
import { SubjectChapterService } from 'app/entities/subject-chapter/service/subject-chapter.service';

@Component({
  selector: 'jhi-question-paper-update',
  templateUrl: './question-paper-update.component.html',
})
export class QuestionPaperUpdateComponent implements OnInit {
  isSaving = false;
  isLoading = false;
found=false;
selectedClassId:any;
	selectedChapterId:any;
    selectedSubjectId:any;
selectedQuestion: any;
	questionsInQuestionPaperFromAChapter: IQuestion[] = [];
	questionsInQuestionPaper: IQuestion[] = [];
  questionsSharedCollection: IQuestion[] = [];
  tagsSharedCollection: ITag[] = [];
  tenantsSharedCollection: ITenant[] = [];
  schoolClassesSharedCollection: ISchoolClass[] = [];
  classSubjectsSharedCollection: IClassSubject[] = [];

  editForm = this.fb.group({
    id: [],
    tenatLogo: [],
    tenatLogoContentType: [],
    questionPaperFile: [],
    questionPaperFileContentType: [],
    questionPaperName: [],
    mainTitle: [],
    subTitle: [],
    leftSubHeading1: [],
    leftSubHeading2: [],
    rightSubHeading1: [],
    rightSubHeading2: [],
    instructions: [],
    footerText: [],
    totalMarks: [],
    createDate: [],
    lastModified: [],
    cancelDate: [],
    questions: [],
	questionsInQuestionPaper: [],
    tags: [],
    tenant: [],
    schoolClass: [],
    classSubject: [],
  });

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected questionPaperService: QuestionPaperService,
    protected questionService: QuestionService,
    protected tagService: TagService,
    protected tenantService: TenantService,
    protected schoolClassService: SchoolClassService,
    protected classSubjectService: ClassSubjectService,
    protected activatedRoute: ActivatedRoute,
	protected subjectChapterService: SubjectChapterService,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ questionPaper }) => {
      this.updateForm(questionPaper);

      this.loadRelationshipsOptions();
    });
  }
 	getChapterIdCallBack = (selectedChapterId: any): void => {
	  // callback code here
	  this.selectedChapterId=selectedChapterId;
	  this.loadQuestionsForASubjectAndChapter(selectedChapterId);
 	 }

//  loadAllChaptersFroASelectedSubject():void{
//		    this.subjectChapterService
//		      .query({ 'classSubjectId.equals': this.editForm.get(['classSubject'])!.value.id })
//		      .pipe(map((res: HttpResponse<ISubjectChapter[]>) => res.body ?? []))
//		      .pipe(
//		        map((subjectChapters: ISubjectChapter[]) =>
//		          this.subjectChapterService.addSubjectChapterToCollectionIfMissing(subjectChapters, this.editForm.get('subjectChapter')!.value)
//		        )
//		      )
//		      .subscribe((subjectChapters: ISubjectChapter[]) => (this.subjectChaptersSharedCollection = subjectChapters));
//
//	}

// onChange(newValue:any) :void {
//	// eslint-disable-next-line no-console
//	console.log(newValue);
//	// console.log(newValue[0].questionText);
//	alert(newValue[0].questionText);
//	// alert("Changed");
//		this.questionsInQuestionPaperFromAChapter = this.editForm.get(['questions'])!.value;
//	alert(this.questionsInQuestionPaperFromAChapter.length);
//	alert(this.selectedQuestion.questionText);
// }
//	loadQuestionsForASubject():void{
//		// alert("Changed");
//		this.selectedSubjectId=this.editForm.get(['classSubject'])!.value.id;
//		
//		// alert(this.selectedSubjectId);
//		this.isLoading = true;
//	 
//	    this.questionService
//	      .query({
//	        'classSubjectId.equals': this.selectedSubjectId,
//	      })
//	      .subscribe({
//	        next: (res: HttpResponse<IQuestion[]>) => {
//	          this.isLoading = false;
//	          this.questionsSharedCollection = res.body ?? [];
//	        },
//	        error: () => {
//	          this.isLoading = false;
//	        },
//	      });
//	}
	
	  loadSubjects(): void {
		 this.selectedClassId=this.editForm.get(['schoolClass'])!.value.id;
		// alert(this.selectedClassId);
  	if(this.selectedClassId&&this.selectedClassId>0){
  		this.classSubjectsSharedCollection =  [];
        this.isLoading = true;
	       
	    this.classSubjectService.query({ 'schoolClassId.equals': this.selectedClassId }).subscribe(
	      (res: HttpResponse<IClassSubject[]>) => {
	        this.isLoading = false;
	        this.classSubjectsSharedCollection = res.body ?? [];
	        // this.callbackFunction(this.classSubjects);
	      },
	      () => {
	      	alert("Error occured while fetching the class subjects ");
	        this.isLoading = false;
	      }
	    );
    }
  } 
 	loadQuestionsForASubjectAndChapter(chapterId:any):void{
		this.isLoading = true;
	 
	    this.questionService
	      .query({
	        'subjectChapterId.equals': chapterId,
	      })
	      .subscribe({
	        next: (res: HttpResponse<IQuestion[]>) => {
	          this.isLoading = false;
	          this.questionsSharedCollection = res.body ?? [];
	        },
	        error: () => {
	          this.isLoading = false;
	        },
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
	if(this.editForm.get(['questionPaperName'])!.value&&this.editForm.get(['schoolClass'])!.value&&this.editForm.get(['classSubject'])!.value&&this.editForm.get(['mainTitle'])!.value){

	    this.isSaving = true;
	    const questionPaper = this.createFromForm();
	    if (questionPaper.id !== undefined) {
	      this.subscribeToSaveResponse(this.questionPaperService.update(questionPaper));
	    } else {
	      this.subscribeToSaveResponse(this.questionPaperService.create(questionPaper));
	    }
	}else{
		alert("Mandatory fields are missing ");
	}
  }

  trackQuestionById(index: number, item: IQuestion): number {
    return item.id!;
  }

  trackTagById(index: number, item: ITag): number {
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

  getSelectedQuestion(option: IQuestion, selectedVals?: IQuestion[]): IQuestion {
    if (selectedVals) {
      for (const selectedVal of selectedVals) {
        if (option.id === selectedVal.id) {
			// alert(option.questionText);
			// this.selectedQuestion=option;
          return selectedVal;
        }
      }
    }
    return option;
  }
removeSelectedQuestion(option: IQuestion): void {
	// alert("selected");
    // if (this.questionsInQuestionPaper) {
	this.questionsInQuestionPaper = this.questionsInQuestionPaper.filter(function(value, index, arr){ 
        return option.id !== value.id;
    });
//      for (const selectedVal of this.questionsInQuestionPaper) {
//        if (option.id === selectedVal.id) {
//			this.questionsInQuestionPaper.d;
//          
//        }
//      }
   //  }
   
  }
addSelectedQuestion(option: IQuestion): void {
    // if (this.questionsInQuestionPaper) {
	 
     for (const selectedVal of this.questionsInQuestionPaper) {
        if (option.id === selectedVal.id) {
			this.found=true;
          
       }
     }
	if(!this.found){
		alert("Not found");
		this.questionsInQuestionPaper.push(option);
	}
   //  }
   
  }
// addSelectedQuestion(option: IQuestion, selectedVals?: IQuestion[]): void {
//    if (selectedVals) {
//      for (const selectedVal of selectedVals) {
//        if (option.id === selectedVal.id) {
//			this.questionsInQuestionPaper.push(option);
//          return selectedVal;
//        }
//      }
//    }
//    return option;
//  }  

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

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IQuestionPaper>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
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

  protected updateForm(questionPaper: IQuestionPaper): void {
	// alert(questionPaper.schoolClass?.id);
	this.selectedClassId = questionPaper.schoolClass?.id;
    this.editForm.patchValue({
      id: questionPaper.id,
      tenatLogo: questionPaper.tenatLogo,
      tenatLogoContentType: questionPaper.tenatLogoContentType,
      questionPaperFile: questionPaper.questionPaperFile,
      questionPaperFileContentType: questionPaper.questionPaperFileContentType,
      questionPaperName: questionPaper.questionPaperName,
      mainTitle: questionPaper.mainTitle,
      subTitle: questionPaper.subTitle,
      leftSubHeading1: questionPaper.leftSubHeading1,
      leftSubHeading2: questionPaper.leftSubHeading2,
      rightSubHeading1: questionPaper.rightSubHeading1,
      rightSubHeading2: questionPaper.rightSubHeading2,
      instructions: questionPaper.instructions,
      footerText: questionPaper.footerText,
      totalMarks: questionPaper.totalMarks,
      createDate: questionPaper.createDate,
      lastModified: questionPaper.lastModified,
      cancelDate: questionPaper.cancelDate,
      questions: questionPaper.questions,
      tags: questionPaper.tags,
      tenant: questionPaper.tenant,
      schoolClass: questionPaper.schoolClass,
      classSubject: questionPaper.classSubject,
    });

    this.questionsSharedCollection = this.questionService.addQuestionToCollectionIfMissing(
      this.questionsSharedCollection,
      ...(questionPaper.questions ?? [])
    );
    this.tagsSharedCollection = this.tagService.addTagToCollectionIfMissing(this.tagsSharedCollection, ...(questionPaper.tags ?? []));
    this.tenantsSharedCollection = this.tenantService.addTenantToCollectionIfMissing(this.tenantsSharedCollection, questionPaper.tenant);
    this.schoolClassesSharedCollection = this.schoolClassService.addSchoolClassToCollectionIfMissing(
      this.schoolClassesSharedCollection,
      questionPaper.schoolClass
    );
    this.classSubjectsSharedCollection = this.classSubjectService.addClassSubjectToCollectionIfMissing(
      this.classSubjectsSharedCollection,
      questionPaper.classSubject
    );
  }

  protected loadRelationshipsOptions(): void {
//    this.questionService
//      .query()
//      .pipe(map((res: HttpResponse<IQuestion[]>) => res.body ?? []))
//      .pipe(
//        map((questions: IQuestion[]) =>
//          this.questionService.addQuestionToCollectionIfMissing(questions, ...(this.editForm.get('questions')!.value ?? []))
//        )
//      )
//      .subscribe((questions: IQuestion[]) => (this.questionsSharedCollection = questions));

//    this.tagService
//      .query()
//      .pipe(map((res: HttpResponse<ITag[]>) => res.body ?? []))
//      .pipe(map((tags: ITag[]) => this.tagService.addTagToCollectionIfMissing(tags, ...(this.editForm.get('tags')!.value ?? []))))
//      .subscribe((tags: ITag[]) => (this.tagsSharedCollection = tags));

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

//    this.classSubjectService
//      .query()
//      .pipe(map((res: HttpResponse<IClassSubject[]>) => res.body ?? []))
//      .pipe(
//        map((classSubjects: IClassSubject[]) =>
//          this.classSubjectService.addClassSubjectToCollectionIfMissing(classSubjects, this.editForm.get('classSubject')!.value)
//        )
//      )
//      .subscribe((classSubjects: IClassSubject[]) => (this.classSubjectsSharedCollection = classSubjects));
  }

  protected createFromForm(): IQuestionPaper {
    return {
      ...new QuestionPaper(),
      id: this.editForm.get(['id'])!.value,
      tenatLogoContentType: this.editForm.get(['tenatLogoContentType'])!.value,
      tenatLogo: this.editForm.get(['tenatLogo'])!.value,
      questionPaperFileContentType: this.editForm.get(['questionPaperFileContentType'])!.value,
      questionPaperFile: this.editForm.get(['questionPaperFile'])!.value,
      questionPaperName: this.editForm.get(['questionPaperName'])!.value,
      mainTitle: this.editForm.get(['mainTitle'])!.value,
      subTitle: this.editForm.get(['subTitle'])!.value,
      leftSubHeading1: this.editForm.get(['leftSubHeading1'])!.value,
      leftSubHeading2: this.editForm.get(['leftSubHeading2'])!.value,
      rightSubHeading1: this.editForm.get(['rightSubHeading1'])!.value,
      rightSubHeading2: this.editForm.get(['rightSubHeading2'])!.value,
      instructions: this.editForm.get(['instructions'])!.value,
      footerText: this.editForm.get(['footerText'])!.value,
      totalMarks: this.editForm.get(['totalMarks'])!.value,
      createDate: this.editForm.get(['createDate'])!.value,
      lastModified: this.editForm.get(['lastModified'])!.value,
      cancelDate: this.editForm.get(['cancelDate'])!.value,
      questions: this.editForm.get(['questions'])!.value,
      tags: this.editForm.get(['tags'])!.value,
      tenant: this.editForm.get(['tenant'])!.value,
      schoolClass: this.editForm.get(['schoolClass'])!.value,
      classSubject: this.editForm.get(['classSubject'])!.value,
    };
  }
}
