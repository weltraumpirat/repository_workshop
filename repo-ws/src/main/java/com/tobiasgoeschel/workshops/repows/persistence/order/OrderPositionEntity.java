package com.tobiasgoeschel.workshops.repows.persistence.order;

import com.tobiasgoeschel.workshops.repows.domain.OrderPosition;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderPositionEntity {
    @Id
    @Column( length = 36, nullable = false, unique = true )
    private UUID   id;
    private String itemName;
    private int    count;
    @Column( length = 20 )
    private String singlePrice;
    @Column( length = 20 )
    private String combinedPrice;

    public static OrderPositionEntity fromOrderPosition( final OrderPosition orderPosition ) {
        return new OrderPositionEntity( orderPosition.getId(),
                                        orderPosition.getItemName(),
                                        orderPosition.getCount(),
                                        orderPosition.getSinglePrice().toString(),
                                        orderPosition.getCombinedPrice().toString() );
    }
}
