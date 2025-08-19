package ru.burn221.gymlite.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.burn221.gymlite.model.Zone;

import java.util.Optional;

@Repository
public interface ZoneRepository extends JpaRepository<Zone, Integer> {

    boolean existsByZoneNameIgnoreCase(String zoneName);





    Optional<Zone> findByZoneNameIgnoreCase(String zoneName);

    Optional<Zone> findByIdAndActiveTrue(Integer id);





    Page<Zone> findByActiveTrue(Pageable pageable);


}
