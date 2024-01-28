jest.mock('@ng-bootstrap/ng-bootstrap');

import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { StudentAdditionalChargesService } from '../service/student-additional-charges.service';

import { StudentAdditionalChargesDeleteDialogComponent } from './student-additional-charges-delete-dialog.component';

describe('Component Tests', () => {
  describe('StudentAdditionalCharges Management Delete Component', () => {
    let comp: StudentAdditionalChargesDeleteDialogComponent;
    let fixture: ComponentFixture<StudentAdditionalChargesDeleteDialogComponent>;
    let service: StudentAdditionalChargesService;
    let mockActiveModal: NgbActiveModal;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [StudentAdditionalChargesDeleteDialogComponent],
        providers: [NgbActiveModal],
      })
        .overrideTemplate(StudentAdditionalChargesDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(StudentAdditionalChargesDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(StudentAdditionalChargesService);
      mockActiveModal = TestBed.inject(NgbActiveModal);
    });

    describe('confirmDelete', () => {
      it('Should call delete service on confirmDelete', inject(
        [],
        fakeAsync(() => {
          // GIVEN
          spyOn(service, 'delete').and.returnValue(of({}));

          // WHEN
          comp.confirmDelete(123);
          tick();

          // THEN
          expect(service.delete).toHaveBeenCalledWith(123);
          expect(mockActiveModal.close).toHaveBeenCalledWith('deleted');
        })
      ));

      it('Should not call delete service on clear', () => {
        // GIVEN
        spyOn(service, 'delete');

        // WHEN
        comp.cancel();

        // THEN
        expect(service.delete).not.toHaveBeenCalled();
        expect(mockActiveModal.close).not.toHaveBeenCalled();
        expect(mockActiveModal.dismiss).toHaveBeenCalled();
      });
    });
  });
});
