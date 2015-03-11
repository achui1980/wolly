package com.sail.cot.service.backtax.impl;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.directwebremoting.WebContext;
import org.directwebremoting.WebContextFactory;

import sun.tools.jar.resources.jar;

import com.jason.core.exception.DAOException;
import com.sail.cot.dao.CotBaseDao;
import com.sail.cot.domain.CotAudit;
import com.sail.cot.domain.CotBacktax;
import com.sail.cot.domain.CotEmps;
import com.sail.cot.domain.CotSplitDetail;
import com.sail.cot.domain.CotSplitInfo;
import com.sail.cot.domain.CotSyslog;
import com.sail.cot.query.QueryInfo;
import com.sail.cot.service.backtax.CotBackTaxService;
import com.sail.cot.util.Log4WebUtil;

public class CotBackTaxServiceImpl implements CotBackTaxService{

	private CotBaseDao backTaxDao;
	public CotBaseDao getBackTaxDao() {
		return backTaxDao;
	}

	public void setBackTaxDao(CotBaseDao backTaxDao) {
		this.backTaxDao = backTaxDao;
	}
	private Logger logger = Log4WebUtil.getLogger(CotBackTaxServiceImpl.class);
	
	public void addOrSaveCotBackTax(List<CotBacktax> backTaxList) {
		
		this.backTaxDao.saveOrUpdateRecords(backTaxList);
		
	}
	
	//处理退税单
	@SuppressWarnings("unchecked")
	public int savaOrUpdateBackTax(CotBacktax cotBackTax,String taxDate,String[] auditNos){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		cotBackTax.setAddTime(new Date(System.currentTimeMillis()));// 修改时间

		List<CotBacktax> records = new ArrayList<CotBacktax>();
		// 保存退税单
		try {
			if (taxDate != null && !"".equals(taxDate)) {
				cotBackTax.setTaxDate(new Date(sdf.parse(taxDate).getTime()));
			}
			if (cotBackTax.getTaxAmount().doubleValue() == cotBackTax.getTaxRealAmount().doubleValue()) {
				cotBackTax.setTaxStatus(1L);
			}else {
				cotBackTax.setTaxStatus(2L);
			}
		
			records.add(cotBackTax);
			this.getBackTaxDao().saveOrUpdateRecords(records);
			
			//关联核销单
//			for (int i = 0; i < auditNos.length; i++) {
//				List<CotAudit> audit = new ArrayList<CotAudit>();
//				String hql = " from CotAudit obj where obj.auditNo ='"+auditNos[i]+"'";
//				List<?> res = this.getBackTaxDao().find(hql);
//				CotAudit cotAudit = (CotAudit) res.get(0);
//				cotAudit.setTaxId(cotBackTax.getId());
//				audit.add(cotAudit);
//				this.getBackTaxDao().updateRecords(audit);
//				
//			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("生成退税单出错！");
			return -1;
		}
		return cotBackTax.getId();
	}

	//退税单中删除核销单时修改核销表中的taxId为空
	public boolean modifyTaxId(Integer taxId,List ids){
		//关联核销单
		try {
			for (int i = 0; i < ids.size(); i++) {
				List<CotAudit> audit = new ArrayList<CotAudit>();
				CotAudit cotAudit = (CotAudit) this.getBackTaxDao().getById(CotAudit.class,new Integer(ids.get(i).toString()) );
				cotAudit.setTaxId(null);
				audit.add(cotAudit);
				this.getBackTaxDao().updateRecords(audit);
				//修改退税单的实际退税金额
				List<CotBacktax> records = new ArrayList<CotBacktax>();
				CotBacktax cotBacktax = this.getCotBackTaxById(taxId);
				cotBacktax.setTaxRealAmount(cotBacktax.getTaxRealAmount()-cotAudit.getTotalTui());
				records.add(cotBacktax);
				this.getBackTaxDao().updateRecords(records);
			}
		} catch (Exception e) {
			logger.error("修改出错");
			return false;
		}
		return true;
	}
	public CotBacktax getCotBackTaxById(Integer Id) {

		return (CotBacktax)this.getBackTaxDao().getById(CotBacktax.class, Id);
		
	}

