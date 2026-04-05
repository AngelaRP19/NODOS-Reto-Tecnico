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

    @Autowired
    private PlatformsRepository platformsRepository;

    @Override
    public Cart getCartByUser(Integer userId) {
        return cartRepository.findByUserIdAndStatus(userId, "activo")
                .orElseGet(() -> {
                    User user = userRepository.findById(userId).orElseThrow();
                    Cart newCart = new Cart();
                    newCart.setUser(user);
                    newCart.setStatus("activo");
                    newCart.setTotal(0.0);
                    return cartRepository.save(newCart);
                });
    }

    @Override
    public Cart addToCart(Integer userId, Integer expansionId, Integer platformId) {
        Cart cart = getCartByUser(userId);

        ExpansionPack expansion = expansionRepository.findById(expansionId)
                .orElseThrow(() -> new RuntimeException("Expansion not found"));
        Platform platform = platformsRepository.findById(platformId)
                .orElseThrow(() -> new RuntimeException("Platform not found"));

        cart.addExpansion(expansion, platform); // recalcula dentro de Cart
        return cartRepository.save(cart);
    }

    @Override
    public Cart removeFromCart(Integer userId, Integer expansionId) {
        Cart cart = getCartByUser(userId);

        ExpansionPack expansion = expansionRepository.findById(expansionId)
                .orElseThrow();

        cart.removeExpansion(expansion); // recalcula dentro de Cart
        return cartRepository.save(cart);
    }

    @Override
    public void clearCart(Integer userId) {
        Cart cart = getCartByUser(userId);
        cart.clearCart(); // limpia y pone total en 0
        cartRepository.save(cart);
    }
}
