package ru.burn221.gymlite.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.burn221.gymlite.model.Equipment;

import java.util.Optional;

@Repository
public interface EquipmentRepository extends JpaRepository<Equipment,Integer> {

    boolean existsByZone_IdAndEquipmentName(Integer zoneId, String equipmentName);

    boolean existByZone_Id(Integer zoneId);



    Page<Equipment> findByZone_Id(Integer zoneId, Pageable pageable);

    Optional<Equipment> findByZone_IdAndActiveTrue(Integer zoneId);

    Optional<Equipment> findByIdAndActiveTrue(Integer id);

    Page<Equipment> findByActiveTrue(Pageable pageable);



}
