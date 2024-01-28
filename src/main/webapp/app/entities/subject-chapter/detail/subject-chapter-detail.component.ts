import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ISubjectChapter } from '../subject-chapter.model';

@Component({
  selector: 'jhi-subject-chapter-detail',
  templateUrl: './subject-chapter-detail.component.html',
})
export class SubjectChapterDetailComponent implements OnInit {
  subjectChapter: ISubjectChapter | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ subjectChapter }) => {
      this.subjectChapter = subjectChapter;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
