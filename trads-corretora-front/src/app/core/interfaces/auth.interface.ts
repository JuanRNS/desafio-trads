export interface AuthRequest {
  email: string;
  senha?: string;
}

export interface AuthResponse {
  token: string;
}

export interface DecodedToken {
  role?: string;
  sub?: string;
  exp?: number;
  [key: string]: any;
}
