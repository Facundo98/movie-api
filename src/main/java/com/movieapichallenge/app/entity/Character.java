package com.movieapichallenge.app.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
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


    @Column(length = 100,nullable = false)
    private String name;

    @Column(nullable = false)
    private Integer age;

    @Column(nullable = false)
    private Float weight;

    private String history;

    @ManyToMany(targetEntity = MovieOrSerie.class)
    private Set movieOrSerieSet;

}
