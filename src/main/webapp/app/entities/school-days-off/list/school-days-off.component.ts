import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { ISchoolDaysOff } from '../school-days-off.model';
import { SchoolDaysOffService } from '../service/school-days-off.service';
import { SchoolDaysOffDeleteDialogComponent } from '../delete/school-days-off-delete-dialog.component';

@Component({
  selector: 'jhi-school-days-off',
  templateUrl: './school-days-off.component.html',
})
export class SchoolDaysOffComponent implements OnInit {
  schoolDaysOffs?: ISchoolDaysOff[];
  isLoading = false;

  constructor(protected schoolDaysOffService: SchoolDaysOffService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.schoolDaysOffService.query().subscribe(
      (res: HttpResponse<ISchoolDaysOff[]>) => {
        this.isLoading = false;
        this.schoolDaysOffs = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: ISchoolDaysOff): number {
    return item.id!;
  }

  delete(schoolDaysOff: ISchoolDaysOff): void {
    const modalRef = this.modalService.open(SchoolDaysOffDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.schoolDaysOff = schoolDaysOff;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
