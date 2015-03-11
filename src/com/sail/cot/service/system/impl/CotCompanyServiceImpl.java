package com.sail.cot.service.system.impl;

import java.io.File;
import java.io.FileInputStream;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.directwebremoting.WebContext;
import org.directwebremoting.WebContextFactory;

import com.jason.core.exception.DAOException;
import com.jason.core.exception.ServiceException;
import com.sail.cot.dao.CotBaseDao;
import com.sail.cot.dao.system.CotCompanyDao;
import com.sail.cot.domain.CotCompany;
import com.sail.cot.domain.CotCustomer;
import com.sail.cot.domain.CotFittings;
import com.sail.cot.domain.CotFittingsPic;
import com.sail.cot.query.QueryInfo;
import com.sail.cot.service.fittings.impl.CotFittingsServiceImpl;
import com.sail.cot.service.img.CotOpImgService;
import com.sail.cot.service.system.CotCompanyService;
import com.sail.cot.util.Log4WebUtil;
import com.sail.cot.util.SystemUtil;

public class CotCompanyServiceImpl implements CotCompanyService {

	private CotBaseDao cotCompanyDao;
	private Logger logger = Log4WebUtil.getLogger(CotCompanyServiceImpl.class);

	public CotBaseDao getCotCompanyDao() {
		return cotCompanyDao;
	}

	public void setCotCompanyDao(CotBaseDao cotCompanyDao) {
		this.cotCompanyDao = cotCompanyDao;
	}

	public int getRecordCount(QueryInfo queryInfo) {
		try {
			return this.getCotCompanyDao().getRecordsCount(queryInfo);
		} catch (DAOException e) {

			e.printStackTrace();
		}
		return 0;
	}

	public List<?> getList(QueryInfo queryInfo) {
		try {
			return this.getCotCompanyDao().findRecords(queryInfo);
		} catch (DAOException e) {

			e.printStackTrace();
		}
		return null;
	}

	public CotCompany getCompanyById(Integer Id) {

		CotCompany company = (CotCompany) this.getCotCompanyDao().getById(
				CotCompany.class, Id);
		CotCompany companyClone = (CotCompany) SystemUtil.deepClone(company);
		if (companyClone != null)
			companyClone.setCompanyLogo(null);
		return companyClone;
	}

