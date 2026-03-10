package com.nodo.retotecnico.serviceImpl;

import org.springframework.stereotype.Service;

@Service
public class BuysService {
    
}

@Service
public class BuyServiveImpl implements IBuysService{

}

@Override
public list<Buy> getAllBuys() {
    return buyRepository.findAll();
}

@Override
public Buy getBuyById(long id){
    return buyRepository.findById(id).orElse(null);
}

