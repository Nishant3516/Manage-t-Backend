import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ISchoolUser } from '../school-user.model';

@Component({
  selector: 'jhi-school-user-detail',
  templateUrl: './school-user-detail.component.html',
})
export class SchoolUserDetailComponent implements OnInit {
  schoolUser: ISchoolUser | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ schoolUser }) => {
      this.schoolUser = schoolUser;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
