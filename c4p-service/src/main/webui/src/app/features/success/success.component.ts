import { ChangeDetectionStrategy, Component } from '@angular/core';
import { RouterLink } from '@angular/router';
import { NavHeaderComponent } from '../../shared/nav-header/nav-header.component';

@Component({
  selector: 'app-success',
  imports: [RouterLink, NavHeaderComponent],
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div class="min-h-screen bg-[#F5F5F5] flex flex-col">
      <app-nav-header />

      <!-- Main content -->
      <main
        class="flex-1 flex items-center justify-center px-4 py-16"
        id="main-content"
        aria-live="polite"
      >
        <div class="bg-white rounded-2xl shadow-sm border border-gray-200 max-w-lg w-full px-8 py-12 text-center">
          <!-- Success icon -->
          <div
            class="mx-auto mb-6 w-20 h-20 rounded-full bg-green-100 flex items-center justify-center"
            aria-hidden="true"
          >
            <svg
              xmlns="http://www.w3.org/2000/svg"
              class="w-10 h-10 text-green-600"
              viewBox="0 0 24 24"
              fill="none"
              stroke="currentColor"
              stroke-width="2.5"
              stroke-linecap="round"
              stroke-linejoin="round"
            >
              <polyline points="20 6 9 17 4 12" />
            </svg>
          </div>

          <h2 class="text-[#003366] text-2xl font-bold mb-3">Proposal Submitted!</h2>
          <p class="text-gray-600 mb-2">
            Thank you for submitting your proposal. We have received it and will review it shortly.
          </p>
          <p class="text-gray-500 text-sm mb-8">
            You will be contacted via email once a decision has been made.
          </p>

          <!-- Divider -->
          <div class="border-t border-gray-100 mb-8"></div>

          <div class="flex flex-col sm:flex-row gap-3 justify-center">
            <a
              routerLink="/proposals"
              class="inline-flex items-center justify-center gap-2 bg-[#003366] hover:bg-[#004080] text-white font-semibold px-6 py-3 rounded-lg transition focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-[#003366]"
            >
              <svg
                xmlns="http://www.w3.org/2000/svg"
                class="w-4 h-4"
                viewBox="0 0 24 24"
                fill="none"
                stroke="currentColor"
                stroke-width="2"
                stroke-linecap="round"
                stroke-linejoin="round"
                aria-hidden="true"
              >
                <path d="M14 2H6a2 2 0 0 0-2 2v16a2 2 0 0 0 2 2h12a2 2 0 0 0 2-2V8z" />
                <polyline points="14 2 14 8 20 8" />
              </svg>
              View All Proposals
            </a>
            <a
              routerLink="/"
              class="inline-flex items-center justify-center gap-2 border border-[#003366] text-[#003366] hover:bg-[#003366] hover:text-white font-semibold px-6 py-3 rounded-lg transition focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-[#003366]"
            >
              <svg
                xmlns="http://www.w3.org/2000/svg"
                class="w-4 h-4"
                viewBox="0 0 24 24"
                fill="none"
                stroke="currentColor"
                stroke-width="2"
                stroke-linecap="round"
                stroke-linejoin="round"
                aria-hidden="true"
              >
                <line x1="12" y1="5" x2="12" y2="19" />
                <line x1="5" y1="12" x2="19" y2="12" />
              </svg>
              Submit Another
            </a>
          </div>
        </div>
      </main>
    </div>
  `,
})
export class SuccessComponent {}

// Made with Bob
