package com.garyfrancodev.ExpenseManagerInfrastructure.repositories.category;

import com.garyfrancodev.ExpenseManagerDomain.enums.CategoryType;
import com.garyfrancodev.ExpenseManagerDomain.model.Category;
import com.garyfrancodev.ExpenseManagerDomain.repositories.CategoryRepository;
import com.garyfrancodev.ExpenseManagerInfrastructure.model.CategoryJpaModel;
import com.garyfrancodev.ExpenseManagerInfrastructure.model.UserJpaModel;
import com.garyfrancodev.ExpenseManagerInfrastructure.repositories.user.UserJpaRepository;
import com.garyfrancodev.ExpenseManagerSharedKernel.core.BusinessRuleValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public class CategoryRepositoryImpl implements CategoryRepository {

    @Autowired
    private CategoryJpaRepository categoryJpaRepository;
    @Autowired
    private UserJpaRepository userJpaRepository;

    @Override
    public UUID create(Category category) {
        CategoryJpaModel categoryJpaModel = new CategoryJpaModel();
        categoryJpaModel.setCategoryIcon(category.getCategoryIcon());
        categoryJpaModel.setCategoryName(category.getCategoryName() != null ? category.getCategoryName().getName() : "");
        categoryJpaModel.setCategoryDescription(category.getCategoryDescription());
        categoryJpaModel.setCategoryType(category.getCategoryType());
        categoryJpaModel.setId(category.getId());
        categoryJpaModel.setUser(userJpaRepository.findById(category.getUserId()).orElseThrow());
        return categoryJpaRepository.save(categoryJpaModel).getId();
    }

    @Override
    public List<Category> getCategoriesByUserId(UUID userId) {
        UserJpaModel userJpaModel = userJpaRepository.findById(userId).orElseThrow();
        List<CategoryJpaModel> categoryJpaList = categoryJpaRepository.getCategoryJpaModelsByUserId(userJpaModel);

        return categoryJpaList.stream().map(categoryJpa -> {
            try {
                return new Category(
                        categoryJpa.getCategoryIcon(),
                        categoryJpa.getCategoryName(),
                        categoryJpa.getCategoryDescription(),
                        categoryJpa.getCategoryType(),
                        categoryJpa.getUser() != null ? categoryJpa.getUser().getId() : null,
                        categoryJpa.getId()
                );
            } catch (BusinessRuleValidationException e) {
                throw new RuntimeException("Error mapping Category JPA to Domain model: " + e.getMessage(), e);
            }
        }).toList();
    }

    @Override
    public Category getCategoryDefaultByUserByCategoryType(UUID userId, String nameDefault) throws BusinessRuleValidationException {
        UserJpaModel userJpaModel = userJpaRepository.findById(userId).orElseThrow();
        CategoryJpaModel categoryJpa = categoryJpaRepository.getCategoryJpaModelsByUserIdByCategoryType(userJpaModel, nameDefault);
        if (categoryJpa == null) {
            return null;
        }
        return new Category(
                categoryJpa.getCategoryIcon(),
                categoryJpa.getCategoryName(),
                categoryJpa.getCategoryDescription(),
                categoryJpa.getCategoryType(),
                categoryJpa.getUser() != null ? categoryJpa.getUser().getId() : null,
                categoryJpa.getId()
        );
    }
}