	public boolean saveCotCompany(CotCompany cotCompany, String logoPath) {
		String classPath = CotCompanyServiceImpl.class.getResource("/")
				.toString();
		String systemPath = classPath.substring(6, classPath.length() - 16);

		String filePath = systemPath + logoPath;
		File picFile = new File(filePath);
		try {
			//判断公司名是否存在
			boolean cn = true;
			//this.getCotCompanyDao().isNotExistCompanyName(cotCompany.getCompanyName());
			if (cn == true) {
				if (picFile.exists()) {
					FileInputStream in = new FileInputStream(picFile);
					byte[] b = new byte[(int) (picFile.length())];
					while (in.read(b) != -1) {
					}
					in.close();
					cotCompany.setCompanyLogo(b);
					if (filePath.indexOf("common/images/zwtp.png") < 0) {
						picFile.delete();
					}
				} else {
					String noPicPath = systemPath + "common/images/zwtp.png";
					File noPicFile = new File(noPicPath);
					FileInputStream in = new FileInputStream(noPicFile);
					byte[] b = new byte[(int) (noPicFile.length())];
					while (in.read(b) != -1) {
					}
					in.close();
					cotCompany.setCompanyLogo(b);
				}
				this.getCotCompanyDao().create(cotCompany);
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public void saveCotCompanyList(List<CotCompany> cotCompanyList)
			throws ServiceException {
		try {
			this.getCotCompanyDao().saveRecords(cotCompanyList);

		} catch (DAOException e) {
			e.printStackTrace();
		}
	}

	public boolean updateCotCompany(CotCompany cotCompany) {
		//克隆对象,避免造成指针混用
		CotCompany cloneObj = (CotCompany) SystemUtil.deepClone(cotCompany);
		byte[] companyLogo = this.getCompanyLogoById(cotCompany.getId());
		cloneObj.setCompanyLogo(companyLogo);

		List<CotCompany> records = new ArrayList<CotCompany>();
		records.add(cloneObj);
		try {
			this.getCotCompanyDao().updateRecords(records);
		} catch (Exception ex) {
			ex.printStackTrace();
			logger.error("保存公司错误!");
			return false;
		}
		return true;
	}

	public void updateCotCompanyByList(List<CotCompany> cotCompanyList)
			throws ServiceException {

		this.getCotCompanyDao().updateRecords(cotCompanyList);
	}

	public int deleteCotCompany(CotCompany cotCompany) {
		int res = 0;
		try {
			this.getCotCompanyDao().delete(cotCompany);
		} catch (Exception e) {
			e.printStackTrace();
			res = -1;
		}
		return res;
	}

	public List<?> getCompanyList() {
		List<CotCompany> companyList = new ArrayList<CotCompany>();
		List<?> list = this.getCotCompanyDao().getRecords("CotCompany");

		for (int i = 0; i < list.size(); i++) {
			CotCompany cotCompany = (CotCompany) list.get(i);
			CotCompany companyClone = (CotCompany) SystemUtil
					.deepClone(cotCompany);
			if (companyClone != null)
				companyClone.setCompanyLogo(null);
			companyList.add(companyClone);
		}
		return companyList;
	}

	public int deleteCotCompanyList(List<CotCompany> cotCompanyList)
			throws ServiceException {
		int res = 0;
		List<Integer> ids = new ArrayList<Integer>();
		for (int i = 0; i < cotCompanyList.size(); i++) {
			CotCompany cotCompany = (CotCompany) cotCompanyList.get(i);
			ids.add(cotCompany.getId());
		}
		try {
			this.getCotCompanyDao().deleteRecordByIds(ids, "CotCompany");
		} catch (DAOException e) {
			e.printStackTrace();
			res = -1;
		}
		return res;
	}

	public boolean findDeptRecordsCount(String companyId) {

		QueryInfo queryInfo = new QueryInfo();
		boolean isExist = false;
		queryInfo.setSelectString("from CotDept obj where obj.companyId='"
				+ companyId + "'");
		queryInfo
				.setCountQuery("select count(*) from CotDept obj where obj.companyId='"
						+ companyId + "'");
		try {
			int count = this.getCotCompanyDao().getRecordsCount(queryInfo);
			if (count > 0)
				isExist = true;
		} catch (DAOException e) {
			e.printStackTrace();
		}
		return isExist;
	}

	public Integer findIsExistdefault() {

		String hql = "select obj.id from CotCompany obj where obj.companyIsdefault=1";
		List list = this.getCotCompanyDao().find(hql);
		if(list!=null && list.size()>0){
			return (Integer)list.get(0);
		}else{
			return 0;
		}
	}

	public boolean findCompanyIsdefault(Integer Id) {

		boolean Isdefault = false;
		List<?> list = this.getCotCompanyDao().find(
				"from CotCompany obj where obj.id='" + Id + "'");
		for (int i = 0; i < list.size(); i++) {
			CotCompany cotCompany = (CotCompany) list.get(0);
			Integer companyIsdefault = cotCompany.getCompanyIsdefault();
			if (companyIsdefault == 1) {
				Isdefault = true;
			}
		}
		return Isdefault;
	}

	//更新companyLogo字段
	public void updatePicImg(String filePath, Integer Id) {

		CotCompany cotCompany = this.getCompanyById(Id);
		File picFile = new File(filePath);
		try {
			FileInputStream in = new FileInputStream(picFile);
			byte[] b = new byte[(int) (picFile.length())];
			while (in.read(b) != -1) {
			}
			in.close();
			cotCompany.setCompanyLogo(b);
			if (filePath.indexOf("common/images/zwtp.png") < 0) {
				picFile.delete();
			}
			List resList = new ArrayList();
			resList.add(cotCompany);
			this.getCotCompanyDao().updateRecords(resList);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("修改公司logo错误!");
		}
	}

	//删除公司logo
	public boolean deletePicImg(Integer Id) {
		String classPath = CotCompanyServiceImpl.class.getResource("/")
				.toString();
		String systemPath = classPath.substring(6, classPath.length() - 16);

		List<CotCompany> res = new ArrayList<CotCompany>();
		String filePath = systemPath + "common/images/zwtp.png";
		CotCompany cotCompany = this.getCompanyById(Id);
		File picFile = new File(filePath);
		FileInputStream in;
		try {
			in = new FileInputStream(picFile);
			byte[] b = new byte[(int) (picFile.length())];
			while (in.read(b) != -1) {
			}
			in.close();
			cotCompany.setCompanyLogo(b);
			res.add(cotCompany);
			this.getCotCompanyDao().updateRecords(res);
			return true;

		} catch (Exception e) {
			e.printStackTrace();
			logger.error("删除公司logo错误!");
			return false;
		}
	}

	//通过路径删除公司logo图片
	public boolean deleteLogoByPath(String logoPath) {
		String classPath = CotCompanyServiceImpl.class.getResource("/")
				.toString();
		String systemPath = classPath.substring(6, classPath.length() - 16);

		String delPath = systemPath + logoPath;
		File delFile = new File(delPath);
		if (delFile.exists()) {
			if (delPath.indexOf("common/images/zwtp.png") < 0) {
				delFile.delete();
			}
			return true;
		}
		return false;
	}

	//根据id查找companyLogo
	public byte[] getCompanyLogoById(Integer Id) {
		CotCompany cotCompany = (CotCompany) this.getCotCompanyDao().getById(
				CotCompany.class, Id);
		if (cotCompany != null) {
			byte[] companyLogo = cotCompany.getCompanyLogo();
			if (companyLogo != null) {
				return companyLogo;
			}
		}
		return null;
	}
	
	public String getJsonData(QueryInfo queryInfo) throws DAOException {
		return this.getCotCompanyDao().getJsonData(queryInfo);
	}
	
	// 保存
	@SuppressWarnings("unchecked")
	public Integer saveOrUpdateCompany(CotCompany company, String picPath) {
		
		List list = new ArrayList();
		
		if (company.getId() == null) {
			// 获得tomcat路径
			String classPath = CotCompanyServiceImpl.class.getResource("/").toString();
			String systemPath = classPath.substring(6, classPath.length() - 16);
			File picFile = new File(systemPath + picPath);
			FileInputStream in;
			byte[] b = new byte[(int) picFile.length()];
			try {
				in = new FileInputStream(picFile);

				while (in.read(b) != -1) {
				}
				in.close();
				if (!"common/images/zwtp.png".equals(picPath)) {
					// 删除上传的图片
					picFile.delete();
				}
			} catch (Exception e1) {
				logger.error("设置公司LOGO错误!");
			}
			company.setCompanyLogo(b);
			
			//this.getCotCompanyDao().saveOrUpdateRecords(list);
		}else{
			company.setCompanyLogo(this.getCompanyLogoById(company.getId()));
		}
		
		list.add(company);
		this.getCotCompanyDao().saveOrUpdateRecords(list);
		
		return company.getId();
	}
	
	// 查询公司简称是否存在
	public Integer findIsExistShortName(String shortName, String id) {
		String hql = "select obj.id from CotCompany obj where obj.companyShortName='"
				+ shortName + "'";
		List<?> res = this.getCotCompanyDao().find(hql);
		if (res.size() == 0) {
			return null; //可添加
		}
		if (res.size() == 1) {
			Integer oldId = (Integer) res.get(0);
			if (oldId.toString().equals(id)) {
				return null; //
			} else {
				return 1;
			}
		}
		return 2;
	}
}
