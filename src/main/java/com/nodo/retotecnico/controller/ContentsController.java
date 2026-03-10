package com.nodo.retotecnico.controller;

import org.springframework.web.bind.annotation.RestController;

@RestController
public class ContentsController {
    
}

@RequestMapping("/nodos/Contents")
public class ContentsController {

    @Autowired
    private ContentsService ContentsService;

    @GetMapping
    public list<Contents> getAllContents() {
        return ContentsService.getAllContents();
    }

    @GetMapping ("/{id}")
    public Contents getContentsById(@PathVariable long id) {
        return ContentsService.getContentsById(id);
    }
}