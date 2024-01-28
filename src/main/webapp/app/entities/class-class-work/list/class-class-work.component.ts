import { Component } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { combineLatest } from 'rxjs';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IClassClassWork } from '../class-class-work.model';

import { ITEMS_PER_PAGE } from 'app/config/pagination.constants';
import { ClassClassWorkService } from '../service/class-class-work.service';
import { ClassClassWorkDeleteDialogComponent } from '../delete/class-class-work-delete-dialog.component';
import { DataUtils } from 'app/core/util/data-util.service';
import { DATE_FORMAT } from 'app/config/input.constants';

@Component({
  selector: 'jhi-class-class-work',
  templateUrl: './class-class-work.component.html',
})
export class ClassClassWorkComponent {
  classClassWorks?: IClassClassWork[];
  isLoading = false;
  totalItems = 0;
  itemsPerPage = ITEMS_PER_PAGE;
  page?: number;
  predicate!: string;
  ascending!: boolean;
  ngbPaginationPage = 1;

  attendanceDate:any;
  attendanceEndDate:any;
  selectedClassId:any;
   selectedSubjectId:any;

  constructor(
    protected classClassWorkService: ClassClassWorkService,
    protected activatedRoute: ActivatedRoute,
    protected dataUtils: DataUtils,
    protected router: Router,
    protected modalService: NgbModal
  ) {}

  loadPage(page?: number, dontNavigate?: boolean): void {
    this.isLoading = true;
    const pageToLoad: number = page ?? this.page ?? 1;

    this.classClassWorkService
      .query({
        page: pageToLoad - 1,
        size: this.itemsPerPage,
        sort: this.sort(),
      })
      .subscribe(
        (res: HttpResponse<IClassClassWork[]>) => {
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

  trackId(index: number, item: IClassClassWork): number {
    return item.id!;
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    return this.dataUtils.openFile(base64String, contentType);
  }

  delete(classClassWork: IClassClassWork): void {
    const modalRef = this.modalService.open(ClassClassWorkDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.classClassWork = classClassWork;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadPage();
      }
    });
  }
   validateMinFields(): boolean{
  if(this.attendanceDate && this.attendanceEndDate && this.selectedClassId) {
  	return true;
  }else{
 
  	return false;
  	}
  }
  loadAllClassworkForASubject(): void {
  if(!this.validateMinFields()){
   alert("Mandatory fields missing ");
  	return;
  }
    this.isLoading = true;
 	this.attendanceDate= this.attendanceDate?.isValid() ? this.attendanceDate.format(DATE_FORMAT) : undefined,
	this.attendanceEndDate= this.attendanceEndDate?.isValid() ? this.attendanceEndDate.format(DATE_FORMAT) : undefined,
    this.classClassWorkService.query({ 'classId': this.selectedClassId,'subjectId': this.selectedSubjectId , 'startDate': this.attendanceDate ,'endDate': this.attendanceEndDate }).subscribe(
      (res: HttpResponse<IClassClassWork[]>) => {
        this.isLoading = false;
        this.classClassWorks = res.body ?? [];
       
      },
      () => {
        this.isLoading = false;
 
      }
    );
  }
loadAllChapterForASubjectCallback = (subjectChapters:any,selectedSubjectId:any,selectedClassId:any): void => {
  // callback code here
   this.selectedClassId=selectedClassId;
   this.selectedSubjectId=selectedSubjectId;
   this.loadAllClassworkForASubject();
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

  protected onSuccess(data: IClassClassWork[] | null, headers: HttpHeaders, page: number, navigate: boolean): void {
    this.totalItems = Number(headers.get('X-Total-Count'));
    this.page = page;
    if (navigate) {
      this.router.navigate(['/class-class-work'], {
        queryParams: {
          page: this.page,
          size: this.itemsPerPage,
          sort: this.predicate + ',' + (this.ascending ? 'asc' : 'desc'),
        },
      });
    }
    this.classClassWorks = data ?? [];
    this.ngbPaginationPage = this.page;
  }

  protected onError(): void {
    this.ngbPaginationPage = this.page ?? 1;
  }
}
