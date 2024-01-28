import { Component, OnInit } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { combineLatest } from 'rxjs';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { ISTIncomeExpenses } from '../st-income-expenses.model';

import { ITEMS_PER_PAGE } from 'app/config/pagination.constants';
import { STIncomeExpensesService } from '../service/st-income-expenses.service';
import { STIncomeExpensesDeleteDialogComponent } from '../delete/st-income-expenses-delete-dialog.component';

@Component({
  selector: 'jhi-st-income-expenses',
  templateUrl: './st-income-expenses.component.html',
})
export class STIncomeExpensesComponent implements OnInit {
  sTIncomeExpenses?: ISTIncomeExpenses[];
  isLoading = false;
  totalItems = 0;
  itemsPerPage = ITEMS_PER_PAGE;
  page?: number;
  predicate!: string;
  ascending!: boolean;
  ngbPaginationPage = 1;
	boundFunction:any;
	selectedClassId:any;
	selectedStudentId:any;

  constructor(
    protected sTIncomeExpensesService: STIncomeExpensesService,
    protected activatedRoute: ActivatedRoute,
    protected router: Router,
    protected modalService: NgbModal
  ) {
	
	this.boundFunction=this.loadAllTransportChargesForAStudentCallBack.bind(this);
}

  loadPage(page?: number, dontNavigate?: boolean): void {
    this.isLoading = true;
    const pageToLoad: number = page ?? this.page ?? 1;

    this.sTIncomeExpensesService
      .query({
        page: pageToLoad - 1,
        size: this.itemsPerPage,
        sort: this.sort(),
      })
      .subscribe({
        next: (res: HttpResponse<ISTIncomeExpenses[]>) => {
          this.isLoading = false;
          this.onSuccess(res.body, res.headers, pageToLoad, !dontNavigate);
        },
        error: () => {
          this.isLoading = false;
          this.onError();
        },
      });
  }

  ngOnInit(): void {
    this.handleNavigation();
  }
	loadAllTransportChargesForAStudentCallBack(selectedClassId:any, selectedStudentId:any) : void{
	  	 this.selectedClassId=selectedClassId;
	  	 this.selectedStudentId=selectedStudentId;
	  	 this.loadAllTransportChargesForAStudent();
	
	  }

loadAllTransportChargesForAStudent(): void {
//	alert(this.selectedStudentId);
	this.isLoading = true;
	 this.sTIncomeExpensesService
      .query({
        size: this.itemsPerPage,
        sort: this.sort(),
		'classStudentId.equals':this.selectedStudentId,
      })
      .subscribe({
        next: (res: HttpResponse<ISTIncomeExpenses[]>) => {
          this.isLoading = false;
          this.sTIncomeExpenses=res.body ?? [];
        },
        error: () => {
          this.isLoading = false;
          this.onError();
        },
      });
	
}
  trackId(index: number, item: ISTIncomeExpenses): number {
    return item.id!;
  }

  delete(sTIncomeExpenses: ISTIncomeExpenses): void {
    const modalRef = this.modalService.open(STIncomeExpensesDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.sTIncomeExpenses = sTIncomeExpenses;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadPage();
      }
    });
  }

  protected sort(): string[] {
    const result = [this.predicate + ',' + (this.ascending ? 'ASC' : 'DESC')];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  protected handleNavigation(): void {
    combineLatest([this.activatedRoute.data, this.activatedRoute.queryParamMap]).subscribe(([data, params]) => {
      const page = params.get('page');
      const pageNumber = +(page ?? 1);
      const sort = (params.get('SORT') ?? data['defaultSort']).split(',');
      const predicate = sort[0];
      const ascending = sort[1] === 'ASC';
      if (pageNumber !== this.page || predicate !== this.predicate || ascending !== this.ascending) {
        this.predicate = predicate;
        this.ascending = ascending;
        this.loadPage(pageNumber, true);
      }
    });
  }

  protected onSuccess(data: ISTIncomeExpenses[] | null, headers: HttpHeaders, page: number, navigate: boolean): void {
    this.totalItems = Number(headers.get('X-Total-Count'));
    this.page = page;
    if (navigate) {
      this.router.navigate(['/st-income-expenses'], {
        queryParams: {
          page: this.page,
          size: this.itemsPerPage,
          sort: this.predicate + ',' + (this.ascending ? 'ASC' : 'DESC'),
        },
      });
    }
    this.sTIncomeExpenses = data ?? [];
    this.ngbPaginationPage = this.page;
  }

  protected onError(): void {
    this.ngbPaginationPage = this.page ?? 1;
  }
}
