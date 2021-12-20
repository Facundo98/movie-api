package com.movieapichallenge.app.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Date;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "movieOrSerie")
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

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "movieOrSerie_genre",
            joinColumns = @JoinColumn(name = "movieOrSerie_id",foreignKey=@ForeignKey(name = "fk_movieOrSerieId")),
            inverseJoinColumns = @JoinColumn(name = "genre_id",foreignKey=@ForeignKey(name  = "fk_genreId")))
    private List<Genre> genres;

    @ManyToMany(mappedBy = "movieOrSerieList")
    private List<Character> characters;


}
