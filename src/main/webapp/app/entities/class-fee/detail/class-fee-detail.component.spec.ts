import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ClassFeeDetailComponent } from './class-fee-detail.component';

describe('Component Tests', () => {
  describe('ClassFee Management Detail Component', () => {
    let comp: ClassFeeDetailComponent;
    let fixture: ComponentFixture<ClassFeeDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [ClassFeeDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ classFee: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(ClassFeeDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(ClassFeeDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load classFee on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.classFee).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
