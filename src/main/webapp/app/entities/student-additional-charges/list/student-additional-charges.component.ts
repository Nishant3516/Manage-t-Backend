import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IStudentAdditionalCharges } from '../student-additional-charges.model';
import { StudentAdditionalChargesService } from '../service/student-additional-charges.service';
import { StudentAdditionalChargesDeleteDialogComponent } from '../delete/student-additional-charges-delete-dialog.component';
import { SchoolClassService } from 'app/entities/school-class/service/school-class.service';
import { ClassStudentService } from 'app/entities/class-student/service/class-student.service';

@Component({
  selector: 'jhi-student-additional-charges',
  templateUrl: './student-additional-charges.component.html',
})
export class StudentAdditionalChargesComponent  {
  studentAdditionalCharges?: IStudentAdditionalCharges[];
  isLoading = false;
  selectedStudentId:any;
  selectedClassId:any;

  constructor(
    protected studentAdditionalChargesService: StudentAdditionalChargesService,
    protected schoolClassService: SchoolClassService,
    protected classStudentService: ClassStudentService,
    protected modalService: NgbModal
  ) {
   
  }
getClassIdCallBack = (selectedClassId: any): void => {
	  // callback code here
	   this.selectedClassId=selectedClassId;
		this.loadAdditionalChargesForAClass();
	   
  	}
//  loadAll(): void {
//    this.isLoading = true;
//
//    this.studentAdditionalChargesService.query().subscribe(
//      (res: HttpResponse<IStudentAdditionalCharges[]>) => {
//        this.isLoading = false;
//        this.studentAdditionalCharges = res.body ?? [];
//      },
//      () => {
//        this.isLoading = false;
//      }
//    );
//  }
getStudentIdCallBack = (selectedStudentId: any): void => {
	  // callback code here
	  this.selectedStudentId=selectedStudentId;
	  // load all the tacking for given date range, class and the subject .. no need to go for the chapter
	this.loadAdditionalChargesForAStudent();
 	 }

 loadAdditionalChargesForAClass(): void {
    this.isLoading = true;
    this.studentAdditionalCharges = [];
    this.studentAdditionalChargesService.query({'studentClassId' :this.selectedClassId }).subscribe(
      (res: HttpResponse<IStudentAdditionalCharges[]>) => {
        this.isLoading = false;
        this.studentAdditionalCharges = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }
  loadAdditionalChargesForAStudent(): void {
    this.isLoading = true;
    this.studentAdditionalCharges = [];
    this.studentAdditionalChargesService.query({ 'classStudentId.equals': this.selectedStudentId, 'studentClassId' :-1 }).subscribe(
      (res: HttpResponse<IStudentAdditionalCharges[]>) => {
        this.isLoading = false;
        this.studentAdditionalCharges = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

//  ngOnInit(): void {
//    this.loadAll();
//  }

  trackId(index: number, item: IStudentAdditionalCharges): number {
    return item.id!;
  }

  delete(studentAdditionalCharges: IStudentAdditionalCharges): void {
    const modalRef = this.modalService.open(StudentAdditionalChargesDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.studentAdditionalCharges = studentAdditionalCharges;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
         this.loadAdditionalChargesForAStudent();
      }
    });
  }
}
