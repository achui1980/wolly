package com.sail.cot.service.worklog.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.sql.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;
import org.directwebremoting.WebContext;
import org.directwebremoting.WebContextFactory;
import com.jason.core.exception.DAOException;
import com.sail.cot.dao.CotBaseDao;
import com.sail.cot.domain.CotEmps;
import com.sail.cot.domain.CotMessage;
import com.sail.cot.domain.CotMsgType;
import com.sail.cot.query.QueryInfo;
import com.sail.cot.service.worklog.CotMessageService;
import com.sail.cot.util.Log4WebUtil;

public class CotMessageServiceImpl implements CotMessageService {


	private CotBaseDao cotBaseDao;
	public CotBaseDao getCotBaseDao() {
		return cotBaseDao;
	}

	public void setCotBaseDao(CotBaseDao cotBaseDao) {
		this.cotBaseDao = cotBaseDao;
	}
	
	private Logger logger = Log4WebUtil.getLogger(CotMessageServiceImpl.class);
	
	public void addMessage(List MessageList) {
		// TODO Auto-generated method stub
		try {
			this.getCotBaseDao().saveRecords(MessageList);
		} catch (DAOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error("添加系统信息异常",e);
		}
	}
	//保存消息单
	public Integer saveMessage(CotMessage cotMessage,String url, String msgBeginDate, String msgEndDate){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat mdf = new SimpleDateFormat("yyyyMMddhhmmss");
		cotMessage.setAddTime(new Date(System.currentTimeMillis()));// 添加时间
		WebContext ctx = WebContextFactory.get();
		CotEmps cotEmps = (CotEmps) ctx.getSession().getAttribute("emp");
		cotMessage.setMsgFromPerson(cotEmps.getId());// 发送人
		Date date = new Date(System.currentTimeMillis());
		cotMessage.setMsgOrderNo("MG-"+mdf.format(date));
//		if (cotMessage.getMsgTypeId() == 4) {
//			cotMessage.setMsgStatus(4L);
//		}else {
			cotMessage.setMsgStatus(0L);
//		}
		cotMessage.setMsgType("recv");
		cotMessage.setMsgTable("CotMessage");
		cotMessage.setMsgAction(url+"&No="+"MG-"+mdf.format(date));
		
		List<CotMessage> records = new ArrayList<CotMessage>();
		
		//保存消息单
		try {
			if (msgBeginDate != null && !"".equals(msgBeginDate)) {
				cotMessage.setMsgBeginDate(new Date(sdf.parse(msgBeginDate).getTime()));
			}
			if (msgEndDate != null && !"".equals(msgEndDate)) {
				cotMessage.setMsgEndDate(new Date(sdf.parse(msgEndDate).getTime()));
			}
			
			records.add(cotMessage);
			this.getCotBaseDao().saveRecords(records);
		} catch (Exception e) {
			logger.error("保存消息单出错！");
			return null;
		}
		return cotMessage.getId();
	}
	//保存消息单
	public Integer updateMessage(CotMessage cotMessage, String msgBeginDate, String msgEndDate){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		List<CotMessage> res = new ArrayList<CotMessage>();
		res = cotBaseDao.find("from CotMessage obj where obj.id ="+cotMessage.getId());
		CotMessage message = res.get(0);
		cotMessage.setMsgOrderNo(message.getMsgOrderNo());
		cotMessage.setMsgTypeId(message.getMsgTypeId());
//		cotMessage.setMsgStatus(message.getMsgStatus());
		cotMessage.setMsgFromPerson(message.getMsgFromPerson());
		cotMessage.setMsgToPerson(message.getMsgToPerson());
		cotMessage.setAddTime(message.getAddTime());
		cotMessage.setMsgAction(message.getMsgAction());
		cotMessage.setMsgTable(message.getMsgTable());
		if (cotMessage.getMsgStatus() ==2) {
			cotMessage.setMsgFlag(0L);
		}else if (cotMessage.getMsgStatus()==1) {
			cotMessage.setMsgFlag(1L);
		}else {
			cotMessage.setMsgFlag(message.getMsgFlag());
		}
		
		List<CotMessage> records = new ArrayList<CotMessage>();
		
		// //保存消息单
		try {
			if (msgBeginDate != null && !"".equals(msgBeginDate)) {
				cotMessage.setMsgBeginDate(new Date(sdf.parse(msgBeginDate).getTime()));
			}
			if (msgEndDate != null && !"".equals(msgEndDate)) {
				cotMessage.setMsgEndDate(new Date(sdf.parse(msgEndDate).getTime()));
			}
			
			records.add(cotMessage);
			this.getCotBaseDao().updateRecords(records);
		} catch (Exception e) {
			logger.error("保存消息单出错！");
			return null;
		}
		return cotMessage.getId();
	}
	public int deleteMessage(List MessageList) {
		List<Integer> ids=new ArrayList<Integer>();
        int res = 0;
		for (int i = 0; i < MessageList.size(); i++) {
			CotMessage cotMessage = (CotMessage) MessageList.get(i);
			ids.add(cotMessage.getId());
		}
		try {
			this.getCotBaseDao().deleteRecordByIds(ids, "CotMessage");
		} catch (DAOException e) {
			e.printStackTrace();
			logger.error("删除系统信息异常",e);
			res = -1;
		}
		return res;
	}

