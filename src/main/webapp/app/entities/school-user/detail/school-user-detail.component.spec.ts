import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { SchoolUserDetailComponent } from './school-user-detail.component';

describe('Component Tests', () => {
  describe('SchoolUser Management Detail Component', () => {
    let comp: SchoolUserDetailComponent;
    let fixture: ComponentFixture<SchoolUserDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [SchoolUserDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ schoolUser: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(SchoolUserDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(SchoolUserDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load schoolUser on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.schoolUser).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
