import { ChangeDetectionStrategy, Component, inject, OnInit, signal } from '@angular/core';
import { RouterLink } from '@angular/router';
import { ProposalDTO, ProposalStatus } from '../../models/proposal.model';
import { ProposalService } from '../../services/proposal.service';
import { NavHeaderComponent } from '../../shared/nav-header/nav-header.component';

@Component({
  selector: 'app-proposals-list',
  imports: [RouterLink, NavHeaderComponent],
  changeDetection: ChangeDetectionStrategy.OnPush,
  templateUrl: './proposals-list.component.html',
})
export class ProposalsListComponent implements OnInit {
  private readonly proposalService = inject(ProposalService);

  readonly proposals = signal<ProposalDTO[]>([]);
  readonly isLoading = signal(true);
  readonly loadError = signal<string | null>(null);

  ngOnInit(): void {
    this.proposalService.getAll().subscribe({
      next: (data) => {
        this.proposals.set(data);
        this.isLoading.set(false);
      },
      error: () => {
        this.loadError.set('Failed to load proposals. Please try again later.');
        this.isLoading.set(false);
      },
    });
  }

  statusLabel(status: ProposalStatus | undefined): string {
    switch (status) {
      case 'ACCEPTED':
        return 'Accepted';
      case 'REJECTED':
        return 'Rejected';
      default:
        return 'Pending';
    }
  }

  statusClasses(status: ProposalStatus | undefined): string {
    switch (status) {
      case 'ACCEPTED':
        return 'bg-green-100 text-green-800 border border-green-200';
      case 'REJECTED':
        return 'bg-red-100 text-red-800 border border-red-200';
      default:
        return 'bg-yellow-100 text-yellow-800 border border-yellow-200';
    }
  }
}

// Made with Bob
