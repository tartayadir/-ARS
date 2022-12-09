import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EditCarButtonComponent } from './edit-car-button.component';

describe('EditCarButtonComponent', () => {
  let component: EditCarButtonComponent;
  let fixture: ComponentFixture<EditCarButtonComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ EditCarButtonComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(EditCarButtonComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
