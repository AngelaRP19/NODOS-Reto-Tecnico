package com.nodo.retotecnico.serviceImpl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nodo.retotecnico.model.Buy;
import com.nodo.retotecnico.model.Cart;
import com.nodo.retotecnico.model.CartDetails;
import com.nodo.retotecnico.model.ExpansionPack;
import com.nodo.retotecnico.model.Platform;
import com.nodo.retotecnico.model.User;
import com.nodo.retotecnico.repository.BuysRepository;
import com.nodo.retotecnico.repository.CartRepository;
import com.nodo.retotecnico.repository.ExpansionPacksRepository;
import com.nodo.retotecnico.repository.PlatformsRepository;
import com.nodo.retotecnico.repository.UserRepository;
import com.nodo.retotecnico.service.BuysService;

@Service
public class BuysServiceImpl implements BuysService{

    @Autowired
    private BuysRepository buysRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ExpansionPacksRepository expansionRepository;

    @Autowired
    private PlatformsRepository platformsRepository;

    @Autowired
    private CartRepository cartRepository;

    @Override
    public List<Buy> getAllBuys() { 
        return buysRepository.findAll();
    }

    @Override
    public Buy getBuyById(Integer id){
        return buysRepository.findById(id).orElse(null);
    }
    
    @Override
    public List<Buy> getBuysByUser(Integer userId) {
        return buysRepository.findByCartUserId(userId);
    }
    
    @Override
    public Integer createBuy(Buy buy) {
        return buysRepository.save(buy).getId();
    }

    @Override
    public Buy updateBuy(Integer id, Buy buy){
        Buy existingBuy = buysRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Buy not found"));
        existingBuy.setCart(buy.getCart());
        existingBuy.setPurchaseDate(buy.getPurchaseDate());
        existingBuy.setTotalPrice(buy.getTotalPrice());
        existingBuy.setPaymentMethod(buy.getPaymentMethod());
        existingBuy.setStatus(buy.getStatus());
        return buysRepository.save(existingBuy);
    }

    @Override
    public void deleteBuy(Integer id){
        Buy existingBuy = buysRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Buy not found"));
        buysRepository.deleteById(id);
    }

    @Override
    public Buy createDirectBuy(Integer userId, Integer expansionId, Integer platformId, String paymentMethod) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        ExpansionPack expansion = expansionRepository.findById(expansionId)
                .orElseThrow(() -> new RuntimeException("Expansion not found"));
        
        Platform platform = platformsRepository.findById(platformId)
                .orElseThrow(() -> new RuntimeException("Platform not found"));

        Cart cart = new Cart();
        cart.setUser(user);
        cart.setStatus("comprado");
        cart.setTotal(expansion.getPrice());
        cart = cartRepository.save(cart);

        CartDetails cartDetail = new CartDetails();
        cartDetail.setCart(cart);
        cartDetail.setExpansionPack(expansion);
        cartDetail.setPlatform(platform);
        cartDetail.setQuantity(1);
        cart.getDetails().add(cartDetail);
        cart = cartRepository.save(cart);

        Buy buy = new Buy();
        buy.setCart(cart);
        buy.setPurchaseDate(new Date());
        buy.setTotalPrice(expansion.getPrice());
        buy.setPaymentMethod(paymentMethod);
        buy.setStatus("completado");
        
        return buysRepository.save(buy);
    }

    @Override
    public Buy purchaseFromCart(Integer userId, String paymentMethod) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Cart activeCart = cartRepository.findByUserIdAndStatus(userId, "activo")
                .orElseThrow(() -> new RuntimeException("No active cart found"));

        if (activeCart.getDetails() == null || activeCart.getDetails().isEmpty()) {
            throw new RuntimeException("Cart is empty");
        }

        activeCart.setStatus("comprado");
        cartRepository.save(activeCart);

        Buy buy = new Buy();
        buy.setCart(activeCart);
        buy.setPurchaseDate(new Date());
        buy.setTotalPrice(activeCart.getTotal());
        buy.setPaymentMethod(paymentMethod);
        buy.setStatus("completado");

        Buy savedBuy = buysRepository.save(buy);

        Cart newCart = new Cart();
        newCart.setUser(user);
        newCart.setStatus("activo");
        newCart.setTotal(0.0);
        cartRepository.save(newCart);

        return savedBuy;
    }
}



