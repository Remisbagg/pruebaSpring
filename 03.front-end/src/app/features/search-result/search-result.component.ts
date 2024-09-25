import {Component, EventEmitter, OnInit, Output} from '@angular/core';
import {TokenUtils} from "../shared/utils/token.utils";
import {Router} from "@angular/router";
import Swal from "sweetalert2";
import {FriendRequestService} from "../../service/friendrequest/friend-request.service";
import {SweetAlertService} from "../../service/alert/sweet-alert.service";
import {ProfileService} from "../../service/profile/profile.service";
import {FriendshipService} from "../../service/friendship/friendship.service";

@Component({
  selector: 'app-search-result',
  templateUrl: './search-result.component.html',
  styleUrls: ['./search-result.component.css']
})
export class SearchResultComponent implements OnInit {
  users!: any;
  keyword!: any;

  constructor(private router: Router,
              private friendRequestService: FriendRequestService,
              private alertService: SweetAlertService,
              private profileService: ProfileService,
              private friendshipService: FriendshipService) {
  }

  ngOnInit(): void {
    const keyword = sessionStorage.getItem('keyword');
    if (keyword) {
      this.keyword = keyword;
      sessionStorage.removeItem("keyword")
      this.search();
    } else {
      this.router.navigate(["/error"]).then(r => {
      })
    }
  }

  search() {
    this.profileService.searchUsers(this.keyword).subscribe({
      next: (data: any) => {
        this.users = data.data;
      },
      error: (data: any) => {
        console.log(data)
      }
    })
  }

  reload() {
    this.ngOnInit();
  }

  isFriend(user: any) {
    return user.friendship.status
  }

  isFriendRequest(user: any) {
    const role = user.friendRequest.role
    return role != null;
  }

  cancelRequest(id: any) {
    Swal.fire({
      title: "Cancel request?",
      showCancelButton: true,
      confirmButtonColor: "#3085d6",
      cancelButtonColor: "#d33",
      confirmButtonText: "Yes!"
    }).then((result) => {
      if (result.isConfirmed) {
        this.friendRequestService.deleteRequest(id).subscribe({
          next: () => {
            this.search()
          },
          error: (data: any) => {
            const text = JSON.stringify(data.error.error);
            this.alertService.error("Error", text);
          }
        })
      }
    });
  }

  confirmRequest(requestId: any) {
    Swal.fire({
      title: "Confirm request?",
      showCancelButton: true,
      confirmButtonColor: "#3085d6",
      cancelButtonColor: "#d33",
      confirmButtonText: "Yes!"
    }).then((result) => {
      if (result.isConfirmed) {
        this.friendRequestService.confirmRequest(requestId).subscribe({
          next: () => {
            this.search()
          },
          error: (data: any) => {
            const text = JSON.stringify(data.error.error);
            this.alertService.error("Error", text);
          }
        })
      }
    })
  }

  cancelOrConfirmRequest(requestId: any) {
    Swal.fire({
      title: "Accept Or Cancel?",
      showDenyButton: true,
      showCancelButton: true,
      confirmButtonText: "Accept",
      denyButtonText: `Reject`
    }).then((result) => {
      if (result.isConfirmed) {
        this.confirmRequest(requestId)
      } else if (result.isDenied) {
        this.cancelRequest(requestId);
      }
    });
  }

  sendRequest(id: any) {
    Swal.fire({
      title: "Send request?",
      showCancelButton: true,
      confirmButtonColor: "#3085d6",
      cancelButtonColor: "#d33",
      confirmButtonText: "Yes!"
    }).then((result) => {
      if (result.isConfirmed) {
        this.friendRequestService.sendRequest(id).subscribe({
          next: () => {
            this.search()
          },
          error: (data: any) => {
            const text = JSON.stringify(data.error.error);
            this.alertService.error("Error", text);
          }
        })
      }
    });
  }

  unfriend(id: any) {
    Swal.fire({
      title: "Unfriend?",
      showCancelButton: true,
      confirmButtonColor: "#3085d6",
      cancelButtonColor: "#d33",
      confirmButtonText: "Yes!"
    }).then((result) => {
      if (result.isConfirmed) {
        this.friendshipService.deleteFriend(id).subscribe({
          next: () => {
            this.search()
          },
          error: (data: any) => {
            const text = JSON.stringify(data.error.error);
            this.alertService.error("Error", text);
          }
        })
      }
    });
  }
}
