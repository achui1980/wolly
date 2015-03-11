package com.sail.cot.service.system.impl;
 
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;

import com.jason.core.exception.DAOException;
import com.sail.cot.dao.CotBaseDao; 
import com.sail.cot.domain.CotCustSeq;
import com.sail.cot.domain.CotCustomer;
import com.sail.cot.domain.CotFactory;
import com.sail.cot.domain.CotOrder;
import com.sail.cot.domain.CotOrderFac;
import com.sail.cot.domain.vo.CotCfgNo;
import com.sail.cot.service.customer.CotCustomerService;
import com.sail.cot.service.system.SetNoService;
import com.sail.cot.util.ContextUtil;
import com.sail.cot.util.SystemUtil;

public class SetNoServiceImpl implements SetNoService {
	
	private CotBaseDao cotBaseDao;
	public CotBaseDao getCotBaseDao() {
		return cotBaseDao;
	}

	public void setCotBaseDao(CotBaseDao cotBaseDao) {
		this.cotBaseDao = cotBaseDao;
	}
	
	//获取sysconfig.properties路径url
	URL url = SetNoServiceImpl.class.getResource("/sysconfig.properties");
	String filepath = url.getPath();
   
	public void saveNo(CotCfgNo cotCfgNo){
		//转化为大写并去除头尾空格
		String priceNoNew = cotCfgNo.getPriceNo().toUpperCase().trim();
		String orderNoNew = cotCfgNo.getOrderNo().toUpperCase().trim();
		String givenNoNew = cotCfgNo.getGivenNo().toUpperCase().trim();
		String signNoNew = cotCfgNo.getSignNo().toUpperCase().trim();
		String orderFacNoNew = cotCfgNo.getOrderFacNo().toUpperCase().trim();
		String orderOutNoNew = cotCfgNo.getOrderOutNo().toUpperCase().trim();
		String containerNoNew = cotCfgNo.getContainerNo().toUpperCase().trim();
		String autoOrderFacNoNew = cotCfgNo.getAutoOrderFacNo().toUpperCase().trim();
		String finacerecvNoNew = cotCfgNo.getFinacerecvNo().toUpperCase().trim();
		String finacegivenNoNew = cotCfgNo.getFinacegivenNo().toUpperCase().trim();
		String fincaeaccountdealNoNew = cotCfgNo.getFincaeaccountdealNo().toUpperCase().trim();
		String fincaeaccountrecvNoNew = cotCfgNo.getFincaeaccountrecvNo().toUpperCase().trim();
		String backtaxNoNew = cotCfgNo.getBacktaxNo().toUpperCase().trim();
		String auditNoNew = cotCfgNo.getAuditNo().toUpperCase().trim();
		String packingNoNew = cotCfgNo.getPackingNo().toUpperCase().trim();
		String accessNoNew = cotCfgNo.getAccessNo().toUpperCase().trim();
		String custNoNew = cotCfgNo.getCustNo().toUpperCase().trim();
		String facNoNew = cotCfgNo.getFacNo().toUpperCase().trim();
		
		//配置文件获取序列号
		String priceNoSequence = SystemUtil.getProperty(filepath, "priceNoSequence").trim();
		String orderNoSequence = SystemUtil.getProperty(filepath, "orderNoSequence").trim();
		String givenNoSequence = SystemUtil.getProperty(filepath, "givenNoSequence").trim();
		String signNoSequence = SystemUtil.getProperty(filepath, "signNoSequence").trim();
		String orderFacNoSequence = SystemUtil.getProperty(filepath, "orderFacNoSequence").trim();
		String orderOutNoSequence = SystemUtil.getProperty(filepath, "orderOutNoSequence").trim();
		String containerNoSequence = SystemUtil.getProperty(filepath, "containerNoSequence").trim();
		String autoOrderFacNoSequence = SystemUtil.getProperty(filepath, "autoOrderFacNoSequence").trim();
		String finacerecvNoSequence = SystemUtil.getProperty(filepath, "finacerecvNoSequence").trim();
		String finacegivenNoSequence = SystemUtil.getProperty(filepath, "finacegivenNoSequence").trim();
		String fincaeaccountdealNoSequence = SystemUtil.getProperty(filepath, "fincaeaccountdealNoSequence").trim();
		String fincaeaccountrecvNoSequence = SystemUtil.getProperty(filepath, "fincaeaccountrecvNoSequence").trim();
		String backtaxNoSequence = SystemUtil.getProperty(filepath, "backtaxNoSequence").trim();
		String auditNoSequence = SystemUtil.getProperty(filepath, "auditNoSequence").trim();
		String packingNoSequence = SystemUtil.getProperty(filepath, "packingNoSequence").trim();
		String accessNoSequence = SystemUtil.getProperty(filepath, "accessNoSequence").trim();
		String custNoSequence = SystemUtil.getProperty(filepath, "custNoSequence").trim();
		String facNoSequence = SystemUtil.getProperty(filepath, "facNoSequence").trim();
		
		//前台获取序列号
//		String priceNoSequence = this.getSequence(priceNoNew);
//		String orderNoSequence = this.getSequence(orderNoNew);
//		String givenNoSequence = this.getSequence(givenNoNew);
//		String signNoSequence = this.getSequence(signNoNew);
//		String orderFacNoSequence = this.getSequence(orderFacNoNew);
//		String orderOutNoSequence = this.getSequence(orderOutNoNew);
//		String containerNoSequence = this.getSequence(containerNoNew);
//		String autoOrderFacNoSequence = this.getSequence(autoOrderFacNoNew);	
//		String finacerecvNoSequence = this.getSequence(finacerecvNoNew);
//		String finacegivenNoSequence = this.getSequence(finacegivenNoNew);
//		String fincaeaccountdealNoSequence = this.getSequence(fincaeaccountdealNoNew);
//		String fincaeaccountrecvNoSequence = this.getSequence(fincaeaccountrecvNoNew);
//		String backtaxNoSequence = this.getSequence(backtaxNoNew);
//		String auditNoSequence = this.getSequence(auditNoNew);
//		String packingNoSequence = this.getSequence(packingNoNew);
//		String accessNoSequence = this.getSequence(accessNoNew);
//		String custNoSequence = this.getSequence(custNoNew);
//		String facNoSequence = this.getSequence(facNoNew);
		
		
		//替换
		priceNoNew = this.doReplace(priceNoNew);
		orderNoNew = this.doReplace(orderNoNew);
		givenNoNew = this.doReplace(givenNoNew);
		signNoNew = this.doReplace(signNoNew);
		orderFacNoNew = this.doReplace(orderFacNoNew);
		orderOutNoNew = this.doReplace(orderOutNoNew);
		containerNoNew = this.doReplace(containerNoNew);
		autoOrderFacNoNew = this.doReplace(autoOrderFacNoNew);
		finacerecvNoNew = this.doReplace(finacerecvNoNew);
		finacegivenNoNew = this.doReplace(finacegivenNoNew);
		fincaeaccountdealNoNew = this.doReplace(fincaeaccountdealNoNew);
		fincaeaccountrecvNoNew = this.doReplace(fincaeaccountrecvNoNew);
		backtaxNoNew = this.doReplace(backtaxNoNew);
		auditNoNew = this.doReplace(auditNoNew);
		packingNoNew = this.doReplace(packingNoNew);
		accessNoNew = this.doReplace(accessNoNew);
		custNoNew = this.doReplace(custNoNew);
		facNoNew = this.doReplace(facNoNew);
		
		Map<String, String> map = new HashMap<String, String>();
		map.put("priceNo", priceNoNew);
		map.put("priceNoSequence", priceNoSequence);
		map.put("orderNo", orderNoNew);
		map.put("orderNoSequence", orderNoSequence);
		map.put("givenNo", givenNoNew);
		map.put("givenNoSequence", givenNoSequence);
		map.put("signNo", signNoNew);
		map.put("signNoSequence", signNoSequence);
		map.put("orderFacNo", orderFacNoNew);
		map.put("orderFacNoSequence", orderFacNoSequence);
		map.put("orderOutNo", orderOutNoNew);
		map.put("orderOutNoSequence", orderOutNoSequence);
		map.put("containerNo", containerNoNew);
		map.put("containerNoSequence", containerNoSequence);
		map.put("autoOrderFacNo", autoOrderFacNoNew);
		map.put("autoOrderFacNoSequence", autoOrderFacNoSequence);	
		
		map.put("finacerecvNo", finacerecvNoNew);
		map.put("finacegivenNo", finacegivenNoNew);
		map.put("fincaeaccountdealNo", fincaeaccountdealNoNew);
		map.put("fincaeaccountrecvNo", fincaeaccountrecvNoNew);
		map.put("backtaxNo", backtaxNoNew);
		map.put("auditNo", auditNoNew);
		map.put("packingNo", packingNoNew);
		map.put("accessNo", accessNoNew);
		map.put("custNo", custNoNew);
		map.put("facNo", facNoNew);
		
		map.put("finacerecvNoSequence", finacerecvNoSequence);
		map.put("finacegivenNoSequence", finacegivenNoSequence);
		map.put("fincaeaccountdealNoSequence", fincaeaccountdealNoSequence);
		map.put("fincaeaccountrecvNoSequence", fincaeaccountrecvNoSequence);
		map.put("backtaxNoSequence", backtaxNoSequence);
		map.put("auditNoSequence", auditNoSequence);
		map.put("packingNoSequence", packingNoSequence);
		map.put("accessNoSequence", accessNoSequence);
		map.put("custNoSequence", custNoSequence);
		map.put("facNoSequence", facNoSequence);
		
		
		
		map.put("priceZeroType", cotCfgNo.getPriceZeroType());
		map.put("orderZeroType", cotCfgNo.getOrderZeroType());
		map.put("orderoutZeroType", cotCfgNo.getOrderoutZeroType());
		map.put("orderfacZeroType", cotCfgNo.getOrderfacZeroType());
		map.put("autoorderfacZeroType", cotCfgNo.getAutoorderfacZeroType());
		map.put("givenZeroType", cotCfgNo.getGivenZeroType());
		map.put("signZeroType", cotCfgNo.getSignZeroType());
		map.put("splitZeroType", cotCfgNo.getSplitZeroType());
		map.put("finacerecvZeroType", cotCfgNo.getFinacerecvZeroType());
		map.put("finacegivenZeroType", cotCfgNo.getFinacegivenZeroType());
		map.put("fincaeaccountdealZeroType", cotCfgNo.getFincaeaccountdealZeroType());
		map.put("fincaeaccountrecvZeroType", cotCfgNo.getFincaeaccountrecvZeroType());
		map.put("backtaxZeroType", cotCfgNo.getBacktaxZeroType());
		map.put("auditZeroType", cotCfgNo.getAuditZeroType());
		map.put("packingZeroType", cotCfgNo.getPackingZeroType());
		map.put("accessZeroType", cotCfgNo.getAccessZeroType());
		map.put("custZeroType", cotCfgNo.getCustZeroType());
		map.put("facZeroType", cotCfgNo.getFacZeroType());
		
		String today = this.getCurrentDate();
		map.put("currentDate", today);
		
		
		SystemUtil.setPropertyFile(map, filepath);
	}
	