	public boolean findExistByNo(String no) {
		QueryInfo queryInfo = new QueryInfo();
		boolean isExist = false;
		queryInfo.setSelectString("from CotMessage obj where obj.msgOrderNo='"+no+"'");
		queryInfo.setCountQuery("select count(*) from CotMessage obj where obj.msgOrderNo='"+no+"'");
		try {
			int count = this.getCotBaseDao().getRecordsCount(queryInfo);
			if(count>0)
			{
				isExist = true;
			}
		} catch (DAOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error("查找重复方法失败", e);
		}
		return isExist;
	}
	public CotMessage getMessageById(Integer Id) {
		// TODO Auto-generated method stub
		return (CotMessage) this.getCotBaseDao().getById(CotMessage.class, Id);
	}

	public List getMessageList() {
		// TODO Auto-generated method stub
		return this.getCotBaseDao().getRecords("CotMessage");
	}

	public boolean modifyMessage(List MessageList) {
		// TODO Auto-generated method stub
		for (int i = 0; i < MessageList.size(); i++) {
			CotMessage cotMessage = (CotMessage)MessageList.get(i);
			Integer id = this.isExistMessageId(cotMessage.getMsgOrderNo());
			if (id!=null&& !id.toString().equals(cotMessage.getId().toString())) {
				return false;
			}
		}
		
		try {
			this.getCotBaseDao().updateRecords(MessageList);
		} catch (Exception e) {
			// TODO: handle exception
			logger.error("更新系统信息异常", e);
		}
		return true;
	}

