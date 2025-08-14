package ru.burn221.gymlite.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.burn221.gymlite.model.Equipment;
import ru.burn221.gymlite.model.Zone;
import ru.burn221.gymlite.repository.EquipmentRepository;
import ru.burn221.gymlite.repository.ZoneRepository;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class EquipmentService {
    private final EquipmentRepository equipmentRepository;
    private final ZoneRepository zoneRepository;

    public Equipment createEquipment(Integer zoneId, String equipmentName, String description, BigDecimal price, boolean active) {
        Zone zone = zoneRepository.findById(zoneId).
                orElseThrow(() -> new RuntimeException("This zone doesn't exist"));
        if (equipmentRepository.existsByZone_IdAndEquipmentName(zoneId, equipmentName)) {
            throw new IllegalArgumentException("This equipment already exists!");
        }

        Equipment equipment = new Equipment();

        equipment.setZone(zone);
        equipment.setEquipmentName(equipmentName);
        equipment.setDescription(description);
        equipment.setPrice(price);
        equipment.setActive(active);

        return equipmentRepository.save(equipment);

    }

    public Equipment updateEquipment(Integer zoneId, String equipmentName, String description, BigDecimal price, boolean active) {
        Zone zone = zoneRepository.findById(zoneId).
                orElseThrow(() -> new RuntimeException("This zone doesn't exist"));
        Equipment equipment = new Equipment();
        equipment.setZone(zone);
        equipment.setEquipmentName(equipmentName);
        equipment.setDescription(description);
        equipment.setPrice(price);
        equipment.setActive(active);

        return equipmentRepository.save(equipment);
    }

    public Page<Equipment> getAllEquipment(Pageable pageable) {
        return equipmentRepository.findAll(pageable);
    }

    public Page<Equipment> getEquipmentByZoneId(Integer zoneId, Pageable pageable) {
        return equipmentRepository.findByZone_Id(zoneId, pageable);

    }

    public Equipment getActiveEquipmentById(Integer id){
        return equipmentRepository.findByIdAndActiveTrue(id)
                .orElseThrow(()->new RuntimeException("This zone doesn't exist"));
    }

    public Equipment getActiveEquipmentByZoneId(Integer zoneId ){

        return equipmentRepository.findByZone_IdAndActiveTrue(zoneId)
                .orElseThrow(()->new RuntimeException("This zone doesn't exist!"));

    }

    public Page<Equipment> getAllActiveEquipment( Pageable pageable){
        return equipmentRepository.findByActiveTrue(pageable);

    }

    public Equipment deactivateEquipment(Integer id){
        Equipment equipment= equipmentRepository.findById(id)
                .orElseThrow(()->new RuntimeException("This equipment doesn't exist"));
        equipment.setActive(false);

        return equipmentRepository.save(equipment);


    }

    public Equipment activateEquipment(Integer id){
        Equipment equipment= equipmentRepository.findById(id)
                .orElseThrow(()->new RuntimeException("This equipment doesn't exist"));
        equipment.setActive(true);

        return equipmentRepository.save(equipment);


    }
}
