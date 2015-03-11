package com.sail.cot.service.system.impl;

import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.directwebremoting.WebContextFactory;

import com.jason.core.exception.DAOException;
import com.sail.cot.dao.CotBaseDao;
import com.sail.cot.domain.CotContact;
import com.sail.cot.domain.CotFactory;
import com.sail.cot.domain.vo.CotAddressVO;
import com.sail.cot.domain.vo.CotPriceFacVO;
import com.sail.cot.query.QueryInfo;

import com.sail.cot.service.system.CotContactService;
import com.sail.cot.service.system.CotFactoryService;
import com.sail.cot.service.system.CotSeqService;
import com.sail.cot.util.Log4WebUtil;
import com.sail.cot.util.SystemUtil;

public class CotFactoryServiceImpl implements CotFactoryService {

	private CotBaseDao factoryDao;
//	private GenAllSeq seq = new GenAllSeq();
	public CotBaseDao getFactoryDao() {
		return factoryDao;
	}

	public void setFactoryDao(CotBaseDao factoryDao) {
		this.factoryDao = factoryDao;
	}
	private Logger logger = Log4WebUtil.getLogger(CotFactoryServiceImpl.class);
	
	
	public int getRecordCount(QueryInfo queryInfo) {
		try {
			return this.getFactoryDao().getRecordsCount(queryInfo);
		} catch (DAOException e) {
			 
			e.printStackTrace();
		}
		return 0;
	}
	
	public List<?> getList(QueryInfo queryInfo) {
		try {
			return this.getFactoryDao().findRecords(queryInfo);
		} catch (DAOException e) {
			 
			e.printStackTrace();
		}
		return null;
	}
	
