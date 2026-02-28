import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { ProposalDTO } from '../models/proposal.model';

@Injectable({
  providedIn: 'root',
})
export class ProposalService {
  private readonly http = inject(HttpClient);
  private readonly apiUrl = '/api/proposals';

  submit(proposal: ProposalDTO): Observable<ProposalDTO> {
    return this.http.post<ProposalDTO>(this.apiUrl, proposal);
  }
}

// Made with Bob
