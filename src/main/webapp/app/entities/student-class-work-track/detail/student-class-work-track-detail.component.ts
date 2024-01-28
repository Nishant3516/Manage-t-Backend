import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IStudentClassWorkTrack } from '../student-class-work-track.model';

@Component({
  selector: 'jhi-student-class-work-track-detail',
  templateUrl: './student-class-work-track-detail.component.html',
})
export class StudentClassWorkTrackDetailComponent implements OnInit {
  studentClassWorkTrack: IStudentClassWorkTrack | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ studentClassWorkTrack }) => {
      this.studentClassWorkTrack = studentClassWorkTrack;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
