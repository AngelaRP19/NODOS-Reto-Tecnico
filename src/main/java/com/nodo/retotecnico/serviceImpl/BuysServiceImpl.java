package com.nodo.retotecnico.serviceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nodo.retotecnico.model.Buy;
import com.nodo.retotecnico.repository.BuysRepository;
import com.nodo.retotecnico.service.BuysService;

@Service
public class BuysServiceImpl implements BuysService{

    @Autowired
    private BuysRepository buysRepository;

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
}



