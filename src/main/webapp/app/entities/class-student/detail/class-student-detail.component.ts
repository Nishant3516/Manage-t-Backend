import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IClassStudent } from '../class-student.model';
import { DataUtils } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-class-student-detail',
  templateUrl: './class-student-detail.component.html',
})
export class ClassStudentDetailComponent implements OnInit {
  classStudent: IClassStudent | null = null;

  constructor(protected dataUtils: DataUtils, protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ classStudent }) => {
      this.classStudent = classStudent;
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
