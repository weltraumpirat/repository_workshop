package com.tobiasgoeschel.workshops.repows.cart;

import lombok.Data;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.EqualsAndHashCode;

import java.util.UUID;

@Entity
@Data
@EqualsAndHashCode
public class ShoppingCartItemEntity {
    @Id
    @Column( length = 36, nullable = false, unique = true )
    private UUID   id;

    private String label;

    @Column( length = 10 )
    private String price;
}
