import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IClassFee } from '../class-fee.model';
import { ClassFeeService } from '../service/class-fee.service';
import { ClassFeeDeleteDialogComponent } from '../delete/class-fee-delete-dialog.component';
import { SchoolClassFilter } from '../../../shared/filters/class.filter';
import { SchoolClassService } from 'app/entities/school-class/service/school-class.service';
import { IClassStudent } from 'app/entities/class-student/class-student.model';

@Component({
  selector: 'jhi-class-fee',
  templateUrl: './class-fee.component.html',
})
export class ClassFeeComponent extends SchoolClassFilter implements OnInit {
  classFees?: IClassFee[];
  isLoading = false;

  constructor(
    protected classFeeService: ClassFeeService,
    protected schoolClassService: SchoolClassService,
    protected modalService: NgbModal
  ) {
    super(schoolClassService);
  }

  loadAll(): void {
    this.isLoading = true;

    this.classFeeService.query().subscribe(
      (res: HttpResponse<IClassFee[]>) => {
        this.isLoading = false;
        this.classFees = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }
  loadAllStudentsForAClass(): void {
    this.isLoading = true;

    this.classFeeService.query({ 'schoolClassId.equals': this.selectedClassId }).subscribe(
      (res: HttpResponse<IClassFee[]>) => {
        this.isLoading = false;
        this.classFees = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }
  ngOnInit(): void {
    this.loadAllClasses();
    this.loadAll();
  }

  trackId(index: number, item: IClassFee): number {
    return item.id!;
  }

  delete(classFee: IClassFee): void {
    const modalRef = this.modalService.open(ClassFeeDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.classFee = classFee;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
