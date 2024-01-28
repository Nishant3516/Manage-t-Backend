import { Component, OnInit } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { combineLatest } from 'rxjs';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { ISchoolNotifications } from '../entities/school-notifications/school-notifications.model';

import { ITEMS_PER_PAGE } from 'app/config/pagination.constants';
import { SchoolNotificationsService } from '../entities/school-notifications/service/school-notifications.service';
import { DataUtils } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-downloads',
  templateUrl: './downloads.component.html',
 styleUrls: ['./downloads.component.scss'],
})
export class DownLoadsComponent implements OnInit {
  schoolNotifications?: ISchoolNotifications[];
  isLoading = false;
  totalItems = 0;
  itemsPerPage = ITEMS_PER_PAGE;
  page?: number;
  predicate!: string;
  ascending!: boolean;
  ngbPaginationPage = 1;

  constructor(
    protected schoolNotificationsService: SchoolNotificationsService,
    protected activatedRoute: ActivatedRoute,
    protected dataUtils: DataUtils,
    protected router: Router,
    protected modalService: NgbModal
  ) {}

  loadPage(page?: number, dontNavigate?: boolean): void {
    this.isLoading = true;
    const pageToLoad: number = page ?? this.page ?? 1;
	// alert("loadPage");
    this.schoolNotificationsService
      .queryPublic({
        page: pageToLoad - 1,
        size: this.itemsPerPage,
        sort: this.sort(),
      })
      .subscribe(
        (res: HttpResponse<ISchoolNotifications[]>) => {
          this.isLoading = false;
          this.onSuccess(res.body, res.headers, pageToLoad, !dontNavigate);
        },
        () => {
          this.isLoading = false;
          this.onError();
        }
      );
  }

  ngOnInit(): void {
	// alert("Download oninit");
    // this.handleNavigation();
 this.loadPage(1, true);
  }

  trackId(index: number, item: ISchoolNotifications): number {
    return item.id!;
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    return this.dataUtils.openFile(base64String, contentType);
  }

 
  protected sort(): string[] {
    const result = [this.predicate + ',' + (this.ascending ? 'asc' : 'desc')];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  protected handleNavigation(): void {
    combineLatest([this.activatedRoute.data, this.activatedRoute.queryParamMap]).subscribe(([data, params]) => {
      const page = params.get('page');
      const pageNumber = page !== null ? +page : 1;
      const sort = (params.get('sort') ?? data['defaultSort']).split(',');
      const predicate = sort[0];
      const ascending = sort[1] === 'asc';
      if (pageNumber !== this.page || predicate !== this.predicate || ascending !== this.ascending) {
        this.predicate = predicate;
        this.ascending = ascending;
        this.loadPage(pageNumber, true);
      }
    });
  }

  protected onSuccess(data: ISchoolNotifications[] | null, headers: HttpHeaders, page: number, navigate: boolean): void {
// alert("Success") ;  
 this.totalItems = Number(headers.get('X-Total-Count'));
    this.page = page;
    if (navigate) {
      this.router.navigate(['/school-notifications'], {
        queryParams: {
          page: this.page,
          size: this.itemsPerPage,
          sort: this.predicate + ',' + (this.ascending ? 'asc' : 'desc'),
        },
      });
    }
    this.schoolNotifications = data ?? [];
    this.ngbPaginationPage = this.page;
  }

  protected onError(): void {
	alert("failed");
    this.ngbPaginationPage = this.page ?? 1;
  }
}
