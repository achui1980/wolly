/**
 * 
 */
package com.sail.cot.service.img.impl;

import java.io.File;
import java.io.FileInputStream;
import java.util.List;

import org.apache.tools.zip.ZipOutputStream;

import com.jason.core.exception.DAOException;
import com.sail.cot.dao.CotBaseDao;
import com.sail.cot.domain.CotCustPc;
import com.sail.cot.domain.CotElePic;
import com.sail.cot.domain.CotFittingsOrderdetailPic;
import com.sail.cot.domain.CotFittingsPic;
import com.sail.cot.domain.CotGivenPic;
import com.sail.cot.domain.CotOrderPc;
import com.sail.cot.domain.CotOrderPic;
import com.sail.cot.domain.CotOrderfacPic;
import com.sail.cot.domain.CotOrderoutPic;
import com.sail.cot.domain.CotOrderoutPicDel;
import com.sail.cot.domain.CotOrderouthsPic;
import com.sail.cot.domain.CotPanOtherPic;
import com.sail.cot.domain.CotPanPic;
import com.sail.cot.domain.CotPricePic;
import com.sail.cot.domain.CotSignPic;
import com.sail.cot.service.img.CotOpImgService;

/**
 * <p>
 * Title: 旗行办公自动化系统（OA）
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2008
 * </p>
 * <p>
 * Company:
 * </p>
 * <p>
 * Create Time: Feb 12, 2009 1:54:06 PM
 * </p>
 * <p>
 * Class Name: CotOpImgServiceImpl.java
 * </p>
 * 
 * @author achui
 * 
 */
public class CotOpImgServiceImpl implements CotOpImgService {

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sail.cot.service.img.CotOpImgService#getElePicImgByEleId(java.lang.Integer)
	 */
	private CotBaseDao impOpDao;

	public CotBaseDao getImpOpDao() {
		return impOpDao;
	}

	public void setImpOpDao(CotBaseDao impOpDao) {
		this.impOpDao = impOpDao;
	}

