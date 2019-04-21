import { Component, OnInit, ViewChild } from '@angular/core';
import { DataStorageService } from './benchmark/data-storage.service';
import { Project } from './model/project.model';
import { Operation } from './model/operation.model';
import { ProcessInfo } from './model/processInfo.model';
import { NgForm } from '@angular/forms';
import { Template } from './model/template.model';
import { LibrariesToChoose } from './model/libraries-to-choose.model';
import { Result } from './model/result.model';

const templateDefault = new Template(
  '',
  2,
  3,
  'java.util.List<Integer> arrayList;\nint[] array;Random random;\njava.util.List<String> temp;',
  'random = new Random();\narray = new int[1000];\narrayList = new ArrayList<>();\nfor (int i = 0; i < 1000; i++) {\n\tint randomNumber = random.nextInt();\n\tarray[i] = randomNumber;\n\tarrayList.add(new Integer(randomNumber));\n}',
  ['Arrays.sort(array);\nblackhole.consume(array);', 'Collections.sort(arrayList);\nblackhole.consume(arrayList);']
);

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit {
  @ViewChild('f')
  slForm: NgForm;

  title = 'app';

  messages: string[] = [];
  showConversation = false;
  disabled = false;

  isNeedChooseImports = false;
  libraries: LibrariesToChoose;

  winnerFragment = -1;
  results = [];

  objectKeys = Object.keys;

  constructor(private dataStorage: DataStorageService) {}

  ngOnInit() {
    setTimeout(() => {
      this.slForm.setValue({
        name: '',
        warmup: templateDefault.warmup,
        measurement: templateDefault.measurement,
        declare: templateDefault.declare,
        init: templateDefault.init,
        testMethod1: templateDefault.testMethods[0],
        testMethod2: templateDefault.testMethods[1]
      });
    }, 1000);

    this.dataStorage.showMessageProject.subscribe((data: Project) => {
      this.showMessage('Operation: ' + data.operation + ', project id: ' + data.id);
      this.disabled = true;
    });
    this.dataStorage.showMessageProcessInfo.subscribe((data: ProcessInfo) => {
      this.showMessage(
        'Time: ' +
          data.time +
          ', estimated time: ' +
          data.estimatedEndTime +
          ', operation: ' +
          data.operation +
          ', iteration: ' +
          data.number +
          '/' +
          data.note
      );
    });
    this.dataStorage.showMessageResult.subscribe((data: Result) => {
      this.winnerFragment = data.bestScoreIndex;
      let result =
        '<strong>Time:</strong> ' +
        data.time +
        '<br><strong>Number of connection:</strong> ' +
        data.numberOfConnections;
      let i = 1;
      data.results.forEach(res => {
        this.results.push(
          '<strong>Time: ' +
            res.score +
            ' [' +
            res.unit +
            ']</strong><br>Error: &plusmn;' +
            res.error +
            '<br>Values: [' +
            res.measureValues.join(', ') +
            ']'
        );

        result +=
          '<br><strong>Benchmark ' +
          i +
          ':</strong> ' +
          res.score +
          ' [' +
          res.unit +
          '], error: ' +
          res.error +
          ', values: [' +
          res.measureValues.join(', ') +
          ']';
        i++;
      });
      this.showMessage(result);
    });

    this.dataStorage.librariesToChoose.subscribe((data: LibrariesToChoose) => {
      this.isNeedChooseImports = true;
      this.libraries = data;
    });
  }

  submitForm(form: NgForm) {
    const value = form.value;
    if (this.isNeedChooseImports) {
      this.showMessage('Operation: ' + Operation.START_IMPORT_LIBRARIES);
      const librariesForm = value.imports.split('\n');
      const result = {
        projectId: this.libraries.projectId,
        libraries: librariesForm
      };

      this.dataStorage.importLibraries(result);
    } else {
      this.messages = [];
      this.showMessage('Operation: ' + Operation.START_CREATE_PROJECT);

      const template = new Template(value.name, value.warmup, value.measurement, value.declare, value.init, [
        value.testMethod1,
        value.testMethod2
      ]);

      this.dataStorage.createBenchmark(template);
    }
  }

  showMessage(message) {
    this.showConversation = true;
    this.messages.push(message);
  }
}
