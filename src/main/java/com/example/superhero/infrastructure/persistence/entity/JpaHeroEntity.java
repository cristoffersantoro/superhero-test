package com.example.superhero.infrastructure.persistence.entity;

import jakarta.persistence.*;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "herois")
public class JpaHeroEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 120)
    private String nome;

    @Column(name = "nome_heroi", nullable = false, length = 120)
    private String nomeHeroi;

    @Column(name = "data_nascimento")
    private Instant dataNascimento;

    @Column(nullable = false)
    private Float altura;

    @Column(nullable = false)
    private Float peso;

    @OneToMany(mappedBy = "heroi", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<JpaHeroSuperpowerEntity> associations = new HashSet<>();

    // getters/setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getNomeHeroi() { return nomeHeroi; }
    public void setNomeHeroi(String nomeHeroi) { this.nomeHeroi = nomeHeroi; }
    public Instant getDataNascimento() { return dataNascimento; }
    public void setDataNascimento(Instant dataNascimento) { this.dataNascimento = dataNascimento; }
    public Float getAltura() { return altura; }
    public void setAltura(Float altura) { this.altura = altura; }
    public Float getPeso() { return peso; }
    public void setPeso(Float peso) { this.peso = peso; }
    public Set<JpaHeroSuperpowerEntity> getAssociations() { return associations; }
    public void setAssociations(Set<JpaHeroSuperpowerEntity> associations) { this.associations = associations; }
}
