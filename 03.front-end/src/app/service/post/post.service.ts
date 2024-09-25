import { Injectable } from '@angular/core';
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {AppConstants} from "../../app-constant";

@Injectable({
  providedIn: 'root'
})
export class PostService {

  constructor(private httpClient:HttpClient) { }

  getNewsfeed(id:any){
    return this.httpClient.get<any>(AppConstants.BASE_URL_API + `/v1/users/${id}/newsfeed`)
  }

  createNewPost(userId:any, postData:any, files:File[]) {
    const formData:FormData = new FormData();

    if (postData) {
      formData.append('post', JSON.stringify(postData));
    }

    // Append files if there are any
    if (files && files.length) {
      for (let file of files) {
        formData.append('files', file, file.name);
      }
    }
    return this.httpClient.post<any>(AppConstants.BASE_URL_API  + `/v1/users/${userId}/posts`,formData)
  }

  getUserPosts(id:any) {
    return this.httpClient.get<any>(AppConstants.BASE_URL_API + `/v1/users/${id}/posts`)
  }

  deletePost(userId:any, postId:any) {
    return this.httpClient.delete<any>(AppConstants.BASE_URL_API + `/v1/users/${userId}/posts/${postId}`)
  }
}
