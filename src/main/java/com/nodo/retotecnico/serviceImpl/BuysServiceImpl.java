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

}



