import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { SchoolLedgerHeadService } from '../service/school-ledger-head.service';

import { SchoolLedgerHeadComponent } from './school-ledger-head.component';

describe('Component Tests', () => {
  describe('SchoolLedgerHead Management Component', () => {
    let comp: SchoolLedgerHeadComponent;
    let fixture: ComponentFixture<SchoolLedgerHeadComponent>;
    let service: SchoolLedgerHeadService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [SchoolLedgerHeadComponent],
      })
        .overrideTemplate(SchoolLedgerHeadComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(SchoolLedgerHeadComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(SchoolLedgerHeadService);

      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [{ id: 123 }],
            headers,
          })
        )
      );
    });

    it('Should call load all on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.schoolLedgerHeads?.[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
