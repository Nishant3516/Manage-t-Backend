import { Component } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IClassSubject } from '../class-subject.model';

import { ITEMS_PER_PAGE } from 'app/config/pagination.constants';
import { ClassSubjectService } from '../service/class-subject.service';
import { ClassSubjectDeleteDialogComponent } from '../delete/class-subject-delete-dialog.component';

@Component({
  selector: 'jhi-class-subject',
  templateUrl: './class-subject.component.html',
})
export class ClassSubjectComponent  {
  classSubjects?: IClassSubject[];
  isLoading = false;
  totalItems = 0;
  itemsPerPage = ITEMS_PER_PAGE;
  page?: number;
  predicate!: string;
  ascending!: boolean;
  ngbPaginationPage = 1;
  selectedClassId:any;

  constructor(
    protected classSubjectService: ClassSubjectService,
    protected activatedRoute: ActivatedRoute,
    protected router: Router,
    protected modalService: NgbModal
  ) {}

  loadPage(page?: number, dontNavigate?: boolean): void {
    this.isLoading = true;
    const pageToLoad: number = page ?? this.page ?? 1;

    this.classSubjectService
      .query({
        page: pageToLoad - 1,
        size: this.itemsPerPage,
        sort: this.sort(),
        'schoolClassId.equals': this.selectedClassId ,
      })
      .subscribe(
        (res: HttpResponse<IClassSubject[]>) => {
          this.isLoading = false;
          this.onSuccess(res.body, res.headers, pageToLoad, !dontNavigate);
        },
        () => {
          this.isLoading = false;
          this.onError();
        }
      );
  }
   loadSubjects(selectedClassId:any): void {
  if(selectedClassId){

  this.classSubjects =  [];
        this.isLoading = true;
    this.classSubjectService.query({ 'schoolClassId.equals': selectedClassId }).subscribe(
      (res: HttpResponse<IClassSubject[]>) => {
        this.isLoading = false;
        this.classSubjects = res.body ?? [];
        // this.callbackFunction(this.classSubjects);
      },
      () => {
      	alert("Error occured while fetching the class subjects ");
        this.isLoading = false;
      }
    );
    }
  }

//  ngOnInit(): void {
//    this.handleNavigation();
//  }

  trackId(index: number, item: IClassSubject): number {
    return item.id!;
  }

  delete(classSubject: IClassSubject): void {
    const modalRef = this.modalService.open(ClassSubjectDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.classSubject = classSubject;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadPage();
      }
    });
  }
  getClassIdCallBack = (selectedClassId: any): void => {
  // callback code here
   this.selectedClassId=selectedClassId;
   this.loadSubjects(selectedClassId);
  }
//  getSubjectIdCallBack = (selectedSubjectId: any): void => {
//  // callback code here
//  alert(`Callback function called which will populate the selected class ID with $selectedSubjectId`);
//  alert(selectedSubjectId);
//  }
  protected sort(): string[] {
    const result = [this.predicate + ',' + (this.ascending ? 'asc' : 'desc')];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

//  protected handleNavigation(): void {
//    combineLatest([this.activatedRoute.data, this.activatedRoute.queryParamMap]).subscribe(([data, params]) => {
//      const page = params.get('page');
//      const pageNumber = page !== null ? +page : 1;
//      const sort = (params.get('sort') ?? data['defaultSort']).split(',');
//      const predicate = sort[0];
//      const ascending = sort[1] === 'asc';
//      if (pageNumber !== this.page || predicate !== this.predicate || ascending !== this.ascending) {
//        this.predicate = predicate;
//        this.ascending = ascending;
//        this.loadPage(pageNumber, true);
//      }
//    });
//  }

  protected onSuccess(data: IClassSubject[] | null, headers: HttpHeaders, page: number, navigate: boolean): void {
    this.totalItems = Number(headers.get('X-Total-Count'));
    this.page = page;
    if (navigate) {
      this.router.navigate(['/class-subject'], {
        queryParams: {
          page: this.page,
          size: this.itemsPerPage,
          sort: this.predicate + ',' + (this.ascending ? 'asc' : 'desc'),
        },
      });
    }
    this.classSubjects = data ?? [];
    this.ngbPaginationPage = this.page;
  }

  protected onError(): void {
    this.ngbPaginationPage = this.page ?? 1;
  }
}
