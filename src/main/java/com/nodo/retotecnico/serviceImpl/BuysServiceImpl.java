package com.nodo.retotecnico.serviceImpl;

import org.springframework.stereotype.Service;

@Service
public class BuyServiceImpl implements IBuysService{

    @Override
    public list<Buy> getAllBuys() { return buyRepository.findAll();
    }

    @Override
    public Buy getBuyById(Integer id){
        return buyRepository.findById(id).orElse(null);
    }

}



