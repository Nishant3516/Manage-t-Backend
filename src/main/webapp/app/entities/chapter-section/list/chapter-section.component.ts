import { Component } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { combineLatest } from 'rxjs';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IChapterSection } from '../chapter-section.model';

import { ITEMS_PER_PAGE } from 'app/config/pagination.constants';
import { ChapterSectionService } from '../service/chapter-section.service';
import { ChapterSectionDeleteDialogComponent } from '../delete/chapter-section-delete-dialog.component';

@Component({
  selector: 'jhi-chapter-section',
  templateUrl: './chapter-section.component.html',
})
export class ChapterSectionComponent {
  chapterSections?: IChapterSection[];
  isLoading = false;
  totalItems = 0;
  itemsPerPage = ITEMS_PER_PAGE;
  page?: number;
  predicate!: string;
  ascending!: boolean;
  ngbPaginationPage = 1;
    selectedClassId:any;
    selectedSubjectId:any;
	selectedChapterId:any;
  constructor(
    protected chapterSectionService: ChapterSectionService,
    protected activatedRoute: ActivatedRoute,
    protected router: Router,
    protected modalService: NgbModal
  ) {}

  loadPage(page?: number, dontNavigate?: boolean): void {
    this.isLoading = true;
    const pageToLoad: number = page ?? this.page ?? 1;

    this.chapterSectionService
      .query({
        page: pageToLoad - 1,
        size: this.itemsPerPage,
        sort: this.sort(),
      })
      .subscribe(
        (res: HttpResponse<IChapterSection[]>) => {
          this.isLoading = false;
          this.onSuccess(res.body, res.headers, pageToLoad, !dontNavigate);
        },
        () => {
          this.isLoading = false;
          this.onError();
        }
      );
  }

//  ngOnInit(): void {
//    this.handleNavigation();
//  }

  trackId(index: number, item: IChapterSection): number {
    return item.id!;
  }
 	   loadSections(selectedChapterId:any): void {
		  if(selectedChapterId){
		
			  this.chapterSectionService.query({ 'subjectChapterId.equals': selectedChapterId }).subscribe(
			      (res: HttpResponse<IChapterSection[]>) => {
			        this.isLoading = false;
			        this.chapterSections = res.body ?? [];
			      },
			      () => {
			      	alert("Error occured while fetching the class subjects ");
			        this.isLoading = false;
			      }
			    );
   		 }
  }
	  getSubjectIdCallBack = (selectedSubjectId: any): void => {
	  // callback code here
	  this.selectedSubjectId=selectedSubjectId;
 	 }
	getChapterIdCallBack = (selectedChapterId: any): void => {
	  // callback code here
	  this.selectedChapterId=selectedChapterId;
	  this.loadSections(selectedChapterId);
 	 }
 	 

	getClassIdCallBack = (selectedClassId: any): void => {
	  // callback code here
	   this.selectedClassId=selectedClassId;
	   // this.loadSubjects(selectedClassId);
  	}
  delete(chapterSection: IChapterSection): void {
    const modalRef = this.modalService.open(ChapterSectionDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.chapterSection = chapterSection;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadPage();
      }
    });
  }

  protected sort(): string[] {
    const result = [this.predicate + ',' + (this.ascending ? 'asc' : 'desc')];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  protected handleNavigation(): void {
    combineLatest([this.activatedRoute.data, this.activatedRoute.queryParamMap]).subscribe(([data, params]) => {
      const page = params.get('page');
      const pageNumber = page !== null ? +page : 1;
      const sort = (params.get('sort') ?? data['defaultSort']).split(',');
      const predicate = sort[0];
      const ascending = sort[1] === 'asc';
      if (pageNumber !== this.page || predicate !== this.predicate || ascending !== this.ascending) {
        this.predicate = predicate;
        this.ascending = ascending;
        this.loadPage(pageNumber, true);
      }
    });
  }

  protected onSuccess(data: IChapterSection[] | null, headers: HttpHeaders, page: number, navigate: boolean): void {
    this.totalItems = Number(headers.get('X-Total-Count'));
    this.page = page;
    if (navigate) {
      this.router.navigate(['/chapter-section'], {
        queryParams: {
          page: this.page,
          size: this.itemsPerPage,
          sort: this.predicate + ',' + (this.ascending ? 'asc' : 'desc'),
        },
      });
    }
    this.chapterSections = data ?? [];
    this.ngbPaginationPage = this.page;
  }

  protected onError(): void {
    this.ngbPaginationPage = this.page ?? 1;
  }
}
