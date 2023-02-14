import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DeleteCarButtonComponent } from './delete-car-button.component';

describe('DeleteCarButtonComponent', () => {
  let component: DeleteCarButtonComponent;
  let fixture: ComponentFixture<DeleteCarButtonComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ DeleteCarButtonComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(DeleteCarButtonComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
