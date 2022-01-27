package com.movieapichallenge.app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MovieOrSeriesInfo {

    private String tittle;
    private String image;
    private String creationDate;
    private Integer score;
    private Set<Long> characterIds;
}
