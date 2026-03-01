import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { ProposalDTO } from '../models/proposal.model';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root',
})
export class ProposalService {
  private readonly http = inject(HttpClient);
  private readonly apiUrl = `${environment.baseUrl}/api/proposals`;

  submit(proposal: ProposalDTO): Observable<ProposalDTO> {
    return this.http.post<ProposalDTO>(this.apiUrl, proposal);
  }

  getAll(): Observable<ProposalDTO[]> {
    return this.http.get<ProposalDTO[]>(this.apiUrl);
  }
}

// Made with Bob
