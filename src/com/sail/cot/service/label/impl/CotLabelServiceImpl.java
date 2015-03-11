/**
 * 
 */
package com.sail.cot.service.label.impl;

import java.io.File;
import java.io.FileInputStream;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.directwebremoting.WebContext;
import org.directwebremoting.WebContextFactory;

import com.jason.core.exception.DAOException;
import com.sail.cot.dao.CotBaseDao;
import com.sail.cot.domain.CotCompany;
import com.sail.cot.domain.CotElementsNew;
import com.sail.cot.domain.CotEmps;
import com.sail.cot.domain.CotFactory;
import com.sail.cot.domain.CotLabel;
import com.sail.cot.domain.CotLabelPicture;
import com.sail.cot.domain.CotLabeldetail;
import com.sail.cot.domain.CotOrder;
import com.sail.cot.domain.CotOrderDetail;
import com.sail.cot.domain.CotPicture;
import com.sail.cot.query.QueryInfo;
import com.sail.cot.service.label.CotLabelService;
import com.sail.cot.util.Log4WebUtil;
import com.sail.cot.util.SystemUtil;

/**
 * 标签管理模块
 */
public class CotLabelServiceImpl implements CotLabelService {
	private CotBaseDao labelDao;

	public CotBaseDao getLabelDao() {
		return labelDao;
	}

	public void setLabelDao(CotBaseDao labelDao) {
		this.labelDao = labelDao;
	}

	private Logger logger = Log4WebUtil.getLogger(CotLabelServiceImpl.class);

	// 删除标签
	public int deleteLabel(List<Integer> labelList) {
		int res = 0;
		for (int i = 0; i < labelList.size(); i++) {
			CotLabel cotLabel = (CotLabel) this.getLabelDao().getById(
					CotLabel.class, (Integer) labelList.get(0));
			// 还原订单导入标志
			List<CotOrder> records = new ArrayList<CotOrder>();
			CotOrder cotOrder = (CotOrder) this.getLabelDao().getById(
					CotOrder.class, cotLabel.getOrderId());
			if (cotOrder != null) {
				CotOrder custClone = (CotOrder) SystemUtil.deepClone(cotOrder);
				custClone.setLabelImpstatus(0);
				records.add(custClone);
				this.getLabelDao().updateRecords(records);
			}
		}
		try {
			this.getLabelDao().deleteRecordByIds(labelList, "CotLabel");
		} catch (DAOException e) {
			e.printStackTrace();
			logger.error("删除标签异常", e);
			res = -1;
		}
		return res;
	}

	// 根据id获取标签信息
	public CotLabel getCotLabelById(Integer Id) {
		CotLabel cotLabel = (CotLabel) this.getLabelDao().getById(CotLabel.class, Id);
		if(cotLabel!=null){
			cotLabel.setCotPictures(null);
			return cotLabel;
		}else{
			return null;
		}
	}

	// 获取标签列表
	public List<?> getCotLabelList() {
		return this.getLabelDao().getRecords("CotLabel");
	}

	public List<?> getList(QueryInfo queryInfo) {
		try {
			return this.getLabelDao().findRecords(queryInfo);
		} catch (DAOException e) {

			e.printStackTrace();
		}
		return null;
	}

	public int getRecordCount(QueryInfo queryInfo) {
		try {
			return this.getLabelDao().getRecordsCount(queryInfo);
		} catch (Exception e) {

			e.printStackTrace();
		}
		return 0;
	}

	// 查询所有业务员姓名
	public Map<?, ?> getEmpsMap() {
		Map<String, String> map = new HashMap<String, String>();
		List<?> list = this.getLabelDao().getRecords("CotEmps");
		for (int i = 0; i < list.size(); i++) {
			CotEmps cotEmps = (CotEmps) list.get(i);
			map.put(cotEmps.getId().toString(), cotEmps.getEmpsName());
		}
		return map;
	}

	// 查询所有公司
	public Map<?, ?> getCompanyNameMap(HttpServletRequest request) {
		Map<String, String> map = new HashMap<String, String>();
		Map<?, ?> dicMap = (Map<?, ?>) request.getSession().getAttribute(
				"sysdic");
		List<?> list = (List<?>) dicMap.get("company");
		for (int i = 0; i < list.size(); i++) {
			CotCompany cotCompany = (CotCompany) list.get(i);
			map.put(cotCompany.getId().toString(), cotCompany
					.getCompanyShortName());
		}
		return map;
	}

	// 查询订单明细
	public List<?> getOrderDetails(Integer orderId) {
		WebContext ctx = WebContextFactory.get();
		String hql = "from CotOrderDetail obj where obj.orderId=" + orderId;
		List list = this.getLabelDao().find(hql);
		List listNew = new ArrayList();
		Map map = this.getFactoryNameMap(ctx.getHttpServletRequest());
		for (int i = 0; i < list.size(); i++) {
			CotOrderDetail orderDetail = (CotOrderDetail) list.get(i);
			if (orderDetail.getFactoryId() != null) {
				String shortName = (String) map.get(orderDetail.getFactoryId()
						.toString());
				orderDetail.setFactoryShortName(shortName);
			}
			listNew.add(orderDetail);
		}
		return listNew;
	}

