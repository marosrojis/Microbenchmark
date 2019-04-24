import { Injectable, OnInit } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import 'rxjs/Rx';
import { Template } from '../model/template.model';
import { Project } from '../model/project.model';
import { Subject } from 'rxjs/Subject';
import { Operation } from '../model/operation.model';
import * as Stomp from 'stompjs';
import { ProcessInfo } from '../model/processInfo.model';
import { LibrariesToChoose } from '../model/libraries-to-choose.model';
import { User } from '../model/user.model';
import { LoggedUser } from '../model/loggedUser.model';
import { AuthService } from '../auth/auth.service';

const REST_URL = 'http://147.228.63.36:8080/api';
const WEBSOCKET_URL = 'ws://147.228.63.36:8080/socket/websocket';

@Injectable()
export class DataStorageService implements OnInit {
  showMessageProject = new Subject<Project>();
  showMessageProcessInfo = new Subject<ProcessInfo>();
  showMessageResult = new Subject<any>();
  showMessage = new Subject<string>();
  librariesToChoose = new Subject<LibrariesToChoose>();

  loggedUser = new Subject<User>();

  ws: any;

  constructor(private http: HttpClient, private authService: AuthService) {}

  ngOnInit() {}

  createBenchmark(template: Template) {
    this.createWebSocketConnection();

    this.http.post(REST_URL + '/project/create', template).subscribe(
      (project: Project) => {
        project.operation = Operation.END_CREATE_PROJECT;
        this.showMessageProject.next(project);
        project.operation = Operation.START_COMPILE;
        this.showMessageProject.next(project);
        this.compileProject(project.id);
      },
      (error: HttpErrorResponse) => {
        this.showMessage.next(Operation.END_CREATE_PROJECT);
        this.showMessage.next(JSON.stringify(error.error));
        if (error.status === 409) {
          const result = new LibrariesToChoose(error.error.projectId, error.error.imports);
          this.librariesToChoose.next(result);
        }
      }
    );
  }

  importLibraries(libraries) {
    this.http.post(REST_URL + '/project/importLibraries', libraries).subscribe(
      (project: Project) => {
        project.operation = Operation.END_IMPORT_LIBRARIES;
        this.showMessageProject.next(project);
        project.operation = Operation.START_COMPILE;
        this.showMessageProject.next(project);
        this.compileProject(project.id);
      },
      (error: HttpErrorResponse) => {
        console.error(error);
        this.showMessage.next(error.error);
      }
    );
  }

  compileProject(projectId: string) {
    this.http.post(REST_URL + '/project/compile/' + projectId, null).subscribe(
      (project: Project) => {
        project.operation = Operation.END_COMPILE;
        this.showMessageProject.next(project);
        project.operation = Operation.START_RUN;
        this.showMessageProject.next(project);

        this.runBenchmark(project.id);
      },
      (error: HttpErrorResponse) => {
        console.error(error);
        this.showMessage.next(JSON.stringify(error.error));
      }
    );
  }

  runBenchmark(projectId: string) {
    this.ws.send('/app/benchmark/run', {}, projectId);
  }

  createWebSocketConnection() {
    const socket = new WebSocket(WEBSOCKET_URL);
    this.ws = Stomp.over(socket);
    const that = this;

    const token = this.authService.getToken();

    that.ws.connect(
      token === null ? {} : { Authorization: 'Bearer ' + token },
      function(frame) {
        that.ws.subscribe('/errors', function(message) {
          alert('Error ' + message.body);
        });
        that.ws.subscribe('/user/benchmark/result/step', info => {
          console.log(info.body);
          const object: ProcessInfo = JSON.parse(info.body);
          that.showMessageProcessInfo.next(object);
        });
        that.ws.subscribe('/user/benchmark/result', info => {
          const object = JSON.parse(info.body);
          that.showMessageResult.next(object);
        });
      },
      function(error) {
        alert('STOMP error ' + error + ', headers = ' + error.headers);
      }
    );
  }

  loginUser(user) {
    this.http.post(REST_URL + '/login', user).subscribe(
      (loggedUser: LoggedUser) => {
        localStorage.setItem('token', loggedUser.token);
        localStorage.setItem('user', JSON.stringify(loggedUser.user));
        this.loggedUser.next(loggedUser.user);
      },
      (error: HttpErrorResponse) => {
        console.error(error);
      }
    );
  }

  disconnect() {
    if (this.ws != null) {
      this.ws.close();
    }
    console.log('Disconnected');
  }
}
