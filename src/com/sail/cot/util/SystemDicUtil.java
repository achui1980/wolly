/**
 * 
 */
package com.sail.cot.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.directwebremoting.WebContextFactory;

import com.sail.cot.service.order.CotOrderService;
import com.sail.cot.service.system.CotCompanyService;
import com.sail.cot.service.system.CotFactoryService;
import com.sail.cot.service.systemdic.CotClauseService;
import com.sail.cot.service.systemdic.CotCommisionService;
import com.sail.cot.service.systemdic.CotConsignCompanyService;
import com.sail.cot.service.systemdic.CotContainerTypeService;
import com.sail.cot.service.systemdic.CotCurrencyService;
import com.sail.cot.service.systemdic.CotEleOtherService;
import com.sail.cot.service.systemdic.CotHsCompanyService;
import com.sail.cot.service.systemdic.CotInsureContractService;
import com.sail.cot.service.systemdic.CotNationService;
import com.sail.cot.service.systemdic.CotPayTypeService;
import com.sail.cot.service.systemdic.CotPriceSituationService;
import com.sail.cot.service.systemdic.CotShipCompanyService;
import com.sail.cot.service.systemdic.CotShipPortService;
import com.sail.cot.service.systemdic.CotTargetPortService;
import com.sail.cot.service.systemdic.CotTaxTypeService;
import com.sail.cot.service.systemdic.CotTradeTypeService;
import com.sail.cot.service.systemdic.CotTrafficTypeService;
import com.sail.cot.service.systemdic.CotTrailCarService;
import com.sail.cot.service.systemdic.CotTypeLv2Service;
import com.sail.cot.service.systemdic.CotTypeLv3Service;
import com.sail.cot.service.systemdic.CotTypeLvService;

/**
 * <p>Title: 旗行办公自动化系统（OA）</p>
 * <p>Description:</p>
 * <p>Copyright: Copyright (c) 2008</p>
 * <p>Company: </p>
 * <p>Create Time: Apr 27, 2009 2:38:49 PM </p>
 * <p>Class Name: SystemDicUtil.java </p>
 * @author achui
 *
 */
