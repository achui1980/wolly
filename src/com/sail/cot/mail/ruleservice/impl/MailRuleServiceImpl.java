/**
 * 
 */
package com.sail.cot.mail.ruleservice.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.rules.RuleRuntime;
import javax.rules.RuleServiceProvider;
import javax.rules.RuleServiceProviderManager;
import javax.rules.StatelessRuleSession;
import javax.rules.admin.RuleAdministrator;
import javax.rules.admin.RuleExecutionSet;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.iterators.FilterIterator;
import org.apache.commons.lang.RandomStringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import com.jason.core.exception.DAOException;
import com.sail.cot.dao.CotBaseDao;
import com.sail.cot.domain.CotCustContact;
import com.sail.cot.domain.CotCustomer;
import com.sail.cot.domain.CotEmps;
import com.sail.cot.domain.CotMail;
import com.sail.cot.domain.CotMailEmpsRule;
import com.sail.cot.domain.CotMailExecute;
import com.sail.cot.domain.CotMailExecuteCfg;
import com.sail.cot.domain.CotMailRule;
import com.sail.cot.domain.CotMailRuleCfg;
import com.sail.cot.domain.vo.CotCustomerVO;
import com.sail.cot.domain.vo.CotEmpsRuleVO;
import com.sail.cot.domain.vo.CotFactoryVO;
import com.sail.cot.mail.MailExecuteAction;
import com.sail.cot.mail.ruleservice.MailRuleService;
import com.sail.cot.query.QueryInfo;

/**
 * <p>Title: 旗行办公自动化系统（OA）</p>
 * <p>Description:</p>
 * <p>Copyright: Copyright (c) 2008</p>
 * <p>Company: </p>
 * <p>Create Time: Jul 12, 2010 3:51:19 PM </p>
 * <p>Class Name: MailRuleServiceImpl.java </p>
 * @author achui
 *
 */
public class MailRuleServiceImpl implements MailRuleService{

	/* (non-Javadoc)
	 * @see com.sail.cot.mail.ruleservice.MailRuleService#getCotMailEmpsRule(java.lang.Integer)
	 */
	private CotBaseDao baseDao;
	public CotBaseDao getBaseDao() {
		return baseDao;
	}

	public void setBaseDao(CotBaseDao baseDao) {
		this.baseDao = baseDao;
	}

	
//--------------------------------------------从表 邮件规则的相关操作	start


	/* (non-Javadoc)通过ruleId获取从表-邮件规则   
	 * @see com.sail.cot.mail.ruleservice.MailRuleService#getMailRuleById(java.lang.Integer)
	 */
	public List<CotMailRule> getMailRuleById(Integer ruleId) {
		String strSql = " from CotMailRule obj where obj.ruleId = "+ruleId;
		List<CotMailRule> res = this.getBaseDao().find(strSql);
		return res;
	}
	/* (non-Javadoc)通过custId获取从表-邮件规则    
	 * @see com.sail.cot.mail.ruleservice.MailRuleService#getMailRuleById(java.lang.Integer)
	 */
	public List<CotMailRule> getMailRuleByCustId(Integer custId,String type,Integer ruleId) {
		String strSql = " from CotMailRule obj where obj.custId ="+custId +" and obj.type='"+type+"'"+" and obj.ruleId="+ruleId;
		System.out.println(strSql);
		List<CotMailRule> res = this.getBaseDao().find(strSql);
		return res;
	}
	
	
	public CotMailRule getCotMailRuleById(Integer id) {
		CotMailRule cotMailRule = (CotMailRule) this.getBaseDao().getById(CotMailRule.class, id);
		return cotMailRule;
	}
	
	//保存从表规则信息
	public void saveCotMailRule(List<CotMailRule> records){
		this.getBaseDao().saveOrUpdateRecords(records);
	}
	
	// 修改规则信息
	public void modifyCotMailRule(List list) {
		try {
			this.getBaseDao().updateRecords(list);
		} catch (Exception e) {
		}
	}
		
//--------------------------------------------从表 邮件规则的相关操作	end
	
	
	
	
	
//--------------------------------------------从表 规则条件的相关操作    start
	//保存从表规则条件
	public void saveCotMailExecute(List<CotMailExecute> records){
		this.getBaseDao().saveOrUpdateRecords(records);
	}
	
	/* (non-Javadoc) 
	 * @see com.sail.cot.mail.ruleservice.MailRuleService#getMailExecuteById(java.lang.Integer)
	 */
	public List<CotMailExecute> getMailExecuteById(Integer ruleId) {
		// TODO Auto-generated method stub
		String strSql = " from CotMailExecute obj where obj.ruleId = "+ruleId;
		List<CotMailExecute> res = this.getBaseDao().find(strSql);
		return res;
	}
	
