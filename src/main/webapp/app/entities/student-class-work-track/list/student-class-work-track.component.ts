import { Component,OnInit } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { WorkStatus } from 'app/entities/enumerations/work-status.model';

import { IStudentClassWorkTrack } from '../student-class-work-track.model';
import { ITEMS_PER_PAGE } from 'app/config/pagination.constants';
import { StudentClassWorkTrackService } from '../service/student-class-work-track.service';
import { DATE_FORMAT } from 'app/config/input.constants';

@Component({
  selector: 'jhi-student-class-work-track',
  templateUrl: './student-class-work-track.component.html',
})
export class StudentClassWorkTrackComponent implements OnInit {
  studentClassWorkTracks?: IStudentClassWorkTrack[];
  studentClassWorkTrack?: IStudentClassWorkTrack;
  isLoading = false;
  totalItems = 0;
  itemsPerPage = ITEMS_PER_PAGE;
  page?: number;
  predicate!: string;
  ascending!: boolean;
  ngbPaginationPage = 1;
isSaving = false;
selectedClassId:any;
selectedStudentId:any;
startDate:any;
endDate:any;
selectedSubjectId:any;

  constructor(
    protected studentClassWorkTrackService: StudentClassWorkTrackService,
    protected activatedRoute: ActivatedRoute,
    protected router: Router,
    protected modalService: NgbModal
  ) {}

//  loadPage(page?: number, dontNavigate?: boolean): void {
//    this.isLoading = true;
//    const pageToLoad: number = page ?? this.page ?? 1;
//
//    this.studentClassWorkTrackService
//      .query({
//        page: pageToLoad - 1,
//        size: this.itemsPerPage,
//        sort: this.sort(),
//      })
//      .subscribe(
//        (res: HttpResponse<IStudentClassWorkTrack[]>) => {
//          this.isLoading = false;
//          this.onSuccess(res.body, res.headers, pageToLoad, !dontNavigate);
//        },
//        () => {
//          this.isLoading = false;
//          this.onError();
//        }
//      );
//  }
loadStudentClassWorkTracks() : void{
   	this.startDate= this.startDate?.isValid() ? this.startDate.format(DATE_FORMAT) : undefined,
	this.endDate= this.endDate?.isValid() ? this.endDate.format(DATE_FORMAT) : undefined,

  this.selectedClassId=this.selectedClassId?this.selectedClassId:-1;
  this.selectedSubjectId=this.selectedSubjectId?this.selectedSubjectId:-1;
  this.selectedStudentId=this.selectedStudentId?this.selectedStudentId:-1;
  	this.studentClassWorkTrackService
      .query({
          'schoolClassId':this.selectedClassId,
         'classSubjectId':this.selectedSubjectId,
         'classStudentId':this.selectedStudentId,
         'startDate':this.startDate,
        'endDate':this.endDate,

      })
      .subscribe(
        (res: HttpResponse<IStudentClassWorkTrack[]>) => {
          this.isLoading = false;
          this.studentClassWorkTracks =res.body??[];
        },
        () => {
          this.isLoading = false;
          this.onError();
        }
      );
  }
  ngOnInit(): void {
    this.selectedClassId=-1;
  }

  trackId(index: number, item: IStudentClassWorkTrack): number {
    return item.id!;
  }
  
   	getClassIdCallBack = (selectedClassId: any): void => {
	  // callback code here
	   this.selectedClassId=selectedClassId;
	   
  	}
  	getStudentIdCallBack = (selectedStudentId: any): void => {
	  // callback code here
	  this.selectedStudentId=selectedStudentId;
	  // load all the tacking for given date range, class and the subject .. no need to go for the chapter
 	 }
 	getSubjectIdCallBack = (selectedSubjectId: any): void => {
	  // callback code here
	  this.selectedSubjectId=selectedSubjectId;
	  // load all the tacking for given date range, class and the subject .. no need to go for the chapter
 	 } 
save(classClassWorkTrack: IStudentClassWorkTrack, present:boolean): void {
	    this.isSaving = true;
	    classClassWorkTrack.workStatus=present?WorkStatus.Done:WorkStatus.NotDone;
	    this.studentClassWorkTrackService.update(classClassWorkTrack).subscribe(
      () => {
        this.isSaving = false;
       },
      () => {
        this.isSaving = false;
      }
    );
	  }
	  
	  studentClassWorkMarked(): boolean {
    this.studentClassWorkTrack = this.studentClassWorkTracks![0];
    return  this.studentClassWorkTrack.cancelDate?true:false;
  }
	  
	  saveAll(): void {
	    this.studentClassWorkTrackService.updateAll(this.studentClassWorkTracks?this.studentClassWorkTracks:[]).subscribe(
      (res: HttpResponse<IStudentClassWorkTrack[]>) => {
        this.isLoading = false;
        this.studentClassWorkTracks = res.body ?? [];
      },
      () => {
        this.isLoading = false;
 
      }
    );
	  }
//  delete(studentClassWorkTrack: IStudentClassWorkTrack): void {
//    const modalRef = this.modalService.open(StudentClassWorkTrackDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
//    modalRef.componentInstance.studentClassWorkTrack = studentClassWorkTrack;
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

  protected onSuccess(data: IStudentClassWorkTrack[] | null, headers: HttpHeaders, page: number, navigate: boolean): void {
    this.totalItems = Number(headers.get('X-Total-Count'));
    this.page = page;
    if (navigate) {
      this.router.navigate(['/student-class-work-track'], {
        queryParams: {
          page: this.page,
          size: this.itemsPerPage,
          sort: this.predicate + ',' + (this.ascending ? 'asc' : 'desc'),
        },
      });
    }
    this.studentClassWorkTracks = data ?? [];
    this.ngbPaginationPage = this.page;
  }

  protected onError(): void {
    this.ngbPaginationPage = this.page ?? 1;
  }
}
