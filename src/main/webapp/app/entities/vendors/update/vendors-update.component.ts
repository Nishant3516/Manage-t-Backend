import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import * as dayjs from 'dayjs';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IVendors, Vendors } from '../vendors.model';
import { VendorsService } from '../service/vendors.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { VendorType } from 'app/entities/enumerations/vendor-type.model';

@Component({
  selector: 'jhi-vendors-update',
  templateUrl: './vendors-update.component.html',
})
export class VendorsUpdateComponent implements OnInit {
  isSaving = false;
  vendorTypeValues = Object.keys(VendorType);

  editForm = this.fb.group({
    id: [],
    vendorPhoto: [],
    vendorPhotoContentType: [],
    vendorPhotoLink: [],
    vendorId: [],
    vendorName: [null, [Validators.required]],
    phoneNumber: [],
    dateOfBirth: [],
    addressLine1: [],
    addressLine2: [],
    nickName: [],
    email: [],
    createDate: [],
    cancelDate: [],
    vendorType: [],
  });

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected vendorsService: VendorsService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ vendors }) => {
      if (vendors.id === undefined) {
        const today = dayjs().startOf('day');
        vendors.dateOfBirth = today;
      }

      this.updateForm(vendors);
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
    const vendors = this.createFromForm();
    if (vendors.id !== undefined) {
      this.subscribeToSaveResponse(this.vendorsService.update(vendors));
    } else {
      this.subscribeToSaveResponse(this.vendorsService.create(vendors));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IVendors>>): void {
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

  protected updateForm(vendors: IVendors): void {
    this.editForm.patchValue({
      id: vendors.id,
      vendorPhoto: vendors.vendorPhoto,
      vendorPhotoContentType: vendors.vendorPhotoContentType,
      vendorPhotoLink: vendors.vendorPhotoLink,
      vendorId: vendors.vendorId,
      vendorName: vendors.vendorName,
      phoneNumber: vendors.phoneNumber,
      dateOfBirth: vendors.dateOfBirth ? vendors.dateOfBirth.format(DATE_TIME_FORMAT) : null,
      addressLine1: vendors.addressLine1,
      addressLine2: vendors.addressLine2,
      nickName: vendors.nickName,
      email: vendors.email,
      createDate: vendors.createDate,
      cancelDate: vendors.cancelDate,
      vendorType: vendors.vendorType,
    });
  }

  protected createFromForm(): IVendors {
    return {
      ...new Vendors(),
      id: this.editForm.get(['id'])!.value,
      vendorPhotoContentType: this.editForm.get(['vendorPhotoContentType'])!.value,
      vendorPhoto: this.editForm.get(['vendorPhoto'])!.value,
      vendorPhotoLink: this.editForm.get(['vendorPhotoLink'])!.value,
      vendorId: this.editForm.get(['vendorId'])!.value,
      vendorName: this.editForm.get(['vendorName'])!.value,
      phoneNumber: this.editForm.get(['phoneNumber'])!.value,
      dateOfBirth: this.editForm.get(['dateOfBirth'])!.value
        ? dayjs(this.editForm.get(['dateOfBirth'])!.value, DATE_TIME_FORMAT)
        : undefined,
      addressLine1: this.editForm.get(['addressLine1'])!.value,
      addressLine2: this.editForm.get(['addressLine2'])!.value,
      nickName: this.editForm.get(['nickName'])!.value,
      email: this.editForm.get(['email'])!.value,
      createDate: this.editForm.get(['createDate'])!.value,
      cancelDate: this.editForm.get(['cancelDate'])!.value,
      vendorType: this.editForm.get(['vendorType'])!.value,
    };
  }
}
