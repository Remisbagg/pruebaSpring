import {AfterViewInit, Component, ElementRef, OnInit} from '@angular/core';
import {Router} from "@angular/router";
import {TokenUtils} from "./features/shared/utils/token.utils";
import {WebsocketService} from "./service/websocket/websocket.service";
import {SweetAlertService} from "./service/alert/sweet-alert.service";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit, AfterViewInit {
  title = '03.front-end';
  userId!: any;

  constructor(private elementRef: ElementRef,
              private router: Router,
              private websocketService: WebsocketService,
              private alertService: SweetAlertService) {
  }

  ngOnInit() {
    const token = sessionStorage.getItem('access_token');
    if (token) {
      const payload = TokenUtils.parseJwt(token);
      this.userId = payload.id;
      this.connectWebSocket();
    }
  }

  ngAfterViewInit() {
    const s1 = document.createElement("script");
    s1.type = "/text/javascript";
    s1.src = "../assets/js/jQuery/jquery-3.4.1.js";
    this.elementRef.nativeElement.appendChild(s1);

    const s2 = document.createElement("script");
    s2.type = "/text/javascript";
    s2.src = "../assets/js/libs/jquery.appear.js";
    this.elementRef.nativeElement.appendChild(s2);

    const s3 = document.createElement("script");
    s3.type = "/text/javascript";
    s3.src = "../assets/js/libs/jquery.mousewheel.js";
    this.elementRef.nativeElement.appendChild(s3);

    const s4 = document.createElement("script");
    s4.type = "/text/javascript";
    s4.src = "../assets/js/libs/perfect-scrollbar.js";
    this.elementRef.nativeElement.appendChild(s4);

    const s5 = document.createElement("script");
    s5.type = "/text/javascript";
    s5.src = "../assets/js/libs/jquery.matchHeight.js";
    this.elementRef.nativeElement.appendChild(s5);

    const s6 = document.createElement("script");
    s6.type = "/text/javascript";
    s6.src = "../assets/js/libs/svgxuse.js";
    this.elementRef.nativeElement.appendChild(s6);

    const s7 = document.createElement("script");
    s7.type = "/text/javascript";
    s7.src = "../assets/js/libs/imagesloaded.pkgd.js";
    this.elementRef.nativeElement.appendChild(s7);

    const s8 = document.createElement("script");
    s8.type = "/text/javascript";
    s8.src = "../assets/js/libs/Headroom.js";
    this.elementRef.nativeElement.appendChild(s8);

    const s9 = document.createElement("script");
    s9.type = "/text/javascript";
    s9.src = "../assets/js/libs/velocity.js";
    this.elementRef.nativeElement.appendChild(s9);

    const s10 = document.createElement("script");
    s10.type = "/text/javascript";
    s10.src = "../assets/js/libs/ScrollMagic.js";
    this.elementRef.nativeElement.appendChild(s10);

    const s11 = document.createElement("script");
    s11.type = "/text/javascript";
    s11.src = "../assets/js/libs/jquery.waypoints.js";
    this.elementRef.nativeElement.appendChild(s11);

    const s12 = document.createElement("script");
    s12.type = "/text/javascript";
    s12.src = "../assets/js/libs/jquery.countTo.js";
    this.elementRef.nativeElement.appendChild(s12);

    const s13 = document.createElement("script");
    s13.type = "/text/javascript";
    s13.src = "../assets/js/libs/popper.min.js";
    this.elementRef.nativeElement.appendChild(s13);

    const s14 = document.createElement("script");
    s14.type = "/text/javascript";
    s14.src = "../assets/js/libs/material.min.js";
    this.elementRef.nativeElement.appendChild(s14);

    const s15 = document.createElement("script");
    s15.type = "/text/javascript";
    s15.src = "../assets/js/libs/bootstrap-select.js";
    this.elementRef.nativeElement.appendChild(s15);

    const s16 = document.createElement("script");
    s16.type = "/text/javascript";
    s16.src = "../assets/js/libs/smooth-scroll.js";
    this.elementRef.nativeElement.appendChild(s16);

    const s17 = document.createElement("script");
    s17.type = "/text/javascript";
    s17.src = "../assets/js/libs/selectize.js";
    this.elementRef.nativeElement.appendChild(s17);

    const s18 = document.createElement("script");
    s18.type = "/text/javascript";
    s18.src = "../assets/js/libs/swiper.jquery.js";
    this.elementRef.nativeElement.appendChild(s18);

    const s19 = document.createElement("script");
    s19.type = "/text/javascript";
    s19.src = "../assets/js/libs/moment.js";
    this.elementRef.nativeElement.appendChild(s19);

    const s20 = document.createElement("script");
    s20.type = "/text/javascript";
    s20.src = "../assets/js/libs/daterangepicker.js";
    this.elementRef.nativeElement.appendChild(s20);

    const s21 = document.createElement("script");
    s21.type = "/text/javascript";
    s21.src = "../assets/js/libs/fullcalendar.js";
    this.elementRef.nativeElement.appendChild(s21);

    const s22 = document.createElement("script");
    s22.type = "/text/javascript";
    s22.src = "../assets/js/libs/isotope.pkgd.js";
    this.elementRef.nativeElement.appendChild(s22);

    const s23 = document.createElement("script");
    s23.type = "/text/javascript";
    s23.src = "../assets/js/libs/ajax-pagination.js";
    this.elementRef.nativeElement.appendChild(s23);

    const s24 = document.createElement("script");
    s24.type = "/text/javascript";
    s24.src = "../assets/js/libs/Chart.js";
    this.elementRef.nativeElement.appendChild(s24);

    const s25 = document.createElement("script");
    s25.type = "/text/javascript";
    s25.src = "../assets/js/libs/chartjs-plugin-deferred.js";
    this.elementRef.nativeElement.appendChild(s25);

    const s26 = document.createElement("script");
    s26.type = "/text/javascript";
    s26.src = "../assets/js/libs/circle-progress.js";
    this.elementRef.nativeElement.appendChild(s26);

    const s27 = document.createElement("script");
    s27.type = "/text/javascript";
    s27.src = "../assets/js/libs/loader.js";
    this.elementRef.nativeElement.appendChild(s27);

    const s28 = document.createElement("script");
    s28.type = "/text/javascript";
    s28.src = "../assets/js/libs/run-chart.js";
    this.elementRef.nativeElement.appendChild(s28);

    const s29 = document.createElement("script");
    s29.type = "/text/javascript";
    s29.src = "../assets/js/libs/jquery.magnific-popup.js";
    this.elementRef.nativeElement.appendChild(s29);

    const s30 = document.createElement("script");
    s30.type = "/text/javascript";
    s30.src = "../assets/js/libs/jquery.gifplayer.js";
    this.elementRef.nativeElement.appendChild(s30);

    const s31 = document.createElement("script");
    s31.type = "/text/javascript";
    s31.src = "../assets/js/libs/mediaelement-and-player.js";
    this.elementRef.nativeElement.appendChild(s31);

    const s32 = document.createElement("script");
    s32.type = "/text/javascript";
    s32.src = "../assets/js/libs/mediaelement-playlist-plugin.min.js";
    this.elementRef.nativeElement.appendChild(s32);

    const s33 = document.createElement("script");
    s33.type = "/text/javascript";
    s33.src = "../assets/js/libs/ion.rangeSlider.js";
    this.elementRef.nativeElement.appendChild(s33);

    const s34 = document.createElement("script");
    s34.type = "/text/javascript";
    s34.src = "../assets/js/main.js";
    this.elementRef.nativeElement.appendChild(s34);

    const s35 = document.createElement("script");
    s35.type = "/text/javascript";
    s35.src = "../assets/js/libs-init/libs-init.js";
    this.elementRef.nativeElement.appendChild(s35);

    const s36 = document.createElement("script");
    s36.type = "/text/javascript";
    s36.src = "../assets/fonts/fontawesome-all.js";
    this.elementRef.nativeElement.appendChild(s36);

    const s37 = document.createElement("script");
    s37.type = "/text/javascript";
    s37.src = "../assets/Bootstrap/dist/js/bootstrap.bundle.js";
    this.elementRef.nativeElement.appendChild(s37);

    const s38 = document.createElement("script");
    s38.type = "/text/javascript";
    s38.src = "../assets/js/libs/webfontloader.min.js";
    this.elementRef.nativeElement.appendChild(s38);

    const s39 = document.createElement("script");
    s39.type = "/text/javascript";
    s39.src = "../assets/js/libs/webfont-load.js";
    this.elementRef.nativeElement.appendChild(s39);

    const s40 = document.createElement("script");
    s40.type = "/text/javascript";
    s40.src = "../assets/js/libs/jquery.magnific-popup.js";
    this.elementRef.nativeElement.appendChild(s40);
  }


  private connectWebSocket() {
    this.notifyNewsfeedUpdate();
    this.notifyNewComment();
    this.notifyNewLike();
  }

  private notifyNewsfeedUpdate() {
    const topic = `/topic/user.${this.userId}.newPost`;
    this.websocketService.subscribeToTopic(topic, (message) => {
      if (this.userId != message.userId) {
        this.alertService.success("Newsfeed Updated");
      }
    }).then(() => {
    })
  }

  private notifyNewComment() {
    const topic = `/topic/user.${this.userId}.newComment`
    this.websocketService.subscribeToTopic(topic, (message) => {
      if (this.userId == message.postOwnerId && this.userId != message.commentOwnerId) {
        this.alertService.success("You got new comment");
      }
    }).then(() => {
    })
  }

  private notifyNewLike() {
    const topic = `/topic/user.${this.userId}.newPostLike`;
    this.websocketService.subscribeToTopic(topic, (message) => {
      if (message.like == "liked" && (this.userId == message.postOwnerId && this.userId != message.likerId))
        this.alertService.success("You got new like");
    }).then(() => {
    })
  }
}
