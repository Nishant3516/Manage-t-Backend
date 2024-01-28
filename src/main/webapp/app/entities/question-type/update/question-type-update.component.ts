import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IQuestionType, QuestionType } from '../question-type.model';
import { QuestionTypeService } from '../service/question-type.service';
import { ITenant } from 'app/entities/tenant/tenant.model';
import { TenantService } from 'app/entities/tenant/service/tenant.service';

@Component({
  selector: 'jhi-question-type-update',
  templateUrl: './question-type-update.component.html',
})
export class QuestionTypeUpdateComponent implements OnInit {
  isSaving = false;

  tenantsSharedCollection: ITenant[] = [];

  editForm = this.fb.group({
    id: [],
    questionType: [],
    marks: [],
    createDate: [],
    lastModified: [],
    cancelDate: [],
    tenant: [],
  });

  constructor(
    protected questionTypeService: QuestionTypeService,
    protected tenantService: TenantService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ questionType }) => {
      this.updateForm(questionType);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const questionType = this.createFromForm();
    if (questionType.id !== undefined) {
      this.subscribeToSaveResponse(this.questionTypeService.update(questionType));
    } else {
      this.subscribeToSaveResponse(this.questionTypeService.create(questionType));
    }
  }

  trackTenantById(index: number, item: ITenant): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IQuestionType>>): void {
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

  protected updateForm(questionType: IQuestionType): void {
    this.editForm.patchValue({
      id: questionType.id,
      questionType: questionType.questionType,
      marks: questionType.marks,
      createDate: questionType.createDate,
      lastModified: questionType.lastModified,
      cancelDate: questionType.cancelDate,
      tenant: questionType.tenant,
    });

    this.tenantsSharedCollection = this.tenantService.addTenantToCollectionIfMissing(this.tenantsSharedCollection, questionType.tenant);
  }

  protected loadRelationshipsOptions(): void {
    this.tenantService
      .query()
      .pipe(map((res: HttpResponse<ITenant[]>) => res.body ?? []))
      .pipe(map((tenants: ITenant[]) => this.tenantService.addTenantToCollectionIfMissing(tenants, this.editForm.get('tenant')!.value)))
      .subscribe((tenants: ITenant[]) => (this.tenantsSharedCollection = tenants));
  }

  protected createFromForm(): IQuestionType {
    return {
      ...new QuestionType(),
      id: this.editForm.get(['id'])!.value,
      questionType: this.editForm.get(['questionType'])!.value,
      marks: this.editForm.get(['marks'])!.value,
      createDate: this.editForm.get(['createDate'])!.value,
      lastModified: this.editForm.get(['lastModified'])!.value,
      cancelDate: this.editForm.get(['cancelDate'])!.value,
      tenant: this.editForm.get(['tenant'])!.value,
    };
  }
}
