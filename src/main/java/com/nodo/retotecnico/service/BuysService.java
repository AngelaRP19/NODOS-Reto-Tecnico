package com.nodo.retotecnico.service;

import java.util.List;

import com.nodo.retotecnico.model.Buy;

public interface BuysService{
    List<Buy> getAllBuys();
    Buy getBuyById(Integer id);
    Integer createBuy(Buy buy);
}
