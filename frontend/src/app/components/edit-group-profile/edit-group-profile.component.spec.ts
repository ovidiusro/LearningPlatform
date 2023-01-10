import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { EditGroupProfileComponent } from './edit-group-profile.component';

describe('EditGroupProfileComponent', () => {
  let component: EditGroupProfileComponent;
  let fixture: ComponentFixture<EditGroupProfileComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ EditGroupProfileComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(EditGroupProfileComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
