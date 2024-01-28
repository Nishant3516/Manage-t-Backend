import { Component, OnInit } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { combineLatest } from 'rxjs';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IQuestion } from '../question.model';
import { IQuestionPaper } from '../../question-paper/question-paper.model';
import { QuestionPaperService } from '../../question-paper/service/question-paper.service';

import { ASC, DESC, ITEMS_PER_PAGE, SORT } from 'app/config/pagination.constants';
import { QuestionService } from '../service/question.service';
import { QuestionDeleteDialogComponent } from '../delete/question-delete-dialog.component';
import { DataUtils } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-question',
  templateUrl: './question.component.html',
})
export class QuestionComponent  implements OnInit{
  questions?: IQuestion[];
foundQuestion:any;
	questionPaper?:IQuestionPaper;
	savedQuestion?:IQuestion;
  questionPapers?:IQuestionPaper[];
  isLoading = false;
isSaving = false;  
totalItems = 0;
  itemsPerPage = ITEMS_PER_PAGE;
  page?: number;
  predicate!: string;
  ascending!: boolean;
  ngbPaginationPage = 1;
    selectedClassId:any;
	selectedQuestionTypeId:any;
    selectedSubjectId:any;
	selectedChapterId:any;
	selectedQuestionPaper:any;
	foundQuestionPaper:any;
	updatedQuestion:any;
  constructor(
    protected questionService: QuestionService,
    protected activatedRoute: ActivatedRoute,
    protected dataUtils: DataUtils,
    protected router: Router,
    protected modalService: NgbModal,
	protected questionPaperService: QuestionPaperService,
  ) {}

  loadPage(page?: number, dontNavigate?: boolean): void {
    this.isLoading = true;
    const pageToLoad: number = page ?? this.page ?? 1;

//    this.questionService
//      .query({
//        page: pageToLoad - 1,
//        size: 4,
//        sort: this.sort(),
//      })
//      .subscribe({
//        next: (res: HttpResponse<IQuestion[]>) => {
//          this.isLoading = false;
//          this.onSuccess(res.body, res.headers, pageToLoad, !dontNavigate);
//        },
//        error: () => {
//          this.isLoading = false;
//          this.onError();
//        },
//      });
  }

