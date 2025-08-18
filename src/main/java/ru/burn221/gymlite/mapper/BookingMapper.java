package ru.burn221.gymlite.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;
import ru.burn221.gymlite.dto.booking.BookingCreateRequest;
import ru.burn221.gymlite.dto.booking.BookingResponse;
import ru.burn221.gymlite.dto.booking.BookingUpdateRequest;
import ru.burn221.gymlite.model.Booking;
import ru.burn221.gymlite.model.Equipment;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface BookingMapper {
    @Mapping(target = "id",ignore = true)
    @Mapping(target = "equipment",source = "equipment")
    Booking toEntity(BookingCreateRequest dto, Equipment equipment);

    @Mapping(target = "equipment", source = "equipment")
    void update(@MappingTarget Booking target, BookingUpdateRequest dto, Equipment equipment);

    @Mapping(target = "equipmentId", source = "equipment.id")
    BookingResponse toResponse(Booking booking);


}
