/**
 * 
 */
package com.sail.cot.mail.ruleservice;

import java.util.List;
import java.util.Map;


import com.sail.cot.domain.CotCustContact;
import com.sail.cot.domain.CotCustomer;
import com.sail.cot.domain.CotEmps;
import com.sail.cot.domain.CotMailEmpsRule;
import com.sail.cot.domain.CotMailExecute;

import com.sail.cot.domain.CotMailRule;

import com.sail.cot.domain.vo.CotCustomerVO;
import com.sail.cot.domain.vo.CotEmpsRuleVO;
import com.sail.cot.domain.vo.CotFactoryVO;
import com.sail.cot.mail.MailExecuteAction;
import com.sail.cot.query.QueryInfo;


//import com.sail.cot.mail.MailExecuteAction;


/**
 * <p>Title: 旗行办公自动化系统（OA）</p>
 * <p>Description:</p>
 * <p>Copyright: Copyright (c) 2008</p>
 * <p>Company: </p>
 * <p>Create Time: Jul 12, 2010 3:45:16 PM </p>
 * <p>Class Name: MailRuleService.java </p>
 * @author achui
 *
 */
public interface MailCheckService {

	//生成规则引擎需要的XML文件,根据员工Id生成
	public String saveRuleXmlFile(Integer ruleId);
	
	//通过ruleId获取从表-邮件规则
	public List<CotMailRule> getMailRuleById(Integer ruleId);
	
	//通过custId获取从表-邮件规则
	public List<CotMailRule> getMailRuleByEmpsId(Integer custId,String type,Integer ruleId);
	
	public CotMailRule getCotMailRuleById(Integer id) ;
	
	//获取邮件条件配置信息
	public List<CotMailExecute> getMailExecuteById(Integer ruleId);
	
	public List<CotMailExecute> getExecuteById(QueryInfo queryInfo);
	
	public CotMailExecute getCotMailExecuteById(Integer id) ;

	//获取默认的邮件规则信息
	public CotMailEmpsRule getDefaultCotMailEmpsRule(Integer empId);
	
	//根据ID获得邮件规则信息
	public CotMailEmpsRule getMailEmpsRuleById(Integer ruleId);

	//根据条件获得邮件规则信息
	public List<CotEmpsRuleVO> getMailEmpsRuleList(QueryInfo queryInfo);
	
	//删除主表邮件规则信息
    public Boolean deleteEmpsRuleList(List<Integer> ruleIds);
	
    //保存邮件规则信息
	public Integer saveOrUpdateMailEmpsRule(CotMailEmpsRule mailEmpsRule);
		
	//通过custId获取从表-规则条件 
	public List<CotMailExecute> getMailExecuteByCustId(Integer custId,String type,Integer ruleId);

	
	//获得邮件规则信息数量
	public Integer getRecordCount(QueryInfo queryInfo);

	//保存从表规则信息
	public void saveCotMailRule(List<CotMailRule> records);
	
	//修改从表规则信息
	public void modifyCotMailRule(List list);

	// 删除
	public void deleteList(List list, String tabName);
	
	//查询员工
	public List<CotEmps> getEmpsList(QueryInfo queryInfo);
	
	//通过ID查找出客户对象
	public CotCustomer getCustomerById(Integer id);
	
	//查找所有业务员
	public List<CotEmps> getAllCotEmps();
	
	//通过客户ID查找相对应的联系人
	public List<CotCustContact> getCotCustContactByCustId(Integer custId);
	
	//查找所有 主表-规则
	public List<CotMailEmpsRule> getAllCotMailEmpsRule();

}
