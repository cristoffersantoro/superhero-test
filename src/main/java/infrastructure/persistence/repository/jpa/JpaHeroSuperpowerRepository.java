package infrastructure.persistence.repository.jpa;

import infrastructure.persistence.entity.JpaHeroSuperpowerEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaHeroSuperpowerRepository extends JpaRepository<JpaHeroSuperpowerEntity, Long> {
    boolean existsByHeroi_IdAndSuperpoder_Id(Long heroiId, Long superpoderId);
    void deleteByHeroi_IdAndSuperpoder_Id(Long heroiId, Long superpoderId);
}