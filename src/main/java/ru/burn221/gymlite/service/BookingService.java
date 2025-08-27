package ru.burn221.gymlite.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.burn221.gymlite.dto.booking.BookingCreateRequest;
import ru.burn221.gymlite.dto.booking.BookingResponse;
import ru.burn221.gymlite.dto.booking.BookingUpdateRequest;
import ru.burn221.gymlite.model.BookingStatus;

import java.awt.print.Book;
import java.time.LocalDateTime;
import java.util.List;

public interface BookingService {

    BookingResponse createBooking(BookingCreateRequest dto);

    BookingResponse updateBooking(BookingUpdateRequest dto);

    BookingResponse cancelBooking(Integer bookingId);

    BookingResponse completeBooking(Integer bookingId);

    void deleteBooking(Integer bookingId);

    void deleteAllByStatus(BookingStatus bookingStatus);

    Page<BookingResponse> getBookingByUser(String username, Pageable pageable);

    List<BookingResponse> getBookingByEquipmentIdAndTime(Integer equipmentId, LocalDateTime startTime, LocalDateTime endTime, BookingStatus bookingStatus);

    Page<BookingResponse> getBookingByEquipmentId(Integer equipmentId,Pageable pageable);

    Page<BookingResponse> getAll(Pageable pageable);
}