public class SystemDicUtil {
	private Map getSystemDicList()
	{
		Map res = new HashMap();
		List reslist = null;
		//获取价格条款
		CotClauseService clauseService = (CotClauseService)SystemUtil.getService("CotClauseService");
		reslist = clauseService.getClauseList();
		res.put("clause", reslist);
		//获取运输方式
		CotTrafficTypeService trafficService = (CotTrafficTypeService)SystemUtil.getService("CotTrafficTypeService");
		 reslist = trafficService.getTrafficList();
		res.put("traffic", reslist);
		//获取报价场合
		CotPriceSituationService situService = (CotPriceSituationService)SystemUtil.getService("CotPriceSituationService");
		reslist = situService.getList();
		res.put("situation", reslist);
		//获取付款方式
		CotPayTypeService paytypeService = (CotPayTypeService)SystemUtil.getService("CotPayTypeService");
		reslist = paytypeService.getList();
		res.put("paytype", reslist);
		//获取目的港
		CotTargetPortService targetportService = (CotTargetPortService)SystemUtil.getService("CotTargetPortService");
		reslist = targetportService.getTargetPortList();
		res.put("targetport", reslist);
		//获取起运港
		CotShipPortService shipportService = (CotShipPortService)SystemUtil.getService("CotShipPortService");
		reslist = shipportService.getShipPortList();
		res.put("shipport", reslist);
		//获取出口公司
		CotCompanyService companyService = (CotCompanyService)SystemUtil.getService("CotCompanyService");
		reslist = companyService.getCompanyList();
		res.put("company", reslist);
		//获取产品分类
		CotTypeLv2Service typeLv2Service = (CotTypeLv2Service)SystemUtil.getService("CotTypeLv2Service");
		reslist = typeLv2Service.getList();
		res.put("typelv2", reslist);
		//获取样品材质
		CotTypeLvService typeLvService  = (CotTypeLvService)SystemUtil.getService("CotTypeLvService");
		reslist = typeLvService.getList();
		res.put("typelv1", reslist);
		//获取厂家数据
		CotFactoryService facservice = (CotFactoryService)SystemUtil.getService("CotFactoryService");
		reslist = facservice.getFactoryList();
		res.put("factory", reslist);	
		//获取币种信息
		CotCurrencyService currservice = (CotCurrencyService)SystemUtil.getService("CotCurrencyService");
		reslist = currservice.getList();
		res.put("currency", reslist);
		//获取佣金类型
		CotCommisionService commisionService = (CotCommisionService)SystemUtil.getService("CotCommisionService");
		reslist = commisionService.getCommisionList();
		res.put("commision", reslist);
		//获取集装箱类型
		CotContainerTypeService containerTypeSrv = (CotContainerTypeService)SystemUtil.getService("CotContainerTypeService");
		reslist = containerTypeSrv.getList();
		res.put("container", reslist);
		//获取保险契约
		CotInsureContractService insureContractSrv = (CotInsureContractService)SystemUtil.getService("CotInsureContractService");
		reslist = insureContractSrv.getInsureContractList();
		res.put("insurecontract", reslist);
		//获取海关编码
		CotEleOtherService eleOtherService = (CotEleOtherService)SystemUtil.getService("CotEleOtherService");
		reslist = eleOtherService.getList();
		res.put("eleother", reslist);
		//获取国家
		CotNationService nationService = (CotNationService)SystemUtil.getService("CotNationService");
		reslist = nationService.getNationList();
		res.put("nation", reslist);
		//获取委托船运公司
		CotConsignCompanyService consignCompanyService = (CotConsignCompanyService)SystemUtil.getService("CotConsignCompanyService");
		reslist = consignCompanyService.getConsignCompanyList();
		res.put("consignCompany", reslist);
		//获取贸易性质
		CotTradeTypeService tradeTypeService = (CotTradeTypeService)SystemUtil.getService("CotTradeTypeService");
		reslist = tradeTypeService.getTradeTypeList();
		res.put("tradetype", reslist);
		//获取征税类型
		CotTaxTypeService taxTypeService = (CotTaxTypeService)SystemUtil.getService("CotTaxTypeService");
		reslist = taxTypeService.getTaxTypeList();
		res.put("taxtype", reslist);
		//获取报关行
		CotHsCompanyService hsCompanyService = (CotHsCompanyService)SystemUtil.getService("CotHsCompanyService");
		reslist = hsCompanyService.getHsCompanyList();
		res.put("hsCompany", reslist);
		//获取拖车行
		CotTrailCarService trailCarService = (CotTrailCarService)SystemUtil.getService("CotTrailCarService");
		reslist = trailCarService.getTrailCarList();
		res.put("trailCar", reslist);
		//获取拖车行
		CotShipCompanyService shipCompanyService = (CotShipCompanyService)SystemUtil.getService("CotShipCompanyService");
		reslist = shipCompanyService.getShipCompanyList();
		res.put("shipCompany", reslist);
		//获取配件类型
		CotTypeLv3Service typeLv3Service = (CotTypeLv3Service)SystemUtil.getService("CotTypeLv3Service");
		reslist = typeLv3Service.getTypeLvList();
		res.put("typelv3", reslist);
		return res;
	}
	//private static Map map = null;
	public Map getSysDicMap()
	{		
		return this.getSystemDicList();
	}
	public void setSysDicMap()
	{
		//map = this.getSystemDicList();
	}
	public List getDicListByName(String objname)
	{
		HttpSession session =  WebContextFactory.get().getSession();
		Map dicMap = (Map)session.getAttribute("sysdic");
		if(dicMap == null) return null;
		List res = (List)dicMap.get(objname);
		return res;
	}
	
	public List getCotContractList(int type){
		CotOrderService orderService = (CotOrderService)SystemUtil.getService("CotOrderService");
		return orderService.getCotContractList(type);
	}
}
