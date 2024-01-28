jest.mock('@ng-bootstrap/ng-bootstrap');

import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { SchoolNotificationsService } from '../service/school-notifications.service';

import { SchoolNotificationsDeleteDialogComponent } from './school-notifications-delete-dialog.component';

describe('Component Tests', () => {
  describe('SchoolNotifications Management Delete Component', () => {
    let comp: SchoolNotificationsDeleteDialogComponent;
    let fixture: ComponentFixture<SchoolNotificationsDeleteDialogComponent>;
    let service: SchoolNotificationsService;
    let mockActiveModal: NgbActiveModal;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [SchoolNotificationsDeleteDialogComponent],
        providers: [NgbActiveModal],
      })
        .overrideTemplate(SchoolNotificationsDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(SchoolNotificationsDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(SchoolNotificationsService);
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
