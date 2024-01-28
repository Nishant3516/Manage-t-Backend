import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IIncomeExpenses } from '../income-expenses.model';
import { IncomeExpensesService } from '../service/income-expenses.service';
import { IncomeExpensesDeleteDialogComponent } from '../delete/income-expenses-delete-dialog.component';
import { DATE_FORMAT } from 'app/config/input.constants';

@Component({
  selector: 'jhi-income-expenses',
  templateUrl: './income-expenses.component.html',
})
export class IncomeExpensesComponent {
  incomeExpenses?: IIncomeExpenses[];
  isLoading = false;
startDate:any;
endDate:any;

  constructor(protected incomeExpensesService: IncomeExpensesService, protected modalService: NgbModal) {}

//  loadAll(): void {
//    this.isLoading = true;
//
//    this.incomeExpensesService.query().subscribe({
//      next: (res: HttpResponse<IIncomeExpenses[]>) => {
//        this.isLoading = false;
//        this.incomeExpenses = res.body ?? [];
//      },
//      error: () => {
//        this.isLoading = false;
//      },
//    });
//  }
//
//  ngOnInit(): void {
//    this.loadAll();
//  }

  trackId(index: number, item: IIncomeExpenses): number {
    return item.id!;
  }
fetchIncomeExpenses(): void {
	 this.isLoading = true;
  	this.startDate= this.startDate?.isValid() ? this.startDate.format(DATE_FORMAT) : undefined,
	this.endDate= this.endDate?.isValid() ? this.endDate.format(DATE_FORMAT) : undefined,
    this.incomeExpensesService
      .query({
        'startDate':this.startDate,
        'endDate':this.endDate,
       })
      .subscribe(
        (res: HttpResponse<IIncomeExpenses[]>) => {
          this.isLoading = false;
          this.incomeExpenses = res.body ?? [];
        },
        () => {
          this.isLoading = false;
          alert("Error in fetching data ..");
        }
      );
}
  delete(incomeExpenses: IIncomeExpenses): void {
    const modalRef = this.modalService.open(IncomeExpensesDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
	// this.incomeExpenses = [];
    modalRef.componentInstance.incomeExpenses = incomeExpenses;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        // this.loadAll();
		// this.fetchIncomeExpenses();
		 if (typeof this.incomeExpenses !== 'undefined') {
			
			this.incomeExpenses = this.incomeExpenses.filter(function(value, index, arr){ 
        return value.id !== incomeExpenses.id;
    });
//		for (const incomeExpense of this.incomeExpenses){
//			if (incomeExpense.id === incomeExpenses.id) {
//				this.incomeExpenses.re
//				}
//				}
//			
	}
      }
    });
  }
}
