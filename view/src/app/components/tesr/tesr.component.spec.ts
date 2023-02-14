import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TesrComponent } from './tesr.component';

describe('TesrComponent', () => {
  let component: TesrComponent;
  let fixture: ComponentFixture<TesrComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ TesrComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(TesrComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
