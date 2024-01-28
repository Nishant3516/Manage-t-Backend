import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { SchoolLedgerHeadDetailComponent } from './school-ledger-head-detail.component';

describe('Component Tests', () => {
  describe('SchoolLedgerHead Management Detail Component', () => {
    let comp: SchoolLedgerHeadDetailComponent;
    let fixture: ComponentFixture<SchoolLedgerHeadDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [SchoolLedgerHeadDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ schoolLedgerHead: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(SchoolLedgerHeadDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(SchoolLedgerHeadDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load schoolLedgerHead on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.schoolLedgerHead).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
