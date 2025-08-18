package ru.burn221.gymlite.mapper;


import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;
import ru.burn221.gymlite.dto.zone.ZoneCreateRequest;
import ru.burn221.gymlite.dto.zone.ZoneResponse;
import ru.burn221.gymlite.dto.zone.ZoneUpdateRequest;
import ru.burn221.gymlite.model.Zone;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ZoneMapper {

    @Mapping(target = "id",ignore = true)
    Zone toEntity(ZoneCreateRequest dto);

    void update(@MappingTarget Zone target, ZoneUpdateRequest dto);

    ZoneResponse toResponse(Zone zone);

}
