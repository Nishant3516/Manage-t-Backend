import { Component, OnChanges, Input , SimpleChanges} from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { IClassStudent } from 'app/entities/class-student/class-student.model';
import { ClassStudentService } from 'app/entities/class-student/service/class-student.service';

@Component({
  selector: 'jhi-student-dropdown',
  templateUrl: './student.dropdown.component.html',
})
export class StudentDropdownComponent implements OnChanges {
  @Input() selectedClassId = '';
   @Input() callbackFunction!: (args: any) => void;

  selectedStudentId: any;
   classStudents: IClassStudent[] = [];
  
  isLoading = false;

  constructor(protected classStudentService: ClassStudentService) {
    
  }
   ngOnChanges(changes: SimpleChanges) : void{
       // changes.prop contains the old and the new value...
    this.loadStudents(this.selectedClassId);
  }
  
  loadStudents(selectedClassId:any): void {
  if(selectedClassId){

  this.classStudents =  [];
        this.isLoading = true;
      this.classStudentService
      .query({
        size: 60,
        'schoolClassId.equals': this.selectedClassId,
      })
      .subscribe(
        (res: HttpResponse<IClassStudent[]>) => {
          this.isLoading = false;
          this.classStudents= res.body ?? [];
        },
        () => {
          this.isLoading = false;
        
        }
      );
    }
  }
  
  /** This is called from the html file on event of a class selection
  
  */
    callCallBackFunction(): void {        
        this.callbackFunction(this.selectedStudentId);
      }
  
  
  trackFilterClassStudentId(index: number, item: IClassStudent): number {
    return item.id!;
  }
}
