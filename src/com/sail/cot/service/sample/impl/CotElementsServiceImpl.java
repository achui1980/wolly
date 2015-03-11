/**
 * 
 */
package com.sail.cot.service.sample.impl;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpSession;

import net.sourceforge.jeval.EvaluationException;
import net.sourceforge.jeval.Evaluator;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.beanutils.ConvertUtilsBean;
import org.apache.commons.beanutils.PropertyUtilsBean;
import org.apache.commons.beanutils.converters.SqlDateConverter;
import org.apache.log4j.Logger;
import org.directwebremoting.WebContext;
import org.directwebremoting.WebContextFactory;

import com.jason.core.exception.DAOException;
import com.sail.cot.dao.CotExportRptDao;
import com.sail.cot.dao.sample.CotElementsDao;
import com.sail.cot.domain.CotBoxPacking;
import com.sail.cot.domain.CotBoxType;
import com.sail.cot.domain.CotClause;
import com.sail.cot.domain.CotCurrency;
import com.sail.cot.domain.CotCustomer;
import com.sail.cot.domain.CotEleCfg;
import com.sail.cot.domain.CotEleFittings;
import com.sail.cot.domain.CotEleOther;
import com.sail.cot.domain.CotElePacking;
import com.sail.cot.domain.CotElePic;
import com.sail.cot.domain.CotElePrice;
import com.sail.cot.domain.CotElementsNew;
import com.sail.cot.domain.CotFactory;
import com.sail.cot.domain.CotFittings;
import com.sail.cot.domain.CotPicture;
import com.sail.cot.domain.CotServerInfo;
import com.sail.cot.domain.CotSyslog;
import com.sail.cot.domain.CotTypeLv1;
import com.sail.cot.domain.vo.CotEleIdCustNo;
import com.sail.cot.domain.vo.CotEleVOForPrint;
import com.sail.cot.domain.vo.CotElementsVO;
import com.sail.cot.domain.vo.CotGivenVO;
import com.sail.cot.domain.vo.CotOrderFacVO;
import com.sail.cot.domain.vo.CotOrderVO;
import com.sail.cot.domain.vo.CotPriceVO;
import com.sail.cot.query.QueryInfo;
import com.sail.cot.service.img.CotOpImgService;
import com.sail.cot.service.sample.CotElementsService;
import com.sail.cot.service.system.CotSeqService;
import com.sail.cot.service.system.CotSysLogService;
import com.sail.cot.service.system.impl.CotSeqServiceImpl;
import com.sail.cot.util.Log4WebUtil;
import com.sail.cot.util.ReflectionUtils;
import com.sail.cot.util.SystemDicUtil;
import com.sail.cot.util.SystemInfoUtil;
import com.sail.cot.util.SystemUtil;

/**
 * 样品管理模块
 * 
 * @author qh-chzy
 * 
 */
public class CotElementsServiceImpl implements CotElementsService {

	private CotElementsDao ElementsDao;
	private CotExportRptDao rptDao;
	private CotSysLogService sysLogService;
	private SystemDicUtil systemDicUtil = new SystemDicUtil();
	private Logger logger = Log4WebUtil.getLogger(CotElementsServiceImpl.class);

	// 保存样品和包装信息和图片信息
	public Integer saveElement(CotElementsNew e, String eleAddTime) {
		// //没选择厂家时,设置厂家为0
		// if(e.getFactoryId()==null){
		// CotFactory facDefault = this.getElementsDao().findFactoryDefault();
		// e.setFactoryId(facDefault.getId());
		// }
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		try {
			if (!eleAddTime.equals("")) {
				// 样品建档时间
				e.setEleAddTime(new Date(sdf.parse(eleAddTime).getTime()));
			}
		} catch (Exception ex) {
			logger.error("保存建档时间错误!");
			return null;
		}
		// 样品添加时间
		//e.setEleAddTime(new Date(System.currentTimeMillis()));
		// 获得tomcat路径
		String classPath = CotElementsServiceImpl.class.getResource("/").toString();
		String systemPath = classPath.substring(6, classPath.length() - 16);
		File picFile = new File(systemPath + e.getPicPath());
		FileInputStream in;
		byte[] b = new byte[(int) picFile.length()];
		try {
			in = new FileInputStream(picFile);

			while (in.read(b) != -1) {
			}
			in.close();
			// cotPicture.setPicImg(b);
			// e.setPicImg(b);
			if (!e.getPicPath().contains("common/images/zwtp.png")) {
				e.setPicName(e.getEleId());
				// 删除上传的图片
				picFile.delete();
			} else {
				e.setPicName("zwtp");
			}
		} catch (Exception e1) {
			logger.error("设置样品图片错误!");
		}
		WebContext ctx = WebContextFactory.get();
		Integer empId = (Integer) ctx.getSession().getAttribute("empId");
		// 保存样品
		try {
			this.getElementsDao().create(e);

			// 保存到系统日记表
			CotSyslog cotSyslog = new CotSyslog();
			cotSyslog.setEmpId(empId);
			cotSyslog.setOpMessage("添加样品成功");
			cotSyslog.setOpModule("elements");
			cotSyslog.setOpTime(new Date(System.currentTimeMillis()));
			cotSyslog.setOpType(1);
			cotSyslog.setItemNo(e.getEleId());
			sysLogService.addSysLogByObj(cotSyslog);
		} catch (Exception ex) {
			logger.error("保存样品失败!");
			// 保存到系统日记表
			CotSyslog cotSyslog = new CotSyslog();
			cotSyslog.setEmpId(empId);
			cotSyslog.setOpMessage("添加样品失败,失败原因:" + ex.getMessage());
			cotSyslog.setOpModule("elements");
			cotSyslog.setOpTime(new Date(System.currentTimeMillis()));
			cotSyslog.setOpType(1);
			cotSyslog.setItemNo(e.getEleId());
			sysLogService.addSysLogByObj(cotSyslog);
			return null;
		}
		//更新样品货号设置的序列号
		CotSeqService cotSeqService = new CotSeqServiceImpl();
		cotSeqService.saveSeq("ele");

		// 添加样品图片到ElePic表 modify by achui 20090213
		CotOpImgService impOpService = (CotOpImgService) SystemUtil.getService("CotOpImgService");
		CotElePic elePic = new CotElePic();
		elePic.setEleId(e.getEleId());
		elePic.setFkId(e.getId());
		elePic.setPicImg(b);
		elePic.setPicSize(b.length);
		elePic.setPicName(e.getEleId());
		List imgList = new ArrayList();
		imgList.add(elePic);
		impOpService.saveImg(imgList);
		return e.getId();
	}

	// 保存样品和包装信息和图片信息
	public Boolean saveByCopy(CotElementsNew e, String newEleId, String eleAddTime) {
//		CotElementsNew elenew = this.getEleById(e.getId());
//		String sqlString = " from CotEleFittings obj where obj.eleId=" + e.getId();
//		// 获取样品配件信息
//		List fitList = this.getElementsDao().find(sqlString);

//		String str = " from CotElePrice obj where obj.eleId=" + e.getId();
//		// 获取样品成本信息
//		List priceList = this.getElementsDao().find(str);

		// String hqlString = " from CotElePacking obj where obj.eleId=" +
		// e.getId();
		// // 获取包材成本信息
		// List<?> packList = this.getElementsDao().find(hqlString);

		// 设置新货号
		e.setEleId(newEleId.toUpperCase());
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		try {
			if (!eleAddTime.equals("")) {
				// 样品开发时间
				e.setEleAddTime(new Date(sdf.parse(eleAddTime).getTime()));
			}
		} catch (Exception ex) {
			logger.error("保存开发时间错误!");
			return false;
		}
		// 样品添加时间
		//e.setEleAddTime(new Date(System.currentTimeMillis()));
//		WebContext ctx = WebContextFactory.get();
//		Integer empId = (Integer) ctx.getSession().getAttribute("empId");

		// 获取原样品的图片
		CotOpImgService impOpService = (CotOpImgService) SystemUtil.getService("CotOpImgService");
		String picHql = "from CotElePic obj where obj.fkId=" + e.getId();
		List picList = this.getElementsDao().find(picHql);
		CotElePic oldPic = (CotElePic) picList.get(0);

		// 保存样品
		try {
//			e.setElePrice(elenew.getElePrice());
//			e.setEleFittingPrice(elenew.getEleFittingPrice());
			List listEle = new ArrayList();
			//另存为新货号是,条形码清空
			e.setBarcode("");
			listEle.add(e);
			this.getElementsDao().saveRecords(listEle);
			// 拷贝图片
			CotElePic elePic = new CotElePic();
			elePic.setEleId(newEleId.toUpperCase());
			elePic.setFkId(e.getId());
			elePic.setPicImg(oldPic.getPicImg());
			elePic.setPicSize(oldPic.getPicSize());
			elePic.setPicName(newEleId.toUpperCase());
			List imgList = new ArrayList();
			imgList.add(elePic);
			impOpService.saveImg(imgList);

			// 同步样品成本信息
//			List elePriceList = new ArrayList();
//			if (priceList != null) {
//				Iterator iterator = priceList.iterator();
//				while (iterator.hasNext()) {
//					CotElePrice elePrice = (CotElePrice) iterator.next();
//					elePrice.setId(null);
//					elePrice.setEleId(e.getId());
//					elePriceList.add(elePrice);
//				}
//			}
//			this.getElementsDao().saveRecords(elePriceList);
			// 同步配件信息
//			List eleFittingsList = new ArrayList();
//
//			if (fitList != null) {
//				Iterator iterator = fitList.iterator();
//				while (iterator.hasNext()) {
//					CotEleFittings eleFittings = (CotEleFittings) iterator.next();
//					eleFittings.setId(null);
//					eleFittings.setEleId(e.getId());
//					eleFittings.setEleChild(null);
//					eleFittings.setEleChildId(null);
//					eleFittingsList.add(eleFittings);
//				}
//			}
//			this.getElementsDao().saveRecords(eleFittingsList);

			// 同步包材信息
			// List<CotElePacking> elepackList = new ArrayList<CotElePacking>();
			// if (packList != null) {
			// Iterator iterator = packList.iterator();
			// while (iterator.hasNext()) {
			// CotElePacking elePacking = (CotElePacking) iterator.next();
			// elePacking.setId(null);
			// elePacking.setEleId(e.getId());
			//
			// elepackList.add(elePacking);
			// }
			// }
			// this.getElementsDao().saveRecords(elepackList);

			// 保存到系统日记表
//			CotSyslog cotSyslog = new CotSyslog();
//			cotSyslog.setEmpId(empId);
//			cotSyslog.setOpMessage("另存样品成功");
//			cotSyslog.setOpModule("elements");
//			cotSyslog.setOpTime(new Date(System.currentTimeMillis()));
//			cotSyslog.setOpType(1);
//			cotSyslog.setItemNo(e.getEleId());
//			sysLogService.addSysLogByObj(cotSyslog);
		} catch (Exception ex) {
			ex.printStackTrace();
//			logger.error("另存样品失败!", ex);
//			// 保存到系统日记表
//			CotSyslog cotSyslog = new CotSyslog();
//			cotSyslog.setEmpId(empId);
//			cotSyslog.setOpMessage("另存样品失败,失败原因:" + ex.getMessage());
//			cotSyslog.setOpModule("elements");
//			cotSyslog.setOpTime(new Date(System.currentTimeMillis()));
//			cotSyslog.setOpType(1);
//			cotSyslog.setItemNo(e.getEleId());
//			sysLogService.addSysLogByObj(cotSyslog);
		}
		return true;
	}

	// 更新
	public Boolean modifyElements(CotElementsNew e, String eleAddTime) {
		// 没选择厂家时,设置厂家为'未定义'
		// if(e.getFactoryId()==null){
		// CotFactory facDefault = this.getElementsDao().findFactoryDefault();
		// e.setFactoryId(facDefault.getId());
		// }
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		// 样品编辑时间
		//e.setEleAddTime(new Date(System.currentTimeMillis()));
		try {
			if (!"".equals(eleAddTime)) {
				// 样品建档时间
				e.setEleAddTime(new Date(sdf.parse(eleAddTime).getTime()));
			}
		} catch (Exception ex) {
			logger.error("保存建档时间错误!");
			return false;
		}
		// 设置图片
		// String hql = "select obj.picImg from CotElementsNew obj where
		// obj.id="+e.getId();
		// List<?> list = this.getElementsDao().find(hql);
		// e.setPicImg((byte[])list.get(0));
		WebContext ctx = WebContextFactory.get();
		Integer empId = (Integer) ctx.getSession().getAttribute("empId");
		// 保存样品
		try {
			this.getElementsDao().update(e);
			// 保存到系统日记表
			CotSyslog cotSyslog = new CotSyslog();
			cotSyslog.setEmpId(empId);
			cotSyslog.setOpMessage("修改样品成功");
			cotSyslog.setOpModule("elements");
			cotSyslog.setOpTime(new Date(System.currentTimeMillis()));
			cotSyslog.setOpType(2);
			cotSyslog.setItemNo(e.getEleId());
			sysLogService.addSysLogByObj(cotSyslog);
		} catch (Exception ex) {
			logger.error("修改样品失败!");
			CotSyslog cotSyslog = new CotSyslog();
			cotSyslog.setEmpId(empId);
			cotSyslog.setOpMessage("修改样品失败,失败原因:" + ex.getMessage());
			cotSyslog.setOpModule("elements");
			cotSyslog.setOpTime(new Date(System.currentTimeMillis()));
			cotSyslog.setOpType(2);
			cotSyslog.setItemNo(e.getEleId());
			sysLogService.addSysLogByObj(cotSyslog);
		}

		return true;
	}

	// 保存或更新子样品
	public Integer saveOrUpdateChild(CotElementsNew e, String eleProTime) {
		// 没选择厂家时,设置厂家为'未定义'
		// if(e.getFactoryId()==null){
		// CotFactory facDefault = this.getElementsDao().findFactoryDefault();
		// e.setFactoryId(facDefault.getId());
		// }
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		// 样品编辑时间
		e.setEleAddTime(new Date(System.currentTimeMillis()));
		try {
			if (!"".equals(eleProTime)) {
				// 样品开发时间
				e.setEleProTime(new Date(sdf.parse(eleProTime).getTime()));
			}
		} catch (Exception ex) {
			logger.error("保存开发时间错误!");
			return null;
		}
		byte[] b = null;
		if (e.getId() == null) {
			// 获得tomcat路径
			String classPath = CotElementsServiceImpl.class.getResource("/").toString();
			String systemPath = classPath.substring(6, classPath.length() - 16);
			File picFile = new File(systemPath + e.getPicPath());
			FileInputStream in;
			b = new byte[(int) picFile.length()];
			try {
				in = new FileInputStream(picFile);

				while (in.read(b) != -1) {
				}
				in.close();
				// cotPicture.setPicImg(b);
				// e.setPicImg(b);
				if (!"common/images/zwtp.png".equals(e.getPicPath())) {
					e.setPicName(e.getEleId());
					// 删除上传的图片
					picFile.delete();
				} else {
					e.setPicName("zwtp");
				}
			} catch (Exception e1) {
				logger.error("设置子样品图片错误!");
			}
		}
		WebContext ctx = WebContextFactory.get();
		Integer empId = (Integer) ctx.getSession().getAttribute("empId");
		// 保存样品
		try {
			List<CotElementsNew> newList = new ArrayList<CotElementsNew>();
			newList.add(e);
			this.getElementsDao().saveOrUpdateRecords(newList);
			// 保存到系统日记表
			CotSyslog cotSyslog = new CotSyslog();
			cotSyslog.setEmpId(empId);
			cotSyslog.setOpMessage("编辑子样品成功");
			cotSyslog.setOpModule("elements");
			cotSyslog.setOpTime(new Date(System.currentTimeMillis()));
			cotSyslog.setOpType(2);
			cotSyslog.setItemNo(e.getEleId());
			sysLogService.addSysLogByObj(cotSyslog);
		} catch (Exception ex) {
			logger.error("编辑子样品失败!");
			CotSyslog cotSyslog = new CotSyslog();
			cotSyslog.setEmpId(empId);
			cotSyslog.setOpMessage("编辑子样品失败,失败原因:" + ex.getMessage());
			cotSyslog.setOpModule("elements");
			cotSyslog.setOpTime(new Date(System.currentTimeMillis()));
			cotSyslog.setOpType(2);
			cotSyslog.setItemNo(e.getEleId());
			sysLogService.addSysLogByObj(cotSyslog);
			return null;
		}
		if (b != null) {
			// 添加样品图片到ElePic表 modify by achui 20090213
			CotOpImgService impOpService = (CotOpImgService) SystemUtil.getService("CotOpImgService");
			CotElePic elePic = new CotElePic();
			elePic.setEleId(e.getEleId());
			elePic.setFkId(e.getId());
			elePic.setPicImg(b);
			elePic.setPicSize(b.length);
			elePic.setPicName(e.getEleId());
			List imgList = new ArrayList();
			imgList.add(elePic);
			impOpService.saveImg(imgList);
		}

		return e.getId();
	}

