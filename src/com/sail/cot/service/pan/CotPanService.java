/**
 * 
 */
package com.sail.cot.service.pan;

import java.util.List;

import com.jason.core.exception.DAOException;
import com.sail.cot.domain.CotContact;
import com.sail.cot.domain.CotElementsNew;
import com.sail.cot.domain.CotFactory;
import com.sail.cot.domain.CotPan;
import com.sail.cot.domain.CotPanDetail;
import com.sail.cot.domain.CotPanEle;
import com.sail.cot.domain.CotPanOtherPic;
import com.sail.cot.query.QueryInfo;

/**
 * *********************************************
 * @Copyright :(C),2008-2010
 * @CompanyName :厦门市旗航软件有限公司(www.xmqh.net)
 * @Version :1.0
 * @Date :2011-6-24
 * @author : azan
 * @class :CotPanService.java
 * @Description :询盘
 */
public interface CotPanService {
	
	public String getJsonData(QueryInfo queryInfo) throws DAOException;
	
	public CotPan getPanById(Integer panId);
	
	public CotFactory getFacById(Integer facId);
	
	public CotContact getContactById(Integer conId);
	
	public Integer getUsdId();
	
	public Integer findIsExistPanNo(String priceNo, String id);
	
	public Integer saveOrUpdatePan(CotPan cotPan,String addTime)throws Exception;
	
	public void addList(List<?> list);
	
	public void saveOrUpdateList(List<?> list);
	
	public boolean deletePans(List<Integer> ids) throws Exception;
	
	public void deletePanDetails(List<Integer> ids) throws Exception;
	
	public void deletePriceDetails(List<Integer> ids) throws Exception;
	
	/**
	 * 描述:隐藏已报过价的询盘单
	 * @param ids
	 * @throws Exception void
	 */
	public void modPanHide(String ids,String facIds) throws Exception;
	
	public CotElementsNew findEleByEleId(String eleId);
	
	public void updatePicImg(String filePath, Integer detailId);
	
	public void saveOtherPans(String fdsIds, List<Integer> pId) throws Exception;
	
	public boolean sendMails(List<String> list,Integer companyType)throws Exception;
	
	/**
	 * 
	 * @Description：查找这个询盘单发送的邮件未被完全确认的厂家id集合
	 * @Flow：
	 * @Example：
	 * @Files：
	 * @author:azan
	 * @Create:Nov 18, 2011 12:03:02 PM
	 * @param panId
	 * @return List【】
	 */
	public List findAlreadySendMails(Integer panId);
	
	public void updatePanState(Integer pId) throws Exception;
	
	public List findFactoryByPanId(Integer pId);
	
	public boolean saveAllEMail(Integer pId) throws Exception;
	
	//wolly选定一条报价信息
	public void updateEleByChoose(Integer dId) throws DAOException;
	
	// 根据Quotation勾选的记录生成一张新的Inquiry
	public Integer saveNewInquiry(String ids,String facIds,Integer flag) throws Exception;
	
	// 上传其他图片
	public void saveOtherPic(String filePath, Integer eId) throws Exception;
	
	// 根据图片编号获得图片
	public CotPanOtherPic getPicById(Integer pictureId);
	
	// 通过图片Id删除图片
	public Boolean deletePictureByPicId(Integer picId);
	
	// 得到总记录数
	public Integer getRecordCount(QueryInfo queryInfo);
	
//	 quotation主界面查询
	public List<?> getList(QueryInfo queryInfo) throws DAOException;
	
	/**
	 * @see 功能描述（必填）：发送邮件给业务员提醒
	 * @see 处理流程（选填）：
	 * @see 调用例子（必填）：
	 * @see 相关说明文件：
	 * @see <p>Author:achui</p>
	 * @see <p>Create Time:Dec 11, 2012 5:56:52 PM</p>
	 * @param list:邮件列表
	 * @param prsNo：prs单号
	 * @return
	 * 返回值：boolean
	 */
	public boolean sendMailToEmps(List<String> list,String prsNo,Integer companyType);
	
	/**
	 * @see 功能描述（必填）：发送邮件给供应商进行询价
	 * @see 处理流程（选填）：
	 * @see 调用例子（必填）：
	 * @see 相关说明文件：
	 * @see <p>Author:achui</p>
	 * @see <p>Create Time:Dec 12, 2012 4:03:54 PM</p>
	 * @param mailList
	 * @param companyType
	 * @return
	 * 返回值：boolean
	 */
	public boolean sendMailToFacContact(List<String> mailList,Integer companyType,String jsonParam);
	
	/**
	 * 根据自定义的新货号新建一条询盘明细
	 * @Method: saveNewPanEle
	* @Description: 
	* @param eleId
	* @param panId : void
	 */
	public void saveNewPanEle(String eleId,Integer panId)throws DAOException;
	
	/**
	 * 根据询盘明细id获得询盘明细对象
	 * @Method: getPanEleById
	* @Description: 
	* @param panId
	* @return : CotPanEle
	 */
	public CotPanEle getPanEleById(Integer panId);
	
	/**
	 * 更新询盘明细
	 * @Method: updatePanEle
	* @Description: 
	* @param cotPanEle
	* @throws DAOException : void
	 */
	public void updatePanEle(CotPanEle cotPanEle) throws DAOException;
	
	/**
	 * 删除询盘明细
	 * @Method: deletePanEle
	* @Description: 
	* @param ids
	* @throws DAOException : void
	 */
	public void deletePanEles(List ids) throws DAOException;
	
	/**
	 * @see 功能描述（必填）：保存询盘单的报价历史
	 * @see 处理流程（选填）：
	 * @see 调用例子（必填）：
	 * @see 相关说明文件：
	 * @see <p>Author:achui</p>
	 * @see <p>Create Time:Dec 14, 2012 2:44:08 PM</p>
	 * @param list
	 * 返回值：void
	 */
	public void savePanDetail(List<CotPanDetail> list);
	
	public CotPanDetail getPanDetailById(Integer id);
	
	/**
	 * 从产品资料导入到询盘明细
	 * @Method: savePanEleFromEles
	* @Description: 
	* @param ids
	* @param panId
	* @throws DAOException : void
	 */
	public void savePanEleFromEles(List ids,Integer panId) throws DAOException;
	
	/**
	 * 从PO明细导入到询盘明细
	 * @Method: savePanEleFromPos
	* @Description: 
	* @param ids
	* @param panId
	* @throws DAOException : void
	 */
	public void savePanEleFromPos(List ids,Integer panId) throws DAOException;
	
	/**
	 * 修改询盘明细的状态
	 * @Method: updatePanEleState
	* @Description: 
	* @param panEleId
	* @param state
	* @throws DAOException : void
	 */
	public void updatePanEleState(Integer panEleId,Integer state) throws DAOException;
	
	/**
	 * 通过询盘主单id获得所有明细对象集合
	 * @Method: getPanEleIds
	* @Description: 
	* @param panId
	* @return : List
	 */
	public List getPanEleIds(Integer panId);
	
	/**
	 * @Method: updatePanEles
	* @Description: 
	* @param list
	* @throws DAOException : void
	 */
	public void updatePanEles(List<CotPanEle> list) throws DAOException;
	
}
