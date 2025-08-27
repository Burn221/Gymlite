package ru.burn221.gymlite.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import ru.burn221.gymlite.dto.booking.BookingCreateRequest;
import ru.burn221.gymlite.dto.booking.BookingResponse;
import ru.burn221.gymlite.dto.booking.BookingUpdateRequest;
import ru.burn221.gymlite.model.BookingStatus;
import ru.burn221.gymlite.repository.BookingRepository;
import ru.burn221.gymlite.service.BookingService;
import ru.burn221.gymlite.service.impl.BookingServiceImpl;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class BookingControllerTest {

    @Mock
    BookingServiceImpl service;

    @InjectMocks
    BookingController controller;

    @Test
    void createBooking_ReturnValidResponse(){
        var req= new BookingCreateRequest(1,"Nikita", LocalDateTime.parse("2025-08-26T13:00"),
                LocalDateTime.parse("2025-08-26T14:00"));

        var response= new BookingResponse(1,
                1,"Nikita",
                LocalDateTime.parse("2025-08-26T13:00" ),
                LocalDateTime.parse("2025-08-26T14:00"),
                BigDecimal.valueOf(100),
                BookingStatus.BOOKED,
                null);

        when(service.createBooking(req)).thenReturn(response);

        var entityResponse= controller.create(req);

        assertNotNull(entityResponse);

        assertEquals(HttpStatus.CREATED, entityResponse.getStatusCode());


        assertSame("Nikita", entityResponse.getBody().userName());

        verify(service).createBooking(req);


    }

    @Test
    void updateBooking_ReturnsValidResponse(){
        var req= new BookingUpdateRequest(1,1,"Nikita", LocalDateTime.parse("2025-08-26T13:00"),
                LocalDateTime.parse("2025-08-26T14:00"),
                BookingStatus.BOOKED);

        var response= new BookingResponse(1,
                1,"Nikita",
                LocalDateTime.parse("2025-08-26T13:00" ),
                LocalDateTime.parse("2025-08-26T14:00"),
                BigDecimal.valueOf(100),
                BookingStatus.BOOKED,
                null);

        when(service.updateBooking(req)).thenReturn(response);

        var entityResponse= controller.update(1,req);

        assertNotNull(entityResponse);

        assertEquals(HttpStatus.OK, entityResponse.getStatusCode());

        assertEquals(entityResponse.getBody().userName(), response.userName());

        verify(service).updateBooking(req);
    }

    @Test
    void getAllBookings_ReturnValidResponsePage(){
        var page= new PageImpl<>(List.of(
                new BookingResponse(1,
                        1,"Nikita",
                        LocalDateTime.parse("2025-08-26T13:00" ),
                        LocalDateTime.parse("2025-08-26T14:00"),
                        BigDecimal.valueOf(100),
                        BookingStatus.BOOKED,
                        null),
                new BookingResponse(2,
                        2,"Nikita",
                        LocalDateTime.parse("2025-08-26T13:00" ),
                        LocalDateTime.parse("2025-08-26T14:00"),
                        BigDecimal.valueOf(100),
                        BookingStatus.BOOKED,
                        null)
        ));

        when(service.getAll(Pageable.unpaged())).thenReturn(page);

        var entityResponse= controller.getAll(Pageable.unpaged());

        assertNotNull(entityResponse);


        assertEquals(HttpStatus.OK,entityResponse.getStatusCode());

        assertEquals(page,entityResponse.getBody());


    }



}