	// 删除
	public Boolean deleteElements(List<Integer> ids) {
		WebContext ctx = WebContextFactory.get();
		Integer empId = (Integer) ctx.getSession().getAttribute("empId");
		try {
			this.getElementsDao().deleteRecordByIds(ids, "CotElementsNew");
			CotSyslog cotSyslog = new CotSyslog();
			cotSyslog.setEmpId(empId);
			cotSyslog.setOpMessage("批量删除样品成功");
			cotSyslog.setOpModule("elements");
			cotSyslog.setOpTime(new Date(System.currentTimeMillis()));
			cotSyslog.setOpType(3);
			sysLogService.addSysLogByObj(cotSyslog);
		} catch (Exception e) {
			logger.error("删除样品信息异常", e);
			CotSyslog cotSyslog = new CotSyslog();
			cotSyslog.setEmpId(empId);
			cotSyslog.setOpMessage("批量删除样品失败,失败原因:" + e.getMessage());
			cotSyslog.setOpModule("elements");
			cotSyslog.setOpTime(new Date(System.currentTimeMillis()));
			cotSyslog.setOpType(3);
			sysLogService.addSysLogByObj(cotSyslog);
			return false;
		}
		return true;
	}

	// 根据编号查询样品
	public CotElementsNew getElementsById(Integer id) {
		Object object = this.getElementsDao().getById(CotElementsNew.class, id);
		if (object != null) {
			return (CotElementsNew) object;
		} else {
			return null;
		}
	}

	// 根据编号查询样品
	public CotElementsNew getEleById(Integer id) {
		CotElementsNew cotElementsNew = (CotElementsNew) this.getElementsDao().getById(CotElementsNew.class, id);
		if (cotElementsNew != null) {
			cotElementsNew.setPicImg(null);
			cotElementsNew.setCotPictures(null);
			cotElementsNew.setCotFile(null);
			cotElementsNew.setChilds(null);
			cotElementsNew.setCotPriceFacs(null);
			cotElementsNew.setCotEleFittings(null);
			cotElementsNew.setCotElePrice(null);
			cotElementsNew.setCotElePacking(null);
			return cotElementsNew;
		} else {
			return null;
		}
	}

	// 查询所有厂家
	public Map<?, ?> getFactoryNameMap() {
		Map<String, String> map = new HashMap<String, String>();
		List<?> list = this.getElementsDao().getRecords("CotFactory");
		for (int i = 0; i < list.size(); i++) {
			CotFactory cotFactory = (CotFactory) list.get(i);
			map.put(cotFactory.getId().toString(), cotFactory.getShortName());
		}
		return map;
	}

	// 查询配件厂家
	public Map<?, ?> getFittingFacMap() {
		Map<String, String> map = new HashMap<String, String>();
		String hql = " from CotFactory obj where obj.factroyTypeidLv1 = 2";
		List<?> list = this.getElementsDao().find(hql);
		for (int i = 0; i < list.size(); i++) {
			CotFactory cotFactory = (CotFactory) list.get(i);
			map.put(cotFactory.getId().toString(), cotFactory.getShortName());
		}
		return map;
	}

	// 查询包材厂家
	public Map<?, ?> getPackingFacMap() {
		Map<String, String> map = new HashMap<String, String>();
		String hql = " from CotFactory obj where obj.factroyTypeidLv1 = 3";
		List<?> list = this.getElementsDao().find(hql);
		for (int i = 0; i < list.size(); i++) {
			CotFactory cotFactory = (CotFactory) list.get(i);
			map.put(cotFactory.getId().toString(), cotFactory.getShortName());
		}
		return map;
	}

	// 查询所有币种名称
	public Map<?, ?> getCurrencyNameMap() {
		Map<String, String> map = new HashMap<String, String>();
		List<?> list = this.getElementsDao().getRecords("CotCurrency");
		for (int i = 0; i < list.size(); i++) {
			CotCurrency cotCurrency = (CotCurrency) list.get(i);
			map.put(cotCurrency.getId().toString(), cotCurrency.getCurNameEn());
		}
		return map;
	}

	// 查询所有客户简称
	public Map<?, ?> getCusShortNameMap() {
		Map<String, String> map = new HashMap<String, String>();
		List<?> list = this.getElementsDao().getRecords("CotCustomer");
		for (int i = 0; i < list.size(); i++) {
			CotCustomer cotCustomer = (CotCustomer) list.get(i);
			map.put(cotCustomer.getId().toString(), cotCustomer.getCustomerShortName());
		}
		return map;
	}

	// 查询所有业务员名称
	public Map<?, ?> getEmpsNameMap() {
		Map<String, String> map = new HashMap<String, String>();
		String sql = "select obj.id,obj.empsName from CotEmps obj";
		List<?> list = this.getElementsDao().find(sql);
		for (int i = 0; i < list.size(); i++) {
			Object[] cotEmps = (Object[]) list.get(i);
			map.put(cotEmps[0].toString(), (String) cotEmps[1]);
		}
		return map;
	}

	// 查询所有条款名称
	public Map<?, ?> getClauseNameMap() {
		Map<String, String> map = new HashMap<String, String>();
		List<?> list = this.getElementsDao().getRecords("CotClause");
		for (int i = 0; i < list.size(); i++) {
			CotClause cotClause = (CotClause) list.get(i);
			map.put(cotClause.getId().toString(), cotClause.getClauseName());
		}
		return map;
	}

	// 获取报价单号映射
	public Map<?, ?> getPriceNoMap() {
		Map<String, String> map = new HashMap<String, String>();
		String sql = "select obj.id,obj.priceNo from CotPrice obj";
		List<?> list = this.getElementsDao().find(sql);
		for (int i = 0; i < list.size(); i++) {
			Object[] cotPrice = (Object[]) list.get(i);
			map.put((String) cotPrice[0], (String) cotPrice[1]);
		}
		return map;
	}

	// 获取报价日期映射
	public Map<?, ?> getPriceTimeMap() {
		Map<String, String> map = new HashMap<String, String>();
		String sql = "select obj.id,obj.priceTime from CotPrice obj";
		List<?> list = this.getElementsDao().find(sql);
		java.text.SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		for (int i = 0; i < list.size(); i++) {
			Object[] cotPrice = (Object[]) list.get(i);
			map.put((String) cotPrice[0], sdf.format((Date) cotPrice[1]));
		}
		return map;
	}

	// 转换时间格式
	public Timestamp getStampTime(String time) {

		Timestamp timestamp = null;
		if (time != null && !time.equals("")) {
			java.text.SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			try {
				Date date = (Date) sdf.parse(time);
				timestamp = new Timestamp(date.getTime());
			} catch (ParseException e1) {

				e1.printStackTrace();
			}
			return timestamp;
		}
		return timestamp;
	}

	// 转换时间格式
	public String getStringTime(Timestamp time) {

		String stringTime = null;
		if (time != null) {
			stringTime = time.toString();
			return stringTime;
		}
		return stringTime;
	}

	// 获取订单单号映射
	public Map<?, ?> getOrderNoMap() {
		Map<String, String> map = new HashMap<String, String>();
		String sql = "select obj.id,obj.orderNo from CotOrder obj";
		List<?> list = this.getElementsDao().find(sql);
		for (int i = 0; i < list.size(); i++) {
			Object[] cotOrder = (Object[]) list.get(i);
			map.put((String) cotOrder[0], (String) cotOrder[1]);
		}
		return map;
	}

	// 获取送样单号映射
	public Map<?, ?> getGivenNoMap() {
		Map<String, String> map = new HashMap<String, String>();
		String sql = "select obj.id,obj.givenNo from CotGiven obj";
		List<?> list = this.getElementsDao().find(sql);
		for (int i = 0; i < list.size(); i++) {
			Object[] cotGiven = (Object[]) list.get(i);
			map.put(cotGiven[0].toString(), (String) cotGiven[1]);
		}
		return map;
	}

	// 获取征样单号映射
	public Map<?, ?> getSignNoMap() {
		Map<String, String> map = new HashMap<String, String>();
		String sql = "select obj.id,obj.signNo from CotSign obj";
		List<?> list = this.getElementsDao().find(sql);
		for (int i = 0; i < list.size(); i++) {
			Object[] cotSign = (Object[]) list.get(i);
			map.put(cotSign[0].toString(), (String) cotSign[1]);
		}
		return map;
	}

	// 获得样品材质的映射
	public Map<?, ?> getTypeMap() {
		Map<String, String> map = new HashMap<String, String>();
		List<?> list = this.getElementsDao().getRecords("CotTypeLv1");
		for (int i = 0; i < list.size(); i++) {
			CotTypeLv1 cotTypeLv1 = (CotTypeLv1) list.get(i);
			map.put(cotTypeLv1.getId().toString(), cotTypeLv1.getTypeName());
		}
		return map;
	}

	// 获得客户编号的映射
	public Map<?, ?> getCustomerNoMap() {
		Map<String, String> map = new HashMap<String, String>();
		String sql = "select obj.id,obj.customerNo from CotCustomer obj";
		List<?> list = this.getElementsDao().find(sql);
		for (int i = 0; i < list.size(); i++) {
			Object[] cotCustomer = (Object[]) list.get(i);
			map.put(cotCustomer[0].toString(), (String) cotCustomer[1]);
		}
		return map;
	}

	// 获得客户简称的映射
	public Map<?, ?> getCustomerShortNameMap() {
		Map<String, String> map = new HashMap<String, String>();
		String sql = "select obj.id,obj.customerShortName from CotCustomer obj";
		List<?> list = this.getElementsDao().find(sql);
		for (int i = 0; i < list.size(); i++) {
			Object[] cotCustomer = (Object[]) list.get(i);
			map.put(cotCustomer[0].toString(), (String) cotCustomer[1]);
		}
		return map;
	}

	// 根据条件查询主报价单记录
	public List<?> getPriceList(QueryInfo queryInfo) {
		try {
			List<?> records = this.getElementsDao().findRecords(queryInfo);
			List<CotPriceVO> list = new ArrayList<CotPriceVO>();
			for (int i = 0; i < records.size(); i++) {
				Object[] obj = (Object[]) records.get(i);
				CotPriceVO cotPriceVO = new CotPriceVO();
				if (obj[0] != null) {
					cotPriceVO.setId((Integer) obj[0]);
				}
				if (obj[1] != null) {
					cotPriceVO.setPriceTime((Timestamp) obj[1]);
				}
				if (obj[2] != null) {
					cotPriceVO.setPriceNo(obj[2].toString());
				}
				if (obj[3] != null) {
					cotPriceVO.setEmpId((Integer) obj[3]);
				}
				if (obj[4] != null) {
					cotPriceVO.setCustomerShortName(obj[4].toString());
				}
				if (obj[5] != null) {
					cotPriceVO.setPriceOut(Float.parseFloat(obj[5].toString()));
				}
				if (obj[6] != null) {
					cotPriceVO.setCurrencyId((Integer) obj[6]);
				}
				if (obj[7] != null) {
					cotPriceVO.setClauseId((Integer) obj[7]);
				}
				if (obj[8] != null) {
					cotPriceVO.setBusinessPerson((Integer) obj[8]);
				}
				if (obj[9] != null) {
					cotPriceVO.setPricePrice(Float.parseFloat(obj[9].toString()));
				}
				if (obj[10] != null) {
					cotPriceVO.setBoxTypeId(new Integer(obj[10].toString()));
				}
				if (obj[11] != null) {
					cotPriceVO.setValidMonths(new Integer(obj[11].toString()));
				}
				if (obj[12] != null) {
					cotPriceVO.setPriceCompose(obj[12].toString());
				}
				if (obj[13] != null) {
					cotPriceVO.setPriceRate(Float.parseFloat(obj[13].toString()));
				}
				if (obj[15] != null) {
					cotPriceVO.setPriceFacUint(Integer.parseInt(obj[15].toString()));
				}
				if (obj[14] != null) {
					cotPriceVO.setPriceFac(Float.parseFloat(obj[14].toString()));
				}
				if (obj[16] != null) {
					cotPriceVO.setPriceStatus(Long.parseLong(obj[16].toString()));
				}
				list.add(cotPriceVO);
			}
			return list;
		} catch (DAOException e) {
			e.printStackTrace();
			logger.error("查询出错");
		}
		return null;
	}

	// 根据条件查询主报价单记录
	public List<?> getGivenList(QueryInfo queryInfo) {
		try {
			List<?> records = this.getElementsDao().findRecords(queryInfo);
			List<CotGivenVO> list = new ArrayList<CotGivenVO>();
			for (int i = 0; i < records.size(); i++) {
				Object[] obj = (Object[]) records.get(i);
				CotGivenVO cotGivenVO = new CotGivenVO();
				if (obj[0] != null) {
					cotGivenVO.setId((Integer) obj[0]);
				}
				if (obj[1] != null) {
					cotGivenVO.setGivenTime((Timestamp) obj[1]);
				}
				if (obj[2] != null) {
					cotGivenVO.setGivenNo(obj[2].toString());
				}
				if (obj[3] != null) {
					cotGivenVO.setEmpId((Integer) obj[3]);
				}
				if (obj[4] != null) {
					cotGivenVO.setCustomerShortName(obj[4].toString());
				}
				if (obj[5] != null) {
					cotGivenVO.setFactoryid((Integer) obj[5]);
				}
				if (obj[6] != null) {
					cotGivenVO.setSignRequire(obj[6].toString());
				}
				if (obj[7] != null) {
					cotGivenVO.setRealGiventime((Date) obj[7]);
				}
				if (obj[8] != null) {
					cotGivenVO.setGivenAddr(obj[8].toString());
				}
				list.add(cotGivenVO);
			}
			return list;
		} catch (DAOException e) {
			e.printStackTrace();
			logger.error("查询出错");
		}
		return null;
	}

