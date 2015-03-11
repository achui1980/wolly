package com.sail.cot.service.importPic.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.directwebremoting.WebContext;
import org.directwebremoting.WebContextFactory;

import com.sail.cot.dao.CotBaseDao;
import com.sail.cot.domain.CotEmps;
import com.sail.cot.domain.CotGivenDetail;
import com.sail.cot.domain.CotGivenPic;
import com.sail.cot.domain.CotOrder;
import com.sail.cot.domain.CotOrderDetail;
import com.sail.cot.domain.CotOrderPic;
import com.sail.cot.domain.CotPicture;
import com.sail.cot.domain.CotPrice;
import com.sail.cot.domain.CotPriceDetail;
import com.sail.cot.domain.CotPricePic;
import com.sail.cot.domain.vo.ImportMsgVO;
import com.sail.cot.service.importPic.ImportPicService;

public class ImportPicServiceImpl implements ImportPicService {

	private CotBaseDao cotBaseDao;

	public CotBaseDao getCotBaseDao() {
		return cotBaseDao;
	}

	public void setCotBaseDao(CotBaseDao cotBaseDao) {
		this.cotBaseDao = cotBaseDao;
	}

	// 获取系统目录
	String classPath = ImportPicServiceImpl.class.getResource("/").toString();
	String systemPath = classPath.substring(6, classPath.length() - 16);
  
	//删除图片
	@SuppressWarnings("unchecked")
	public void deletePic(List list){	
		for(int i=0;i<list.size();i++){
			System.out.println("=======PIC");
			String fileName=(String) list.get(i);
			String realPath=systemPath+fileName;
			this.delFile(realPath);
		}		
	}
	
	// 新建文件
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

	// 删除文件
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

	// 删除文件夹
	public void delFolder(String folderPath) {
		try {
			delAllFile(folderPath); // 删除文件夹目录下所有文件
			File file = new File(folderPath);
			file.delete(); // 删除空文件夹
		} catch (Exception e) {
			System.out.println("删除文件夹操作出错");
			e.printStackTrace();
		}
	}

