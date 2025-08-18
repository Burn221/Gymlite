package ru.burn221.gymlite.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;
import ru.burn221.gymlite.dto.equipment.EquipmentCreateRequest;
import ru.burn221.gymlite.dto.equipment.EquipmentResponse;
import ru.burn221.gymlite.dto.equipment.EquipmentUpdateRequest;
import ru.burn221.gymlite.model.Equipment;
import ru.burn221.gymlite.model.Zone;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface EquipmentMapper {
    @Mapping(target = "id",ignore = true)
    @Mapping(target = "zone",source = "zone")
    Equipment toEntity(EquipmentCreateRequest dto, Zone zone);

    @Mapping(target = "zone", source = "zone")
    void update(@MappingTarget Equipment target, EquipmentUpdateRequest dto, Zone zone);

    @Mapping(target = "zoneId", source = "zone.id")
    EquipmentResponse toResponse(Equipment equipment);


}
