package com.sail.cot.service.sample.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.net.InetAddress;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import com.jason.core.exception.DAOException;
import com.sail.cot.dao.CotBaseDao;
import com.sail.cot.domain.CotEleCfg;
import com.sail.cot.domain.CotElePic;
import com.sail.cot.domain.CotElementsNew;
import com.sail.cot.domain.CotPicture;
import com.sail.cot.domain.vo.ImportMsgVO;
import com.sail.cot.query.QueryInfo;
import com.sail.cot.service.img.CotOpImgService;
import com.sail.cot.service.importPic.impl.ImportPicServiceImpl;
import com.sail.cot.service.sample.ImportPictureService;
import com.sail.cot.servlet.DownImportResultServlet;
import com.sail.cot.util.ContextUtil;
import com.sail.cot.util.SystemInfoUtil;
import com.sail.cot.util.SystemUtil;

public class ImportPictureServiceImpl implements ImportPictureService {

	private CotBaseDao cotBaseDao;

	public CotBaseDao getCotBaseDao() {
		return cotBaseDao;
	}

	public void setCotBaseDao(CotBaseDao cotBaseDao) {
		this.cotBaseDao = cotBaseDao;
	}

	public void newFile(String filePathAndName, String fileContent) {

		try {
			File myFilePath = new File(filePathAndName);

			FileWriter resultFile = new FileWriter(myFilePath, true);
			resultFile.write(fileContent);
			resultFile.close();
		} catch (Exception e) {
			System.out.println("新建文件操作出错");
			e.printStackTrace();

		}
	}
	
	// 获取系统目录
	String classPath = ImportPicServiceImpl.class.getResource("/").toString();
	String systemPath = classPath.substring(6, classPath.length() - 16);
	//删除图片
	@SuppressWarnings("unchecked")
	public void deletePic(List list){	
		for(int i=0;i<list.size();i++){
			String fileName=(String) list.get(i);
			String realPath=systemPath+fileName;
			this.delFile(realPath);
		}		
	}
	

	public void delFile(String filePathAndName) {
		try {
			File myDelFile = new File(filePathAndName);
			// 当且仅当成功删除文件或目录时，返回 true；否则返回 false,如果文件不存在,返回 false
			myDelFile.delete();
		} catch (Exception e) {
			System.out.println("删除文件操作出错");
			e.printStackTrace();
		}
	}

	public void delFolder(String folderPath) {
		try {
			delAllFile(folderPath); // 删除完里面所有内容
			File file = new File(folderPath);
			file.delete(); // 删除空文件夹

		} catch (Exception e) {
			System.out.println("删除文件夹操作出错");
			e.printStackTrace();

		}

	}

	public void delAllFile(String path) {
		File file = new File(path);
		if (!file.exists()) {
			return;
		}
		if (!file.isDirectory()) {
			return;
		}
		File[] temp = file.listFiles();
		for (int i = 0; i < temp.length; i++) {
			if (temp[i].isFile()) {
				System.out.println(temp[i].getPath());
				temp[i].delete();
			}
			if (temp[i].isDirectory()) {
				System.out.println(temp[i].getPath());
				delAllFile(temp[i].getPath());
				delFolder(temp[i].getPath());
			}
		}
	}

	public int isExistFile() {
		String classPath = DownImportResultServlet.class.getResource("/")
				.toString();
		String systemPath = classPath.substring(6, classPath.length() - 16);
		String oldPath = systemPath + "temp/";
		System.out.println("oldPath=" + oldPath);
		int res = 0;
		File a = new File(oldPath);
		if (a.isDirectory()) {
			String[] file = a.list();
			if (file.length == 0) {
				res = 0;
			} else {
				res = 1;
			}
		}
		return res;
	}

	public void getFileNameList(String oldPath, List<CotPicture> res,
			String systemPath) {
		File a = new File(oldPath);
		String[] file = a.list();
		File temp = null;
		for (int i = 0; i < file.length; i++) {
			if (oldPath.endsWith(File.separator)) {
				temp = new File(oldPath + file[i]);
			} else {
				temp = new File(oldPath + File.separator + file[i]);
			}
			if (temp.isFile()) {
				String path = (temp.getPath()).toString();
				String filePath = path.substring(systemPath.length(), path.length());
				System.out.println("filePath+++" + filePath);
				CotPicture cotPicture = new CotPicture();
				cotPicture.setPicName(filePath);
				res.add(cotPicture);
			}
			if (temp.isDirectory()) {
				getFileNameList(oldPath + File.separator + file[i], res,
						systemPath);
			}
		}
		System.out.println("----------" + res.size());
	}

