package com.garyfrancodev.ExpenseManagerApplication.dtos.category;

import com.garyfrancodev.ExpenseManagerDomain.enums.CategoryType;

import java.util.UUID;

public class CategoryDto {
    public String categoryName;
    public String categoryIcon;
    public String categoryDescription;
    public CategoryType categoryType;
    public UUID userId;
    public UUID id;

    public CategoryDto() {}

    public CategoryDto(String categoryName, String categoryIcon, String categoryDescription, CategoryType categoryType, UUID userId, UUID id) {
        this.categoryName = categoryName;
        this.categoryIcon = categoryIcon;
        this.categoryDescription = categoryDescription;
        this.categoryType = categoryType;
        this.userId = userId;
        this.id = id;
    }

    public String getCategoryName() { return categoryName; }
    public void setCategoryName(String categoryName) { this.categoryName = categoryName; }
    public String getCategoryIcon() { return categoryIcon; }
    public void setCategoryIcon(String categoryIcon) { this.categoryIcon = categoryIcon; }
    public String getCategoryDescription() { return categoryDescription; }
    public void setCategoryDescription(String categoryDescription) { this.categoryDescription = categoryDescription; }
    public CategoryType getCategoryType() { return categoryType; }
    public void setCategoryType(CategoryType categoryType) { this.categoryType = categoryType; }
    public UUID getUserId() { return userId; }
    public void setUserId(UUID userId) { this.userId = userId; }
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
}
