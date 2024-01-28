import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IClassSubject } from '../class-subject.model';

@Component({
  selector: 'jhi-class-subject-detail',
  templateUrl: './class-subject-detail.component.html',
})
export class ClassSubjectDetailComponent implements OnInit {
  classSubject: IClassSubject | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ classSubject }) => {
      this.classSubject = classSubject;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
