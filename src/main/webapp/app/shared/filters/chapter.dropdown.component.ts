import { Component, OnChanges, Input , SimpleChanges} from '@angular/core';
import { SchoolClassService } from 'app/entities/school-class/service/school-class.service';
import { HttpResponse } from '@angular/common/http';
import { ClassSubjectService } from 'app/entities/class-subject/service/class-subject.service';
import { ISubjectChapter } from 'app/entities/subject-chapter/subject-chapter.model';
import { SubjectChapterService } from 'app/entities/subject-chapter/service/subject-chapter.service';

@Component({
  selector: 'jhi-chapter-dropdown',
  templateUrl: './chapter.dropdown.component.html',
})
export class ChapterDropdownComponent implements OnChanges {
  @Input() selectedSubjectId = '';
   @Input() callbackFunction!: (args: any) => void;

  selectedChapterId: any;
   subjectChapters: ISubjectChapter[] = [];
  
  isLoading = false;

  constructor(protected schoolClassService: SchoolClassService, 
  protected classSubjectService : ClassSubjectService,
  protected subjectChapterService:SubjectChapterService) {
    
  }
   ngOnChanges(changes: SimpleChanges) : void{
       // changes.prop contains the old and the new value...
    this.loadChapters(this.selectedSubjectId);
  }
  
  loadChapters(selectedSubjectId:any): void {
  if(selectedSubjectId){

  this.subjectChapterService.query({ 'classSubjectId.equals': selectedSubjectId }).subscribe(
      (res: HttpResponse<ISubjectChapter[]>) => {
        this.isLoading = false;
        this.subjectChapters = res.body ?? [];
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
        this.callbackFunction(this.selectedChapterId);
      }
  
  
  trackFilterSubjectChapterId(index: number, item: ISubjectChapter): number {
    return item.id!;
  }
}
