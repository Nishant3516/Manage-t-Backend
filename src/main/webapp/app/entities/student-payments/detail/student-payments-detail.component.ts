import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IStudentPayments } from '../student-payments.model';

@Component({
  selector: 'jhi-student-payments-detail',
  templateUrl: './student-payments-detail.component.html',
})
export class StudentPaymentsDetailComponent implements OnInit {
  studentPayments: IStudentPayments | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ studentPayments }) => {
      this.studentPayments = studentPayments;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
