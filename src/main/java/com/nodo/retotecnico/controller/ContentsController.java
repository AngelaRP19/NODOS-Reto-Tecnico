package com.nodo.retotecnico.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nodo.retotecnico.model.Content;
import com.nodo.retotecnico.service.ContentsService;

@RestController
@RequestMapping("/nodos/Contents")
public class ContentsController {

    @Autowired
    private ContentsService contentsService;

    @GetMapping
    public List<Content> getAllContents() {
        return contentsService.getAllContents();
    }

    @GetMapping ("/{id}")
    public Content getContentsById(@PathVariable Integer id) {
        return contentsService.getContentsById(id);
    }
    @PostMapping("/create")
    public Integer createContent(@RequestBody Content content) {
        try {
            return contentsService.createContent(content);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}   
