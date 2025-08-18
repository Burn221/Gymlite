package ru.burn221.gymlite.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.burn221.gymlite.model.BookingStatus;
import ru.burn221.gymlite.model.Equipment;
import ru.burn221.gymlite.model.Zone;
import ru.burn221.gymlite.repository.BookingRepository;
import ru.burn221.gymlite.repository.EquipmentRepository;
import ru.burn221.gymlite.repository.ZoneRepository;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class EquipmentService {
    private final EquipmentRepository equipmentRepository;
    private final ZoneRepository zoneRepository;
    private final BookingRepository bookingRepository;


    @Transactional
    public Equipment createEquipment(Integer zoneId, String equipmentName, String description, BigDecimal price, boolean active) {
        Zone zone = zoneRepository.findById(zoneId).
                orElseThrow(() -> new RuntimeException("Zone with id " + zoneId + " not found"));
        if (equipmentRepository.existsByZone_IdAndEquipmentNameIgnoreCase(zoneId, equipmentName)) {
            throw new IllegalArgumentException("Equipment " + equipmentName + " already exists");
        }
        if(equipmentName.isEmpty()) throw new IllegalArgumentException("Name can't be empty");

        if (price == null || price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Price must be >= 0");
        }



        Equipment equipment = new Equipment();

        equipment.setZone(zone);
        equipment.setEquipmentName(equipmentName);
        equipment.setDescription(description);
        equipment.setPrice(price);
        equipment.setActive(active);

        return equipmentRepository.save(equipment);

    }

    @Transactional
    public Equipment updateEquipment(Integer equipmentId, Integer zoneId, String equipmentName, String description, BigDecimal price, boolean active) {
        Equipment equipment = equipmentRepository.findById(equipmentId)
                .orElseThrow(() -> new RuntimeException("Equipment " + equipmentName + " not found or inactive"));
        Zone zone = zoneRepository.findById(zoneId).
                orElseThrow(() -> new RuntimeException("Zone with id " + zoneId + " not found"));

        if (equipmentRepository.existsByZone_IdAndEquipmentNameAndIdNot(zoneId, equipmentName, equipmentId)) {
            throw new IllegalArgumentException("Equipment with this name already exists in the zone");
        }

        if(equipmentName.isEmpty()) throw new IllegalArgumentException("Name can't be empty");

        if (price == null || price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Price must be >= 0");
        }


        equipment.setZone(zone);
        equipment.setEquipmentName(equipmentName.trim());
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

    public Equipment getActiveEquipmentById(Integer id) {
        return equipmentRepository.findByIdAndActiveTrue(id)
                .orElseThrow(() -> new RuntimeException("Equipment not found or inactive"));
    }

    public Page<Equipment> getActiveEquipmentByZoneId(Integer zoneId, Pageable pageable) {

        return equipmentRepository.findByZone_IdAndActiveTrue(zoneId,pageable);


    }


    public Page<Equipment> getAllActiveEquipment(Pageable pageable) {
        return equipmentRepository.findByActiveTrue(pageable);

    }

    @Transactional
    public Equipment deactivateEquipment(Integer id) {
        Equipment equipment = equipmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Equipment not found or inactive"));
        equipment.setActive(false);

        return equipmentRepository.save(equipment);


    }

    @Transactional
    public Equipment activateEquipment(Integer id) {
        Equipment equipment = equipmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Equipment not found or inactive"));
        equipment.setActive(true);

        return equipmentRepository.save(equipment);


    }

    @Transactional
    public void deleteEquipment(Integer id){
        if(bookingRepository.existsByEquipment_IdAndBookingStatus(id, BookingStatus.BOOKED)){
            throw new IllegalArgumentException("This equipment has active bookings");
        }
        equipmentRepository.deleteById(id);
    }
}
