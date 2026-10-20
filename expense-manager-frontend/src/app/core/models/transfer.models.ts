export interface Transfer {
  id: string;
  sourceAccountId: string;
  destinationAccountId: string;
  amount: number;
  description: string;
  date: string;
  userId: string;
}

export interface CreateTransferRequest {
  sourceAccountId: string;
  destinationAccountId: string;
  amount: number;
  description: string;
  date: string;
  userId: string;
}
