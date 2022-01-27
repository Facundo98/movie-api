package com.movieapichallenge.app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CharacterInfo {

    private String name;
    private String image;
    private Integer age;
    private Float weight;
    private String history;
    private Set<Long> movieOrSeriesIds = new HashSet<>();
}
