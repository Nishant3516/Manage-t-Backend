import { Component } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { DATE_FORMAT } from 'app/config/input.constants';

import { IStudentAttendence } from '../student-attendence.model';
import { StudentAttendenceService } from '../service/student-attendence.service';

@Component({
  selector: 'jhi-student-attendence',
  templateUrl: './student-attendence.component.html',
})
export class StudentAttendenceComponent {
  attendanceDate:any;
  attendanceEndDate:any;
  studentAttendences?: IStudentAttendence[];
  studentAttendence?: IStudentAttendence;
  isLoading = false;
  isSaving = false;
  selectedClassId:any;
  boundFunction:any;

  constructor(protected studentAttendenceService: StudentAttendenceService, protected modalService: NgbModal) {
  	this.boundFunction=this.loadAllAttendanceForAClassCallBack.bind(this);
  }

 
  

 
	  loadAllAttendanceForAClassCallBack(selectedClassId:any) : void{
	    
	  	 this.selectedClassId=selectedClassId;
	  	 this.loadAllAttendanceForAClass();
	
	  }
  loadAllAttendanceForAClass(): void {
    this.isLoading = true;
	this.attendanceDate= this.attendanceDate?.isValid() ? this.attendanceDate.format(DATE_FORMAT) : undefined,
	this.attendanceEndDate= this.attendanceEndDate?.isValid() ? this.attendanceEndDate.format(DATE_FORMAT) : undefined,
	this.selectedClassId=this.selectedClassId?this.selectedClassId:0;
		
	
    this.studentAttendenceService.query({ 'classId': this.selectedClassId , 'startDate': this.attendanceDate ,'endDate': this.attendanceEndDate }).subscribe(
      (res: HttpResponse<IStudentAttendence[]>) => {
        this.isLoading = false;
        this.studentAttendences = res.body ?? [];
      },
      () => {
        this.isLoading = false;
 
      }
    );
  }
  trackId(index: number, item: IStudentAttendence): number {
    return item.id!;
  }
	save(studentAttendence: IStudentAttendence, present:boolean): void {
	    this.isSaving = true;
	    studentAttendence.attendence=present;
	    this.studentAttendenceService.update(studentAttendence).subscribe(
      () => {
        this.isSaving = false;
       },
      () => {
        this.isSaving = false;
      }
    );
	  }
  attendanceMarked(): boolean {
    this.studentAttendence = this.studentAttendences![0];
    return  this.studentAttendence.cancelDate?true:false;
  }
	  
	  saveAll(): void {
	    this.studentAttendenceService.updateAll(this.studentAttendences?this.studentAttendences:[]).subscribe(
      (res: HttpResponse<IStudentAttendence[]>) => {
        this.isLoading = false;
        this.studentAttendences = res.body ?? [];
      },
      () => {
        this.isLoading = false;
 
      }
    );
	  }
//  delete(studentAttendence: IStudentAttendence): void {
//    const modalRef = this.modalService.open(StudentAttendenceDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
//    modalRef.componentInstance.studentAttendence = studentAttendence;
//    // unsubscribe not needed because closed completes on modal close
//    modalRef.closed.subscribe(reason => {
//      if (reason === 'deleted') {
//        // this.loadAll();
//      }
//    });
//  }
}
