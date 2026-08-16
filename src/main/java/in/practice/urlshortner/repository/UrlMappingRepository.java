package in.practice.urlshortner.repository;

import in.practice.urlshortner.entity.UrlMapping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UrlMappingRepository extends JpaRepository<UrlMapping, Long> {

    boolean existsByShortURLCode(final String shortCode);

    Optional<UrlMapping> findByShortURLCode(final String shortCode);
}
