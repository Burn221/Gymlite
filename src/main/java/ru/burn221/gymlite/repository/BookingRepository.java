package ru.burn221.gymlite.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.burn221.gymlite.model.Booking;
import ru.burn221.gymlite.model.BookingStatus;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking,Integer> {
    Page<Booking> findByUsernameOrderByStartTimeDesc(String username, Pageable pageable);

    boolean existsByEquipment_IdAndBookingStatusIsNotAndStartTimeLessThanAndEndTimeGreaterThan(
            Integer id,
            BookingStatus bookingStatus,
            LocalDateTime startTime,
            LocalDateTime endTime
    );

    List<Booking> findAllByEquipment_IdAndStartTimeLessThanAndEndTimeGreaterThan(
            Integer equipmentId,
            LocalDateTime dayEnd,
            LocalDateTime dayStart
    );


    Page<Booking> findByEquipment_Id(Integer equipmentId, Pageable pageable);



}
