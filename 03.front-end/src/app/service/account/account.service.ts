import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {AppConstants} from "../../app-constant";

@Injectable({
  providedIn: 'root'
})
export class AccountService {

  constructor(private httpClient: HttpClient) {
  }

  validateResetPasswordToken(token: string) {
    return this.httpClient.get<any>(AppConstants.BASE_URL_API + "/v1/account/reset-password?token=" + token);
  }

  resetPassword(account: any) {
    return this.httpClient.post<any>(AppConstants.BASE_URL_API + "/v1/account/reset-password", account);
  }

  login(account: any) {
    return this.httpClient.post<any>(AppConstants.BASE_URL_API + "/v1/account/login", account);
  }

  register(account:any) {
    return this.httpClient.post<any>(AppConstants.BASE_URL_API + "/v1/account/register", account)
  }

  forgotPassword(account:any){
    return this.httpClient.post<any>(AppConstants.BASE_URL_API + "/v1/account/forgot-password", account);
  }
}
