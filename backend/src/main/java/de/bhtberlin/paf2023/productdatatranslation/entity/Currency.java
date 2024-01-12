package de.bhtberlin.paf2023.productdatatranslation.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class Currency {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;

    private String normalized;

    private String symbol;

    //  ISO 4217 currency code
    private String isoCode;

    @OneToMany(mappedBy = "currency", cascade = {CascadeType.PERSIST, CascadeType.REFRESH, CascadeType.MERGE})
    private Set<Language> languages = new HashSet<>();
}
