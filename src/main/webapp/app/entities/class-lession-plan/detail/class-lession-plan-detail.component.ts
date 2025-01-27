import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IClassLessionPlan } from '../class-lession-plan.model';
import { DataUtils } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-class-lession-plan-detail',
  templateUrl: './class-lession-plan-detail.component.html',
})
export class ClassLessionPlanDetailComponent implements OnInit {
  classLessionPlan: IClassLessionPlan | null = null;

  constructor(protected dataUtils: DataUtils, protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ classLessionPlan }) => {
      this.classLessionPlan = classLessionPlan;
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }

  previousState(): void {
    window.history.back();
  }
}