	//归零
	public void toZero(String seqName){
		Map<String, String> map = new HashMap<String, String>();
		if(seqName.equals("priceNoSeq")){
			map.put("priceNoSequence", "0");
		}
		if(seqName.equals("orderNoSeq")){
			map.put("orderNoSequence", "0");
		}
		if(seqName.equals("givenNoSeq")){
			map.put("givenNoSequence", "0");
		}
		if(seqName.equals("signNoSeq")){
			map.put("signNoSequence", "0");
		}
		if(seqName.equals("orderFacNoSeq")){
			map.put("orderFacNoSequence", "0");
		}
		if(seqName.equals("orderOutNoSeq")){
			map.put("orderOutNoSequence", "0");
		}
		if(seqName.equals("containerNoSeq")){
			map.put("containerNoSequence", "0");
		}
		if(seqName.equals("autoOrderFacNoSeq")){
			map.put("autoOrderFacNoSequence", "0");
		}
		if(seqName.equals("finacerecvNoSeq")){
			map.put("finacerecvNoSequence", "0");
		}
		if(seqName.equals("finacegivenNoSeq")){
			map.put("finacegivenNoSequence", "0");
		}
		if(seqName.equals("fincaeaccountdealNoSeq")){
			map.put("fincaeaccountdealNoSequence", "0");
		}
		if(seqName.equals("fincaeaccountrecvNoSeq")){
			map.put("fincaeaccountrecvNoSequence", "0");
		}
		if(seqName.equals("backtaxNoSeq")){
			map.put("backtaxNoSequence", "0");
		}
		if(seqName.equals("auditNoSeq")){
			map.put("auditNoSequence", "0");
		}
		if(seqName.equals("packingNoSeq")){
			map.put("packingNoSequence", "0");
		}
		if(seqName.equals("accessNoSeq")){
			map.put("accessNoSequence", "0");
		}
		if(seqName.equals("custNoSeq")){
			map.put("custNoSequence", "0");
		}
		if(seqName.equals("facNoSeq")){
			map.put("facNoSequence", "0");
		}
		SystemUtil.setPropertyFile(map, filepath);
	}
    
