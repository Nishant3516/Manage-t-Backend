import { Component, OnInit } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IClassLessionPlanTrack } from '../class-lession-plan-track.model';

import { ITEMS_PER_PAGE } from 'app/config/pagination.constants';
import { ClassLessionPlanTrackService } from '../service/class-lession-plan-track.service';
import { TaskStatus } from 'app/entities/enumerations/task-status.model';
import { DATE_FORMAT } from 'app/config/input.constants';

@Component({
  selector: 'jhi-class-lession-plan-track',
  templateUrl: './class-lession-plan-track.component.html',
})
export class ClassLessionPlanTrackComponent implements OnInit {
  classLessionPlanTracks?: IClassLessionPlanTrack[];
  classLessionPlanTrack?:IClassLessionPlanTrack;
  isLoading = false;
  totalItems = 0;
  itemsPerPage = ITEMS_PER_PAGE;
  page?: number;
  predicate!: string;
  ascending!: boolean;
  ngbPaginationPage = 1;
isSaving = false;
selectedClassId:any;
selectedSubjectId:any;
startDate:any;
endDate:any;

  constructor(
    protected classLessionPlanTrackService: ClassLessionPlanTrackService,
    protected activatedRoute: ActivatedRoute,
    protected router: Router,
    protected modalService: NgbModal
  ) {}

//  loadPage(page?: number, dontNavigate?: boolean): void {
//    this.isLoading = true;
//    const pageToLoad: number = page ?? this.page ?? 1;
//
//    this.classLessionPlanTrackService
//      .query({
//        page: pageToLoad - 1,
//        size: this.itemsPerPage,
//        sort: this.sort(),
//      })
//      .subscribe(
//        (res: HttpResponse<IClassLessionPlanTrack[]>) => {
//          this.isLoading = false;
//          this.onSuccess(res.body, res.headers, pageToLoad, !dontNavigate);
//        },
//        () => {
//          this.isLoading = false;
//          this.onError();
//        }
//      );
//  }
 fetchLessionPlan(): void {
    this.isLoading = true;
  	this.startDate= this.startDate?.isValid() ? this.startDate.format(DATE_FORMAT) : undefined,
	this.endDate= this.endDate?.isValid() ? this.endDate.format(DATE_FORMAT) : undefined,
  this.selectedClassId=this.selectedClassId?this.selectedClassId:-1;
  this.selectedSubjectId=this.selectedSubjectId?this.selectedSubjectId:-1;

    this.classLessionPlanTrackService
      .query({
        'startDate':this.startDate,
        'endDate':this.endDate,
        'schoolClassId': this.selectedClassId,
        'classSubjectId':this.selectedSubjectId,
       })
      .subscribe(
        (res: HttpResponse<IClassLessionPlanTrack[]>) => {
          this.isLoading = false;
          this.classLessionPlanTracks = res.body ?? [];
        },
        () => {
          this.isLoading = false;
          this.onError();
        }
      );
  }

  	// getDatesCallBack
  	getDatesCallBack = (startDate: any, endDate:any): void => {
	  // callback code here
	   this.startDate=startDate;
	   this.endDate=endDate;
	  
	   
  	}
  	
  	getClassIdCallBack = (selectedClassId: any): void => {
	  // callback code here
	   this.selectedClassId=selectedClassId;
	   
  	}
  	getSubjectIdCallBack = (selectedSubjectId: any): void => {
	  // callback code here
	  this.selectedSubjectId=selectedSubjectId;
	  // load all the tacking for given date range, class and the subject .. no need to go for the chapter
 	 }
  ngOnInit(): void {
    this.selectedClassId=-1;
  }

  trackId(index: number, item: IClassLessionPlanTrack): number {
    return item.id!;
  }
  
   lessionPlanMarked(): boolean {
    this.classLessionPlanTrack = this.classLessionPlanTracks![0];
    return  this.classLessionPlanTrack.cancelDate?true:false;
  }
save(classLessionPlan: IClassLessionPlanTrack, status:string): void {

	if(status==="InProgress"){
		classLessionPlan.workStatus=TaskStatus.InProgress;
	}else if(status==="OnTrack"){
		classLessionPlan.workStatus=TaskStatus.OnTrack;
	}else if(status==="OffTrack"){
		classLessionPlan.workStatus=TaskStatus.OffTrack;
	}else {
		return;
	}
	    
	    this.classLessionPlanTrackService.update(classLessionPlan).subscribe(
      () => {
        this.isSaving = false;
       },
      () => {
        this.isSaving = false;
      }
    );
	  }
	  
	    saveAll(): void {
	    this.classLessionPlanTrackService.updateAll(this.classLessionPlanTracks?this.classLessionPlanTracks:[]).subscribe(
      (res: HttpResponse<IClassLessionPlanTrack[]>) => {
        this.isLoading = false;
        this.classLessionPlanTracks = res.body ?? [];
      },
      () => {
        this.isLoading = false;
 
      }
    );
	  }
//  delete(classLessionPlanTrack: IClassLessionPlanTrack): void {
//    const modalRef = this.modalService.open(ClassLessionPlanTrackDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
//    modalRef.componentInstance.classLessionPlanTrack = classLessionPlanTrack;
//    // unsubscribe not needed because closed completes on modal close
//    modalRef.closed.subscribe(reason => {
//      if (reason === 'deleted') {
//        this.loadPage();
//      }
//    });
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

  protected onSuccess(data: IClassLessionPlanTrack[] | null, headers: HttpHeaders, page: number, navigate: boolean): void {
    this.totalItems = Number(headers.get('X-Total-Count'));
    this.page = page;
    if (navigate) {
      this.router.navigate(['/class-lession-plan-track'], {
        queryParams: {
          page: this.page,
          size: this.itemsPerPage,
          sort: this.predicate + ',' + (this.ascending ? 'asc' : 'desc'),
        },
      });
    }
    this.classLessionPlanTracks = data ?? [];
    this.ngbPaginationPage = this.page;
  }

  protected onError(): void {
    this.ngbPaginationPage = this.page ?? 1;
  }
}
