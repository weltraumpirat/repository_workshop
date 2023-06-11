package com.tobiasgoeschel.workshops.repows.application.persistence.cart;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ShoppingCartCrudRepository extends CrudRepository<ShoppingCartEntity, UUID> {
}
