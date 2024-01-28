import { Component, OnInit } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { combineLatest } from 'rxjs';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';


import { ASC, DESC, ITEMS_PER_PAGE, SORT } from 'app/config/pagination.constants';
import { IQuestionPaper } from '../question-paper.model';
import { QuestionPaperService } from '../service/question-paper.service';
import { QuestionPaperDeleteDialogComponent } from '../delete/question-paper-delete-dialog.component';
import { DataUtils } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-question-paper',
  templateUrl: './question-paper.component.html',
})
export class QuestionPaperComponent implements OnInit {
  questionPapers?: IQuestionPaper[];
  isLoading = false;
  totalItems = 0;
  itemsPerPage = ITEMS_PER_PAGE;
  page?: number;
  predicate!: string;
  ascending!: boolean;
  ngbPaginationPage = 1;
    selectedClassId:any;
    selectedSubjectId:any;

  constructor(
    protected questionPaperService: QuestionPaperService,
    protected activatedRoute: ActivatedRoute,
    protected dataUtils: DataUtils,
    protected router: Router,
    protected modalService: NgbModal
  ) {}

  loadPage(page?: number, dontNavigate?: boolean): void {
    this.isLoading = true;
    const pageToLoad: number = page ?? this.page ?? 1;

    this.questionPaperService
      .query({
        page: pageToLoad - 1,
        size: this.itemsPerPage,
        sort: this.sort(),
      })
      .subscribe({
        next: (res: HttpResponse<IQuestionPaper[]>) => {
          this.isLoading = false;
          this.onSuccess(res.body, res.headers, pageToLoad, !dontNavigate);
        },
        error: () => {
          this.isLoading = false;
          this.onError();
        },
      });
  }

  ngOnInit(): void {
    this.handleNavigation();
  }
	 getSubjectIdCallBack = (selectedSubjectId: any): void => {
	  // callback code here
	  this.selectedSubjectId=selectedSubjectId;
		this.loadQuestionPapersForASubject(selectedSubjectId);
 	 }
	
 	loadQuestionPapersForASubject(subjectId:any):void{
		this.isLoading = true;
	 
	    this.questionPaperService
	      .query({
	        'classSubjectId.equals': subjectId,
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
  	loadQuestionPapersForAClass(classId:any):void{
		this.isLoading = true;
	 
	    this.questionPaperService
	      .query({
	        'schoolClassId.equals': classId,
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
	getClassIdCallBack = (selectedClassId: any): void => {
	  // callback code here
// alert(selectedClassId);
	   this.selectedClassId=selectedClassId;
	    this.loadQuestionPapersForAClass(selectedClassId);
  	}

  trackId(index: number, item: IQuestionPaper): number {
    return item.id!;
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    return this.dataUtils.openFile(base64String, contentType);
  }

  delete(questionPaper: IQuestionPaper): void {
    const modalRef = this.modalService.open(QuestionPaperDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.questionPaper = questionPaper;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadPage();
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
        // this.loadPage(pageNumber, true);
      }
    });
  }

  protected onSuccess(data: IQuestionPaper[] | null, headers: HttpHeaders, page: number, navigate: boolean): void {
    this.totalItems = Number(headers.get('X-Total-Count'));
    this.page = page;
    if (navigate) {
      this.router.navigate(['/question-paper'], {
        queryParams: {
          page: this.page,
          size: this.itemsPerPage,
          sort: this.predicate + ',' + (this.ascending ? ASC : DESC),
        },
      });
    }
    this.questionPapers = data ?? [];
    this.ngbPaginationPage = this.page;
  }

  protected onError(): void {
    this.ngbPaginationPage = this.page ?? 1;
  }
}
