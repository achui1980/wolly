package com.sail.cot.service.customer.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.jason.core.exception.DAOException;
import com.sail.cot.dao.CotBaseDao;
import com.sail.cot.domain.CotCustContact;
import com.sail.cot.domain.CotCustomer;
import com.sail.cot.domain.CotMailEmpsRule;
import com.sail.cot.domain.CotMailExecute;
import com.sail.cot.domain.CotMailExecuteCfg;
import com.sail.cot.domain.CotMailRule;
import com.sail.cot.domain.CotMailRuleCfg;
import com.sail.cot.mail.ruleservice.MailRuleService;
import com.sail.cot.mail.ruleservice.impl.MailRuleServiceImpl;
import com.sail.cot.query.QueryInfo;
import com.sail.cot.service.customer.CotCustContactService;
import com.sail.cot.util.Log4WebUtil;

public class CotCustContactServiceImpl implements CotCustContactService {

	private CotBaseDao cotBaseDao;
	private MailRuleService mailRuleService;
	
	public CotBaseDao getCotBaseDao() {
		return cotBaseDao;
	}

	public void setCotBaseDao(CotBaseDao cotBaseDao) {
		this.cotBaseDao = cotBaseDao;
	}

	public MailRuleService getMailRuleService() {
		return mailRuleService;
	}

	public void setMailRuleService(MailRuleService mailRuleService) {
		this.mailRuleService = mailRuleService;
	}
	private Logger logger = Log4WebUtil.getLogger(CotCustContactServiceImpl.class);
	
	public CotCustContact addCustContact(CotCustContact cotCustContact) {
		 
		this.getCotBaseDao().create(cotCustContact);
		return cotCustContact;
	}
	
	public void modifyCustContact(CotCustContact cotCustContact) {
		
		this.getCotBaseDao().update(cotCustContact);
	}
	
	public void deleteCustContact(List<CotCustContact> CustContactList) {
		 
		List<Integer> ids=new ArrayList<Integer>();
        for(int i=0; i<CustContactList.size(); i++)
		{
        	CotCustContact cotCustContact = (CotCustContact)CustContactList.get(i);
			ids.add(cotCustContact.getId());
		}
         try{
        	this.getCotBaseDao().deleteRecordByIds(ids, "CotCustContact");
        }
        catch(DAOException e)
        {
        	logger.error("删除联系人信息异常",e);
        }
	}
  
	public boolean findExistByName(String name) {
		 
		QueryInfo queryInfo = new QueryInfo();
		boolean isExist = false;
		queryInfo.setSelectString("from CotCustContact obj where obj.contactPerson='"+name+"'");
		queryInfo.setCountQuery("select count(*) from CotCustContact obj where obj.contactPerson='"+name+"'");
		try {
			int count = this.getCotBaseDao().getRecordsCount(queryInfo);
			if(count > 0)
				isExist =  true;
		} catch (DAOException e) {
			 
			logger.error("查找重复方法失败", e);
		}
		return isExist;
	}
	@SuppressWarnings("unchecked")
	public Integer findExistByEMail(String email){
		try {
			String hql = "from CotCustContact obj where obj.contactEmail=?";
			Object[] objs = new Object[1];
			objs[0] = email;
			List<CotCustContact> contacts = this.getCotBaseDao().queryForLists(hql, objs);
			if(contacts.size() > 0){
				return contacts.get(0).getId();
			}
		} catch (Exception e) {
			logger.error("查找重复方法失败", e);
		}
		return null;
	}

	public CotCustContact getCustContactById(Integer Id) {
		 
		return (CotCustContact)this.getCotBaseDao().getById(CotCustContact.class, Id);
	}

	public List<?> getCustContactList() {
		 
		return this.getCotBaseDao().getRecords("CotCustContact");
	}

	public Map<?, ?> getCustomerMap() {
		Map<String, String> map = new HashMap<String, String>();
		String sql = "select obj.id,obj.customerShortName from CotCustomer obj";
		List<?> list = this.getCotBaseDao().find(sql);
		for(int i=0;i<list.size();i++)
		{
			Object[] cotCustomer = (Object[])list.get(i);
			map.put(cotCustomer[0].toString(), (String)cotCustomer[1]);
		}
		 return map;
	}

	 
    public void modify(CotCustContact cotCustContact) {
		 
		List<CotCustContact> list = new ArrayList<CotCustContact>();
		list.add(cotCustContact);
		try {
			this.getCotBaseDao().updateRecords(list);
		} catch (Exception e) {
			 
			 e.printStackTrace();
			 logger.error("更新联系人信息异常", e);
		}
	}
	
	public List<?> getList(QueryInfo queryInfo) {
		 
		try {
			return this.getCotBaseDao().findRecords(queryInfo);
		} catch (DAOException e) {
			 
			e.printStackTrace();
		}
		return  null;
	}
	
	public int getRecordCount(QueryInfo queryInfo) {
		 
		try {
			return this.getCotBaseDao().getRecordsCount(queryInfo);
		} catch (DAOException e) {
			 
			e.printStackTrace();
		}
		return  0;
	}

