package com.sail.cot.service.audit.impl;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.directwebremoting.WebContext;
import org.directwebremoting.WebContextFactory;

import com.jason.core.exception.DAOException;
import com.sail.cot.dao.CotBaseDao;
import com.sail.cot.domain.CotAudit;
import com.sail.cot.domain.CotCustomer;
import com.sail.cot.domain.CotEmps;
import com.sail.cot.domain.CotGiven;
import com.sail.cot.domain.CotMessage;
import com.sail.cot.domain.CotOrderDetail;
import com.sail.cot.domain.CotOrderOut;
import com.sail.cot.domain.CotOrderOutdetail;
import com.sail.cot.domain.CotSyslog;
import com.sail.cot.query.QueryInfo;
import com.sail.cot.service.audit.CotAuditService;
import com.sail.cot.service.system.impl.CotAreaServiceImpl;
import com.sail.cot.service.system.impl.SetNoServiceImpl;
import com.sail.cot.util.Log4WebUtil;
import com.sail.cot.util.SystemUtil;

public class CotAuditServiceImpl implements CotAuditService{

	private CotBaseDao auditDao;
	public CotBaseDao getAuditDao() {
		return auditDao;
	}

	public void setAuditDao(CotBaseDao auditDao) {
		this.auditDao = auditDao;
	}
	private Logger logger = Log4WebUtil.getLogger(CotAreaServiceImpl.class);
	
	public void addOrSaveCotAudit(List<CotAudit> cotAuditList) {
		
		this.auditDao.saveOrUpdateRecords(cotAuditList);
		
	}
	//生成核销单
	public boolean saveAudit(String auditNo,Integer auditNum, String receiveDate, String effectDate){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		if (auditNum >0) {
			for (int i = 0; i < auditNum; i++) {
				
				CotAudit cotAudit = new CotAudit();
				if (i<9) {
					cotAudit.setAuditNo(auditNo+"-00"+(i+1));//设置单据编号
				}
				if (i>=9 && i<99) {
					cotAudit.setAuditNo(auditNo+"-0"+(i+1));//设置单据编号
				}
				if (i>=99) {
					cotAudit.setAuditNo(auditNo+"-"+(i+1));//设置单据编号
				}
				cotAudit.setAddTime(new Date(System.currentTimeMillis()));// 修改时间
				// 核销状态 1:空白
				cotAudit.setAuditStatus(1L);//核销状态
			
				List<CotAudit> records = new ArrayList<CotAudit>();
				// 保存单据
				try {
					if (receiveDate != null && !"".equals(receiveDate)) {
						cotAudit.setReceiveDate(new Date(sdf.parse(receiveDate).getTime()));
					}
					if (effectDate != null && !"".equals(effectDate)) {
						cotAudit.setEffectDate(new Date(sdf.parse(effectDate).getTime()));
					}
					records.add(cotAudit);
					this.getAuditDao().saveOrUpdateRecords(records);
				} catch (Exception e) {
					logger.error("生成核销单出错！");
					return false;
				}
			}
		}
		return true;
	}
	
	//处理核销单
	public int savaOrUpdateAudit(CotAudit cotAudit){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		cotAudit.setAddTime(new Date(System.currentTimeMillis()));// 修改时间
		
		List<CotAudit> records = new ArrayList<CotAudit>();
		// 保存单据
		try {
//			if (receiveDate != null && !"".equals(receiveDate)) {
//				cotAudit.setReceiveDate(new Date(sdf.parse(receiveDate).getTime()));
//			}
//			if (effectDate != null && !"".equals(effectDate)) {
//				cotAudit.setEffectDate(new Date(sdf.parse(effectDate).getTime()));
//			}
//			if (usedDate != null && !"".equals(usedDate)) {
//				cotAudit.setUsedDate(new Date(sdf.parse(usedDate).getTime()));
//			}
//			if (submitDate != null && !"".equals(submitDate)) {
//				cotAudit.setSubmitDate(new Date(sdf.parse(submitDate).getTime()));
//			}
//			if (auditDate != null && !"".equals(auditDate)) {
//				cotAudit.setAuditDate(new Date(sdf.parse(auditDate).getTime()));
//			}
//			if (balanceDate != null && !"".equals(balanceDate)) {
//				cotAudit.setBalanceDate(new Date(sdf.parse(balanceDate).getTime()));
//			}
			records.add(cotAudit);
			this.getAuditDao().saveOrUpdateRecords(records);
		} catch (Exception e) {
			logger.error("生成核销单出错！");
			return -1;
		}
		return cotAudit.getId();
	}

	public CotAudit getCotAuditById(Integer Id) {

		return (CotAudit)this.getAuditDao().getById(CotAudit.class, Id);
		
	}

