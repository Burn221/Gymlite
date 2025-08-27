package ru.burn221.gymlite.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.burn221.gymlite.dto.equipment.EquipmentCreateRequest;
import ru.burn221.gymlite.dto.equipment.EquipmentResponse;
import ru.burn221.gymlite.dto.equipment.EquipmentUpdateRequest;

public interface EquipmentService {

    EquipmentResponse createEquipment(EquipmentCreateRequest dto);

    EquipmentResponse updateEquipment(EquipmentUpdateRequest dto);

    EquipmentResponse deactivateEquipment(Integer id);

    EquipmentResponse activateEquipment(Integer id);

    void deleteEquipment(Integer id);

    Page<EquipmentResponse> getAllEquipment(Pageable pageable);

    Page<EquipmentResponse> getEquipmentByZoneId(Integer zoneId, Pageable pageable);

    EquipmentResponse getActiveEquipmentById(Integer id);

    EquipmentResponse getEquipmentById(Integer id);

    Page<EquipmentResponse> getActiveEquipmentByZoneId(Integer zoneId, Pageable pageable);

    Page<EquipmentResponse> getAllActiveEquipment(Pageable pageable);
}
