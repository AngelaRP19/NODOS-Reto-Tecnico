package com.nodo.retotecnico.controller;

import org.springframework.web.bind.annotation.RestController;

@RestController

@RequestMapping("/nodos/ExpansionPacks")
public class ExpansionPacksController {

    @Autowired
    private ExpansionPacksService ExpansionPacksService;

    @GetMapping
    public list<ExpansionPacks> getAllExpansionPacks() {
        return ExpansionPacksService.getAllExpansionPacks();
    }

    @GetMapping ("/{id}")
    public ExpansionPacks getExpansionPacksById(@PathVariable Integer id) {
        return ExpansionPacksService.getExpansionPacksById(id);
    }
}
