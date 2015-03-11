/**
 * 
 */
package com.sail.cot.service.fittings;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.jason.core.exception.DAOException;
import com.sail.cot.domain.CotFittings;
import com.sail.cot.domain.CotPriceOut;
import com.sail.cot.query.QueryInfo;


public interface CotFittingsService {
	
	 	public int getRecordCount(QueryInfo queryInfo);
	 	
		public List<?> getList(QueryInfo queryInfo);
		
		// 保存明细
		public Boolean addList(List<?> details);
		
		//根据编号获得对象
		public CotFittings getFittingById(Integer id);
		
		//判断材料编号是否存在
		public boolean findIsExistFitNo(String fitNo,Integer eId);
		
		//保存
		public Integer saveFittings(CotFittings e,String picPath);
		
		// 查询所有厂家
		public Map<?, ?> getFactoryNameMap(HttpServletRequest request);
		
		// 查询所有配件类型
		public Map<?, ?> getTypeLv3NameMap(HttpServletRequest request);
		
		//删除
		public Boolean deleteFittings(List<Integer> ids);
		
		//更改默认图片
		public void updateDefaultPic(String filePath,Integer eId) throws Exception;
		
		//删除配件图片
		public boolean deletePicImg(Integer Id);
		
		//查找含有该配件的样品编号字符串
		public String getEleFitIds(Integer fitId);
		
		//同步更新样品档案中的单价
		public void updateEleFitting(Integer fitId,Double price);
		
		// 同步更新样品档案中的厂家
		public void updateEleFittingFac(Integer fitId, Integer facId)throws DAOException;
		
		// 保存配件报价记录
		public boolean savePriceOut(CotPriceOut cotPriceOut, String addTime);
		
		//根据编号获得配件报价记录
		public CotPriceOut getCotPriceOutById(Integer id);
		
		// 删除配件报价记录
		public Boolean deletePriceOuts(List<?> priceOutIds);
		
		// 根据文件路径导入
		public List<?> saveReport(String filename, Boolean isCover);
		
		//根据文件路径和行号导入excel
		public List<?> updateOneReport(String filePath,Integer rowNum, Boolean isCover);
		
		public String getJsonData(QueryInfo queryInfo) throws DAOException;
		
		//判断材料编号是否存在
		public boolean findIsExistByFitNo(String fitNo);

}