  ngOnInit(): void {
    this.handleNavigation();
  }
	 getSubjectIdCallBack = (selectedSubjectId: any): void => {
	  // callback code here
	  this.selectedSubjectId=selectedSubjectId;
		// alert(this.selectedQuestionTypeId);
		this.loadQuestionsAndQuestionPapersForASubjectAllTypes(selectedSubjectId);
 	 }
	getChapterIdCallBack = (selectedChapterId: any): void => {
	  // callback code here
	  this.selectedChapterId=selectedChapterId;
	  this.loadQuestionsForASubjectAndChapter(selectedChapterId);
 	 }
 	loadQuestionsForASubjectOfGivenType(subjectId:any, questionTypeId:any):void{
		this.isLoading = true;
	 //  alert("loadQuestionsForASubject");
	// alert(subjectId);
	subjectId=subjectId===null?-1:subjectId;
	// alert(subjectId);
	    this.questionService
	      .query({
	        'classSubjectId.equals': subjectId, 'questionTypeId.equals':questionTypeId,
	        size: this.itemsPerPage,
	        sort: this.sort(),
	      })
	      .subscribe({
	        next: (res: HttpResponse<IQuestion[]>) => {
	          this.isLoading = false;
	          this.questions = res.body ?? [];
				// this.loadQuestionPapersForASubject(subjectId);
				this.findAQuestionPaperForASubject();

	        },
	        error: () => {
	          this.isLoading = false;
	          this.onError();
	        },
	      });
	}
	 	loadQuestionsForASubjectOfAllType(subjectId:any):void{
		this.isLoading = true;
	 //  alert("loadQuestionsForASubject");
	// alert(subjectId);
	subjectId=subjectId===null?-1:subjectId;
	// alert(subjectId);
	    this.questionService
	      .query({
	        'classSubjectId.equals': subjectId,
	        size: this.itemsPerPage,
	        sort: this.sort(),
	      })
	      .subscribe({
	        next: (res: HttpResponse<IQuestion[]>) => {
	          this.isLoading = false;
	          this.questions = res.body ?? [];
				// this.loadQuestionPapersForASubject(subjectId);
				this.findAQuestionPaperForASubject();

	        },
	        error: () => {
	          this.isLoading = false;
	          this.onError();
	        },
	      });
	}
	loadQuestionsAndQuestionPapersForASubjectAllTypes(subjectId:any):void{
		this.isLoading = true;
	 //  alert("loadQuestionsForASubject");
	// alert(subjectId);
	subjectId=subjectId===null?-1:subjectId;
	
	// alert(subjectId);
	    this.questionService
	      .query({
	        'classSubjectId.equals': subjectId,
	        size: this.itemsPerPage,
	        sort: this.sort(),
	      })
	      .subscribe({
	        next: (res: HttpResponse<IQuestion[]>) => {
	          this.isLoading = false;
	          this.questions = res.body ?? [];
				 this.loadQuestionPapersForASubject(subjectId);

	        },
	        error: () => {
	          this.isLoading = false;
	          this.onError();
	        },
	      });
	}
loadQuestionsAndQuestionPapersForASubject(subjectId:any):void{
		this.isLoading = true;
	 //  alert("loadQuestionsForASubject");
	// alert(subjectId);
	subjectId=subjectId===null?-1:subjectId;
	if(!this.selectedQuestionTypeId){
		alert("Select a Question Type");
		return;
	}
	// alert(subjectId);
	    this.questionService
	      .query({
	        'classSubjectId.equals': subjectId, 'questionTypeId.equals':this.selectedQuestionTypeId,
	        size: this.itemsPerPage,
	        sort: this.sort(),
	      })
	      .subscribe({
	        next: (res: HttpResponse<IQuestion[]>) => {
	          this.isLoading = false;
	          this.questions = res.body ?? [];
				 this.loadQuestionPapersForASubject(subjectId);

	        },
	        error: () => {
	          this.isLoading = false;
	          this.onError();
	        },
	      });
	}
	findAQuestionForASubject(questionId:any):void{
		this.isLoading = true;
	 // this.foundQuestion=null;
	    this.questionService
	      .find(questionId)
	      .subscribe({
	        next: (res: HttpResponse<IQuestion>) => {
	          this.isLoading = false;
	          this.foundQuestion =  res.body;
	        },
	        error: () => {
	          this.isLoading = false;
	          this.onError();
	        },
	      });
	}
 	loadQuestionsForASubjectAndChapter(chapterId:any):void{
		this.isLoading = true;
		 chapterId=chapterId===null?-1:chapterId;
		// alert(chapterId);
		// this.selectedQuestionTypeId
	    this.questionService
	      .query({
	         'classSubjectId.equals': this.selectedSubjectId,'subjectChapterId.equals': chapterId, 'questionTypeId.equals':this.selectedQuestionTypeId,
	        size: this.itemsPerPage,
	        sort: this.sort(),
	      })
	      .subscribe({
	        next: (res: HttpResponse<IQuestion[]>) => {
	          this.isLoading = false;
	          this.questions = res.body ?? [];
	        },
	        error: () => {
	          this.isLoading = false;
	          this.onError();
	        },
	      });
	}
		loadQuestionPapersForASubject(selectedSubjectId: any):void{
		this.isLoading = true;
	// alert("loadQuestionPapersForASubject");
	    this.questionPaperService
	      .query({
	        'classSubjectId.equals': selectedSubjectId,
	        size: this.itemsPerPage,
	        sort: this.sort(),
	      })
	      .subscribe({
	        next: (res: HttpResponse<IQuestionPaper[]>) => {
	          this.isLoading = false;
	          this.questionPapers = res.body ?? [];
	        },
	        error: () => {
	          this.isLoading = false;
	          this.onError();
	        },
	      });
	}
	findAQuestionPaperForASubject():void{
		this.isLoading = true;
	// alert("loadQA ---  uestionPapersForASubject");
	    this.questionPaperService
	      .find(this.selectedQuestionPaper.id)
	      .subscribe({
	        next: (res: HttpResponse<IQuestionPaper>) => {
	          this.isLoading = false;
	          this.foundQuestionPaper = res.body ?? [];
				// alert(this.foundQuestionPaper.questionPaperFileContentType);
				// alert(this.foundQuestionPaper.questionPaperFile);
				this.openFile(this.foundQuestionPaper.questionPaperFile, this.foundQuestionPaper.questionPaperFileContentType);

	        },
	        error: () => {
	          this.isLoading = false;
	          this.onError();
	        },
	      });
	}
	getClassIdCallBack = (selectedClassId: any): void => {
		// alert("Class called");
	  // callback code here
	   this.selectedClassId=selectedClassId;
	   // this.loadSubjects(selectedClassId);
  	}
	getQuestionTypeIdCallBack = (selectedQuestionTypeId: any): void => {
	  // callback code here
		this.selectedQuestionTypeId=selectedQuestionTypeId;
	    // alert(this.selectedSubjectId);
	   // this.loadSubjects(selectedClassId);
		if(!selectedQuestionTypeId){
			// select a question type
			alert("Select a question type");
		}else if(this.selectedSubjectId&&this.selectedChapterId){
			// filter subect and chater question to a type
			this.loadQuestionsForASubjectAndChapter(this.selectedChapterId);
		}else if(this.selectedSubjectId&&this.selectedQuestionTypeId){
			// filter all questions for a subject
			// alert('loadQuestionPapersForASubject');
			this.loadQuestionsForASubjectOfGivenType(this.selectedSubjectId,this.selectedQuestionTypeId);
		}else if(this.selectedSubjectId&&!this.selectedQuestionTypeId){
			// filter all questions for a subject
			// alert('loadQuestionPapersForASubject');
			this.loadQuestionsForASubjectOfAllType(this.selectedSubjectId);
		}
  	}

