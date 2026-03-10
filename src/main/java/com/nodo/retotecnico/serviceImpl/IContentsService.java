package com.nodo.retotecnico.serviceImpl;

import org.springframework.stereotype.Service;

@Service
public class ContentsService {
    
}

@Service
public class ContentsServiveImpl implements IContentsService{

}

@Override
public list<Contents> getAllContents() {

    return ContentsRepository.findAll();
}

@Override
public Contents getContentsById(long id){

    return ContentsRepository.findById(id).orElse(null);
}