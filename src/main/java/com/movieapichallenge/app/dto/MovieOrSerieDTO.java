package com.movieapichallenge.app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class MovieOrSerieDTO {

    private String tittle;
    private String image;
    private String creationDate;
}