	// 删除目录下所有文件
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
				// System.out.println(temp[i].getPath());
				temp[i].delete();
			}
			if (temp[i].isDirectory()) {
				// System.out.println(temp[i].getPath());
				delAllFile(temp[i].getPath());
				delFolder(temp[i].getPath());
			}
		}
	}

	// 获取目录下所有文件夹名称(主单编号)
	public List<String> getFolderNameList(String path) {
		List<String> folderNameList = new ArrayList<String>();
		File a = new File(path);
		String[] file = a.list();
		File temp = null;
		for (int i = 0; i < file.length; i++) {
			if (path.endsWith(File.separator)) {
				temp = new File(path + file[i]);
			} else {
				temp = new File(path + File.separator + file[i]);
			}
			if (temp.isDirectory()) {
				String folderName = file[i];
				folderNameList.add(folderName);
			}
		}
		return folderNameList;
	}

	// 通过主单id获取报价明细id的集合
	@SuppressWarnings("unchecked")
	public List<Integer> getPriceDetailIdsByMainId(Integer mainId) {
		List<Integer> detailIds = this.getCotBaseDao().find(
				"select obj.id from CotPriceDetail obj where obj.priceId="
						+ mainId);
		if (detailIds != null && detailIds.size() > 0) {
			return detailIds;
		}
		return null;
	}

	// 通过主单id获取订单明细id的集合
	@SuppressWarnings("unchecked")
	public List<Integer> getOrderDetailIdsByMainId(Integer mainId) {
		List<Integer> detailIds = this.getCotBaseDao().find(
				"select obj.id from CotOrderDetail obj where obj.orderId="
						+ mainId);
		if (detailIds != null && detailIds.size() > 0) {
			return detailIds;
		}
		return null;
	}

	// 通过明细id获取报价图片对象
	@SuppressWarnings("unchecked")
	public CotPricePic getPricePicByFkId(Integer fkId) {
		List<CotPricePic> list = this.getCotBaseDao().find(
				"from CotPricePic obj where obj.fkId=" + fkId);
		if (list != null && list.size() > 0) {
			CotPricePic pricePic = list.get(0);
			//pricePic.setPicImg(null);
			return pricePic;
		}
		return null;
	}
	
	// 通过主单id获取报价明细的集合
	@SuppressWarnings("unchecked")
	public List<Integer> getPDByMainId(Integer mainId) {
		List<Integer> detailIds = this.getCotBaseDao().find(
				"from CotPriceDetail obj where obj.priceId="
						+ mainId);
		if (detailIds != null && detailIds.size() > 0) {
			return detailIds;
		}
		return null;
	}

	// 通过主单id获取订单明细的集合
	@SuppressWarnings("unchecked")
	public List<Integer> getODByMainId(Integer mainId) {
		List<Integer> detailIds = this.getCotBaseDao().find(
				"from CotOrderDetail obj where obj.orderId="
						+ mainId);
		if (detailIds != null && detailIds.size() > 0) {
			return detailIds;
		}
		return null;
	}
	
	// 通过主单id获取送样明细的集合
	@SuppressWarnings("unchecked")
	public List<Integer> getGDByMainId(Integer mainId) {
		List<Integer> detailIds = this.getCotBaseDao().find(
				"from CotGivenDetail obj where obj.givenId="
						+ mainId);
		if (detailIds != null && detailIds.size() > 0) {
			return detailIds;
		}
		return null;
	}

	
	// 通过主单id获取送样明细id的集合
	@SuppressWarnings("unchecked")
	public List<Integer> getGivenDetailIdsByMainId(Integer mainId) {
		List<Integer> detailIds = this.getCotBaseDao().find(
				"select obj.id from CotGivenDetail obj where obj.givenId="
						+ mainId);
		if (detailIds != null && detailIds.size() > 0) {
			return detailIds;
		}
		return null;
	}

	// 通过明细id获取订单图片对象
	@SuppressWarnings("unchecked")
	public CotOrderPic getOrderPicByFkId(Integer fkId) {
		List<CotOrderPic> list = this.getCotBaseDao().find(
				"from CotOrderPic obj where obj.fkId=" + fkId);
		if (list != null && list.size() > 0) {
			CotOrderPic orderPic = list.get(0);
			//orderPic.setPicImg(null);
			return orderPic;
		}
		return null;
	}
	
	// 通过明细id获取订单图片对象
	@SuppressWarnings("unchecked")
	public CotGivenPic getGivenPicByFkId(Integer fkId) {
		List<CotGivenPic> list = this.getCotBaseDao().find(
				"from CotGivenPic obj where obj.fkId=" + fkId);
		if (list != null && list.size() > 0) {
			CotGivenPic givenPic = list.get(0);
			//orderPic.setPicImg(null);
			return givenPic;
		}
		return null;
	}

	// 根据主单编号获取明细id的集合
	@SuppressWarnings("unchecked")
	public List<Integer> getDetailIdsByNo(String No, String type) {
		List<Integer> list = this.getCotBaseDao().find(
				"select d.id from CotPrice obj,CotPriceDetail d where d.priceId = obj.id and obj.priceNo='" + No
						+ "'");
		return list;
	}

	// 获取图片字节对象
	public byte[] getPicImg(String folderPath, String eleId) {
		byte[] picImg = null;
		File a = new File(folderPath);
		String[] file = a.list();
		File temp = null;
		for (int i = 0; i < file.length; i++) {
			if (folderPath.endsWith(File.separator)) {
				temp = new File(folderPath + file[i]);
			} else {
				temp = new File(folderPath + File.separator + file[i]);
			}
			if (temp.isFile()) {
				String fileName = (temp.getName()).toString();
				String picName = fileName.substring(0, fileName.length() - 4).toUpperCase();
				if (eleId!=null&&picName.equals(eleId.toUpperCase())) {
					try {
						FileInputStream input = new FileInputStream(temp);
						picImg = new byte[(int) temp.length()];
						input.read(picImg);
						input.close();
						return picImg;
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
		return null;
	}

	//导入图片(临时)
	public int updatePic(String path, String type) {
		File file = new File(path);
		if (!file.exists()) {
			return 1;
		}
		if (!file.isDirectory()) {
			return 2;
		}
		// 获取目录下所有文件夹名称(主单编号)
		List<String> noList = this.getFolderNameList(path);
		Iterator<?> it = noList.iterator();
		CotPricePic pricePic = new CotPricePic();
		CotOrderPic orderPic = new CotOrderPic();
		while (it.hasNext()) {
			String No = (String) it.next();
			String folderPath = path + "\\" + No;
			// 根据主单编号获取明细id的集合
			List<Integer> detailIds = this.getDetailIdsByNo(No, type);
			if (detailIds != null && detailIds.size() > 0) {
				for (int i = 0; i < detailIds.size(); i++) {
					Integer detailId = detailIds.get(i);
					if (type.equals("price")) {
						pricePic = this.getPricePicByFkId(detailId);
						if (pricePic != null) {
							String eleId = pricePic.getEleId();
							byte[] picImg = this.getPicImg(folderPath, eleId);
							if(picImg!=null){
								pricePic.setPicImg(picImg);
								pricePic.setPicSize(picImg.length);
							    List<CotPricePic> list = new ArrayList<CotPricePic>();
							    list.add(pricePic);
								this.getCotBaseDao().updateRecords(list);
							}
						}
						pricePic = null;
					}
					if (type.equals("order")) {
						orderPic = this.getOrderPicByFkId(detailId);
						if (orderPic != null) {
							String eleId = orderPic.getEleId();
							byte[] picImg = this.getPicImg(folderPath, eleId);
							if(picImg!=null){
								orderPic.setPicImg(picImg);
								orderPic.setPicSize(picImg.length);
								List<CotOrderPic> list = new ArrayList<CotOrderPic>();
							    list.add(orderPic);
								this.getCotBaseDao().updateRecords(list);
							}
						}
						orderPic = null;
					}
				}
			}
		}
		return 0;
	}
	
	/////////////////////////////////////////////////////////////////////////////////////////////////
	
	 //通过报价单id获取编号
	 public String getPriceNoById(Integer mainId){
		 CotPrice cotPrice = (CotPrice) this.getCotBaseDao().getById(CotPrice.class, mainId);
		 if(cotPrice!=null){
			 return cotPrice.getPriceNo();
		 }
		 return null;
	 }
	 
	//通过订单id获取编号
	 public String getOrderNoById(Integer mainId){
		 CotOrder cotOrder = (CotOrder) this.getCotBaseDao().getById(CotOrder.class, mainId);
		 if(cotOrder!=null){
			 return cotOrder.getOrderNo();
		 }
		 return null;
	 }
	
	// 导入图片 
	public List<?> updatePicture(Integer mainId,String type ) throws Exception {
		//获取图片路径
		WebContext ctx = WebContextFactory.get();
		CotEmps cotEmps = (CotEmps) ctx.getSession().getAttribute("emp");
		String empNo = cotEmps.getEmpsId();
		String picPath = systemPath + "picTemp/" + empNo + "/";
		 
		List<ImportMsgVO> msgList = new ArrayList<ImportMsgVO>();
		
		File fileSys = new File(picPath);
		if (!fileSys.exists()) {
			fileSys.mkdir();
		}
		
//		int coverSum = 0;
		//ImportMsgVO importMsgVO = new ImportMsgVO();
		// 导入图片
		//updatePicture(mainId, picPath, type, coverSum, msgList,importMsgVO);
		updatePictureWithInsert(mainId, picPath, type, msgList);
		// 删除临时文件夹文件
		delFolder(picPath);
		return msgList;
	}

	 
	
	public int updatePicture(Integer mainId, String path, String type, int coverSum,   
			  List<ImportMsgVO> msgList,ImportMsgVO importMsgVO) {
		
		File file = new File(path);
		if (!file.exists()) {
			return 1;
		}
		if (!file.isDirectory()) {
			return 2;
		}
		// 根据主单id获取明细id的集合
		List<Integer> detailIdList = new ArrayList<Integer>();
		if (type.equals("price")) {
			detailIdList = this.getPriceDetailIdsByMainId(mainId);
		}
		if (type.equals("order")) {
			detailIdList = this.getOrderDetailIdsByMainId(mainId);
		}
		if (type.equals("given")) {
			detailIdList = this.getGivenDetailIdsByMainId(mainId);
		}
		if (detailIdList != null && detailIdList.size() > 0) {
			for (int i = 0; i < detailIdList.size(); i++) {
				Integer detailId = detailIdList.get(i);
				if (type.equals("price")) {
					CotPricePic pricePic = this.getPricePicByFkId(detailId);
					if (pricePic != null) {
						String eleId = pricePic.getEleId();
						byte[] picImg = this.getPicImg(path, eleId);
						if(picImg!=null){
							pricePic.setPicImg(picImg);
							pricePic.setPicSize(picImg.length);
							List<CotPricePic> list = new ArrayList<CotPricePic>();
						    list.add(pricePic);
							this.getCotBaseDao().updateRecords(list);
							coverSum = coverSum + 1;// 覆盖条数
						}
					}
				}
				if (type.equals("order")) {
					CotOrderPic orderPic = this.getOrderPicByFkId(detailId);
					if (orderPic != null) {
						String eleId = orderPic.getEleId();
						byte[] picImg = this.getPicImg(path, eleId);
						if(picImg!=null){
							orderPic.setPicImg(picImg);
							orderPic.setPicSize(picImg.length);
							List<CotOrderPic> list = new ArrayList<CotOrderPic>();
						    list.add(orderPic);
							this.getCotBaseDao().updateRecords(list);
							coverSum = coverSum + 1;// 覆盖条数
						}
					}
				}
				if (type.equals("given")) {
					CotGivenPic orderPic = this.getGivenPicByFkId(detailId);
					if (orderPic != null) {
						String eleId = orderPic.getEleId();
						byte[] picImg = this.getPicImg(path, eleId);
						if(picImg!=null){
							orderPic.setPicImg(picImg);
							orderPic.setPicSize(picImg.length);
							List<CotGivenPic> list = new ArrayList<CotGivenPic>();
						    list.add(orderPic);
							this.getCotBaseDao().updateRecords(list);
							coverSum = coverSum + 1;// 覆盖条数
						}
					}
				}
			}
			importMsgVO.setCoverSum(coverSum);
			msgList.add(importMsgVO);
		}
		return 0;
	}
	

	// 获取未导入图片信息
	public List<CotPicture> getUnImportPicList() {
		WebContext ctx = WebContextFactory.get();
		CotEmps cotEmps = (CotEmps) ctx.getSession().getAttribute("emp");
		String empNo = cotEmps.getEmpsId();
		String picPath = systemPath + "picTemp/" + empNo + "/";
		// 如果文件夹不存在 则建立新文件夹
		File a = new File(picPath);
		if (!a.exists())
			a.mkdirs();
		List<CotPicture> UnImportPicList = new ArrayList<CotPicture>();
		this.getFileNameList(picPath, UnImportPicList, systemPath);
		return UnImportPicList;
	}
	
	public void getFileNameList(String picPath, List<CotPicture> res,String systemPath) {
		File a = new File(picPath);
		String[] file = a.list();
		File temp = null;
		for (int i = 0; i < file.length; i++) {
			if (picPath.endsWith(File.separator)) {
				temp = new File(picPath + file[i]);
			} else {
				temp = new File(picPath + File.separator + file[i]);
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
				getFileNameList(picPath + File.separator + file[i], res,systemPath);
			}
		}
		System.out.println("----------" + res.size());
	}
	
	// 遍历图片文件夹,图片名称和明细货号一致就覆盖,不存在于明细时就新增一条空明细
	public void updatePictureWithInsert(Integer mainId, String path, String type,  
			  List<ImportMsgVO> msgList) throws Exception {
		ImportMsgVO importMsgVO = new ImportMsgVO();
		// 根据主单id获取明细id的集合
		int maxSortNo=0;
		List detailIdList = new ArrayList();
		if (type.equals("price")) {
			detailIdList = this.getPDByMainId(mainId);
			//查询最大的序号
			String hql = "select max(obj.sortNo) from CotPriceDetail obj where obj.priceId="+mainId;
			List listSort = this.getCotBaseDao().find(hql);
			if(listSort!=null && listSort.size()==1){
				Object obj = listSort.get(0);
				if(obj!=null){
					maxSortNo=(Integer) listSort.get(0);
				}
			}
		}
		if (type.equals("order")) {
			detailIdList = this.getODByMainId(mainId);
			//查询最大的序号
			String hql = "select max(obj.sortNo) from CotOrderDetail obj where obj.orderId="+mainId;
			List listSort = this.getCotBaseDao().find(hql);
			if(listSort!=null && listSort.size()==1){
				Object obj = listSort.get(0);
				if(obj!=null){
					maxSortNo=(Integer) listSort.get(0);
				}
			}
		}
		if (type.equals("given")) {
			detailIdList = this.getGDByMainId(mainId);
			//查询最大的序号
			String hql = "select max(obj.sortNo) from CotGivenDetail obj where obj.givenId="+mainId;
			List listSort = this.getCotBaseDao().find(hql);
			if(listSort!=null && listSort.size()==1){
				Object obj = listSort.get(0);
				if(obj!=null){
					maxSortNo=(Integer) listSort.get(0);
				}
			}
		}
		//获得业务配置的生产价币种
		Integer facPriceUnit=0;
		String hql = "select obj.facPriceUnit from CotPriceCfg obj";
		List listFac = this.getCotBaseDao().find(hql);
		if(listFac!=null && listFac.size()==1){
			facPriceUnit=(Integer) listFac.get(0);
		} 
		
		int addSum=0;
		int coverSum=0;
		byte[] picImg = null;
		File a = new File(path);
		String[] file = a.list();
		File temp = null;
		for (int i = 0; i < file.length; i++) {
			if (path.endsWith(File.separator)) {
				temp = new File(path + file[i]);
			} else {
				temp = new File(path + File.separator + file[i]);
			}
			if (temp.isFile()) {
				String fileName = (temp.getName()).toString();
				String picName = fileName.substring(0, fileName.length() - 4).toUpperCase();
				FileInputStream input = new FileInputStream(temp);
				picImg = new byte[(int) temp.length()];
				input.read(picImg);
				input.close();
				boolean flag=false;
				if(detailIdList!=null){
					for (int j = 0; j < detailIdList.size(); j++) {
						if (type.equals("price")) {
							CotPriceDetail detail = (CotPriceDetail) detailIdList.get(j);
							if (picName.equals(detail.getEleId().toUpperCase())) {
								CotPricePic pricePic = this.getPricePicByFkId(detail.getId());
								if (pricePic != null) {
									pricePic.setPicImg(picImg);
									pricePic.setPicSize(picImg.length);
									List<CotPricePic> list = new ArrayList<CotPricePic>();
									list.add(pricePic);
									this.getCotBaseDao().updateRecords(list);
									coverSum++;// 覆盖条数
									flag=true;
								}
							}
						}
						if (type.equals("order")) {
							CotOrderDetail detail = (CotOrderDetail) detailIdList.get(j);
							if (picName.equals(detail.getEleId().toUpperCase())) {
								CotOrderPic orderPic = this.getOrderPicByFkId(detail.getId());
								if (orderPic != null) {
									orderPic.setPicImg(picImg);
									orderPic.setPicSize(picImg.length);
									List<CotOrderPic> list = new ArrayList<CotOrderPic>();
									list.add(orderPic);
									this.getCotBaseDao().updateRecords(list);
									coverSum++;// 覆盖条数
									flag=true;
								}
							}
						}
						if (type.equals("given")) {
							CotGivenDetail detail = (CotGivenDetail) detailIdList.get(j);
							if (picName.equals(detail.getEleId().toUpperCase())) {
								CotGivenPic orderPic = this.getGivenPicByFkId(detail.getId());
								if (orderPic != null) {
									orderPic.setPicImg(picImg);
									orderPic.setPicSize(picImg.length);
									List<CotGivenPic> list = new ArrayList<CotGivenPic>();
									list.add(orderPic);
									this.getCotBaseDao().updateRecords(list);
									coverSum++;// 覆盖条数
									flag=true;
								}
							}
						}
					}
				}
				//插入图片
				if(!flag){
					if (type.equals("price")) {
						maxSortNo++;
						CotPriceDetail detail = new CotPriceDetail();
						detail.setEleId(picName);
						detail.setPriceId(mainId);
						detail.setSortNo(maxSortNo);
						detail.setPricePrice(0f);
						detail.setBoxObCount(1l);
						detail.setPriceFac(0f);
						detail.setPriceFacUint(facPriceUnit);
						List<CotPriceDetail> list = new ArrayList<CotPriceDetail>();
						list.add(detail);
						this.getCotBaseDao().saveRecords(list);
						CotPricePic pricePic = new CotPricePic();
						pricePic.setEleId(picName);
						pricePic.setPicImg(picImg);
						pricePic.setPicSize(picImg.length);
						pricePic.setFkId(detail.getId());
						List<CotPricePic> listPic = new ArrayList<CotPricePic>();
						listPic.add(pricePic);
						this.getCotBaseDao().saveRecords(listPic);
						addSum++;
					}
					if (type.equals("order")) {
						maxSortNo++;
						CotOrderDetail detail = new CotOrderDetail();
						detail.setEleId(picName);
						detail.setOrderId(mainId);
						detail.setSortNo(maxSortNo);
						detail.setOrderPrice(0.0);
						detail.setBoxCount(0l);
						detail.setBoxObCount(1l);
						detail.setPriceFac(0f);
						detail.setPriceFacUint(facPriceUnit);
						detail.setBoxCbm(0f);
						detail.setTotalCbm(0f);
						detail.setTotalGrossWeigth(0f);
						List<CotOrderDetail> list = new ArrayList<CotOrderDetail>();
						list.add(detail);
						this.getCotBaseDao().saveRecords(list);
						CotOrderPic pic = new CotOrderPic();
						pic.setEleId(picName);
						pic.setPicImg(picImg);
						pic.setPicSize(picImg.length);
						pic.setFkId(detail.getId());
						List<CotOrderPic> listPic = new ArrayList<CotOrderPic>();
						listPic.add(pic);
						this.getCotBaseDao().saveRecords(listPic);
						addSum++;
					}
					if (type.equals("given")) {
						maxSortNo++;
						CotGivenDetail detail = new CotGivenDetail();
						detail.setEleId(picName);
						detail.setGivenId(mainId);
						detail.setSortNo(maxSortNo);
						detail.setPriceOut(0f);
						detail.setBoxObCount(1l);
						detail.setPriceFac(0f);
						detail.setPriceFacUint(facPriceUnit);
						List<CotGivenDetail> list = new ArrayList<CotGivenDetail>();
						list.add(detail);
						this.getCotBaseDao().saveRecords(list);
						CotGivenPic pic = new CotGivenPic();
						pic.setEleId(picName);
						pic.setPicImg(picImg);
						pic.setPicSize(picImg.length);
						pic.setFkId(detail.getId());
						List<CotGivenPic> listPic = new ArrayList<CotGivenPic>();
						listPic.add(pic);
						this.getCotBaseDao().saveRecords(listPic);
						addSum++;
					}
				}
			}
		}
		importMsgVO.setAddSum(addSum);
		importMsgVO.setCoverSum(coverSum);
		msgList.add(importMsgVO);
	}
	
}
