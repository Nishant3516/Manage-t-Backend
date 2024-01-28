import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IStudentDiscount } from '../student-discount.model';
import { StudentDiscountService } from '../service/student-discount.service';
import { StudentDiscountDeleteDialogComponent } from '../delete/student-discount-delete-dialog.component';
import { SchoolClassService } from 'app/entities/school-class/service/school-class.service';
import { ClassStudentService } from 'app/entities/class-student/service/class-student.service';
import { StudentClassAndStudentFilter } from '../../../shared/filters/classandstudent.filter';

@Component({
  selector: 'jhi-student-discount',
  templateUrl: './student-discount.component.html',
})
export class StudentDiscountComponent extends StudentClassAndStudentFilter implements OnInit {
  studentDiscounts?: IStudentDiscount[];
  isLoading = false;

  constructor(
    protected studentDiscountService: StudentDiscountService,
    protected schoolClassService: SchoolClassService,
    protected classStudentService: ClassStudentService,
    protected modalService: NgbModal
  ) {
    super(schoolClassService, classStudentService);
  }

  loadAll(): void {
    this.isLoading = true;
	this.loadAllClasses();
//    this.studentDiscountService.query().subscribe(
//      (res: HttpResponse<IStudentDiscount[]>) => {
//        this.isLoading = false;
//        this.studentDiscounts = res.body ?? [];
//        this.loadAllClasses();
//      },
//      () => {
//        this.isLoading = false;
//      }
//    );
  }
  loadDiscountsForAStudent(): void {
    this.isLoading = true;
    this.studentDiscounts = [];
    this.studentDiscountService.query({ 'classStudentId.equals': this.selectedStudentId }).subscribe(
      (res: HttpResponse<IStudentDiscount[]>) => {
        this.isLoading = false;
        this.studentDiscounts = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }
  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IStudentDiscount): number {
    return item.id!;
  }

  delete(studentDiscount: IStudentDiscount): void {
    const modalRef = this.modalService.open(StudentDiscountDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.studentDiscount = studentDiscount;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
