package com.sail.cot.service.given.impl;

import java.io.File;
import java.io.FileInputStream;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import jxl.Cell;
import jxl.CellType;
import jxl.DateCell;
import jxl.NumberCell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.WorkbookSettings;
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
import com.sail.cot.dao.CotBaseDao;
import com.sail.cot.dao.sample.CotElementsDao;
import com.sail.cot.dao.sample.CotReportDao;
import com.sail.cot.domain.CotBoxPacking;
import com.sail.cot.domain.CotBoxType;
import com.sail.cot.domain.CotCurrency;
import com.sail.cot.domain.CotCustomer;
import com.sail.cot.domain.CotEleCfg;
import com.sail.cot.domain.CotEleOther;
import com.sail.cot.domain.CotElePic;
import com.sail.cot.domain.CotElementsNew;
import com.sail.cot.domain.CotEmps;
import com.sail.cot.domain.CotFactory;
import com.sail.cot.domain.CotFinaceAccountrecv;
import com.sail.cot.domain.CotFinacerecv;
import com.sail.cot.domain.CotFinacerecvDetail;
import com.sail.cot.domain.CotGiven;
import com.sail.cot.domain.CotGivenDetail;
import com.sail.cot.domain.CotGivenPic;
import com.sail.cot.domain.CotOrderDetail;
import com.sail.cot.domain.CotOrderPic;
import com.sail.cot.domain.CotPicture;
import com.sail.cot.domain.CotPriceDetail;
import com.sail.cot.domain.CotPricePic;
import com.sail.cot.domain.CotSign;
import com.sail.cot.domain.CotSyslog;
import com.sail.cot.domain.CotTypeLv1;
import com.sail.cot.domain.CotTypeLv2;
import com.sail.cot.domain.vo.CotGivenVO;
import com.sail.cot.domain.vo.CotMsgVO;
import com.sail.cot.domain.vo.CotPriceVO;
import com.sail.cot.query.QueryInfo;

import com.sail.cot.service.customer.CotCustomerService;
import com.sail.cot.service.given.CotGivenService;
import com.sail.cot.service.img.CotOpImgService;
import com.sail.cot.service.sample.CotElementsService;
import com.sail.cot.service.sample.CotReportService;
import com.sail.cot.service.system.CotSeqService;
import com.sail.cot.service.system.CotSysLogService;
import com.sail.cot.service.system.SetNoService;
import com.sail.cot.service.system.impl.CotSeqServiceImpl;
import com.sail.cot.util.ContextUtil;
import com.sail.cot.util.ListSort;
import com.sail.cot.util.Log4WebUtil;
import com.sail.cot.util.ReflectionUtils;
import com.sail.cot.util.SystemDicUtil;
import com.sail.cot.util.SystemUtil;
import com.sail.cot.util.ThreadLocalManager;
import com.sail.cot.util.ThreadObject;

public class CotGivenServiceImpl implements CotGivenService {

	private CotBaseDao cotBaseDao;
	private CotElementsDao cotElementsDao;
	private CotReportDao reportDao;
	private CotCustomerService custService;
	private SetNoService noService;
	private CotElementsService cotElementsService;
	private SystemDicUtil systemDicUtil = new SystemDicUtil();
	// private GenAllSeq seq = new GenAllSeq();
	private CotSeqService cotSeqService;

	private Logger logger = Log4WebUtil.getLogger(CotGivenServiceImpl.class);

	public CotElementsDao getCotElementsDao() {
		return cotElementsDao;
	}

	public void setCotElementsDao(CotElementsDao cotElementsDao) {
		this.cotElementsDao = cotElementsDao;
	}

	public CotBaseDao getCotBaseDao() {
		return cotBaseDao;
	}

	public void setCotBaseDao(CotBaseDao cotBaseDao) {
		this.cotBaseDao = cotBaseDao;
	}

	public void setNoService(SetNoService noService) {
		this.noService = noService;
	}

	// 操作日志
	CotSysLogService sysLogService;

	public void setSysLogService(CotSysLogService sysLogService) {
		this.sysLogService = sysLogService;
	}

	public CotElementsService getCotElementsService() {
		return cotElementsService;
	}

	public void setCotElementsService(CotElementsService cotElementsService) {
		this.cotElementsService = cotElementsService;
	}

	// 删除送样明细单产品的原来图片
	@SuppressWarnings("deprecation")
	public void deleteGivenDetailImg(Integer id) {

		CotGivenDetail cotGivenDetail = (CotGivenDetail) this.getCotBaseDao().getById(CotGivenDetail.class, id);
		WebContext ctx = WebContextFactory.get();
		String path = ctx.getHttpServletRequest().getRealPath("/");
		File file = new File(path + cotGivenDetail.getPicPath());
		if (file.exists()) {
			file.delete();
		}
	}

	// 删除送样单的产品明细
	@SuppressWarnings("deprecation")
	public Boolean deleteGivenDetailList(List<CotGivenDetail> givenDetailList) {
		List<Integer> ids = new ArrayList<Integer>();

		for (int i = 0; i < givenDetailList.size(); i++) {
			CotGivenDetail cotGivenDetail = (CotGivenDetail) givenDetailList.get(i);
			ids.add(cotGivenDetail.getId());
		}
		// 获取操作人
		WebContext ctx = WebContextFactory.get();
		Integer empId = (Integer) ctx.getSession().getAttribute("empId");
		try {
			this.getCotBaseDao().deleteRecordByIds(ids, "CotGivenDetail");
			// 添加操作日志
			CotSyslog syslog = new CotSyslog();
			syslog.setEmpId(empId);
			syslog.setOpMessage("批量删除送样单明细成功");
			syslog.setOpModule("given");
			syslog.setOpTime(new Date(System.currentTimeMillis()));
			syslog.setOpType(3); // 1:添加 2：修改 3：删除
			List<CotSyslog> logList = new ArrayList<CotSyslog>();
			logList.add(syslog);
			sysLogService.addSysLog(logList);
		} catch (DAOException e) {
			e.printStackTrace();
			logger.error("删除送样单的产品明细异常", e);
			// 添加操作日志
			CotSyslog syslog = new CotSyslog();
			syslog.setEmpId(empId);
			syslog.setOpMessage("批量删除送样单的产品明细失败，失败原因：" + e.getMessage());
			syslog.setOpModule("given");
			syslog.setOpTime(new Date(System.currentTimeMillis()));
			syslog.setOpType(3); // 1:添加 2：修改 3：删除
			List<CotSyslog> logList = new ArrayList<CotSyslog>();
			logList.add(syslog);
			sysLogService.addSysLog(logList);
			return false;
		}

		return true;
	}

	// 删除主送样单
	@SuppressWarnings( { "unchecked", "deprecation" })
	public Boolean deleteGivenList(List<CotGiven> givenList) {
		// 获取操作人
		WebContext ctx = WebContextFactory.get();
		Integer empId = (Integer) ctx.getSession().getAttribute("empId");
		List<Integer> ids = new ArrayList<Integer>();
		for (int i = 0; i < givenList.size(); i++) {
			CotGiven cotGiven = (CotGiven) givenList.get(i);
			Integer givenId = cotGiven.getId();
			ids.add(givenId);
			List<CotGivenDetail> givenDetailList = (List<CotGivenDetail>) this.getDetailListByGivenId(givenId);
			if (givenDetailList != null) {
				this.deleteGivenDetailList(givenDetailList);
			}
			List<CotSign> signList = (List<CotSign>) this.getSignByGivenId(givenId);
			if (signList != null) {
				this.deleteSignList(signList);
			}
		}
		try {
			this.getCotBaseDao().deleteRecordByIds(ids, "CotGiven");
			// 添加操作日志
			CotSyslog syslog = new CotSyslog();
			syslog.setEmpId(empId);
			syslog.setOpMessage("批量删除主送样单成功");
			syslog.setOpModule("given");
			syslog.setOpTime(new Date(System.currentTimeMillis()));
			syslog.setOpType(3); // 1:添加 2：修改 3：删除
			List<CotSyslog> logList = new ArrayList<CotSyslog>();
			logList.add(syslog);
			sysLogService.addSysLog(logList);
			return true;
		} catch (DAOException e) {
			logger.error("删除主送样单出错");
			// 添加操作日志
			CotSyslog syslog = new CotSyslog();
			syslog.setEmpId(empId);
			syslog.setOpMessage("批量删除主送样单失败，失败原因：" + e.getMessage());
			syslog.setOpModule("given");
			syslog.setOpTime(new Date(System.currentTimeMillis()));
			syslog.setOpType(3); // 1:添加 2：修改 3：删除
			List<CotSyslog> logList = new ArrayList<CotSyslog>();
			logList.add(syslog);
			sysLogService.addSysLog(logList);
			return false;
		}
	}

	// 查询送样单单号是否存在
	public Integer findIsExistGivenNo(String givenNo, String id) {
		String hql = "select obj.id from CotGiven obj where obj.givenNo='" + givenNo + "'";
		List<?> res = this.getCotBaseDao().find(hql);
		if (res.size() == 0) {
			return null;
		}
		if (res.size() == 1) {
			Integer oldId = (Integer) res.get(0);
			if (oldId.toString().equals(id)) {
				return null;
			} else {
				return 1;
			}
		}
		return 2;
	}

	// 查询客户中文简称
	public Map<?, ?> getCustMap() {

		Map<String, String> retur = new HashMap<String, String>();
		List<?> res = this.getCotBaseDao().getRecords("CotCustomer");
		for (int i = 0; i < res.size(); i++) {
			CotCustomer cotCustomer = (CotCustomer) res.get(i);
			retur.put(cotCustomer.getId().toString(), cotCustomer.getCustomerShortName());

		}
		return retur;
	}

	// 根据客户编号查询客户信息
	public CotCustomer getCustomerById(Integer id) {
		CotCustomer cotCustomer = (CotCustomer) this.getCotBaseDao().getById(CotCustomer.class, id);
		if (cotCustomer != null) {
			CotCustomer custClone = (CotCustomer) SystemUtil.deepClone(cotCustomer);
			custClone.setPicImg(null);
			custClone.setCustImg(null);
			custClone.setCustomerClaim(null);
			custClone.setCotCustContacts(null);
			custClone.setCustomerVisitedLogs(null);
			return custClone;
		}
		return null;
	}

	// 查询所有厂家名称
	public Map<?, ?> getFacMap() {

		Map<String, String> retur = new HashMap<String, String>();
		List<?> res = this.getCotBaseDao().getRecords("CotFactory");
		for (int i = 0; i < res.size(); i++) {
			CotFactory cotFactory = (CotFactory) res.get(i);
			retur.put(cotFactory.getId().toString(), cotFactory.getShortName());

		}
		return retur;
	}

	// 获得样品材质的映射
	public Map<?, ?> getTypeMap() {
		Map<String, String> map = new HashMap<String, String>();
		List<?> list = this.getCotBaseDao().getRecords("CotTypeLv1");
		for (int i = 0; i < list.size(); i++) {
			CotTypeLv1 cotTypeLv1 = (CotTypeLv1) list.get(i);
			map.put(cotTypeLv1.getId().toString(), cotTypeLv1.getTypeName());
		}
		return map;
	}

	// 获得包装方式的映射
	public Map<?, ?> getBoxTypeMap() {
		Map<String, String> map = new HashMap<String, String>();
		List<?> list = this.getCotBaseDao().getRecords("CotBoxType");
		for (int i = 0; i < list.size(); i++) {
			CotBoxType cotBoxType = (CotBoxType) list.get(i);
			map.put(cotBoxType.getId().toString(), cotBoxType.getTypeName());
		}
		return map;
	}

	// 查询所有用户姓名
	public Map<?, ?> getEmpsMap() {
		Map<String, String> map = new HashMap<String, String>();
		List<?> list = this.getCotBaseDao().getRecords("CotEmps");
		for (int i = 0; i < list.size(); i++) {
			CotEmps cotEmps = (CotEmps) list.get(i);
			map.put(cotEmps.getId().toString(), cotEmps.getEmpsName());
		}
		return map;
	}

	// 根据厂家编号查询厂家信息
	public CotFactory getFactoryById(Integer id) {

		return (CotFactory) this.getCotBaseDao().getById(CotFactory.class, id);
	}

	// 根据送样编号查找送样单信息
	public CotGiven getGivenById(Integer id) {
		CotGiven obj = (CotGiven) this.getCotBaseDao().getById(CotGiven.class, id);
		obj.setCotGivenDetails(null);
		obj.setCotSign(null);
		return obj;
	}

	// 根据送样编号查找产品信息
	public List<?> getDetailListByGivenId(Integer givenId) {

		List<?> givenDetailList = this.getCotBaseDao().find("from CotGivenDetail obj where obj.givenId=" + givenId);
		if (givenDetailList.size() > 0) {
			return givenDetailList;
		}
		return null;
	}

	// 根据送样明细编号查找送样明细单信息
	public CotGivenDetail getGivenDetailById(Integer detailId) {
		CotGivenDetail cotGivenDetail = (CotGivenDetail) this.getCotBaseDao().getById(CotGivenDetail.class, detailId);
		if (cotGivenDetail != null) {
			cotGivenDetail.setPicImg(null);
			return cotGivenDetail;
		}
		return null;
	}

	// 根据送样明细编号查找PicImg
	public byte[] getPicImgByDetailId(Integer detailId) {
		List<?> list = this.getCotBaseDao().find(" from CotGivenDetail obj where obj.id=" + detailId);
		if (list.size() > 0) {
			CotGivenDetail cotGivenDetail = (CotGivenDetail) list.get(0);
			byte[] picImg = cotGivenDetail.getPicImg();
			if (picImg != null) {
				return picImg;
			}
		}
		return null;
	}

	// 查询记录
	public List<?> getList(QueryInfo queryInfo) {

		try {
			return this.getCotBaseDao().findRecords(queryInfo);
		} catch (DAOException e) {

			e.printStackTrace();
		}
		return null;
	}

	// 得到objName的集合
	public List<?> getObjList(String objName) {

		return this.getCotBaseDao().getRecords(objName);
	}

	// 得到总记录数
	public int getRecordCount(QueryInfo queryInfo) {

		try {
			return this.getCotBaseDao().getRecordsCount(queryInfo);
		} catch (DAOException e) {

			e.printStackTrace();
		}
		return 0;
	}

	// 更新送样明细的产品
	public Boolean modifyGivenDetail(List<CotGivenDetail> detailList) {
		// WebContext ctx = WebContextFactory.get();
		// CotEmps cotEmps = (CotEmps) ctx.getSession().getAttribute("emp");
		try {
			this.getCotBaseDao().updateRecords(detailList);
			// //添加操作日志
			// CotSyslog syslog = new CotSyslog();
			// //syslog.setEmpId(cotEmps.getId());
			// syslog.setOpMessage("批量更新送样明细成功");
			// syslog.setOpModule("given");
			// syslog.setOpTime(new Date(System.currentTimeMillis()));
			// syslog.setOpType(2); //1:添加 2：修改 3：删除
			// List<CotSyslog> logList = new ArrayList<CotSyslog>();
			// logList.add(syslog);
			// sysLogService.addSysLog(logList);
			return true;
		} catch (Exception e) {
			// //添加操作日志
			// CotSyslog syslog = new CotSyslog();
			// syslog.setEmpId(cotEmps.getId());
			// syslog.setOpMessage("批量/更新送样明细失败，失败原因："+e.getMessage());
			// syslog.setOpModule("given");
			// syslog.setOpTime(new Date(System.currentTimeMillis()));
			// syslog.setOpType(2); //1:添加 2：修改 3：删除
			// List<CotSyslog> logList = new ArrayList<CotSyslog>();
			// logList.add(syslog);
			e.printStackTrace();
			return false;
		}
	}

	// 更新送样单的产品明细
	public Boolean updateGivenDetail(CotGivenDetail e, String eleProTime) {
		// // 没选择厂家时,设置厂家为'未定义'
		// if (e.getFactoryId() == null) {
		// String hql = "from CotFactory obj where obj.shortName='未定义'";
		// List<?> list = this.getCotBaseDao().find(hql);
		// CotFactory facDefault = (CotFactory) list.get(0);
		// e.setFactoryId(facDefault.getId());
		// }
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		// 样品编辑时间
		e.setEleAddTime(new java.sql.Date(System.currentTimeMillis()));
		try {
			if (!"".equals(eleProTime)) {
				// 样品开发时间
				e.setEleProTime(new java.sql.Date(sdf.parse(eleProTime).getTime()));
			}
		} catch (Exception ex) {
			logger.error("保存开发时间错误!");
			return false;
		}
		// 克隆对象,避免造成指针混用
		CotGivenDetail cloneObj = (CotGivenDetail) SystemUtil.deepClone(e);
		byte[] picImg = this.getPicImgByDetailId(e.getId());
		cloneObj.setPicImg(picImg);

		List<CotGivenDetail> records = new ArrayList<CotGivenDetail>();
		records.add(cloneObj);
		// 获取操作人
		WebContext ctx = WebContextFactory.get();
		CotEmps cotEmps = (CotEmps) ctx.getSession().getAttribute("emp");
		try {
			this.getCotBaseDao().updateRecords(records);
			// 添加操作日志
			CotSyslog syslog = new CotSyslog();
			syslog.setEmpId(cotEmps.getId());
			syslog.setItemNo(cloneObj.getId().toString());
			syslog.setOpMessage("更改送样明细成功");
			syslog.setOpModule("given");
			syslog.setOpTime(new Date(System.currentTimeMillis()));
			syslog.setOpType(2); // 1:添加 2：修改 3：删除
			List<CotSyslog> logList = new ArrayList<CotSyslog>();
			logList.add(syslog);
			sysLogService.addSysLog(logList);
		} catch (Exception ex) {
			ex.printStackTrace();
			logger.error("保存样品错误!");
			// 添加操作日志
			CotSyslog syslog = new CotSyslog();
			syslog.setEmpId(cotEmps.getId());
			syslog.setItemNo(cloneObj.getId().toString());
			syslog.setOpMessage("更改送样明细失败，失败原因：" + ex.getMessage());
			syslog.setOpModule("given");
			syslog.setOpTime(new Date(System.currentTimeMillis()));
			syslog.setOpType(2); // 1:添加 2：修改 3：删除
			List<CotSyslog> logList = new ArrayList<CotSyslog>();
			logList.add(syslog);
			sysLogService.addSysLog(logList);
			return false;
		}
		return true;
	}

	// 获取费用结算方式
	public List<?> getBalanceTypeList() {

		return this.getCotBaseDao().getRecords("CotBalanceType");
	}

	// 获取送样类型
	public List<?> getGivenTypeList() {

		return this.getCotBaseDao().getRecords("CotGivenType");
	}

	// 保存主送样单
	public Integer saveOrUpdateGiven(CotGiven cotGiven, String givenTime, String custRequiretime, String realGiventime) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
		cotGiven.setAddTime(new Date(System.currentTimeMillis()));// 修改时间
		WebContext ctx = WebContextFactory.get();
		CotEmps cotEmps = (CotEmps) ctx.getSession().getAttribute("emp");
		cotGiven.setEmpId(cotEmps.getId());// 操作人ID
		cotGiven.setAddPerson(cotEmps.getEmpsName());// 操作人
		// 征样状态 0:(未审核),1(已审核)通过,2(审核不通过)
		if (cotGiven.getGivenStatus() == null) {
			cotGiven.setGivenStatus(new Long(0));// 审核状态
		}
		// cotGiven.setGivenIscheck(new Long(0));// 是否需要审核
		// 是否需要审核 0:不需要审核 1:需要审核 （默认0）

		List<CotGiven> records = new ArrayList<CotGiven>();

