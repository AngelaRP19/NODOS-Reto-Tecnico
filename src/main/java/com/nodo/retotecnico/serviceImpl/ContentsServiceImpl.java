package com.nodo.retotecnico.serviceImpl;

import org.springframework.stereotype.Service;

@Service
public class ContentsService {
    
}

@Service
public class ContentServiveImpl implements IContentsService{

    @Override
    public list<Contents> getAllContents() {

        return ContentsRepository.findAll();
    }

    @Override
    public Content getContentsById(long id){

        return ContentsRepository.findById(id).orElse(null);
    }
}

