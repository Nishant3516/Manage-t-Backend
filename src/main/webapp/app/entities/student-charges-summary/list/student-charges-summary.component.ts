import { Component, OnInit } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IStudentChargesSummary } from '../student-charges-summary.model';

import { ITEMS_PER_PAGE } from 'app/config/pagination.constants';
import { StudentChargesSummaryService } from '../service/student-charges-summary.service';
import { StudentChargesSummaryDeleteDialogComponent } from '../delete/student-charges-summary-delete-dialog.component';
import { StudentClassAndStudentFilter } from '../../../shared/filters/classandstudent.filter';
import { SchoolClassService } from 'app/entities/school-class/service/school-class.service';
import { ClassStudentService } from 'app/entities/class-student/service/class-student.service';
import { IClassStudent } from 'app/entities/class-student/class-student.model';

@Component({
  selector: 'jhi-student-charges-summary',
  templateUrl: './student-charges-summary.component.html',
})
export class StudentChargesSummaryComponent extends StudentClassAndStudentFilter implements OnInit {
  studentChargesSummaries?: IStudentChargesSummary[];
  isLoading = false;
  totalItems = 0;
  itemsPerPage = ITEMS_PER_PAGE;
  page?: number;
  predicate!: string;
  ascending!: boolean;
  ngbPaginationPage = 1;

  constructor(
    protected studentChargesSummaryService: StudentChargesSummaryService,
    protected schoolClassService: SchoolClassService,
    protected classStudentService: ClassStudentService,
    protected activatedRoute: ActivatedRoute,
    protected router: Router,
    protected modalService: NgbModal
  ) {
    super(schoolClassService, classStudentService);
  }

  loadAllFinancialSummariesForAStudent(): void {
    const schoolStudentIDsForAClass = [];
    schoolStudentIDsForAClass.push(this.selectedStudentId);
    this.isLoading = true;
    this.studentChargesSummaryService.query({ studentIds: this.selectedStudentId, onlyDues: false }).subscribe(
      (res: HttpResponse<IStudentChargesSummary[]>) => {
        this.isLoading = false;
        this.studentChargesSummaries = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }
  loadAllStudentsForAClass(): void {
    this.isLoading = true;
    this.classStudentsForAClass = [];
    this.classStudentService.query({ 'schoolClassId.equals': this.selectedClassId, size: 60 }).subscribe(
      (res: HttpResponse<IClassStudent[]>) => {
        this.isLoading = false;
        this.classStudentsForAClass = res.body ?? [];
        this.loadAllFinancialSummariesForAllStudent();
      },
      () => {
        this.isLoading = false;
      }
    );
  }
  loadAllFinancialSummariesForAllStudent(): void {
    const schoolStudentIDsForAClass = [];
    for (const student of this.classStudentsForAClass) {
      schoolStudentIDsForAClass.push(student.studentId);
    }
    this.isLoading = true;
    this.studentChargesSummaryService.query({ studentIds: schoolStudentIDsForAClass, onlyDues: true }).subscribe(
      (res: HttpResponse<IStudentChargesSummary[]>) => {
        this.isLoading = false;
        this.studentChargesSummaries = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }
  ngOnInit(): void {
    this.loadAllClasses();
  }

  trackId(index: number, item: IStudentChargesSummary): number {
    return item.id!;
  }

  delete(studentChargesSummary: IStudentChargesSummary): void {
    const modalRef = this.modalService.open(StudentChargesSummaryDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.studentChargesSummary = studentChargesSummary;
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAllClasses();
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

  protected onSuccess(data: IStudentChargesSummary[] | null, headers: HttpHeaders, page: number, navigate: boolean): void {
    this.totalItems = Number(headers.get('X-Total-Count'));
    this.page = page;
    if (navigate) {
      this.router.navigate(['/student-charges-summary'], {
        queryParams: {
          page: this.page,
          size: this.itemsPerPage,
          sort: this.predicate + ',' + (this.ascending ? 'asc' : 'desc'),
        },
      });
    }
    this.studentChargesSummaries = data ?? [];
    this.ngbPaginationPage = this.page;
  }

  protected onError(): void {
    this.ngbPaginationPage = this.page ?? 1;
  }
}
