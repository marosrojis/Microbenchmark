import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { HttpModule } from '@angular/http';

import { AppComponent } from './app.component';
import { FormsModule } from '@angular/forms';
import { BenchmarkComponent } from './benchmark/benchmark.component';
import { DataStorageService } from './benchmark/data-storage.service';

@NgModule({
  declarations: [AppComponent, BenchmarkComponent],
  imports: [BrowserModule, FormsModule, HttpModule],
  providers: [DataStorageService],
  bootstrap: [AppComponent],
})
export class AppModule {}
