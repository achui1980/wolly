/**
 * 
 */
package com.sail.cot.dao.sample.impl;

import java.io.File;
import java.io.FileInputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sourceforge.jeval.EvaluationException;
import net.sourceforge.jeval.Evaluator;

import com.sail.cot.dao.impl.CotBaseDaoImpl;
import com.sail.cot.dao.sample.CotReportDao;
import com.sail.cot.domain.CotBoxPacking;
import com.sail.cot.domain.CotBoxType;
import com.sail.cot.domain.CotContainerType;
import com.sail.cot.domain.CotCurrency;
import com.sail.cot.domain.CotEleCfg;
import com.sail.cot.domain.CotEleOther;
import com.sail.cot.domain.CotElePrice;
import com.sail.cot.domain.CotElementsNew;
import com.sail.cot.domain.CotFactory;
import com.sail.cot.domain.CotGivenDetail;
import com.sail.cot.domain.CotOrderDetail;
import com.sail.cot.domain.CotOrderOutdetail;
import com.sail.cot.domain.CotPriceCfg;
import com.sail.cot.domain.CotPriceDetail;
import com.sail.cot.domain.CotTypeLv1;
import com.sail.cot.domain.CotTypeLv2;
import com.sail.cot.domain.CotTypeLv3;
import com.sail.cot.service.sample.impl.CotReportServiceImpl;
import com.sail.cot.util.SystemDicUtil;

public class CotReportDaoImpl extends CotBaseDaoImpl implements CotReportDao {
	
	private SystemDicUtil systemDicUtil = new SystemDicUtil();

