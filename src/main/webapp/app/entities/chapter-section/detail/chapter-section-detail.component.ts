import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IChapterSection } from '../chapter-section.model';

@Component({
  selector: 'jhi-chapter-section-detail',
  templateUrl: './chapter-section-detail.component.html',
})
export class ChapterSectionDetailComponent implements OnInit {
  chapterSection: IChapterSection | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ chapterSection }) => {
      this.chapterSection = chapterSection;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
