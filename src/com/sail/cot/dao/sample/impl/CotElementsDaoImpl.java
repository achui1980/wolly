/**
 * 
 */
package com.sail.cot.dao.sample.impl;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sourceforge.barbecue.BarcodeException;
import net.sourceforge.barbecue.linear.code39.Code39Barcode;
import net.sourceforge.jeval.EvaluationException;
import net.sourceforge.jeval.Evaluator;

import org.apache.log4j.Logger;
import org.hibernate.Query;

import com.sail.cot.dao.impl.CotBaseDaoImpl;
import com.sail.cot.dao.sample.CotElementsDao;
import com.sail.cot.domain.CotBoxPacking;
import com.sail.cot.domain.CotCurrency;
import com.sail.cot.domain.CotEleCfg;
import com.sail.cot.domain.CotElementsNew;
import com.sail.cot.domain.CotFactory;
import com.sail.cot.domain.CotPicture;
import com.sail.cot.domain.CotPriceFac;
import com.sail.cot.domain.CotPriceOut;
import com.sail.cot.domain.CotTypeLv1;
import com.sail.cot.domain.vo.CotEleIdCustNo;
import com.sail.cot.domain.vo.CotElementsVO;
import com.sail.cot.domain.vo.TreeNode;
import com.sail.cot.query.QueryInfo;
import com.sail.cot.service.sample.impl.CotReportServiceImpl;
import com.sail.cot.util.ContextUtil;
import com.sail.cot.util.Log4WebUtil;
import com.sail.cot.util.SystemUtil;

