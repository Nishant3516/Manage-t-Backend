import { Component } from '@angular/core';
import { HttpResponse } from '@angular/common/http';

import { IStudentAttendence } from '../student-attendence.model';
import { StudentAttendenceService } from '../service/student-attendence.service';
import { IClassStudent } from 'app/entities/class-student/class-student.model';
import { DATE_FORMAT } from 'app/config/input.constants';


@Component({
  selector: 'jhi-student-attendence-update',
  templateUrl: './student-attendence-update.component.html',
})
export class StudentAttendenceUpdateComponent  {
  attendanceDate:any;
  attendanceEndDate:any;
  studentAttendences?: IStudentAttendence[];
  isLoading = false;
  selectedClassId:any;
  boundFunction:any;
  classStudentsSharedCollection: IClassStudent[] = [];
 selectedStudentId:any;


    constructor(	protected studentAttendenceService: StudentAttendenceService  ) {  	
    	this.boundFunction=this.loadAllAttendanceForAClassCallBack.bind(this);
	}
  
	loadAllAttendanceForAClassCallBack(selectedClassId:any, selectedStudentId:any) : void{
	  	 this.selectedClassId=selectedClassId;
	  	 this.selectedStudentId=selectedStudentId;
	  	 this.loadAllAttendanceForAClass();
	
	  }
	  
  loadAllAttendanceForAClass(): void {
    this.isLoading = true;
	this.attendanceDate= this.attendanceDate?.isValid() ? this.attendanceDate.format(DATE_FORMAT) : undefined,
	this.attendanceEndDate= this.attendanceEndDate?.isValid() ? this.attendanceEndDate.format(DATE_FORMAT) : undefined,
    this.studentAttendenceService.query({ 'classId': this.selectedClassId , 'startDate': this.attendanceDate ,'endDate': this.attendanceEndDate,'studentId':this.selectedStudentId }).subscribe(
      (res: HttpResponse<IStudentAttendence[]>) => {
        this.isLoading = false;
        this.studentAttendences = res.body ?? [];
      },
      () => {
        this.isLoading = false;
 
      }
    );
  }
   previousState(): void {
    window.history.back();
  }
  trackId(index: number, item: IStudentAttendence): number {
    return item.id!;
  }



  

  

  trackClassStudentById(index: number, item: IClassStudent): number {
    return item.id!;
  }

  

}
