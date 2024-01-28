jest.mock('@ng-bootstrap/ng-bootstrap');

import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { SchoolDaysOffService } from '../service/school-days-off.service';

import { SchoolDaysOffDeleteDialogComponent } from './school-days-off-delete-dialog.component';

describe('Component Tests', () => {
  describe('SchoolDaysOff Management Delete Component', () => {
    let comp: SchoolDaysOffDeleteDialogComponent;
    let fixture: ComponentFixture<SchoolDaysOffDeleteDialogComponent>;
    let service: SchoolDaysOffService;
    let mockActiveModal: NgbActiveModal;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [SchoolDaysOffDeleteDialogComponent],
        providers: [NgbActiveModal],
      })
        .overrideTemplate(SchoolDaysOffDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(SchoolDaysOffDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(SchoolDaysOffService);
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
