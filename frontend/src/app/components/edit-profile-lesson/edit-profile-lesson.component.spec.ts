import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { EditProfileLessonComponent } from './edit-profile-lesson.component';

describe('EditProfileLessonComponent', () => {
  let component: EditProfileLessonComponent;
  let fixture: ComponentFixture<EditProfileLessonComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ EditProfileLessonComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(EditProfileLessonComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
