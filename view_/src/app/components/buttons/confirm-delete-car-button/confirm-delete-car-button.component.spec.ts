import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ConfirmDeleteCarButtonComponent } from './confirm-delete-car-button.component';

describe('ConfirmDeleteCarButtonComponent', () => {
  let component: ConfirmDeleteCarButtonComponent;
  let fixture: ComponentFixture<ConfirmDeleteCarButtonComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ConfirmDeleteCarButtonComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ConfirmDeleteCarButtonComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