	//获取配置文件数据
	public CotCfgNo getNoMap(){
		String priceNo = SystemUtil.getProperty(filepath, "priceNo");
		String orderNo = SystemUtil.getProperty(filepath, "orderNo");
		String givenNo = SystemUtil.getProperty(filepath, "givenNo");
		String signNo = SystemUtil.getProperty(filepath, "signNo");
		String orderFacNo = SystemUtil.getProperty(filepath, "orderFacNo");
		String orderOutNo = SystemUtil.getProperty(filepath, "orderOutNo");
		String containerNo = SystemUtil.getProperty(filepath, "containerNo");
		String autoOrderFacNo = SystemUtil.getProperty(filepath, "autoOrderFacNo");
		String finacerecvNo = SystemUtil.getProperty(filepath, "finacerecvNo");
		String finacegivenNo = SystemUtil.getProperty(filepath, "finacegivenNo");
		String fincaeaccountdealNo = SystemUtil.getProperty(filepath, "fincaeaccountdealNo");
		String fincaeaccountrecvNo = SystemUtil.getProperty(filepath, "fincaeaccountrecvNo");
		String backtaxNo = SystemUtil.getProperty(filepath, "backtaxNo");
		String auditNo = SystemUtil.getProperty(filepath, "auditNo");
		String packingNo = SystemUtil.getProperty(filepath, "packingNo");
		String accessNo = SystemUtil.getProperty(filepath, "accessNo");
		String custNo = SystemUtil.getProperty(filepath, "custNo");
		String facNo = SystemUtil.getProperty(filepath, "facNo");
		
		String priceNoSequence = SystemUtil.getProperty(filepath, "priceNoSequence");
		String orderNoSequence = SystemUtil.getProperty(filepath, "orderNoSequence");
		String givenNoSequence = SystemUtil.getProperty(filepath, "givenNoSequence");
		String signNoSequence = SystemUtil.getProperty(filepath, "signNoSequence");
		String orderFacNoSequence = SystemUtil.getProperty(filepath, "orderFacNoSequence");
		String orderOutNoSequence = SystemUtil.getProperty(filepath, "orderOutNoSequence");
		String containerNoSequence = SystemUtil.getProperty(filepath, "containerNoSequence");
		String autoOrderFacNoSequence = SystemUtil.getProperty(filepath, "autoOrderFacNoSequence");
		String finacerecvNoSequence = SystemUtil.getProperty(filepath, "finacerecvNoSequence");
		String finacegivenNoSequence = SystemUtil.getProperty(filepath, "finacegivenNoSequence");
		String fincaeaccountdealNoSequence = SystemUtil.getProperty(filepath, "fincaeaccountdealNoSequence");
		String fincaeaccountrecvNoSequence = SystemUtil.getProperty(filepath, "fincaeaccountrecvNoSequence");
		String backtaxNoSequence = SystemUtil.getProperty(filepath, "backtaxNoSequence");
		String auditNoSequence = SystemUtil.getProperty(filepath, "auditNoSequence");
		String packingNoSequence = SystemUtil.getProperty(filepath, "packingNoSequence");
		String accessNoSequence = SystemUtil.getProperty(filepath, "accessNoSequence");
		String custNoSequence = SystemUtil.getProperty(filepath, "custNoSequence");
		String facNoSequence = SystemUtil.getProperty(filepath, "facNoSequence");
		
		//获取归0方式
		String priceZeroType = SystemUtil.getProperty(filepath, "priceZeroType");
		String orderZeroType = SystemUtil.getProperty(filepath, "orderZeroType");
		String orderoutZeroType = SystemUtil.getProperty(filepath, "orderoutZeroType");
		String orderfacZeroType = SystemUtil.getProperty(filepath, "orderfacZeroType");
		String givenZeroType = SystemUtil.getProperty(filepath, "givenZeroType");
		String signZeroType = SystemUtil.getProperty(filepath, "signZeroType");
		String splitZeroType = SystemUtil.getProperty(filepath, "splitZeroType");
		String autoorderfacZeroType = SystemUtil.getProperty(filepath, "autoorderfacZeroType");
		String finacerecvZeroType = SystemUtil.getProperty(filepath, "finacerecvZeroType");
		String finacegivenZeroType = SystemUtil.getProperty(filepath, "finacegivenZeroType");
		String fincaeaccountdealZeroType = SystemUtil.getProperty(filepath, "fincaeaccountdealZeroType");
		String fincaeaccountrecvZeroType = SystemUtil.getProperty(filepath, "fincaeaccountrecvZeroType");
		String backtaxZeroType = SystemUtil.getProperty(filepath, "backtaxZeroType");
		String auditZeroType = SystemUtil.getProperty(filepath, "auditZeroType");
		String packingZeroType = SystemUtil.getProperty(filepath, "packingZeroType");
		String accessZeroType = SystemUtil.getProperty(filepath, "accessZeroType");
		String custZeroType = SystemUtil.getProperty(filepath, "custZeroType");
		String facZeroType = SystemUtil.getProperty(filepath, "facZeroType");
		
		
		
		//还原替换
		priceNo = this.backReplace(priceNo);
		orderNo = this.backReplace(orderNo);
		givenNo = this.backReplace(givenNo);
		signNo = this.backReplace(signNo);
		orderFacNo = this.backReplace(orderFacNo);
		orderOutNo = this.backReplace(orderOutNo);
		containerNo = this.backReplace(containerNo);
		autoOrderFacNo = this.backReplace(autoOrderFacNo);
		finacerecvNo = this.backReplace(finacerecvNo);
		finacegivenNo = this.backReplace(finacegivenNo);
		fincaeaccountdealNo = this.backReplace(fincaeaccountdealNo);
		fincaeaccountrecvNo = this.backReplace(fincaeaccountrecvNo);
		backtaxNo = this.backReplace(backtaxNo);
		auditNo = this.backReplace(auditNo);
		packingNo = this.backReplace(packingNo);
		accessNo = this.backReplace(accessNo);
		custNo = this.backReplace(custNo);
		facNo = this.backReplace(facNo);
		
		
		CotCfgNo cotCfgNo = new CotCfgNo();
		cotCfgNo.setPriceNo(priceNo);
		cotCfgNo.setOrderNo(orderNo);
		cotCfgNo.setGivenNo(givenNo);
		cotCfgNo.setSignNo(signNo);
		cotCfgNo.setOrderFacNo(orderFacNo);
		cotCfgNo.setOrderOutNo(orderOutNo);
		cotCfgNo.setContainerNo(containerNo);
		cotCfgNo.setAutoOrderFacNo(autoOrderFacNo);
		cotCfgNo.setFinacerecvNo(finacerecvNo);
		cotCfgNo.setFinacegivenNo(finacegivenNo);
		cotCfgNo.setFincaeaccountdealNo(fincaeaccountdealNo);
		cotCfgNo.setFincaeaccountrecvNo(fincaeaccountrecvNo);
		cotCfgNo.setBacktaxNo(backtaxNo);
		cotCfgNo.setAuditNo(auditNo);
		cotCfgNo.setPackingNo(packingNo);
		cotCfgNo.setAccessNo(accessNo);
		cotCfgNo.setCustNo(custNo);
		cotCfgNo.setFacNo(facNo);
		
		cotCfgNo.setPriceNoSeq(priceNoSequence);
		cotCfgNo.setOrderNoSeq(orderNoSequence);
		cotCfgNo.setGivenNoSeq(givenNoSequence);
		cotCfgNo.setSignNoSeq(signNoSequence);
		cotCfgNo.setOrderFacNoSeq(orderFacNoSequence);
		cotCfgNo.setOrderOutNoSeq(orderOutNoSequence);
		cotCfgNo.setContainerNoSeq(containerNoSequence);
		cotCfgNo.setAutoOrderFacNoSeq(autoOrderFacNoSequence);	
		cotCfgNo.setFinacerecvNoSeq(finacerecvNoSequence);
		cotCfgNo.setFinacegivenNoSeq(finacegivenNoSequence);
		cotCfgNo.setFincaeaccountdealNoSeq(fincaeaccountdealNoSequence);
		cotCfgNo.setFincaeaccountrecvNoSeq(fincaeaccountrecvNoSequence);
		cotCfgNo.setBacktaxNoSeq(backtaxNoSequence);
		cotCfgNo.setAuditNoSeq(auditNoSequence);
		cotCfgNo.setPackingNoSeq(packingNoSequence);
		cotCfgNo.setAccessNoSeq(accessNoSequence);
		cotCfgNo.setCustNoSeq(custNoSequence);
		cotCfgNo.setFacNoSeq(facNoSequence);
		
		cotCfgNo.setPriceZeroType(priceZeroType);
		cotCfgNo.setOrderZeroType(orderZeroType);
		cotCfgNo.setOrderfacZeroType(orderfacZeroType);
		cotCfgNo.setOrderoutZeroType(orderoutZeroType);
		cotCfgNo.setGivenZeroType(givenZeroType);
		cotCfgNo.setSignZeroType(signZeroType);
		cotCfgNo.setSplitZeroType(splitZeroType);
		cotCfgNo.setAutoorderfacZeroType(autoorderfacZeroType);
		cotCfgNo.setFinacerecvZeroType(finacerecvZeroType);
		cotCfgNo.setFinacegivenZeroType(finacegivenZeroType);
		cotCfgNo.setFincaeaccountdealZeroType(fincaeaccountdealZeroType);
		cotCfgNo.setFincaeaccountrecvZeroType(fincaeaccountrecvZeroType);
		cotCfgNo.setBacktaxZeroType(backtaxZeroType);
		cotCfgNo.setAuditZeroType(auditZeroType);
		cotCfgNo.setPackingZeroType(packingZeroType);
		cotCfgNo.setAccessZeroType(accessZeroType);
		cotCfgNo.setCustZeroType(custZeroType);
		cotCfgNo.setFacZeroType(facZeroType);
		return cotCfgNo;
	}
	
