package ru.burn221.gymlite.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.burn221.gymlite.dto.booking.BookingCreateRequest;
import ru.burn221.gymlite.dto.booking.BookingResponse;
import ru.burn221.gymlite.dto.booking.BookingUpdateRequest;
import ru.burn221.gymlite.model.BookingStatus;
import ru.burn221.gymlite.service.impl.BookingServiceImpl;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;

@Tag(name = "Bookings",description = "Gym booking control")
@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("/api/v1/bookings")
public class BookingController {

    private final BookingServiceImpl bookingServiceImpl;

    @Operation(summary = "Create booking")
    @PostMapping
    public ResponseEntity<BookingResponse> create(@RequestBody @Valid BookingCreateRequest request) {
        BookingResponse created = bookingServiceImpl.createBooking(request);

        return ResponseEntity.created(URI.create("/api/v1/booking/" + created.id()))
                .body(created);
    }

    @Operation(summary = "Update booking")
    @PutMapping("/{id}")
    public ResponseEntity<BookingResponse> update(@PathVariable Integer id,
                                                  @RequestBody @Valid BookingUpdateRequest request) {
        BookingUpdateRequest fixed = new BookingUpdateRequest(
                id,
                request.equipmentId(),
                request.userName(),
                request.startTime(),
                request.endTime(),
                request.bookingStatus()
        );

        BookingResponse updated = bookingServiceImpl.updateBooking(fixed);

        return ResponseEntity.ok(updated);
    }

    @Operation(summary = "Cancel booking")
    @PatchMapping("/cancel/{id}")
    public ResponseEntity<BookingResponse> cancel(@PathVariable Integer id) {
        return ResponseEntity.ok(bookingServiceImpl.cancelBooking(id));
    }

    @Operation(summary = "Complete booking")
    @PatchMapping("/complete/{id}")
    public ResponseEntity<BookingResponse> complete(@PathVariable Integer id) {
        return ResponseEntity.ok(bookingServiceImpl.completeBooking(id));
    }

    @Operation(summary = "delete booking")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        bookingServiceImpl.deleteBooking(id);

        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Delete all booking by status")
    @DeleteMapping("/by-status/{bookingStatus}")
    public ResponseEntity<Void> deleteByStatus(@PathVariable BookingStatus bookingStatus) {
        bookingServiceImpl.deleteAllByStatus(bookingStatus);

        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Get all bookings")
    @GetMapping
    public ResponseEntity<Page<BookingResponse>> getAll(
            @ParameterObject @PageableDefault(size = 20, sort = "id") Pageable pageable
    ) {


        return ResponseEntity.ok(bookingServiceImpl.getAll(pageable));
    }

    //todo
    @Operation(summary = "Get booking by username")
    @GetMapping("/by-user/{userName}")
    public ResponseEntity<Page<BookingResponse>> getAllByUser(@PathVariable String userName,
                                                              @ParameterObject @PageableDefault(size = 20, sort = "id") Pageable pageable) {
        Page<BookingResponse> page = bookingServiceImpl.getBookingByUser(userName, pageable);

        return ResponseEntity.ok(page);
    }

    @Operation(summary = "Get booking by equipment and time period", description = "example: api/v1/bookings/by-equipment-and-time-period/2?start=2025-08-20T11:00&end=2025-08-20T12:00")
    @GetMapping("/by-equipment-and-time-period/{equipmentId}")
    public ResponseEntity<List<BookingResponse>> getAllByEquipmentAndTime(@PathVariable Integer equipmentId,
                                                                          @RequestParam("start")
                                                                          @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startTime,
                                                                          @RequestParam("end") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endTime) {
        List<BookingResponse> list = bookingServiceImpl.getBookingByEquipmentIdAndTime(equipmentId, startTime, endTime, BookingStatus.CANCELED);

        return ResponseEntity.ok(list);
    }

    @Operation(summary = "Get booking by equipment Id")
    @GetMapping("/by-equipment/{equipmentId}")
    public ResponseEntity<Page<BookingResponse>> getAllByEquipment(@PathVariable Integer equipmentId,
                                                                   @ParameterObject @PageableDefault(size = 20, sort = "id") Pageable pageable) {
        Page<BookingResponse> page = bookingServiceImpl.getBookingByEquipmentId(equipmentId, pageable);

        return ResponseEntity.ok(page);
    }

}
