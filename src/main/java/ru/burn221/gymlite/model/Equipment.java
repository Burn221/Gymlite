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
@Table(
        name = "equipment",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"zone_id", "equipment_name"})
        }

)
public class Equipment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @ManyToOne
    @JoinColumn(name = "zone_id", nullable = false)
    private Zone zone;
    @Column(name = "equipment_name", nullable = false)
    private String equipmentName;
    private String description;
    private BigDecimal price;
    @Column(nullable = false)
    private Boolean active;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;


}
