import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { STRouteDetailComponent } from './st-route-detail.component';

describe('STRoute Management Detail Component', () => {
  let comp: STRouteDetailComponent;
  let fixture: ComponentFixture<STRouteDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [STRouteDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ sTRoute: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(STRouteDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(STRouteDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load sTRoute on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.sTRoute).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