	public List<?> getCotBackTaxList() {
		
		return this.getBackTaxDao().getRecords("CotBacktax");
		
	}
	
	public List<?> getAuditList(Integer txtId) {
		
		List<CotAudit> res = new ArrayList<CotAudit>();
		
		res = this.getBackTaxDao().find(" from CotAudit obj where obj.taxId="+txtId);
		for (int i = 0; i < res.size(); i++) {
			CotAudit cotAudit = res.get(i);
			res.add(cotAudit);
		}
		
		return res;
		
	}

	public List<?> getList(QueryInfo queryInfo) {
		try {
			return this.getBackTaxDao().findRecords(queryInfo);
		} catch (DAOException e) {
			 
			e.printStackTrace();
		}
		return null;
	}

	public int getRecordCount(QueryInfo queryInfo) {
		try {
			return this.getBackTaxDao().getRecordsCount(queryInfo);
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
	
	//查询所有用户姓名
	public Map<?, ?> getEmpsMap(){
		Map<String, String> map = new HashMap<String, String>();
		List<?> list = this.getBackTaxDao().getRecords("CotEmps");
		for (int i = 0; i < list.size(); i++) {
			CotEmps cotEmps = (CotEmps) list.get(i);
			map.put(cotEmps.getId().toString(), cotEmps.getEmpsName());
		}
		return map;
	}
	
	public CotAudit getCotAuditById(Integer Id) {

		return (CotAudit)this.getBackTaxDao().getById(CotAudit.class, Id);
		
	}
	//删除退税单
	public int deleteBackTax(List<CotBacktax> backtaxList) {
		List<Integer> ids=new ArrayList<Integer>();
        int res = 0;
		for (int i = 0; i < backtaxList.size(); i++) {
			CotBacktax cotBackTax = (CotBacktax) backtaxList.get(i);

			String hql = " from CotAudit obj where obj.taxId =" +cotBackTax.getId();
			List<?> auditList =this.getBackTaxDao().find(hql);
			List<CotAudit> records = new ArrayList<CotAudit>();
			if(auditList.size() > 0){
				for (int j = 0; j < auditList.size(); j++) {
					CotAudit cotAudit = (CotAudit) auditList.get(j);
					cotAudit.setTaxId(null);
					records.add(cotAudit);
				}
				this.getBackTaxDao().updateRecords(records);
			}
			ids.add(cotBackTax.getId());
		}
		try {
			this.getBackTaxDao().deleteRecordByIds(ids, "CotBacktax");
		} catch (DAOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error("删除退税单异常",e);
			res = -1;
		}
		return res;
	}
	//判断退税单是否存在
	public Integer findExistByNo(String taxNo) {
		String hql = "select obj.id from CotBacktax obj where obj.taxNo='"+taxNo+"'";
		List<?> res = this.getBackTaxDao().find(hql);
		if (res.size() != 1) {
			return null;
		} else {
			return (Integer) res.get(0);
		}
	}
	//判断核销单是否已包含
	public List<?> isExistAuditNo(Integer id){
		List<CotAudit> res = new ArrayList<CotAudit>();
		List<?> list =  this.getBackTaxDao().find("from CotAudit obj where obj.taxId ="+id);
		if(list.size()>0){
			for (int i = 0; i < list.size(); i++) {
				CotAudit cotAudit = (CotAudit) list.get(i);
				res.add(cotAudit);
			}
		}
		return res;
	}

	/* (non-Javadoc)
	 * @see com.sail.cot.service.backtax.CotBackTaxService#getJsonData(com.sail.cot.query.QueryInfo)
	 */
	public String getJsonData(QueryInfo queryInfo) throws DAOException {
		// TODO Auto-generated method stub
		return this.getBackTaxDao().getJsonData(queryInfo);
	}
}
