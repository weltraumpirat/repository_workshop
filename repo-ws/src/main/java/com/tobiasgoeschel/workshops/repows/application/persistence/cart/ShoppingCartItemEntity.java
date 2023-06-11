package com.tobiasgoeschel.workshops.repows.application.persistence.cart;

import lombok.AllArgsConstructor;
import lombok.Data;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Data
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class ShoppingCartItemEntity {
    @Id
    @Column( length = 36, nullable = false, unique = true )
    private UUID   id;

    private String label;

    @Column( length = 10 )
    private String price;
}
