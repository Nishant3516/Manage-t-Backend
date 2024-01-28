import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IStudentChargesSummary } from '../student-charges-summary.model';

@Component({
  selector: 'jhi-student-charges-summary-detail',
  templateUrl: './student-charges-summary-detail.component.html',
})
export class StudentChargesSummaryDetailComponent implements OnInit {
  studentChargesSummary: IStudentChargesSummary | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ studentChargesSummary }) => {
      this.studentChargesSummary = studentChargesSummary;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
