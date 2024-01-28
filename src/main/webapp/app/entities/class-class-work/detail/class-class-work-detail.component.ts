import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IClassClassWork } from '../class-class-work.model';
import { DataUtils } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-class-class-work-detail',
  templateUrl: './class-class-work-detail.component.html',
})
export class ClassClassWorkDetailComponent implements OnInit {
  classClassWork: IClassClassWork | null = null;

  constructor(protected dataUtils: DataUtils, protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ classClassWork }) => {
      this.classClassWork = classClassWork;
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
