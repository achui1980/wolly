package com.sail.cot.service.customer;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import com.jason.core.exception.DAOException;
import com.sail.cot.domain.CotClaim;
import com.sail.cot.domain.CotCustPc;
import com.sail.cot.domain.CotCustSeq;
import com.sail.cot.domain.CotCustomer;
import com.sail.cot.domain.CotEmps;
import com.sail.cot.domain.CotNationCity;
import com.sail.cot.domain.CotProvince;
import com.sail.cot.domain.vo.CotAddressVO;
import com.sail.cot.query.QueryInfo;
import com.sailing.oa.domain.CotStatData;

public interface CotCustomerService {

	List<?> getCustomerList();

	CotCustomer getCustomerById(Integer Id);

	public boolean saveCustomer(CotCustomer cotCustomer, String photoPath,
			String MbPath, String addTime);

	boolean modifyCustomer(CotCustomer cotCustomer, String addTime);

	int deleteCustomer(List<CotCustomer> CustomerList);

	int deleteCustomerById(Integer Id);

	void deleteCustomPhoto(String customPhoto);

	Integer getIdByNo(String customerNo);

	boolean findExistPhotoName(String customPhoto);

	boolean findExistMb(String customerMb);

	// 通过id获取客户索赔信息
	CotClaim getClaimById(Integer Id);

	// 添加或保存客户索赔信息
	boolean saveOrUpdateClaim(CotClaim cotClaim);

	// 批量删除客户索赔信息
	boolean deleteClaimByList(List<Integer> ids);

	List<?> getEmpsList();

	List<?> getShipPortList();

	List<?> getTargetPortList();

	List<?> getNationList();

	List<?> getCustomerTypeList();

	List<?> getCustomerLvList();

	List<?> getClauseList();

	List<?> getCommisionTypeList();

	List<?> getPayTypeList();

	List<?> getProvinceList();

	List<?> getNationCityList();

	List<CotProvince> getProvinceListByNationId(Integer nationId);

	List<CotNationCity> getNationCityListByProvinceId(Integer provinceId);

	Timestamp getAddTime();

	public List<?> getList(QueryInfo queryInfo);

	public int getRecordCount(QueryInfo queryInfo);

	// 根据图片路径删除图片
	public Boolean delPicByPath(String picPath);

	// 获取业务员映射
	Map<?, ?> getEmpMap();

	// 通过部门id获取所在部门员工列表
	List<CotEmps> getDeptEmpListByDeptId(Integer deptId);

	// 通过员工id获取所在员工信息
	List<CotEmps> getEmpListByEmpId(Integer empId);

	// 更新数据路中的唛标字段
	public void updatePicImg(String filePath, Integer Id);

	// 删除数据库中的唛标
	public boolean deletePicImg(Integer Id);

	// 根据id查找数据库中的唛标字段
	public byte[] getPicImgById(Integer Id);

	// 更新数据路中的客户照片字段
	public void updateCustImg(String filePath, Integer Id);

	// 删除数据库中的客户照片
	public boolean deleteCustImg(Integer Id);

	// 根据id查找数据库中的客户照片字段
	public byte[] getCustImgById(Integer Id);

	// 检查客户邮箱地址是否为空
	public boolean checkMailAdd(Integer customerId);

	public boolean updateCustSeq(Integer custId, Integer custSeq, String key);

	// 获取各单据的客户序列号
	public CotCustSeq getCustSeqByCustId(Integer custId, String currdate);

	// 更新各个单据的客户序列号
	public boolean updateCustSeqByType(Integer custId, String type,
			String currDate);

	// 获取客户编号
	// String getCustNo();

	// 查询客户简称是否存在
	public Integer findIsExistShortName(String customerShortName, String id);

	// 查询客户英文名是否存在
	public Integer findIsExistEnName(String fullNameEn, String id);

	// 统计信息
	public CotStatData statData();

	// 统计表行数
	public Integer statRows(String objName);

	// 获取默认公司
	public String getCompany();

	// 得到总记录数
	public Integer getRecordCountJDBC(QueryInfo queryInfo);

	// 查询客户VO
	public List<?> getCustVO(QueryInfo queryInfo);

	public String getJsonData(QueryInfo queryInfo) throws DAOException;

	/**
	 * 获得客户ID，姓名，邮件地址
	 * 
	 * @param queryInfo
	 * @return
	 */
	public List<CotAddressVO> getCusMailList(QueryInfo queryInfo);

	// 保存custPc
	public void saveOrUpdateCustPc(CotCustPc custPc, String picPath)
			throws Exception;

	// 删除图片
	public Boolean deleteCustPcs(List<Integer> ids) throws Exception;

	// 保存custPc
	public CotCustPc getCustPcById(Integer id);

	// 删除custpc图片
	public boolean deleteCustPcImg(Integer Id) throws Exception;

	// 更改包装图片
	public void updateCustPc(String filePath, Integer fkId) throws Exception;

}
