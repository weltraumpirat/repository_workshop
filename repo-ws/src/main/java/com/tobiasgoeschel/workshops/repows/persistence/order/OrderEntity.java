package com.tobiasgoeschel.workshops.repows.persistence.order;

import com.tobiasgoeschel.workshops.repows.domain.Order;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class OrderEntity {
    @Id
    @Column( length = 36, nullable = false, unique = true )
    private UUID                      id;
    private String                    total;
    @Column( columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP" )
    private LocalDateTime             timestamp;
    @OneToMany( cascade = CascadeType.ALL, fetch = FetchType.EAGER )
    private List<OrderPositionEntity> positions;

    public OrderEntity( final UUID id, final String total, final List<OrderPositionEntity> positions ) {
        this.id = id;
        this.total = total;
        this.positions = positions;
        this.timestamp = LocalDateTime.now();
    }

    public static OrderEntity fromOrder( final Order order ) {
        return new OrderEntity(
            order.getId(),
            order.getTotal().toString(),
            order.getTimestamp(),
            order.getPositions().stream()
                .map( OrderPositionEntity::fromOrderPosition )
                .toList()
        );
    }
}
