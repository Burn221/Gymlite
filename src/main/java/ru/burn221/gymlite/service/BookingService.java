package ru.burn221.gymlite.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.burn221.gymlite.model.Booking;
import ru.burn221.gymlite.model.BookingStatus;
import ru.burn221.gymlite.model.Equipment;
import ru.burn221.gymlite.repository.BookingRepository;
import ru.burn221.gymlite.repository.EquipmentRepository;
import ru.burn221.gymlite.repository.ZoneRepository;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingService {
    private final BookingRepository bookingRepository;
    private final EquipmentRepository equipmentRepository;
    private final ZoneRepository zoneRepository;

    @Transactional
    public Booking createBooking(Integer equipmentId
            , String userName
            , LocalDateTime startTime
            , LocalDateTime endTime
            ) {

        Booking booking = new Booking();
        Equipment equipment = equipmentRepository.findById(equipmentId)
                .orElseThrow(() -> new RuntimeException("Equipment with id " + equipmentId + " not found"));

        if (endTime == null || startTime == null || startTime.isAfter(endTime)) {
            throw new IllegalArgumentException("Start time can't be after end time");
        }

        if(userName==null || userName.isBlank()){
            throw new IllegalArgumentException("User name can't be blank");
        }

        if (bookingRepository.existsOverlappingBooking(equipmentId, startTime, endTime)) {
            throw new RuntimeException("This time is already taken");
        }

        if(bookingRepository.existsByEquipment_IdAndUserNameIsAndStartTimeIsAndEndTimeIs(equipmentId,userName,startTime,endTime)){
            throw new IllegalArgumentException("This booking already exists");
        }
        booking.setEquipment(equipment);
        booking.setUserName(userName);
        booking.setStartTime(startTime);
        booking.setEndTime(endTime);
        booking.setBookingStatus(BookingStatus.BOOKED);
        booking.setFinalPrice(computeFinalPrice(equipment.getPrice(), startTime, endTime));

        return bookingRepository.save(booking);


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
    public Booking updateBooking(Integer bookingId
            , Integer equipmentId
            , String userName
            , LocalDateTime startTime
            , LocalDateTime endTime
            , BookingStatus bookingStatus) {

        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new IllegalArgumentException("Booking with id " + bookingId + " not found"));
        Equipment equipment = equipmentRepository.findById(equipmentId)
                .orElseThrow(() -> new RuntimeException("Equipment with id " + equipmentId + " not found"));

        if (endTime == null || startTime == null || startTime.isAfter(endTime)) {
            throw new IllegalArgumentException("Start time can't be after end time");
        }

        if (bookingRepository.existsOverlappingBookingExcludingId(bookingId,equipmentId, startTime, endTime)) {
            throw new RuntimeException("This time is already taken");
        }

        if(bookingRepository.existsByEquipment_IdAndUserNameAndStartTimeAndEndTimeAndIdNot(equipmentId,userName,startTime,endTime,bookingId)){
            throw new IllegalArgumentException("This booking already exists");
        }
        booking.setEquipment(equipment);
        booking.setUserName(userName);
        booking.setStartTime(startTime);
        booking.setEndTime(endTime);
        booking.setBookingStatus(bookingStatus);
        booking.setFinalPrice(computeFinalPrice(equipment.getPrice(), startTime, endTime));

        return bookingRepository.save(booking);


    }

    @Transactional
    public Booking cancelBooking(Integer bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new IllegalArgumentException("Booking with id " + bookingId + " not found"));

        if (booking.getBookingStatus().equals(BookingStatus.CANCELED)) {
            throw new IllegalArgumentException("This booking is already canceled");
        }
        booking.setBookingStatus(BookingStatus.CANCELED);
        return bookingRepository.save(booking);
    }

    @Transactional
    public Booking completeBooking(Integer bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new IllegalArgumentException("Booking with id " + bookingId + " not found"));

        if (booking.getBookingStatus().equals(BookingStatus.COMPLETED)) {
            throw new IllegalArgumentException("This booking is already completed");
        }
        booking.setBookingStatus(BookingStatus.COMPLETED);
        return bookingRepository.save(booking);
    }

    @Transactional
    public void deleteBooking(Integer bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new IllegalArgumentException("Booking with id " + bookingId + " not found"));

        if (booking.getBookingStatus().equals(BookingStatus.BOOKED)) {
            throw new IllegalArgumentException("This booking can't be deleted");

        }
        bookingRepository.deleteById(bookingId);
    }

    @Transactional
    public void deleteAllByStatus(BookingStatus bookingStatus){
        if (bookingStatus.equals(BookingStatus.BOOKED)){
            throw new IllegalArgumentException("This book can't be deleted");
        }
        bookingRepository.deleteByBookingStatus(bookingStatus);


    }

    public Page<Booking> getBookingByUser(String username, Pageable pageable){
        return bookingRepository.findByUserNameOrderByStartTimeDesc(username,pageable);
    }

    public List<Booking> getBookingByEquipmentIdAndTime(Integer equipmentId, LocalDateTime startTime, LocalDateTime endTime ){
        return bookingRepository.findByEquipment_IdAndStartTimeLessThanAndEndTimeGreaterThan(equipmentId,endTime,startTime);
    }

    public Page<Booking> getBookingByEquipmentId(Integer equipmentId,Pageable pageable){
        return bookingRepository.findByEquipment_Id(equipmentId,pageable);
    }


}
