import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IStudentHomeWorkTrack } from '../student-home-work-track.model';

@Component({
  selector: 'jhi-student-home-work-track-detail',
  templateUrl: './student-home-work-track-detail.component.html',
})
export class StudentHomeWorkTrackDetailComponent implements OnInit {
  studentHomeWorkTrack: IStudentHomeWorkTrack | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ studentHomeWorkTrack }) => {
      this.studentHomeWorkTrack = studentHomeWorkTrack;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
