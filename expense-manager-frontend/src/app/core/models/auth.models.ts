export interface LoginRequest {
  email: string;
  password: string;
}

export interface RegisterRequest {
  firstName: string;
  lastName: string;
  email: string;
  password: string;
}

export interface AuthResponse {
  jwt: string;
  userId: string;
  email: string;
}

export interface UserProfile {
  id: string;
  firstName: string;
  lastName: string;
  email: string;
}
