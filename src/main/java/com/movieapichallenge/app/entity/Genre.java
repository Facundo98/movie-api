package com.movieapichallenge.app.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "genres")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Genre implements Serializable {

    private static final long serialVersionUID = 8799656478674716638L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 100,nullable = false)
    @NotBlank(message = "The genre name cannot be blank")
    @Size(max = 100, message = "The name must contain a maximum of 100 characters")
    private String name;

    @Column(length = 100,nullable = false)
    private String image;

    @ManyToMany(cascade = {CascadeType.PERSIST,CascadeType.REFRESH,CascadeType.MERGE},fetch = FetchType.LAZY)
    @JoinTable(
            name = "movieOrSerie_genre",
            joinColumns = @JoinColumn(name = "movieOrSerie_id",foreignKey=@ForeignKey(name = "fk_movieOrSerieId")),
            inverseJoinColumns = @JoinColumn(name = "genre_id",foreignKey=@ForeignKey(name  = "fk_genreId")))
    private Set<MovieOrSerie> movieOrSeriesEnrolled = new HashSet<>();
}
