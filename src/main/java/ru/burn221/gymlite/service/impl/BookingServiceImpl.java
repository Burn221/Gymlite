package ru.burn221.gymlite.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.burn221.gymlite.dto.booking.BookingCreateRequest;
import ru.burn221.gymlite.dto.booking.BookingResponse;
import ru.burn221.gymlite.dto.booking.BookingUpdateRequest;
import ru.burn221.gymlite.exceptions.ConflictException;
import ru.burn221.gymlite.exceptions.NotFoundException;
import ru.burn221.gymlite.mapper.BookingMapper;
import ru.burn221.gymlite.model.Booking;
import ru.burn221.gymlite.model.BookingStatus;
import ru.burn221.gymlite.model.Equipment;
import ru.burn221.gymlite.repository.BookingRepository;
import ru.burn221.gymlite.repository.EquipmentRepository;
import ru.burn221.gymlite.service.BookingService;


import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final EquipmentRepository equipmentRepository;
    private final BookingMapper bookingMapper;

    @Transactional
    public BookingResponse createBooking(BookingCreateRequest dto) {

        String normalizedName= dto.userName();
        Equipment equipment = equipmentRepository.findById(dto.equipmentId())
                .orElseThrow(() -> new NotFoundException("Equipment with id " + dto.equipmentId() + " not found"));

        if (dto.endTime() == null || dto.startTime() == null || dto.startTime().isAfter(dto.endTime())) {
            throw new IllegalArgumentException("Start time can't be after end time");
        }

        if(normalizedName==null || normalizedName.isBlank()){
            throw new IllegalArgumentException("User name can't be blank");
        }

        if (bookingRepository.existsOverlappingBooking(dto.equipmentId(), dto.startTime(), dto.endTime())) {
            throw new ConflictException("This time is already taken");
        }

        if(bookingRepository.existsByEquipment_IdAndUserNameIsAndStartTimeIsAndEndTimeIs(dto.equipmentId(), normalizedName,dto.startTime(),dto.endTime())){
            throw new ConflictException("This booking already exists");
        }
        Booking booking= bookingMapper.toEntity(dto,equipment);
        booking.setFinalPrice(computeFinalPrice(equipment.getPrice(),dto.startTime(),dto.endTime()));
        booking.setBookingStatus(BookingStatus.BOOKED);

        Booking saved= bookingRepository.save(booking);
        return bookingMapper.toResponse(saved);


    }

    private BigDecimal computeFinalPrice(BigDecimal rate, LocalDateTime startTime, LocalDateTime endTime) {
        if (rate == null || rate.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Rate must be positive number");
        }

        long minutes = Duration.between(startTime, endTime).toMinutes();
        if (minutes <= 0) {
            throw new IllegalArgumentException("Minutes must be a positive number");
        }

        BigDecimal hours = BigDecimal.valueOf(minutes)
                .divide(BigDecimal.valueOf(60), 4, RoundingMode.HALF_UP);

        return rate.multiply(hours).setScale(2, RoundingMode.HALF_UP);
    }

    @Transactional
    public BookingResponse updateBooking(BookingUpdateRequest dto) {

        String normalizedName= dto.userName().trim();
        Booking booking = bookingRepository.findById(dto.bookingId())
                .orElseThrow(() -> new NotFoundException("Booking with id " + dto.bookingId() + " not found"));
        Equipment equipment = equipmentRepository.findById(dto.equipmentId())
                .orElseThrow(() -> new NotFoundException("Equipment with id " + dto.equipmentId() + " not found"));

        if (dto.endTime() == null || dto.startTime() == null || dto.startTime().isAfter(dto.endTime())) {
            throw new IllegalArgumentException("Start time can't be after end time");
        }

        if (bookingRepository.existsOverlappingBookingExcludingId(dto.bookingId(),dto.equipmentId(), dto.startTime(), dto.endTime())) {
            throw new ConflictException("This time is already taken");
        }

        if(bookingRepository.existsByEquipment_IdAndUserNameAndStartTimeAndEndTimeAndIdNot(dto.equipmentId(),normalizedName,dto.startTime(),dto.endTime(),dto.bookingId())){
            throw new ConflictException("This booking already exists");
        }

        bookingMapper.update(booking, dto, equipment);
        booking.setFinalPrice(computeFinalPrice(equipment.getPrice(),dto.startTime(),dto.endTime()));

        Booking saved= bookingRepository.save(booking);
        return bookingMapper.toResponse(saved);


    }

    @Transactional
    public BookingResponse cancelBooking(Integer bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException("Booking with id " + bookingId + " not found"));

        if (booking.getBookingStatus().equals(BookingStatus.CANCELED)) {
            throw new ConflictException("This booking is already canceled");
        }
        booking.setBookingStatus(BookingStatus.CANCELED);

        Booking saved= bookingRepository.save(booking);
        return bookingMapper.toResponse(saved);
    }

    @Transactional
    public BookingResponse completeBooking(Integer bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException("Booking with id " + bookingId + " not found"));

        if (booking.getBookingStatus().equals(BookingStatus.COMPLETED)) {
            throw new ConflictException("This booking is already completed");
        }
        booking.setBookingStatus(BookingStatus.COMPLETED);

        Booking saved= bookingRepository.save(booking);
        return bookingMapper.toResponse(saved);
    }

    @Transactional
    public void deleteBooking(Integer bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException("Booking with id " + bookingId + " not found"));

        if (booking.getBookingStatus().equals(BookingStatus.BOOKED)) {
            throw new ConflictException("This booking can't be deleted");

        }
        bookingRepository.deleteById(bookingId);
    }

    @Transactional
    public void deleteAllByStatus(BookingStatus bookingStatus){
        if (bookingStatus.equals(BookingStatus.BOOKED)){
            throw new ConflictException(
                    "This book can't be deleted");
        }
        bookingRepository.deleteByBookingStatus(bookingStatus);


    }

    public Page<BookingResponse> getBookingByUser(String username, Pageable pageable){
        return bookingRepository.findByUserNameOrderByStartTimeDesc(username,pageable)
                .map(bookingMapper::toResponse);
    }

    public List<BookingResponse> getBookingByEquipmentIdAndTime(Integer equipmentId, LocalDateTime startTime, LocalDateTime endTime ){
        return bookingRepository.findByEquipment_IdAndStartTimeLessThanAndEndTimeGreaterThan(equipmentId,endTime,startTime)
                .stream().map(bookingMapper::toResponse).collect(Collectors.toList());
    }

    public Page<BookingResponse> getBookingByEquipmentId(Integer equipmentId,Pageable pageable){
        return bookingRepository.findByEquipment_Id(equipmentId,pageable)
                .map(bookingMapper::toResponse);
    }

    public Page<BookingResponse> getAll(Pageable pageable){
        return bookingRepository.findAll(pageable)
                .map(bookingMapper::toResponse);
    }


}
