import { Component, Input } from '@angular/core';
import { IClassStudent } from '../../entities/class-student/class-student.model';
import { HttpResponse } from '@angular/common/http';
import { ClassStudentService } from '../../entities/class-student/service/class-student.service';
import { StudentAttendenceService } from '../../entities/student-attendence/service/student-attendence.service';

@Component({
  selector: 'jhi-class-student-dropdown',
  templateUrl: './class.student.dropdown.component.html',
})
export class ClassStudentDropdownComponent  {
  @Input() callbackFunction!: (...arg:any) => void;

  classStudents: IClassStudent[] = [];
  selectedStudentId:any;
  selectedClassId:any;
  isLoading = false;

  constructor(protected classStudentService: ClassStudentService,protected studentAttendanceService: StudentAttendenceService) {
    
  }
 
   loadAllStudentsForAClass(selectedClassId:any): void {
 
    this.isLoading = true;
    this.classStudents = [];
    this.selectedClassId=selectedClassId;
    this.classStudentService.query({ 'schoolClassId.equals': selectedClassId, size: 60 }).subscribe(
      (res: HttpResponse<IClassStudent[]>) => {
        this.isLoading = false;
        this.classStudents = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }
  
  loadAllStudentsForAClassCallback = (selectedClassId:any): void => {
  // callback code here
  this.loadAllStudentsForAClass(selectedClassId);
  // this.classSubjects=classSubjects;
  }
   /** This is called from the html file on event of a class selection , ideally this should be the call to callback function and the callback should decide what to do further!
  
  */
    callCallBackFunction(): void {
         this.callbackFunction(this.selectedClassId,this.selectedStudentId);
      
  }
  trackFilterStudentId(index: number, item: IClassStudent): number {
    return item.id!;
  }
  
  
}
