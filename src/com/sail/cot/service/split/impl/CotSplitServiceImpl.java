package com.sail.cot.service.split.impl;

import com.sail.cot.service.split.CotSplitService;
import com.sail.cot.service.system.CotSeqService;
import com.sail.cot.service.system.CotSysLogService;
import com.sail.cot.service.system.impl.CotSeqServiceImpl;
import com.sail.cot.service.system.impl.SetNoServiceImpl;

import java.awt.image.RescaleOp;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpSession;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.log4j.Logger;
import org.directwebremoting.WebContext;
import org.directwebremoting.WebContextFactory;
import com.jason.core.exception.DAOException;
import com.sail.cot.dao.CotBaseDao;
import com.sail.cot.domain.CotContainerType;
import com.sail.cot.domain.CotElementsNew;
import com.sail.cot.domain.CotEmps;
import com.sail.cot.domain.CotFactory;
import com.sail.cot.domain.CotGivenDetail;
import com.sail.cot.domain.CotOrderDetail;
import com.sail.cot.domain.CotOrderOut;
import com.sail.cot.domain.CotOrderOutdetail;
import com.sail.cot.domain.CotPriceDetail;
import com.sail.cot.domain.CotSplitDetail;
import com.sail.cot.domain.CotSplitInfo;
import com.sail.cot.domain.CotSyslog;
import com.sail.cot.query.QueryInfo;
import com.sail.cot.util.Log4WebUtil;
import com.sail.cot.util.SystemUtil;
import com.sail.cot.util.ThreadLocalManager;
import com.sail.cot.util.ThreadObject;
import com.sun.jndi.url.iiopname.iiopnameURLContextFactory;

public class CotSplitServiceImpl implements CotSplitService {

	private CotBaseDao cotBaseDao;
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
	private Logger logger = Log4WebUtil.getLogger(CotSplitServiceImpl.class);
	
