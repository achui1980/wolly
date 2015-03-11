/**
 * 
 */
package com.sail.cot.mail.ruleservice.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
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


import mondrian.gui.MondrianGuiDef.Value;

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
import com.sail.cot.domain.CotMailEmpsRule;
import com.sail.cot.domain.CotMailExecute;
import com.sail.cot.domain.CotMailExecuteCfg;
import com.sail.cot.domain.CotMailRuleCfg;

import com.sail.cot.domain.CotMailRule;

import com.sail.cot.domain.CotPriceFittings;

import com.sail.cot.domain.CotOrder;

import com.sail.cot.domain.vo.CotCustomerVO;
import com.sail.cot.domain.vo.CotEmpsRuleVO;
import com.sail.cot.domain.vo.CotFactoryVO;
import com.sail.cot.domain.vo.CotRuleVO;




import com.sail.cot.mail.MailExecuteAction;

import com.sail.cot.mail.MailExecuteAction;

import com.sail.cot.mail.ruleservice.MailCheckService;
import com.sail.cot.mail.ruleservice.MailRuleService;
import com.sail.cot.mail.ruleservice.impl.MailRuleServiceImpl.MyPrediacateRule;
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
public class MailCheckServiceImpl implements MailCheckService{

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
	public List<CotMailRule> getMailRuleByEmpsId(Integer custId,String type,Integer ruleId) {
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
	
	
	/* (non-Javadoc) 
	 * @see com.sail.cot.mail.ruleservice.MailRuleService#getMailExecuteById(java.lang.Integer)
	 */
	public List<CotMailExecute> getMailExecuteById(Integer ruleId) {
		// TODO Auto-generated method stub
		String strSql = " from CotMailExecute obj where obj.type='CHECK' and obj.ruleId = "+ruleId;
		List<CotMailExecute> res = this.getBaseDao().find(strSql);
		return res;
	}
	
//	//获取审核人ID
//    public Integer getMailCheckEmpId(Integer ruleId){
//    	String strSql = " from CotMailExecute obj where obj.type='CHECK' and obj.ruleId = "+ruleId;
//		List<CotMailExecute> res = this.getBaseDao().find(strSql);
//		CotMailExecute cotMailExecute = null;
//		if(res != null && res.size() > 0){
//			cotMailExecute = res.get(0);
//		}
//		int args =Integer.parseInt(cotMailExecute.getArgs());
//		return args;
//    }
	
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

	
//--------------------------------------------从表 规则条件的相关操作   end
	
	
	
	
//--------------------------------------------主表 邮件规则的相关操作   start

	public CotMailEmpsRule getDefaultCotMailEmpsRule(Integer empId) {
		// TODO Auto-generated method stub
		String sql = " from CotMailEmpsRule obj where obj.ruleDefault = 1 and obj.type ='CHECK' and obj.empId="+empId;
		System.out.println(sql);
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
				if (obj[7] != null) {
					cotEmpsRuleVO.setType(obj[7].toString());
				}
				list.add(cotEmpsRuleVO);
			}
			return list;
		} catch (DAOException e) {
			e.printStackTrace();
		}
		return null;	
	}
	

	/**1111111111111
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
	
	//查找所有 主表-规则
	public List<CotMailEmpsRule> getAllCotMailEmpsRule(){
		String strSql = " from CotMailEmpsRule";
		List<CotMailEmpsRule> res = this.getBaseDao().find(strSql);
		return res;
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
		try {
			//url解码，避免中文路径乱码
			path  = URLDecoder.decode(path,"UTF-8");
			File outputFile = new File(path+File.separator+"Rule_EMPID_CHECK_"+mailEmpsRule.getEmpId()+".xml");
			XMLWriter xmlWriter = new XMLWriter(new FileOutputStream(outputFile),xmlFormat);
			xmlWriter.write(doc);
			xmlWriter.close();
			mailEmpsRule.setXmlPath("mailrules"+File.separator+"Rule_EMPID_CHECK_"+mailEmpsRule.getEmpId()+".xml");
			List arrList = new ArrayList();
			//更新路径
			arrList.add(mailEmpsRule);
			this.getBaseDao().updateRecords(arrList);
			return "mailrules"+File.separator+"Rule_EMPID_CHECK_"+mailEmpsRule.getEmpId()+".xml";
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return null;
	}
	private Document createRuleXml(CotMailEmpsRule empRule,List<CotMailRule> ruleList,List<CotMailExecute> executeList) {
		if(ruleList == null || ruleList.size() < 1) return null;
		if(executeList == null || executeList.size() < 1) return null;
		Map<String,String> map = new HashMap<String,String>();
		Map<Integer, String> custMap = new HashMap<Integer, String>();
		
		//命名规则：配置项+规则ID
		for(CotMailExecute execute : executeList){
			//过滤实例化对象的类名
			map.put(execute.getClass_()+"_"+execute.getRuleId(), execute.getPackage_());
		}
		for(CotMailRule mailrule : ruleList){
			if(mailrule.getType().equals("CHECK"))
				custMap.put(mailrule.getCustId(), mailrule.getRelate());
			
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
			myPrediacate.type = "CHECK";
			String relate =  custMap.get(custId);
			Iterator iterator_obj = new FilterIterator(ruleList.iterator(),myPrediacate);
			if("AND".equals(relate)){
				//生成规则
				while (iterator_obj.hasNext()) {
					CotMailRule mailrule = (CotMailRule) iterator_obj.next();
					Element ifElement = rule.addElement("if");
					ifElement.addAttribute("leftTerm", mailrule.getLeftTerm());
					ifElement.addAttribute("op", mailrule.getOp());
					
					System.out.println(mailrule.getRightTerm());
					String right=mailrule.getRightTerm().replaceAll(";", ",");
					System.out.println(right);
				    ifElement.addAttribute("rightTerm","["+ right+"]");
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
					String right=mailrule.getRightTerm().replaceAll(";", ",");
				    ifElement.addAttribute("rightTerm","["+ right+"]");
					
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


	//查询员工
	public List<CotEmps> getEmpsList(QueryInfo queryInfo){
		List<CotEmps> empsList = new ArrayList<CotEmps>();
		try {
			List<?> list = this.getBaseDao().findRecords(queryInfo);
			for (int i = 0; i < list.size(); i++) {
				Object[] objs = (Object[]) list.get(i);
				CotEmps cotEmps = new CotEmps();
				cotEmps.setId((Integer) objs[0]);
				cotEmps.setEmpsName((String) objs[1]);
				cotEmps.setEmpsMail((String) objs[2]);
				empsList.add(cotEmps);
			}
			return empsList;
		} catch (DAOException e) {
			e.printStackTrace();
		}
		return null;
	}
	

	//条件筛细类，用于对list的对象按照某个关键之进行分类
	class MyPrediacate implements Predicate{
		public Integer custId;
		public String type;
		public boolean evaluate(Object obj) {
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
	
	//主表删除 且删除对应的XML文件
	public Boolean deleteEmpsRuleList(List<Integer> ruleIds){
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
		} catch (DAOException e) {
			e.printStackTrace();
		}
		return true;
	}
}
