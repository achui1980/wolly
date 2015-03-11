package com.sail.cot.action.mail;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.sail.cot.action.AbstractAction;
import com.sail.cot.dao.CotBaseDao;
import com.sail.cot.domain.CotEmps;
import com.sail.cot.domain.vo.CotAddressVO;
import com.sail.cot.domain.vo.CotAutoCompleteVO;
import com.sail.cot.email.service.MailLocalService;
import com.sail.cot.query.QueryInfo;
import com.sail.cot.service.customer.CotCustomerService;
import com.sail.cot.service.system.CotEmpsService;
import com.sail.cot.service.system.CotFactoryService;
import com.sail.cot.util.GridServerHandler;
import com.sail.cot.util.Log4WebUtil;
import com.sail.cot.util.SystemUtil;

public class CotMailSendAction extends AbstractAction{
	private Logger logger = Log4WebUtil.getLogger(CotMailAction.class);
	private CotFactoryService cotFactoryService;
	private CotCustomerService cotCustomerService;
	private CotEmpsService cotEmpsService;
	public CotEmpsService getCotEmpsService(){
		if(cotEmpsService==null){
			cotEmpsService=(CotEmpsService) super.getBean("CotEmpsService");
		}
		return cotEmpsService;
	}
	public CotFactoryService getCotFactoryService(){
		if(cotFactoryService==null){
			cotFactoryService=(CotFactoryService) super.getBean("CotFactoryService");
		}
		return cotFactoryService;
	}
	private CotBaseDao cotBaseDao;
	public CotBaseDao getCotBaseDao(){
		if(cotBaseDao == null){
			cotBaseDao = (CotBaseDao)super.getBean("CotBaseDao");
		}
		return cotBaseDao;
	}
	public CotCustomerService getCotCustomerService(){
		if(cotCustomerService==null){
			cotCustomerService=(CotCustomerService) super.getBean("CotCustomerService");
		}
		return cotCustomerService;
	}
	private MailLocalService mailLocalService;
	public MailLocalService getMailLocalService(){
		if(mailLocalService == null){
			mailLocalService = (MailLocalService)super.getBean("MailLocalService");
		}
		return mailLocalService;
	}
	
