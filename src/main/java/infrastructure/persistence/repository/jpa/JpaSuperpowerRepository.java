package infrastructure.persistence.repository.jpa;

import infrastructure.persistence.entity.JpaSuperpowerEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaSuperpowerRepository extends JpaRepository<JpaSuperpowerEntity, Long> {
    boolean existsBySuperpoderIgnoreCase(String superpoder);
}
