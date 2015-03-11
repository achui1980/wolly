/**
 * 公共查询
 */
package com.sail.cot.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.swing.JSpinner.ListEditor;

import com.jason.core.exception.DAOException;
import com.sail.cot.domain.CotCompany;
import com.sail.cot.domain.CotContact;
import com.sail.cot.domain.CotCustomer;
import com.sail.cot.domain.CotElementsNew;
import com.sail.cot.domain.CotFactory;
import com.sail.cot.domain.CotFittings;
import com.sail.cot.domain.CotGivenDetail;
import com.sail.cot.domain.CotMessage;
import com.sail.cot.domain.CotOrderDetail;
import com.sail.cot.domain.CotOrderFacdetail;
import com.sail.cot.domain.CotPanEle;
import com.sail.cot.domain.CotPriceDetail;
import com.sail.cot.domain.CotRptFile;
import com.sail.cot.domain.VDetailStatusId;
import com.sail.cot.query.QueryInfo;

public interface QueryService {

	//查询所有厂家名称
	public Map<?, ?> getFactoryNameMap(HttpServletRequest request);

	// 查询所有新厂家
	public Map<?, ?> getNewFactoryNameMap();

	// 查询所有订单号
	public Map<?, ?> getOrderNoMap();

	//查询所有发票
	public Map<?, ?> getOrderOutNoMap();

	//查询所有集装箱
	public Map<?, ?> getContainerNoMap(HttpServletRequest request);

	//查询所有客户名称
	public Map<?, ?> getCusNameMap();

	// 查询所有客户编号
	public Map<?, ?> getCustNoMap();

	//查询所有价格条款
	public Map<?, ?> getClauseMap(HttpServletRequest request);

	//查询所有币种
	public Map<?, ?> getCurrencyMap(HttpServletRequest request);

	//查询所有材质
	public Map<?, ?> getTypeMap(HttpServletRequest request);

	//查询所有用户姓名
	public Map<?, ?> getEmpsMap();

	//查询所有部门
	public Map<?, ?> getDeptMap();

	//查询所有公司简称
	public Map<?, ?> getCompanyMap();

	//查询记录
	public List<?> getList(QueryInfo queryInfo);

	//查询priceVO记录
	public List<?> getPriceVOList(HttpServletRequest request,
			QueryInfo queryInfo);

	// 查询VO记录
	public List<?> getOrderVOs(QueryInfo queryInfo);

	//查询orderVO记录
	public List<?> getOrderVOList(QueryInfo queryInfo);

	//查询givenVO记录
	public List<?> getGivenVOList(QueryInfo queryInfo);

	//得到总记录数
	public Integer getRecordCount(QueryInfo queryInfo);

	// 得到总记录数
	public Integer getRecordCountJDBC(QueryInfo queryInfo);

	//设置是否有权限查看样品图片
	public void findIsSelPic(HttpServletRequest request, List<?> list);

	// 判断是否有权限查看厂家报价
	public List<?> findIsSelPrice(HttpServletRequest request, List<?> list)
			throws Exception;

	// 查询所有客户简称
	public Map<?, ?> getCusShortNameMap();

	// 查找订单明细
	public List<CotOrderDetail> findOrderDetailByIds(String ids);

	/**
	 * @Description 查找报价明细
	 * @param ids 报价编号字符串
	 * @return 报价明细集合
	 */
	public List<CotPriceDetail> findPriceDetailByIds(String ids);
	
	// 根据报价编号字符串查找报价明细
	public List<CotPanEle> findPanEleByIds(String ids);

	// 查找送样明细
	public List<CotGivenDetail> findGivenDetailByIds(String ids);

	// 根据条件查询主报价单记录
	public List<?> getPriceList(QueryInfo queryInfo);

	// 根据条件查询主单记录
	public List<?> getOrderList(QueryInfo queryInfo);

	// 根据条件查询订单明细记录
	public List<?> getOrderDetailList(QueryInfo queryInfo);

	// 得到objName的集合
	public List<?> getList(String objName);

	//更新(样品转明细)
	public Object modifyElementsToDetail(CotElementsNew e,
			Map<String, String> map) throws DAOException;

	//根据币种换算价格
	public float updatePrice(Float price, Integer oldCurId, Integer newCurId);

	public void updateCotOrderDetail(CotOrderDetail cotOrderDetail);

	//添加提醒消息到消息列表
	public Integer addToMessage(CotMessage cotMessage, String msgBeginDate,
			String msgEndDate, String no, Integer empId, Integer gid);

	//返回当前时间+10天后的日期
	public String addtenToCurDate(String msgBeginDate);

	//本地打印所有报表
	public boolean localPrintAll();

	//查询PriceVO记录
	public List<?> getPriceVO(QueryInfo queryInfo);

