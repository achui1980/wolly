package com.sail.cot.service.system.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.jason.core.exception.DAOException;
import com.sail.cot.dao.CotBaseDao;
import com.sail.cot.domain.CotContact;
import com.sail.cot.domain.CotFactory;
import com.sail.cot.query.QueryInfo;
import com.sail.cot.service.system.CotContactService;
import com.sail.cot.util.Log4WebUtil;

public class CotContactServiceImpl implements CotContactService {

	private CotBaseDao cotBaseDao;
	public CotBaseDao getCotBaseDao() {
		return cotBaseDao;
	}

	public void setCotBaseDao(CotBaseDao cotBaseDao) {
		this.cotBaseDao = cotBaseDao;
	}

	private Logger logger = Log4WebUtil.getLogger(CotContactServiceImpl.class);
	
	
	public int getRecordCount(QueryInfo queryInfo) {
		try {
			return this.getCotBaseDao().getRecordsCount(queryInfo);
		} catch (DAOException e) {
			 
			e.printStackTrace();
		}
		return 0;
	}
	
	public List<?> getList(QueryInfo queryInfo) {
		try {
			return this.getCotBaseDao().findRecords(queryInfo);
		} catch (DAOException e) {
			 
			e.printStackTrace();
		}
		return null;
	}
	
	public boolean addContact(List<CotContact> contactList) {
		try {
//			CotContact contact = contactList.get(0);
//			if(contact.getMainFlag()==1){
//				String uphql = "update CotContact obj set obj.mainFlag=2 where obj.factoryId=:factoryId";
//				QueryInfo queryInfo = new QueryInfo();
//				queryInfo.setSelectString(uphql);
//				Map map = new HashMap();
//				map.put("factoryId", contact.getFactoryId());
//				this.getCotBaseDao().executeUpdate(queryInfo, map);
//			}
			this.getCotBaseDao().saveRecords(contactList);
			return true;
		} catch (DAOException e) {
			e.printStackTrace();
			logger.error("添加联系人信息异常",e);
			return false;
		}
	}

	public boolean deleteContact(List<CotContact> contactList) {
		List<Integer> ids=new ArrayList<Integer>();
        for(int i=0; i<contactList.size(); i++)
		{
			CotContact cotContact = (CotContact)contactList.get(i);
			ids.add(cotContact.getId());
		}
         try{
        	this.getCotBaseDao().deleteRecordByIds(ids, "CotContact");
        	return true;
        }
        catch(DAOException e)
        {
        	logger.error("删除联系人信息异常",e);
        	return false;
        }
	}
	
	public boolean modifyContact(List<CotContact> contactList) {
		try{
			//如果该联系人是默认 则把其他默认的修改成非默认
//			CotContact contact = contactList.get(0);
//			if(contact.getMainFlag()!=null && contact.getMainFlag()==1){
//				String hql = "select obj.id from CotContact obj where obj.mainFlag=1 and obj.factoryId="+contact.getFactoryId();
//				List list = this.getCotBaseDao().find(hql);
//				Integer dId = 0;
//				if(list!=null && list.size()>0){
//					dId = (Integer) list.get(0);
//				}
//				if(dId.intValue()!=contact.getId().intValue()){
//					String uphql = "update CotContact obj set obj.mainFlag=2 where obj.id=:id";
//					QueryInfo queryInfo = new QueryInfo();
//					queryInfo.setSelectString(uphql);
//					Map map = new HashMap();
//					map.put("id", dId);
//					this.getCotBaseDao().executeUpdate(queryInfo, map);
//				}
//			}
			this.getCotBaseDao().updateRecords(contactList);
			return true;
		}catch(Exception ex){
	      	logger.error("更新联系人信息异常", ex);
	      	return false;
	    }
	}

