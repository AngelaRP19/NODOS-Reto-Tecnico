package com.nodo.retotecnico.service;

import java.util.List;

import com.nodo.retotecnico.model.Content;

public interface ContentsService {

    List<Content> getAllContents();
    Content getContentsById(Integer id);
}
