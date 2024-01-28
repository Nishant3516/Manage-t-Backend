import { Component, OnChanges, Input , SimpleChanges} from '@angular/core';
import { IClassSubject } from 'app/entities/class-subject/class-subject.model';
import { SchoolClassService } from 'app/entities/school-class/service/school-class.service';
import { HttpResponse } from '@angular/common/http';
import { ClassSubjectService } from 'app/entities/class-subject/service/class-subject.service';

@Component({
  selector: 'jhi-subject-dropdown',
  templateUrl: './subject.dropdown.component.html',
})
export class SubjectDropdownComponent implements OnChanges {
  @Input() classId = '';
@Input() questionTypeId = '';
   @Input() callbackFunction!: (args: any) => void;

  selectedSubjectId: any;
   classSubjects: IClassSubject[] = [];
  
  isLoading = false;

  constructor(protected schoolClassService: SchoolClassService, protected classSubjectService : ClassSubjectService) {
    
  }
   ngOnChanges(changes: SimpleChanges) : void{
       // changes.prop contains the old and the new value...
    this.loadSubjects(this.classId);
  }
  
  loadSubjects(selectedClassId:any): void {
	// alert("in subjectdropdown "+this.questionTypeId);
  if(selectedClassId&&selectedClassId>0){
  this.classSubjects =  [];
        this.isLoading = true;
       
    this.classSubjectService.query({ 'schoolClassId.equals': selectedClassId }).subscribe(
      (res: HttpResponse<IClassSubject[]>) => {
        this.isLoading = false;
        this.classSubjects = res.body ?? [];
        // this.callbackFunction(this.classSubjects);
      },
      () => {
      	alert("Error occured while fetching the class subjects ");
        this.isLoading = false;
      }
    );
    }
  }
  
  /** This is called from the html file on event of a class selection
  
  */
    callCallBackFunction(): void {        
        this.callbackFunction(this.selectedSubjectId);
      }
  
  
  trackFilterClassSubjectId(index: number, item: IClassSubject): number {
    return item.id!;
  }
}
