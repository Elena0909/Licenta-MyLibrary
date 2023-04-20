package com.example.myLibrary.service;

import com.example.myLibrary.mapper.FormatMapper;
import com.example.myLibrary.model.FormatBook;
import com.example.myLibrary.model.dto.FormatBookDTO;
import com.example.myLibrary.repository.FormatRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class FormatBookServer {
    private final FormatRepository formatRepository;
    private final FormatMapper formatMapper;

    public FormatBookServer(FormatRepository statusRepository, FormatMapper statusMapper) {
        this.formatRepository = statusRepository;
        this.formatMapper = statusMapper;
    }

    public List<FormatBookDTO> getAll(){
        List<FormatBookDTO> formatBookDTOS = new ArrayList<>();
        List<FormatBook> formatBooks = formatRepository.findAll();

        for (FormatBook formatBook:formatBooks
             ) {
            formatBookDTOS.add(formatMapper.convertToFormatDTO(formatBook));
        }
        return formatBookDTOS;
    }
}
