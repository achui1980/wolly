package com.sail.cot.service.order.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.jason.core.exception.DAOException;
import com.sail.cot.dao.CotBaseDao;
import com.sail.cot.domain.CotAnwser;
import com.sail.cot.domain.CotQuestion;
import com.sail.cot.query.QueryInfo;
import com.sail.cot.service.order.CotFaqService;
import com.sail.cot.util.GridServerHandler;

/**
 * *********************************************
 * 
 * @Copyright :(C),2008-2010
 * @CompanyName :厦门市旗航软件有限公司(www.xmqh.net)
 * @Version :1.0
 * @Date :2011-3-10
 * @author : azan
 * @class :CotFaqServiceImpl.java
 * @Description :HOME的聊天记录
 */
public class CotFaqServiceImpl implements CotFaqService {

	private CotBaseDao faqDao;

	public CotBaseDao getFaqDao() {
		return faqDao;
	}

	public void setFaqDao(CotBaseDao faqDao) {
		this.faqDao = faqDao;
	}

	public String getJsonData(QueryInfo queryInfo) throws DAOException {
		return this.getFaqDao().getJsonData(queryInfo);
	}

	public void saveOrUpdateQuestion(CotQuestion question) throws DAOException {
		List list = new ArrayList();
		list.add(question);
		this.getFaqDao().saveOrUpdateRecords(list);
	}

	public void saveOrUpdateAnswer(CotAnwser anwser) throws DAOException {
		List list = new ArrayList();
		list.add(anwser);
		this.getFaqDao().saveOrUpdateRecords(list);
	}

	public void deleteQuestion(List ids) throws DAOException {
		this.getFaqDao().deleteRecordByIds(ids, "CotQuestion");
	}

	public void deleteAnswer(List ids) throws DAOException {
		this.getFaqDao().deleteRecordByIds(ids, "CotAnwser");
	}
	
	//过滤掉orderFac中5个状态为1的记录
	public List getNoApproveData(QueryInfo queryInfo) throws DAOException{
		List res = this.getFaqDao().findRecords(queryInfo);
		Iterator<?> it = res.iterator();
		while(it.hasNext()){
			CotQuestion question = (CotQuestion) it.next();
			if(question.getFlag()==1){
				String hql = "select obj.id from CotOrderFac obj where " +
						"obj.sampleStatus is not null and obj.sampleStatus=1 and obj.orderId="+question.getOrderId();
				List temp=this.getFaqDao().find(hql);
				if(temp.size()>0){
					it.remove();
				}
			}
			if(question.getFlag()==2){
				String hql = "select obj.id from CotOrderFac obj where " +
						"obj.packetStatus is not null and obj.packetStatus=1 and obj.orderId="+question.getOrderId();
				List temp=this.getFaqDao().find(hql);
				if(temp.size()>0){
					it.remove();
				}
			}
			if(question.getFlag()==3){
				String hql = "select obj.id from CotOrderFac obj where " +
						"obj.sampleOutStatus is not null and obj.sampleOutStatus=1 and obj.orderId="+question.getOrderId();
				List temp=this.getFaqDao().find(hql);
				if(temp.size()>0){
					it.remove();
				}
			}
			if(question.getFlag()==4){
				String hql = "select obj.id from CotOrderFac obj where " +
						"obj.qcStatus is not null and obj.qcStatus=1 and obj.orderId="+question.getOrderId();
				List temp=this.getFaqDao().find(hql);
				if(temp.size()>0){
					it.remove();
				}
			}
			if(question.getFlag()==5){
				String hql = "select obj.id from CotOrderFac obj where " +
						"obj.outStatus is not null and obj.outStatus=1 and obj.orderId="+question.getOrderId();
				List temp=this.getFaqDao().find(hql);
				if(temp.size()>0){
					it.remove();
				}
			}
		}
		return res;
	}

	//flag 0:全部提问 2:没有全部答复  3.全部答复
	public String getListData(List res, String flag)
			throws DAOException {
		GridServerHandler gd = new GridServerHandler();
		int count = res.size();
		if(flag!=null && !flag.equals("0") && !flag.equals("")){
			Iterator<?> it = res.iterator();
			while (it.hasNext()) {
				CotQuestion question = (CotQuestion) it.next();
				String anhql = "select obj.id from CotAnwser obj where obj.questionId="
					+ question.getId();
				List anTmp = this.getFaqDao().find(anhql);
				if (anTmp.size() > 0 && flag.equals("2")) {
					it.remove();
					count--;
				}
				if (anTmp.size() == 0 && flag.equals("3")) {
					it.remove();
					count--;
				}
			}
		}
		

		gd.setData(res);
		gd.setTotalCount(count);
		return gd.getLoadResponseText();
	}
}
