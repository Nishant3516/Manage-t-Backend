import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { ISchoolEvent } from '../school-event.model';
import { SchoolEventService } from '../service/school-event.service';
import { SchoolEventDeleteDialogComponent } from '../delete/school-event-delete-dialog.component';

@Component({
  selector: 'jhi-school-event',
  templateUrl: './school-event.component.html',
})
export class SchoolEventComponent implements OnInit {
  schoolEvents?: ISchoolEvent[];
  isLoading = false;

  constructor(protected schoolEventService: SchoolEventService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.schoolEventService.query().subscribe(
      (res: HttpResponse<ISchoolEvent[]>) => {
        this.isLoading = false;
        this.schoolEvents = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: ISchoolEvent): number {
    return item.id!;
  }

  delete(schoolEvent: ISchoolEvent): void {
    const modalRef = this.modalService.open(SchoolEventDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.schoolEvent = schoolEvent;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
