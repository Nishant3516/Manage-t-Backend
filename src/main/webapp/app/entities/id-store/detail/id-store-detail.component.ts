import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IIdStore } from '../id-store.model';

@Component({
  selector: 'jhi-id-store-detail',
  templateUrl: './id-store-detail.component.html',
})
export class IdStoreDetailComponent implements OnInit {
  idStore: IIdStore | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ idStore }) => {
      this.idStore = idStore;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
