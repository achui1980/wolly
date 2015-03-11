package com.sail.cot.email.service;

import java.util.List;

import com.jason.core.exception.DAOException;
/**
 * 移动节点下的邮件
 * @author zhao
 *
 */
public interface MailMoveNodeService {
	/**
	 * TODO:
	 * 移动邮件到节点,只能在本员工下移动
	 * @param nodeId
	 * @param mailIds
	 * @return
	 */
	public boolean moveMailToNode(Integer nodeId,List<String> mailIds)throws DAOException;
	
	/**
	 * TODO:
	 * 移动邮件到员工下的收件箱
	 * @param empId
	 * @param mailIds
	 * @return
	 */
	public boolean moveMailToInbox(Integer empId,List<String> mailIds)throws DAOException;
	/**
	 * // TODO:有使用
	 * 移动邮件到员工下的发件箱
	 * @param empId
	 * @param mailIds
	 * @return
	 */
	public boolean moveMailToSend(Integer empId,List<String> mailIds)throws DAOException;
	/**
	 * // TODO:有使用
	 * 移动邮件到员工下的草稿箱
	 * @param empId
	 * @param mailIds
	 * @return
	 */
	public boolean moveMailToDraft(Integer empId,List<String> mailIds)throws DAOException;
	/**
	 * // TODO:有使用
	 * 移动邮件到员工下的废件箱
	 * @param empId
	 * @param mailIds
	 * @return
	 */
	public boolean moveMailToDel(Integer empId,List<String> mailIds)throws DAOException;
	/**
	 * 还原员工下废件箱的邮件到原来所属邮件箱
	 * @param empId
	 * @param mailIds
	 * @return
	 */
	public boolean moveDelMailToRevert(Integer empId,List<String> mailIds)throws DAOException;
	
}
