import { Routes } from '@angular/router';

export const routes: Routes = [
  {
    path: '',
    loadComponent: () =>
      import('./features/proposal-form/proposal-form.component').then(
        (m) => m.ProposalFormComponent
      ),
  },
  {
    path: 'proposals',
    loadComponent: () =>
      import('./features/proposals-list/proposals-list.component').then(
        (m) => m.ProposalsListComponent
      ),
  },
  {
    path: 'success',
    loadComponent: () =>
      import('./features/success/success.component').then((m) => m.SuccessComponent),
  },
  {
    path: '**',
    redirectTo: '',
  },
];

// Made with Bob
