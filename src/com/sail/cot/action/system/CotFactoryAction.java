package com.sail.cot.action.system;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.sail.cot.action.AbstractAction;
import com.sail.cot.action.mail.CotMailAction;
import com.sail.cot.dao.CotBaseDao;
import com.sail.cot.domain.CotEmps;
import com.sail.cot.domain.CotMail;
import com.sail.cot.email.service.MailTreeService;
import com.sail.cot.email.util.MailEntityConverUtil;
import com.sail.cot.query.QueryInfo;
import com.sail.cot.service.system.CotContactService;
import com.sail.cot.service.system.CotFactoryService;
import com.sail.cot.util.GridServerHandler;
import com.sail.cot.util.Log4WebUtil;
import com.sail.cot.util.SystemUtil;

public class CotFactoryAction extends AbstractAction {
	private Logger logger = Log4WebUtil.getLogger(CotMailAction.class);
	private MailTreeService mailTreeService;

	public MailTreeService getMailTreeService() {
		if (mailTreeService == null) {
			mailTreeService = (MailTreeService) super.getBean("MailTreeService");
		}
		return mailTreeService;
	}

	private CotBaseDao cotBaseDao;

	public CotBaseDao getCotBaseDao() {
		if (cotBaseDao == null) {
			cotBaseDao = (CotBaseDao) super.getBean("CotBaseDao");
		}
		return cotBaseDao;
	}

	private CotFactoryService cotFactoryService;

	public CotFactoryService getCotFactoryService() {
		if (cotFactoryService == null) {
			cotFactoryService = (CotFactoryService) super.getBean("CotFactoryService");
		}
		return cotFactoryService;
	}

	private CotContactService cotContactService;

	public CotContactService getCotContactService() {
		if (cotContactService == null) {
			cotContactService = (CotContactService) super.getBean("CotContactService");
		}
		return cotContactService;
	}

	public void setCotContactService(CotContactService cotContactService) {
		this.cotContactService = cotContactService;
	}

