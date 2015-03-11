package com.sail.cot.action.mail;

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
import com.sail.cot.dao.CotBaseDao;
import com.sail.cot.domain.CotEmps;
import com.sail.cot.domain.CotMail;
import com.sail.cot.domain.vo.CotAutoCompleteVO;
import com.sail.cot.domain.vo.HistoryMailAttachVO;
import com.sail.cot.domain.vo.HistoryMailVO;
import com.sail.cot.email.service.MailCfgService;
import com.sail.cot.email.service.MailLocalService;
import com.sail.cot.email.service.MailTreeService;
import com.sail.cot.email.util.Constants;
import com.sail.cot.email.util.MailEntityConverUtil;
import com.sail.cot.util.GridServerHandler;
import com.sail.cot.util.Log4WebUtil;
import com.sail.cot.util.SystemUtil;

public class CotMailAction extends AbstractAction {
	private Logger logger = Log4WebUtil.getLogger(CotMailAction.class);
	private MailCfgService mailCfgService;
	public MailCfgService getMailCfgService(){
		if(mailCfgService == null){
			mailCfgService = (MailCfgService)super.getBean("MailCfgService");
		}
		return mailCfgService;
	}
	private MailLocalService mailLocalService;
	public MailLocalService getMailLocalService(){
		if(mailLocalService == null){
			mailLocalService = (MailLocalService)super.getBean("MailLocalService");
		}
		return mailLocalService;
	}
	private MailTreeService mailTreeService;
	public MailTreeService getMailTreeService(){
		if(mailTreeService == null){
			mailTreeService = (MailTreeService)super.getBean("MailTreeService");
		}
		return mailTreeService;
	}
	private CotBaseDao cotBaseDao;
	public CotBaseDao getCotBaseDao(){
		if(cotBaseDao == null){
			cotBaseDao = (CotBaseDao)super.getBean("CotBaseDao");
		}
		return cotBaseDao;
	}
	
	@Override
	public ActionForward add(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		CotEmps emp = (CotEmps) request.getSession().getAttribute("emp");
		request.setAttribute("emp", emp);
		return mapping.findForward("add");
	}         

	@Override
	public ActionForward modify(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		return mapping.findForward("modify");
	}
	
	public ActionForward setMail(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		CotEmps emp = (CotEmps) request.getSession().getAttribute("emp");
		request.setAttribute("emp", emp);
		return mapping.findForward("setMail");
	}
	
