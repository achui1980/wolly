package com.sail.cot.mail.service;

import java.util.List;
import com.sail.cot.domain.CotMailCfg;
import com.sail.cot.query.QueryInfo;
public interface MailCfgService {

	
	//获取邮件列表
	List<?> getMailCfgList();
	
	//删除邮件
	public void deleteMailCfg(List<?> mailCfgList);
	
	//根据id获取邮件信息
	CotMailCfg getMailCfgById(Integer Id);
	
    public	int getRecordCount(QueryInfo queryInfo);
    
    public	List<?> getList(QueryInfo queryInfo);

	public void addMailCfg(CotMailCfg cotMailCfg);
	
	public void modifyMailCfg(CotMailCfg cotMailCfg);
	
	public boolean findRecord();
	
	public String updateAutoRecvCfg(String jobName,int mintues,String smtpHost,String pop3Host,String account,String pwd);
	
	public String updateAutoRecvCfgDefault(int minitues);
	
	public void removeAutoRecvCfg(String jobName);
	
	public boolean isRecvCfgRun(String jobName);
	
	public CotMailCfg getMailCfg();
}
