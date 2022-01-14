package com.movieapichallenge.app.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.*;
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

    @Column(length = 100,nullable = false)
    private String image;


    @Column(nullable = false)
    @NotBlank(message = "The character name cannot be blank")
    @Size(max = 100, message = "The name must contain a maximum of 100 characters")
    private String name;

    @Min(value = 1, message = "Must be equal or greater than 1")
    @NotNull(message = "The character age cannot be null")
    @Max(value = 130,message = "Must be equal or less than 130")
    @Column(nullable = false)
    private Integer age;

    @Min(value = 1, message = "Must be equal or greater than 1")
    @NotNull(message = "The character weight cannot be null")
    @Max(value = 400,message = "Must be equal or less than 400")
    @Column(nullable = false)
    private Float weight;

    @Size(max = 300, message = "The history must contain a maximum of 300 characters")
    @NotBlank(message = "The character history cannot be blank")
    @Column(nullable = false)
    private String history;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "movieOrSerie_characters",
            joinColumns = @JoinColumn(name = "character_id",foreignKey=@ForeignKey(name = "fk_characterId")),
            inverseJoinColumns = @JoinColumn(name = "movieOrSerie_id",foreignKey=@ForeignKey(name = "fk_movieOrSerieId")))
    private List<MovieOrSerie> movieOrSerieList;

}
