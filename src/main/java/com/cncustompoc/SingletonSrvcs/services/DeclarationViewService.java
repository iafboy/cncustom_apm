package com.cncustompoc.SingletonSrvcs.services;


import com.cncustompoc.SingletonSrvcs.domains.DeclarationViewEntity;
import com.cncustompoc.SingletonSrvcs.repository.DeclarationViewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class DeclarationViewService {
    @Autowired
    private DeclarationViewRepository declarationRepository;
    @Transactional(rollbackFor = Throwable.class)
    public List<DeclarationViewEntity> getDeclarationViews() { return declarationRepository.selectDeclarationViews();}
    @Transactional(rollbackFor = Throwable.class)
    public List<DeclarationViewEntity> getDeclarationViewsByCriteria(String ct)
    {
        return declarationRepository.selectDeclarationViewsByCriteria(ct);
    }

}
