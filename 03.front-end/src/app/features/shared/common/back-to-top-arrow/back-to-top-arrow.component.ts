import { Component } from '@angular/core';

@Component({
  selector: 'app-back-to-top-arrow',
  templateUrl: './back-to-top-arrow.component.html',
  styleUrls: ['./back-to-top-arrow.component.css']
})
export class BackToTopArrowComponent {

  toTop() {
    window.scroll({
      top:0,
      left:0,
      behavior: "smooth"
      }
    )
  }
}
