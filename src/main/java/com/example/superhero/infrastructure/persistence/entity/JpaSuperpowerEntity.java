package com.example.superhero.infrastructure.persistence.entity;


import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "superpoderes")
public class JpaSuperpowerEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50, unique = true)
    private String superpoder;

    @Column(length = 250)
    private String descricao;

    @OneToMany(mappedBy = "superpoder", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<JpaHeroSuperpowerEntity> associations = new HashSet<>();

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getSuperpoder() { return superpoder; }
    public void setSuperpoder(String superpoder) { this.superpoder = superpoder; }
    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
    public Set<JpaHeroSuperpowerEntity> getAssociations() { return associations; }
    public void setAssociations(Set<JpaHeroSuperpowerEntity> associations) { this.associations = associations; }
}

