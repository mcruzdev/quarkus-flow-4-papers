export interface SpeakerDTO {
  name: string;
  email: string;
  company?: string;
  bio?: string;
}

export interface ProposalDTO {
  id?: number;
  title: string;
  subject: string;
  description: string;
  speaker: SpeakerDTO;
}

// Made with Bob
