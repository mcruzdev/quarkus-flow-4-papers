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
