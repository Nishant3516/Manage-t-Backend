import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IClassLessionPlanTrack } from '../class-lession-plan-track.model';

@Component({
  selector: 'jhi-class-lession-plan-track-detail',
  templateUrl: './class-lession-plan-track-detail.component.html',
})
export class ClassLessionPlanTrackDetailComponent implements OnInit {
  classLessionPlanTrack: IClassLessionPlanTrack | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ classLessionPlanTrack }) => {
      this.classLessionPlanTrack = classLessionPlanTrack;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