	public boolean findExistByName(String str,String factoryId) {
		 
		QueryInfo queryInfo = new QueryInfo();
		boolean isExist = false;
		queryInfo.setSelectString("from CotContact obj where obj.contactPerson='"+str+"' and obj.factoryId ="+ factoryId);
		queryInfo.setCountQuery("select count(*) from CotContact obj where obj.contactPerson='"+str+"' and obj.factoryId ="+ factoryId);
		try {
			int count = this.getCotBaseDao().getRecordsCount(queryInfo);
			if(count > 0)
				isExist =  true;
		} catch (DAOException e) {
			 logger.error("查找重复方法失败", e);
		}
		return isExist;
	}
	@SuppressWarnings("unchecked")
	public Integer findExistByEMail(String email){
		try {
			String hql = "from CotContact obj where obj.contactEmail=?";
			Object[] objs = new Object[1];
			objs[0] = email;
			List<CotContact> contacts = this.getCotBaseDao().queryForLists(hql, objs);
			if(contacts.size() > 0){
				return contacts.get(0).getId();
			}
		} catch (Exception e) {
			logger.error("查找重复方法失败", e);
		}
		return null;
	}

	public CotContact getContactById(Integer Id) {
		 
		return (CotContact)this.getCotBaseDao().getById(CotContact.class, Id);
	}
	
	public CotFactory getFactoryById(Integer Id) {
		 
		return (CotFactory)this.getCotBaseDao().getById(CotFactory.class, Id);
	}

	public List<?> getContactList() {
		 
		return this.getCotBaseDao().getRecords("CotContact");
	}

	public Map<?, ?> getFactoryMap() {
		 
		Map<String, String> map=new HashMap<String, String>();
		List<?> list=this.cotBaseDao.getRecords("CotFactory");
		for(int i=0;i<list.size();i++)
		{
			CotFactory cotFactory=(CotFactory)list.get(i);
			map.put(cotFactory.getId().toString(), cotFactory.getShortName());
		}
		 return map;
	}

	public boolean findContactRecordsCount(String factoryId) {
		 
		QueryInfo queryInfo = new QueryInfo();
		boolean isExist = false;
		queryInfo.setSelectString("from CotContact obj where obj.factoryId='"+factoryId+"'");
		queryInfo.setCountQuery("select count(*) from CotContact obj where obj.factoryId='"+factoryId+"'");
		try {
			int count = this.getCotBaseDao().getRecordsCount(queryInfo);
			if(count > 0)
				isExist =  true;
		} catch (DAOException e) {
			logger.error("查找记录方法失败", e);
		}
		return isExist;
	}
	
	//查询得到json数据
	public String getJsonData(QueryInfo queryInfo) throws DAOException {
		return this.getCotBaseDao().getJsonData(queryInfo);
	}

	//查找是否重复
	public Integer findExistByNo(String contactPerson,String factoryId, String id) {
		String hql = "select obj.id from CotContact obj where obj.contactPerson='"
				+ contactPerson + "' and obj.factoryId ="+factoryId;
		List<?> res = this.getCotBaseDao().find(hql);
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
	public boolean saveOrUpdate(CotContact contact){
		try {
			List list = new ArrayList();
			list.add(contact);
			this.getCotBaseDao().saveOrUpdateRecords(list);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	//判断联系人帐号是否重复 也不能和员工的账户重复
	public boolean findExistLoginName(String loginName,String id) {
		if(loginName==null || loginName.equals("")){
			return true;
		}
		String hql = "select obj.id from CotContact obj where obj.loginName=?";
		List<?> res = this.getCotBaseDao().queryForLists(hql, new Object[]{loginName});
		boolean flag = false;
		if (res.size() == 0) {
			flag=true;
		}
		if (res.size() == 1) {
			Integer oldId = (Integer) res.get(0);
			if (oldId.toString().equals(id)) {
				flag=true;
			}
		}
		if(flag){
			String shql = "select obj.id from CotEmps obj where obj.empsId=?";
			List<?> sres = this.getCotBaseDao().queryForLists(shql, new Object[]{loginName});
			if (sres.size()>=1) {
				flag = false;
			}
		}
		return flag;
	}
}
