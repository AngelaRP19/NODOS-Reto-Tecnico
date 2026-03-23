package com.nodo.retotecnico.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nodo.retotecnico.model.Buy;
import com.nodo.retotecnico.model.User;
import com.nodo.retotecnico.repository.UserRepository;
import com.nodo.retotecnico.service.BuysService;

@RestController
@RequestMapping("/nodos/buys")
public class BuysController {

    @Autowired
    private BuysService buysService;
    
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/")
    public String hello() {
        return "Bienvenido a las compras";
    }

    // Obtiene todas las compras del usuario autenticado
    @GetMapping
    public List<Buy> getUserBuys(@AuthenticationPrincipal OAuth2User oauth2User) {
        if (oauth2User == null) {
            throw new AccessDeniedException("Usuario no autenticado");
        }
        String userEmail = oauth2User.getAttribute("email");
        if (userEmail == null) {
            userEmail = oauth2User.getAttribute("name");
        }
        User currentUser = userRepository.findByEmail(userEmail);
        if (currentUser == null) {
            currentUser = userRepository.findByUsernameIgnoreCase(userEmail);
        }
        if (currentUser == null) {
            throw new AccessDeniedException("Usuario no encontrado");
        }
        
        return buysService.getBuysByUser(currentUser.getId());
    }

    /**
     * Obtiene una compra específica del usuario autenticado
     * Valida que el usuario solo pueda ver sus propias compras
     * @param id ID de la compra
     * @param oauth2User Usuario autenticado
     * @return La compra solicitada
     */
    @GetMapping("/{id}")
    public Buy getBuyById(
            @PathVariable Integer id,
            @AuthenticationPrincipal OAuth2User oauth2User) {
        
        if (oauth2User == null) {
            throw new AccessDeniedException("Usuario no autenticado");
        }
        
        Buy buy = buysService.getBuyById(id);
        if (buy == null) {
            throw new RuntimeException("Compra no encontrada");
        }
        
        // Obtener email/username del usuario autenticado
        String userEmail = oauth2User.getAttribute("email");
        if (userEmail == null) {
            userEmail = oauth2User.getAttribute("name");
        }
        
        // Buscar usuario en la base de datos
        User currentUser = userRepository.findByEmail(userEmail);
        if (currentUser == null) {
            currentUser = userRepository.findByUsernameIgnoreCase(userEmail);
        }
        
        if (currentUser == null) {
            throw new AccessDeniedException("Usuario no encontrado");
        }
        
        // Validar que la compra pertenece al usuario autenticado
        if (!buy.getCart().getUser().getId().equals(currentUser.getId())) {
            throw new AccessDeniedException("No tienes permiso para ver esta compra");
        }
        
        return buy;
    }
    
    @PostMapping("/add")
    public Integer createBuy(@RequestBody Buy buy) {
        return buysService.createBuy(buy);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Buy> updateBuy(@PathVariable Integer id, @RequestBody Buy buy){
        return ResponseEntity.ok(buysService.updateBuy(id, buy));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteBuy(@PathVariable Integer id){
        buysService.deleteBuy(id);
        return ResponseEntity.ok("Buy deleted successfully");
    }
}
