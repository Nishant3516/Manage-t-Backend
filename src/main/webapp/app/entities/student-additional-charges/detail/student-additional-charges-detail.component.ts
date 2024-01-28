import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IStudentAdditionalCharges } from '../student-additional-charges.model';

@Component({
  selector: 'jhi-student-additional-charges-detail',
  templateUrl: './student-additional-charges-detail.component.html',
})
export class StudentAdditionalChargesDetailComponent implements OnInit {
  studentAdditionalCharges: IStudentAdditionalCharges | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ studentAdditionalCharges }) => {
      this.studentAdditionalCharges = studentAdditionalCharges;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
