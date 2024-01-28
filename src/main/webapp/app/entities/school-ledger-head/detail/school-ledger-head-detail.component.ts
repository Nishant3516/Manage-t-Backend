import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ISchoolLedgerHead } from '../school-ledger-head.model';

@Component({
  selector: 'jhi-school-ledger-head-detail',
  templateUrl: './school-ledger-head-detail.component.html',
})
export class SchoolLedgerHeadDetailComponent implements OnInit {
  schoolLedgerHead: ISchoolLedgerHead | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ schoolLedgerHead }) => {
      this.schoolLedgerHead = schoolLedgerHead;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
