package com.garyfrancodev.ExpenseManagerApi.controller;

import an.awesome.pipelinr.Pipeline;
import com.garyfrancodev.ExpenseManagerApplication.dtos.category.CategoryDto;
import com.garyfrancodev.ExpenseManagerApplication.dtos.category.CreateCategoryDto;
import com.garyfrancodev.ExpenseManagerApplication.use_cases.category.command.CreateCategoryCommand;
import com.garyfrancodev.ExpenseManagerApplication.use_cases.category.query.GetCategoriesQuery;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Tag(name = "Category Controller")
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/category")
public class CategoryController {

    @Autowired
    private Pipeline pipeline;

    @PostMapping()
    public ResponseEntity<UUID> createCategory(@RequestBody CreateCategoryDto createCategoryDto) {
        CreateCategoryCommand createCategoryCommand = new CreateCategoryCommand(createCategoryDto);
        UUID categoryId = createCategoryCommand.execute(pipeline);
        return ResponseEntity.ok(categoryId);
    }

    @GetMapping()
    public ResponseEntity<List<CategoryDto>> getCategoriesByUser(@Parameter(description = "User UUID") @RequestParam String userId) {
        GetCategoriesQuery getCategoriesQuery = new GetCategoriesQuery(UUID.fromString(userId));
        List<CategoryDto> categories = getCategoriesQuery.execute(pipeline);
        return ResponseEntity.ok(categories);
    }
}
