package com.sail.cot.service.customer.impl;

import java.io.File;
import java.io.FileInputStream;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.log4j.Logger;
import org.directwebremoting.WebContext;
import org.directwebremoting.WebContextFactory;

import com.jason.core.exception.DAOException;
import com.sail.cot.dao.CotBaseDao;
import com.sail.cot.domain.CotClaim;
import com.sail.cot.domain.CotCompany;
import com.sail.cot.domain.CotCustContact;
import com.sail.cot.domain.CotCustMb;
import com.sail.cot.domain.CotCustPc;
import com.sail.cot.domain.CotCustSeq;
import com.sail.cot.domain.CotCustomer;
import com.sail.cot.domain.CotEmps;
import com.sail.cot.domain.CotFile;
import com.sail.cot.domain.CotNationCity;
import com.sail.cot.domain.CotOrderPc;
import com.sail.cot.domain.CotProvince;
import com.sail.cot.domain.CotRptFile;
import com.sail.cot.domain.vo.CotAddressVO;
import com.sail.cot.domain.vo.CotCfgNo;
import com.sail.cot.query.QueryInfo;
import com.sail.cot.service.customer.CotCustomerService;
import com.sail.cot.service.fittings.impl.CotFittingsServiceImpl;
import com.sail.cot.service.img.CotOpImgService;
import com.sail.cot.service.order.impl.CotOrderServiceImpl;
import com.sail.cot.service.sample.impl.CotElementsServiceImpl;
import com.sail.cot.service.system.CotSeqService;
import com.sail.cot.service.system.impl.CotSeqServiceImpl;
import com.sail.cot.service.system.impl.SetNoServiceImpl;
import com.sail.cot.util.Log4WebUtil;
import com.sail.cot.util.SystemUtil;
import com.sailing.oa.domain.CotStatData;

public class CotCustomerServiceImpl implements CotCustomerService {

	private CotBaseDao cotBaseDao;

	// private GenAllSeq seq = new GenAllSeq();
	public CotBaseDao getCotBaseDao() {
		return cotBaseDao;
	}

	public void setCotBaseDao(CotBaseDao cotBaseDao) {
		this.cotBaseDao = cotBaseDao;
	}

	private Logger logger = Log4WebUtil.getLogger(CotCustomerServiceImpl.class);

	public boolean saveCustomer(CotCustomer cotCustomer, String photoPath,
			String MbPath, String addTime) {
		Integer id = this.getIdByNo(cotCustomer.getCustomerNo());
		if (id != null) {
			return false;
		}
		if (!"".equals(addTime)) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			// 添加客户信息
			try {
				cotCustomer.setAddTime(new Timestamp(sdf.parse(addTime)
						.getTime()));
			} catch (ParseException e1) {
				e1.printStackTrace();
				return false;
			}
		} else {
			cotCustomer.setAddTime(null);
		}