	public List<CotMailExecute> getExecuteByFKId(QueryInfo queryInfo) {
		List<CotMailExecute> res = null;
		try {
			res = this.getBaseDao().findRecords(queryInfo);
		} catch (DAOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return res;
	}
	
	/* (non-Javadoc)通过custId获取从表-规则条件    
	 * @see com.sail.cot.mail.ruleservice.MailRuleService#getMailRuleById(java.lang.Integer)
	 */
	public List<CotMailExecute> getMailExecuteByCustId(Integer custId,String type,Integer ruleId) {
		String strSql = " from CotMailExecute obj where obj.custId = "+custId +" and obj.type='"+type+"'" +" and obj.ruleId="+ruleId;
		List<CotMailExecute> res = this.getBaseDao().find(strSql);
		return res;
	}
	
	//===========================删除
	public List<CotMailExecute> getExecuteById(QueryInfo queryInfo) {
		List<CotMailExecute> list = null;
		try {
			 list =this.getBaseDao().findRecords(queryInfo);
		} catch (DAOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
	}
	
	
	public CotMailExecute getCotMailExecuteById(Integer id) {
		CotMailExecute mailExecute = (CotMailExecute) this.getBaseDao().getById(CotMailExecute.class, id);
		return mailExecute;
	}

	/**
     * 根据条件获得规则
     * @return List
     */
	public List getCotMailExecuteList(QueryInfo queryInfo){
		List records = null;
		try {
			records = this.getBaseDao().findRecords(queryInfo);
		} catch (DAOException e) {
			e.printStackTrace();
		}
		return records;
	}
	
	// 从表删除
	public void deleteList(List list, String tabName) {
		try {
			this.getBaseDao().deleteRecordByIds(list, tabName);
		} catch (DAOException e) {
			e.printStackTrace();
		}
	}
	//通过主表ID 查找 从表-条件（FAC）
	public List<CotMailExecute> getCotMailExecuteFACByruleId(Integer ruleId){
		// TODO Auto-generated method stub
		String strSql = " from CotMailExecute obj where obj.type='FAC' and obj.ruleId = "+ruleId;
		List<CotMailExecute> res = this.getBaseDao().find(strSql);
		System.out.println(res.size());
		return res;
	}
	
	
//--------------------------------------------从表 规则条件的相关操作   end
	
	
	
	
//--------------------------------------------主表 邮件规则的相关操作   start

	public CotMailEmpsRule getDefaultCotMailEmpsRule(Integer empId) {
		// TODO Auto-generated method stub
		String sql = " from CotMailEmpsRule obj where obj.ruleDefault = 1 and type='MAIL' and obj.empId="+empId;
		List<CotMailEmpsRule> res = this.getBaseDao().find(sql);
		if(res != null && res.size() > 0)
			return res.get(0);
		return null;
	}
	
	/**
     * 根据条件获得主表邮件规则信息
     * @return List
     */
	public List<CotEmpsRuleVO> getMailEmpsRuleList(QueryInfo queryInfo) {
		
		try {
			List<?> records = this.getBaseDao().findRecords(queryInfo);
			List<CotEmpsRuleVO> list = new ArrayList<CotEmpsRuleVO>();
			for (int i = 0; i < records.size(); i++){
				Object[] obj = (Object[]) records.get(i);
				CotEmpsRuleVO cotEmpsRuleVO = new CotEmpsRuleVO();
				if (obj[0] != null) {
					cotEmpsRuleVO.setId((Integer) obj[0]);
				}
				if (obj[1] != null) {
					cotEmpsRuleVO.setEmpId((Integer) obj[1]);
				}
				if (obj[2] != null) {
					cotEmpsRuleVO.setRelate(obj[2].toString());
				}
				if (obj[3] != null) {
					cotEmpsRuleVO.setRuleDefault((Integer) obj[3]);
				}
				if (obj[4] != null) {
					cotEmpsRuleVO.setRuleName(obj[4].toString());
				}
				if (obj[5] != null) {
					cotEmpsRuleVO.setRuleDesc(obj[5].toString());
				}
				if (obj[6] != null) {
					cotEmpsRuleVO.setXmlPath(obj[6].toString());
				}
//				if (obj[7] != null) {
//					cotEmpsRuleVO.setType(obj[7].toString());
//				}
				list.add(cotEmpsRuleVO);
			}
			return list;
		} catch (DAOException e) {
			e.printStackTrace();
		}
		return null;	
	}
	

	/**
     * 根据ID获得主表邮件规则信息
     * @return List
     */
	public CotMailEmpsRule getMailEmpsRuleById(Integer ruleId) {
		// TODO Auto-generated method stub
		CotMailEmpsRule rule = null;
		String strSql = " from CotMailEmpsRule obj where obj.id = "+ruleId;
		List<CotMailEmpsRule> res = this.getBaseDao().find(strSql);
		if(res.size()!=0){
			 rule =res.get(0);
		}
		return rule;
	}
	
	//获得记录数
	public Integer getRecordCount(QueryInfo queryInfo) {
		try {
			return this.getBaseDao().getRecordsCount(queryInfo);
		} catch (DAOException e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	//通过业务员ID查询客户
	public List<CotCustomer> getCotCustomerById(Integer empId) {
		String strSql = " from CotCustomer obj where obj.empId = "+ empId;
		List<CotCustomer> res = this.getBaseDao().find(strSql);
		return res;
	}
	
	//通过客户ID查找相对应的联系人
	public List<CotCustContact> getCotCustContactByCustId(Integer custId) {
		String strSql = " from CotCustContact obj where obj.customerId = "+ custId + " and obj.contactEmail is not null and obj.contactEmail <> ''";
		List<CotCustContact> res = this.getBaseDao().find(strSql);
		return res;
	}
	
	
	//查找所有业务员
	public List<CotEmps> getAllCotEmps() {
		String strSql = " from CotEmps";
		List<CotEmps> res = this.getBaseDao().find(strSql);
		return res;
	}
	//查找部分业务员
	public List<CotEmps> getSomeCotEmps(String str) {
		String strSql = " from CotEmps obj where obj.id not in("+str+")";
		List<CotEmps> res = this.getBaseDao().find(strSql);
		return res;
	}
	
	//查找所有 主表-规则
	public List<CotMailEmpsRule> getAllCotMailEmpsRule(){
		String strSql = " from CotMailEmpsRule";
		List<CotMailEmpsRule> res = this.getBaseDao().find(strSql);
		return res;
	}
	
	//切换邮件规则
	public void switchRule(Integer id){
		CotMailEmpsRule cotMailEmpsRule=this.getMailEmpsRuleById(id);
		int index=cotMailEmpsRule.getRuleDefault();
		if(index==0){
			cotMailEmpsRule.setRuleDefault(1);
		}else{
			cotMailEmpsRule.setRuleDefault(0);
		}
		this.saveOrUpdateMailEmpsRule(cotMailEmpsRule);
	}
	
	
	public void saveRule(CotEmps cotEmp,Integer ruleDf,CotMailExecuteCfg regExc,CotMailExecuteCfg Exc,CotMailRuleCfg cotMailRuleCfg){
		//保存 主表-规则
		List<CotMailEmpsRule> list = new ArrayList<CotMailEmpsRule>();
		CotMailEmpsRule mailEmpsRule =new CotMailEmpsRule();
		mailEmpsRule.setEmpId(cotEmp.getId());
		if(ruleDf==1){
			//按个人规则执行   默认1 
			mailEmpsRule.setRuleDefault(1);
		}else{
			//按公共配置执行   
			mailEmpsRule.setRuleDefault(0);
		}
		mailEmpsRule.setEmpName(cotEmp.getEmpsName());
		String name =cotEmp.getEmpsName()+"的邮件规则";
		mailEmpsRule.setRuleDesc(name);
		mailEmpsRule.setRuleName(name);
		mailEmpsRule.setType("MAIL");
		list.add(mailEmpsRule);
		this.getBaseDao().saveOrUpdateRecords(list);
		//得到 主表-规则 ID
		int empRuleId =mailEmpsRule.getId();
		
		//通过业务员ID查询客户
		int empId =cotEmp.getId();
		List<CotCustomer> custList=this.getCotCustomerById(empId);
		
		if(custList.size()>0){
			for(int x =0;x<custList.size();x++){
				CotCustomer cust =custList.get(x);
				int custId =cust.getId();
				//通过客户ID查找相对应的联系人
				List<CotCustContact> custContactList =this.getCotCustContactByCustId(custId);
			if(custContactList.size()>0){
				for(int op =0;op<custContactList.size();op++){
					CotCustContact custContact =custContactList.get(op);
					//保存 从表-规则
					List<CotMailRule> ls = new ArrayList<CotMailRule>();
					CotMailRule mailRule= new CotMailRule();
					mailRule.setLeftTerm(cotMailRuleCfg.getProperty());
					mailRule.setOp("contains");
					mailRule.setRelate("AND");
					mailRule.setRuleCfgId(1);
					mailRule.setRuleId(empRuleId);
					mailRule.setCustId(custContact.getId());
					mailRule.setRightTerm(custContact.getContactEmail().toLowerCase());
					mailRule.setType("CUST");
					ls.add(mailRule);
					this.saveCotMailRule(ls);
					
					//保存 从表-条件 指派给
					List<CotMailExecute> excArray = new ArrayList<CotMailExecute>();
					CotMailExecute mailExc = new CotMailExecute();
					
					mailExc.setMethod(regExc.getMethod());
					mailExc.setName(regExc.getName());
					mailExc.setType("CUST");
					mailExc.setPackage_(regExc.getPackage_());
					mailExc.setClass_(regExc.getClass_());
					mailExc.setExecuteCfgId(2);
					
					mailExc.setArgsName(cotEmp.getEmpsName());
					String args = String.valueOf(cotEmp.getId());
					mailExc.setArgs(args);	
					mailExc.setRuleId(empRuleId);
					mailExc.setCustId(custContact.getId());
					
					excArray.add(mailExc);
					this.saveCotMailExecute(excArray);

					//保存 从表-条件 归档到
					List<CotMailExecute> excAy = new ArrayList<CotMailExecute>();
					CotMailExecute mailExcCls = new CotMailExecute();
					mailExcCls.setMethod(Exc.getMethod());
					mailExcCls.setName(Exc.getName());
					mailExcCls.setPackage_(Exc.getPackage_());
					mailExcCls.setClass_(Exc.getClass_());
				
					mailExcCls.setArgsName(cust.getCustomerShortName());
					mailExcCls.setArgs(String.valueOf(cust.getId()));
					mailExcCls.setRuleId(empRuleId);
					mailExcCls.setCustId(custContact.getId());
					mailExcCls.setExecuteCfgId(3);
					mailExcCls.setType("CUST");
					excAy.add(mailExcCls);
					this.saveCotMailExecute(excAy);
				}
				this.saveRuleXmlFile(empRuleId);
				
			}
			}
		}
		
	}
	
	//主表删除 但不删除对应的XML文件
	public Boolean deleteEmpsRuleAll(List<Integer> ruleIds,Integer ruleDf){
		CotMailExecuteCfg regExc=this.getCotMailExecuteCfgById(2);
		CotMailExecuteCfg Exc=this.getCotMailExecuteCfgById(3);
		CotMailRuleCfg cotMailRuleCfg=this.getCotMailRuleCfgById(1);
		//找到所有业务员
		List<CotEmps> empsList =this.getAllCotEmps();
		if(ruleIds==null){
			for(int i =0;i <empsList.size();i++){
				CotEmps cotEmp=empsList.get(i);
				this.saveRule(cotEmp, ruleDf, regExc, Exc, cotMailRuleCfg);
			}
		}else{
			//找到部分业务员
			StringBuffer sf=new StringBuffer();
			//从主表里找到原有的规则对象
			List<CotMailEmpsRule> oldRuleList = this.getAllCotMailEmpsRule();
			
			for(int y=0;y<oldRuleList.size();y++){
				CotMailEmpsRule mailrule =oldRuleList.get(y);
				sf.append(mailrule.getEmpId()).append(",");
			}
			int end =sf.toString().lastIndexOf(",");
			String str=sf.toString().substring(0, end);
			System.out.println(str);
			if(empsList.size()!=oldRuleList.size()){
				List<CotEmps> empsSome =this.getSomeCotEmps(str);
				for(int zx=0;zx<empsSome.size();zx++){
					CotEmps cotEmp =empsSome.get(zx);
					this.saveRule(cotEmp, ruleDf, regExc, Exc, cotMailRuleCfg);
				}
			}
		}
		
		if(ruleIds !=null){
			//原有的规则
			//从主表里找到原有的规则对象
			List<CotMailEmpsRule> oldRuleList = this.getAllCotMailEmpsRule();
			for(int i=0;i<oldRuleList.size();i++){
				CotMailEmpsRule cotMailEmpsRule =oldRuleList.get(i);
				int empRuleId=cotMailEmpsRule.getId();
			    //更新主表
				List<CotMailEmpsRule> updateRule =new ArrayList<CotMailEmpsRule>();
				cotMailEmpsRule.setEmpId(cotMailEmpsRule.getEmpId());
				if(ruleDf==1){
					//按个人规则执行   默认1 
					cotMailEmpsRule.setRuleDefault(1);
				}else{
					//按公共配置执行   
					cotMailEmpsRule.setRuleDefault(0);
				}
				cotMailEmpsRule.setEmpName(cotMailEmpsRule.getEmpName());
				String name =cotMailEmpsRule.getEmpName()+"的邮件规则";
				System.out.println(cotMailEmpsRule.getRuleDefault());
				cotMailEmpsRule.setRuleDesc(name);
				cotMailEmpsRule.setRuleName(name);
				cotMailEmpsRule.setType("MAIL");
				updateRule.add(cotMailEmpsRule);
				this.getBaseDao().saveOrUpdateRecords(updateRule);
				
				//先通过ruleId到从表-规则查询全部规则记录
				List<CotMailRule> res =this.getMailRuleById(empRuleId);
				//先通过ruleId到从表-条件查询全部规则记录
				List<CotMailExecute> excList = this.getMailExecuteById(empRuleId);
				
				//通过业务员ID找到相应的客户联系人
				//先找到客户对象
				List<CotCustomer> list =this.getCustByEmpId(cotMailEmpsRule.getEmpId());
				
				//联系人对象
				//===========================以客户对象做为基点
				for(CotCustomer cust : list){
					//在找到客户联系人对象
					List<CotCustContact> arr =this.getCotCustContactByCustId(cust.getId());
					
					//=====================处理从表-规则
					Map map =new HashMap();
					for(CotCustContact cotCustContact : arr){
						map.put(cotCustContact.getId(), cotCustContact.getContactEmail());
					}
					MyPrediacateRule prediacateRule=new MyPrediacateRule();
					prediacateRule.property ="ruleCfgId";
					prediacateRule.compareId="1";
					
					try {
						//过滤出符合条件的规则对象
						List<CotMailRule> ruleList = this.getConfigedList(res,map,prediacateRule);
						for(CotMailRule cotMailRule : ruleList){
							//循环出联系人跟规则对象对比
							for(CotCustContact contact : arr){
								if(cotMailRule.getCustId().intValue()==contact.getId().intValue()){
									System.out.println(cotMailRule.getCustId());
									List<CotMailRule> ruleArray = new ArrayList<CotMailRule>();
									cotMailRule.setLeftTerm(cotMailRuleCfg.getProperty());
									cotMailRule.setOp("contains");
									cotMailRule.setRelate("AND");
									cotMailRule.setRuleCfgId(1);
									cotMailRule.setRuleId(empRuleId);
									cotMailRule.setCustId(contact.getId());
									cotMailRule.setRightTerm(contact.getContactEmail().toLowerCase());
									cotMailRule.setType("CUST");
									ruleArray.add(cotMailRule);
									this.saveCotMailRule(ruleArray);
								}
							}
						}
						//不符合
						if(map.size()>0){
							Iterator it =map.keySet().iterator();
							while(it.hasNext()){
								int key =(Integer) it.next();
								String em =(String) map.get(key);
								List<CotMailRule> ls = new ArrayList<CotMailRule>();
								CotMailRule mailRule= new CotMailRule();
								mailRule.setLeftTerm(cotMailRuleCfg.getProperty());
								mailRule.setOp("contains");
								mailRule.setRelate("AND");
								mailRule.setRuleCfgId(1);
								mailRule.setRuleId(empRuleId);
								System.out.println(empRuleId);
								mailRule.setCustId(key);
								mailRule.setRightTerm(em.toLowerCase());
								mailRule.setType("CUST");
								ls.add(mailRule);
								this.saveCotMailRule(ls);
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
					//=====================处理从表-条件  指派
					Map mapcombExc =new HashMap();	
					for(CotCustContact cotCustContact : arr){
						mapcombExc.put(cotCustContact.getId(), cotCustContact.getContactEmail());
					}
					MyPrediacateRule prediacateExc=new MyPrediacateRule();
					prediacateExc.property ="executeCfgId";
					prediacateExc.compareId="2";
					
					//过滤出符合条件的规则对象
					try {
						List<CotMailExecute> combExcArr = this.getConfigedList(excList,mapcombExc,prediacateExc);
						for(CotMailExecute cotMailExecute : combExcArr){
							for(CotCustContact contact : arr){
								if(cotMailExecute.getCustId().intValue()==contact.getId().intValue()){
									List<CotMailExecute> combExtList=new ArrayList<CotMailExecute>();
									cotMailExecute.setMethod(regExc.getMethod());
									cotMailExecute.setName(regExc.getName());
									cotMailExecute.setArgsName(cotMailEmpsRule.getEmpName());
									cotMailExecute.setArgs(String.valueOf(cotMailEmpsRule.getEmpId()));	
									cotMailExecute.setRuleId(empRuleId);
									cotMailExecute.setPackage_(regExc.getPackage_());
									cotMailExecute.setClass_(regExc.getClass_());
									cotMailExecute.setCustId(cotMailExecute.getCustId());
									cotMailExecute.setExecuteCfgId(2);
									combExtList.add(cotMailExecute);
									this.saveCotMailExecute(combExtList);
								}
							}
						}
						//不符合
						if(mapcombExc.size() > 0){
							Iterator it =mapcombExc.keySet().iterator();
							while(it.hasNext()){
								int key =(Integer) it.next();
								List<CotMailExecute> excArray = new ArrayList<CotMailExecute>();
								CotMailExecute mailExc = new CotMailExecute();
								mailExc.setMethod(regExc.getMethod());
								mailExc.setName(regExc.getName());
								mailExc.setArgsName(cotMailEmpsRule.getEmpName());
								String args = String.valueOf(cotMailEmpsRule.getEmpId());
								mailExc.setArgs(args);	
								mailExc.setRuleId(empRuleId);
								mailExc.setPackage_(regExc.getPackage_());
								mailExc.setClass_(regExc.getClass_());
								mailExc.setCustId(key);
								mailExc.setExecuteCfgId(2);
								mailExc.setType("CUST");
								excArray.add(mailExc);
								this.saveCotMailExecute(excArray);
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
					
					//==============================处理从表-条件  归档
					Map mapExcCl=new HashMap();
					for(CotCustContact cotCustContact : arr){
						mapExcCl.put(cotCustContact.getId(), cotCustContact.getContactEmail());
					}
					
					MyPrediacateRule predCl=new MyPrediacateRule();
					predCl.property ="executeCfgId";
					predCl.compareId="3";
					
					try {
						//符合过滤   归档
						List<CotMailExecute> excClsArr = this.getConfigedList(excList,mapExcCl,predCl);
						for(CotMailExecute mailClassify :excClsArr){
							for(CotCustContact contact : arr){
								if(mailClassify.getCustId().intValue()==contact.getId().intValue()){
									List<CotMailExecute> excClassify=new ArrayList<CotMailExecute>();
									mailClassify.setMethod(Exc.getMethod());
									mailClassify.setName(Exc.getName());
									mailClassify.setRuleId(empRuleId);
									mailClassify.setPackage_(Exc.getPackage_());
									mailClassify.setClass_(Exc.getClass_());
									
									mailClassify.setArgsName(cust.getCustomerShortName());
									mailClassify.setArgs(String.valueOf(contact.getCustomerId())); 
									
									mailClassify.setCustId(contact.getId());
									mailClassify.setType("CUST");
									mailClassify.setExecuteCfgId(3);
									excClassify.add(mailClassify);
									this.saveCotMailExecute(excClassify);
								}
							}
						}
						//不符合
						if(mapExcCl.size() >0){
							Iterator itCl =mapExcCl.keySet().iterator();
							while(itCl.hasNext()){
								int key =(Integer) itCl.next();
								List<CotMailExecute> excAy = new ArrayList<CotMailExecute>();
								CotMailExecute mailExcCls = new CotMailExecute();
								mailExcCls.setMethod(Exc.getMethod());
								mailExcCls.setName(Exc.getName());
								mailExcCls.setArgsName(cust.getCustomerShortName());
								//客户ID
								mailExcCls.setArgs(String.valueOf(cust.getId()));
								mailExcCls.setRuleId(empRuleId);
								mailExcCls.setPackage_(Exc.getPackage_());
								mailExcCls.setClass_(Exc.getClass_());
								mailExcCls.setCustId(key);
								mailExcCls.setExecuteCfgId(3);
								mailExcCls.setType("CUST");
								excAy.add(mailExcCls);
								this.saveCotMailExecute(excAy);
								
							}
						 }
						} catch (Exception e) {
							e.printStackTrace();
						}
				}
				//更新规则文件
				if(cotMailEmpsRule.getXmlPath()!=null){
					this.saveRuleXmlFile(empRuleId);
				}
			}
			
			try {//更新公共配置文件
				this.createPublicMailRuleFile();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return true;
	}
	
	//主表删除 且删除对应的XML文件
	public Boolean deleteEmpsRuleList(List<?> ruleIds){
		try {
			
			for(int i=0;i<ruleIds.size();i++){
				System.out.println(ruleIds.size());
				int id =(Integer)ruleIds.get(i);
				CotMailEmpsRule mailEmpsRule =this.getMailEmpsRuleById(id);
				if(mailEmpsRule.getXmlPath() !=null){
					String path =mailEmpsRule.getXmlPath();
					int st =path.indexOf("\\");
					String filePath =path.substring(st+1);
					String xpath = this.getClass().getResource("/mailrules").getPath();
					String realPath =xpath + filePath;
					File file =new File(realPath);
					if(file !=null){
						file.delete();
					}	
				}
				
			}
			this.getBaseDao().deleteRecordByIds(ruleIds, "CotMailEmpsRule");
			try {
				//重新创建公共配置文件
				this.createPublicMailRuleFile();
			} catch (Exception e) {
				e.printStackTrace();
			}
		} catch (DAOException e) {
			e.printStackTrace();
		}
		return true;
	}
    //主表保存
	public Integer saveOrUpdateMailEmpsRule(CotMailEmpsRule mailEmpsRule){
		if(mailEmpsRule!=null){
			List<CotMailEmpsRule> list = new ArrayList<CotMailEmpsRule>();
			list.add(mailEmpsRule);
			this.getBaseDao().saveOrUpdateRecords(list);
		}
		return mailEmpsRule.getId();
		
	}

//--------------------------------------------主表 邮件规则的相关操作   end


	/* (non-Javadoc)
	 * @see com.sail.cot.mail.ruleservice.MailRuleService#saveRuleXmlFile()
	 */
	public String saveRuleXmlFile(Integer ruleId) {
		// TODO Auto-generated method stub
		CotMailEmpsRule mailEmpsRule = (CotMailEmpsRule) this.getBaseDao().getById(CotMailEmpsRule.class, ruleId);
		List<CotMailRule> ruleList = this.getMailRuleById(ruleId);
		List<CotMailExecute> executeList = this.getMailExecuteById(ruleId);
		Document doc = this.createRuleXml(mailEmpsRule, ruleList, executeList);
		OutputFormat xmlFormat = new OutputFormat("\t",true);
		xmlFormat.setEncoding("utf-8");
		String path = this.getClass().getResource("/mailrules").getPath();
		System.out.println(path);
		try {
			//url解码，避免中文路径乱码
			path  = URLDecoder.decode(path,"UTF-8");
			File outputFile = new File(path+File.separator+"Rule_EMPID_"+mailEmpsRule.getEmpId()+".xml");
			XMLWriter xmlWriter = new XMLWriter(new FileOutputStream(outputFile),xmlFormat);
			xmlWriter.write(doc);
			xmlWriter.close();
			mailEmpsRule.setXmlPath("mailrules"+File.separator+"Rule_EMPID_"+mailEmpsRule.getEmpId()+".xml");
			List arrList = new ArrayList();
			//更新路径
			arrList.add(mailEmpsRule);
			this.getBaseDao().updateRecords(arrList);
			return "mailrules"+File.separator+"Rule_EMPID_"+mailEmpsRule.getEmpId()+".xml";
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return null;
	}
	private Document createDefRuleXml(CotMailEmpsRule empRule){
//		Document doc = DocumentHelper.createDocument();
//		//生成根节点
//		Element root = doc.addElement("rule-execution-set");
//		//添加name节点
//		root.addElement("name").setText("Mail Rule For "+empRule.getEmpName());
//		//添加description节点
//		root.addElement("description").setText("Mail RuleSet For "+empRule.getEmpName());
		return null;
	}
	private Document createRuleXml(CotMailEmpsRule empRule,List<CotMailRule> ruleList,List<CotMailExecute> executeList) {
		if(ruleList == null || ruleList.size() < 1) return null;
		if(executeList == null || executeList.size() < 1) return null;
		Map<String,String> map = new HashMap<String,String>();
		Map<Integer, String> custMap = new HashMap<Integer, String>();
		Map<Integer, String> facMap = new HashMap<Integer, String>();
		
		//命名规则：配置项+规则ID
		for(CotMailExecute execute : executeList){
			//过滤实例化对象的类名
			map.put(execute.getClass_()+"_"+execute.getRuleId(), execute.getPackage_());
		}
		for(CotMailRule mailrule : ruleList){
			if(mailrule.getType().equals("CUST"))
				custMap.put(mailrule.getCustId(), mailrule.getRelate());
			else
				facMap.put(mailrule.getCustId(), mailrule.getRelate());
		}
		Document doc = DocumentHelper.createDocument();
		//生成根节点
		Element root = doc.addElement("rule-execution-set");
		//添加name节点
		root.addElement("name").setText("Mail Rule For "+empRule.getEmpName());
		//添加description节点
		root.addElement("description").setText("Mail RuleSet For "+empRule.getEmpName());
		//添加synonymn节点
		Iterator<String> iterator = map.keySet().iterator();
		while(iterator.hasNext()){
			String class_ = iterator.next();
			String package_ = map.get(class_);
			Element synonymn = root.addElement("synonymn");
			synonymn.addAttribute("name", class_);
			synonymn.addAttribute("class", package_);
		}
		//生成客户联系人规则
		Iterator<Integer> custIterator = custMap.keySet().iterator();
		while(custIterator.hasNext()){
			Element rule = root.addElement("rule");
			String random = RandomStringUtils.randomAlphabetic(10);
			rule.addAttribute("name", "Rule_CUST_"+random.toUpperCase());
			
			Integer custId = custIterator.next();
			MyPrediacate myPrediacate = new MyPrediacate();
			//设置list的过滤条件
			myPrediacate.custId = custId;
			myPrediacate.type = "CUST";
			String relate =  custMap.get(custId);
			Iterator iterator_obj = new FilterIterator(ruleList.iterator(),myPrediacate);
			if("AND".equals(relate)){
				//生成规则
				while (iterator_obj.hasNext()) {
					CotMailRule mailrule = (CotMailRule) iterator_obj.next();
					Element ifElement = rule.addElement("if");
					ifElement.addAttribute("leftTerm", mailrule.getLeftTerm());
					ifElement.addAttribute("op", mailrule.getOp());
					ifElement.addAttribute("rightTerm", mailrule.getRightTerm());
				}
				//生成执行条件
				iterator_obj = new FilterIterator(executeList.iterator(),myPrediacate);
				while (iterator_obj.hasNext()) {
					CotMailExecute execute  = (CotMailExecute) iterator_obj.next();
					Element thenEmElement = rule.addElement("then");
					thenEmElement.addAttribute("method", execute.getClass_()+"_"+execute.getRuleId()+"."+execute.getMethod());
					if(execute.getArgs() == null || "".equals(execute.getArgs())) continue;
					String[] args = execute.getArgs().split(";");
					for(int j=0; j<=args.length-1; j++){
						thenEmElement.addAttribute("arg"+(j+1), args[j]);
					}
				}
			}else if("OR".equals(relate)){
				//生成规则
				while (iterator_obj.hasNext()) {
					CotMailRule mailrule = (CotMailRule) iterator_obj.next();
					Element ifElement = rule.addElement("if");
					ifElement.addAttribute("leftTerm", mailrule.getLeftTerm());
					ifElement.addAttribute("op", mailrule.getOp());
					ifElement.addAttribute("rightTerm", mailrule.getRightTerm());
					//生成执行条件
					Iterator iterator_obj_or = new FilterIterator(executeList.iterator(),myPrediacate);
					while (iterator_obj_or.hasNext()) {
						CotMailExecute execute  = (CotMailExecute) iterator_obj_or.next();
						Element thenEmElement = rule.addElement("then");
						thenEmElement.addAttribute("method", execute.getClass_()+"_"+execute.getRuleId()+"."+execute.getMethod());
						if(execute.getArgs() == null || "".equals(execute.getArgs())) continue;
						String[] args = execute.getArgs().split(";");
						for(int j=0; j<=args.length-1; j++){
							thenEmElement.addAttribute("arg"+(j+1), args[j]);
						}
					}
				}
			}
		}
		//规则间用或表示
		//生成厂家联系人规则
		Iterator<Integer> facIterator = facMap.keySet().iterator();
		while(facIterator.hasNext()){
			Element rule = root.addElement("rule");
			String random = RandomStringUtils.randomAlphabetic(10);
			rule.addAttribute("name", "Rule_FAC_"+random.toUpperCase());
			
			Integer facId = facIterator.next();
			MyPrediacate myPrediacate = new MyPrediacate();
			//设置list的过滤条件
			myPrediacate.custId = facId;
			myPrediacate.type = "FAC";
			String relate =  facMap.get(facId);
			Iterator iterator_obj = new FilterIterator(ruleList.iterator(),myPrediacate);
			if("AND".equals(relate)){
				//生成规则
				while (iterator_obj.hasNext()) {
					CotMailRule mailrule = (CotMailRule) iterator_obj.next();
					Element ifElement = rule.addElement("if");
					ifElement.addAttribute("leftTerm", mailrule.getLeftTerm());
					ifElement.addAttribute("op", mailrule.getOp());
					ifElement.addAttribute("rightTerm", mailrule.getRightTerm());
				}
				//生成执行条件
				iterator_obj = new FilterIterator(executeList.iterator(),myPrediacate);
				while (iterator_obj.hasNext()) {
					CotMailExecute execute  = (CotMailExecute) iterator_obj.next();
					Element thenEmElement = rule.addElement("then");
					thenEmElement.addAttribute("method", execute.getClass_()+"_"+execute.getRuleId()+"."+execute.getMethod());
					if(execute.getArgs() == null || "".equals(execute.getArgs())) continue;
					String[] args = execute.getArgs().split(";");
					for(int j=0; j<=args.length-1; j++){
						thenEmElement.addAttribute("arg"+(j+1), args[j]);
					}
				}
			}else if("OR".equals(relate)){
				//生成规则
				while (iterator_obj.hasNext()) {
					CotMailRule mailrule = (CotMailRule) iterator_obj.next();
					Element ifElement = rule.addElement("if");
					ifElement.addAttribute("leftTerm", mailrule.getLeftTerm());
					ifElement.addAttribute("op", mailrule.getOp());
					ifElement.addAttribute("rightTerm", mailrule.getRightTerm());
					//生成执行条件
					Iterator iterator_obj_or = new FilterIterator(executeList.iterator(),myPrediacate);
					while (iterator_obj_or.hasNext()) {
						CotMailExecute execute  = (CotMailExecute) iterator_obj_or.next();
						Element thenEmElement = rule.addElement("then");
						thenEmElement.addAttribute("method", execute.getClass_()+"_"+execute.getRuleId()+"."+execute.getMethod());
						if(execute.getArgs() == null || "".equals(execute.getArgs())) continue;
						String[] args = execute.getArgs().split(";");
						for(int j=0; j<=args.length-1; j++){
							thenEmElement.addAttribute("arg"+(j+1), args[j]);
						}
					}
				}
			}
		}
		return doc;
		
	}
	//生成规则节点
	private Element createRuleElement(Element root,List<CotMailRule> ruleList,Integer custId){
		Element rule = root.addElement("rule");
		String random = RandomStringUtils.randomAlphabetic(10);
		rule.addAttribute("name", "Rule_"+random.toUpperCase());
		MyPrediacate myPrediacate = new MyPrediacate();
		//设置list的过滤条件
		myPrediacate.custId = custId;
		Iterator iterator_obj = new FilterIterator(ruleList.iterator(),myPrediacate);
		//生成规则
		while (iterator_obj.hasNext()) {
			CotMailRule mailrule = (CotMailRule) iterator_obj.next();
			Element ifElement = rule.addElement("if");
			ifElement.addAttribute("leftTerm", mailrule.getLeftTerm());
			ifElement.addAttribute("op", mailrule.getOp());
			ifElement.addAttribute("rightTerm", mailrule.getRightTerm());
		}
		return rule;
	}
	//生成执节点
	private void createExecuteElement(Element rule,List<CotMailExecute> executeList,Integer custId){
		MyPrediacate myPrediacate = new MyPrediacate();
		//设置list的过滤条件
		myPrediacate.custId = custId;
		//生成执行条件
		Iterator iterator_obj = new FilterIterator(executeList.iterator(),myPrediacate);
		while (iterator_obj.hasNext()) {
			CotMailExecute execute  = (CotMailExecute) iterator_obj.next();
			Element thenEmElement = rule.addElement("then");
			thenEmElement.addAttribute("method", execute.getClass_()+"_"+execute.getRuleId()+"."+execute.getMethod());
			if(execute.getArgs() == null || "".equals(execute.getArgs())) continue;
			String[] args = execute.getArgs().split(";");
			for(int j=0; j<=args.length-1; j++){
				thenEmElement.addAttribute("arg"+(j+1), args[j]);
			}
		}
	}
	/* (non-Javadoc)
	 * @see com.sail.cot.mail.ruleservice.MailRuleService#getPublicMailRule()
	 */
	public void createPublicMailRuleFile() throws Exception {
		// TODO Auto-generated method stub
		List<CotMailEmpsRule> list = this.getBaseDao().find(" from CotMailEmpsRule obj");
		Document document = DocumentHelper.createDocument();
		Element root = document.addElement("rule-execution-set");
		//添加name节点
		root.addElement("name").setText("Mail Rule For Public");
		//添加description节点
		root.addElement("description").setText("Mail RuleSet For Public");
		String path = this.getClass().getResource("/").getPath();
		path  = URLDecoder.decode(path,"UTF-8");
		for (CotMailEmpsRule cotMailEmpsRule : list) {
			if(cotMailEmpsRule.getXmlPath() == null) continue;
			String xmlpath = path + cotMailEmpsRule.getXmlPath();
			File xmlFile = new File(xmlpath);
			SAXReader reader = new SAXReader();
			File ruleFile = new File(xmlpath);
			if(!ruleFile.exists()) continue;
			Document doc = reader.read(xmlFile);
			Element rootElement = doc.getRootElement();
//			List<Node> list2 = doc.selectNodes("//rule-execution-set//synonymn");
			int index = root.indexOf(root.selectSingleNode("description"));
			for(Iterator<Element> iterator = rootElement.elementIterator("synonymn"); iterator.hasNext();){
				Element synonymn = iterator.next().createCopy();
				root.elements().add(index+1,synonymn);
			}
			
			for(Iterator<Element> iterator = rootElement.elementIterator("rule"); iterator.hasNext();){
				Element rule = iterator.next().createCopy();
				root.add(rule);
			}
			
		}
		OutputFormat xmlFormat = new OutputFormat("\t",true);
		xmlFormat.setEncoding("utf-8");
		File outputFile = new File(path+File.separator+"mailrules/Rule_PUBLIC_.xml");
		XMLWriter xmlWriter = new XMLWriter(new FileOutputStream(outputFile),xmlFormat);
		xmlWriter.write(document);
		xmlWriter.close();
	}

	


	/* (non-Javadoc)
	 * @see com.sail.cot.mail.ruleservice.MailRuleService#getRuleResult(java.lang.Object)
	 */

	public List<MailExecuteAction> getRuleResult(CotMail cotMail,String xmlPath) {
		try {
			Class.forName( "org.jruleengine.RuleServiceProviderImpl" );
			RuleServiceProvider serviceProvider = RuleServiceProviderManager.getRuleServiceProvider( "org.jruleengine" );
			RuleAdministrator ruleAdministrator = serviceProvider.getRuleAdministrator();
			InputStream inStream = new FileInputStream(xmlPath);
			RuleExecutionSet res1 = ruleAdministrator.getLocalRuleExecutionSetProvider( null ).createRuleExecutionSet( inStream, null );
	        inStream.close();
	        String uri = res1.getName();
            ruleAdministrator.registerRuleExecutionSet(uri, res1, null );
            RuleRuntime ruleRuntime = serviceProvider.getRuleRuntime();
            StatelessRuleSession statelessRuleSession =
                (StatelessRuleSession) ruleRuntime.createRuleSession(uri,
                new HashMap(), RuleRuntime.STATELESS_SESSION_TYPE);
            
            List input = new ArrayList();
            input.add(cotMail);
            List results = statelessRuleSession.executeRules(input);
            
            Iterator itr = results.iterator();
            List<MailExecuteAction> resList = new ArrayList<MailExecuteAction>();
            while(itr.hasNext()) {
                 Object obj = itr.next();
                 if(obj instanceof MailExecuteAction){
                	 MailExecuteAction action = (MailExecuteAction)obj;
                	 action.setMailObject(cotMail);
                	 System.out.println("邮件指派动作，设置员工ID为："+action.getEmpId());
                	 resList.add(action);
                 }
            }
            return resList;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
		 
	}

	//查询客户
	public List<CotCustomer> getCusMailList(QueryInfo queryInfo){
		List<CotCustomer> customerList = new ArrayList<CotCustomer>();
		try {
			List<?> list = this.getBaseDao().findRecords(queryInfo);
			for (int i = 0; i < list.size(); i++) {
				Object[] objs = (Object[]) list.get(i);
				CotCustomer customer = new CotCustomer();
				customer.setId((Integer) objs[0]);
				customer.setCustomerShortName((String) objs[1]); 
				customer.setCustomerEmail((String) objs[2]);
				customer.setEmpId((Integer) objs[3]);
				customerList.add(customer);
			}
			return customerList;
		} catch (DAOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	//查询客户和联系人
	public List<CotCustomerVO> getCusMailAndContact(QueryInfo queryInfo){
		List<CotCustomerVO> customerList = new ArrayList<CotCustomerVO>();
		try {
			List<?> list = this.getBaseDao().findRecords(queryInfo);
			for (int i = 0; i < list.size(); i++) {
				Object[] objs = (Object[]) list.get(i);
				CotCustomerVO customer = new CotCustomerVO();
				customer.setId((Integer) objs[0]);
				customer.setCustomerShortName((String) objs[1]); 
				customer.setCustomerEmail((String) objs[2]);
				customer.setEmpId((Integer) objs[3]);
				customer.setContactPerson((String) objs[4]);
				customer.setCustId((Integer) objs[5]);
				customer.setType("CUST");
				customerList.add(customer);
			}
			return customerList;
		} catch (DAOException e) {
			return null;
		}
	}
	
	
	
	//条件筛细类，用于对list的对象按照某个关键之进行分类
	class MyPrediacate implements Predicate{
		public Integer custId;
		public String type;
		public boolean evaluate(Object obj) {
			;
			try {
				String value1 = BeanUtils.getProperty(obj,"custId");
				String value2 = BeanUtils.getProperty(obj,"type");
				return (value1.equals(custId.toString())&& value2.equals(type));
			}catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
			return false;
		}
		
	}

	//条件筛细类，用于对list的对象按照某个关键之进行分类
	class MyPrediacateRule implements Predicate{
		public String property;//需要获取的属性
		public String compareId;//需要比较的值
		public boolean evaluate(Object obj) {
			String value;
			try {
				value = BeanUtils.getProperty(obj,property);
				return value.equals(compareId);
			}catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
			return false;
		}
	}
	/**
	 * 描述：过滤出含有“发件人含有”规则的记录，
	 * @param ruleList 规则列表
	 * @param custIds  客户ID映射表， key：客户ID，value：客户邮件
	 * @return custIds,最后返回没有配置该规则的客户映射
	 * 返回值：List<CotMailRule>
	 * @throws Exception 
	 */
	private List getConfigedList( List list,Map custIds,MyPrediacateRule prediacateRule) throws Exception{
		Iterator iterator = new FilterIterator(list.iterator(),prediacateRule);
		List rules = new ArrayList();
		while(iterator.hasNext()){
			Object obj = iterator.next();
			rules.add(obj);
			String value = BeanUtils.getProperty(obj, "custId");
			
			custIds.remove(new Integer(value));
		}
		return rules;
	}
	//初始化
	public String initRules(Integer empId,Integer empRuleId){
		StringBuffer queryString =new StringBuffer();
		queryString.append("select con.id,"
				+"obj.customerShortName,"+"con.contactEmail,"+"obj.empId," + "con.contactPerson,"+"obj.id"
				+" from CotCustomer obj, CotCustContact con");
		queryString.append(" where obj.customerEmail is not null and obj.customerEmail <> '' and con.contactEmail is not null and con.contactEmail <> ''");
		queryString.append(" and obj.id = con.customerId");
		queryString.append(" and obj.empId="+empId);
		System.out.println(queryString.toString());
		
		List<CotCustomerVO> customerList = new ArrayList<CotCustomerVO>();
		List<?> list = this.getBaseDao().find(queryString.toString());
		for (int i = 0; i < list.size(); i++) {
			Object[] objs = (Object[]) list.get(i);
			CotCustomerVO customer = new CotCustomerVO();
			customer.setId((Integer) objs[0]);
			customer.setCustomerShortName((String) objs[1]); 
			customer.setCustomerEmail((String) objs[2]);
			customer.setEmpId((Integer) objs[3]);
			customer.setContactPerson((String) objs[4]);
			customer.setCustId((Integer) objs[5]);
			customer.setType("CUST");
			customerList.add(customer);
		}
		return this.initCustomer(customerList, empRuleId);		
	}
	
	
	//初始化customer
	public String initCustomer(List<CotCustomerVO> list,Integer empRuleId){
		//先通过ruleId到从表-规则查询全部规则记录
		List<CotMailRule> res =this.getMailRuleById(empRuleId);
			try {
				List<CotMailExecute> listExc = this.getMailExecuteById(empRuleId);
				//构建Map
				Map map =new HashMap();
				for(int i =0;i<list.size(); i++){
					CotCustomerVO cust =list.get(i);
					map.put(cust.getId(),cust.getCustomerEmail());
				}
				CotMailRuleCfg cotMailRuleCfg=this.getCotMailRuleCfgById(1);
				
				//实例化MyPrediacateRule 从表-规则
				MyPrediacateRule prediacateRule=new MyPrediacateRule();
				prediacateRule.property ="ruleCfgId";
				prediacateRule.compareId="1";
				//过滤 从表-规则 出符合条件的记录
				List ruleList = this.getConfigedList(res,map,prediacateRule);
				for(int y=0;y<ruleList.size();y++){
					CotMailRule mailRule =(CotMailRule) ruleList.get(y);
					int id =mailRule.getCustId();
					String em =null;
					for(int tp =0;tp<list.size();tp++){
						CotCustomerVO vo =list.get(tp);
						if(vo.getId()==id){
							em=vo.getCustomerEmail();
						}
					}
					List<CotMailRule> arr = new ArrayList<CotMailRule>();
					mailRule.setLeftTerm(cotMailRuleCfg.getProperty());
					mailRule.setOp("contains");
					mailRule.setRelate("AND");
					mailRule.setRuleCfgId(1);
					mailRule.setRuleId(empRuleId);
					mailRule.setCustId(id);
					mailRule.setRightTerm(em.toLowerCase());
					mailRule.setType("CUST");
					arr.add(mailRule);
					this.saveCotMailRule(arr);
			   }
			
				if(map.size()>0){
					Iterator it =map.keySet().iterator();
					while(it.hasNext()){
						int key =(Integer) it.next();
						String em =(String) map.get(key);
						
						List<CotMailRule> ls = new ArrayList<CotMailRule>();
						CotMailRule mailRule= new CotMailRule();
						mailRule.setLeftTerm(cotMailRuleCfg.getProperty());
						System.out.println(cotMailRuleCfg.getProperty());
						mailRule.setOp("contains");
						mailRule.setRelate("AND");
						mailRule.setRuleCfgId(1);
						mailRule.setRuleId(empRuleId);
						mailRule.setCustId(key);
						mailRule.setRightTerm(em.toLowerCase());
						mailRule.setType("CUST");
						ls.add(mailRule);
						this.saveCotMailRule(ls);
					}
				}
				
				//先通过ruleId到从表-规则查询全部规则记录
				List<CotMailExecute> listExcs = this.getMailExecuteById(empRuleId);
				CotMailExecuteCfg regExc=this.getCotMailExecuteCfgById(2);
				CotMailExecuteCfg Exc=this.getCotMailExecuteCfgById(3);
				//构建Map(指派给)
				Map mapExc=new HashMap();
				for(int i =0;i<list.size(); i++){
					CotCustomerVO cust =list.get(i);
					mapExc.put(cust.getId(),cust.getCustomerEmail());
				}
				
				//实例化MyPrediacateRule 从表-条件
				MyPrediacateRule pred=new MyPrediacateRule();
				pred.property ="executeCfgId";
				pred.compareId="2";
				
				//过滤 从表-条件 出符合条件的记录
				List excList = this.getConfigedList(listExcs,mapExc,pred);
				//通过empRuleId获得主表对象
				CotMailEmpsRule mailEmpsRule = this.getMailEmpsRuleById(empRuleId);
				
				for(int z =0;z<excList.size();z++){
					 List<CotMailExecute> excArray = new ArrayList<CotMailExecute>();
					 CotMailExecute mailExc =(CotMailExecute) excList.get(z);
					 //获取业务员名称和ID
					 String argsName =mailEmpsRule.getEmpName();
					 Integer args =mailEmpsRule.getEmpId();
					 
					 mailExc.setMethod(regExc.getMethod());
					 mailExc.setName(regExc.getName());
					 mailExc.setArgsName(argsName);
					 mailExc.setArgs(String.valueOf(args));	
					 mailExc.setRuleId(empRuleId);
					 mailExc.setPackage_(regExc.getPackage_());
					 mailExc.setClass_(regExc.getClass_());
					 mailExc.setCustId(mailExc.getCustId());
					 mailExc.setExecuteCfgId(2);
					 excArray.add(mailExc);
					 this.saveCotMailExecute(excArray);
				}
				
				if(mapExc.size() > 0){
					Iterator it =mapExc.keySet().iterator();
					while(it.hasNext()){
						int key =(Integer) it.next();
					List<CotMailExecute> excArray = new ArrayList<CotMailExecute>();
					 CotMailExecute mailExc = new CotMailExecute();
					 mailExc.setMethod(regExc.getMethod());
					 mailExc.setName(regExc.getName());
					 mailExc.setArgsName(mailEmpsRule.getEmpName());
					 String args = String.valueOf(mailEmpsRule.getEmpId());
					 mailExc.setArgs(args);	
					 mailExc.setRuleId(empRuleId);
					 mailExc.setPackage_(regExc.getPackage_());
					 mailExc.setClass_(regExc.getClass_());
					 mailExc.setCustId(key);
					 mailExc.setExecuteCfgId(2);
					 mailExc.setType("CUST");
					 excArray.add(mailExc);
					 this.saveCotMailExecute(excArray);
				}
			   }
				
				//构建Map
				int excCustId = 0;
				Map mapExcCl=new HashMap();
				for(int i =0;i<list.size(); i++){
					CotCustomerVO cust =list.get(i);
					excCustId=cust.getCustId();
					mapExcCl.put(cust.getId(),cust.getCustomerEmail());
				}
				
				//实例化MyPrediacateRule 从表-条件
				MyPrediacateRule predCl=new MyPrediacateRule();
				predCl.property ="executeCfgId";
				predCl.compareId="3";
				
				//过滤 从表-条件 出符合条件的记录
				List excListCl = this.getConfigedList(listExc,mapExcCl,predCl);
				
				for(int w =0;w<excListCl.size();w++){
					List<CotMailExecute> excClassify = new ArrayList<CotMailExecute>();
					CotMailExecute mailClassify=(CotMailExecute) excListCl.get(w);
					//获得联系人ID
					int id =mailClassify.getCustId();
					System.out.println("id"+id);
					//通过联系人Id 获得相应的客户名称
					String custShortName = null;
					String custIds =null;
					for(int xp =0;xp<list.size();xp++){
						CotCustomerVO vo =list.get(xp);
						System.out.println("void"+vo.getCustId());
						custIds=String.valueOf(vo.getCustId());
						if(vo.getId()==id){
							custShortName=vo.getCustomerShortName();
						}
					}
					 mailClassify.setArgsName(custShortName);
					 mailClassify.setArgs(custIds); 
				
					 mailClassify.setMethod(Exc.getMethod());
					 mailClassify.setName(Exc.getName());
					 mailClassify.setRuleId(empRuleId);
					 mailClassify.setPackage_(Exc.getPackage_());
					 mailClassify.setClass_(Exc.getClass_());
					 mailClassify.setCustId(id);
					 mailClassify.setType("CUST");
					 mailClassify.setExecuteCfgId(3);
					 excClassify.add(mailClassify);
					 this.saveCotMailExecute(excClassify);
				}
						
				
				if(mapExcCl.size() >0){
					Iterator itCl =mapExcCl.keySet().iterator();
					while(itCl.hasNext()){
						int key =(Integer) itCl.next();
						//通过客户Id 获得相应的客户名称
						 CotCustomer customer = this.getCustomerById(excCustId);
						
						 String custShortName = null;
							for(int xp =0;xp<list.size();xp++){
								CotCustomerVO vo =list.get(xp);
								System.out.println("void"+vo.getId());
								if(vo.getId()==key){
									custShortName=vo.getCustomerShortName();
									System.out.println("客户名称"+vo.getCustomerShortName());
								}
							}
						List<CotMailExecute> excAy = new ArrayList<CotMailExecute>();
						 CotMailExecute mailExcCls = new CotMailExecute();
						 mailExcCls.setMethod(Exc.getMethod());
						 mailExcCls.setName(Exc.getName());
						 mailExcCls.setArgsName(custShortName);
						 //客户ID
						 mailExcCls.setArgs(String.valueOf(excCustId));
						 mailExcCls.setRuleId(empRuleId);
						 mailExcCls.setPackage_(Exc.getPackage_());
						 mailExcCls.setClass_(Exc.getClass_());
						 mailExcCls.setCustId(key);
						 mailExcCls.setExecuteCfgId(3);
						 mailExcCls.setType("CUST");
						 excAy.add(mailExcCls);
						 this.saveCotMailExecute(excAy);
						
					}
				}
				
			} catch (Exception e) {
				e.printStackTrace();
			
		}
		return "success";
	}
	
	//通过ID查找执行内容对象
	public CotMailExecuteCfg getCotMailExecuteCfgById(Integer id) {
		// TODO Auto-generated method stub
		String sql = " from CotMailExecuteCfg obj where obj.id="+id;
		List<CotMailExecuteCfg> res = this.getBaseDao().find(sql);
		if(res != null && res.size() > 0)
			return res.get(0);
		return null;
	}
	
	//通过ID查找邮件规则对象
	public CotMailRuleCfg getCotMailRuleCfgById(Integer id) {
		// TODO Auto-generated method stub
		String sql = " from CotMailRuleCfg obj where obj.id="+id;
		List<CotMailRuleCfg> res = this.getBaseDao().find(sql);
		if(res != null && res.size() > 0)
			return res.get(0);
		return null;
	}
	
	//通过ID查找出员工对象 CotEmps
	public CotEmps getCotEmpsById(Integer id) {
		// TODO Auto-generated method stub
		String sql = " from CotEmps obj where obj.id="+id;
		List<CotEmps> res = this.getBaseDao().find(sql);
		if(res != null && res.size() > 0)
			return res.get(0);
		return null;
	}
	
	//通过ID查找出客户对象   ==查
	public CotCustomer getCustomerById(Integer id) {
		// TODO Auto-generated method stub
		String sql = " from CotCustomer obj where obj.id="+id;
		List<CotCustomer> res = this.getBaseDao().find(sql);
		if(res != null && res.size() > 0)
			return res.get(0);
		return null;
	}
	
	

	//查找工厂和联系人
	public List<CotFactoryVO> getFactoryAndContact(QueryInfo queryInfo){
		List<CotFactoryVO> factoryList = new ArrayList<CotFactoryVO>();
		try {
			List<?> list = this.getBaseDao().findRecords(queryInfo);
			for (int i = 0; i < list.size(); i++) {
				Object[] objs = (Object[]) list.get(i);
				CotFactoryVO factory = new CotFactoryVO();
				factory.setId((Integer) objs[0]);
				factory.setFactoryName((String) objs[1]); 
				factory.setContactEmail((String) objs[2]);
				factory.setContactPerson((String) objs[3]);
				factory.setFactoryId((Integer) objs[4]);
				factory.setType("FAC");
				factoryList.add(factory);
			}
			return factoryList;
		} catch (DAOException e) {
			return null;
		}
	}
	
	//通过业务员ID获取相应的客户
	public List<CotCustomer> getCustByEmpId(Integer empId){
		String sql=" from CotCustomer obj where obj.empId="+empId;
		List<CotCustomer> res = this.getBaseDao().find(sql);
		if(res==null) return null;
		return res;
	}
	
}
