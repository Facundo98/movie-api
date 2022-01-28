package com.movieapichallenge.app.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "movieOrSerie")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MovieOrSerie implements Serializable {

    private static final long serialVersionUID = 8799614478674716638L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long  movieOrSerieId;

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

    @ManyToMany(cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    @JoinTable(
            name = "movieOrSerie_characters",
            joinColumns = @JoinColumn(name = "character_id",foreignKey=@ForeignKey(name = "fk_characterId")),
            inverseJoinColumns = @JoinColumn(name = "movieOrSerie_id",foreignKey=@ForeignKey(name = "fk_movieOrSerieId")))
    private Set<Character> charactersEnrolled = new HashSet<>();


}
