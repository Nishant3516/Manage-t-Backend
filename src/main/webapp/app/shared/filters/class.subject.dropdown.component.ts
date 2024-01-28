import { Component, Input } from '@angular/core';
import { ISubjectChapter } from 'app/entities/subject-chapter/subject-chapter.model';
import { HttpResponse } from '@angular/common/http';
import { IClassSubject } from 'app/entities/class-subject/class-subject.model';
import { SubjectChapterService } from 'app/entities/subject-chapter/service/subject-chapter.service';
import { ClassSubjectService } from 'app/entities/class-subject/service/class-subject.service';

@Component({
  selector: 'jhi-class-subject-dropdown',
  templateUrl: './class.subject.dropdown.component.html',
})
export class ClassSubjectDropdownComponent  {
  @Input() callbackFunction!: (...args: any) => void;

  classSubjects:IClassSubject[] =[];
  selectedSubjectId: any;
  subjectChapters: ISubjectChapter[] = [];   
  isLoading = false;
  selectedClassId:any;

  constructor(protected subjectChapterService: SubjectChapterService, protected classSubjectService: ClassSubjectService) {
    
  }
  
  
    loadAllChaptersForASubject(): void {
        this.isLoading = true;
	    this.subjectChapterService.query({ 'classSubjectId.equals': this.selectedSubjectId }).subscribe(
      (res: HttpResponse<ISubjectChapter[]>) => {
        this.isLoading = false;
        this.subjectChapters = res.body ?? [];
        this.callbackFunction(this.subjectChapters,this.selectedSubjectId, this.selectedClassId);
      },
      () => {
      	alert("Error occured while fetching the class subjects ");
        this.isLoading = false;
      }
    );
  }
   loadAllSubjectsForAClassCallback = (selectedClassId:any): void => {
  // callback code here
  this.loadSubjects(selectedClassId);
  this.selectedClassId=selectedClassId;
  // this.classSubjects=classSubjects;
  }
loadSubjects(selectedClassId:any): void {
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
  trackFilterClassSubjectId(index: number, item:IClassSubject): number {
    return item.id!;
  }
}
