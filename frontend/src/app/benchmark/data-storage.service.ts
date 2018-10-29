import { Injectable, OnInit } from '@angular/core';
import { Http, Response } from '@angular/http';
import 'rxjs/Rx';
import { Template } from '../model/template.model';
import { Project } from '../model/project.model';
import { Subject } from 'rxjs/Subject';
import { Operation } from '../model/operation.model';
import * as Stomp from 'stompjs';
import { ProcessInfo } from '../model/processInfo.model';
import { LibrariesToChoose } from '../model/libraries-to-choose.model';

const REST_URL = 'http://localhost:8080';
const WEBSOCKET_URL = 'ws://localhost:8080/socket/websocket';

@Injectable()
export class DataStorageService implements OnInit {
  showMessageProject = new Subject<Project>();
  showMessageProcessInfo = new Subject<ProcessInfo>();
  showMessage = new Subject<string>();
  librariesToChoose = new Subject<LibrariesToChoose>();

  ws: any;

  constructor(private http: Http) {}

  ngOnInit() {}

  createBenchmark(template: Template) {
    this.createWebSocketConnection();

    this.http
      .post(REST_URL + '/benchmark/create', template)
      .map((response: Response) => {
        const project: Project = response.json();
        project.operation = Operation.END_CREATE_PROJECT;
        return project;
      })
      .subscribe(
        (project: Project) => {
          this.showMessageProject.next(project);
          project.operation = Operation.START_COMPILE;
          this.showMessageProject.next(project);
          this.compileProject(project.id);
        },
        error => {
          console.error(error._body);
          this.showMessage.next(error._body);
          if (error.status === 409) {
            const result: LibrariesToChoose = JSON.parse(error._body);
            this.librariesToChoose.next(result);
          }
        },
      );
  }

  importLibraries(libraries) {
    this.http
      .post(REST_URL + '/benchmark/importLibraries', libraries)
      .map((response: Response) => {
        const project: Project = response.json();
        project.operation = Operation.END_IMPORT_LIBRARIES;
        return project;
      })
      .subscribe(
        (project: Project) => {
          this.showMessageProject.next(project);
          project.operation = Operation.START_COMPILE;
          this.showMessageProject.next(project);
          this.compileProject(project.id);
        },
        error => {
          console.error(error._body);
          this.showMessage.next(error._body);
        },
      );
  }

  compileProject(projectId: string) {
    this.http
      .post(REST_URL + '/benchmark/compile/' + projectId, null)
      .map((response: Response) => {
        const project: Project = response.json();
        project.operation = Operation.END_COMPILE;
        return project;
      })
      .subscribe(
        (project: Project) => {
          this.showMessageProject.next(project);
          project.operation = Operation.START_RUN;
          this.showMessageProject.next(project);

          this.runBenchmark(project.id);
        },
        error => {
          console.error(error._body);
          this.showMessage.next(error._body);
        },
      );
  }

  runBenchmark(projectId: string) {
    this.ws.send('/app/benchmark/run', {}, projectId);
  }

  createWebSocketConnection() {
    const token = 'eyJlbWFpbCI6InRlc3RAdGVzdC5jeiIsInBhc3N3b3JkIjoiJDJhJDEwJExjbENTVUpzQzJ1QlIvd3hhS1EzZXVDaGxLLnppSC5iQXc2MDRlSXY4Y0RVa1o5UGsxY3dPIiwiZXhwaXJlcyI6MTU0MTU3ODU2MzIxOCwicm9sZXMiOlt7ImlkIjoxLCJ0eXBlIjoiVVNFUiIsIm5ldyI6ZmFsc2V9XSwiZmlyc3ROYW1lIjoiVGVzdCIsImxhc3ROYW1lIjoiVGVzdG92aWMifQ==.ITrzSJ+rcqfuTMKZlk4sqAA/RQVomLEo3Ycn4Nk6qUU=';
    const socket = new WebSocket(WEBSOCKET_URL);
    this.ws = Stomp.over(socket);
    const that = this;

    const headers = {
            Authorization : 'Bearer ' + token,
        };

    that.ws.connect(
      headers,
      // {},
      function(frame) {
        that.ws.subscribe('/errors', function(message) {
          alert('Error ' + message.body);
        });
        that.ws.subscribe('/user/benchmark/result/step', function(info) {
          console.log(info.body);
          that.showMessage.next(info.body);
        });
        that.ws.subscribe('/user/benchmark/result', function(info) {
          that.showMessage.next(info.body);
          // that.disconnect();
        });
      },
      function(error) {
        debugger;
        alert('STOMP error ' + error + ', headers = ' + error.headers);
      },
    );
  }

  disconnect() {
    if (this.ws != null) {
      this.ws.close();
    }
    console.log('Disconnected');
  }
}
