export interface AuthRequest {
  email: string;
  senha?: string; // Optional if you have other auth types, but mostly required
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
