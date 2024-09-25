import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {AppConstants} from "../../app-constant";

@Injectable({
  providedIn: 'root'
})
export class ProfileService {

  constructor(private httpClient: HttpClient) {
  }

  getCurrentUser(id:any){
    return this.httpClient.get<any>(AppConstants.BASE_URL_API + `/v1/users/${id}/profile`)
  }

  searchUsers(keyword:any) {
    return this.httpClient.get<any>(AppConstants.BASE_URL_API + `/v1/users/search?keyword=${keyword}`)
  }
}