	public void addSplit(List SplitList) {
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
			this.getCotBaseDao().saveRecords(SplitList);
			//添加操作日志
			if(ctx != null || obj != null)
			{
				CotSyslog syslog = new CotSyslog();
				syslog.setEmpId(empId);
				syslog.setOpMessage("添加排载单成功");
				syslog.setOpModule("split");
				syslog.setOpTime(new Date(System.currentTimeMillis()));
				syslog.setOpType(1);  //1:添加 2：修改 3：删除
				List<CotSyslog> logList = new ArrayList<CotSyslog>();
				logList.add(syslog);
				sysLogService.addSysLog(logList);
			}
		} catch (DAOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error("添加排载信息异常",e);
			//添加操作日志
			if(ctx != null || obj != null)
			{
				CotSyslog syslog = new CotSyslog();
				syslog.setEmpId(empId);
				syslog.setOpMessage("添加排载单失败,失败原因："+e.getMessage());
				syslog.setOpModule("split");
				syslog.setOpTime(new Date(System.currentTimeMillis()));
				syslog.setOpType(1);  //1:添加 2：修改 3：删除
				List<CotSyslog> logList = new ArrayList<CotSyslog>();
				logList.add(syslog);
				sysLogService.addSysLog(logList);
			}
		}
	}

	public int deleteSplit(List SplitList) {
		List<Integer> ids=new ArrayList<Integer>();
        int res = 0;
		for (int i = 0; i < SplitList.size(); i++) {
			CotSplitInfo cotSplit = (CotSplitInfo) SplitList.get(i);
			Integer orderOutId = (Integer) this.getOrderOutIdBySplitId(cotSplit.getId());
			List<CotSplitDetail> splitDetailRes = this.getEleId(cotSplit.getId());
			if (splitDetailRes != null) {
				for (int j = 0; j < splitDetailRes.size(); j++) {
					CotSplitDetail cotSplitDetail = splitDetailRes.get(j);
					this.addCountAndCbm(cotSplit.getId(),orderOutId,cotSplitDetail.getId());
				}
			}
			this.modifySplitFlag(orderOutId);
			ids.add(cotSplit.getId());
		}
		//获取操作人
		WebContext ctx = WebContextFactory.get();
		CotEmps cotEmps = (CotEmps) ctx.getSession().getAttribute("emp");
		try {
			this.getCotBaseDao().deleteRecordByIds(ids, "CotSplitInfo");
			//添加操作日志
			CotSyslog syslog = new CotSyslog();
			syslog.setEmpId(cotEmps.getId());
			syslog.setOpMessage("删除排载单成功");
			syslog.setOpModule("split");
			syslog.setOpTime(new Date(System.currentTimeMillis()));
			syslog.setOpType(3);  //1:添加 2：修改 3：删除
			List<CotSyslog> logList = new ArrayList<CotSyslog>();
			logList.add(syslog);
			sysLogService.addSysLog(logList);
		} catch (DAOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error("删除排载信息异常",e);
			res = -1;
			//添加操作日志
			CotSyslog syslog = new CotSyslog();
			syslog.setEmpId(cotEmps.getId());
			syslog.setOpMessage("删除排载信息失败,失败原因："+e.getMessage());
			syslog.setOpModule("split");
			syslog.setOpTime(new Date(System.currentTimeMillis()));
			syslog.setOpType(3);  //1:添加 2：修改 3：删除
			List<CotSyslog> logList = new ArrayList<CotSyslog>();
			logList.add(syslog);
			sysLogService.addSysLog(logList);
		}
		return res;
	}

	public Integer findExistByNo(String cNo,Integer orderOutId) {
		String hql = "select obj.id from CotSplitInfo obj where obj.orderOutId ="+orderOutId+" and obj.containerNo='"+cNo+"'";
		List<?> res = this.getCotBaseDao().find(hql);
		if (res.size() != 1) {
			return null;
		} else {
			return (Integer) res.get(0);
		}
	}

	public CotSplitInfo getSplitById(Integer Id) {
		CotSplitInfo cotSplitInfo = (CotSplitInfo) this.getCotBaseDao().getById(CotSplitInfo.class, Id);
		cotSplitInfo.setCotSplitDetails(null);
		return cotSplitInfo;
	}

	public List getSplitList() {
		return this.getCotBaseDao().getRecords("CotSplitInfo");
	}

	public boolean modifySplit(List SplitList) {
		for (int i = 0; i < SplitList.size(); i++) {
			CotSplitInfo cotSplit = (CotSplitInfo)SplitList.get(i);
			Integer id = this.isExistSplitId(cotSplit.getContainerNo());
			if(id!=null && !id.toString().equals(cotSplit.getId().toString())){
				return false;
			}
		}
		//获取操作人
		WebContext ctx = WebContextFactory.get();
		CotEmps cotEmps = (CotEmps) ctx.getSession().getAttribute("emp");
		try {
			this.getCotBaseDao().updateRecords(SplitList);
			//添加操作日志
			CotSyslog syslog = new CotSyslog();
			syslog.setEmpId(cotEmps.getId());
			syslog.setOpMessage("更新排载信息成功");
			syslog.setOpModule("split");
			syslog.setOpTime(new Date(System.currentTimeMillis()));
			syslog.setOpType(2);  //1:添加 2：修改 3：删除
			List<CotSyslog> logList = new ArrayList<CotSyslog>();
			logList.add(syslog);
			sysLogService.addSysLog(logList);
		} catch (Exception e) {
			// TODO: handle exception
			logger.error("更新排载信息异常", e);
			//添加操作日志
			CotSyslog syslog = new CotSyslog();
			syslog.setEmpId(cotEmps.getId());
			syslog.setOpMessage("更新排载信息失败,失败原因："+e.getMessage());
			syslog.setOpModule("split");
			syslog.setOpTime(new Date(System.currentTimeMillis()));
			syslog.setOpType(2);  //1:添加 2：修改 3：删除
			List<CotSyslog> logList = new ArrayList<CotSyslog>();
			logList.add(syslog);
			sysLogService.addSysLog(logList);
		}
		return true;
	}

	public List getList(QueryInfo queryInfo) {
		try {
			return this.getCotBaseDao().findRecords(queryInfo);
		} catch (DAOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	//查询所有发票
	public Map<?, ?> getOrderNoMap(){
		Map<String, String> map = new HashMap<String, String>();
		List<?> list = this.getCotBaseDao().getRecords("CotOrderOut");
		for (int i = 0; i < list.size(); i++) {
			CotOrderOut cotOrderOut = (CotOrderOut) list.get(i);
			map.put(cotOrderOut.getId().toString(), cotOrderOut.getOrderNo());
		}
		return map;
	}
	//查询所有集装箱
	public Map<?, ?> getContainerNoMap(){
		Map<String, String> map = new HashMap<String, String>();
		List<?> list = this.getCotBaseDao().getRecords("CotContainerType");
		for (int i = 0; i < list.size(); i++) {
			CotContainerType cotContainerType = (CotContainerType) list.get(i);
			map.put(cotContainerType.getId().toString(), cotContainerType.getContainerName());
		}
		return map;
	}

	//根据明细编号查找明细单信息
	public CotSplitDetail getSplitDetailById(Integer detailId) {
		CotSplitDetail cotGivenDetail = (CotSplitDetail) this.getCotBaseDao().getById(CotSplitDetail.class, detailId);
		if(cotGivenDetail!=null){
			return cotGivenDetail;
		}
		return null;
	}
	
	public List<?> getList(String objName) {
		return this.getCotBaseDao().getRecords(objName);
	}
	
	public int getRecordCount(QueryInfo queryInfo) {
		try {
			return this.getCotBaseDao().getRecordsCount(queryInfo);
		} catch (DAOException e) {
			e.printStackTrace();
		}
		return 0;
	}

	
    /**
     * 判断排载编号是否重复
     * @param currencyId
     * @return
     */
    @SuppressWarnings("unchecked")
	public Integer isExistSplitId(String cNo){
    	List<Integer> res = new ArrayList<Integer>();
		res = cotBaseDao.find("select c.id from CotSplitInfo c where c.containerNo='"+cNo+"'");
		if(res.size()!=1){
			return null;
		}else{
			return res.get(0);
		}
    }

	public CotContainerType getContainerTypeById(Integer id) {
		CotContainerType cotContainerType = (CotContainerType) this.getCotBaseDao().getById(
				CotContainerType.class, id);
		return cotContainerType;
	}
	
	public CotContainerType getContainerCubeById(Integer id) {
		CotContainerType cotContainerType = (CotContainerType) this.getCotBaseDao().getById(
				CotContainerType.class, id);
		return cotContainerType;
	}

	public CotOrderOut getOrderOutById(Integer id) {
		CotOrderOut cotOrderOut = (CotOrderOut) this.getCotBaseDao().getById(
				CotOrderOut.class, id);
		cotOrderOut.setCotOrderOutdetails(null);
		cotOrderOut.setCotOrderOuthsdetails(null);
		cotOrderOut.setCotSplitInfos(null);
		cotOrderOut.setCotShipments(null);
		cotOrderOut.setCotSymbols(null);
		cotOrderOut.setCotHsInfos(null);
		cotOrderOut.setOrderMBImg(null);
		return cotOrderOut;
	}

	public Boolean deleteSplitDetailList(List<CotSplitDetail> splitDetailList) {
		List<Integer> ids = new ArrayList<Integer>();
		
		for (int i = 0; i < splitDetailList.size(); i++) {
			CotSplitDetail cotSplitDetail = (CotSplitDetail) splitDetailList.get(i);
			ids.add(cotSplitDetail.getId());
			//this.modifyCotSplitTotalCbm(cotSplitDetail.getSplitId());
		}
		//获取操作人
		WebContext ctx = WebContextFactory.get();
		CotEmps cotEmps = (CotEmps) ctx.getSession().getAttribute("emp");
		try {
			this.getCotBaseDao().deleteRecordByIds(ids, "CotSplitDetail");
			//添加操作日志
			CotSyslog syslog = new CotSyslog();
			syslog.setEmpId(cotEmps.getId());
			syslog.setOpMessage("删除排载单的产品明细成功");
			syslog.setOpModule("split");
			syslog.setOpTime(new Date(System.currentTimeMillis()));
			syslog.setOpType(3);  //1:添加 2：修改 3：删除
			List<CotSyslog> logList = new ArrayList<CotSyslog>();
			logList.add(syslog);
			sysLogService.addSysLog(logList);
		} catch (DAOException e) {
			e.printStackTrace();
			logger.error("删除排载单的产品明细异常", e);
			//添加操作日志
			CotSyslog syslog = new CotSyslog();
			syslog.setEmpId(cotEmps.getId());
			syslog.setOpMessage("删除排载单的产品明细失败,失败原因："+e.getMessage());
			syslog.setOpModule("split");
			syslog.setOpTime(new Date(System.currentTimeMillis()));
			syslog.setOpType(3);  //1:添加 2：修改 3：删除
			List<CotSyslog> logList = new ArrayList<CotSyslog>();
			logList.add(syslog);
			sysLogService.addSysLog(logList);
			return false;
		}
		 
		return true;
	}
	// Action储存signMap
	@SuppressWarnings("unchecked")
	public void setMapAction(HttpSession session, Integer orderDetailId,
			CotSplitDetail cotSplitDetail) {
		Object obj = SystemUtil.getObjBySession(session, "split");
		if (obj == null) {
			HashMap<Integer, CotSplitDetail> splitMap = new HashMap<Integer, CotSplitDetail>();
			splitMap.put(orderDetailId, cotSplitDetail);
			SystemUtil.setObjBySession(session, splitMap, "split");
		} else {
			HashMap<Integer, CotSplitDetail> splitMap = (HashMap<Integer, CotSplitDetail>) obj;
			splitMap.put(orderDetailId, cotSplitDetail);
			SystemUtil.setObjBySession(session, splitMap, "split");
		}

	}
	// 根据出货编号字符串查找出货明细
	@SuppressWarnings("unchecked")
	public List<CotOrderOutdetail> findOrderDetailByIds(String ids) {
		List<CotOrderOutdetail> list = new ArrayList();
		String hql = "from CotOrderOutdetail obj where obj.id in ("
				+ ids.substring(0, ids.length() - 1) + ")";
		list = this.getCotBaseDao().find(hql);
		return list;
	}	
	//判断货号是否已经添加
	public boolean checkExist(Integer orderDetailId){
		HashMap<Integer, CotSplitDetail> splitMap = this.getSplitMap();
		if(splitMap!=null){
			if(splitMap.containsKey(orderDetailId)){
				return true;
			}
		}
		return false;
	}
	//获取SplitMap
	@SuppressWarnings("unchecked")
	public  HashMap<Integer, CotSplitDetail> getSplitMap() {
		Object obj =  SystemUtil.getObjBySession(null,"split");
		HashMap<Integer, CotSplitDetail> splitMap = (HashMap<Integer, CotSplitDetail>) obj;
		return splitMap ;
	}
	//储存SplitMap
	@SuppressWarnings("unchecked")
	public  void setSplitMap(Integer orderDetailId,CotSplitDetail cotSplitDetail) {
	    Object obj =  SystemUtil.getObjBySession(null,"split");
	    if(obj==null){
	    	HashMap<Integer, CotSplitDetail> splitMap = new HashMap<Integer, CotSplitDetail>();
	    	splitMap.put(orderDetailId, cotSplitDetail);
			SystemUtil.setObjBySession(null,splitMap, "split");
	    }else{
	    	HashMap<Integer, CotSplitDetail> splitMap = (HashMap<Integer, CotSplitDetail>) obj;
	    	splitMap.put(orderDetailId, cotSplitDetail);
	    	SystemUtil.setObjBySession(null,splitMap, "split");
	    }
	}
	//清空GivenMap
	public void clearSplitMap()
	{
		SystemUtil.clearSessionByType(null,"split");
	}
	//保存主排载单
	public Integer saveOrUpdateSplit(CotSplitInfo cotSplit, String splitDate){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		List<CotSplitInfo> records = new ArrayList<CotSplitInfo>();
		//保存主排载单
		try {
			if (splitDate != null && !"".equals(splitDate)) {
				cotSplit.setSplitDate(new Date(sdf.parse(splitDate).getTime()));
			}
			records.add(cotSplit);
			if(cotSplit.getId() == null)
			{
				//更新全局序列号
//				SetNoServiceImpl setNoService = new SetNoServiceImpl();
//				setNoService.saveSeq("split",cotSplit.getSplitDate().toString());
				CotSeqService cotSeqService=new CotSeqServiceImpl();
				cotSeqService.saveSeq("split");
			}
			this.getCotBaseDao().saveOrUpdateRecords(records);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return cotSplit.getId();
	}
	//判断排载明细是否包含该货号
	public boolean isExistEleId(Integer splitid, Integer orderDetailId){
		List<?> list =  this.getCotBaseDao().find("from CotSplitDetail obj where obj.splitId ="+splitid+" and obj.orderDetailId="+orderDetailId);
		if(list.size()>0){
			return true;
		}
		return false;
	}
	//清除SplitMap中eleId对应的映射
	public void delSplitMapByKey(Integer orderDetailId){
		HashMap<Integer, CotSplitDetail> splitMap = this.getSplitMap();
		if(splitMap!=null){
			if(splitMap.containsKey(orderDetailId)){
				splitMap.remove(orderDetailId);
			}
		}
	}
	//通过key获取SplitMap的value 
	public CotSplitDetail getSplitMapValue(Integer orderDetailId){
		HashMap<Integer, CotSplitDetail> splitMap = this.getSplitMap();
		if(splitMap!=null){
			CotSplitDetail cotSplitDetail = (CotSplitDetail) splitMap.get(orderDetailId);
			return cotSplitDetail;
		}
		return null;
	}
	//通过货号修改Map中对应的排载明细
	public boolean updateMapValueByEleId(Integer orderDetailId ,String property,String value)
	{
		CotSplitDetail cotSplitDetail = getSplitMapValue(orderDetailId);
		if(cotSplitDetail==null)
			return false;
		try
		{
			BeanUtils.setProperty(cotSplitDetail, property, value);
			this.setSplitMap(orderDetailId, cotSplitDetail);
			return true;
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			return false;
		}
	}
	//获取出货明细该货号剩余数量
	public Long checkRemainCount(Integer orderOutId,Integer orderDetailId)
	{
		List<Long> res = new ArrayList<Long>();
		res = cotBaseDao.find("select c.remainBoxCount from CotOrderOutdetail c where c.orderId ="+orderOutId+" and c.id="+orderDetailId);
		if(res.size() != 1){
			return null;
		}else{
			return res.get(0);
		}
	}
	//获取出货明细该货号剩余数量+已出货数量用于判断是否已超出
	public Long getRemainCountAndExist(Integer splitId,Integer orderOutId,Integer orderDetailId)
	{
		Long remainCount = 0L;
		Long outBoxCount = 0L;
		List<Long> res = new ArrayList<Long>();
		res = cotBaseDao.find("select c.remainBoxCount from CotOrderOutdetail c where c.orderId ="+orderOutId+" and c.id="+orderDetailId);
		if(res.size() != 1){
			return null;
		}else{
			if (splitId != -1) {
				Long boxCount = this.getOutCountDetail(splitId,orderDetailId);
				outBoxCount = res.get(0) + boxCount;
			}else {
				outBoxCount = res.get(0);
			}
			
			return outBoxCount;
		}
	}
	//获取出货单中该货号总Cbm
	public Float getTotalCBM(Integer orderOutId,Integer orderDetailId)
	{
		List<Float> res = new ArrayList<Float>();
		res = cotBaseDao.find("select c.totalCbm from CotOrderOutdetail c where c.orderId ="+orderOutId+" and c.id="+orderDetailId);
		if(res.size() != 1){
			return null;
		}else{
			return res.get(0);
		}
	}
	//获取出货单中该货号BoxCbm
	public Float getBoxCBM(Integer orderOutId,Integer orderDetailId)
	{
		List<Float> res = new ArrayList<Float>();
		res = cotBaseDao.find("select c.boxCbm from CotOrderOutdetail c where c.orderId ="+orderOutId+" and c.id="+orderDetailId);
		if(res.size() != 1){
			return null;
		}else{
			return res.get(0);
		}
	}
	//获取出货单中该货号剩余数量
	public Long getRemainBoxCount(Integer orderOutId,Integer orderDetailId)
	{
		List<Long> res = new ArrayList<Long>();
		res = cotBaseDao.find("select c.remainBoxCount from CotOrderOutdetail c where c.orderId ="+orderOutId+" and c.id="+orderDetailId);
		if(res.size() != 1){
			return null;
		}else{
			return res.get(0);
		}
	}
	//获取排载单中该货号
	public List<CotSplitDetail> getEleId(Integer splitId)
	{
		List<CotSplitDetail> res = new ArrayList<CotSplitDetail>();
		res = cotBaseDao.find("from CotSplitDetail c where c.splitId ="+splitId);
		if(res.size() == 0){
			return null;
		}else{
			return res;
		}
	}
	//获取排载单中该货号原来箱数
	public List<CotSplitDetail> getOldContainerCount(Integer splitId,Integer orderDetailId)
	{
		List<CotSplitDetail> res = new ArrayList<CotSplitDetail>();
		res = cotBaseDao.find("from CotSplitDetail c where c.splitId ="+splitId+" and c.orderDetailId="+orderDetailId);
		if(res.size() != 1){
			return null;
		}else{
			return res;
		}
	}
	//Action获取SplitMap
	@SuppressWarnings("unchecked")
	public  HashMap<Integer, CotSplitDetail> getSplitMapAction(HttpSession session) {
		Object obj =  SystemUtil.getObjBySession(session,"split");
		HashMap<Integer, CotSplitDetail> splitMap = (HashMap<Integer, CotSplitDetail>) obj;
		return splitMap ;
	}
	//保存排载单的产品明细
	public Boolean addSplitDetails(List<?> details){
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
			this.getCotBaseDao().saveOrUpdateRecords(details);
			//添加操作日志
			if(ctx != null || obj != null)
			{
				CotSyslog syslog = new CotSyslog();
				syslog.setEmpId(empId);
				syslog.setOpMessage("保存排载单的产品明细成功");
				syslog.setOpModule("split");
				syslog.setOpTime(new Date(System.currentTimeMillis()));
				syslog.setOpType(1);  //1:添加 2：修改 3：删除
				List<CotSyslog> logList = new ArrayList<CotSyslog>();
				logList.add(syslog);
				sysLogService.addSysLog(logList);
			}
			return true;
		} catch (Exception e) {
			logger.error("保存排载单的产品明细出错!");
			//添加操作日志
			if(ctx != null || obj != null)
			{
				CotSyslog syslog = new CotSyslog();
				syslog.setEmpId(empId);
				syslog.setOpMessage("保存排载单的产品明细失败,失败原因："+e.getMessage());
				syslog.setOpModule("split");
				syslog.setOpTime(new Date(System.currentTimeMillis()));
				syslog.setOpType(1);  //1:添加 2：修改 3：删除
				List<CotSyslog> logList = new ArrayList<CotSyslog>();
				logList.add(syslog);
				sysLogService.addSysLog(logList);
			}
			return false;
		}
	}
	//根据排载明细产品的ids删除
	public void deleteDetailByIds(List<Integer> ids){
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
			this.getCotBaseDao().deleteRecordByIds(ids, "CotSplitDetail");
			//添加操作日志
			if(ctx != null || obj != null)
			{
				CotSyslog syslog = new CotSyslog();
				syslog.setEmpId(empId);
				syslog.setOpMessage("根据排载明细产品的ids删除成功");
				syslog.setOpModule("split");
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
				syslog.setOpMessage("根据排载明细产品的ids删除失败,失败原因："+e.getMessage());
				syslog.setOpModule("split");
				syslog.setOpTime(new Date(System.currentTimeMillis()));
				syslog.setOpType(3);  //1:添加 2：修改 3：删除
				List<CotSyslog> logList = new ArrayList<CotSyslog>();
				logList.add(syslog);
				sysLogService.addSysLog(logList);
			}
		}
	}
	//在Action中清除SplitMap中eleId对应的映射
	public void delSplitMapByKeyAction(Integer orderDetailId,HttpSession session){
		HashMap<Integer, CotSplitDetail> splitMap = this.getSplitMapAction(session);
		if(splitMap!=null){
			if(splitMap.containsKey(orderDetailId)){
				splitMap.remove(orderDetailId);
			}
		}
	}
	//更新排载明细的产品
	public Boolean modifySplitDetail(List<CotSplitDetail> detailList) {
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
			this.getCotBaseDao().updateRecords(detailList);
			//添加操作日志
			if(ctx != null || obj != null)
			{
				CotSyslog syslog = new CotSyslog();
				syslog.setEmpId(empId);
				syslog.setOpMessage("更新排载明细的产品成功");
				syslog.setOpModule("split");
				syslog.setOpTime(new Date(System.currentTimeMillis()));
				syslog.setOpType(3);  //1:添加 2：修改 3：删除
				List<CotSyslog> logList = new ArrayList<CotSyslog>();
				logList.add(syslog);
				sysLogService.addSysLog(logList);
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			//添加操作日志
			if(ctx != null || obj != null)
			{
				CotSyslog syslog = new CotSyslog();
				syslog.setEmpId(empId);
				syslog.setOpMessage("更新排载明细的产品失败,失败原因："+e.getMessage());
				syslog.setOpModule("split");
				syslog.setOpTime(new Date(System.currentTimeMillis()));
				syslog.setOpType(3);  //1:添加 2：修改 3：删除
				List<CotSyslog> logList = new ArrayList<CotSyslog>();
				logList.add(syslog);
				sysLogService.addSysLog(logList);
			}
			return false;
		}
	}
	// 修改主单的总cbm
	public void modifyCotSplitTotalCbm(Integer splitId) {
		// 查找主单号对象,修改总cbm
		CotSplitInfo cotSplitInfo = (CotSplitInfo) this.getCotBaseDao().getById(
				CotSplitInfo.class, splitId);
		double totalCbm = 0.0;
		String hql = "from CotSplitDetail obj where obj.splitId = " + splitId;
		List<?> details = this.getCotBaseDao().find(hql);
		Iterator<?> it = details.iterator();
		while (it.hasNext()) {
			CotSplitDetail detail = (CotSplitDetail) it.next();
			//获取该货号在出货单中的总CBM
			
			Float CBM = (Float) this.getTotalCBM(cotSplitInfo.getOrderOutId(), detail.getOrderDetailId());
			//获取出货单中该货号剩余数量
			Long remainBoxCount = (Long) this.getRemainBoxCount(cotSplitInfo.getOrderOutId(), detail.getOrderDetailId());
			//计算剩余箱数
			Long remainContainerCount = (Long)(remainBoxCount/detail.getTotalBoxCount());
			//计算该排载单的总CBM
			if(detail.getBoxCount() == null){
				detail.setBoxCount(remainBoxCount);
			}
			if(detail.getContainerCount() == null){
				detail.setContainerCount(remainContainerCount);
			}
			if (detail.getTotalContainerCont() != 0) {
				
				totalCbm += (detail.getContainerCount()/new Float(detail.getTotalContainerCont()))*CBM;
			}
		}
    	try {
			cotSplitInfo.setTotalCbm(totalCbm);
			this.getCotBaseDao().update(cotSplitInfo);
		} catch (Exception e) {
			logger.error("保存排载总cbm出错!");
		}
	}
	// 删除列表时修改主单的总cbm
	public void modifyCotSplitCbm(Integer splitId,Integer orderDetailId) {
		// 查找主单号对象,修改总cbm
		CotSplitInfo cotSplitInfo = (CotSplitInfo) this.getCotBaseDao().getById(
				CotSplitInfo.class, splitId);
		double totalCbm = 0.0;
		String hql = "from CotSplitDetail obj where obj.orderDetailId = " + orderDetailId;
		List<?> details = this.getCotBaseDao().find(hql);
		//Iterator<?> it = details.iterator();
		//while (it.hasNext()) {
			CotSplitDetail detail = (CotSplitDetail) details.get(0);
			//获取该货号在出货单中的总CBM
			
			Float CBM = (Float) this.getTotalCBM(cotSplitInfo.getOrderOutId(), detail.getOrderDetailId());
			
			//获取排载明细单中已出货箱数
			Long outContainerCount = (Long) detail.getContainerCount();
			if (detail.getTotalContainerCont() != 0) {
				
				totalCbm = cotSplitInfo.getTotalCbm() - (outContainerCount/new Float(detail.getTotalContainerCont()))*CBM;
				//totalCbm += (detail.getContainerCount()/new Float(detail.getTotalContainerCont()))*CBM;
			}

	    try {
			cotSplitInfo.setTotalCbm(totalCbm);
//			this.getCotBaseDao().update(cotSplitInfo);
		} catch (Exception e) {
			logger.error("保存排载总cbm出错!");
		}
	}

	//获取已出货数量
	public Long getOutCount(Integer orderDetailId)
	{	
		Long outCount = 0L;
		List<CotSplitDetail> res = new ArrayList<CotSplitDetail>();
		res = cotBaseDao.find("from CotSplitDetail c where c.orderDetailId="+orderDetailId);
		for (int i = 0; i < res.size(); i++) {
			CotSplitDetail cotSplitDetail = res.get(i);
			outCount += cotSplitDetail.getBoxCount();
		}
		return outCount;
	}
	//根据排载主单号修改出货明细单中的剩余数量及CBM(新增时)
	public void modifyRemainCountAndCbm(Integer splitId) {
		// 查找主单号对象,修改总cbm
		CotSplitInfo cotSplitInfo = (CotSplitInfo) this.getCotBaseDao().getById(
				CotSplitInfo.class, splitId);
		double totalCbm = 0.0;
		String hql = "from CotSplitDetail obj where obj.splitId = " + splitId;
		List<?> details = this.getCotBaseDao().find(hql);
		Iterator<?> it = details.iterator();
		while (it.hasNext()) {
			CotSplitDetail detail = (CotSplitDetail) it.next();
			this.modifyCountAndCbm(splitId,cotSplitInfo.getOrderOutId(),detail.getOrderDetailId());
		}
	}
	//修改出货明细单的剩余数量及cbm(新增时)
	public void modifyCountAndCbm(Integer splitId,Integer orderOutId,Integer orderDetailId) {
		List<CotOrderOutdetail> res = new ArrayList<CotOrderOutdetail>();
		res = cotBaseDao.find("from CotOrderOutdetail obj where obj.orderId =" +orderOutId+" and obj.id="+orderDetailId);
		CotOrderOutdetail cotOrderOutdetail = res.get(0);
		//修改剩余数量
		Long outCount = this.getOutCountDetail(splitId,orderDetailId);
		if (cotOrderOutdetail.getRemainBoxCount() != 0) {
			
			Long remainBoxCount =(Long) (cotOrderOutdetail.getRemainBoxCount()- outCount);
			cotOrderOutdetail.setRemainBoxCount(remainBoxCount);
			//修改剩余cbm
			Float remainCbm =(Float) ((remainBoxCount/new Float(cotOrderOutdetail.getBoxCount()))*cotOrderOutdetail.getTotalCbm());
			cotOrderOutdetail.setRemainTotalCbm(remainCbm);
		}
	    try {
			this.getCotBaseDao().update(cotOrderOutdetail);
		} catch (RuntimeException e) {
			logger.error("修改出货明细剩余数量及cbm出错！");
		}
	}
	//根据排载主单号修改出货明细单中的剩余数量及CBM(已存在时)
	public void updateRemainCountAndCbm(Integer splitId,Integer orderDetailId,Long newBoxCount) {
		// 查找主单号对象,修改总cbm
		CotSplitInfo cotSplitInfo = (CotSplitInfo) this.getCotBaseDao().getById(
				CotSplitInfo.class, splitId);
		double totalCbm = 0.0;
		String hql = "from CotSplitDetail obj where obj.splitId = " + splitId+" and obj.orderDetailId ="+orderDetailId;
		List<?> details = this.getCotBaseDao().find(hql);
		if (details.size() == 1) {
			CotSplitDetail cotSplitDetail = (CotSplitDetail)details.get(0);
			this.updateCountAndCbm(splitId,cotSplitInfo.getOrderOutId(),orderDetailId,cotSplitDetail.getBoxCount(),newBoxCount);
		}
	}
	//修改出货明细单的剩余数量及cbm(已存在时)
	public void updateCountAndCbm(Integer splitId,Integer orderOutId,Integer orderDetailId,Long oldBoxCount,Long newBoxCount) {
		List<CotOrderOutdetail> res = new ArrayList<CotOrderOutdetail>();
		res = cotBaseDao.find("from CotOrderOutdetail obj where obj.orderId =" +orderOutId+" and obj.id="+orderDetailId);
		CotOrderOutdetail cotOrderOutdetail = res.get(0);
		//修改剩余数量
		//Long outCount = this.getOutCount(splitId,eleId);
		//if (cotOrderOutdetail.getRemainBoxCount() != 0) {
			
			Long remainBoxCount =(Long) (cotOrderOutdetail.getRemainBoxCount()+ oldBoxCount - newBoxCount);
			cotOrderOutdetail.setRemainBoxCount(remainBoxCount);
			//修改剩余cbm
			Float remainCbm =(Float) ((remainBoxCount/new Float(cotOrderOutdetail.getBoxCount()))*cotOrderOutdetail.getTotalCbm());
			cotOrderOutdetail.setRemainTotalCbm(remainCbm);

		try {
			this.getCotBaseDao().update(cotOrderOutdetail);
		} catch (RuntimeException e) {
			e.printStackTrace();
			logger.error("修改出货明细剩余数量及cbm出错！");
		}
	}
	
	//获取已出货数量(一张单)
	public Long getOutCountDetail(Integer splitId,Integer orderDetailId)
	{	
		Long outCount = 0L;
		List<CotSplitDetail> res = new ArrayList<CotSplitDetail>();
		res = cotBaseDao.find("from CotSplitDetail c where c.orderDetailId="+orderDetailId+" and c.splitId="+splitId);
		if (res.size() == 1) {
			CotSplitDetail detail = res.get(0);
			outCount = detail.getBoxCount();
		}
		return outCount;
	}
	//修改出货明细单的剩余数量及cbm(删除时)
	public void addCountAndCbm(Integer splitId,Integer orderOutId,Integer detailId) {
		
		CotSplitDetail detail = this.getSplitDetailById(detailId);
		if (detail != null) {
			List<CotOrderOutdetail> res = new ArrayList<CotOrderOutdetail>();
			res = cotBaseDao.find("from CotOrderOutdetail obj where obj.orderId =" +orderOutId+" and obj.id ="+detail.getOrderDetailId());
			if(res == null || res.size()==0) return;
			CotOrderOutdetail cotOrderOutdetail = res.get(0);
			//修改剩余数量
			Long outCount = this.getOutCountDetail(splitId,detail.getOrderDetailId());
			Long remainBoxCount =(Long) (cotOrderOutdetail.getRemainBoxCount()+ outCount);
			cotOrderOutdetail.setRemainBoxCount(remainBoxCount);
			//修改剩余cbm
			Float remainCbm =(Float) (cotOrderOutdetail.getRemainTotalCbm() + (outCount/new Float(cotOrderOutdetail.getBoxCount()))*cotOrderOutdetail.getTotalCbm());
			cotOrderOutdetail.setRemainTotalCbm(remainCbm);
			try {
				this.getCotBaseDao().update(cotOrderOutdetail);
			} catch (RuntimeException e) {
				logger.error("修改出货明细剩余数量及cbm出错！");
			}
		}
	}
	//通过排载主单号获取出货编号
	public Integer getOrderOutIdBySplitId(Integer splitId) {
		// TODO Auto-generated method stub
		List<Integer> res = new ArrayList<Integer>();
		res = cotBaseDao.find("select c.orderOutId from CotSplitInfo c where c.id ="+splitId);
		if(res.size() != 1){
			return null;
		}else{
			return res.get(0);
		}
	}
	// 查找所有报表类型
	public List<?> getRptFileList(Integer rptTypeId) {
		List<?> list = this.getCotBaseDao().find(
				"from CotRptFile f where f.rptType=" + rptTypeId);
		return list;
	}
	//设置出货单中排载标志
	public void modifySplitFlag(Integer orderOutId){
		CotOrderOut cotOrderOut = (CotOrderOut) this.getCotBaseDao().getById(
				CotOrderOut.class, orderOutId);
		Integer flag = 0;
		List<CotOrderOutdetail> res = new ArrayList<CotOrderOutdetail>();
		res = cotBaseDao.find("from CotOrderOutdetail obj where obj.orderId =" +orderOutId+" and obj.remainTotalCbm > 0");
		if (res.size() == 0) {
			flag = 1;
		}
		cotOrderOut.setSplitFlag(flag);
		this.getCotBaseDao().update(cotOrderOut);
	}
	//判断该出货明细是否已添加
	@SuppressWarnings("unchecked")
	public void findIsExistDetail(CotSplitDetail detail){
		Integer orderDetailId = detail.getOrderDetailId();
		long boxCount = detail.getBoxCount();
		long aBoxCount = detail.getTotalBoxCount()/detail.getTotalContainerCont();
		Object obj = SystemUtil.getObjBySession(null, "split");
		if (obj == null) {
			HashMap<Integer, CotSplitDetail> splitMap = new HashMap<Integer, CotSplitDetail>();
			splitMap.put(orderDetailId, detail);
			SystemUtil.setObjBySession(null, splitMap, "split");
		} else {
			HashMap<Integer, CotSplitDetail> splitMap = (HashMap<Integer, CotSplitDetail>) obj;
			CotSplitDetail old = splitMap.get(orderDetailId);
			if (old!=null) {
				long allBox = boxCount+old.getBoxCount();
				old.setBoxCount(allBox);//数量相加
				long containerBox = 0L;
				if(allBox%aBoxCount!=0){
					containerBox = allBox/aBoxCount+1;
				}else{
					containerBox = allBox/aBoxCount;
				}
				old.setContainerCount(containerBox);//箱数相加
				splitMap.put(orderDetailId, old);
			}else{
				splitMap.put(orderDetailId, detail);
			}
			SystemUtil.setObjBySession(null, splitMap, "split");
		}
	}
	//修改出货明细剩余数量及cbm
	public void updateOrderDetail(String detailIds){
		String[] detail = detailIds.split(",");
		List<CotOrderOutdetail> list = new ArrayList<CotOrderOutdetail>();
		for (int i = 0; i < detail.length; i++) {
			String[] temp = detail[i].split("-");
			CotOrderOutdetail cotOrderOutdetail = (CotOrderOutdetail)this.getCotBaseDao().getById(CotOrderOutdetail.class, Integer.parseInt(temp[0]));
			if(cotOrderOutdetail!=null){
				Long boxCount = Long.parseLong(temp[1]);
				Long remainBoxCount = cotOrderOutdetail.getBoxCount() - boxCount;
				cotOrderOutdetail.setRemainBoxCount(remainBoxCount);
				Float remainTotalCbm = (remainBoxCount/new Float(cotOrderOutdetail.getBoxCount()))*cotOrderOutdetail.getTotalCbm();
				cotOrderOutdetail.setRemainTotalCbm(remainTotalCbm);
				list.add(cotOrderOutdetail);
			}
		}
		this.getCotBaseDao().updateRecords(list);
	}
	//统计柜数
	public List<String> statContainer(Integer orderOutId){
		
		List<String> list = new ArrayList<String>();

		List<CotSplitInfo> res = new ArrayList<CotSplitInfo>();
		res = cotBaseDao.find(" select distinct obj.containerType from CotSplitInfo obj where obj.orderOutId ="+orderOutId);
		for (int i = 0; i < res.size(); i++) {

			List<Integer> count = new ArrayList<Integer>();
			count = cotBaseDao.find("select obj from CotSplitInfo obj where obj.orderOutId ="+orderOutId+" and obj.containerType ="+res.get(i));
			
			String containerName = null;
			List<CotContainerType> name = new ArrayList<CotContainerType>();
			name = cotBaseDao.find(" from CotContainerType obj where obj.id ="+res.get(i));
			if (name.size() != 1) {
				return null;
			}else {
				CotContainerType containerType =(CotContainerType) name.get(0);
				containerName = containerType.getContainerName();
			}			
			String allList = count.size()+"x"+containerName;
			
			list.add(allList);
		}
		return list;
	}
	
	// 判断货号是否已经添加
	public boolean checkExistDetail(Integer orderDetailId) {
		HashMap<Integer, CotSplitDetail> splitMap = this.getSplitMap();
		if (splitMap != null) {
			if (splitMap.containsKey(orderDetailId)) {
				return true;
			}
		}
		return false;
	}	
	
	//查找该产品是否出货记录
	@SuppressWarnings("unchecked")
	public Object findDetail(String splitId,Integer id) {
		
		boolean flag = false;
		if (!"null".equals(splitId) &&!"".equals(splitId)) {
			flag = this.checkExistDetail(id);
		}
		if (flag) {
			return null;
		} 
		Object rtn = new Object();
		String hql = "select obj from CotOrderOutdetail obj where obj.id ="+id;
		
		List<?> list = this.getCotBaseDao().find(hql);
		
		Object objTemp = (Object) list.get(0);
		CotOrderOutdetail detail = (CotOrderOutdetail) objTemp;
			
		return detail;
	
	}	
}
