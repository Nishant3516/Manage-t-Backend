import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { ITenant, Tenant } from '../tenant.model';
import { TenantService } from '../service/tenant.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-tenant-update',
  templateUrl: './tenant-update.component.html',
})
export class TenantUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    tenantName: [],
    tenantLogo: [],
    tenantLogoContentType: [],
    createDate: [],
    lastModified: [],
    cancelDate: [],
  });

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected tenantService: TenantService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ tenant }) => {
      this.updateForm(tenant);
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }

  setFileData(event: Event, field: string, isImage: boolean): void {
    this.dataUtils.loadFileToForm(event, this.editForm, field, isImage).subscribe({
      error: (err: FileLoadError) =>
        this.eventManager.broadcast(new EventWithContent<AlertError>('manageitApp.error', { ...err, key: 'error.file.' + err.key })),
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const tenant = this.createFromForm();
    if (tenant.id !== undefined) {
      this.subscribeToSaveResponse(this.tenantService.update(tenant));
    } else {
      this.subscribeToSaveResponse(this.tenantService.create(tenant));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITenant>>): void {
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

  protected updateForm(tenant: ITenant): void {
    this.editForm.patchValue({
      id: tenant.id,
      tenantName: tenant.tenantName,
      tenantLogo: tenant.tenantLogo,
      tenantLogoContentType: tenant.tenantLogoContentType,
      createDate: tenant.createDate,
      lastModified: tenant.lastModified,
      cancelDate: tenant.cancelDate,
    });
  }

  protected createFromForm(): ITenant {
    return {
      ...new Tenant(),
      id: this.editForm.get(['id'])!.value,
      tenantName: this.editForm.get(['tenantName'])!.value,
      tenantLogoContentType: this.editForm.get(['tenantLogoContentType'])!.value,
      tenantLogo: this.editForm.get(['tenantLogo'])!.value,
      createDate: this.editForm.get(['createDate'])!.value,
      lastModified: this.editForm.get(['lastModified'])!.value,
      cancelDate: this.editForm.get(['cancelDate'])!.value,
    };
  }
}
