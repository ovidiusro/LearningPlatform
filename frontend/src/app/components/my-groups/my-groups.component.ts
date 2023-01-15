import { Component, OnInit } from "@angular/core";
import { HttpAccountService } from "src/app/services/httpAccount.service";
import { HttpGroupService } from "src/app/services/http-group.service";

@Component({
  selector: "app-my-groups",
  templateUrl: "./my-groups.component.html",
  styleUrls: ["./my-groups.component.css"],
})
export class MyGroupsComponent implements OnInit {
  initialized = false;
  groups: any;
  httpRoot = "http://localhost:8080/api/files/";

  constructor(
    private httpAccountService: HttpAccountService,
    private httpGroupService: HttpGroupService
  ) {}

  ngOnInit() {
    this.getOwnGroups();
  }

  getOwnGroups() {
    const principal = JSON.parse(localStorage.getItem("currentUser"));
    this.groups = this.httpAccountService
      .getOwnGroups(principal.id)
      .subscribe((res: any) => {
        this.groups = res;
        this.initialized = true;
      });
  }

  deleteGroup(idGroup) {
    this.httpGroupService.deleteGroup(idGroup).subscribe((res: any) => {
      this.getOwnGroups();
    });
  }
}
