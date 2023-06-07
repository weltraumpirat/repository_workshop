package com.tobiasgoeschel.workshops.repows.persistence.order;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

import java.util.UUID;

@Entity
@Data
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
}
