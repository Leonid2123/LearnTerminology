package com.leokarelsky.learnterminology.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "termins")
public class Termin {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "meaning", nullable = false)
    private String meaning;

    @ManyToOne
    @JoinColumn(name = "collection_id")
    private TerminCollection collection;

    public Termin(String name, String meaning, TerminCollection collection) {
        this.name = name;
        this.meaning = meaning;
        this.collection = collection;
    }
}