	//替换
	public String doReplace(String str){
		str = str.replace("[YYYY]","%1$tY");
		str = str.replace("[YY]","%1$ty");
		str = str.replace("[MM]","%1$tm");
		str = str.replace("[DD]","%1$td");
		str = str.replace("[2SEQ]","%2$02d");
		str = str.replace("[3SEQ]","%2$03d");
		str = str.replace("[4SEQ]","%2$04d");
		str = str.replace("[2KHSEQ]","_KH%3$02d");
		str = str.replace("[3KHSEQ]","_KH%3$03d");
		str = str.replace("[4KHSEQ]","_KH%3$04d");
		 
		return str;
	}
	
	//获取序列号
	public String getSequence(String str){
		if(str.indexOf("[2SEQ]")>=0){
			str = "00";
			return str;
		}
		if(str.indexOf("[3SEQ]")>=0){
			str = "000";
			return str;
		}
		if(str.indexOf("[4SEQ]")>=0){
			str = "0000";
			return str;
		}
		else{
			str = "0000";
			return str;
		}
	}
	
	//还原替换
	public String backReplace(String str){
		str = str.replace("%1$tY","[YYYY]");
		str = str.replace("%1$ty","[YY]");
		str = str.replace("%1$tm","[MM]");
		str = str.replace("%1$td","[DD]");
		str = str.replace("%2$02d","[2SEQ]");
		str = str.replace("%2$03d","[3SEQ]");
		str = str.replace("%2$04d","[4SEQ]");
		str = str.replace("_KH%3$02d","[2KHSEQ]");
		str = str.replace("_KH%3$03d","[3KHSEQ]");
		str = str.replace("_KH%3$04d","[4KHSEQ]");
		
		return str;
	}
	
	
	
