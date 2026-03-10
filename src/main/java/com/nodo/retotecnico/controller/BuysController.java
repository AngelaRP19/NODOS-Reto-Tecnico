package com.nodo.retotecnico.controller;

import org.springframework.web.bind.annotation.RestController;

@RestController
public class BuysController {
    
}

@RequestMapping("/nodos/buys")
public class BuysController {
    
    @Autowired
    private BuysService buysService;

    @GetMapping
    public list<Buys> getAllBuys() {
        return buysService.getAllBuys();
    }

    @GetMapping ("/{id}")
    public Buys getBuysById(@PathVariable long id) {
        return buysService.getBuysById(id);
    }
}