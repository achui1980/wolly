package com.sail.cot.email.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.jason.core.exception.DAOException;
import com.sail.cot.dao.CotBaseDao;
import com.sail.cot.domain.CotMail;
import com.sail.cot.domain.CotMailTree;
import com.sail.cot.email.service.MailTreeService;
import com.sail.cot.email.util.Constants;
import com.sail.cot.query.QueryInfo;
import com.sail.cot.util.Log4WebUtil;
/**
 * 对树节点的所有操作
 * @author zhao
 *
 */
public class MailTreeServiceImpl implements MailTreeService{
	private Logger logger = Log4WebUtil.getLogger(MailTreeServiceImpl.class);
	private CotBaseDao cotBaseDao;
	public CotBaseDao getCotBaseDao() {
		return cotBaseDao;
	}
	public void setCotBaseDao(CotBaseDao cotBaseDao) {
		this.cotBaseDao = cotBaseDao;
	}
	
	// 查询某个员工的邮件树
	@SuppressWarnings("unchecked")
	public String getMailTreeByEmpId(Integer empId) {
		String hql = "from CotMailTree obj where obj.empId="+empId;
		List mailList = this.getCotBaseDao().find(hql);
		StringBuffer returnStr = new StringBuffer();
		this.recursionFn(returnStr,mailList, new CotMailTree(1,"员工邮箱", 0,null));
		return this.modifyStr(returnStr.toString()); 
	}
	
	// 查询员工邮件树
	@SuppressWarnings("unchecked")
	public String getMailTree(String hql) throws DAOException {
		List mailList = this.getCotBaseDao().find(hql);
		StringBuffer returnStr = new StringBuffer();
		this.recursionFn(returnStr,mailList, new CotMailTree(1,"员工邮箱", 0,null));
		return this.modifyStr(returnStr.toString()); 
	}
	//递归运算
	@SuppressWarnings("unchecked")
	public void recursionFn(StringBuffer returnStr,List list, CotMailTree node) {
		if(node.getNodeName()!=null && !"".equals(node.getNodeName())){
			if (hasChild(list, node)) {
				if(node.getParentId()!=0){
					returnStr.append("{id:");
					returnStr.append(node.getId());
					returnStr.append(",empId:");
					returnStr.append(node.getEmpId());
					returnStr.append(",text:'");
					returnStr.append(node.getNodeName());
					returnStr.append("',iconCls:'");
					returnStr.append(node.getCls());
					returnStr.append("',parentId:");
					returnStr.append(node.getParentId());
					returnStr.append(",flag:'");
					returnStr.append(node.getFlag());
					returnStr.append("',updateFlag:'");
					returnStr.append(node.getUpdateFlag());
					returnStr.append("',leaf:false");
					//returnStr.append(",expanded:true");
					returnStr.append(",children:[");
				}
				List childList = getChildList(list, node);
				Iterator it = childList.iterator();
				while (it.hasNext()) {
					CotMailTree n = (CotMailTree) it.next();
					recursionFn(returnStr,list, n);
				}
				if(node.getParentId()!=0){
					returnStr.append("]},");
				}
			} else {
				returnStr.append("{id:");
				returnStr.append(node.getId());
				returnStr.append(",empId:");
				returnStr.append(node.getEmpId());
				returnStr.append(",text:'");
				returnStr.append(node.getNodeName());
				returnStr.append("',iconCls:'");
				returnStr.append(node.getCls());
				returnStr.append("',parentId:");
				returnStr.append(node.getParentId());
				returnStr.append(",flag:'");
				returnStr.append(node.getFlag());
				returnStr.append("',updateFlag:'");
				returnStr.append(node.getUpdateFlag());
				returnStr.append("',leaf:true},");
			}
		}
	}

	//判断是否有子节点 
	@SuppressWarnings("unchecked")
	public boolean hasChild(List list, CotMailTree node) {
		return getChildList(list, node).size() > 0 ? true : false;
	}

	//得到子节点列表   
	@SuppressWarnings("unchecked")
	public List getChildList(List list, CotMailTree node) {
		List li = new ArrayList();
		Iterator it = list.iterator();
		while (it.hasNext()) {
			CotMailTree n = (CotMailTree) it.next();
			if (n.getParentId() != null && n.getParentId().intValue() == node.getId().intValue()) {
				li.add(n);
			}
		}
		return li;
	}

