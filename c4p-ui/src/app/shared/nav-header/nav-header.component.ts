import { ChangeDetectionStrategy, Component } from '@angular/core';
import { RouterLink, RouterLinkActive } from '@angular/router';

@Component({
  selector: 'app-nav-header',
  imports: [RouterLink, RouterLinkActive],
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <header class="bg-[#003366] shadow-md">
      <div class="max-w-5xl mx-auto px-4 py-4 flex items-center justify-between gap-4">
        <!-- Brand -->
        <div class="flex items-center gap-3">
          <svg
            xmlns="http://www.w3.org/2000/svg"
            viewBox="0 0 24 24"
            fill="none"
            stroke="#FFD700"
            stroke-width="2"
            stroke-linecap="round"
            stroke-linejoin="round"
            class="w-8 h-8 shrink-0"
            aria-hidden="true"
          >
            <path d="M12 2L2 7l10 5 10-5-10-5z" />
            <path d="M2 17l10 5 10-5" />
            <path d="M2 12l10 5 10-5" />
          </svg>
          <div>
            <span class="text-white text-lg font-bold leading-tight block">Call for Papers</span>
            <span class="text-[#4285F4] text-xs">Submit & track your proposals</span>
          </div>
        </div>

        <!-- Navigation -->
        <nav aria-label="Main navigation">
          <ul class="flex items-center gap-1 list-none m-0 p-0">
            <li>
              <a
                routerLink="/"
                routerLinkActive="bg-white/10 text-white"
                [routerLinkActiveOptions]="{ exact: true }"
                class="px-4 py-2 rounded-lg text-sm font-medium text-[#4285F4] hover:bg-white/10 hover:text-white transition focus:outline-none focus:ring-2 focus:ring-[#FFD700]"
              >
                Submit Proposal
              </a>
            </li>
            <li>
              <a
                routerLink="/proposals"
                routerLinkActive="bg-white/10 text-white"
                class="px-4 py-2 rounded-lg text-sm font-medium text-[#4285F4] hover:bg-white/10 hover:text-white transition focus:outline-none focus:ring-2 focus:ring-[#FFD700]"
              >
                All Proposals
              </a>
            </li>
          </ul>
        </nav>
      </div>
    </header>
  `,
})
export class NavHeaderComponent {}

// Made with Bob
