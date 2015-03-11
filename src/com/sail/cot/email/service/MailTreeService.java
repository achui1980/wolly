package com.sail.cot.email.service;

import com.jason.core.exception.DAOException;


/**
 * 对树节点的所有操作
 * @author zhao
 *
 */
public interface MailTreeService extends MailFindNodeService,MailMoveNodeService{
	/**
	 * 查询某个员工的邮件树
	 * @return
	 */
	public String getMailTreeByEmpId(Integer empId);
	/**
	 * 查询员工邮件树
	 * @return
	 */
	public String getMailTree(String hql) throws DAOException;
	/**
	 * 新建个邮件箱
	 * @param parentId
	 * @param nodeName
	 * @return
	 */
	public Integer saveTreeNode(Integer parentId,String nodeName);
	/**
	 * 删除邮件箱
	 * @param nodeId
	 */
	public void delTreeNode(Integer nodeId);
	/**
	 * 重命名邮件箱
	 * @param nodeId
	 * @param nodeName
	 */
	public void updateTreeNode(Integer nodeId,String nodeName);
	/**
	 * 获得某个邮件树节点的路径, a->b->c
	 * @return
	 */
	public String getNodePath(String idPath);
	
}
