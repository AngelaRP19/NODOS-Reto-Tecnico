package com.nodo.retotecnico.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nodo.retotecnico.dto.BuyResponseDTO;
import com.nodo.retotecnico.dto.DirectBuyRequest;
import com.nodo.retotecnico.model.Buy;
import com.nodo.retotecnico.model.User;
import com.nodo.retotecnico.repository.UserRepository;
import com.nodo.retotecnico.service.BuysService;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/nodos/buys")
public class BuysController {

    @Autowired
    private BuysService buysService;
    
    @Autowired
    private UserRepository userRepository;


    private String getAuthenticatedUsername() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            throw new AccessDeniedException("Usuario no autenticado");
        }
        
        Object principal = auth.getPrincipal();
        if (principal instanceof OAuth2User) {
            OAuth2User oauth2User = (OAuth2User) principal;
            String email = oauth2User.getAttribute("email");
            return email != null ? email : oauth2User.getAttribute("name");
        } else if (principal instanceof UserDetails) {
            return ((UserDetails) principal).getUsername();
        } else {
            return principal.toString(); // fall back if simple string via Jwt
        }
    }

    private User getAuthenticatedUserEntity() {
        String username = getAuthenticatedUsername();
        User currentUser = userRepository.findByEmail(username);
        if (currentUser == null) {
            currentUser = userRepository.findByUsernameIgnoreCase(username);
        }
        if (currentUser == null) {
            throw new AccessDeniedException("Usuario no encontrado");
        }
        return currentUser;
    }

    private void validateUserOwnsBuy(Integer buyId) {
        Buy buy = buysService.getBuyById(buyId);
        if (buy == null) {
            throw new RuntimeException("Compra no encontrada");
        }
        
        User currentUser = getAuthenticatedUserEntity();
        
        // Validar que la compra pertenece al usuario autenticado
        if (!buy.getCart().getUser().getId().equals(currentUser.getId())) {
            throw new AccessDeniedException("No tienes permiso para acceder a esta compra");
        }
    }

    @GetMapping("/")
    public String hello() {
        return "Bienvenido a las compras";
    }

    // Obtiene todas las compras del usuario autenticado
    @GetMapping
    public List<BuyResponseDTO> getUserBuys() {
        User currentUser = getAuthenticatedUserEntity();
        return buysService.getBuysByUser(currentUser.getId()).stream()
            .map(BuyResponseDTO::fromBuy)
            .collect(Collectors.toList());
    }

    /**
     * Obtiene una compra específica del usuario
     */
    @GetMapping("/{id}")
    public BuyResponseDTO getBuyById(@PathVariable Integer id) {
        
        Buy buy = buysService.getBuyById(id);
        if (buy == null) {
            throw new RuntimeException("Compra no encontrada");
        }
        
        User currentUser = getAuthenticatedUserEntity();
        
        // Validar que la compra pertenece al usuario autenticado
        if (!buy.getCart().getUser().getId().equals(currentUser.getId())) {
            throw new AccessDeniedException("No tienes permiso para ver esta compra");
        }
        
        return BuyResponseDTO.fromBuy(buy);
    }
    
    @PostMapping("/add")
    public Integer createBuy(@RequestBody Buy buy) {
        User currentUser = getAuthenticatedUserEntity();
        // Here we could validate that the buy actually belongs to the currentUser's cart, etc...
        // For simplicity we let the service do it or just allow if cart matches. 
        if(buy.getCart() == null || buy.getCart().getUser().getId() != currentUser.getId()) {
            throw new AccessDeniedException("The cart doesn't belong to the current user.");
        }
        return buysService.createBuy(buy);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Buy> updateBuy(
            @PathVariable Integer id,
            @RequestBody Buy buy) {
        validateUserOwnsBuy(id);
        return ResponseEntity.ok(buysService.updateBuy(id, buy));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteBuy(@PathVariable Integer id) {
        validateUserOwnsBuy(id);
        buysService.deleteBuy(id);
        return ResponseEntity.ok("Buy deleted successfully");
    }

    @PostMapping("/direct")
    public BuyResponseDTO directBuy(@RequestBody DirectBuyRequest request) {
        User currentUser = getAuthenticatedUserEntity();
        Buy buy = buysService.createDirectBuy(
            currentUser.getId(), 
            request.getExpansionId(), 
            request.getPlatformId(),
            request.getPaymentMethod()
        );
        return BuyResponseDTO.fromBuy(buy);
    }

    @PostMapping("/purchase")
    public BuyResponseDTO purchaseFromCart(@RequestBody String paymentMethod) {
        User currentUser = getAuthenticatedUserEntity();
        Buy buy = buysService.purchaseFromCart(currentUser.getId(), paymentMethod);
        return BuyResponseDTO.fromBuy(buy);
    }
}
