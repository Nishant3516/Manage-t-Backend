import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { IdStoreDetailComponent } from './id-store-detail.component';

describe('Component Tests', () => {
  describe('IdStore Management Detail Component', () => {
    let comp: IdStoreDetailComponent;
    let fixture: ComponentFixture<IdStoreDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [IdStoreDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ idStore: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(IdStoreDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(IdStoreDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load idStore on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.idStore).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
