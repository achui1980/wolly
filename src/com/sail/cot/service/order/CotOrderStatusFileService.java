/**
 * 
 */
package com.sail.cot.service.order;

import java.util.List;

import com.sail.cot.domain.CotOrderstatusFile;

/**
 * <p>Title: 旗航外贸管理软件V8.0</p>
 * <p>Description:</p>
 * <p>Copyright: Copyright (c) 2011</p>
 * <p>Company: 厦门市旗航软件有限公司</p>
 * <p>Create Time: Mar 24, 2013 5:07:48 PM </p>
 * <p>Class Name: CotOrderStatusFileService.java </p>
 * @author achui
 *
 */
public interface CotOrderStatusFileService {
	public String saveFile(CotOrderstatusFile file,String orderStatus,Integer currentEmpId); 
	
	/**
	 * @see 功能描述（必填）：删除上传的记录，连文件也一并上传
	 * @see 处理流程（选填）：
	 * @see 调用例子（必填）：
	 * @see 相关说明文件：
	 * @see <p>Author:achui</p>
	 * @see <p>Create Time:Mar 25, 2013 10:32:10 PM</p>
	 * @param ids
	 * @param filePath
	 * 返回值：void
	 */
	public void delFile(List<Integer> ids,List<String> filePath);
	
	/**
	 * @see 功能描述（必填）：生成订单备注
	 * @see 处理流程（选填）：
	 * @see 调用例子（必填）：
	 * @see 相关说明文件：
	 * @see <p>Author:achui</p>
	 * @see <p>Create Time:Mar 30, 2013 9:28:49 PM</p>
	 * @param orderId
	 * @param emps
	 * @param remark
	 * 返回值：void
	 */
	public void saveNewRemark(Integer orderId, String emps, String remark);
}
