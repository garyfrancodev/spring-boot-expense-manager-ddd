export interface Transaction {
  id: string;
  accountId: string;
  categoryId: string;
  amount: number;
  transactionType: string; // "INCOME" or "EXPENSE"
  description: string;
  date: string;
  userId: string;
}

export interface CreateTransactionRequest {
  accountId: string;
  categoryId: string;
  amount: number;
  transactionType: string;
  description: string;
  date: string;
  userId: string;
}