	/* (non-Javadoc)
	 * @see com.sail.cot.service.customer.CotCustContactService#getJsonData(com.sail.cot.query.QueryInfo)
	 */
	public String getJsonData(QueryInfo queryInfo) throws DAOException {
		// TODO Auto-generated method stub
		return this.getCotBaseDao().getJsonData(queryInfo);
	}
	
	//保存从表邮件规则
	public void saveCotMailRule(List<CotMailRule> records){
		this.getCotBaseDao().saveOrUpdateRecords(records);
	}
	
	//保存从表执行条件
	public void saveCotMailExecute(List<CotMailExecute> records){
		this.getCotBaseDao().saveOrUpdateRecords(records);
	}
	
	public String saveRuleXmlFile(Integer empRuleId){
	//	MailRuleServiceImpl mailRule =new MailRuleServiceImpl();
		try {
			System.out.println(empRuleId);
			mailRuleService.saveRuleXmlFile(empRuleId);
			mailRuleService.createPublicMailRuleFile();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	} 
	//通过ID查找执行内容对象
	public CotMailExecuteCfg getCotMailExecuteCfgById(Integer id) {
		// TODO Auto-generated method stub
		String sql = " from CotMailExecuteCfg obj where obj.id="+id;
		List<CotMailExecuteCfg> res = this.getCotBaseDao().find(sql);
		if(res != null && res.size() > 0)
			return res.get(0);
		return null;
	}
	
	//通过ID查找邮件规则对象
	public CotMailRuleCfg getCotMailRuleCfgById(Integer id) {
		// TODO Auto-generated method stub
		String sql = " from CotMailRuleCfg obj where obj.id="+id;
		List<CotMailRuleCfg> res = this.getCotBaseDao().find(sql);
		if(res != null && res.size() > 0)
			return res.get(0);
		return null;
	}
	
	//增加联系人时顺便增加规则
	public void addRule(Integer custId,Integer custContactId,String em) {
		CotMailExecuteCfg regExc=this.getCotMailExecuteCfgById(2);
		CotMailExecuteCfg Exc=this.getCotMailExecuteCfgById(3);
		CotMailRuleCfg cotMailRuleCfg=this.getCotMailRuleCfgById(1);
		String strSql = " from CotCustomer obj where obj.id ="+custId;
		List<CotCustomer> res = this.getCotBaseDao().find(strSql);
		CotCustomer cotCustomer=null;
		if(res.size()>0){
			//客户对象
			cotCustomer=res.get(0);
			if(cotCustomer.getEmpId()!=null){
				int empId=cotCustomer.getEmpId();
				String sql=" from CotMailEmpsRule obj where obj.empId="+empId;
				List<CotMailEmpsRule> list=this.getCotBaseDao().find(sql);
				if(list.size()>0){
					    for(int i=0;i<list.size();i++){
						//主表规则对象
						CotMailEmpsRule empRule=list.get(i);
						if("MAIL".equals(empRule.getType())){
						//保存从表邮件规则
						List<CotMailRule> ruleList = new ArrayList<CotMailRule>();
						CotMailRule mailRule= new CotMailRule();
						mailRule.setLeftTerm(cotMailRuleCfg.getProperty());
						mailRule.setOp("contains");
						mailRule.setRelate("AND");
						mailRule.setRuleCfgId(1);
						mailRule.setRuleId(empRule.getId());
						//联系人ID
						mailRule.setCustId(custContactId);
						mailRule.setRightTerm(em);
						mailRule.setType("CUST");
						ruleList.add(mailRule);
						this.saveCotMailRule(ruleList);
						
						//保存 从表-条件 指派给
						List<CotMailExecute> excArray = new ArrayList<CotMailExecute>();
						CotMailExecute mailExc = new CotMailExecute();
						mailExc.setMethod(regExc.getMethod());
						mailExc.setName(regExc.getName());
						mailExc.setType("CUST");
						mailExc.setPackage_(regExc.getPackage_());
						mailExc.setClass_(regExc.getClass_());
						mailExc.setExecuteCfgId(2);
						//业务员
						mailExc.setArgsName(empRule.getEmpName());
						String args = String.valueOf(empRule.getEmpId());
						mailExc.setArgs(args);	
						mailExc.setRuleId(empRule.getId());
						mailExc.setCustId(custContactId);
						excArray.add(mailExc);
						this.saveCotMailExecute(excArray);
						
						//保存 从表-条件 归档到
						List<CotMailExecute> excAy = new ArrayList<CotMailExecute>();
						CotMailExecute mailExcCls = new CotMailExecute();
						mailExcCls.setMethod(Exc.getMethod());
						mailExcCls.setName(Exc.getName());
						mailExcCls.setPackage_(Exc.getPackage_());
						mailExcCls.setClass_(Exc.getClass_());
					
						mailExcCls.setArgsName(cotCustomer.getCustomerShortName());
						mailExcCls.setArgs(String.valueOf(cotCustomer.getId()));
						mailExcCls.setRuleId(empRule.getId());
						mailExcCls.setCustId(custContactId);
						mailExcCls.setExecuteCfgId(3);
						mailExcCls.setType("CUST");
						excAy.add(mailExcCls);
						this.saveCotMailExecute(excAy);
						
						this.saveRuleXmlFile(empRule.getId());
						}	
				 }
				}
			}
			
		}
	}

}
