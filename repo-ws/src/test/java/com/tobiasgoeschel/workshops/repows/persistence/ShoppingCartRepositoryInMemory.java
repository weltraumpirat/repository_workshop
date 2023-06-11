package com.tobiasgoeschel.workshops.repows.persistence;

import com.tobiasgoeschel.workshops.repows.domain.ShoppingCart;
import com.tobiasgoeschel.workshops.repows.domain.ShoppingCartRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class ShoppingCartRepositoryInMemory implements ShoppingCartRepository {
    private final Set<ShoppingCart> carts;

    public ShoppingCartRepositoryInMemory() {
        carts = new HashSet<>();
    }

    @Override
    public ShoppingCart findById(UUID id) {
        return carts.stream()
                .filter(cart -> cart.getId().equals(id))
                .findAny()
                .orElseThrow(() -> shoppingCartNotFoundResponse(id));
    }

    @Override
    public void add(ShoppingCart cart) {
        if (carts.contains(cart)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "A shopping cart with id " + cart.getId() + " already exists.");
        } else {
            carts.add(cart);
        }
    }

    @Override
    public void addAll(List<ShoppingCart> carts) {
       carts.forEach(this::add);
    }

    @Override
    public void remove(ShoppingCart cart) {
        if (!carts.contains(cart)) {
            throw shoppingCartNotFoundResponse(cart.getId());
        } else {
            carts.remove(cart);
        }
    }

    @Override
    public void removeAll(List<ShoppingCart> carts) {
      carts.forEach(this::remove);
    }

    @Override
    public void removeAll() {
        carts.clear();
    }

    @Override
    public List<ShoppingCart> findAll() {
        return carts.stream().toList();
    }

    private ResponseStatusException shoppingCartNotFoundResponse(final UUID cartId) {
        return new ResponseStatusException(HttpStatus.NOT_FOUND,
                "A shopping cart with id " + cartId + " does not exist.");
    }
}
