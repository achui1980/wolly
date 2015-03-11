/**
 * 
 */
package com.sail.cot.service.label;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.sail.cot.domain.CotLabel;
import com.sail.cot.domain.CotLabelPicture;
import com.sail.cot.query.QueryInfo;

public interface CotLabelService {

    public int getRecordCount(QueryInfo queryInfo);
	
	public List<?> getList(QueryInfo queryInfo);
	
	//获取标签列表
	public List<?> getCotLabelList();
	
	//根据id获取标签信息
	public CotLabel getCotLabelById(Integer Id);
	
	//删除标签
	public int deleteLabel(List<Integer> labelList);
	
	//查询所有业务员姓名
	public Map<?, ?> getEmpsMap();
	
	// 查询所有公司
	public Map<?, ?> getCompanyNameMap(HttpServletRequest request);
	
	//查询订单明细
	public List<?> getOrderDetails(Integer orderId);
	
	// 保存或者更新主标签单
	public Integer saveOrUpdateLabel(CotLabel cotLabel, String sendDate);
	
	// 保存明细
	public Boolean addList(List<?> details);
	
	//更新明细
	public Boolean updateList(List<?> details);
	

	//删除标签明细
	public Boolean deleteList(List<?> details);
	
	// 查询所有厂家
	public Map<?, ?> getFactoryNameMap(HttpServletRequest request);
	
	//更新总金额
	public void updateTotalMoney(Integer labelId);
	
	//根据图片编号获得图片
	public CotLabelPicture getPicById(Integer pictureId);
	
	//获取样品其他图片的HTML显示，用图片演示用
	public String getOtherPicHtml(int currentPage, int countOnEachPage,CotLabelPicture queryCondition ,int imgHeigth,int imgWidth);
	
	//获取样品其他图片查询的记录数,图片演示用
	public int findOtherPicCount(CotLabelPicture queryCondition);
	
	//通过图片Id删除图片
	public Boolean deletePictureByPicId(Integer picId);
	
	//上传其他图片
	public void saveOtherPic(String filePath,Integer eId) throws Exception;
}