	public boolean addFactory(List<CotFactory> factoryList) {
		try {
			this.getFactoryDao().saveRecords(factoryList);
			//判断是否有厂家联系人邮箱，有的话添加之
			List contactList = new ArrayList();
			for(CotFactory fac : factoryList){
				if(fac.getFactoryEmail() != null && !fac.getFactoryEmail().equals("")){
					CotContact cotContact = new CotContact();
					cotContact.setContactPerson(fac.getShortName());
					cotContact.setContactEmail(fac.getFactoryEmail());
					cotContact.setFactoryId(fac.getId());
					contactList.add(cotContact);
				}
			}
			if(contactList.size() > 0){
				CotContactService contact = (CotContactService)SystemUtil.getService("CotContactService");
				contact.addContact(contactList);
			}
			CotSeqService cotSeqService = new CotSeqServiceImpl();
			cotSeqService.saveSeq("factoryNo");
			//seq.saveSeq("facNo");
			//将厂家添加到数据字典中
			Map res = new HashMap();
			//获取厂家数据
			List reslist = this.getFactoryDao().getRecords("CotFactory");
			HttpSession session =  WebContextFactory.get().getSession();
			Map dicMap = (Map)session.getAttribute("sysdic");
			if(dicMap != null){
				res.put("factory", reslist);
			}
			
			return true;
		} catch (DAOException e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean deleteFactory(List<CotFactory> factoryList) {
		List<Integer> ids = new ArrayList<Integer>();
        for(int i=0; i<factoryList.size(); i++)
		{
			CotFactory cotFactory = (CotFactory)factoryList.get(i);
			ids.add(cotFactory.getId());
		}
        try{
        	this.getFactoryDao().deleteRecordByIds(ids, "CotFactory");
        	return true;
        }
        catch(DAOException e)
        {
        	logger.error("删除工厂信息异常",e);
        	return false;
        }
	}
	
	public int deleteFactoryById(Integer Id) {
		int res = 0;
		try {
			 
			this.getFactoryDao().deleteRecordById(Id,"CotFactory");
		} catch (DAOException e) {
			logger.error("删除工厂信息异常",e);
			res = -1;
		}
		return res;
	}

	public boolean modifyFactory(List<CotFactory> factoryList) {
		 try{
      	   this.getFactoryDao().updateRecords(factoryList);
      	   //判断是否有厂家联系人邮箱，有的话添加之
			List contactList = new ArrayList();
//			for(CotFactory fac : factoryList){
//				if(fac.getFactoryEmail() == null || fac.getFactoryEmail().equals("")){
//					continue;
//				}
//				CotContact cotContact = new CotContact();
//				String strSql = " from CotContact obj where obj.contactEmail = '"+fac.getFactoryEmail()+"' and "+" obj.contactPerson='"+fac.getContactPerson()+"'";
//				List res = this.getFactoryDao().find(strSql);
//				if(res.size() == 0){
//					cotContact.setContactPerson(fac.getShortName());
//					cotContact.setContactEmail(fac.getFactoryEmail());
//					cotContact.setFactoryId(fac.getId());
//					contactList.add(cotContact);
//				}
//			}
			if(contactList.size() > 0){
				CotContactService contact = (CotContactService)SystemUtil.getService("CotContactService");
				contact.addContact(contactList);
			}
			//将厂家添加到数据字典中
			Map res = new HashMap();
			//获取厂家数据
			List reslist = this.getFactoryDao().getRecords("CotFactory");
			HttpSession session =  WebContextFactory.get().getSession();
			Map dicMap = (Map)session.getAttribute("sysdic");
			if(dicMap != null){
				res.put("factory", reslist);
			}
      	   return true;
         }catch(Exception ex)
         {
      	   logger.error("更新工厂信息异常", ex);
      	   return false;
         }
	}
	
	public boolean findExistByName(String name) {
		QueryInfo queryInfo = new QueryInfo();
		boolean isExist = false;
		queryInfo.setSelectString("from CotFactory obj where obj.shortName='"+name+"'");
		queryInfo.setCountQuery("select count(*) from CotFactory obj where obj.shortName='"+name+"'");
		try {
			int count = this.getFactoryDao().getRecordsCount(queryInfo);
			if(count > 0)
				isExist =  true;
		} catch (DAOException e) {
			logger.error("查找重复方法失败", e);
		}
		return isExist;
	}

	public CotFactory getFactoryById(Integer Id) {
		 
		return (CotFactory)this.getFactoryDao().getById(CotFactory.class, Id);
	}

	public List<?> getFactoryList() {
		 
		return this.getFactoryDao().getRecords("CotFactory");
	}

	 
	
	public List<?> getTypeLv1List() {
		 
		return this.getFactoryDao().getRecords("CotTypeLv1");
	}

	/* (non-Javadoc)
	 * @see com.sail.cot.service.system.CotFactoryService#getFacNo()
	 */
	public String getFacNo() {
		//String facNo = seq.getAllSeqByType("facNo", null);
		CotSeqService cotSeqService = new CotSeqServiceImpl();
		String facNo=cotSeqService.getFacNo();
		return facNo;
	}
	
	public boolean findElementsRecordsCount(String factoryId) {
		 
		QueryInfo queryInfo = new QueryInfo();
		boolean isExist = false;
		queryInfo.setSelectString("from CotElementsNew obj where obj.factoryId='"+factoryId+"'");
		queryInfo.setCountQuery("select count(*) from CotElementsNew obj where obj.factoryId='"+factoryId+"'");
		try {
			int count = this.getFactoryDao().getRecordsCount(queryInfo);
			if(count > 0)
				isExist =  true;
		} catch (DAOException e) {
			logger.error("查找记录方法失败", e);
		}
		return isExist;
	}
	
	public String getJsonData(QueryInfo queryInfo) throws DAOException {
		// TODO Auto-generated method stub
		return this.getFactoryDao().getJsonData(queryInfo);
	}
	
	public Integer findExistByNo(String No,String id) {
		String hql = "select obj.id from CotFactory obj where obj.factoryNo='"+No+"'";
		List<?> res = this.getFactoryDao().find(hql);
		if (res.size() == 0) {
			return null;
		}
		if (res.size() == 1) {
			Integer oldId = (Integer) res.get(0);
			if (oldId.toString().equals(id)) {
				return null;
			} else {
				return 1;
			}
		}
		return 2;
	}
	
	//保存和修改
	public boolean saveOrUpdate(CotFactory factory,List<?> contact){
		try {
			List list = new ArrayList();
			list.add(factory);
			this.getFactoryDao().saveOrUpdateRecords(list);
			
			List<CotContact> newList = new ArrayList();
			for (int i = 0; i < contact.size(); i++) {
				CotContact cotContact =  (CotContact)contact.get(i);
				cotContact.setFactoryId(factory.getId());
				newList.add(cotContact);
			}
			if(newList.size()>0){
				this.getFactoryDao().saveRecords(newList);
			}
			
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	//判断厂家简称重复性
	public boolean findIsExistShortName(int id,String shortName){
		boolean isExist = false;
		
		String hql = "from CotFactory obj where obj.shortName='"+shortName+"'";
		List<?> list = this.getFactoryDao().find(hql);
		if (list.size()>=1) {
			CotFactory factory = (CotFactory) list.get(0);
			if (id != factory.getId()) {
				isExist = true;
			}
		}
		return isExist;
	}
	/*
	 * (non-Javadoc)
	 * @see com.sail.cot.service.system.CotFactoryService#getFactoryMailList(com.sail.cot.query.QueryInfo)
	 */
	public List<CotAddressVO> getFactoryMailList(QueryInfo queryInfo){
		List<CotAddressVO> addressList = new ArrayList<CotAddressVO>();
		try {
			List<?> list = this.getFactoryDao().findRecords(queryInfo);
			for (int i = 0; i < list.size(); i++) {
				Object[] objs = (Object[]) list.get(i);
				CotAddressVO addressVO = new CotAddressVO();
				addressVO.setId((Integer) objs[0]);
				addressVO.setName((String) objs[1]); 
				addressVO.setAddressName((String) objs[2]);
				addressVO.setEmail((String) objs[3]);
				addressList.add(addressVO);
			}
			return addressList;
		} catch (DAOException e) {
			logger.error("查询出错",e);
			return null;
		}
	}
	
	// 查询厂家报价VO记录
	public List<?> getPriceFacVO(QueryInfo queryInfo) {
		List<CotPriceFacVO> listVo = new ArrayList<CotPriceFacVO>();
		try {
			List<?> list = this.getFactoryDao().findRecords(queryInfo);
			for (int i = 0; i < list.size(); i++) {
				CotPriceFacVO priceFacVO = new CotPriceFacVO();
				Object[] obj = (Object[]) list.get(i);
				priceFacVO.setId((Integer) obj[0]);
				priceFacVO.setEleId((String) obj[1]);
				priceFacVO.setEleName((String) obj[2]);
				priceFacVO.setAddTime((Date) obj[3]);
				priceFacVO.setPriceFac((Float) obj[4]);
				priceFacVO.setPriceUint((Integer) obj[5]);
				priceFacVO.setShortName((String) obj[6]);
				priceFacVO.setPriceRemark((String) obj[7]);
				listVo.add(priceFacVO);
			}
			return listVo;
		} catch (DAOException e) {
			e.printStackTrace();
			logger.error("查询出错");
			return null;
		}
	}
 }
