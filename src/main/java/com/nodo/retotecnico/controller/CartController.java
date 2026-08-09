package com.nodo.retotecnico.controller;

import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.nodo.retotecnico.dto.CartItemDTO;
import com.nodo.retotecnico.dto.CartResponseDTO;
import com.nodo.retotecnico.dto.ExpansionPackDTO;
import com.nodo.retotecnico.dto.ExpansionPackResponseDTO;
import com.nodo.retotecnico.dto.PlatformDTO;
import com.nodo.retotecnico.dto.UserDTO;
import com.nodo.retotecnico.model.Cart;
import com.nodo.retotecnico.model.User;
import com.nodo.retotecnico.repository.UserRepository;
import com.nodo.retotecnico.service.CartService;
import com.nodo.retotecnico.service.LocalizedContentService;

@RestController
@RequestMapping("/nodos/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LocalizedContentService localizedContentService;

    // Obtener usuario autenticado
    private User getAuthenticatedUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || !auth.isAuthenticated()) {
            throw new AccessDeniedException("User must be authenticated");
        }

        if (auth.getPrincipal() instanceof OAuth2User) {
            OAuth2User oauth2User = (OAuth2User) auth.getPrincipal();
            String email = (String) oauth2User.getAttributes().get("email");
            User user = userRepository.findByEmail(email);
            if (user == null) {
                throw new AccessDeniedException("User not found");
            }
            return user;
        }

        if (auth.getPrincipal() instanceof User) {
            return (User) auth.getPrincipal();
        }

        String username = auth.getName();
        User user = userRepository.findByUsername(username).orElse(null);
        if (user == null) {
            throw new AccessDeniedException("User not found");
        }
        return user;
    }

    // Convertir entidad Cart a DTO
    private CartResponseDTO convertToDTO(Cart cart, Locale locale) {
        User user = cart.getUser();
        UserDTO userDTO = new UserDTO(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getUsername(),
                user.getCountry());

        List<CartItemDTO> items = cart.getDetails() != null ? cart.getDetails().stream().map(detail -> {
            ExpansionPackResponseDTO localizedPack = localizedContentService.toResponseDto(detail.getExpansionPack(),
                    locale);
            ExpansionPackDTO packDTO = new ExpansionPackDTO(
                    localizedPack.getId(),
                    localizedPack.getName(),
                    localizedPack.getDescription(),
                    localizedPack.getPrice());
            PlatformDTO platformDTO = new PlatformDTO(
                    detail.getPlatform().getId(),
                    detail.getPlatform().getName());
            return new CartItemDTO(detail.getId(), packDTO, platformDTO);
        }).collect(Collectors.toList()) : List.of();

        return new CartResponseDTO(
                cart.getId(),
                cart.getStatus(),
                userDTO,
                items,
                cart.getTotal() // siempre devuelve el total actualizado
        );
    }

    // Ver carrito
    @GetMapping
    public CartResponseDTO getCart(Locale locale) {
        User authenticatedUser = getAuthenticatedUser();
        Cart cart = cartService.getCartByUser(authenticatedUser.getId());
        return convertToDTO(cart, locale);
    }

    // Agregar producto al carrito
    @PostMapping("/add")
    public CartResponseDTO addToCart(@RequestParam Integer expansionId, @RequestParam Integer platformId,
            Locale locale) {
        User authenticatedUser = getAuthenticatedUser();
        Cart cart = cartService.addToCart(authenticatedUser.getId(), expansionId, platformId);
        return convertToDTO(cart, locale);
    }

    // Remover producto del carrito
    @DeleteMapping("/remove")
    public CartResponseDTO removeFromCart(@RequestParam Integer expansionId, Locale locale) {
        User authenticatedUser = getAuthenticatedUser();
        Cart cart = cartService.removeFromCart(authenticatedUser.getId(), expansionId);
        return convertToDTO(cart, locale);
    }

    // Vaciar carrito
    @DeleteMapping("/clear")
    public void clearCart() {
        User authenticatedUser = getAuthenticatedUser();
        cartService.clearCart(authenticatedUser.getId());
    }
}
