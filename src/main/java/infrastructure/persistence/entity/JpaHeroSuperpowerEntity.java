package infrastructure.persistence.entity;

import jakarta.persistence.*;

import java.time.Instant;

@Entity
@Table(
        name = "herois_superpoderes",
        uniqueConstraints = {
                @UniqueConstraint(name = "ux_heroi_superpoder", columnNames = {"heroi_id", "superpoder_id"})
        }
)
public class JpaHeroSuperpowerEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "heroi_id", nullable = false)
    private JpaHeroEntity heroi;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "superpoder_id", nullable = false)
    private JpaSuperpowerEntity superpoder;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public JpaHeroEntity getHeroi() { return heroi; }
    public void setHeroi(JpaHeroEntity heroi) { this.heroi = heroi; }

    public JpaSuperpowerEntity getSuperpoder() { return superpoder; }
    public void setSuperpoder(JpaSuperpowerEntity superpoder) { this.superpoder = superpoder; }

}
