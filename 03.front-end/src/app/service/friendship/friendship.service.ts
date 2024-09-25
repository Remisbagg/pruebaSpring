import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {AppConstants} from "../../app-constant";

@Injectable({
  providedIn: 'root'
})
export class FriendshipService {

  constructor(private httpClient:HttpClient) { }

  getFriendList() {
    return this.httpClient.get<any>(AppConstants.BASE_URL_API + `/v1/friendship/friends-list`)
  }

  deleteFriend(id:any) {
    return this.httpClient.delete<any>(AppConstants.BASE_URL_API + `/v1/friendship/${id}`)
  }
}
