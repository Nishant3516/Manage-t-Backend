import { Component, OnInit } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { combineLatest } from 'rxjs';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IClassStudent } from '../class-student.model';

import { ITEMS_PER_PAGE } from 'app/config/pagination.constants';
import { ClassStudentService } from '../service/class-student.service';
import { ClassStudentDeleteDialogComponent } from '../delete/class-student-delete-dialog.component';
import { DataUtils } from 'app/core/util/data-util.service';
import { SchoolClassFilter } from '../../../shared/filters/class.filter';
import { SchoolClassService } from 'app/entities/school-class/service/school-class.service';

@Component({
  selector: 'jhi-class-student',
  templateUrl: './class-student.component.html',
})
export class ClassStudentComponent extends SchoolClassFilter implements OnInit {
  classStudents?: IClassStudent[];
  isLoading = false;
  totalItems = 0;
  itemsPerPage = ITEMS_PER_PAGE;
  page?: number;
  predicate!: string;
  ascending!: boolean;
  ngbPaginationPage = 1;

  constructor(
    protected classStudentService: ClassStudentService,
    protected schoolClassService: SchoolClassService,
    protected activatedRoute: ActivatedRoute,
    protected dataUtils: DataUtils,
    protected router: Router,
    protected modalService: NgbModal
  ) {
    super(schoolClassService);
  }

  loadPage(page?: number, dontNavigate?: boolean): void {
    this.isLoading = true;
    const pageToLoad: number = page ?? this.page ?? 1;
	this.selectedClassId=this.selectedClassId?this.selectedClassId:-1;

    this.classStudentService
      .query({
        page: pageToLoad - 1,
        size: 60,
        sort: this.sort(),
        'schoolClassId.equals': this.selectedClassId,
      })
      .subscribe(
        (res: HttpResponse<IClassStudent[]>) => {
          this.isLoading = false;
          this.onSuccess(res.body, res.headers, pageToLoad, !dontNavigate);
        },
        () => {
          this.isLoading = false;
          this.onError();
        }
      );
  }
loadPageWithDeleted(page?: number, dontNavigate?: boolean): void {
this.classStudents=[];
	this.selectedClassId=this.selectedClassId?this.selectedClassId:1;
    this.isLoading = true;
    const pageToLoad: number = page ?? this.page ?? 1;

    this.classStudentService
      .query({
        page: pageToLoad - 1,
        size: 60,
        sort: this.sort(),
        'schoolClassId.equals': this.selectedClassId,
        'cancelDate.specified':true,

      })
      .subscribe(
        (res: HttpResponse<IClassStudent[]>) => {
          this.isLoading = false;
          this.onSuccess(res.body, res.headers, pageToLoad, !dontNavigate);
        },
        () => {
          this.isLoading = false;
          this.onError();
        }
      );
  }  
  loadAllStudentsForAClass(): void {
    this.isLoading = true;

    this.classStudentService.query({ 'schoolClassId.equals': this.selectedClassId, size: 60, sort: this.sort() }).subscribe(
      (res: HttpResponse<IClassStudent[]>) => {
        this.isLoading = false;
        this.classStudents = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }
  ngOnInit(): void {
    this.loadAllClasses();
    this.handleNavigation();
  }

  trackId(index: number, item: IClassStudent): number {
    return item.id!;
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    return this.dataUtils.openFile(base64String, contentType);
  }

  delete(classStudent: IClassStudent): void {
    const modalRef = this.modalService.open(ClassStudentDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.classStudent = classStudent;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAllStudentsForAClass();
      }
    });
  }
  restore(classStudent: IClassStudent): void {
    const modalRef = this.modalService.open(ClassStudentDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.classStudent = classStudent;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAllStudentsForAClass();
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

  protected onSuccess(data: IClassStudent[] | null, headers: HttpHeaders, page: number, navigate: boolean): void {
    this.totalItems = Number(headers.get('X-Total-Count'));
    this.page = page;
    if (navigate) {
      this.router.navigate(['/class-student'], {
        queryParams: {
          page: this.page,
          size: this.itemsPerPage,
          sort: this.predicate + ',' + (this.ascending ? 'asc' : 'desc'),
        },
      });
    }
    this.classStudents = data ?? [];
    this.ngbPaginationPage = this.page;
  }

  protected onError(): void {
    this.ngbPaginationPage = this.page ?? 1;
  }
}
