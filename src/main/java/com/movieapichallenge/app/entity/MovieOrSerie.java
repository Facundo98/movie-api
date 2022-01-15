package com.movieapichallenge.app.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.List;

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

    @Column(nullable = false)
    @NotBlank(message = "The tittle cannot be blank")
    @Size(max = 100, message = "The tittle must contain a maximum of 100 characters")
    private String tittle;

    @Column(length = 100,nullable = false)
    private String image;


    private String creationDate = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new java.util.Date());

    @Min(value = 1, message = "Must be equal or greater than 1")
    @NotNull(message = "The movie/serie score cannot be null")
    @Max(value = 5,message = "Must be equal or less than 5")
    @Column(nullable = false)
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