	//获取报价单号
	/**
	 * 描述：
	 * @param custId
	 * @param currdate 报价单的当前时间
	 * @return
	 * 返回值：String
	 */
	public String getPriceNo(Integer custId,String currdate){
		String priceNo = SystemUtil.getProperty(filepath, "priceNo");
		String today = this.getCurrentDate();
		String otherDay = SystemUtil.getProperty(filepath, "currentDate");
		Integer custSeq = 0;
		if(custId!=null && priceNo.indexOf("[KH]")>=0){
			CotCustomer cotCustomer = (CotCustomer) this.getCotBaseDao().getById(CotCustomer.class, custId);
			String customerNo = cotCustomer.getCustomerNo();
			if(customerNo!=null){
				priceNo = priceNo.replace("[KH]",customerNo);
			}
			if(priceNo.indexOf("_KH")>=0)
			{
				custSeq = this.updateCustSeq(cotCustomer, priceNo,"price",currdate);
			}
		}
		//获取序列号
		Integer intPriceNoSeq = this.getSeq("price",currdate);	
		priceNo = priceNo.replace("_KH", "");
		java.util.Calendar cal = java.util.Calendar.getInstance();
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		try {
			cal.setTime(format.parse(currdate));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String str = String.format(priceNo,cal,intPriceNoSeq,custSeq);
		return str;
	}
	
	//获取订单号
	public String getOrderNo(Integer custId,String currdate){
		String orderNo = SystemUtil.getProperty(filepath, "orderNo");
		String today = this.getCurrentDate();
		String otherDay = SystemUtil.getProperty(filepath, "currentDate");
		Integer custSeq = 0;
		if(custId!=null && orderNo.indexOf("[KH]")>=0){
			CotCustomer cotCustomer = (CotCustomer) this.getCotBaseDao().getById(CotCustomer.class, custId);
			String customerNo = cotCustomer.getCustomerNo();
			if(customerNo!=null){
				orderNo = orderNo.replace("[KH]",customerNo);
			}
			if(orderNo.indexOf("_KH")>=0)
			{
				custSeq = this.updateCustSeq(cotCustomer, orderNo,"order",currdate);
			}
		}
		Integer intOrderNoSeq = this.getSeq("order",currdate);
		orderNo = orderNo.replace("_KH", "");
		java.util.Calendar cal = java.util.Calendar.getInstance();
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		try {
			cal.setTime(format.parse(currdate));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String str = String.format(orderNo,cal,intOrderNoSeq,custSeq);
		
		return str;
	}
	
	//获取送样单号
	public String getGivenNo(Integer custId,String currdate){
		String givenNo = SystemUtil.getProperty(filepath, "givenNo");
		String today = this.getCurrentDate();
		String otherDay = SystemUtil.getProperty(filepath, "currentDate");
		Integer custSeq = 0;
		if(custId!=null && givenNo.indexOf("[KH]")>=0){
			CotCustomer cotCustomer = (CotCustomer) this.getCotBaseDao().getById(CotCustomer.class, custId);
			String customerNo = cotCustomer.getCustomerNo();
			if(customerNo!=null){
				givenNo = givenNo.replace("[KH]",customerNo);
			}
			if(givenNo.indexOf("_KH")>=0)
			{
				custSeq = this.updateCustSeq(cotCustomer, givenNo,"given",currdate);
			}
		}
		
		Integer intGivenNoSeq = this.getSeq("given",currdate);
		givenNo = givenNo.replace("_KH", "");
		java.util.Calendar cal = java.util.Calendar.getInstance();
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		try {
			cal.setTime(format.parse(currdate));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String str = String.format(givenNo,cal,intGivenNoSeq,custSeq);

		return str;
	}
	
	//获取征样单号
	public String getSignNo(String givenNo,Integer factoryId,String currdate){
		String signNo = SystemUtil.getProperty(filepath, "signNo");
		if(factoryId!=null && signNo.indexOf("[CH]")>=0){
			CotFactory cotFactory = (CotFactory) this.getCotBaseDao().getById(CotFactory.class, factoryId);
			String factoryNo = cotFactory.getFactoryNo();
			if(factoryNo!=null){
				signNo = signNo.replace("[CH]",factoryNo);
			}
		}
		if(givenNo!=null && signNo.indexOf("[GIVENNO]")>=0)
		{
			signNo = signNo.replace("[GIVENNO]",givenNo);
		}
		Integer intSignNoSeq = this.getSeq("sign",currdate);
		java.util.Calendar cal = java.util.Calendar.getInstance();
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		try {
			cal.setTime(format.parse(currdate));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String str = String.format(signNo,cal,intSignNoSeq);
		
		return str;
	}

	//获取生产合同单号
	public String getOrderFacNo(Integer factoryId,String currdate){
		String orderFacNo = SystemUtil.getProperty(filepath, "orderFacNo");
		if(factoryId!=null && orderFacNo.indexOf("[CH]")>=0){
			CotFactory cotFactory = (CotFactory) this.getCotBaseDao().getById(CotFactory.class, factoryId);
			String factoryNo = cotFactory.getFactoryNo();
			if(factoryNo!=null){
				orderFacNo = orderFacNo.replace("[CH]",factoryNo);
			}
		}
		Integer intOrderFacNoSeq = this.getSeq("orderfac",currdate);
		java.util.Calendar cal = java.util.Calendar.getInstance();
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		try {
			cal.setTime(format.parse(currdate));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String str = String.format(orderFacNo,cal,intOrderFacNoSeq);
		return str;
	}
	
	//获取出货单号
	public String getOrderOutNo(Integer custId,String currdate){
		String orderOutNo = SystemUtil.getProperty(filepath, "orderOutNo");
		String today = this.getCurrentDate();
		String otherDay = SystemUtil.getProperty(filepath, "currentDate");
		Integer custSeq = 0;
		if(custId!=null && orderOutNo.indexOf("[KH]")>=0){
			CotCustomer cotCustomer = (CotCustomer) this.getCotBaseDao().getById(CotCustomer.class, custId);
			String customerNo = cotCustomer.getCustomerNo();
			if(customerNo!=null){
				orderOutNo = orderOutNo.replace("[KH]",customerNo);
			}
			if(orderOutNo.indexOf("_KH")>=0)
			{
				//获取当前序列号，不更新数据库
				custSeq = this.updateCustSeq(cotCustomer, orderOutNo,"orderout",currdate);
			}
		}
		Integer intOrderOutNoSeq = this.getSeq("orderout",currdate);
		java.util.Calendar cal = java.util.Calendar.getInstance();
		orderOutNo = orderOutNo.replace("_KH", "");
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		try {
			cal.setTime(format.parse(currdate));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String str = String.format(orderOutNo,cal,intOrderOutNoSeq,custSeq);
		return str;
	}
	
	//获取排载单号
	public String getContainerNo(String currdate){
		
		String containerNo = SystemUtil.getProperty(filepath, "containerNo");
		Integer intContainerNoSeq = this.getSeq("split",currdate);
		java.util.Calendar cal = java.util.Calendar.getInstance();
		String str = String.format(containerNo,cal,intContainerNoSeq);
		return str;
	}

	/* (non-Javadoc)
	 * @see com.sail.cot.service.system.SetNoService#getAutoOrderFacNo(java.lang.String)
	 */
	//获取自动分解单号
	public String getAutoOrderFacNo(String orderNo,Integer factoryId,String currdate) {
		String autoOrderFacNo = SystemUtil.getProperty(filepath, "autoOrderFacNo");
		if(factoryId!=null && autoOrderFacNo.indexOf("[CH]")>=0){
			CotFactory cotFactory = (CotFactory) this.getCotBaseDao().getById(CotFactory.class, factoryId);
			String factoryNo = cotFactory.getFactoryNo();
			if(factoryNo!=null){
				autoOrderFacNo = autoOrderFacNo.replace("[CH]",factoryNo);
			}
		}
		if(autoOrderFacNo != null && !autoOrderFacNo.equals("") && autoOrderFacNo.indexOf("[ORDERNO]") >=0)
		{
			autoOrderFacNo = autoOrderFacNo.replace("[ORDERNO]", orderNo);
		}
		Integer intOrderFacNoSeq = this.getSeq("autoFac",currdate);
		
		java.util.Calendar cal = java.util.Calendar.getInstance();
		HashMap map = new HashMap();
		String str = String.format(autoOrderFacNo,cal,intOrderFacNoSeq);
		map.put("autoOrderFacNoSequence", intOrderFacNoSeq.toString());
		map.put("autoorderfacHisDate", currdate);
		SystemUtil.setPropertyFile(map, filepath);//自动序列号要直接更新
		return str;
	}
	private Integer updateCustSeq(CotCustomer customer,String abstractNo,String type,String currDate)
	{
		String strSql = " from  CotCustSeq obj where obj.custId="+customer.getId()+" and obj.currDate='"+currDate+"'";
		List seq = this.getCotBaseDao().find(strSql);
		CotCustSeq custSeq = null;
		if(seq == null || seq.size() == 0)
			custSeq = null;
		else
			custSeq = (CotCustSeq)seq.get(0);
		Integer currentCustSeq =0;
		if(custSeq == null) //不存在记录是，添加一条记录
		{
			custSeq = new CotCustSeq();
			custSeq.setCustId(customer.getId());
			custSeq.setGivenSeq(0);
			custSeq.setOrderfacSeq(0);
			custSeq.setOrderoutSeq(0);
			custSeq.setOrderSeq(0);
			custSeq.setPriceSeq(0);
			custSeq.setSignSeq(0);
			custSeq.setSplitSeq(0);
			custSeq.setFinacegivenSeq(0);
			custSeq.setFinacerecvSeq(0);
			custSeq.setFincaeaccountdealSeq(0);
			custSeq.setFincaeaccountrecvSeq(0);
			custSeq.setBacktaxSeq(0);
			custSeq.setAuditSeq(0);
			custSeq.setPackingSeq(0);
			custSeq.setAccessSeq(0);
			SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd"); 
			try {
				custSeq.setCurrDate(new java.sql.Date(sdf.parse(currDate).getTime()));
			} catch (ParseException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			List res = new ArrayList();
			res.add(custSeq);
			try {
				this.getCotBaseDao().saveRecords(res);
			} catch (DAOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		if(custSeq != null) //数据库中存在记录
		{
			//判断是否同一天，如果不是同一天，则归0
			custSeq = this.updateCustSeqToZero(custSeq, 0);
			if("price".equals(type))
				currentCustSeq = custSeq.getPriceSeq();
			else if("order".equals(type))
				currentCustSeq = custSeq.getOrderSeq();
			else if("orderfac".equals(type))
				currentCustSeq = custSeq.getOrderfacSeq();
			else if("orderout".equals(type))
				currentCustSeq = custSeq.getOrderoutSeq();
			else if("given".equals(type))
				currentCustSeq = custSeq.getGivenSeq();
			else if("sign".equals(type))
				currentCustSeq = custSeq.getSignSeq();
			else if("split".equals(type))
				currentCustSeq = custSeq.getSplitSeq();
		}
		if(currentCustSeq == null)
		{
			currentCustSeq = 0;
		}
		currentCustSeq = currentCustSeq+1;
		if(abstractNo.indexOf("%3$02d")>=0 && currentCustSeq > 99)
		{
			currentCustSeq = 1;
		}
		if(abstractNo.indexOf("%3$03d")>=0 && currentCustSeq > 999)
		{
			currentCustSeq = 1;
		}
		if(abstractNo.indexOf("%3$04d")>=0 && currentCustSeq > 999)
		{
			currentCustSeq = 1;
		}
		return currentCustSeq;
	}	
	private String getCurrentDate()
	{
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");      
		Calendar rightNow = Calendar.getInstance();
		Date now = rightNow.getTime();
		String today = sdf.format(now).toString();
		return today;
	}
	/* (non-Javadoc)
	 * @see com.sail.cot.service.customer.CotCustomerService#updateCustSeq(java.lang.Integer, java.lang.Integer)
	 */
	private CotCustSeq updateCustSeqToZero(CotCustSeq customerSeq, Integer custSeq) {
		
		Calendar currcal = Calendar.getInstance(); //获取历史比对时间
		Calendar today = Calendar.getInstance(); //获取当前时间
		currcal.setTime(customerSeq.getCurrDate());
		CotCfgNo cotCfgNo = this.getNoMap();
		//获取报价单的归0方式
		HashMap<String,String> map = new HashMap<String,String>();
		map.put("priceSeq", cotCfgNo.getPriceZeroType());
		map.put("orderSeq", cotCfgNo.getOrderZeroType());
		map.put("givenSeq", cotCfgNo.getGivenZeroType());
		map.put("signSeq", cotCfgNo.getSignZeroType());
		map.put("orderfacSeq", cotCfgNo.getOrderfacZeroType());
		map.put("orderoutSeq", cotCfgNo.getOrderoutZeroType());
		map.put("splitSeq", cotCfgNo.getSplitZeroType());
		Iterator iterator = map.keySet().iterator();
		try
		{
		while(iterator.hasNext())
		{
			String key = (String)iterator.next();
			int zeroType = Integer.parseInt(map.get(key).toString());
				switch(zeroType)
				{
					case 1: //按年归档
					{
						if(today.get(Calendar.YEAR) != currcal.get(Calendar.YEAR))
							BeanUtils.setProperty(customerSeq, key, 0);
						break;
					}
					case 2: //按月归档
					{
						if(today.get(Calendar.MONTH) != currcal.get(Calendar.MONTH))
							BeanUtils.setProperty(customerSeq, key, 0);
						break;
					}
					case 3: //按日归档
//					{
//						if(today.get(Calendar.DATE) != currcal.get(Calendar.DATE))
//							BeanUtils.setProperty(customerSeq, key, 0);
//						break;
//					}	
				}
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		customerSeq.setCurrDate(customerSeq.getCurrDate());
		List res = new ArrayList();
		res.add(customerSeq);
		this.getCotBaseDao().updateRecords(res);
		return customerSeq;
	}
	//获取全局序列号
	/**
	 * 描述：
	 * @param type 标识是需要获取那种类型的序列号
	 * @param currDate 当前时间
	 * @return 序列号
	 * 返回值：Integer
	 */
	private Integer getSeq(String type,String currDate)
	{
		CotCfgNo cotCfgNo = this.getNoMap();
		String orderno = "";
		Integer currSeq = 0;
		String hisDate = "";//历史比对时间
		int zeroType = 0;
		HashMap map = new HashMap();
		//判断是否同一天，如果不是同一天，则归0
		String currdate = SystemUtil.getProperty(filepath, "currentDate");
		Calendar currcal = Calendar.getInstance(); //获取历史比对时间
		Calendar today = Calendar.getInstance(); //获取当前时间
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd");         
		
		if("price".equals(type))
		{
			String zero = cotCfgNo.getPriceZeroType();
			orderno = cotCfgNo.getPriceNo();
			zeroType = Integer.parseInt(zero);
			currSeq = new Integer(cotCfgNo.getPriceNoSeq());
			hisDate = SystemUtil.getProperty(filepath, "priceHisDate");
			map.put("priceHisDate", currDate);
		}
		else if("order".equals(type))
		{
			String zero = cotCfgNo.getOrderZeroType();
			orderno = cotCfgNo.getOrderNo();
			zeroType = Integer.parseInt(zero);
			currSeq = new Integer(cotCfgNo.getOrderNoSeq());
			hisDate = SystemUtil.getProperty(filepath, "orderHisDate");
			map.put("orderHisDate", currDate);
		}
		else if("orderfac".equals(type))
		{
			String zero = cotCfgNo.getOrderfacZeroType();
			orderno = cotCfgNo.getOrderFacNo();
			zeroType = Integer.parseInt(zero);
			currSeq = new Integer(cotCfgNo.getOrderFacNoSeq());
			hisDate = SystemUtil.getProperty(filepath, "orderfacHisDate");
			map.put("orderfacHisDate", currDate);
		}
		else if("orderout".equals(type))
		{
			String zero = cotCfgNo.getOrderoutZeroType();
			orderno = cotCfgNo.getOrderOutNo();
			zeroType = Integer.parseInt(zero);
			currSeq = new Integer(cotCfgNo.getOrderOutNoSeq());
			hisDate = SystemUtil.getProperty(filepath, "orderoutHisDate");
			map.put("orderoutHisDate", currDate);
		}
		else if("given".equals(type))
		{
			String zero = cotCfgNo.getGivenZeroType();
			orderno = cotCfgNo.getGivenNo();
			zeroType = Integer.parseInt(zero);
			currSeq = new Integer(cotCfgNo.getGivenNoSeq());
			hisDate = SystemUtil.getProperty(filepath, "givenHisDate");
			map.put("givenHisDate", currDate);
		}
		else if("sign".equals(type))
		{
			String zero = cotCfgNo.getSignZeroType();
			orderno = cotCfgNo.getSignNo();
			zeroType = Integer.parseInt(zero);
			currSeq = new Integer(cotCfgNo.getSignNoSeq());
			hisDate = SystemUtil.getProperty(filepath, "signHisDate");
			map.put("signHisDate", currDate);
			map.put("signNoSequence", String.valueOf((currSeq+1)));
		}
		else if("split".equals(type))
		{
			String zero = cotCfgNo.getSplitZeroType();
			orderno = cotCfgNo.getContainerNo();
			zeroType = Integer.parseInt(zero);
			currSeq = new Integer(cotCfgNo.getContainerNoSeq());
			hisDate = SystemUtil.getProperty(filepath, "splitHisDate");
			map.put("splitHisDate", currDate);
		}
		else if("autoFac".equals(type))
		{
			String zero = cotCfgNo.getAutoorderfacZeroType();
			orderno = cotCfgNo.getAutoOrderFacNo();
			zeroType = Integer.parseInt(zero);	
			currSeq = new Integer(cotCfgNo.getAutoOrderFacNoSeq());
			hisDate = SystemUtil.getProperty(filepath, "autoorderfacHisDate");
			map.put("autoorderfacHisDate", currDate);
		}
		try {
			currcal.setTime(format.parse(hisDate));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(orderno.indexOf("2SEQ")>=0)
			currSeq = currSeq+1;
		else if(orderno.indexOf("3SEQ")>=0)
			currSeq = currSeq+1;
		else if(orderno.indexOf("4SEQ")>=0)
			currSeq = currSeq+1;
		if(orderno.indexOf("2SEQ")>=0 && currSeq > 99)
			currSeq = 0;
		else if(orderno.indexOf("3SEQ")>=0 && currSeq > 999)
			currSeq = 0;
		else if(orderno.indexOf("4SEQ")>=0 && currSeq > 9999)
			currSeq = 0;
		switch(zeroType)
		{
			case 1: //按年归档
			{
				if(today.get(Calendar.YEAR) != currcal.get(Calendar.YEAR))
					currSeq = 1;
				break;
			}
			case 2: //按月归档
			{
				if(today.get(Calendar.MONTH) != currcal.get(Calendar.MONTH))
					currSeq = 1;
				break;
			}
			case 3: //按日归档
//			{
//				if(today.get(Calendar.DATE) != currcal.get(Calendar.DATE))
//					currSeq = 1;
//				break;
//			}	
		}
		SystemUtil.setPropertyFile(map, filepath);//保存历史时间
		return currSeq;
	}
	public void saveSeq(String type,String currDate)
	{
		Integer currSeq = this.getSeq(type,currDate);
		HashMap map = new HashMap();	
		if("price".equals(type))
		{
			map.put("priceNoSequence", currSeq.toString());
			map.put("priceHisDate", currDate);
		}
		else if("order".equals(type))
		{
			map.put("orderNoSequence", currSeq.toString());
			map.put("orderHisDate", currDate);
		}
		else if("given".equals(type))
		{
			map.put("givenNoSequence", currSeq.toString());
			map.put("givenHisDate", currDate);
		}
		else if("sign".equals(type))
		{
			map.put("signNoSequence", currSeq.toString());
			map.put("signHisDate", currDate);
		}
		else if("orderfac".equals(type))
		{
			map.put("orderFacNoSequence", currSeq.toString());
			map.put("orderfacHisDate", currDate);
		}
		else if("orderout".equals(type))
		{
			map.put("orderOutNoSequence", currSeq.toString());
			map.put("orderoutHisDate", currDate);
		}
		else if("split".equals(type))
		{
			map.put("containerNoSequence", currSeq.toString());
			map.put("splitHisDate", currDate);
		}
		else if("autoFac".equals(type))
		{
			map.put("autoOrderFacNoSequence", currSeq.toString());
			map.put("autoorderfacHisDate", currDate);
		}
		map.put("currentDate", this.getCurrentDate());
		SystemUtil.setPropertyFile(map, filepath);
	}

	//获取自动分解的采购单号
	public String getCotOrderfacNO(Integer orderId,Integer factoryId){
		String orderFacNo = null;
		String currDate = ContextUtil.getCurrentDate("yyyy-MM-dd");
		CotOrder cotOrder = (CotOrder) this.getCotBaseDao().getById(
				CotOrder.class, orderId);
		orderFacNo = this.getAutoOrderFacNo(cotOrder.getOrderNo(), factoryId, currDate);
		return orderFacNo;
	}
}