	// 保存或者更新主标签单
	public Integer saveOrUpdateLabel(CotLabel cotLabel, String sendDate) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		try {
			cotLabel.setAddTime(new Date(System.currentTimeMillis()));// 添加时间
			if (sendDate != null && !"".equals(sendDate)) {
				cotLabel.setSendDate(new Date(sdf.parse(sendDate).getTime()));// 下单时间
			}
			// 新建时,更改订单的标识
			if (cotLabel.getId() == null) {
				List<CotOrder> records = new ArrayList<CotOrder>();
				CotOrder cotOrder = (CotOrder) this.getLabelDao().getById(
						CotOrder.class, cotLabel.getOrderId());
				if (cotOrder != null) {
					CotOrder custClone = (CotOrder) SystemUtil
							.deepClone(cotOrder);
					custClone.setLabelImpstatus(1);
					records.add(custClone);
					this.getLabelDao().updateRecords(records);
				}
			}

			List<CotLabel> list = new ArrayList<CotLabel>();
			list.add(cotLabel);
			this.getLabelDao().saveOrUpdateRecords(list);
			return cotLabel.getId();
		} catch (Exception e) {
			logger.error("保存标签单出错！");
			return null;
		}

	}

	// 保存明细
	public Boolean addList(List<?> details) {
		try {
			this.getLabelDao().saveRecords(details);
			return true;
		} catch (DAOException e) {
			logger.error("保存标签明细出错!");
			return false;
		}
	}

	// 更新明细
	public Boolean updateList(List<?> details) {
		try {
			this.getLabelDao().updateRecords(details);
		} catch (Exception e) {
			logger.error("更新标签明细异常", e);
		}
		return true;
	}

	// 删除标签明细
	public Boolean deleteList(List<?> details) {
		List<Integer> ids = new ArrayList<Integer>();
		for (int i = 0; i < details.size(); i++) {
			CotLabeldetail labeldetail = (CotLabeldetail) details.get(i);
			ids.add(labeldetail.getId());
		}
		try {
			this.getLabelDao().deleteRecordByIds(ids, "CotLabeldetail");
		} catch (DAOException e) {
			e.printStackTrace();
			logger.error("删除标签明细异常", e);
			return false;
		}
		return true;
	}

	// 查询所有厂家
	public Map<?, ?> getFactoryNameMap(HttpServletRequest request) {
		Map<String, String> map = new HashMap<String, String>();
		Map<?, ?> dicMap = (Map<?, ?>) request.getSession().getAttribute(
				"sysdic");
		List<?> list = (List<?>) dicMap.get("factory");
		for (int i = 0; i < list.size(); i++) {
			CotFactory cotFactory = (CotFactory) list.get(i);
			map.put(cotFactory.getId().toString(), cotFactory.getShortName());
		}
		return map;
	}
	
	//更新总金额
	public void updateTotalMoney(Integer labelId) {
		CotLabel cotLabel = (CotLabel) this.getLabelDao().getById(CotLabel.class, labelId);
		if(cotLabel!=null){
			CotLabel clone = (CotLabel) SystemUtil.deepClone(cotLabel);
			String hql = "from CotLabeldetail obj where obj.labelId=" + labelId;
			List list = this.getLabelDao().find(hql);
			double totalMoney = 0;
			for (int i = 0; i < list.size(); i++) {
				CotLabeldetail lbl = (CotLabeldetail) list.get(i);
				totalMoney += lbl.getLabelProd() * lbl.getAmountProd()
						+ lbl.getLabelIb() * lbl.getAmountIb() + lbl.getLabelMb()
						* lbl.getAmountMb() + lbl.getLabelOb() * lbl.getAmountOb();
			}
			if(clone.getOtherMoney()!=null){
				totalMoney +=clone.getOtherMoney();
			}
			clone.setTotalMoney(totalMoney);
			List listNew = new ArrayList();
			listNew.add(clone);
			this.getLabelDao().updateRecords(listNew);
		}
	}
	//根据图片编号获得图片
	public CotLabelPicture getPicById(Integer pictureId){
		return (CotLabelPicture) this.getLabelDao().getById(CotLabelPicture.class, pictureId);
	}
	
	//获取样品其他图片的HTML显示，用图片演示用 
	@SuppressWarnings("unchecked")
	public String getOtherPicHtml(int currentPage, int countOnEachPage,CotLabelPicture queryCondition ,int imgHeigth,int imgWidth) {
		QueryInfo queryInfo = new QueryInfo();
		queryInfo.setStartIndex(currentPage-1);
		queryInfo.setCountOnEachPage(countOnEachPage);
		
		StringBuffer queryString = new StringBuffer();
		queryString.append(" where 1=1");
		if(queryCondition.getLabelId()!=null && !queryCondition.getLabelId().equals("")){
			queryString.append(" and obj.labelId = "+queryCondition.getLabelId());
		}
		 
		String sql = "FROM CotLabelPicture obj";
		queryInfo.setSelectString(sql);
		queryInfo.setQueryString(queryString.toString());
		queryInfo.setOrderString(" order by obj.id desc");
		String html = "<div align='center' style='width:100%;margin:10px;'>";
		queryInfo.setQueryObjType("CotLabelPicture");
		try {
			List<CotLabelPicture> res = this.getLabelDao().findRecords(queryInfo);
			for(CotLabelPicture cotLabelPicture : res)
			{
				String title = cotLabelPicture.getPicName()+"</br>" ;
				
				html +="<div style='width:15%;float:left; margin:5px;'>";
//				html +="<a style='width:100%;' href='/CotSystem/showPicture.action?flag=label&picId="+cotLabelPicture.getId()+"&height="+imgHeigth+"&width="+imgWidth+"' title='"+title+"'  class='thickbox' rel='gallery-plants'>";
//				html += "<img src='/CotSystem/showPicture.action?flag=label&picId="+cotLabelPicture.getId()+"' height='150px' width='150px' alt='"+cotLabelPicture.getPicName()+"' onload='javascript:DrawImage(this,150,150);'/>";
				html +="<a style='width:100%;' href='showPicture.action?flag=label&picId="+cotLabelPicture.getId()+"&height="+imgHeigth+"&width="+imgWidth+"' title='"+title+"'  class='thickbox' rel='gallery-plants'>";
				html += "<img src='showPicture.action?flag=label&picId="+cotLabelPicture.getId()+"' height='150px' width='150px' alt='"+cotLabelPicture.getPicName()+"' onload='javascript:DrawImage(this,150,150);'/>";
				html += "</a>";
//				html += "<a href='#' style='width:100%;'><label onclick=\"\" style='height:20px;margin-top:5px;cursor:hand'><font size=2px>"+cotLabelPicture.getPicName()+"</font></label><label onclick=\"delPic("+cotLabelPicture.getId()+")\" style='margin-left:2px;cursor:hand'><img  src='/CotSystem/common/images/_del-x_.gif' height='18px' width='18px' border='0' alt='删除'></label></a>";
				html += "<a href='#' style='width:100%;'><label onclick=\"\" style='height:20px;margin-top:5px;cursor:hand'><font size=2px>"+cotLabelPicture.getPicName()+"</font></label><label onclick=\"delPic("+cotLabelPicture.getId()+")\" style='margin-left:2px;cursor:hand'><img  src='common/images/_del-x_.gif' height='18px' width='18px' border='0' alt='删除'></label></a>";
				html += "</div>";
			}
			html +="</div>";
			System.out.println(html);
		} catch (DAOException e) {
			 
			e.printStackTrace();
		}
		return html;
	}
	
	//获取样品其他图片查询的记录数,图片演示用
	public int findOtherPicCount(CotLabelPicture queryCondition){
		QueryInfo queryInfo = new QueryInfo();
		
		StringBuffer queryString = new StringBuffer();
		queryString.append(" where 1=1");
		if(queryCondition.getLabelId()!=null && !queryCondition.getLabelId().equals("")){
			queryString.append(" and obj.labelId = "+queryCondition.getLabelId());
		}
		 
		String sql = "SELECT Count(*) "+

		" FROM CotLabelPicture obj";
		queryInfo.setCountQuery(sql+queryString.toString()); 
		
		int res = 0;
		try {
			res = this.getLabelDao().getRecordsCount(queryInfo);
		} catch (DAOException e) { 
			
			e.printStackTrace();
		}
	    return res;
	}
	
	//通过图片Id删除图片
	public Boolean deletePictureByPicId(Integer picId){
		try {
			this.getLabelDao().deleteRecordById(picId, "CotLabelPicture");
			return true;
		} catch (DAOException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	//上传其他图片
	public void saveOtherPic(String filePath,Integer eId) throws Exception{
		CotLabelPicture picOther = new CotLabelPicture();
		Integer s = filePath.lastIndexOf('\\');
		String picName= filePath.substring(s+1,filePath.length());
		File picFile = new File(filePath);
		FileInputStream in;
		in = new FileInputStream(picFile);
		byte[] b = new byte[(int)picFile.length()];
		while (in.read(b)!= -1) {}
		in.close();
		picOther.setPicImg(b);
		picOther.setPicName(picName);
		picOther.setLabelId(eId);
		picOther.setPicCap(new Long(b.length));
		if(filePath.indexOf("common/images/zwtp.png")<0){
			picFile.delete();
		}
		this.getLabelDao().create(picOther);
	}

}