	// 获取未导入图片信息
	public List<CotPicture> getUnImportPicList() {
		String classPath = DownImportResultServlet.class.getResource("/")
				.toString();
		String systemPath = classPath.substring(6, classPath.length() - 16);
		String oldPath = systemPath + "temp/";
		// 如果文件夹不存在 则建立新文件夹
		File a = new File(oldPath);
		if (!a.exists())
			a.mkdirs();
		List<CotPicture> UnImportPicList = new ArrayList<CotPicture>();
		this.getFileNameList(oldPath, UnImportPicList, systemPath);
		return UnImportPicList;
	}

	public void copyFolder(String newPath, String oldPath, boolean isCover,
			boolean isAdd, int coverSum, int addSum, int crossSum,String crossMsg,
			List<CotElePic> cotPicCoverList, List<CotElePic> cotPicAddList,
			List<CotElementsNew> cotEleList, List<ImportMsgVO> msgList,
			ImportMsgVO importMsgVO) {

		CotOpImgService impOpService = (CotOpImgService) SystemUtil
				.getService("CotOpImgService");

		// 查找简称是'未定义'的厂家
//		String hql = "from CotFactory obj where obj.shortName='未定义'";
//		List<?> list = this.getCotBaseDao().find(hql);
//		CotFactory facDefault = new CotFactory();
//		if (list.size() != 0) {
//			facDefault = (CotFactory) list.get(0);
//		}
		try {
			// 如果文件夹不存在 则建立新文件夹
			File a = new File(oldPath);
			if (!a.exists())
				a.mkdirs();
			String[] file = a.list();
			File temp = null;
			for (int i = 0; i < file.length; i++) {
				if (oldPath.endsWith(File.separator)) {
					temp = new File(oldPath + file[i]);
				} else {
					temp = new File(oldPath + File.separator + file[i]);
				}
				if (temp.isFile()) {
					String fileName = (temp.getName()).toString();
					String eleId = fileName.substring(0, fileName.length() - 4);
					eleId = eleId.toUpperCase();
					// String newFileName = eleId + ".png";
					FileInputStream input = new FileInputStream(temp);
					byte[] b = new byte[(int) temp.length()];
					input.read(b);
					input.close();

					// 判断是否存在相同样品编号
					boolean isExistEleId = this.findExistEleId(eleId);
					if (isExistEleId) {
						// 判断是否覆盖
						if (isCover) {
							String picName = eleId;
							// String picPath =
							// newPath.substring(systemPath.length(),
							// newPath.length()) + newFileName;
							// 覆盖样品
							CotElementsNew cotElementsNew = this.getCotElementsByEleId(eleId);
							cotElementsNew.setPicName(picName);
							// cotElementsNew.setPicPath(picPath);
							// cotElementsNew.setPicImg(b);
							cotElementsNew.setCotPictures(null);
							cotElementsNew.setCotFile(null);
							cotElementsNew.setChilds(null);
							cotElementsNew.setCotPriceFacs(null);
							cotElementsNew.setCotEleFittings(null);
							cotElementsNew.setCotElePrice(null);
							cotElementsNew.setCotElePacking(null);
							// cotEleList.add(cotElementsNew);
							// 逐条添加
							this.getCotBaseDao().update(cotElementsNew);
							// 覆盖样品条数;
							coverSum = coverSum + 1;

							// 覆盖样品图片
							CotElePic cotElePic = impOpService.getElePicImgByEleId(cotElementsNew.getId());
							// Integer eId = this.getIdByEleId(eleId);
							// String picRealpath = newPath + newFileName;
							// CotPicture cotPicture =
							// this.getCotPictureByEId(cotElementsNew.getId().toString());
							if (cotElePic != null) {
								cotElePic.setPicName(picName);
								cotElePic.setPicImg(b);
								cotElePic.setEleId(cotElementsNew.getEleId());
								cotElePic.setPicSize(b.length);
								// cotPicCoverList.add(cotElePic);
								List<CotElePic> imgList = new ArrayList<CotElePic>();
								imgList.add(cotElePic);
								impOpService.modifyImg(imgList);
							} else {
								CotElePic cotElePicAdd = new CotElePic();
								cotElePicAdd.setPicName(picName);
								cotElePicAdd.setEleId(cotElementsNew.getEleId());
								cotElePicAdd.setPicImg(b);
								cotElePicAdd.setFkId(cotElementsNew.getId());
								cotElePicAdd.setPicSize(b.length);
								// cotPicAddList.add(cotElePicAdd);
								List<CotElePic> imgList = new ArrayList<CotElePic>();
								imgList.add(cotElePicAdd);
								impOpService.saveImg(imgList);
							}
						} else {
							// 跳过条数
							crossSum = crossSum + 1;
							crossMsg += fileName + ";";
						}
					} else {
						// 判断是否添加新信息
						if (isAdd) {
							String picName = eleId;
							// String picPath = newPath.substring(systemPath.length(), newPath.length()) + newFileName;
							// 储存样品
							Date currDate = new java.sql.Date(System.currentTimeMillis());
							CotElementsNew cotElementsNew = new CotElementsNew();
							cotElementsNew.setEleId(eleId);
							cotElementsNew.setEleAddTime(currDate);
//							cotElementsNew.setFactoryId(facDefault.getId());
							cotElementsNew.setPicName(picName);
							// cotElementsNew.setPicPath(picPath);
							// cotElementsNew.setPicImg(b);
							cotElementsNew.setCotPictures(null);
							cotElementsNew.setCotFile(null);
							cotElementsNew.setCotPriceFacs(null);
							cotElementsNew.setCotEleFittings(null);
							cotElementsNew.setCotElePrice(null);
							cotElementsNew.setCotElePacking(null);
							// 设置新增样品为默认样品配置
							CotElementsNew elementsNew = this.setEleCfgToAddEle(cotElementsNew);
							// cotEleList.add(elementsNew);
							this.getCotBaseDao().create(elementsNew);

							// 储存样品图片
							// Integer eId = this.getIdByEleId(eleId);
							// Integer picFlag = 1;
							// String picRealpath = newPath + newFileName;
							CotElePic cotPicture = new CotElePic();
							cotPicture.setPicName(picName);
							cotPicture.setEleId(eleId);
							cotPicture.setPicImg(b);
							cotPicture.setPicSize(b.length);
							cotPicture.setFkId(elementsNew.getId());
							// cotPicAddList.add(cotPicture);
							List<CotElePic> imgList = new ArrayList<CotElePic>();
							imgList.add(cotPicture);
							impOpService.saveImg(imgList);
							addSum = addSum + 1;// 新增条数
						} else {
							crossSum = crossSum + 1;// 跳过条数
							crossMsg += fileName + ";";
						}
					}
					importMsgVO.setCoverSum(coverSum);
					importMsgVO.setAddSum(addSum);
					importMsgVO.setCrossSum(crossSum);
					importMsgVO.setCrossMsg(crossMsg);
					msgList.add(importMsgVO);
				}
				// 如果是子文件夹
				if (temp.isDirectory()) {
					copyFolder(newPath + file[i] + "/",
							oldPath + file[i] + "/", isCover, isAdd, coverSum,
							addSum, crossSum,crossMsg, cotPicCoverList, cotPicAddList,
							cotEleList, msgList, importMsgVO);
				}
			}
		} catch (Exception e) {
			System.out.println("复制整个文件夹内容操作出错");
			e.printStackTrace();
		}
	}
	public List<?> moveFile(boolean isCover, boolean isAdd) {
		String classPath = DownImportResultServlet.class.getResource("/")
				.toString();
		String systemPath = classPath.substring(6, classPath.length() - 16);
		String oldPath = systemPath + "temp/";
		String newPath = systemPath + "sampleImg/";

		int coverSum = 0;
		int addSum = 0;
		int crossSum = 0;
		String crossMsg = "";
		List<CotElePic> cotPicCoverList = new ArrayList<CotElePic>();
		List<CotElePic> cotPicAddList = new ArrayList<CotElePic>();
		// List<CotElePic> cotPicList = new ArrayList<CotElePic>();
		List<CotElementsNew> cotEleList = new ArrayList<CotElementsNew>();
		List<ImportMsgVO> msgList = new ArrayList<ImportMsgVO>();
		ImportMsgVO importMsgVO = new ImportMsgVO();
		copyFolder(newPath, oldPath, isCover, isAdd, coverSum, addSum,
				crossSum, crossMsg,cotPicCoverList, cotPicAddList, cotEleList, msgList,
				importMsgVO);
		/*
		 * try { if(cotPicCoverList.size()>0){
		 * this.getCotBaseDao().updateRecords(cotPicCoverList); }
		 * 
		 * if(cotEleList.size()>0){
		 * this.getCotBaseDao().saveOrUpdateRecords(cotEleList); } //插入样品图片表
		 * CotOpImgService impOpService
		 * =(CotOpImgService)SystemUtil.getService("CotOpImgService");
		 * if(cotPicAddList.size()>0){ for (int i = 0; i < cotPicAddList.size();
		 * i++) { CotElePic cotPicture = cotPicAddList.get(i); String eleId =
		 * cotPicture.getPicName(); Integer eId = this.getIdByEleId(eleId);
		 * cotPicture.setFkId(eId); cotPicList.add(cotPicture); } } //样品表存在添加
		 * if(cotPicAddList.size() >0 ) impOpService.saveImg(cotPicAddList);
		 * //样品表存在，修改 if(cotPicCoverList.size() >0)
		 * impOpService.modifyImg(cotPicCoverList); } catch (Exception e) {
		 * e.printStackTrace(); }
		 */
		delAllFile(oldPath);
		return msgList;
	}

