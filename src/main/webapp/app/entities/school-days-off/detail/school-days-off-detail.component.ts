import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ISchoolDaysOff } from '../school-days-off.model';

@Component({
  selector: 'jhi-school-days-off-detail',
  templateUrl: './school-days-off-detail.component.html',
})
export class SchoolDaysOffDetailComponent implements OnInit {
  schoolDaysOff: ISchoolDaysOff | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ schoolDaysOff }) => {
      this.schoolDaysOff = schoolDaysOff;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
