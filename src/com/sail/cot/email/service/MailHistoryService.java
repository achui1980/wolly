package com.sail.cot.email.service;

import java.util.List;

import com.sail.cot.domain.vo.HistoryMailAttachVO;
import com.sail.cot.domain.vo.HistoryMailVO;

/**
 * 往来邮件历史
 * @author zhao
 *
 */
public interface MailHistoryService {
	/**
	 * 获得往来邮件历史
	 * "select obj.id,obj.mailType,obj.subject,obj.isContainAttach,obj.sendTime from CotMail obj";
	 * @param queryInfo
	 * @return
	 */
	public List<HistoryMailVO> getHistoryMail(String hql,Object[] params,int start,int limit);
	/**
	 * 获得往来附件历史
	 * @param queryInfo
	 * @return
	 */
	public List<HistoryMailAttachVO> getHistoryAttach(String hql,Object[] params,int start,int limit);
}