		String custPhotoPath = systemPath + photoPath;
		String custMbPath = systemPath + MbPath;
		File custPhotoFile = new File(custPhotoPath);
		File custMbFile = new File(custMbPath);
		try {
			if (custPhotoFile.exists()) {
				FileInputStream in1 = new FileInputStream(custPhotoFile);
				byte[] b = new byte[(int) (custPhotoFile.length())];
				while (in1.read(b) != -1) {
				}
				in1.close();
				cotCustomer.setCustImg(b);
				if (custPhotoPath.indexOf("common/images/zwtp.png") < 0) {
					custPhotoFile.delete();
				}
			}
			/*
			 * else{ String noPicPath = systemPath+"common/images/zwtp.png";
			 * File noPicFile = new File(noPicPath); FileInputStream in2 = new
			 * FileInputStream(noPicFile); byte[] b = new
			 * byte[(int)(noPicFile.length())]; while (in2.read(b)!= -1) {}
			 * in2.close(); cotCustomer.setCustImg(b); }
			 */
			CotCustMb cotCustMb = null;
			// if(custMbFile.exists()){
			// FileInputStream in3 = new FileInputStream(custMbFile);
			// byte[] b = new byte[(int)(custMbFile.length())];
			// while (in3.read(b)!= -1) {}
			// in3.close();
			// //cotCustomer.setPicImg(b);
			// //新建客户麦标
			// cotCustMb = new CotCustMb();
			// cotCustMb.setPicImg(b);
			// cotCustMb.setPicSize(b.length);
			// if(custMbPath.indexOf("common/images/zwtp.png")<0){
			// custMbFile.delete();
			// }
			// }
			/*
			 * else{ String noPicPath = systemPath+"common/images/zwtp.png";
			 * File noPicFile = new File(noPicPath); FileInputStream in4 = new
			 * FileInputStream(noPicFile); byte[] b = new
			 * byte[(int)(noPicFile.length())]; while (in4.read(b)!= -1) {}
			 * in4.close(); cotCustomer.setPicImg(b); }
			 */

			this.getCotBaseDao().create(cotCustomer);
			// seq.saveSeq("custNo");
			CotSeqService cotSeqService = new CotSeqServiceImpl();
			cotSeqService.saveSeq("cust");

			if (cotCustomer.getPriContact() != null
					&& !cotCustomer.getPriContact().trim().equals("")) {
				System.out.println(cotCustomer.getPriContact());
				CotCustContact custContact = new CotCustContact();
				custContact.setCustomerId(cotCustomer.getId());
				custContact.setContactEmail(cotCustomer.getCustomerEmail());
				custContact.setContactPerson(cotCustomer.getPriContact());
				List<CotCustContact> custContactList = new ArrayList<CotCustContact>();
				custContactList.add(custContact);
				this.getCotBaseDao().saveOrUpdateRecords(custContactList);
			}

			// 保存麦标
			if (cotCustMb != null) {
				List list = new ArrayList();
				cotCustMb.setFkId(cotCustomer.getId());
				list.add(cotCustMb);
				this.getCotBaseDao().saveRecords(list);
			}
			return true;

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public int deleteCustomer(List<CotCustomer> CustomerList) {
		List<Integer> ids = new ArrayList<Integer>();
		int res = 0;
		for (int i = 0; i < CustomerList.size(); i++) {
			CotCustomer cotCustomer = (CotCustomer) CustomerList.get(i);
			String customPhoto = cotCustomer.getCustomPhoto();
			String customMb = cotCustomer.getCustomerMb();
			this.delPicByPath(customMb);
			this.delPicByPath(customPhoto);
			ids.add(cotCustomer.getId());
		}
		try {
			this.getCotBaseDao().deleteRecordByIds(ids, "CotCustomer");
		} catch (DAOException e) {
			logger.error("删除客户信息异常", e);
			res = -1;
		}
		return res;
	}

	public int deleteCustomerById(Integer Id) {
		int res = 0;
		try {
			this.getCotBaseDao().deleteRecordById(Id, "CotCustomer");
		} catch (DAOException e) {
			logger.error("删除客户信息异常", e);
			res = -1;

		}
		return res;
	}

	public void deleteCustomPhoto(String customPhoto) {
		if (customPhoto != null) {
			String classPath = CotElementsServiceImpl.class.getResource("/")
					.toString();
			String systemPath = classPath.substring(6, classPath.length() - 16);
			System.out.println(systemPath + customPhoto);
			File f = new File(systemPath + customPhoto);
			f.delete();
		}
	}

	// 通过查找相应的联系人
	public CotCustContact getCustContactBycustId(Integer id,
			String contactPerson) {
		String strSql = " from CotCustContact obj where obj.contactPerson ='"
				+ contactPerson + "' and obj.customerId=" + id;
		List<CotCustContact> res = this.getCotBaseDao().find(strSql);
		if (res.size() > 0) {
			CotCustContact contact = (CotCustContact) res.get(0);
			return contact;
		} else {
			return null;
		}
	}

	public boolean modifyCustomer(CotCustomer cotCustomer, String addTime) {
		Integer id = this.getIdByNo(cotCustomer.getCustomerNo());
		System.out.println(cotCustomer.getEmpId());
		if (id != null && !id.toString().equals(cotCustomer.getId().toString())) {
			return false;
		}
		// 修改客户信息
		// cotCustomer.setAddTime(new Timestamp(new
		// Long(System.currentTimeMillis())));
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		// 添加客户信息
		try {
			if (!"".equals(addTime)) {
				cotCustomer.setAddTime(new Timestamp(sdf.parse(addTime)
						.getTime()));
			} else {
				cotCustomer.setAddTime(null);
			}
		} catch (ParseException e1) {
			e1.printStackTrace();
			return false;
		}

		// 克隆对象,避免造成指针混用
		CotCustomer cloneObj = (CotCustomer) SystemUtil.deepClone(cotCustomer);
		byte[] custImg = this.getCustImgById(cotCustomer.getId());
		byte[] picImg = this.getPicImgById(cotCustomer.getId());
		cloneObj.setCustImg(custImg);
		// cloneObj.setPicImg(picImg);

		List<CotCustomer> records = new ArrayList<CotCustomer>();
		records.add(cloneObj);
		try {
			String contactPerson = cotCustomer.getPriContact();
			int custId = cotCustomer.getId();
			CotCustContact custContact = this.getCustContactBycustId(custId,
					contactPerson);
			this.getCotBaseDao().saveOrUpdateRecords(records);

			if (custContact != null) {
				custContact.setCustomerId(cotCustomer.getId());
				custContact.setContactEmail(cotCustomer.getCustomerEmail());
				custContact.setContactPerson(cotCustomer.getPriContact());
				List<CotCustContact> custContactList = new ArrayList<CotCustContact>();
				custContactList.add(custContact);
				this.getCotBaseDao().saveOrUpdateRecords(custContactList);
			} else {
				String contact = cotCustomer.getPriContact();
				if (!contact.trim().equals("")) {
					CotCustContact custCt = new CotCustContact();
					custCt.setCustomerId(cotCustomer.getId());
					custCt.setContactEmail(cotCustomer.getCustomerEmail());
					custCt.setContactPerson(cotCustomer.getPriContact());
					List<CotCustContact> custContactList = new ArrayList<CotCustContact>();
					custContactList.add(custCt);
					this.getCotBaseDao().saveOrUpdateRecords(custContactList);
				}
			}
			// this.getCotBaseDao().saveOrUpdateRecords(records);

		} catch (Exception ex) {
			ex.printStackTrace();
			logger.error("保存客户错误!");
			return false;
		}
		return true;
	}

	public Integer getIdByNo(String customerNo) {
		List<?> list = this.getCotBaseDao().find(
				"select obj.id from CotCustomer obj where obj.customerNo='"
						+ customerNo + "'");
		if (list.size() > 0) {
			Integer id = (Integer) list.get(0);
			return id;
		} else {
			return null;
		}
	}

	public boolean findExistPhotoName(String customPhoto) {
		QueryInfo queryInfo = new QueryInfo();
		boolean isExist = false;
		queryInfo
				.setSelectString("from CotCustomer obj where obj.customPhoto='"
						+ customPhoto + "'");
		queryInfo
				.setCountQuery("select count(*) from CotCustomer obj where obj.customPhoto='"
						+ customPhoto + "'");
		try {
			int count = this.getCotBaseDao().getRecordsCount(queryInfo);
			if (count > 0)
				isExist = true;
		} catch (DAOException e) {

			logger.error("查找重复方法失败", e);
		}
		return isExist;
	}

	public boolean findExistMb(String customerMb) {
		QueryInfo queryInfo = new QueryInfo();
		boolean isExist = false;
		queryInfo.setSelectString("from CotCustomer obj where obj.customerMb='"
				+ customerMb + "'");
		queryInfo
				.setCountQuery("select count(*) from CotCustomer obj where obj.customerMb='"
						+ customerMb + "'");
		try {
			int count = this.getCotBaseDao().getRecordsCount(queryInfo);
			if (count > 0)
				isExist = true;
		} catch (DAOException e) {

			logger.error("查找重复方法失败", e);
		}
		return isExist;
	}

	// 通过id获取索赔信息
	public CotClaim getClaimById(Integer Id) {
		CotClaim cotClaim = (CotClaim) this.getCotBaseDao().getById(
				CotClaim.class, Id);
		return cotClaim;
	}

	// 添加或保存客户索赔信息
	public boolean saveOrUpdateClaim(CotClaim cotClaim) {
		// 获取登录人
		WebContext ctx = WebContextFactory.get();
		CotEmps cotEmps = (CotEmps) ctx.getSession().getAttribute("emp");
		try {
			cotClaim.setAddPerson(cotEmps.getId());
			cotClaim.setAddTime(new Date(System.currentTimeMillis()));// 添加时间
			List<CotClaim> records = new ArrayList<CotClaim>();
			records.add(cotClaim);
			this.getCotBaseDao().saveOrUpdateRecords(records);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

	}

	// 批量删除客户索赔信息
	public boolean deleteClaimByList(List<Integer> ids) {
		try {
			this.getCotBaseDao().deleteRecordByIds(ids, "CotClaim");
			return true;
		} catch (DAOException e) {
			e.printStackTrace();
			return false;
		}
	}

	public CotCustomer getCustomerById(Integer Id) {
		CotCustomer cotCustomer = (CotCustomer) this.getCotBaseDao().getById(
				CotCustomer.class, Id);
		if (cotCustomer != null) {
			CotCustomer custClone = (CotCustomer) SystemUtil
					.deepClone(cotCustomer);
			custClone.setPicImg(null);
			custClone.setCustImg(null);
			custClone.setCustomerClaim(null);
			custClone.setCotCustContacts(null);
			custClone.setCustomerVisitedLogs(null);
			return custClone;
		}
		return null;
	}

	public List<?> getCustomerList() {
		List<CotCustomer> customerList = new ArrayList<CotCustomer>();
		List<?> list = this.getCotBaseDao().getRecords("CotCustomer");

		for (int i = 0; i < list.size(); i++) {
			CotCustomer cotCustomer = (CotCustomer) list.get(i);
			CotCustomer clone = (CotCustomer) SystemUtil.deepClone(cotCustomer);
			clone.setPicImg(null);
			clone.setCustImg(null);
			clone.setCustomerClaim(null);
			clone.setCotCustContacts(null);
			clone.setCustomerVisitedLogs(null);
			customerList.add(clone);
		}
		return customerList;
	}

	@SuppressWarnings("unchecked")
	public List<?> getEmpsList() {
		List<CotEmps> empsList = new ArrayList<CotEmps>();
		List<CotEmps> list = this.getCotBaseDao().getRecords("CotEmps");
		if (list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				CotEmps cotEmps = list.get(i);
				cotEmps.setCustomers(null);
				empsList.add(cotEmps);
			}
			return empsList;
		}
		return null;
	}

	public List<?> getClauseList() {

		return this.getCotBaseDao().getRecords("CotClause");
	}

	public List<?> getCommisionTypeList() {

		return this.getCotBaseDao().getRecords("CotCommisionType");
	}

	public List<?> getCustomerLvList() {

		return this.getCotBaseDao().getRecords("CotCustomerLv");
	}

	public List<?> getCustomerTypeList() {

		return this.getCotBaseDao().getRecords("CotCustomerType");
	}

	public List<?> getNationList() {

		return this.getCotBaseDao().getRecords("CotNation");
	}

	public List<?> getPayTypeList() {

		return this.getCotBaseDao().getRecords("CotPayType");
	}

	public List<?> getShipPortList() {

		return this.getCotBaseDao().getRecords("CotShipPort");
	}

	public List<?> getTargetPortList() {

		return this.getCotBaseDao().getRecords("CotTargetPort");
	}

	public List<?> getProvinceList() {

		return this.getCotBaseDao().getRecords("CotProvince");
	}

	public List<?> getNationCityList() {

		return this.getCotBaseDao().getRecords("CotNationCity");
	}

	@SuppressWarnings("unchecked")
	public List<CotProvince> getProvinceListByNationId(Integer nationId) {

		return this.getCotBaseDao().find(
				"from CotProvince obj where obj.nationId=" + nationId);
	}

	@SuppressWarnings("unchecked")
	public List<CotNationCity> getNationCityListByProvinceId(Integer provinceId) {

		return this.getCotBaseDao().find(
				"from CotNationCity obj where obj.provinceId=" + provinceId);
	}

	public Timestamp getAddTime() {
		Timestamp time = new Timestamp(new Long(System.currentTimeMillis()));
		return time;
	}

	public List<?> getList(QueryInfo queryInfo) {

		try {
			return this.getCotBaseDao().findRecords(queryInfo);
		} catch (DAOException e) {

			e.printStackTrace();
		}
		return null;
	}

	public int getRecordCount(QueryInfo queryInfo) {

		try {
			return this.getCotBaseDao().getRecordsCount(queryInfo);
		} catch (DAOException e) {

			e.printStackTrace();
		}
		return 0;
	}

	// 根据图片路径删除图片
	public Boolean delPicByPath(String picPath) {

		if (picPath != null && picPath != "") {
			String delPicPath = systemPath + picPath;
			try {
				File delPic = new File(delPicPath);
				if (delPicPath.indexOf("common/images/zwtp.png") < 0) {
					delPic.delete();
				}
				return true;
			} catch (Exception e) {
				System.out.println("删除上传图片操作出错");
				e.printStackTrace();
				return false;
			}
		}
		return false;
	}

	// 获取业务员映射
	public Map<?, ?> getEmpMap() {

		Map<String, String> map = new HashMap<String, String>();
		List<?> list = this.cotBaseDao.getRecords("CotEmps");
		for (int i = 0; i < list.size(); i++) {
			CotEmps emp = (CotEmps) list.get(i);
			map.put(emp.getId().toString(), emp.getEmpsName());
		}
		return map;
	}

	// 通过部门id获取所在部门员工列表
	@SuppressWarnings("unchecked")
	public List<CotEmps> getDeptEmpListByDeptId(Integer deptId) {

		List<CotEmps> deptEmpList = this.getCotBaseDao().find(
				"from CotEmps obj where obj.deptId=" + deptId);
		if (deptEmpList.size() > 0) {
			return deptEmpList;
		}
		return null;
	}

	// 通过员工id获取所在员工信息
	@SuppressWarnings("unchecked")
	public List<CotEmps> getEmpListByEmpId(Integer empId) {
		List<CotEmps> list = this.getCotBaseDao().find(
				"from CotEmps obj where obj.id=" + empId);
		List<CotEmps> empsList = new ArrayList<CotEmps>();
		if (list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				CotEmps cotEmps = list.get(i);
				cotEmps.setCustomers(null);
				empsList.add(cotEmps);
			}
			return empsList;
		}
		return null;
	}

