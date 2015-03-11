/**
 * 
 */
package com.sail.cot.service.img;

import java.io.FileNotFoundException;
import java.util.List;

import org.apache.tools.zip.ZipOutputStream;

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

/**
 * <p>Title: 样品管理系统</p>
 * <p>Description:</p>
 * <p>Copyright: Copyright (c) 2008</p>
 * <p>Company: </p>
 * <p>Create Time: Feb 12, 2009 1:44:11 PM </p>
 * <p>Class Name: CotOpImg.java </p>
 * @author achui
 *
 */
public interface CotOpImgService {
	
	//保存图片
	public void saveImg(List imgList);
	//修改图片
	public void modifyImg(List imgList);
	
	public void saveOrUpdateImg(List imgList);
	
	//根据样品编号获取样品图片信息
	public CotElePic getElePicImgByEleName(String eleId);
	//根据样品ID样品图片信息
	public CotElePic getElePicImgByEleId(Integer eleId);
	//获取报价图片信息
	public CotPricePic getPricePic(Integer detailId);
	//获取订单图片信息
	public CotOrderPic getOrderPic(Integer detailId);
	//根据货号获取订单图片信息
	public CotOrderPic getOrderPicByEleId(String eleId);
	//获取送样图片信息
	public CotGivenPic getGivenPic(Integer detailId);
	//获取征样图片信息
	public CotSignPic getSignPic(Integer detailId);
	//获取询盘图片信息
	public CotPanPic getPanPic(Integer detailId);
	public CotPanOtherPic getPanOtherPic(Integer detailId);
	//获取采购图片信息
	public CotOrderfacPic getOrderFacPic(Integer detailId);
	//获取出货图片信息
	public CotOrderoutPic getOrderOutPic(Integer detailId);
	//获取作废单图片信息
	public CotOrderoutPicDel getOrderOutDelPic(Integer detailId);
	//获取差额图片信息
	public CotOrderouthsPic getOrderOuthsPic(Integer detailId);
	//获取作废单的差额图片信息
//	public CotOrderouthsPicDel getOrderOuthsPicDel(Integer detailId);
	//获取配件图片信息
	public CotFittingsPic getFitPicImgById(Integer fkId);
	//获取配件采购明细图片信息
	public CotFittingsOrderdetailPic getFitOrderPicImgById(Integer fkId);
	//获取CustPc
	public CotCustPc getCustPcImgById(Integer fkId);
	//获取OrderPc
	public CotOrderPc getOrderPcImgById(Integer fkId);
	
	//批量下载客户art work
	public void setZipOutByCustPc(ZipOutputStream out,String filePath,String tmp)throws Exception;

}