	@Override
	public ActionForward add(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ActionForward modify(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ActionForward query(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		CotEmps emp = (CotEmps) request.getSession().getAttribute("emp");
		request.setAttribute("sendEmpEmail", emp.getEmpsMail());
		request.setAttribute("sendEmpName", emp.getEmpsName());
		request.setAttribute("empsId", emp.getId());
		CotEmps emps=this.getCotEmpsService().getEmpsById(emp.getId());
		request.setAttribute("empsSign", emps.getEmpsSign());
		return mapping.findForward("querySuccess");
	}

	@Override
	public ActionForward remove(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public ActionForward queryFactoryAddress(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String startStr = request.getParameter("start");
		String limitStr = request.getParameter("limit");
		String query = request.getParameter("query");
		try {
			StringBuffer queryString = new StringBuffer();
			queryString.append(" where con.contactEmail is not null and con.contactEmail <> ''");
			queryString.append(" and obj.id = con.factoryId");
			try {
				JSONObject jObject = JSONObject.fromObject(query);
				String addressName = jObject.getString("addressName");
				if(addressName!=null&&!addressName.trim().equals("")){
					queryString.append(" and con.contactPerson like '%"+addressName+"%'");
				}
				String name = jObject.getString("name");
				if(name!=null&&!name.trim().equals(""))
					queryString.append(" and obj.shortName like '%"+name+"%'");
			} catch (Exception e) {
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
			queryInfo.setCountQuery("select count(*) from CotFactory AS obj,CotContact AS con " +queryString);
			//设置查询记录语句
			queryInfo.setSelectString("select con.id,"
					+"obj.shortName,"+"con.contactPerson,"+"con.contactEmail"
					+" from CotFactory AS obj,CotContact AS con ");
			// 设置条件语句
			queryInfo.setQueryString(queryString.toString());
			// 设置排序语句
			queryInfo.setOrderString(" order by con.id asc");
			int count = this.getCotFactoryService().getRecordCount(queryInfo);
			
			queryInfo.setStartIndex(start);
			List<CotAddressVO> list = this.getCotFactoryService().getFactoryMailList(queryInfo);
			
			GridServerHandler grid = new GridServerHandler();
			grid.setTotalCount(count);
			grid.setData(list);
			String json = grid.getLoadResponseText();
			response.getWriter().write(json);
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
		return null;
	}
	public ActionForward readAutoFactoryAddressName(ActionMapping mapping,ActionForm form,
			HttpServletRequest request,HttpServletResponse response){
		logger.debug("读取自动补全邮件");
		String startStr = request.getParameter("start");
		String limitStr = request.getParameter("limit");
		String query = request.getParameter("query");
		String json;
		
		try {
			StringBuffer queryString = new StringBuffer();
			queryString.append(" where con.contactEmail is not null and con.contactEmail <> ''");
			queryString.append(" and obj.id = con.factoryId");
			try {
				JSONObject jObject = JSONObject.fromObject(query);
				String addressName = jObject.getString("addressName");
				if(addressName!=null&&!addressName.trim().equals("")){
					queryString.append(" and con.contactPerson like '%"+addressName+"%'");
				}
				String name = jObject.getString("name");
				if(name!=null&&!name.trim().equals(""))
					queryString.append(" and obj.shortName like '%"+name+"%'");
			} catch (Exception e) {
			}
			int start = Integer.parseInt(startStr);
			int limit = Integer.parseInt(limitStr);
			QueryInfo queryInfo = new QueryInfo();
			// 设置起始
			start = start < 0 ? 0 :start;
			// 设置每页显示多少行
			limit = limit <=0 ? 20:limit;
			// 设定每页显示记录数
			queryInfo.setCountOnEachPage(limit);

			//设置查询记录总数语句
			queryInfo.setCountQuery("select count(distinct con.contactPerson) from CotFactory AS obj,CotContact AS con " +queryString);
			//设置查询记录语句
			queryInfo.setSelectString("select distinct con.contactPerson as name from CotFactory AS obj,CotContact AS con ");
			// 设置条件语句
			queryInfo.setQueryString(queryString.toString());
			// 设置排序语句
			queryInfo.setOrderString(" order by con.id asc");
			int count = this.getCotFactoryService().getRecordCount(queryInfo);
			
			queryInfo.setStartIndex(start);
			// 根据起始
			List<CotAutoCompleteVO> list = this.getMailLocalService().getAutoCompletList(queryInfo);
			
			GridServerHandler grid = new GridServerHandler();
			grid.setTotalCount(count);
			grid.setData(list);
			json = grid.getLoadResponseText();
			response.getWriter().write(json);
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
		return null;
	}
	public ActionForward readFactoryName(ActionMapping mapping,ActionForm form,
			HttpServletRequest request,HttpServletResponse response){
		logger.debug("读取");
		String startStr = request.getParameter("start");
		String limitStr = request.getParameter("limit");
		String json;
		
		try {
			StringBuffer queryString = new StringBuffer();
			queryString.append(" where con.contactEmail is not null and con.contactEmail <> ''");
			queryString.append(" and obj.id = con.factoryId");
			int start = Integer.parseInt(startStr);
			int limit = Integer.parseInt(limitStr);
			QueryInfo queryInfo = new QueryInfo();
			// 设置起始
			start = start < 0 ? 0 :start;
			// 设置每页显示多少行
			limit = limit <=0 ? 20:limit;
			// 设定每页显示记录数
			queryInfo.setCountOnEachPage(limit);

			//设置查询记录总数语句
			queryInfo.setCountQuery("select count(distinct obj.shortName) from CotFactory AS obj,CotContact AS con " +queryString);
			//设置查询记录语句
			queryInfo.setSelectString("select distinct obj.shortName as name from CotFactory AS obj,CotContact AS con ");
			// 设置条件语句
			queryInfo.setQueryString(queryString.toString());
			// 设置排序语句
			queryInfo.setOrderString(" order by con.id asc");
			int count = this.getCotFactoryService().getRecordCount(queryInfo);
			
			queryInfo.setStartIndex(start);
			// 根据起始
			List<CotAutoCompleteVO> list = this.getMailLocalService().getAutoCompletList(queryInfo);
			
			GridServerHandler grid = new GridServerHandler();
			grid.setTotalCount(count);
			grid.setData(list);
			json = grid.getLoadResponseText();
			response.getWriter().write(json);
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
		return null;
	}
	public ActionForward readCustName(ActionMapping mapping,ActionForm form,
			HttpServletRequest request,HttpServletResponse response){
		logger.debug("读取");
		String startStr = request.getParameter("start");
		String limitStr = request.getParameter("limit");
		String query = request.getParameter("query");
		String json;
		
		try {
			StringBuffer queryString = new StringBuffer();
			queryString.append(" where con.contactEmail is not null and con.contactEmail <> ''");
			queryString.append(" and obj.id = con.customerId");
			if(query!=null&&!query.trim().equals(""))
				queryString.append(" and obj.customerShortName like '%"+query+"%'");
			customreSystem(request, queryString);
			int start = Integer.parseInt(startStr);
			int limit = Integer.parseInt(limitStr);
			QueryInfo queryInfo = new QueryInfo();
			// 设置起始
			start = start < 0 ? 0 :start;
			// 设置每页显示多少行
			limit = limit <=0 ? 20:limit;
			// 设定每页显示记录数
			queryInfo.setCountOnEachPage(limit);

			//设置查询记录总数语句
			queryInfo.setCountQuery("select count(distinct obj.customerShortName) from CotCustomer AS obj,CotCustContact AS con " +queryString);
			//设置查询记录语句
			queryInfo.setSelectString("select distinct obj.customerShortName as name from CotCustomer AS obj,CotCustContact AS con ");
			// 设置条件语句
			queryInfo.setQueryString(queryString.toString());
			// 设置排序语句
			queryInfo.setOrderString(" order by con.id asc");
			int count = this.getCotFactoryService().getRecordCount(queryInfo);
			
			queryInfo.setStartIndex(start);
			// 根据起始
			List<CotAutoCompleteVO> list = this.getMailLocalService().getAutoCompletList(queryInfo);
			
			GridServerHandler grid = new GridServerHandler();
			grid.setTotalCount(count);
			grid.setData(list);
			json = grid.getLoadResponseText();
			response.getWriter().write(json);
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
		return null;
	}
	public ActionForward readAutoCustAddressName(ActionMapping mapping,ActionForm form,
			HttpServletRequest request,HttpServletResponse response){
		logger.debug("读取自动补全邮件");
		String startStr = request.getParameter("start");
		String limitStr = request.getParameter("limit");
		String query = request.getParameter("query");
		String json;
		
		try {
			StringBuffer queryString = new StringBuffer();
			queryString.append(" where con.contactEmail is not null and con.contactEmail <> ''");
			queryString.append(" and obj.id = con.customerId");
			try {
				JSONObject jObject = JSONObject.fromObject(query);
				String addressName = jObject.getString("addressName");
				if(addressName!=null&&!addressName.trim().equals("")){
					queryString.append(" and con.contactPerson like '%"+addressName+"%'");
				}
				String name = jObject.getString("name");
				if(name!=null&&!name.trim().equals("")){
					queryString.append(" and obj.customerShortName like '%"+name+"%'");
				}
			} catch (Exception e) {
			}
			customreSystem(request, queryString);
			int start = Integer.parseInt(startStr);
			int limit = Integer.parseInt(limitStr);
			QueryInfo queryInfo = new QueryInfo();
			// 设置起始
			start = start < 0 ? 0 :start;
			// 设置每页显示多少行
			limit = limit <=0 ? 20:limit;
			// 设定每页显示记录数
			queryInfo.setCountOnEachPage(limit);

			//设置查询记录总数语句
			queryInfo.setCountQuery("select count(distinct con.contactPerson) from CotCustomer AS obj,CotCustContact AS con " +queryString);
			//设置查询记录语句
			queryInfo.setSelectString("select distinct con.contactPerson as name from CotCustomer AS obj,CotCustContact AS con ");
			// 设置条件语句
			queryInfo.setQueryString(queryString.toString());
			// 设置排序语句
			queryInfo.setOrderString(" order by con.id asc");
			int count = this.getCotFactoryService().getRecordCount(queryInfo);
			
			queryInfo.setStartIndex(start);
			// 根据起始
			List<CotAutoCompleteVO> list = this.getMailLocalService().getAutoCompletList(queryInfo);
			
			GridServerHandler grid = new GridServerHandler();
			grid.setTotalCount(count);
			grid.setData(list);
			json = grid.getLoadResponseText();
			response.getWriter().write(json);
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
		return null;
	}
	public ActionForward queryCusAddress(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String startStr = request.getParameter("start");
		String limitStr = request.getParameter("limit");
		String query = request.getParameter("query");
		try {
			StringBuffer queryString = new StringBuffer();
			queryString.append(" where con.contactEmail is not null and con.contactEmail <> ''");
			queryString.append(" and obj.id = con.customerId");
			try {
				JSONObject jObject = JSONObject.fromObject(query);
				String addressName = jObject.getString("addressName");
				if(addressName!=null&&!addressName.trim().equals("")){
					queryString.append(" and con.contactPerson like '%"+addressName+"%'");
				}
				String name = jObject.getString("name");
				if(name!=null&&!name.trim().equals(""))
					queryString.append(" and obj.customerShortName like '%"+name+"%'");
			} catch (Exception e) {
			}
			customreSystem(request, queryString); // 权限控制
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
			queryInfo.setCountQuery("select count(*) from CotCustomer AS obj,CotCustContact AS con " +queryString);
			//设置查询记录语句
			queryInfo.setSelectString("select con.id,"
					+"obj.customerShortName,"+"con.contactPerson,"+"con.contactEmail"
					+" from CotCustomer AS obj,CotCustContact AS con ");
			// 设置条件语句
			queryInfo.setQueryString(queryString.toString());
			// 设置排序语句
			queryInfo.setOrderString(" order by con.id asc");
			int count = this.getCotCustomerService().getRecordCount(queryInfo);
			
			queryInfo.setStartIndex(start);
			List<CotAddressVO> list = this.getCotCustomerService().getCusMailList(queryInfo);
			GridServerHandler grid = new GridServerHandler();
			grid.setTotalCount(count);
			grid.setData(list);
			String json = grid.getLoadResponseText();
			response.getWriter().write(json);
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
		return null;
	}
	@SuppressWarnings("unchecked")
	private void customreSystem(HttpServletRequest request,
			StringBuffer queryString) {
		CotEmps cotEmps = (CotEmps) request.getSession().getAttribute("emp");
		if("admin".equals(cotEmps.getEmpsId())){ // 管理员查看所有
		}else{
			if(SystemUtil.isAction(request, "cotcustomer.do", "ALL")){ // 公司客户
			}else if(SystemUtil.isAction(request, "cotcustomer.do", "DEPT")&&cotEmps.getDeptId()!=null){ // 部门客户
				String hql = "select obj.id from CotEmps obj where obj.deptId = "+cotEmps.getDeptId();
				List<Integer> ids = this.getCotBaseDao().find(hql);
				queryString.append(" and obj.empId in("+ids.get(0));
				for (int i = 1; i < ids.size(); i++) {
					queryString.append(","+ids.get(i));
				}
				queryString.append(")");
			}else if(SystemUtil.isAction(request, "cotcustomer.do", "MINE")){ // 个人客户
				queryString.append(" and obj.empId =" +cotEmps.getId());
			}else{
				queryString.append(" and 1<>1");
			}
		}
	}
	
	public ActionForward querySignature(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		CotEmps emp = (CotEmps) request.getSession().getAttribute("emp");
		request.setAttribute("empsId", emp.getId());
		return mapping.findForward("querySign");
	}
	
}
