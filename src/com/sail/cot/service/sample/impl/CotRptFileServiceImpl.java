package com.sail.cot.service.sample.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.jason.core.exception.DAOException;
import com.sail.cot.dao.CotBaseDao;
import com.sail.cot.domain.CotRptFile;
import com.sail.cot.domain.CotRptType;
import com.sail.cot.query.QueryInfo;
import com.sail.cot.service.sample.CotRptFileService;
import com.sail.cot.servlet.DownImportResultServlet;
import com.sail.cot.util.Log4WebUtil;

public class CotRptFileServiceImpl implements CotRptFileService {

	private CotBaseDao cotBaseDao;

	private Logger logger = Log4WebUtil.getLogger(CotRptFileServiceImpl.class);

	public CotBaseDao getCotBaseDao() {
		return cotBaseDao;
	}

	public void setCotBaseDao(CotBaseDao cotBaseDao) {
		this.cotBaseDao = cotBaseDao;
	}

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

	public void saveOrUpdateRptFile(CotRptFile rptfile) {
		try {
			// 添加默认标识
			if (rptfile.getFlag() == 1) {
				// 更新旧的默认模板的flag=0
				String hql = "from CotRptFile obj where obj.flag=1 and obj.rptType="
						+ rptfile.getRptType();
				List list = this.getCotBaseDao().find(hql);
				if (list != null && list.size() > 0) {
					CotRptFile old = (CotRptFile) list.get(0);
					old.setFlag(0);
					List newList = new ArrayList();
					newList.add(old);
					this.cotBaseDao.updateRecords(newList);
				}
			}
			List list = new ArrayList();
			list.add(rptfile);
			this.getCotBaseDao().saveOrUpdateRecords(list);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("保存报表文件异常", e);
		}
	}

	public void deleteRptFile(List<CotRptFile> rptfileList) {

		List<Integer> ids = new ArrayList<Integer>();
		for (int i = 0; i < rptfileList.size(); i++) {
			CotRptFile cotRptFile = (CotRptFile) rptfileList.get(i);
			ids.add(cotRptFile.getId());
		}
		try {
			this.getCotBaseDao().deleteRecordByIds(ids, "CotRptFile");
		} catch (DAOException e) {
			e.printStackTrace();
			logger.error("删除报表文件异常", e);
		}
	}

	public void delRptFileById(Integer Id) {
		try {
			this.getCotBaseDao().deleteRecordById(Id, "CotRptFile");
		} catch (DAOException e) {
			e.printStackTrace();
		}
	}

	public boolean findExistByName(String name) {

		QueryInfo queryInfo = new QueryInfo();
		boolean isExist = false;
		queryInfo
				.setSelectString("from CotRptFile obj where obj.rptName='"
						+ name + "'");
		queryInfo
				.setCountQuery("select count(*) from CotRptFile obj where obj.rptName='"
						+ name + "'");
		try {
			int count = this.getCotBaseDao().getRecordsCount(queryInfo);
			if (count > 0) {
				isExist = true;
			}

		} catch (DAOException e) {
			e.printStackTrace();
			logger.error("查找重复方法失败", e);
		}

		return isExist;
	}

	public CotRptFile getRptFileById(Integer Id) {
		return (CotRptFile) this.getCotBaseDao().getById(CotRptFile.class, Id);
	}

	public List<?> getRptFileList() {

		return this.getCotBaseDao().getRecords("CotRptFile");
	}

	public void delImg(String filePathAndName) {
		try {
			File myDelFile = new File(filePathAndName);
			myDelFile.delete();
		} catch (Exception e) {
			System.out.println("删除文件操作出错");
			e.printStackTrace();
		}
	}

	public Map<String, String> getMap() {

		Map<String, String> retur = new HashMap<String, String>();
		List<?> res = this.getCotBaseDao().getRecords("CotRptType");
		for (int i = 0; i < res.size(); i++) {
			CotRptType cotRptType = (CotRptType) res.get(i);
			retur.put(cotRptType.getId().toString(), cotRptType.getRptName());
		}
		return retur;
	}

	public void delUploadFileById(Integer Id) {
		CotRptFile cotRptFile = (CotRptFile) this.getRptFileById(Id);
		String rptfilePath = cotRptFile.getRptfilePath();

		String classPath = DownImportResultServlet.class.getResource("/")
				.toString();
		String systemPath = classPath.substring(6, classPath.length() - 16);

		String allPath = systemPath + rptfilePath;
		File rptFile = new File(allPath);
		System.out.println("111111111111111111111111" + rptFile.isFile());
		System.out.println("===========allPath============" + allPath);
		rptFile.delete();
	}

	public void delUploadImgById(Integer Id) {
		CotRptFile cotRptFile = (CotRptFile) this.getRptFileById(Id);
		String rptImgPath = cotRptFile.getRptImgPath();

		if (rptImgPath.equals("common/images/zwtp.png")) {
			return;
		}
		String classPath = DownImportResultServlet.class.getResource("/")
				.toString();
		String systemPath = classPath.substring(6, classPath.length() - 16);

		String allPath = systemPath + rptImgPath;
		File imgFile = new File(allPath);
		System.out.println("222222222222222222222" + imgFile.isFile());
		System.out.println("==========rptImgPath=============" + rptImgPath);
		imgFile.delete();
	}
	
	//设置默认模板
	public boolean setRptDefault(Integer rptId){
		try {
			CotRptFile cotRptFile = (CotRptFile) this.getCotBaseDao().getById(CotRptFile.class, rptId);
			List newList = new ArrayList();
			// 更新旧的默认模板的flag=0
			String hql = "from CotRptFile obj where obj.flag=1 and obj.rptType="
				+ cotRptFile.getRptType();
			List list = this.getCotBaseDao().find(hql);
			if (list != null && list.size() > 0) {
				CotRptFile old = (CotRptFile) list.get(0);
				old.setFlag(0);
				newList.add(old);
			}
			cotRptFile.setFlag(1);
			newList.add(cotRptFile);
			this.cotBaseDao.updateRecords(newList);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public String getJsonData(QueryInfo queryInfo) throws DAOException {
		// TODO Auto-generated method stub
		return this.getCotBaseDao().getJsonData(queryInfo);
	}  
}
