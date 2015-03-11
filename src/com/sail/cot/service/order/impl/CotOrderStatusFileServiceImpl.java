/**
 * 
 */
package com.sail.cot.service.order.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.directwebremoting.WebContext;

import com.jason.core.exception.DAOException;
import com.sail.cot.dao.CotBaseDao;
import com.sail.cot.domain.CotOrder;
import com.sail.cot.domain.CotOrderFac;
import com.sail.cot.domain.CotOrderRemark;
import com.sail.cot.domain.CotOrderstatusFile;
import com.sail.cot.query.QueryInfo;
import com.sail.cot.service.order.CotOrderStatusFileService;
import com.sail.cot.util.ContextUtil;

/**
 * <p>Title: 旗航外贸管理软件V8.0</p>
 * <p>Description:</p>
 * <p>Copyright: Copyright (c) 2011</p>
 * <p>Company: 厦门市旗航软件有限公司</p>
 * <p>Create Time: Mar 24, 2013 5:10:48 PM </p>
 * <p>Class Name: CotOrderStatusFileServiceImpl.java </p>
 * @author achui
 *
 */
public class CotOrderStatusFileServiceImpl implements CotOrderStatusFileService{

	private CotBaseDao orderDao;
	
	public CotBaseDao getOrderDao() {
		return orderDao;
	}

	public void setOrderDao(CotBaseDao orderDao) {
		this.orderDao = orderDao;
	}

	/* (non-Javadoc)
	 * @see com.sail.cot.service.order.CotOrderStatusFileService#saveFile(com.sail.cot.domain.CotOrderstatusFile)
	 */
	@Override
	public String saveFile(CotOrderstatusFile file,String orderStatus,Integer currentEmpId) {
		String count = ContextUtil.getProperty("remoteaddr.properties", "wolly_"+orderStatus.toUpperCase());
		String defaultCount = ContextUtil.getProperty("remoteaddr.properties", "wolly_Default");
		int iCount = StringUtils.isEmpty(count)?Integer.valueOf(defaultCount):Integer.valueOf(count);
		List list = this.getOrderDao().find("from CotOrderstatusFile obj where obj.orderId="+file.getOrderId()+" and obj.orderStatus='"+orderStatus+"'");
		if(CollectionUtils.isNotEmpty(list)){
			if(iCount <= list.size()){
				return "Only "+iCount+" file(s) allowed,current is "+list.size();
			}
		}
		List records = new ArrayList();
		file.setOrderStatus(orderStatus);
		records.add(file);
		this.getOrderDao().saveOrUpdateRecords(records);
		return "Save Success";
	}

	public void delFile(List<Integer> ids,List<String> filePath){
		for(String path : filePath){
			File file =new File(ContextUtil.getRealPath()+File.separator+path);
			if(file.exists()){
				file.delete();
			}
		}
		try {
			this.getOrderDao().deleteRecordByIds(ids, "CotOrderstatusFile");
		} catch (DAOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	@Override
	public void saveNewRemark(Integer orderId, String emps,String remark) {
		// TODO Auto-generated method stub
		List addList = new ArrayList();
		CotOrderRemark orderRemark = new CotOrderRemark();
		orderRemark.setOrderId(orderId);
		orderRemark.setAddPerson(emps);
		orderRemark.setRemark(remark);
		orderRemark.setAddTime(new Date(System.currentTimeMillis()));
		addList.add(orderRemark);
		this.getOrderDao().saveOrUpdateRecords(addList);
		//更新PI的最新备注
		QueryInfo queryInfo = new QueryInfo();
		queryInfo.setSelectString("update CotOrder set newRemark=:remark where id=:orderId");
		Map map = new HashMap();
		map.put("remark", remark);
		map.put("orderId", orderId);
		try {
			this.getOrderDao().executeUpdate(queryInfo, map);
		} catch (DAOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//更新PO的最新备注
		String hql = " from CotOrderFac where orderId = "+orderId;
		List list = this.getOrderDao().find(hql);
		if(CollectionUtils.isNotEmpty(list)){
			queryInfo.setSelectString("update CotOrderFac set newRemark=:remark where orderId=:orderId");
			map.put("remark", remark);
			map.put("orderId", orderId);
			try {
				this.getOrderDao().executeUpdate(queryInfo, map);
			} catch (DAOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
}
