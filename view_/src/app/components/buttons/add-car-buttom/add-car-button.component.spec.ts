import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AddCarButtonComponent } from './add-car-button.component';

describe('AddCarButtomComponent', () => {
  let component: AddCarButtonComponent;
  let fixture: ComponentFixture<AddCarButtonComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ AddCarButtonComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AddCarButtonComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
