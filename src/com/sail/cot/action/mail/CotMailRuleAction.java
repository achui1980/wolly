package com.sail.cot.action.mail;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.sail.cot.action.AbstractAction;
import com.sail.cot.domain.CotCustomer;
import com.sail.cot.domain.CotEmps;
import com.sail.cot.domain.CotFactory;
import com.sail.cot.domain.CotMailEmpsRule;
import com.sail.cot.domain.CotMailExecute;
import com.sail.cot.domain.CotMailRule;
import com.sail.cot.domain.vo.CotCustomerVO;
import com.sail.cot.domain.vo.CotFactoryVO;
import com.sail.cot.domain.vo.CotEmpsRuleVO;
import com.sail.cot.domain.vo.MailSysServiceVo;
import com.sail.cot.mail.ruleservice.MailRuleService;
import com.sail.cot.query.QueryInfo;
import com.sail.cot.util.GridServerHandler;
import com.sail.cot.util.ReflectionUtils;



public class CotMailRuleAction extends AbstractAction{

	private MailRuleService mailRuleService;
	
	public MailRuleService getMailRuleService() {
		if(mailRuleService==null){
			mailRuleService =(MailRuleService)super.getBean("MailRuleService");
		}
		return mailRuleService;
	}

	public void setMailRuleService(MailRuleService mailRuleService) {
		this.mailRuleService = mailRuleService;
	}

	
	@Override
	public ActionForward add(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String empsRuleId = request.getParameter("empsRuleId");
		String custId =request.getParameter("custId");
		String type =request.getParameter("type");
		System.out.println(type);
		// 前台传递来的数据
		String json = request.getParameter("data");
		
		// 判断数据是一条还是多条
		boolean single = json.startsWith("{");
		// 1.使用反射获取对象的所有属性名称
		String[] properties = ReflectionUtils
				.getDeclaredFields(CotMailRule.class);
		List<CotMailRule> records = new ArrayList<CotMailRule>();
		try {
			if (single) {
				JSONObject jsonObject = JSONObject.fromObject(json);
				CotMailRule cotMailRule = new CotMailRule();
				for (int i = 0; i < properties.length; i++) {
					Object temp = jsonObject.get(properties[i]);
					if (temp instanceof JSONObject) {
						BeanUtils.setProperty(cotMailRule, properties[i],
								null);
					} else {
						BeanUtils.setProperty(cotMailRule, properties[i],
								jsonObject.get(properties[i]));
					}
				}
				cotMailRule.setId(null);
				cotMailRule.setType(type);
				cotMailRule.setRuleId(Integer.parseInt(empsRuleId));
				cotMailRule.setCustId(Integer.parseInt(custId));
				records.add(cotMailRule);
			} else {
				JSONArray jarray = JSONArray.fromObject(json);
				for (int i = 0; i < jarray.size(); i++) {
					CotMailRule cotMailRule = new CotMailRule();
					JSONObject jsonObject = (JSONObject) jarray.get(i);
					for (int j = 0; j < properties.length; j++) {
						Object temp = jsonObject.get(properties[j]);
						if (temp instanceof JSONObject) {
							BeanUtils.setProperty(cotMailRule, properties[j],
									null);
						} else {
							BeanUtils.setProperty(cotMailRule, properties[j],
									jsonObject.get(properties[j]));
						}
					}
					cotMailRule.setId(null);
					cotMailRule.setType(type);
					cotMailRule.setRuleId(Integer.parseInt(empsRuleId));
					cotMailRule.setCustId(Integer.parseInt(custId));
					records.add(cotMailRule);
				}
			}
			this.getMailRuleService().saveCotMailRule(records);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public ActionForward modify(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String type =request.getParameter("type");
		System.out.println(type);
		// 前台传递来的数据
		String json = request.getParameter("data");
		// 判断数据是一条还是多条
		boolean single = json.startsWith("{");
		// 1.使用反射获取对象的所有属性名称
		String[] properties = ReflectionUtils
				.getDeclaredFields(CotMailRule.class);
		List<CotMailRule> records = new ArrayList<CotMailRule>();
		try {
			if (single) {
				JSONObject jsonObject = JSONObject.fromObject(json);
				CotMailRule cotMailRule= this.getMailRuleService().getCotMailRuleById(Integer.parseInt(jsonObject.getString("id")));
			    for (int i = 0; i < properties.length; i++) {
					Object temp = jsonObject.get(properties[i]);
					if (temp instanceof JSONObject) {
						BeanUtils.setProperty(cotMailRule, properties[i],
								null);
					} else {
						BeanUtils.setProperty(cotMailRule, properties[i],
								jsonObject.get(properties[i]));
					}
				}
			    cotMailRule.setType(type);
				records.add(cotMailRule);
			} else {
				JSONArray jarray = JSONArray.fromObject(json);
				for (int i = 0; i < jarray.size(); i++) {
					JSONObject jsonObject = (JSONObject) jarray.get(i);
				//	List<CotMailRule> list= this.getMailRuleService().getMailRuleById(Integer.parseInt(jsonObject.getString("id")));
				//	CotMailRule cotMailRule =list.get(0);
					CotMailRule cotMailRule= this.getMailRuleService().getCotMailRuleById(Integer.parseInt(jsonObject.getString("id")));
					for (int j = 0; j < properties.length; j++) {
						Object temp = jsonObject.get(properties[j]);
						if (temp instanceof JSONObject) {
							BeanUtils.setProperty(cotMailRule, properties[j],
									null);
						} else {
							BeanUtils.setProperty(cotMailRule, properties[j],
									jsonObject.get(properties[j]));
						}
					}
					cotMailRule.setType(type);
					records.add(cotMailRule);
				}
			}
			this.getMailRuleService().modifyCotMailRule(records);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public ActionForward query(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String start = request.getParameter("start");
		String limit = request.getParameter("limit");
		String bussinessPerson = request.getParameter("businessPersonFind");
		String json;
	
		if(start==null || limit ==null){
			return mapping.findForward("querySuccess");
		}
	
		StringBuffer queryString = new StringBuffer();
		queryString.append(" where obj.empId= e.id");
		queryString.append(" and obj.type='MAIL'");

		// 获得登录人
//		CotEmps emp = (CotEmps) request.getSession().getAttribute("emp");
//		if (!"admin".equals(emp.getEmpsId())) {
//			queryString.append(" and e.id = "+emp.getId());
//		}
			
		if(null!=bussinessPerson && !"".equals(bussinessPerson))
		{
			bussinessPerson = bussinessPerson.trim();
			
			queryString.append(" and e.id = "+bussinessPerson); 
		}

		
		// 设置查询参数
		QueryInfo queryInfo = new QueryInfo();
	
		// 设置每页显示多少行
		int pageCount  = Integer.parseInt(limit);
		// 设定每页显示记录数
		queryInfo.setCountOnEachPage(pageCount);
		int startIndex = Integer.parseInt(start);	
		queryInfo.setStartIndex(startIndex);
	
		// 设置查询记录总数语句
		queryInfo.setCountQuery("select count(*) from CotMailEmpsRule obj, CotEmps e" + queryString);
		// 设置查询记录语句
		queryInfo.setSelectString("select obj.id, obj.empId , obj.relate , obj.ruleDefault , obj.ruleName ,obj.ruleDesc ,obj.xmlPath ,obj.type from CotMailEmpsRule obj, CotEmps e");
		
		// 设置条件语句
		queryInfo.setQueryString(queryString.toString());
		// 设置排序语句
		queryInfo.setOrderString(" order by obj.id desc");
		
		// 根据起始List<CotMailEmpsRule>
		List<CotEmpsRuleVO> list = this.getMailRuleService().getMailEmpsRuleList(queryInfo);			
		int count = this.getMailRuleService().getRecordCount(queryInfo);
		
		GridServerHandler grid = new GridServerHandler();
		grid.setTotalCount(count);
		grid.setData(list);
		json = grid.getLoadResponseText();
		try {
			response.getWriter().write(json);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

    @Override
	public ActionForward remove(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
    	String json = request.getParameter("data");
		List ids = null;
		// 判断数据是一条还是多条
		boolean single = json.startsWith("[");
		if (!single) {
			ids = new ArrayList();
			ids.add(new Integer(json));
		} else {
			JSONArray jarray = JSONArray.fromObject(json);
			ids = jarray.toList(jarray, ArrayList.class);
		}
		this.getMailRuleService().deleteList(ids, "CotMailRule");
		return null;
	}

 // 跳转到规则添加页面
    public ActionForward addRule(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {

		return mapping.findForward("addDetail");
	}
   //获得规则信息
    public ActionForward queryRule(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response){
  	
		String custId =request.getParameter("custId");
		String type =request.getParameter("type");
		String ruleId =request.getParameter("ruleId");
		if (custId == null || "".equals(custId)) {
			return null;
		}
	    int id =Integer.parseInt(custId);
	    int rId = 0;
	    if(ruleId != null && !ruleId.trim().equals("null")){
	    	rId =Integer.parseInt(ruleId);
	    }
	    
		List<CotMailRule> list = this.getMailRuleService().getMailRuleByCustId(id,type,rId);		
		int count = list.size();
	
		GridServerHandler grid = new GridServerHandler();
		grid.setTotalCount(count);
		grid.setData(list);
		String json = grid.getLoadResponseText();
    	
		try {
			response.getWriter().write(json);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
    }
	//获得规则条件
    public ActionForward queryExecute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response){
    	String type =request.getParameter("type");
		String custId =request.getParameter("custId");
		String ruleId =request.getParameter("ruleId");
		if (custId == null || "".equals(custId)) {
			return null;
		}
    	int id =Integer.parseInt(custId);
    	int rId = 0;
	    if(ruleId != null && !ruleId.trim().equals("null")){
	    	rId =Integer.parseInt(ruleId);
	    }
		List<CotMailExecute> list = this.getMailRuleService().getMailExecuteByCustId(id,type,rId);		
		int count = list.size();

		GridServerHandler grid = new GridServerHandler();
		grid.setTotalCount(count);
		grid.setData(list);
		String json = grid.getLoadResponseText();
    	
		try {
			response.getWriter().write(json);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
    }

    public ActionForward addExc(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String empsRuleId = request.getParameter("empsRuleId");
		String custId =request.getParameter("custId");
		String type =request.getParameter("type");
		// 前台传递来的数据
		String json = request.getParameter("data");
		System.out.println(json);
		
		// 判断数据是一条还是多条
		boolean single = json.startsWith("{");
		// 1.使用反射获取对象的所有属性名称
		String[] properties = ReflectionUtils
				.getDeclaredFields(CotMailExecute.class);
		List<CotMailExecute> records = new ArrayList<CotMailExecute>();
		try {
			if (single) {
				JSONObject jsonObject = JSONObject.fromObject(json);
				CotMailExecute cotMailExecute = new CotMailExecute();
				for (int i = 0; i < properties.length; i++) {
					Object temp = jsonObject.get(properties[i]);
					if (temp instanceof JSONObject) {
						BeanUtils.setProperty(cotMailExecute, properties[i],
								null);
					} else {
						BeanUtils.setProperty(cotMailExecute, properties[i],
								jsonObject.get(properties[i]));
					}
				}
				cotMailExecute.setId(null);
				cotMailExecute.setType(type);
				cotMailExecute.setRuleId(Integer.parseInt(empsRuleId));
				cotMailExecute.setCustId(Integer.parseInt(custId));
				records.add(cotMailExecute);
			} else {
				JSONArray jarray = JSONArray.fromObject(json);
				for (int i = 0; i < jarray.size(); i++) {
					CotMailExecute cotMailExecute = new CotMailExecute();
					JSONObject jsonObject = (JSONObject) jarray.get(i);
					for (int j = 0; j < properties.length; j++) {
						Object temp = jsonObject.get(properties[j]);
						if (temp instanceof JSONObject) {
							BeanUtils.setProperty(cotMailExecute, properties[j],
									null);
						} else {
							BeanUtils.setProperty(cotMailExecute, properties[j],
									jsonObject.get(properties[j]));
						}
					}
					cotMailExecute.setId(null);
					cotMailExecute.setType(type);
					cotMailExecute.setRuleId(Integer.parseInt(empsRuleId));
					cotMailExecute.setCustId(Integer.parseInt(custId));
					records.add(cotMailExecute);
				}
			}
			this.getMailRuleService().saveCotMailExecute(records);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

    public ActionForward modifyExc(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
    	//String empsRuleId = request.getParameter("empsRuleId");
    	String type =request.getParameter("type");
    	// 前台传递来的数据
		String json = request.getParameter("data");
		System.out.println(json);
		// 判断数据是一条还是多条
		boolean single = json.startsWith("{");
		// 1.使用反射获取对象的所有属性名称
		String[] properties = ReflectionUtils
				.getDeclaredFields(CotMailExecute.class);
		List<CotMailExecute> records = new ArrayList<CotMailExecute>();
		try {
			if (single) {
				JSONObject jsonObject = JSONObject.fromObject(json);
			    CotMailExecute cotMailExecute = this.getMailRuleService().getCotMailExecuteById(Integer.parseInt(jsonObject.getString("id")));
			    for (int i = 0; i < properties.length; i++) {
					Object temp = jsonObject.get(properties[i]);
					if (temp instanceof JSONObject) {
						BeanUtils.setProperty(cotMailExecute, properties[i],
								null);
					} else {
						BeanUtils.setProperty(cotMailExecute, properties[i],
								jsonObject.get(properties[i]));
					}
				}
			    cotMailExecute.setType(type);
				records.add(cotMailExecute);
			} else {
				JSONArray jarray = JSONArray.fromObject(json);
				for (int i = 0; i < jarray.size(); i++) {
					JSONObject jsonObject = (JSONObject) jarray.get(i);
					System.out.println(Integer.parseInt(jsonObject.getString("id")));
				//	List<CotMailExecute> list= this.getMailRuleService().getMailExecuteById(Integer.parseInt(jsonObject.getString("id")));
				//	CotMailExecute cotMailExecute =list.get(0);
					CotMailExecute cotMailExecute = this.getMailRuleService().getCotMailExecuteById(Integer.parseInt(jsonObject.getString("id")));
					for (int j = 0; j < properties.length; j++) {
						Object temp = jsonObject.get(properties[j]);
						if (temp instanceof JSONObject) {
							BeanUtils.setProperty(cotMailExecute, properties[j],
									null);
						} else {
							BeanUtils.setProperty(cotMailExecute, properties[j],
									jsonObject.get(properties[j]));
						}
					}
					cotMailExecute.setType(type);
					records.add(cotMailExecute);
				}
			}
			this.getMailRuleService().modifyCotMailRule(records);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

    public ActionForward removeExc(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
    	String json = request.getParameter("data");
		List ids = null;
		// 判断数据是一条还是多条
		boolean single = json.startsWith("[");
		if (!single) {
			ids = new ArrayList();
			ids.add(new Integer(json));
		} else {
			JSONArray jarray = JSONArray.fromObject(json);
			ids = jarray.toList(jarray, ArrayList.class);
		}
		this.getMailRuleService().deleteList(ids, "CotMailExecute");
		return null;
	}
    //查询客户资料
    public ActionForward queryCust(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String startStr = request.getParameter("start");
		String limitStr = request.getParameter("limit");
		String combox =request.getParameter("comboValue");
//		String flag =request.getParameter("flag");
//		if(flag ==null){
//			return null ;
//		}
		
		if (startStr== null || limitStr == null)
			return mapping.findForward("addDetail");
		try {
			StringBuffer queryString = new StringBuffer();
			queryString.append(" where obj.customerEmail is not null and obj.customerEmail <> ''");
			
			if(combox!=null){
				int com =Integer.parseInt(combox);
				queryString.append(" and obj.empId="+com);
			}
		
			int start = Integer.parseInt(startStr);
			int limit = Integer.parseInt(limitStr);
			QueryInfo queryInfo = new QueryInfo();
			
			// 设定每页显示记录数
			queryInfo.setCountOnEachPage(limit);
			//设置查询记录总数语句
			queryInfo.setCountQuery("select count(*) from CotCustomer obj " +queryString);
			//设置查询记录语句
			queryInfo.setSelectString("select obj.id,"
					+"obj.customerShortName,"+"obj.customerEmail,"+"obj.empId"
					+" from CotCustomer AS obj");
			// 设置条件语句
			queryInfo.setQueryString(queryString.toString());
			// 设置排序语句
//			queryInfo.setOrderString(" order by obj.id asc");
			int count = this.getMailRuleService().getRecordCount(queryInfo);
			
			queryInfo.setStartIndex(start);
			List<CotCustomer> list = this.getMailRuleService().getCusMailList(queryInfo);
			GridServerHandler grid = new GridServerHandler();
			grid.setTotalCount(count);
			grid.setData(list);
			String json = grid.getLoadResponseText();
			response.getWriter().write(json);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
    
    //查询客户和联系人资料
    public ActionForward queryCustAndContact(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String startStr = request.getParameter("start");
		String limitStr = request.getParameter("limit");
		String combox =request.getParameter("comboValue");
		String custPerson = request.getParameter("custFind");
		
		try {
			if (startStr== null || limitStr == null)
				return mapping.findForward("addDetail");
			StringBuffer queryString = new StringBuffer();
			queryString.append(" where con.contactEmail is not null and con.contactEmail <> ''");
			queryString.append(" and obj.id = con.customerId");
			
			if(combox==null || "".equals(combox)){
				return null;
			}else{
				int com =Integer.parseInt(combox);
				queryString.append(" and obj.empId="+com);
			}

			
			if(null!=custPerson && !"".equals(custPerson))
			{
				custPerson = custPerson.trim();
				queryString.append(" and con.customerId = "+custPerson); 
			}
			
			int start = Integer.parseInt(startStr);
			int limit = Integer.parseInt(limitStr);
			QueryInfo queryInfo = new QueryInfo();
			
			// 设定每页显示记录数
			queryInfo.setCountOnEachPage(limit);
			//设置查询记录总数语句
			queryInfo.setCountQuery("select count(*) from CotCustomer obj ,CotCustContact con" +queryString);
			//设置查询记录语句
			queryInfo.setSelectString("select con.id,"
					+"obj.customerShortName,"+"con.contactEmail,"+"obj.empId," + "con.contactPerson,"+"obj.id"
					+" from CotCustomer obj, CotCustContact con");
			// 设置条件语句
			queryInfo.setQueryString(queryString.toString());
			// 设置排序语句
			queryInfo.setOrderString(" order by obj.id asc");
			int count = this.getMailRuleService().getRecordCount(queryInfo);
			
			queryInfo.setStartIndex(start);
			List<CotCustomerVO> list = this.getMailRuleService().getCusMailAndContact(queryInfo);
			GridServerHandler grid = new GridServerHandler();
			grid.setTotalCount(count);
			grid.setData(list);
			String json = grid.getLoadResponseText();
			response.getWriter().write(json);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
    
    //查找工厂和联系人
    public ActionForward queryFactoryAndContact(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String startStr = request.getParameter("start");
		String limitStr = request.getParameter("limit");
		String[] custIds =request.getParameterValues("custIds");
		String factoryId=request.getParameter("factoryId");

		if (startStr== null || limitStr == null)
			return null;
		try {
			StringBuffer queryString = new StringBuffer();
			queryString.append(" where cot.contactEmail is not null and cot.contactEmail <> ''");
			queryString.append(" and obj.id = cot.factoryId");
			
			if(custIds!= null) {
				StringBuffer sql = new StringBuffer();
				for (int i=0; i<custIds.length; i++) {
					sql.append("'")
						.append(custIds[i])
						.append("'")
						.append(",");
				}
				String ids =sql.toString().substring(0, sql.toString().length()-1);
				System.out.println(ids);
				queryString.append(" and cot.id in("+ ids + ")");
			}
			
			if (factoryId != null && !factoryId.trim().equals("")) {
				queryString.append(" and obj.id="+Integer.parseInt(factoryId.trim()));
			}
			int start = Integer.parseInt(startStr);
			int limit = Integer.parseInt(limitStr);
			QueryInfo queryInfo = new QueryInfo();
			// 设置起始
			start = start < 0 ? 0 :start;
			// 设置每页显示多少行
			limit = limit <=0 ? 10:limit;
			// 设定每页显示记录数
			queryInfo.setCountOnEachPage(limit);
			//设置查询记录总数语句
			queryInfo.setCountQuery("select count(*) from CotFactory obj , CotContact cot " +queryString);
			//设置查询记录语句
			queryInfo.setSelectString("select cot.id,"
					+"obj.factoryName,"+"cot.contactEmail," + "cot.contactPerson," +"obj.id"
					+" from CotFactory obj ,CotContact cot");
			// 设置条件语句
			queryInfo.setQueryString(queryString.toString());
			// 设置排序语句
			queryInfo.setOrderString(" order by obj.id asc");
			int count = this.getMailRuleService().getRecordCount(queryInfo);
			
			queryInfo.setStartIndex(start);
			List<CotFactoryVO> list = this.getMailRuleService().getFactoryAndContact(queryInfo);
			
			GridServerHandler grid = new GridServerHandler();
			grid.setTotalCount(count);
			grid.setData(list);
			String json = grid.getLoadResponseText();
			response.getWriter().write(json);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
    //
    public ActionForward addFac(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response){
    	return null;
    }
    
}
