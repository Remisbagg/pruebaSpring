import {Injectable} from '@angular/core';
import * as Stomp from 'stompjs';
import * as SockJS from 'sockjs-client';

@Injectable({
  providedIn: 'root'
})
export class WebsocketService {
  private serverUrl = 'http://localhost:8080/ws'
  private stompClient!: any;
  private connected!: Promise<any>;

  constructor() {
    this.initializeWebSocketConnection();
  }

  private initializeWebSocketConnection() {
    const ws = new SockJS(this.serverUrl);
    this.stompClient = Stomp.over(ws);
    this.connected = new Promise((resolve, reject) => {
      this.stompClient.connect({}, (frame: any) => {
        resolve(frame);
      }, (error: any) => {
        reject(error);
      });
    });
  }

  async subscribeToTopic(topic: string, callback: (message: any) => void) {
    await this.connected; // Ensure the connection is established
    return this.stompClient.subscribe(topic, (message: any) => {
      callback(JSON.parse(message.body));
    });
  }

  unsubscribeFromTopic(subscription: any) {
    if (subscription) {
      subscription.unsubscribe();
    }
  }

  async sendMessage(destination: string, message: any) {
    await this.connected; // Ensure the connection is established
    this.stompClient.send(destination, {}, JSON.stringify(message));
  }

  // private onNewsfeedPostReceivedCallback!: (newPost: any) => void;
  //
  // private onNewCommentReceivedCallback!: (newPost: any) => void;
  //
  // private onNewPostLikeReactionReceivedCallback!: (newPost: any) => void;
  //
  // initializeWebSocketGetNewsfeedPostConnection(userId: any) {
  //   let ws = new SockJS(this.serverUrl);
  //   this.stompClient = Stomp.over(ws);
  //   this.stompClient.connect({}, (frames: any) => {
  //     const userSpecificTopic = `/topic/user.${userId}.newPost`;
  //     this.stompClient.subscribe(userSpecificTopic, (message: any) => {
  //       if (this.onNewsfeedPostReceivedCallback) {
  //         this.onNewsfeedPostReceivedCallback(JSON.parse(message.body));
  //       }
  //     })
  //   })
  // }
  //
  // initializeWebSocketGetNewCommentConnection(userId: any) {
  //   let ws = new SockJS(this.serverUrl);
  //   this.stompClient = Stomp.over(ws);
  //   this.stompClient.connect({}, (frames: any) => {
  //     const userSpecificTopic = `/topic/user.${userId}.newComment`;
  //     this.stompClient.subscribe(userSpecificTopic, (message: any) => {
  //       if (this.onNewCommentReceivedCallback) {
  //         this.onNewCommentReceivedCallback(JSON.parse(message.body));
  //       }
  //     })
  //   })
  // }
  //
  // initializeWebSocketGetNewPostLikeReaction(userId: any) {
  //   let ws = new SockJS(this.serverUrl);
  //   this.stompClient = Stomp.over(ws);
  //   this.stompClient.connect({}, (frames: any) => {
  //     const userSpecificTopic = `/topic/user.${userId}.newPostLikeReact`;
  //     this.stompClient.subscribe(userSpecificTopic, (message: any) => {
  //       if (this.onNewPostLikeReactionReceivedCallback) {
  //         this.onNewPostLikeReactionReceivedCallback(JSON.parse(message.body));
  //       }
  //     })
  //   })
  // }
  //
  // setOnNewsfeedPostReceivedCallback(callback: (newPost: any) => void) {
  //   this.onNewsfeedPostReceivedCallback = callback;
  // }
  //
  // setOnNewCommentReceivedCallback(callback: (newPost: any) => void) {
  //   this.onNewCommentReceivedCallback = callback;
  // }
  //
  // setOnNewPostLikeReactionReceivedCallback(callback: (newPost: any) => void) {
  //   this.onNewPostLikeReactionReceivedCallback = callback;
  // }
}
