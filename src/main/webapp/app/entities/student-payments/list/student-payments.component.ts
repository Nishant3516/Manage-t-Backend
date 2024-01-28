import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IStudentPayments } from '../student-payments.model';
import { StudentPaymentsService } from '../service/student-payments.service';
import { StudentPaymentsDeleteDialogComponent } from '../delete/student-payments-delete-dialog.component';
import { SchoolClassService } from 'app/entities/school-class/service/school-class.service';
import { ClassStudentService } from 'app/entities/class-student/service/class-student.service';
import { StudentClassAndStudentFilter } from '../../../shared/filters/classandstudent.filter';

@Component({
  selector: 'jhi-student-payments',
  templateUrl: './student-payments.component.html',
})
export class StudentPaymentsComponent extends StudentClassAndStudentFilter implements OnInit {
  studentPayments?: IStudentPayments[];
  isLoading = false;

  constructor(
    protected studentPaymentsService: StudentPaymentsService,
    protected schoolClassService: SchoolClassService,
    protected classStudentService: ClassStudentService,
    protected modalService: NgbModal
  ) {
    super(schoolClassService, classStudentService);
  }

  loadAll(): void {
    this.isLoading = true;

    this.studentPaymentsService.query().subscribe(
      (res: HttpResponse<IStudentPayments[]>) => {
        this.isLoading = false;
        this.studentPayments = res.body ?? [];
        
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAllClasses();
  }
  loadPaymentsForAStudent(): void {
    this.isLoading = true;
    this.studentPayments = [];
    this.studentPaymentsService.query({ 'classStudentId.equals': this.selectedStudentId }).subscribe(
      (res: HttpResponse<IStudentPayments[]>) => {
        this.isLoading = false;
        this.studentPayments = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }
  trackId(index: number, item: IStudentPayments): number {
    return item.id!;
  }

  delete(studentPayments: IStudentPayments): void {
    const modalRef = this.modalService.open(StudentPaymentsDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.studentPayments = studentPayments;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        // this.loadAll();
        this.loadPaymentsForAStudent();
      }
    });
  }
}