	// 根据条件查询主报价单记录
	public List<?> getOrderList(QueryInfo queryInfo) {
		try {
			List<?> records = this.getElementsDao().findRecords(queryInfo);
			List<CotOrderVO> list = new ArrayList<CotOrderVO>();
			for (int i = 0; i < records.size(); i++) {
				Object[] obj = (Object[]) records.get(i);
				CotOrderVO cotOrderVO = new CotOrderVO();
				if (obj[0] != null) {
					cotOrderVO.setId((Integer) obj[0]);
				}
				if (obj[1] != null) {
					cotOrderVO.setOrderTime((Timestamp) obj[1]);
				}
				if (obj[2] != null) {
					cotOrderVO.setTotalCount((Integer) obj[2]);
				}
				if (obj[3] != null) {
					cotOrderVO.setTotalMoney(Float.parseFloat(obj[3].toString()));
				}
				if (obj[4] != null) {
					cotOrderVO.setOrderNo(obj[4].toString());
				}
				if (obj[5] != null) {
					cotOrderVO.setEmpId((Integer) obj[5]);
				}
				if (obj[6] != null) {
					cotOrderVO.setCustomerShortName(obj[6].toString());
				}
				if (obj[7] != null) {
					cotOrderVO.setPriceFac(Float.parseFloat(obj[7].toString()));
				}
				if (obj[8] != null) {
					cotOrderVO.setPriceFacUint((Integer) obj[8]);
				}
				if (obj[9] != null) {
					cotOrderVO.setPriceOut(Float.parseFloat(obj[9].toString()));
				}
				if (obj[10] != null) {
					cotOrderVO.setPriceOutUint((Integer) obj[10]);
				}
				if (obj[11] != null) {
					cotOrderVO.setBussinessPerson(obj[11].toString());
				}
				if (obj[12] != null) {
					cotOrderVO.setOrderPrice(Float.parseFloat(obj[12].toString()));
				}
				if (obj[13] != null) {
					cotOrderVO.setCurrencyId((Integer) obj[13]);
				}
				if (obj[14] != null) {
					cotOrderVO.setBoxCount((Long) obj[14]);
				}
				if (obj[15] != null) {
					cotOrderVO.setBoxTypeId((Integer) obj[15]);
				}
				if (obj[16] != null) {
					cotOrderVO.setOrderCompose(obj[16].toString());
				}

				if (obj[17] != null) {
					cotOrderVO.setOrderStatus((Long) obj[17]);
				}
				list.add(cotOrderVO);
			}
			return list;
		} catch (DAOException e) {
			e.printStackTrace();
			logger.error("查询出错");
		}
		return null;
	}

	// 查询所有工厂信息
	public List<?> getFactoryList() {
		return this.getElementsDao().getRecords("CotFactory");
	}

	// 查询所有材质
	public List<?> loadTypeLv1List() {
		return this.getElementsDao().getRecords("CotTypeLv1");
	}

	// 查询所有产品分类
	public List<?> loadTypeLv2List() {
		return this.getElementsDao().getRecords("CotTypeLv2");
	}

	// 查询图片名称是否存在
	public boolean findExistName(String name) {
		List<?> list = this.getElementsDao().find("select count(*) from CotPicture p where p.picName='" + name + "'");
		int count = (Integer) list.get(0);
		if (count == 0) {
			return false;
		} else {
			return true;
		}
	}

	// 通过图片Id删除图片
	public Boolean deletePictureByPicId(Integer picId) {
		try {
			this.getElementsDao().deleteRecordById(picId, "CotPicture");
			return true;
		} catch (DAOException e) {
			e.printStackTrace();
			return false;
		}
	}

	// 查找所有海关信息
	public List<?> getEleOtherList() {
		return this.getElementsDao().getRecords("CotEleOther");
	}

	// 查找所有币种信息
	public List<?> getCurrencyList() {
		return this.getElementsDao().getRecords("CotCurrency");
	}

	// 查找所有包装类型
	public List<?> getBoxTypeList() {
		return this.getElementsDao().getRecords("CotBoxType");
	}

	// 通过编号查找包装类型
	public CotBoxType getBoxTypeById(Integer id) {
		if (id == null) {
			return null;
		} else {
			return (CotBoxType) this.getElementsDao().getById(CotBoxType.class, id);
		}
	}

	// 通过编号查找包装类型名称
	@SuppressWarnings("unchecked")
	public String getBoxNameById(Integer id) {
		List<CotBoxPacking> list = this.getElementsDao().find("from CotBoxPacking obj where obj.id=" + id);
		if (list != null && list.size() > 0) {
			CotBoxPacking cotBoxPacking = (CotBoxPacking) list.get(0);
			String value = cotBoxPacking.getValue();
			String valueEn = cotBoxPacking.getValueEn();
			if (valueEn == null) {
				valueEn = "";
			}
			String composeBoxName = value + "(" + valueEn + ")";
			return composeBoxName;
		}
		return null;
	}

	// 删除数据库中的主样品图片
	public boolean deletePicImg(Integer Id) {
		WebContext ctx = WebContextFactory.get();
		Integer empId = (Integer) ctx.getSession().getAttribute("empId");
		String classPath = CotElementsServiceImpl.class.getResource("/").toString();
		String systemPath = classPath.substring(6, classPath.length() - 16);
		CotOpImgService impOpService = (CotOpImgService) SystemUtil.getService("CotOpImgService");
		String filePath = systemPath + "common/images/zwtp.png";
		CotElePic cotElementsNew = impOpService.getElePicImgByEleId(Id);
		File picFile = new File(filePath);
		FileInputStream in;
		try {
			in = new FileInputStream(picFile);
			byte[] b = new byte[(int) (picFile.length())];
			while (in.read(b) != -1) {
			}
			in.close();
			cotElementsNew.setPicImg(b);
			List<CotElePic> imgList = new ArrayList<CotElePic>();
			imgList.add(cotElementsNew);
			impOpService.modifyImg(imgList);
			CotSyslog cotSyslog = new CotSyslog();
			cotSyslog.setEmpId(empId);
			cotSyslog.setOpMessage("删除样品主图片成功");
			cotSyslog.setOpModule("elements");
			cotSyslog.setOpTime(new Date(System.currentTimeMillis()));
			cotSyslog.setItemNo(Id.toString());
			cotSyslog.setOpType(3);
			sysLogService.addSysLogByObj(cotSyslog);
			return true;

		} catch (Exception e) {
			logger.error("删除主样品图片错误!");
			CotSyslog cotSyslog = new CotSyslog();
			cotSyslog.setEmpId(empId);
			cotSyslog.setOpMessage("删除样品主图片失败,失败原因:" + e.getMessage());
			cotSyslog.setOpModule("elements");
			cotSyslog.setOpTime(new Date(System.currentTimeMillis()));
			cotSyslog.setOpType(3);
			cotSyslog.setItemNo(Id.toString());
			sysLogService.addSysLogByObj(cotSyslog);
			return false;
		}
	}

	// 查找所有报表类型
	public List<?> getReportTypeList() {
		return this.getElementsDao().getRecords("CotRptType");
	}

	// 查找所有报表类型
	public List<?> getRptFileList(Integer rptTypeId) {
		List<?> list = this.getElementsDao().find("from CotRptFile f where f.rptType=" + rptTypeId);
		return list;
	}

	// 根据ids批量修改
	public Boolean modifyBatch(String ids, Map<?, ?> map, Integer flag, boolean fit, boolean price, boolean pack) {
		WebContext ctx = WebContextFactory.get();
		Integer empId = (Integer) ctx.getSession().getAttribute("empId");
		try {

			this.getElementsDao().modifyBatch(ids, map, flag, fit, price, pack);
			// if (pack) {
			// this.updatePackPrice(ids);
			// }
			// 保存到系统日记表
			CotSyslog cotSyslog = new CotSyslog();
			cotSyslog.setEmpId(empId);
			cotSyslog.setOpMessage("批量修改样品成功");
			cotSyslog.setOpModule("elements");
			cotSyslog.setOpTime(new Date(System.currentTimeMillis()));
			cotSyslog.setOpType(2);
			sysLogService.addSysLogByObj(cotSyslog);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			// 保存到系统日记表
			CotSyslog cotSyslog = new CotSyslog();
			cotSyslog.setEmpId(empId);
			cotSyslog.setOpMessage("批量修改样品失败,失败原因:" + e.getMessage());
			cotSyslog.setOpModule("elements");
			cotSyslog.setOpTime(new Date(System.currentTimeMillis()));
			cotSyslog.setOpType(2);
			sysLogService.addSysLogByObj(cotSyslog);
			return false;
		}
	}

	// 根据表格查询条件批量修改
	public Boolean modifyBatchAll(Map<String, String> queryMap, Map<?, ?> map, Integer flag, boolean fit, boolean price, boolean pack) {
		WebContext ctx = WebContextFactory.get();
		Integer empId = (Integer) ctx.getSession().getAttribute("empId");
		try {
			this.getElementsDao().modifyBatchAll(queryMap, map, flag, fit, price, pack);
			// if (pack) {
			// this.updatePackPriceAll();
			// }
			// 保存到系统日记表
			CotSyslog cotSyslog = new CotSyslog();
			cotSyslog.setEmpId(empId);
			cotSyslog.setOpMessage("批量修改样品成功");
			cotSyslog.setOpModule("elements");
			cotSyslog.setOpTime(new Date(System.currentTimeMillis()));
			cotSyslog.setOpType(2);
			sysLogService.addSysLogByObj(cotSyslog);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			// 保存到系统日记表
			CotSyslog cotSyslog = new CotSyslog();
			cotSyslog.setEmpId(empId);
			cotSyslog.setOpMessage("批量修改样品失败,失败原因:" + e.getMessage());
			cotSyslog.setOpModule("elements");
			cotSyslog.setOpTime(new Date(System.currentTimeMillis()));
			cotSyslog.setOpType(2);
			sysLogService.addSysLogByObj(cotSyslog);
			return false;
		}

	}

	// 查询要导出报表的样品数据
	public String[][] findExportData(String ids, String page, String queryString, String queryStringHQL) {
		return this.getElementsDao().findExportData(ids, page, queryString, queryStringHQL);
	}

