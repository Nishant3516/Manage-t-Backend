import { Component, OnInit, Input } from '@angular/core';
import { ISchoolClass } from 'app/entities/school-class/school-class.model';
import { SchoolClassService } from 'app/entities/school-class/service/school-class.service';
import { HttpResponse } from '@angular/common/http';
@Component({
  selector: 'jhi-class-dropdown',
  templateUrl: './class.dropdown.component.html',
})
export class ClassDropdownComponent implements OnInit {
  @Input() callbackFunction!: (args: any) => void;

  selectedClassId: any;
   schoolClasses: ISchoolClass[] = [];
  
  isLoading = false;

  constructor(protected schoolClassService: SchoolClassService) {
    
  }
  ngOnInit(): void {
    this.loadAllClasses();
  }
  
  loadAllClasses(): void {
    this.isLoading = true;

    this.schoolClassService.query().subscribe(
      (res: HttpResponse<ISchoolClass[]>) => {
        this.isLoading = false;
        this.schoolClasses = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }
   
  
  
  /** This is called from the html file on event of a class selection
  
  */
    callCallBackFunction(): void {        
        this.callbackFunction(this.selectedClassId);
      }
  
  trackFilterClassByIdCommon(index: number, item: ISchoolClass): number {
    return item.id!;
  }
}