	public List<?> getCotAuditList() {
		
		return this.getAuditDao().getRecords("CotAudit");
		
	}

	public List<?> getList(QueryInfo queryInfo) {
		try {
			return this.getAuditDao().findRecords(queryInfo);
		} catch (DAOException e) {
			 
			e.printStackTrace();
		}
		return null;
	}
	
	public List<?> getAuditList(QueryInfo queryInfo) {
		try {
			return this.getAuditDao().findRecords(queryInfo,CotAudit.class);
		} catch (DAOException e) {
			 
			e.printStackTrace();
		}
		return null;
	}

	public int getRecordCount(QueryInfo queryInfo) {
		try {
			return this.getAuditDao().getRecordsCountJDBC(queryInfo);
		} catch (Exception e) {
			 
			e.printStackTrace();
		}
		return 0;
	}
	//获得当前登陆员工
	public CotEmps getEmps(){
		WebContext ctx = WebContextFactory.get();
		CotEmps cotEmps = (CotEmps) ctx.getSession().getAttribute("emp");
		return cotEmps;
	}
	// 根据目的港编号获得目的港名称
	public String[] getTargetNameById(Integer id) {
		String sql = "select obj.id,obj.targetPortEnName from CotTargetPort obj where obj.id="
				+ id;
		List<?> list = this.getAuditDao().find(sql);
		String[] str = new String[2];
		if (list.size() == 1) {
			Object[] obj = (Object[]) list.get(0);
			str[0] = obj[0].toString();
			str[1] = obj[1].toString();
		}
		return str;
	}
	// 根据币种编号获得币种名称
	public String[] getCurrencyNameById(Integer id) {
		String sql = "select obj.id,obj.curNameEn from CotCurrency obj where obj.id="
				+ id;
		List<?> list = this.getAuditDao().find(sql);
		String[] str = new String[2];
		if (list.size() == 1) {
			Object[] obj = (Object[]) list.get(0);
			str[0] = obj[0].toString();
			str[1] = obj[1].toString();
		}
		return str;
	}
	// 根据出货编号查找出货信息
	public CotOrderOut getOrderOutById(Integer id) {
		CotOrderOut orderOut = (CotOrderOut) this.getAuditDao().getById(
				CotOrderOut.class, id);
		if (orderOut != null) {
			CotOrderOut orderOutClone = (CotOrderOut) SystemUtil
					.deepClone(orderOut);
			orderOutClone.setCotHsInfos(null);
			orderOutClone.setCotOrderOutdetails(null);
			orderOutClone.setCotOrderOuthsdetails(null);
			orderOutClone.setCotShipments(null);
			orderOutClone.setOrderMBImg(null);
			orderOutClone.setCotSymbols(null);
			orderOutClone.setCotSplitInfos(null);
			orderOutClone.setCotOrderouthsRpt(null);
			
			return orderOutClone;
		}
		return null;
	}
	//检查是否超过核销日期
	public void updateStatus(String curDate){
		String hql = " from CotAudit as obj where (obj.auditStatus = 2 or obj.auditStatus = 3) and obj.effectDate <'"+curDate+"'";
		
		List<?> details = this.getAuditDao().find(hql);
		Iterator<?> it = details.iterator();
		while (it.hasNext()) {
			CotAudit cotAudit = (CotAudit) it.next();
			if (cotAudit.getAuditStatus() == 2) {
				cotAudit.setAuditStatus(6L);
			}
			if (cotAudit.getAuditStatus() == 3) {
				cotAudit.setAuditStatus(5L);
			}
			this.getAuditDao().update(cotAudit);
		}
	}
	//计算退税金额
	public Float calTotalTuiLv(String orderNo){
		
		Float totalTui = 0.0f;
		String hql = "select obj from CotOrderOutdetail obj,CotOrderOut o where obj.orderId = o.id and o.orderNo ='" + orderNo+"'";
		List<?> details = this.getAuditDao().find(hql);
		Iterator<?> it = details.iterator();
		while (it.hasNext()) {
			CotOrderOutdetail outDetail = (CotOrderOutdetail) it.next();
			if (outDetail.getBoxCount() != null && outDetail.getTuiLv() != null) {
				totalTui += (Float)((outDetail.getBoxCount()-outDetail.getRemainBoxCount())*outDetail.getTuiLv());
			}
		}
		return totalTui;
	}

	/* (non-Javadoc)
	 * @see com.sail.cot.service.audit.CotAuditService#getJsonData(com.sail.cot.query.QueryInfo)
	 */
	public String getJsonData(QueryInfo queryInfo) throws DAOException {
		// TODO Auto-generated method stub
		return this.getAuditDao().getJsonData(queryInfo);
	}
	
}
