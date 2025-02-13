import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { HttpClientModule } from '@angular/common/http';
import { AppComponent } from '../app.component';
import { CryptiesListComponent } from '../crypties-list/crypties-list.component';
import { CryptoService } from '../crypto.service';

@NgModule({
  declarations: [

  ],
  imports: [
    BrowserModule,
    HttpClientModule,
    AppComponent,
    CryptiesListComponent
  ],
  providers: [CryptoService],
  bootstrap: [AppComponent]
})
export class AppModule { }
