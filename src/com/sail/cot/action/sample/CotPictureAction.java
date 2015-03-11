/**
 * 
 */
package com.sail.cot.action.sample;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.sail.cot.action.AbstractAction;
import com.sail.cot.query.QueryInfo;
import com.sail.cot.service.sample.CotElementsService;
import com.sail.cot.util.SystemUtil;

/**
 * <p>Title: 工艺品管理系统</p>
 * <p>Description:</p>
 * <p>Copyright: Copyright (c) 2008</p>
 * <p>Company: </p>
 * <p>Create Time: Aug 31, 2008 8:34:57 PM </p>
 * <p>Class Name: CotBoxInfoAction.java </p>
 * @author achui
 *
 */
public class CotPictureAction  extends AbstractAction{
	
	private CotElementsService cotElementsService;

	public CotElementsService getCotElementsService() {
		if (cotElementsService == null) {
			cotElementsService = (CotElementsService) super.getBean("CotElementsService");
		}
		return cotElementsService;
	}
	
	//查找主样品图片信息
	public ActionForward query(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		//主样品的编号
//		String id = request.getParameter("id");
//		StringBuffer queryString = new StringBuffer();
//		queryString.append(" where obj.picName<>'zwtp' and obj.eleId="+id);
//		
//		QueryInfo queryInfo = new QueryInfo();
//		// 设置每页显示多少行
//		int pageCount = 15;
//		// 取得页面选择的每页显示条数
//		String pc = request.getParameter("pictureTable_crd");
//		if (pc != null) {
//			pageCount = Integer.parseInt(pc);
//		}
//		// 设定每页显示记录数
//		queryInfo.setCountOnEachPage(pageCount);
//		// 设置查询记录总数语句
//		queryInfo
//				.setCountQuery("select count(*) from CotPicture obj"
//						+ queryString);
//		String sql = "from CotPicture obj";
//
//		queryInfo.setSelectString(sql);
//		// 设置查询参数
//		// 设置条件语句
//		queryInfo.setQueryString(queryString.toString());
//		// 设置排序语句
//		queryInfo.setOrderString(" order by obj.picFlag desc");
//		// 得到limit对象
//		Limit limit = RequestUtils.getLimit(request, "pictureTable");
//		// 取得排序信息
//		Sort sort = limit.getSort();
//		Map<?, ?> sortValueMap = sort.getSortValueMap();
//		queryInfo.setOrderString(SystemUtil.getDefaultSortSQL(
//				"obj", sortValueMap));
//		
//		// 每次翻页不重新计算总行数
//		int totalRows = RequestUtils.getTotalRowsFromRequest(request);
//		if (totalRows < 0) {
//			totalRows = this.getCotElementsService().getRecordCount(queryInfo);
//		}
//		// 设置总行数和每页显示多少条
//		limit.setRowAttributes(totalRows, pageCount);
//		int startIndex = limit.getRowStart();
//		queryInfo.setStartIndex(startIndex);
//		// 根据起始
//		List<?> list = this.getCotElementsService().getList(queryInfo);
//		request.setAttribute("cotPictureList", list);
		return mapping.findForward("queryPictureSuccess");
	}

	/* (non-Javadoc)
	 * @see com.sail.cot.action.AbstractAction#add(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	public ActionForward add(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.sail.cot.action.AbstractAction#modify(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	public ActionForward modify(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.sail.cot.action.AbstractAction#remove(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	public ActionForward remove(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		return null;
	}

}
