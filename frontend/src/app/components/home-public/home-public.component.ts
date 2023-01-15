import { Component, OnInit } from "@angular/core";
import { HttpGroupService } from "../../services/http-group.service";
import { HttpSerachService } from "src/app/services/http-serach.service";
import { HomeComponent } from "../home/home.component";

@Component({
  selector: "app-home-public",
  templateUrl: "./home-public.component.html",
  styleUrls: ["./home-public.component.css"],
})
export class HomePublicComponent implements OnInit {
  groups: any;
  httpSearchService: any;
  httpRoot = "http://localhost:8080/api/files/";

  constructor(
    private httpGroupService: HttpGroupService,
    private homeComponent: HomeComponent
  ) {
    this.httpSearchService = homeComponent.getHttpSearchService();
  }

  ngOnInit() {
    if (this.groups !== null) {
      this.getAll();
    }
    this.httpSearchService.castSearchTermForPublic.subscribe((searchTerm) => {
      if (searchTerm !== "" && searchTerm !== undefined) {
        this.search(searchTerm);
      }
    });
  }

  getAll() {
    this.httpGroupService.getAll().subscribe((res: any) => {
      this.groups = res;
    });
  }

  search(searchTerm) {
    console.log("search in public");
    this.httpSearchService.search(searchTerm).subscribe((res: any) => {
      this.groups = res;
    });
  }
}
