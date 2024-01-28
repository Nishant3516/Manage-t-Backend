import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IVendors } from '../vendors.model';
import { VendorsService } from '../service/vendors.service';
import { VendorsDeleteDialogComponent } from '../delete/vendors-delete-dialog.component';
import { DataUtils } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-vendors',
  templateUrl: './vendors.component.html',
})
export class VendorsComponent implements OnInit {
  vendors?: IVendors[];
  isLoading = false;

  constructor(protected vendorsService: VendorsService, protected dataUtils: DataUtils, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.vendorsService.query().subscribe({
      next: (res: HttpResponse<IVendors[]>) => {
        this.isLoading = false;
        this.vendors = res.body ?? [];
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IVendors): number {
    return item.id!;
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    return this.dataUtils.openFile(base64String, contentType);
  }

  delete(vendors: IVendors): void {
    const modalRef = this.modalService.open(VendorsDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.vendors = vendors;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
