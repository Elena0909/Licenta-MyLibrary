package com.example.myLibrary.mapper;

import com.example.myLibrary.model.Series;
import com.example.myLibrary.model.dto.SeriesDTO;
import org.springframework.stereotype.Component;

@Component
public class SeriesMapper {

    public SeriesDTO convertToSeriesDTO(Series series) {
        if (series != null)
            return new SeriesDTO(series.getId(), series.getName());
        return null;
    }

}
