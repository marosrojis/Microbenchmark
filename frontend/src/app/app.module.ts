import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { HttpClientModule } from '@angular/common/http';

import { AppComponent } from './app.component';
import { FormsModule } from '@angular/forms';
import { BenchmarkComponent } from './benchmark/benchmark.component';
import { DataStorageService } from './benchmark/data-storage.service';
import { LoginComponent } from './login/login.component';
import { AuthService } from './auth/auth.service';

import { HTTP_INTERCEPTORS } from '@angular/common/http';
import { TokenInterceptor } from './auth/token.interceptor';

@NgModule({
  declarations: [AppComponent, BenchmarkComponent, LoginComponent],
  imports: [BrowserModule, FormsModule, HttpClientModule],
  providers: [
    DataStorageService,
    AuthService,
    {
      provide: HTTP_INTERCEPTORS,
      useClass: TokenInterceptor,
      multi: true,
    },
  ],
  bootstrap: [AppComponent],
})
export class AppModule {}
