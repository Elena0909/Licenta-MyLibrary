package com.example.myLibrary.mapper;

import com.example.myLibrary.model.FormatBook;
import com.example.myLibrary.model.Status;
import com.example.myLibrary.model.dto.StatusDTO;
import org.springframework.stereotype.Component;

@Component
public class StatusMapper {
    public StatusDTO convertToStatusDTO(Status status ){
        return new StatusDTO(status.getId(),status.getName());
    }
}
