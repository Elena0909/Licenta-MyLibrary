package com.example.myLibrary.mapper;

import com.example.myLibrary.model.Category;
import com.example.myLibrary.model.dto.CategoryDTO;
import org.springframework.stereotype.Component;

@Component
public class CategoryMapper {

    public CategoryDTO convertToCategoryDTO(Category category){
        return new CategoryDTO(category.getId(), category.getName(), category.getPhotoURL());
    }

}
