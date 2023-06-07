package com.tobiasgoeschel.workshops.repows.order;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor
public class OrderEntity {
    @Id
    @Column( length = 36, nullable = false, unique = true )
    private UUID                      id;
    private String                    total;
    @Column( columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP" )
    private LocalDateTime             timestamp;
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<OrderPositionEntity> positions;

    public OrderEntity( final UUID id, final String total, final List<OrderPositionEntity> positions ) {
        this.id = id;
        this.total = total;
        this.positions = positions;
        this.timestamp = LocalDateTime.now();
    }
}
