package com.nodo.retotecnico.controller;

import com.nodo.retotecnico.model.Cart;
import com.nodo.retotecnico.model.User;
import com.nodo.retotecnico.repository.UserRepository;
import com.nodo.retotecnico.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/nodos/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @Autowired
    private UserRepository userRepository;

    /**
     * Validates that the authenticated user owns the cart being accessed.
     * Throws AccessDeniedException if the user is not authorized.
     */
    private void validateUserOwnsCart(Integer cartUserId, OAuth2User principal) {
        if (principal == null) {
            throw new AccessDeniedException("User must be authenticated");
        }
        
        String email = (String) principal.getAttributes().get("email");
        User user = userRepository.findByEmail(email);
        
        if (user == null || !user.getId().equals(cartUserId)) {
            throw new AccessDeniedException("User does not have permission to access this cart");
        }
    }

    // Ver carrito
    @GetMapping("/{userId}")
    public Cart getCart(@PathVariable Integer userId,
                       @org.springframework.security.core.annotation.AuthenticationPrincipal OAuth2User principal) {
        validateUserOwnsCart(userId, principal);
        return cartService.getCartByUser(userId);
    }

    // Agregar
    @PostMapping("/add")
    public Cart addToCart(@RequestParam Integer userId,
                          @RequestParam Integer expansionId,
                          @org.springframework.security.core.annotation.AuthenticationPrincipal OAuth2User principal) {
        validateUserOwnsCart(userId, principal);
        return cartService.addToCart(userId, expansionId);
    }

    // Eliminar
    @PostMapping("/remove")
    public Cart removeFromCart(@RequestParam Integer userId,
                               @RequestParam Integer expansionId,
                               @org.springframework.security.core.annotation.AuthenticationPrincipal OAuth2User principal) {
        validateUserOwnsCart(userId, principal);
        return cartService.removeFromCart(userId, expansionId);
    }

    // Vaciar
    @PostMapping("/clear/{userId}")
    public void clearCart(@PathVariable Integer userId,
                         @org.springframework.security.core.annotation.AuthenticationPrincipal OAuth2User principal) {
        validateUserOwnsCart(userId, principal);
        cartService.clearCart(userId);
    }
}