	// 查询要导出报表的样品数据
	public void saveCheckMachineData(String ids, String page, String queryStringHQL) {
		try {
			this.getElementsDao().saveCheckMachineData(ids, page, queryStringHQL);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public CotElementsDao getElementsDao() {
		return ElementsDao;
	}

	public void setElementsDao(CotElementsDao ElementsDao) {
		this.ElementsDao = ElementsDao;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sail.cot.service.sample.CotElementsService#getQueryCondition(java.lang.String)
	 */
	public List getQueryCondition(String queryType) {

		return this.getElementsDao().getQueryCondition(queryType);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sail.cot.service.sample.CotElementsService#exoprtRpt(java.lang.String)
	 */
	public int getExoprtRpt(String type, String strSql, String rptXMLPath, String exportPath) {

		int res = -1;
		HashMap paramMap = new HashMap();
		paramMap.put("RPT_TITLE", strSql);
		paramMap.put("STR_SQL", strSql);
		if ("XLS".equals(type))
			res = this.getRptDao().exportRptToXLS(rptXMLPath, exportPath, paramMap);
		else if ("PDF".equals(type))
			res = this.getRptDao().exportRptToPDF(rptXMLPath, exportPath, paramMap);
		else if ("HTML".equals(type))
			res = this.getRptDao().exportRptToHTML(rptXMLPath, exportPath, paramMap);
		return res;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sail.cot.service.sample.CotElementsService#getEleImgHtml(int,
	 *      int, com.sail.cot.domain.vo.CotElementsVO)
	 */
	public String getEleImgHtml(int currentPage, int countOnEachPage, CotElementsVO queryCondition, int imgHeigth, int imgWidth) {
		QueryInfo queryInfo = new QueryInfo();
		queryInfo.setStartIndex(currentPage);
		queryInfo.setCountOnEachPage(countOnEachPage);

		StringBuffer queryString = new StringBuffer();
		queryString.append(" where 1=1 and c.id = obj.price_out_unit and c1.id = obj.price_fac_uint");
		if (queryCondition.getEleId() != null && !queryCondition.getEleId().trim().equals("")) {
			queryString.append(" and obj.ELE_ID like '%" + queryCondition.getEleId().trim() + "%'");
		}
		if (queryCondition.getChild() != null && queryCondition.getChild().trim().equals("true")) {
			queryString.append(" and obj.ELE_FLAG=2");
		} else {
			queryString.append(" and obj.ELE_FLAG<>2");
		}
		if (queryCondition.getEleName() != null && !queryCondition.getEleName().trim().equals("")) {
			queryString.append(" and obj.ELE_NAME like '%" + queryCondition.getEleName().trim() + "%'");
		}
		if (queryCondition.getEleNameEn() != null && !queryCondition.getEleNameEn().trim().equals("")) {
			queryString.append(" and obj.ELE_NAME_EN like '%" + queryCondition.getEleNameEn().trim() + "%'");
		}
		if (queryCondition.getFactoryId() != null && !queryCondition.getFactoryId().equals("")) {
			queryString.append(" and obj.FACTORY_ID=" + queryCondition.getFactoryId());
		}
		if (queryCondition.getEleCol() != null && !queryCondition.getEleCol().trim().equals("")) {
			queryString.append(" and obj.ELE_COL like '%" + queryCondition.getEleCol().trim() + "%'");
		}
		if (queryCondition.getEleTypeidLv1() != null && !queryCondition.getEleTypeidLv1().toString().equals("")) {
			queryString.append(" and obj.ELE_TYPEID_LV1=" + queryCondition.getEleTypeidLv1());
		}
		if (queryCondition.getEleTypenameLv2() != null && !queryCondition.getEleTypenameLv2().toString().equals("")) {
			queryString.append(" and obj.ELE_TYPENAME_LV2=" + queryCondition.getEleTypenameLv2());
		}

		if (queryCondition.getStartTime() != null && !"".equals(queryCondition.getStartTime().toString())) {
			queryString.append(" and obj.ele_Pro_Time >='" + queryCondition.getStartTime().toString() + "'");
		}
		if (queryCondition.getEndTime() != null && !"".equals(queryCondition.getEndTime().toString())) {
			queryString.append(" and obj.ele_Pro_Time <='" + queryCondition.getEndTime() + " 23:59:59'");
		}

		if (queryCondition.getEleGrade() != null && !queryCondition.getEleGrade().trim().equals("")) {
			queryString.append(" and obj.ELE_GRADE like '%" + queryCondition.getEleGrade().trim() + "%'");
		}

		if (queryCondition.getEleDesc() != null && !queryCondition.getEleDesc().equals("")) {
			queryString.append(" and obj.ELE_DESC like '%" + queryCondition.getEleDesc().toString().trim() + "%'");
		}

		if (queryCondition.getEleForPersonFind() != null && !queryCondition.getEleForPersonFind().equals("")) {
			queryString.append(" and obj.ELE_FOR_PERSON like '%" + queryCondition.getEleForPersonFind().toString().trim() + "%'");
		}
		if (queryCondition.getBoxLS() != null && queryCondition.getBoxLE() != null) {
			if (!"".equals(queryCondition.getBoxLS().trim()) && !"".equals(queryCondition.getBoxLE().trim())) {
				queryString.append(" and obj.BOX_L between " + queryCondition.getBoxLS() + " and " + queryCondition.getBoxLE());
			}
		}

		if (queryCondition.getBoxWS() != null && queryCondition.getBoxWE() != null) {
			if (!"".equals(queryCondition.getBoxWS().trim()) && !"".equals(queryCondition.getBoxWE().trim())) {
				queryString.append(" and obj.box_W between " + queryCondition.getBoxWS() + " and " + queryCondition.getBoxWE());
			}
		}

		if (queryCondition.getBoxHS() != null && queryCondition.getBoxHE() != null) {
			if (!"".equals(queryCondition.getBoxHS().trim()) && !"".equals(queryCondition.getBoxHE().trim())) {
				queryString.append(" and obj.box_H between " + queryCondition.getBoxHS() + " and " + queryCondition.getBoxHE());
			}
		}
		String sql = "SELECT " + "obj.ID as id," + "obj.ELE_ID as eleId," + "obj.ELE_NAME as eleName," + "obj.ELE_NAME_EN as eleNameEn," + "obj.ELE_SIZE_DESC as eleSizeDesc," + "obj.FACTORY_ID as factoryId," + "obj.PIC_PATH as picPath," + "obj.PRICE_OUT as price," + "c.CUR_NAME_EN as currency " + "FROM cot_elements_new obj , cot_currency c ,cot_currency c1";
		queryInfo.setSelectString(sql);
		queryInfo.setQueryString(queryString.toString());
		queryInfo.setOrderString(" ");
		String html = "<div align='center' style='width:100%;margin:10px;'>";
		try {
			queryInfo.setQueryObjType("CotElementsVo");
			List<CotElementsVO> res = this.getElementsDao().findRecordsJDBC(queryInfo);
			for (CotElementsVO eleVo : res) {
				String title = "<font color=red size=3>" + eleVo.getEleId() + "</font> " + eleVo.getEleNameEn() + "(" + eleVo.getEleName() + ")</br>" + eleVo.getEleSizeDesc() + "</br>" + eleVo.getBoxObL() + "*" + eleVo.getBoxObW() + "*" + eleVo.getBoxObH() + "</br>" + "price:<font color=red size=4.5>" + Float.parseFloat(eleVo.getPrice()) + "</font>" + "(" + eleVo.getCurrenty() + ")";
				// title = "";
				html += "<div style='width:15%; height:200px;float:left; margin:5px;border:1 solid blue '>";
				// html += "<a style='width:100%;'
				// href='/CotSystem/showPicture.action?flag=ele&elementId=" +
				// eleVo.getId() + "&height=" + imgHeigth + "&width=" + imgWidth
				// + "' title='" + title + "' class='thickbox'
				// rel='gallery-plants'>";
				// html += "<img
				// src='/CotSystem/showPicture.action?flag=ele&elementId=" +
				// eleVo.getId() + "' height='150px' width='150px' alt='" +
				// eleVo.getEleName() + "'
				// onload='javascript:DrawImage(this,150,150);'/>";
				html += "<a style='width:100%;' href='showPicture.action?flag=ele&elementId=" + eleVo.getId() + "&height=" + imgHeigth + "&width=" + imgWidth + "' title='" + title + "'  class='thickbox' rel='gallery-plants'>";
				html += "<img src='showPicture.action?flag=ele&elementId=" + eleVo.getId() + "' height='150px' width='150px' alt='" + eleVo.getEleName() + "' onload='javascript:DrawImage(this,150,150);'/>";
				html += "</a>";
				html += "<a href='#' style='width:100%;'><label onclick=\"getDetail(" + eleVo.getId() + ")\" style='height:20px;margin-top:5px;cursor:hand'><font size=2px>" + eleVo.getEleId() + "</font></label></a>";
				html += "</div>";
			}
			html += "</div>";
			// System.out.println(html);
		} catch (DAOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return html;
	}

	// 得到总记录数
	public Integer getRecordCount(QueryInfo queryInfo) {
		try {
			return this.getElementsDao().getRecordsCount(queryInfo);
		} catch (DAOException e) {
			e.printStackTrace();
			logger.error("查询出错");
		}
		return 0;
	}

	// 得到总记录数
	public Integer getRecordCountJDBC(QueryInfo queryInfo) {
		return this.getElementsDao().getRecordsCountJDBC(queryInfo);
	}

	// 得到objName的集合
	public List<?> getList(String objName) {
		return this.getElementsDao().getRecords(objName);
	}

	// 得到样品总记录数
	public Integer getCount() {
		String sql = "select count(*) from CotElementsNew obj";
		List<?> list = this.getElementsDao().find(sql);
		return (Integer) list.get(0);
	}

	// 根据条件查询主报价单记录
	public List<?> getList(QueryInfo queryInfo) {
		try {
			return this.getElementsDao().findRecords(queryInfo);
		} catch (DAOException e) {
			e.printStackTrace();
			logger.error("查询出错");
		}
		return null;
	}

	// 判断产品货号是否存在
	public boolean findIsExistEleId(String eleId, Integer eId) {
		Integer id = this.getElementsDao().isExistEleId(eleId);
		if (id != null && !id.toString().equals(eId.toString())) {
			return true;
		} else {
			return false;
		}
	}

	public CotExportRptDao getRptDao() {
		return rptDao;
	}

	public void setRptDao(CotExportRptDao rptDao) {
		this.rptDao = rptDao;
	}

	public List<?> getDefaultList() {
		return this.getElementsDao().getRecords("CotEleCfg");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sail.cot.service.sample.CotElementsService#findEleCount(com.sail.cot.domain.vo.CotElementsVO)
	 */
	public int findEleCount(CotElementsVO queryCondition) {
		QueryInfo queryInfo = new QueryInfo();

		StringBuffer queryString = new StringBuffer();
		queryString.append(" where 1=1 ");
		if (queryCondition.getEleId() != null && !queryCondition.getEleId().trim().equals("")) {
			queryString.append(" and obj.eleId like '%" + queryCondition.getEleId().trim() + "%'");
		}
		if (queryCondition.getEleName() != null && !queryCondition.getEleName().trim().equals("")) {
			queryString.append(" and obj.eleName like '%" + queryCondition.getEleName().trim() + "%'");
		}
		if (queryCondition.getFactoryId() != null && !queryCondition.getFactoryId().equals("")) {
			queryString.append(" and obj.factoryId=" + queryCondition.getFactoryId());
		}
		if (queryCondition.getEleCol() != null && !queryCondition.getEleCol().trim().equals("")) {
			queryString.append(" and obj.eleCol like '%" + queryCondition.getEleCol().trim() + "%'");
		}
		if (queryCondition.getEleTypeidLv1() != null && !queryCondition.getEleTypeidLv1().toString().equals("")) {
			queryString.append(" and obj.eleTypeidLv1=" + queryCondition.getEleTypeidLv1());
		}
		if (queryCondition.getStartTime() != null && !"".equals(queryCondition.getStartTime().toString())) {
			queryString.append(" and obj.eleProTime >='" + queryCondition.getStartTime().toString() + "'");
		}
		if (queryCondition.getEndTime() != null && !"".equals(queryCondition.getEndTime().toString())) {
			queryString.append(" and obj.eleProTime <='" + queryCondition.getEndTime() + " 23:59:59'");
		}

		if (queryCondition.getEleGrade() != null && !queryCondition.getEleGrade().trim().equals("")) {
			queryString.append(" and obj.eleGrade like '%" + queryCondition.getEleGrade().trim() + "%'");
		}

		if (queryCondition.getEleForPersonFind() != null && !queryCondition.getEleForPersonFind().equals("")) {
			queryString.append(" and obj.eleForPerson like '%" + queryCondition.getEleForPersonFind().toString().trim() + "%'");
		}
		if (queryCondition.getBoxLS() != null && queryCondition.getBoxLE() != null) {
			if (!"".equals(queryCondition.getBoxLS().trim()) && !"".equals(queryCondition.getBoxLE().trim())) {
				queryString.append(" and obj.boxL between " + queryCondition.getBoxLS() + " and " + queryCondition.getBoxLE());
			}
		}

		if (queryCondition.getBoxWS() != null && queryCondition.getBoxWE() != null) {
			if (!"".equals(queryCondition.getBoxWS().trim()) && !"".equals(queryCondition.getBoxWE().trim())) {
				queryString.append(" and obj.boxW between " + queryCondition.getBoxWS() + " and " + queryCondition.getBoxWE());
			}
		}

		if (queryCondition.getBoxHS() != null && queryCondition.getBoxHE() != null) {
			if (!"".equals(queryCondition.getBoxHS().trim()) && !"".equals(queryCondition.getBoxHE().trim())) {
				queryString.append(" and obj.boxH between " + queryCondition.getBoxHS() + " and " + queryCondition.getBoxHE());
			}
		}
		String sql = "SELECT Count(*) " +

		"FROM CotElementsNew obj ";
		queryInfo.setCountQuery(sql + queryString.toString());
		// queryInfo.setQueryString(queryString.toString());

		int res = 0;
		try {
			res = this.getElementsDao().getRecordsCount(queryInfo);
		} catch (DAOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return res;
	}

	// 根据货号取得样品对象
	@SuppressWarnings("unchecked")
	public CotElementsNew getElementsByEleId(String eleId) {
		CotElementsNew cotElementsNew = new CotElementsNew();
		List<CotElementsNew> list = this.getElementsDao().find("from CotElementsNew obj where obj.eleId='" + eleId + "'");
		if (list.size() > 0) {
			cotElementsNew = list.get(0);
			return cotElementsNew;
		}
		return null;
	}

	// 根据货号取得样品对象
	@SuppressWarnings("unchecked")
	public CotElementsNew getEleByEleId(String eleId) {
		CotElementsNew cotElementsNew = new CotElementsNew();
		List<CotElementsNew> list = this.getElementsDao().find("from CotElementsNew obj where obj.eleId='" + eleId + "'");
		if (list.size() > 0) {
			cotElementsNew = list.get(0);
			cotElementsNew.setPicImg(null);
			cotElementsNew.setCotFile(null);
			cotElementsNew.setCotPictures(null);
			cotElementsNew.setChilds(null);
			cotElementsNew.setCotPriceFacs(null);
			cotElementsNew.setCotEleFittings(null);
			cotElementsNew.setCotElePrice(null);
			cotElementsNew.setCotElePacking(null);
			return cotElementsNew;
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sail.cot.service.sample.CotElementsService#deleteEleCustByCodition(java.lang.String,
	 *      java.lang.String, java.lang.Integer)
	 */
	public void deleteEleCustByCodition(String EleId, String type, Integer typePrimId) {
		this.getElementsDao().deleteEleCustByCodition(EleId, type, typePrimId);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sail.cot.service.sample.CotElementsService#deleteEleCustNoByTypeAndDetailId(java.lang.String,
	 *      java.lang.Integer)
	 */
	public void deleteEleCustNoByTypeAndDetailId(String type, Integer typeDetail) {
		this.getElementsDao().deleteEleCustNoByTypeAndDetailId(type, typeDetail);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sail.cot.service.sample.CotElementsService#deleteEleCustNoByTypeAndPrimId(java.lang.String,
	 *      java.lang.Integer)
	 */
	public void deleteEleCustNoByTypeAndPrimId(String type, Integer typePrimId) {
		this.getElementsDao().deleteEleCustNoByTypeAndPrimId(type, typePrimId);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sail.cot.service.sample.CotElementsService#getCustNoListByEleId(java.lang.String)
	 */
	public List<CotEleIdCustNo> getCustNoListByEleId(String EleId) {
		// TODO Auto-generated method stub
		return this.getElementsDao().getCustNoListByEleId(EleId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sail.cot.service.sample.CotElementsService#saveEleCustNo(java.lang.String,
	 *      java.lang.String, java.lang.String, java.lang.Integer,
	 *      java.lang.Integer)
	 */
	public void saveEleCustNo(String EleId, String CustNo, String type, Integer typePrimId, Integer typeDetailId, Integer custId, String eleNameEn) {
		// TODO Auto-generated method stub
		this.getElementsDao().saveEleCustNo(EleId, CustNo, type, typePrimId, typeDetailId, custId, eleNameEn);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sail.cot.service.sample.CotElementsService#getCustNoListByEleId(java.util.List)
	 */
	public Map getCustNoListMapByEleId(String EleIds, Integer custId) {
		// TODO Auto-generated method stub
		return this.getElementsDao().getCustNoListMapByEleId(EleIds, custId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sail.cot.service.sample.CotElementsService#getCustNoByEleId(java.lang.String)
	 */
	public String getCustNoByEleId(String EleId, Integer custId) {
		// TODO Auto-generated method stub
		return this.getElementsDao().getCustNoByEleId(EleId, custId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sail.cot.service.sample.CotElementsService#saveEleCustNoByCustList(java.util.Map,
	 *      java.lang.String)
	 */
	public void saveEleCustNoByCustList(Map elecustMap, Map elenameenMap, Integer custId, String type) {
		this.getElementsDao().saveEleCustNoByCustList(elecustMap, elenameenMap, custId, type);
	}

	// 根据图片编号获得图片
	public CotPicture getPicById(Integer pictureId) {
		return (CotPicture) this.getElementsDao().getById(CotPicture.class, pictureId);
	}

	// 更改默认图片
	public void updateDefaultPic(String filePath, Integer eId) throws Exception {
		CotOpImgService impOpService = (CotOpImgService) SystemUtil.getService("CotOpImgService");
		CotElePic cotElePic = impOpService.getElePicImgByEleId(eId);
		File picFile = new File(filePath);
		FileInputStream in;
		in = new FileInputStream(picFile);
		byte[] b = new byte[(int) picFile.length()];
		while (in.read(b) != -1) {
		}
		in.close();
		cotElePic.setPicImg(b);
		if (filePath.indexOf("common/images/zwtp.png") < 0) {
			picFile.delete();
		}
		List<CotElePic> imgList = new ArrayList<CotElePic>();
		imgList.add(cotElePic);
		impOpService.modifyImg(imgList);
	}

	// 上传其他图片
	public void saveOtherPic(String filePath, Integer eId) throws Exception {
		CotPicture picOther = new CotPicture();
		CotElementsNew elements = (CotElementsNew) this.getElementsDao().getById(CotElementsNew.class, eId);
		Integer s = filePath.lastIndexOf('\\');
		String picName = filePath.substring(s + 1, filePath.length());

		File picFile = new File(filePath);
		FileInputStream in;
		in = new FileInputStream(picFile);
		byte[] b = new byte[(int) picFile.length()];
		while (in.read(b) != -1) {
		}
		in.close();
		picOther.setPicImg(b);
		// picOther.setPicName(picName);
		picOther.setPicName(picName.substring(0, picName.length() - 4));
		picOther.setEleId(elements.getId());
		if (filePath.indexOf("common/images/zwtp.png") < 0) {
			picFile.delete();
		}
		this.getElementsDao().create(picOther);
	}

	// 更新其他图片的名称
	public void updateOtherPic(List list) {
		this.getElementsDao().updateRecords(list);
	}

	// 获得暂无图片的图片字节
	public byte[] getZwtpPic() {
		// 获得tomcat路径
		String classPath = CotElementsServiceImpl.class.getResource("/").toString();
		String systemPath = classPath.substring(6, classPath.length() - 16);
		File picFile = new File(systemPath + "common/images/zwtp.png");
		FileInputStream in;
		try {
			in = new FileInputStream(picFile);
			byte[] b = new byte[((int) picFile.length())];
			while (in.read(b) != -1) {
			}
			in.close();
			return b;
		} catch (Exception e1) {
			logger.error("设置样品图片错误!");
			return null;
		}
	}

	// 获得没有权限图片的图片字节
	public byte[] getNoPicSel() {
		// 获得tomcat路径
		String classPath = CotElementsServiceImpl.class.getResource("/").toString();
		String systemPath = classPath.substring(6, classPath.length() - 16);
		File picFile = new File(systemPath + "common/images/noElePicSel.png");
		FileInputStream in;
		try {
			in = new FileInputStream(picFile);
			byte[] b = new byte[((int) picFile.length())];
			while (in.read(b) != -1) {
			}
			in.close();
			return b;
		} catch (Exception e1) {
			logger.error("设置样品图片错误!");
			return null;
		}
	}

	// 获取注册软件版本
	public String getSoftVer() {
		// 取得mac地址
		SystemInfoUtil systemInfoUtil = new SystemInfoUtil();
		String mac = systemInfoUtil.getVolumSeri();
		// 查询注册表的信息
		List<?> slist = this.getElementsDao().find("from CotServerInfo obj where obj.mechineKey='" + mac.substring(4, mac.length()).trim() + "'");
		if (slist.size() == 1) {
			CotServerInfo cotServerInfo = (CotServerInfo) slist.get(0);
			String softVer = cotServerInfo.getSoftVer();
			return softVer;
		}
		return null;
	}

	// 获取样品其他图片的HTML显示，用图片演示用
	@SuppressWarnings("unchecked")
	public String getOtherPicHtml(int currentPage, int countOnEachPage, CotPicture queryCondition, int imgHeigth, int imgWidth) {
		QueryInfo queryInfo = new QueryInfo();
		queryInfo.setStartIndex(currentPage - 1);
		queryInfo.setCountOnEachPage(countOnEachPage);

		StringBuffer queryString = new StringBuffer();
		queryString.append(" where 1=1");
		if (queryCondition.getEleId() != null && !queryCondition.getEleId().equals("")) {
			queryString.append(" and obj.eleId = " + queryCondition.getEleId());
		}

		String sql = "FROM CotPicture obj";
		queryInfo.setSelectString(sql);
		queryInfo.setQueryString(queryString.toString());
		queryInfo.setOrderString(" order by obj.id desc");
		String html = "<div align='center' style='width:100%;margin:10px;'>";
		queryInfo.setQueryObjType("CotPicture");
		try {
			List<CotPicture> res = this.getElementsDao().findRecords(queryInfo);
			for (CotPicture cotPicture : res) {
				String title = cotPicture.getPicName() + "</br>";

				html += "<div style='width:15%;float:left; margin:5px;'>";
				// html += "<a style='width:100%;'
				// href='/CotSystem/showPicture.action?flag=pic&picId=" +
				// cotPicture.getId() + "&height=" + imgHeigth + "&width=" +
				// imgWidth + "' title='" + title + "' class='thickbox'
				// rel='gallery-plants'>";
				// html += "<img
				// src='/CotSystem/showPicture.action?flag=pic&picId=" +
				// cotPicture.getId() + "' height='150px' width='150px' alt='" +
				// cotPicture.getPicName() + "'
				// onload='javascript:DrawImage(this,150,150);'/>";
				html += "<a style='width:100%;' href='showPicture.action?flag=pic&picId=" + cotPicture.getId() + "&height=" + imgHeigth + "&width=" + imgWidth + "' title='" + title + "'  class='thickbox' rel='gallery-plants'>";
				html += "<img src='showPicture.action?flag=pic&picId=" + cotPicture.getId() + "' height='150px' width='150px' alt='" + cotPicture.getPicName() + "' onload='javascript:DrawImage(this,150,150);'/>";
				html += "</a>";
				// html += "<a href='#' style='width:100%;'><label onclick=\"\"
				// style='height:20px;margin-top:5px;cursor:hand'><font
				// size=2px>" + cotPicture.getPicName() + "</font></label><label
				// onclick=\"delPic(" + cotPicture.getId() + ")\"
				// style='margin-left:2px;cursor:hand'><img
				// src='/CotSystem/common/images/_del-x_.gif' height='18px'
				// width='18px' border='0' alt='删除'></label></a>";
				html += "<a href='#' style='width:100%;'><label onclick=\"\" style='height:20px;margin-top:5px;cursor:hand'><font size=2px>" + cotPicture.getPicName() + "</font></label><label onclick=\"delPic(" + cotPicture.getId() + ")\" style='margin-left:2px;cursor:hand'><img  src='common/images/_del-x_.gif' height='18px' width='18px' border='0' alt='删除'></label></a>";
				html += "</div>";
			}
			html += "</div>";
			System.out.println(html);
		} catch (DAOException e) {

			e.printStackTrace();
		}
		return html;
	}

	// 获取样品其他图片查询的记录数,图片演示用
	public int findOtherPicCount(CotPicture queryCondition) {
		QueryInfo queryInfo = new QueryInfo();

		StringBuffer queryString = new StringBuffer();
		queryString.append(" where 1=1");
		if (queryCondition.getEleId() != null && !queryCondition.getEleId().equals("")) {
			queryString.append(" and obj.eleId = " + queryCondition.getEleId());
		}

		String sql = "SELECT Count(*) " +

		" FROM CotPicture obj";
		queryInfo.setCountQuery(sql + queryString.toString());

		int res = 0;
		try {
			res = this.getElementsDao().getRecordsCount(queryInfo);
		} catch (DAOException e) {

			e.printStackTrace();
		}
		return res;
	}

	// 获取查询客号的记录
	@SuppressWarnings("unchecked")
	public List getCotEleIdCustNoList(int currentPage, int countOnEachPage, CotEleIdCustNo queryCondition) {
		QueryInfo queryInfo = new QueryInfo();
		queryInfo.setStartIndex(currentPage);
		queryInfo.setCountOnEachPage(countOnEachPage);

		StringBuffer queryString = new StringBuffer();

		queryString.append(" where 1=1 and obj.cust_id = c.id");
		if (queryCondition.getEleId() != null && !queryCondition.getEleId().equals("")) {
			queryString.append(" and obj.ELE_ID = '" + queryCondition.getEleId() + "'");
		}
		if (queryCondition.getCustId() != null && !queryCondition.getCustId().equals("")) {
			queryString.append(" and obj.CUST_ID = " + queryCondition.getCustId());
		}

		String sql = "SELECT " + "obj.CUST_NO as custNo," + "obj.ELE_ID as eleId," + "obj.cust_id as custId," + "obj.ele_name_en as eleNameEn,c.customer_short_name as custName" +

		" FROM cot_ele_cust obj,cot_customer c  ";
		queryInfo.setSelectString(sql);
		queryInfo.setQueryString(queryString.toString());
		queryInfo.setOrderString(" order by obj.ID desc");

		queryInfo.setQueryObjType("CotEleIdCustNo");
		try {
			List<CotEleIdCustNo> eleIdCustNoList = this.getElementsDao().findRecordsJDBC(queryInfo);
			return eleIdCustNoList;

		} catch (DAOException e) {
			e.printStackTrace();
			return null;
		}

	}

	// 获取查询客号的记录数
	public int findCotEleIdCustNoCount(CotEleIdCustNo queryCondition) {
		QueryInfo queryInfo = new QueryInfo();

		StringBuffer queryString = new StringBuffer();

		queryString.append(" where 1=1");
		if (queryCondition.getEleId() != null && !queryCondition.getEleId().equals("")) {
			queryString.append(" and obj.ELE_ID = '" + queryCondition.getEleId() + "'");
		}
		if (queryCondition.getCustId() != null && !queryCondition.getCustId().equals("")) {
			queryString.append(" and obj.CUST_ID = " + queryCondition.getCustId());
		}

		String sql = "SELECT Count(*) " +

		// "obj.CUST_NO as custNo,"+
				// "obj.ELE_ID as eleId,"+
				// "obj.cust_id as custId,"+
				// "obj.ele_name_en as eleNameEn"+

				" FROM cot_ele_cust obj ";

		queryInfo.setCountQuery(sql + queryString.toString());

		int res = 0;
		try {
			res = this.getElementsDao().getRecordsCountJDBC(queryInfo);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return res;
	}

	// 根据条件查询出样品的货号和编号的map
	public List<?> findByParms(Map<String, String> map) {
		Object eleId = map.get("eleIdFind");// 样品编号
		Object child = map.get("childFind");// 子货号标识
		Object eleName = map.get("eleNameFind");// 中文名
		Object eleNameEn = map.get("eleNameEnFind");// 英文名
		Object factoryId = map.get("factoryIdFind");// 厂家
		Object eleCol = map.get("eleColFind");// 颜色
		Object startTime = map.get("startTime");// 起始时间
		Object endTime = map.get("endTime");// 结束时间
		Object eleTypeidLv1 = map.get("eleTypeidLv1Find");// 大类
		Object eleTypeidLv2 = map.get("eleTypeidLv2");// 中类
		Object eleTypeidLv3 = map.get("eleTypeidLv3");// 小类
		Object eleGrade = map.get("eleGradeFind");// 等级
		Object eleForPerson = map.get("eleForPersonFind");// 开发对象
		Object eleHsid = map.get("eleHsid");// 海关编码

		Object boxLS = map.get("boxLS");// 产品起始长
		Object boxLE = map.get("boxLE");// 产品结束长
		Object boxWS = map.get("boxWS");// 产品起始宽
		Object boxWE = map.get("boxWE");// 产品结束宽
		Object boxHS = map.get("boxHS");// 产品起始高
		Object boxHE = map.get("boxHE");// 产品结束高

		StringBuffer queryString = new StringBuffer();
		queryString.append(" where 1=1");
		if (child != null && child.toString().trim().equals("true")) {
			queryString.append(" and ele.eleFlag=2");
		} else {
			queryString.append(" and (ele.eleFlag is null or ele.eleFlag!=2)");
		}
		if (eleId != null && !eleId.toString().trim().equals("")) {
			queryString.append(" and ele.eleId like '%" + eleId.toString().trim() + "%'");
		}
		if (eleName != null && !eleName.toString().trim().equals("")) {
			queryString.append(" and ele.eleName like '%" + eleName.toString().trim() + "%'");
		}
		if (eleNameEn != null && !eleNameEn.toString().trim().equals("")) {
			queryString.append(" and ele.eleNameEn like '%" + eleNameEn.toString().trim() + "%'");
		}
		if (factoryId != null && !factoryId.toString().equals("")) {
			queryString.append(" and ele.factoryId=" + factoryId.toString());
		}
		if (eleCol != null && !eleCol.toString().trim().equals("")) {
			queryString.append(" and ele.eleCol like '%" + eleCol.toString().trim() + "%'");
		}
		if (eleTypeidLv1 != null && !eleTypeidLv1.toString().equals("")) {
			queryString.append(" and ele.eleTypeidLv1=" + eleTypeidLv1.toString());
		}
		if (eleTypeidLv2 != null && !eleTypeidLv2.toString().equals("")) {
			queryString.append(" and ele.eleTypeidLv2=" + eleTypeidLv2.toString());
		}
		if (eleTypeidLv3 != null && !eleTypeidLv3.toString().equals("")) {
			queryString.append(" and ele.eleTypeidLv3=" + eleTypeidLv3.toString());
		}
		if (eleGrade != null && !eleGrade.toString().equals("")) {
			queryString.append(" and ele.eleGrade like '%" + eleGrade.toString().trim() + "%'");
		}

		if (eleForPerson != null && !eleForPerson.toString().equals("")) {
			queryString.append(" and ele.eleForPerson like '%" + eleForPerson.toString().trim() + "%'");
		}

		if (startTime != null && !"".equals(startTime.toString().trim())) {
			queryString.append(" and ele.eleProTime >='" + startTime.toString().trim() + "'");
		}
		if (endTime != null && !"".equals(endTime.toString().trim())) {
			queryString.append(" and ele.eleProTime <='" + endTime.toString() + " 23:59:59'");
		}
		if (eleHsid != null && !eleHsid.toString().equals("")) {
			queryString.append(" and ele.eleHsid=" + eleHsid.toString());
		}

		if (boxLS != null && boxLE != null) {
			if (!"".equals(boxLS.toString().trim()) && !"".equals(boxLE.toString().trim())) {
				queryString.append(" and ele.boxL between " + boxLS + " and " + boxLE);
			}
		}

		if (boxWS != null && boxWE != null) {
			if (!"".equals(boxWS.toString().trim()) && !"".equals(boxWE.toString().trim())) {
				queryString.append(" and ele.boxW between " + boxWS.toString() + " and " + boxWE.toString());
			}
		}

		if (boxHS != null && boxHE != null) {
			if (!"".equals(boxHS.toString().trim()) && !"".equals(boxHE.toString().trim())) {
				queryString.append(" and ele.boxH between " + boxHS.toString() + " and " + boxHE.toString());
			}
		}
		String sql = "select ele.id,ele.eleId from CotElementsNew ele";
		List<?> list = this.getElementsDao().find(sql + queryString);
		List<CotEleVOForPrint> listSel = new ArrayList<CotEleVOForPrint>();
		for (int i = 0; i < list.size(); i++) {
			Object[] obj = (Object[]) list.get(i);
			CotEleVOForPrint cotEleVOForPrint = new CotEleVOForPrint();
			cotEleVOForPrint.setId((Integer) obj[0]);
			cotEleVOForPrint.setEleId((String) obj[1]);
			listSel.add(cotEleVOForPrint);
		}
		return listSel;
	}

	// 根据查询条件删除样品
	public void deleteEleByCondition(Map<String, String> map) {
		WebContext ctx = WebContextFactory.get();
		Integer empId = (Integer) ctx.getSession().getAttribute("empId");
		List<?> list = this.findByParms(map);
		List<Integer> ids = new ArrayList<Integer>();
		Iterator<?> it = list.iterator();
		while (it.hasNext()) {
			CotEleVOForPrint obj = (CotEleVOForPrint) it.next();
			ids.add(obj.getId());
		}
		try {
			this.getElementsDao().deleteRecordByIds(ids, "CotElementsNew");
			CotSyslog cotSyslog = new CotSyslog();
			cotSyslog.setEmpId(empId);
			cotSyslog.setOpMessage("批量删除样品成功");
			cotSyslog.setOpModule("elements");
			cotSyslog.setOpTime(new Date(System.currentTimeMillis()));
			cotSyslog.setOpType(3);
			sysLogService.addSysLogByObj(cotSyslog);
		} catch (DAOException e) {
			logger.error("删除样品数据失败!");
			CotSyslog cotSyslog = new CotSyslog();
			cotSyslog.setEmpId(empId);
			cotSyslog.setOpMessage("批量删除样品失败,失败原因" + e.getMessage());
			cotSyslog.setOpModule("elements");
			cotSyslog.setOpTime(new Date(System.currentTimeMillis()));
			cotSyslog.setOpType(3);
			sysLogService.addSysLogByObj(cotSyslog);
		}
	}

	// 查询条件删除样品得到样品总记录数
	public Integer getCountByCondition(Map<String, String> map) {
		List<?> list = this.findByParms(map);
		return list.size();
	}

	public CotSysLogService getSysLogService() {
		return sysLogService;
	}

	public void setSysLogService(CotSysLogService sysLogService) {
		this.sysLogService = sysLogService;
	}

	// 通过币种id取得汇率
	public Float getCurRate(Integer id) {
		List<?> res = this.getElementsDao().find(" from CotCurrency obj where obj.id =" + id);
		CotCurrency cotCurrency = (CotCurrency) res.get(0);
		Float curRate = cotCurrency.getCurRate();
		return curRate;
	}

	// 获取样品默认配置中的公式及利润系数
	public CotEleCfg getExpessionAndProfit() {
		List<CotEleCfg> list = new ArrayList<CotEleCfg>();
		String hql = " from CotEleCfg obj ";
		list = this.getElementsDao().find(hql);
		if (list != null && list.size() > 0) {
			CotEleCfg cotEleCfg = (CotEleCfg) list.get(0);
			return cotEleCfg;
		} else {
			return null;
		}
	}

	// 设置外销价值
	public Double getPriceOut(Float priceFac, Float tuiLv, Integer priceFacUint, Integer priceOutUint, Float liRun, Float cbm, Integer boxObCount) {

		if (tuiLv.isNaN() || tuiLv.equals("")) {
			tuiLv = 0.0f;
		}
		if (liRun.isNaN() || liRun.equals("")) {
			liRun = 0.0f;
		}
		//生成价汇率
		Float priceFacRate = this.getCurRate(priceFacUint);
		//外销价汇率
		Float priceOutRate = this.getCurRate(priceOutUint);
		//以美元为基准,重新计算生产价
		Float newPriceFac =priceFac * priceFacRate;
		
		Float rate = 0.0f;
		if (priceFacRate != 0) {
			rate = priceFacRate / priceOutRate;
		}
		CotEleCfg cotEleCfg = (CotEleCfg) this.getExpessionAndProfit();
		// Float priceProfit = cotEleCfg.getPriceProfit();
		if(cotEleCfg==null){
			return null;
		}
		String expessionIn = cotEleCfg.getExpessionIn();
		System.out.println(expessionIn);
		// 定义jeavl对象,用于计算字符串公式
		Evaluator evaluator = new Evaluator();
		evaluator.putVariable("priceProfit", liRun.toString());
		//生成价
		evaluator.putVariable("priceFac", newPriceFac.toString());
		//外销价汇率
		evaluator.putVariable("priceRate", priceOutRate.toString());
		evaluator.putVariable("tuiLv", tuiLv.toString());
		evaluator.putVariable("cbm", cbm.toString());
		evaluator.putVariable("boxObCount", boxObCount.toString());
		
		Double res = null;
		try {
			if (!"".equals(expessionIn)) {
				String result = evaluator.evaluate(expessionIn);
				res = Double.parseDouble(result);
			}
		} catch (EvaluationException e) {
			e.printStackTrace();
			return null;
		}
		return res;
	}

	// 计算生产价
	public Double calPriceFac(Integer id) {

		CotElementsNew elements = (CotElementsNew) this.getElementsDao().getById(CotElementsNew.class, id);
		CotEleCfg cotEleCfg = (CotEleCfg) this.getExpessionAndProfit();
		Float elePrice = 0.0f;
		Float eleFittingPrice = 0.0f;
		Float packingPrice = 0.0f;

		if (elements != null) {
			if (elements.getElePrice() != null && !"".equals(elements.getElePrice())) {
				elePrice = elements.getElePrice();
			}
			if (elements.getEleFittingPrice() != null && !"".equals(elements.getEleFittingPrice())) {
				eleFittingPrice = elements.getEleFittingPrice();
			}
			if (elements.getPackingPrice() != null && !"".equals(elements.getPackingPrice())) {
				packingPrice = elements.getPackingPrice();
			}
		}
		String expessionFacIn = cotEleCfg.getExpessionFacIn();
		if (expessionFacIn == null || expessionFacIn.trim().equals("")) {
			return 0.0;
		}
		// 定义jeavl对象,用于计算字符串公式
		Evaluator evaluator = new Evaluator();
		evaluator.putVariable("elePrice", elePrice.toString());
		evaluator.putVariable("eleFittingPrice", eleFittingPrice.toString());
		evaluator.putVariable("packingPrice", packingPrice.toString());
		Double res = null;
		try {
			String result = evaluator.evaluate(expessionFacIn);
			res = Double.parseDouble(result);
		} catch (EvaluationException e) {
			e.printStackTrace();
		}
		return res;
	}

	// 根据厂家编号获取简称
	public String getFacShortName(Integer id) throws Exception {
		String shortName = "";
		try {
			// List<?> list = systemDicUtil.getDicListByName("factory");
			List<?> list = this.getElementsDao().getRecords("CotFactory");
			for (int i = 0; i < list.size(); i++) {
				CotFactory cotFactory = (CotFactory) list.get(i);
				if (cotFactory.getId().intValue() == id.intValue()) {
					shortName = cotFactory.getShortName();
					break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("根据厂家编号获取简称异常!");
		}
		return shortName;
	}

	// 根据海关编码id获取退税率
	public Float getTaxRate(Integer id) {
		String hql = "select obj.taxRate from CotEleOther obj where obj.id=" + id;
		List<?> list = this.getElementsDao().find(hql);
		if (list.get(0) == null) {
			return null;
		} else {
			return (Float) list.get(0);
		}
	}

	// 获取子货号的所有中英文规格
	@SuppressWarnings("unchecked")
	public List getEleSizeAndInchDesc(Integer parentId) {

		List<CotElementsNew> res = new ArrayList<CotElementsNew>();
		res = this.getElementsDao().find(" from CotElementsNew as obj where obj.eleParentId =" + parentId);
		List<CotElementsNew> eleList = new ArrayList<CotElementsNew>();
		// List<CotElementsNew> list = new ArrayList<CotElementsNew>();
		for (int i = 0; i < res.size(); i++) {
			CotElementsNew cotElementsNew = (CotElementsNew) res.get(i);
			eleList.add(cotElementsNew);
		}
		return eleList;
	}

	// 删除综合查询中样品档案记录
	public void deleteCustNo(Integer id) {
		this.getElementsDao().deleteEleCustNoById(id);
	}

	// 判断货号是否已经添加
	public boolean checkExistFitNo(String fitNo, Integer eleid, String eleNo, String flag) {

		String hql = null;
		if ("child".equals(flag)) {
			hql = " from CotEleFittings obj where obj.eleId =" + eleid + " and obj.fitNo='" + fitNo + "' and obj.eleChild='" + eleNo + "'";
		} else {
			hql = " from CotEleFittings obj where obj.eleId =" + eleid + " and obj.fitNo='" + fitNo + "'";
		}

		List<?> res = this.getElementsDao().find(hql);
		if (res.size() != 0) {
			return true;
		}
		return false;
	}

	// 根据样品货号组装送样明细产品对象
	public CotEleFittings findDetailByFitNo(String fitNo) {
		// 查找样品对象
		String hql = "from CotFittings obj where obj.fitNo = '" + fitNo + "'";
		List<?> list = this.getElementsDao().find(hql);
		// 样品对象不能为空
		if (list.size() == 0) {
			return null;
		}
		CotFittings cotFittings = (CotFittings) list.get(0);

		// 新建送样明细对象
		CotEleFittings cotEleFitting = new CotEleFittings();

		// 1.使用反射获取对象的所有属性名称
		String[] propEle = ReflectionUtils.getDeclaredFields(CotFittings.class);

		ConvertUtilsBean convertUtils = new ConvertUtilsBean();
		SqlDateConverter dateConverter = new SqlDateConverter();
		convertUtils.register(dateConverter, Date.class);
		// 因为要注册converter,所以不能再使用BeanUtils的静态方法了，必须创建BeanUtilsBean实例
		BeanUtilsBean beanUtils = new BeanUtilsBean(convertUtils, new PropertyUtilsBean());
		// beanUtils.setProperty(bean, name, value);

		for (int i = 0; i < propEle.length; i++) {
			try {
				String value = beanUtils.getProperty(cotFittings, propEle[i]);
				if ("buyUnit".equals(propEle[i]) || "useUnit".equals(propEle[i]) || "fitTrans".equals(propEle[i]) || "op".equals(propEle[i]) || "fitMinCount".equals(propEle[i]) || "fitQualityStander".equals(propEle[i]) || "typeLv3Id".equals(propEle[i]) || "addTime".equals(propEle[i])) {
					continue;
				}
				if (value != null) {
					BeanUtils.setProperty(cotEleFitting, propEle[i], BeanUtils.getProperty(cotFittings, propEle[i]));
					cotEleFitting.setId(null);
				}

			} catch (Exception e) {
				logger.error(propEle[i] + ":转换错误!");
			}
		}
		// // 填充厂家简称
		// List<?> facList = getDicList("factory");
		// for (int j = 0; j < facList.size(); j++) {
		// CotFactory cotFactory = (CotFactory) facList.get(j);
		// if (cotGivenDetail.getFactoryId() != null
		// && cotFactory.getId().intValue() ==
		// cotGivenDetail.getFactoryId().intValue()) {
		// cotGivenDetail.setFactoryShortName(cotFactory.getShortName());
		// }
		// }
		return cotEleFitting;
	}

	// 获取fitMap
	@SuppressWarnings("unchecked")
	public TreeMap<String, CotEleFittings> getFitMap() {
		Object obj = SystemUtil.getObjBySession(null, "fit");
		TreeMap<String, CotEleFittings> fitMap = (TreeMap<String, CotEleFittings>) obj;
		return fitMap;
	}

	// 储存fitMap
	@SuppressWarnings("unchecked")
	public void setFitMap(String fitNo, CotEleFittings eleFitting) {
		Object obj = SystemUtil.getObjBySession(null, "fit");
		if (obj == null) {
			TreeMap<String, CotEleFittings> fitMap = new TreeMap<String, CotEleFittings>();
			fitMap.put(fitNo.toLowerCase(), eleFitting);
			SystemUtil.setObjBySession(null, fitMap, "fit");
		} else {
			TreeMap<String, CotEleFittings> fitMap = (TreeMap<String, CotEleFittings>) obj;
			fitMap.put(fitNo.toLowerCase(), eleFitting);
			SystemUtil.setObjBySession(null, fitMap, "fit");
		}

	}

	// Action获取fitMap
	@SuppressWarnings("unchecked")
	public TreeMap<String, CotEleFittings> getFitMapAction(HttpSession session) {
		Object obj = SystemUtil.getObjBySession(session, "fit");
		TreeMap<String, CotEleFittings> fitMap = (TreeMap<String, CotEleFittings>) obj;
		return fitMap;
	}

	// Action储存fitMap
	@SuppressWarnings("unchecked")
	public void setFitMapAction(HttpSession session, String fitNo, CotEleFittings eleFitting) {
		Object obj = SystemUtil.getObjBySession(session, "fit");
		if (obj == null) {
			TreeMap<String, CotEleFittings> fitMap = new TreeMap<String, CotEleFittings>();
			fitMap.put(fitNo.toLowerCase(), eleFitting);
			SystemUtil.setObjBySession(session, fitMap, "fit");
		} else {
			TreeMap<String, CotEleFittings> fitMap = (TreeMap<String, CotEleFittings>) obj;
			fitMap.put(fitNo.toLowerCase(), eleFitting);
			SystemUtil.setObjBySession(session, fitMap, "fit");
		}

	}

	// 清空fitMap
	public void clearFitMap() {
		SystemUtil.clearSessionByType(null, "fit");
	}

	// 清除fitMap中fitNo对应的映射
	public void delFitMapByKey(String fitNo) {
		TreeMap<String, CotEleFittings> fitMap = this.getFitMap();
		if (fitMap != null) {
			if (fitMap.containsKey(fitNo.toLowerCase())) {
				fitMap.remove(fitNo.toLowerCase());
			}
		}
	}

	// 在Action中清除fitMap中fitNo对应的映射
	public void delFitMapByKeyAction(String fitNo, HttpSession session) {
		TreeMap<String, CotEleFittings> fitMap = this.getFitMapAction(session);
		if (fitMap != null) {
			if (fitMap.containsKey(fitNo.toLowerCase())) {
				fitMap.remove(fitNo.toLowerCase());
			}
		}
	}

	// 根据fitid查询配件信息
	public CotFittings getFittingById(Integer fitId) {
		return (CotFittings) this.getElementsDao().getById(CotFittings.class, fitId);
	}

	// 通过key获取Map的value
	public CotEleFittings getFitMapValue(String fitNo) {
		TreeMap<String, CotEleFittings> fitMap = this.getFitMap();
		if (fitMap != null) {
			CotEleFittings eleFittings = (CotEleFittings) fitMap.get(fitNo.toLowerCase());
			return eleFittings;
		}
		return null;
	}

	// 通过单号修改Map中对应的配件明细
	public boolean updateMapValueByFitNo(String fitNo, String property, String value) {
		CotEleFittings eleFittings = getFitMapValue(fitNo.toLowerCase());
		if (eleFittings == null)
			return false;
		try {
			BeanUtils.setProperty(eleFittings, property, value);
			this.setFitMap(fitNo.toLowerCase(), eleFittings);
			return true;
		} catch (Exception ex) {
			ex.printStackTrace();
			return false;
		}
	}

	// 保存配件明细
	public Boolean addEleFitting(List<?> details) {
		try {
			this.getElementsDao().saveRecords(details);
			return true;
		} catch (Exception e) {
			logger.error("保存配件出错!");
			return false;
		}
	}

	// 更新配件明细
	public Boolean modifyEleFitting(List<CotEleFittings> detailList) {
		try {
			this.getElementsDao().updateRecords(detailList);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	// 根据配件明细的ids删除
	public boolean deleteEleFittingByIds(List<Integer> ids) {
		try {
			this.getElementsDao().deleteRecordByIds(ids, "CotEleFittings");
			return true;
		} catch (DAOException e) {
			e.printStackTrace();
			return false;
		}
	}

	// 根据编号查找配件明细单信息
	public CotEleFittings getEleFittingById(Integer detailId) {
		CotEleFittings eleFittings = (CotEleFittings) this.getElementsDao().getById(CotEleFittings.class, detailId);
		if (eleFittings != null) {
			return eleFittings;
		}
		return null;
	}

	// 根据配件号查询配件信息
	public CotFittings getFittingByFitNO(String fitNo) {

		String hql = "from CotFittings obj where obj.fitNo = '" + fitNo + "'";
		List<?> list = this.getElementsDao().find(hql);
		// 样品对象不能为空
		if (list.size() == 0) {
			return null;
		}
		CotFittings cotFittings = (CotFittings) list.get(0);

		return cotFittings;
	}

	// 根据id获取厂家信息
	public CotFactory getFactoryById(Integer id) {
		return (CotFactory) this.getElementsDao().getById(CotFactory.class, id);
	}

	// 根据父货号id及子货号获取配件总成本
	public Float getTotalFitPrice(Integer parentId, String eleNo) {

		// 配件总金额
		Double totalMoney = 0.0;
		String hql = " from CotEleFittings obj where obj.eleId=" + parentId + " and obj.eleChild='" + eleNo + "'";

		List<?> list = this.getElementsDao().find(hql);
		if (list.size() != 0) {
			for (int i = 0; i < list.size(); i++) {
				CotEleFittings fitting = (CotEleFittings) list.get(i);

				if (fitting.getFitTotalPrice() != null && !fitting.getFitTotalPrice().equals("")) {
					totalMoney += fitting.getFitTotalPrice();
				}
			}
		}
		return totalMoney.floatValue();
	}

	// 修改子货号配件成本
	public void modifyChildFitPrice(Integer parentId, String eleNo) {

		Float totalFitMoney = this.getTotalFitPrice(parentId, eleNo);
		List<CotElementsNew> res = new ArrayList<CotElementsNew>();

		String hql = " from CotElementsNew ele where ele.eleParentId=" + parentId + " and ele.eleId='" + eleNo + "'";

		List<?> list = this.getElementsDao().find(hql);
		if (list.size() == 1) {
			CotElementsNew cotElements = (CotElementsNew) list.get(0);
			cotElements.setEleFittingPrice(totalFitMoney);
			cotElements.setCotPictures(null);
			cotElements.setPicImg(null);
			cotElements.setCotFile(null);
			cotElements.setCotPictures(null);
			cotElements.setChilds(null);
			cotElements.setCotPriceFacs(null);
			cotElements.setCotEleFittings(null);
			cotElements.setCotElePrice(null);
			cotElements.setCotElePacking(null);
			res.add(cotElements);
			this.getElementsDao().updateRecords(res);
		}
	}

	// 根据父货号id获取配件总成本
	public Float getParentTotalFitPrice(Integer parentId) {

		// 配件总金额
		Float totalMoney = 0.0f;
		String hql = " from CotEleFittings obj where obj.eleId=" + parentId;

		List<?> list = this.getElementsDao().find(hql);
		if (list.size() != 0) {
			for (int i = 0; i < list.size(); i++) {
				CotEleFittings fitting = (CotEleFittings) list.get(i);

				if (fitting.getFitTotalPrice() != null && !fitting.getFitTotalPrice().equals("")) {
					totalMoney += fitting.getFitTotalPrice().floatValue();
				}
			}
		}
		return totalMoney;
	}

	// 修改父货号配件成本
	public void modifyParentFitPrice(Integer parentId) {

		Float totalFitMoney = this.getParentTotalFitPrice(parentId);
		List<CotElementsNew> res = new ArrayList<CotElementsNew>();

		String hql = " from CotElementsNew ele where ele.id=" + parentId;

		List<?> list = this.getElementsDao().find(hql);
		if (list.size() == 1) {
			CotElementsNew cotElements = (CotElementsNew) list.get(0);
			cotElements.setEleFittingPrice(totalFitMoney);
			cotElements.setCotPictures(null);
			cotElements.setPicImg(null);
			cotElements.setCotFile(null);
			cotElements.setCotPictures(null);
			cotElements.setChilds(null);
			cotElements.setCotPriceFacs(null);
			cotElements.setCotEleFittings(null);
			cotElements.setCotElePrice(null);
			cotElements.setCotElePacking(null);

			res.add(cotElements);
			this.getElementsDao().updateRecords(res);
		}
	}

	// 更改货号生产价
	public void modifyPriceFac(Integer id, Float priceFac) {
		CotElementsNew elements = (CotElementsNew) this.getElementsDao().getById(CotElementsNew.class, id);
		List<CotElementsNew> list = new ArrayList<CotElementsNew>();
		if (elements != null) {
			elements.setPriceFac(priceFac);
			elements.setCotPictures(null);
			elements.setPicImg(null);
			elements.setCotFile(null);
			elements.setCotPictures(null);
			elements.setChilds(null);
			elements.setCotPriceFacs(null);
			elements.setCotEleFittings(null);
			elements.setCotElePrice(null);
			elements.setCotElePacking(null);
			list.add(elements);

			this.getElementsDao().updateRecords(list);
		}
	}

	// 批量修改样品价格
	public void modifyPriceBatch(Integer flag) {
		// 取得样品默认配置对象
		CotEleCfg cotEleCfg = this.getElementsDao().getDefaultEleCfg();
		List<?> list = this.getElementsDao().getRecords("CotElementsNew");
		List<CotElementsNew> newList = new ArrayList<CotElementsNew>();
		for (int i = 0; i < list.size(); i++) {
			// 将成本价格累加到该货号上
			CotElementsNew elementsNew = (CotElementsNew) list.get(i);
			elementsNew.setPicImg(null);
			elementsNew.setCotPictures(null);
			elementsNew.setCotFile(null);
			elementsNew.setChilds(null);
			elementsNew.setCotPriceFacs(null);
			elementsNew.setCotEleFittings(null);
			elementsNew.setCotElePrice(null);
			elementsNew.setCotElePacking(null);

			// 更新生产价
			if (flag == 1 || flag == 3) {
				Float elePrice = elementsNew.getElePrice();
				if (elePrice == null) {
					elePrice = 0f;
				}
				Float eleFitPrice = elementsNew.getEleFittingPrice();
				if (eleFitPrice == null) {
					eleFitPrice = 0f;
				}
				Float packingPrice = elementsNew.getPackingPrice();
				if (packingPrice == null) {
					packingPrice = 0f;
				}
				Evaluator evaluator = new Evaluator();
				evaluator.putVariable("elePrice", elePrice.toString());
				evaluator.putVariable("eleFittingPrice", eleFitPrice.toString());
				evaluator.putVariable("packingPrice", packingPrice.toString());
				try {
					String result = evaluator.evaluate(cotEleCfg.getExpessionFacIn());
					if (elementsNew.getPriceFacUint() != null) {
						CotCurrency cur = (CotCurrency) this.getElementsDao().getById(CotCurrency.class, elementsNew.getPriceFacUint());
						Float fac = Float.parseFloat(result) / cur.getCurRate();
						elementsNew.setPriceFac(fac);

					}
				} catch (EvaluationException e) {
					e.printStackTrace();
				}
			}

			// 更新外销价
			if (flag == 2 || flag == 3) {
				Float lirun = elementsNew.getLiRun();
				if (lirun == null) {
					lirun = 0f;
				}
				Float priceFac = elementsNew.getPriceFac();
				if (priceFac == null) {
					priceFac = 0f;
				}
				Float tuiLv = elementsNew.getTuiLv();
				if (tuiLv == null) {
					tuiLv = 0f;
				}
				Float cbm = elementsNew.getBoxCbm();
				if (cbm == null) {
					cbm = 0f;
				}
				Long boxObCount = elementsNew.getBoxObCount();
				if (boxObCount == null) {
					boxObCount = 0l;
				}
				Evaluator evaluator = new Evaluator();
				Float priceOutRate = this.getCurRate(elementsNew.getPriceOutUint());
				evaluator.putVariable("priceProfit", lirun.toString());
				evaluator.putVariable("priceFac", priceFac.toString());
				evaluator.putVariable("priceRate", priceOutRate.toString());
				evaluator.putVariable("tuiLv", tuiLv.toString());
				evaluator.putVariable("cbm", cbm.toString());
				evaluator.putVariable("boxObCount", boxObCount.toString());
				try {
					String result = evaluator.evaluate(cotEleCfg.getExpessionIn());
					elementsNew.setPriceOut(Float.parseFloat(result));
				} catch (EvaluationException e) {
					e.printStackTrace();
				}
			}
			newList.add(elementsNew);
		}
		this.getElementsDao().updateRecords(newList);
	}

	// 得到objName的集合
	public List<?> getDicList(String objName) {
		return systemDicUtil.getDicListByName(objName);
	}

	// 根据样品编号计算生产价
	public Float[] modifyPriceFacByEleId(Integer id) {
		Float[] temp = new Float[3];
		// 取得样品默认配置对象
		CotEleCfg cotEleCfg = this.getElementsDao().getDefaultEleCfg();
		// 得到内存的币种
		List listCur = this.getDicList("currency");

		CotElementsNew elementsNew = (CotElementsNew) this.getElementsDao().getById(CotElementsNew.class, id);
		elementsNew.setPicImg(null);
		elementsNew.setCotPictures(null);
		elementsNew.setCotFile(null);
		elementsNew.setChilds(null);
		elementsNew.setCotPriceFacs(null);
		elementsNew.setCotEleFittings(null);
		elementsNew.setCotElePrice(null);
		elementsNew.setCotElePacking(null);
		// 计算出所有成本的RMB值
		Double allRmbMoney = 0.0;
		String hql = "from CotEleFittings obj where obj.eleId=" + id;
		List list = this.getElementsDao().find(hql);
		for (int i = 0; i < list.size(); i++) {
			CotEleFittings eleFittings = (CotEleFittings) list.get(i);
			Double mo = eleFittings.getFitTotalPrice();
			allRmbMoney += mo;
		}
		// 存储新的配件总和
		elementsNew.setEleFittingPrice(allRmbMoney.floatValue());
		temp[0] = allRmbMoney.floatValue();
		if (elementsNew.getElePrice() == null) {
			temp[1] = 0f;
		} else {
			temp[1] = elementsNew.getElePrice();
		}
		if (elementsNew.getPriceFac() == null) {
			temp[2] = 0f;
		} else {
			temp[2] = elementsNew.getPriceFac();
		}
		if (cotEleCfg.getExpessionFacIn() != null) {
			String result = null;
			try {
				Evaluator evaluator = new Evaluator();
				// 定义FOB公式的变量
				if (elementsNew.getEleFittingPrice() == null) {
					evaluator.putVariable("eleFittingPrice", "0");
				} else {
					evaluator.putVariable("eleFittingPrice", allRmbMoney.toString());
				}
				if (elementsNew.getElePrice() == null) {
					evaluator.putVariable("elePrice", "0");
				} else {
					evaluator.putVariable("elePrice", elementsNew.getElePrice().toString());
				}
				if (elementsNew.getPackingPrice() == null) {
					evaluator.putVariable("packingPrice", "0");
				} else {
					evaluator.putVariable("packingPrice", elementsNew.getPackingPrice().toString());
				}
				result = evaluator.evaluate(cotEleCfg.getExpessionFacIn());
			} catch (Exception e) {
				e.printStackTrace();
				result = null;
			}
			if(result!=null){
				// 根据公式算出RMB价格,样品必须要有生产币种
				float rate = 0f;
				for (int j = 0; j < listCur.size(); j++) {
					CotCurrency cur = (CotCurrency) listCur.get(j);
					if (cur.getId().intValue() == elementsNew.getPriceFacUint().intValue()) {
						rate = cur.getCurRate();
						break;
					}
				}
				Float fac = Float.parseFloat(result) / rate;
				elementsNew.setPriceFac(fac);
				temp[2] = fac;
			}
		}

		List<CotElementsNew> listEle = new ArrayList<CotElementsNew>();
		listEle.add(elementsNew);
		this.getElementsDao().updateRecords(listEle);
		return temp;

	}

	// 根据编号获取包材计算公式
	public CotBoxPacking getCalculation(Integer boxPackingId) {
		return (CotBoxPacking) this.getElementsDao().getById(CotBoxPacking.class, boxPackingId);
	}

	// 计算价格
	public String calPrice(CotElementsNew elements, Integer boxPackingId) {
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

		// 产品长、宽、高
		if (elements.getBoxL() == null || elements.getBoxL().equals("")) {
			evaluator.putVariable("boxL", "0.0");
		} else {
			evaluator.putVariable("boxL", elements.getBoxL().toString());
		}
		if (elements.getBoxW() == null || elements.getBoxW().equals("")) {
			evaluator.putVariable("boxW", "0.0");
		} else {
			evaluator.putVariable("boxW", elements.getBoxW().toString());
		}
		if (elements.getBoxH() == null || elements.getBoxH().equals("")) {
			evaluator.putVariable("boxH", "0.0");
		} else {
			evaluator.putVariable("boxH", elements.getBoxH().toString());
		}
		// 产品包装长、宽、高
		if (elements.getBoxPbL() == null || elements.getBoxPbL().equals("")) {
			evaluator.putVariable("boxPbL", "0.0");
		} else {
			evaluator.putVariable("boxPbL", elements.getBoxPbL().toString());
		}
		if (elements.getBoxPbW() == null || elements.getBoxPbW().equals("")) {
			evaluator.putVariable("boxPbW", "0.0");
		} else {
			evaluator.putVariable("boxPbW", elements.getBoxPbW().toString());
		}
		if (elements.getBoxPbH() == null || elements.getBoxPbH().equals("")) {
			evaluator.putVariable("boxPbH", "0.0");
		} else {
			evaluator.putVariable("boxPbH", elements.getBoxPbH().toString());
		}
		// 中盒包装长、宽、高
		if (elements.getBoxMbL() == null || elements.getBoxMbL().equals("")) {
			evaluator.putVariable("boxMbL", "0.0");
		} else {
			evaluator.putVariable("boxMbL", elements.getBoxMbL().toString());
		}
		if (elements.getBoxMbW() == null || elements.getBoxMbW().equals("")) {
			evaluator.putVariable("boxMbW", "0.0");
		} else {
			evaluator.putVariable("boxMbW", elements.getBoxMbW().toString());
		}
		if (elements.getBoxMbH() == null || elements.getBoxMbH().equals("")) {
			evaluator.putVariable("boxMbH", "0.0");
		} else {
			evaluator.putVariable("boxMbH", elements.getBoxMbH().toString());
		}
		// 内盒包装长、宽、高
		if (elements.getBoxIbL() == null || elements.getBoxIbL().equals("")) {
			evaluator.putVariable("boxIbL", "0.0");
		} else {
			evaluator.putVariable("boxIbL", elements.getBoxIbL().toString());
		}
		if (elements.getBoxIbW() == null || elements.getBoxIbW().equals("")) {
			evaluator.putVariable("boxIbW", "0.0");
		} else {
			evaluator.putVariable("boxIbW", elements.getBoxIbW().toString());
		}
		if (elements.getBoxIbH() == null || elements.getBoxIbH().equals("")) {
			evaluator.putVariable("boxIbH", "0.0");
		} else {
			evaluator.putVariable("boxIbH", elements.getBoxIbH().toString());
		}
		// 外箱包装长、宽、高
		if (elements.getBoxObL() == null || elements.getBoxObL().equals("")) {
			evaluator.putVariable("boxObL", "0.0");
		} else {
			evaluator.putVariable("boxObL", elements.getBoxObL().toString());
		}
		if (elements.getBoxObW() == null || elements.getBoxObW().equals("")) {
			evaluator.putVariable("boxObW", "0.0");
		} else {
			evaluator.putVariable("boxObW", elements.getBoxObW().toString());
		}
		if (elements.getBoxObH() == null || elements.getBoxObH().equals("")) {
			evaluator.putVariable("boxObH", "0.0");
		} else {
			evaluator.putVariable("boxObH", elements.getBoxObH().toString());
		}
		// 材料单价
		if (boxPacking.getMaterialPrice() == null || boxPacking.getMaterialPrice().equals("")) {
			evaluator.putVariable("materialPrice", "0.0");
		} else {
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
			return "0.0";
		}
	}

	// 计算价格
	public Float[] calPriceAll(CotElementsNew elements) {
		Float[] res = new Float[7];
		DecimalFormat dfTwo = new DecimalFormat("#.00");
		Float pb = 0f;
		if (elements.getBoxPbTypeId() != null && elements.getBoxPbTypeId() != 0) {
			String cal = this.calPrice(elements, elements.getBoxPbTypeId());
			pb = Float.parseFloat(dfTwo.format(Float.parseFloat(cal)));
		}
		Float ib = 0f;
		if (elements.getBoxIbTypeId() != null && elements.getBoxIbTypeId() != 0) {
			String cal = this.calPrice(elements, elements.getBoxIbTypeId());
			ib = Float.parseFloat(dfTwo.format(Float.parseFloat(cal)));
		}
		Float mb = 0f;
		if (elements.getBoxMbTypeId() != null && elements.getBoxMbTypeId() != 0) {
			String cal = this.calPrice(elements, elements.getBoxMbTypeId());
			mb = Float.parseFloat(dfTwo.format(Float.parseFloat(cal)));
		}
		Float ob = 0f;
		if (elements.getBoxObTypeId() != null && elements.getBoxObTypeId() != 0) {
			String cal = this.calPrice(elements, elements.getBoxObTypeId());
			ob = Float.parseFloat(dfTwo.format(Float.parseFloat(cal)));
		}
		Float ig = 0f;
		if (elements.getInputGridTypeId() != null && elements.getInputGridTypeId() != 0) {
			String cal = this.calPrice(elements, elements.getInputGridTypeId());
			ig = Float.parseFloat(dfTwo.format(Float.parseFloat(cal)));
		}
		res[0] = pb;
		res[1] = ib;
		res[2] = mb;
		res[3] = ob;
		res[6] = ig;
		// 计算单个价格
		if (elements.getBoxObCount() == null || elements.getBoxObCount() == 0) {
			res[4] = 0f;
		} else {
			Float pRes = 0f;
			Float iRes = 0f;
			Float mRes = 0f;
			if (elements.getBoxPbCount() != null && elements.getBoxPbCount() != 0) {
				pRes = pb * (elements.getBoxObCount().floatValue() / elements.getBoxPbCount());
			}
			if (elements.getBoxIbCount() != null && elements.getBoxIbCount() != 0) {
				iRes = ib * (elements.getBoxObCount().floatValue() / elements.getBoxIbCount());
			}
			if (elements.getBoxMbCount() != null && elements.getBoxMbCount() != 0) {
				mRes = mb * (elements.getBoxObCount().floatValue() / elements.getBoxMbCount());
			}
			Float all = (pRes + iRes + mRes + ob + ig) / elements.getBoxObCount();
			res[4] = all;
		}
		// 计算生产价
		res[5] = 0f;
		Float elePrice = 0.0f;
		Float eleFittingPrice = 0.0f;
		if (elements.getId() != null) {
			String hql = "select obj.elePrice,obj.eleFittingPrice from CotElementsNew obj where obj.id=" + elements.getId();
			List list = this.getElementsDao().find(hql);
			if (list != null && list.size() > 0) {
				Object[] obj = (Object[]) list.get(0);
				if (obj[0] != null) {
					elePrice = (Float) obj[0];
				}
				if (obj[1] != null) {
					eleFittingPrice = (Float) obj[1];
				}
			}
		}
		CotEleCfg cotEleCfg = (CotEleCfg) this.getExpessionAndProfit();
		if (cotEleCfg != null) {
			String expessionFacIn = cotEleCfg.getExpessionFacIn();
			// 定义jeavl对象,用于计算字符串公式
			Evaluator evaluator = new Evaluator();
			evaluator.putVariable("elePrice", elePrice.toString());
			evaluator.putVariable("eleFittingPrice", eleFittingPrice.toString());
			evaluator.putVariable("packingPrice", res[4].toString());
			try {
				if (expessionFacIn == null || expessionFacIn.trim().equals("")) {
					res[5] = -1f;
				} else {
					String result = evaluator.evaluate(expessionFacIn);
					res[5] = Float.parseFloat(result);
				}
			} catch (EvaluationException e) {
				e.printStackTrace();
				res[5] = -1f;
			}
		} else {
			res[5] = -1f;
		}
		return res;
	}

	// 保存包装方案｛参数中数组的顺序对应外、中、内、产品｝
	public void saveOrUpdateElePacking(Integer eleId, CotElementsNew element, String[] priceAry, String[] oldIdAry) {

		if (element.getBoxObTypeId() != null && !element.getBoxObTypeId().equals("")) {
			this.saveOrUpdatePacking(eleId, element.getBoxObTypeId(), priceAry[0], element.getBoxObCount(), oldIdAry[0]);
		}
		if (element.getBoxMbTypeId() != null && !element.getBoxMbTypeId().equals("")) {
			this.saveOrUpdatePacking(eleId, element.getBoxMbTypeId(), priceAry[1], element.getBoxMbCount(), oldIdAry[1]);
		}
		if (element.getBoxIbTypeId() != null && !element.getBoxIbTypeId().equals("")) {
			this.saveOrUpdatePacking(eleId, element.getBoxIbTypeId(), priceAry[2], element.getBoxIbCount(), oldIdAry[2]);
		}
		if (element.getBoxPbTypeId() != null && !element.getBoxPbTypeId().equals("")) {
			this.saveOrUpdatePacking(eleId, element.getBoxPbTypeId(), priceAry[3], element.getBoxPbCount(), oldIdAry[3]);
		}
	}

	// 保存每种包装成本
	public void saveOrUpdatePacking(Integer eleId, Integer boxPackingId, String price, Long count, String oldId) {

		List<CotElePacking> list = new ArrayList<CotElePacking>();
		CotBoxPacking boxPacking = this.getCalculation(boxPackingId);
		CotElePacking elePacking = new CotElePacking();
		Integer packingId = 0;

		// 如果oldId不为0，则为修改页面
		if (Integer.parseInt(oldId) != 0) {
			// 如果oldId与boxPackid不等，则
			if (boxPackingId != Integer.parseInt(oldId)) {
				packingId = Integer.parseInt(oldId);
			} else {
				packingId = boxPackingId;
			}
			String hql = " from CotElePacking obj where obj.eleId=" + eleId + " and obj.packingId=" + packingId;
			List<?> resList = this.getElementsDao().find(hql);
			if (resList.size() == 1) {
				elePacking = (CotElePacking) resList.get(0);
				elePacking.setId(elePacking.getId());
			}
		}

		elePacking.setEleId(eleId);
		elePacking.setFactoryId(boxPacking.getFacId());
		elePacking.setPackingId(boxPackingId);
		elePacking.setPackingUnit(boxPacking.getUnit());
		elePacking.setPackingName(boxPacking.getValue());
		elePacking.setPackingNameEn(boxPacking.getValueEn());
		elePacking.setPackingPrice(boxPacking.getMaterialPrice());
		elePacking.setPackingTypePrice(Double.parseDouble(price));
		elePacking.setPackingType(boxPacking.getType());
		elePacking.setPackingCount(count.intValue());

		list.add(elePacking);

		this.getElementsDao().saveOrUpdateRecords(list);

	}

	// 根据包材价格调整生产价
	public Double calPriceFacByPackPrice(Integer id, String packingPrice) {

		CotElementsNew elements = (CotElementsNew) this.getElementsDao().getById(CotElementsNew.class, id);
		CotEleCfg cotEleCfg = (CotEleCfg) this.getExpessionAndProfit();
		Float elePrice = 0.0f;
		Float eleFittingPrice = 0.0f;

		if (elements != null) {
			if (elements.getElePrice() != null && !"".equals(elements.getElePrice())) {
				elePrice = elements.getElePrice();
			}
			if (elements.getEleFittingPrice() != null && !"".equals(elements.getEleFittingPrice())) {
				eleFittingPrice = elements.getEleFittingPrice();
			}
		}
		String expessionFacIn = cotEleCfg.getExpessionFacIn();
		// 定义jeavl对象,用于计算字符串公式
		Evaluator evaluator = new Evaluator();
		evaluator.putVariable("elePrice", elePrice.toString());
		evaluator.putVariable("eleFittingPrice", eleFittingPrice.toString());
		evaluator.putVariable("packingPrice", packingPrice.toString());
		Double res = null;
		try {
			if (expessionFacIn == null || expessionFacIn.trim().equals("")) {
				return 0.0;
			}
			String result = evaluator.evaluate(expessionFacIn);
			res = Double.parseDouble(result);
		} catch (EvaluationException e) {
			e.printStackTrace();
		}
		return res;
	}

	// 批量修改包材成本
	public void updatePackPrice(String ids) {

		Float iPrice = 0.0f;
		Float mPrice = 0.0f;
		Float oPrice = 0.0f;
		Float pPrice = 0.0f;
		Float packPrice = 0.0f;

		List<CotElementsNew> resList = new ArrayList<CotElementsNew>();

		String[] idAry = ids.split(",");
		for (int i = 0; i < idAry.length; i++) {
			String hql = " from CotElementsNew obj where obj.id =" + idAry[i];
			List<?> list = this.getElementsDao().find(hql);
			CotElementsNew elements = new CotElementsNew();
			if (list.size() == 1) {
				elements = (CotElementsNew) list.get(0);
				if (elements.getBoxIbTypeId() != null && !"".equals(elements.getBoxIbTypeId())) {
					String ibPrice = this.calPrice(elements, elements.getBoxIbTypeId());
					if (elements.getBoxIbCount() > 0) {
						iPrice = Float.parseFloat(ibPrice) * elements.getBoxObCount() / elements.getBoxIbCount();
					}
					elements.setBoxIbPrice(Float.parseFloat(ibPrice));
				}
				if (elements.getBoxMbTypeId() != null && !"".equals(elements.getBoxMbTypeId())) {
					String mbPrice = this.calPrice(elements, elements.getBoxMbTypeId());
					if (elements.getBoxMbCount() > 0) {
						mPrice = Float.parseFloat(mbPrice) * elements.getBoxObCount() / elements.getBoxMbCount();
					}
					elements.setBoxMbPrice(Float.parseFloat(mbPrice));
				}
				if (elements.getBoxObTypeId() != null && !"".equals(elements.getBoxObTypeId())) {
					String obPrice = this.calPrice(elements, elements.getBoxObTypeId());
					if (elements.getBoxObCount() > 0) {
						oPrice = Float.parseFloat(obPrice);
					}
					elements.setBoxObPrice(Float.parseFloat(obPrice));
				}
				if (elements.getBoxPbTypeId() != null && !"".equals(elements.getBoxPbTypeId())) {
					String pbPrice = this.calPrice(elements, elements.getBoxPbTypeId());
					if (elements.getBoxPbCount() > 0) {
						pPrice = Float.parseFloat(pbPrice) * elements.getBoxObCount() / elements.getBoxPbCount();
					}
					elements.setBoxPbPrice(Float.parseFloat(pbPrice));
				}
			}
			if (elements.getBoxObCount() != null && !"".equals(elements.getBoxObCount())) {
				if (elements.getBoxObCount() > 0) {
					packPrice = (pPrice + iPrice + mPrice + oPrice) / elements.getBoxObCount();
				}
			}
			Double priceFac = this.calPriceFacByPackPrice(elements.getId(), packPrice.toString());

			elements.setPackingPrice(packPrice);
			elements.setPriceFac(priceFac.floatValue());

			elements.setPicImg(null);
			elements.setCotPictures(null);
			elements.setCotFile(null);
			elements.setChilds(null);
			elements.setCotPriceFacs(null);
			elements.setCotEleFittings(null);
			elements.setCotElePrice(null);
			elements.setCotElePacking(null);

			resList.add(elements);
		}
		this.getElementsDao().updateRecords(resList);
	}

	// 修改全部样品包材成本
	public void updatePackPriceAll() {

		Float iPrice = 0.0f;
		Float mPrice = 0.0f;
		Float oPrice = 0.0f;
		Float pPrice = 0.0f;
		Float packPrice = 0.0f;

		List<CotElementsNew> resList = new ArrayList<CotElementsNew>();

		List<?> list = this.getElementsDao().find(" from CotElementsNew obj ");

		for (int i = 0; i < list.size(); i++) {
			CotElementsNew elements = (CotElementsNew) list.get(i);

			if (elements.getBoxIbTypeId() != null && !"".equals(elements.getBoxIbTypeId())) {
				String ibPrice = this.calPrice(elements, elements.getBoxIbTypeId());
				if (elements.getBoxIbCount() > 0) {
					iPrice = Float.parseFloat(ibPrice) * elements.getBoxObCount() / elements.getBoxIbCount();
				}
				elements.setBoxIbPrice(Float.parseFloat(ibPrice));
			}
			if (elements.getBoxMbTypeId() != null && !"".equals(elements.getBoxMbTypeId())) {
				String mbPrice = this.calPrice(elements, elements.getBoxMbTypeId());
				if (elements.getBoxMbCount() > 0) {
					mPrice = Float.parseFloat(mbPrice) * elements.getBoxObCount() / elements.getBoxMbCount();
				}
				elements.setBoxMbPrice(Float.parseFloat(mbPrice));
			}
			if (elements.getBoxObTypeId() != null && !"".equals(elements.getBoxObTypeId())) {
				String obPrice = this.calPrice(elements, elements.getBoxObTypeId());
				if (elements.getBoxObCount() > 0) {
					oPrice = Float.parseFloat(obPrice);
				}
				elements.setBoxObPrice(Float.parseFloat(obPrice));
			}
			if (elements.getBoxPbTypeId() != null && !"".equals(elements.getBoxPbTypeId())) {
				String pbPrice = this.calPrice(elements, elements.getBoxPbTypeId());
				if (elements.getBoxPbCount() > 0) {
					pPrice = Float.parseFloat(pbPrice) * elements.getBoxObCount() / elements.getBoxPbCount();
				}
				elements.setBoxPbPrice(Float.parseFloat(pbPrice));
			}
			if (elements.getBoxObCount() != null && !"".equals(elements.getBoxObCount())) {
				if (elements.getBoxObCount() > 0) {
					packPrice = (pPrice + iPrice + mPrice + oPrice) / elements.getBoxObCount();
				}
			}
			Double priceFac = this.calPriceFacByPackPrice(elements.getId(), packPrice.toString());

			elements.setPackingPrice(packPrice);
			elements.setPriceFac(priceFac.floatValue());

			elements.setPicImg(null);
			elements.setCotPictures(null);
			elements.setCotFile(null);
			elements.setChilds(null);
			elements.setCotPriceFacs(null);
			elements.setCotEleFittings(null);
			elements.setCotElePrice(null);
			elements.setCotElePacking(null);

			resList.add(elements);
		}
		this.getElementsDao().updateRecords(resList);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sail.cot.service.sample.CotElementsService#getJsonData(com.sail.cot.query.QueryInfo)
	 */
	public String getJsonData(QueryInfo queryInfo) throws DAOException {
		// TODO Auto-generated method stub
		return this.getElementsDao().getJsonData(queryInfo);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sail.cot.service.sample.CotElementsService#getOrderFacList(com.sail.cot.query.QueryInfo)
	 */
	// 根据条件查询主采购记录
	public List<?> getOrderFacList(QueryInfo queryInfo) {
		try {
			List<?> records = this.getElementsDao().findRecords(queryInfo);
			List<CotOrderFacVO> list = new ArrayList<CotOrderFacVO>();
			for (int i = 0; i < records.size(); i++) {
				Object[] obj = (Object[]) records.get(i);
				CotOrderFacVO cotOrderVO = new CotOrderFacVO();
				if (obj[0] != null) {
					cotOrderVO.setId((Integer) obj[0]);
				}
				if (obj[1] != null) {
					cotOrderVO.setOrderTime((Date) obj[1]);
				}
				if (obj[2] != null) {
					cotOrderVO.setFactoryId((Integer) obj[2]);
				}
				if (obj[3] != null) {
					cotOrderVO.setOrderNo(obj[3].toString());
				}
				if (obj[4] != null) {
					cotOrderVO.setBusinessPerson((Integer) obj[4]);
				}
				if (obj[5] != null) {
					cotOrderVO.setPriceFac(Float.parseFloat(obj[5].toString()));
				}
				if (obj[6] != null) {
					cotOrderVO.setPriceFacUint((Integer) obj[6]);
				}
				if (obj[7] != null) {
					cotOrderVO.setTotalFac(Float.parseFloat(obj[7].toString()));
				}
				if (obj[8] != null) {
					cotOrderVO.setBoxCount((Long) obj[8]);
				}
				if (obj[10] != null) {
					cotOrderVO.setBoxTypeId(new Integer(obj[10].toString()));
				}
				if (obj[9] != null) {
					cotOrderVO.setOrderStatus(new Integer(obj[9].toString()));
				}
				list.add(cotOrderVO);
			}
			return list;
		} catch (DAOException e) {
			e.printStackTrace();
			logger.error("查询出错");
		}
		return null;
	}

	// 根据查询条件查询样品编号字符串
	public String findEles(String queryStringHQL) {
		String ids = "";
		try {
			ids = this.getElementsDao().findEles(queryStringHQL);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return ids;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sail.cot.service.sample.CotElementsService#saveCheckMachineData4Mc550(java.lang.String,
	 *      java.lang.String, java.lang.String)
	 */
	public void saveCheckMachineData4Mc550(String ids, String page, String queryStringHQL) throws IOException {
		this.getElementsDao().saveCheckMachineData4Mc550(ids, page, queryStringHQL);

	}

	// 根据货号取得样品对象
	public void createPic(String eleId) {
		// 获得tomcat路径
		String classPath = CotElementsServiceImpl.class.getResource("/").toString();
		String systemPath = classPath.substring(6, classPath.length() - 16);

		CotElementsNew cotElementsNew = new CotElementsNew();
		List<CotElePic> list = this.getElementsDao().find("select p from CotElementsNew obj,CotElePic p where obj.id=p.fkId and obj.eleId='" + eleId + "'");
		if (list.size() > 0) {
			try {
				CotElePic cotElePic = (CotElePic) list.get(0);
				File picFile = new File(systemPath + "sampleImg/" + eleId + ".png");
				if (!picFile.isFile()) {
					picFile.createNewFile();
				}
				DataOutputStream os = new DataOutputStream(new FileOutputStream(picFile));
				os.write(cotElePic.getPicImg());
				os.close();
			} catch (FileNotFoundException e) { // 文件未找到
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	// 查找所有海关海关编码的退税率
	public Float getEleTax(Integer eId) {
		CotEleOther eleOther = (CotEleOther) this.getElementsDao().getById(CotEleOther.class, eId);
		return eleOther.getTaxRate();
	}

}
