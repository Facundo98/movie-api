package com.movieapichallenge.app.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Date;

@Entity
@Table(name = "series")
public class Serie implements Serializable {

    private static final long serialVersionUID = 8799656478674716838L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String image;

    @Column(nullable = false)
    private Date creationDate;

    private Integer score;
}
