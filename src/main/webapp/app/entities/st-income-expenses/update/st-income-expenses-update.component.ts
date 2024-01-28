import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { ISTIncomeExpenses, STIncomeExpenses } from '../st-income-expenses.model';
import { STIncomeExpensesService } from '../service/st-income-expenses.service';
import { IClassStudent } from 'app/entities/class-student/class-student.model';
import { ClassStudentService } from 'app/entities/class-student/service/class-student.service';
import { ISTRoute } from 'app/entities/st-route/st-route.model';
import { STRouteService } from 'app/entities/st-route/service/st-route.service';
import { IVendors } from 'app/entities/vendors/vendors.model';
import { VendorsService } from 'app/entities/vendors/service/vendors.service';
import { ModeOfPayment } from 'app/entities/enumerations/mode-of-payment.model';
import { TransactionType } from 'app/entities/enumerations/transaction-type.model';

@Component({
  selector: 'jhi-st-income-expenses-update',
  templateUrl: './st-income-expenses-update.component.html',
})
export class STIncomeExpensesUpdateComponent implements OnInit {
  isSaving = false;
  modeOfPaymentValues = Object.keys(ModeOfPayment);
  transactionTypeValues = Object.keys(TransactionType);
  isLoading = false;

  classStudentsSharedCollection: IClassStudent[] = [];
  sTRoutesSharedCollection: ISTRoute[] = [];
  vendorsSharedCollection: IVendors[] = [];
selectedClassId:any;
// selectedStudentId:any;
// selectedRouteAmount:any;

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
    classStudent: [],
    stRoute: [],
    operatedBy: [],
  });

  constructor(
    protected sTIncomeExpensesService: STIncomeExpensesService,
    protected classStudentService: ClassStudentService,
    protected sTRouteService: STRouteService,
    protected vendorsService: VendorsService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ sTIncomeExpenses }) => {
      this.updateForm(sTIncomeExpenses);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
	if(this.selectedClassId&&this.editForm.get(['classStudent'])!.value){
	    this.isSaving = true;
	    const sTIncomeExpenses = this.createFromForm();
	    if (sTIncomeExpenses.id !== undefined) {
	      this.subscribeToSaveResponse(this.sTIncomeExpensesService.update(sTIncomeExpenses));
	    } else {
	      this.subscribeToSaveResponse(this.sTIncomeExpensesService.create(sTIncomeExpenses));
	    }
	}else{
		// alert(this.selectedClassId);
		// alert(this.editForm.get(['classStudent'])!.value);
		alert("Select mandatory fields");
	}
  }
routeChanged():void{
	// this.selectedRouteAmount=this.editForm.get(['stRoute'])!.value.routeCharge;
	// this.editForm.get(['amountPaid']).value=this.editForm.get(['stRoute'])!.value.routeCharge;
	// alert(this.selectedRouteAmount);
	this.editForm.patchValue({
      amountPaid: this.editForm.get(['stRoute'])!.value.routeCharge,
    })
	}
  trackClassStudentById(index: number, item: IClassStudent): number {
    return item.id!;
  }

  trackSTRouteById(index: number, item: ISTRoute): number {
    return item.id!;
  }

  trackVendorsById(index: number, item: IVendors): number {
    return item.id!;
  }
getClassIdCallBack = (selectedClassId: any): void => {
	  // callback code here
	   this.selectedClassId=selectedClassId;
this.loadAllStudentsForAClass();
	   
  	}
// getStudentIdCallBack = (selectedStudentId: any): void => {
//	  // callback code here
//	  this.selectedStudentId=selectedStudentId;
//	  // load all the tacking for given date range, class and the subject .. no need to go for the chapter
// 	 }

loadAllStudentsForAClass(): void {
    this.isLoading = true;

    this.classStudentService.query({ 'schoolClassId.equals': this.selectedClassId, size: 60 }).subscribe(
      (res: HttpResponse<IClassStudent[]>) => {
        this.isLoading = false;
        this.classStudentsSharedCollection = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }
  protected subscribeToSaveResponse(result: Observable<HttpResponse<ISTIncomeExpenses>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
   // this.previousState();
this.ngOnInit();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(sTIncomeExpenses: ISTIncomeExpenses): void {
    this.editForm.patchValue({
      id: sTIncomeExpenses.id,
      amountPaid: sTIncomeExpenses.amountPaid,
      modeOfPay: sTIncomeExpenses.modeOfPay,
      noteNumbers: sTIncomeExpenses.noteNumbers,
      upiId: sTIncomeExpenses.upiId,
      remarks: sTIncomeExpenses.remarks,
      paymentDate: sTIncomeExpenses.paymentDate,
      receiptId: sTIncomeExpenses.receiptId,
      createDate: sTIncomeExpenses.createDate,
      lastModified: sTIncomeExpenses.lastModified,
      cancelDate: sTIncomeExpenses.cancelDate,
      transactionType: sTIncomeExpenses.transactionType,
      classStudent: sTIncomeExpenses.classStudent,
      stRoute: sTIncomeExpenses.stRoute,
      operatedBy: sTIncomeExpenses.operatedBy,
    });

//    this.classStudentsSharedCollection = this.classStudentService.addClassStudentToCollectionIfMissing(
//      this.classStudentsSharedCollection,
//      sTIncomeExpenses.classStudent
//    );
    this.sTRoutesSharedCollection = this.sTRouteService.addSTRouteToCollectionIfMissing(
      this.sTRoutesSharedCollection,
      sTIncomeExpenses.stRoute
    );
//    this.vendorsSharedCollection = this.vendorsService.addVendorsToCollectionIfMissing(
//      this.vendorsSharedCollection,
//      sTIncomeExpenses.operatedBy
//    );
  }

  protected loadRelationshipsOptions(): void {
//    this.classStudentService
//      .query()
//      .pipe(map((res: HttpResponse<IClassStudent[]>) => res.body ?? []))
//      .pipe(
//        map((classStudents: IClassStudent[]) =>
//          this.classStudentService.addClassStudentToCollectionIfMissing(classStudents, this.editForm.get('classStudent')!.value)
//        )
//      )
//      .subscribe((classStudents: IClassStudent[]) => (this.classStudentsSharedCollection = classStudents));

    this.sTRouteService
      .query()
      .pipe(map((res: HttpResponse<ISTRoute[]>) => res.body ?? []))
      .pipe(
        map((sTRoutes: ISTRoute[]) => this.sTRouteService.addSTRouteToCollectionIfMissing(sTRoutes, this.editForm.get('stRoute')!.value))
      )
      .subscribe((sTRoutes: ISTRoute[]) => (this.sTRoutesSharedCollection = sTRoutes));

//    this.vendorsService
//      .query()
//      .pipe(map((res: HttpResponse<IVendors[]>) => res.body ?? []))
//      .pipe(
//        map((vendors: IVendors[]) => this.vendorsService.addVendorsToCollectionIfMissing(vendors, this.editForm.get('operatedBy')!.value))
//      )
//      .subscribe((vendors: IVendors[]) => (this.vendorsSharedCollection = vendors));
  }

  protected createFromForm(): ISTIncomeExpenses {
    return {
      ...new STIncomeExpenses(),
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
      classStudent: this.editForm.get(['classStudent'])!.value,
      stRoute: this.editForm.get(['stRoute'])!.value,
      operatedBy: this.editForm.get(['operatedBy'])!.value,
    };
  }
}
