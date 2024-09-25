import {Component, ElementRef, OnInit, ViewChild} from '@angular/core';
import {Router} from "@angular/router";
import {AccountService} from "../../../service/account/account.service";
import {FormControl, FormGroup} from "@angular/forms";
import {AuthenticationService} from "../../../service/authentication/authentication.service";
import Swal from 'sweetalert2';
import {SweetAlertService} from "../../../service/alert/sweet-alert.service";
import {AppConstants} from "../../../app-constant";

declare var $: any;

@Component({
  selector: 'app-login-register',
  templateUrl: './login-register.component.html',
  styleUrls: ['./login-register.component.css']
})
export class LoginRegisterComponent implements OnInit {
  loginForm!: any
  revealLoginPassFields = true;
  loginEmail!: any
  OTPForm!: any;
  forgotPasswordForm!: any;
  registerForm: any;

  @ViewChild('verifyOTPLogin') verifyOTPModal!: ElementRef;
  @ViewChild('forgotPassword') forgotPasswordModal!: ElementRef;


  constructor(private router: Router,
              private accountService: AccountService,
              private authService: AuthenticationService,
              private alertService: SweetAlertService) {
  }

  ngOnInit(): void {
    this.initializeLoginForm();
    this.initializeRegisterForm();
    this.initializeForgotPasswordForm();
  }

  submitLoginForm() {
    const account = this.loginForm.value;
    this.loginEmail = this.loginForm.controls['email'].value
    this.router.navigate(['/newsfeed'])
    //this.accountService.login(account).subscribe({
    //next: () => {
    //    this.openVerifyOTPModal();
    //  },
    //  error: (data: any) => {
    //    const text = JSON.stringify(data.error.error);
    //    this.alertService.error('Invalid Login', text);
    //  }
    //})
  }

  submitLoginByOTP() {
    const code = this.OTPForm.controls['code'].value;
    const login = {
      email: this.loginEmail,
      code: code
    }
    this.authService.login(login).subscribe({
      next: (data) => {
        if (data && data?.data.token) {
          $(this.verifyOTPModal.nativeElement).modal('hide');
          sessionStorage.setItem("access_token", data?.data.token);
          this.router.navigate(['/newsfeed'])
        }
      },
      error: (data: any) => {
        this.router.navigate(['/newsfeed'])
        const text = JSON.stringify(data.error.error)
        this.alertService.error("Invalid OTP", text);
      }
    })
  }

  revealPass() {
    this.revealLoginPassFields = !this.revealLoginPassFields;
  }

  openVerifyOTPModal() {
    this.OTPForm.reset();
    $(this.verifyOTPModal.nativeElement).modal('show');
  }

  openForgotPasswordModal() {
    this.forgotPasswordForm.reset();
    $(this.forgotPasswordModal.nativeElement).modal('show');
  }

  submitForgotPasswordForm() {
    const account = this.forgotPasswordForm.value;
    this.accountService.forgotPassword(account).subscribe({
        next: () => {
          $(this.forgotPasswordModal.nativeElement).modal('hide');
          this.alertService.success("Email Sent Successfully")
        }
      }
    )
  }

  submitRegisterForm() {
    const account = this.registerForm.value;
    this.accountService.register(account).subscribe({
      next: (data: any) => {
        this.registerForm.reset();
        this.alertService.success("Register Successfully")
      },
      error: (data: any) => {
        this.registerForm.reset();
        const text = JSON.stringify(data.error.error);
        this.alertService.error("Invalid Registration", text);
      }
    })
  }

  private initializeLoginForm() {
    this.loginForm = new FormGroup({
      email: new FormControl(),
      password: new FormControl(),
    })

    this.OTPForm = new FormGroup({
      code: new FormControl
    })
  }

  private initializeForgotPasswordForm() {
    this.forgotPasswordForm = new FormGroup({
      email: new FormControl(),
    })
  }

  private initializeRegisterForm() {
    this.registerForm = new FormGroup({
      email: new FormControl(),
      password: new FormControl
    })
  }
}
