import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PostAdditionalInfoComponent } from './post-additional-info.component';

describe('PostAdditionalInfoComponent', () => {
  let component: PostAdditionalInfoComponent;
  let fixture: ComponentFixture<PostAdditionalInfoComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ PostAdditionalInfoComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(PostAdditionalInfoComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
