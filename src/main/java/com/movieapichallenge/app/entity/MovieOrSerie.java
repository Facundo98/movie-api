package com.movieapichallenge.app.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Date;
import java.util.Set;

@Entity
@Table(name = "movies_or_series")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MovieOrSerie implements Serializable {

    private static final long serialVersionUID = 8799614478674716638L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 100, nullable = false)
    private String tittle;

    private String image;

    @Column(nullable = false)
    private Date creationDate;

    private Integer score;

    @ManyToMany(targetEntity = Character.class)
    private Set characterSet;


}
