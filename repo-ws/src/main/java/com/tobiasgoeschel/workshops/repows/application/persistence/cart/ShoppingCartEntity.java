package com.tobiasgoeschel.workshops.repows.application.persistence.cart;

import com.tobiasgoeschel.workshops.repows.domain.ShoppingCart;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Entity()
@Getter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(exclude = "items")
public class ShoppingCartEntity {
  @Id
  @Column( length = 36, nullable = false, unique = true )
  private UUID id;

  @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
  private List<ShoppingCartItemEntity> items ;

  public static ShoppingCartEntity fromCart( ShoppingCart cart ) {
    final List<ShoppingCartItemEntity> items = cart.getItems().stream()
                                                   .map( item -> new ShoppingCartItemEntity( item.getId(),
                                                                                             item.getLabel(),
                                                                                             item.getPrice()
                                                                                                 .toString() ) )
                                                   .toList();
    return new ShoppingCartEntity( cart.getId(), items );
  }
}
