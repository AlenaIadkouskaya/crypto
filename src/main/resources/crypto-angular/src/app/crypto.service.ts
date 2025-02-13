import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { HttpErrorResponse } from '@angular/common/http';
import { Crypto } from './crypto';

@Injectable({
  providedIn: 'root'
})
export class CryptoService {
  private apiUrl = 'http://localhost:8099/api/cryptos';

  constructor(private http: HttpClient) { }

  getCrypties(filters: any): Observable<Crypto[]> {
    let params = new HttpParams();

    if (filters.symbol) {
      params = params.set('symbol', filters.symbol);
    }
    if (filters.minPrice !== null) {
      params = params.set('minPrice', filters.minPrice.toString());
    }
    if (filters.maxPrice !== null) {
      params = params.set('maxPrice', filters.maxPrice.toString());
    }
    
    return this.http.get<Crypto[]>(this.apiUrl, { params }).pipe(
      catchError(this.handleError)
    );
  }

  
  private handleError(error: HttpErrorResponse) {
    let errorMessage = 'An unknown error occurred!';

    if (error.error instanceof ErrorEvent) {
      errorMessage = `Client-side error: ${error.error.message}`;
    } else {
      if (error.status == 0) {
        errorMessage = 'Network error: Please check your internet connection.';
      } else if (error.status == 404) {
        errorMessage = 'API not found: The requested resource is not available.';
      } else if (error.status == 500) {
        errorMessage = 'Server error: Something went wrong on the server side.';
      } else {
        errorMessage = `Error ${error.status}: ${error.error}`;
      }
    }

    console.error('Error occurred:', errorMessage);
    
    return throwError(() => new Error(errorMessage));
  }
}
