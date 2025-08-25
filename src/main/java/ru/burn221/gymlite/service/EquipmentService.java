package ru.burn221.gymlite.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.burn221.gymlite.dto.equipment.EquipmentCreateRequest;
import ru.burn221.gymlite.dto.equipment.EquipmentResponse;
import ru.burn221.gymlite.dto.equipment.EquipmentUpdateRequest;
import ru.burn221.gymlite.exceptions.ConflictException;
import ru.burn221.gymlite.exceptions.NotFoundException;
import ru.burn221.gymlite.mapper.EquipmentMapper;
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
    private final EquipmentMapper equipmentMapper;


    @Transactional
    public EquipmentResponse createEquipment(EquipmentCreateRequest dto) {
        String normalizedName = dto.equipmentName().trim();
        Zone zone = zoneRepository.findById(dto.zoneId()).
                orElseThrow(() -> new NotFoundException("Zone with id " + dto.zoneId() + " not found"));
        if (equipmentRepository.existsByZone_IdAndEquipmentNameIgnoreCase(dto.zoneId(), normalizedName)) {
            throw new ConflictException("Equipment " + dto.equipmentName() + " already exists");
        }
        if (normalizedName.isBlank()) throw new IllegalArgumentException("Name can't be empty");

        if (dto.price() == null || dto.price().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Price must be >= 0");
        }


        Equipment equipment = equipmentMapper.toEntity(dto, zone);

        Equipment saved = equipmentRepository.save(equipment);
        return equipmentMapper.toResponse(saved);
    }

    @Transactional
    public EquipmentResponse updateEquipment(EquipmentUpdateRequest dto) {
        String normalizedName = dto.equipmentName().trim();

        Equipment equipment = equipmentRepository.findById(dto.equipmentId())
                .orElseThrow(() -> new NotFoundException("Equipment " + dto.equipmentId() + " not found"));
        Zone zone = zoneRepository.findById(dto.zoneId()).
                orElseThrow(() -> new NotFoundException("Zone with id " + dto.zoneId() + " not found"));

        if (equipmentRepository.existsByZone_IdAndEquipmentNameIgnoreCaseAndIdNot(dto.zoneId(), normalizedName, dto.equipmentId())) {
            throw new ConflictException("Equipment with this name already exists in the zone");
        }

        if (normalizedName.isBlank()) throw new IllegalArgumentException("Name can't be empty");

        if (dto.price() == null || dto.price().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Price must be >= 0");
        }


        equipmentMapper.update(equipment,
                dto, zone);

        Equipment saved= equipmentRepository.save(equipment);
        return equipmentMapper.toResponse(saved);
    }

    public Page<EquipmentResponse> getAllEquipment(Pageable pageable) {
        return equipmentRepository.findAll(pageable)
                .map(equipmentMapper::toResponse);
    }

    public Page<EquipmentResponse> getEquipmentByZoneId(Integer zoneId, Pageable pageable) {
        return equipmentRepository.findByZone_Id(zoneId, pageable)
                .map(equipmentMapper::toResponse);

    }

    public EquipmentResponse getActiveEquipmentById(Integer id) {
        Equipment equipment=equipmentRepository.findByIdAndActiveTrue(id)
                .orElseThrow(() -> new NotFoundException("Equipment with id  "+id+ " not found "));

        return equipmentMapper.toResponse(equipment);
    }

    public EquipmentResponse getEquipmentById(Integer id) {
        Equipment equipment=equipmentRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Equipment with id  "+id+ " not found "));

        return equipmentMapper.toResponse(equipment);
    }

    public Page<EquipmentResponse> getActiveEquipmentByZoneId(Integer zoneId, Pageable pageable) {

        return equipmentRepository.findByZone_IdAndActiveTrue(zoneId, pageable)
                .map(equipmentMapper::toResponse);


    }


    public Page<EquipmentResponse> getAllActiveEquipment(Pageable pageable) {
        return equipmentRepository.findByActiveTrue(pageable)
                .map(equipmentMapper::toResponse);

    }

    @Transactional
    public EquipmentResponse deactivateEquipment(Integer id) {
        Equipment equipment = equipmentRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Equipment with id  "+id+ " not found "));
        equipment.setActive(false);

        return equipmentMapper.toResponse(equipmentRepository.save(equipment));


    }

    @Transactional
    public EquipmentResponse activateEquipment(Integer id) {
        Equipment equipment = equipmentRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Equipment with id  "+id+ " not found "));
        equipment.setActive(true);

        return equipmentMapper.toResponse(equipmentRepository.save(equipment));


    }

    @Transactional
    public void deleteEquipment(Integer id) {
        if (bookingRepository.existsByEquipment_IdAndBookingStatus(id, BookingStatus.BOOKED)) {
            throw new ConflictException("Equipment with id "+id+" has active bookings");
        }
        equipmentRepository.deleteById(id);
    }
}
