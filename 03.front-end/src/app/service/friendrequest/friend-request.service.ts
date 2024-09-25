import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {AppConstants} from "../../app-constant";

@Injectable({
  providedIn: 'root'
})
export class FriendRequestService {

  constructor(private httpClient: HttpClient) {
  }

  getReceivedRequests() {
    return this.httpClient.get<any>(AppConstants.BASE_URL_API + `/v1/friends/requests/received`)
  }

  getSentRequests() {
    return this.httpClient.get<any>(AppConstants.BASE_URL_API + `/v1/friends/requests/sent`)
  }

  sendRequest(targetId: any) {
    return this.httpClient.post<any>(AppConstants.BASE_URL_API + `/v1/friends/requests/sent/${targetId}`, null);
  }

  deleteRequest(requestId: any) {
    return this.httpClient.delete<any>(AppConstants.BASE_URL_API + `/v1/friends/requests/${requestId}`)
  }

  confirmRequest(requestId: any) {
    return this.httpClient.put<any>(AppConstants.BASE_URL_API + `/v1/friends/requests/accept/${requestId}`, null);
  }
}
