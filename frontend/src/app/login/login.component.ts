import { Component, OnInit, ViewChild } from '@angular/core';
import { NgForm } from '@angular/forms';
import { DataStorageService } from '../benchmark/data-storage.service';
import { User } from '../model/user.model';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css'],
})
export class LoginComponent implements OnInit {
  @ViewChild('loginForm')
  loginForm: NgForm;

  loggedUser: User;

  constructor(private dataStorage: DataStorageService) {}

  ngOnInit() {
    this.dataStorage.loggedUser.subscribe((user: User) => {
      this.loggedUser = user;
    });
    const userStorage = JSON.parse(localStorage.getItem('user'));
    if (userStorage !== null) {
      this.loggedUser = userStorage;
    }
  }

  submitForm(form: NgForm) {
    const value = form.value;
    this.dataStorage.loginUser(value);
  }

  logoutUser() {
    localStorage.removeItem('user');
    localStorage.removeItem('token');
    this.loggedUser = null;
  }
}