	//修饰一下才能满足Extjs的Json格式 
	public String modifyStr(String returnStr) {
		return ("[" + returnStr + "]").replaceAll(",]", "]");

	}
	
	//新建个邮件箱
	@SuppressWarnings("unchecked")
	public Integer saveTreeNode(Integer parentId,String nodeName){
		CotMailTree parent = (CotMailTree) this.getCotBaseDao().getById(CotMailTree.class, parentId);
		CotMailTree child = new CotMailTree();
		child.setEmpId(parent.getEmpId());
		child.setCls("folder");
		child.setNodeName(nodeName);
		if(parent.getFlag()==null||parent.getFlag().trim().equals(""))
			child.setFlag(Constants.MAIL_TREE_FLAG_INBOX);
		else
			child.setFlag(parent.getFlag());
		child.setParentId(parentId);
		List list = new ArrayList();
		list.add(child);
		try {
			this.getCotBaseDao().saveRecords(list);
			return child.getId();
		} catch (DAOException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	//删除邮件箱
	public void delTreeNode(Integer nodeId){
		try {
			this.getCotBaseDao().deleteRecordById(nodeId, "CotMailTree");
		} catch (DAOException e) {
			e.printStackTrace();
		}
	}
	
	//重命名邮件箱
	@SuppressWarnings("unchecked")
	public void updateTreeNode(Integer nodeId,String nodeName){
		CotMailTree node = (CotMailTree) this.getCotBaseDao().getById(CotMailTree.class, nodeId);
		node.setNodeName(nodeName);
		List list = new ArrayList();
		list.add(node);
		try {
			this.getCotBaseDao().updateRecords(list);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//获得某个邮件树节点的路径, a->b->c
	public String getNodePath(String idPath){
		return "";
	}
	
	//查询出所有子节点的邮件编号集合
	@SuppressWarnings("unchecked")
	public List<String> findChildrenMail(Integer nodeId){
		List mailList = this.getCotBaseDao().getRecords("CotMailTree");
		List list = new ArrayList();
		CotMailTree parentNode = (CotMailTree) this.getCotBaseDao().getById(CotMailTree.class, nodeId);
		this.recursionFnMail(list,mailList, parentNode);
		return list; 
	}
	
	//递归运算求子节点邮件编号集合
	@SuppressWarnings("unchecked")
	public void recursionFnMail(List childMailList,List list, CotMailTree node) {
		if(node.getNodeName()!=null && !"".equals(node.getNodeName())){
			String hql = "select obj.id from CotMail obj where obj.nodeId="+node.getId();
			List temp = this.getCotBaseDao().find(hql);
			if(temp!=null && temp.size()>0){
				childMailList.addAll(temp);
			}
			if (hasChild(list, node)) {
				List childList = getChildList(list, node);
				Iterator it = childList.iterator();
				while (it.hasNext()) {
					CotMailTree n = (CotMailTree) it.next();
					recursionFnMail(childMailList,list, n);
				}
			}
		}
	}
	@SuppressWarnings("unchecked")
	public Integer findMailGInboxId(){
		String hql = "select obj.id from CotMailTree as obj " +
				"where obj.flag = ? and obj.updateFlag='n'";
		Object[] values = new Object[1];
		values[0] = "G";
		List<Integer> list = this.getCotBaseDao().queryForLists(hql, values);
		if(list.size()>0)
			return list.get(0);
		return null;
	}
	/*
	 * (non-Javadoc)
	 * @see com.sail.cot.email.service.MailFindNodeService#findMailDelId(java.lang.Integer)
	 */
	public Integer findMailDelId(Integer empId) {
		logger.debug("查询员工下的废件箱");
		// TODO:有使用
		return this.findMailInboxNodeId(empId, Constants.MAIL_TREE_FLAG_DEL);
	}
	/*
	 * (non-Javadoc)
	 * @see com.sail.cot.email.service.MailFindNodeService#findMailDraftId(java.lang.Integer)
	 */
	public Integer findMailDraftId(Integer empId) {
		logger.debug("查询员工下的草稿箱");
		// TODO:有使用
		return this.findMailInboxNodeId(empId, Constants.MAIL_TREE_FLAG_DRATF);
	}
	/*
	 * (non-Javadoc)
	 * @see com.sail.cot.email.service.MailFindNodeService#findMailInboxId(java.lang.Integer)
	 */
	public Integer findMailInboxId(Integer empId) {
		logger.debug("查询员工下的收件箱");
		// TODO:有使用
		return this.findMailInboxNodeId(empId, Constants.MAIL_TREE_FLAG_INBOX);
	}
	/*
	 * (non-Javadoc)
	 * @see com.sail.cot.email.service.MailFindNodeService#findMailSendId(java.lang.Integer)
	 */
	public Integer findMailSendId(Integer empId) {
		logger.debug("查询员工下的已发送邮件箱");
		// TODO:有使用
		return this.findMailInboxNodeId(empId, Constants.MAIL_TREE_FLAG_SEND);
	}
	public Integer findMailCheckId(Integer empId) {
		logger.debug("查询员工下的待发送箱");
		// TODO:有使用
		return this.findMailInboxNodeId(empId, Constants.MAIL_TREE_FLAG_CHECK);
	}
	/**
	 * 根据flag查询员工节点下的邮箱
	 * @param empId
	 * @param flag
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private Integer findMailInboxNodeId(Integer empId,String flag){
		if(empId == null || flag == null)
			return null;
		String hql = "select obj.id from CotMailTree as obj " +
				"where obj.empId=? and obj.flag = ? and obj.updateFlag='n'";
		Object[] values = new Object[2];
		values[0] = empId;
		values[1] = flag;
		List<Integer> list = this.getCotBaseDao().queryForLists(hql, values);
		if(list.size()>0)
			return list.get(0);
		return null;
	}
	/*
	 * (non-Javadoc)
	 * @see com.sail.cot.email.service.MailMoveNodeService#moveMailToNode(java.lang.Integer, java.util.List)
	 */
	@SuppressWarnings("unchecked")
	public boolean moveMailToNode(Integer nodeId, List<String> mailIds) throws DAOException {
		// TODO:有使用
		logger.info("执行更新邮件树节点方法");
		if(nodeId==null||mailIds==null||mailIds.size()==0){
			logger.info("节点ID为空或邮件ID为空，不执行更新");
			return false;
		}
		String hql = "update CotMail obj set obj.nodeId = :nodeId " +
					"where obj.id in (:mailIds)";
		Map map = new HashMap();
		map.put("nodeId", nodeId);
		map.put("mailIds", mailIds);
		QueryInfo queryInfo = new QueryInfo();
		queryInfo.setSelectString(hql);
		int result = this.getCotBaseDao().executeUpdate(queryInfo,map);
		if(result>0){
			logger.info("更新成功");
			return true;
		}else {
			logger.info("更新失败");
			return false;
		}
	}
	/*
	 * (non-Javadoc)
	 * @see com.sail.cot.email.service.MailMoveNodeService#moveMailToDel(java.lang.Integer, java.util.List)
	 */
	public boolean moveMailToDel(Integer empId, List<String> mailIds) throws DAOException {
		logger.debug("执行移动邮件到员工下的废件箱方法");
		Integer nodeId = this.findMailDelId(empId);
		return this.moveMailToNode(nodeId, mailIds);
	}
	/*
	 * (non-Javadoc)
	 * @see com.sail.cot.email.service.MailMoveNodeService#moveMailToDraft(java.lang.Integer, java.util.List)
	 */
	public boolean moveMailToDraft(Integer empId, List<String> mailIds) throws DAOException {
		logger.debug("执行移动邮件到员工下的草稿箱方法");
		Integer nodeId = this.findMailDraftId(empId);
		return this.moveMailToNode(nodeId, mailIds);
	}
	/*
	 * (non-Javadoc)
	 * @see com.sail.cot.email.service.MailMoveNodeService#moveMailToInbox(java.lang.Integer, java.util.List)
	 */
	public boolean moveMailToInbox(Integer empId, List<String> mailIds) throws DAOException {
		logger.debug("执行移动邮件到员工下的收件箱方法");
		Integer nodeId = this.findMailInboxId(empId);
		return this.moveMailToNode(nodeId, mailIds);
	}
	/*
	 * (non-Javadoc)
	 * @see com.sail.cot.email.service.MailMoveNodeService#moveMailToSend(java.lang.Integer, java.util.List)
	 */
	public boolean moveMailToSend(Integer empId, List<String> mailIds) throws DAOException {
		logger.debug("执行移动邮件到员工下的已发送邮件箱方法");
		Integer nodeId = this.findMailSendId(empId);
		return this.moveMailToNode(nodeId, mailIds);
	}
	/*
	 * (non-Javadoc)
	 * @see com.sail.cot.email.service.MailMoveNodeService#moveDelMailToRevert(java.lang.Integer, java.util.List)
	 */
	public boolean moveDelMailToRevert(Integer empId, List<String> mailIds) throws DAOException {
		logger.debug("将邮件还原");
		if(mailIds==null||mailIds.size()==0)
			return false;
		List<String> inboxIds = new ArrayList<String>();
		List<String> sendIds = new ArrayList<String>();
		List<String> draftIds = new ArrayList<String>();
		for (String id : mailIds) {
			CotMail cotMail = (CotMail) this.getCotBaseDao().getById(CotMail.class,id);
			int mailType = cotMail.getMailType();
			if(mailType==Constants.MAIL_LOCAL_TYPE_SEND)
				sendIds.add(id);
			else if(mailType==Constants.MAIL_LOCAL_TYPE_DRAFT)
				draftIds.add(id);
			else
				inboxIds.add(id);
		}
		this.moveMailToInbox(empId, inboxIds);
		this.moveMailToSend(empId, sendIds);
		this.moveMailToDraft(empId, draftIds);
		return true;
	}
	/*
	 * (non-Javadoc)
	 * @see com.sail.cot.email.service.MailFindNodeService#findNodeOfEmpId(java.lang.Integer)
	 */
	public Integer findNodeOfEmpId(Integer nodeId) {
		CotMailTree cotMailTree = (CotMailTree) this.getCotBaseDao().getById(CotMailTree.class, nodeId);
		if(cotMailTree==null)
			return null;
		return cotMailTree.getEmpId();
	}
	/*
	 * (non-Javadoc)
	 * @see com.sail.cot.email.service.MailFindNodeService#findMailEmpId(java.lang.Integer)
	 */
	@SuppressWarnings("unchecked")
	public Integer findMailEmpId(Integer empId) {
		logger.debug("查询员工下邮件箱节点");
		if(empId == null)
			return null;
		String hql = "select obj.id from CotMailTree as obj " +
				"where obj.empId=? and obj.flag is null and obj.updateFlag='n'";
		Object[] values = new Object[1];
		values[0] = empId;
		List<Integer> list = this.getCotBaseDao().queryForLists(hql, values);
		if(list.size()>0)
			return list.get(0);
		return null;
	}
	/*
	 * (non-Javadoc)
	 * @see com.sail.cot.email.service.MailFindNodeService#findNodeExistsMail(java.lang.Integer)
	 */
	@SuppressWarnings("unchecked")
	public boolean findNodeExistsMail(Integer nodeId){
		logger.debug("查询节点和子节点是否存在邮件");
		List list = this.findChildrenMail(nodeId);
		return list!=null&&!list.isEmpty();
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.sail.cot.email.service.MailFindNodeService#findChildrenIds(java.lang.Integer)
	 */
	@SuppressWarnings("unchecked")
	public List<Integer> findChildrenIds(Integer nodeId){
		List mailList = this.getCotBaseDao().getRecords("CotMailTree");
		List list = new ArrayList();
		//含本节点的id
		list.add(nodeId);
		CotMailTree parentNode = (CotMailTree) this.getCotBaseDao().getById(CotMailTree.class, nodeId);
		this.recursionFnChildId(list,mailList, parentNode);
		return list; 
	}
	//递归运算求子节点编号集合
	@SuppressWarnings("unchecked")
	public void recursionFnChildId(List childList,List list, CotMailTree node) {
		if(node.getNodeName()!=null && !"".equals(node.getNodeName())){
			String hql = "select obj.id from CotMailTree obj where obj.parentId="+node.getId();
			List temp = this.getCotBaseDao().find(hql);
			if(temp!=null && temp.size()>0){
				childList.addAll(temp);
			}
			if (hasChild(list, node)) {
				List child = getChildList(list, node);
				Iterator it = child.iterator();
				while (it.hasNext()) {
					CotMailTree n = (CotMailTree) it.next();
					recursionFnChildId(childList,list, n);
				}
			}
		}
	}
}
