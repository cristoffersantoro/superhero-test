package domain.model;

import java.util.Objects;

public class HeroSuperpower {
    private Hero hero;           // agregado dono
    private Long superpowerId;   // referencia por ID (sem objeto cadastral)

    public HeroSuperpower() {}

    public HeroSuperpower(Hero hero, Long superpowerId) {
        this.hero = Objects.requireNonNull(hero);
        this.superpowerId = Objects.requireNonNull(superpowerId);
    }

    public Hero getHero() { return hero; }
    public void setHero(Hero hero) { this.hero = hero; }

    public Long getSuperpowerId() { return superpowerId; }
    public void setSuperpowerId(Long superpowerId) { this.superpowerId = superpowerId; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof HeroSuperpower that)) return false;
        return Objects.equals(hero != null ? hero.getId() : null, that.hero != null ? that.hero.getId() : null)
                && Objects.equals(superpowerId, that.superpowerId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(hero != null ? hero.getId() : null, superpowerId);
    }
}
