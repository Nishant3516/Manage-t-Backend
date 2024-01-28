import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { ISchoolLedgerHead } from '../school-ledger-head.model';
import { SchoolLedgerHeadService } from '../service/school-ledger-head.service';
import { SchoolLedgerHeadDeleteDialogComponent } from '../delete/school-ledger-head-delete-dialog.component';

@Component({
  selector: 'jhi-school-ledger-head',
  templateUrl: './school-ledger-head.component.html',
})
export class SchoolLedgerHeadComponent implements OnInit {
  schoolLedgerHeads?: ISchoolLedgerHead[];
  isLoading = false;

  constructor(protected schoolLedgerHeadService: SchoolLedgerHeadService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.schoolLedgerHeadService.query().subscribe(
      (res: HttpResponse<ISchoolLedgerHead[]>) => {
        this.isLoading = false;
        this.schoolLedgerHeads = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: ISchoolLedgerHead): number {
    return item.id!;
  }

  delete(schoolLedgerHead: ISchoolLedgerHead): void {
    const modalRef = this.modalService.open(SchoolLedgerHeadDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.schoolLedgerHead = schoolLedgerHead;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
