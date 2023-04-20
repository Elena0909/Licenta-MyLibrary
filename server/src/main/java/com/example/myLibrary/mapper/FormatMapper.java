package com.example.myLibrary.mapper;

import com.example.myLibrary.model.FormatBook;
import com.example.myLibrary.model.dto.FormatBookDTO;
import org.springframework.stereotype.Component;

@Component
public class FormatMapper {

    public FormatBookDTO convertToFormatDTO(FormatBook formatBook){
        return new FormatBookDTO(formatBook.getId(),formatBook.getName());
    }
}
