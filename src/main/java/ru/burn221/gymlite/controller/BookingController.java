package ru.burn221.gymlite.controller;

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
import ru.burn221.gymlite.service.BookingService;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("/api/bookings")
public class BookingController {

    private final BookingService bookingService;

    @PostMapping
    public ResponseEntity<BookingResponse> create(@RequestBody @Valid BookingCreateRequest request) {
        BookingResponse created = bookingService.createBooking(request);

        return ResponseEntity.created(URI.create("/api/booking/" + created.id()))
                .body(created);
    }

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

        BookingResponse updated = bookingService.updateBooking(fixed);

        return ResponseEntity.ok(updated);
    }

    @PatchMapping("/{id}/cancel")
    public ResponseEntity<BookingResponse> cancel(@PathVariable Integer id) {
        return ResponseEntity.ok(bookingService.cancelBooking(id));
    }

    @PatchMapping("/{id}/complete")
    public ResponseEntity<BookingResponse> complete(@PathVariable Integer id) {
        return ResponseEntity.ok(bookingService.completeBooking(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        bookingService.deleteBooking(id);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{bookingStatus}/by-status")
    public ResponseEntity<Void> deleteByStatus(@PathVariable BookingStatus bookingStatus) {
        bookingService.deleteAllByStatus(bookingStatus);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/by-user/{userName}")
    public ResponseEntity<Page<BookingResponse>> getAllByUser(@PathVariable String userName,
                                                              @ParameterObject @PageableDefault(size = 20, sort = "id") Pageable pageable) {
        Page<BookingResponse> page = bookingService.getBookingByUser(userName, pageable);

        return ResponseEntity.ok(page);
    }

    @GetMapping("/by-equipment-and-time-period/{equipmentId}")
    public ResponseEntity<List<BookingResponse>> getAllByEquipmentAndTime(@PathVariable Integer equipmentId,
                                                                          @RequestParam("start")
                                                                          @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startTime,
                                                                          @RequestParam("end") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endTime) {
        List<BookingResponse> list = bookingService.getBookingByEquipmentIdAndTime(equipmentId, startTime, endTime);

        return ResponseEntity.ok(list);
    }

    @GetMapping("/by-equipment/{equipmentId}")
    public ResponseEntity<Page<BookingResponse>> getAllByEquipment(@PathVariable Integer equipmentId,
                                                                   @ParameterObject @PageableDefault(size = 20, sort = "id") Pageable pageable) {
        Page<BookingResponse> page = bookingService.getBookingByEquipmentId(equipmentId, pageable);

        return ResponseEntity.ok(page);
    }

}
