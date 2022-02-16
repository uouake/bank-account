import {Component, OnInit} from '@angular/core';
import {HttpClient} from "@angular/common/http";

@Component({
  selector: 'app-account',
  templateUrl: './account.component.html',
  styleUrls: ['./account.component.scss']
})
export class AccountComponent implements OnInit {

  public myAccount: any;
  public myAccountStatement: any;

  public typeOperation: string = 'DEPOT';
  public operationAmount: number = 0;

  public myAccountUrl = "http://localhost:8082/account/1";


  constructor(private httpClient:HttpClient) { }

  ngOnInit(): void {
    this.onGetMyAccount()
  }

  onGetMyAccount() {
    this.httpClient.get(this.myAccountUrl)
      .subscribe(data => {
        this.myAccount = data
        this.onGetMyAccountStatement()
        })
  }

  onGetMyAccountStatement() {
    this.httpClient.get(this.myAccountUrl + "/statements")
      .subscribe(data => this.myAccountStatement = data)
  }

  makeOperation(typeOp:string, amount:number) {
    this.httpClient.post(this.myAccountUrl + "/operation?typeOperation=" + typeOp + "&amount=" + amount, {})
      .subscribe(data => this.onGetMyAccount())
  }

}