public class CotElementsDaoImpl extends CotBaseDaoImpl implements
		CotElementsDao {

	private Logger logger = Log4WebUtil.getLogger(CotElementsDaoImpl.class);

	/**
	 * 判断样品编号是否重复
	 * 
	 * @param empsId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Integer isExistEleId(String eleId) {
		List<Integer> res = new ArrayList<Integer>();
		res = super
				.find("select c.id from CotElementsNew c where c.eleId='"
						+ eleId + "'");
		if (res.size() != 1) {
			return null;
		} else {
			return res.get(0);
		}
	}

	/**
	 * jdbc样品表左联图片表查询
	 * 
	 * @return
	 * @throws SQLException
	 */
	public List<?> findCotElementsVOList(String sql) {
		List<CotElementsVO> list = new ArrayList<CotElementsVO>();
		Connection con = null;
		ResultSet rs = null;
		try {
			con = super.getSession().connection();
			Statement stmt = con.createStatement();
			rs = stmt.executeQuery(sql);
			while (rs.next()) {
				CotElementsVO cotElementsVO = new CotElementsVO();
				cotElementsVO.setId(rs.getInt(1));
				cotElementsVO.setEleId(rs.getString(2));
				cotElementsVO.setEleName(rs.getString(3));
				cotElementsVO.setEleNameEn(rs.getString(4));
				cotElementsVO.setEleSizeDesc(rs.getString(5));
				cotElementsVO.setFactoryId(rs.getInt(6));
				cotElementsVO.setPicPath(rs.getString(7));
				cotElementsVO.setEleParentid(rs.getInt(8));
				list.add(cotElementsVO);
			}

		} catch (Exception e) {
			logger.error("查询失败!样品查询异常");
		} finally {
			try {
				rs.close();
				con.close();
			} catch (SQLException e) {
				logger.error("查询失败!样品查询异常");
			}
		}
		return list;
	}

	/**
	 * 判断类别名称是否存在
	 * 
	 * @param typeName
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Boolean isExistTypeName(String typeName) {
		List<Integer> res1 = new ArrayList<Integer>();
		res1 = super.find("select c.id from CotTypeLv1 c where c.typeName='"
				+ typeName + "'");
		List<Integer> res2 = new ArrayList<Integer>();
		res2 = super.find("select c.id from CotTypeLv2 c where c.typeName='"
				+ typeName + "'");
		List<Integer> res3 = new ArrayList<Integer>();
		res3 = super.find("select c.id from CotTypeLv3 c where c.typeName='"
				+ typeName + "'");
		if (res1.size() == 0 && res2.size() == 0 && res3.size() == 0) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * 判断厂家编号是否存在
	 * 
	 * @param factoryNo
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Integer isExistFactoryNo(String factoryNo) {
		List<Integer> res = new ArrayList<Integer>();
		res = super
				.find("select c.id from CotFactory c where upper(c.factoryNo)=upper('"
						+ factoryNo + "')");
		if (res.size() != 1) {
			return null;
		} else {
			return res.get(0);
		}
	}

	public List getQueryCondition(String queryType) {

		
		
		List<TreeNode> res = new ArrayList<TreeNode>();
		// 根据厂家生成类别树
		if ("factory".equals(queryType)) {
			List factoryList = super.getRecords("CotFactory");
			for (int i = 0; i < factoryList.size(); i++) {
				TreeNode node = new TreeNode();
				CotFactory fac = (CotFactory) factoryList.get(i);
				 node.setId("root_" + fac.getId());
				 node.setText(fac.getShortName());
				 node.setHrefTarget("#");

				 res.add(node);
			}
		} else if ("type".equals(queryType)) {
			List typelv1 = super.getRecords("CotTypeLv1");
			for (int i = 0; i < typelv1.size(); i++) {
				TreeNode node = new TreeNode();
				CotTypeLv1 type = (CotTypeLv1) typelv1.get(i);
				 node.setId("root_" + type.getId());
				 node.setText(type.getTypeName());
				 node.setHrefTarget("#");
				 res.add(node);
			}
		}
		return res;
	}

	// 修改图片当前的默认图标的picFlag为0
	public void updateDefaultPic(Integer cotElementsId) {
		List<?> list = super
				.find("from CotPicture p where p.picFlag=1 and p.eleId="
						+ cotElementsId);
		if (list.size() == 1) {
			CotPicture cotPicture = (CotPicture) list.get(0);
			cotPicture.setPicFlag(0);
			super.update(cotPicture);
		}
	}

	/**
	 * 查询最近的厂家报价信息
	 * 
	 * @param eleId
	 */
	public CotPriceFac queryLastCotPriceFac(Integer eleId) {
		Query q = super.getSession().createQuery(
				"from CotPriceFac f where f.eleId=" + eleId
						+ " order by f.addTime desc");
		q.setFirstResult(0);
		q.setMaxResults(1);
		List<?> list = q.list();
		if (list.size() == 1) {
			return (CotPriceFac) list.get(0);
		} else {
			return null;
		}
	}

	/**
	 * 查询最近的对外报价信息
	 * 
	 * @param eleId
	 */
	public CotPriceOut queryLastCotPriceOut(Integer eleId) {
		Query q = super.getSession().createQuery(
				"from CotPriceOut o where o.eleId=" + eleId
						+ " order by o.addTime desc");
		q.setFirstResult(0);
		q.setMaxResults(1);
		List<?> list = q.list();
		if (list.size() == 1) {
			return (CotPriceOut) list.get(0);
		} else {
			return null;
		}
	}
	
	// 根据编号获取包材计算公式
	public CotBoxPacking getCalculation(Integer boxPackingId){
		return (CotBoxPacking) super.getById(CotBoxPacking.class, boxPackingId);
	}
	
	//计算价格
	public String calPrice(CotElementsNew elements,Integer boxPackingId){
		CotBoxPacking boxPacking = this.getCalculation(boxPackingId);
		
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
			if (boxPacking.getFormulaIn() == null || boxPacking.getFormulaIn().trim().equals("")) {
				return "0.0";
			}
			String result = evaluator.evaluate(boxPacking.getFormulaIn());
			return result;
		} catch (EvaluationException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 批量修改样品
	 * 
	 * @param ids
	 * @param map
	 */
	public void modifyBatch(String ids, Map<?, ?> map,Integer flag,boolean fit,boolean price,boolean pack) {
		// 取得样品默认配置对象
		CotEleCfg cotEleCfg = this.getDefaultEleCfg();
		Object eleCol = map.get("eleCol");// 颜色
		Object eleName = map.get("eleName");// 中文名
		Object eleNameEn = map.get("eleNameEn");// 英文名
		Object eleTypeidLv1 = map.get("eleTypeidLv1");// 材质
		Object factoryId = map.get("factoryId");// 厂家
		Object eleHsid = map.get("eleHsid");// 海关编码
		Object eleTypeIdLv2 = map.get("eleTypeidLv2");//产品分类
		
		Object eleMod = map.get("eleMod");//最小订量
		Object eleForPerson = map.get("eleForPerson");//开发对象
		Object eleRemark = map.get("eleRemark");//产品备注 
		Object boxRemarkCn = map.get("boxRemarkCn");//中文包装
		Object boxRemark = map.get("boxRemark");//英文包装
		
		DecimalFormat dfTwo = new DecimalFormat("#.00");
		String hql = "from CotElementsNew obj where obj.id in ("
				+ ids.substring(0, ids.length() - 1) + ")";
		List<?> list = super.find(hql);
		List<CotElementsNew> listNew = new ArrayList<CotElementsNew>();
		for (int i = 0; i < list.size(); i++) {
			CotElementsNew cotElementsNew = (CotElementsNew) list.get(i);
			cotElementsNew.setCotPictures(null);
			cotElementsNew.setChilds(null);
			cotElementsNew.setCotFile(null);
			cotElementsNew.setCotPriceFacs(null);
			cotElementsNew.setCotEleFittings(null);
			cotElementsNew.setCotElePrice(null);
			cotElementsNew.setCotElePacking(null);
			
			if (eleMod != null && !"".equals(eleMod)) {
				cotElementsNew.setEleMod(Integer.parseInt(eleMod.toString()));
			}
			if (eleForPerson != null && !"".equals(eleForPerson)) {
				cotElementsNew.setEleForPerson(eleForPerson.toString());
			}
			if (eleRemark != null && !"".equals(eleRemark)) {
				cotElementsNew.setEleRemark(eleRemark.toString());
			}
			if (boxRemarkCn != null && !"".equals(boxRemarkCn)) {
				cotElementsNew.setBoxRemarkCn(boxRemarkCn.toString());
			}
			if (boxRemark != null && !"".equals(boxRemark)) {
				cotElementsNew.setBoxRemark(boxRemark.toString());
			}
			
			if (eleCol != null && !"".equals(eleCol)) {
				cotElementsNew.setEleCol(eleCol.toString());
			}
			if (eleName != null && !"".equals(eleName)) {
				cotElementsNew.setEleName(eleName.toString());
			}
			if (eleNameEn != null && !"".equals(eleNameEn)) {
				cotElementsNew.setEleNameEn(eleNameEn.toString());
			}
			if (eleTypeidLv1 != null && !"".equals(eleTypeidLv1)) {
				cotElementsNew.setEleTypeidLv1(Integer.parseInt(eleTypeidLv1
						.toString()));
			}
			if (factoryId != null && !"".equals(factoryId)) {
				cotElementsNew.setFactoryId(Integer.parseInt(factoryId
						.toString()));
			}
			if (eleHsid != null && !"".equals(eleHsid)) {
				cotElementsNew.setEleHsid(Integer.parseInt(eleHsid.toString()));
			}
			if(eleTypeIdLv2 != null && !"".equals(eleTypeIdLv2))
			{
				cotElementsNew.setEleTypeidLv2(Integer.parseInt(eleTypeIdLv2.toString()));
			}
			//重新计算配件成本
			if(fit){
				String sql = "select sum(obj.fitTotalPrice) from CotEleFittings obj where obj.eleId="+cotElementsNew.getId();
				List listEle = super.find(sql);
				if(listEle.get(0)!=null){
					cotElementsNew.setEleFittingPrice(((Double)listEle.get(0)).floatValue());
				}else{
					cotElementsNew.setEleFittingPrice(0f);
				}
			}
			//重新计算样品成本
			if(price){
				String sql = "select sum(obj.priceAmount) from CotElePrice obj where obj.eleId="+cotElementsNew.getId();
				List listEle = super.find(sql);
				if(listEle.get(0)!=null){
					cotElementsNew.setElePrice(((Double)listEle.get(0)).floatValue());
				}else{
					cotElementsNew.setElePrice(0f);
				}
			}
			//重新计算包材
			if(pack){
				Float iPrice = 0.0f;
				Float mPrice = 0.0f;
				Float oPrice = 0.0f;
				Float pPrice = 0.0f;
				Float packPrice = 0.0f;
				Float peiPrice = 0.0f;
				if(cotElementsNew.getPackingPrice()!=null){
					packPrice=cotElementsNew.getPackingPrice();
				}
				if (cotElementsNew.getBoxIbTypeId() != null) {
					if(cotElementsNew.getBoxIbPrice()!=null){
						iPrice=cotElementsNew.getBoxIbPrice();
					}
					String ip = this.calPrice(cotElementsNew, cotElementsNew.getBoxIbTypeId());
					if(ip!=null){
						iPrice=Float.parseFloat(ip);
					}
					if (cotElementsNew.getBoxObCount()!=null && cotElementsNew.getBoxIbCount()!=null && cotElementsNew.getBoxIbCount()!=0) {
						iPrice =  iPrice* cotElementsNew.getBoxObCount()/cotElementsNew.getBoxIbCount();
					}
					cotElementsNew.setBoxIbPrice(Float.parseFloat(dfTwo.format(iPrice)));
				}
				if (cotElementsNew.getBoxMbTypeId() != null) {
					if(cotElementsNew.getBoxMbPrice()!=null){
						mPrice=cotElementsNew.getBoxMbPrice();
					}
					String ip = this.calPrice(cotElementsNew, cotElementsNew.getBoxMbTypeId());
					if(ip!=null){
						mPrice=Float.parseFloat(ip);
					}
					if (cotElementsNew.getBoxObCount()!=null && cotElementsNew.getBoxMbCount()!=null && cotElementsNew.getBoxMbCount()!=0) {
						mPrice =  mPrice* cotElementsNew.getBoxObCount()/cotElementsNew.getBoxMbCount();
					}
					cotElementsNew.setBoxMbPrice(Float.parseFloat(dfTwo.format(mPrice)));
				}
				if (cotElementsNew.getBoxObTypeId() != null) {
					if(cotElementsNew.getBoxObPrice()!=null){
						oPrice=cotElementsNew.getBoxObPrice();
					}
					String ip = this.calPrice(cotElementsNew, cotElementsNew.getBoxObTypeId());
					if(ip!=null){
						oPrice=Float.parseFloat(ip);
					}
					cotElementsNew.setBoxObPrice(Float.parseFloat(dfTwo.format(oPrice)));
				}
				if (cotElementsNew.getInputGridTypeId() != null) {
					if(cotElementsNew.getInputGridPrice()!=null){
						peiPrice=cotElementsNew.getInputGridPrice();
					}
					String ip = this.calPrice(cotElementsNew, cotElementsNew.getInputGridTypeId());
					if(ip!=null){
						peiPrice=Float.parseFloat(ip);
					}
					cotElementsNew.setInputGridPrice(Float.parseFloat(dfTwo.format(peiPrice)));
				}
				if (cotElementsNew.getBoxPbTypeId() != null) {
					if(cotElementsNew.getBoxPbPrice()!=null){
						pPrice=cotElementsNew.getBoxPbPrice();
					}
					String ip = this.calPrice(cotElementsNew, cotElementsNew.getBoxPbTypeId());
					if(ip!=null){
						pPrice=Float.parseFloat(ip);
					}
					if (cotElementsNew.getBoxObCount()!=null && cotElementsNew.getBoxPbCount()!=null && cotElementsNew.getBoxPbCount()!=0) {
						pPrice =  pPrice* cotElementsNew.getBoxObCount()/cotElementsNew.getBoxPbCount();
					}
					cotElementsNew.setBoxPbPrice(Float.parseFloat(dfTwo.format(pPrice)));
				}
				if (cotElementsNew.getBoxObCount() != null && cotElementsNew.getBoxObCount()!= 0) {
					packPrice = (pPrice + iPrice + mPrice + oPrice+peiPrice)/cotElementsNew.getBoxObCount();
				}
				cotElementsNew.setPackingPrice(Float.parseFloat(dfTwo.format(packPrice)));
			}
			
			// 更新生产价
			if (flag == 1 || flag==3) {
				Float elePrice = cotElementsNew.getElePrice();
				if ( elePrice== null) {
					elePrice=0f;
				}
				Float eleFitPrice = cotElementsNew.getEleFittingPrice();
				if ( eleFitPrice== null) {
					eleFitPrice=0f;
				}
				Float packingPrice = cotElementsNew.getPackingPrice();
				if ( packingPrice== null) {
					packingPrice=0f;
				}
				Evaluator evaluator = new Evaluator();
				evaluator.putVariable("elePrice", elePrice.toString());
				evaluator.putVariable("eleFittingPrice", eleFitPrice.toString());
				evaluator.putVariable("packingPrice", packingPrice.toString());
				try {
					if(cotEleCfg!=null && cotEleCfg.getExpessionFacIn()!=null){
						String result = evaluator.evaluate(cotEleCfg.getExpessionFacIn());
						if (cotElementsNew.getPriceFacUint() != null) {
							CotCurrency cur = (CotCurrency) super
							.getById(CotCurrency.class,
									cotElementsNew.getPriceFacUint());
							Float fac = Float.parseFloat(result) / cur.getCurRate();
							cotElementsNew.setPriceFac(fac);
							
						}
					}
				} catch (EvaluationException e) {
					e.printStackTrace();
				}
			}
			
			//更新外销价
			if (flag == 2  || flag==3) {
				Float lirun = cotElementsNew.getLiRun();
				if ( lirun== null) {
					lirun=0f;
				}
				Float priceFac = cotElementsNew.getPriceFac();
				if ( priceFac== null) {
					priceFac=0f;
				}
				Float tuiLv = cotElementsNew.getTuiLv();
				if ( tuiLv== null) {
					tuiLv=0f;
				}
				Float cbm = cotElementsNew.getBoxCbm();
				if ( cbm== null) {
					cbm=0f;
				}
				Long boxObCount = cotElementsNew.getBoxObCount();
				if ( boxObCount== null) {
					boxObCount=0l;
				}
				Evaluator evaluator = new Evaluator();
				if(cotElementsNew.getPriceOutUint()!=null){
					Float priceOutRate = this.getCurRate(cotElementsNew.getPriceOutUint());
					evaluator.putVariable("priceProfit", lirun.toString());
					evaluator.putVariable("priceFac", priceFac.toString());
					evaluator.putVariable("priceRate", priceOutRate.toString());
					evaluator.putVariable("tuiLv", tuiLv.toString());
					evaluator.putVariable("cbm", cbm.toString());
					evaluator.putVariable("boxObCount", boxObCount.toString());
					try {
						if(cotEleCfg!=null && cotEleCfg.getExpessionIn()!=null && !cotEleCfg.getExpessionIn().equals("")){
							String result = evaluator.evaluate(cotEleCfg.getExpessionIn());
							cotElementsNew.setPriceOut(Float.parseFloat(result));
						}
					} catch (EvaluationException e) {
						e.printStackTrace();
					}
				}
			}
			
			listNew.add(cotElementsNew);
		}
		super.updateRecords(listNew);
	}
	
	// 通过币种id取得汇率
	public Float getCurRate(Integer id) {
		List<?> res = super.find(
				" from CotCurrency obj where obj.id =" + id);
		CotCurrency cotCurrency = (CotCurrency) res.get(0);
		Float curRate = cotCurrency.getCurRate();
		return curRate;
	}
	
	// 根据查询条件批量修改
	public void modifyBatchAll(Map<?, ?> queryMap, Map<?, ?> map,Integer flag,boolean fit,boolean price,boolean pack) throws Exception {
		// 取得样品默认配置对象
		CotEleCfg cotEleCfg = this.getDefaultEleCfg();
		DecimalFormat dfTwo = new DecimalFormat("#.00");
		
		Object eleId = queryMap.get("eleIdFind");// 样品编号
		Object child = queryMap.get("childFind");// 子货号标识
		Object eleName = queryMap.get("eleNameFind");// 中文名
		Object eleNameEn = queryMap.get("eleNameEnFind");// 英文名
		Object factoryIdFind = queryMap.get("factoryIdFind");// 厂家
		Object eleColFind = queryMap.get("eleColFind");// 颜色
		Object startTime = queryMap.get("startTime");// 起始时间
		Object endTime = queryMap.get("endTime");// 结束时间
		Object eleTypeidLv1Find = queryMap.get("eleTypeidLv1Find");// 大类
		Object eleTypeidLv2 = queryMap.get("eleTypeidLv2");// 中类
		Object eleTypeidLv3 = queryMap.get("eleTypeidLv3");// 小类
		Object eleGrade = queryMap.get("eleGradeFind");// 等级
		Object eleForPerson = queryMap.get("eleForPersonFind");// 开发对象
		Object eleHsidFind = queryMap.get("eleHsid");// 海关编码
		Object boxLS = queryMap.get("boxLS");// 产品起始长
		Object boxLE = queryMap.get("boxLE");// 产品结束长
		Object boxWS = queryMap.get("boxWS");// 产品起始宽
		Object boxWE = queryMap.get("boxWE");// 产品结束宽
		Object boxHS = queryMap.get("boxHS");// 产品起始高
		Object boxHE = queryMap.get("boxHE");// 产品结束高

		StringBuffer queryString = new StringBuffer();
		queryString.append(" where 1=1");
		if (eleId != null && !eleId.toString().trim().equals("")) {
			queryString.append(" and ele.eleId like '%"
					+ eleId.toString().trim() + "%'");
		}
		if (child != null && child.toString().trim().equals("true")) {
			queryString.append(" and ele.eleFlag=2");
		}
//		else{
//			queryString.append(" and (ele.eleFlag is null or ele.eleFlag!=2)");
//		}
		if (eleHsidFind != null && !eleHsidFind.toString().equals("")) {
			queryString.append(" and ele.eleHsid="
					+ eleHsidFind.toString());
		}
		if (eleName != null && !eleName.toString().trim().equals("")) {
			queryString.append(" and ele.eleName like '%"
					+ eleName.toString().trim() + "%'");
		}
		if (eleNameEn != null && !eleNameEn.toString().trim().equals("")) {
			queryString.append(" and ele.eleNameEn like '%"
					+ eleNameEn.toString().trim() + "%'");
		}
		if (factoryIdFind != null && !factoryIdFind.toString().equals("")) {
			queryString
					.append(" and ele.factoryId=" + factoryIdFind.toString());
		}
		if (eleColFind != null && !eleColFind.toString().trim().equals("")) {
			queryString.append(" and ele.eleCol like '%"
					+ eleColFind.toString().trim() + "%'");
		}
		if (eleTypeidLv1Find != null && !eleTypeidLv1Find.toString().equals("")) {
			queryString.append(" and ele.eleTypeidLv1="
					+ eleTypeidLv1Find.toString());
		}
		if (eleTypeidLv2 != null && !eleTypeidLv2.toString().equals("")) {
			queryString.append(" and ele.eleTypeidLv2="
					+ eleTypeidLv2.toString());
		}
		if (eleTypeidLv3 != null && !eleTypeidLv3.toString().equals("")) {
			queryString.append(" and ele.eleTypeidLv3="
					+ eleTypeidLv3.toString());
		}
		if (eleGrade != null && !eleGrade.toString().equals("")) {
			queryString.append(" and ele.eleGrade like '%"
					+ eleGrade.toString().trim() + "%'");
		}

		if (eleForPerson != null && !eleForPerson.toString().equals("")) {
			queryString.append(" and ele.eleForPerson like '%"
					+ eleForPerson.toString().trim() + "%'");
		}

		if (startTime != null && !"".equals(startTime.toString().trim())) {
			queryString.append(" and ele.eleProTime >='" + startTime.toString()
					+ "'");
		}
		if (endTime != null && !"".equals(endTime.toString().trim())) {
			queryString.append(" and ele.eleProTime <='" + endTime.toString()
					+ " 23:59:59'");
		}

		if (boxLS != null && boxLE != null) {
			if (!"".equals(boxLS.toString().trim())
					&& !"".equals(boxLE.toString().trim())) {
				queryString.append(" and ele.boxL between " + boxLS.toString()
						+ " and " + boxLE.toString());
			}
		}

		if (boxWS != null && boxWE != null) {
			if (!"".equals(boxWS.toString().trim())
					&& !"".equals(boxWE.toString().trim())) {
				queryString.append(" and ele.boxW between " + boxWS.toString()
						+ " and " + boxWE.toString());
			}
		}

		if (boxHS != null && boxHE != null) {
			if (!"".equals(boxHS.toString().trim())
					&& !"".equals(boxHE.toString().trim())) {
				queryString.append(" and ele.boxH between " + boxHS.toString()
						+ " and " + boxHE.toString());
			}
		}

		Object eleCol = map.get("eleCol");// 颜色
		Object eleTypeidLv1 = map.get("eleTypeidLv1");// 材质
		Object factoryId = map.get("factoryId");// 厂家
		Object eleHsid = map.get("eleHsid");// 海关编码
		Object eleTypeIdLv2 = map.get("eleTypeidLv2");//产品分类

		String hql = "from CotElementsNew ele " + queryString;
		List<?> list = super.find(hql);
		List<CotElementsNew> listNew = new ArrayList<CotElementsNew>();
		
		for (int i = 0; i < list.size(); i++) {
			CotElementsNew cotElementsNew = (CotElementsNew) list.get(i);
			cotElementsNew.setCotPictures(null);
			cotElementsNew.setChilds(null);
			cotElementsNew.setCotFile(null);
			cotElementsNew.setCotPriceFacs(null);
			cotElementsNew.setCotEleFittings(null);
			cotElementsNew.setCotElePrice(null);
			cotElementsNew.setCotElePacking(null);
			if (eleCol != null && !"".equals(eleCol)) {
				cotElementsNew.setEleCol(eleCol.toString());
			}
			if (eleTypeidLv1 != null && !"".equals(eleTypeidLv1)) {
				cotElementsNew.setEleTypeidLv1(Integer.parseInt(eleTypeidLv1
						.toString()));
			}
			if (factoryId != null && !"".equals(factoryId)) {
				cotElementsNew.setFactoryId(Integer.parseInt(factoryId
						.toString()));
			}
			if (eleHsid != null && !"".equals(eleHsid)) {
				cotElementsNew.setEleHsid(Integer.parseInt(eleHsid.toString()));
			}
			if(eleTypeIdLv2 != null && !"".equals(eleTypeIdLv2))
			{
				cotElementsNew.setEleTypeidLv2(Integer.parseInt(eleTypeIdLv2.toString()));
			}
			
			//重新计算配件成本
			if(fit){
				String sql = "select sum(obj.fitTotalPrice) from CotEleFittings obj where obj.eleId="+cotElementsNew.getId();
				List listEle = super.find(sql);
				if(listEle.get(0)!=null){
					cotElementsNew.setEleFittingPrice(((Double)listEle.get(0)).floatValue());
				}else{
					cotElementsNew.setEleFittingPrice(0f);
				}
			}
			//重新计算样品成本
			if(price){
				String sql = "select sum(obj.priceAmount) from CotElePrice obj where obj.eleId="+cotElementsNew.getId();
				List listEle = super.find(sql);
				if(listEle.get(0)!=null){
					cotElementsNew.setElePrice(((Double)listEle.get(0)).floatValue());
				}else{
					cotElementsNew.setElePrice(0f);
				}
			}
			//重新计算包材
			if(pack){
				Float iPrice = 0.0f;
				Float mPrice = 0.0f;
				Float oPrice = 0.0f;
				Float pPrice = 0.0f;
				Float packPrice = 0.0f;
				Float peiPrice = 0.0f;
				if(cotElementsNew.getPackingPrice()!=null){
					packPrice=cotElementsNew.getPackingPrice();
				}
				if (cotElementsNew.getBoxIbTypeId() != null) {
					if(cotElementsNew.getBoxIbPrice()!=null){
						iPrice=cotElementsNew.getBoxIbPrice();
					}
					String ip = this.calPrice(cotElementsNew, cotElementsNew.getBoxIbTypeId());
					if(ip!=null){
						iPrice=Float.parseFloat(ip);
					}
					if (cotElementsNew.getBoxObCount()!=null && cotElementsNew.getBoxIbCount()!=null && cotElementsNew.getBoxIbCount()!=0) {
						iPrice =  iPrice* cotElementsNew.getBoxObCount()/cotElementsNew.getBoxIbCount();
					}
					cotElementsNew.setBoxIbPrice(Float.parseFloat(dfTwo.format(iPrice)));
				}
				if (cotElementsNew.getBoxMbTypeId() != null) {
					if(cotElementsNew.getBoxMbPrice()!=null){
						mPrice=cotElementsNew.getBoxMbPrice();
					}
					String ip = this.calPrice(cotElementsNew, cotElementsNew.getBoxMbTypeId());
					if(ip!=null){
						mPrice=Float.parseFloat(ip);
					}
					if (cotElementsNew.getBoxObCount()!=null && cotElementsNew.getBoxMbCount()!=null && cotElementsNew.getBoxMbCount()!=0) {
						mPrice =  mPrice* cotElementsNew.getBoxObCount()/cotElementsNew.getBoxMbCount();
					}
					cotElementsNew.setBoxMbPrice(Float.parseFloat(dfTwo.format(mPrice)));
				}
				if (cotElementsNew.getBoxObTypeId() != null) {
					if(cotElementsNew.getBoxObPrice()!=null){
						oPrice=cotElementsNew.getBoxObPrice();
					}
					String ip = this.calPrice(cotElementsNew, cotElementsNew.getBoxObTypeId());
					if(ip!=null){
						oPrice=Float.parseFloat(ip);
					}
					cotElementsNew.setBoxObPrice(Float.parseFloat(dfTwo.format(oPrice)));
				}
				if (cotElementsNew.getBoxPbTypeId() != null) {
					if(cotElementsNew.getBoxPbPrice()!=null){
						pPrice=cotElementsNew.getBoxPbPrice();
					}
					String ip = this.calPrice(cotElementsNew, cotElementsNew.getBoxPbTypeId());
					if(ip!=null){
						pPrice=Float.parseFloat(ip);
					}
					if (cotElementsNew.getBoxObCount()!=null && cotElementsNew.getBoxPbCount()!=null && cotElementsNew.getBoxPbCount()!=0) {
						pPrice =  pPrice* cotElementsNew.getBoxObCount()/cotElementsNew.getBoxPbCount();
					}
					cotElementsNew.setBoxPbPrice(Float.parseFloat(dfTwo.format(pPrice)));
				}
				if (cotElementsNew.getInputGridTypeId() != null) {
					if(cotElementsNew.getInputGridPrice()!=null){
						peiPrice=cotElementsNew.getInputGridPrice();
					}
					String ip = this.calPrice(cotElementsNew, cotElementsNew.getInputGridTypeId());
					if(ip!=null){
						peiPrice=Float.parseFloat(ip);
					}
					cotElementsNew.setInputGridPrice(Float.parseFloat(dfTwo.format(peiPrice)));
				}
				if (cotElementsNew.getBoxObCount() != null && cotElementsNew.getBoxObCount()!= 0) {
					packPrice = (pPrice + iPrice + mPrice + oPrice+peiPrice)/cotElementsNew.getBoxObCount();
				}
				cotElementsNew.setPackingPrice(Float.parseFloat(dfTwo.format(packPrice)));
			}
			
			// 更新生产价
			if (flag == 1 || flag==3) {
				Float elePrice = cotElementsNew.getElePrice();
				if ( elePrice== null) {
					elePrice=0f;
				}
				Float eleFitPrice = cotElementsNew.getEleFittingPrice();
				if ( eleFitPrice== null) {
					eleFitPrice=0f;
				}
				Float packingPrice = cotElementsNew.getPackingPrice();
				if ( packingPrice== null) {
					packingPrice=0f;
				}
				Evaluator evaluator = new Evaluator();
				evaluator.putVariable("elePrice", elePrice.toString());
				evaluator.putVariable("eleFittingPrice", eleFitPrice.toString());
				evaluator.putVariable("packingPrice", packingPrice.toString());
				try {
					if(cotEleCfg.getExpessionFacIn()!=null){
						String result = evaluator.evaluate(cotEleCfg.getExpessionFacIn());
						if (cotElementsNew.getPriceFacUint() != null) {
							CotCurrency cur = (CotCurrency) super
							.getById(CotCurrency.class,
									cotElementsNew.getPriceFacUint());
							Float fac = Float.parseFloat(result) / cur.getCurRate();
							cotElementsNew.setPriceFac(fac);
							
						}
					}
				} catch (EvaluationException e) {
					e.printStackTrace();
				}
			}
			
			//更新外销价
			if (flag == 2  || flag==3) {
				Float lirun = cotElementsNew.getLiRun();
				if ( lirun== null) {
					lirun=0f;
				}
				Float priceFac = cotElementsNew.getPriceFac();
				if ( priceFac== null) {
					priceFac=0f;
				}
				Float tuiLv = cotElementsNew.getTuiLv();
				if ( tuiLv== null) {
					tuiLv=0f;
				}
				Float cbm = cotElementsNew.getBoxCbm();
				if ( cbm== null) {
					cbm=0f;
				}
				Long boxObCount = cotElementsNew.getBoxObCount();
				if ( boxObCount== null) {
					boxObCount=0l;
				}
				Evaluator evaluator = new Evaluator();
				if(cotElementsNew.getPriceOutUint()!=null){
					Float priceOutRate = this.getCurRate(cotElementsNew.getPriceOutUint());
					evaluator.putVariable("priceProfit", lirun.toString());
					evaluator.putVariable("priceFac", priceFac.toString());
					evaluator.putVariable("priceRate", priceOutRate.toString());
					evaluator.putVariable("tuiLv", tuiLv.toString());
					evaluator.putVariable("cbm", cbm.toString());
					evaluator.putVariable("boxObCount", boxObCount.toString());
					try {
						if(cotEleCfg.getExpessionIn()!=null && !cotEleCfg.getExpessionIn().equals("")){
							String result = evaluator.evaluate(cotEleCfg.getExpessionIn());
							cotElementsNew.setPriceOut(Float.parseFloat(result));
						}
					} catch (EvaluationException e) {
						e.printStackTrace();
					}
				}
			}
			
			listNew.add(cotElementsNew);
		}
		super.updateRecords(listNew);
	}

	/**
	 * 判断图片是否在这个样品中存在
	 * 
	 * @param eleId
	 * @param picName
	 */
	public Boolean isExistPicture(Integer eleId, String picName) {
		List<?> list = super.find("from CotPicture p where p.eleId=" + eleId
				+ " and p.picName='" + picName + "'");
		if (list.size() != 0) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 查询要导出的样品数据
	 * 
	 * @param ids
	 * @param page
	 * @return String[][]
	 */
	public String[][] findExportData(String ids, String page,
			String queryString, String queryStringHQL) {
		String sql = "SELECT "
				+ "e.ELE_ID,"
				+ "e.ELE_NAME,"
				+ "e.ELE_NAME_EN,"
				+ "e.FACTORY_NO,"
				+ "e.ELE_FLAG,"
				+ "e.ELE_UNITNUM,"
				+ "e.tao_unit,"
				+ "e.ELE_PARENT,"
				+ "e.ELE_FROM,"
				+ "e.ELE_GRADE,"
				+ "t.TYPE_NAME,"
				+ "p.TYPE_NAME,"
				+ "e.ELE_COL,"
				+ "e.ELE_UNIT,"
				+ "h.HSCODE,"
				+ "factory.SHORT_NAME,"
				+ "e.ELE_SIZE_DESC,"
				+ "e.ELE_INCH_DESC,"
				+ "e.ELE_PRO_TIME,"
				+ "e.ELE_FOR_PERSON,"
				+ "e.ELE_MOD,"
				+ "e.ELE_REMARK,"
				+ "e.BOX_L_INCH,"
				+ "e.BOX_W_INCH,"
				+ "e.BOX_H_INCH,"
				+ "e.cube,"
				+ "e.BOX_L,"
				+ "e.BOX_W,"
				+ "e.BOX_H,"
				+ "e.BOX_HANDLEH,"
				+ "e.ELE_DESC,"
				+ "e.BOX_TDESC,"
				+ "e.BOX_BDESC,"
				+ "e.BOX_IB_L,"
				+ "e.BOX_IB_W,"
				+ "e.BOX_IB_H,"
				+ "e.BOX_MB_L,"
				+ "e.BOX_MB_W,"
				+ "e.BOX_MB_H,"
				+ "e.BOX_CBM,"
				+ "e.BOX_OB_L,"
				+ "e.BOX_OB_W,"
				+ "e.BOX_OB_H,"
				+ "e.BOX_WEIGTH,"
				+ "e.BOX_GROSS_WEIGTH,"
				+ "e.BOX_NET_WEIGTH,"
				+ "e.BOX_IB_COUNT,"
				+ "e.BOX_MB_COUNT,"
				+ "e.BOX_OB_COUNT,"
				+ "box.TYPE_NAME,"
				+ "e.BOX_REMARK,"
				+ "c.CUR_NAME_EN,"
				+ "e.PRICE_FAC,"
				+ "u.CUR_NAME_EN,"
				+ "e.PRICE_OUT"
				+ " FROM"
				+ " cot_elements_new AS e"
				+ " left Join cot_ele_other AS h ON h.ID = e.HS_ID"
				+ " Left Join cot_currency AS c ON e.price_fac_uint = c.ID"
				+ " Left Join cot_currency AS u ON e.price_out_unit = u.ID"
				+ " left Join cot_factory AS factory ON factory.ID = e.FACTORY_ID"
				+ " left Join cot_type_lv1 AS t ON t.ID = e.ELE_TYPEID_LV1"
				+ " left Join cot_type_lv2 AS p ON p.ID = e.ELE_TYPEID_LV2"
				+ " left Join cot_boxtype AS box ON box.ID = e.box_type_id";

		String[][] data = null;
		// 总列数
		int columnCount = 55;
		int rowCount = 0;
		String countSql = "select count(*) from CotElementsNew obj";
		// 按选择的样品备份
		if (ids != null && !ids.equals("")) {
			sql += " where e.ID in (" + ids.substring(0, ids.length() - 1)
					+ ")";
			countSql += " where obj.id in ("
					+ ids.substring(0, ids.length() - 1) + ")";
			List<?> list = super.find(countSql);
			if ((Integer) list.get(0) == 0) {
				return data;
			}

			rowCount = (Integer) list.get(0);
			data = new String[rowCount][columnCount];
		}
		// 按页数备份(每页2000条)
		if (page != null && !page.equals("")) {
			countSql += queryStringHQL;
			int pageNum = Integer.parseInt(page);
			sql += queryString;
			sql += " limit " + (pageNum - 1) * 2000 + ",2000";
			Query query = super.getSession().createQuery(countSql);
			query.setFirstResult((pageNum - 1) * 2000);
			query.setMaxResults(2000);
			List<?> list = query.list();
			if ((Integer) list.get(0) == 0) {
				return data;
			}
			rowCount = (Integer) list.get(0);
			data = new String[rowCount][columnCount];
		}

		Connection con = null;
		ResultSet rs = null;
		try {
			con = super.getSession().connection();
			Statement stmt = con.createStatement();
			rs = stmt.executeQuery(sql);
			for (int i = 0; i < rowCount; i++) {
				if (rs.next()) {
					for (int j = 0; j < columnCount; j++) {
						Object temp = rs.getObject(j + 1);
						if (temp != null) {
							if (j == 4 && ("0".equals(temp.toString()) || "2".equals(temp.toString()))) {
								temp = "单件";
							}
							if (j == 4 && "1".equals(temp.toString())) {
								temp = "套件";
							}
							if (j == 4 && "3".equals(temp.toString())) {
								temp = "组件";
							}
							data[i][j] = temp.toString();
						} else {
							data[i][j] = null;
						}
					}
				}
			}
			return data;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("查询失败!样品查询异常");
			return null;
		} finally {
			try {
				rs.close();
				con.close();
			} catch (SQLException e) {
				logger.error("查询失败!样品查询异常");
			}
		}
	}

	//保存盘点的样品数据到文件upload/BaseInfo.txt
	public void saveCheckMachineData(String ids, String page,
			String queryStringHQL) throws IOException {
		String sql = "select " + "e.ELE_ID," + "e.BOX_L," + "e.BOX_W,"
				+ "e.BOX_H," + "e.BOX_IB_COUNT," + "e.BOX_MB_COUNT,"
				+ "e.BOX_OB_COUNT," + "e.BOX_CBM," + "e.BOX_OB_L,"
				+ "e.BOX_OB_W," + "e.BOX_OB_H," + "e.PRICE_OUT,"
				+ "e.price_out_unit," + "e.ELE_MOD from cot_elements_new e";

		// 按选择的样品备份
		if (ids != null && !ids.equals("")) {
			sql += " where e.ID in (" + ids.substring(0, ids.length() - 1)
					+ ")";
		}
		// 按页数备份(每页2000条)
		if (page != null && !page.equals("")) {
			sql += queryStringHQL;
		}
		// 获得币种为美元的编号
		String sqlCur = "select obj.id from CotCurrency obj where obj.curNameEn='USD'";
		List<?> listCur = super.find(sqlCur);
		Integer curUSD = (Integer) listCur.get(0);
		
		// 获得tomcat路径
		String classPath = CotReportServiceImpl.class.getResource("/")
				.toString();
		String systemPath = classPath.substring(6, classPath.length() - 16);
		File file = new File(systemPath + "reportfile/BaseInfo.txt");
		BufferedWriter bw = new BufferedWriter(
		         new FileWriter(file));

		DecimalFormat nbf = new DecimalFormat("0.00");
		DecimalFormat nbfCBM = new DecimalFormat("0.000");

		Connection con = null;
		ResultSet rs = null;
		//按货号名称排序
		sql+=" order by e.ELE_ID";
		try {
			con = super.getSession().connection();
			Statement stmt = con.createStatement();
			rs = stmt.executeQuery(sql);
			while (rs.next()) {
				StringBuffer temp = new StringBuffer();
				
				try {
					Code39Barcode code39Barcode = new Code39Barcode(rs.getString(1),true);
				} catch (BarcodeException e) {
					//System.out.println("msg:"+e);
					continue;
				}
				// 货号
				temp.append(SystemUtil.formatString(rs.getString(1), " ", 24) + ",");
				// 产品尺寸
				String size = nbf.format(rs.getFloat(2)) + "*" + nbf.format(rs.getFloat(3)) + "*" + nbf.format(rs.getFloat(4)) + "CM";
				temp.append(SystemUtil.formatString(size, " ", 22) + ",");
				// 装箱情况
				String boxCase = rs.getInt(5) + "/" + rs.getInt(6) + "/"
						+ rs.getInt(7) + "/" + nbfCBM.format(rs.getFloat(8)) + "CBM";
				temp.append(SystemUtil.formatString(boxCase, " ", 20) + ",");
				// 外箱尺寸
				String boxOsize = nbf.format(rs.getFloat(9)) + "*" + nbf.format(rs.getFloat(10)) + "*" + nbf.format(rs.getFloat(11)) + "CM";
				temp.append(SystemUtil.formatString(boxOsize, " ", 22) + ",");
				// 价格
				Float newPrice = this.updatePrice(rs.getFloat(12), rs.getInt(13),
						curUSD);
				temp.append(SystemUtil.formatString(nbfCBM.format(newPrice), " ",
						7)
						+ ",");
				// 定量
				temp.append(SystemUtil.formatString(String.valueOf(rs.getInt(14)),
						" ", 5)
						+ "\r\n");
				bw.write(temp.toString());
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("查询失败!样品查询异常");
		} finally {
			try {
				bw.close();
				rs.close();
				con.close();
			} catch (SQLException e) {
				logger.error("查询失败!样品查询异常");
			}
		}
	}
	//保存盘点的样品数据到文件upload/BaseInfo.txt
	public void saveCheckMachineData4Mc550(String ids, String page,
			String queryStringHQL) throws IOException {
		String sql = "select " + "e.ELE_ID," + "e.BOX_L," + "e.BOX_W,"
				+ "e.BOX_H," + "e.BOX_IB_COUNT," + "e.BOX_MB_COUNT,"
				+ "e.BOX_OB_COUNT," + "e.BOX_CBM," + "e.BOX_OB_L,"
				+ "e.BOX_OB_W," + "e.BOX_OB_H," + "e.PRICE_OUT,"
				+ "e.price_out_unit," + "e.ELE_MOD,e.ELE_NAME,e.price_fac from cot_elements_new e";

		// 按选择的样品备份
		if (ids != null && !ids.equals("")) {
			sql += " where e.ID in (" + ids.substring(0, ids.length() - 1)
					+ ")";
		}
		// 按页数备份(每页2000条)
		if (page != null && !page.equals("")) {
			sql += queryStringHQL;
		}
		// 获得币种为美元的编号
		String sqlCur = "select obj.id from CotCurrency obj where obj.curNameEn='USD'";
		List<?> listCur = super.find(sqlCur);
		Integer curUSD = (Integer) listCur.get(0);
		
		// 获得tomcat路径
		String classPath = CotReportServiceImpl.class.getResource("/")
				.toString();
		String systemPath = classPath.substring(6, classPath.length() - 16);
		File file = new File(systemPath + "reportfile/BaseInfo.txt");
		BufferedWriter bw = new BufferedWriter(
		         new FileWriter(file));

		DecimalFormat nbf = new DecimalFormat("0.00");
		DecimalFormat nbfCBM = new DecimalFormat("0.000");

		Connection con = null;
		ResultSet rs = null;
		//按货号名称排序
		sql+=" order by e.ELE_ID";
		try {
			con = super.getSession().connection();
			Statement stmt = con.createStatement();
			rs = stmt.executeQuery(sql);
			while (rs.next()) {
				String tempStr = "";
				StringBuffer temp = new StringBuffer();
				try {
					Code39Barcode code39Barcode = new Code39Barcode(rs.getString(1),true);
				} catch (BarcodeException e) {
					//System.out.println("msg:"+e);
					continue;
				}
				// 货号
				temp.append(rs.getString(1)).append("#|#");
				
				
				//中文名称
				tempStr = rs.getString(15) == null ? "":rs.getString(15);
				temp.append(tempStr).append("#|#");
				// 长
				//tmpFloat = rs.getFloat(2) == null ? 0f:rs.getString(2);
				temp.append(nbf.format(rs.getFloat(2))).append("#|#");
				// 宽
				temp.append(nbf.format(rs.getFloat(3))).append("#|#");
				// 高
				temp.append(nbf.format(rs.getFloat(4))).append("#|#");
				//内盒装数
				temp.append(rs.getInt(5)).append("#|#");
				//外箱装数
				temp.append(rs.getInt(7)).append("#|#");
				// 外箱长
				temp.append(nbf.format(rs.getFloat(9))).append("#|#");
				// 外箱宽
				temp.append(nbf.format(rs.getFloat(10))).append("#|#");
				// 外箱高
				temp.append(nbf.format(rs.getFloat(11))).append("#|#");
				//CBM
				temp.append(nbfCBM.format(rs.getFloat(8))).append("#|#");
				// 定量
				temp.append(rs.getInt(14)).append("#|#");
				// 价格
				Float newPrice = this.updatePrice(rs.getFloat(12), rs.getInt(13),
						curUSD);
				temp.append(nbfCBM.format(newPrice)).append("#|#");
				// 工厂价
				temp.append(nbfCBM.format(rs.getFloat(16))+ "\r\n");
				bw.write(temp.toString());
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("查询失败!样品查询异常");
		} finally {
			try {
				bw.close();
				rs.close();
				con.close();
			} catch (SQLException e) {
				logger.error("查询失败!样品查询异常");
			}
		}
	}

	// 查找未定义的厂家
	public CotFactory findFactoryDefault() {
		String hql = "from CotFactory obj where obj.shortName='未定义'";
		List<?> list = super.find(hql);
		return (CotFactory) list.get(0);
	}

	// 修改图片当前的默认图标的名称
	public void updateDefaultPicName(Integer elementsId, String newName,
			String newPath) {
		String hql = "from CotPicture obj where obj.eleId = " + elementsId
				+ " and obj.picFlag=1";
		List<?> list = super.find(hql);
		Iterator<?> it = list.iterator();
		while (it.hasNext()) {
			CotPicture cotPicture = (CotPicture) it.next();
			cotPicture.setPicName(newName);
			cotPicture.setPicPath(newPath);
			super.update(cotPicture);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sail.cot.dao.sample.CotElementsDao#deleteEleCustByCodition(java.lang.String,
	 *      java.lang.String, java.lang.Integer)
	 */
	public void deleteEleCustByCodition(String EleId, String type,
			Integer typePrimId) {
		String strSql = "delete from cot_ele_cust  where 1=1" + " and ele_id="
				+ "'" + EleId + "'" + " and TYPE_Prim_Id=" + typePrimId
				+ " and type = " + "'" + type + "'";
		int res = 0;
		Connection conn = null;
		PreparedStatement pstm = null;
		try {
			conn = super.getSession().connection();
			pstm = conn.prepareStatement(strSql);
			res = pstm.executeUpdate();

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (pstm != null)
				try {
					pstm.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sail.cot.dao.sample.CotElementsDao#deleteEleCustNoByTypeAndDetailId(java.lang.String,
	 *      java.lang.Integer)
	 */
	public void deleteEleCustNoByTypeAndDetailId(String type, Integer typeDetail) {
		String strSql = "delete from cot_ele_cust  where 1=1"
				+ " and Type_detail_id=" + typeDetail + " and type = " + type;
		int res = 0;
		Connection conn = null;
		PreparedStatement pstm = null;
		try {
			conn = super.getSession().connection();
			pstm = conn.prepareStatement(strSql);
			res = pstm.executeUpdate();

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (pstm != null)
				try {
					pstm.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sail.cot.dao.sample.CotElementsDao#deleteEleCustNoByTypeAndPrimId(java.lang.String,
	 *      java.lang.Integer)
	 */
	public void deleteEleCustNoByTypeAndPrimId(String type, Integer typePrimId) {
		String strSql = "delete from cot_ele_cust  where 1=1"
				+ " and TYPE_Prim_Id=" + typePrimId + " and type = " + "'"
				+ type + "'";
		int res = 0;
		Connection conn = null;
		PreparedStatement pstm = null;
		try {
			conn = super.getSession().connection();
			pstm = conn.prepareStatement(strSql);
			res = pstm.executeUpdate();

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (pstm != null)
				try {
					pstm.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sail.cot.dao.sample.CotElementsDao#getCustNoListByEleId(java.lang.String)
	 */
	public List<CotEleIdCustNo> getCustNoListByEleId(String EleId) {
		List<CotEleIdCustNo> res = new ArrayList<CotEleIdCustNo>();
		String strSql = "select CUST_NO,ele_name_en,cust_id from cot_ele_cust where 1=1  "
				+ " and ELE_ID = '" + EleId + "'" + " order by id desc";
		Connection conn = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		conn = super.getSession().connection();
		try {
			pstm = conn.prepareStatement(strSql);
			rs = pstm.executeQuery();
			while (rs.next()) {
				CotEleIdCustNo eleNo = new CotEleIdCustNo();
				eleNo.setCustNo(rs.getString("CUST_NO"));
				eleNo.setEleNameEn(rs.getString("ele_name_en"));
				eleNo.setCustId(rs.getString("cust_id"));
				eleNo.setEleId(EleId);
				res.add(eleNo);
			}
		} catch (SQLException e) {

			e.printStackTrace();
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (pstm != null)
					pstm.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return res;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sail.cot.dao.sample.CotElementsDao#saveEleCustNo(java.lang.String,
	 *      java.lang.String, java.lang.String, java.lang.Integer,
	 *      java.lang.Integer, java.lang.Integer, java.lang.String)
	 */
	public void saveEleCustNo(String EleId, String CustNo, String type,
			Integer typePrimId, Integer typeDetailId, Integer custId,
			String eleNameEn) {
		String strSql = "insert into cot_ele_cust  (ele_id,cust_no,type,TYPE_Prim_Id,Type_detail_id,cust_id,ele_name_en,add_time) "
				+ " values"
				+ "( '"
				+ EleId
				+ "',"
				+ "'"
				+ CustNo
				+ "',"
				+ "'"
				+ type
				+ "',"
				+ typePrimId
				+ ","
				+ typeDetailId
				+ ","
				+ custId
				+ ","
				+ "'"
				+ eleNameEn
				+ "',"
				+ "'"
				+ new java.sql.Timestamp(System.currentTimeMillis())
				+ "'"
				+ ")";

		int res = 0;
		Connection conn = null;
		PreparedStatement pstm = null;
		try {
			conn = super.getSession().connection();
			pstm = conn.prepareStatement(strSql);
			res = pstm.executeUpdate();

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (pstm != null)
				try {
					pstm.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sail.cot.dao.sample.CotElementsDao#getCustNoListByEleId(java.util.List)
	 */
	public Map getCustNoListMapByEleId(String eleId, Integer custId) {
		if (eleId == null)
			return null;
		Map res = new HashMap();
		String strSql = "select ele_id,CUST_NO from cot_ele_cust where 1=1  "
				+ " and ELE_ID =  '" + eleId + "' and cust_id = " + custId
				+ " order by id asc";
		Connection conn = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		conn = super.getSession().connection();
		try {
			pstm = conn.prepareStatement(strSql);
			rs = pstm.executeQuery();
			while (rs.next()) {
				res.put(rs.getString("ele_id").toLowerCase(), rs
						.getString("CUST_NO"));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (pstm != null)
					pstm.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return res;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sail.cot.dao.sample.CotElementsDao#saveEleCustNoByCustList(java.util.List)
	 *      获取在货号样品表中不存在的客号列表 和一存在的列表
	 */
	private Map getUnExitsMap(Map map, Integer custId) {
		if (map == null)
			return null;
		Map res = new HashMap();
		Map exitMap = new HashMap();
		String custNos = "";
		Iterator iterator = map.keySet().iterator();
		int i = 0;
		while (iterator.hasNext()) {
			custNos += "'"+String.valueOf(iterator.next())+"'";
			if (i < map.values().size() - 1)
			{
				custNos += ",";
			}
			i++;
		}
		String strSql = "select ele_id,CUST_NO from cot_ele_cust where 1=1  "
				+ " and ele_id in( " + custNos + ") and cust_id = " + custId;
		Connection conn = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		conn = super.getSession().connection();
		try {
			pstm = conn.prepareStatement(strSql);
			rs = pstm.executeQuery();
			while (rs.next()) {
				// 过滤出货号客号表中没有的客号
				exitMap.put(rs.getString("ele_id"), map.get(rs.getString("ele_id")));
				map.remove(rs.getString("ele_id"));
				
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (pstm != null)
					pstm.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		res.put("unExist", map);
		res.put("exist", exitMap);
		return res;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sail.cot.dao.sample.CotElementsDao#saveEleCustNoByCustList(java.util.List)
	 *      elecustMap:样品货号与客号对应的MAP elenameenMap:样品货号与英文品名对应的MAP
	 */
	public void saveEleCustNoByCustList(Map elecustMap, Map elenameenMap,
			Integer custId, String type) {
		// 获取不存在的客户货号关系
		Map map = this.getUnExitsMap(elecustMap, custId);
		if (map == null)
			return;
		Map unExitMap = (HashMap)map.get("unExist");
		Map exitMap = (HashMap)map.get("exist");
		String strSql = "insert into cot_ele_cust (ele_id,cust_no,type,cust_Id,ele_name_en,add_time) "
				+ " values (?,?,?,?,?,?)";
		String strSqlUpdate = "update cot_ele_cust set cust_no=? where cust_Id=? and ele_id=?";
		Iterator iterator = unExitMap.keySet().iterator();
		Connection conn = null;
		PreparedStatement pstm = null;
		PreparedStatement pstmupdate = null;
		ResultSet rs = null;
		conn = super.getSession().connection();
		try {
			pstm = conn.prepareStatement(strSql);
			while (iterator.hasNext()) {
				String key = String.valueOf(iterator.next());
				String value = String.valueOf(unExitMap.get(key));
				pstm.setString(1, key);
				pstm.setString(2, value);
				pstm.setString(3, type);
				pstm.setInt(4, custId.intValue());
				pstm.setString(5, String.valueOf(elenameenMap.get(key)));
				pstm.setTimestamp(6, new java.sql.Timestamp(System
						.currentTimeMillis()));
				pstm.addBatch();
			}
			int[] i = pstm.executeBatch(); //新增
			pstmupdate =  conn.prepareStatement(strSqlUpdate);
			Iterator iteratorExist = exitMap.keySet().iterator();
			while(iteratorExist.hasNext())
			{
				String key = String.valueOf(iteratorExist.next());
				String value = String.valueOf(exitMap.get(key));
				pstmupdate.setString(1, value);
				pstmupdate.setInt(2, custId.intValue());
				pstmupdate.setString(3, key);
				pstmupdate.addBatch();
			}
			int[] j= pstmupdate.executeBatch();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (pstm != null)
					pstm.close();
				if(pstmupdate != null)
					pstmupdate.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sail.cot.dao.sample.CotElementsDao#getCustNoByEleId(java.lang.String)
	 */
	public String getCustNoByEleId(String EleId, Integer custId) {
		Map custmap = this.getCustNoListMapByEleId(EleId, custId);
		Object obj = custmap.get(EleId.toLowerCase());
		if (obj == null)
			return null;
		String res = String.valueOf(custmap.get(EleId.toLowerCase()));
		return res;
	}

	// 获得暂无图片的图片字节
	public byte[] getZwtpPic() {
		// 获得默认图片的
		String fileLength = ContextUtil.getProperty("sysconfig.properties",
				"maxLength");
		// 获得tomcat路径
		String classPath = CotReportServiceImpl.class.getResource("/")
				.toString();
		String systemPath = classPath.substring(6, classPath.length() - 16);
		File picFile = new File(systemPath + "common/images/zwtp.png");
		FileInputStream in;
		try {
			in = new FileInputStream(picFile);
			byte[] b = new byte[Integer.parseInt(fileLength)];
			while (in.read(b) != -1) {
			}
			in.close();
			return b;
		} catch (Exception e1) {
			logger.error("设置样品图片错误!");
			return null;
		}
	}

	// 根据币种换算价格
	public float updatePrice(Float price, Integer oldCurId, Integer newCurId) {
		if (price == null) {
			return 0;
		}
		// 先根据该的币种的汇率转成人民币,
		CotCurrency oldCur = (CotCurrency) super.getById(CotCurrency.class,
				oldCurId);
		Float rmb =0f;
		if(oldCur!=null){
			rmb = price * oldCur.getCurRate();
		}
		// 在根据币种的汇率转成该币种的值
		CotCurrency newCur = (CotCurrency) super.getById(CotCurrency.class,
				newCurId);
		Float obj = 0f;
		if(newCur!=null){
			obj = rmb / newCur.getCurRate();
		}
		DecimalFormat nbf = new DecimalFormat("0.000");
		obj = Float.parseFloat(nbf.format(obj));
		return obj;
	}

	public int getRecordsCountJDBC(QueryInfo queryInfo) {
		return super.getRecordsCountJDBC(queryInfo);
	}

	public void deleteEleCustNoById(Integer id) {
		String strSql = "delete from cot_ele_cust  where 1=1"
				+ " and id=" + id;
		int res = 0;
		Connection conn = null;
		PreparedStatement pstm = null;
		try {
			conn = super.getSession().connection();
			pstm = conn.prepareStatement(strSql);
			res = pstm.executeUpdate();

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (pstm != null)
				try {
					pstm.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
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
	
	//根据查询条件查询样品编号字符串
	public String findEles(String queryStringHQL) throws IOException {
		String sql = "select e.ID from cot_elements_new e"+queryStringHQL;
		Connection con = null;
		ResultSet rs = null;
		//按货号名称排序
		sql+=" order by e.ELE_ID";
		String ids = "";
		try {
			con = super.getSession().connection();
			Statement stmt = con.createStatement();
			rs = stmt.executeQuery(sql);
			while (rs.next()) {
				ids+=rs.getInt(1)+",";
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("查询失败!样品查询异常");
		} finally {
			try {
				rs.close();
				con.close();
			} catch (SQLException e) {
				logger.error("查询失败!样品查询异常");
			}
		}
		return ids;
	}

	
}
