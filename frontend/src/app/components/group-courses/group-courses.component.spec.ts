import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { GroupCoursesComponent } from './group-courses.component';

describe('GroupCoursesComponent', () => {
  let component: GroupCoursesComponent;
  let fixture: ComponentFixture<GroupCoursesComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ GroupCoursesComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(GroupCoursesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