	@Override
	public ActionForward add(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {

		return mapping.findForward("add");
	}

	public ActionForward addContact(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {

		return mapping.findForward("addContact");
	}

	@Override
	public ActionForward modify(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {

		return mapping.findForward("modify");

	}

	@Override
	public ActionForward query(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {

		String start = request.getParameter("start");
		String limit = request.getParameter("limit");

		if (start == null || limit == null)
			return mapping.findForward("querySuccess");

		StringBuffer sql = new StringBuffer();
		String factoryNo = request.getParameter("factoryNoFind");
		String shortName = request.getParameter("shortNameFind");
		String facType = request.getParameter("facType");
		String factoryNbr = request.getParameter("factoryNbr");
		String remark = request.getParameter("remark");
		
		// 获得登录人
		CotEmps emp = (CotEmps) request.getSession().getAttribute("emp");
		if(!"admin".equalsIgnoreCase(emp.getEmpsId())){
			// 判断是否有最高权限
			boolean all = SystemUtil.isAction(request, "cotfactory.do", "ALL");
			if (all == false) {
				JSONObject json = SystemUtil.getEmpDataPopedom(emp.getEmpRight());
				//判断国家权限
				boolean nation = SystemUtil.isAction(request, "cotfactory.do", "NATION");
				if(nation ==true){
					String nationStr = json.getString("nation");
					sql.append(" and obj.nationId in(").append(nationStr).append(")");
				}else {
					sql.append(" and obj.nationId = 0");
				}
			}
		}
	
		if (null != factoryNbr && !"".equals(factoryNbr)) {
			factoryNbr = factoryNbr.trim();
			sql.append(" and obj.factoryNbr like '%" + factoryNbr + "%'");
		}
		if (null != remark && !"".equals(remark)) {
			remark = remark.trim();
			sql.append(" and obj.remark like '%" + remark + "%'");
		}
		if (null != factoryNo && !"".equals(factoryNo)) {
			factoryNo = factoryNo.trim();
			sql.append(" and obj.factoryNo like '%" + factoryNo + "%'");
		}
		if (null != shortName && !"".equals(shortName)) {
			shortName = shortName.trim();
			sql.append(" and obj.shortName like '%" + shortName + "%'");
		}
		if (null != facType && !"".equals(facType)) {
			sql.append(" and obj.factroyTypeidLv1 = " + facType);
		}
		sql.append(" and obj.shortName<>'未定义'");

		QueryInfo queryInfo = new QueryInfo();

		// 设置每页显示多少行
		int pageCount = 15;
		// 取得页面选择的每页显示条数
		pageCount = Integer.parseInt(limit);
		// 设定每页显示记录数
		queryInfo.setCountOnEachPage(pageCount);
		// 设置查询记录总数语句
		queryInfo.setCountQuery("select count(*) from CotFactory obj where 1=1" + sql.toString());
		// 设置查询记录语句
		queryInfo.setSelectString("from CotFactory obj where 1=1");
		// 设置条件语句
		queryInfo.setQueryString(sql.toString());
		// 设置排序语句
		queryInfo.setOrderString("");

		// int totalCount =
		// this.getCotFactoryService().getRecordCount(queryInfo);
		int startIndex = Integer.parseInt(start);
		queryInfo.setStartIndex(startIndex);

		try {
			String json = this.getCotFactoryService().getJsonData(queryInfo);
			response.getWriter().write(json);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

		return null;
	}

	@Override
	public ActionForward remove(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {

		return null;
	}

	public ActionForward queryMain(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {

		return mapping.findForward("queryMainSuccess");
	}

	public ActionForward queryContact(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {

		String start = request.getParameter("start");
		String limit = request.getParameter("limit");
		String factoryId = request.getParameter("factoryId");

		if (start == null || limit == null)
			return mapping.findForward("queryContact");

		StringBuffer sql = new StringBuffer();
		sql.append(" where 1=1");

		if (factoryId.trim() != null && !"".equals(factoryId.trim())) {
			sql.append(" and obj.factoryId=" + factoryId);
		}
		QueryInfo queryInfo = new QueryInfo();
		// 设置每页显示多少行
		int pageCount = 15;
		// 取得页面选择的每页显示条数
		pageCount = Integer.parseInt(limit);
		// 设定每页显示记录数
		queryInfo.setCountOnEachPage(pageCount);
		// 设置查询记录总数语句
		queryInfo.setCountQuery("select count(*) from CotContact obj " + sql.toString());
		// 设置查询记录语句
		queryInfo.setSelectString("from CotContact obj ");
		// 设置条件语句
		queryInfo.setQueryString(sql.toString());
		// 设置排序语句
		queryInfo.setOrderString("");
		int startIndex = Integer.parseInt(start);
		queryInfo.setStartIndex(startIndex);
		try {
			String json = this.getCotContactService().getJsonData(queryInfo);
			response.getWriter().write(json);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;

	}

	// 查询厂家的采购记录
	public ActionForward queryOrderFac(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {

		String start = request.getParameter("start");
		String limit = request.getParameter("limit");
		if (start == null || limit == null)
			return mapping.findForward("queryOrderFac");
		String orderNoFind = request.getParameter("orderNoFind"); // 发票编号
		String businessPerson = request.getParameter("businessPerson"); // 业务员
		String factoryId = request.getParameter("factoryId");// 厂家
		String startTime = request.getParameter("startTime"); // 下单起始日期
		String endTime = request.getParameter("endTime"); // 下单结束日期
		StringBuffer queryString = new StringBuffer();

		// 获得登录人
		CotEmps emp = (CotEmps) request.getSession().getAttribute("emp");
		if ("admin".equals(emp.getEmpsId())) {
			queryString.append(" where obj.businessPerson=e.id");
		} else {
			// 判断是否有最高权限
			boolean all = SystemUtil.isAction(request, "cotorderfac.do", "ALL");
			if (all == true) {
				queryString.append(" where obj.businessPerson=e.id");
			} else {
				// 判断是否有查看该部门征样的权限
				boolean dept = SystemUtil.isAction(request, "cotorderfac.do", "DEPT");
				if (dept == true) {
					queryString.append(" where obj.businessPerson = e.id and e.deptId=" + emp.getDeptId());
				} else {
					queryString.append(" where obj.businessPerson = e.id and obj.businessPerson =" + emp.getId());
				}
			}
		}

		if (orderNoFind != null && !orderNoFind.trim().equals("")) {
			queryString.append(" and obj.orderNo like '%" + orderNoFind.trim() + "%'");
		}
		if (businessPerson != null && !businessPerson.trim().equals("")) {
			queryString.append(" and obj.businessPerson=" + businessPerson.trim());
		}
		if (factoryId != null && !factoryId.trim().equals("")) {
			queryString.append(" and obj.factoryId=" + factoryId.trim());
		}
		if (startTime != null && !"".equals(startTime.trim())) {
			queryString.append(" and obj.orderTime >='" + startTime + "'");
		}
		if (endTime != null && !"".equals(endTime.trim())) {
			queryString.append(" and obj.orderTime <='" + endTime + " 23:59:59'");
		}

		// 设置查询参数
		QueryInfo queryInfo = new QueryInfo();
		// 设置每页显示多少行
		int pageCount = 15;
		// 取得页面选择的每页显示条数
		pageCount = Integer.parseInt(limit);
		// 设定每页显示记录数
		queryInfo.setCountOnEachPage(pageCount);
		// 设置查询记录总数语句
		queryInfo.setCountQuery("select count(*) from CotOrderFac obj,CotEmps e" + queryString);

		// 查询语句
		queryInfo.setSelectString("select obj from CotOrderFac obj,CotEmps e");
		// 设置条件语句
		queryInfo.setQueryString(queryString.toString());
		// 设置排序语句
		queryInfo.setOrderString(" order by obj.id desc");
		int startIndex = Integer.parseInt(start);
		queryInfo.setStartIndex(startIndex);
		String excludes[] = { "orderMBImg", "cotOrderFacdetails" };
		queryInfo.setExcludes(excludes);
		try {
			String json = this.getCotFactoryService().getJsonData(queryInfo);
			response.getWriter().write(json);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	// 跳转到厂家的评分记录
	public ActionForward gotoFacFen(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		return mapping.findForward("queryOrderFacFen");
	}
	
	// 查询厂家的评分记录
	public ActionForward queryOrderFacFen(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {

		String start = request.getParameter("start");
		String limit = request.getParameter("limit");
		if (start == null || limit == null)
			return null;
		String orderNoFind = request.getParameter("orderNoFind"); // 发票编号
		String businessPerson = request.getParameter("businessPerson"); // 业务员
		String factoryId = request.getParameter("factoryId");// 厂家
		String startTime = request.getParameter("startTime"); // 下单起始日期
		String endTime = request.getParameter("endTime"); // 下单结束日期
		StringBuffer queryString = new StringBuffer();

		// 获得登录人
		CotEmps emp = (CotEmps) request.getSession().getAttribute("emp");
		if ("admin".equals(emp.getEmpsId())) {
			queryString.append(" where obj.businessPerson=e.id");
		} else {
			// 判断是否有最高权限
			boolean all = SystemUtil.isAction(request, "cotorderfac.do", "ALL");
			if (all == true) {
				queryString.append(" where obj.businessPerson=e.id");
			} else {
				// 判断是否有查看该部门征样的权限
				boolean dept = SystemUtil.isAction(request, "cotorderfac.do", "DEPT");
				if (dept == true) {
					queryString.append(" where obj.businessPerson = e.id and e.deptId=" + emp.getDeptId());
				} else {
					queryString.append(" where obj.businessPerson = e.id and obj.businessPerson =" + emp.getId());
				}
			}
		}

		if (orderNoFind != null && !orderNoFind.trim().equals("")) {
			queryString.append(" and obj.orderNo like '%" + orderNoFind.trim() + "%'");
		}
		if (businessPerson != null && !businessPerson.trim().equals("")) {
			queryString.append(" and obj.businessPerson=" + businessPerson.trim());
		}
		if (factoryId != null && !factoryId.trim().equals("")) {
			queryString.append(" and obj.factoryId=" + factoryId.trim());
		}
		if (startTime != null && !"".equals(startTime.trim())) {
			queryString.append(" and obj.orderTime >='" + startTime + "'");
		}
		if (endTime != null && !"".equals(endTime.trim())) {
			queryString.append(" and obj.orderTime <='" + endTime + " 23:59:59'");
		}

		// 设置查询参数
		QueryInfo queryInfo = new QueryInfo();
		// 设置每页显示多少行
		int pageCount = 15;
		// 取得页面选择的每页显示条数
		pageCount = Integer.parseInt(limit);
		// 设定每页显示记录数
		queryInfo.setCountOnEachPage(pageCount);
		// 设置查询记录总数语句
		queryInfo.setCountQuery("select count(*) from CotOrderFac obj,CotEmps e" + queryString);

		// 查询语句
		queryInfo.setSelectString("select obj from CotOrderFac obj,CotEmps e");
		// 设置条件语句
		queryInfo.setQueryString(queryString.toString());
		// 设置排序语句
		queryInfo.setOrderString(" order by obj.id desc");
		int startIndex = Integer.parseInt(start);
		queryInfo.setStartIndex(startIndex);
		String excludes[] = { "orderMBImg", "cotOrderFacdetails" };
		queryInfo.setExcludes(excludes);
		try {
			String json = this.getCotFactoryService().getJsonData(queryInfo);
			response.getWriter().write(json);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	// 加载邮件
	public ActionForward loadMail(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {

		return mapping.findForward("loadMail");
	}
	
	// 加载厂家报价
	public ActionForward loadPriceFac(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {

		return mapping.findForward("loadPriceFac");
	}

	/**
	 * 根据节点ID读取本地邮件分页邮件
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public ActionForward readLocalMails(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		logger.debug("读取本地邮件");
		String startStr = request.getParameter("start");
		String limitStr = request.getParameter("limit");
		String sortStr = request.getParameter("sort");
		String dirStr = request.getParameter("dir");

		String nodeIdStr = request.getParameter("nodeId");
		String empId = request.getParameter("empId");

		String factoryId = request.getParameter("factoryId");
		String mailType = request.getParameter("mailType");
		String query = request.getParameter("query");

		String isShowChildMail = request.getParameter("isShowChildMail");

		String json;

		try {
			StringBuffer queryString = new StringBuffer();
			List queryParams = new ArrayList();
			Integer nodeId = null;
			try {
				nodeId = Integer.parseInt(nodeIdStr);
			} catch (Exception e) {
			}
			if (empId == null || empId.trim().equals("null")) { // 所有往来邮件
				queryString.append(" where 1=1");
			} else if (empId.equals("")) { // 公共邮箱
				queryString.append(" where obj.empId is null");
			} else { // 个人邮箱
				queryString.append(" where obj.empId = ?");
				queryParams.add(Integer.parseInt(empId));
			}
			if (factoryId != null && !factoryId.equals("")) {
				queryString.append(" and obj.facId = ?");
				queryParams.add(Integer.parseInt(factoryId));
			}
			if (mailType != null && !mailType.equals("")) {
				queryString.append(" and obj.mailType = ?");
				queryParams.add(Integer.parseInt(mailType));
			}
			try {
				JSONObject jObject = JSONObject.fromObject(query);
				try {
					String status = jObject.getString("status");
					if (status != null && !status.trim().equals("")) {
						if (status.equals("1"))
							queryString.append(" and obj.mailStatus <= 1 ");
						else {
							queryString.append(" and obj.mailStatus = ?");
							queryParams.add(Integer.parseInt(status));
						}
					}
				} catch (Exception e) {
				}
				try {
					String personId = jObject.getString("empId");
					if (personId != null && !personId.trim().equals("")) {
						queryString.append(" and obj.empId = ?");
						queryParams.add(Integer.parseInt(personId));
					}
				} catch (Exception e) {
				}
				try {
					String person = jObject.getString("person"); // 联系人
					if (person != null && !person.trim().equals("")) {
						person = "%" + person + "%";
						queryString.append(" and (obj.sendUrl like ?");
						queryParams.add(person);
						queryString.append(" or obj.sendName like ?");
						queryParams.add(person);
						queryString.append(" or obj.toUrl like ?");
						queryParams.add(person);
						queryString.append(" or obj.toName like ?");
						queryParams.add(person);
						queryString.append(" or obj.ccUrl like ?");
						queryParams.add(person);
						queryString.append(" or obj.ccName like ?");
						queryParams.add(person);
						queryString.append(" or obj.bccUrl like ?");
						queryParams.add(person);
						queryString.append(" or obj.bccName like ?)");
						queryParams.add(person);
					}
				} catch (Exception e) {
				}
				try {
					String subject = jObject.getString("subject");
					if (subject != null && !subject.trim().equals("")) {
						queryString.append(" and obj.subject like ?");
						queryParams.add("%" + jObject.getString("subject") + "%");
					}
				} catch (Exception e) {
				}
				try {
					String startDateStr = (String) jObject.get("startDate");
					if (startDateStr != null && !startDateStr.trim().equals("")) {
						queryString.append(" and obj.sendTime >= '" + startDateStr + "'");
					}
				} catch (Exception e) {
				}
				try {
					String endDateStr = (String) jObject.get("endDate");
					if (endDateStr != null && !endDateStr.trim().equals("")) {
						queryString.append(" and obj.sendTime <= '" + endDateStr + "'");
					}
				} catch (Exception e) {
				}
			} catch (Exception e) {
			}
			if (nodeId != null) {
				List<Integer> idList = this.getMailTreeService().findChildrenIds(nodeId);
				if (isShowChildMail != null && isShowChildMail.equals("0")) {
					queryString.append(" and obj.nodeId = ?");
					queryParams.add(nodeId);
				} else if (idList.size() > 0) {
					queryString.append(" and obj.nodeId in(");
					for (int i = 0; i < idList.size() - 1; i++) {
						queryString.append("?,");
						queryParams.add(idList.get(i));
					}
					queryString.append("?)");
					queryParams.add(idList.get(idList.size() - 1));
				} else {
					response.getWriter().write("{'data':[],'totalCount':0}");
					return null;
				}
			}
			List<Integer> countList = this.getCotBaseDao().queryForLists("select count(*) from CotMail obj" + queryString, queryParams.toArray());
			int count = countList.get(0);
			logger.debug("sort:" + sortStr + ",dir:" + dirStr);
			if (sortStr != null) {
				if (sortStr.equals("groupDate")) {
					sortStr = "sendTime";
				} else if (sortStr.equals("sender")) {
					sortStr = "sendUrl";
				}
			}
			if (sortStr == null)
				sortStr = "sendTime";
			if (dirStr == null)
				dirStr = "DESC";
			queryString.append(" order by obj." + sortStr + " " + dirStr);

			int start = Integer.parseInt(startStr);
			int limit = Integer.parseInt(limitStr);
			// 设置起始
			start = start < 0 ? 0 : start;
			// 设置每页显示多少行
			limit = limit <= 0 ? 20 : limit;
			logger.debug("start:" + start + ",limit:" + limit + ",sort:" + sortStr + ",dir:" + dirStr);

			StringBuffer selHql = new StringBuffer("select obj.id,obj.custId,obj.empId,obj.nodeId,");
			selHql.append("obj.sendName,obj.sendUrl,obj.toName,obj.toUrl,obj.ccName,obj.ccUrl,");
			selHql.append("obj.bccName,obj.bccUrl,obj.addTime,obj.mailStatus,obj.mailType,obj.errMessage,");
			selHql.append("obj.subject,obj.isContainAttach,obj.sendTime,obj.size");
			selHql.append(" from CotMail obj");
			// 根据起始
			List<Object[]> list = this.getCotBaseDao().queryForLists(selHql.toString() + queryString, start, limit, queryParams.toArray());
			System.out.println(list.size());
			List<CotMail> cotMailList = new ArrayList<CotMail>();
			for (Object[] objs : list) {
				cotMailList.add(MailEntityConverUtil.toCotMail(objs));
			}
			GridServerHandler grid = new GridServerHandler();
			grid.setTotalCount(count);
			grid.setData(cotMailList);
			json = grid.getLoadResponseText();
			response.getWriter().write(json);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return null;
	}
	
	//查询厂家报价记录
	public ActionForward queryPriceFac(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String start = request.getParameter("start");
		String limit = request.getParameter("limit");
		if(start == null || limit == null)
			return mapping.findForward("queryPriceFac");
		
		String facId = request.getParameter("factoryId");
		String eleId = request.getParameter("eleIdFind");
		String startTime = request.getParameter("startTime");
		String endTime = request.getParameter("endTime");
		StringBuffer queryString=new StringBuffer();
		queryString.append(" where p.eleId=obj.id and p.facId=f.id");
		
		if (facId != null && !facId.toString().equals("")) {
			queryString.append(" and p.facId="+ facId.toString());
		}
		if (eleId != null && !eleId.toString().equals("")) {
			queryString.append(" and obj.eleId like '%"+ eleId.toString()+"%'");
		}
		if (startTime != null && !"".equals(startTime.trim())) {
			queryString.append(" and p.addTime >='" + startTime + "'");
		}
		if (endTime != null && !"".equals(endTime.trim())) {
			queryString.append(" and p.addTime <='" + endTime
					+ " 23:59:59'");
		}
		
		//设置查询参数
		QueryInfo queryInfo = new QueryInfo();
		//设置每页显示多少行
		int pageCount = Integer.parseInt(limit);
		//设定每页显示记录数
		queryInfo.setCountOnEachPage(pageCount);
		// 设置查询记录总数语句
		queryInfo.setCountQuery("select count(*) from CotPriceFac p,CotElementsNew obj,CotFactory f"+ queryString);
		// 查询语句
		queryInfo.setSelectString("select p.id," +
				"obj.eleId," +
				"obj.eleName," +
				"p.addTime," +
				"p.priceFac," +
				"p.priceUint," +
				"f.shortName," +
				"p.priceRemark" +
				" from CotPriceFac p,CotElementsNew obj,CotFactory f");
		 
		//设置条件语句
		queryInfo.setQueryString(queryString.toString());
		//设置排序语句
		queryInfo.setOrderString(" order by p.addTime desc");
		int count = this.getCotFactoryService().getRecordCount(queryInfo);
		//得到limit对象
		int startIndex = Integer.parseInt(start);	
		queryInfo.setStartIndex(startIndex);
		GridServerHandler gd = new GridServerHandler();
		List res = this.getCotFactoryService().getPriceFacVO(queryInfo);
		gd.setData(res);
		gd.setTotalCount(count);
		try {
			response.getWriter().write(gd.getLoadResponseText());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

}
