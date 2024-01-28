import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IIdStore } from '../id-store.model';
import { IdStoreService } from '../service/id-store.service';
import { IdStoreDeleteDialogComponent } from '../delete/id-store-delete-dialog.component';

@Component({
  selector: 'jhi-id-store',
  templateUrl: './id-store.component.html',
})
export class IdStoreComponent implements OnInit {
  idStores?: IIdStore[];
  isLoading = false;

  constructor(protected idStoreService: IdStoreService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.idStoreService.query().subscribe(
      (res: HttpResponse<IIdStore[]>) => {
        this.isLoading = false;
        this.idStores = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IIdStore): number {
    return item.id!;
  }

  delete(idStore: IIdStore): void {
    const modalRef = this.modalService.open(IdStoreDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.idStore = idStore;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
