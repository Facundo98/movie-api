package com.movieapichallenge.app.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table (name = "characters")
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(exclude = "movieOrSeriesEnrolled")
public class Character implements Serializable{

    private static final long serialVersionUID = 8799656478674716638L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long characterId;

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

    @ManyToMany(mappedBy = "charactersEnrolled",
            cascade = {CascadeType.PERSIST,CascadeType.MERGE,CascadeType.REFRESH},
            fetch = FetchType.LAZY)
    private Set<MovieOrSerie> movieOrSeriesEnrolled = new HashSet<>();
}
