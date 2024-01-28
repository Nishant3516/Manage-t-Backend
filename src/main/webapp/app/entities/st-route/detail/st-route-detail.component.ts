import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ISTRoute } from '../st-route.model';

@Component({
  selector: 'jhi-st-route-detail',
  templateUrl: './st-route-detail.component.html',
})
export class STRouteDetailComponent implements OnInit {
  sTRoute: ISTRoute | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ sTRoute }) => {
      this.sTRoute = sTRoute;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