	/**
	 * 判断样品编号是否重复
	 * @param empsId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Integer isExistEleId(String eleId) {
		List<Integer> res = new ArrayList<Integer>();
		res = super.find("select c.id from CotElementsNew c where c.eleId='"
						+ eleId.trim()+"'");
		if (res.size() != 1) {
			return null;
		} else {
			return res.get(0);
		}
	}

	/**
	 * 判断类别名称是否存在
	 * @param typeName
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Integer isExistTypeName(String typeEnName) {
		List<Integer> res = new ArrayList<Integer>();
		res = super.find("select c.id from CotTypeLv1 c where c.typeEnName='"
				+ typeEnName + "'");
		if (res.size() != 0) {
			return res.get(0);
		} else {
			return null;
		}
	}
	
	/**
	 * 判断产品分类是否存在
	 * @param typeName
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Integer isExistEleTypeName(String typeName) {
		List<Integer> res = new ArrayList<Integer>();
		res = super.find("select c.id from CotTypeLv2 c where c.typeName='"
				+ typeName + "'");
		if (res.size() != 0) {
			return res.get(0);
		} else {
			return null;
		}
	}

	/**
	 * 判断厂家简称是否存在
	 * @param shortName
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Integer isExistFactoryShortName(String shortName) {
		List<Integer> res = new ArrayList<Integer>();
		res = super.find("select c.id from CotFactory c where c.shortName='"
				+ shortName + "'");
		if (res.size() != 1) {
			return null;
		} else {
			return res.get(0);
		}
	}

	/**
	 * 判断海关编吗是否存在
	 * @param hsId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Integer isExistHsId(String hsId) {
		List<Integer> res = new ArrayList<Integer>();
		res = super.find("select e.id from CotEleOther e where e.hscode='"
				+ hsId + "'");
		if (res.size() != 1) {
			return null;
		} else {
			return res.get(0);
		}
	}

	/**
	 * 判断币种单位是否存在
	 * @param curName
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Integer isExistCurUnit(String curUnit) {
		List<Integer> res = new ArrayList<Integer>();
		res = super.find("select c.id from CotCurrency c where c.curNameEn='"+curUnit+"'");
		if (res.size() ==0) {
			return null;
		} else {
			return res.get(0);
		}
	}
	
	//得到所有配件编号,返回map(key为配件编号,value为配件Id)
	public Map<String, Integer> getAllFitNo(){
		String sql = "select obj.id,obj.fitNo from CotFittings obj";
		List<?> list = super.find(sql);
		Map<String, Integer> map = new HashMap<String, Integer>();
		Iterator<?> it = list.iterator();
		while(it.hasNext()){
			Object[] obj = (Object[]) it.next();
			map.put(((String)obj[1]).toLowerCase(), (Integer)obj[0]);
		}
		return map;
	}

	//得到所有样品货号的编号,返回map(key为样品编号,value为类别Id)
	public Map<String, Integer> getAllEleId(){
		String sql = "select obj.id,obj.eleId from CotElementsNew obj";
		List<?> list = super.find(sql);
		Map<String, Integer> map = new HashMap<String, Integer>();
		Iterator<?> it = list.iterator();
		while(it.hasNext()){
			Object[] obj = (Object[]) it.next();
			map.put(((String)obj[1]).toLowerCase(), (Integer)obj[0]);
		}
		return map;
	}
	
	//得到所有样品父货号的编号,返回map(key为样品编号,value为类别Id)
	public Map<String, Integer> getAllParentEleId(){
		String sql = "select obj.id,obj.eleId from CotElementsNew obj where obj.eleFlag!=2 and obj.eleParentId is null";
		List<?> list = super.find(sql);
		Map<String, Integer> map = new HashMap<String, Integer>();
		Iterator<?> it = list.iterator();
		while(it.hasNext()){
			Object[] obj = (Object[]) it.next();
			map.put(((String)obj[1]).toLowerCase(), (Integer)obj[0]);
		}
		return map;
	}
	
	//得到所有样品材质返回map(key为材质中文名称,value为类别Id)
	public Map<String, Integer> getAllTypeCn(){
		List<?> list =systemDicUtil.getDicListByName("typelv1");
		Map<String, Integer> map = new HashMap<String, Integer>();
		Iterator<?> it = list.iterator();
		while(it.hasNext()){
			CotTypeLv1 cotTypeLv1 = (CotTypeLv1) it.next();
			map.put(cotTypeLv1.getTypeName().toLowerCase(), cotTypeLv1.getId());
		}
		return map;
	}
	
	//得到所有订单明细
	public List<?> getOrderDetails(Integer orderId){
		String sql = "from CotOrderDetail obj where obj.orderId=?";
		Object[] parm = new Object[1];
		parm[0]=orderId;
		List list=this.queryForLists(sql,parm);
		return list;
	}
	
	//得到所有报价明细
	public List<?> getPriceDetails(Integer orderId){
		String sql = "from CotPriceDetail obj where obj.priceId=?";
		Object[] parm = new Object[1];
		parm[0]=orderId;
		List list=this.queryForLists(sql,parm);
		return list;
	}
	
	//得到所有寄样明细
	public List<?> getGivenDetails(Integer givenId){
		String sql = "from CotGivenDetail obj where obj.givenId=?";
		Object[] parm = new Object[1];
		parm[0]=givenId;
		List list=this.queryForLists(sql,parm);
		return list;
	}
	
	//得到所有样品材质返回map(key为材质英文名称,value为类别Id)
	public Map<String, Integer> getAllTypeEn(){
		List<?> list =systemDicUtil.getDicListByName("typelv1");
		Map<String, Integer> map = new HashMap<String, Integer>();
		Iterator<?> it = list.iterator();
		while(it.hasNext()){
			CotTypeLv1 cotTypeLv1 = (CotTypeLv1) it.next();
			map.put(cotTypeLv1.getTypeEnName().toLowerCase(), cotTypeLv1.getId());
		}
		return map;
	}
	
	//得到所有产品类别返回map(key为类别名称,value为类别Id)
	public Map<String, Integer> getAllEleType(){
		List<?> list =systemDicUtil.getDicListByName("typelv2");
		Map<String, Integer> map = new HashMap<String, Integer>();
		Iterator<?> it = list.iterator();
		while(it.hasNext()){
			CotTypeLv2 cotTypeLv2 = (CotTypeLv2) it.next();
			map.put(cotTypeLv2.getTypeName().toLowerCase(), cotTypeLv2.getId());
		}
		return map;
	}
	
	//得到所有币种,返回map(key为币种名称,value为类别Id)
	public Map<String, Integer> getAllCurrency(){
		List<?> list =systemDicUtil.getDicListByName("currency");
		Map<String, Integer> map = new HashMap<String, Integer>();
		Iterator<?> it = list.iterator();
		while(it.hasNext()){
			CotCurrency currency = (CotCurrency) it.next();
			map.put(currency.getCurNameEn().toLowerCase(), currency.getId());
		}
		return map;
	}
	
	//得到所有厂家简称,返回map(key为厂家简称,value为类别Id)
	public Map<String, Integer> getAllFactoryShortName(){
		//List<?> list =systemDicUtil.getDicListByName("factory");
		List<?> list =super.getRecords("CotFactory");
		Map<String, Integer> map = new HashMap<String, Integer>();
		Iterator<?> it = list.iterator();
		while(it.hasNext()){
			CotFactory factory = (CotFactory) it.next();
			map.put(factory.getShortName().toLowerCase(), factory.getId());
		}
		return map;
	}
	
	//得到所有配件类别,返回map(key为配件类别名称,value为类别Id)
	public Map<String, Integer> getTypeLv3Name(){
		List<?> list =systemDicUtil.getDicListByName("typelv3");
		Map<String, Integer> map = new HashMap<String, Integer>();
		Iterator<?> it = list.iterator();
		while(it.hasNext()){
			CotTypeLv3 typeLv3 = (CotTypeLv3) it.next();
			map.put(typeLv3.getTypeName().toLowerCase(), typeLv3.getId());
		}
		return map;
	}
	
	//得到所有海关编码,返回map(key为海关编码,value为Id)
	public Map<String, Integer> getAllHsId(){
		List<?> list =systemDicUtil.getDicListByName("eleother");
		Map<String, Integer> map = new HashMap<String, Integer>();
		Iterator<?> it = list.iterator();
		while(it.hasNext()){
			CotEleOther eleOther = (CotEleOther) it.next();
			map.put(eleOther.getHscode().toLowerCase(), eleOther.getId());
		}
		return map;
	}
	
	//得到所有包装类型返回map(key为类别名称,value为类别Id)
	public Map<String, String[]> getAllBoxTypeByCn(){
		List<?> list = super.getRecords("CotBoxType");
		Map<String, String[]> map = new HashMap<String, String[]>();
		for (int i = 0; i < list.size(); i++) {
			String[] box = new String[5];  
			CotBoxType boxType = (CotBoxType)list.get(i);
			box[0]=boxType.getId().toString();
			if(boxType.getBoxIName()!=null){
				box[1] = boxType.getBoxIName().toString();
			}else{
				box[1] =null;
			}
			if(boxType.getBoxMName()!=null){
				box[2] = boxType.getBoxMName().toString();
			}else{
				box[2] =null;
			}
			if(boxType.getBoxOName()!=null){
				box[3] = boxType.getBoxOName().toString();
			}else{
				box[3] =null;
			}
			if(boxType.getBoxPName()!=null){
				box[4] = boxType.getBoxPName().toString();
			}else{
				box[4] =null;
			}
			
			map.put(boxType.getTypeName().toLowerCase(), box);
		}
		return map;
	}
	
	//得到所有包装类型返回map(key为类别名称,value为类别Id)
	public Map<String, String[]> getAllBoxTypeByEn(){
		List<?> list = super.getRecords("CotBoxType");
		Map<String, String[]> map = new HashMap<String, String[]>();
		for (int i = 0; i < list.size(); i++) {
			String[] box = new String[5];  
			CotBoxType boxType = (CotBoxType)list.get(i);
			box[0]=boxType.getId().toString();
			if(boxType.getBoxIName()!=null){
				box[1] = boxType.getBoxIName().toString();
			}else{
				box[1] =null;
			}
			if(boxType.getBoxMName()!=null){
				box[2] = boxType.getBoxMName().toString();
			}else{
				box[2] =null;
			}
			if(boxType.getBoxOName()!=null){
				box[3] = boxType.getBoxOName().toString();
			}else{
				box[3] =null;
			}
			if(boxType.getBoxPName()!=null){
				box[4] = boxType.getBoxPName().toString();
			}else{
				box[4] =null;
			}
			
			map.put(boxType.getTypeNameEn().toLowerCase(), box);
		}
		return map;
	}
	
	
	//设置导入时样品的默认值
	public CotElementsNew setDefault(CotEleCfg cfg,CotElementsNew e,Boolean isExist){
		if(!isExist){
			e.setEleTypeidLv1(cfg.getEleTypeId());
			e.setFactoryId(cfg.getEleFacId());
			e.setPriceFacUint(cfg.getElePriceFacUnit());
			e.setPriceOutUint(cfg.getElePriceOutUnit());
			e.setBoxIbCount(cfg.getBoxIbCount());
			e.setBoxMbCount(cfg.getBoxMbCount());
			e.setBoxObCount(cfg.getBoxObCount());
			e.setBoxTypeId(cfg.getBoxTypeId());
			e.setBoxIbType(cfg.getBoxIbType());
			e.setBoxMbType(cfg.getBoxMbType());
			e.setBoxObType(cfg.getBoxObType());
			e.setEleUnit(cfg.getEleUnit());
			e.setEleFlag(cfg.getEleFlag());
			e.setEleUnitNum(cfg.getEleUnitnum());
			//报价设为默认0
			e.setPriceFac(0f);
			e.setPriceOut(0f);
			e.setTuiLv(cfg.getTuiLv());
			if(cfg.getPriceProfit()!=null){
				e.setLiRun(cfg.getPriceProfit().floatValue());
			}
		}
		return e;
	}
	
	//设置导入时报价的默认值
	public CotPriceDetail setPriceDefault(CotEleCfg cfg,CotPriceDetail e){
		e.setEleTypeidLv1(cfg.getEleTypeId());
		e.setFactoryId(cfg.getEleFacId());
		e.setPriceFacUint(cfg.getElePriceFacUnit());
		e.setPriceOutUint(cfg.getElePriceOutUnit());
		e.setBoxIbCount(cfg.getBoxIbCount());
		e.setBoxMbCount(cfg.getBoxMbCount());
		e.setBoxObCount(cfg.getBoxObCount());
		e.setBoxTypeId(cfg.getBoxTypeId());
		e.setBoxIbType(cfg.getBoxIbType());
		e.setBoxMbType(cfg.getBoxMbType());
		e.setBoxObType(cfg.getBoxObType());
		e.setEleUnit(cfg.getEleUnit());
		e.setEleFlag(cfg.getEleFlag());
		e.setEleUnitNum(cfg.getEleUnitnum());
		//报价设为默认0
		e.setPriceFac(0f);
		e.setPriceOut(0f);
		e.setTuiLv(cfg.getTuiLv());
		if(cfg.getPriceProfit()!=null){
			e.setLiRun(cfg.getPriceProfit().floatValue());
		}
		return e;
	}
	
	//设置导入时订单的默认值
	public CotOrderDetail setOrderDefault(CotEleCfg cfg,CotOrderDetail e){
		e.setEleTypeidLv1(cfg.getEleTypeId());
		e.setFactoryId(cfg.getEleFacId());
		e.setPriceFacUint(cfg.getElePriceFacUnit());
		e.setPriceOutUint(cfg.getElePriceOutUnit());
		e.setBoxIbCount(cfg.getBoxIbCount());
		e.setBoxMbCount(cfg.getBoxMbCount());
		e.setBoxObCount(cfg.getBoxObCount());
		e.setBoxTypeId(cfg.getBoxTypeId());
		e.setBoxIbType(cfg.getBoxIbType());
		e.setBoxMbType(cfg.getBoxMbType());
		e.setBoxObType(cfg.getBoxObType());
		e.setEleUnit(cfg.getEleUnit());
		e.setEleFlag(cfg.getEleFlag());
		e.setEleUnitNum(cfg.getEleUnitnum());
		//报价设为默认0
		e.setPriceFac(0f);
		e.setPriceOut(0f);
		e.setTuiLv(cfg.getTuiLv());
		if(cfg.getPriceProfit()!=null){
			e.setLiRun(cfg.getPriceProfit().floatValue());
		}
		return e;
	}
	
	//设置导入时寄样的默认值
	public CotGivenDetail setGivenDefault(CotEleCfg cfg,CotGivenDetail e){
		e.setEleTypeidLv1(cfg.getEleTypeId());
		e.setFactoryId(cfg.getEleFacId());
		e.setPriceFacUint(cfg.getElePriceFacUnit());
		e.setPriceOutUint(cfg.getElePriceOutUnit());
		e.setBoxIbCount(cfg.getBoxIbCount());
		e.setBoxMbCount(cfg.getBoxMbCount());
		e.setBoxObCount(cfg.getBoxObCount());
		e.setBoxTypeId(cfg.getBoxTypeId());
		e.setBoxIbType(cfg.getBoxIbType());
		e.setBoxMbType(cfg.getBoxMbType());
		e.setBoxObType(cfg.getBoxObType());
		e.setEleUnit(cfg.getEleUnit());
		e.setEleFlag(cfg.getEleFlag());
		e.setEleUnitNum(cfg.getEleUnitnum());
		//报价设为默认0
		e.setPriceFac(0f);
		e.setPriceOut(0f);
		e.setTuiLv(cfg.getTuiLv());
		if(cfg.getPriceProfit()!=null){
			e.setLiRun(cfg.getPriceProfit().floatValue());
		}
		return e;
	}
	
	//获得暂无图片的图片字节
	public byte[] getZwtpPic(){
		//获得默认图片的
		//String fileLength = ContextUtil.getProperty("sysconfig.properties","maxLength");
		// 获得tomcat路径
		String classPath = CotReportServiceImpl.class.getResource("/")
				.toString();
		String systemPath = classPath.substring(6, classPath.length() - 16);
		File picFile = new File(systemPath+"common/images/zwtp.png");
		FileInputStream in;
		try {
			in = new FileInputStream(picFile);
			byte[] b = new byte[(int)picFile.length()];
			while (in.read(b)!= -1) {}
			in.close();
			return b;
		} catch (Exception e1) {
			logger.error("设置样品图片错误!");
			return null;
		}
	}
	
	//取得默认的样品配置
	public CotEleCfg getDefaultEleCfg(){
		String hql = "from CotEleCfg obj where obj.cfgFlag=1";
		List<?> list = super.find(hql);
		if(list.size()>0){
			return (CotEleCfg) list.get(0);
		}else{
			return null;
		}
	}
	
	//取得业务配置
	public CotPriceCfg getPriceCfg(){
		List list = super.getRecords("CotPriceCfg");
		if(list!=null && list.size()>0){
			return (CotPriceCfg) list.get(0);
		}else{
			return null;
		}
	}
	
	// 得到objName的集合
	public List<?> getDicList(String objName) {
		return systemDicUtil.getDicListByName(objName);
	}
	
	// 获得20/40/40HQ/45的柜体积(默认24/54/68/86)
	public Float[] getContainerCube() {
		Float[] temp = new Float[4];
		temp[0] = 24f;
		temp[1] = 54f;
		temp[2] = 68f;
		temp[3] = 86f;
		//List<?> container = this.getDicList("container");
		List<?> container = super.getRecords("CotContainerType");
		
		for (int i = 0; i < container.size(); i++) {
			CotContainerType containerType = (CotContainerType) container
					.get(i);
			if (containerType.getContainerName().equals("20C")) {
				temp[0] = containerType.getContainerCube();
			}
			if (containerType.getContainerName().equals("40HC")) {
				temp[1] = containerType.getContainerCube();
			}
			if (containerType.getContainerName().equals("40C")) {
				temp[2] = containerType.getContainerCube();
			}
			if (containerType.getContainerName().equals("45C")) {
				temp[3] = containerType.getContainerCube();
			}
		}
		return temp;
	}
	
	//根据样品默认配置的公式和厂价求外销价
	public Float getPriceOut(Map map){
		DecimalFormat df = new DecimalFormat("#.00");
		Object priceProfit = map.get("priceProfit");
		Object priceFac = map.get("priceFac");
		Object priceRate = map.get("priceRate");
		Object tuiLv = map.get("tuiLv");
		Object cbm = map.get("cbm");
		Object boxObCount = map.get("boxObCount");
		CotEleCfg cotEleCfg = this.getDefaultEleCfg();
		String expessionIn = cotEleCfg.getExpessionIn();
		if(expessionIn!=null){
			// 定义jeavl对象,用于计算字符串公式
			Evaluator evaluator = new Evaluator();
			evaluator.putVariable("priceProfit", priceProfit.toString());
			evaluator.putVariable("priceFac", priceFac.toString());
			evaluator.putVariable("priceRate", priceRate.toString());
			evaluator.putVariable("tuiLv", tuiLv.toString());
			evaluator.putVariable("cbm", cbm.toString());
			evaluator.putVariable("boxObCount", boxObCount.toString());
			Float res = null;
			try {
				String result = evaluator.evaluate(expessionIn);
				res = Float.parseFloat(df.format(Float.parseFloat(result)));
				return res;
			} catch (EvaluationException e) {
				e.printStackTrace();
				return 0f;
			}
		}else{
			return 0f;
		}
	}
	
	//通过币种id取得汇率
	public Float getCurRate(Integer id) {
		Float curRate =0f;
		List<?> list =systemDicUtil.getDicListByName("currency");
		for (int i = 0; i < list.size(); i++) {
			CotCurrency cotCurrency = (CotCurrency) list.get(i);
			if(cotCurrency.getId().intValue()==id.intValue()){
				curRate = cotCurrency.getCurRate();
				break;
			}
		}
		return curRate;
	}
	
	//判断该名称在该货号是否重复
	public CotElePrice checkElePriceName(String name,Integer eId){
		String hql = "from CotElePrice obj where obj.eleId="+eId+" and obj.priceName='"+name+"'";
		List<CotElePrice> list = super.find(hql);
		if(list.size()==1){
			return list.get(0);
		}else{
			return null;
		}
	}
	
	//查询所有包装材料
	public Map getAllBoxPacking(){
		List list = super.getRecords("CotBoxPacking");
		Map<Integer, CotBoxPacking> map = new HashMap<Integer, CotBoxPacking>();
		Iterator<?> it = list.iterator();
		while(it.hasNext()){
			CotBoxPacking boxPacking = (CotBoxPacking) it.next();
			map.put(boxPacking.getId(), boxPacking);
		}
		return map;
	}
	
	public String sumPackPrice(CotElementsNew elements,CotBoxPacking boxPacking){
		Evaluator evaluator = new Evaluator();
		// 摆放长、宽、高
		if (elements.getPutL() == null || elements.getPutL().equals("")) {
			evaluator.putVariable("putL", "0.0");
		} else {
			evaluator.putVariable("putL", elements.getPutL().toString());
		}
		if (elements.getPutW() == null || elements.getPutW().equals("")) {
			evaluator.putVariable("putW", "0.0");
		} else {
			evaluator.putVariable("putW", elements.getPutW().toString());
		}
		if (elements.getPutH() == null || elements.getPutH().equals("")) {
			evaluator.putVariable("putH", "0.0");
		} else {
			evaluator.putVariable("putH", elements.getPutH().toString());
		}
		//产品长、宽、高
		if (elements.getBoxL() == null || elements.getBoxL().equals("")) {
			evaluator.putVariable("boxL", "0.0");
		}else {
			evaluator.putVariable("boxL", elements.getBoxL().toString());
		}
		if (elements.getBoxW() == null || elements.getBoxW().equals("")) {
			evaluator.putVariable("boxW", "0.0");
		}else {
			evaluator.putVariable("boxW", elements.getBoxW().toString());
		}
		if (elements.getBoxH() == null || elements.getBoxH().equals("")) {
			evaluator.putVariable("boxH", "0.0");
		}else {
			evaluator.putVariable("boxH", elements.getBoxH().toString());
		}
		//产品包装长、宽、高
		if (elements.getBoxPbL() == null || elements.getBoxPbL().equals("")) {
			evaluator.putVariable("boxPbL", "0.0");
		}else {
			evaluator.putVariable("boxPbL", elements.getBoxPbL().toString());
		}
		if (elements.getBoxPbW() == null || elements.getBoxPbW().equals("")) {
			evaluator.putVariable("boxPbW", "0.0");
		}else {
			evaluator.putVariable("boxPbW", elements.getBoxPbW().toString());
		}
		if (elements.getBoxPbH() == null || elements.getBoxPbH().equals("")) {
			evaluator.putVariable("boxPbH", "0.0");
		}else {
			evaluator.putVariable("boxPbH", elements.getBoxPbH().toString());
		}
		//中盒包装长、宽、高
		if (elements.getBoxMbL() == null || elements.getBoxMbL().equals("")) {
			evaluator.putVariable("boxMbL", "0.0");
		}else {
			evaluator.putVariable("boxMbL", elements.getBoxMbL().toString());
		}
		if (elements.getBoxMbW() == null || elements.getBoxMbW().equals("")) {
			evaluator.putVariable("boxMbW", "0.0");
		}else {
			evaluator.putVariable("boxMbW", elements.getBoxMbW().toString());
		}
		if (elements.getBoxMbH() == null || elements.getBoxMbH().equals("")) {
			evaluator.putVariable("boxMbH", "0.0");
		}else {
			evaluator.putVariable("boxMbH", elements.getBoxMbH().toString());
		}
		//内盒包装长、宽、高
		if (elements.getBoxIbL() == null || elements.getBoxIbL().equals("")) {
			evaluator.putVariable("boxIbL", "0.0");
		}else {
			evaluator.putVariable("boxIbL", elements.getBoxIbL().toString());
		}
		if (elements.getBoxIbW() == null || elements.getBoxIbW().equals("")) {
			evaluator.putVariable("boxIbW", "0.0");
		}else {
			evaluator.putVariable("boxIbW", elements.getBoxIbW().toString());
		}
		if (elements.getBoxIbH() == null || elements.getBoxIbH().equals("")) {
			evaluator.putVariable("boxIbH", "0.0");
		}else {
			evaluator.putVariable("boxIbH", elements.getBoxIbH().toString());
		}
		//外箱包装长、宽、高
		if (elements.getBoxObL() == null || elements.getBoxObL().equals("")) {
			evaluator.putVariable("boxObL", "0.0");
		}else {
			evaluator.putVariable("boxObL", elements.getBoxObL().toString());
		}
		if (elements.getBoxObW() == null || elements.getBoxObW().equals("")) {
			evaluator.putVariable("boxObW", "0.0");
		}else {
			evaluator.putVariable("boxObW", elements.getBoxObW().toString());
		}
		if (elements.getBoxObH() == null || elements.getBoxObH().equals("")) {
			evaluator.putVariable("boxObH", "0.0");
		}else {
			evaluator.putVariable("boxObH", elements.getBoxObH().toString());
		}		
		//材料单价
		if (boxPacking.getMaterialPrice() == null || boxPacking.getMaterialPrice().equals("")) {
			evaluator.putVariable("materialPrice", "0.0");
		}else {
			evaluator.putVariable("materialPrice", boxPacking.getMaterialPrice().toString());
		}
		
		try {
			String result = evaluator.evaluate(boxPacking.getFormulaIn());
			return result;
		} catch (EvaluationException e) {
			e.printStackTrace();
			return "";
		}
	}
	
	//计算价格
	public CotElementsNew calPrice(Map map,CotElementsNew obj){
		DecimalFormat df = new DecimalFormat("#.00");
		if(obj.getBoxTypeId()!=null){
			if(obj.getBoxPbTypeId()!=null){
				CotBoxPacking pak = (CotBoxPacking)map.get(obj.getBoxPbTypeId());
				if(pak.getFormulaIn()!=null && !"".equals(pak.getFormulaIn())){
					String price = this.sumPackPrice(obj, pak);
					if(!"".equals(price)){
						Float num = Float.parseFloat(df.format(Float.parseFloat(price)));
						obj.setBoxPbPrice(num);
					}
				}else{
					obj.setBoxPbPrice(0f);
				}
			}else{
				obj.setBoxPbPrice(0f);
			}
			if(obj.getBoxIbTypeId()!=null){
				CotBoxPacking pak = (CotBoxPacking)map.get(obj.getBoxIbTypeId());
				if(pak.getFormulaIn()!=null && !"".equals(pak.getFormulaIn())){
					String price = this.sumPackPrice(obj, pak);
					if(!"".equals(price)){
						Float num = Float.parseFloat(df.format(Float.parseFloat(price)));
						obj.setBoxIbPrice(num);
					}
				}else{
					obj.setBoxIbPrice(0f);
				}
			}else{
				obj.setBoxIbPrice(0f);
			}
			if(obj.getBoxMbTypeId()!=null){
				CotBoxPacking pak = (CotBoxPacking)map.get(obj.getBoxMbTypeId());
				if(pak.getFormulaIn()!=null && !"".equals(pak.getFormulaIn())){
					String price = this.sumPackPrice(obj, pak);
					if(!"".equals(price)){
						Float num = Float.parseFloat(df.format(Float.parseFloat(price)));
						obj.setBoxMbPrice(num);
					}
				}else{
					obj.setBoxMbPrice(0f);
				}
			}else{
				obj.setBoxMbPrice(0f);
			}
			if(obj.getBoxObTypeId()!=null){
				CotBoxPacking pak = (CotBoxPacking)map.get(obj.getBoxObTypeId());
				if(pak.getFormulaIn()!=null && !"".equals(pak.getFormulaIn())){
					String price = this.sumPackPrice(obj, pak);
					if(!"".equals(price)){
						Float num = Float.parseFloat(df.format(Float.parseFloat(price)));
						obj.setBoxObPrice(num);
					}
				}else{
					obj.setBoxObPrice(0f);
				}
			}else{
				obj.setBoxObPrice(0f);
			}
			
			
			if(obj.getInputGridTypeId()!=null){
				CotBoxPacking pak = (CotBoxPacking)map.get(obj.getInputGridTypeId());
				if(pak.getFormulaIn()!=null && !"".equals(pak.getFormulaIn())){
					String price = this.sumPackPrice(obj, pak);
					if(!"".equals(price)){
						Float num = Float.parseFloat(df.format(Float.parseFloat(price)));
						obj.setInputGridPrice(num);
					}
				}else{
					obj.setInputGridPrice(0f);
				}
			}else{
				obj.setInputGridPrice(0f);
			}
			
			
			//计算单个价格
			if(obj.getBoxObCount()!=null && obj.getBoxObCount()!=0){
				Long ob = obj.getBoxObCount();
				Float pb = 0f;
				if(obj.getBoxPbCount()!=null && obj.getBoxPbCount()!=0){
					pb = obj.getBoxPbPrice()*(ob/obj.getBoxPbCount());
				}
				
				Float ib = 0f;
				if(obj.getBoxIbCount()!=null && obj.getBoxIbCount()!=0){
					ib = obj.getBoxIbPrice()*(ob/obj.getBoxIbCount());
				}
				
				Float mb = 0f;
				if(obj.getBoxMbCount()!=null && obj.getBoxMbCount()!=0){
					mb = obj.getBoxMbPrice()*(ob/obj.getBoxMbCount());
				}
				
				//整箱价格
				Float total = (pb+ib+mb+obj.getBoxObPrice()+obj.getInputGridPrice())/ob;
				Float num = Float.parseFloat(df.format(total));
				obj.setPackingPrice(num);
			}else{
				obj.setPackingPrice(0f);
			}
		}else{
			obj.setBoxPbTypeId(null);
			obj.setBoxIbTypeId(null);
			obj.setBoxMbTypeId(null);
			obj.setBoxObTypeId(null);
			obj.setInputGridTypeId(null);
			obj.setPackingPrice(0f);
		}
		return obj;
	}
	
	public String sumPackPrice(CotPriceDetail elements,CotBoxPacking boxPacking){
		Evaluator evaluator = new Evaluator();
		// 摆放长、宽、高
		if (elements.getPutL() == null || elements.getPutL().equals("")) {
			evaluator.putVariable("putL", "0.0");
		} else {
			evaluator.putVariable("putL", elements.getPutL().toString());
		}
		if (elements.getPutW() == null || elements.getPutW().equals("")) {
			evaluator.putVariable("putW", "0.0");
		} else {
			evaluator.putVariable("putW", elements.getPutW().toString());
		}
		if (elements.getPutH() == null || elements.getPutH().equals("")) {
			evaluator.putVariable("putH", "0.0");
		} else {
			evaluator.putVariable("putH", elements.getPutH().toString());
		}
		//产品包装长、宽、高
		if (elements.getBoxPbL() == null || elements.getBoxPbL().equals("")) {
			evaluator.putVariable("boxPbL", "0.0");
		}else {
			evaluator.putVariable("boxPbL", elements.getBoxPbL().toString());
		}
		if (elements.getBoxPbW() == null || elements.getBoxPbW().equals("")) {
			evaluator.putVariable("boxPbW", "0.0");
		}else {
			evaluator.putVariable("boxPbW", elements.getBoxPbW().toString());
		}
		if (elements.getBoxPbH() == null || elements.getBoxPbH().equals("")) {
			evaluator.putVariable("boxPbH", "0.0");
		}else {
			evaluator.putVariable("boxPbH", elements.getBoxPbH().toString());
		}
		//中盒包装长、宽、高
		if (elements.getBoxMbL() == null || elements.getBoxMbL().equals("")) {
			evaluator.putVariable("boxMbL", "0.0");
		}else {
			evaluator.putVariable("boxMbL", elements.getBoxMbL().toString());
		}
		if (elements.getBoxMbW() == null || elements.getBoxMbW().equals("")) {
			evaluator.putVariable("boxMbW", "0.0");
		}else {
			evaluator.putVariable("boxMbW", elements.getBoxMbW().toString());
		}
		if (elements.getBoxMbH() == null || elements.getBoxMbH().equals("")) {
			evaluator.putVariable("boxMbH", "0.0");
		}else {
			evaluator.putVariable("boxMbH", elements.getBoxMbH().toString());
		}
		//内盒包装长、宽、高
		if (elements.getBoxIbL() == null || elements.getBoxIbL().equals("")) {
			evaluator.putVariable("boxIbL", "0.0");
		}else {
			evaluator.putVariable("boxIbL", elements.getBoxIbL().toString());
		}
		if (elements.getBoxIbW() == null || elements.getBoxIbW().equals("")) {
			evaluator.putVariable("boxIbW", "0.0");
		}else {
			evaluator.putVariable("boxIbW", elements.getBoxIbW().toString());
		}
		if (elements.getBoxIbH() == null || elements.getBoxIbH().equals("")) {
			evaluator.putVariable("boxIbH", "0.0");
		}else {
			evaluator.putVariable("boxIbH", elements.getBoxIbH().toString());
		}
		//外箱包装长、宽、高
		if (elements.getBoxObL() == null || elements.getBoxObL().equals("")) {
			evaluator.putVariable("boxObL", "0.0");
		}else {
			evaluator.putVariable("boxObL", elements.getBoxObL().toString());
		}
		if (elements.getBoxObW() == null || elements.getBoxObW().equals("")) {
			evaluator.putVariable("boxObW", "0.0");
		}else {
			evaluator.putVariable("boxObW", elements.getBoxObW().toString());
		}
		if (elements.getBoxObH() == null || elements.getBoxObH().equals("")) {
			evaluator.putVariable("boxObH", "0.0");
		}else {
			evaluator.putVariable("boxObH", elements.getBoxObH().toString());
		}		
		//材料单价
		if (boxPacking.getMaterialPrice() == null || boxPacking.getMaterialPrice().equals("")) {
			evaluator.putVariable("materialPrice", "0.0");
		}else {
			evaluator.putVariable("materialPrice", boxPacking.getMaterialPrice().toString());
		}
		
		try {
			String result = evaluator.evaluate(boxPacking.getFormulaIn());
			return result;
		} catch (EvaluationException e) {
			e.printStackTrace();
			return "";
		}
	}
	
	//计算价格
	public CotPriceDetail calPrice(Map map,CotPriceDetail obj){
		DecimalFormat df = new DecimalFormat("#.00");
		if(obj.getBoxTypeId()!=null){
			if(obj.getBoxPbTypeId()!=null){
				CotBoxPacking pak = (CotBoxPacking)map.get(obj.getBoxPbTypeId());
				if(pak.getFormulaIn()!=null && !"".equals(pak.getFormulaIn())){
					String price = this.sumPackPrice(obj, pak);
					if(!"".equals(price)){
						Float num = Float.parseFloat(df.format(Float.parseFloat(price)));
						obj.setBoxPbPrice(num);
					}
				}else{
					obj.setBoxPbPrice(0f);
				}
			}else{
				obj.setBoxPbPrice(0f);
			}
			if(obj.getBoxIbTypeId()!=null){
				CotBoxPacking pak = (CotBoxPacking)map.get(obj.getBoxIbTypeId());
				if(pak.getFormulaIn()!=null && !"".equals(pak.getFormulaIn())){
					String price = this.sumPackPrice(obj, pak);
					if(!"".equals(price)){
						Float num = Float.parseFloat(df.format(Float.parseFloat(price)));
						obj.setBoxIbPrice(num);
					}
				}else{
					obj.setBoxIbPrice(0f);
				}
			}else{
				obj.setBoxIbPrice(0f);
			}
			if(obj.getBoxMbTypeId()!=null){
				CotBoxPacking pak = (CotBoxPacking)map.get(obj.getBoxMbTypeId());
				if(pak.getFormulaIn()!=null && !"".equals(pak.getFormulaIn())){
					String price = this.sumPackPrice(obj, pak);
					if(!"".equals(price)){
						Float num = Float.parseFloat(df.format(Float.parseFloat(price)));
						obj.setBoxMbPrice(num);
					}
				}else{
					obj.setBoxMbPrice(0f);
				}
			}else{
				obj.setBoxMbPrice(0f);
			}
			if(obj.getBoxObTypeId()!=null){
				CotBoxPacking pak = (CotBoxPacking)map.get(obj.getBoxObTypeId());
				if(pak.getFormulaIn()!=null && !"".equals(pak.getFormulaIn())){
					String price = this.sumPackPrice(obj, pak);
					if(!"".equals(price)){
						Float num = Float.parseFloat(df.format(Float.parseFloat(price)));
						obj.setBoxObPrice(num);
					}
				}else{
					obj.setBoxObPrice(0f);
				}
			}else{
				obj.setBoxObPrice(0f);
			}
			if(obj.getInputGridTypeId()!=null){
				CotBoxPacking pak = (CotBoxPacking)map.get(obj.getInputGridTypeId());
				if(pak.getFormulaIn()!=null && !"".equals(pak.getFormulaIn())){
					String price = this.sumPackPrice(obj, pak);
					if(!"".equals(price)){
						Float num = Float.parseFloat(df.format(Float.parseFloat(price)));
						obj.setInputGridPrice(num);
					}
				}else{
					obj.setInputGridPrice(0f);
				}
			}else{
				obj.setInputGridPrice(0f);
			}
			
			
			//计算单个价格
			if(obj.getBoxObCount()!=null && obj.getBoxObCount()!=0){
				Long ob = obj.getBoxObCount();
				Float pb = 0f;
				if(obj.getBoxPbCount()!=null && obj.getBoxPbCount()!=0){
					pb = obj.getBoxPbPrice()*(ob/obj.getBoxPbCount());
				}
				
				Float ib = 0f;
				if(obj.getBoxIbCount()!=null && obj.getBoxIbCount()!=0){
					ib = obj.getBoxIbPrice()*(ob/obj.getBoxIbCount());
				}
				
				Float mb = 0f;
				if(obj.getBoxMbCount()!=null && obj.getBoxMbCount()!=0){
					mb = obj.getBoxMbPrice()*(ob/obj.getBoxMbCount());
				}
				
				//整箱价格
				Float total = (pb+ib+mb+obj.getBoxObPrice()+obj.getInputGridPrice())/ob;
				Float num = Float.parseFloat(df.format(total));
				obj.setPackingPrice(num);
			}else{
				obj.setPackingPrice(0f);
			}
		}else{
			obj.setBoxPbTypeId(null);
			obj.setBoxIbTypeId(null);
			obj.setBoxMbTypeId(null);
			obj.setBoxObTypeId(null);
			obj.setInputGridTypeId(null);
			obj.setPackingPrice(0f);
		}
		return obj;
	}
	
	//计算价格
	public CotOrderOutdetail calPrice(Map map,CotOrderOutdetail obj){
		DecimalFormat df = new DecimalFormat("#.00");
		if(obj.getBoxTypeId()!=null){
			if(obj.getBoxPbTypeId()!=null){
				CotBoxPacking pak = (CotBoxPacking)map.get(obj.getBoxPbTypeId());
				if(pak.getFormulaIn()!=null && !"".equals(pak.getFormulaIn())){
					String price = this.sumPackPrice(obj, pak);
					if(!"".equals(price)){
						Float num = Float.parseFloat(df.format(Float.parseFloat(price)));
						obj.setBoxPbPrice(num);
					}
				}else{
					obj.setBoxPbPrice(0f);
				}
			}else{
				obj.setBoxPbPrice(0f);
			}
			if(obj.getBoxIbTypeId()!=null){
				CotBoxPacking pak = (CotBoxPacking)map.get(obj.getBoxIbTypeId());
				if(pak.getFormulaIn()!=null && !"".equals(pak.getFormulaIn())){
					String price = this.sumPackPrice(obj, pak);
					if(!"".equals(price)){
						Float num = Float.parseFloat(df.format(Float.parseFloat(price)));
						obj.setBoxIbPrice(num);
					}
				}else{
					obj.setBoxIbPrice(0f);
				}
			}else{
				obj.setBoxIbPrice(0f);
			}
			if(obj.getBoxMbTypeId()!=null){
				CotBoxPacking pak = (CotBoxPacking)map.get(obj.getBoxMbTypeId());
				if(pak.getFormulaIn()!=null && !"".equals(pak.getFormulaIn())){
					String price = this.sumPackPrice(obj, pak);
					if(!"".equals(price)){
						Float num = Float.parseFloat(df.format(Float.parseFloat(price)));
						obj.setBoxMbPrice(num);
					}
				}else{
					obj.setBoxMbPrice(0f);
				}
			}else{
				obj.setBoxMbPrice(0f);
			}
			if(obj.getBoxObTypeId()!=null){
				CotBoxPacking pak = (CotBoxPacking)map.get(obj.getBoxObTypeId());
				if(pak.getFormulaIn()!=null && !"".equals(pak.getFormulaIn())){
					String price = this.sumPackPrice(obj, pak);
					if(!"".equals(price)){
						Float num = Float.parseFloat(df.format(Float.parseFloat(price)));
						obj.setBoxObPrice(num);
					}
				}else{
					obj.setBoxObPrice(0f);
				}
			}else{
				obj.setBoxObPrice(0f);
			}
			
			
			if(obj.getInputGridTypeId()!=null){
				CotBoxPacking pak = (CotBoxPacking)map.get(obj.getInputGridTypeId());
				if(pak.getFormulaIn()!=null && !"".equals(pak.getFormulaIn())){
					String price = this.sumPackPrice(obj, pak);
					if(!"".equals(price)){
						Float num = Float.parseFloat(df.format(Float.parseFloat(price)));
						obj.setInputGridPrice(num);
					}
				}else{
					obj.setInputGridPrice(0f);
				}
			}else{
				obj.setInputGridPrice(0f);
			}
			
			
			//计算单个价格
			if(obj.getBoxObCount()!=null && obj.getBoxObCount()!=0){
				Long ob = obj.getBoxObCount();
				Float pb = 0f;
				if(obj.getBoxPbCount()!=null && obj.getBoxPbCount()!=0){
					pb = obj.getBoxPbPrice()*(ob/obj.getBoxPbCount());
				}
				
				Float ib = 0f;
				if(obj.getBoxIbCount()!=null && obj.getBoxIbCount()!=0){
					ib = obj.getBoxIbPrice()*(ob/obj.getBoxIbCount());
				}
				
				Float mb = 0f;
				if(obj.getBoxMbCount()!=null && obj.getBoxMbCount()!=0){
					mb = obj.getBoxMbPrice()*(ob/obj.getBoxMbCount());
				}
				
				//整箱价格
				Float total = (pb+ib+mb+obj.getBoxObPrice()+obj.getInputGridPrice())/ob;
				Float num = Float.parseFloat(df.format(total));
				obj.setPackingPrice(num);
			}else{
				obj.setPackingPrice(0f);
			}
		}else{
			obj.setBoxPbTypeId(null);
			obj.setBoxIbTypeId(null);
			obj.setBoxMbTypeId(null);
			obj.setBoxObTypeId(null);
			obj.setInputGridTypeId(null);
			obj.setPackingPrice(0f);
		}
		return obj;
	}
	
	public String sumPackPrice(CotOrderOutdetail elements,CotBoxPacking boxPacking){
		Evaluator evaluator = new Evaluator();
		// 摆放长、宽、高
		if (elements.getPutL() == null || elements.getPutL().equals("")) {
			evaluator.putVariable("putL", "0.0");
		} else {
			evaluator.putVariable("putL", elements.getPutL().toString());
		}
		if (elements.getPutW() == null || elements.getPutW().equals("")) {
			evaluator.putVariable("putW", "0.0");
		} else {
			evaluator.putVariable("putW", elements.getPutW().toString());
		}
		if (elements.getPutH() == null || elements.getPutH().equals("")) {
			evaluator.putVariable("putH", "0.0");
		} else {
			evaluator.putVariable("putH", elements.getPutH().toString());
		}
		//产品包装长、宽、高
		if (elements.getBoxPbL() == null || elements.getBoxPbL().equals("")) {
			evaluator.putVariable("boxPbL", "0.0");
		}else {
			evaluator.putVariable("boxPbL", elements.getBoxPbL().toString());
		}
		if (elements.getBoxPbW() == null || elements.getBoxPbW().equals("")) {
			evaluator.putVariable("boxPbW", "0.0");
		}else {
			evaluator.putVariable("boxPbW", elements.getBoxPbW().toString());
		}
		if (elements.getBoxPbH() == null || elements.getBoxPbH().equals("")) {
			evaluator.putVariable("boxPbH", "0.0");
		}else {
			evaluator.putVariable("boxPbH", elements.getBoxPbH().toString());
		}
		//中盒包装长、宽、高
		if (elements.getBoxMbL() == null || elements.getBoxMbL().equals("")) {
			evaluator.putVariable("boxMbL", "0.0");
		}else {
			evaluator.putVariable("boxMbL", elements.getBoxMbL().toString());
		}
		if (elements.getBoxMbW() == null || elements.getBoxMbW().equals("")) {
			evaluator.putVariable("boxMbW", "0.0");
		}else {
			evaluator.putVariable("boxMbW", elements.getBoxMbW().toString());
		}
		if (elements.getBoxMbH() == null || elements.getBoxMbH().equals("")) {
			evaluator.putVariable("boxMbH", "0.0");
		}else {
			evaluator.putVariable("boxMbH", elements.getBoxMbH().toString());
		}
		//内盒包装长、宽、高
		if (elements.getBoxIbL() == null || elements.getBoxIbL().equals("")) {
			evaluator.putVariable("boxIbL", "0.0");
		}else {
			evaluator.putVariable("boxIbL", elements.getBoxIbL().toString());
		}
		if (elements.getBoxIbW() == null || elements.getBoxIbW().equals("")) {
			evaluator.putVariable("boxIbW", "0.0");
		}else {
			evaluator.putVariable("boxIbW", elements.getBoxIbW().toString());
		}
		if (elements.getBoxIbH() == null || elements.getBoxIbH().equals("")) {
			evaluator.putVariable("boxIbH", "0.0");
		}else {
			evaluator.putVariable("boxIbH", elements.getBoxIbH().toString());
		}
		//外箱包装长、宽、高
		if (elements.getBoxObL() == null || elements.getBoxObL().equals("")) {
			evaluator.putVariable("boxObL", "0.0");
		}else {
			evaluator.putVariable("boxObL", elements.getBoxObL().toString());
		}
		if (elements.getBoxObW() == null || elements.getBoxObW().equals("")) {
			evaluator.putVariable("boxObW", "0.0");
		}else {
			evaluator.putVariable("boxObW", elements.getBoxObW().toString());
		}
		if (elements.getBoxObH() == null || elements.getBoxObH().equals("")) {
			evaluator.putVariable("boxObH", "0.0");
		}else {
			evaluator.putVariable("boxObH", elements.getBoxObH().toString());
		}		
		//材料单价
		if (boxPacking.getMaterialPrice() == null || boxPacking.getMaterialPrice().equals("")) {
			evaluator.putVariable("materialPrice", "0.0");
		}else {
			evaluator.putVariable("materialPrice", boxPacking.getMaterialPrice().toString());
		}
		
		try {
			String result = evaluator.evaluate(boxPacking.getFormulaIn());
			return result;
		} catch (EvaluationException e) {
			e.printStackTrace();
			return "";
		}
	}
	
	public String sumPackPrice(CotOrderDetail elements,CotBoxPacking boxPacking){
		Evaluator evaluator = new Evaluator();
		// 摆放长、宽、高
		if (elements.getPutL() == null || elements.getPutL().equals("")) {
			evaluator.putVariable("putL", "0.0");
		} else {
			evaluator.putVariable("putL", elements.getPutL().toString());
		}
		if (elements.getPutW() == null || elements.getPutW().equals("")) {
			evaluator.putVariable("putW", "0.0");
		} else {
			evaluator.putVariable("putW", elements.getPutW().toString());
		}
		if (elements.getPutH() == null || elements.getPutH().equals("")) {
			evaluator.putVariable("putH", "0.0");
		} else {
			evaluator.putVariable("putH", elements.getPutH().toString());
		}
		//产品包装长、宽、高
		if (elements.getBoxPbL() == null || elements.getBoxPbL().equals("")) {
			evaluator.putVariable("boxPbL", "0.0");
		}else {
			evaluator.putVariable("boxPbL", elements.getBoxPbL().toString());
		}
		if (elements.getBoxPbW() == null || elements.getBoxPbW().equals("")) {
			evaluator.putVariable("boxPbW", "0.0");
		}else {
			evaluator.putVariable("boxPbW", elements.getBoxPbW().toString());
		}
		if (elements.getBoxPbH() == null || elements.getBoxPbH().equals("")) {
			evaluator.putVariable("boxPbH", "0.0");
		}else {
			evaluator.putVariable("boxPbH", elements.getBoxPbH().toString());
		}
		//中盒包装长、宽、高
		if (elements.getBoxMbL() == null || elements.getBoxMbL().equals("")) {
			evaluator.putVariable("boxMbL", "0.0");
		}else {
			evaluator.putVariable("boxMbL", elements.getBoxMbL().toString());
		}
		if (elements.getBoxMbW() == null || elements.getBoxMbW().equals("")) {
			evaluator.putVariable("boxMbW", "0.0");
		}else {
			evaluator.putVariable("boxMbW", elements.getBoxMbW().toString());
		}
		if (elements.getBoxMbH() == null || elements.getBoxMbH().equals("")) {
			evaluator.putVariable("boxMbH", "0.0");
		}else {
			evaluator.putVariable("boxMbH", elements.getBoxMbH().toString());
		}
		//内盒包装长、宽、高
		if (elements.getBoxIbL() == null || elements.getBoxIbL().equals("")) {
			evaluator.putVariable("boxIbL", "0.0");
		}else {
			evaluator.putVariable("boxIbL", elements.getBoxIbL().toString());
		}
		if (elements.getBoxIbW() == null || elements.getBoxIbW().equals("")) {
			evaluator.putVariable("boxIbW", "0.0");
		}else {
			evaluator.putVariable("boxIbW", elements.getBoxIbW().toString());
		}
		if (elements.getBoxIbH() == null || elements.getBoxIbH().equals("")) {
			evaluator.putVariable("boxIbH", "0.0");
		}else {
			evaluator.putVariable("boxIbH", elements.getBoxIbH().toString());
		}
		//外箱包装长、宽、高
		if (elements.getBoxObL() == null || elements.getBoxObL().equals("")) {
			evaluator.putVariable("boxObL", "0.0");
		}else {
			evaluator.putVariable("boxObL", elements.getBoxObL().toString());
		}
		if (elements.getBoxObW() == null || elements.getBoxObW().equals("")) {
			evaluator.putVariable("boxObW", "0.0");
		}else {
			evaluator.putVariable("boxObW", elements.getBoxObW().toString());
		}
		if (elements.getBoxObH() == null || elements.getBoxObH().equals("")) {
			evaluator.putVariable("boxObH", "0.0");
		}else {
			evaluator.putVariable("boxObH", elements.getBoxObH().toString());
		}		
		//材料单价
		if (boxPacking.getMaterialPrice() == null || boxPacking.getMaterialPrice().equals("")) {
			evaluator.putVariable("materialPrice", "0.0");
		}else {
			evaluator.putVariable("materialPrice", boxPacking.getMaterialPrice().toString());
		}
		
		try {
			String result = evaluator.evaluate(boxPacking.getFormulaIn());
			return result;
		} catch (EvaluationException e) {
			e.printStackTrace();
			return "";
		}
	}
	
	//计算价格
	public CotOrderDetail calPrice(Map map,CotOrderDetail obj){
		DecimalFormat df = new DecimalFormat("#.00");
		if(obj.getBoxTypeId()!=null){
			if(obj.getBoxPbTypeId()!=null){
				CotBoxPacking pak = (CotBoxPacking)map.get(obj.getBoxPbTypeId());
				if(pak.getFormulaIn()!=null && !"".equals(pak.getFormulaIn())){
					String price = this.sumPackPrice(obj, pak);
					if(!"".equals(price)){
						Float num = Float.parseFloat(df.format(Float.parseFloat(price)));
						obj.setBoxPbPrice(num);
					}
				}else{
					obj.setBoxPbPrice(0f);
				}
			}else{
				obj.setBoxPbPrice(0f);
			}
			if(obj.getBoxIbTypeId()!=null){
				CotBoxPacking pak = (CotBoxPacking)map.get(obj.getBoxIbTypeId());
				if(pak.getFormulaIn()!=null && !"".equals(pak.getFormulaIn())){
					String price = this.sumPackPrice(obj, pak);
					if(!"".equals(price)){
						Float num = Float.parseFloat(df.format(Float.parseFloat(price)));
						obj.setBoxIbPrice(num);
					}
				}else{
					obj.setBoxIbPrice(0f);
				}
			}else{
				obj.setBoxIbPrice(0f);
			}
			if(obj.getBoxMbTypeId()!=null){
				CotBoxPacking pak = (CotBoxPacking)map.get(obj.getBoxMbTypeId());
				if(pak.getFormulaIn()!=null && !"".equals(pak.getFormulaIn())){
					String price = this.sumPackPrice(obj, pak);
					if(!"".equals(price)){
						Float num = Float.parseFloat(df.format(Float.parseFloat(price)));
						obj.setBoxMbPrice(num);
					}
				}else{
					obj.setBoxMbPrice(0f);
				}
			}else{
				obj.setBoxMbPrice(0f);
			}
			if(obj.getBoxObTypeId()!=null){
				CotBoxPacking pak = (CotBoxPacking)map.get(obj.getBoxObTypeId());
				if(pak.getFormulaIn()!=null && !"".equals(pak.getFormulaIn())){
					String price = this.sumPackPrice(obj, pak);
					if(!"".equals(price)){
						Float num = Float.parseFloat(df.format(Float.parseFloat(price)));
						obj.setBoxObPrice(num);
					}
				}else{
					obj.setBoxObPrice(0f);
				}
			}else{
				obj.setBoxObPrice(0f);
			}
			
			
			if(obj.getInputGridTypeId()!=null){
				CotBoxPacking pak = (CotBoxPacking)map.get(obj.getInputGridTypeId());
				if(pak.getFormulaIn()!=null && !"".equals(pak.getFormulaIn())){
					String price = this.sumPackPrice(obj, pak);
					if(!"".equals(price)){
						Float num = Float.parseFloat(df.format(Float.parseFloat(price)));
						obj.setInputGridPrice(num);
					}
				}else{
					obj.setInputGridPrice(0f);
				}
			}else{
				obj.setInputGridPrice(0f);
			}
			
			
			//计算单个价格
			if(obj.getBoxObCount()!=null && obj.getBoxObCount()!=0){
				Long ob = obj.getBoxObCount();
				Float pb = 0f;
				if(obj.getBoxPbCount()!=null && obj.getBoxPbCount()!=0){
					pb = obj.getBoxPbPrice()*(ob/obj.getBoxPbCount());
				}
				
				Float ib = 0f;
				if(obj.getBoxIbCount()!=null && obj.getBoxIbCount()!=0){
					ib = obj.getBoxIbPrice()*(ob/obj.getBoxIbCount());
				}
				
				Float mb = 0f;
				if(obj.getBoxMbCount()!=null && obj.getBoxMbCount()!=0){
					mb = obj.getBoxMbPrice()*(ob/obj.getBoxMbCount());
				}
				
				//整箱价格
				Float total = (pb+ib+mb+obj.getBoxObPrice()+obj.getInputGridPrice())/ob;
				Float num = Float.parseFloat(df.format(total));
				obj.setPackingPrice(num);
			}else{
				obj.setPackingPrice(0f);
			}
		}else{
			obj.setBoxPbTypeId(null);
			obj.setBoxIbTypeId(null);
			obj.setBoxMbTypeId(null);
			obj.setBoxObTypeId(null);
			obj.setInputGridTypeId(null);
			obj.setPackingPrice(0f);
		}
		return obj;
	}
	
	public String sumPackPrice(CotGivenDetail elements,CotBoxPacking boxPacking){
		Evaluator evaluator = new Evaluator();
		// 摆放长、宽、高
		if (elements.getPutL() == null || elements.getPutL().equals("")) {
			evaluator.putVariable("putL", "0.0");
		} else {
			evaluator.putVariable("putL", elements.getPutL().toString());
		}
		if (elements.getPutW() == null || elements.getPutW().equals("")) {
			evaluator.putVariable("putW", "0.0");
		} else {
			evaluator.putVariable("putW", elements.getPutW().toString());
		}
		if (elements.getPutH() == null || elements.getPutH().equals("")) {
			evaluator.putVariable("putH", "0.0");
		} else {
			evaluator.putVariable("putH", elements.getPutH().toString());
		}
		//产品包装长、宽、高
		if (elements.getBoxPbL() == null || elements.getBoxPbL().equals("")) {
			evaluator.putVariable("boxPbL", "0.0");
		}else {
			evaluator.putVariable("boxPbL", elements.getBoxPbL().toString());
		}
		if (elements.getBoxPbW() == null || elements.getBoxPbW().equals("")) {
			evaluator.putVariable("boxPbW", "0.0");
		}else {
			evaluator.putVariable("boxPbW", elements.getBoxPbW().toString());
		}
		if (elements.getBoxPbH() == null || elements.getBoxPbH().equals("")) {
			evaluator.putVariable("boxPbH", "0.0");
		}else {
			evaluator.putVariable("boxPbH", elements.getBoxPbH().toString());
		}
		//中盒包装长、宽、高
		if (elements.getBoxMbL() == null || elements.getBoxMbL().equals("")) {
			evaluator.putVariable("boxMbL", "0.0");
		}else {
			evaluator.putVariable("boxMbL", elements.getBoxMbL().toString());
		}
		if (elements.getBoxMbW() == null || elements.getBoxMbW().equals("")) {
			evaluator.putVariable("boxMbW", "0.0");
		}else {
			evaluator.putVariable("boxMbW", elements.getBoxMbW().toString());
		}
		if (elements.getBoxMbH() == null || elements.getBoxMbH().equals("")) {
			evaluator.putVariable("boxMbH", "0.0");
		}else {
			evaluator.putVariable("boxMbH", elements.getBoxMbH().toString());
		}
		//内盒包装长、宽、高
		if (elements.getBoxIbL() == null || elements.getBoxIbL().equals("")) {
			evaluator.putVariable("boxIbL", "0.0");
		}else {
			evaluator.putVariable("boxIbL", elements.getBoxIbL().toString());
		}
		if (elements.getBoxIbW() == null || elements.getBoxIbW().equals("")) {
			evaluator.putVariable("boxIbW", "0.0");
		}else {
			evaluator.putVariable("boxIbW", elements.getBoxIbW().toString());
		}
		if (elements.getBoxIbH() == null || elements.getBoxIbH().equals("")) {
			evaluator.putVariable("boxIbH", "0.0");
		}else {
			evaluator.putVariable("boxIbH", elements.getBoxIbH().toString());
		}
		//外箱包装长、宽、高
		if (elements.getBoxObL() == null || elements.getBoxObL().equals("")) {
			evaluator.putVariable("boxObL", "0.0");
		}else {
			evaluator.putVariable("boxObL", elements.getBoxObL().toString());
		}
		if (elements.getBoxObW() == null || elements.getBoxObW().equals("")) {
			evaluator.putVariable("boxObW", "0.0");
		}else {
			evaluator.putVariable("boxObW", elements.getBoxObW().toString());
		}
		if (elements.getBoxObH() == null || elements.getBoxObH().equals("")) {
			evaluator.putVariable("boxObH", "0.0");
		}else {
			evaluator.putVariable("boxObH", elements.getBoxObH().toString());
		}		
		//材料单价
		if (boxPacking.getMaterialPrice() == null || boxPacking.getMaterialPrice().equals("")) {
			evaluator.putVariable("materialPrice", "0.0");
		}else {
			evaluator.putVariable("materialPrice", boxPacking.getMaterialPrice().toString());
		}
		
		try {
			String result = evaluator.evaluate(boxPacking.getFormulaIn());
			return result;
		} catch (EvaluationException e) {
			e.printStackTrace();
			return "";
		}
	}
	
	//计算价格
	public CotGivenDetail calPrice(Map map,CotGivenDetail obj){
		DecimalFormat df = new DecimalFormat("#.00");
		if(obj.getBoxTypeId()!=null){
			if(obj.getBoxPbTypeId()!=null){
				CotBoxPacking pak = (CotBoxPacking)map.get(obj.getBoxPbTypeId());
				if(pak.getFormulaIn()!=null && !"".equals(pak.getFormulaIn())){
					String price = this.sumPackPrice(obj, pak);
					if(!"".equals(price)){
						Float num = Float.parseFloat(df.format(Float.parseFloat(price)));
						obj.setBoxPbPrice(num);
					}
				}else{
					obj.setBoxPbPrice(0f);
				}
			}else{
				obj.setBoxPbPrice(0f);
			}
			if(obj.getBoxIbTypeId()!=null){
				CotBoxPacking pak = (CotBoxPacking)map.get(obj.getBoxIbTypeId());
				if(pak.getFormulaIn()!=null && !"".equals(pak.getFormulaIn())){
					String price = this.sumPackPrice(obj, pak);
					if(!"".equals(price)){
						Float num = Float.parseFloat(df.format(Float.parseFloat(price)));
						obj.setBoxIbPrice(num);
					}
				}else{
					obj.setBoxIbPrice(0f);
				}
			}else{
				obj.setBoxIbPrice(0f);
			}
			if(obj.getBoxMbTypeId()!=null){
				CotBoxPacking pak = (CotBoxPacking)map.get(obj.getBoxMbTypeId());
				if(pak.getFormulaIn()!=null && !"".equals(pak.getFormulaIn())){
					String price = this.sumPackPrice(obj, pak);
					if(!"".equals(price)){
						Float num = Float.parseFloat(df.format(Float.parseFloat(price)));
						obj.setBoxMbPrice(num);
					}
				}else{
					obj.setBoxMbPrice(0f);
				}
			}else{
				obj.setBoxMbPrice(0f);
			}
			if(obj.getBoxObTypeId()!=null){
				CotBoxPacking pak = (CotBoxPacking)map.get(obj.getBoxObTypeId());
				if(pak.getFormulaIn()!=null && !"".equals(pak.getFormulaIn())){
					String price = this.sumPackPrice(obj, pak);
					if(!"".equals(price)){
						Float num = Float.parseFloat(df.format(Float.parseFloat(price)));
						obj.setBoxObPrice(num);
					}
				}else{
					obj.setBoxObPrice(0f);
				}
			}else{
				obj.setBoxObPrice(0f);
			}
			
			
			if(obj.getInputGridTypeId()!=null){
				CotBoxPacking pak = (CotBoxPacking)map.get(obj.getInputGridTypeId());
				if(pak.getFormulaIn()!=null && !"".equals(pak.getFormulaIn())){
					String price = this.sumPackPrice(obj, pak);
					if(!"".equals(price)){
						Float num = Float.parseFloat(df.format(Float.parseFloat(price)));
						obj.setInputGridPrice(num);
					}
				}else{
					obj.setInputGridPrice(0f);
				}
			}else{
				obj.setInputGridPrice(0f);
			}
			
			
			//计算单个价格
			if(obj.getBoxObCount()!=null && obj.getBoxObCount()!=0){
				Long ob = obj.getBoxObCount();
				Float pb = 0f;
				if(obj.getBoxPbCount()!=null && obj.getBoxPbCount()!=0){
					pb = obj.getBoxPbPrice()*(ob/obj.getBoxPbCount());
				}
				
				Float ib = 0f;
				if(obj.getBoxIbCount()!=null && obj.getBoxIbCount()!=0){
					ib = obj.getBoxIbPrice()*(ob/obj.getBoxIbCount());
				}
				
				Float mb = 0f;
				if(obj.getBoxMbCount()!=null && obj.getBoxMbCount()!=0){
					mb = obj.getBoxMbPrice()*(ob/obj.getBoxMbCount());
				}
				
				//整箱价格
				Float total = (pb+ib+mb+obj.getBoxObPrice()+obj.getInputGridPrice())/ob;
				Float num = Float.parseFloat(df.format(total));
				obj.setPackingPrice(num);
			}else{
				obj.setPackingPrice(0f);
			}
		}else{
			obj.setBoxPbTypeId(null);
			obj.setBoxIbTypeId(null);
			obj.setBoxMbTypeId(null);
			obj.setBoxObTypeId(null);
			obj.setInputGridTypeId(null);
			obj.setPackingPrice(0f);
		}
		return obj;
	}
}
