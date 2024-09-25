import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from "@angular/router";
import {AccountService} from "../../../service/account/account.service";
import {FormControl, FormGroup} from "@angular/forms";

@Component({
  selector: 'app-reset-password',
  templateUrl: './reset-password.component.html',
  styleUrls: ['./reset-password.component.css']
})
export class ResetPasswordComponent implements OnInit {
  email!: any;
  formData!: any;
  revealPassFields = true;

  constructor(private activatedRoute: ActivatedRoute,
              private router: Router,
              private accountService: AccountService) {
  }

  ngOnInit() {
    this.activatedRoute.queryParams.subscribe(params => {
      const token = params['token'];
      if (!sessionStorage.getItem('resetPwdToken')) {
        this.validateToken(token);
      }
      if (sessionStorage.getItem('email')) {
        this.email = sessionStorage.getItem('email')
      }
      this.initializeForm();
    })
  }

  validateToken(token: string) {
    this.accountService.validateResetPasswordToken(token).subscribe({
      next: (data) => {
        if (data.status === 'error') {
          this.router.navigate(['/error']);
          sessionStorage.removeItem('resetPwdToken');
          sessionStorage.removeItem('email');
        } else {
          this.email = data.data.email;
          this.initializeForm();
          sessionStorage.setItem('resetPwdToken', 'true');
          sessionStorage.setItem('email', data.data.email);
        }
      },
      error: () => {
        this.router.navigate(['/error']);
        sessionStorage.removeItem('resetPwdToken');
        sessionStorage.removeItem('email');
      }
    })
  }

  initializeForm() {
    this.formData = new FormGroup({
      email: new FormControl(this.email),
      password: new FormControl('')
    })
  }

  submitForm() {
    const account = this.formData.value;
    this.accountService.resetPassword(account).subscribe({
      next: (data) => {
        if (data.status === 'error') {
          this.router.navigate(['/error']);
          sessionStorage.removeItem('resetPwdToken');
        } else {
          this.router.navigate(['/login'])
          sessionStorage.removeItem('resetPwdToken');
        }
      },
      error: () => {
        this.router.navigate(['/error']);
        sessionStorage.removeItem('resetPwdToken');
      }
    })
  }

  revealPass() {
    this.revealPassFields = !this.revealPassFields;
  }
}
