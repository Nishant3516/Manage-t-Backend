import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IIncomeExpenses } from '../income-expenses.model';

@Component({
  selector: 'jhi-income-expenses-detail',
  templateUrl: './income-expenses-detail.component.html',
})
export class IncomeExpensesDetailComponent implements OnInit {
  incomeExpenses: IIncomeExpenses | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ incomeExpenses }) => {
      this.incomeExpenses = incomeExpenses;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
