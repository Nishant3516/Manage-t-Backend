import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IStudentAttendence } from '../student-attendence.model';

@Component({
  selector: 'jhi-student-attendence-detail',
  templateUrl: './student-attendence-detail.component.html',
})
export class StudentAttendenceDetailComponent implements OnInit {
  studentAttendence: IStudentAttendence | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ studentAttendence }) => {
      this.studentAttendence = studentAttendence;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
