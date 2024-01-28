import { Component, OnInit, Input } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { IQuestionType } from 'app/entities/question-type/question-type.model';
import { QuestionTypeService } from 'app/entities/question-type/service/question-type.service';
@Component({
  selector: 'jhi-questiontype-dropdown',
  templateUrl: './questiontype.dropdown.component.html',
})
export class QuestionTypeDropdownComponent implements OnInit {
  @Input() callbackFunction!: (args: any) => void;

  selectedQuestionTypeId: any;
   questionTypes: IQuestionType[] = [];
  
  isLoading = false;

  constructor(protected questionTypeService: QuestionTypeService) {
    
  }
  ngOnInit(): void {
    this.loadAllQuestionTypes();
  }
  
  loadAllQuestionTypes(): void {
    this.isLoading = true;

    this.questionTypeService.query().subscribe(
      (res: HttpResponse<IQuestionType[]>) => {
        this.isLoading = false;
        this.questionTypes = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }
   
  
  
  /** This is called from the html file on event of a class selection
  
  */
    callCallBackFunction(): void {        
        this.callbackFunction(this.selectedQuestionTypeId);
      }
  
  trackFilterQuestionTypeByIdCommon(index: number, item: IQuestionType): number {
    return item.id!;
  }
}
