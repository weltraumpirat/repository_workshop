package com.tobiasgoeschel.workshops.repows.cart;

import jakarta.persistence.*;
import lombok.*;

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
}
