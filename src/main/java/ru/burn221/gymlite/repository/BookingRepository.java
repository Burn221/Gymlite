package ru.burn221.gymlite.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.burn221.gymlite.model.Booking;
import ru.burn221.gymlite.model.BookingStatus;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking,Integer> {
    Page<Booking> findByUserNameOrderByStartTimeDesc(String username, Pageable pageable);

    boolean existsByEquipment_IdAndBookingStatusIsNotAndStartTimeLessThanAndEndTimeGreaterThan(
            Integer equipmentId,
            BookingStatus bookingStatus,
            LocalDateTime startTime,
            LocalDateTime endTime
    );

    boolean existsByEquipment_IdAndUserNameIsAndStartTimeIsAndEndTimeIs(
            Integer equipmentId,
            String userName,
            LocalDateTime startTime,
            LocalDateTime endtime
    );

    boolean existsByEquipment_IdAndBookingStatus(Integer equipmentId, BookingStatus bookingStatus);

    List<Booking> findByEquipment_IdAndStartTimeLessThanAndEndTimeGreaterThan(
            Integer equipmentId,
            LocalDateTime startTime,
            LocalDateTime endTime
    );


    Page<Booking> findByEquipment_Id(Integer equipmentId, Pageable pageable);

    @Query("""
    SELECT COUNT(b)>0
    FROM Booking b
    WHERE b.equipment.id= :equipmentId
    AND b.startTime< :endTime
    AND b.endTime > :startTime
          
    """)
    boolean existsOverlappingBooking(
            @Param("equipmentId") Integer equipmentId,
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime

    );

    void deleteByBookingStatus(BookingStatus bookingStatus);

    @Query("""
  SELECT COUNT(b) > 0
  FROM Booking b
  WHERE b.equipment.id = :equipmentId
    AND b.id <> :bookingId
    AND b.startTime < :endTime
    AND b.endTime   > :startTime
""")
    boolean existsOverlappingBookingExcludingId(
            @Param("bookingId") Integer bookingId,
            @Param("equipmentId") Integer equipmentId,
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime
    );

    boolean existsByEquipment_IdAndUserNameAndStartTimeAndEndTimeAndIdNot(
            Integer equipmentId, String userName, LocalDateTime startTime, LocalDateTime endTime, Integer id);




}
