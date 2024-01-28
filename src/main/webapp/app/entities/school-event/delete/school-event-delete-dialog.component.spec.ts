jest.mock('@ng-bootstrap/ng-bootstrap');

import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { SchoolEventService } from '../service/school-event.service';

import { SchoolEventDeleteDialogComponent } from './school-event-delete-dialog.component';

describe('Component Tests', () => {
  describe('SchoolEvent Management Delete Component', () => {
    let comp: SchoolEventDeleteDialogComponent;
    let fixture: ComponentFixture<SchoolEventDeleteDialogComponent>;
    let service: SchoolEventService;
    let mockActiveModal: NgbActiveModal;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [SchoolEventDeleteDialogComponent],
        providers: [NgbActiveModal],
      })
        .overrideTemplate(SchoolEventDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(SchoolEventDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(SchoolEventService);
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
