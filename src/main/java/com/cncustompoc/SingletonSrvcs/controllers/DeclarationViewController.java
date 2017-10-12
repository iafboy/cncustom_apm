package com.cncustompoc.SingletonSrvcs.controllers;


import com.cncustompoc.SingletonSrvcs.domains.DeclarationViewEntity;
import com.cncustompoc.SingletonSrvcs.domains.DeclarationViewOldEntity;
import com.cncustompoc.SingletonSrvcs.domains.QueryCriteriaEntity;
import com.cncustompoc.SingletonSrvcs.services.DeclarationViewService;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
public class DeclarationViewController {
	private final Logger log = LoggerFactory.getLogger(getClass());

	@Autowired
	private DeclarationViewService declarationViewService;
	private DeclarationViewEntity dve;
	@ApiIgnore
	@RequestMapping(path = "/declarationview", method = RequestMethod.GET,headers="Accept=application/json",produces="application/json;charset=UTF-8")
	public List<DeclarationViewEntity> getDeclarationViews() {
		List<DeclarationViewEntity> res=null;
		try{
			res=declarationViewService.getDeclarationViews();
		}catch(Exception ex){
			log.error(ex.toString(),ex);
		}
		return res;
	}


	@ApiOperation(value="通过条件获取报关单数据(1.0)", notes=
			"<b>参数格式：</b>" +
			"<br />" +
			"[{\"colName\":\"String\",\"colValue\":\"String\",\"operator\":\"String\"}]" +
			"<br />" +
			"<b>调用举例1 - 查询企业ID为001的报关单信息：</b>" +
			"<br />" +
			"[{\"colName\":\"EnterpriseID\",\"colValue\":\"001\",\"operator\":\"=\"}]" +
			"<br />" +
			"<b>调用举例2 - 查询运费在50000到60000之间的报关单信息：</b>" +
			"<br />" +
			"[{\"colName\":\"FreightCost\",\"colValue\":\"50000\",\"operator\":\">=\"},{\"colName\":\"FreightCost\",\"colValue\":\"60000\",\"operator\":\"<=\"}]" +
			"")
	@RequestMapping(path = "/1.0/declarationview", method = RequestMethod.POST,headers="Accept=application/json",produces="application/json;charset=UTF-8")
	public List<DeclarationViewOldEntity> getDeclarationViewsByCriteria10(@Valid @RequestBody QueryCriteriaEntity[] param) {
		List<DeclarationViewEntity> res=null;
		String ct = "";
		for(int i=0;i<param.length;i++)
		{
			QueryCriteriaEntity q = param[i];
			ct+=q.getColName()+" "+q.getOperator()+" "+q.getColValue() +" and ";
		}
		ct+=" 1=1 ";
		try{
			res=declarationViewService.getDeclarationViewsByCriteria(ct);
		}catch(Exception ex){
			log.error(ex.toString(),ex);
		}
		List<DeclarationViewOldEntity> ret = new ArrayList<DeclarationViewOldEntity>();
		for(int i=0;i<res.size();i++)
		{
			DeclarationViewOldEntity id = new DeclarationViewOldEntity();
			DeclarationViewEntity nid = res.get(i);
			id.setID(nid.getID());
			id.setAttachedInfo(nid.getAttachedInfo());
			id.setEnterpriseID(nid.getEnterpriseID());
			id.setEnterpriseInfo(nid.getEnterpriseInfo());
			id.setFreightCost(nid.getFreightCost());
			id.setInsuranceCost(nid.getInsuranceCost());
			id.setOtherCost(nid.getOtherCost());
			id.setStatus(nid.getStatus());
			ret.add(id);
		}
		return ret;
	}
	@ApiOperation(value="通过条件获取报关单数据(1.1)", notes=
					"<br />" +
					"<b><font color='red'>更新说明：</b>" +
					"<br />" +
					"返回数组参增加了企业名称字段，便于取值。</font>" +
					"<br />" +
					"<br />" +
					"<b>参数格式：</b>" +
					"<br />" +
					"[{\"colName\":\"String\",\"colValue\":\"String\",\"operator\":\"String\"}]" +
					"<br />" +
					"<b>调用举例1 - 查询企业ID为001的报关单信息：</b>" +
					"<br />" +
					"[{\"colName\":\"EnterpriseID\",\"colValue\":\"001\",\"operator\":\"=\"}]" +
					"<br />" +
					"<b>调用举例2 - 查询运费在50000到60000之间的报关单信息：</b>" +
					"<br />" +
					"[{\"colName\":\"FreightCost\",\"colValue\":\"50000\",\"operator\":\">=\"},{\"colName\":\"FreightCost\",\"colValue\":\"60000\",\"operator\":\"<=\"}]" +
					"")
	@RequestMapping(path = "/1.1/declarationview", method = RequestMethod.POST,headers="Accept=application/json",produces="application/json;charset=UTF-8")
	public List<DeclarationViewEntity> getDeclarationViewsByCriteria11(@Valid @RequestBody QueryCriteriaEntity[] param) {
		List<DeclarationViewEntity> res=null;
		String ct = "";
		for(int i=0;i<param.length;i++)
		{
			QueryCriteriaEntity q = param[i];
			ct+=q.getColName()+" "+q.getOperator()+" "+q.getColValue() +" and ";
		}
		ct+=" 1=1 ";
		try{
			res=declarationViewService.getDeclarationViewsByCriteria(ct);
		}catch(Exception ex){
			log.error(ex.toString(),ex);
		}
		return res;
	}
}
