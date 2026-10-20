export interface Category {
  id: string;
  categoryName: string;
  categoryIcon: string;
  categoryDescription: string;
  categoryType: string; // e.g. "EXPENSE", "INCOME"
  userId: string;
}

export interface CreateCategoryRequest {
  categoryName: string;
  categoryIcon: string;
  categoryDescription: string;
  categoryType: string;
  userId: string;
}
