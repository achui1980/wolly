package com.sail.cot.service.split;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpSession;
import com.sail.cot.domain.CotContainerType;
import com.sail.cot.domain.CotOrderOut;
import com.sail.cot.domain.CotOrderOutdetail;
import com.sail.cot.domain.CotSplitDetail;
import com.sail.cot.domain.CotSplitInfo;
import com.sail.cot.query.QueryInfo;

public interface CotSplitService {

	public List getSplitList();
	 
	public  CotSplitInfo getSplitById(Integer Id);
	 
	public void addSplit(List SplitList);
 
	public boolean modifySplit(List SplitList);
	 
	public int deleteSplit(List SplitList);
 
	public Integer findExistByNo(String no,Integer orderOutId);

	public int getRecordCount(QueryInfo queryInfo);

	public List getList(QueryInfo queryInfo);
	
	public Integer isExistSplitId(String containerNo);
	
	public CotContainerType getContainerTypeById(Integer id);
	
	public List<?> getList(String objName);

	public CotContainerType getContainerCubeById(Integer id);
	
	public CotOrderOut getOrderOutById(Integer id);
	
	public Map<?, ?> getOrderNoMap();
	
	public Map<?, ?> getContainerNoMap();
	
	//根据明细编号查找送样明细单信息
	public CotSplitDetail getSplitDetailById(Integer detailId);
	
	// 删除排载单的产品明细	
	public Boolean deleteSplitDetailList(List<CotSplitDetail> splitDetailList);
	
	public void setMapAction(HttpSession session, Integer orderDetailId,CotSplitDetail cotSplitDetail);
	
	// 根据出货编号字符串查找出货明细
	public List<CotOrderOutdetail> findOrderDetailByIds(String ids);
	
	//判断货号是否已经添加
	public boolean checkExist(Integer orderDetailId);
	
	//获取splitMap
	public  HashMap<Integer, CotSplitDetail> getSplitMap();
	
	//储存splitMap
	public  void setSplitMap(Integer orderDetailId,CotSplitDetail cotSplitDetail);
	
	//清空GivenMap
	public void clearSplitMap();
	
	//保存主排载单
	public Integer saveOrUpdateSplit(CotSplitInfo cotSplit, String splitDate);
	
	//判断排载明细是否包含该货号
	public boolean isExistEleId(Integer splitid, Integer orderDetailId);
	
	//清除splitMap中eleId对应的映射
	public void delSplitMapByKey(Integer orderDetailId);
	
	//通过key获取SplitMap的value 
	public CotSplitDetail getSplitMapValue(Integer orderDetailId);
	
	//通过货号修改Map中对应的排载明细
	public boolean updateMapValueByEleId(Integer orderDetailId ,String property,String value);
	
	//获取出货明细剩余数量
	public Long checkRemainCount(Integer orderOutId,Integer orderDetailId);
	
	//获取出货明细该货号剩余数量及已出货数量
	public Long getRemainCountAndExist(Integer splitId,Integer orderOutId,Integer orderDetailId);
	//获取该货号的总cbm
	public Float getTotalCBM(Integer orderOutId,Integer orderDetailId);
	
	//获取出货单中该货号BoxCbm
	public Float getBoxCBM(Integer orderOutId,Integer orderDetailId);
	
	//获取出货单中该货号剩余数量
	public Long getRemainBoxCount(Integer orderOutId,Integer orderDetailId);
	
	//获取排载单中该货号原来箱数
	public List<CotSplitDetail> getOldContainerCount(Integer splitId,Integer orderDetailId);
	
	//Action获取SplitMap
	public  HashMap<Integer, CotSplitDetail> getSplitMapAction(HttpSession session);
	
	//保存排载单的产品明细
	public Boolean addSplitDetails(List<?> details);
	
	//根据排载明细产品的ids删除
	public void deleteDetailByIds(List<Integer> ids);
	
	//更新排载明细的产品
	public Boolean modifySplitDetail(List<CotSplitDetail> detailList);
	
	//在Action中清除SplitMap中eleId对应的映射
	public void delSplitMapByKeyAction(Integer orderDetailId,HttpSession session);
	
	// 修改主单的总cbm
	public void modifyCotSplitTotalCbm(Integer splitId);
	
	// 删除列表时修改主单的总cbm
	public void modifyCotSplitCbm(Integer splitId,Integer orderDetailId);
	
	//获取已出货数量
	public Long getOutCount(Integer orderDetailId);
	
	//修改出货明细单中的剩余数量及CBM(新增时)
	public void modifyRemainCountAndCbm(Integer splitId);
	
	//修改出货明细单的剩余数量及cbm(新增时)
	public void modifyCountAndCbm(Integer splitId,Integer orderOutId,Integer orderDetailId);
	
	//修改出货明细单的剩余数量及cbm(删除时)
	public void addCountAndCbm(Integer splitId,Integer orderOutId,Integer orderDetailId);
	
	//根据排载主单号修改出货明细单中的剩余数量及CBM(已存在时)
	public void updateRemainCountAndCbm(Integer splitId,Integer orderDetailId,Long newBoxCount);
	
	//修改出货明细单的剩余数量及cbm(已存在时)
	public void updateCountAndCbm(Integer splitId,Integer orderOutId,Integer orderDetailId,Long oldBoxCount,Long newBoxCount);
	
	//通过排载主单号获取出货编号
	public Integer getOrderOutIdBySplitId(Integer splitId);
	
	//获取排载单中该货号
	public List<CotSplitDetail> getEleId(Integer splitId);
	
	// 查找所有报表类型
	public List<?> getRptFileList(Integer rptTypeId);
	
	//设置出货单中排载标志
	public void modifySplitFlag(Integer orderOutId);
	
	//判断该出货明细是否已添加
	@SuppressWarnings("unchecked")
	public void findIsExistDetail(CotSplitDetail detail);
	
	//修改出货明细剩余数量及cbm
	public void updateOrderDetail(String detailIds);
	
	//统计柜数
	public List<String> statContainer(Integer orderOutId);
	
	// 判断货号是否已经添加
	public boolean checkExistDetail(Integer orderDetailId);
	
	// 查找该产品是否出货记录
	public Object findDetail(String splitId,Integer id);
	
	//获取已出货数量(一张单)
	public Long getOutCountDetail(Integer splitId,Integer orderDetailId);

}

