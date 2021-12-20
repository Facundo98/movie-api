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
import java.util.List;

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

    @Column(length = 100)
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

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "movieOrSerie_characters",
            joinColumns = @JoinColumn(name = "character_id",foreignKey=@ForeignKey(name = "fk_characterId")),
            inverseJoinColumns = @JoinColumn(name = "movieOrSerie_id",foreignKey=@ForeignKey(name = "fk_movieOrSerieId")))
    private List<MovieOrSerie> movieOrSerieList;

}
