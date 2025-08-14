package ru.burn221.gymlite.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @ManyToOne
    @JoinColumn(name = "equipment_id",nullable = false)
    private Equipment equipment_id;
    @Column(nullable = false)
    private String userName;
    @Column(name = "start_time")
    private LocalDateTime startTime;
    @Column(name = "end_time")
    private LocalDateTime endTime;
    @Column(name = "final_price")
    private BigDecimal finalPrice;
    @Enumerated(EnumType.STRING)
    @Column(name= "booking_status")
    private BookingStatus.bookingStatus bookedStatus;
    private LocalDateTime createdAt;


}
