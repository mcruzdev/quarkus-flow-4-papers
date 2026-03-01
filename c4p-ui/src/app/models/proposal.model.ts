export type ProposalStatus = 'PENDING' | 'ACCEPTED' | 'REJECTED';

export interface SpeakerDTO {
  name: string;
  title?: string;
  email: string;
  company?: string;
  bio?: string;
}

export interface ProposalDTO {
  id?: number;
  title: string;
  subject: string;
  description: string;
  status?: ProposalStatus;
  speaker: SpeakerDTO;
}

// Made with Bob
