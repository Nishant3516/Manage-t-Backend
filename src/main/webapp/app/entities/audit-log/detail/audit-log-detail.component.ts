import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IAuditLog } from '../audit-log.model';

@Component({
  selector: 'jhi-audit-log-detail',
  templateUrl: './audit-log-detail.component.html',
})
export class AuditLogDetailComponent implements OnInit {
  auditLog: IAuditLog | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ auditLog }) => {
      this.auditLog = auditLog;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
