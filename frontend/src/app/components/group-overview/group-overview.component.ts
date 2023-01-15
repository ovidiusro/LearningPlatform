import { Component, OnInit } from "@angular/core";
import { GroupDetailsComponent } from "../group-details/group-details.component";
import { HttpGroupService } from "src/app/services/http-group.service";
import { Router } from "@angular/router";
import { AppComponent } from "src/app/app.component";

@Component({
  selector: "app-group-overview",
  templateUrl: "./group-overview.component.html",
  styleUrls: ["./group-overview.component.css"],
})
export class GroupOverviewComponent implements OnInit {
  constructor(
    private groupDetail: GroupDetailsComponent,
    private httpGroupService: HttpGroupService,
    private app: AppComponent,
    private rout: Router
  ) {}

  group: any;
  principal: any;
  http: string;
  subscribed = false;
  isLogged;

  ngOnInit() {
    this.isLogged = this.app.isLogged;
    this.groupDetail.groupSubjectCast.subscribe((res) => {
      this.group = res;
      console.log(this.group);
      if (res !== false && res.banner !== null) {
        this.http = "http://localhost:8080/api/files/" + this.group.banner.id;
      }
      if (res !== false) {
        this.checkIfRegistered();
      }
    });
  }
  checkIfRegistered() {
    console.log("in check " + this.isLogged);
    if (this.isLogged === true) {
      this.httpGroupService
        .getFolowers(this.group.id)
        .subscribe((res2: any) => {
          const list: string[] = [];
          this.principal = JSON.parse(localStorage.getItem("currentUser"));
          for (const result of res2) {
            list.push(result.Id);
            if (result.id === this.principal.id) {
              this.subscribed = true;
              break;
            }
          }
        });
    }
  }

  unsubscribing() {
    this.httpGroupService
      .removeFollower(this.group.id, this.principal.id)
      .subscribe((res: any) => {
        this.subscribed = false;
      });
  }
  subscribing() {
    if (this.isLogged === true) {
      this.httpGroupService
        .addFollower(this.group.id, this.principal.id)
        .subscribe((res: any) => {
          this.subscribed = true;
        });
    } else {
      this.rout.navigateByUrl("sign-in");
    }
  }
}
