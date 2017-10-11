package com.cncustompoc.SingletonSrvcs.services;

import com.cncustompoc.SingletonSrvcs.domains.DeclarationEntity;
import com.cncustompoc.SingletonSrvcs.repository.DeclarationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class DeclarationService {
    @Autowired
    private DeclarationRepository declarationRepository;
    @Transactional(rollbackFor = Throwable.class)
    public List<DeclarationEntity> getDeclarations() { return declarationRepository.selectDeclarations();}
    @Transactional(rollbackFor = Throwable.class)
    public DeclarationEntity getDeclarationByID(String ID) {
        return declarationRepository.selectDeclarationByID(ID);
    }
    @Transactional(rollbackFor = Throwable.class)
    public void saveDeclaration(DeclarationEntity entity) {
        declarationRepository.updateDeclarationByID(entity);
    }
    @Transactional(rollbackFor = Throwable.class)
    public void createNewAccount(DeclarationEntity entity) {
        declarationRepository.insertDeclaratio(entity);
    }
}
