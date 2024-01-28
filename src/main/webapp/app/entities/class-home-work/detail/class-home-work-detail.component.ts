import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IClassHomeWork } from '../class-home-work.model';
import { DataUtils } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-class-home-work-detail',
  templateUrl: './class-home-work-detail.component.html',
})
export class ClassHomeWorkDetailComponent implements OnInit {
  classHomeWork: IClassHomeWork | null = null;

  constructor(protected dataUtils: DataUtils, protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ classHomeWork }) => {
      this.classHomeWork = classHomeWork;
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
