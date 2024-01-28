import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ISTIncomeExpenses } from '../st-income-expenses.model';

@Component({
  selector: 'jhi-st-income-expenses-detail',
  templateUrl: './st-income-expenses-detail.component.html',
})
export class STIncomeExpensesDetailComponent implements OnInit {
  sTIncomeExpenses: ISTIncomeExpenses | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ sTIncomeExpenses }) => {
      this.sTIncomeExpenses = sTIncomeExpenses;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
