package com.nodo.retotecnico.controller;

import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/nodos/buys")
public class BuysController {

    @Autowired
    private BuysService buysService;

    @GetMapping
    public list<Buy> getAllBuy() {
        return buysService.getAllBuy();
    }

    @GetMapping ("/{id}")
    public Buy getBuysById(@PathVariable Integer id) {
        return buysService.getBuyById(id);
    }
}
