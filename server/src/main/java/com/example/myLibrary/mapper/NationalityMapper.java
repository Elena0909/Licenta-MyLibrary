package com.example.myLibrary.mapper;

import com.example.myLibrary.model.Nationality;
import com.example.myLibrary.model.dto.NationalityDTO;
import org.springframework.stereotype.Component;

@Component
public class NationalityMapper {

    public NationalityDTO convertToNationalityDTO(Nationality nationality){
        return new NationalityDTO(nationality.getId(), nationality.getName(),nationality.getLatitude(),nationality.getLongitude());
    }
}
