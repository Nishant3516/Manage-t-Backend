import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ISchoolClass } from '../school-class.model';

@Component({
  selector: 'jhi-school-class-detail',
  templateUrl: './school-class-detail.component.html',
})
export class SchoolClassDetailComponent implements OnInit {
  schoolClass: ISchoolClass | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ schoolClass }) => {
      this.schoolClass = schoolClass;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
