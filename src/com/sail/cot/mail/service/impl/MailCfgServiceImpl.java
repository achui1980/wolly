package com.sail.cot.mail.service.impl;

import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;
import com.jason.core.exception.DAOException;
import com.sail.cot.dao.CotBaseDao;
import com.sail.cot.domain.CotEmps;
import com.sail.cot.domain.CotMailCfg;
import com.sail.cot.mail.scheduler.SchedulerMail;
import com.sail.cot.mail.service.MailCfgService;
import com.sail.cot.query.QueryInfo;
import com.sail.cot.util.Log4WebUtil;

public class MailCfgServiceImpl extends SchedulerMail implements MailCfgService {

	private CotBaseDao cotBaseDao;
	
	public CotBaseDao getCotBaseDao() {
		return cotBaseDao;
	}

	public void setCotBaseDao(CotBaseDao cotBaseDao) {
		this.cotBaseDao = cotBaseDao;
	}
	private Logger logger = Log4WebUtil.getLogger(MailCfgServiceImpl.class);
	
	
	public void deleteMailCfg(List<?> mailCfgList) {
		List<Integer> ids = new ArrayList<Integer>();
		for (int i = 0; i < mailCfgList.size(); i++) {
			CotMailCfg cotMailCfg = (CotMailCfg) mailCfgList.get(i);
			ids.add(cotMailCfg.getId());
		}
		try {
			this.getCotBaseDao().deleteRecordByIds(ids, "CotMailCfg");
		} catch (DAOException e) {
			 
			e.printStackTrace();
			logger.error("删除邮件配置信息异常",e);
		}
	}

	public List<?> getList(QueryInfo queryInfo) {
		try {
			return this.getCotBaseDao().findRecords(queryInfo);
		} catch (DAOException e) {
			 
			e.printStackTrace();
		}
		return null;
	}

	public List<?> getMailCfgList() {
		return this.getCotBaseDao().getRecords("CotMailCfg");
	}

	public CotMailCfg getMailCfgById(Integer Id) {
		return (CotMailCfg) this.getCotBaseDao().getById(CotMailCfg.class, Id);
	}

	public int getRecordCount(QueryInfo queryInfo) {
		try {
			return this.getCotBaseDao().getRecordsCount(queryInfo);
		} catch (DAOException e) {
			 
			e.printStackTrace();
		}
		return 0;
	}

	public void addMailCfg(CotMailCfg cotMailCfg) {
		this.getCotBaseDao().create(cotMailCfg);
	}

	public void modifyMailCfg(CotMailCfg cotMailCfg) {
		
		cotMailCfg.setDefaultDebug(1L);
		this.getCotBaseDao().update(cotMailCfg);
	}
	
	public boolean findRecord() {
		boolean isHaveRecord = false;
		List<?> record = this.getCotBaseDao().find("from CotMailCfg obj");
		if (record.size()>0) {
			isHaveRecord = true;
		}
		return isHaveRecord;
	}

	//更新员工信息
	@SuppressWarnings("unchecked")
	public void updateCotEmps(String empsNo,int mintues) {
		String sql = " from CotEmps t where t.empsId = '"+empsNo+"'";
		List<CotEmps> list = this.getCotBaseDao().find(sql);
		if(list != null && list.size() >0){
			CotEmps emps = (CotEmps)list.get(0);
			emps.setEmpsMintues(new Long(mintues));
			//List<CotEmps> records = new ArrayList<CotEmps>();
			//records.add(emps);
			this.getCotBaseDao().update(emps);
		}
	}
	
	//更新邮箱默认配置
	public void updateCotMailCfg(CotMailCfg cfg,int mintues) {
		if(cfg!= null){
			cfg.setDefaultMintues(new Long(mintues));
			List<CotMailCfg> records = new ArrayList<CotMailCfg>();
			records.add(cfg);
			this.getCotBaseDao().updateRecords(records);
		}
	}
	
	public String updateAutoRecvCfg(String jobName, int mintues, String smtpHost,
			String pop3Host, String account, String pwd) {
		String res = "";
		try {
			this.addMailAuto(jobName, mintues, smtpHost, pop3Host, account, pwd);
		} catch (Exception e) {
			res = e.getMessage();
			if(res == null || "".equals(res))
				res = "未知错误";
			System.out.println("===========+"+res);
			e.printStackTrace();
			return res;
		}
		//更新员工信息
		this.updateCotEmps(jobName, mintues);
		return res;
	}

	 
	public String updateAutoRecvCfgDefault(int minitues) {
		List<?> list = getMailCfgList();
		CotMailCfg cfg = (CotMailCfg)list.get(0);
		String res = "";
		try {
			this.addMailAuto("DEFAULTCFG", minitues, cfg.getDefaultHostSmtp(), cfg.getDefaultHost(), cfg.getDefaultAccount(), cfg.getDefaultPwd());
		} catch (Exception e) {
			res = e.getMessage();
			if(res == null || "".equals(res))
				res = "未知错误";
			System.out.println("=====CFG======+"+res);
			e.printStackTrace();
			return res;
		}
		//更新邮箱默认配置
		cfg.setDefaultMintues(new Long(minitues));
		this.getCotBaseDao().update(cfg);
		return res;
	}
	
	public void removeAutoRecvCfg(String jobName){
		this.removeJob(jobName);
	}
	
	public boolean isRecvCfgRun(String jobName){
		return this.isRun(jobName);
	}
	
	public CotMailCfg getMailCfg(){
		String hql = " from CotMailCfg as m";
		List<CotMailCfg> res = new ArrayList<CotMailCfg>();
		res = this.getCotBaseDao().find(hql);
		if (res.size() !=0 ) {
			CotMailCfg cotMailCfg = res.get(0);
			return cotMailCfg;
		}else {
			return null;
		}
	}
	
}
