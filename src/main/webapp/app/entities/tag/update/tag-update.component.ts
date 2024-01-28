import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { ITag, Tag } from '../tag.model';
import { TagService } from '../service/tag.service';
import { ITenant } from 'app/entities/tenant/tenant.model';
import { TenantService } from 'app/entities/tenant/service/tenant.service';
import { TagLevel } from 'app/entities/enumerations/tag-level.model';

@Component({
  selector: 'jhi-tag-update',
  templateUrl: './tag-update.component.html',
})
export class TagUpdateComponent implements OnInit {
  isSaving = false;
  tagLevelValues = Object.keys(TagLevel);

  tenantsSharedCollection: ITenant[] = [];

  editForm = this.fb.group({
    id: [],
    tagText: [],
    tagLevel: [],
    createDate: [],
    lastModified: [],
    cancelDate: [],
    tenant: [],
  });

  constructor(
    protected tagService: TagService,
    protected tenantService: TenantService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ tag }) => {
      this.updateForm(tag);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const tag = this.createFromForm();
    if (tag.id !== undefined) {
      this.subscribeToSaveResponse(this.tagService.update(tag));
    } else {
      this.subscribeToSaveResponse(this.tagService.create(tag));
    }
  }

  trackTenantById(index: number, item: ITenant): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITag>>): void {
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

  protected updateForm(tag: ITag): void {
    this.editForm.patchValue({
      id: tag.id,
      tagText: tag.tagText,
      tagLevel: tag.tagLevel,
      createDate: tag.createDate,
      lastModified: tag.lastModified,
      cancelDate: tag.cancelDate,
      tenant: tag.tenant,
    });

    this.tenantsSharedCollection = this.tenantService.addTenantToCollectionIfMissing(this.tenantsSharedCollection, tag.tenant);
  }

  protected loadRelationshipsOptions(): void {
    this.tenantService
      .query()
      .pipe(map((res: HttpResponse<ITenant[]>) => res.body ?? []))
      .pipe(map((tenants: ITenant[]) => this.tenantService.addTenantToCollectionIfMissing(tenants, this.editForm.get('tenant')!.value)))
      .subscribe((tenants: ITenant[]) => (this.tenantsSharedCollection = tenants));
  }

  protected createFromForm(): ITag {
    return {
      ...new Tag(),
      id: this.editForm.get(['id'])!.value,
      tagText: this.editForm.get(['tagText'])!.value,
      tagLevel: this.editForm.get(['tagLevel'])!.value,
      createDate: this.editForm.get(['createDate'])!.value,
      lastModified: this.editForm.get(['lastModified'])!.value,
      cancelDate: this.editForm.get(['cancelDate'])!.value,
      tenant: this.editForm.get(['tenant'])!.value,
    };
  }
}
