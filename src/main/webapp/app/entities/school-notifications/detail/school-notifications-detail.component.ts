import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ISchoolNotifications } from '../school-notifications.model';
import { DataUtils } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-school-notifications-detail',
  templateUrl: './school-notifications-detail.component.html',
})
export class SchoolNotificationsDetailComponent implements OnInit {
  schoolNotifications: ISchoolNotifications | null = null;

  constructor(protected dataUtils: DataUtils, protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ schoolNotifications }) => {
      this.schoolNotifications = schoolNotifications;
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
