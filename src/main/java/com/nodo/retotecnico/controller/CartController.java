package com.nodo.retotecnico.controller;

import com.nodo.retotecnico.model.Cart;
import com.nodo.retotecnico.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    // Ver carrito
    @GetMapping("/{userId}")
    public Cart getCart(@PathVariable Integer userId) {
        return cartService.getCartByUser(userId);
    }

    // Agregar
    @PostMapping("/add")
    public Cart addToCart(@RequestParam Integer userId,
                          @RequestParam Integer expansionId) {
        return cartService.addToCart(userId, expansionId);
    }

    // Eliminar
    @PostMapping("/remove")
    public Cart removeFromCart(@RequestParam Integer userId,
                               @RequestParam Integer expansionId) {
        return cartService.removeFromCart(userId, expansionId);
    }

    // Vaciar
    @PostMapping("/clear/{userId}")
    public void clearCart(@PathVariable Integer userId) {
        cartService.clearCart(userId);
    }
}