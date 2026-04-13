export interface IAuthRequest {
  email: string;
  senha?: string;
}

export interface IAuthResponse {
  token: string;
}

export interface IDecodedToken {
  role?: string;
  sub?: string;
  exp?: number;
  [key: string]: any;
}