	// 获取系统路径
	String classPath = CotCustomerServiceImpl.class.getResource("/").toString();
	String systemPath = classPath.substring(6, classPath.length() - 16);

	// 更新数据路中的唛标字段
	public void updatePicImg(String filePath, Integer Id) {
		List<?> list = this.getCotBaseDao().find(
				"from CotCustMb obj where obj.fkId=" + Id);
		CotCustMb cotCustMb = new CotCustMb();
		if (list.size() == 1) {
			cotCustMb = (CotCustMb) list.get(0);
		}
		File picFile = new File(filePath);
		try {
			FileInputStream in = new FileInputStream(picFile);
			byte[] b = new byte[(int) (picFile.length())];
			while (in.read(b) != -1) {
			}
			in.close();

			cotCustMb.setFkId(Id);
			cotCustMb.setPicImg(b);
			cotCustMb.setPicSize(b.length);

			List<CotCustMb> records = new ArrayList<CotCustMb>();
			records.add(cotCustMb);
			this.getCotBaseDao().saveOrUpdateRecords(records);

			if (filePath.indexOf("common/images/zwtp.png") < 0) {
				picFile.delete();
			}
		} catch (Exception e) {
			logger.error("修改客户唛标错误!");
		}
	}

	// 删除数据库中的唛标
	public boolean deletePicImg(Integer Id) {
		String filePath = systemPath + "common/images/zwtp.png";
		String hql = "from CotCustMb obj where obj.fkId=" + Id;
		List list = this.getCotBaseDao().find(hql);
		CotCustMb cotCustMb = (CotCustMb) list.get(0);

		File picFile = new File(filePath);
		FileInputStream in;
		try {
			in = new FileInputStream(picFile);
			byte[] b = new byte[(int) picFile.length()];
			while (in.read(b) != -1) {
			}
			in.close();
			cotCustMb.setPicImg(b);
			cotCustMb.setPicSize(b.length);
			this.getCotBaseDao().update(cotCustMb);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("删除唛标图片错误!");
			return false;
		}
	}