		// 保存主送样单
		try {
			if (givenTime != null && !"".equals(givenTime)) {
				cotGiven.setGivenTime(new Timestamp(sdf.parse(givenTime).getTime()));
			}
			if (custRequiretime != null && !"".equals(custRequiretime)) {
				cotGiven.setCustRequiretime(new Date(sdf2.parse(custRequiretime).getTime()));
			}
			if (realGiventime != null && !"".equals(realGiventime)) {
				cotGiven.setRealGiventime(new Date(sdf2.parse(realGiventime).getTime()));
			}
			if (cotGiven.getId() == null) // 新增时更新客户序列号
			{
				// this.getCustService().updateCustSeqByType(cotGiven.getCustId(),
				// "given", cotGiven.getGivenTime().toString());
				// // 更新全局序列号
				// SetNoServiceImpl setNoService = new SetNoServiceImpl();
				// setNoService.saveSeq("given", cotGiven.getGivenTime()
				// .toString());
				CotSeqService cotSeqService = new CotSeqServiceImpl();
				cotSeqService.saveCustSeq(cotGiven.getCustId(), "given", cotGiven.getGivenTime().toString());
				cotSeqService.saveSeq("given");
			}
			records.add(cotGiven);
			this.getCotBaseDao().saveOrUpdateRecords(records);
			// 添加操作日志
			CotSyslog syslog = new CotSyslog();
			syslog.setEmpId(cotEmps.getId());
			syslog.setItemNo(cotGiven.getGivenNo());
			syslog.setOpMessage("添加或修改主送样单成功");
			syslog.setOpModule("given");
			syslog.setOpTime(new Date(System.currentTimeMillis()));
			syslog.setOpType(1); // 1:添加 2：修改 3：删除
			List<CotSyslog> logList = new ArrayList<CotSyslog>();
			logList.add(syslog);
			sysLogService.addSysLog(logList);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("保存送样单出错！");
			// 添加操作日志
			CotSyslog syslog = new CotSyslog();
			syslog.setEmpId(cotEmps.getId());
			syslog.setItemNo(cotGiven.getGivenNo());
			syslog.setOpMessage("添加或修改主送样单失败，失败原因：" + e.getMessage());
			syslog.setOpModule("given");
			syslog.setOpTime(new Date(System.currentTimeMillis()));
			syslog.setOpType(1); // 1:添加 2：修改 3：删除
			List<CotSyslog> logList = new ArrayList<CotSyslog>();
			logList.add(syslog);
			sysLogService.addSysLog(logList);
			return null;
		}
		return cotGiven.getId();
	}

	// 保存主单
	public Integer saveByExcel(CotGiven cotGiven, String givenTime, String custRequiretime, String realGiventime) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
		cotGiven.setAddTime(new Date(System.currentTimeMillis()));// 修改时间
		WebContext ctx = WebContextFactory.get();
		CotEmps cotEmps = (CotEmps) ctx.getSession().getAttribute("emp");
		cotGiven.setEmpId(cotEmps.getId());// 操作人ID
		cotGiven.setAddPerson(cotEmps.getEmpsName());// 操作人
		// 征样状态 0:(未审核),1(已审核)通过,2(审核不通过)
		if (cotGiven.getGivenStatus() == null) {
			cotGiven.setGivenStatus(new Long(0));// 审核状态
		}
		cotGiven.setGivenIscheck(new Long(0));// 是否需要审核
		// 是否需要审核 0:不需要审核 1:需要审核 （默认0）

		List<CotGiven> records = new ArrayList<CotGiven>();

		// 保存主送样单
		try {
			if (givenTime != null && !"".equals(givenTime)) {

				cotGiven.setGivenTime(new Timestamp(sdf2.parse(givenTime).getTime()));

			}
			if (custRequiretime != null && !"".equals(custRequiretime)) {
				cotGiven.setCustRequiretime(new Date(sdf2.parse(custRequiretime).getTime()));
			}
			if (realGiventime != null && !"".equals(realGiventime)) {
				cotGiven.setRealGiventime(new Date(sdf2.parse(realGiventime).getTime()));
			}

			records.add(cotGiven);
			this.getCotBaseDao().saveOrUpdateRecords(records);
			// 添加操作日志
			CotSyslog syslog = new CotSyslog();
			syslog.setEmpId(cotEmps.getId());
			syslog.setItemNo(cotGiven.getGivenNo());
			syslog.setOpMessage("添加或修改主送样单成功");
			syslog.setOpModule("given");
			syslog.setOpTime(new Date(System.currentTimeMillis()));
			syslog.setOpType(1); // 1:添加 2：修改 3：删除
			List<CotSyslog> logList = new ArrayList<CotSyslog>();
			logList.add(syslog);
			sysLogService.addSysLog(logList);
			// 保存明细
			this.saveDetail(cotGiven.getId(), cotGiven.getCurrencyId(), cotGiven.getCustId());
			return cotGiven.getId();
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("保存送样单出错！");
			// 添加操作日志
			CotSyslog syslog = new CotSyslog();
			syslog.setEmpId(cotEmps.getId());
			syslog.setItemNo(cotGiven.getGivenNo());
			syslog.setOpMessage("添加或修改主送样单失败，失败原因：" + e.getMessage());
			syslog.setOpModule("given");
			syslog.setOpTime(new Date(System.currentTimeMillis()));
			syslog.setOpType(1); // 1:添加 2：修改 3：删除
			List<CotSyslog> logList = new ArrayList<CotSyslog>();
			logList.add(syslog);
			sysLogService.addSysLog(logList);
			return null;
		}
	}

	// 根据样品货号组装送样明细产品对象
	public CotGivenDetail findDetailByEleId(String eleId) {
		// 查找样品对象
		String hql = "from CotElementsNew obj where obj.eleId = '" + eleId + "'";
		List<?> list = this.getCotBaseDao().find(hql);
		// 样品对象不能为空
		if (list.size() == 0) {
			return null;
		}
		CotElementsNew cotElementsNew = (CotElementsNew) list.get(0);

		// 新建送样明细对象
		CotGivenDetail cotGivenDetail = new CotGivenDetail();

		// 1.使用反射获取对象的所有属性名称
		String[] propEle = ReflectionUtils.getDeclaredFields(CotElementsNew.class);

		ConvertUtilsBean convertUtils = new ConvertUtilsBean();
		SqlDateConverter dateConverter = new SqlDateConverter();
		convertUtils.register(dateConverter, Date.class);
		// 因为要注册converter,所以不能再使用BeanUtils的静态方法了，必须创建BeanUtilsBean实例
		BeanUtilsBean beanUtils = new BeanUtilsBean(convertUtils, new PropertyUtilsBean());
		// beanUtils.setProperty(bean, name, value);

		for (int i = 0; i < propEle.length; i++) {
			try {
				String value = beanUtils.getProperty(cotElementsNew, propEle[i]);
				if ("cotPictures".equals(propEle[i]) || "cotFile".equals(propEle[i]) || "childs".equals(propEle[i]) || "picImg".equals(propEle[i]) || "cotPriceFacs".equals(propEle[i]) || "cotElePrice".equals(propEle[i]) || "cotEleFittings".equals(propEle[i]) || "eleFittingPrice".equals(propEle[i]) || "cotElePacking".equals(propEle[i]) || "packingPrice".equals(propEle[i])) {
					continue;
				}
				if (value != null) {
					BeanUtils.setProperty(cotGivenDetail, propEle[i], BeanUtils.getProperty(cotElementsNew, propEle[i]));
					cotGivenDetail.setSrcId(cotElementsNew.getId());
					cotGivenDetail.setId(null);
				}

			} catch (Exception e) {
				logger.error(propEle[i] + ":转换错误!");
			}
		}
		// 填充厂家简称
		// List<?> facList = getDicList("factory");
		List facList = this.getCotBaseDao().getRecords("CotFactory");
		for (int j = 0; j < facList.size(); j++) {
			CotFactory cotFactory = (CotFactory) facList.get(j);
			if (cotGivenDetail.getFactoryId() != null && cotFactory.getId().intValue() == cotGivenDetail.getFactoryId().intValue()) {
				cotGivenDetail.setFactoryShortName(cotFactory.getShortName());
			}
		}
		return cotGivenDetail;
	}

	// 判断该产品货号是否存在
	public Boolean findIsExistEleByEleId(String eleId) {
		String hql = "from CotElementsNew obj where obj.eleId='" + eleId + "'";
		List<?> list = this.getCotBaseDao().find(hql);
		if (list.size() != 0) {
			return true;
		} else {
			return false;
		}
	}

	// 保存送样单的产品明细
	public Boolean addGivenDetails(List<?> details) {
		WebContext ctx = WebContextFactory.get();
		CotEmps cotEmps = null;
		ThreadObject obj = (ThreadObject) ThreadLocalManager.getCurrentThread().get();
		Integer empId = 0;
		if (ctx != null) {
			cotEmps = (CotEmps) ctx.getSession().getAttribute("emp");
			empId = cotEmps.getId();
		}
		if (obj != null)
			empId = obj.getEmpId();
		try {
			this.getCotBaseDao().saveOrUpdateRecords(details);
			// 添加操作日志
			CotSyslog syslog = new CotSyslog();
			syslog.setEmpId(empId);
			syslog.setOpMessage("批量添加送样单的产品明细成功");
			syslog.setOpModule("given");
			syslog.setOpTime(new Date(System.currentTimeMillis()));
			syslog.setOpType(1); // 1:添加 2：修改 3：删除
			List<CotSyslog> logList = new ArrayList<CotSyslog>();
			logList.add(syslog);
			sysLogService.addSysLog(logList);
			return true;
		} catch (Exception e) {
			logger.error("保存送样单的产品明细出错!");
			// 添加操作日志
			CotSyslog syslog = new CotSyslog();
			syslog.setEmpId(empId);
			syslog.setOpMessage("批量添加送样单的产品明细失败，失败原因：" + e.getMessage());
			syslog.setOpModule("given");
			syslog.setOpTime(new Date(System.currentTimeMillis()));
			syslog.setOpType(1); // 1:添加 2：修改 3：删除
			List<CotSyslog> logList = new ArrayList<CotSyslog>();
			logList.add(syslog);
			sysLogService.addSysLog(logList);
			return false;
		}
	}

	// 修改主送样单
	public Boolean modifyGivenList(List<?> givenList) {

		for (int i = 0; i < givenList.size(); i++) {
			CotGiven cotGiven = (CotGiven) givenList.get(i);
			cotGiven.setAddTime(new Date(System.currentTimeMillis()));// 修改时间
		}
		WebContext ctx = WebContextFactory.get();
		Integer empId = (Integer) ctx.getSession().getAttribute("empId");
		try {
			this.getCotBaseDao().updateRecords(givenList);
			// 添加操作日志
			CotSyslog syslog = new CotSyslog();
			syslog.setEmpId(empId);
			syslog.setOpMessage("修改主送样单成功");
			syslog.setOpModule("given");
			syslog.setOpTime(new Date(System.currentTimeMillis()));
			syslog.setOpType(2); // 1:添加 2：修改 3：删除
			List<CotSyslog> logList = new ArrayList<CotSyslog>();
			logList.add(syslog);
			sysLogService.addSysLog(logList);
			return true;
		} catch (Exception e) {
			// 添加操作日志
			CotSyslog syslog = new CotSyslog();
			syslog.setEmpId(empId);
			syslog.setOpMessage("修改主送样单失败，失败原因：" + e.getMessage());
			syslog.setOpModule("given");
			syslog.setOpTime(new Date(System.currentTimeMillis()));
			syslog.setOpType(2); // 1:添加 2：修改 3：删除
			List<CotSyslog> logList = new ArrayList<CotSyslog>();
			logList.add(syslog);
			sysLogService.addSysLog(logList);
			return false;
		}
	}

	// 查询该单的所有送样明细的产品货号
	public String findEleByGivenId(Integer givenId) {
		String hql = "select obj.eleId from CotGivenDetail obj where obj.givenId=" + givenId;
		List<?> list = this.getCotBaseDao().find(hql);
		String eleIds = "";
		for (int i = 0; i < list.size(); i++) {
			String eleId = (String) list.get(i);
			eleIds += eleId + ",";
		}
		return eleIds;
	}

	// 根据送样明细产品的ids删除
	public void deleteDetailByIds(HttpServletRequest request, List<Integer> ids) {
		WebContext ctx = WebContextFactory.get();
		CotEmps cotEmps = null;
		ThreadObject obj = (ThreadObject) ThreadLocalManager.getCurrentThread().get();
		Integer empId = 0;
		if (ctx != null) {
			cotEmps = (CotEmps) ctx.getSession().getAttribute("emp");
			empId = cotEmps.getId();
		}
		if (obj != null)
			empId = obj.getEmpId();
		try {
			// 清除内存数据
			// Object temp = SystemUtil.getObjBySession(request.getSession(),
			// "given");
			// Map<String, CotGivenDetail> givenMap = (TreeMap<String,
			// CotGivenDetail>) temp;
			// if (givenMap != null) {
			// for (int i = 0; i < ids.size(); i++) {
			// Integer id = (Integer) ids.get(i);
			// Iterator<?> it = givenMap.keySet().iterator();
			// while(it.hasNext()){
			// String key =(String) it.next();
			// CotGivenDetail detail = givenMap.get(key);
			// if(detail.getId()!=null &&
			// detail.getId().intValue()==id.intValue()){
			// it.remove();
			// }
			// }
			// }
			// }
			this.getCotBaseDao().deleteRecordByIds(ids, "CotGivenDetail");
			// 添加操作日志
			if (ctx != null || obj != null) {
				CotSyslog syslog = new CotSyslog();
				syslog.setEmpId(empId);
				syslog.setOpMessage("根据送样明细产品的ids删除成功");
				syslog.setOpModule("given");
				syslog.setOpTime(new Date(System.currentTimeMillis()));
				syslog.setOpType(3); // 1:添加 2：修改 3：删除
				List<CotSyslog> logList = new ArrayList<CotSyslog>();
				logList.add(syslog);
				sysLogService.addSysLog(logList);
			}
		} catch (DAOException e) {
			e.printStackTrace();
			// 添加操作日志
			if (ctx != null || obj != null) {
				CotSyslog syslog = new CotSyslog();
				syslog.setEmpId(empId);
				syslog.setOpMessage("根据送样明细产品的ids删除失败，失败原因：" + e.getMessage());
				syslog.setOpModule("given");
				syslog.setOpTime(new Date(System.currentTimeMillis()));
				syslog.setOpType(3); // 1:添加 2：修改 3：删除
				List<CotSyslog> logList = new ArrayList<CotSyslog>();
				logList.add(syslog);
				sysLogService.addSysLog(logList);
			}
		}
	}

	// 通过部门id获取所在部门员工列表
	@SuppressWarnings("unchecked")
	public List<CotEmps> getDeptEmpListByDeptId(Integer deptId) {
		List<CotEmps> empsList = new ArrayList<CotEmps>();
		List<CotEmps> deptEmpList = this.getCotBaseDao().find("from CotEmps obj where obj.deptId=" + deptId);
		if (deptEmpList.size() > 0) {
			for (int i = 0; i < deptEmpList.size(); i++) {
				CotEmps cotEmps = deptEmpList.get(i);
				cotEmps.setCustomers(null);
				empsList.add(cotEmps);
			}
			return empsList;
		}
		return null;
	}

	// 通过员工id获取所在员工信息
	@SuppressWarnings("unchecked")
	public List<CotEmps> getEmpListByEmpId(Integer empId) {
		List<CotEmps> empsList = new ArrayList<CotEmps>();
		List<CotEmps> list = this.getCotBaseDao().find("from CotEmps obj where obj.id=" + empId);
		if (list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				CotEmps cotEmps = list.get(i);
				cotEmps.setCustomers(null);
				empsList.add(cotEmps);
			}
			return empsList;
		}
		return null;
	}

	// 根据条件查询主报价单记录
	public List<?> getPriceList(QueryInfo queryInfo) {
		try {
			List<?> records = this.getCotBaseDao().findRecords(queryInfo);
			List<CotPriceVO> list = new ArrayList<CotPriceVO>();
			for (int i = 0; i < records.size(); i++) {
				Object[] obj = (Object[]) records.get(i);
				CotPriceVO cotPriceVO = new CotPriceVO();
				if (obj[0] != null) {
					cotPriceVO.setId((Integer) obj[0]);
				}
				if (obj[1] != null) {
					cotPriceVO.setPriceNo(obj[1].toString());
				}
				if (obj[2] != null) {
					cotPriceVO.setCustomerNo(obj[2].toString());
				}
				if (obj[3] != null) {
					cotPriceVO.setFullNameCn(obj[3].toString());
				}
				if (obj[4] != null) {
					cotPriceVO.setClauseId((Integer) obj[4]);
				}
				if (obj[5] != null) {
					cotPriceVO.setCurrencyId((Integer) obj[5]);
				}
				if (obj[6] != null) {
					cotPriceVO.setCommisionScale(Double.parseDouble(obj[6].toString()));
				}
				if (obj[7] != null) {
					cotPriceVO.setPriceProfit(Double.parseDouble(obj[7].toString()));
				}
				if (obj[8] != null) {
					cotPriceVO.setPriceTime((Timestamp) obj[8]);
				}
				if (obj[9] != null) {
					cotPriceVO.setAddPerson(obj[9].toString());
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

	// 查找所有报表类型
	public List<?> getRptFileList(Integer rptTypeId) {
		List<?> list = this.getCotBaseDao().find("from CotRptFile f where f.rptType=" + rptTypeId);
		return list;
	}

	// 根据报价编号字符串查找报价明细
	@SuppressWarnings("unchecked")
	public List<CotPriceDetail> findPriceDetailByIds(String ids) {
		List<CotPriceDetail> list = new ArrayList();
		String hql = "from CotPriceDetail obj where obj.id in (" + ids.substring(0, ids.length() - 1) + ")";
		list = this.getCotBaseDao().find(hql);
		return list;
	}

	// 根据订单编号字符串查找订单明细
	@SuppressWarnings("unchecked")
	public List<CotOrderDetail> findOrderDetailByIds(String ids) {
		List<CotOrderDetail> list = new ArrayList();
		String hql = "from CotOrderDetail obj where obj.id in (" + ids.substring(0, ids.length() - 1) + ")";
		list = this.getCotBaseDao().find(hql);
		return list;
	}

	// 得到objName的集合
	public List<?> getList(String objName) {
		return this.getCotBaseDao().getRecords(objName);
	}

	// 查询所有币种
	public Map<?, ?> getCurrencyMap() {
		Map<String, String> map = new HashMap<String, String>();
		List<?> list = this.getCotBaseDao().getRecords("CotCurrency");
		for (int i = 0; i < list.size(); i++) {
			CotCurrency cotCurrency = (CotCurrency) list.get(i);
			map.put(cotCurrency.getId().toString(), cotCurrency.getCurNameEn());
		}
		return map;
	}

	// 判断要更新到样品表的明细货号哪些重复
	public Map<String, List<String>> findIsExistInEle(String[] eleIdAry) {
		Map<String, List<String>> map = new HashMap<String, List<String>>();
		String eles = "";
		List<String> allEleList = new ArrayList<String>();
		List<String> sameList = new ArrayList<String>();
		List<String> disList = new ArrayList<String>();

		for (int i = 0; i < eleIdAry.length; i++) {
			eles += "'" + eleIdAry[i] + "',";
			allEleList.add(eleIdAry[i]);
		}

		String hql = "select obj.eleId from CotElementsNew obj where obj.eleId in (" + eles.substring(0, eles.length() - 1) + ")";
		List<?> list = this.getCotBaseDao().find(hql);
		Iterator<?> it = list.iterator();
		while (it.hasNext()) {
			String str = (String) it.next();
			sameList.add(str);
			// 截取掉eles中eleID
			allEleList.remove(str);
		}
		for (int i = 0; i < allEleList.size(); i++) {
			String eleId = (String) allEleList.get(i);
			disList.add(eleId);
		}
		map.put("same", sameList);
		map.put("dis", disList);
		return map;
	}

	// 根据报价的图片转换成样品图片
	public CotElePic changeElePic(String type, Integer srcId, CotElePic pic) {
		CotElePic cotElePic = null;
		if (pic != null) {
			cotElePic = pic;
		} else {
			cotElePic = new CotElePic();
		}
		if (("price").equals(type)) {
			CotPricePic p = (CotPricePic) this.getCotBaseDao().getById(CotPricePic.class, srcId);
			cotElePic.setEleId(p.getEleId());
			cotElePic.setPicImg(p.getPicImg());
			cotElePic.setPicName(p.getPicName());
			cotElePic.setPicSize(p.getPicImg().length);
		}
		if (("order").equals(type)) {
			CotOrderPic p = (CotOrderPic) this.getCotBaseDao().getById(CotOrderPic.class, srcId);
			cotElePic.setEleId(p.getEleId());
			cotElePic.setPicImg(p.getPicImg());
			cotElePic.setPicName(p.getPicName());
			cotElePic.setPicSize(p.getPicImg().length);
		}
		if (("given").equals(type)) {
			CotGivenPic p = (CotGivenPic) this.getCotBaseDao().getById(CotGivenPic.class, srcId);
			cotElePic.setEleId(p.getEleId());
			cotElePic.setPicImg(p.getPicImg());
			cotElePic.setPicName(p.getPicName());
			cotElePic.setPicSize(p.getPicImg().length);
		}
		if (("ele").equals(type)) {
			cotElePic = (CotElePic) this.getCotBaseDao().getById(CotElePic.class, srcId);
		}
		if (("none").equals(type)) {
			byte[] zwtp = this.getZwtpPic();
			cotElePic.setEleId("");
			cotElePic.setPicImg(zwtp);
			cotElePic.setPicName("");
			cotElePic.setPicSize(zwtp.length);
		}
		return cotElePic;
	}

	// 同步不同的样品的图片
	public Map getGivenByDisEles(String eleId) {

		Map map = new HashMap();

		Object[] obj = new Object[2];
		CotGivenDetail givenDetail = (CotGivenDetail) this.getGivenMapValue(eleId);

		obj[0] = givenDetail;

		CotElePic cotElePic = null;
		if (givenDetail.getType() == null) {
			String hql = "from CotGivenPic obj where obj.fkId=" + givenDetail.getId();
			List list = this.getCotBaseDao().find(hql);
			CotGivenPic cotGivenPic = (CotGivenPic) list.get(0);
			cotElePic = new CotElePic();
			cotElePic.setEleId(cotGivenPic.getEleId());
			cotElePic.setPicImg(cotGivenPic.getPicImg());
			cotElePic.setPicName(cotGivenPic.getPicName());
			cotElePic.setPicSize(cotGivenPic.getPicImg().length);
		} else {
			String type = givenDetail.getType();
			Integer srcId = givenDetail.getSrcId();
			cotElePic = changeElePic(type, srcId, null);
		}
		obj[1] = cotElePic;
		map.put(eleId.toLowerCase(), obj);

		return map;
	}

	// 根据样品货号字符串查询明细
	public void updateToEle(String[] same, String[] dis, String eleStr, String boxStr, String otherStr, boolean isPic) {
		// 相同时处理
		if (same != null) {
			String t = "";
			// key为eleId,value为随机数
			// Map<String, String> sameMap = new HashMap<String, String>();
			for (int i = 0; i < same.length; i++) {
				// sameMap.put(same[i], sameRdm[i]);
				t += "'" + same[i] + "',";
			}
			// 获得样品和图片集合
			String elePichql = "select e,e.eleId,obj from CotElementsNew e,CotElePic obj where obj.fkId=e.id and e.eleId in (" + t.substring(0, t.length() - 1) + ")";
			List<?> elePicList = this.getCotBaseDao().find(elePichql);
			// 给样品设置id
			List<CotElementsNew> listTemp = new ArrayList<CotElementsNew>();
			// 修改样品图片表
			List<CotElePic> listPic = new ArrayList<CotElePic>();
			for (int i = 0; i < elePicList.size(); i++) {
				Object[] obj = (Object[]) elePicList.get(i);
				// 获得报价明细,样品图片
				Object[] temp = (Object[]) getGivenByEle(obj[1].toString(), (CotElePic) obj[2]);
				CotElementsNew ele = setEleByTong((CotElementsNew) obj[0], (CotGivenDetail) temp[0], eleStr, boxStr, otherStr);
				listTemp.add(ele);
				listPic.add((CotElePic) temp[1]);
			}

			this.getCotBaseDao().updateRecords(listTemp);
			if (isPic == true) {
				for (int i = 0; i < listTemp.size(); i++) {
					CotElementsNew elementsNew = (CotElementsNew) listTemp.get(i);
					CotElePic cotElePic = (CotElePic) listPic.get(i);
					cotElePic.setFkId(elementsNew.getId());
					if (cotElePic.getEleId().equals("")) {
						cotElePic.setEleId(elementsNew.getEleId());
					}
					listPic.add(cotElePic);
				}
				this.getCotBaseDao().updateRecords(listPic);
			}
		}
		// 不同的处理
		if (dis != null) {

			List<CotElementsNew> listTemp = new ArrayList<CotElementsNew>();
			for (int i = 0; i < dis.length; i++) {
				// 获得寄样明细,样品图片(key为eleId,value为obj[2])
				Map map = getGivenByDisEles(dis[i]);

				Object[] temp = (Object[]) map.get(dis[i].toLowerCase());
				CotElementsNew newEle = new CotElementsNew();
				CotElementsNew ele = setEleByTong(newEle, (CotGivenDetail) temp[0], eleStr, boxStr, otherStr);
				if (ele.getEleFlag() == null) {
					ele.setEleFlag(0l);
				}
				listTemp.add(ele);
			}

			try {
				this.getCotBaseDao().saveRecords(listTemp);
				// 新建样品图片表
				List<CotElePic> listPic = new ArrayList<CotElePic>();
				for (int i = 0; i < listTemp.size(); i++) {
					CotElementsNew e = listTemp.get(i);
					Object[] temp = (Object[]) getGivenByDisEles(e.getEleId().toLowerCase()).get(e.getEleId().toLowerCase());
					CotElePic cotElePic = (CotElePic) temp[1];
					cotElePic.setFkId(e.getId());
					if (isPic != true) {
						byte[] zwtp = this.getZwtpPic();
						cotElePic.setEleId(e.getEleId());
						cotElePic.setPicImg(zwtp);
						cotElePic.setPicName(e.getEleId());
						cotElePic.setPicSize(zwtp.length);
					}
					listPic.add(cotElePic);
				}
				this.getCotBaseDao().saveRecords(listPic);
			} catch (DAOException e) {
				e.printStackTrace();
			}
		}
	}

	// 通过单号删除送样单的所有产品图片
	@SuppressWarnings("deprecation")
	public void deleteGivenImgByGivenNo(String givenNo) {
		WebContext ctx = WebContextFactory.get();
		String path = ctx.getHttpServletRequest().getRealPath("/");
		File file = new File(path + "/givenImg//" + givenNo);
		if (file.exists()) {
			File[] child = file.listFiles();
			if (child != null) {
				for (int i = 0; i < child.length; i++) {
					child[i].delete();
				}
			}
			file.delete();
		}
	}

	// 获取GivenMap
	@SuppressWarnings("unchecked")
	public TreeMap<String, CotGivenDetail> getGivenMap() {
		Object obj = SystemUtil.getObjBySession(null, "given");
		TreeMap<String, CotGivenDetail> givenMap = (TreeMap<String, CotGivenDetail>) obj;
		return givenMap;
	}

	// 储存GivenMap
	@SuppressWarnings("unchecked")
	public void setGivenMap(String eleId, CotGivenDetail cotGivenDetail) {
		Object obj = SystemUtil.getObjBySession(null, "given");
		if (obj == null) {
			TreeMap<String, CotGivenDetail> givenMap = new TreeMap<String, CotGivenDetail>();
			givenMap.put(eleId.toLowerCase(), cotGivenDetail);
			SystemUtil.setObjBySession(null, givenMap, "given");
		} else {
			TreeMap<String, CotGivenDetail> givenMap = (TreeMap<String, CotGivenDetail>) obj;
			givenMap.put(eleId.toLowerCase(), cotGivenDetail);
			SystemUtil.setObjBySession(null, givenMap, "given");
		}

	}

	// Action获取GivenMap
	@SuppressWarnings("unchecked")
	public TreeMap<String, CotGivenDetail> getGivenMapAction(HttpSession session) {
		Object obj = SystemUtil.getObjBySession(session, "given");
		TreeMap<String, CotGivenDetail> givenMap = (TreeMap<String, CotGivenDetail>) obj;
		return givenMap;
	}

	// Action储存GivenMap
	@SuppressWarnings("unchecked")
	public void setGivenMapAction(HttpSession session, String eleId, CotGivenDetail cotGivenDetail) {
		Object obj = SystemUtil.getObjBySession(session, "given");
		if (obj == null) {
			TreeMap<String, CotGivenDetail> givenMap = new TreeMap<String, CotGivenDetail>();
			givenMap.put(eleId.toLowerCase(), cotGivenDetail);
			SystemUtil.setObjBySession(session, givenMap, "given");
		} else {
			TreeMap<String, CotGivenDetail> givenMap = (TreeMap<String, CotGivenDetail>) obj;
			givenMap.put(eleId.toLowerCase(), cotGivenDetail);
			SystemUtil.setObjBySession(session, givenMap, "given");
		}

	}

	// 清空GivenMap
	public void clearGivenMap() {
		SystemUtil.clearSessionByType(null, "given");
	}

	// 清除GivenMap中eleId对应的映射
	public void delGivenMapByKey(String eleId) {
		TreeMap<String, CotGivenDetail> givenMap = this.getGivenMap();
		if (givenMap != null) {
			if (givenMap.containsKey(eleId.toLowerCase())) {
				givenMap.remove(eleId.toLowerCase());
			}
		}
	}

	// 在Action中清除GivenMap中eleId对应的映射
	public void delGivenMapByKeyAction(String eleId, HttpSession session) {
		TreeMap<String, CotGivenDetail> givenMap = this.getGivenMapAction(session);
		if (givenMap != null) {
			if (givenMap.containsKey(eleId.toLowerCase())) {
				givenMap.remove(eleId.toLowerCase());
			}
		}
	}

	// 判断货号是否已经添加
	public boolean checkExist(String eleId) {
		TreeMap<String, CotGivenDetail> givenMap = this.getGivenMap();
		if (givenMap != null) {
			if (givenMap.containsKey(eleId.toLowerCase())) {
				return true;
			}
		}
		return false;
	}

	// 判断货号字符串是否已经添加
	public List<String> checkExistEles(String[] eleAry) {
		List<String> list = new ArrayList<String>();
		for (int i = 0; i < eleAry.length; i++) {
			boolean flag = this.checkExist(eleAry[i]);
			if (!flag) {
				list.add(eleAry[i]);
			}
		}
		return list;
	}

	// 通过key获取GivenMap的value
	public CotGivenDetail getGivenMapValue(String eleId) {
		TreeMap<String, CotGivenDetail> givenMap = this.getGivenMap();
		if (givenMap != null) {
			CotGivenDetail cotGivenDetail = (CotGivenDetail) givenMap.get(eleId.toLowerCase());
			return cotGivenDetail;
		}
		return null;
	}

	// 通过货号修改Map中对应的送样明细
	public boolean updateMapValueByEleId(String eleId, String property, String value) {
		CotGivenDetail cotGivenDetail = getGivenMapValue(eleId.toLowerCase());
		if (cotGivenDetail == null)
			return false;
		try {
			if ("factoryId".equals(property)) {
				if ("".equals(value)) {
					cotGivenDetail.setFactoryId(null);
				} else {
					cotGivenDetail.setFactoryId(Integer.parseInt(value));
				}
			} else if ("eleTypeidLv1".equals(property)) {
				if ("".equals(value)) {
					cotGivenDetail.setEleTypeidLv1(null);
				} else {
					cotGivenDetail.setEleTypeidLv1(Integer.parseInt(value));
				}
			} else {
				BeanUtils.setProperty(cotGivenDetail, property, value);
			}
			this.setGivenMap(eleId.toLowerCase(), cotGivenDetail);
			return true;
		} catch (Exception ex) {
			ex.printStackTrace();
			return false;
		}
	}

	// 根据样品货号字符串获得样品集合并转化为订单明细
	public List<CotGivenDetail> saveDetailByEleIds(String eleIds, Integer mainId) {
		// 1.使用反射获取对象的所有属性名称
		String[] propEle = ReflectionUtils.getDeclaredFields(CotElementsNew.class);
		String hql = "from CotElementsNew obj where obj.eleId in (" + eleIds.substring(0, eleIds.length() - 1) + ")";
		List<?> listEle = this.getCotBaseDao().find(hql);
		List<CotGivenDetail> listDetail = new ArrayList<CotGivenDetail>();
		Iterator<?> it = listEle.iterator();
		while (it.hasNext()) {
			CotElementsNew elements = (CotElementsNew) it.next();
			CotGivenDetail detail = new CotGivenDetail();
			for (int i = 0; i < propEle.length; i++) {
				try {
					String value = BeanUtils.getProperty(elements, propEle[i]);
					if ("cotPictures".equals(propEle[i]) || "cotFile".equals(propEle[i]) || "childs".equals(propEle[i]) || "picImg".equals(propEle[i]) || "cotPriceFacs".equals(propEle[i]) || "cotElePrice".equals(propEle[i]) || "cotEleFittings".equals(propEle[i]) || "eleFittingPrice".equals(propEle[i]) || "cotElePacking".equals(propEle[i]) || "packingPrice".equals(propEle[i])) {
						continue;
					}
					if (value != null) {
						BeanUtils.setProperty(detail, propEle[i], BeanUtils.getProperty(elements, propEle[i]));
					}
				} catch (Exception e) {
					logger.error(propEle[i] + ":转换错误!");
				}
			}
			detail.setGivenId(mainId);
			detail.setEleAddTime(new java.sql.Date(System.currentTimeMillis()));// 添加时间
			detail.setAddTime(new java.sql.Date(System.currentTimeMillis()));// 添加时间
			listDetail.add(detail);
		}
		if (listEle.size() == 0) {
			CotGivenDetail detail = new CotGivenDetail();
			detail.setGivenId(mainId);
			detail.setEleAddTime(new java.sql.Date(System.currentTimeMillis()));// 添加时间
			detail.setAddTime(new java.sql.Date(System.currentTimeMillis()));// 添加时间
			listDetail.add(detail);
		}
		return listDetail;
	}

	// 判断该主单的明细是否已存在该产品货号
	@SuppressWarnings("unchecked")
	public boolean findIsExistEleId(Integer mainId, String eleId, Integer eId) {
		List<Integer> res = this.getCotBaseDao().find("select c.id from CotOrderDetail c where c.orderId = " + mainId + " and c.eleId='" + eleId + "'");
		if (res.size() == 0) {
			return false;
		} else if (res.size() == 1) {
			if (!res.get(0).toString().equals(eId.toString())) {
				return true;
			} else {
				return false;
			}
		} else {
			return true;
		}
	}

	// 根据货号获取样品图片对象(CotPicture)
	public CotPicture getCotPictureByEleId(String eleId) {
		String picName = eleId;
		List<?> list = this.getCotBaseDao().find("from CotPicture obj where obj.picName='" + picName + "'");
		if (list.size() > 0) {
			return (CotPicture) list.get(0);
		}
		return null;
	}

	// 根据货号获取样品对象
	public CotElementsNew getElementsByEleId(String eleId) {
		List<?> list = this.getCotBaseDao().find("from CotElementsNew obj where obj.eleId='" + eleId + "'");
		if (list.size() > 0) {
			return (CotElementsNew) list.get(0);
		}
		return null;
	}

	// 更新送样图片picImg字段
	public void updatePicImg(HttpServletRequest request, String filePath, Integer detailId) {

		List<CotGivenPic> imgList = new ArrayList<CotGivenPic>();
		CotOpImgService impOpService = (CotOpImgService) SystemUtil.getService("CotOpImgService");
		CotGivenPic cotElePic = impOpService.getGivenPic(detailId);
		File picFile = new File(filePath);
		FileInputStream in;
		// 获取操作人
		// WebContext ctx = WebContextFactory.get();
		// CotEmps cotEmps = (CotEmps) ctx.getSession().getAttribute("emp");
		CotEmps cotEmps = (CotEmps) request.getSession().getAttribute("emp");
		try {
			in = new FileInputStream(picFile);
			byte[] b = new byte[(int) picFile.length()];
			while (in.read(b) != -1) {
			}
			in.close();
			// cotGivenDetail.setPicImg(b);
			cotElePic.setPicImg(b);
			cotElePic.setPicSize(b.length);
			imgList.add(cotElePic);
			if (filePath.indexOf("common/images/zwtp.png") < 0) {
				picFile.delete();
			}
			// 修改样品图片
			impOpService.modifyImg(imgList);
			// 添加操作日志
			CotSyslog syslog = new CotSyslog();
			syslog.setEmpId(cotEmps.getId());
			syslog.setItemNo(detailId.toString());
			syslog.setOpMessage("修改送样明细图片成功");
			syslog.setOpModule("given");
			syslog.setOpTime(new Date(System.currentTimeMillis()));
			syslog.setOpType(2); // 1:添加 2：修改 3：删除
			List<CotSyslog> logList = new ArrayList<CotSyslog>();
			logList.add(syslog);
			sysLogService.addSysLog(logList);
			// this.getCotBaseDao().update(cotGivenDetail);
		} catch (Exception e) {
			logger.error("修改送样明细图片错误!");
			// 添加操作日志
			CotSyslog syslog = new CotSyslog();
			syslog.setEmpId(cotEmps.getId());
			syslog.setItemNo(detailId.toString());
			syslog.setOpMessage("修改送样明细图片失败，失败原因：" + e.getMessage());
			syslog.setOpModule("given");
			syslog.setOpTime(new Date(System.currentTimeMillis()));
			syslog.setOpType(2); // 1:添加 2：修改 3：删除
			List<CotSyslog> logList = new ArrayList<CotSyslog>();
			logList.add(syslog);
			sysLogService.addSysLog(logList);
		}
	}

	// 删除送样图片picImg
	public boolean deletePicImg(Integer detailId) {
		String classPath = CotGivenServiceImpl.class.getResource("/").toString();
		String systemPath = classPath.substring(6, classPath.length() - 16);
		String filePath = systemPath + "common/images/zwtp.png";
		// CotGivenDetail cotGivenDetail = this.getGivenDetailById(detailId);
		List<CotGivenPic> imgList = new ArrayList<CotGivenPic>();
		CotOpImgService impOpService = (CotOpImgService) SystemUtil.getService("CotOpImgService");
		CotGivenPic cotElePic = impOpService.getGivenPic(detailId);
		// String fileLength =
		// ContextUtil.getProperty("sysconfig.properties","maxLength");
		File picFile = new File(filePath);
		FileInputStream in;
		// 获取操作人
		WebContext ctx = WebContextFactory.get();
		CotEmps cotEmps = (CotEmps) ctx.getSession().getAttribute("emp");
		try {
			in = new FileInputStream(picFile);
			byte[] b = new byte[(int) picFile.length()];
			while (in.read(b) != -1) {
			}
			in.close();
			cotElePic.setPicImg(b);
			cotElePic.setPicSize(b.length);
			imgList.add(cotElePic);
			impOpService.modifyImg(imgList);
			// 添加操作日志
			CotSyslog syslog = new CotSyslog();
			syslog.setEmpId(cotEmps.getId());
			syslog.setItemNo(detailId.toString());
			syslog.setOpMessage("删除送样明细图片成功");
			syslog.setOpModule("given");
			syslog.setOpTime(new Date(System.currentTimeMillis()));
			syslog.setOpType(3); // 1:添加 2：修改 3：删除
			List<CotSyslog> logList = new ArrayList<CotSyslog>();
			logList.add(syslog);
			sysLogService.addSysLog(logList);
			return true;

		} catch (Exception e) {
			e.printStackTrace();
			logger.error("删除送样明细图片错误!");
			// 添加操作日志
			CotSyslog syslog = new CotSyslog();
			syslog.setEmpId(cotEmps.getId());
			syslog.setItemNo(detailId.toString());
			syslog.setOpMessage("删除送样明细图片失败，失败原因：" + e.getMessage());
			syslog.setOpModule("given");
			syslog.setOpTime(new Date(System.currentTimeMillis()));
			syslog.setOpType(3); // 1:添加 2：修改 3：删除
			List<CotSyslog> logList = new ArrayList<CotSyslog>();
			logList.add(syslog);
			sysLogService.addSysLog(logList);
			return false;
		}
	}

	// 得到objName的集合
	public List<?> getDicList(String objName) {
		return systemDicUtil.getDicListByName(objName);
	}

	// 查询新增寄样明细的工厂是否已存在
	public boolean checkIsFactory(Integer factoryId, Integer givenId) {
		String hql = " from CotSign obj where obj.givenId =" + givenId + " and obj.factoryId=" + factoryId;
		List<?> res = this.getCotBaseDao().find(hql);
		if (res.size() > 0) {
			return false;
		}
		return true;
	}

	// 重新分解判断
	public boolean checkIsNew(Integer factoryId, Integer givenId) {
		String hql = " from CotGivenDetail obj where obj.factoryId =" + factoryId + " and obj.givenId =" + givenId + " and (obj.signFlag=0 or obj.signFlag is null)";
		List<?> res = this.getCotBaseDao().find(hql);
		if (res.size() > 0) {
			return true;
		} else {
			return false;
		}
	}

	// 分解生成主征样单
	public boolean saveSign(String[] factoryIdAry, Integer givenId) {

		SimpleDateFormat mdf = new SimpleDateFormat("yy-MM-dd-mmss");
		// 通过givenId获取主寄样单对象
		CotGiven cotGiven = this.getGivenById(givenId);
		String givenNo = cotGiven.getGivenNo(); // 主寄样单编号
		Integer custId = cotGiven.getCustId(); // 客户
		Integer bussinessPerson = cotGiven.getBussinessPerson(); // 业务员

		// 获取登陆员工
		WebContext ctx = WebContextFactory.get();
		CotEmps cotEmps = (CotEmps) ctx.getSession().getAttribute("emp");
		// 获取当前时间
		Date addTime = new Date(System.currentTimeMillis());

		// 用HashSet过滤factoryIdAry中的相同厂家id
		HashSet<String> set = new HashSet<String>();
		set.addAll(Arrays.asList(factoryIdAry));
		String[] factoryIdAry2 = (String[]) set.toArray(new String[0]);

		for (int i = 0; i < factoryIdAry2.length; i++) {
			if (factoryIdAry2[i].equals(""))
				continue;
			Integer factoryId = Integer.parseInt(factoryIdAry2[i]);

			// 重新分解判断
			boolean flag = this.checkIsNew(factoryId, givenId);
			if (flag == true) {
				// 判断是否需要重新生成征样单号
				boolean flg = this.checkIsFactory(factoryId, givenId);
				if (flg == true) {
					String currDate = ContextUtil.getCurrentDate("yyyy-MM-dd");
					// String signNo = noService.getSignNo(givenNo, factoryId,
					// currDate);

					String signNo = cotSeqService.getSignNo(custId, bussinessPerson, factoryId, currDate);
					// String signNo = givenNo + "-" + (i + 1);
					// String signNo = mdf.format(addTime);
					try {
						CotSign cotSign = new CotSign();

						cotSign.setSignTime(new Date(System.currentTimeMillis()));// 征样完成日期
						cotSign.setGivenId(givenId); // 主寄样单id
						cotSign.setGivenNo(givenNo); // 主寄样单编号
						cotSign.setFactoryId(factoryId); // 厂家
						CotFactory factory = this.getFactoryById(factoryId);
						cotSign.setContactPerson(factory.getContactPerson());// 厂家联系人
						cotSign.setGivenAddr(cotGiven.getGivenAddr());// 送样地点
						// TODO:单号修正
						cotSign.setSignNo(signNo);// 征样单编号
						cotSign.setBussinessPerson(bussinessPerson); // 业务员
						cotSign.setCustId(custId);// 客户
						cotSign.setCustTime(cotGiven.getCustRequiretime());// 客户要样日期
						cotSign.setAddTime(addTime); // 添加时间
						cotSign.setEmpId(cotEmps.getId()); // 制单人ID
						cotSign.setAddPerson(cotEmps.getEmpsName());// 制单人
						// 审核状态 0:(未审核),1(已审核)通过,2(审核不通过)
						cotSign.setSignStatus(new Long(0));// 审核状态
						cotSign.setSignIscheck(new Long(0));// 是否需要审核
						// 是否需要审核 0:不需要审核 1:需要审核 （默认0）
						List<CotSign> records = new ArrayList<CotSign>();
						records.add(cotSign);
						this.getCotBaseDao().saveRecords(records);
						// 修改寄样明细的征样是否完成标志
						this.modifySignFlag(factoryId, givenId);
						// 更新征样序列号
						this.cotSeqService.saveSeq("sign");

						// 添加操作日志
						CotSyslog syslog = new CotSyslog();
						syslog.setEmpId(cotEmps.getId());
						syslog.setItemNo(cotSign.getSignNo());
						syslog.setOpMessage("分解寄样单添加到征样单成功");
						syslog.setOpModule("sign");
						syslog.setOpTime(new Date(System.currentTimeMillis()));
						syslog.setOpType(1); // 1:添加 2：修改 3：删除
						List<CotSyslog> logList = new ArrayList<CotSyslog>();
						logList.add(syslog);
						sysLogService.addSysLog(logList);

					} catch (Exception e) {
						logger.error("添加主征样单出错！");
						// 添加操作日志
						CotSyslog syslog = new CotSyslog();
						syslog.setEmpId(cotEmps.getId());
						syslog.setOpMessage("分解寄样单添加到征样单明细失败，失败原因：" + e.getMessage());
						syslog.setOpModule("sign");
						syslog.setOpTime(new Date(System.currentTimeMillis()));
						syslog.setOpType(1); // 1:添加 2：修改 3：删除
						List<CotSyslog> logList = new ArrayList<CotSyslog>();
						logList.add(syslog);
						sysLogService.addSysLog(logList);
						return false;
					}
				} else {
					// 修改寄样明细的征样是否完成标志
					this.modifySignFlag(factoryId, givenId);
				}
			}
		}
		return true;
	}

	// 修改寄样明细分解状态
	public void modifySignFlag(Integer factoryId, Integer givenId) {

		List<CotGivenDetail> records = new ArrayList<CotGivenDetail>();
		// 通过givenId获取主寄样单对象
		String hql = " from CotGivenDetail obj where obj.givenId=" + givenId + " and obj.factoryId =" + factoryId;
		List<?> res = this.getCotBaseDao().find(hql);
		for (int i = 0; i < res.size(); i++) {
			CotGivenDetail givenDetail = (CotGivenDetail) res.get(i);
			givenDetail.setSignFlag(1);

			records.add(givenDetail);
		}

		this.getCotBaseDao().updateRecords(records);
	}

	// 修改寄样主单分解状态
	public void modifyGivenStatus(Integer givenId, String flag) {

		// 通过givenId获取主寄样单对象
		CotGiven cotGiven = this.getGivenById(givenId);
		if ("new".equals(flag)) {
			cotGiven.setGivenStatus(0L);
		}
		if ("separate".equals(flag)) {
			cotGiven.setGivenStatus(1L);
		}
		if ("reseparate".equals(flag)) {
			cotGiven.setGivenStatus(2L);
		}
		cotGiven.setCotSign(null);
		cotGiven.setCotGivenDetails(null);
		List<CotGiven> givenList = new ArrayList<CotGiven>();
		givenList.add(cotGiven);

		this.getCotBaseDao().updateRecords(givenList);
	}

	// 根据送样编号查找征样单
	public List<?> getSignByGivenId(Integer givenId) {

		List<?> signList = this.getCotBaseDao().find("from CotSign obj where obj.givenId=" + givenId);
		if (signList.size() > 0) {
			return signList;
		}
		return null;
	}

	public CotSign getSignById(Integer id) {
		CotSign cotSign = (CotSign) this.getCotBaseDao().getById(CotSign.class, id);
		if (cotSign != null) {
			return cotSign;
		}
		return null;
	}

	// 批量删除征样单
	@SuppressWarnings( { "deprecation", "unchecked" })
	public Boolean deleteSignList(List<CotSign> signList) {
		// 获取操作人
		WebContext ctx = WebContextFactory.get();
		CotEmps cotEmps = (CotEmps) ctx.getSession().getAttribute("emp");
		List<Integer> ids = new ArrayList<Integer>();

		for (int i = 0; i < signList.size(); i++) {
			CotSign cotSign = (CotSign) signList.get(i);
			Integer signId = cotSign.getId();

			CotSign sign = (CotSign) this.getSignById(signId);
			this.modifySignFlagForDel(sign.getFactoryId(), sign.getGivenId());

			ids.add(signId);
		}
		try {
			this.getCotBaseDao().deleteRecordByIds(ids, "CotSign");
			// 添加操作日志
			CotSyslog syslog = new CotSyslog();
			syslog.setEmpId(cotEmps.getId());
			syslog.setOpMessage("批量删除主征样单成功");
			syslog.setOpModule("sign");
			syslog.setOpTime(new Date(System.currentTimeMillis()));
			syslog.setOpType(3); // 1:添加 2：修改 3：删除
			List<CotSyslog> logList = new ArrayList<CotSyslog>();
			logList.add(syslog);
			sysLogService.addSysLog(logList);
			return true;

		} catch (DAOException e) {
			logger.error("删除主征样单出错");
			// 添加操作日志
			CotSyslog syslog = new CotSyslog();
			syslog.setEmpId(cotEmps.getId());
			syslog.setOpMessage("批量删除主征样单失败，失败原因：" + e.getMessage());
			syslog.setOpModule("sign");
			syslog.setOpTime(new Date(System.currentTimeMillis()));
			syslog.setOpType(3); // 1:添加 2：修改 3：删除
			List<CotSyslog> logList = new ArrayList<CotSyslog>();
			logList.add(syslog);
			sysLogService.addSysLog(logList);
			return false;
		}

	}

	// 删除征样主单时修改寄样明细分解状态
	public void modifySignFlagForDel(Integer factoryId, Integer givenId) {

		List<CotGivenDetail> records = new ArrayList<CotGivenDetail>();
		// 通过givenId获取主寄样单对象
		String hql = " from CotGivenDetail obj where obj.givenId=" + givenId + " and obj.factoryId =" + factoryId;
		List<?> res = this.getCotBaseDao().find(hql);
		for (int i = 0; i < res.size(); i++) {
			CotGivenDetail givenDetail = (CotGivenDetail) res.get(i);
			givenDetail.setSignFlag(0);

			records.add(givenDetail);
		}

		this.getCotBaseDao().updateRecords(records);
	}

	// 计算寄样单样品总费用
	public Float calSampleFee(Integer givenId) {

		Float fee = 0.0f;

		String hql = " from CotGivenDetail obj where obj.givenId =" + givenId;
		List<?> res = this.getCotBaseDao().find(hql);
		if (res.size() > 0) {
			for (int i = 0; i < res.size(); i++) {
				CotGivenDetail givenDetail = (CotGivenDetail) res.get(i);
				if (givenDetail.getTotalMoney() != null) {
					fee += givenDetail.getTotalMoney();
				}
			}
		}
		return fee;
	}

	// 删除征样明细
	public boolean deleteSignDetail(Integer id) {
		List<CotGivenDetail> records = new ArrayList<CotGivenDetail>();
		CotGivenDetail givenDetail = this.getGivenDetailById(id);
		if (givenDetail != null) {
			givenDetail.setSignFlag(0);
			givenDetail.setSignCount(givenDetail.getGivenCount());
			records.add(givenDetail);

			this.getCotBaseDao().updateRecords(records);

			return true;
		}
		return false;
	}

	// 根据主寄样单号检索所有明细记录的征样状态
	public Integer getChangeNum(Integer gid) {
		String hql = " from CotGivenDetail obj where obj.givenId=" + gid + " and (obj.signFlag = 0 or obj.signFlag is null)";
		List<?> res = this.getCotBaseDao().find(hql);
		if (res.size() != 0) {
			return res.size();
		} else {
			return 0;
		}
	}

	// 根据文件路径和行号导入excel
	public List<?> updateOneReport(String filename, Integer rowNum, Integer givenId, boolean isCover) {
		// 选取Excel文件
		Workbook workbook = null;
		// 定义成功条数
		int successNum = 0;
		List<CotMsgVO> msgList = new ArrayList<CotMsgVO>();
		// 格式化样品包装数据
		DecimalFormat df = new DecimalFormat("#.####");
		DecimalFormat dfTwo = new DecimalFormat("#.##");
		// 当前时间
		Date now = new java.sql.Date(System.currentTimeMillis());
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		// 取得样品默认配置对象
		CotEleCfg cotEleCfg = this.getReportDao().getDefaultEleCfg();

		// 先查询所有订单明细的货号
		List<?> detailEles = this.getReportDao().getGivenDetails(givenId);
		List<CotGivenDetail> detailElesBak = new ArrayList();
		for (int i = 0; i < detailEles.size(); i++) {
			CotGivenDetail det = (CotGivenDetail) detailEles.get(i);
			detailElesBak.add(det);
		}

		// 取得默认的20/40/40HQ/45的柜体积,数据字典没设置的话默认为24/54/68/86)
		WebContext ctx = WebContextFactory.get();
		Float[] cubes = this.getReportDao().getContainerCube();
		// 查询所有包装材料
		Map boxPackMap = this.getReportDao().getAllBoxPacking();
		// 查询所有样品材质(中文KEY)
		Map<String, Integer> mapTypeCn = this.getReportDao().getAllTypeCn();
		// 查询所有样品材质(英文KEY)
		Map<String, Integer> mapTypeEn = this.getReportDao().getAllTypeEn();
		// 查询所有包装类型(中文KEY)
		Map<String, String[]> mapBoxTypeCn = this.getReportDao().getAllBoxTypeByCn();
		// 查询所有包装类型(英文KEY)
		Map<String, String[]> mapBoxTypeEn = this.getReportDao().getAllBoxTypeByEn();
		// 获得tomcat路径
		String classPath = CotReportService.class.getResource("/").toString();
		String systemPath = classPath.substring(6, classPath.length() - 16);
		File file = new File(systemPath + "upload/" + filename);
		try {
			// 设置本地时间格式
			WorkbookSettings setting = new WorkbookSettings();
			java.util.Locale locale = new java.util.Locale("zh", "CN");
			setting.setEncoding("ISO-8859-1");
			setting.setLocale(locale);
			workbook = Workbook.getWorkbook(file, setting);
		} catch (Exception e) {
			file.delete();
			return null;
		}
		// 通过Workbook的getSheet方法选择第一个工作簿（从0开始）
		Sheet sheet = workbook.getSheet(0);
		int i = rowNum;
		// 新建样品对象
		CotGivenDetail detail = new CotGivenDetail();
		// 定义厂家
		CotFactory cotFactory = new CotFactory();
		// 是否有成功标识
		boolean isSuccess = true;
		// 用于计算CBM和CUFT
		float boxObL = 0f;
		float boxObW = 0f;
		float boxObH = 0f;
		// 产品长宽高
		float boxL = 0f;
		float boxW = 0f;
		float boxH = 0f;
		float boxLInch = 0f;
		float boxWInch = 0f;
		float boxHInch = 0f;

		// 单重
		float boxWeigth = 0f;
		float gWeigh = 0f;
		float nWeigh = 0f;

		// 厂价
		Float priceFac = 0f;
		// 外销价
		Float priceOut = 0f;

		String eleSize = "";
		String eleSizeInch = "";
		boolean isChangeL = false;
		// 判断是否有删除最后一行
		boolean isDel = false;
		// 用于计算装箱数
		long boxObCount = 0;
		Integer hsIdTemp = 0;
		float tuiLv = 0f;
		ctx.getSession().removeAttribute("orderReport");
		TreeMap<String, CotGivenDetail> mapObj = new TreeMap<String, CotGivenDetail>();
		boolean rowChk = false;
		for (int j = 0; j < sheet.getColumns(); j++) {
			// 表头
			Cell headCtn = sheet.getCell(j, 1);
			Cell row = null;
			try {
				row = sheet.getCell(j, i);
			} catch (Exception e) {
				isDel = true;
				isSuccess = false;
				break;
			}

			String rowCtn = row.getContents();
			// 如果没转换成数字cell,默认的最长小数位是3位
			if (row.getType() == CellType.NUMBER || row.getType() == CellType.NUMBER_FORMULA) {
				NumberCell nc = (NumberCell) row;
				rowCtn = df.format(nc.getValue());
			}
			if (headCtn.getContents().equals("ELE_ID")) {
				if (rowCtn == null || rowCtn.trim().equals("")) {
					CotMsgVO cotMsgVO = new CotMsgVO(i, j, "Article No. can not be empty");
					msgList.add(cotMsgVO);
					isSuccess = false;
					break;
				}
				if (rowCtn.trim().getBytes().length > 100) {
					CotMsgVO cotMsgVO = new CotMsgVO(i, j, "The length of Article No. can not exceed 100");

					msgList.add(cotMsgVO);
					isSuccess = false;
					break;
				}
				if (isCover == true) {
					boolean ck = false;
					for (int k = 0; k < detailElesBak.size(); k++) {
						CotGivenDetail givenDetail = (CotGivenDetail) detailElesBak.get(k);
						if (rowCtn.trim().toLowerCase().equals(givenDetail.getEleId().toLowerCase())) {
							ck = true;
							rowChk = true;
							detail = givenDetail;
							detailElesBak.remove(givenDetail);
							break;
						}
					}
					if (ck == false && rowChk == false) {
						rowChk = false;
						detail = new CotGivenDetail();
						detail.setEleId(rowCtn.trim().toUpperCase());
						if (cotEleCfg != null) {
							// 设置样品默认值
							detail = this.getReportDao().setGivenDefault(cotEleCfg, detail);
						}
					}
					if (ck == false && rowChk == true) {
						isSuccess = false;
						rowChk = false;
						break;
					}
				} else {
					detail = new CotGivenDetail();
					detail.setEleId(rowCtn.trim().toUpperCase());
					if (cotEleCfg != null) {
						// 设置样品默认值
						detail = this.getReportDao().setGivenDefault(cotEleCfg, detail);
					}
				}
				if (detail.getBoxObCount() != null) {
					boxObCount = detail.getBoxObCount();
				}
				// 处理中英文规格
				if (detail.getBoxL() != null) {
					boxL = detail.getBoxL();
				}
				if (detail.getBoxW() != null) {
					boxW = detail.getBoxW();
				}
				if (detail.getBoxH() != null) {
					boxH = detail.getBoxH();
				}
				if (detail.getBoxLInch() != null) {
					boxLInch = detail.getBoxLInch();
				}
				if (detail.getBoxWInch() != null) {
					boxWInch = detail.getBoxWInch();
				}
				if (detail.getBoxHInch() != null) {
					boxHInch = detail.getBoxHInch();
				}
			}
			if (headCtn.getContents().equals("HS_ID") && rowCtn != null && !rowCtn.trim().equals("")) {
				Integer flag = this.getReportDao().isExistHsId(rowCtn.trim());
				if (flag == null) {
					CotEleOther cotEleOther = new CotEleOther();
					cotEleOther.setHscode(rowCtn.trim());
					cotEleOther.setCnName(rowCtn.trim());
					if (tuiLv != 0) {
						cotEleOther.setTaxRate(tuiLv);
					}
					try {
						this.getReportDao().create(cotEleOther);
						detail.setEleHsid(cotEleOther.getId());
						hsIdTemp = cotEleOther.getId();
					} catch (Exception e) {
						CotMsgVO cotMsgVO = new CotMsgVO(i, j, "保存海关编码异常");
						msgList.add(cotMsgVO);
						isSuccess = false;
						break;
					}
				} else {
					detail.setEleHsid(flag);
					hsIdTemp = flag;
				}
			}

			if (headCtn.getContents().equals("ELE_NAME") && rowCtn != null && !rowCtn.trim().equals("")) {
				detail.setEleName(rowCtn.trim());
			}
			if (headCtn.getContents().equals("SIGN_REQUIRE") && rowCtn != null && !rowCtn.trim().equals("")) {
				detail.setSignRequire(rowCtn.trim());
			}
			if (headCtn.getContents().equals("SIGN_COUNT")) {
				if (rowCtn != null && !rowCtn.trim().equals("")) {
					detail.setSignCount(Integer.parseInt(rowCtn.trim()));
				}else{
					detail.setSignCount(1);
				}
			}
			if (headCtn.getContents().equals("GIVEN_COUNT")) {
				if (rowCtn != null && !rowCtn.trim().equals("")) {
					detail.setGivenCount(Integer.parseInt(rowCtn.trim()));
				}else{
					detail.setGivenCount(1);
				}
			}
			if (headCtn.getContents().equals("ELE_TYPENAME_LV2") && rowCtn != null && !rowCtn.trim().equals("")) {
				detail.setEleTypenameLv2(rowCtn.trim());
			}

			if (headCtn.getContents().equals("ELE_NAME_EN") && rowCtn != null && !rowCtn.trim().equals("")) {
				detail.setEleNameEn(rowCtn.trim());
			}
			if (headCtn.getContents().equals("ELE_FLAG")) {
				if (rowCtn != null && !rowCtn.trim().equals("")) {
					if (detail.getEleFlag() == null || detail.getEleFlag() != 2) {
						if (rowCtn.trim().equals("套件")) {
							detail.setEleFlag(1l);
						}
						if (rowCtn.trim().equals("单件")) {
							detail.setEleFlag(0l);
						}
						if (rowCtn.trim().equals("组件")) {
							detail.setEleFlag(3l);
						}
					}
				}
			}
			if (headCtn.getContents().equals("ELE_PARENT") && rowCtn != null && !rowCtn.trim().equals("")) {
				Integer flag = this.getReportDao().isExistEleId(rowCtn.trim().toLowerCase());
				if (flag == null) {
					CotMsgVO cotMsgVO = new CotMsgVO(i, j, "Parent Article No. does not exist!");
					msgList.add(cotMsgVO);
					isSuccess = false;
					break;
				} else {
					detail.setEleParentId(flag);
					detail.setEleParent(rowCtn.trim());
					detail.setEleFlag(2l);
				}
			}
			if (headCtn.getContents().equals("CUST_NO") && rowCtn != null && !rowCtn.trim().equals("")) {
				detail.setCustNo(rowCtn.trim());
			}
			if (headCtn.getContents().equals("FACTORY_NO") && rowCtn != null && !rowCtn.trim().equals("")) {
				detail.setFactoryNo(rowCtn.trim());
			}
			if (headCtn.getContents().equals("ELE_DESC") && rowCtn != null && !rowCtn.trim().equals("")) {
				detail.setEleDesc(rowCtn.trim());
			}
			if (headCtn.getContents().equals("ELE_FROM") && rowCtn != null && !rowCtn.trim().equals("")) {
				detail.setEleFrom(rowCtn.trim());
			}
			if (headCtn.getContents().equals("ELE_FACTORY") && rowCtn != null && !rowCtn.trim().equals("")) {
				detail.setEleFactory(rowCtn.trim());
			}
			if (headCtn.getContents().equals("ELE_COL") && rowCtn != null && !rowCtn.trim().equals("")) {
				detail.setEleCol(rowCtn.trim());
			}
			if (headCtn.getContents().equals("BARCODE") && rowCtn != null && !rowCtn.trim().equals("")) {
				if (rowCtn.trim().length() > 30) {
					detail.setBarcode(rowCtn.trim().substring(0, 30));
				} else {
					detail.setBarcode(rowCtn.trim());
				}
			}
			if (headCtn.getContents().equals("ELE_COMPOSETYPE") && rowCtn != null && !rowCtn.trim().equals("")) {
				detail.setEleComposeType(rowCtn.trim());
			}
			if (headCtn.getContents().equals("ELE_UNIT")) {
				if (rowCtn != null && !rowCtn.trim().equals("")) {
					detail.setEleUnit(rowCtn.trim());
				}
			}
			if (headCtn.getContents().equals("ELE_GRADE") && rowCtn != null && !rowCtn.trim().equals("")) {
				detail.setEleGrade(rowCtn.trim());
			}
			if (headCtn.getContents().equals("ELE_FOR_PERSON") && rowCtn != null && !rowCtn.trim().equals("")) {
				detail.setEleForPerson(rowCtn.trim());
			}
			if (headCtn.getContents().equals("ELE_PRO_TIME") && rowCtn != null && !rowCtn.trim().equals("")) {
				if (row.getType() == CellType.DATE) {
					DateCell dc = (DateCell) row;
					detail.setEleProTime(new java.sql.Date(dc.getDate().getTime()));
				} else {
					try {
						java.util.Date date = sdf.parse(rowCtn.trim());
						detail.setEleProTime(new java.sql.Date(date.getTime()));
					} catch (ParseException e) {
						CotMsgVO cotMsgVO = new CotMsgVO(i, j, "Date format is incorrect");
						msgList.add(cotMsgVO);
						isSuccess = false;
						break;
					}
				}
			}
			if (headCtn.getContents().equals("ELE_SIZE_DESC") && rowCtn != null && !rowCtn.trim().equals("")) {
				eleSize = rowCtn.trim();
			}
			if (headCtn.getContents().equals("TAO_UNIT") && rowCtn != null && !rowCtn.trim().equals("")) {
				detail.setTaoUnit(rowCtn.trim());
			}
			if (headCtn.getContents().equals("ELE_INCH_DESC") && rowCtn != null && !rowCtn.trim().equals("")) {
				eleSizeInch = rowCtn.trim();
			}
			if (headCtn.getContents().equals("ELE_REMARK") && rowCtn != null && !rowCtn.trim().equals("")) {
				if (rowCtn.trim().length() > 500) {
					detail.setEleRemark(rowCtn.trim().substring(0, 500));
				} else {
					detail.setEleRemark(rowCtn.trim());
				}
			}
			if (headCtn.getContents().equals("ELE_TYPENAME_LV1")) {
				if (rowCtn != null && !rowCtn.trim().equals("")) {
					// 去掉厂家名称中的回车换行
					String t = rowCtn.trim().replaceAll("\r", "");
					String temp = t.replaceAll("\n", "");

					Integer typeId = mapTypeCn.get(temp.toLowerCase());
					if (typeId == null) {
						typeId = mapTypeEn.get(temp.toLowerCase());
					}
					if (typeId == null) {
						// 新增一条材质,中英文名相同
						CotTypeLv1 cotTypeLv1 = new CotTypeLv1();
						cotTypeLv1.setTypeName(temp);
						cotTypeLv1.setTypeEnName(temp);
						List<CotTypeLv1> listType = new ArrayList<CotTypeLv1>();
						listType.add(cotTypeLv1);
						try {
							this.getReportDao().saveRecords(listType);
							typeId = cotTypeLv1.getId();
							mapTypeCn.put(temp.toLowerCase(), typeId);
						} catch (DAOException e) {
							e.printStackTrace();
							CotMsgVO cotMsgVO = new CotMsgVO(i, j, "保存样品材质异常!");
							msgList.add(cotMsgVO);
							isSuccess = false;
							break;
						}
					}
					detail.setEleTypenameLv1(temp);
					detail.setEleTypeidLv1(typeId);
				}
			}

			if (headCtn.getContents().equals("ELE_TYPEID_LV2")) {
				if (rowCtn != null && !rowCtn.trim().equals("")) {
					Integer flag = this.getReportDao().isExistEleTypeName(rowCtn.trim());
					CotTypeLv2 cotTypeLv2 = null;
					if (flag == null) {
						cotTypeLv2 = new CotTypeLv2();
						cotTypeLv2.setTypeName(rowCtn.trim());
						try {
							this.getReportDao().create(cotTypeLv2);
						} catch (Exception e) {
							CotMsgVO cotMsgVO = new CotMsgVO(i, j, "保存产品分类异常");
							msgList.add(cotMsgVO);
							isSuccess = false;
							break;
						}
					} else {
						cotTypeLv2 = (CotTypeLv2) this.getReportDao().getById(CotTypeLv2.class, flag);
					}
					detail.setEleTypeidLv2(cotTypeLv2.getId());
				}
			}

			if (headCtn.getContents().equals("SHORT_NAME")) {
				if (rowCtn != null && !rowCtn.trim().equals("")) {
					// 去掉厂家名称中的回车换行
					String t = rowCtn.trim().replaceAll("\r", "");
					String temp = t.replaceAll("\n", "");
					Integer factoryFlag = this.getReportDao().isExistFactoryShortName(temp);
					Integer fId = 0;
					if (factoryFlag == null) {
						cotFactory.setFactoryName(temp);
						cotFactory.setShortName(temp);
						cotFactory.setFactroyTypeidLv1(1);
						try {
							// String facNo = seq.getAllSeqByType("facNo",
							// null);
							// cotFactory.setFactoryNo(facNo);
							this.getReportDao().create(cotFactory);
							// seq.saveSeq("facNo");
							cotSeqService.saveSeq("factoryNo");

						} catch (Exception e) {
							CotMsgVO cotMsgVO = new CotMsgVO(i, j, "Save Supplier exception");
							msgList.add(cotMsgVO);
							isSuccess = false;
							break;
						}
						fId = cotFactory.getId();
					} else {
						fId = factoryFlag;
					}
					detail.setFactoryId(fId);
					detail.setFactoryShortName(temp);
				}

			}
			// -----------------------------包装信息
			try {
				if (headCtn.getContents().equals("ELE_MOD") && rowCtn != null && !rowCtn.trim().equals("")) {
					Double elemod = Double.parseDouble(rowCtn.trim());
					detail.setEleMod(elemod.intValue());
				}
				if (headCtn.getContents().equals("ELE_UNITNUM")) {
					if (rowCtn != null && !rowCtn.trim().equals("")) {
						Double elemod = Double.parseDouble(rowCtn.trim());
						int numEle = elemod.intValue();
						if (numEle != 0) {
							detail.setEleFlag(1l);
						}
						detail.setEleUnitNum(numEle);
					}
				}
				if (headCtn.getContents().equals("cube") && rowCtn != null && !rowCtn.trim().equals("")) {
					detail.setCube(Float.parseFloat(rowCtn.trim()));
				}
				if (headCtn.getContents().equals("BOX_L") && rowCtn != null && !rowCtn.trim().equals("")) {
					isChangeL = true;
					float num = Float.parseFloat(rowCtn.trim());
					int temp = rowCtn.trim().lastIndexOf(".");
					if (temp > -1) {
						boxL = Float.parseFloat(dfTwo.format(num));
					} else {
						boxL = num;
					}
					boxLInch = Float.parseFloat(dfTwo.format(num / 2.54f));
					detail.setBoxL(boxL);
					detail.setBoxLInch(boxLInch);
				}
				if (headCtn.getContents().equals("BOX_L_INCH") && rowCtn != null && !rowCtn.trim().equals("")) {
					isChangeL = true;
					float num = Float.parseFloat(rowCtn.trim());
					int temp = rowCtn.trim().lastIndexOf(".");
					if (temp > -1) {
						boxLInch = Float.parseFloat(dfTwo.format(num));
					} else {
						boxLInch = num;
					}
					boxL = Float.parseFloat(dfTwo.format(num * 2.54f));
					detail.setBoxL(boxL);
					detail.setBoxLInch(boxLInch);

				}
				if (headCtn.getContents().equals("BOX_W") && rowCtn != null && !rowCtn.trim().equals("")) {
					isChangeL = true;
					float num = Float.parseFloat(rowCtn.trim());
					int temp = rowCtn.trim().lastIndexOf(".");
					if (temp > -1) {
						boxW = Float.parseFloat(dfTwo.format(num));
					} else {
						boxW = num;
					}
					boxWInch = Float.parseFloat(dfTwo.format(num / 2.54f));
					detail.setBoxW(boxW);
					detail.setBoxWInch(boxWInch);
				}
				if (headCtn.getContents().equals("BOX_W_INCH") && rowCtn != null && !rowCtn.trim().equals("")) {
					isChangeL = true;
					float num = Float.parseFloat(rowCtn.trim());
					int temp = rowCtn.trim().lastIndexOf(".");
					if (temp > -1) {
						boxWInch = Float.parseFloat(dfTwo.format(num));
					} else {
						boxWInch = num;
					}
					boxW = Float.parseFloat(dfTwo.format(num * 2.54f));
					detail.setBoxW(boxW);
					detail.setBoxWInch(boxWInch);
				}
				if (headCtn.getContents().equals("BOX_H") && rowCtn != null && !rowCtn.trim().equals("")) {
					isChangeL = true;
					float num = Float.parseFloat(rowCtn.trim());
					int temp = rowCtn.trim().lastIndexOf(".");
					if (temp > -1) {
						boxH = Float.parseFloat(dfTwo.format(num));
					} else {
						boxH = num;
					}
					boxHInch = Float.parseFloat(dfTwo.format(num / 2.54f));
					detail.setBoxH(boxH);
					detail.setBoxHInch(boxHInch);
				}
				if (headCtn.getContents().equals("BOX_H_INCH") && rowCtn != null && !rowCtn.trim().equals("")) {
					isChangeL = true;
					float num = Float.parseFloat(rowCtn.trim());
					int temp = rowCtn.trim().lastIndexOf(".");
					if (temp > -1) {
						boxHInch = Float.parseFloat(dfTwo.format(num));
					} else {
						boxHInch = num;
					}
					boxH = Float.parseFloat(dfTwo.format(num * 2.54f));
					detail.setBoxH(boxH);
					detail.setBoxHInch(boxHInch);
				}
				if (headCtn.getContents().equals("BOX_HANDLEH") && rowCtn != null && !rowCtn.trim().equals("")) {
					detail.setBoxHandleH(Float.parseFloat(rowCtn.trim()));
				}
				if (headCtn.getContents().equals("BOX_PB_L") && rowCtn != null && !rowCtn.trim().equals("")) {
					detail.setBoxPbL(Float.parseFloat(rowCtn.trim()));
					detail.setBoxPbLInch(Float.parseFloat(rowCtn.trim()) / 2.54f);
				}

				if (headCtn.getContents().equals("BOX_PB_W") && rowCtn != null && !rowCtn.trim().equals("")) {
					detail.setBoxPbW(Float.parseFloat(rowCtn.trim()));
					detail.setBoxPbWInch(Float.parseFloat(rowCtn.trim()) / 2.54f);
				}

				if (headCtn.getContents().equals("BOX_PB_H") && rowCtn != null && !rowCtn.trim().equals("")) {
					detail.setBoxPbH(Float.parseFloat(rowCtn.trim()));
					detail.setBoxPbHInch(Float.parseFloat(rowCtn.trim()) / 2.54f);
				}
				if (headCtn.getContents().equals("BOX_PB_COUNT")) {
					if (rowCtn != null && !rowCtn.trim().equals("")) {
						Double elemod = Double.parseDouble(rowCtn.trim());
						detail.setBoxPbCount(elemod.longValue());
					}
				}
				if (headCtn.getContents().equals("BOX_IB_L") && rowCtn != null && !rowCtn.trim().equals("")) {
					detail.setBoxIbL(Float.parseFloat(rowCtn.trim()));
					detail.setBoxIbLInch(Float.parseFloat(rowCtn.trim()) / 2.54f);
				}

				if (headCtn.getContents().equals("BOX_IB_W") && rowCtn != null && !rowCtn.trim().equals("")) {
					detail.setBoxIbW(Float.parseFloat(rowCtn.trim()));
					detail.setBoxIbWInch(Float.parseFloat(rowCtn.trim()) / 2.54f);
				}

				if (headCtn.getContents().equals("BOX_IB_H") && rowCtn != null && !rowCtn.trim().equals("")) {
					detail.setBoxIbH(Float.parseFloat(rowCtn.trim()));
					detail.setBoxIbHInch(Float.parseFloat(rowCtn.trim()) / 2.54f);
				}

				if (headCtn.getContents().equals("BOX_MB_L") && rowCtn != null && !rowCtn.trim().equals("")) {
					detail.setBoxMbL(Float.parseFloat(rowCtn.trim()));
					detail.setBoxMbLInch(Float.parseFloat(rowCtn.trim()) / 2.54f);
				}

				if (headCtn.getContents().equals("BOX_MB_W") && rowCtn != null && !rowCtn.trim().equals("")) {
					detail.setBoxMbW(Float.parseFloat(rowCtn.trim()));
					detail.setBoxMbWInch(Float.parseFloat(rowCtn.trim()) / 2.54f);
				}

				if (headCtn.getContents().equals("BOX_MB_H") && rowCtn != null && !rowCtn.trim().equals("")) {
					detail.setBoxMbH(Float.parseFloat(rowCtn.trim()));
					detail.setBoxMbHInch(Float.parseFloat(rowCtn.trim()) / 2.54f);
				}

				if (headCtn.getContents().equals("BOX_OB_L") && rowCtn != null && !rowCtn.trim().equals("")) {
					boxObL = Float.parseFloat(rowCtn.trim());
					detail.setBoxObL(boxObL);
					detail.setBoxObLInch(boxObL / 2.54f);

					float cbm = Float.parseFloat(df.format(boxObL * boxObW * boxObH * 0.000001F));
					float cuft = Float.parseFloat(df.format(cbm * 35.315f));
					detail.setBoxCbm(cbm);
					detail.setBoxCuft(cuft);
					// 外箱数不为0时计算装箱数,计算装箱数(20/40/40HQ/45的柜体积分别为24/54/68/86),计算公式=柜体积/cbm*外包装

					if (boxObCount != 0 && cbm != 0) {
						int count20 = (int) ((cubes[0] / cbm) * boxObCount);
						int count40 = (int) ((cubes[2] / cbm) * boxObCount);
						int count40hq = (int) ((cubes[1] / cbm) * boxObCount);
						int count45 = (int) ((cubes[3] / cbm) * boxObCount);
						detail.setBox20Count(new Float(count20));
						detail.setBox40Count(new Float(count40));
						detail.setBox40hqCount(new Float(count40hq));
						detail.setBox45Count(new Float(count45));
					}

				}

				if (headCtn.getContents().equals("BOX_OB_W") && rowCtn != null && !rowCtn.trim().equals("")) {
					boxObW = Float.parseFloat(rowCtn.trim());
					detail.setBoxObW(boxObW);
					detail.setBoxObWInch(boxObW / 2.54f);
					float cbm = Float.parseFloat(df.format(boxObL * boxObW * boxObH * 0.000001F));
					float cuft = Float.parseFloat(df.format(cbm * 35.315f));
					detail.setBoxCbm(cbm);
					detail.setBoxCuft(cuft);
					// 外箱数不为0时计算装箱数,计算装箱数(20/40/40HQ/45的柜体积分别为24/54/68/86),计算公式=柜体积/cbm*外包装

					if (boxObCount != 0 && cbm != 0) {
						int count20 = (int) ((cubes[0] / cbm) * boxObCount);
						int count40 = (int) ((cubes[2] / cbm) * boxObCount);
						int count40hq = (int) ((cubes[1] / cbm) * boxObCount);
						int count45 = (int) ((cubes[3] / cbm) * boxObCount);
						detail.setBox20Count(new Float(count20));
						detail.setBox40Count(new Float(count40));
						detail.setBox40hqCount(new Float(count40hq));
						detail.setBox45Count(new Float(count45));
					}
				}

				if (headCtn.getContents().equals("BOX_OB_H") && rowCtn != null && !rowCtn.trim().equals("")) {
					boxObH = Float.parseFloat(rowCtn.trim());
					detail.setBoxObH(boxObH);
					detail.setBoxObHInch(boxObH / 2.54f);

					float cbm = Float.parseFloat(df.format(boxObL * boxObW * boxObH * 0.000001F));
					float cuft = Float.parseFloat(df.format(cbm * 35.315f));
					detail.setBoxCbm(cbm);
					detail.setBoxCuft(cuft);
					// 外箱数不为0时计算装箱数,计算装箱数(20/40/40HQ/45的柜体积分别为24/54/68/86),计算公式=柜体积/cbm*外包装

					if (boxObCount != 0 && cbm != 0) {
						int count20 = (int) ((cubes[0] / cbm) * boxObCount);
						int count40 = (int) ((cubes[2] / cbm) * boxObCount);
						int count40hq = (int) ((cubes[1] / cbm) * boxObCount);
						int count45 = (int) ((cubes[3] / cbm) * boxObCount);
						detail.setBox20Count(new Float(count20));
						detail.setBox40Count(new Float(count40));
						detail.setBox40hqCount(new Float(count40hq));
						detail.setBox45Count(new Float(count45));
					}
				}
				if (headCtn.getContents().equals("BOX_WEIGTH") && rowCtn != null && !rowCtn.trim().equals("")) {
					boxWeigth = Float.parseFloat(rowCtn.trim());
					detail.setBoxWeigth(Float.parseFloat(rowCtn.trim()));
				}
				if (headCtn.getContents().equals("BOX_GROSS_WEIGTH") && rowCtn != null && !rowCtn.trim().equals("")) {
					gWeigh = Float.parseFloat(rowCtn.trim());
				}
				if (headCtn.getContents().equals("BOX_NET_WEIGTH") && rowCtn != null && !rowCtn.trim().equals("")) {
					nWeigh = Float.parseFloat(rowCtn.trim());
				}
				if (headCtn.getContents().equals("BOX_IB_COUNT")) {
					if (rowCtn != null && !rowCtn.trim().equals("")) {
						Double elemod = Double.parseDouble(rowCtn.trim());
						detail.setBoxIbCount(elemod.longValue());
					}
				}
				if (headCtn.getContents().equals("BOX_MB_COUNT")) {
					if (rowCtn != null && !rowCtn.trim().equals("")) {
						Double elemod = Double.parseDouble(rowCtn.trim());
						detail.setBoxMbCount(elemod.longValue());
					}
				}
				if (headCtn.getContents().equals("BOX_OB_COUNT")) {
					if (rowCtn != null && !rowCtn.trim().equals("")) {
						Double elemod = Double.parseDouble(rowCtn.trim());
						boxObCount = elemod.longValue();
						detail.setBoxObCount(boxObCount);
						float cbm = Float.parseFloat(df.format(boxObL * boxObW * boxObH * 0.000001F));
						// 外箱数不为0时计算装箱数,计算装箱数(20/40/40HQ/45的柜体积分别为24/54/68/86),计算公式=柜体积/cbm*外包装

						if (boxObCount != 0 && cbm != 0) {
							int count20 = (int) ((cubes[0] / cbm) * boxObCount);
							int count40 = (int) ((cubes[2] / cbm) * boxObCount);
							int count40hq = (int) ((cubes[1] / cbm) * boxObCount);
							int count45 = (int) ((cubes[3] / cbm) * boxObCount);
							detail.setBox20Count(new Float(count20));
							detail.setBox40Count(new Float(count40));
							detail.setBox40hqCount(new Float(count40hq));
							detail.setBox45Count(new Float(count45));
						}
					}
				}

				if (headCtn.getContents().equals("LI_RUN") && rowCtn != null && !rowCtn.trim().equals("")) {
					float priceProfit = Float.parseFloat(rowCtn.trim());
					detail.setLiRun(priceProfit);
				}

				if (headCtn.getContents().equals("TUI_LV") && rowCtn != null && !rowCtn.trim().equals("")) {
					tuiLv = Float.parseFloat(rowCtn.trim());
					detail.setTuiLv(tuiLv);
					if (hsIdTemp != 0) {
						CotEleOther cotEleOther = (CotEleOther) this.getReportDao().getById(CotEleOther.class, hsIdTemp);
						cotEleOther.setTaxRate(tuiLv);
						List list = new ArrayList();
						list.add(cotEleOther);
						this.getReportDao().updateRecords(list);
					}
				}

				if (headCtn.getContents().equals("PRICE_FAC") && rowCtn != null && !rowCtn.trim().equals("")) {
					priceFac = Float.parseFloat(rowCtn.trim());
					if (priceFac < 0) {
						CotMsgVO cotMsgVO = new CotMsgVO(i, j, "Priced at not less than 0!");
						msgList.add(cotMsgVO);
						isSuccess = false;
						break;
					}
				}
				if (headCtn.getContents().equals("PRICE_OUT") && rowCtn != null && !rowCtn.trim().equals("")) {
					priceOut = Float.parseFloat(rowCtn.trim());
					if (priceOut < 0) {
						CotMsgVO cotMsgVO = new CotMsgVO(i, j, "Priced at not less than 0!");
						msgList.add(cotMsgVO);
						isSuccess = false;
						break;
					}
				}
				if (headCtn.getContents().equals("BOX_CBM") && rowCtn != null && !rowCtn.trim().equals("")) {
					float cbm = Float.parseFloat(rowCtn.trim());
					float cuft = Float.parseFloat(df.format(cbm * 35.315f));
					detail.setBoxCbm(cbm);
					detail.setBoxCuft(cuft);
					// 外箱数不为0时计算装箱数,计算装箱数(20/40/40HQ/45的柜体积分别为24/54/68/86),计算公式=柜体积/cbm*外包装
					if (boxObCount != 0 && cbm != 0) {
						int count20 = (int) ((cubes[0] / cbm) * boxObCount);
						int count40 = (int) ((cubes[2] / cbm) * boxObCount);
						int count40hq = (int) ((cubes[1] / cbm) * boxObCount);
						int count45 = (int) ((cubes[3] / cbm) * boxObCount);
						detail.setBox20Count(new Float(count20));
						detail.setBox40Count(new Float(count40));
						detail.setBox40hqCount(new Float(count40hq));
						detail.setBox45Count(new Float(count45));
					}
					if (headCtn.getContents().equals("BOX_TYPE_ID")) {
						if (rowCtn != null && !rowCtn.trim().equals("")) {
							String[] box = mapBoxTypeCn.get(rowCtn.trim().toLowerCase());
							if (box == null) {
								box = mapBoxTypeEn.get(rowCtn.trim().toLowerCase());
							}
							if (box == null) {
								// 新增一条包装类型,中英文名相同
								CotBoxType cotBoxType = new CotBoxType();
								cotBoxType.setTypeName(rowCtn.trim());
								cotBoxType.setTypeNameEn(rowCtn.trim());
								List<CotBoxType> listType = new ArrayList<CotBoxType>();
								listType.add(cotBoxType);
								try {
									this.getReportDao().saveRecords(listType);
									box = new String[5];
									box[0] = cotBoxType.getId().toString();
									box[1] = null;
									box[2] = null;
									box[3] = null;
									box[4] = null;
									mapBoxTypeCn.put(rowCtn.trim().toLowerCase(), box);
								} catch (DAOException e) {
									e.printStackTrace();
									CotMsgVO cotMsgVO = new CotMsgVO(i, j, "Save Packing Way exception!");
									msgList.add(cotMsgVO);
									isSuccess = false;
									break;
								}
							}
							if (box[0] != null) {
								detail.setBoxTypeId(Integer.parseInt(box[0]));
							}
							if (box[1] != null) {
								detail.setBoxIbTypeId(Integer.parseInt(box[1]));
							}
							if (box[2] != null) {
								detail.setBoxMbTypeId(Integer.parseInt(box[2]));
							}
							if (box[3] != null) {
								detail.setBoxObTypeId(Integer.parseInt(box[3]));
							}
							if (box[4] != null) {
								detail.setBoxPbTypeId(Integer.parseInt(box[4]));
							}
						}
					}
				}
			} catch (Exception e) {
				CotMsgVO cotMsgVO = new CotMsgVO(i, j, "The number of cell values can only be!");
				msgList.add(cotMsgVO);
				isSuccess = false;
				break;
			}
			if (headCtn.getContents().equals("BOX_UINT") && rowCtn != null && !rowCtn.trim().equals("")) {
				detail.setBoxUint(rowCtn.trim());
			}
			if (headCtn.getContents().equals("BOX_TDESC") && rowCtn != null && !rowCtn.trim().equals("")) {
				detail.setBoxTDesc(rowCtn.trim());
			}
			if (headCtn.getContents().equals("BOX_BDESC") && rowCtn != null && !rowCtn.trim().equals("")) {
				detail.setBoxBDesc(rowCtn.trim());
			}
			if (headCtn.getContents().equals("BOX_REMARK") && rowCtn != null && !rowCtn.trim().equals("")) {
				detail.setBoxRemark(rowCtn.trim());
			}
			if (headCtn.getContents().equals("BOX_REMARK_CN") && rowCtn != null && !rowCtn.trim().equals("")) {
				detail.setBoxRemarkCn(rowCtn.trim());
			}
			// ---------------------报价信息
			if (headCtn.getContents().equals("PRICE_UINT")) {
				if (rowCtn != null && !rowCtn.trim().equals("")) {
					// 判断该币种是否存在
					Integer flag = this.getReportDao().isExistCurUnit(rowCtn.trim());
					if (flag == null) {
						CotMsgVO cotMsgVO = new CotMsgVO(i, j, "Currency does not exist");
						msgList.add(cotMsgVO);
						isSuccess = false;
						break;
					} else {
						detail.setPriceFacUint(flag);
					}
				}

			}

			if (headCtn.getContents().equals("PRICE_UNIT")) {
				if (rowCtn != null && !rowCtn.trim().equals("")) {
					// 判断该币种是否存在
					Integer flag = this.getReportDao().isExistCurUnit(rowCtn.trim());
					if (flag == null) {
						CotMsgVO cotMsgVO = new CotMsgVO(i, j, "Currency does not exist");
						msgList.add(cotMsgVO);
						isSuccess = false;
						break;
					} else {
						detail.setPriceOutUint(flag);
					}
				}
			}
		}
		if (isSuccess == true) {
			detail.setEleAddTime(now);
			// 设置中英文规格
			if (!eleSize.equals("")) {
				detail.setEleSizeDesc(eleSize);
			} else {
				if (isChangeL)
					detail.setEleSizeDesc(boxL + "*" + boxW + "*" + boxH);
			}
			if (!eleSizeInch.equals("")) {
				detail.setEleInchDesc(eleSizeInch);
			} else {
				if (isChangeL)
					detail.setEleInchDesc(boxLInch + "*" + boxWInch + "*" + boxHInch);
			}
			// 设置毛净重
			if (gWeigh == 0) {
				Float cfgGross = 0f;
				if (cotEleCfg != null && cotEleCfg.getGrossNum() != null) {
					cfgGross = cotEleCfg.getGrossNum();
				}
				float grossWeight = boxWeigth * boxObCount / 1000 + cfgGross;
				detail.setBoxGrossWeigth(grossWeight);
			} else {
				detail.setBoxGrossWeigth(gWeigh);
			}
			if (nWeigh == 0) {
				float netWeight = boxWeigth * boxObCount / 1000;
				detail.setBoxNetWeigth(netWeight);
			} else {
				detail.setBoxNetWeigth(nWeigh);
			}

			// 1.excel都没有厂价和外销价(新增时重新计算,覆盖时不计算)
			if (priceFac == null && priceOut == null) {
				priceFac = this.sumPriceFac(cotEleCfg.getExpessionFacIn(), detail);
				priceOut = this.sumPriceOut(cotEleCfg.getExpessionIn(), priceFac, detail);
			}
			// 2.excel有厂价没有外销价,只计算外销价
			if (priceFac != null && priceOut == null) {
				priceOut = this.sumPriceOut(cotEleCfg.getExpessionIn(), priceFac, detail);
			}
			// 3.只有外销价
			if (priceFac == null && priceOut != null) {
				priceFac = this.sumPriceFac(cotEleCfg.getExpessionFacIn(), detail);
			}
			detail.setPriceFac(priceFac);
			detail.setPriceOut(priceOut);

			// ctx.getSession().removeAttribute("givenReport");
			// TreeMap<String, CotGivenDetail> map = new TreeMap<String,
			// CotGivenDetail>();
			mapObj.put(detail.getEleId(), detail);
			ctx.getSession().setAttribute("givenReport", mapObj);
			successNum++;
		}
		if (!isDel) {
			// 增加影响行数
			CotMsgVO cotMsgVO = new CotMsgVO();
			cotMsgVO.setFlag(0);
			cotMsgVO.setSuccessNum(successNum);
			cotMsgVO.setFailNum(msgList.size());
			msgList.add(cotMsgVO);
			// 将错误信息存入系统日志
			// this.saveErrorMsgToFile(msgList);
		}
		// 获取系统常用数据字典
		SystemDicUtil dicUtil = new SystemDicUtil();
		Map map = dicUtil.getSysDicMap();
		ctx.getSession().setAttribute("sysdic", map);
		file.delete();
		return msgList;
	}

	// 根据文件路径导入
	public List<?> saveReport(String filename, boolean isCover, Integer givenId) {
		WebContext ctx = WebContextFactory.get();
		ctx.getSession().removeAttribute("givenReport");
		// 选取Excel文件
		Workbook workbook = null;
		// 定义成功条数
		int successNum = 0;
		// 定义覆盖条数
		int coverNum = 0;
		List<CotMsgVO> msgList = new ArrayList<CotMsgVO>();
		// 格式化样品包装数据
		DecimalFormat df = new DecimalFormat("#.####");
		DecimalFormat dfTwo = new DecimalFormat("#.##");
		// 当前时间
		Date now = new java.sql.Date(System.currentTimeMillis());

		// 先查询所有订单明细的货号
		List<?> detailEles = this.getReportDao().getGivenDetails(givenId);
		List<CotGivenDetail> detailElesBak = new ArrayList();
		for (int i = 0; i < detailEles.size(); i++) {
			CotGivenDetail det = (CotGivenDetail) detailEles.get(i);
			detailElesBak.add(det);
		}

		// 查询所有样品编号
		TreeMap<String, CotGivenDetail> mapEleId = new TreeMap<String, CotGivenDetail>();
		// 查询所有样品材质(中文KEY)
		Map<String, Integer> mapTypeCn = this.getReportDao().getAllTypeCn();
		// 查询所有样品材质(英文KEY)
		Map<String, Integer> mapTypeEn = this.getReportDao().getAllTypeEn();
		// 查询所有产品类别
		Map<String, Integer> mapEleType = this.getReportDao().getAllEleType();
		// 查询所有包装类型(中文KEY)
		Map<String, String[]> mapBoxTypeCn = this.getReportDao().getAllBoxTypeByCn();
		// 查询所有包装类型(英文KEY)
		Map<String, String[]> mapBoxTypeEn = this.getReportDao().getAllBoxTypeByEn();
		// 查询所有币种
		Map<String, Integer> mapCurrency = this.getReportDao().getAllCurrency();
		// 查询所有厂家简称
		Map<String, Integer> mapShortName = this.getReportDao().getAllFactoryShortName();
		// 查询所有海关编码
		Map<String, Integer> mapHsCode = this.getReportDao().getAllHsId();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		// 取得样品默认配置对象
		CotEleCfg cotEleCfg = this.getReportDao().getDefaultEleCfg();
		// 取得默认的20/40/40HQ/45的柜体积,数据字典没设置的话默认为24/54/68/86)
		Float[] cubes = this.getReportDao().getContainerCube();
		// 查询所有包装材料
		Map boxPackMap = this.getReportDao().getAllBoxPacking();
		// 获得tomcat路径
		String classPath = CotGivenServiceImpl.class.getResource("/").toString();
		String systemPath = classPath.substring(6, classPath.length() - 16);
		File file = new File(systemPath + "upload/" + filename);
		try {
			// 设置本地时间格式
			WorkbookSettings setting = new WorkbookSettings();
			java.util.Locale locale = new java.util.Locale("zh", "CN");
			setting.setLocale(locale);
			setting.setEncoding("ISO-8859-1");
			workbook = Workbook.getWorkbook(file, setting);

		} catch (Exception e) {
			file.delete();
			return null;
		}

		// 通过Workbook的getSheet方法选择第一个工作簿（从0开始）
		Sheet sheet = workbook.getSheet(0);
		// 限制一次性只能导入300条记录
		if (sheet.getRows() > 304) {
			CotMsgVO cotMsgVO = new CotMsgVO();
			cotMsgVO.setFlag(1);
			cotMsgVO.setMsg("Import failed! Only import a maximum of 300 samples！");
			msgList.add(cotMsgVO);
			file.delete();
			return msgList;
		}
		// 如果没有序列号这列或者序列号的值是空,自动填值
		int sortTemp = 0;
		boolean isHaveSort = false;

		for (int i = 4; i < sheet.getRows(); i++) {
			// 新建样品对象
			CotGivenDetail detail = null;
			// 定义厂家
			CotFactory cotFactory = new CotFactory();
			// 是否有成功标识
			boolean isSuccess = true;
			// 用于计算CBM和CUFT
			float boxObL = 0f;
			float boxObW = 0f;
			float boxObH = 0f;

			// 产品长宽高
			float boxL = 0f;
			float boxW = 0f;
			float boxH = 0f;
			float boxLInch = 0f;
			float boxWInch = 0f;
			float boxHInch = 0f;

			// 单重
			float boxWeigth = 0f;
			float gWeigh = 0f;
			float nWeigh = 0f;

			// 厂价
			Float priceFac = 0f;
			// 外销价
			Float priceOut = 0f;

			String eleSize = "";
			String eleSizeInch = "";
			boolean isChangeL = false;

			// 用于计算装箱数
			long boxObCount = 0;

			Integer hsIdTemp = 0;
			float tuiLv = 0f;

			boolean rowChk = false;
			for (int j = 0; j < sheet.getColumns(); j++) {
				// 表头
				Cell headCtn = sheet.getCell(j, 1);
				Cell row = sheet.getCell(j, i);
				String rowCtn = row.getContents();
				// 如果没转换成数字cell,默认的最长小数位是3位
				if (row.getType() == CellType.NUMBER || row.getType() == CellType.NUMBER_FORMULA) {
					NumberCell nc = (NumberCell) row;
					rowCtn = df.format(nc.getValue());
				}
				if (headCtn.getContents().equals("ELE_ID")) {
					if (rowCtn == null || rowCtn.trim().equals("")) {
						CotMsgVO cotMsgVO = new CotMsgVO(i, j, "Article No. can not be empty");
						msgList.add(cotMsgVO);
						isSuccess = false;
						break;
					}
					Object eleId = mapEleId.get(rowCtn.trim().toLowerCase());
					if (eleId == null) {
						if (rowCtn.trim().getBytes().length > 100) {
							CotMsgVO cotMsgVO = new CotMsgVO(i, j, "The length of Article No. can not exceed 100");
							msgList.add(cotMsgVO);
							isSuccess = false;
							break;
						}
					}
					if (isCover == true) {
						boolean ck = false;
						for (int k = 0; k < detailElesBak.size(); k++) {
							CotGivenDetail givenDetail = (CotGivenDetail) detailElesBak.get(k);
							if (rowCtn.trim().toLowerCase().equals(givenDetail.getEleId().toLowerCase())) {
								ck = true;
								rowChk = true;
								detail = givenDetail;
								detailElesBak.remove(givenDetail);
								coverNum++;
								break;
							}
						}
						if (ck == false && rowChk == false) {
							rowChk = false;
							detail = new CotGivenDetail();
							detail.setEleId(rowCtn.trim().toUpperCase());
							if (cotEleCfg != null) {
								// 设置样品默认值
								detail = this.getReportDao().setGivenDefault(cotEleCfg, detail);
							}
						}
						if (ck == false && rowChk == true) {
							isSuccess = false;
							rowChk = false;
							break;
						}
					} else {
						detail = new CotGivenDetail();
						detail.setEleId(rowCtn.trim().toUpperCase());
						if (cotEleCfg != null) {
							// 设置样品默认值
							detail = this.getReportDao().setGivenDefault(cotEleCfg, detail);
						}
					}
					if (detail.getBoxObCount() != null) {
						boxObCount = detail.getBoxObCount();
					}
					// 处理中英文规格
					if (detail.getBoxL() != null) {
						boxL = detail.getBoxL();
					}
					if (detail.getBoxW() != null) {
						boxW = detail.getBoxW();
					}
					if (detail.getBoxH() != null) {
						boxH = detail.getBoxH();
					}
					if (detail.getBoxLInch() != null) {
						boxLInch = detail.getBoxLInch();
					}
					if (detail.getBoxWInch() != null) {
						boxWInch = detail.getBoxWInch();
					}
					if (detail.getBoxHInch() != null) {
						boxHInch = detail.getBoxHInch();
					}
				}

				if (headCtn.getContents().equals("HS_ID") && rowCtn != null && !rowCtn.trim().equals("")) {
					// Integer flag = this.getReportDao().isExistHsId(
					// rowCtn.trim());
					Object hsId = mapHsCode.get(rowCtn.trim().toLowerCase());
					if (hsId == null) {
						CotEleOther cotEleOther = new CotEleOther();
						cotEleOther.setHscode(rowCtn.trim());
						cotEleOther.setCnName(rowCtn.trim());
						if (tuiLv != 0) {
							cotEleOther.setTaxRate(tuiLv);
						}
						try {
							this.getReportDao().create(cotEleOther);
							// 将新类别添加到已有的map中
							mapHsCode.put(rowCtn.trim().toLowerCase(), cotEleOther.getId());
						} catch (Exception e) {
							CotMsgVO cotMsgVO = new CotMsgVO(i, j, "保存海关编码异常");
							msgList.add(cotMsgVO);
							isSuccess = false;
							break;
						}
					}
					detail.setEleHsid(mapHsCode.get(rowCtn.trim().toLowerCase()));
					hsIdTemp = mapHsCode.get(rowCtn.trim().toLowerCase());
				}
				if (headCtn.getContents().equals("SORT_NO")) {
					isHaveSort = true;
					// 覆盖的时候,不覆盖序列号
					if (detail.getId() == null) {
						if (rowCtn != null && !rowCtn.trim().equals("")) {
							Double elemod = Double.parseDouble(rowCtn.trim());
							detail.setSortNo(elemod.intValue());
						} else {
							sortTemp++;
							detail.setSortNo(sortTemp);
						}
					}
				}
				if (headCtn.getContents().equals("ELE_NAME") && rowCtn != null && !rowCtn.trim().equals("")) {
					detail.setEleName(rowCtn.trim());
				}
				if (headCtn.getContents().equals("SIGN_REQUIRE") && rowCtn != null && !rowCtn.trim().equals("")) {
					detail.setSignRequire(rowCtn.trim());
				}
				if (headCtn.getContents().equals("SIGN_COUNT")) {
					if (rowCtn != null && !rowCtn.trim().equals("")) {
						detail.setSignCount(Integer.parseInt(rowCtn.trim()));
					}else{
						detail.setSignCount(1);
					}
				}
				if (headCtn.getContents().equals("GIVEN_COUNT")) {
					if (rowCtn != null && !rowCtn.trim().equals("")) {
						detail.setGivenCount(Integer.parseInt(rowCtn.trim()));
					}else{
						detail.setGivenCount(1);
					}
				}
				if (headCtn.getContents().equals("ELE_TYPENAME_LV2") && rowCtn != null && !rowCtn.trim().equals("")) {
					detail.setEleTypenameLv2(rowCtn.trim());
				}
				if (headCtn.getContents().equals("ELE_NAME_EN") && rowCtn != null && !rowCtn.trim().equals("")) {
					detail.setEleNameEn(rowCtn.trim());
				}
				if (headCtn.getContents().equals("ELE_FLAG")) {
					if (rowCtn != null && !rowCtn.trim().equals("")) {
						if (detail.getEleFlag() == null || detail.getEleFlag() != 2) {
							if (rowCtn.trim().equals("套件")) {
								detail.setEleFlag(1l);
							}
							if (rowCtn.trim().equals("单件")) {
								detail.setEleFlag(0l);
							}
							if (rowCtn.trim().equals("组件")) {
								detail.setEleFlag(3l);
							}
						}
					}
				}
				if (headCtn.getContents().equals("ELE_PARENT") && rowCtn != null && !rowCtn.trim().equals("")) {
					Integer temp = this.getReportDao().isExistEleId(rowCtn.trim());
					if (temp == null) {
						CotMsgVO cotMsgVO = new CotMsgVO(i, j, "Parent Article No. does not exist!");
						msgList.add(cotMsgVO);
						isSuccess = false;
						break;
					} else {
						detail.setEleParentId(temp);
						detail.setEleParent(rowCtn.trim());
						detail.setEleFlag(2l);
					}
				}
				if (headCtn.getContents().equals("CUST_NO") && rowCtn != null && !rowCtn.trim().equals("")) {
					detail.setCustNo(rowCtn.trim());
				}
				if (headCtn.getContents().equals("FACTORY_NO") && rowCtn != null && !rowCtn.trim().equals("")) {
					detail.setFactoryNo(rowCtn.trim());
				}
				if (headCtn.getContents().equals("ELE_DESC") && rowCtn != null && !rowCtn.trim().equals("")) {
					detail.setEleDesc(rowCtn.trim());
				}
				if (headCtn.getContents().equals("ELE_FROM") && rowCtn != null && !rowCtn.trim().equals("")) {
					detail.setEleFrom(rowCtn.trim());
				}
				if (headCtn.getContents().equals("ELE_FACTORY") && rowCtn != null && !rowCtn.trim().equals("")) {
					detail.setEleFactory(rowCtn.trim());
				}
				if (headCtn.getContents().equals("ELE_COL") && rowCtn != null && !rowCtn.trim().equals("")) {
					detail.setEleCol(rowCtn.trim());
				}
				if (headCtn.getContents().equals("BARCODE") && rowCtn != null && !rowCtn.trim().equals("")) {
					if (rowCtn.trim().length() > 30) {
						detail.setBarcode(rowCtn.trim().substring(0, 30));
					} else {
						detail.setBarcode(rowCtn.trim());
					}
				}
				if (headCtn.getContents().equals("ELE_COMPOSETYPE") && rowCtn != null && !rowCtn.trim().equals("")) {
					detail.setEleComposeType(rowCtn.trim());
				}
				if (headCtn.getContents().equals("ELE_UNIT")) {
					if (rowCtn != null && !rowCtn.trim().equals("")) {
						detail.setEleUnit(rowCtn.trim());
					}
				}
				if (headCtn.getContents().equals("ELE_GRADE") && rowCtn != null && !rowCtn.trim().equals("")) {
					detail.setEleGrade(rowCtn.trim());
				}
				if (headCtn.getContents().equals("ELE_FOR_PERSON") && rowCtn != null && !rowCtn.trim().equals("")) {
					detail.setEleForPerson(rowCtn.trim());
				}
				if (headCtn.getContents().equals("ELE_PRO_TIME") && rowCtn != null && !rowCtn.trim().equals("")) {
					if (row.getType() == CellType.DATE) {
						DateCell dc = (DateCell) row;
						detail.setEleProTime(new java.sql.Date(dc.getDate().getTime()));
					} else {
						try {
							java.util.Date date = sdf.parse(rowCtn.trim());

							detail.setEleProTime(new java.sql.Date(date.getTime()));
						} catch (ParseException e) {
							CotMsgVO cotMsgVO = new CotMsgVO(i, j, "Date format is incorrect");
							msgList.add(cotMsgVO);
							isSuccess = false;
							break;
						}
					}
				}
				if (headCtn.getContents().equals("ELE_SIZE_DESC") && rowCtn != null && !rowCtn.trim().equals("")) {
					eleSize = rowCtn.trim();
				}
				if (headCtn.getContents().equals("TAO_UNIT") && rowCtn != null && !rowCtn.trim().equals("")) {
					detail.setTaoUnit(rowCtn.trim());
				}
				if (headCtn.getContents().equals("ELE_INCH_DESC") && rowCtn != null && !rowCtn.trim().equals("")) {
					eleSizeInch = rowCtn.trim();
				}
				if (headCtn.getContents().equals("ELE_REMARK") && rowCtn != null && !rowCtn.trim().equals("")) {
					if (rowCtn.trim().length() > 500) {
						detail.setEleRemark(rowCtn.trim().substring(0, 500));
					} else {
						detail.setEleRemark(rowCtn.trim());
					}
				}
				if (headCtn.getContents().equals("ELE_TYPENAME_LV1")) {
					if (rowCtn != null && !rowCtn.trim().equals("")) {
						// 去掉厂家名称中的回车换行
						String t = rowCtn.trim().replaceAll("\r", "");
						String temp = t.replaceAll("\n", "");

						Integer typeId = mapTypeCn.get(temp.toLowerCase());
						if (typeId == null) {
							typeId = mapTypeEn.get(temp.toLowerCase());
						}
						if (typeId == null) {
							// 新增一条材质,中英文名相同
							CotTypeLv1 cotTypeLv1 = new CotTypeLv1();
							cotTypeLv1.setTypeName(temp);
							cotTypeLv1.setTypeEnName(temp);
							List<CotTypeLv1> listType = new ArrayList<CotTypeLv1>();
							listType.add(cotTypeLv1);
							try {
								this.getReportDao().saveRecords(listType);
								typeId = cotTypeLv1.getId();
								mapTypeCn.put(temp.toLowerCase(), typeId);
							} catch (DAOException e) {
								e.printStackTrace();
								CotMsgVO cotMsgVO = new CotMsgVO(i, j, "保存样品材质异常!");
								msgList.add(cotMsgVO);
								isSuccess = false;
								break;
							}
						}
						detail.setEleTypenameLv1(temp);
						detail.setEleTypeidLv1(typeId);
					}
				}

				if (headCtn.getContents().equals("ELE_TYPEID_LV2")) {
					if (rowCtn != null && !rowCtn.trim().equals("")) {
						Object typeId = mapEleType.get(rowCtn.trim().toLowerCase());
						if (typeId == null) {
							CotTypeLv2 cotTypeLv2 = new CotTypeLv2();
							cotTypeLv2.setTypeName(rowCtn.trim());
							try {
								this.getReportDao().create(cotTypeLv2);
								// 将新类别添加到已有的map中
								mapEleType.put(rowCtn.trim().toLowerCase(), cotTypeLv2.getId());
							} catch (Exception e) {
								CotMsgVO cotMsgVO = new CotMsgVO(i, j, "保存产品分类异常");
								msgList.add(cotMsgVO);
								isSuccess = false;
								break;
							}
						}
						detail.setEleTypeidLv2(mapEleType.get(rowCtn.trim().toLowerCase()));
					}

				}

				// 厂家简称不存在时新建厂家
				if (headCtn.getContents().equals("SHORT_NAME")) {
					if (rowCtn != null && !rowCtn.trim().equals("")) {
						// 去掉厂家名称中的回车换行
						String t = rowCtn.trim().replaceAll("\r", "");
						String temp = t.replaceAll("\n", "");
						Object factoryId = mapShortName.get(temp.toLowerCase());
						if (factoryId == null) {
							cotFactory.setFactoryName(temp);
							cotFactory.setShortName(temp);
							cotFactory.setFactroyTypeidLv1(1);
							try {

								String facNo = cotSeqService.getFacNo();
								// String facNo = seq.getAllSeqByType("facNo",
								// null);
								cotFactory.setFactoryNo(facNo);
								this.getReportDao().create(cotFactory);
								// seq.saveSeq("facNo");
								cotSeqService.saveSeq("factoryNo");
								// 将新类别添加到已有的map中
								mapShortName.put(temp.toLowerCase(), cotFactory.getId());
							} catch (Exception e) {
								CotMsgVO cotMsgVO = new CotMsgVO(i, j, "Save Supplier exception");
								msgList.add(cotMsgVO);
								isSuccess = false;
								break;
							}
						}
						detail.setFactoryId(mapShortName.get(temp.toLowerCase()));
						detail.setFactoryShortName(temp);
					}
				}
				// -----------------------------包装信息
				try {
					if (headCtn.getContents().equals("ELE_MOD") && rowCtn != null && !rowCtn.trim().equals("")) {
						Double elemod = Double.parseDouble(rowCtn.trim());
						detail.setEleMod(elemod.intValue());
					}
					if (headCtn.getContents().equals("ELE_UNITNUM")) {
						if (rowCtn != null && !rowCtn.trim().equals("")) {
							Double elemod = Double.parseDouble(rowCtn.trim());
							int numEle = elemod.intValue();
							if (numEle != 0) {
								detail.setEleFlag(1l);
							}
							detail.setEleUnitNum(numEle);
						}
					}
					if (headCtn.getContents().equals("cube") && rowCtn != null && !rowCtn.trim().equals("")) {
						detail.setCube(Float.parseFloat(rowCtn.trim()));
					}
					if (headCtn.getContents().equals("BOX_L") && rowCtn != null && !rowCtn.trim().equals("")) {
						isChangeL = true;
						float num = Float.parseFloat(rowCtn.trim());
						int temp = rowCtn.trim().lastIndexOf(".");
						if (temp > -1) {
							boxL = Float.parseFloat(dfTwo.format(num));
						} else {
							boxL = num;
						}
						boxLInch = Float.parseFloat(dfTwo.format(num / 2.54f));
						detail.setBoxL(boxL);
						detail.setBoxLInch(boxLInch);
					}
					if (headCtn.getContents().equals("BOX_L_INCH") && rowCtn != null && !rowCtn.trim().equals("")) {
						isChangeL = true;
						float num = Float.parseFloat(rowCtn.trim());
						int temp = rowCtn.trim().lastIndexOf(".");
						if (temp > -1) {
							boxLInch = Float.parseFloat(dfTwo.format(num));
						} else {
							boxLInch = num;
						}
						boxL = Float.parseFloat(dfTwo.format(num * 2.54f));
						detail.setBoxL(boxL);
						detail.setBoxLInch(boxLInch);

					}
					if (headCtn.getContents().equals("BOX_W") && rowCtn != null && !rowCtn.trim().equals("")) {
						isChangeL = true;
						float num = Float.parseFloat(rowCtn.trim());
						int temp = rowCtn.trim().lastIndexOf(".");
						if (temp > -1) {
							boxW = Float.parseFloat(dfTwo.format(num));
						} else {
							boxW = num;
						}
						boxWInch = Float.parseFloat(dfTwo.format(num / 2.54f));
						detail.setBoxW(boxW);
						detail.setBoxWInch(boxWInch);
					}
					if (headCtn.getContents().equals("BOX_W_INCH") && rowCtn != null && !rowCtn.trim().equals("")) {
						isChangeL = true;
						float num = Float.parseFloat(rowCtn.trim());
						int temp = rowCtn.trim().lastIndexOf(".");
						if (temp > -1) {
							boxWInch = Float.parseFloat(dfTwo.format(num));
						} else {
							boxWInch = num;
						}
						boxW = Float.parseFloat(dfTwo.format(num * 2.54f));
						detail.setBoxW(boxW);
						detail.setBoxWInch(boxWInch);
					}
					if (headCtn.getContents().equals("BOX_H") && rowCtn != null && !rowCtn.trim().equals("")) {
						isChangeL = true;
						float num = Float.parseFloat(rowCtn.trim());
						int temp = rowCtn.trim().lastIndexOf(".");
						if (temp > -1) {
							boxH = Float.parseFloat(dfTwo.format(num));
						} else {
							boxH = num;
						}
						boxHInch = Float.parseFloat(dfTwo.format(num / 2.54f));
						detail.setBoxH(boxH);
						detail.setBoxHInch(boxHInch);
					}
					if (headCtn.getContents().equals("BOX_H_INCH") && rowCtn != null && !rowCtn.trim().equals("")) {
						isChangeL = true;
						float num = Float.parseFloat(rowCtn.trim());
						int temp = rowCtn.trim().lastIndexOf(".");
						if (temp > -1) {
							boxHInch = Float.parseFloat(dfTwo.format(num));
						} else {
							boxHInch = num;
						}
						boxH = Float.parseFloat(dfTwo.format(num * 2.54f));
						detail.setBoxH(boxH);
						detail.setBoxHInch(boxHInch);
					}
					if (headCtn.getContents().equals("BOX_HANDLEH") && rowCtn != null && !rowCtn.trim().equals("")) {
						detail.setBoxHandleH(Float.parseFloat(rowCtn.trim()));
					}
					if (headCtn.getContents().equals("BOX_PB_L") && rowCtn != null && !rowCtn.trim().equals("")) {
						detail.setBoxPbL(Float.parseFloat(rowCtn.trim()));
						detail.setBoxPbLInch(Float.parseFloat(rowCtn.trim()) / 2.54f);
					}

					if (headCtn.getContents().equals("BOX_PB_W") && rowCtn != null && !rowCtn.trim().equals("")) {
						detail.setBoxPbW(Float.parseFloat(rowCtn.trim()));
						detail.setBoxPbWInch(Float.parseFloat(rowCtn.trim()) / 2.54f);
					}

					if (headCtn.getContents().equals("BOX_PB_H") && rowCtn != null && !rowCtn.trim().equals("")) {
						detail.setBoxPbH(Float.parseFloat(rowCtn.trim()));
						detail.setBoxPbHInch(Float.parseFloat(rowCtn.trim()) / 2.54f);
					}
					if (headCtn.getContents().equals("BOX_PB_COUNT")) {
						if (rowCtn != null && !rowCtn.trim().equals("")) {
							Double elemod = Double.parseDouble(rowCtn.trim());
							detail.setBoxPbCount(elemod.longValue());
						}
					}
					if (headCtn.getContents().equals("BOX_IB_L") && rowCtn != null && !rowCtn.trim().equals("")) {
						detail.setBoxIbL(Float.parseFloat(rowCtn.trim()));
						detail.setBoxIbLInch(Float.parseFloat(rowCtn.trim()) / 2.54f);
					}

					if (headCtn.getContents().equals("BOX_IB_W") && rowCtn != null && !rowCtn.trim().equals("")) {
						detail.setBoxIbW(Float.parseFloat(rowCtn.trim()));
						detail.setBoxIbWInch(Float.parseFloat(rowCtn.trim()) / 2.54f);
					}

					if (headCtn.getContents().equals("BOX_IB_H") && rowCtn != null && !rowCtn.trim().equals("")) {
						detail.setBoxIbH(Float.parseFloat(rowCtn.trim()));
						detail.setBoxIbHInch(Float.parseFloat(rowCtn.trim()) / 2.54f);
					}

					if (headCtn.getContents().equals("BOX_MB_L") && rowCtn != null && !rowCtn.trim().equals("")) {
						detail.setBoxMbL(Float.parseFloat(rowCtn.trim()));
						detail.setBoxMbLInch(Float.parseFloat(rowCtn.trim()) / 2.54f);
					}

					if (headCtn.getContents().equals("BOX_MB_W") && rowCtn != null && !rowCtn.trim().equals("")) {
						detail.setBoxMbW(Float.parseFloat(rowCtn.trim()));
						detail.setBoxMbWInch(Float.parseFloat(rowCtn.trim()) / 2.54f);
					}

					if (headCtn.getContents().equals("BOX_MB_H") && rowCtn != null && !rowCtn.trim().equals("")) {
						detail.setBoxMbH(Float.parseFloat(rowCtn.trim()));
						detail.setBoxMbHInch(Float.parseFloat(rowCtn.trim()) / 2.54f);
					}

					if (headCtn.getContents().equals("BOX_OB_L") && rowCtn != null && !rowCtn.trim().equals("")) {
						boxObL = Float.parseFloat(rowCtn.trim());
						detail.setBoxObL(boxObL);
						detail.setBoxObLInch(boxObL / 2.54f);

						float cbm = Float.parseFloat(df.format(boxObL * boxObW * boxObH * 0.000001F));
						float cuft = Float.parseFloat(df.format(cbm * 35.315f));
						detail.setBoxCbm(cbm);
						detail.setBoxCuft(cuft);
						// 外箱数不为0时计算装箱数,计算装箱数(20/40/40HQ/45的柜体积分别为24/54/68/86),计算公式=柜体积/cbm*外包装

						if (boxObCount != 0 && cbm != 0) {
							int count20 = (int) ((cubes[0] / cbm) * boxObCount);
							int count40 = (int) ((cubes[2] / cbm) * boxObCount);
							int count40hq = (int) ((cubes[1] / cbm) * boxObCount);
							int count45 = (int) ((cubes[3] / cbm) * boxObCount);
							detail.setBox20Count(new Float(count20));
							detail.setBox40Count(new Float(count40));
							detail.setBox40hqCount(new Float(count40hq));
							detail.setBox45Count(new Float(count45));
						}
					}

					if (headCtn.getContents().equals("BOX_OB_W") && rowCtn != null && !rowCtn.trim().equals("")) {
						boxObW = Float.parseFloat(rowCtn.trim());
						detail.setBoxObW(boxObW);
						detail.setBoxObWInch(boxObW / 2.54f);
						float cbm = Float.parseFloat(df.format(boxObL * boxObW * boxObH * 0.000001F));
						float cuft = Float.parseFloat(df.format(cbm * 35.315f));
						detail.setBoxCbm(cbm);
						detail.setBoxCuft(cuft);
						// 外箱数不为0时计算装箱数,计算装箱数(20/40/40HQ/45的柜体积分别为24/54/68/86),计算公式=柜体积/cbm*外包装

						if (boxObCount != 0 && cbm != 0) {
							int count20 = (int) ((cubes[0] / cbm) * boxObCount);
							int count40 = (int) ((cubes[2] / cbm) * boxObCount);
							int count40hq = (int) ((cubes[1] / cbm) * boxObCount);
							int count45 = (int) ((cubes[3] / cbm) * boxObCount);
							detail.setBox20Count(new Float(count20));
							detail.setBox40Count(new Float(count40));
							detail.setBox40hqCount(new Float(count40hq));
							detail.setBox45Count(new Float(count45));
						}
					}

					if (headCtn.getContents().equals("BOX_OB_H") && rowCtn != null && !rowCtn.trim().equals("")) {
						boxObH = Float.parseFloat(rowCtn.trim());
						detail.setBoxObH(boxObH);
						detail.setBoxObHInch(boxObH / 2.54f);

						float cbm = Float.parseFloat(df.format(boxObL * boxObW * boxObH * 0.000001F));
						float cuft = Float.parseFloat(df.format(cbm * 35.315f));
						detail.setBoxCbm(cbm);
						detail.setBoxCuft(cuft);
						// 外箱数不为0时计算装箱数,计算装箱数(20/40/40HQ/45的柜体积分别为24/54/68/86),计算公式=柜体积/cbm*外包装

						if (boxObCount != 0 && cbm != 0) {
							int count20 = (int) ((cubes[0] / cbm) * boxObCount);
							int count40 = (int) ((cubes[2] / cbm) * boxObCount);
							int count40hq = (int) ((cubes[1] / cbm) * boxObCount);
							int count45 = (int) ((cubes[3] / cbm) * boxObCount);
							detail.setBox20Count(new Float(count20));
							detail.setBox40Count(new Float(count40));
							detail.setBox40hqCount(new Float(count40hq));
							detail.setBox45Count(new Float(count45));
						}
					}
					if (headCtn.getContents().equals("BOX_WEIGTH") && rowCtn != null && !rowCtn.trim().equals("")) {
						boxWeigth = Float.parseFloat(rowCtn.trim());
						detail.setBoxWeigth(Float.parseFloat(rowCtn.trim()));
					}
					if (headCtn.getContents().equals("BOX_GROSS_WEIGTH") && rowCtn != null && !rowCtn.trim().equals("")) {
						gWeigh = Float.parseFloat(rowCtn.trim());
					}
					if (headCtn.getContents().equals("BOX_NET_WEIGTH") && rowCtn != null && !rowCtn.trim().equals("")) {
						nWeigh = Float.parseFloat(rowCtn.trim());
					}
					if (headCtn.getContents().equals("BOX_IB_COUNT")) {
						if (rowCtn != null && !rowCtn.trim().equals("")) {
							Double elemod = Double.parseDouble(rowCtn.trim());
							detail.setBoxIbCount(elemod.longValue());
						}
					}
					if (headCtn.getContents().equals("BOX_MB_COUNT")) {
						if (rowCtn != null && !rowCtn.trim().equals("")) {
							Double elemod = Double.parseDouble(rowCtn.trim());
							detail.setBoxMbCount(elemod.longValue());
						}
					}
					if (headCtn.getContents().equals("BOX_OB_COUNT")) {
						if (rowCtn != null && !rowCtn.trim().equals("")) {
							Double elemod = Double.parseDouble(rowCtn.trim());
							boxObCount = elemod.longValue();
							detail.setBoxObCount(boxObCount);
							float cbm = Float.parseFloat(df.format(boxObL * boxObW * boxObH * 0.000001F));
							// 外箱数不为0时计算装箱数,计算装箱数(20/40/40HQ/45的柜体积分别为24/54/68/86),计算公式=柜体积/cbm*外包装

							if (boxObCount != 0 && cbm != 0) {
								int count20 = (int) ((cubes[0] / cbm) * boxObCount);
								int count40 = (int) ((cubes[2] / cbm) * boxObCount);
								int count40hq = (int) ((cubes[1] / cbm) * boxObCount);
								int count45 = (int) ((cubes[3] / cbm) * boxObCount);
								detail.setBox20Count(new Float(count20));
								detail.setBox40Count(new Float(count40));
								detail.setBox40hqCount(new Float(count40hq));
								detail.setBox45Count(new Float(count45));
							}
						}
					}

					if (headCtn.getContents().equals("LI_RUN") && rowCtn != null && !rowCtn.trim().equals("")) {
						float priceProfit = Float.parseFloat(rowCtn.trim());
						detail.setLiRun(priceProfit);
					}
					if (headCtn.getContents().equals("TUI_LV") && rowCtn != null && !rowCtn.trim().equals("")) {
						tuiLv = Float.parseFloat(rowCtn.trim());
						detail.setTuiLv(tuiLv);
						if (hsIdTemp != 0) {
							CotEleOther cotEleOther = (CotEleOther) this.getReportDao().getById(CotEleOther.class, hsIdTemp);
							cotEleOther.setTaxRate(tuiLv);
							List list = new ArrayList();
							list.add(cotEleOther);
							this.getReportDao().updateRecords(list);
						}
					}

					if (headCtn.getContents().equals("PRICE_FAC") && rowCtn != null && !rowCtn.trim().equals("")) {
						priceFac = Float.parseFloat(rowCtn.trim());
						if (priceFac < 0) {
							CotMsgVO cotMsgVO = new CotMsgVO(i, j, "Priced at not less than 0!");
							msgList.add(cotMsgVO);
							isSuccess = false;
							break;
						}
					}
					if (headCtn.getContents().equals("PRICE_OUT") && rowCtn != null && !rowCtn.trim().equals("")) {
						priceOut = Float.parseFloat(rowCtn.trim());
						if (priceOut < 0) {
							CotMsgVO cotMsgVO = new CotMsgVO(i, j, "Priced at not less than 0!");
							msgList.add(cotMsgVO);
							isSuccess = false;
							break;
						}
					}
					if (headCtn.getContents().equals("BOX_CBM") && rowCtn != null && !rowCtn.trim().equals("")) {
						float cbm = Float.parseFloat(rowCtn.trim());
						float cuft = Float.parseFloat(df.format(cbm * 35.315f));
						detail.setBoxCbm(cbm);
						detail.setBoxCuft(cuft);
						// 外箱数不为0时计算装箱数,计算装箱数(20/40/40HQ/45的柜体积分别为24/54/68/86),计算公式=柜体积/cbm*外包装
						if (boxObCount != 0 && cbm != 0) {
							int count20 = (int) ((cubes[0] / cbm) * boxObCount);
							int count40 = (int) ((cubes[2] / cbm) * boxObCount);
							int count40hq = (int) ((cubes[1] / cbm) * boxObCount);
							int count45 = (int) ((cubes[3] / cbm) * boxObCount);
							detail.setBox20Count(new Float(count20));
							detail.setBox40Count(new Float(count40));
							detail.setBox40hqCount(new Float(count40hq));
							detail.setBox45Count(new Float(count45));
						}
					}
					if (headCtn.getContents().equals("BOX_TYPE_ID")) {
						if (rowCtn != null && !rowCtn.trim().equals("")) {
							String[] box = mapBoxTypeCn.get(rowCtn.trim().toLowerCase());
							if (box == null) {
								box = mapBoxTypeEn.get(rowCtn.trim().toLowerCase());
							}
							if (box == null) {
								// 新增一条包装类型,中英文名相同
								CotBoxType cotBoxType = new CotBoxType();
								cotBoxType.setTypeName(rowCtn.trim());
								cotBoxType.setTypeNameEn(rowCtn.trim());
								List<CotBoxType> listType = new ArrayList<CotBoxType>();
								listType.add(cotBoxType);
								try {
									this.getReportDao().saveRecords(listType);
									box = new String[5];
									box[0] = cotBoxType.getId().toString();
									box[1] = null;
									box[2] = null;
									box[3] = null;
									box[4] = null;
									mapBoxTypeCn.put(rowCtn.trim().toLowerCase(), box);
								} catch (DAOException e) {
									e.printStackTrace();
									CotMsgVO cotMsgVO = new CotMsgVO(i, j, "Save Packing Way exception!");
									msgList.add(cotMsgVO);
									isSuccess = false;
									break;
								}
							}
							if (box[0] != null) {
								detail.setBoxTypeId(Integer.parseInt(box[0]));
							}
							if (box[1] != null) {
								detail.setBoxIbTypeId(Integer.parseInt(box[1]));
							}
							if (box[2] != null) {
								detail.setBoxMbTypeId(Integer.parseInt(box[2]));
							}
							if (box[3] != null) {
								detail.setBoxObTypeId(Integer.parseInt(box[3]));
							}
							if (box[4] != null) {
								detail.setBoxPbTypeId(Integer.parseInt(box[4]));
							}
						}
					}
				} catch (Exception e) {
					CotMsgVO cotMsgVO = new CotMsgVO(i, j, "The number of cell values can only be!");
					msgList.add(cotMsgVO);
					isSuccess = false;
					break;
				}
				if (headCtn.getContents().equals("BOX_UINT") && rowCtn != null && !rowCtn.trim().equals("")) {
					detail.setBoxUint(rowCtn.trim());
				}
				if (headCtn.getContents().equals("BOX_TDESC") && rowCtn != null && !rowCtn.trim().equals("")) {
					detail.setBoxTDesc(rowCtn.trim());
				}
				if (headCtn.getContents().equals("BOX_BDESC") && rowCtn != null && !rowCtn.trim().equals("")) {
					detail.setBoxBDesc(rowCtn.trim());
				}

				if (headCtn.getContents().equals("BOX_REMARK") && rowCtn != null && !rowCtn.trim().equals("")) {
					detail.setBoxRemark(rowCtn.trim());
				}
				if (headCtn.getContents().equals("BOX_REMARK_CN") && rowCtn != null && !rowCtn.trim().equals("")) {
					detail.setBoxRemarkCn(rowCtn.trim());
				}

				// ---------------------报价信息
				if (headCtn.getContents().equals("PRICE_UINT")) {
					if (rowCtn != null && !rowCtn.trim().equals("")) {
						Object curId = mapCurrency.get(rowCtn.trim().toLowerCase());
						if (curId == null) {
							CotMsgVO cotMsgVO = new CotMsgVO(i, j, "Currency does not exist");
							msgList.add(cotMsgVO);
							isSuccess = false;
							break;
						}
						detail.setPriceFacUint(mapCurrency.get(rowCtn.trim().toLowerCase()));
					}
				}

				if (headCtn.getContents().equals("PRICE_UNIT")) {
					if (rowCtn != null && !rowCtn.trim().equals("")) {
						Object curId = mapCurrency.get(rowCtn.trim().toLowerCase());
						if (curId == null) {
							CotMsgVO cotMsgVO = new CotMsgVO(i, j, "Currency does not exist");
							msgList.add(cotMsgVO);
							isSuccess = false;
							break;
						}
						detail.setPriceOutUint(mapCurrency.get(rowCtn.trim().toLowerCase()));
					}
				}
			}
			if (isSuccess == true) {
				// 设置序列号
				if (isHaveSort == false) {
					sortTemp++;
					if (detail.getSortNo() == null) {
						detail.setSortNo(sortTemp);
					}
				}
				// 保存样品信息
				detail.setEleAddTime(now);
				// 设置中英文规格
				if (!eleSize.equals("")) {
					detail.setEleSizeDesc(eleSize);
				} else {
					if (isChangeL)
						detail.setEleSizeDesc(boxL + "*" + boxW + "*" + boxH);
				}
				if (!eleSizeInch.equals("")) {
					detail.setEleInchDesc(eleSizeInch);
				} else {
					if (isChangeL)
						detail.setEleInchDesc(boxLInch + "*" + boxWInch + "*" + boxHInch);
				}
				// 设置毛净重
				if (gWeigh == 0) {
					Float cfgGross = 0f;
					if (cotEleCfg != null && cotEleCfg.getGrossNum() != null) {
						cfgGross = cotEleCfg.getGrossNum();
					}
					float grossWeight = boxWeigth * boxObCount / 1000 + cfgGross;
					detail.setBoxGrossWeigth(grossWeight);
				} else {
					detail.setBoxGrossWeigth(gWeigh);
				}
				if (nWeigh == 0) {
					float netWeight = boxWeigth * boxObCount / 1000;
					detail.setBoxNetWeigth(netWeight);
				} else {
					detail.setBoxNetWeigth(nWeigh);
				}

				// 计算包材价格
				detail = this.getReportDao().calPrice(boxPackMap, detail);

				// 1.excel都没有厂价和外销价(新增时重新计算,覆盖时不计算)
				if (priceFac == null && priceOut == null) {
					priceFac = this.sumPriceFac(cotEleCfg.getExpessionFacIn(), detail);
					priceOut = this.sumPriceOut(cotEleCfg.getExpessionIn(), priceFac, detail);
				}
				// 2.excel有厂价没有外销价,只计算外销价
				if (priceFac != null && priceOut == null) {
					priceOut = this.sumPriceOut(cotEleCfg.getExpessionIn(), priceFac, detail);
				}
				// 3.只有外销价
				if (priceFac == null && priceOut != null) {
					priceFac = this.sumPriceFac(cotEleCfg.getExpessionFacIn(), detail);
				}
				detail.setPriceFac(priceFac);
				detail.setPriceOut(priceOut);

				mapEleId.put(detail.getEleId().toLowerCase(), detail);
				// 增加成功条数
				successNum++;
			}
			// if (rowChk == true) {
			// i--;
			// } else {
			// detailElesBak = new ArrayList();
			// for (int m = 0; m < detailEles.size(); m++) {
			// CotGivenDetail det = (CotGivenDetail) detailEles.get(m);
			// detailElesBak.add(det);
			// }
			// }
		}
		// 增加影响行数
		CotMsgVO cotMsgVO = new CotMsgVO();
		cotMsgVO.setFlag(0);
		cotMsgVO.setSuccessNum(successNum);
		cotMsgVO.setCoverNum(coverNum);
		cotMsgVO.setFailNum(msgList.size());
		msgList.add(cotMsgVO);
		// 将错误信息存入系统日志
		// this.saveErrorMsgToFile(msgList);
		ctx.getSession().setAttribute("givenReport", mapEleId);
		// 获取系统常用数据字典
		SystemDicUtil dicUtil = new SystemDicUtil();
		Map map = dicUtil.getSysDicMap();
		ctx.getSession().setAttribute("sysdic", map);
		file.delete();
		return msgList;
	}

	// 判断货号字符串是否已经添加
	public boolean checkExistByExcel() {

		WebContext ctx = WebContextFactory.get();
		Object obj = ctx.getSession().getAttribute("givenReport");
		List<String> list = new ArrayList<String>();
		if (obj == null) {
			return false;
		}
		TreeMap<String, CotGivenDetail> map = (TreeMap<String, CotGivenDetail>) obj;
		Iterator<?> it = map.keySet().iterator();
		while (it.hasNext()) {
			String eleId = (String) it.next();
			CotGivenDetail detail = map.get(eleId);
			detail.setType("none");
			detail.setSrcId(0);
			if(detail.getGivenCount()==null){
				detail.setGivenCount(0);
			}
			if(detail.getSignCount()==null){
				detail.setSignCount(0);
			}
			Object objNew = SystemUtil.getObjBySession(null, "excelTempGiven");
			if (objNew == null) {
				TreeMap<String, CotGivenDetail> orderMap = new TreeMap<String, CotGivenDetail>();
				orderMap.put(eleId, detail);
				SystemUtil.setObjBySession(null, orderMap, "excelTempGiven");
			} else {
				TreeMap<String, CotGivenDetail> orderMap = (TreeMap<String, CotGivenDetail>) objNew;
				orderMap.put(eleId, detail);
				SystemUtil.setObjBySession(null, orderMap, "excelTempGiven");
			}
		}
		this.removeExcelSession();
		return true;
	}

	// 根据样品货号组装报价明细产品对象,并根据报价参数算出单价(excel导入)
	public CotGivenDetail findDetailByEleIdExcel(String eleId, Map<?, ?> map, String liRunCau) {
		WebContext ctx = WebContextFactory.get();
		Object temp = ctx.getSession().getAttribute("givenReport");
		CotGivenDetail cotGivenDetail = null;
		if (temp != null) {
			TreeMap<String, CotGivenDetail> mapExcel = (TreeMap<String, CotGivenDetail>) temp;
			cotGivenDetail = mapExcel.get(eleId);
		} else {
			return null;
		}

		return cotGivenDetail;
	}

	// 获得暂无图片的图片字节
	public byte[] getZwtpPic() {
		// 获得系统路径
		String classPath = CotGivenServiceImpl.class.getResource("/").toString();
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
			logger.error("获得暂无图片的图片字节错误!");
			return null;
		}
	}

	// 保存明细
	public void saveDetail(Integer givenId, Integer currencyId, Integer custId) {
		Object obj = SystemUtil.getObjBySession(null, "excelTempGiven");
		TreeMap<String, CotGivenDetail> givenMap = (TreeMap<String, CotGivenDetail>) obj;
		List<CotGivenDetail> records = new ArrayList<CotGivenDetail>();
		List<CotGivenPic> imgList = new ArrayList<CotGivenPic>();

		if (givenMap != null) {
			Iterator<?> it = givenMap.keySet().iterator();
			while (it.hasNext()) {
				String eleId = (String) it.next();
				CotGivenDetail cotGivenDetail = givenMap.get(eleId);
				if (cotGivenDetail.getGivenId() == null || (cotGivenDetail.getGivenId() != null && cotGivenDetail.getGivenId().intValue() == givenId.intValue())) {
					cotGivenDetail.setGivenId(givenId);
					cotGivenDetail.setEleAddTime(new Date(System.currentTimeMillis()));// 添加时间
					// 判断利润率是否超过数据库范围
					Float liRes = cotGivenDetail.getLiRun();
					if (liRes == null) {
						cotGivenDetail.setLiRun(0f);
					} else {
						if (liRes <= -1000) {
							cotGivenDetail.setLiRun(-999f);
						}
						if (liRes >= 1000) {
							cotGivenDetail.setLiRun(999f);
						}
					}
					records.add(cotGivenDetail);
				}
			}
		}

		WebContext ctx = WebContextFactory.get();
		Integer empId = (Integer) ctx.getSession().getAttribute("empId");
		this.getCotBaseDao().saveOrUpdateRecords(records);
		// 保存到系统日记表
		CotSyslog cotSyslog = new CotSyslog();
		cotSyslog.setEmpId(empId);
		cotSyslog.setOpMessage("excel导入后保存寄样单明细成功");
		cotSyslog.setOpModule("given");
		cotSyslog.setOpTime(new Date(System.currentTimeMillis()));
		cotSyslog.setOpType(1);
		sysLogService.addSysLogByObj(cotSyslog);

		// 图片操作类
		CotOpImgService impOpService = (CotOpImgService) SystemUtil.getService("CotOpImgService");
		byte[] zwtpByte = this.getZwtpPic();
		for (int i = 0; i < records.size(); i++) {
			CotGivenDetail cotGivenDetail = records.get(i);
			String type = cotGivenDetail.getType();
			Integer srcId = cotGivenDetail.getSrcId();
			if (type != null) {
				// 表格下拉框选择"请选择"时
				if (cotGivenDetail.getFactoryId() != null && cotGivenDetail.getFactoryId() == 0) {
					cotGivenDetail.setFactoryId(null);
				}
				CotGivenPic givenPic = null;
				// 查询该明细是否已经有图片
				if (cotGivenDetail.getId() != null) {
					String hql = "from CotGivenPic obj where obj.fkId=" + cotGivenDetail.getId();
					List ls = this.getCotBaseDao().find(hql);
					if (ls != null && ls.size() > 0) {
						givenPic = (CotGivenPic) ls.get(0);
					} else {
						givenPic = new CotGivenPic();
					}
				} else {
					givenPic = new CotGivenPic();
				}

				if (type.equals("ele")) {
					CotElePic cotElePic = impOpService.getElePicImgByEleId(srcId);
					cotGivenDetail.setPicName(cotElePic.getEleId());
					givenPic.setEleId(cotElePic.getEleId());
					givenPic.setPicImg(cotElePic.getPicImg());
					givenPic.setPicSize(cotElePic.getPicImg().length);
					givenPic.setPicName(cotElePic.getEleId());
				}
				if (type.equals("price")) {
					CotPricePic cotPricePic = impOpService.getPricePic(srcId);
					cotGivenDetail.setPicName(cotPricePic.getEleId());
					givenPic.setEleId(cotPricePic.getEleId());
					givenPic.setPicImg(cotPricePic.getPicImg());
					givenPic.setPicSize(cotPricePic.getPicImg().length);
					givenPic.setPicName(cotPricePic.getEleId());
				}
				if (type.equals("order")) {
					CotOrderPic cotOrderPic = impOpService.getOrderPic(srcId);
					cotGivenDetail.setPicName(cotOrderPic.getEleId());
					givenPic.setEleId(cotOrderPic.getEleId());
					givenPic.setPicImg(cotOrderPic.getPicImg());
					givenPic.setPicSize(cotOrderPic.getPicImg().length);
					givenPic.setPicName(cotOrderPic.getEleId());
				}
				if (type.equals("given")) {
					CotGivenPic cotGivenPic = impOpService.getGivenPic(srcId);
					cotGivenDetail.setPicName(cotGivenPic.getEleId());
					givenPic.setEleId(cotGivenPic.getEleId());
					givenPic.setPicImg(cotGivenPic.getPicImg());
					givenPic.setPicSize(cotGivenPic.getPicImg().length);
					givenPic.setPicName(cotGivenPic.getEleId());
				}
				if (type.equals("none")) {
					if(givenPic.getPicImg()==null){
						// excel导入后查找样品档案有没有该货号,有就取样品档案的图片
						String strSql = "select pic from CotElementsNew obj,CotElePic pic where obj.id=pic.fkId and obj.eleId = '" + cotGivenDetail.getEleId() + "'";
						List list = this.getCotBaseDao().find(strSql);
						if (list != null && list.size() > 0) {
							CotElePic cotElePic = (CotElePic) list.get(0);
							givenPic.setEleId(cotElePic.getEleId());
							givenPic.setPicImg(cotElePic.getPicImg());
							givenPic.setPicSize(cotElePic.getPicImg().length);
							givenPic.setPicName(cotElePic.getEleId());
						} else {
							givenPic.setEleId(cotGivenDetail.getEleId());
							givenPic.setPicImg(zwtpByte);
							givenPic.setPicSize(zwtpByte.length);
							givenPic.setPicName(cotGivenDetail.getEleId());
						}
					}
				}
				givenPic.setFkId(cotGivenDetail.getId());
				// 添加到图片数组
				imgList.add(givenPic);
			}
		}

		// 插入报价图片信息表
		for (int i = 0; i < imgList.size(); i++) {
			CotGivenPic givenPic = (CotGivenPic) imgList.get(i);
			List picList = new ArrayList();
			picList.add(givenPic);
			// 逐条添加，避免数据量大的时候，内存溢出
			impOpService.saveOrUpdateImg(picList);
		}

		// 获取货号、客号保存数据到客号表
		Iterator<?> it = records.iterator();
		Map<String, String> elecustMap = new HashMap<String, String>();
		Map<String, String> elenameenMap = new HashMap<String, String>();
		String type = "given";
		while (it.hasNext()) {
			CotGivenDetail detail = (CotGivenDetail) it.next();
			String eleId = detail.getEleId();
			String custNo = detail.getCustNo();
			String eleNameEn = detail.getEleNameEn();

			if (custNo != null && !custNo.trim().equals("")) {
				elecustMap.put(eleId, custNo);
			}
			if (eleNameEn != null && !eleNameEn.trim().equals("")) {
				elenameenMap.put(eleId, eleNameEn);
			}
		}
		if (elecustMap.size() != 0 && elenameenMap.size() != 0) {
			this.getCotElementsService().saveEleCustNoByCustList(elecustMap, elenameenMap, custId, type);
		}
		// 清空内存
		ctx.getSession().removeAttribute("SessionEXCELTEMPGIVEN");
	}

	// 清空excel的缓存
	public void removeExcelSession() {
		WebContext ctx = WebContextFactory.get();
		ctx.getSession().removeAttribute("givenReport");
	}

	// 清空Pan的缓存
	public void removePanSession() {
		WebContext ctx = WebContextFactory.get();
		ctx.getSession().removeAttribute("CheckGivenMachine");
	}

	// 将该货号对应的样品转成寄样明细
	public CotGivenDetail changeEleToGivenDetail(String eleId) {
		// 查找样品对象
		String hql = "from CotElementsNew obj where obj.eleId = '" + eleId + "'";
		List<?> list = this.getCotBaseDao().find(hql);
		if (list.size() == 0) {
			return null;
		}
		CotElementsNew cotElements = (CotElementsNew) list.get(0);
		// 新建明细对象
		CotGivenDetail cotGivenDetail = new CotGivenDetail();
		// 1.使用反射获取对象的所有属性名称
		String[] propEle = ReflectionUtils.getDeclaredFields(CotElementsNew.class);

		ConvertUtilsBean convertUtils = new ConvertUtilsBean();
		SqlDateConverter dateConverter = new SqlDateConverter();
		convertUtils.register(dateConverter, Date.class);
		// 因为要注册converter,所以不能再使用BeanUtils的静态方法了，必须创建BeanUtilsBean实例
		BeanUtilsBean beanUtils = new BeanUtilsBean(convertUtils, new PropertyUtilsBean());
		boolean flag = true;
		for (int i = 0; i < propEle.length; i++) {
			try {
				String value = beanUtils.getProperty(cotElements, propEle[i]);
				if ("cotPictures".equals(propEle[i]) || "cotFile".equals(propEle[i]) || "childs".equals(propEle[i]) || "picImg".equals(propEle[i]) || "cotPriceFacs".equals(propEle[i]) || "cotElePrice".equals(propEle[i]) || "cotEleFittings".equals(propEle[i]) || "eleFittingPrice".equals(propEle[i]) || "cotElePacking".equals(propEle[i]) || "packingPrice".equals(propEle[i])) {
					continue;
				}
				if (value != null) {
					beanUtils.setProperty(cotGivenDetail, propEle[i], value);
				}
			} catch (Exception e) {
				logger.error(propEle[i] + ":转换错误!");
				flag = false;
				break;
			}
		}
		if (flag == false) {
			return null;
		} else {
			// List facList = getDicList("factory");
			List facList = this.getCotBaseDao().getRecords("CotFactory");
			for (int i = 0; i < facList.size(); i++) {
				CotFactory cotFactory = (CotFactory) facList.get(i);
				if (cotGivenDetail.getFactoryId() != null && cotFactory.getId().intValue() == cotGivenDetail.getFactoryId().intValue()) {
					cotGivenDetail.setFactoryShortName(cotFactory.getShortName());
				}
			}
			return cotGivenDetail;
		}
	}

	// 获取当前登陆员工信息
	public Boolean checkCurrEmpsIsSuperAd() {
		WebContext ctx = WebContextFactory.get();
		CotEmps cotEmps = (CotEmps) ctx.getSession().getAttribute("emp");
		if ("admin".equals(cotEmps.getEmpsName())) {
			return true;
		} else {
			return false;
		}
	}

	// 返回当前时间+10天后的日期
	public String addtenToCurDate(String msgBeginDate) {
		Calendar cal = Calendar.getInstance();

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		try {
			cal.setTime(sdf.parse(msgBeginDate));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		cal.add(Calendar.DATE, +10);
		String dateString = sdf.format(cal.getTime());

		return dateString;
	}

	// 批量删除时对审核通过单据的处理
	public String givenIsChecks(List<CotGiven> givenList) {
		StringBuffer givenNos = new StringBuffer();

		for (int i = 0; i < givenList.size(); i++) {
			CotGiven cotGiven = (CotGiven) givenList.get(i);
			Integer givenId = cotGiven.getId();

			CotGiven given = (CotGiven) this.getCotBaseDao().getById(CotGiven.class, givenId);
			if (given.getGivenIscheck() == 2) {
				givenNos.append(given.getGivenNo() + ",");
			}
		}
		return givenNos.toString();
	}

	// 更新送样审核状态
	public int saveGivenStatus(Integer id, Long status) {
		WebContext ctx = WebContextFactory.get();
		CotEmps cotEmps = (CotEmps) ctx.getSession().getAttribute("emp");
		CotGiven cotGiven = this.getGivenById(id);
		List<CotGiven> list = new ArrayList<CotGiven>();

		cotGiven.setGivenIscheck(status);
		cotGiven.setCotSign(null);
		// 如果状态为通过或不通过时,保存审核人姓名
		if (cotGiven.getGivenIscheck() == 1 || cotGiven.getGivenIscheck() == 2) {
			cotGiven.setCheckPerson(cotEmps.getEmpsName());
		}

		list.add(cotGiven);

		this.getCotBaseDao().updateRecords(list);
		return cotGiven.getId();

	}

	// 保存报价、订单导入明细
	public void saveGivenDetail(String gid, String eleId) {
		WebContext ctx = WebContextFactory.get();
		CotEmps cotEmps = (CotEmps) ctx.getSession().getAttribute("emp");

		List<CotGivenDetail> res = new ArrayList<CotGivenDetail>();

		TreeMap<String, CotGivenDetail> givenMap = this.getGivenMap();
		CotGivenDetail cotGivenDetail = givenMap.get(eleId.toLowerCase());
		cotGivenDetail.setGivenCount(1);
		cotGivenDetail.setTotalMoney(cotGivenDetail.getPriceOut());
		cotGivenDetail.setGivenId(Integer.parseInt(gid));
		cotGivenDetail.setAddTime(new Date(System.currentTimeMillis()));// 添加时间
		cotGivenDetail.setAddPerson(cotEmps.getEmpsName());// 操作人

		res.add(cotGivenDetail);

		try {
			this.getCotBaseDao().saveRecords(res);
		} catch (DAOException e) {
			e.printStackTrace();
		}

		// ============保存图片===============================
		List<CotGivenPic> imgList = new ArrayList<CotGivenPic>();
		// 图片操作类
		CotOpImgService impOpService = (CotOpImgService) SystemUtil.getService("CotOpImgService");

		CotGivenPic givenPic = new CotGivenPic();
		// 添加图片信息，生成图片列表
		if (cotGivenDetail.getType().equals("order")) {
			CotOrderPic orderPic = impOpService.getOrderPic(cotGivenDetail.getSrcId());
			if (orderPic != null) {
				givenPic.setEleId(orderPic.getEleId());
				givenPic.setPicImg(orderPic.getPicImg());
				givenPic.setPicName(orderPic.getPicName());
				givenPic.setPicSize(orderPic.getPicImg().length);
				givenPic.setFkId(cotGivenDetail.getId());
			}
		} else if (cotGivenDetail.getType().equals("price")) {
			CotPricePic pricePic = impOpService.getPricePic(cotGivenDetail.getSrcId());
			if (pricePic != null) {
				givenPic.setEleId(pricePic.getEleId());
				givenPic.setPicImg(pricePic.getPicImg());
				givenPic.setPicName(pricePic.getPicName());
				givenPic.setPicSize(pricePic.getPicImg().length);
				givenPic.setFkId(cotGivenDetail.getId());
			}
		} else {
			CotElePic cotElePic = impOpService.getElePicImgByEleName(eleId);
			if (cotElePic != null) {
				givenPic.setEleId(cotElePic.getEleId());
				givenPic.setPicImg(cotElePic.getPicImg());
				givenPic.setPicName(cotElePic.getPicName());
				givenPic.setPicSize(cotElePic.getPicImg().length);
				givenPic.setFkId(cotGivenDetail.getId());
			} else {
				byte[] zwtpByte = this.getCotElementsService().getZwtpPic();
				givenPic.setEleId(eleId);
				givenPic.setPicImg(zwtpByte);
				givenPic.setPicSize(zwtpByte.length);
				givenPic.setPicName(eleId);
			}
		}
		imgList.add(givenPic);

		try {
			this.getCotBaseDao().saveRecords(imgList);
		} catch (DAOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// 根据编号获取包材计算公式
	public CotBoxPacking getCalculation(Integer boxPackingId) {
		return (CotBoxPacking) this.getCotBaseDao().getById(CotBoxPacking.class, boxPackingId);
	}

	// 获取样品默认配置中的公式及利润系数
	public CotEleCfg getExpessionAndProfit() {
		List<CotEleCfg> list = new ArrayList<CotEleCfg>();
		String hql = " from CotEleCfg obj ";
		list = this.getCotBaseDao().find(hql);
		CotEleCfg cotEleCfg = (CotEleCfg) list.get(0);
		return cotEleCfg;
	}

	// 计算价格
	public String calPrice(CotGivenDetail elements, Integer boxPackingId) {
		CotBoxPacking boxPacking = this.getCalculation(boxPackingId);
		Evaluator evaluator = new Evaluator();
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
	public Float[] calPriceAllByEleId(String eleId) {
		CotGivenDetail elements = this.getGivenMapValue(eleId);
		if (elements == null) {
			return null;
		}
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
			res[4] = Float.parseFloat(dfTwo.format(all));
		}
		// 计算生产价
		res[5] = 0f;
		Float elePrice = 0.0f;
		Float eleFittingPrice = 0.0f;
		if (elements.getId() != null) {
			if (elements.getElePrice() != null) {
				elePrice = elements.getElePrice();
			}
			if (elements.getEleFittingPrice() != null) {
				eleFittingPrice = elements.getEleFittingPrice();
			}
		} else {
			if ("price".equals(elements.getType())) {
				String hql = "select obj.elePrice,obj.eleFittingPrice from CotPriceDetail obj where obj.id=" + elements.getSrcId();
				List list = this.getCotBaseDao().find(hql);
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
			if ("ele".equals(elements.getType()) || "given".equals(elements.getType())) {
				String hql = "select obj.elePrice,obj.eleFittingPrice from CotElementsNew obj where obj.id=" + elements.getSrcId();
				List list = this.getCotBaseDao().find(hql);
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
			if ("order".equals(elements.getType())) {
				String hql = "select obj.elePrice,obj.eleFittingPrice from CotOrderDetail obj where obj.id=" + elements.getId();
				List list = this.getCotBaseDao().find(hql);
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
					res[5] = Float.parseFloat(dfTwo.format(Float.parseFloat(result)));
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

	// 计算价格
	public Float[] calPriceAll(String rdm) {
		CotGivenDetail elements = this.getGivenMapValue(rdm);
		if (elements == null) {
			return null;
		}
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
			res[4] = Float.parseFloat(dfTwo.format(all));
		}
		// 计算生产价
		res[5] = 0f;
		Float elePrice = 0.0f;
		Float eleFittingPrice = 0.0f;
		if (elements.getId() != null) {
			if (elements.getElePrice() != null) {
				elePrice = elements.getElePrice();
			}
			if (elements.getEleFittingPrice() != null) {
				eleFittingPrice = elements.getEleFittingPrice();
			}
		} else {
			if ("price".equals(elements.getType())) {
				String hql = "select obj.elePrice,obj.eleFittingPrice from CotPriceDetail obj where obj.id=" + elements.getSrcId();
				List list = this.getCotBaseDao().find(hql);
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
			if ("ele".equals(elements.getType()) || "given".equals(elements.getType())) {
				String hql = "select obj.elePrice,obj.eleFittingPrice from CotElementsNew obj where obj.id=" + elements.getSrcId();
				List list = this.getCotBaseDao().find(hql);
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
			if ("order".equals(elements.getType())) {
				String hql = "select obj.elePrice,obj.eleFittingPrice from CotOrderDetail obj where obj.id=" + elements.getId();
				List list = this.getCotBaseDao().find(hql);
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
					res[5] = Float.parseFloat(dfTwo.format(Float.parseFloat(result)));
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

	// 根据包材价格调整生产价
	public Float calPriceFacByPackPrice(String eleId, String packingPrice) {
		CotGivenDetail detail = this.getGivenMapValue(eleId);
		if (detail == null) {
			return null;
		}
		CotEleCfg cotEleCfg = (CotEleCfg) this.getExpessionAndProfit();
		Float elePrice = 0.0f;
		Float eleFittingPrice = 0.0f;

		if (detail.getElePrice() != null) {
			elePrice = detail.getElePrice();
		}
		if (detail.getEleFittingPrice() != null) {
			eleFittingPrice = detail.getEleFittingPrice();
		}
		String expessionFacIn = cotEleCfg.getExpessionFacIn();
		// 定义jeavl对象,用于计算字符串公式
		Evaluator evaluator = new Evaluator();
		evaluator.putVariable("elePrice", elePrice.toString());
		evaluator.putVariable("eleFittingPrice", eleFittingPrice.toString());
		evaluator.putVariable("packingPrice", packingPrice);
		Float res = null;
		try {
			if (expessionFacIn == null || expessionFacIn.trim().equals("")) {
				res = detail.getPriceFac();
			} else {
				String result = evaluator.evaluate(expessionFacIn);
				res = Float.parseFloat(result);
			}
			detail.setPriceFac(res);
			detail.setPackingPrice(Float.parseFloat(packingPrice));
			this.setGivenMap(eleId, detail);
		} catch (EvaluationException e) {
			e.printStackTrace();
		}
		return res;
	}

	// 根据客号查找报价明细表中的货号(取最近报价时间)
	public Object[] findEleByCustNo(String custNo, Integer custId, String gTime) {
		DateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		java.util.Date ptimeDate = null;
		try {
			ptimeDate = format1.parse(gTime);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		Object[] obj = new Object[3];
		obj[0] = custId;
		obj[1] = custNo;
		obj[2] = ptimeDate;
		Object[] rtn = new Object[2];
		// 报价
		String hql = "select obj,p.priceTime from CotPrice p,CotPriceDetail obj,CotCustomer c " + "where obj.priceId=p.id and p.custId=c.id and c.id=?" + " and obj.custNo=?" + " and p.priceTime<=?" + " order by p.priceTime desc,p.id desc,obj.id desc limit 0,1";
		List<?> list = this.getCotBaseDao().queryForLists(hql, obj);
		// 订单
		String hqlOrder = "select obj,p.orderTime from CotOrder p,CotOrderDetail obj,CotCustomer c " + "where obj.orderId=p.id and p.custId=c.id and c.id=?" + " and obj.custNo=?" + " and p.orderTime<=?" + " order by p.orderTime desc,p.id desc,obj.id desc limit 0,1";
		List<?> listOrder = this.getCotBaseDao().queryForLists(hqlOrder, obj);

		// 送样
		String hqlGiven = "select obj,p.givenTime from CotGiven p,CotGivenDetail obj,CotCustomer c " + "where obj.givenId=p.id and p.custId=c.id and c.id=?" + " and obj.custNo=?" + " and p.givenTime<=?" + " order by p.givenTime desc,p.id desc,obj.id desc limit 0,1";
		List<?> listGiven = this.getCotBaseDao().queryForLists(hqlGiven, obj);

		Timestamp priceTime = null;
		Timestamp orderTime = null;
		Timestamp givenTime = null;
		TreeMap sort = new TreeMap();
		if (list.size() > 0) {
			Object[] detail = (Object[]) list.get(0);
			if (detail[1] != null) {
				priceTime = (Timestamp) detail[1];
				sort.put(priceTime.getTime(), 1);
			}
		}
		if (listOrder.size() > 0) {
			Object[] detailOrder = (Object[]) listOrder.get(0);
			if (detailOrder[1] != null) {
				orderTime = (Timestamp) detailOrder[1];
				sort.put(orderTime.getTime(), 2);
			}
		}
		if (listGiven.size() > 0) {
			Object[] detailGiven = (Object[]) listGiven.get(0);
			if (detailGiven[1] != null) {
				givenTime = (Timestamp) detailGiven[1];
				sort.put(givenTime.getTime(), 3);
			}
		}
		if (sort.keySet().size() == 0) {
			return null;
		}
		Integer flag = (Integer) sort.get(sort.lastKey());
		if (flag == 1) {
			Object[] objTemp = (Object[]) list.get(0);
			CotPriceDetail detail = (CotPriceDetail) objTemp[0];
			// // 填充厂家简称
			// List<?> facList = this.getCotBaseDao().getRecords("CotFactory");
			// for (int i = 0; i < facList.size(); i++) {
			// CotFactory cotFactory = (CotFactory) facList.get(i);
			// if (detail.getFactoryId() != null
			// && cotFactory.getId().intValue() == detail
			// .getFactoryId().intValue()) {
			// detail.setFactoryShortName(cotFactory.getShortName());
			// }
			// }
			rtn[0] = 1;
			rtn[1] = detail;
			return rtn;
		} else if (flag == 2) {
			Object[] objTemp = (Object[]) listOrder.get(0);
			CotOrderDetail detail = (CotOrderDetail) objTemp[0];
			// // 填充厂家简称
			// // List<?> facList = getDicList("factory");
			// List<?> facList = this.getCotBaseDao().getRecords("CotFactory");
			// for (int i = 0; i < facList.size(); i++) {
			// CotFactory cotFactory = (CotFactory) facList.get(i);
			// if (detail.getFactoryId() != null
			// && cotFactory.getId().intValue() == detail
			// .getFactoryId().intValue()) {
			// detail.setFactoryShortName(cotFactory.getShortName());
			// }
			// }
			rtn[0] = 2;
			rtn[1] = detail;
			return rtn;
		} else {
			Object[] objTemp = (Object[]) listGiven.get(0);
			CotGivenDetail detail = (CotGivenDetail) objTemp[0];
			// // 填充厂家简称
			// // List<?> facList = getDicList("factory");
			// List<?> facList = this.getCotBaseDao().getRecords("CotFactory");
			// for (int i = 0; i < facList.size(); i++) {
			// CotFactory cotFactory = (CotFactory) facList.get(i);
			// if (detail.getFactoryId() != null
			// && cotFactory.getId().intValue() == detail
			// .getFactoryId().intValue()) {
			// detail.setFactoryShortName(cotFactory.getShortName());
			// }
			// }
			rtn[0] = 3;
			rtn[1] = detail;
			return rtn;
		}
	}

	// 查询所有币种
	public Map<?, ?> getCurrencyMap(HttpServletRequest request) {
		Map<String, String> map = new HashMap<String, String>();
		Map<?, ?> dicMap = (Map<?, ?>) request.getSession().getAttribute("sysdic");
		List<?> list = (List<?>) dicMap.get("currency");
		for (int i = 0; i < list.size(); i++) {
			CotCurrency cotCurrency = (CotCurrency) list.get(i);
			map.put(cotCurrency.getId().toString(), cotCurrency.getCurNameEn());
		}
		return map;
	}

	// 判断样品单费用和快递费用是否添加
	public int findIsExist(Integer gId) {
		String hql = "select obj.finaceName from CotFinaceAccountrecv obj where obj.source='given' and obj.fkId=" + gId;
		List list = this.getCotBaseDao().find(hql);
		boolean flag = false;
		boolean temp = false;
		for (int i = 0; i < list.size(); i++) {
			String finaceName = (String) list.get(i);
			if (finaceName.equals("样品费用")) {
				flag = true;
			}
			if (finaceName.equals("快递费用")) {
				temp = true;
			}
		}
		if (flag == true && temp == false) {
			return 1;
		}
		if (flag == false && temp == true) {
			return 2;
		}
		if (flag == true && temp == true) {
			return 3;
		}
		return 0;
	}

	// 生成应收帐款单号
	public String createRecvNo(Integer custId) {
		// Map idMap = new HashMap<String, Integer>();
		// idMap.put("KH", custId);
		// GenAllSeq seq = new GenAllSeq();
		// String finaceNo = seq.getAllSeqByType("fincaeaccountrecvNo", idMap);
		CotSeqService seq = new CotSeqServiceImpl();
		String finaceNo = seq.getFinaceNeeRecGivenNo(custId);
		return finaceNo;
	}

	// 保存应收帐款单号
	public void savaSeq() {
		// GenAllSeq seq = new GenAllSeq();
		CotSeqService seq = new CotSeqServiceImpl();
		seq.saveSeq("fincaeaccountrecv");
	}

	// 生成应收款
	public boolean saveRecv(Integer gId, Integer flag, Integer curId, Integer currencyId, String sampleFeeStr, String signTotalPriceStr, Integer sampleFeeCheck, Integer checkFee) {
		WebContext ctx = WebContextFactory.get();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
		Integer empId = (Integer) ctx.getSession().getAttribute("empId");
		CotGiven given = this.getGivenById(gId);

		Double sampleFee = 0.0;
		if (!"".equals(sampleFeeStr)) {
			sampleFee = Double.parseDouble(sampleFeeStr);
		}
		Double signTotalPrice = 0.0;
		if (!"".equals(signTotalPriceStr)) {
			signTotalPrice = Double.parseDouble(signTotalPriceStr);
		}

		given.setCurId(curId);
		given.setCurrencyId(currencyId);
		given.setSampleFee(sampleFee);
		given.setSignTotalPrice(signTotalPrice);
		given.setSampleFeeCheck(sampleFeeCheck);
		given.setCheckFee(checkFee);

		if ((flag == 1 || flag == 0) && checkFee == 1) {
			String finaceNo = this.createRecvNo(given.getCustId());
			CotFinaceAccountrecv accountrecv = new CotFinaceAccountrecv();
			accountrecv.setFinaceNo(finaceNo);
			accountrecv.setFinaceName("快递费用");
			accountrecv.setAmount(given.getSignTotalPrice());
			accountrecv.setRealAmount(0.0);
			accountrecv.setRemainAmount(given.getSignTotalPrice());
			accountrecv.setCurrencyId(given.getCurrencyId());
			accountrecv.setCustId(given.getCustId());
			accountrecv.setSource("given");
			accountrecv.setOrderNo(given.getGivenNo());
			accountrecv.setFkId(given.getId());
			accountrecv.setBusinessPerson(given.getBussinessPerson());
			accountrecv.setStatus(0);
			accountrecv.setEmpId(empId);
			accountrecv.setAmountDate(given.getGivenTime());
			accountrecv.setAddDate(new java.util.Date());
			accountrecv.setCompanyId(given.getCompanyId());
			accountrecv.setZhRemainAmount(given.getSignTotalPrice());
			List list = new ArrayList();
			list.add(accountrecv);
			try {
				this.getCotBaseDao().saveRecords(list);
				this.savaSeq();
			} catch (DAOException e) {
				e.printStackTrace();
				return false;
			}
		}
		if ((flag == 2 || flag == 0) && sampleFeeCheck == 1) {
			String finaceNo = this.createRecvNo(given.getCustId());
			CotFinaceAccountrecv accountrecv = new CotFinaceAccountrecv();
			accountrecv.setFinaceNo(finaceNo);
			accountrecv.setFinaceName("样品费用");
			accountrecv.setAmount(given.getSampleFee());
			accountrecv.setRealAmount(0.0);
			accountrecv.setRemainAmount(given.getSampleFee());
			accountrecv.setCurrencyId(given.getCurId());
			accountrecv.setCustId(given.getCustId());
			accountrecv.setSource("given");
			accountrecv.setOrderNo(given.getGivenNo());
			accountrecv.setFkId(given.getId());
			accountrecv.setBusinessPerson(given.getBussinessPerson());
			accountrecv.setStatus(0);
			accountrecv.setEmpId(empId);
			accountrecv.setAmountDate(given.getGivenTime());
			accountrecv.setAddDate(new java.util.Date());
			accountrecv.setCompanyId(given.getCompanyId());
			accountrecv.setZhRemainAmount(given.getSampleFee());
			List list = new ArrayList();
			list.add(accountrecv);
			try {
				this.getCotBaseDao().saveRecords(list);
				this.savaSeq();
			} catch (DAOException e) {
				e.printStackTrace();
				return false;
			}
		}
		try {
			// 保存送样单
			List listGiven = new ArrayList();
			listGiven.add(given);
			this.getCotBaseDao().updateRecords(listGiven);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	// 判断应收帐是否有收款记录
	public boolean checkIsShou(List<Integer> ids) {
		String idsStr = "";
		for (int i = 0; i < ids.size(); i++) {
			idsStr += ids.get(i) + ",";
		}
		String tempHql = "select obj.recvId from CotFinacerecvDetail obj where obj.recvId in (" + idsStr.substring(0, idsStr.length() - 1) + ")";
		List list = this.getCotBaseDao().find(tempHql);
		if (list.size() > 0) {
			return true;
		} else {
			return false;
		}
	}

	// 删除应收帐
	public Boolean deleteByAccount(List ids) {
		try {
			this.getCotBaseDao().deleteRecordByIds(ids, "CotFinaceAccountrecv");
			return true;
		} catch (DAOException e) {
			e.printStackTrace();
			return false;
		}
	}

	// 根据条件查询冲帐明细记录
	public List<?> getRecvDetailList(List<?> list) {
		List<CotFinacerecvDetail> listVo = new ArrayList<CotFinacerecvDetail>();
		for (int i = 0; i < list.size(); i++) {
			Object[] obj = (Object[]) list.get(i);
			CotFinacerecvDetail detail = (CotFinacerecvDetail) obj[0];
			detail.setFinaceNo((String) obj[1]);
			listVo.add(detail);
		}
		return listVo;
	}

	// 根据币种换算价格
	public float updatePrice(Float price, Integer oldCurId, Integer newCurId) {
		if (price == null || oldCurId == null) {
			return 0;
		}
		// 先根据该的币种的汇率转成人民币,
		CotCurrency oldCur = (CotCurrency) this.getCotBaseDao().getById(CotCurrency.class, oldCurId);
		Float rmb = price * oldCur.getCurRate();
		// 在根据币种的汇率转成该币种的值
		CotCurrency newCur = (CotCurrency) this.getCotBaseDao().getById(CotCurrency.class, newCurId);
		Float obj = rmb / newCur.getCurRate();

		DecimalFormat nbf = new DecimalFormat("0.000");
		obj = Float.parseFloat(nbf.format(obj));
		return obj;
	}

	// 删除收款记录
	public Boolean deleteByRecvDetail(List<Integer> ids) {
		Integer mainId = 0;
		float allAmou = 0f;
		List<CotFinaceAccountrecv> accountList = new ArrayList<CotFinaceAccountrecv>();
		for (int i = 0; i < ids.size(); i++) {
			CotFinacerecvDetail finacerecvDetail = (CotFinacerecvDetail) this.getCotBaseDao().getById(CotFinacerecvDetail.class, ids.get(i));
			mainId = finacerecvDetail.getFinaceRecvid();
			allAmou += finacerecvDetail.getCurrentAmount();
			// 将货款转回应收帐
			CotFinaceAccountrecv acOld = (CotFinaceAccountrecv) this.getCotBaseDao().getById(CotFinaceAccountrecv.class, finacerecvDetail.getRecvId());
			CotFinaceAccountrecv ac = (CotFinaceAccountrecv) SystemUtil.deepClone(acOld);
			// 币种转换
			float mon = this.updatePrice(finacerecvDetail.getCurrentAmount().floatValue(), finacerecvDetail.getCurrencyId(), ac.getCurrencyId());
			ac.setRealAmount(ac.getRealAmount() - mon);
			ac.setRemainAmount(ac.getRemainAmount() + mon);
			if ("预收货款".equals(ac.getFinaceName())) {
				ac.setZhRemainAmount(ac.getZhRemainAmount() - mon);
			} else {
				ac.setZhRemainAmount(ac.getZhRemainAmount() + mon);
			}
			ac.setStatus(0);
			accountList.add(ac);
		}
		if (accountList.size() > 0) {
			this.getCotBaseDao().updateRecords(accountList);
		}
		// 修改主单的未冲帐金额
		List<CotFinacerecv> recvList = new ArrayList<CotFinacerecv>();
		CotFinacerecv recvOld = (CotFinacerecv) this.getCotBaseDao().getById(CotFinacerecv.class, mainId);
		CotFinacerecv recv = (CotFinacerecv) SystemUtil.deepClone(recvOld);
		recv.setRemainAmount(recv.getRemainAmount() + allAmou);
		recvList.add(recv);
		try {
			this.getCotBaseDao().deleteRecordByIds(ids, "CotFinacerecvDetail");
			this.getCotBaseDao().updateRecords(recvList);
		} catch (DAOException e) {
			e.printStackTrace();
			logger.error("删除其他费用异常", e);
			return false;
		}
		return true;
	}

	// 获取默认公司ID
	public Integer getDefaultCompanyId() {
		List<?> ids = this.getCotBaseDao().find("select obj.id from CotCompany obj " + "where obj.companyIsdefault=1");
		if (ids != null & ids.size() > 0) {
			return (Integer) ids.get(0);
		}
		return null;
	}

	// 计算生产价
	public Float sumPriceFac(String expessionFacIn, CotGivenDetail detail) {
		DecimalFormat dfTwo = new DecimalFormat("#.00");
		if (expessionFacIn == null || expessionFacIn.trim().equals("")) {
			return 0f;
		}
		// 定义jeavl对象,用于计算字符串公式
		Evaluator evaluator = new Evaluator();
		Float elePrice = 0f;
		if (detail.getElePrice() != null) {
			elePrice = detail.getElePrice();
		}
		Float fitPrice = 0f;
		if (detail.getEleFittingPrice() != null) {
			fitPrice = detail.getEleFittingPrice();
		}
		Float packPrice = 0f;
		if (detail.getPackingPrice() != null) {
			packPrice = detail.getPackingPrice();
		}
		evaluator.putVariable("elePrice", elePrice.toString());
		evaluator.putVariable("eleFittingPrice", fitPrice.toString());
		evaluator.putVariable("packingPrice", packPrice.toString());
		try {
			String result = evaluator.evaluate(expessionFacIn);
			return Float.parseFloat(dfTwo.format(Float.parseFloat(result)));
		} catch (EvaluationException e) {
			e.printStackTrace();
			return 0f;
		}

	}

	// 计算外销价
	public Float sumPriceOut(String expessionIn, Float priceFac, CotGivenDetail detail) {
		DecimalFormat df = new DecimalFormat("#.00");
		if (expessionIn == null || expessionIn.trim().equals("")) {
			return 0f;
		}
		// 利润率
		Float lirun = 0f;
		if (detail.getLiRun() != null) {
			lirun = detail.getLiRun();
		}
		// 汇率
		Float rate = 0f;
		if (detail.getPriceOutUint() != null) {
			rate = this.getReportDao().getCurRate(detail.getPriceOutUint());
		}
		// 退税率
		Float tui = 0f;
		if (detail.getTuiLv() != null) {
			tui = detail.getTuiLv();
		}
		// CBM
		Float cb = 0f;
		if (detail.getBoxCbm() != null) {
			cb = detail.getBoxCbm();
		}
		// 外包装
		Long bc = 0l;
		if (detail.getBoxObCount() != null) {
			bc = detail.getBoxObCount();
		}

		// 定义jeavl对象,用于计算字符串公式
		Evaluator evaluator = new Evaluator();
		evaluator.putVariable("priceProfit", lirun.toString());
		evaluator.putVariable("priceFac", priceFac.toString());
		evaluator.putVariable("priceRate", rate.toString());
		evaluator.putVariable("tuiLv", tui.toString());
		evaluator.putVariable("cbm", cb.toString());
		evaluator.putVariable("boxObCount", bc.toString());
		Float res = null;
		try {
			String result = evaluator.evaluate(expessionIn);
			res = Float.parseFloat(df.format(Float.parseFloat(result)));
			return res;
		} catch (EvaluationException e) {
			e.printStackTrace();
			return 0f;
		}
	}

	// 查找该产品是否有送样、报价、订单
	@SuppressWarnings("unchecked")
	public Object findIsExistDetail(String eleId, String id, String type) {

		boolean flag = this.checkExist(eleId);
		if (flag) {
			return null;
		}
		Object rtn = new Object();
		String hql = "";

		if ("given".equals(type)) {
			// 送样
			hql = "select obj from CotGivenDetail obj where obj.id =" + id;
		} else if ("price".equals(type)) {
			// 报价
			hql = "select obj from CotPriceDetail obj where obj.id =" + id;
		} else if ("order".equals(type)) {
			// 订单
			hql = "select obj from CotOrderDetail obj where obj.id =" + id;
		} else {
			// 样品
			hql = "select obj from CotElementsNew obj where obj.id =" + id;
		}

		List<?> list = this.getCotBaseDao().find(hql);

		if ("price".equals(type)) {
			Object objTemp = (Object) list.get(0);
			CotPriceDetail detail = (CotPriceDetail) objTemp;
			detail.setPicImg(null);
			// 填充厂家简称
			List<?> facList = this.getCotBaseDao().getRecords("CotFactory");
			for (int i = 0; i < facList.size(); i++) {
				CotFactory cotFactory = (CotFactory) facList.get(i);
				if (detail.getFactoryId() != null && cotFactory.getId().intValue() == detail.getFactoryId().intValue()) {
					detail.setFactoryShortName(cotFactory.getShortName());
				}
			}
			rtn = detail;
		} else if ("order".equals(type)) {
			Object objTemp = (Object) list.get(0);
			CotOrderDetail detail = (CotOrderDetail) objTemp;
			detail.setPicImg(null);
			// 填充厂家简称
			List<?> facList = this.getCotBaseDao().getRecords("CotFactory");
			for (int i = 0; i < facList.size(); i++) {
				CotFactory cotFactory = (CotFactory) facList.get(i);
				if (detail.getFactoryId() != null && cotFactory.getId().intValue() == detail.getFactoryId().intValue()) {
					detail.setFactoryShortName(cotFactory.getShortName());
				}
			}
			rtn = detail;
		} else if ("given".equals(type)) {
			Object objTemp = (Object) list.get(0);
			CotGivenDetail detail = (CotGivenDetail) objTemp;
			// 填充厂家简称
			List<?> facList = this.getCotBaseDao().getRecords("CotFactory");
			for (int i = 0; i < facList.size(); i++) {
				CotFactory cotFactory = (CotFactory) facList.get(i);
				if (detail.getFactoryId() != null && cotFactory.getId().intValue() == detail.getFactoryId().intValue()) {
					detail.setFactoryShortName(cotFactory.getShortName());
				}
			}
			rtn = detail;
		} else if ("ele".equals(type)) {
			Object objTemp = (Object) list.get(0);
			CotElementsNew detail = (CotElementsNew) objTemp;
			// 填充厂家简称
			List<?> facList = this.getCotBaseDao().getRecords("CotFactory");
			for (int i = 0; i < facList.size(); i++) {
				CotFactory cotFactory = (CotFactory) facList.get(i);
				if (detail.getFactoryId() != null && cotFactory.getId().intValue() == detail.getFactoryId().intValue()) {
					detail.setFacShortName(cotFactory.getShortName());
				}
			}
			rtn = detail;
		}
		return rtn;

	}

	// 根据上传的盘点机数据存入内存
	public void saveCheckMachine(List<String> list, HttpServletRequest request) {
		Map<String, List<CotGivenDetail>> map = new HashMap<String, List<CotGivenDetail>>();
		String[] properties = ReflectionUtils.getDeclaredFields(CotElementsNew.class);
		// 获得币种为美元的编号
		String sqlCur = "select obj.id from CotCurrency obj where obj.curNameEn='USD'";
		List<?> listCur = this.getCotBaseDao().find(sqlCur);
		// 如果没有名称为USD的币种,不能导入
		if (listCur == null || listCur.size() == 0) {
			map = null;
			return;
		}
		DecimalFormat df3 = new DecimalFormat("#.000");
		DecimalFormat dfTwo = new DecimalFormat("#.00");
		Integer curUSD = (Integer) listCur.get(0);
		try {
			for (int i = 0; i < list.size(); i++) {
				String temp = list.get(i);
				String[] price = temp.split(",");
				map.put(price[0].trim(), null);
			}
			for (Iterator<?> itor = map.keySet().iterator(); itor.hasNext();) {
				String field = (String) itor.next();
				List<CotGivenDetail> listDetail = new ArrayList<CotGivenDetail>();
				for (int i = 0; i < list.size(); i++) {
					String temp = list.get(i);
					String[] price = temp.split(",");
					if (field.equals(price[0].trim())) {
						String sql = "from CotElementsNew obj where obj.eleId='" + price[1].trim() + "'";
						List<?> listOld = this.getCotBaseDao().find(sql);
						if (listOld.size() > 0) {
							CotElementsNew cotElements = (CotElementsNew) listOld.get(0);
							CotGivenDetail givenDetail = new CotGivenDetail();
							for (int j = 0; j < properties.length; j++) {
								if ("cotPictures".equals(properties[j]) || "id".equals(properties[j]) || "cotFile".equals(properties[j]) || "childs".equals(properties[j]) || "picImg".equals(properties[j]) || "cotPriceFacs".equals(properties[j]) || "cotElePrice".equals(properties[j]) || "cotEleFittings".equals(properties[j]) || "eleFittingPrice".equals(properties[j]) || "cotElePacking".equals(properties[j]) || "packingPrice".equals(properties[j]))
									continue;

								String value = BeanUtils.getProperty(cotElements, properties[j]);
								if (value != null) {
									BeanUtils.setProperty(givenDetail, properties[j], value);
								}
							}
							// 单价
							Float priceOut = Float.parseFloat(price[2].trim());

							givenDetail.setPriceOut(priceOut);
							givenDetail.setTotalMoney(Float.parseFloat(dfTwo.format(1 * givenDetail.getPriceOut())));
							if (givenDetail.getFactoryId() != null && !"".equals(givenDetail.getFactoryId())) {
								CotFactory factory = getFactoryById(givenDetail.getFactoryId());
								givenDetail.setFactoryShortName(factory.getShortName());
							} else {
								String hql = "from CotEleCfg obj";
								List<?> res = this.getCotBaseDao().find(hql);
								if (res.size() >= 1) {
									CotEleCfg cfg = (CotEleCfg) res.get(0);
									if (cfg.getEleFacId() != null && !"".equals(cfg.getEleFacId())) {
										CotFactory factory = getFactoryById(cfg.getEleFacId());
										givenDetail.setFactoryShortName(factory.getShortName());
										givenDetail.setFactoryId(cfg.getEleFacId());
									}
								} else {
									continue;
								}
							}
							givenDetail.setId(null);
							givenDetail.setGivenCount(1);
							givenDetail.setSignCount(1);
							givenDetail.setType("ele");
							givenDetail.setSrcId(cotElements.getId());
							listDetail.add(givenDetail);
						}
					}
				}
				map.put(field, listDetail);
			}
		} catch (Exception e) {
			e.printStackTrace();
			map = null;
		}
		request.getSession().setAttribute("CheckGivenMachine", map);
	}

	// 得到盘点机的内存map的key集合
	public void savePanList(String no, Integer givenId) {
		// 取得内存中的最大排序号
		Object order = SystemUtil.getObjBySession(null, "given");
		int temp = 0;
		if (order != null) {
			TreeMap<String, CotGivenDetail> givenMap = (TreeMap<String, CotGivenDetail>) order;
			Iterator<?> it = givenMap.keySet().iterator();
			while (it.hasNext()) {
				String eleId = (String) it.next();
				CotGivenDetail givenDetail = givenMap.get(eleId.toLowerCase());
				if (givenDetail.getSortNo() != null && givenDetail.getSortNo() > temp) {
					temp = givenDetail.getSortNo();
				}
			}
		}

		WebContext ctx = WebContextFactory.get();
		Object obj = ctx.getSession().getAttribute("CheckGivenMachine");
		Map<String, List<CotGivenDetail>> map = (Map<String, List<CotGivenDetail>>) obj;
		List<CotGivenDetail> list = map.get(no);
		for (int i = 0; i < list.size(); i++) {
			temp++;
			CotGivenDetail givenDetail = list.get(i);
			givenDetail.setSortNo(temp);
			givenDetail.setGivenId(givenId);
			// this.setGivenMapAndDel(givenDetail.getEleId(), givenDetail);
		}
		this.addGivenDetails(list);
		CotOpImgService impOpService = (CotOpImgService) SystemUtil.getService("CotOpImgService");
		byte[] zwtpByte = this.getCotElementsService().getZwtpPic();
		// 保存图片,图片取样品档案的
		for (int i = 0; i < list.size(); i++) {
			CotGivenDetail detail = list.get(i);
			// 新建图片
			CotGivenPic pricePic = new CotGivenPic();
			pricePic.setFkId(detail.getId());
			if (detail.getType().equals("ele")) {
				CotElePic cotElePic = impOpService.getElePicImgByEleId(detail.getSrcId());
				if (cotElePic != null) {
					pricePic.setEleId(cotElePic.getEleId());
					pricePic.setPicImg(cotElePic.getPicImg());
					pricePic.setPicSize(cotElePic.getPicImg().length);
					pricePic.setPicName(cotElePic.getEleId());
				} else {
					pricePic.setEleId(detail.getEleId());
					pricePic.setPicImg(zwtpByte);
					pricePic.setPicSize(zwtpByte.length);
					pricePic.setPicName(detail.getEleId());
				}
			}
			// 添加到图片数组
			List<CotGivenPic> imgList = new ArrayList<CotGivenPic>();
			imgList.add(pricePic);
			// 逐条添加，避免数据量大的时候，内存溢出
			impOpService.saveImg(imgList);
		}
	}

	// 储存Map
	public void setGivenMapAndDel(String eleId, CotGivenDetail givenDetail) {
		Object obj = SystemUtil.getObjBySession(null, "given");
		if (obj == null) {
			TreeMap<String, CotGivenDetail> givenMap = new TreeMap<String, CotGivenDetail>();
			givenMap.put(eleId.toLowerCase(), givenDetail);
			SystemUtil.setObjBySession(null, givenMap, "given");
		} else {
			TreeMap<String, CotGivenDetail> givenMap = (TreeMap<String, CotGivenDetail>) obj;
			boolean flag = false;
			Iterator<?> it = givenMap.keySet().iterator();
			while (it.hasNext()) {
				String temp = (String) it.next();
				CotGivenDetail detail = givenMap.get(temp);
				if (givenDetail.getEleId().toLowerCase().equals(detail.getEleId().toLowerCase())) {
					flag = true;
					break;
				}
			}
			if (flag == false) {
				givenMap.put(eleId.toLowerCase(), givenDetail);
			}
			SystemUtil.setObjBySession(null, givenMap, "given");
		}
	}

	// 从session中取得上传的盘点机流水号
	@SuppressWarnings("unchecked")
	public List<String> getMachineNum() {
		List<String> list = new ArrayList<String>();
		WebContext ctx = WebContextFactory.get();
		Object obj = ctx.getSession().getAttribute("CheckGivenMachine");
		if (obj != null) {
			Map<String, List<CotGivenDetail>> map = (Map<String, List<CotGivenDetail>>) obj;
			for (Iterator<?> itor = map.keySet().iterator(); itor.hasNext();) {
				String field = (String) itor.next();
				list.add(field);
			}
		}
		return list;
	}

	// 根据盘点机流水号获得明细字符串集合
	@SuppressWarnings("unchecked")
	public List<String> getMachineDetails(String checkNo) {
		List<String> list = new ArrayList<String>();
		WebContext ctx = WebContextFactory.get();
		Object obj = ctx.getSession().getAttribute("CheckGivenMachine");
		if (obj != null) {
			Map<String, List<CotGivenDetail>> map = (Map<String, List<CotGivenDetail>>) obj;
			List<CotGivenDetail> listDetail = map.get(checkNo);
			for (int i = 0; i < listDetail.size(); i++) {
				CotGivenDetail givenDetail = (CotGivenDetail) listDetail.get(i);
				String detail = givenDetail.getEleId() + "---" + givenDetail.getPriceOut() + "---" + 1;
				list.add(detail);
			}
		}
		return list;
	}

	// 根据盘点机流水号获得明细对象集合
	@SuppressWarnings("unchecked")
	public List<CotGivenDetail> getMachineDetailList(String checkNo) {
		WebContext ctx = WebContextFactory.get();
		Object obj = ctx.getSession().getAttribute("CheckGivenMachine");
		if (obj != null) {
			Map<String, List<CotGivenDetail>> map = (Map<String, List<CotGivenDetail>>) obj;
			List<CotGivenDetail> listDetail = map.get(checkNo);
			return listDetail;
		}
		return null;
	}

	public String getJsonData(QueryInfo queryInfo) throws DAOException {
		return this.getCotBaseDao().getJsonData(queryInfo);
	}

	public CotCustomerService getCustService() {
		return custService;
	}

	public void setCustService(CotCustomerService custService) {
		this.custService = custService;
	}

	public CotReportDao getReportDao() {
		return reportDao;
	}

	public void setReportDao(CotReportDao reportDao) {
		this.reportDao = reportDao;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sail.cot.service.given.CotGivenService#updateSortNo(java.lang.Integer,
	 *      java.lang.Integer, java.lang.String, java.lang.Integer)
	 */
	public boolean updateSortNo(Integer id, Integer type, String field, String fieldType) {
		WebContext ctx = WebContextFactory.get();
		Map<String, CotGivenDetail> givenMap = this.getGivenMapAction(ctx.getSession());
		List list = new ArrayList();
		Iterator<?> it = givenMap.keySet().iterator();
		while (it.hasNext()) {
			String key = (String) it.next();
			CotGivenDetail detail = givenMap.get(key);
			list.add(detail);
		}
		ListSort listSort = new ListSort();
		listSort.setField(field);
		listSort.setFieldType(fieldType);
		listSort.setTbName("CotGivenDetail");
		if (type.intValue() == 0) {
			listSort.setType(true);
		} else {
			listSort.setType(false);
		}

		Collections.sort(list, listSort);
		List listNew = new ArrayList();
		for (int i = 0; i < list.size(); i++) {
			CotGivenDetail detail = (CotGivenDetail) list.get(i);
			detail.setSortNo(i + 1);
			listNew.add(detail);
		}
		this.getCotBaseDao().updateRecords(listNew);
		return true;
	}

	// 根据同步选择项同步更新样品
	public CotElementsNew setEleByTong(CotElementsNew old, CotGivenDetail newEle, String eleStr, String boxStr, String otherStr) {
		old.setEleId(newEle.getEleId());

		if (eleStr != null && !"".equals(eleStr)) {
			String[] eleAry = eleStr.split(",");
			for (int i = 0; i < eleAry.length; i++) {
				if (eleAry[i].equals("eleTypeidLv1")) {
					old.setEleTypeidLv1(newEle.getEleTypeidLv1());
				} else if (eleAry[i].equals("eleUnit")) {
					old.setEleUnit(newEle.getEleUnit());
				} else if (eleAry[i].equals("eleFlag")) {
					old.setEleFlag(newEle.getEleFlag());
				} else if (eleAry[i].equals("eleUnitNum")) {
					old.setEleUnitNum(newEle.getEleUnitNum());
				} else if (eleAry[i].equals("eleNameEn")) {
					old.setEleNameEn(newEle.getEleNameEn());
				} else if (eleAry[i].equals("factoryId")) {
					old.setFactoryId(newEle.getFactoryId());
				} else if (eleAry[i].equals("factoryNo")) {
					old.setFactoryNo(newEle.getFactoryNo());
				} else if (eleAry[i].equals("eleName")) {
					old.setEleName(newEle.getEleName());
				} else if (eleAry[i].equals("eleFrom")) {
					old.setEleFrom(newEle.getEleFrom());
				} else if (eleAry[i].equals("eleCol")) {
					old.setEleCol(newEle.getEleCol());
				} else if (eleAry[i].equals("eleProTime")) {
					old.setEleProTime(newEle.getEleProTime());
				} else if (eleAry[i].equals("eleForPerson")) {
					old.setEleForPerson(newEle.getEleForPerson());
				} else if (eleAry[i].equals("eleGrade")) {
					old.setEleGrade(newEle.getEleGrade());
				} else if (eleAry[i].equals("eleMod")) {
					old.setEleMod(newEle.getEleMod());
				} else if (eleAry[i].equals("eleTypeidLv2")) {
					old.setEleTypeidLv2(newEle.getEleTypeidLv2());
				} else if (eleAry[i].equals("eleTypenameLv2")) {
					old.setEleTypenameLv2(newEle.getEleTypenameLv2());
				} else if (eleAry[i].equals("eleHsid")) {
					old.setEleHsid(newEle.getEleHsid());
				} else if (eleAry[i].equals("boxWeigth")) {
					old.setBoxWeigth(newEle.getBoxWeigth());
				} else if (eleAry[i].equals("cube")) {
					old.setCube(newEle.getCube());
				} else if (eleAry[i].equals("priceFac")) {
					old.setPriceFac(newEle.getPriceFac());
					old.setPriceFacUint(newEle.getPriceFacUint());
				} else if (eleAry[i].equals("priceOut")) {
					old.setPriceOut(newEle.getPriceOut());
					old.setPriceOutUint(newEle.getPriceOutUint());
				} else if (eleAry[i].equals("tuiLv")) {
					old.setTuiLv(newEle.getTuiLv());
				} else if (eleAry[i].equals("liRun")) {
					old.setLiRun(newEle.getLiRun());
				} else if (eleAry[i].equals("barcode")) {
					old.setBarcode(newEle.getBarcode());
				} else if (eleAry[i].equals("eleDesc")) {
					old.setEleDesc(newEle.getEleDesc());
				} else if (eleAry[i].equals("eleRemark")) {
					old.setEleRemark(newEle.getEleRemark());
				}
			}
		}

		if (boxStr != null && !"".equals(boxStr)) {
			String[] boxAry = boxStr.split(",");
			for (int i = 0; i < boxAry.length; i++) {
				if (boxAry[i].equals("boxL")) {
					old.setBoxL(newEle.getBoxL());
					old.setBoxLInch(newEle.getBoxLInch());
				} else if (boxAry[i].equals("boxW")) {
					old.setBoxW(newEle.getBoxW());
					old.setBoxWInch(newEle.getBoxWInch());
				} else if (boxAry[i].equals("boxH")) {
					old.setBoxH(newEle.getBoxH());
					old.setBoxHInch(newEle.getBoxHInch());
				} else if (boxAry[i].equals("boxTypeId")) {
					old.setBoxTypeId(newEle.getBoxTypeId());
				} else if (boxAry[i].equals("boxPbL")) {
					old.setBoxPbL(newEle.getBoxPbL());
					old.setBoxPbLInch(newEle.getBoxPbLInch());
				} else if (boxAry[i].equals("boxPbW")) {
					old.setBoxPbW(newEle.getBoxPbW());
					old.setBoxPbWInch(newEle.getBoxPbWInch());
				} else if (boxAry[i].equals("boxPbH")) {
					old.setBoxPbH(newEle.getBoxPbH());
					old.setBoxPbHInch(newEle.getBoxPbHInch());
				} else if (boxAry[i].equals("boxPbCount")) {
					old.setBoxPbCount(newEle.getBoxPbCount());
				} else if (boxAry[i].equals("boxPbTypeId")) {
					old.setBoxPbTypeId(newEle.getBoxPbTypeId());
				} else if (boxAry[i].equals("boxPbPrice")) {
					old.setBoxPbPrice(newEle.getBoxPbPrice());
				} else if (boxAry[i].equals("boxIbL")) {
					old.setBoxIbL(newEle.getBoxIbL());
					old.setBoxIbLInch(newEle.getBoxIbLInch());
				} else if (boxAry[i].equals("boxIbW")) {
					old.setBoxIbW(newEle.getBoxIbW());
					old.setBoxIbWInch(newEle.getBoxIbWInch());
				} else if (boxAry[i].equals("boxIbH")) {
					old.setBoxIbH(newEle.getBoxIbH());
					old.setBoxIbHInch(newEle.getBoxIbHInch());
				} else if (boxAry[i].equals("boxIbCount")) {
					old.setBoxIbCount(newEle.getBoxIbCount());
				} else if (boxAry[i].equals("boxIbTypeId")) {
					old.setBoxIbTypeId(newEle.getBoxIbTypeId());
				} else if (boxAry[i].equals("boxIbPrice")) {
					old.setBoxIbPrice(newEle.getBoxIbPrice());
				} else if (boxAry[i].equals("boxMbL")) {
					old.setBoxMbL(newEle.getBoxMbL());
					old.setBoxMbLInch(newEle.getBoxMbLInch());
				} else if (boxAry[i].equals("boxMbW")) {
					old.setBoxMbW(newEle.getBoxMbW());
					old.setBoxMbWInch(newEle.getBoxMbWInch());
				} else if (boxAry[i].equals("boxMbH")) {
					old.setBoxMbH(newEle.getBoxMbH());
					old.setBoxMbHInch(newEle.getBoxMbHInch());
				} else if (boxAry[i].equals("boxMbCount")) {
					old.setBoxMbCount(newEle.getBoxMbCount());
				} else if (boxAry[i].equals("boxMbTypeId")) {
					old.setBoxMbTypeId(newEle.getBoxMbTypeId());
				} else if (boxAry[i].equals("boxMbPrice")) {
					old.setBoxMbPrice(newEle.getBoxMbPrice());
				} else if (boxAry[i].equals("boxObL")) {
					old.setBoxObL(newEle.getBoxObL());
					old.setBoxObLInch(newEle.getBoxObLInch());
				} else if (boxAry[i].equals("boxObW")) {
					old.setBoxObW(newEle.getBoxObW());
					old.setBoxObWInch(newEle.getBoxObWInch());
				} else if (boxAry[i].equals("boxObH")) {
					old.setBoxObH(newEle.getBoxObH());
					old.setBoxObHInch(newEle.getBoxObHInch());
				} else if (boxAry[i].equals("boxObCount")) {
					old.setBoxObCount(newEle.getBoxObCount());
				} else if (boxAry[i].equals("boxObTypeId")) {
					old.setBoxObTypeId(newEle.getBoxObTypeId());
				} else if (boxAry[i].equals("boxObPrice")) {
					old.setBoxObPrice(newEle.getBoxObPrice());
				} else if (boxAry[i].equals("eleSizeDesc")) {
					old.setEleSizeDesc(newEle.getEleSizeDesc());
				} else if (boxAry[i].equals("eleInchDesc")) {
					old.setEleInchDesc(newEle.getEleInchDesc());
				} else if (boxAry[i].equals("packingPrice")) {
					old.setPackingPrice(newEle.getPackingPrice());
				} else if (boxAry[i].equals("boxCbm")) {
					old.setBoxCbm(newEle.getBoxCbm());
					old.setBoxCuft(newEle.getBoxCuft());
				} else if (boxAry[i].equals("boxNetWeigth")) {
					old.setBoxGrossWeigth(newEle.getBoxGrossWeigth());
					old.setBoxNetWeigth(newEle.getBoxNetWeigth());
				} else if (boxAry[i].equals("boxRemark")) {
					old.setBoxRemark(newEle.getBoxRemark());
				} else if (boxAry[i].equals("boxRemarkCn")) {
					old.setBoxRemarkCn(newEle.getBoxRemarkCn());
				} else if (boxAry[i].equals("boxTDesc")) {
					old.setBoxTDesc(newEle.getBoxTDesc());
				} else if (boxAry[i].equals("boxBDesc")) {
					old.setBoxBDesc(newEle.getBoxBDesc());
				}
			}
		}

		if (otherStr != null && !"".equals(otherStr)) {
			String[] otherAry = otherStr.split(",");
			for (int i = 0; i < otherAry.length; i++) {
				if (otherAry[i].equals("box20Count")) {
					old.setBox20Count(newEle.getBox20Count());
				} else if (otherAry[i].equals("box40Count")) {
					old.setBox40Count(newEle.getBox40Count());
				} else if (otherAry[i].equals("box40hqCount")) {
					old.setBox40hqCount(newEle.getBox40hqCount());
				} else if (otherAry[i].equals("box45Count")) {
					old.setBox45Count(newEle.getBox45Count());
				}
			}
		}
		old.setChilds(null);
		old.setCotFile(null);
		old.setCotPictures(null);
		old.setCotPriceFacs(null);
		old.setCotEleFittings(null);
		old.setCotElePrice(null);
		old.setCotElePacking(null);
		return old;
	}

	// 同步相同的样品的图片
	public Object[] getGivenByEle(String eleId, CotElePic cotElePic) {
		Object[] obj = new Object[2];
		CotGivenDetail givenDetail = (CotGivenDetail) this.getGivenMapValue(eleId);
		obj[0] = givenDetail;
		CotElePic pic = null;
		if (givenDetail.getType() == null) {
			String hql = "from CotGivenPic obj where obj.fkId=" + givenDetail.getId();
			List list = this.getCotBaseDao().find(hql);
			CotGivenPic cotGivenPic = (CotGivenPic) list.get(0);
			pic = cotElePic;
			pic.setEleId(cotGivenPic.getEleId());
			pic.setPicImg(cotGivenPic.getPicImg());
			pic.setPicName(cotGivenPic.getPicName());
			pic.setPicSize(cotGivenPic.getPicImg().length);
		} else {
			String type = givenDetail.getType();
			Integer srcId = givenDetail.getSrcId();
			pic = changeElePic(type, srcId, cotElePic);
		}
		obj[1] = pic;
		return obj;
	}

	// 查询VO记录
	public List<?> getGivenVOList(QueryInfo queryInfo) {
		List<CotGivenVO> listVo = new ArrayList<CotGivenVO>();
		try {
			List<?> list = this.getCotBaseDao().findRecords(queryInfo);
			for (int i = 0; i < list.size(); i++) {
				CotGivenVO givenVO = new CotGivenVO();
				Object[] obj = (Object[]) list.get(i);
				givenVO.setId((Integer) obj[0]);
				givenVO.setGivenNo((String) obj[1]);
				givenVO.setGivenTime((Timestamp) obj[2]);
				givenVO.setCustomerShortName((String) obj[3]);
				givenVO.setCustId((Integer) obj[4]);
				givenVO.setEmpsName((String) obj[5]);
				givenVO.setCustRequiretime((Date) obj[6]);
				givenVO.setCheckComplete((Integer) obj[7]);
				givenVO.setRealGiventime((Date) obj[8]);
				givenVO.setGivenStatus((Long) obj[9]);
				givenVO.setGivenAddr((String) obj[10]);
				givenVO.setGivenIscheck(new Integer(obj[11].toString()));
				listVo.add(givenVO);
			}
			return listVo;
		} catch (DAOException e) {
			e.printStackTrace();
			logger.error("查询出错");
			return null;
		}
	}

	// 储存Map
	public void setExcelMap(String rdm, CotGivenDetail cotGivenDetail) {
		Object obj = SystemUtil.getObjBySession(null, "excelTempGiven");
		if (obj == null) {
			TreeMap<String, CotGivenDetail> orderMap = new TreeMap<String, CotGivenDetail>();
			orderMap.put(rdm, cotGivenDetail);
			SystemUtil.setObjBySession(null, orderMap, "excelTempGiven");
		} else {
			TreeMap<String, CotGivenDetail> orderMap = (TreeMap<String, CotGivenDetail>) obj;
			orderMap.put(rdm, cotGivenDetail);
			SystemUtil.setObjBySession(null, orderMap, "excelTempGiven");
		}

	}

	// 根据id判断该生产合同是否已存在应付帐款
	public Integer getDealNumById(Integer givenid) {
		String hql = " from CotFinaceAccountrecv obj where 1=1 and obj.source='given' and obj.fkId=" + givenid;
		List<?> list = this.getCotBaseDao().find(hql);
		if (list.size() != 0) {
			return list.size();
		} else {
			return -1;
		}
	}

	// 判断该产品是否对该用户报价过
	public Object[] findDetail(String eleId, String cusId, String pTime) {
		DateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		java.util.Date ptimeDate = null;
		try {
			ptimeDate = format1.parse(pTime);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		Object[] rtn = new Object[2];
		Object[] obj = new Object[3];
		obj[0] = new Integer(cusId);
		obj[1] = eleId;
		obj[2] = ptimeDate;
		// 送样
		String hqlGiven = "select obj,p.givenTime from CotGiven p,CotGivenDetail obj,CotCustomer c " + "where obj.givenId=p.id and p.custId=c.id and c.id=?" + " and obj.eleId=?" + " and p.givenTime<=?" + " order by p.givenTime desc,p.id desc,obj.id desc limit 0,1";
		List<?> listGiven = this.getCotBaseDao().queryForLists(hqlGiven, obj);

		// 报价和订单多一个价格条款条件
		Object[] obj2 = new Object[3];
		obj2[0] = new Integer(cusId);
		;
		obj2[1] = eleId;
		obj2[2] = ptimeDate;
		// 报价
		String hql = "select obj,p.priceTime from CotPrice p,CotPriceDetail obj,CotCustomer c " + "where obj.priceId=p.id and p.custId=c.id and c.id=?" + " and obj.eleId=?" + " and p.priceTime<=?" + " order by p.priceTime desc,p.id desc,obj.id desc limit 0,1";
		List<?> list = this.getCotBaseDao().queryForLists(hql, obj2);
		// 订单
		Object[] obj3 = new Object[3];
		obj3[0] = new Integer(cusId);
		obj3[1] = eleId;
		obj3[2] = ptimeDate;
		String hqlOrder = "select obj,p.orderTime from CotOrder p,CotOrderDetail obj,CotCustomer c " + "where obj.orderId=p.id and p.custId=c.id and c.id=?" + " and obj.eleId=?" + " and p.orderTime<=?" + " order by p.orderTime desc,p.id desc,obj.id desc limit 0,1";
		List<?> listOrder = this.getCotBaseDao().queryForLists(hqlOrder, obj3);

		Timestamp priceTime = null;
		Timestamp orderTime = null;
		Timestamp givenTime = null;
		TreeMap sort = new TreeMap();
		if (list.size() > 0) {
			Object[] detail = (Object[]) list.get(0);
			if (detail[1] != null) {
				priceTime = (Timestamp) detail[1];
				sort.put(priceTime.getTime(), 1);
			}
		}
		if (listOrder.size() > 0) {
			Object[] detailOrder = (Object[]) listOrder.get(0);
			if (detailOrder[1] != null) {
				orderTime = (Timestamp) detailOrder[1];
				sort.put(orderTime.getTime(), 2);
			}
		}
		if (listGiven.size() > 0) {
			Object[] detailGiven = (Object[]) listGiven.get(0);
			if (detailGiven[1] != null) {
				givenTime = (Timestamp) detailGiven[1];
				sort.put(givenTime.getTime(), 3);
			}
		}
		if (sort.keySet().size() == 0) {
			return null;
		}
		Integer flag = (Integer) sort.get(sort.lastKey());
		if (flag == 1) {
			Object[] objTemp = (Object[]) list.get(0);
			CotPriceDetail detail = (CotPriceDetail) objTemp[0];
			detail.setPicImg(null);
			// // 填充厂家简称
			// List<?> facList = this.getCotBaseDao().getRecords("CotFactory");
			// for (int i = 0; i < facList.size(); i++) {
			// CotFactory cotFactory = (CotFactory) facList.get(i);
			// if (detail.getFactoryId() != null
			// && cotFactory.getId().intValue() == detail
			// .getFactoryId().intValue()) {
			// detail.setFactoryShortName(cotFactory.getShortName());
			// }
			// }
			rtn[0] = 1;
			rtn[1] = detail;
			return rtn;
		} else if (flag == 2) {
			Object[] objTemp = (Object[]) listOrder.get(0);
			CotOrderDetail detail = (CotOrderDetail) objTemp[0];
			detail.setPicImg(null);
			// // 填充厂家简称
			// // List<?> facList = getDicList("factory");
			// List<?> facList = this.getCotBaseDao().getRecords("CotFactory");
			// for (int i = 0; i < facList.size(); i++) {
			// CotFactory cotFactory = (CotFactory) facList.get(i);
			// if (detail.getFactoryId() != null
			// && cotFactory.getId().intValue() == detail
			// .getFactoryId().intValue()) {
			// detail.setFactoryShortName(cotFactory.getShortName());
			// }
			// }
			rtn[0] = 2;
			rtn[1] = detail;
			return rtn;
		} else {
			Object[] objTemp = (Object[]) listGiven.get(0);
			CotGivenDetail detail = (CotGivenDetail) objTemp[0];
			// // 填充厂家简称
			// // List<?> facList = getDicList("factory");
			// List<?> facList = this.getCotBaseDao().getRecords("CotFactory");
			// for (int i = 0; i < facList.size(); i++) {
			// CotFactory cotFactory = (CotFactory) facList.get(i);
			// if (detail.getFactoryId() != null
			// && cotFactory.getId().intValue() == detail
			// .getFactoryId().intValue()) {
			// detail.setFactoryShortName(cotFactory.getShortName());
			// }
			// }
			rtn[0] = 3;
			rtn[1] = detail;
			return rtn;
		}

	}

	public CotSeqService getCotSeqService() {
		return cotSeqService;
	}

	public void setCotSeqService(CotSeqService cotSeqService) {
		this.cotSeqService = cotSeqService;
	}
}
