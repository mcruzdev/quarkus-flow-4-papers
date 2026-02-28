import { ChangeDetectionStrategy, Component } from '@angular/core';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-success',
  imports: [RouterLink],
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div class="min-h-screen bg-[#F5F5F5] flex flex-col">
      <!-- Header -->
      <header class="bg-[#003366] shadow-md">
        <div class="max-w-4xl mx-auto px-4 py-5 flex items-center gap-3">
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
            <h1 class="text-white text-xl font-bold leading-tight">Call for Papers</h1>
            <p class="text-[#4285F4] text-sm">Submit your proposal</p>
          </div>
        </div>
      </header>

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

          <a
            routerLink="/"
            class="inline-flex items-center gap-2 bg-[#003366] hover:bg-[#004080] text-white font-semibold px-6 py-3 rounded-lg transition focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-[#003366]"
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
              <line x1="19" y1="12" x2="5" y2="12" />
              <polyline points="12 19 5 12 12 5" />
            </svg>
            Submit Another Proposal
          </a>
        </div>
      </main>
    </div>
  `,
})
export class SuccessComponent {}

// Made with Bob
