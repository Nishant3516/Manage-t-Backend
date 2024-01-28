import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ClassSubjectDetailComponent } from './class-subject-detail.component';

describe('Component Tests', () => {
  describe('ClassSubject Management Detail Component', () => {
    let comp: ClassSubjectDetailComponent;
    let fixture: ComponentFixture<ClassSubjectDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [ClassSubjectDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ classSubject: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(ClassSubjectDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(ClassSubjectDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load classSubject on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.classSubject).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
