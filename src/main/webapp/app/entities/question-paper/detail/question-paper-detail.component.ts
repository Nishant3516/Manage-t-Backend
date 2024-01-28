import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IQuestionPaper } from '../question-paper.model';
import { DataUtils } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-question-paper-detail',
  templateUrl: './question-paper-detail.component.html',
})
export class QuestionPaperDetailComponent implements OnInit {
  questionPaper: IQuestionPaper | null = null;

  constructor(protected dataUtils: DataUtils, protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ questionPaper }) => {
      this.questionPaper = questionPaper;
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }

  previousState(): void {
    window.history.back();
  }
}
