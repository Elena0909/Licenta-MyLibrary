package com.example.myLibrary.service;

import com.example.myLibrary.mapper.CategoryMapper;
import com.example.myLibrary.model.Category;
import com.example.myLibrary.model.dto.CategoryDTO;
import com.example.myLibrary.repository.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    public CategoryService(CategoryRepository genreRepository,CategoryMapper categoryMapper) {
        this.categoryRepository = genreRepository;
        this.categoryMapper = categoryMapper;
    }

    public List<CategoryDTO> getAll() {
        List<Category> categories = categoryRepository.findAll();
        List<CategoryDTO> categoryDTOS = new ArrayList<>();

        for (Category category : categories) {
            categoryDTOS.add(categoryMapper.convertToCategoryDTO(category));
        }
        return categoryDTOS;
    }
}
