package com.sail.cot.email.service;

import java.util.List;

public interface MailFindNodeService {
	/**
	 * 查询公共邮件箱
	 * @return
	 */
	public Integer findMailGInboxId();
	/**
	 * 查询员工的邮件箱节点
	 * @param empId
	 * @return
	 */
	public Integer findMailEmpId(Integer empId);
	/**
	 * 查询员工邮件箱下的收件箱
	 * @param empId
	 * @return
	 */
	public Integer findMailInboxId(Integer empId);
	/**
	 * 查询员工邮件箱下的发件箱
	 * @param empId
	 * @return
	 */
	public Integer findMailSendId(Integer empId);
	/**
	 * 查询员工邮件箱下的草稿箱
	 * @param empId
	 * @return
	 */
	public Integer findMailDraftId(Integer empId);
	/**
	 * 查询员工邮件箱下的废件箱
	 * @param empId
	 * @return
	 */
	public Integer findMailDelId(Integer empId);
	/**
	 * 查询员工邮件箱下的待审核
	 * @param empId
	 * @return
	 */
	public Integer findMailCheckId(Integer empId);
	/**
	 * 查询节点ID所属的员工
	 * @param nodeId
	 * @return
	 */
	public Integer findNodeOfEmpId(Integer nodeId);
	/**
	 * 查询出所有子节点的邮件编号集合,包含本节点
	 * @param nodeId
	 * @return
	 */
	public List<String> findChildrenMail(Integer nodeId);
	/**
	 * 查询出该节点和子节点是否存在邮件，如果存在，则不能删除
	 * @param nodeId
	 * @return
	 */
	public boolean findNodeExistsMail(Integer nodeId);
	/**
	 * 查询出所有子节点的编号集合(含本节点的id)
	 * @param nodeId
	 * @return
	 */
	public List<Integer> findChildrenIds(Integer nodeId);
}