	public List getList(QueryInfo queryInfo) {
		// TODO Auto-generated method stub
		try {
			return this.getCotBaseDao().findRecords(queryInfo);
		} catch (DAOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public int getRecordCount(QueryInfo queryInfo) {
		// TODO Auto-generated method stub
		try {
			return this.getCotBaseDao().getRecordsCount(queryInfo);
		} catch (DAOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}

	public Integer isExistMessageId(String msgOrderNo) {
		// TODO Auto-generated method stub
		List<Integer> res = new ArrayList<Integer>();
		res = cotBaseDao.find("select c.id from CotMessage c where c.msgOrderNo='"+msgOrderNo+"'");
		if (res.size()!=1) {
			return null;
		}else {
			return res.get(0);
		}
	}
	//查询所有消息类型
	public Map<?, ?> getMsgTypeMap(){
		Map<String, String> map = new HashMap<String, String>();
		List<?> list = this.getCotBaseDao().getRecords("CotMsgType");
		for (int i = 0; i < list.size(); i++) {
			CotMsgType cotMsgType = (CotMsgType) list.get(i);
			map.put(cotMsgType.getId().toString(), cotMsgType.getMsgTypeName());
		}
		return map;
	}
	//查询所有emps
	public Map<?, ?> getEmpsMap(){
		Map<String, String> map = new HashMap<String, String>();
		List<?> list = this.getCotBaseDao().getRecords("CotEmps");
		for (int i = 0; i < list.size(); i++) {
			CotEmps cotEmps = (CotEmps) list.get(i);
			map.put(cotEmps.getId().toString(), cotEmps.getEmpsName());
		}
		return map;
	}
	// 得到objName的集合
	public List<?> getList(String objName) {
		return this.getCotBaseDao().getRecords(objName);
	}
	public CotEmps getEmpById(Integer id) {
		// TODO Auto-generated method stub
		CotEmps cotEmps = (CotEmps) this.getCotBaseDao().getById(
				CotEmps.class, id);
		return cotEmps;
	}
	//获取未读消息条数
	public Integer getUnReadMessage(){
		HttpSession session =  WebContextFactory.get().getSession();
		CotEmps emp = (CotEmps)session.getAttribute("emp");
		List<CotMessage> res = new ArrayList<CotMessage>();
		if(emp==null){
			return 0;
		}
		res = cotBaseDao.find("from CotMessage obj where obj.msgToPerson =" +emp.getId()+" and obj.msgStatus=0 and obj.msgFlag =1");
		if (res == null) {
			return 0;
		}else {
			return res.size();
		}
	}
	//获取未处理消息条数
	public Integer getUnHandleMessage(){
		HttpSession session =  WebContextFactory.get().getSession();
		CotEmps emp = (CotEmps)session.getAttribute("emp");
		List<CotMessage> res = new ArrayList<CotMessage>();
		res = cotBaseDao.find("from CotMessage obj where obj.msgToPerson =" +emp.getId()+" and obj.msgStatus=1");
		if (res == null) {
			return 0;
		}else {
			return res.size();
		}
	}
	//获取未处理,提醒已结束消息条数
	public Integer getOverMessage(){
		HttpSession session =  WebContextFactory.get().getSession();
		CotEmps emp = (CotEmps)session.getAttribute("emp");
		List<CotMessage> res = new ArrayList<CotMessage>();
		res = cotBaseDao.find("from CotMessage obj where obj.msgToPerson =" +emp.getId()+" and obj.msgStatus=3");
		if (res == null) {
			return 0;
		}else {
			return res.size();
		}
	}
	//取消时修改消息状态为已读，未处理
	public void updateMsgStatus(Integer id) {
		HttpSession session = WebContextFactory.get().getSession();
		CotEmps emp = (CotEmps) session.getAttribute("emp");
		List<CotMessage> res = new ArrayList<CotMessage>();
		res = cotBaseDao.find("from CotMessage obj where obj.id =" +id);
		CotMessage cotMessage = (CotMessage)res.get(0);
		if (((cotMessage.getMsgToPerson()).toString()).equals((emp.getId().toString()))) {
			if (cotMessage.getMsgStatus() ==3 || cotMessage.getMsgStatus()==2) {
				cotMessage.setMsgStatus(cotMessage.getMsgStatus());
			}else {
				cotMessage.setMsgStatus(1L);
			}
		}
		this.getCotBaseDao().update(cotMessage);
	}
	//判断消息是否已经提醒结束
	public void modifyMsgStatus(){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date date = new Date(System.currentTimeMillis());
		String curDate = sdf.format(date);
		String hql = "from CotMessage obj where (obj.msgStatus =1 or obj.msgStatus = 0) and obj.msgEndDate <'"+curDate+"'";
		List<?> details = this.getCotBaseDao().find(hql);
		Iterator<?> it = details.iterator();
		while (it.hasNext()) {
			CotMessage cotMessage = (CotMessage) it.next();
			cotMessage.setMsgStatus(3L);
			cotMessage.setMsgFlag(0L);
			this.getCotBaseDao().update(cotMessage);
		}
	}
	//判断接收者是否为当前用户
	public boolean recvIsCurUser(Integer id) {
		HttpSession session = WebContextFactory.get().getSession();
		CotEmps emp = (CotEmps)session.getAttribute("emp");
		List<CotMessage> res = new ArrayList<CotMessage>();
		res = cotBaseDao.find("from CotMessage obj where obj.id =" +id);
		CotMessage cotMessage = (CotMessage)res.get(0);		
		if ((emp.getId()).toString().equals((cotMessage.getMsgToPerson()).toString())) {
			return true;
		}else{
			return false;
		}
	}
	//批量删除时判断发送者是否全为当前用户而且消息状态是否都已处理
	public boolean checkIsCurUser(List messageList){
		Boolean flag = true;
		HttpSession session = WebContextFactory.get().getSession();
		CotEmps emp = (CotEmps)session.getAttribute("emp");
		for (int i = 0; i < messageList.size(); i++) {
			CotMessage cotMessage = (CotMessage) messageList.get(i);
			List<CotMessage> res = new ArrayList<CotMessage>();
			res = cotBaseDao.find("from CotMessage obj where obj.id =" +cotMessage.getId());
			CotMessage selecedMessage = (CotMessage)res.get(0);
			if (selecedMessage.getMsgFromPerson().toString().equals( emp.getId().toString())){
				if (selecedMessage.getMsgStatus() < 2) {
					flag = false;
					break;
				}
			}else {
				flag = false;
				break;
			}
		}
		return flag;
	}
	public String getWarnMessage(Integer empId,String empName) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		List<CotMessage> res = new ArrayList<CotMessage>();
		res = this.getCotBaseDao().find("from CotMessage obj " +
				"where obj.msgTypeId = 1 and obj.msgFlag = 1 " +
				"and (obj.msgStatus = 0 or obj.msgStatus = 1) " +
				"and obj.msgToPerson="+empId+" order by id desc limit 5");
		String htmlWarn = "<div style = 'margin-left:10px;'>";
		for (int i = 0; i < res.size(); i++) {
			CotMessage cotMessages = (CotMessage)res.get(i);
			htmlWarn += "<li style = 'margin-top:4px;'>";
			//htmlWarn += "<a href='#' onclick=\"doAction('"+cotMessages.getMsgAction()+"');\"><span style='font-size:10pt;'>"+cotMessages.getMsgContent()+"</span></a>";
			htmlWarn += "<a href='"+cotMessages.getMsgAction()+"'><span style='font-size:10pt;'>"+cotMessages.getMsgContent()+"</span></a>";
			htmlWarn += "<span style='font-size:11pt;margin-left:10px;color:#a67'>"+sdf.format(cotMessages.getAddTime())+"</span>";
//			htmlWarn += "&nbsp;&nbsp;<img src='/CotSystem/common/images/_new3.gif'>";
			htmlWarn += "&nbsp;&nbsp;<img src='common/images/_new3.gif'>";
			htmlWarn += "</li>";
		}
		htmlWarn += "</div>";
		System.out.println(htmlWarn);
		return htmlWarn;
	}
	public String getMutualMessage(Integer empId,String empName) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		List<CotMessage> res = new ArrayList<CotMessage>();
		res = this.getCotBaseDao().find("from CotMessage obj " +
				"where obj.msgTypeId = 2 and obj.msgFlag = 1 " +
				"and (obj.msgStatus = 0 or obj.msgStatus = 1) " +
				"and obj.msgToPerson="+empId+" order by id desc limit 5");
		String htmlWarn = "<div style = 'margin-left:10px;'>";
		for (int i = 0; i < res.size(); i++) {
			CotMessage cotMessages = (CotMessage)res.get(i);
			htmlWarn += "<li style = 'margin-top:3px;'>";
			htmlWarn += "<a href ='"+cotMessages.getMsgAction()+"'><span style='font-size:10pt;'>"+cotMessages.getMsgContent()+"</span></a>";
			htmlWarn += "<span style='font-size:11pt;margin-left:10px;color:#a67'>"+sdf.format(cotMessages.getAddTime())+"</span>";
			htmlWarn += "</li>";
		}
		htmlWarn += "</div>";
		System.out.println(htmlWarn);
		return htmlWarn;
	}
	public String getGroupMessage() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		List<CotMessage> res = new ArrayList<CotMessage>();
		res = this.getCotBaseDao().find("from CotMessage obj " +
				"where obj.msgTypeId = 4 and obj.msgFlag = 1 " +
				"and (obj.msgStatus = 0 or obj.msgStatus = 1) " +
				"order by id desc limit 5");
		String htmlWarn = "<div style = 'margin-left:10px;'>";
		for (int i = 0; i < res.size(); i++) {
			CotMessage cotMessages = (CotMessage)res.get(i);
			htmlWarn += "<li style = 'margin-top:3px;'>";
			htmlWarn += "<a href ='"+cotMessages.getMsgAction()+"'><span style='font-size:10pt;'>"+cotMessages.getMsgContent()+"</span></a>";
			htmlWarn += "<span style='font-size:11pt;margin-left:10px;color:#a67'>"+sdf.format(cotMessages.getAddTime())+"</span>";
			htmlWarn += "</li>";
		}
		htmlWarn += "</div>";
		System.out.println(htmlWarn);
		return htmlWarn;
	}
	public String getAgencyMessage(Integer empId,String empName) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		List<CotMessage> res = new ArrayList<CotMessage>();
		res = this.getCotBaseDao().find("from CotMessage obj " +
				"where obj.msgTypeId = 3 and obj.msgFlag = 1 " +
				"and (obj.msgStatus = 0 or obj.msgStatus = 1) " +
				"and obj.msgToPerson="+empId+" order by id desc limit 5");
		String htmlWarn = "<div style = 'margin-left:10px;'>";
		for (int i = 0; i < res.size(); i++) {
			CotMessage cotMessages = (CotMessage)res.get(i);
			htmlWarn += "<li style = 'margin-top:3px;'>";
			htmlWarn += "<a href ='"+cotMessages.getMsgAction()+"'><span style='font-size:10pt;'>"+cotMessages.getMsgContent()+"</span></a>";
			htmlWarn += "<span style='font-size:11pt;margin-left:10px;color:#a67'>"+sdf.format(cotMessages.getAddTime())+"</span>";
			htmlWarn += "</li>";
		}
		htmlWarn += "</div>";
		System.out.println(htmlWarn);
		return htmlWarn;
	}

	public CotEmps getCurEmp() {
		HttpSession session = WebContextFactory.get().getSession();
		CotEmps emp = (CotEmps)session.getAttribute("emp");
		return emp;
	}
	//判断发送者是否为当前用户
	public boolean sendIsCurUser(Integer id) {
		HttpSession session = WebContextFactory.get().getSession();
		CotEmps emp = (CotEmps)session.getAttribute("emp");
		List<CotMessage> res = new ArrayList<CotMessage>();
		res = cotBaseDao.find("from CotMessage obj where obj.id =" +id);
		CotMessage cotMessage = (CotMessage)res.get(0);		
		if ((emp.getId()).toString().equals((cotMessage.getMsgFromPerson()).toString()) && cotMessage.getMsgToPerson() == 0) {
			return true;
		}else{
			return false;
		}
	}
	
	//判断当前用户是不是该系统消息的接收人(如果该消息的群发的返回true)
	public boolean recvIsCur(Integer id) {
		HttpSession session = WebContextFactory.get().getSession();
		CotEmps emp = (CotEmps)session.getAttribute("emp");
		List<CotMessage> res = new ArrayList<CotMessage>();
		res = cotBaseDao.find("from CotMessage obj where obj.id =" +id);
		CotMessage cotMessage = (CotMessage)res.get(0);		
		if ((emp.getId()).toString().equals((cotMessage.getMsgToPerson()).toString())|| cotMessage.getMsgToPerson() == 0) {
			return true;
		}else{
			return false;
		}
	}
	
	//判断发送者是否为当前用户
	public boolean sendIsCur(Integer id) {
		HttpSession session = WebContextFactory.get().getSession();
		CotEmps emp = (CotEmps)session.getAttribute("emp");
		List<CotMessage> res = new ArrayList<CotMessage>();
		res = cotBaseDao.find("from CotMessage obj where obj.id =" +id);
		CotMessage cotMessage = (CotMessage)res.get(0);		
		if ((emp.getId()).toString().equals((cotMessage.getMsgFromPerson()).toString())) {
			return true;
		}else{
			return false;
		}
	}
	
	//判断发送者是否为消息接收者
	public boolean sendIsRecv(Integer id) {

		List<CotMessage> res = new ArrayList<CotMessage>();
		res = cotBaseDao.find("from CotMessage obj where obj.id =" +id);
		CotMessage cotMessage = (CotMessage)res.get(0);		
		if (cotMessage.getMsgFromPerson().toString().equals(cotMessage.getMsgToPerson().toString())) {
			return true;
		}else{
			return false;
		}
	}
	
	public String getJsonData(QueryInfo queryInfo) throws DAOException {
		// TODO Auto-generated method stub
		return getCotBaseDao().getJsonData(queryInfo);
	}
}
