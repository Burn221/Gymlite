package ru.burn221.gymlite.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.burn221.gymlite.model.Equipment;

import java.util.Optional;

@Repository
public interface EquipmentRepository extends JpaRepository<Equipment,Integer> {

    boolean existsByZone_IdAndEquipmentNameIgnoreCase(Integer zoneId, String equipmentName);

    boolean existsByZone_Id(Integer zoneId);

    boolean existsByZone_IdAndEquipmentNameIgnoreCaseAndIdNot(Integer zoneId, String equipmentName, Integer id);



    Page<Equipment> findByZone_Id(Integer zoneId, Pageable pageable);

    Page<Equipment> findByZone_IdAndActiveTrue(Integer zoneId, Pageable pageable);

    Optional<Equipment> findByIdAndActiveTrue(Integer id);

    Page<Equipment> findByActiveTrue(Pageable pageable);



}
