package com.sail.cot.service.sign.impl;
 
import java.io.File;
import java.io.FileInputStream;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

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
import com.sail.cot.domain.CotCurrency;
import com.sail.cot.domain.CotCustomer;
import com.sail.cot.domain.CotElementsNew;
import com.sail.cot.domain.CotEmps;
import com.sail.cot.domain.CotFactory;
import com.sail.cot.domain.CotPicture;
import com.sail.cot.domain.CotPriceDetail;
import com.sail.cot.domain.CotSign;
import com.sail.cot.domain.CotSignDetail;
import com.sail.cot.domain.CotSignPic;
import com.sail.cot.domain.CotSignType;
import com.sail.cot.domain.CotSyslog;
import com.sail.cot.domain.CotTypeLv1;
import com.sail.cot.domain.vo.CotPriceVO;
import com.sail.cot.query.QueryInfo;
import com.sail.cot.service.img.CotOpImgService;
import com.sail.cot.service.sign.CotSignService;
import com.sail.cot.service.system.CotSeqService;
import com.sail.cot.service.system.CotSysLogService;
import com.sail.cot.service.system.impl.CotSeqServiceImpl;
import com.sail.cot.service.system.impl.SetNoServiceImpl;
import com.sail.cot.util.Log4WebUtil;
import com.sail.cot.util.ReflectionUtils;
import com.sail.cot.util.SystemUtil;
import com.sail.cot.util.ThreadLocalManager;
import com.sail.cot.util.ThreadObject;

public class CotSignServiceImpl implements CotSignService {

	private CotBaseDao cotBaseDao;
	
	private Logger logger = Log4WebUtil.getLogger(CotSignServiceImpl.class);
		
	public CotBaseDao getCotBaseDao() {
		return cotBaseDao;
	}

	public void setCotBaseDao(CotBaseDao cotBaseDao) {
		this.cotBaseDao = cotBaseDao;
	}
	
	//操作日志
	CotSysLogService sysLogService;

	public void setSysLogService(CotSysLogService sysLogService) {
		this.sysLogService = sysLogService;
	}

	 
    // 查询征样单单号是否存在
	public Integer findIsExistSignNo(String signNo,String id) {
		String hql = "select obj.id from CotSign obj where obj.signNo='"
			+ signNo + "'";
		List<?> res = this.getCotBaseDao().find(hql);
		if (res.size() == 0) {
			return null;
		}
		if (res.size() == 1) {
			Integer oldId = (Integer)res.get(0);
			if(oldId.toString().equals(id)){
				return null;
			}else{
				return 1;
			}
		}
		return 2;
	}
	

	//根据查询语句获取记录
	public List<?> getList(QueryInfo queryInfo){
		try {
			return this.getCotBaseDao().findRecords(queryInfo);
		} catch (DAOException e) {
			e.printStackTrace();
		}
		return  null;
	}
	
	// 得到objName的集合
	public List<?> getList(String objName) {
		return this.getCotBaseDao().getRecords(objName);
	}

	
	//根据查询语句获取总记录数
	public int getRecordCount(QueryInfo queryInfo) {
		try {
			return this.getCotBaseDao().getRecordsCount(queryInfo);
		} catch (DAOException e) {
			e.printStackTrace();
		}
		return 0;
	} 
	
	
	//得到objName的集合
	public List<?> getObjList(String objName){
		
		return this.getCotBaseDao().getRecords(objName);
	}
	
	
	// 根据征样编号查找征样单信息
	public CotSign getSignById(Integer id) {
		CotSign cotSign = (CotSign) this.getCotBaseDao().getById(CotSign.class, id);
		if(cotSign!=null){
			return cotSign;
		}
		return null;
	}
	
	
	//根据征样明细编号查找不包含PicImg征样明细单信息
	public CotSignDetail getSignDetailById(Integer detailId) {
		CotSignDetail cotSignDetail = (CotSignDetail) this.getCotBaseDao().getById(CotSignDetail.class, detailId);
		if(cotSignDetail!=null){
			cotSignDetail.setPicImg(null);
			return cotSignDetail;
		}
		return null;
	}
	
	//根据征样明细编号查找PicImg
	public byte[] getPicImgByDetailId(Integer detailId) {
		List<?> list = this.getCotBaseDao().find(" from CotSignDetail obj where obj.id="+detailId);
		if(list.size()>0){
			CotSignDetail cotSignDetail = (CotSignDetail)list.get(0) ;
			byte[] picImg = cotSignDetail.getPicImg();
			if(picImg!=null){
				return picImg;
			}
		}
		return null;
   }
	 
 
	//根据货号获取样品图片对象(CotPicture)
	public CotPicture getCotPictureByEleId(String eleId){
		String picName = eleId;
		List<?> list =  this.getCotBaseDao().find("from CotPicture obj where obj.picName='"+picName+"'");
		if(list.size()>0){
			return (CotPicture) list.get(0);
		}
		return null;
	}
  
