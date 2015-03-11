package com.sail.cot.email.service;

import java.util.List;

import com.sail.cot.domain.vo.CotAutoCompleteVO;
import com.sail.cot.query.QueryInfo;
/**
 * 对本地邮件进行操作
 * @author zhao
 *
 */
public interface MailLocalService extends MailReadOneService,MailUpdateService,MailHistoryService,MailCacheService {	
	/**
	 * 自动补全
	 * @param queryInfo
	 * @return
	 */
	public List<CotAutoCompleteVO> getAutoCompletList(QueryInfo queryInfo);
	
}