	public boolean findExistEleId(String eleId) {

		QueryInfo queryInfo = new QueryInfo();
		boolean isExist = false;
		queryInfo
				.setCountQuery("select count(*) from CotElementsNew obj where obj.eleId='"
						+ eleId + "'");
		try {
			int count = this.getCotBaseDao().getRecordsCount(queryInfo);
			if (count > 0)
				isExist = true;
		} catch (DAOException e) {

			e.printStackTrace();
		}
		return isExist;
	}

	public boolean findExistPicName(String picName) {

		QueryInfo queryInfo = new QueryInfo();
		boolean isExist = false;
		queryInfo.setSelectString("from CotPicture obj where obj.picName='"
				+ picName + "'");
		queryInfo
				.setCountQuery("select count(*) from CotPicture obj where obj.picName='"
						+ picName + "'");
		try {
			int count = this.getCotBaseDao().getRecordsCount(queryInfo);
			if (count > 0)
				isExist = true;
		} catch (DAOException e) {

			e.printStackTrace();
		}
		return isExist;
	}

	public Integer getIdByEleId(String eleId) {
		List<?> list = this.getCotBaseDao().find(
				"select obj.id from CotElementsNew obj where obj.eleId='"
						+ eleId + "'");
		Integer id = (Integer) list.get(0);
		return id;
	}

