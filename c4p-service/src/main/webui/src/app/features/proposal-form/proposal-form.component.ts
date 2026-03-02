import { ChangeDetectionStrategy, Component, inject, signal } from '@angular/core';
import { ReactiveFormsModule, FormBuilder, Validators, AbstractControl } from '@angular/forms';
import { Router } from '@angular/router';
import { ProposalService } from '../../services/proposal.service';
import { NavHeaderComponent } from '../../shared/nav-header/nav-header.component';

@Component({
  selector: 'app-proposal-form',
  imports: [ReactiveFormsModule, NavHeaderComponent],
  changeDetection: ChangeDetectionStrategy.OnPush,
  templateUrl: './proposal-form.component.html',
})
export class ProposalFormComponent {
  private readonly fb = inject(FormBuilder);
  private readonly proposalService = inject(ProposalService);
  private readonly router = inject(Router);

  readonly isSubmitting = signal(false);
  readonly submitError = signal<string | null>(null);

  readonly form = this.fb.group({
    title: [
      'Building Event-Driven Workflows with Quarkus, Kafka and AI',
      [Validators.required],
    ],
    subject: ['Java', [Validators.required]],
    description: [
      'Learn how to build event-driven workflows using Quarkus and Kafka. We will explore CloudEvents, fault tolerance, observability, and AI integration with LangChain4j to design resilient Java systems.',
      [Validators.required, Validators.minLength(100)],
    ],
    speaker: this.fb.group({
      name: ['Matheus Cruz', [Validators.required]],
      title: ['Sr. Software Engineer'],
      email: ['matheus.cruz@email.com', [Validators.required, Validators.email]],
      company: ['IBM'],
      bio: [`I am Matheus Cruz, Open Source Software Engineer (OSS 🥋)
I am a passionate learner, open-source enthusiast, and Java developer with a drive for continuous improvement. When I’m not coding, you can find me on the Brazilian Jiu Jitsu mats, playing the guitar, or exploring new challenges. I believe in the power of community, collaboration, and creativity—both in and outside of tech.`],
    }),
  });

  get titleControl(): AbstractControl {
    return this.form.get('title')!;
  }

  get subjectControl(): AbstractControl {
    return this.form.get('subject')!;
  }

  get descriptionControl(): AbstractControl {
    return this.form.get('description')!;
  }

  get speakerNameControl(): AbstractControl {
    return this.form.get('speaker.name')!;
  }

  get speakerTitleControl(): AbstractControl {
    return this.form.get('speaker.title')!;
  }

  get speakerEmailControl(): AbstractControl {
    return this.form.get('speaker.email')!;
  }

  get speakerCompanyControl(): AbstractControl {
    return this.form.get('speaker.company')!;
  }

  get speakerBioControl(): AbstractControl {
    return this.form.get('speaker.bio')!;
  }

  isInvalid(control: AbstractControl): boolean {
    return control.invalid && (control.dirty || control.touched);
  }

  onSubmit(): void {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }

    this.isSubmitting.set(true);
    this.submitError.set(null);

    const value = this.form.getRawValue();

    this.proposalService
      .submit({
        title: value.title!,
        subject: value.subject!,
        description: value.description!,
        speaker: {
          name: value.speaker!.name!,
          title: value.speaker!.title ?? undefined,
          email: value.speaker!.email!,
          company: value.speaker!.company ?? undefined,
          bio: value.speaker!.bio ?? undefined,
        },
      })
      .subscribe({
        next: () => {
          this.isSubmitting.set(false);
          this.router.navigate(['/success']);
        },
        error: () => {
          this.isSubmitting.set(false);
          this.submitError.set(
            'An error occurred while submitting your proposal. Please try again.'
          );
        },
      });
  }
}

// Made with Bob
