package com.sail.cot.service.sign;
 
import java.util.HashMap;
import java.util.List;
import java.util.Map;

 
import javax.servlet.http.HttpSession;


import com.sail.cot.domain.CotCustomer; 
  
 
import com.sail.cot.domain.CotElementsNew;
import com.sail.cot.domain.CotEmps;
import com.sail.cot.domain.CotFactory;
import com.sail.cot.domain.CotPicture;
import com.sail.cot.domain.CotPriceDetail;
import com.sail.cot.domain.CotSign;
import com.sail.cot.domain.CotSignDetail; 
import com.sail.cot.query.QueryInfo;

public interface CotSignService {
	
	// 查询征样单单号是否存在
	public Integer findIsExistSignNo(String signNo,String id);

	//根据查询语句获取记录
	public List<?> getList(QueryInfo queryInfo);
	
	// 得到objName的集合
	public List<?> getList(String objName);
	
	//根据查询语句获取总记录数
	public int getRecordCount(QueryInfo queryInfo);
	
	//得到objName的集合
	public List<?> getObjList(String objName);
	
	// 根据征样编号查找征样单信息
    public CotSign getSignById(Integer id);
	
    //根据征样明细编号查找不包含PicImg征样明细单信息
	public CotSignDetail getSignDetailById(Integer detailId);
	
 
	//根据征样明细编号查找PicImg
	public byte[] getPicImgByDetailId(Integer detailId);
	
	//根据货号获取样品图片对象(CotPicture)
	public CotPicture getCotPictureByEleId(String eleId);
	
    // 根据征样编号查找征样单信息
	public List<?> getDetailListBySignId(Integer signId);
	
	// 根据客户编号查找客户信息
    public CotCustomer getCustomerById(Integer id);
	
    // 根据厂家编号查找厂家信息
	public CotFactory getFactoryById(Integer id);
	
	//获得征样类型的映射
	public Map<?, ?> getSignTypeMap();
	
	//获得征样单编号的映射
	public Map<?, ?> getSignNoMap();
	
	//获得客户简称的映射
    public Map<?, ?> getCustMap();
	
    //获得厂家简称的映射
	public Map<?, ?> getFacMap();
	
	//获得员工的映射
	public Map<?, ?> getEmpMap();
	
	//获得样品材质的映射
	public Map<?, ?> getTypeMap();
	
	//保存主征样单
	public Integer saveOrUpdateSign(CotSign cotSign,Integer givenId,String givenNo);
	
	//保存征样单的产品明细
	public Boolean addSignDetails(List<?> details);
	
	//保存征样图片对象
	public Boolean addSignPic(List<?> cotSignPicList);
	
	//修改主征样单
	public Boolean modifySignList(List<?> signList);
	
	//修改征样明细的产品
	public Boolean modifySignDetail(List<CotSignDetail> detailList);
	
	// 更新征样单的产品明细
	public Boolean updateSignDetail(CotSignDetail e, String eleProTime);
	
	// 删除主征样单
	public Boolean deleteSignList(List<CotSign> signList);
	
	// 删除征样单的产品明细
	public Boolean deleteSignDetailList(List<CotSignDetail> signDetailList);
	
	//根据征样明细产品的ids删除
	public void deleteDetailByIds(List<Integer> ids);
	
	//将String类型的时间转化为Timestamp类型
	//public Timestamp getStampTime(String time);
	
	//将Timestamp类型的时间转化为String类型
	//public String getStringTime(Timestamp time);
	
	//删除征样明细单产品的原来图片
	public void deleteSignDetailImg(Integer id);
	
	//通过单号删除征样单的所有产品图片
	public void deleteSignImgBySignNo(String signNo);
	
	// 根据样品货号组装征样明细产品对象 
	public CotSignDetail findDetailByEleId(String eleId,Integer factoryId);
	
	//查询该单的所有征样明细的产品货号
	public String findEleBySignId(Integer signId);
	
	//判断该产品货号是否存在
	public Boolean findIsExistEleByEleId(String eleId);
	
	// 判断该主单的明细是否已存在该产品货号
	public boolean findIsExistEleId(Integer mainId, String eleId, Integer eId);
	
	//通过业务员ID获取业务员名字
	public String getEmpsNameByEmpId(Integer empId);
	
	// 根据报价编号字符串查找报价明细
	public List<CotPriceDetail> findPriceDetailByIds(String ids);
	
	// 根据条件查询主报价单记录
	public List<?> getPriceList(QueryInfo queryInfo);
	
	// 查询所有币种
	public Map<?, ?> getCurrencyMap();
	 
	// 查询所有客户
	public Map<?, ?> getCusNameMap();
	
	//通过部门id获取所在部门员工列表
	public List<CotEmps> getDeptEmpListByDeptId(Integer deptId);
	
	//通过员工id获取所在员工信息
	public List<CotEmps> getEmpListByEmpId(Integer empId);
	
	//根据货号取得样品对象
	public CotElementsNew getElementsByEleId(String eleId);
	
	//根据货号取得征样明细对象
	public CotSignDetail getCotSignDetailByEleId(String eleId);
	
	//根据货号和主征样单id获取明细id
	public Integer getDetailId(String signId,String eleId);
	
	//获取signMap
	public  HashMap<String, CotSignDetail> getSignMap();
		
	//储存signMap
	public  void setSignMap(String eleId,CotSignDetail cotSignDetail);
	
	//Action获取signMap
	public  HashMap<String, CotSignDetail> getSignMapAction(HttpSession session);
	
	//Action储存signMap
	public  void setSignMapAction(HttpSession session,String eleId,CotSignDetail cotSignDetail);
	
	//清空signMap
	public void clearSignMap();
	
	//清除signMap中eleId对应的映射
	public void delSignMapByKey(String eleId);
	
	//在Action中清除signMap中eleId对应的映射
	public void delSignMapByKeyAction(String eleId,HttpSession session);
	
	//判断货号是否已经添加
	public boolean checkExist(String eleId);
	
	// 判断货号字符串是否已经添加
	public List<String> checkExistEles(String[] eleAry);
	
	//通过key获取signMap的value 
	public CotSignDetail getSignMapValue(String eleId);
	
	//通过货号修改Map中对应的征样明细
	public  boolean updateMapValueByEleId(String eleId ,String property,String value);
	
    //判断要更新到样品表的明细货号哪些重复
	public String[] findIsExistInEle(String eleIds);
	
	//根据样品货号字符串查询明细
	public void updateToEle(String same,String dis,String mainId);
	 
	//更新征样图片picImg字段
	public void updatePicImg(String filePath,Integer detailId);
	
	//删除征样图片picImg
	public boolean deletePicImg(Integer detailId);
	
	//判断厂家是否已存在
	public Integer findIsExistFactory(Integer gid,Integer factoryId);
}
