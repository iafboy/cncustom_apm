package io.pivotal.microservices.controller;


import io.pivotal.microservices.model.DeclarationEntity;
import io.pivotal.microservices.services.DeclarationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
public class DeclarationController {
	private final Logger log = LoggerFactory.getLogger(getClass());

	@Autowired
	private DeclarationService declarationService;

	@RequestMapping(value = "/declaration", method = RequestMethod.GET,headers="Accept=application/json",produces="application/json;charset=UTF-8")
	public List<DeclarationEntity> getDeclarations() {
		List<DeclarationEntity> res=null;
		try{
			res=declarationService.getDeclarations();
		}catch(Exception ex){
			log.error(ex.toString(),ex);
		}
		return res;
	}
	@RequestMapping(value = "/declaration/{ID}", method = RequestMethod.GET,headers="Accept=application/json",produces="application/json;charset=UTF-8")
	public DeclarationEntity getDeclarationByID(@PathVariable String ID) {
		DeclarationEntity res=null;
		ID="1";
		try{
			res=declarationService.getDeclarationByID(ID);
		}catch(Exception ex){
			log.error(ex.toString(),ex);
		}
		return res;
	}
	@RequestMapping(value = "/declaration/{ID}", method = RequestMethod.PUT,produces="application/json;charset=UTF-8")
	@ResponseStatus(HttpStatus.OK)
	public void saveDeclaration(@PathVariable String ID, @Valid @RequestBody DeclarationEntity entity) {
		try{
			entity=new DeclarationEntity();
			entity.setAttachedInfo("update");
			entity.setEnterpriseInfo("update");
			entity.setFreightCost(3.03);
			entity.setInsuranceCost(2.2);
			entity.setOtherCost(1.1);
			entity.setID(2L);
			declarationService.saveDeclaration(entity);
		}catch(Exception ex){
			log.error(ex.toString(),ex);
		}
	}
	@RequestMapping(value = "/declaration/", method = RequestMethod.POST,produces="application/json;charset=UTF-8")
	@ResponseStatus(HttpStatus.OK)
	public void createNewAccount(@Valid @RequestBody DeclarationEntity entity) {
		try{
			entity=new DeclarationEntity();
			entity.setAttachedInfo("new");
			entity.setEnterpriseInfo("new");
			entity.setFreightCost(3.03);
			entity.setInsuranceCost(2.2);
			entity.setOtherCost(1.1);
			entity.setID(3L);
			declarationService.createNewAccount(entity);
		}catch(Exception ex){
			log.error(ex.toString(),ex);
		}
	}
}