	//查询订单VO记录
	public List<?> getOrderVO(QueryInfo queryInfo);

	// 查询出货VO记录
	public List<?> getOrderOutVO(QueryInfo queryInfo);
	
	// 查询出货明细VO记录
	public List<?> getOrderDetailVO(QueryInfo queryInfo);
	
	// 查询厂家报价VO记录
	public List<?> getPriceFacVO(QueryInfo queryInfo);

	//查询征样VO记录
	public List<?> getSignVO(QueryInfo queryInfo);
	
	// 样品档案查询征样VO记录
	public List<?> getSignVoByEle(QueryInfo queryInfo);

	//查询送样VO记录
	public List<?> getGivenVO(QueryInfo queryInfo);

	//查询国内采购VO记录
	public List<?> getOrderFacVO(QueryInfo queryInfo);

	//模糊查询(表名,每页数,主字段,主字段文本值,辅字段,参数字段,参数值)
	public List<?> find(String tabName, int pageIdx, int maxsize, String field,
			String param, String fNo, String qF, String qV);

	//模糊查询客户
	public List<?> findWithCus(int pageIdx, int maxsize, String param,
			String fNo);

	//查询样品VO
	public List<?> getSampleVO(QueryInfo queryInfo);

	public List<?> getHistoryPriceVOList(QueryInfo queryInfo);

	public List<?> getHistoryOrderVOList(QueryInfo queryInfo);

	public List<?> getHistoryOrderFacVOList(QueryInfo queryInfo);

	//得到新厂家并存入内存数据字典
	public List<?> getNewFactoryList();

	//删除登录信息
	public void deleteLoginInfos();

	//编辑页面初始化时显示下拉框的文本
	public String showText(String tabName, String field, int id);

	//根据位数取随机数
	public String getRandom(int byteNum);

	//获得20/40/40HQ/45的柜体积(默认24/54/68/86)
	public Float[] getContainerCube();

	//获得20/40/40HQ/45的最大可装重(默认24/54/68/86)
	public Float[] getContainerWeigh();

	// 查询所有付款方式
	public Map<?, ?> getPayTypeMap(HttpServletRequest request);

	// 查找所有报表类型
	public List<?> getRptFileList(Integer rptTypeId);

	// 查找默认的报表
	public CotRptFile getRptDefault(Integer rptTypeId);

	//获得材质名称
	public String getTypeName(Integer typeId);

	//获得厂家名称
	public String getFacName(Integer facId);

	public String getJsonData(QueryInfo queryInfo) throws DAOException;
	
	public String getHomeData(QueryInfo queryInfo) throws DAOException;

	// 判断该产品货号是否存在
	public Integer findIsExistEleByEleId(String eleId);

	//查询默认公司的全称
	public CotCompany findDefaultCompany();

	//调用存储过程更新工厂数据
	public boolean updateFacId(Integer srcFacId, Integer desFacId);

	//调用存储过程更新客户数据
	public boolean updateCustId(Integer srcCustId, Integer desCustId);

	public List findRecords(QueryInfo queryInfo);

	//查询报价明细是否已含有该配件
	public CotFittings getFittingByPrice(Integer orderDetailId, Integer fitId);

	//查询订单明细是否已含有该配件
	public CotFittings getFittingByOrder(Integer orderDetailId, Integer fitId);

	//根据子货号字符串合并子货号
	public String[] getComChilds(String ids, Integer currenyId);
	
	// 查询客户
	public CotCustomer getCustById(Integer custId);
	
	// 查询厂家
	public CotFactory getFactoryById(Integer facId);
	
	// 查询联系人
	public CotContact getCotContactById(Integer facId);
	
	public String saveMail(Map<?, ?> map);
	
	// 保存并获取邮件对象Id
	public String savePanMail(Map<?, ?> map);
	
	// 查询客户VO
	public List<?> getCustVO(QueryInfo queryInfo);
	// 根据id查找视图对象
	public VDetailStatusId getVDetailStatusIdById(Integer id);
	public void saveOrUpdateVDetailStatusId(VDetailStatusId vDetailStatusId);
	
	public java.util.Date addDate(java.util.Date tp,int date);
	
	//查询订单是否有提问,以及是否全部答复
	public String getListData(QueryInfo queryInfo,String type,String flag)throws DAOException ;
	
	//查询数据字典 起运港和目的港的交货期限天数
	public Integer findDays(String shipId,String tarId);
	
	// 查询VO记录
	public List<?> getOrderFacVOList(QueryInfo queryInfo);
	
	// 根据报价编号字符串查找报价明细
	public List<CotOrderFacdetail> findOrderFacDetailByIds(String ids);
	
	//查询公司全称
	public String findCompanyStr(Integer companyId);
	
	public List getListBy(String sql);
	
}
