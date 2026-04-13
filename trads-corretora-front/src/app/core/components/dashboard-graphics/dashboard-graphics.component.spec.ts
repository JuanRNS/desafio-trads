import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DashboardGraphicsComponent } from './dashboard-graphics.component';

describe('DashboardGraphicsComponent', () => {
  let component: DashboardGraphicsComponent;
  let fixture: ComponentFixture<DashboardGraphicsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [DashboardGraphicsComponent],
    }).compileComponents();

    fixture = TestBed.createComponent(DashboardGraphicsComponent);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
