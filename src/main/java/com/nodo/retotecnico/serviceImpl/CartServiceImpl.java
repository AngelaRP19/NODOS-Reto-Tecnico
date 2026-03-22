package com.nodo.retotecnico.serviceImpl;

import com.nodo.retotecnico.model.*;
import com.nodo.retotecnico.repository.*;
import com.nodo.retotecnico.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ExpansionPacksRepository expansionRepository;

    @Override
    public Cart getCartByUser(Integer userId) {
        return cartRepository.findByUserId(userId)
                .orElseGet(() -> {
                    User user = userRepository.findById(userId).orElseThrow();
                    Cart newCart = new Cart(user);
                    return cartRepository.save(newCart);
                });
    }

    @Override
    public Cart addToCart(Integer userId, Integer expansionId) {

        Cart cart = getCartByUser(userId);

        ExpansionPack expansion = expansionRepository.findById(expansionId)
                .orElseThrow();

        cart.addExpansion(expansion);

        return cartRepository.save(cart);
    }

    @Override
    public Cart removeFromCart(Integer userId, Integer expansionId) {

        Cart cart = getCartByUser(userId);

        ExpansionPack expansion = expansionRepository.findById(expansionId)
                .orElseThrow();

        cart.removeExpansion(expansion);

        return cartRepository.save(cart);
    }

    @Override
    public void clearCart(Integer userId) {

        Cart cart = getCartByUser(userId);
        cart.clearCart();
        cartRepository.save(cart);
    }
}