package domain.model;

import java.util.Objects;

public class Superpower {
    private Long id;
    private String superpoder;
    private String descricao;

    public Superpower() {}

    public Superpower(Long id, String superpoder, String descricao) {
        this.id = id;
        this.superpoder = Objects.requireNonNull(superpoder);
        this.descricao = descricao;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getSuperpoder() { return superpoder; }
    public void setSuperpoder(String superpoder) { this.superpoder = superpoder; }
    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
}
