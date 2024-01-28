import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ISchoolEvent } from '../school-event.model';

@Component({
  selector: 'jhi-school-event-detail',
  templateUrl: './school-event-detail.component.html',
})
export class SchoolEventDetailComponent implements OnInit {
  schoolEvent: ISchoolEvent | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ schoolEvent }) => {
      this.schoolEvent = schoolEvent;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
