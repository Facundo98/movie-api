package com.movieapichallenge.app.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Set;

@Entity
@Table (name = "characters")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Character implements Serializable{

    private static final long serialVersionUID = 8799656478674716638L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String image;


    @Column(nullable = false)
    @Size(max = 100, message = "The name must contain a maximum of 30 characters")
    private String name;

    @Min(value = 1, message = "Must be equal or greater than 1")
    @Max(value = 5,message = "Must be equal or less than 5")
    private Integer age;

    @Min(value = 1, message = "Must be equal or greater than 1")
    @Max(value = 400,message = "Must be equal or less than 400")
    private Float weight;

    @Column(nullable = false)
    @Size(max = 300, message = "The history must contain a maximum of 300 characters")
    private String history;

    @ManyToMany(targetEntity = MovieOrSerie.class)
    private Set<MovieOrSerie> movieOrSerieSet;

}
