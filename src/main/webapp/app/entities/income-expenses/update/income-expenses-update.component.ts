import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IIncomeExpenses, IncomeExpenses } from '../income-expenses.model';
import { IncomeExpensesService } from '../service/income-expenses.service';
import { IVendors } from 'app/entities/vendors/vendors.model';
import { VendorsService } from 'app/entities/vendors/service/vendors.service';
import { ISchoolLedgerHead } from 'app/entities/school-ledger-head/school-ledger-head.model';
import { SchoolLedgerHeadService } from 'app/entities/school-ledger-head/service/school-ledger-head.service';
import { ModeOfPayment } from 'app/entities/enumerations/mode-of-payment.model';
import { TransactionType } from 'app/entities/enumerations/transaction-type.model';

@Component({
  selector: 'jhi-income-expenses-update',
  templateUrl: './income-expenses-update.component.html',
})
export class IncomeExpensesUpdateComponent implements OnInit {
  isSaving = false;
  modeOfPaymentValues = Object.keys(ModeOfPayment);
  transactionTypeValues = Object.keys(TransactionType);

  vendorsSharedCollection: IVendors[] = [];
  schoolLedgerHeadsSharedCollection: ISchoolLedgerHead[] = [];

  editForm = this.fb.group({
    id: [],
    amountPaid: [null, [Validators.required]],
    modeOfPay: [],
    noteNumbers: [],
    upiId: [],
    remarks: [],
    paymentDate: [],
    receiptId: [],
    createDate: [],
    lastModified: [],
    cancelDate: [],
    transactionType: [],
    vendor: [],
    ledgerHead: [],
  });

  constructor(
    protected incomeExpensesService: IncomeExpensesService,
    protected vendorsService: VendorsService,
    protected schoolLedgerHeadService: SchoolLedgerHeadService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ incomeExpenses }) => {
      this.updateForm(incomeExpenses);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
	if(this.editForm.get(['vendor'])!.value&&this.editForm.get(['ledgerHead'])!.value){
	    this.isSaving = true;
	    const incomeExpenses = this.createFromForm();
	    if (incomeExpenses.id !== undefined) {
	      this.subscribeToSaveResponse(this.incomeExpensesService.update(incomeExpenses));
	    } else {
	      this.subscribeToSaveResponse(this.incomeExpensesService.create(incomeExpenses));
	    }
	}else{
		alert("Mandatory fields missing");
	}
  }

  trackVendorsById(index: number, item: IVendors): number {
    return item.id!;
  }

  trackSchoolLedgerHeadById(index: number, item: ISchoolLedgerHead): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IIncomeExpenses>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
     this.ngOnInit();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(incomeExpenses: IIncomeExpenses): void {
    this.editForm.patchValue({
      id: incomeExpenses.id,
      amountPaid: incomeExpenses.amountPaid,
      modeOfPay: incomeExpenses.modeOfPay,
      noteNumbers: incomeExpenses.noteNumbers,
      upiId: incomeExpenses.upiId,
      remarks: incomeExpenses.remarks,
      paymentDate: incomeExpenses.paymentDate,
      receiptId: incomeExpenses.receiptId,
      createDate: incomeExpenses.createDate,
      lastModified: incomeExpenses.lastModified,
      cancelDate: incomeExpenses.cancelDate,
      transactionType: incomeExpenses.transactionType,
      vendor: incomeExpenses.vendor,
      ledgerHead: incomeExpenses.ledgerHead,
    });

    this.vendorsSharedCollection = this.vendorsService.addVendorsToCollectionIfMissing(this.vendorsSharedCollection, incomeExpenses.vendor);
    this.schoolLedgerHeadsSharedCollection = this.schoolLedgerHeadService.addSchoolLedgerHeadToCollectionIfMissing(
      this.schoolLedgerHeadsSharedCollection,
      incomeExpenses.ledgerHead
    );
  }

  protected loadRelationshipsOptions(): void {
    this.vendorsService
      .query()
      .pipe(map((res: HttpResponse<IVendors[]>) => res.body ?? []))
      .pipe(map((vendors: IVendors[]) => this.vendorsService.addVendorsToCollectionIfMissing(vendors, this.editForm.get('vendor')!.value)))
      .subscribe((vendors: IVendors[]) => (this.vendorsSharedCollection = vendors));

    this.schoolLedgerHeadService
      .query()
      .pipe(map((res: HttpResponse<ISchoolLedgerHead[]>) => res.body ?? []))
      .pipe(
        map((schoolLedgerHeads: ISchoolLedgerHead[]) =>
          this.schoolLedgerHeadService.addSchoolLedgerHeadToCollectionIfMissing(schoolLedgerHeads, this.editForm.get('ledgerHead')!.value)
        )
      )
      .subscribe((schoolLedgerHeads: ISchoolLedgerHead[]) => (this.schoolLedgerHeadsSharedCollection = schoolLedgerHeads));
  }

  protected createFromForm(): IIncomeExpenses {
    return {
      ...new IncomeExpenses(),
      id: this.editForm.get(['id'])!.value,
      amountPaid: this.editForm.get(['amountPaid'])!.value,
      modeOfPay: this.editForm.get(['modeOfPay'])!.value,
      noteNumbers: this.editForm.get(['noteNumbers'])!.value,
      upiId: this.editForm.get(['upiId'])!.value,
      remarks: this.editForm.get(['remarks'])!.value,
      paymentDate: this.editForm.get(['paymentDate'])!.value,
      receiptId: this.editForm.get(['receiptId'])!.value,
      createDate: this.editForm.get(['createDate'])!.value,
      lastModified: this.editForm.get(['lastModified'])!.value,
      cancelDate: this.editForm.get(['cancelDate'])!.value,
      transactionType: this.editForm.get(['transactionType'])!.value,
      vendor: this.editForm.get(['vendor'])!.value,
      ledgerHead: this.editForm.get(['ledgerHead'])!.value,
    };
  }
}
