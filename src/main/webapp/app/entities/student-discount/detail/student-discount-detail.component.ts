import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IStudentDiscount } from '../student-discount.model';

@Component({
  selector: 'jhi-student-discount-detail',
  templateUrl: './student-discount-detail.component.html',
})
export class StudentDiscountDetailComponent implements OnInit {
  studentDiscount: IStudentDiscount | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ studentDiscount }) => {
      this.studentDiscount = studentDiscount;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
