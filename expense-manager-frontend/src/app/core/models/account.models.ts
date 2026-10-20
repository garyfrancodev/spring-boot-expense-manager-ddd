export interface Account {
  id: string;
  accountName: string;
  description: string;
  amount: number;
  userId: string;
}

export interface CreateAccountRequest {
  accountName: string;
  description: string;
  amount: number;
  userId: string;
}