	// 根据id查找数据库中的唛标字段
	public byte[] getPicImgById(Integer Id) {
		List<?> list = this.getCotBaseDao().find(
				"from CotCustMb obj where obj.fkId=" + Id);
		CotCustMb cotCustMb = new CotCustMb();
		if (list.size() == 1) {
			cotCustMb = (CotCustMb) list.get(0);
		}
		if (cotCustMb != null) {
			return cotCustMb.getPicImg();
		}
		return null;
	}

	// 更新数据路中的客户照片字段
	public void updateCustImg(String filePath, Integer Id) {

		byte[] picImg = this.getPicImgById(Id);
		CotCustomer cotCustomer = this.getCustomerById(Id);
		// 克隆对象,避免造成指针混用
		CotCustomer cloneObj = (CotCustomer) SystemUtil.deepClone(cotCustomer);
		// cloneObj.setPicImg(picImg);

		File picFile = new File(filePath);
		try {
			FileInputStream in = new FileInputStream(picFile);
			byte[] b = new byte[(int) (picFile.length())];
			while (in.read(b) != -1) {
			}
			in.close();
			cloneObj.setCustImg(b);

			List<CotCustomer> records = new ArrayList<CotCustomer>();
			records.add(cloneObj);
			this.getCotBaseDao().updateRecords(records);

			if (filePath.indexOf("common/images/zwtp.png") < 0) {
				picFile.delete();
			}
		} catch (Exception e) {
			logger.error("修改客户照片错误!");
		}
	}

