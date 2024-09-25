import {ChangeDetectorRef, NgZone, Pipe, PipeTransform} from '@angular/core';
import {interval, takeWhile} from 'rxjs';
import { startWith, switchMap } from 'rxjs/operators';
import { Subscription } from 'rxjs';

@Pipe({
  name: 'timeAgo',
  pure: false // this is required for the pipe to update itself when the input value changes
})
export class TimeAgoPipe implements PipeTransform {
  private timer!: Subscription;

  constructor(private changeDetectorRef: ChangeDetectorRef, private ngZone: NgZone) {}

  transform(value: any): any {
    if (!value) return value;

    this.ngZone.runOutsideAngular(() => {
      if (this.timer) {
        this.timer.unsubscribe();
      }
      this.timer = interval(60000).pipe(
        startWith(0),
        takeWhile(() => true)
      ).subscribe(() => {
        // Required because we're running outside of the Angular zone
        this.ngZone.run(() => this.changeDetectorRef.markForCheck());
      });
    });

    let diff = Math.floor((new Date().getTime() - new Date(value).getTime()) / 1000);
    if (diff < 60) return 'just now';
    if (diff < 3600) return Math.floor(diff / 60) + ' minutes ago';
    if (diff < 86400) return Math.floor(diff / 3600) + ' hours ago';
    return Math.floor(diff / 86400) + ' days ago';
  }

  ngOnDestroy(): void {
    if (this.timer) {
      this.timer.unsubscribe();
    }
  }
}