	 // 根据征样编号查找征样单信息
	public List<?> getDetailListBySignId(Integer signId) {
		String hql = "from CotSignDetail obj where obj.signId="+signId;
		return this.getCotBaseDao().find(hql);
	}
	
	
	// 根据客户编号查找客户信息
	public CotCustomer getCustomerById(Integer id) {
		CotCustomer cotCustomer = (CotCustomer) this.getCotBaseDao().getById(CotCustomer.class, id);
		if(cotCustomer!=null){
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
	
	
	// 根据厂家编号查找厂家信息
	public CotFactory getFactoryById(Integer id) {
		return (CotFactory) this.getCotBaseDao().getById(CotFactory.class, id);
	}
	
	 
	//获得征样类型的映射
	public Map<?, ?> getSignTypeMap() {
		Map<String, String> map = new HashMap<String, String>();
		List<?> res = this.getCotBaseDao().getRecords("CotSignType");
		for(int i=0; i<res.size(); i++)
		{
			CotSignType cotSignType = (CotSignType)res.get(i);
			map.put(cotSignType.getId().toString(),cotSignType.getSignName());
		}
		return map;
	}
	
	//获得征样单编号的映射
	public Map<?, ?> getSignNoMap() {
		Map<String, String> map = new HashMap<String, String>();
		List<?> res = this.getCotBaseDao().getRecords("CotSign");
		for(int i=0; i<res.size(); i++)
		{
			CotSign cotSign = (CotSign)res.get(i);
			map.put(cotSign.getId().toString(),cotSign.getSignNo());
		}
		return map;
	}
	
	//获得客户简称的映射
	public Map<?, ?> getCustMap() {
		Map<String, String> map = new HashMap<String, String>();
		List<?> res = this.getCotBaseDao().getRecords("CotCustomer");
		for(int i=0; i<res.size(); i++)
		{
			CotCustomer cotCustomer = (CotCustomer)res.get(i);
			map.put(cotCustomer.getId().toString(),cotCustomer.getCustomerShortName());
		}
		return map;
	}
	
	
	//获得厂家简称的映射
	public Map<?, ?> getFacMap() {
		Map<String, String> map = new HashMap<String, String>();
		List<?> res = this.getCotBaseDao().getRecords("CotFactory");
		for(int i=0; i<res.size(); i++)
		{
			CotFactory cotFactory = (CotFactory)res.get(i);
			map.put(cotFactory.getId().toString(),cotFactory.getShortName());
		}
		return map;
	}
	
	//获得员工的映射
	public Map<?, ?> getEmpMap() {
		Map<String, String> map = new HashMap<String, String>();
		List<?> res = this.getCotBaseDao().getRecords("CotEmps");
		for(int i=0; i<res.size(); i++)
		{
			CotEmps cotEmps = (CotEmps)res.get(i);
			map.put(cotEmps.getId().toString(),cotEmps.getEmpsName());
		}
		return map;
	}
	
	
	//获得样品材质的映射
	public Map<?, ?> getTypeMap() {
		Map<String, String> map = new HashMap<String, String>();
		List<?> list = this.getCotBaseDao().getRecords("CotTypeLv1");
		for (int i = 0; i < list.size(); i++) {
			CotTypeLv1 cotTypeLv1 = (CotTypeLv1) list.get(i);
			map.put(cotTypeLv1.getId().toString(), cotTypeLv1.getTypeName());
		}
		return map;
	}
	
	
	//保存主征样单
	public Integer saveOrUpdateSign(CotSign cotSign,Integer givenId,String givenNo){
		 
		cotSign.setAddTime(new Date(System.currentTimeMillis()));// 修改时间
		//获取登录人
		WebContext ctx = WebContextFactory.get();
		CotEmps cotEmps = (CotEmps) ctx.getSession().getAttribute("emp");
		cotSign.setAddPerson(cotEmps.getEmpsName());// 制单人
		cotSign.setEmpId(cotEmps.getId());// 制单人ID
		// 审核状态 0:(未审核),1(已审核)通过,2(审核不通过)
		cotSign.setSignStatus(new Long(0));// 审核状态
		cotSign.setSignIscheck(new Long(0));// 是否需要审核
		// 是否需要审核 0:不需要审核 1:需要审核 （默认0）
		if (cotSign.getGivenId() == null || "".equals(cotSign.getGivenId())) {
			cotSign.setGivenId(givenId);
		}
		if (cotSign.getGivenNo() == null || "".equals(cotSign.getGivenNo())) {
			cotSign.setGivenNo(givenNo);
		}
		List<CotSign> records = new ArrayList<CotSign>();
		if(cotSign.getId() == null)
		{
			//更新全局序列号
//			SetNoServiceImpl setNoService = new SetNoServiceImpl();
//			setNoService.saveSeq("sign",cotSign.getSignTime().toString());
			CotSeqService cotSeqService=new CotSeqServiceImpl();
			cotSeqService.saveCustSeq(cotSign.getCustId(), "sign",cotSign.getSignTime().toString());
			cotSeqService.saveSeq("sign");
		}
		records.add(cotSign);
		// 保存主征样单
		try {
			this.getCotBaseDao().saveOrUpdateRecords(records);
			//添加操作日志
			CotSyslog syslog = new CotSyslog();
			syslog.setEmpId(cotEmps.getId());
			syslog.setItemNo(cotSign.getSignNo());
			syslog.setOpMessage("添加或修改主征样单成功");
			syslog.setOpModule("sign");
			syslog.setOpTime(new Date(System.currentTimeMillis()));
			syslog.setOpType(1);  //1:添加 2：修改 3：删除
			List<CotSyslog> logList = new ArrayList<CotSyslog>();
			logList.add(syslog);
			sysLogService.addSysLog(logList);
			
		} catch (Exception e) {
			logger.error("保存征样单出错！");
			//添加操作日志
			CotSyslog syslog = new CotSyslog();
			syslog.setEmpId(cotEmps.getId());
			syslog.setItemNo(cotSign.getSignNo());
			syslog.setOpMessage("添加或修改主征样单失败，失败原因："+e.getMessage());
			syslog.setOpModule("sign");
			syslog.setOpTime(new Date(System.currentTimeMillis()));
			syslog.setOpType(1);  //1:添加 2：修改 3：删除
			List<CotSyslog> logList = new ArrayList<CotSyslog>();
			logList.add(syslog);
			sysLogService.addSysLog(logList);
			return null;
		}
		return cotSign.getId();
	}
	
	 
	
	
	//保存征样单的产品明细

	public Boolean addSignDetails(List<?> details){
		WebContext ctx = WebContextFactory.get();
		CotEmps cotEmps = null;
		ThreadObject obj = (ThreadObject)ThreadLocalManager.getCurrentThread().get();
		Integer empId = 0;
		if(ctx != null)
		{
			 cotEmps = (CotEmps) ctx.getSession().getAttribute("emp");
			 empId = cotEmps.getId();
		}
		if(obj != null)
			empId = obj.getEmpId();
		try {
			this.getCotBaseDao().saveRecords(details);
			//添加操作日志
			if(ctx != null || obj != null)
			{
				CotSyslog syslog = new CotSyslog();
				syslog.setEmpId(empId);
				syslog.setOpMessage("批量添加征样单明细成功");
				syslog.setOpModule("sign");
				syslog.setOpTime(new Date(System.currentTimeMillis()));
				syslog.setOpType(1);  //1:添加 2：修改 3：删除
				List<CotSyslog> logList = new ArrayList<CotSyslog>();
				logList.add(syslog);
			
				sysLogService.addSysLog(logList);
			}
			
			return true;
		} catch (DAOException e) {
			logger.error("保存征样单的产品明细出错!");
			//添加操作日志
			if(ctx != null || obj != null)
			{
				CotSyslog syslog = new CotSyslog();
				syslog.setEmpId(empId);
				syslog.setOpMessage("批量添加征样单明细失败，失败原因："+e.getMessage());
				syslog.setOpModule("sign");
				syslog.setOpTime(new Date(System.currentTimeMillis()));
				syslog.setOpType(1);  //1:添加 2：修改 3：删除
				List<CotSyslog> logList = new ArrayList<CotSyslog>();
				logList.add(syslog);
				sysLogService.addSysLog(logList);
			}
			return false;
		}
	}
	
	//保存征样图片对象
	public Boolean addSignPic(List<?> cotSignPicList){
		//获取操作人
		WebContext ctx = WebContextFactory.get();
		CotEmps cotEmps = null;
		ThreadObject obj = (ThreadObject)ThreadLocalManager.getCurrentThread().get();
		Integer empId = 0;
		if(ctx != null)
		{
			 cotEmps = (CotEmps) ctx.getSession().getAttribute("emp");
			 empId = cotEmps.getId();
		}
		if(obj != null)
			empId = obj.getEmpId();
		try {
			this.getCotBaseDao().saveRecords(cotSignPicList);
			//添加操作日志
			if(ctx != null || obj != null)
			{
				CotSyslog syslog = new CotSyslog();
				syslog.setEmpId(empId);
				syslog.setOpMessage("批量添加征样图片成功");
				syslog.setOpModule("sign");
				syslog.setOpTime(new Date(System.currentTimeMillis()));
				syslog.setOpType(1);  //1:添加 2：修改 3：删除
				List<CotSyslog> logList = new ArrayList<CotSyslog>();
				logList.add(syslog);
				sysLogService.addSysLog(logList);
			}
			return true;
		} catch (DAOException e) {
			logger.error("保存征样图片对象出错!");
			//添加操作日志
			if(ctx != null || obj != null)
			{
				CotSyslog syslog = new CotSyslog();
				syslog.setEmpId(empId);
				syslog.setOpMessage("批量添加征样图片失败，失败原因："+e.getMessage());
				syslog.setOpModule("sign");
				syslog.setOpTime(new Date(System.currentTimeMillis()));
				syslog.setOpType(1);  //1:添加 2：修改 3：删除
				List<CotSyslog> logList = new ArrayList<CotSyslog>();
				logList.add(syslog);
				sysLogService.addSysLog(logList);
			}
			return false;
		}
	}
	
	
	//批量修改主征样单
	public Boolean modifySignList(List<?> signList){
		//获取操作人
		WebContext ctx = WebContextFactory.get();
		CotEmps cotEmps = null;
		ThreadObject obj = (ThreadObject)ThreadLocalManager.getCurrentThread().get();
		Integer empId = 0;
		if(ctx != null)
		{
			 cotEmps = (CotEmps) ctx.getSession().getAttribute("emp");
			 empId = cotEmps.getId();
		}
		if(obj != null)
			empId = obj.getEmpId();
		
		for (int i = 0; i < signList.size(); i++) {
			CotSign cotSign = (CotSign) signList.get(i);
			cotSign.setAddTime(new Date(System.currentTimeMillis()));// 修改时间
		}
		try {
			this.getCotBaseDao().updateRecords(signList);
			//添加操作日志
			if(ctx != null || obj != null)
			{
				CotSyslog syslog = new CotSyslog();
				syslog.setEmpId(empId);
				syslog.setOpMessage("批量修改主征样单成功");
				syslog.setOpModule("sign");
				syslog.setOpTime(new Date(System.currentTimeMillis()));
				syslog.setOpType(2);  //1:添加 2：修改 3：删除
				List<CotSyslog> logList = new ArrayList<CotSyslog>();
				logList.add(syslog);
				sysLogService.addSysLog(logList);
			}
			
			return true;
		} catch (Exception e) {
			//添加操作日志
			if(ctx != null || obj != null)
			{
				CotSyslog syslog = new CotSyslog();
				syslog.setEmpId(empId);
				syslog.setOpMessage("批量修改主征样单失败，失败原因："+e.getMessage());
				syslog.setOpModule("sign");
				syslog.setOpTime(new Date(System.currentTimeMillis()));
				syslog.setOpType(2);  //1:添加 2：修改 3：删除
				List<CotSyslog> logList = new ArrayList<CotSyslog>();
				logList.add(syslog);
				sysLogService.addSysLog(logList);
			}
			return false;
		}
	}
	
	
	//批量修改征样单明细
	public Boolean modifySignDetail(List<CotSignDetail> detailList) {
		WebContext ctx = WebContextFactory.get();
		CotEmps cotEmps = null;
		ThreadObject obj = (ThreadObject)ThreadLocalManager.getCurrentThread().get();
		Integer empId = 0;
		if(ctx != null)
		{
			 cotEmps = (CotEmps) ctx.getSession().getAttribute("emp");
			 empId = cotEmps.getId();
		}
		if(obj != null)
			empId = obj.getEmpId();
		try {
			this.getCotBaseDao().updateRecords(detailList);
			//添加操作日志
			if(ctx != null || obj != null)
			{
				CotSyslog syslog = new CotSyslog();
				syslog.setEmpId(empId);
				syslog.setOpMessage("批量修改征样单明细成功");
				syslog.setOpModule("sign");
				syslog.setOpTime(new Date(System.currentTimeMillis()));
				syslog.setOpType(2);  //1:添加 2：修改 3：删除
				List<CotSyslog> logList = new ArrayList<CotSyslog>();
				logList.add(syslog);
				sysLogService.addSysLog(logList);
			}
			
			return true;
		} catch (Exception e) {
			//添加操作日志
			if(ctx != null || obj != null)
			{
				CotSyslog syslog = new CotSyslog();
				syslog.setEmpId(empId);
				syslog.setOpMessage("批量修改征样单明细失败，失败原因："+e.getMessage());
				syslog.setOpModule("sign");
				syslog.setOpTime(new Date(System.currentTimeMillis()));
				syslog.setOpType(2);  //1:添加 2：修改 3：删除
				List<CotSyslog> logList = new ArrayList<CotSyslog>();
				logList.add(syslog);
			}
			
			e.printStackTrace();
			return false;
		}
	}
	
	// 更新征样单的产品明细
	public Boolean updateSignDetail(CotSignDetail e, String eleProTime) {
//		// 没选择厂家时,设置厂家为'未定义'
//		if (e.getFactoryId() == null) {
//			String hql = "from CotFactory obj where obj.shortName='未定义'";
//			List<?> list = this.getCotBaseDao().find(hql);
//			CotFactory facDefault = (CotFactory) list.get(0);
//			e.setFactoryId(facDefault.getId());
//		}
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
		//克隆对象,避免造成指针混用
		CotSignDetail cloneObj = (CotSignDetail)SystemUtil.deepClone(e); 
    	byte[] picImg =  this.getPicImgByDetailId(e.getId());
    	cloneObj.setPicImg(picImg);
    	
		List<CotSignDetail> records = new ArrayList<CotSignDetail>();
		records.add(cloneObj);
		//获取操作人
		WebContext ctx = WebContextFactory.get();
		CotEmps cotEmps = (CotEmps) ctx.getSession().getAttribute("emp");
    	try {
			this.getCotBaseDao().updateRecords(records);
			//添加操作日志
			CotSyslog syslog = new CotSyslog();
			syslog.setEmpId(cotEmps.getId());
			syslog.setItemNo(cloneObj.getId().toString());
			syslog.setOpMessage("修改征样单明细成功");
			syslog.setOpModule("sign");
			syslog.setOpTime(new Date(System.currentTimeMillis()));
			syslog.setOpType(2);  //1:添加 2：修改 3：删除
			List<CotSyslog> logList = new ArrayList<CotSyslog>();
			logList.add(syslog);
			sysLogService.addSysLog(logList);
			
		} catch (Exception ex) {
			ex.printStackTrace();
			logger.error("保存样品错误!");
			//添加操作日志
			CotSyslog syslog = new CotSyslog();
			syslog.setEmpId(cotEmps.getId());
			syslog.setItemNo(cloneObj.getId().toString());
			syslog.setOpMessage("修改征样单明细失败，失败原因："+ex.getMessage());
			syslog.setOpModule("sign");
			syslog.setOpTime(new Date(System.currentTimeMillis()));
			syslog.setOpType(2);  //1:添加 2：修改 3：删除
			List<CotSyslog> logList = new ArrayList<CotSyslog>();
			logList.add(syslog);
			sysLogService.addSysLog(logList);
			return false;
		}
		return true;
	}
	
	
	// 批量删除主征样单
	@SuppressWarnings({ "deprecation", "unchecked" })
	public Boolean deleteSignList(List<CotSign> signList) {
		//获取操作人
		WebContext ctx = WebContextFactory.get();
		CotEmps cotEmps = (CotEmps) ctx.getSession().getAttribute("emp");
		List<Integer> ids = new ArrayList<Integer>();
		 
		for (int i = 0; i < signList.size(); i++) {
			CotSign cotSign = (CotSign)signList.get(i);
			Integer signId = cotSign.getId();
			ids.add(signId);
			List<CotSignDetail> signDetailList = (List<CotSignDetail>) this.getDetailListBySignId(signId);
			if(signDetailList!=null){
				this.deleteSignDetailList(signDetailList);
			}
		}
		try {
			this.getCotBaseDao().deleteRecordByIds(ids, "CotSign");
			//添加操作日志
			CotSyslog syslog = new CotSyslog();
			syslog.setEmpId(cotEmps.getId());
			syslog.setOpMessage("批量删除主征样单成功");
			syslog.setOpModule("sign");
			syslog.setOpTime(new Date(System.currentTimeMillis()));
			syslog.setOpType(3);  //1:添加 2：修改 3：删除
			List<CotSyslog> logList = new ArrayList<CotSyslog>();
			logList.add(syslog);
			sysLogService.addSysLog(logList);
			return true;
			
		} catch (DAOException e) {
			logger.error("删除主征样单出错");
			//添加操作日志
			CotSyslog syslog = new CotSyslog();
			syslog.setEmpId(cotEmps.getId());
			syslog.setOpMessage("批量删除主征样单失败，失败原因："+e.getMessage());
			syslog.setOpModule("sign");
			syslog.setOpTime(new Date(System.currentTimeMillis()));
			syslog.setOpType(3);  //1:添加 2：修改 3：删除
			List<CotSyslog> logList = new ArrayList<CotSyslog>();
			logList.add(syslog);
			sysLogService.addSysLog(logList);
			return false;
		}
		
	}
	
	
	// 批量删除征样单明细
	public Boolean deleteSignDetailList(List<CotSignDetail> signDetailList) {
		List<Integer> ids = new ArrayList<Integer>();
		for (int i = 0; i < signDetailList.size(); i++) {
			CotSignDetail cotSignDetail = (CotSignDetail) signDetailList.get(i);
			ids.add(cotSignDetail.getId());
		}
		//获取操作人
		WebContext ctx = WebContextFactory.get();
		CotEmps cotEmps = (CotEmps) ctx.getSession().getAttribute("emp");
		try {
			this.getCotBaseDao().deleteRecordByIds(ids, "CotSignDetail");
			//添加操作日志
			CotSyslog syslog = new CotSyslog();
			syslog.setEmpId(cotEmps.getId());
			syslog.setOpMessage("批量删除征样单明细成功");
			syslog.setOpModule("sign");
			syslog.setOpTime(new Date(System.currentTimeMillis()));
			syslog.setOpType(3);  //1:添加 2：修改 3：删除
			List<CotSyslog> logList = new ArrayList<CotSyslog>();
			logList.add(syslog);
			sysLogService.addSysLog(logList);
		} catch (DAOException e) {
			e.printStackTrace();
			logger.error("删除征样单的产品明细异常", e);
			//添加操作日志
			CotSyslog syslog = new CotSyslog();
			syslog.setEmpId(cotEmps.getId());
			syslog.setOpMessage("批量删除主征样单失败，失败原因："+e.getMessage());
			syslog.setOpModule("sign");
			syslog.setOpTime(new Date(System.currentTimeMillis()));
			syslog.setOpType(3);  //1:添加 2：修改 3：删除
			List<CotSyslog> logList = new ArrayList<CotSyslog>();
			logList.add(syslog);
			sysLogService.addSysLog(logList);
			return false;
		}
		 
		return true;
	}
	
	
	//根据征样明细产品的ids批量删除征样明细
	public void deleteDetailByIds(List<Integer> ids){
		WebContext ctx = WebContextFactory.get();
		CotEmps cotEmps = null;
		ThreadObject obj = (ThreadObject)ThreadLocalManager.getCurrentThread().get();
		Integer empId = 0;
		if(ctx != null)
		{
			 cotEmps = (CotEmps) ctx.getSession().getAttribute("emp");
			 empId = cotEmps.getId();
		}
		if(obj != null)
			empId = obj.getEmpId();
		try {
			this.getCotBaseDao().deleteRecordByIds(ids, "CotSignDetail");
			//添加操作日志
			if(ctx != null || obj != null)
			{
				CotSyslog syslog = new CotSyslog();
				syslog.setEmpId(empId);
				syslog.setOpMessage("批量删除征样单明细成功");
				syslog.setOpModule("sign");
				syslog.setOpTime(new Date(System.currentTimeMillis()));
				syslog.setOpType(3);  //1:添加 2：修改 3：删除
				List<CotSyslog> logList = new ArrayList<CotSyslog>();
				logList.add(syslog);
				sysLogService.addSysLog(logList);
			}
		} catch (DAOException e) {
			e.printStackTrace();
			//添加操作日志
			if(ctx != null || obj != null)
			{
				CotSyslog syslog = new CotSyslog();
				syslog.setEmpId(empId);
				syslog.setOpMessage("批量删除征样单明细失败，失败原因："+e.getMessage());
				syslog.setOpModule("sign");
				syslog.setOpTime(new Date(System.currentTimeMillis()));
				syslog.setOpType(3);  //1:添加 2：修改 3：删除
				List<CotSyslog> logList = new ArrayList<CotSyslog>();
				logList.add(syslog);
				sysLogService.addSysLog(logList);
			}
		}
	}
	
	
	/*//将String类型的时间转化为Timestamp类型
	public Timestamp getStampTime(String time){
		Timestamp timestamp = null;
		if(time!=null&&!time.equals("")){
			java.text.SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			try {
			    timestamp = new Timestamp(sdf.parse(time).getTime());
			} catch (ParseException e1) {
				e1.printStackTrace();
			}
			return timestamp;
		}
		return timestamp;
	}
	
	
	//将Timestamp类型的时间转化为String类型
	public String getStringTime(Timestamp time){
		String stringTime =null;
		if(time!=null){
			stringTime = time.toString();
			return stringTime;
		}
		return stringTime;
	}*/
	
	
	//删除征样明细单产品的原来图片
	@SuppressWarnings("deprecation")
	public void deleteSignDetailImg(Integer id) {
		CotSignDetail cotSignDetail =  (CotSignDetail) this.getCotBaseDao().getById(CotSignDetail.class, id);
		WebContext ctx = WebContextFactory.get();
		String path = ctx.getHttpServletRequest().getRealPath("/");
		File file = new File(path+cotSignDetail.getPicPath());
		if(file.exists()){
			file.delete();
		}
	}
	
	//通过单号删除征样单的所有产品图片
	@SuppressWarnings("deprecation")
	public void deleteSignImgBySignNo(String signNo) {
		WebContext ctx = WebContextFactory.get();
		String path = ctx.getHttpServletRequest().getRealPath("/");
		File file = new File(path+"/signImg//"+signNo);
		if(file.exists()){
			File[] child = file.listFiles();
			if(child!=null){
				for (int i = 0; i < child.length; i++) {
					child[i].delete();
				}
			}
			file.delete();
		}
	}
	

	// 根据样品货号组装征样明细产品对象 
	public CotSignDetail findDetailByEleId(String eleId,Integer factoryId) {
		// 查找样品对象
		String hql = "from CotElementsNew obj where obj.eleId = '" + eleId + "' and obj.factoryId="+factoryId;
		List<?> list = this.getCotBaseDao().find(hql);
		//样品对象不能为空
		if (list.size() == 0) {
			return null;
		}
		CotElementsNew cotElementsNew = (CotElementsNew) list.get(0);

		// 新建征样明细对象
		CotSignDetail cotSignDetail = new CotSignDetail();
		// 1.使用反射获取对象的所有属性名称
		String[] propEle = ReflectionUtils.getDeclaredFields(CotElementsNew.class);
		
		ConvertUtilsBean convertUtils = new ConvertUtilsBean(); 
		SqlDateConverter dateConverter = new SqlDateConverter();
		convertUtils.register(dateConverter,Date.class);
		//因为要注册converter,所以不能再使用BeanUtils的静态方法了，必须创建BeanUtilsBean实例
		BeanUtilsBean beanUtils = new BeanUtilsBean(convertUtils,new PropertyUtilsBean());
		//beanUtils.setProperty(bean, name, value);
		
		for (int i = 0; i < propEle.length; i++) {
			try {
				String value = BeanUtils.getProperty(cotElementsNew, propEle[i]);
				if ("cotPictures".equals(propEle[i])
						|| "cotFile".equals(propEle[i])
						|| "childs".equals(propEle[i])
						|| "picImg".equals(propEle[i])
						|| "cotPriceFacs".equals(propEle[i])
						|| "cotElePrice".equals(propEle[i])
						|| "cotEleFittings".equals(propEle[i]) 
						|| "eleFittingPrice".equals(propEle[i])
						|| "cotElePacking".equals(propEle[i])
						|| "cotPackingPrice".equals(propEle[i]))
				{
					continue;
				}
				if (value != null) {
					
					beanUtils.setProperty(cotSignDetail, propEle[i], beanUtils.getProperty(cotElementsNew, propEle[i]));
					cotSignDetail.setId(null);
				}
				
			} catch (Exception e) {
				logger.error(propEle[i] + ":转换错误!");
			}
		}
 
		// 插入样品的图片信息
		cotSignDetail.setPicName(cotElementsNew.getPicName());
		cotSignDetail.setPicPath(cotElementsNew.getPicPath());
		//cotSignDetail.setPicImg(cotElementsNew.getPicImg());
 
		return cotSignDetail;
	}
	
	
	//查询该单的所有征样明细的产品货号
	public String findEleBySignId(Integer signId){
		String hql = "select obj.eleId from CotSignDetail obj where obj.signId="+signId;
		List<?> list = this.getCotBaseDao().find(hql);
		String eleIds = "";
		for (int i = 0; i < list.size(); i++) {
			String eleId = (String) list.get(i);
			eleIds +=eleId+",";
		}
		return eleIds;
	}
	
	//判断该产品货号是否存在
	public Boolean findIsExistEleByEleId(String eleId){
		String hql = "from CotElementsNew obj where obj.eleId='"+eleId+"'";
		List<?> list = this.getCotBaseDao().find(hql);
		if(list.size()!=0){
			return true;
		}else{
			return false;
		}
	}
	
	// 判断该主单的明细是否已存在该产品货号
	@SuppressWarnings("unchecked")
	public boolean findIsExistEleId(Integer mainId, String eleId, Integer eId) {
		List<Integer> res = this.getCotBaseDao().find(
				"select c.id from CotSignDetail c where c.signId = " + mainId
						+ " and c.eleId='" + eleId + "'");
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
	
	//通过业务员ID获取业务员名字
	public String getEmpsNameByEmpId(Integer empId){
		String empsName = null;
		String hql = "select obj.empsName from CotEmps obj where id = "+empId;
		List<?> list = this.getCotBaseDao().find(hql);
		if(list.size()>0){
			empsName = (String) list.get(0);
		}
		return empsName;
	}
	
	// 根据报价编号字符串查找报价明细
	@SuppressWarnings("unchecked")
	public List<CotPriceDetail> findPriceDetailByIds(String ids) {
		List<CotPriceDetail> list = new ArrayList();
		String hql = "from CotPriceDetail obj where obj.id in ("+ids.substring(0,ids.length()-1)+")";
		list = this.getCotBaseDao().find(hql);
		return list;
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
					cotPriceVO.setCommisionScale(Double.parseDouble(obj[6]
							.toString()));
				}
				if (obj[7] != null) {
					cotPriceVO.setPriceProfit(Double.parseDouble(obj[7]
							.toString()));
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
	
	// 查询所有客户
	public Map<?, ?> getCusNameMap() {
		Map<String, String> map = new HashMap<String, String>();
		List<?> list = this.getCotBaseDao().getRecords("CotCustomer");
		for (int i = 0; i < list.size(); i++) {
			CotCustomer cotCustomer = (CotCustomer) list.get(i);
			map
					.put(cotCustomer.getId().toString(), cotCustomer
							.getFullNameCn());
		}
		return map;
	}
	
	//通过部门id获取所在部门员工列表
	@SuppressWarnings("unchecked")
	public List<CotEmps> getDeptEmpListByDeptId(Integer deptId){
		List<CotEmps> empsList = new ArrayList<CotEmps>();
		List<CotEmps> deptEmpList = this.getCotBaseDao().find("from CotEmps obj where obj.deptId="+deptId);
		if(deptEmpList.size()>0){
			for (int i = 0; i < deptEmpList.size(); i++) {
				CotEmps cotEmps = deptEmpList.get(i);
				cotEmps.setCustomers(null);
				empsList.add(cotEmps);
			}
			return empsList;
		}
		return null;
	}
	
	//通过员工id获取所在员工信息
	@SuppressWarnings("unchecked")
	public List<CotEmps> getEmpListByEmpId(Integer empId){
		List<CotEmps> empsList = new ArrayList<CotEmps>();
		List<CotEmps> list = this.getCotBaseDao().find("from CotEmps obj where obj.id="+empId);
		if(list.size()>0){
			for (int i = 0; i < list.size(); i++) {
				CotEmps cotEmps = list.get(i);
				cotEmps.setCustomers(null);
				empsList.add(cotEmps);
			}
			return empsList;
		}
		return null;
	}
	
	//根据货号取得样品对象
	@SuppressWarnings("unchecked")
	public CotElementsNew getElementsByEleId(String eleId){
		CotElementsNew cotElementsNew = new CotElementsNew();
		List<CotElementsNew> list = this.getCotBaseDao().find("from CotElementsNew obj where obj.eleId='"+eleId+"'");
		if(list.size()>0){
			cotElementsNew =  list.get(0);
			return cotElementsNew;
		}
		return null;
	}
	
	//根据货号取得征样明细对象
	@SuppressWarnings("unchecked")
	public CotSignDetail getCotSignDetailByEleId(String eleId){
		CotSignDetail cotSignDetail = new CotSignDetail();
		List<CotSignDetail> list = this.getCotBaseDao().find("from CotSignDetail obj where obj.eleId='"+eleId+"'");
		if(list.size()>0){
			cotSignDetail =  list.get(0);
			return cotSignDetail;
		}
		return null;
	}
	
	//根据货号和主征样单id获取明细id
	public Integer getDetailId(String signId,String eleId){ 
		List<?> list = this.getCotBaseDao().find("select obj.id from CotSignDetail obj where obj.eleId='"+eleId+"'and obj.signId="+signId);
		if(list.size()>0){
			Integer detailId = (Integer) list.get(0);
			return detailId;
		}
		return null;
	}
	 
	
	//获取signMap
	@SuppressWarnings("unchecked")
	public  HashMap<String, CotSignDetail> getSignMap() {
		Object obj =  SystemUtil.getObjBySession(null,"sign");
		HashMap<String, CotSignDetail> signMap = (HashMap<String, CotSignDetail>) obj;
		return signMap ;
	}
	
	//储存signMap
	@SuppressWarnings("unchecked")
	public  void setSignMap(String eleId,CotSignDetail cotSignDetail) {
	    Object obj =  SystemUtil.getObjBySession(null,"sign");
	    if(obj==null){
	    	HashMap<String, CotSignDetail> signMap = new HashMap<String, CotSignDetail>();
	    	signMap.put(eleId.toLowerCase(), cotSignDetail);
	    	SystemUtil.setObjBySession(null,signMap, "sign");
	    }else{
	    	HashMap<String, CotSignDetail> signMap = (HashMap<String, CotSignDetail>) obj;
	    	signMap.put(eleId.toLowerCase(), cotSignDetail);
	    	SystemUtil.setObjBySession(null,signMap, "sign");
	    }
		
	}
	
	//Action获取signMap
	@SuppressWarnings("unchecked")
	public  HashMap<String, CotSignDetail> getSignMapAction(HttpSession session) {
		Object obj =  SystemUtil.getObjBySession(session,"sign");
		HashMap<String, CotSignDetail> signMap = (HashMap<String, CotSignDetail>) obj;
		return signMap ;
	}
	
	//Action储存signMap
	@SuppressWarnings("unchecked")
	public  void setSignMapAction(HttpSession session,String eleId,CotSignDetail cotSignDetail) {
	    Object obj =  SystemUtil.getObjBySession(session,"sign");
	    if(obj==null){
	    	HashMap<String, CotSignDetail> signMap = new HashMap<String, CotSignDetail>();
	    	signMap.put(eleId.toLowerCase(), cotSignDetail);
	    	SystemUtil.setObjBySession(session,signMap, "sign");
	    }else{
	    	HashMap<String, CotSignDetail> signMap = (HashMap<String, CotSignDetail>) obj;
	    	signMap.put(eleId.toLowerCase(), cotSignDetail);
	    	SystemUtil.setObjBySession(session,signMap, "sign");
	    }
		
	}
	
	
	//清空signMap
	public void clearSignMap()
	{
		SystemUtil.clearSessionByType(null,"sign");
	}
	
	
	//清除signMap中eleId对应的映射
	public void delSignMapByKey(String eleId){
		HashMap<String, CotSignDetail> signMap = this.getSignMap();
		if(signMap!=null){
			if(signMap.containsKey(eleId.toLowerCase())){
				signMap.remove(eleId.toLowerCase());
			}
		}
	}
	
	//在Action中清除signMap中eleId对应的映射
	public void delSignMapByKeyAction(String eleId,HttpSession session){
		HashMap<String, CotSignDetail> signMap = this.getSignMapAction(session);
		if(signMap!=null){
			if(signMap.containsKey(eleId.toLowerCase())){
				signMap.remove(eleId.toLowerCase());
			}
		}
	}

	
	//判断货号是否已经添加
	public boolean checkExist(String eleId){
		HashMap<String, CotSignDetail> signMap = this.getSignMap();
		if(signMap!=null){
			if(signMap.containsKey(eleId.toLowerCase())){
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
	
	//通过key获取signMap的value 
	public CotSignDetail getSignMapValue(String eleId){
		HashMap<String, CotSignDetail> signMap = this.getSignMap();
		if(signMap!=null){
			CotSignDetail cotSignDetail = (CotSignDetail) signMap.get(eleId.toLowerCase());
			return cotSignDetail;
		}
		return null;
	}
	
	//通过货号修改Map中对应的征样明细
	public boolean updateMapValueByEleId(String eleId ,String property,String value)
	{
		CotSignDetail cotSignDetail = getSignMapValue(eleId.toLowerCase());
		if(cotSignDetail==null)
			return false;
		try
		{
			BeanUtils.setProperty(cotSignDetail, property, value);
			this.setSignMap(eleId.toLowerCase(), cotSignDetail);
			return true;
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			return false;
		}
		 
	}
	
 
	// 判断要更新到样品表的明细货号哪些重复
	@SuppressWarnings("unchecked")
	public String[] findIsExistInEle(String eleIds) {
		String[] temp = new String[2];
		String[] eleAry = eleIds.split(",");
		String same = "";
		String dis = "";
		String hql = "select obj.eleId from CotElementsNew obj";
		List list = this.getCotBaseDao().find(hql);
		for (int i = 0; i < eleAry.length; i++) {
			boolean flag = false;
			for (int j = 0; j < list.size(); j++) {
				if (list.get(j).toString().equals(eleAry[i])) {
					same += eleAry[i] + ",";
					flag = true;
					break;
				}
			}
			if (!flag) {
				dis += eleAry[i] + ",";
			}
		}
		temp[0] = same;
		temp[1] = dis;
		return temp;
	}

	// 根据样品货号字符串查询明细
	public void updateToEle(String same, String dis, String mainId) {
		String picStr = "";
		//相同时处理
		if(!"".equals(same)){
			String[] eleSameAry = same.split(",");
			String t = "";
			for (int i = 0; i < eleSameAry.length; i++) {
				t+="'"+eleSameAry[i]+"',";
			}
			picStr+=t;
			//查找样品的原来编号
			String hql = "select obj.id,obj.eleId,obj.picPath from CotElementsNew obj where obj.eleId in ("+t.substring(0,t.length()-1)+")";
			List<?> list = this.getCotBaseDao().find(hql);
			
			String hqlDetail = "from CotSignDetail obj where obj.signId="+mainId+" and obj.eleId in ("+t.substring(0,t.length()-1)+")";
			List<?> listDetail = this.getCotBaseDao().find(hqlDetail);
			//将明细转成样品
			List<?> oldlist = SystemUtil.changeToCotElementsNew(listDetail,"sign");
			List<CotElementsNew> listTemp = new ArrayList<CotElementsNew>();
			for (int i = 0; i < oldlist.size(); i++) {
				CotElementsNew element = (CotElementsNew)oldlist.get(i);
				for (int j = 0; j < list.size(); j++) {
					Object[] obj = (Object[]) list.get(j);
					if(obj[1].toString().equals(element.getEleId())){
						//拷贝报价明细的图片到sampleImg底下
						String srcPath = element.getPicPath();
						String toPath = "sampleImg/"+element.getEleId()+".png";
						SystemUtil.copyFile(srcPath, toPath);
						if(!toPath.equals((String) obj[2])){
							//删除原来图片
							SystemUtil.deleteFile((String)obj[2]);
						}
						element.setId((Integer)obj[0]);
						element.setPicPath(toPath);
						listTemp.add(element);
						break;
					}
				}
			}
			this.getCotBaseDao().updateRecords(listTemp);
		}
		//不同的处理
		if(!"".equals(dis)){
			String[] eleDisAry = dis.split(",");
			String temp = "";
			for (int i = 0; i < eleDisAry.length; i++) {
				temp+="'"+eleDisAry[i]+"',";
			}
			picStr+=temp;
			String hql = "from CotSignDetail obj where obj.signId="+mainId+" and obj.eleId in ("+temp.substring(0,temp.length()-1)+")";
			List<?> list = this.getCotBaseDao().find(hql);
			List<?> newlist = SystemUtil.changeToCotElementsNew(list,"sign");
			List<CotElementsNew> listTemp = new ArrayList<CotElementsNew>();
			for (int i = 0; i < newlist.size(); i++) {
				CotElementsNew element = (CotElementsNew)newlist.get(i);
				//拷贝报价明细的图片到sampleImg底下
				String srcPath = element.getPicPath();
				String toPath = "sampleImg/"+element.getEleId()+".png";
				SystemUtil.copyFile(srcPath, toPath);
				element.setPicPath(toPath);
				listTemp.add(element);
			}
			//获取操作人
			WebContext ctx = WebContextFactory.get();
			CotEmps cotEmps = (CotEmps) ctx.getSession().getAttribute("emp");
			try {
				this.getCotBaseDao().saveRecords(listTemp);
				//添加操作日志
				CotSyslog syslog = new CotSyslog();
				syslog.setEmpId(cotEmps.getId());
				syslog.setOpMessage("添加不存在样品的明细成功");
				syslog.setOpModule("sign");
				syslog.setOpTime(new Date(System.currentTimeMillis()));
				syslog.setOpType(1);  //1:添加 2：修改 3：删除
				List<CotSyslog> logList = new ArrayList<CotSyslog>();
				logList.add(syslog);
				sysLogService.addSysLog(logList);
			} catch (DAOException e1) {
				e1.printStackTrace();
				logger.error("保存不存在样品的明细出错!");
				//添加操作日志
				CotSyslog syslog = new CotSyslog();
				syslog.setEmpId(cotEmps.getId());
				syslog.setOpMessage("添加不存在样品的明细失败，失败原因："+e1.getMessage());
				syslog.setOpModule("sign");
				syslog.setOpTime(new Date(System.currentTimeMillis()));
				syslog.setOpType(1);  //1:添加 2：修改 3：删除
				List<CotSyslog> logList = new ArrayList<CotSyslog>();
				logList.add(syslog);
				sysLogService.addSysLog(logList);
			}
		}
		//修改图片表
		String hql = "select p from CotElementsNew obj,CotPicture p where p.eleId=obj.id and p.picFlag=1 and obj.eleId in ("+picStr.substring(0,picStr.length()-1)+")";
		List<?> list = this.getCotBaseDao().find(hql);
		Iterator<?> it = list.iterator();
		while(it.hasNext()){
			CotPicture picture = (CotPicture)it.next();
			picture.setPicPath("sampleImg/"+picture.getPicName()+".png");
		}
		this.getCotBaseDao().updateRecords(list);
	}
	
	//更新征样图片picImg字段
	public void updatePicImg(String filePath,Integer detailId){
		
		//CotSignDetail cotSignDetail = this.getSignDetailById(detailId);
		//String fileLength = ContextUtil.getProperty("sysconfig.properties","maxLength"); 
		List<CotSignPic> imgList = new ArrayList<CotSignPic>();
		CotOpImgService impOpService = (CotOpImgService)SystemUtil.getService("CotOpImgService");
		CotSignPic cotElePic = impOpService.getSignPic(detailId) ;
		File picFile = new File(filePath);
		FileInputStream in;
		//获取操作人
		WebContext ctx = WebContextFactory.get();
		CotEmps cotEmps = (CotEmps) ctx.getSession().getAttribute("emp");
		try {
			if(cotElePic == null ) return;
			in = new FileInputStream(picFile);
			byte[] b = new byte[(int)picFile.length()];
			while (in.read(b)!= -1) {}
			in.close();
			cotElePic.setPicImg(b);
			cotElePic.setPicSize(b.length);
			imgList.add(cotElePic);
			if(filePath.indexOf("common/images/zwtp.png")<0){
				picFile.delete();
			}
			//修改样品图片
			impOpService.modifyImg(imgList);
			//添加操作日志
			CotSyslog syslog = new CotSyslog();
			syslog.setEmpId(cotEmps.getId());
			syslog.setItemNo(detailId.toString());
			syslog.setOpMessage("修改征样明细图片成功");
			syslog.setOpModule("sign");
			syslog.setOpTime(new Date(System.currentTimeMillis()));
			syslog.setOpType(2);  //1:添加 2：修改 3：删除
			List<CotSyslog> logList = new ArrayList<CotSyslog>();
			logList.add(syslog);
			sysLogService.addSysLog(logList);
			//this.getCotBaseDao().update(cotSignDetail); 
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("修改征样明细图片错误!");
			//添加操作日志
			CotSyslog syslog = new CotSyslog();
			syslog.setEmpId(cotEmps.getId());
			syslog.setItemNo(detailId.toString());
			syslog.setOpMessage("修改征样明细图片失败，失败原因："+e.getMessage());
			syslog.setOpModule("sign");
			syslog.setOpTime(new Date(System.currentTimeMillis()));
			syslog.setOpType(2);  //1:添加 2：修改 3：删除
			List<CotSyslog> logList = new ArrayList<CotSyslog>();
			logList.add(syslog);
			sysLogService.addSysLog(logList);
		}
	}
	
	//删除征样图片picImg
	public boolean deletePicImg(Integer detailId){
		String classPath = CotSignServiceImpl.class.getResource("/").toString();
		String systemPath = classPath.substring(6, classPath.length() - 16);
		String filePath = systemPath+"common/images/zwtp.png";
		List<CotSignPic> imgList = new ArrayList<CotSignPic>();
		CotOpImgService impOpService = (CotOpImgService)SystemUtil.getService("CotOpImgService");
		CotSignPic cotElePic = impOpService.getSignPic(detailId) ;
		//CotSignDetail cotSignDetail = this.getSignDetailById(detailId);
		//String fileLength = ContextUtil.getProperty("sysconfig.properties","maxLength"); 
		File picFile = new File(filePath);
		FileInputStream in;
		//获取操作人
		WebContext ctx = WebContextFactory.get();
		CotEmps cotEmps = (CotEmps) ctx.getSession().getAttribute("emp");
		try {
			in = new FileInputStream(picFile);
			byte[] b = new byte[(int)picFile.length()];
			while (in.read(b)!= -1) {}
			in.close();
			cotElePic.setPicImg(b); 
			//cotElePic.setPicName("zwtp");
			cotElePic.setPicSize(b.length);
			imgList.add(cotElePic);
			impOpService.modifyImg(imgList);
			//添加操作日志
			CotSyslog syslog = new CotSyslog();
			syslog.setEmpId(cotEmps.getId());
			syslog.setItemNo(detailId.toString());
			syslog.setOpMessage("删除征样明细图片成功");
			syslog.setOpModule("sign");
			syslog.setOpTime(new Date(System.currentTimeMillis()));
			syslog.setOpType(3);  //1:添加 2：修改 3：删除
			List<CotSyslog> logList = new ArrayList<CotSyslog>();
			logList.add(syslog);
			sysLogService.addSysLog(logList);
			return true;
			
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("删除征样明细图片错误!");
			//添加操作日志
			CotSyslog syslog = new CotSyslog();
			syslog.setEmpId(cotEmps.getId());
			syslog.setItemNo(detailId.toString());
			syslog.setOpMessage("删除征样明细图片失败，失败原因："+e.getMessage());
			syslog.setOpModule("sign");
			syslog.setOpTime(new Date(System.currentTimeMillis()));
			syslog.setOpType(3);  //1:添加 2：修改 3：删除
			List<CotSyslog> logList = new ArrayList<CotSyslog>();
			logList.add(syslog);
			sysLogService.addSysLog(logList);
			return false;
		}
	}
	//判断厂家是否已存在
	public Integer findIsExistFactory(Integer gid, Integer factoryId) {
		
		List<?> res = this.getCotBaseDao().find(" from CotSign obj where obj.givenId="+gid+" and obj.factoryId="+factoryId);
		if (res.size() >= 1) {
			return 1;
		}
 		return null;
	}

	
	
	 
	
}
