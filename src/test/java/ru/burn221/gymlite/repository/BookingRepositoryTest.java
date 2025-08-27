package ru.burn221.gymlite.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import ru.burn221.gymlite.model.Booking;
import ru.burn221.gymlite.model.BookingStatus;
import ru.burn221.gymlite.model.Equipment;
import ru.burn221.gymlite.model.Zone;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
public class BookingRepositoryTest {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private TestEntityManager em;

    @Test
    @DisplayName("booking exists over lapping time should return true if exists ")
    void existsOverLapping_ShouldReturnTrue(){


        Zone zone = new Zone();
        zone.setZoneName("Cardio");
        zone.setDescription("desc");
        zone.setActive(true);
        em.persist(zone);


        Equipment eq = new Equipment();
        eq.setZone(zone);
        eq.setEquipmentName("Tgreg");
        eq.setDescription("sp");
        eq.setPrice(new BigDecimal("100"));
        eq.setActive(true);
        em.persist(eq);


        Booking b = new Booking();
        b.setEquipment(eq);
        b.setUserName("Nikita");
        b.setStartTime(LocalDateTime.parse("2025-08-26T13:00"));
        b.setEndTime(LocalDateTime.parse("2025-08-26T14:00"));
        b.setFinalPrice(new BigDecimal("100"));
        b.setBookingStatus(BookingStatus.BOOKED);


        em.persistAndFlush(b);

        boolean bl= bookingRepository.existsOverlappingBooking(1
                ,LocalDateTime.parse("2025-08-26T13:30"),
                LocalDateTime.parse("2025-08-26T14:00"));

        assertTrue(bl);


    }




}