	// 删除数据库中的客户照片
	public boolean deleteCustImg(Integer Id) {
		String filePath = systemPath + "common/images/zwtp.png";
		File picFile = new File(filePath);
		FileInputStream in;
		CotCustomer cotCustomer = this.getCustomerById(Id);
		try {
			in = new FileInputStream(picFile);
			byte[] b = new byte[(int) (picFile.length())];
			while (in.read(b) != -1) {
			}
			in.close();
			cotCustomer.setCustImg(b);
			List list = new ArrayList();
			list.add(cotCustomer);
			this.getCotBaseDao().updateRecords(list);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("删除客户照片错误!");
			return false;
		}
	}

	// 根据id查找数据库中的客户照片字段
	public byte[] getCustImgById(Integer Id) {
		CotCustomer cotCustomer = (CotCustomer) this.getCotBaseDao().getById(
				CotCustomer.class, Id);
		if (cotCustomer != null) {
			byte[] custImg = cotCustomer.getCustImg();
			if (custImg != null) {
				return custImg;
			}
		}
		return null;
	}

	public boolean checkMailAdd(Integer customerId) {
		boolean flag = true;
		CotCustomer cotCustomer = (CotCustomer) this.getCotBaseDao().getById(
				CotCustomer.class, customerId);
		if (cotCustomer.getCustomerEmail().equals("")
				|| cotCustomer.getCustomerEmail() == null) {
			flag = false;
		}
		return flag;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sail.cot.service.customer.CotCustomerService#updateCustSeq(java.lang.Integer,
	 *      java.lang.Integer)
	 */
	public boolean updateCustSeq(Integer custId, Integer custSeq, String key) {
		CotCustSeq cust = (CotCustSeq) this.getCotBaseDao().getById(
				CotCustSeq.class, custId);
		if (cust == null)
			return false;
		try {
			BeanUtils.setProperty(cust, key, custSeq);
		} catch (Exception e) {
			e.printStackTrace();
		}
		List res = new ArrayList();
		res.add(cust);
		this.getCotBaseDao().updateRecords(res);
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sail.cot.service.customer.CotCustomerService#getCustSeqByCustId(java.lang.Integer)
	 */
	public CotCustSeq getCustSeqByCustId(Integer custId, String currdate) {
		// TODO Auto-generated method stub
		CotCustSeq custSeq = null;
		String strSql = " from  CotCustSeq obj where obj.custId=" + custId
				+ " and obj.currDate='" + currdate + "'";
		List seq = this.getCotBaseDao().find(strSql);
		if (seq == null || seq.size() == 0)
			custSeq = null;
		else
			custSeq = (CotCustSeq) seq.get(0);
		if (custSeq == null) {
			custSeq = new CotCustSeq();
			custSeq.setCustId(custId);
			custSeq.setGivenSeq(0);
			custSeq.setOrderfacSeq(0);
			custSeq.setOrderoutSeq(0);
			custSeq.setOrderSeq(0);
			custSeq.setPriceSeq(0);
			custSeq.setSignSeq(0);
			custSeq.setSplitSeq(0);
		}
		return custSeq;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sail.cot.service.customer.CotCustomerService#updateCustSeqByType(com.sail.cot.domain.CotCustSeq,
	 *      java.lang.String)
	 */
	public boolean updateCustSeqByType(Integer custId, String type,
			String currDate) {
		SetNoServiceImpl setNoService = new SetNoServiceImpl();
		CotCfgNo cotCfgNo = setNoService.getNoMap();
		String key = "";
		Integer currSeq = 0;
		String orderno = "";
		int zeroType = 0;
		if ("price".equals(type))
			orderno = cotCfgNo.getPriceNo();
		else if ("order".equals(type))
			orderno = cotCfgNo.getOrderNo();
		else if ("orderfac".equals(type))
			orderno = cotCfgNo.getOrderFacNo();
		else if ("orderout".equals(type))
			orderno = cotCfgNo.getOrderOutNo();
		else if ("given".equals(type))
			orderno = cotCfgNo.getGivenNo();
		else if ("sign".equals(type))
			orderno = cotCfgNo.getSignNo();
		else if ("split".equals(type))
			orderno = cotCfgNo.getContainerNo();

		else if ("finacerecv".equals(type))
			orderno = cotCfgNo.getFinacerecvNo();
		else if ("finacegiven".equals(type))
			orderno = cotCfgNo.getFinacegivenNo();
		else if ("fincaeaccountdeal".equals(type))
			orderno = cotCfgNo.getFincaeaccountdealNo();
		else if ("fincaeaccountrecv".equals(type))
			orderno = cotCfgNo.getFincaeaccountrecvNo();
		else if ("backtax".equals(type))
			orderno = cotCfgNo.getBacktaxNo();
		else if ("audit".equals(type))
			orderno = cotCfgNo.getAuditNo();
		else if ("packing".equals(type))
			orderno = cotCfgNo.getPackingNo();
		else if ("access".equals(type))
			orderno = cotCfgNo.getAccessNo();

		if (orderno.indexOf("KHSEQ") < 0) // 没有指定客户序列号，就不做更新操作
			return false;
		CotCustSeq custSeq = null;
		try {
			String strSql = " from  CotCustSeq obj where obj.custId=" + custId
					+ " and obj.currDate='" + currDate + "'";
			List seq = this.getCotBaseDao().find(strSql);
			if (seq == null || seq.size() == 0)
				custSeq = null;
			else
				custSeq = (CotCustSeq) seq.get(0);
		} catch (Exception ex) {
			custSeq = null;
		}
		if (custSeq == null)
			return false;
		Calendar currcal = Calendar.getInstance(); // 获取历史比对时间
		Calendar today = Calendar.getInstance(); // 获取当前时间
		currcal.setTime(custSeq.getCurrDate());

		if ("price".equals(type)) {
			zeroType = Integer.parseInt(cotCfgNo.getPriceZeroType());
			key = "priceSeq";
			currSeq = custSeq.getPriceSeq() == null ? 0 : custSeq.getPriceSeq();
			orderno = cotCfgNo.getPriceNo();
		} else if ("order".equals(type)) {
			zeroType = Integer.parseInt(cotCfgNo.getOrderZeroType());
			key = "orderSeq";
			currSeq = custSeq.getOrderSeq() == null ? 0 : custSeq.getOrderSeq();
			orderno = cotCfgNo.getOrderNo();
		} else if ("orderfac".equals(type)) {
			zeroType = Integer.parseInt(cotCfgNo.getOrderfacZeroType());
			key = "orderfacSeq";
			currSeq = custSeq.getOrderfacSeq() == null ? 0 : custSeq
					.getOrderfacSeq();
			orderno = cotCfgNo.getOrderFacNo();
		} else if ("orderout".equals(type)) {
			zeroType = Integer.parseInt(cotCfgNo.getOrderoutZeroType());
			key = "orderoutSeq";
			currSeq = custSeq.getOrderoutSeq() == null ? 0 : custSeq
					.getOrderoutSeq();
			orderno = cotCfgNo.getOrderOutNo();
		} else if ("given".equals(type)) {
			zeroType = Integer.parseInt(cotCfgNo.getGivenZeroType());
			key = "givenSeq";
			currSeq = custSeq.getGivenSeq() == null ? 0 : custSeq.getGivenSeq();
			orderno = cotCfgNo.getGivenNo();
		} else if ("sign".equals(type)) {
			zeroType = Integer.parseInt(cotCfgNo.getSignZeroType());
			key = "signSeq";
			currSeq = custSeq.getSignSeq() == null ? 0 : custSeq.getSignSeq();
			orderno = cotCfgNo.getSignNo();
		} else if ("split".equals(type)) {
			zeroType = Integer.parseInt(cotCfgNo.getSplitZeroType());
			key = "splitSeq";
			currSeq = custSeq.getSplitSeq() == null ? 0 : custSeq.getSplitSeq();
			orderno = cotCfgNo.getContainerNo();
		} else if ("finacerecv".equals(type)) {
			zeroType = Integer.parseInt(cotCfgNo.getFinacerecvZeroType());
			key = "finacerecvSeq";
			currSeq = custSeq.getFinacerecvSeq() == null ? 0 : custSeq
					.getFinacerecvSeq();
			orderno = cotCfgNo.getFinacerecvNo();
		} else if ("finacegiven".equals(type)) {
			zeroType = Integer.parseInt(cotCfgNo.getGivenZeroType());
			key = "finacegivenSeq";
			currSeq = custSeq.getFinacegivenSeq() == null ? 0 : custSeq
					.getFinacegivenSeq();
			orderno = cotCfgNo.getFinacegivenNo();
		} else if ("fincaeaccountdeal".equals(type)) {
			zeroType = Integer
					.parseInt(cotCfgNo.getFincaeaccountdealZeroType());
			key = "fincaeaccountdealSeq";
			currSeq = custSeq.getFincaeaccountdealSeq() == null ? 0 : custSeq
					.getFincaeaccountdealSeq();
			orderno = cotCfgNo.getFincaeaccountdealNo();
		} else if ("fincaeaccountrecv".equals(type)) {
			zeroType = Integer
					.parseInt(cotCfgNo.getFincaeaccountrecvZeroType());
			key = "fincaeaccountrecvSeq";
			currSeq = custSeq.getFincaeaccountrecvSeq() == null ? 0 : custSeq
					.getFincaeaccountrecvSeq();
			orderno = cotCfgNo.getFincaeaccountrecvNo();
		} else if ("backtax".equals(type)) {
			zeroType = Integer.parseInt(cotCfgNo.getBacktaxZeroType());
			key = "backtaxSeq";
			currSeq = custSeq.getBacktaxSeq() == null ? 0 : custSeq
					.getBacktaxSeq();
			orderno = cotCfgNo.getBacktaxNo();
		} else if ("audit".equals(type)) {
			zeroType = Integer.parseInt(cotCfgNo.getAuditZeroType());
			key = "auditSeq";
			currSeq = custSeq.getAuditSeq() == null ? 0 : custSeq.getAuditSeq();
			orderno = cotCfgNo.getAuditNo();
		} else if ("packing".equals(type)) {
			zeroType = Integer.parseInt(cotCfgNo.getPackingZeroType());
			key = "packingSeq";
			currSeq = custSeq.getPackingSeq() == null ? 0 : custSeq
					.getPackingSeq();
			orderno = cotCfgNo.getPackingNo();
		} else if ("access".equals(type)) {
			zeroType = Integer.parseInt(cotCfgNo.getAccessZeroType());
			key = "accessSeq";
			currSeq = custSeq.getAccessSeq() == null ? 0 : custSeq
					.getAccessSeq();
			orderno = cotCfgNo.getAccessNo();
		}

		try {
			if (orderno.indexOf("KHSEQ") >= 0) // 有配置客户序列号才能更新
			{
				switch (zeroType) {
				case 1: // 按年归档
				{

					if (today.get(Calendar.YEAR) != currcal.get(Calendar.YEAR))
						BeanUtils.setProperty(custSeq, key, 0);
					else
						BeanUtils.setProperty(custSeq, key, currSeq + 1);
					break;
				}
				case 2: // 按月归档
				{
					if (today.get(Calendar.MONTH) != currcal
							.get(Calendar.MONTH))
						BeanUtils.setProperty(custSeq, key, 0);
					else
						BeanUtils.setProperty(custSeq, key, currSeq + 1);
					break;
				}
				case 3: // 按日归档
					// {
					// if(today.get(Calendar.DATE) !=
					// currcal.get(Calendar.DATE))
					// BeanUtils.setProperty(custSeq, key, 0);
					// else
					// BeanUtils.setProperty(custSeq, key, currSeq + 1);
					// break;
					// }
				default: // 默认系统
					BeanUtils.setProperty(custSeq, key, currSeq + 1);
				}
			}
			if (orderno.indexOf("2KHSEQ") >= 0 && (currSeq + 1) > 99)
				BeanUtils.setProperty(custSeq, key, 0);
			if (orderno.indexOf("3KHSEQ") >= 0 && (currSeq + 1) > 999)
				BeanUtils.setProperty(custSeq, key, 0);
			if (orderno.indexOf("4KHSEQ") >= 0 && (currSeq + 1) > 9999)
				BeanUtils.setProperty(custSeq, key, 0);
		}

		catch (Exception ex) {
			ex.printStackTrace();
		}
		// custSeq.setCurrDate(new Date(System.currentTimeMillis()));

		List res = new ArrayList();
		res.add(custSeq);
		this.getCotBaseDao().updateRecords(res);
		return true;
	}

	private String getCurrentDate() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar rightNow = Calendar.getInstance();
		java.util.Date now = (java.util.Date) rightNow.getTime();
		String today = sdf.format(now).toString();
		return today;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sail.cot.service.customer.CotCustomerService#getCustNo()
	 */
	// public String getCustNo() {
	// String custNo = seq.getAllSeqByType("custNo", null);
	// System.out.println(custNo);
	// return custNo;
	// }
	// 查询客户简称是否存在
	public Integer findIsExistShortName(String customerShortName, String id) {
		String hql = "select obj.id from CotCustomer obj where obj.customerShortName=?";
		Object[] values = new Object[1];
		values[0] = customerShortName;
		List<?> res = this.getCotBaseDao().queryForLists(hql, values);
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

	// 查询客户英文名是否存在
	public Integer findIsExistEnName(String fullNameEn, String id) {
		String hql = "select obj.id from CotCustomer obj where obj.fullNameEn='"
				+ fullNameEn + "'";
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

	// 统计信息
	public CotStatData statData() {
		CotStatData data = new CotStatData();
		List<Integer> list = new ArrayList<Integer>();
		String[] objNames = { "CotFactory", "CotCustomer", "CotEmps",
				"CotElementsNew", "CotElementsChild", "CotGiven", "CotSign",
				"CotGivenDetail", "CotOrder", "CotOrderDetail", "CotOrderFac",
				"CotOrderFacdetail", "CotPrice", "CotPriceDetail",
				"CotFittingOrder", "CotFittingsOrderdetail",
				"CotFinaceAccountdeal", "CotFinaceAccountrecv",
				"CotFinacegiven", "CotFinacerecv" };
		for (int i = 0; i < objNames.length; i++) {
			Integer nums = this.statRows(objNames[i]);
			list.add(nums);
		}

		String custName = this.getCompany();
		data.setCustName(custName);
		data.setFacNum(list.get(0));
		data.setCustNum(list.get(1));
		data.setEmpsNum(list.get(2));
		data.setEleNum(list.get(3));
		data.setSubEleNum(list.get(4));
		data.setGivenNum(list.get(5));
		data.setSignNum(list.get(6));
		data.setSignDetailNum(list.get(7));
		data.setOrderNum(list.get(8));
		data.setOrderDetailNum(list.get(9));
		data.setOrderfacNum(list.get(10));
		data.setOrderfacDetailNum(list.get(11));
		data.setPriceNum(list.get(12));
		data.setPriceDetailNum(list.get(13));
		data.setFitorderNum(list.get(14));
		data.setFitorderDetailNum(list.get(15));
		data.setAccountdealNum(list.get(16));
		data.setAccountrecvNum(list.get(17));
		data.setFinacegivenNum(list.get(18));
		data.setFinacerecvNum(list.get(19));

		return data;
	}

	// 统计表行数
	public Integer statRows(String objName) {

		String hql = null;
		if ("CotElementsChild".equals(objName)) {
			hql = " from CotElementsNew as obj where obj.eleParentId is not null";
		} else {
			hql = " from " + objName + " as obj";
		}

		List<?> rows = this.getCotBaseDao().find(hql);
		if (rows != null) {
			return rows.size();
		} else {
			return 0;
		}
	}

	// 获取默认公司
	public String getCompany() {
		String hql = " from CotCompany obj where obj.companyIsdefault = 1";
		List<?> list = this.getCotBaseDao().find(hql);
		if (list.size() > 0) {
			CotCompany company = (CotCompany) list.get(0);
			if (company.getCompanyShortName() != null
					&& !"".equals(company.getCompanyShortName())) {
				return company.getCompanyShortName();
			}
		}
		return null;
	}

	// 得到总记录数
	public Integer getRecordCountJDBC(QueryInfo queryInfo) {
		return this.getCotBaseDao().getRecordsCountJDBC(queryInfo);
	}

	// 查询客户VO
	public List<?> getCustVO(QueryInfo queryInfo) {
		try {
			List<?> list = this.getCotBaseDao().findRecordsJDBC(queryInfo);
			return list;
		} catch (DAOException e) {
			e.printStackTrace();
			logger.error("查询出错");
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sail.cot.service.customer.CotCustomerService#getJsonData(com.sail.cot.query.QueryInfo)
	 */
	public String getJsonData(QueryInfo queryInfo) throws DAOException {
		// TODO Auto-generated method stub
		return this.getCotBaseDao().getJsonData(queryInfo);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sail.cot.service.customer.CotCustomerService#getCusMailList(com.sail.cot.query.QueryInfo)
	 */
	public List<CotAddressVO> getCusMailList(QueryInfo queryInfo) {
		List<CotAddressVO> addressList = new ArrayList<CotAddressVO>();
		try {
			List<?> list = this.getCotBaseDao().findRecords(queryInfo);
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
			logger.error("查询出错", e);
			return null;
		}
	}

	// 保存custPc
	public void saveOrUpdateCustPc(CotCustPc pc, String picPath)
			throws Exception {
		try {
			if (pc.getId() == null) {
				File picFile = new File(systemPath + picPath);
				FileInputStream in;
				byte[] b = new byte[(int) picFile.length()];
				in = new FileInputStream(picFile);
				while (in.read(b) != -1) {
				}
				in.close();
				if (!"common/images/zwtp.png".equals(picPath)) {
					// 删除上传的图片
					picFile.delete();
				}
				// 添加图片
				CotOpImgService impOpService = (CotOpImgService) SystemUtil
						.getService("CotOpImgService");
				CotCustPc custPc = new CotCustPc();
				custPc.setCustId(pc.getCustId());
				custPc.setCategoryId(pc.getCategoryId());
				custPc.setPhone(b);
				custPc.setTempLate(pc.getTempLate());
				custPc.setPcRemark(pc.getPcRemark());
				custPc.setFilePath(pc.getFilePath());
				List imgList = new ArrayList();
				imgList.add(custPc);
				impOpService.saveImg(imgList);
			} else {
				CotCustPc old = (CotCustPc) this.getCotBaseDao().getById(
						CotCustPc.class, pc.getId());
				pc.setPhone(old.getPhone());
				
				//如果pdf文件名不一致,删除旧的pdf
				if(!old.getFilePath().equals(pc.getFilePath())){
					File file = new File(systemPath + "artWorkPdf/client/"
							+ old.getFilePath());
					file.delete();
				}
				List list = new ArrayList();
				list.add(pc);
				this.getCotBaseDao().saveOrUpdateRecords(list);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	// 删除custPc
	public Boolean deleteCustPcs(List<Integer> ids) throws Exception {
		try {
			List files = new ArrayList();
			for (int i = 0; i < ids.size(); i++) {
				Integer id = (Integer) ids.get(i);
				CotCustPc cotCustPc = (CotCustPc) this.getCotBaseDao().getById(
						CotCustPc.class, id);
				files.add(cotCustPc.getFilePath());
			}
			this.getCotBaseDao().deleteRecordByIds(ids, "CotCustPc");
			// 删除文件
			for (int i = 0; i < files.size(); i++) {
				File file = new File(systemPath + "artWorkPdf/client/"
						+ (String) files.get(i));
				file.delete();
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return true;
	}

	// 保存custPc
	public CotCustPc getCustPcById(Integer id) {
		CotCustPc custPc= (CotCustPc) this.getCotBaseDao().getById(CotCustPc.class, id);
		if (custPc != null) {
			CotCustPc custClone = (CotCustPc) SystemUtil.deepClone(custPc);
			custClone.setPhone(null);
			return custClone;
		}
		return custPc;
	}
	
	// 删除custpc图片
	public boolean deleteCustPcImg(Integer Id) throws Exception {
		CotOpImgService impOpService = (CotOpImgService) SystemUtil
				.getService("CotOpImgService");
		String filePath = systemPath + "common/images/zwtp.png";
		CotCustPc cotCustPc= impOpService.getCustPcImgById(Id);
		File picFile = new File(filePath);
		FileInputStream in;
		try {
			in = new FileInputStream(picFile);
			byte[] b = new byte[(int) (picFile.length())];
			while (in.read(b) != -1) {
			}
			in.close();
			cotCustPc.setPhone(b);
			List<CotCustPc> imgList = new ArrayList<CotCustPc>();
			imgList.add(cotCustPc);
			impOpService.modifyImg(imgList);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("删除custpc出错");
		}
	}
	
	// 更改包装图片
	public void updateCustPc(String filePath, Integer fkId)
			throws Exception {
		CotOpImgService impOpService = (CotOpImgService) SystemUtil
				.getService("CotOpImgService");
		CotCustPc cotCustPc = impOpService.getCustPcImgById(fkId);
		File picFile = new File(filePath);
		FileInputStream in;
		in = new FileInputStream(picFile);
		byte[] b = new byte[(int) picFile.length()];
		while (in.read(b) != -1) {
		}
		in.close();
		cotCustPc.setPhone(b);
		if (filePath.indexOf("common/images/zwtp.png") < 0) {
			picFile.delete();
		}
		List<CotCustPc> imgList = new ArrayList<CotCustPc>();
		imgList.add(cotCustPc);
		impOpService.modifyImg(imgList);
	}

}