	loadQuestionsForATypeOrAllTypes (subjectId:any, questionTypeId:any) :void{
		
		if(subjectId&& questionTypeId){
			// filter all questions for a subject
			// alert('loadQuestionPapersForASubject');
			this.loadQuestionsForASubjectOfGivenType(subjectId,questionTypeId);
		}else if(this.selectedSubjectId&&!this.selectedQuestionTypeId){
			// filter all questions for a subject
			// alert('loadQuestionPapersForASubject');
			this.loadQuestionsForASubjectOfAllType(subjectId);
		}
	}
	save(question: IQuestion): void {
	    this.isSaving = true;
//		if(!question.questionPapers){
//			question.questionPapers=new Array(0);
//		}
//		if(this.questionPapers){
//			this.questionPaper=this.questionPapers.find(e => e.id===this.selectedQuestionPaperId);
//			if(this.questionPaper){
//				question.questionPapers.push(this.questionPaper);
//			}
//		}
//	    this.questionService.update(question).subscribe(
//      () => {
//        this.isSaving = false;
//		this.loadQuestionsForASubjectAndChapter(this.selectedChapterId);
//       },
//      () => {
//        this.isSaving = false;
//      }
//    );


    this.questionService
      .update(question)
      .subscribe({
        next: (res: HttpResponse<IQuestion>) => {
          this.isLoading = false;
		  this.savedQuestion = res.body ?? new Object();
			// alert(this.savedQuestion.classSubject?.id);
         // this.onSuccess(res.body, res.headers, pageToLoad, !dontNavigate);
// Question is being laoded not the question paper, when a question paper reference is added
// need to reload all question to reflect the status
// TODO Actually one the question need to be reloaded , not the whole queston ..
		//	this.findAQuestionForASubject(question.id);
						this.loadQuestionsForATypeOrAllTypes(this.savedQuestion.classSubject?.id,this.selectedQuestionTypeId);
						this.updatedQuestion=question;

        },
        error: () => {
          this.isLoading = false;
          this.onError();
        },
      });
	  }
	addToQuestionAdded(question: IQuestion): void {
	//	alert(this.selectedQuestionPaper.id);
		// This is  bad hack, question paper ID is stored on the serveer so that it is easy while rendering the questions in the question paper
		question.weightage=this.selectedQuestionPaper.id;
		this.save(question);
		

	}
	removeFromQuestionAdded(question: IQuestion): void {
		question.weightage=0-this.selectedQuestionPaper.id;
		this.save(question);
	  }
	isQuestionAdded(question: IQuestion): boolean{
		if(!this.foundQuestionPaper){
			// alert("return false");
			return false;
		}
		//  this.questionPaper = question.questionPapers?.find(e => e.id===this.selectedQuestionPaper?.id);
		if(question.questionPapers){
			for (let index = 0; index < question.questionPapers.length; index++) {
   					const  questionPaper=question.questionPapers[index];
					if(this.foundQuestionPaper.id===questionPaper.id){
						// alert("true");
						// eslint-disable-next-line no-console
						console.log("qPaperId ",questionPaper.id, " selectedQpaperId ",this.selectedQuestionPaper.id);
						return true;
						 
					}
				}
		}
		  // alert(this.questionPaper?.id);
		if(this.questionPaper){
			// alert(this.questionPaper.id);
			return true;
		}else{
			return false;
		}
	}
	isQuestionPaperSelected(): boolean{
		
		if(this.selectedQuestionPaper?.id){
			return false;
		}else{
			return true;
		}
		return false;
	}
//	openQuestionPaper(questionPaperToOpen:IQuestionPaper): void{
//			//  {{ selectedQuestionPaper?.questionPaperFileContentType }}, {{ byteSize(selectedQuestionPaper?.questionPaperFile) }}
//			//if(questionPaperToOpen){
//				alert(questionPaperToOpen.questionPaperFileContentType);
//				alert(questionPaperToOpen.questionPaperFile);
//				this.openFile(questionPaperToOpen.questionPaperFile, questionPaperToOpen.questionPaperFileContentType);
//		//	}
//		
//	}
	isQuestionPaperSelectedForDownload(): boolean{
		
		if(this.selectedQuestionPaper?.id){
						alert(this.selectedQuestionPaper.id);
						this.findAQuestionPaperForASubject();
			return false;
		}else{
			return true;
		}
	}	
  trackId(index: number, item: IQuestion): number {
    return item.id!;
  }
  trackIdQPaper(index: number, item: IQuestionPaper): number {
    return item.id!;
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    return this.dataUtils.openFile(base64String, contentType);
  }
 delete(question: IQuestion): void {
	if(question.id){
     this.questionService
      .delete(question.id)
      .subscribe({
        next: (res: HttpResponse<{}>) => {
			// alert("Delete success ");
			//	alert(this.selectedSubjectId);
          this.isLoading = false;
		 this.loadQuestionsForATypeOrAllTypes(question.classSubject?.id,this.selectedQuestionTypeId);
        },
        error: () => {
          this.isLoading = false;
          this.onError();
			alert("Delete failed");
        },
      });
}
  }
  delete1(question: IQuestion): void {
    const modalRef = this.modalService.open(QuestionDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.question = question;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
	// alert("deleted ");
        this.loadQuestionsForATypeOrAllTypes(question.classSubject?.id,this.selectedQuestionTypeId);
      }
    });
  }

  protected sort(): string[] {
    const result = [this.predicate + ',' + (this.ascending ? ASC : DESC)];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  protected handleNavigation(): void {
    combineLatest([this.activatedRoute.data, this.activatedRoute.queryParamMap]).subscribe(([data, params]) => {
      const page = params.get('page');
      const pageNumber = +(page ?? 1);
      const sort = (params.get(SORT) ?? data['defaultSort']).split(',');
      const predicate = sort[0];
      const ascending = sort[1] === ASC;
      if (pageNumber !== this.page || predicate !== this.predicate || ascending !== this.ascending) {
        this.predicate = predicate;
        this.ascending = ascending;
        this.loadPage(pageNumber, true);
      }
    });
  }

  protected onSuccess(data: IQuestion[] | null, headers: HttpHeaders, page: number, navigate: boolean): void {
    this.totalItems = Number(headers.get('X-Total-Count'));
    this.page = page;
    if (navigate) {
      this.router.navigate(['/question'], {
        queryParams: {
          page: this.page,
          size: this.itemsPerPage,
          sort: this.predicate + ',' + (this.ascending ? ASC : DESC),
        },
      });
    }
    this.questions = data ?? [];
    this.ngbPaginationPage = this.page;
  }

  protected onError(): void {
    this.ngbPaginationPage = this.page ?? 1;
  }
}
