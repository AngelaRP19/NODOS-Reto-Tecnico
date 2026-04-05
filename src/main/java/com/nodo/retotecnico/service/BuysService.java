package com.nodo.retotecnico.service;

import java.util.List;

import com.nodo.retotecnico.model.Buy;

public interface BuysService{
    List<Buy> getAllBuys();
    Buy getBuyById(Integer id);
    Integer createBuy(Buy buy);
    Buy updateBuy(Integer id, Buy buy);
    void deleteBuy(Integer id);
    List<Buy> getBuysByUser(Integer userId);
    Buy createDirectBuy(Integer userId, Integer expansionId, Integer platformId, String paymentMethod);
    Buy purchaseFromCart(Integer userId, String paymentMethod);
}
