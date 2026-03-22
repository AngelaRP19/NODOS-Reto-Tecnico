package com.nodo.retotecnico.service;

import com.nodo.retotecnico.model.Cart;

public interface CartService {

    Cart getCartByUser(Integer userId);

    Cart addToCart(Integer userId, Integer expansionId);

    Cart removeFromCart(Integer userId, Integer expansionId);

    void clearCart(Integer userId);
}