	public CotElePic getElePicImgByEleId(Integer eleId) {
		String strSql = " from CotElePic obj where obj.fkId = " + eleId;
		List list = this.getImpOpDao().find(strSql);
		CotElePic res = null;
		if (list != null && list.size() > 0)
			res = (CotElePic) list.get(0);
		return res;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sail.cot.service.img.CotOpImgService#getElePicImgByEleName(java.lang.String)
	 */
	public CotElePic getElePicImgByEleName(String eleId) {
		String strSql = " from CotElePic obj where obj.eleId = '" + eleId + "'";
		List list = this.getImpOpDao().find(strSql);
		CotElePic res = null;
		if (list != null && list.size() > 0)
			res = (CotElePic) list.get(0);
		return res;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sail.cot.service.img.CotOpImgService#getGivenPic(java.lang.Integer)
	 */
	public CotGivenPic getGivenPic(Integer detailId) {
		String strSql = " from CotGivenPic obj where obj.fkId = " + detailId;
		List list = this.getImpOpDao().find(strSql);
		CotGivenPic res = null;
		if (list != null && list.size() > 0)
			res = (CotGivenPic) list.get(0);
		return res;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sail.cot.service.img.CotOpImgService#getOrderPic(java.lang.Integer)
	 */
	public CotOrderPic getOrderPic(Integer detailId) {
		String strSql = " from CotOrderPic obj where obj.fkId = " + detailId;
		List list = this.getImpOpDao().find(strSql);
		CotOrderPic res = null;
		if (list != null && list.size() > 0)
			res = (CotOrderPic) list.get(0);
		return res;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sail.cot.service.img.CotOpImgService#getOrderPic(java.lang.Integer)
	 */
	public CotOrderPic getOrderPicByEleId(String eleId) {
		String strSql = " from CotOrderPic obj where obj.eleId = '" + eleId
				+ "'";
		List list = this.getImpOpDao().find(strSql);
		CotOrderPic res = null;
		if (list != null && list.size() > 0)
			res = (CotOrderPic) list.get(0);
		return res;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sail.cot.service.img.CotOpImgService#getPricePic(java.lang.Integer)
	 */
	public CotPricePic getPricePic(Integer detailId) {
		String strSql = " from CotPricePic obj where obj.fkId = " + detailId;
		List list = this.getImpOpDao().find(strSql);
		CotPricePic res = null;
		if (list != null && list.size() > 0)
			res = (CotPricePic) list.get(0);
		return res;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sail.cot.service.img.CotOpImgService#getSignPic(java.lang.Integer)
	 */
	public CotSignPic getSignPic(Integer detailId) {
		String strSql = " from CotSignPic obj where obj.fkId = " + detailId;
		List list = this.getImpOpDao().find(strSql);
		CotSignPic res = null;
		if (list != null && list.size() > 0)
			res = (CotSignPic) list.get(0);
		return res;
	}
	
	public CotPanPic getPanPic(Integer detailId) {
		String strSql = " from CotPanPic obj where obj.fkId = " + detailId;
		List list = this.getImpOpDao().find(strSql);
		CotPanPic res = null;
		if (list != null && list.size() > 0)
			res = (CotPanPic) list.get(0);
		return res;
	}
	
	public CotPanOtherPic getPanOtherPic(Integer detailId) {
		String strSql = " from CotPanOtherPic obj where obj.id = " + detailId;
		List list = this.getImpOpDao().find(strSql);
		CotPanOtherPic res = null;
		if (list != null && list.size() > 0)
			res = (CotPanOtherPic) list.get(0);
		return res;
	}

	// 获取采购图片信息
	public CotOrderfacPic getOrderFacPic(Integer detailId) {
		String strSql = " from CotOrderfacPic obj where obj.fkId = " + detailId;
		List<?> list = this.getImpOpDao().find(strSql);
		CotOrderfacPic res = null;
		if (list != null && list.size() > 0)
			res = (CotOrderfacPic) list.get(0);
		return res;
	}

	// 获取出货图片信息
	public CotOrderoutPic getOrderOutPic(Integer detailId) {
		String strSql = " from CotOrderoutPic obj where obj.fkId = " + detailId;
		List<?> list = this.getImpOpDao().find(strSql);
		CotOrderoutPic res = null;
		if (list != null && list.size() > 0)
			res = (CotOrderoutPic) list.get(0);
		return res;
	}

	// 获取作废单图片信息
	public CotOrderoutPicDel getOrderOutDelPic(Integer detailId) {
		String strSql = " from CotOrderoutPicDel obj where obj.fkId = "
				+ detailId;
		List<?> list = this.getImpOpDao().find(strSql);
		CotOrderoutPicDel res = null;
		if (list != null && list.size() > 0)
			res = (CotOrderoutPicDel) list.get(0);
		return res;
	}

	// 获取差额图片信息
	public CotOrderouthsPic getOrderOuthsPic(Integer detailId) {
		String strSql = " from CotOrderouthsPic obj where obj.fkId = "
				+ detailId;
		List<?> list = this.getImpOpDao().find(strSql);
		CotOrderouthsPic res = null;
		if (list != null && list.size() > 0)
			res = (CotOrderouthsPic) list.get(0);
		return res;
	}

	// 获取作废单的差额图片信息
	// public CotOrderouthsPicDel getOrderOuthsPicDel(Integer detailId){
	// String strSql = " from CotOrderouthsPicDel obj where obj.fkId =
	// "+detailId;
	// List<?> list = this.getImpOpDao().find(strSql);
	// CotOrderouthsPicDel res = null;
	// if(list != null && list.size() > 0)
	// res = (CotOrderouthsPicDel)list.get(0);
	// return res;
	// }

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sail.cot.service.img.CotOpImgService#modifyImg(java.util.List)
	 */
	public void modifyImg(List imgList) {
		this.getImpOpDao().updateRecords(imgList);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sail.cot.service.img.CotOpImgService#saveImg(java.util.List)
	 */
	public void saveImg(List imgList) {
		try {
			this.getImpOpDao().saveRecords(imgList);
		} catch (DAOException e) {
			e.printStackTrace();
		}
	}

	public void saveOrUpdateImg(List imgList) {
		try {
			this.getImpOpDao().saveOrUpdateRecords(imgList);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 获取配件图片信息
	public CotFittingsPic getFitPicImgById(Integer fkId) {
		String strSql = " from CotFittingsPic obj where obj.fkId = " + fkId;
		List list = this.getImpOpDao().find(strSql);
		CotFittingsPic res = null;
		if (list != null && list.size() > 0)
			res = (CotFittingsPic) list.get(0);
		return res;
	}

	// 获取配件采购明细图片信息
	public CotFittingsOrderdetailPic getFitOrderPicImgById(Integer fkId) {
		String strSql = " from CotFittingsOrderdetailPic obj where obj.fkId = "
				+ fkId;
		List list = this.getImpOpDao().find(strSql);
		CotFittingsOrderdetailPic res = null;
		if (list != null && list.size() > 0)
			res = (CotFittingsOrderdetailPic) list.get(0);
		return res;
	}

	// 获取CustPc
	public CotCustPc getCustPcImgById(Integer fkId) {
		String strSql = " from CotCustPc obj where obj.id = " + fkId;
		List list = this.getImpOpDao().find(strSql);
		CotCustPc res = null;
		if (list != null && list.size() > 0)
			res = (CotCustPc) list.get(0);
		return res;
	}

	// 获取OrderPc
	public CotOrderPc getOrderPcImgById(Integer fkId) {
		String strSql = " from CotOrderPc obj where obj.id = " + fkId;
		List list = this.getImpOpDao().find(strSql);
		CotOrderPc res = null;
		if (list != null && list.size() > 0)
			res = (CotOrderPc) list.get(0);
		return res;
	}

	// 批量下载客户art work
	public void setZipOutByCustPc(ZipOutputStream out, String filePath,String tmp) throws Exception {
		String classPath = CotOpImgServiceImpl.class.getResource("/")
				.toString();
		String systemPath = classPath.substring(6, classPath.length() - 16);
		File file = new File(systemPath + tmp + filePath);

		if (file != null) {
			FileInputStream fis = new FileInputStream(file);
			if (fis != null) {
				int len = fis.available();
				byte[] xml = new byte[len];
				fis.read(xml);
				out.putNextEntry(new org.apache.tools.zip.ZipEntry(filePath));
				out.write(xml);
			}
		}
	}

}
