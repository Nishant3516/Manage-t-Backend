import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { AuditLogDetailComponent } from './audit-log-detail.component';

describe('Component Tests', () => {
  describe('AuditLog Management Detail Component', () => {
    let comp: AuditLogDetailComponent;
    let fixture: ComponentFixture<AuditLogDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [AuditLogDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ auditLog: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(AuditLogDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(AuditLogDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load auditLog on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.auditLog).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
