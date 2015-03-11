package com.sail.cot.email.service;

import com.zhao.mail.entity.MailCfg;
import com.sail.cot.domain.CotMailCfg;
/**
 * 
 * 邮件配置信息
 * @author zhao
 *
 */
public interface MailCfgService {
	/**
	 * 更新公共配置信息
	 * @param cotMailCfg
	 */
	public int updateCfg(CotMailCfg cotMailCfg);
	/**
	 * 更新个人配置信息
	 * @param cotMailCfg
	 * @return 
	 */
	public int updateEmpCfg(CotMailCfg cotMailCfg);
	/**
	 * 获得公共配置信息
	 * @return
	 */
	public CotMailCfg getCfg();
	/**
	 * 获得个人配置信息
	 * @param id 员工ID
	 * @return
	 */
	public CotMailCfg getEmpCfg(Integer id);
	/**
	 * 测试连接
	 * @param mailCfg 连接配置
	 * @return 连接信息
	 */
	public int connTest(MailCfg mailCfg);
}