	public CotElementsNew getCotElementsByEleId(String eleId) {
		CotElementsNew cotElementsNew = new CotElementsNew();
		List<?> list = this.getCotBaseDao().find(
				" from CotElementsNew obj where obj.eleId='" + eleId + "'");
		cotElementsNew = (CotElementsNew) list.get(0);
		return cotElementsNew;
	}

	public CotPicture getCotPictureByEId(String eleId) {
		CotPicture cotPicture = new CotPicture();
		List<?> list = this.getCotBaseDao().find(
				" from CotPicture obj where obj.eleId='" + eleId
						+ "' and obj.picFlag = 1");
		if (list.size() > 0) {
			cotPicture = (CotPicture) list.get(0);
			return cotPicture;
		}
		return null;
	}

	// 扫描
	@SuppressWarnings("unchecked")
	public void checkPic(String oldPath, Integer maxLength, List res) {

		try {
			String fileLength = ContextUtil.getProperty("sysconfig.properties",
					"maxLength");

			File a = new File(oldPath);
			if (!a.exists())
				a.mkdirs();
			String[] file = a.list();
			File temp = null;
			for (int i = 0; i < file.length; i++) {
				if (oldPath.endsWith(File.separator)) {
					temp = new File(oldPath + file[i]);
				} else {
					temp = new File(oldPath + File.separator + file[i]);
				}
				String fileName = (temp.getName()).toString();
				if (temp.isFile()) {
					if (temp.length() > maxLength) {
						res.add(fileName + "：图片大小超过"
								+ Integer.parseInt(fileLength) / 1024
								+ "KB，不允许导入！<br/>");
						this.delFile(oldPath + fileName);
					}
					int point = fileName.lastIndexOf(".");
					if (point >= 0) {
						String KZM = fileName.substring(point + 1, fileName
								.length());
						if (!KZM.toLowerCase().equals("jpg")
								&& !KZM.toLowerCase().equals("gif")
								&& !KZM.toLowerCase().equals("png")
								&& !KZM.toLowerCase().equals("bmp")
								&& !KZM.toLowerCase().equals("img")) {
							res.add(fileName + "：文件类型错误！<br/>");
							this.delFile(oldPath + fileName);
						}
					}
				}
				if (temp.isDirectory()) {
					checkPic(oldPath + file[i] + "/", maxLength, res);
				}
			}
		} catch (Exception e) {
			System.out.println("扫描操作出错");
			e.printStackTrace();
		}
	}

