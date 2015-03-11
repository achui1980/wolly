package com.sail.cot.service.worklog;

import java.util.List;
import java.util.Map;

import com.jason.core.exception.DAOException;
import com.sail.cot.domain.CotEmps;
import com.sail.cot.domain.CotMessage;
import com.sail.cot.query.QueryInfo;

public interface CotMessageService {

	List getMessageList();
	 
	CotMessage getMessageById(Integer Id);
	 
	void addMessage(List MessageList);
	
	//保存消息单
	public Integer saveMessage(CotMessage cotMessage,String url, String msgBeginDate, String msgEndDate);
 
	//保存消息单
	public Integer updateMessage(CotMessage cotMessage, String msgBeginDate, String msgEndDate);
	
	boolean modifyMessage(List MessageList);
	 
	int deleteMessage(List MessageList);
	 
	boolean findExistByNo(String no);
	
	public List getList(QueryInfo queryInfo);
	
	public int getRecordCount(QueryInfo queryInfo);
	
	public Integer isExistMessageId(String msgOrderNo);
	
	//查询所有消息类型
	public Map<?, ?> getMsgTypeMap();
	
	//查询所有emps
	public Map<?, ?> getEmpsMap();
	
	// 得到objName的集合
	public List<?> getList(String objName);
	
	public CotEmps getEmpById(Integer id);
	
	//获取当前用户
	public CotEmps getCurEmp();
	
	//获取未读消息条数
	public Integer getUnReadMessage();
	
	//获取未处理消息条数
	public Integer getUnHandleMessage();
	
	//获取未处理,提醒已结束消息条数
	public Integer getOverMessage();
	
	//取消时修改消息状态为已读，未处理
	public void updateMsgStatus(Integer id);
	
	//判断消息是否已经提醒结束
	public void modifyMsgStatus();
	
	//判断接收者是否为当前用户
	public boolean recvIsCurUser(Integer id);
	
	//批量删除时判断接收者是否全为当前用户
	public boolean checkIsCurUser(List messageList);
	
	//获取提醒消息
	public String getWarnMessage(Integer empId,String empName);
	
	//获取互发消息
	public String getMutualMessage(Integer empId,String empName);
	
	//获取群发消息
	public String getGroupMessage();
	
	//获取代理消息
	public String getAgencyMessage(Integer empId,String empName);
	
	//删除公告消息
	public boolean sendIsCurUser(Integer id);
	
	//判断接收者是否为当前用户
	public boolean recvIsCur(Integer id);
	
	//判断发送者是否为当前用户
	public boolean sendIsCur(Integer id);
	
	//判断发送者是否为消息接收者
	public boolean sendIsRecv(Integer id);
	
	public String getJsonData(QueryInfo queryInfo) throws DAOException;
}
