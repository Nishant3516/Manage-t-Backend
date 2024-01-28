import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { ISTRoute, STRoute } from '../st-route.model';
import { STRouteService } from '../service/st-route.service';

@Component({
  selector: 'jhi-st-route-update',
  templateUrl: './st-route-update.component.html',
})
export class STRouteUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    transportRouteName: [null, [Validators.required]],
    routeCharge: [null, [Validators.required]],
    transportRouteAddress: [],
    contactNumber: [],
    createDate: [],
    cancelDate: [],
    remarks: [],
  });

  constructor(protected sTRouteService: STRouteService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ sTRoute }) => {
      this.updateForm(sTRoute);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const sTRoute = this.createFromForm();
    if (sTRoute.id !== undefined) {
      this.subscribeToSaveResponse(this.sTRouteService.update(sTRoute));
    } else {
      this.subscribeToSaveResponse(this.sTRouteService.create(sTRoute));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ISTRoute>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(sTRoute: ISTRoute): void {
    this.editForm.patchValue({
      id: sTRoute.id,
      transportRouteName: sTRoute.transportRouteName,
      routeCharge: sTRoute.routeCharge,
      transportRouteAddress: sTRoute.transportRouteAddress,
      contactNumber: sTRoute.contactNumber,
      createDate: sTRoute.createDate,
      cancelDate: sTRoute.cancelDate,
      remarks: sTRoute.remarks,
    });
  }

  protected createFromForm(): ISTRoute {
    return {
      ...new STRoute(),
      id: this.editForm.get(['id'])!.value,
      transportRouteName: this.editForm.get(['transportRouteName'])!.value,
      routeCharge: this.editForm.get(['routeCharge'])!.value,
      transportRouteAddress: this.editForm.get(['transportRouteAddress'])!.value,
      contactNumber: this.editForm.get(['contactNumber'])!.value,
      createDate: this.editForm.get(['createDate'])!.value,
      cancelDate: this.editForm.get(['cancelDate'])!.value,
      remarks: this.editForm.get(['remarks'])!.value,
    };
  }
}
