import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IClassFee } from '../class-fee.model';

@Component({
  selector: 'jhi-class-fee-detail',
  templateUrl: './class-fee-detail.component.html',
})
export class ClassFeeDetailComponent implements OnInit {
  classFee: IClassFee | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ classFee }) => {
      this.classFee = classFee;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
