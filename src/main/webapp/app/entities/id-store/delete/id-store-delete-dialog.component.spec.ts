jest.mock('@ng-bootstrap/ng-bootstrap');

import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IdStoreService } from '../service/id-store.service';

import { IdStoreDeleteDialogComponent } from './id-store-delete-dialog.component';

describe('Component Tests', () => {
  describe('IdStore Management Delete Component', () => {
    let comp: IdStoreDeleteDialogComponent;
    let fixture: ComponentFixture<IdStoreDeleteDialogComponent>;
    let service: IdStoreService;
    let mockActiveModal: NgbActiveModal;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [IdStoreDeleteDialogComponent],
        providers: [NgbActiveModal],
      })
        .overrideTemplate(IdStoreDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(IdStoreDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(IdStoreService);
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