	@Override
	public ActionForward query(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		logger.info("访问收件箱页面");
		return mapping.findForward("querySuccess");
	}
	@Override
	public ActionForward remove(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		return null;
	}
	@SuppressWarnings("unchecked")
	public ActionForward readComeAndGoMails(ActionMapping mapping,ActionForm form,
			HttpServletRequest request,HttpServletResponse response){
		logger.info("读取历史邮件");
		String startStr = request.getParameter("start");
		String limitStr = request.getParameter("limit");
		String mailUrl = request.getParameter("mailUrl");
		String empId = request.getParameter("empId");
		String seachName = request.getParameter("seachName");
		String json;
		try {
			StringBuffer queryString = new StringBuffer();
			List queryParams = new ArrayList();
			if(empId==null||empId.trim().equals("null")){ // 所有往来邮件
				queryString.append(" where 1=1");
			}else if(empId.equals("")){	// 公共邮箱
				queryString.append(" where obj.empId is null");
			}else{	// 个人邮箱
				queryString.append(" where obj.empId = ?");
				queryParams.add(Integer.parseInt(empId));
			}
			if(mailUrl!=null&&!mailUrl.trim().equals("")){
				mailUrl = "%"+mailUrl+"%";
				// 接收历史
				queryString.append(" and ((obj.sendUrl like ?");
				queryParams.add(mailUrl);
				queryString.append(" or obj.sendName like ?)");
				queryParams.add(mailUrl);
				queryString.append(" and obj.mailType = ?");
				queryParams.add(Constants.MAIL_LOCAL_TYPE_INBOX);
				// 发件历史
				queryString.append(" or (obj.toUrl like ?");
				queryParams.add(mailUrl);
				queryString.append(" or obj.toName like ?");
				queryParams.add(mailUrl);
				queryString.append(" or obj.ccUrl like ?");
				queryParams.add(mailUrl);
				queryString.append(" or obj.ccName like ?");
				queryParams.add(mailUrl);
				queryString.append(" or obj.bccUrl like ?");
				queryParams.add(mailUrl);
				queryString.append(" or obj.bccName like ?)");
				queryParams.add(mailUrl);
				queryString.append(" and obj.mailType = ?)");
				queryParams.add(Constants.MAIL_LOCAL_TYPE_SEND);
			}
			if(seachName!=null&&!seachName.trim().equals("")){
				queryString.append(" and obj.subject like ?");
				queryParams.add("%"+seachName+"%");
			}
			List<Integer> countList = this.getCotBaseDao().queryForLists("select count(*) from CotMail obj" +queryString, queryParams.toArray());
			int count = countList.get(0);
			
			queryString.append(" and obj.mailType <> ?");
			queryParams.add(Constants.MAIL_LOCAL_TYPE_DRAFT);
			queryString.append(" order by obj.sendTime desc");
			
			int start = Integer.parseInt(startStr);
			int limit = Integer.parseInt(limitStr);
			// 设置起始
			start = start < 0 ? 0 :start;
			// 设置每页显示多少行
			limit = limit <=0 ? 20:limit;
			StringBuffer selHql = new StringBuffer("select obj.id,obj.mailType,");
			selHql.append("obj.subject,obj.isContainAttach,obj.sendTime");
			selHql.append(" from CotMail obj");
			// 根据起始
			List<HistoryMailVO> cotMailList = this.getMailLocalService().getHistoryMail(selHql.toString()+queryString, queryParams.toArray(), start, limit);
			
			GridServerHandler grid = new GridServerHandler();
			grid.setTotalCount(count);
			grid.setData(cotMailList);
			json = grid.getLoadResponseText();
			response.getWriter().write(json);
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
		return null;
	}
	@SuppressWarnings("unchecked")
	public ActionForward readComeAndGoAttachs(ActionMapping mapping,ActionForm form,
			HttpServletRequest request,HttpServletResponse response){
		logger.info("读取历史附件");
		String startStr = request.getParameter("start");
		String limitStr = request.getParameter("limit");
		String mailUrl = request.getParameter("mailUrl");
		String empId = request.getParameter("empId");
		String seachName = request.getParameter("seachName");
		String json;
		
		try {
			StringBuffer queryString = new StringBuffer();
			List queryParams = new ArrayList();
			if(empId==null||empId.trim().equals("null")){ // 所有往来邮件
				queryString.append(" where 1=1");
			}else if(empId.equals("")){	// 公共邮箱
				queryString.append(" where obj.empId is null");
			}else{	// 个人邮箱
				queryString.append(" where obj.empId = ?");
				queryParams.add(Integer.parseInt(empId));
			}
			if(mailUrl!=null&&!mailUrl.trim().equals("")){
				queryString.append(" and obj.custUrl like ?");
				queryParams.add("%"+mailUrl+"%");
			}
			if(seachName!=null&&!seachName.trim().equals("")){
				queryString.append(" and obj.name like ?");
				queryParams.add("%"+seachName+"%");
			}
			
			List<Integer> countList = this.getCotBaseDao().queryForLists("select count(*) from CotMailAttach obj" +queryString, queryParams.toArray());
			int count = countList.get(0);
			
			queryString.append(" and obj.mailType <> ?");
			queryParams.add(Constants.MAIL_LOCAL_TYPE_DRAFT);
			queryString.append(" order by obj.sendTime desc");
			
			int start = Integer.parseInt(startStr);
			int limit = Integer.parseInt(limitStr);
			// 设置起始
			start = start < 0 ? 0 :start;
			// 设置每页显示多少行
			limit = limit <=0 ? 20:limit;
			// 根据起始
			StringBuffer selHql = new StringBuffer("select obj.id,obj.mailId,obj.mailType,");
			selHql.append("obj.name,obj.sendTime,obj.size,obj.url");
			selHql.append(" from CotMailAttach obj");
			// 根据起始
			List<HistoryMailAttachVO> cotMailList = this.getMailLocalService().getHistoryAttach(selHql.toString()+queryString, queryParams.toArray(), start, limit);
			
			GridServerHandler grid = new GridServerHandler();
			grid.setTotalCount(count);
			grid.setData(cotMailList);
			json = grid.getLoadResponseText();
			response.getWriter().write(json);
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
		return null;
	}
	/**
	 * 根据节点ID读取本地邮件分页邮件
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public ActionForward readLocalMails(ActionMapping mapping,ActionForm form,
			HttpServletRequest request,HttpServletResponse response){
		logger.debug("读取本地邮件");
		String startStr = request.getParameter("start");
		String limitStr = request.getParameter("limit");
		String sortStr = request.getParameter("sort");
		String dirStr = request.getParameter("dir");
			
		String nodeIdStr = request.getParameter("nodeId");
		String empId = request.getParameter("empId");
		String custId = request.getParameter("custId");
		String mailType = request.getParameter("mailType");
		String query = request.getParameter("query");
		
		String isShowChildMail = request.getParameter("isShowChildMail");
		String isCheckMail = request.getParameter("isCheckMail");
		
		logger.debug("sort:"+sortStr+",dir:"+dirStr);
		if(sortStr!=null){
			if(sortStr.equals("sender")){
				sortStr = "sendUrl";
			}
		}
		if(sortStr==null)
			sortStr = "sendTime";
		if(dirStr==null)
			dirStr = "DESC";
		
		String json;
		
		try {
			StringBuffer queryString = new StringBuffer();
			List queryParams = new ArrayList();
			Integer nodeId = null;
			try {
				nodeId = Integer.parseInt(nodeIdStr);
			} catch (Exception e) {
			}
			if(empId==null||empId.trim().equals("null")){ // 所有往来邮件
				queryString.append(" where 1=1");
			}else if(empId.equals("")){	// 公共邮箱
				queryString.append(" where obj.empId is null");
			}else{	// 个人邮箱
				queryString.append(" where obj.empId = ?");
				queryParams.add(Integer.parseInt(empId));
			}
			if(custId!=null&&!custId.equals("")){
				queryString.append(" and obj.custId = ?");
				queryParams.add(Integer.parseInt(custId));
			}
			if(mailType!=null&&!mailType.equals("")){
				queryString.append(" and obj.mailType = ?");
				queryParams.add(Integer.parseInt(mailType));
			}
			try {
				JSONObject jObject = JSONObject.fromObject(query);
				try {
					String status = jObject.getString("status");
					if (status != null && !status.trim().equals("")) {
						if(status.equals("0")){
						}else if (status.equals("1"))
							queryString.append(" and obj.mailStatus <= 1 ");
						else if(status.equals("-9")){
							queryString.append(" and (obj.mailStatus = ? or obj.mailStatus = ?)");
							queryParams.add(Constants.MAIL_LOCAL_STATUS_WAITONCHECK);
							queryParams.add(Constants.MAIL_LOCAL_STATUS_PARTINGWAITONCHECK);
						}else if(status.equals("-8")) {
							queryString.append(" and (obj.mailStatus = ? or obj.mailStatus = ?)");
							queryParams.add(Constants.MAIL_LOCAL_STATUS_CHECKNOTGO);
							queryParams.add(Constants.MAIL_LOCAL_STATUS_PARTINGCHECKNOTGO);
						}else {
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
						queryParams.add("%" + jObject.getString("subject")
								+ "%");
					}
				} catch (Exception e) {
				}
				try {
					String startDateStr = (String) jObject.get("startDate");
					if (startDateStr != null && !startDateStr.trim().equals("")) {
						queryString.append(" and obj."+(sortStr.equals("sendTime")?"sendTime":"addTime")+" >= '"
								+ startDateStr + "'");
					}
				} catch (Exception e) {
				}
				try {
					String endDateStr = (String) jObject.get("endDate");
					if (endDateStr != null && !endDateStr.trim().equals("")) {
						queryString.append(" and obj."+(sortStr.equals("sendTime")?"sendTime":"addTime")+" <= '"
								+ endDateStr + "'");
					}
				} catch (Exception e) {
				}
			} catch (Exception e) {
			}
			if(nodeId!=null){
				List<Integer> idList = this.getMailTreeService().findChildrenIds(nodeId);
				if(isShowChildMail!=null&&isShowChildMail.equals("0")){
					queryString.append(" and obj.nodeId = ?");
					queryParams.add(nodeId);
				}else if(idList.size()>0){
					queryString.append(" and obj.nodeId in(");
					for (int i = 0; i < idList.size()-1; i++) {
						queryString.append("?,");
						queryParams.add(idList.get(i));
					}
					queryString.append("?)");
					queryParams.add(idList.get(idList.size()-1));
				}else{
					response.getWriter().write("{'data':[],'totalCount':0}");
					return null;
				}
			}
			if(isCheckMail!=null&&isCheckMail.equals("true")){
				queryString.append(" and obj.mailType = ?");
				queryParams.add(Constants.MAIL_LOCAL_TYPE_CHECK);
				queryString.append(" and (obj.mailStatus = ? or obj.mailStatus = ?)");
				queryParams.add(Constants.MAIL_LOCAL_STATUS_WAITONCHECK);
				queryParams.add(Constants.MAIL_LOCAL_STATUS_PARTINGWAITONCHECK);
				CotEmps emp = (CotEmps) request.getSession().getAttribute("emp");
				if(!"admin".equals(emp.getEmpsId())){
					queryString.append(" and obj.checkEmpId = ?");
					queryParams.add(emp.getId());
				}
			}
			List<Integer> countList = this.getCotBaseDao().queryForLists("select count(*) from CotMail obj" +queryString, queryParams.toArray());
			int count = countList.get(0);
			
			queryString.append(" order by obj."+sortStr+" "+dirStr);
			
			int start = Integer.parseInt(startStr);
			int limit = Integer.parseInt(limitStr);
			// 设置起始
			start = start < 0 ? 0 :start;
			// 设置每页显示多少行
			limit = limit <=0 ? 20:limit;
			logger.debug("start:"+start+",limit:"+limit+",sort:"+sortStr+",dir:"+dirStr);
			
			StringBuffer selHql = new StringBuffer("select obj.id,obj.custId,obj.empId,obj.nodeId,");
			selHql.append("obj.sendName,obj.sendUrl,obj.toName,obj.toUrl,obj.ccName,obj.ccUrl,");
			selHql.append("obj.bccName,obj.bccUrl,obj.addTime,obj.mailStatus,obj.mailType,obj.errMessage,");
			selHql.append("obj.subject,obj.isContainAttach,obj.sendTime,obj.size,obj.mailTag,obj.facId,obj.isNotification");
			selHql.append(" from CotMail obj");
			// 根据起始
			List<Object []> list = this.getCotBaseDao().queryForLists(selHql.toString()+queryString,start,limit, queryParams.toArray());
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
			logger.error(e.getMessage(),e);
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public ActionForward readAutoCompletMails(ActionMapping mapping,ActionForm form,
			HttpServletRequest request,HttpServletResponse response){
		logger.debug("读取自动补全邮件");
		String startStr = request.getParameter("start");
		String limitStr = request.getParameter("limit");
		String nodeIdStr = request.getParameter("nodeId");
		String empId = request.getParameter("empId");
		String query = request.getParameter("query");
		String json;
		
		String isShowChildMail = request.getParameter("isShowChildMail");
		String isCheckMail = request.getParameter("isCheckMail");
		
		try {
			StringBuffer queryString = new StringBuffer();
			List queryParams = new ArrayList();
			Integer nodeId = null;
			try {
				nodeId = Integer.parseInt(nodeIdStr);
			} catch (Exception e) {
			}
			if(empId==null||empId.trim().equals("null")){ // 所有往来邮件
				queryString.append(" where 1=1");
			}else if(empId.equals("")){	// 公共邮箱
				queryString.append(" where obj.empId is null");
			}else{	// 个人邮箱
				queryString.append(" where obj.empId = ?");
				queryParams.add(Integer.parseInt(empId));
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
							queryParams.add(status);
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
						queryParams.add("%" + jObject.getString("subject")
								+ "%");
					}
				} catch (Exception e) {
				}
				try {
					String startDateStr = (String) jObject.get("startDate");
					if (startDateStr != null && !startDateStr.trim().equals("")) {
						queryString.append(" and obj.sendTime >= ?");
						queryParams.add(startDateStr);
					}
				} catch (Exception e) {
				}
				try {
					String endDateStr = (String) jObject.get("endDate");
					if (endDateStr != null && !endDateStr.trim().equals("")) {
						queryString.append(" and obj.sendTime <= ?");
						queryParams.add(endDateStr);
					}
				} catch (Exception e) {
				}
			} catch (Exception e) {
			}
			if(nodeId!=null){
				List<Integer> idList = this.getMailTreeService().findChildrenIds(nodeId);
				if(isShowChildMail!=null&&isShowChildMail.equals("0")){
					queryString.append(" and obj.nodeId = ?");
					queryParams.add(nodeId);
				}else if(idList.size()>0){
					queryString.append(" and obj.nodeId in(");
					for (int i = 0; i < idList.size()-1; i++) {
						queryString.append("?,");
						queryParams.add(idList.get(i));
					}
					queryString.append("?)");
					queryParams.add(idList.get(idList.size()-1));
				}else{
					response.getWriter().write("{'data':[],'totalCount':0}");
					return null;
				}
			}
			if(isCheckMail!=null&&isCheckMail.equals("true")){
				queryString.append(" and obj.mailType = ?");
				queryParams.add(Constants.MAIL_LOCAL_TYPE_CHECK);
				queryString.append(" and (obj.mailStatus = ? or obj.mailStatus = ?)");
				queryParams.add(Constants.MAIL_LOCAL_STATUS_WAITONCHECK);
				queryParams.add(Constants.MAIL_LOCAL_STATUS_PARTINGWAITONCHECK);
				CotEmps emp = (CotEmps) request.getSession().getAttribute("emp");
				if(!"admin".equals(emp.getEmpsId())){
					queryString.append(" and obj.checkEmpId = ?");
					queryParams.add(emp.getId());
				}
			}
			
			List<Integer> countList = this.getCotBaseDao().queryForLists("select count(distinct obj.subject) from CotMail obj" +queryString, queryParams.toArray());
			int count = countList.get(0);
			
			queryString.append(" order by obj.sendTime desc");
			
			int start = Integer.parseInt(startStr);
			int limit = Integer.parseInt(limitStr);
			// 设置起始
			start = start < 0 ? 0 :start;
			// 设置每页显示多少行
			limit = limit <=0 ? 20:limit;
			
			// 根据起始
			List<String> list = this.getCotBaseDao().queryForLists("select distinct obj.subject from CotMail obj"+queryString,start,limit, queryParams.toArray());
			List<CotAutoCompleteVO> aList = new ArrayList<CotAutoCompleteVO>();
			for (String name : list) {
				aList.add(new CotAutoCompleteVO(name));
			}
			GridServerHandler grid = new GridServerHandler();
			grid.setTotalCount(count);
			grid.setData(aList);
			json = grid.getLoadResponseText();
			response.getWriter().write(json);
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
		return null;
	}
	
	//查询员工邮件列表树
	@SuppressWarnings("unchecked")
	public ActionForward queryMailTree(ActionMapping mapping,ActionForm form,
			HttpServletRequest request,HttpServletResponse response){
		logger.debug("查询员工邮件列表树");
		String empId = request.getParameter("empId");
		try {
			StringBuffer queryString = new StringBuffer(); 
			CotEmps cotEmps = (CotEmps) request.getSession().getAttribute("emp");
			queryString.append(" from CotMailTree as obj where 1=1");
			if("admin".equals(cotEmps.getEmpsId())){ // 管理员查看所有
			}else{
				if(SystemUtil.isAction(request, "cotmail.do", "ALL")){ // 查看公司邮箱
					queryString.append(" and (obj.flag = null or obj.flag not like 'A%' and obj.flag <> 'G')");
				}else if(SystemUtil.isAction(request, "cotmail.do", "DEPT")&&cotEmps.getDeptId()!=null){ // 查看部门邮件
					String hql = "select obj.id from CotEmps obj where obj.deptId = "+cotEmps.getDeptId();
					List<Integer> ids = this.getCotBaseDao().find(hql);
					queryString.append(" and obj.empId in("+ids.get(0));
					for (int i = 1; i < ids.size(); i++) {
						queryString.append(","+ids.get(i));
					}
					queryString.append(")");
				}else if(SystemUtil.isAction(request, "cotmail.do", "MINE")){ // 查看个人邮件
					queryString.append(" and obj.empId =" +cotEmps.getId());
				}else{
					queryString.append(" and 1<>1");
				}
				if(SystemUtil.isAction(request, "cotmail.do", "PUBLIC")){	// 查看公共邮箱
					queryString.append(" or obj.flag = 'G'");
				}
				if(SystemUtil.isAction(request, "cotmail.do", "ALLMAIL")){	// 查看邮件列表
					queryString.append(" or obj.flag like 'A%'");
				}				
			}
			if(empId!=null&&!empId.trim().equals("")) // 
				queryString.append(" and obj.empId = "+empId);
			//按节点名称排序
			queryString.append(" order by obj.nodeName");
			
			String jsonTree = this.getMailTreeService().getMailTree(queryString.toString());
			response.getWriter().write(jsonTree);
		} catch (Exception e) {
			logger.error("查询员工邮件列表树出错："+e.getMessage(),e);
		}
		return null;
	}
	public ActionForward printcontent(ActionMapping mapping,ActionForm form,
			HttpServletRequest request,HttpServletResponse response){
		
		return mapping.findForward("printContent");
	}
	public ActionForward queryEmpsSign(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		logger.info("访问签名页面");
		CotEmps emp = (CotEmps) request.getSession().getAttribute("emp");
		request.setAttribute("empsId", emp.getId());
		return mapping.findForward("querySign");
	}
}
