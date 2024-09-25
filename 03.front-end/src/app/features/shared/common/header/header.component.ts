import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {TokenUtils} from "../../utils/token.utils";
import {ProfileService} from "../../../../service/profile/profile.service";
import {Router} from "@angular/router";
import {FormControl, FormGroup} from "@angular/forms";

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css']
})
export class HeaderComponent implements OnInit {

  @Input() pageTitle!: any;
  @Output() searchAgain: EventEmitter<any> = new EventEmitter();
  userId!: any;
  user: any;
  searchForm!: any;

  constructor(private service: ProfileService,
              private router: Router) {

  }

  ngOnInit() {
    const token = sessionStorage.getItem('access_token');
    if (token) {
      const payload = TokenUtils.parseJwt(token);
      this.userId = payload.id;
      this.getCurrentUser(this.userId);
    }
    this.initializeSearchForm();
  }


  getCurrentUser(id: any) {
    this.service.getCurrentUser(id).subscribe({
      next: (data: any) => {
        this.user = data.data;
      }
    })
  }

  logOut() {
    sessionStorage.removeItem("access_token");
    this.router.navigate(["/login"]).then(() => {
    })
  }

  private initializeSearchForm() {
    this.searchForm = new FormGroup({
      keyword: new FormControl(),
    })
  }

  toSearchPage() {
    const keyword = this.searchForm.controls['keyword'].value;
    sessionStorage.setItem("keyword",keyword)
    this.router.navigate(["/search-result"]).then(r => {
    });
    this.searchAgain.emit()
  }
}
