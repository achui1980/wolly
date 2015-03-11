package com.sail.cot.email.service;

import java.util.List;

import com.jason.core.exception.DAOException;
import com.sail.cot.domain.CotMail;
/**
 * 对本地邮件进行增，删，改
 * @author zhao
 *
 */
public interface MailUpdateService {
	/**
	 * TODO:
	 * 指派邮件到员工收件箱
	 * @param empId
	 * @param dbIDs
	 * @return
	 */
	public boolean moveAssignMail(Integer empId,List<String> dbIDs);
	/**
	 * TODO:
	 * 保存邮件到指定的节点，并加入节点信息到邮件
	 * @param cotMail
	 * @param nodeId
	 * @return
	 */
	public boolean saveMail(CotMail cotMail,Integer nodeId);
	/**
	 * TODO:
	 * 保存公共新邮件到本地
	 * @return 返回已保存的新邮件，如果不存在，则为null
	 */
	public List<CotMail> saveMailAll();
	/**
	 * TODO:
	 * 保存员工新邮件到本地
	 * @return 返回已保存的新邮件，如果不存在，则为null
	 */
	public List<CotMail> saveMailByEmp(Integer empId);
	/**
	 * TODO：
	 * 根据员工ID,读取员工邮件服务器上的邮件,保存到相应节点
	 * @param empId,
	 * @return
	 */
	public boolean saveMailToEmpNode(Integer empId,Integer nodeId)throws NumberFormatException, DAOException;
	/**
	 * TODO:
	 * 归档到客户
	 * @param custId
	 * @param ids
	 * @return
	 */
	public boolean saveArchivesMail(Integer custId,List<String> dbIDs)throws DAOException;
	/**
	 * TODO:
	 * 归档到厂家
	 * @param facId
	 * @param ids
	 * @return
	 */
	public boolean saveArchivesMail4Fac(Integer facId,List<String> dbIDs)throws DAOException;
	/**
	 * TODO:
	 * 保存邮件到发件箱
	 * @param mailContent
	 */
	public void saveSend(CotMail cotMail,String random)
	 		throws DAOException;
	/**
	 * TODO:
	 * 保存邮件到待发送
	 * @param mailContent
	 */
	public void saveCheck(CotMail cotMail,String random)
	 		throws DAOException;
	/**
	 * TODO:
	 * 保存邮件发送到草稿箱
	 */
	public void saveDraft(CotMail cotMail,String random) 
			throws DAOException;
	/**
	 * TODO:
	 * 将本地邮件彻底删除
	 * @param ids
	 * @return
	 */
	public boolean deleteMails(List<String> ids);
	/**
	 * TODO:
	 * 设置收件箱邮件为已读或未读
	 * @param ids
	 * @param isNew true设置为未读,false设置为已读
	 */
	public void updateMailStatus(List<String> ids,boolean isNew);
	/**
	 * TODO:
	 * 重新从邮件服务器上更新邮件
	 * @param id
	 */
	public void updateMailByServer(String id);
}
