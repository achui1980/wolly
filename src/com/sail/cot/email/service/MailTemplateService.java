package com.sail.cot.email.service;

import com.sail.cot.domain.CotMail;

public interface MailTemplateService {
	//从员工表读取模版
	public String getEmpsMailTemplate(Integer empId,String logo);
	//读取各种模版
	public String getHtmFile(String file,String logo);
	
	//保存模版
	public void saveCotEmpsMailTemplate(Integer empsId,String template,String nodeText);
	//获取当前模版
	public String getCurrentMailTemplate(Integer empsId,String nodeText);
	
	//获得模版
	public String getMailTemplate(Integer empId,String logo,CotMail cotMail);
	
	//获得新建模版
	public String getNewMailTemplate(Integer empId,String logo);
	
	//保存签名
	public boolean savecotEmps(Integer empId,String empsSign);
	
	//获取签名
	public String isExistEmpsSign(Integer empId);
}