	// 获取扫描信息
	public String getCheckMessage(Integer maxLength) {

		String classPath = DownImportResultServlet.class.getResource("/")
				.toString();
		String systemPath = classPath.substring(6, classPath.length() - 16);
		String oldPath = systemPath + "temp/";
		StringBuffer checkMessag = new StringBuffer();
		List<?> list = new ArrayList<Object>();
		this.checkPic(oldPath, maxLength, list);
		for (int i = 0; i < list.size(); i++) {
			checkMessag.append(list.get(i));
		}
		return checkMessag.toString();
	}

	// 删除错误文件
	public boolean delWrongFile(Integer maxLength) {

		boolean isDel = false;
		String classPath = DownImportResultServlet.class.getResource("/")
				.toString();
		String systemPath = classPath.substring(6, classPath.length() - 16);
		String oldPath = systemPath + "temp/";
		List<?> list = new ArrayList<Object>();
		this.checkPic(oldPath, maxLength, list);
		for (int i = 0; i < list.size(); i++) {
			String Message = (String) list.get(i);
			int point = Message.indexOf("：");
			String fileName = Message.substring(0, point);
			System.out.println("错误文件：" + fileName);
			this.delFile(oldPath + fileName);
			isDel = true;
		}
		return isDel;
	}

	// 打开本地文件夹
	public void openUploadFolder(boolean isNet) {

		SystemInfoUtil systemInfoUtil = new SystemInfoUtil();
		try {
			String path = "";
			InetAddress addr;
			addr = InetAddress.getLocalHost();
			String hostname = addr.getHostName();
			System.out.println("hostname:" + hostname);
			if (isNet)// 网络版
				path = "\\\\" + hostname + "\\temp";
			else
				path = System.getProperty("webapp.root");
			String tempPath = path + "temp";
			// 如果文件夹不存在 则建立新文件夹
			File newFile = new File(tempPath);
			if (!newFile.exists())
				newFile.mkdirs();
			systemInfoUtil.getUploadPath(tempPath);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	// 获取注册版本
	public Integer getVersion() {
		Integer isStandAlone = null;
		SystemInfoUtil systemInfoUtil = new SystemInfoUtil();
		String mKey = systemInfoUtil.getVolumSeri();
		String mechineKey = mKey.substring(4, mKey.length());
		mechineKey = mechineKey.trim();
		List<?> list = this.getCotBaseDao().find(
				"select obj.isStandAlone from CotServerInfo obj where obj.mechineKey='"
						+ mechineKey + "'");
		isStandAlone = (Integer) list.get(0);
		return isStandAlone;
	}

	public String getUploadPath(boolean isNet) {
		String path = "";
		try {

			InetAddress addr;
			addr = InetAddress.getLocalHost();
			String hostname = addr.getHostName();
			System.out.println("hostname:" + hostname);
			if (isNet)// 网络版
				path = "\\\\" + hostname + "\\temp";
			else
				path = System.getProperty("webapp.root") + "temp";
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return path;
	}

	@SuppressWarnings("unchecked")
	// 设置新增样品为默认样品配置
	public CotElementsNew setEleCfgToAddEle(CotElementsNew cotElementsNew) {
		List<CotEleCfg> eleCfgList = this.getCotBaseDao().find(
				"from CotEleCfg obj where 1=1");
		if (eleCfgList.size() > 0) {
			CotEleCfg cotEleCfg = eleCfgList.get(0);

			cotElementsNew.setEleTypeidLv1(cotEleCfg.getEleTypeId());
			cotElementsNew.setFactoryId(cotEleCfg.getEleFacId());

			cotElementsNew.setPriceFacUint(cotEleCfg.getElePriceFacUnit());
			cotElementsNew.setPriceOutUint(cotEleCfg.getElePriceOutUnit());

			cotElementsNew.setPriceFac(0f);
			cotElementsNew.setPriceOut(0f);

			cotElementsNew.setBoxIbCount(cotEleCfg.getBoxIbCount());
			cotElementsNew.setBoxMbCount(cotEleCfg.getBoxMbCount());
			cotElementsNew.setBoxObCount(cotEleCfg.getBoxObCount());

			cotElementsNew.setBoxIbType(cotEleCfg.getBoxIbType());
			cotElementsNew.setBoxMbType(cotEleCfg.getBoxMbType());
			cotElementsNew.setBoxObType(cotEleCfg.getBoxObType());

			cotElementsNew.setEleUnit(cotEleCfg.getEleUnit());
			cotElementsNew.setEleFlag(cotEleCfg.getEleFlag());

			cotElementsNew.setBoxTypeId(cotEleCfg.getBoxTypeId());
			cotElementsNew.setEleUnitNum(cotEleCfg.getEleUnitnum());
			return cotElementsNew;
		}
		return cotElementsNew;
	}

}
