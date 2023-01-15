import { Component, OnInit } from "@angular/core";
import { AppComponent } from "../../app.component";
import { HttpAccountService } from "../../services/httpAccount.service";
import { HttpSerachService } from "src/app/services/http-serach.service";

@Component({
  selector: "app-home",
  templateUrl: "./home.component.html",
  styleUrls: ["./home.component.css"],
})
export class HomeComponent implements OnInit {
  isLogged: boolean;

  constructor(
    private httpAccount: HttpAccountService,
    private httpSearchService: HttpSerachService
  ) {}

  ngOnInit() {
    this.httpAccount.castLogInfo.subscribe((isLogged) => {
      this.isLogged = isLogged;
    });
  }

  getHttpSearchService() {
    return this.httpSearchService;
  }
}
