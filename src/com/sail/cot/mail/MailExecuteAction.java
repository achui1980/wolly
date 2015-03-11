/**
 * 
 */
package com.sail.cot.mail;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.jason.core.exception.DAOException;
import com.sail.cot.domain.CotMail;
import com.sail.cot.email.service.MailLocalService;
import com.sail.cot.email.service.MailTreeService;
import com.sail.cot.util.SystemUtil;

/**
 * <p>Title: 旗行办公自动化系统（OA）</p>
 * <p>Description: 通过配置规则形成的一个邮件规则动作，以及对应动作的实现</p>
 * <p>Copyright: Copyright (c) 2008</p>
 * <p>Company: </p>
 * <p>Create Time: Jul 14, 2010 4:44:55 PM </p>
 * <p>Class Name: MailExecuteAction.java </p>
 * @author achui
 *
 */
public class MailExecuteAction {
	private Logger log = Logger.getLogger(MailExecuteAction.class);
	private Integer empId; //邮件指定用户
	private String moveTo; //邮件要移动到的节点
	private Integer custId;//邮件的指定客户
	private Integer facId;//邮件的指定厂家
	private CotMail mailObject;// 接收邮件对象
	private boolean checked = false; //是否需要审核
	
	private MailTreeService mailTreeService;
	private MailLocalService  mailLocalService;
	public Integer getFacId() {
		return facId;
	}
	public void setFacId(Integer facId) {
		this.facId = facId;
	}
	public MailTreeService getMailTreeService() {
		if(mailTreeService == null){
			mailTreeService = (MailTreeService)SystemUtil.getService("MailTreeService");
		}
		return mailTreeService;
	}
	public MailLocalService getMailLocalService() {
		if(mailLocalService == null){
			mailLocalService = (MailLocalService)SystemUtil.getService("MailLocalService");
		}
		return mailLocalService;
	}
	public Integer getEmpId() {
		return empId;
	}
	public void setEmpId(Integer empId) {
		this.empId = empId;
	}
	public String getMoveTo() {
		return moveTo;
	}
	public void setMoveTo(String moveTo) {
		this.moveTo = moveTo;
	}
	public CotMail getMailObject() {
		return mailObject;
	}
	public void setMailObject(CotMail cotMail) {
		this.mailObject = cotMail;
	}

	public Integer getCustId() {
		return custId;
	}
	public void setCustId(Integer custId) {
		this.custId = custId;
	}
	/**
	 * 描述： 将邮件指派给相应的业务员，分2部执行
	 * 1、数据库生成的邮件ID，对邮件进行指派
	 * 返回值：返回数据库生成的Id
	 */
	public String asignTo(){
		log.info("执行邮件指派方法");
		if(this.empId == null) return null;
		List mailDbIds = new ArrayList();
		mailDbIds.add(this.mailObject.getId());
		this.getMailLocalService().moveAssignMail(empId, mailDbIds);
		log.info("指派... ..."+this.getEmpId());
		return mailObject.getId();
	}
	/**
	 * 描述： 将邮件移动到相应的树节点下
	 * 1、通过邮件ID和员工ID，获取该邮件所在的节点
	 * 2、更新该邮件的节点数据
	 * 返回值：void
	 * @throws DAOException 
	 * @throws NumberFormatException 
	 */
	public void moveToNode() throws NumberFormatException, DAOException{
		log.info("执行邮件移动节点方法");
		if(this.moveTo == null) return;
		List mailDbIds = new ArrayList();
		mailDbIds.add(this.mailObject.getId());
		this.getMailTreeService().moveMailToNode(new Integer(this.getMoveTo()), mailDbIds);
	}
	/**
	 * 描述： 将邮件归档到某个客户名下，表名该封邮件是由这个客户发送的过来的
	 * 返回值：void
	 * @throws DAOException 
	 */
	public void archiveCust() throws DAOException{
		if(this.custId == null) return;
		log.info("执行邮件归档到客户方法");
		List mailDbIds = new ArrayList();
		mailDbIds.add(this.mailObject.getId());
		this.getMailLocalService().saveArchivesMail(this.custId, mailDbIds);
	}
	/**
	 * 描述： 将邮件归档到某个厂家名下，表明该封邮件是由这个厂家发送的过来的
	 * 返回值：void
	 * @throws DAOException 
	 */
	public void archiveFac() throws DAOException {
		if(this.facId == null) return;
		log.info("执行邮件归档到厂家方法");
		List mailDbIds = new ArrayList();
		mailDbIds.add(this.mailObject.getId());
		this.getMailLocalService().saveArchivesMail(this.facId, mailDbIds);
	}
	public boolean isChecked() {
		return checked;
	}
	public void setChecked(boolean checked) {
		this.checked = checked;
	}

	
	
}
