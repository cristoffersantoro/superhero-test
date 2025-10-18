package domain.model;

import java.time.Instant;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class Hero {
    private Long id;
    private String nome;
    private String nomeHeroi;
    private Instant dataNascimento;
    private Float altura;
    private Float peso;

    // Associação explícita com entidade de ligação (agregada aqui)
    private Set<HeroSuperpower> associations = new HashSet<>();

    public Hero() {}

    public Hero(Long id, String nome, String nomeHeroi, Instant dataNascimento, Float altura, Float peso) {
        this.id = id;
        this.nome = Objects.requireNonNull(nome);
        this.nomeHeroi = Objects.requireNonNull(nomeHeroi);
        this.dataNascimento = dataNascimento;
        this.altura = Objects.requireNonNull(altura);
        this.peso = Objects.requireNonNull(peso);
    }

    // Métodos de negócio operam SOMENTE sobre HeroSuperpower
    public void replaceAssociations(Set<HeroSuperpower> newAssociations) {
        this.associations.clear();
        if (newAssociations != null) {
            newAssociations.forEach(this::addAssociation);
        }
    }

    public void addAssociation(HeroSuperpower assoc) {
        if (assoc == null) return;
// garante consistência do lado agregador
        assoc.setHero(this);
        this.associations.add(assoc);
    }

    public void removeAssociationBySuperpowerId(Long superpowerId) {
        this.associations.removeIf(a -> a.getSuperpowerId() != null && a.getSuperpowerId().equals(superpowerId));
    }

    // getters/setters básicos
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

    public Set<HeroSuperpower> getAssociations() { return associations; }
    public void setAssociations(Set<HeroSuperpower> associations) { this.associations = associations; }
}
