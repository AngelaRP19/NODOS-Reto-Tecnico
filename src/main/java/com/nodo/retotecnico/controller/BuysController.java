package com.nodo.retotecnico.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nodo.retotecnico.model.Buy;
import com.nodo.retotecnico.service.BuysService;

@RestController
@RequestMapping("/nodos/buys")
public class BuysController {

    @Autowired
    private BuysService buysService;

    @GetMapping
    public List<Buy> getAllBuys() {
        return buysService.getAllBuys();
    }

    @GetMapping ("/{id}")
    public Buy getBuysById(@PathVariable Integer id) {
        return buysService.getBuyById(id);
    }
    @PostMapping("/create")
    public Integer createBuy(@RequestBody Buy buy) {
        return buysService.createBuy(buy);
    }
}
