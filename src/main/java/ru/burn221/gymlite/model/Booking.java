package ru.burn221.gymlite.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

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
    private Equipment equipment;
    @Column(nullable = false)
    private String userName;
    @Column(name = "start_time",nullable = false)
    private LocalDateTime startTime;
    @Column(name = "end_time",nullable = false)
    private LocalDateTime endTime;
    @Column(name = "final_price",precision = 10, scale = 2, nullable = false)
    private BigDecimal finalPrice;
    @Enumerated(EnumType.STRING)
    @Column(name= "booking_status",nullable = false)
    private BookingStatus bookingStatus;
    @CreationTimestamp
    @Column(nullable = false,updatable = false)
    private LocalDateTime createdAt;


}
