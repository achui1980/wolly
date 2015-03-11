/**
 * 
 */
package com.sail.cot.action.systemdic;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.sail.cot.action.AbstractAction;
import com.sail.cot.dao.CotBaseDao;
import com.sail.cot.query.QueryInfo;
import com.sail.cot.util.SystemUtil;

/**
 * 货币类型
 * 
 * @author qh-chzy
 * 
 */
public class CotCurrencyAction extends AbstractAction {
	private CotBaseDao baseDao ;
	public CotBaseDao getBaseDao() {
		if(baseDao == null)
			baseDao = (CotBaseDao)SystemUtil.getService("CotBaseDao");
		return baseDao;
	}

	@Override
	public ActionForward add(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		return null;
	}

	@Override
	public ActionForward modify(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		return null;
	}
	
	@Override
	public ActionForward remove(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		return null;
	}

	@Override
	public ActionForward query(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String start = request.getParameter("start");
		String limit = request.getParameter("limit");
		String curNameEn = request.getParameter("curNameEn");
		String curName = request.getParameter("curName");
		String curUnit = request.getParameter("curUnit");
		//String curRate = request.getParameter("curRate");
		
		if (start == null || limit == null)
			return mapping.findForward("querySuccess");
		
		StringBuffer sql = new StringBuffer();
		sql.append(" where 1=1");
		
		if(null!=curNameEn && !"".equals(curNameEn) ){
			sql.append(" and obj.curNameEn like '%"+curNameEn.trim()+"%'");
		}
		if(null!=curName && !"".equals(curName) ){
			sql.append(" and obj.curName like '%"+curName.trim()+"%'");
		}
		if(null!=curUnit && !"".equals(curUnit) ){
			sql.append(" and obj.curUnit like '%"+curUnit.trim()+"%'");
		}
//		if(null!=curRate && !"".equals(curRate) ){
//			sql.append(" and obj.curRate="+curRate);
//		}
		
		QueryInfo queryInfo = new QueryInfo();
		// 设置每页显示多少行
		int pageCount = 15;
		// 取得页面选择的每页显示条数
		pageCount = Integer.parseInt(limit);
		// 设定每页显示记录数
		queryInfo.setCountOnEachPage(pageCount);
		// 设置查询记录总数语句
		queryInfo.setCountQuery("select count(*) from CotCurrency obj "
				+ sql.toString());
		// 设置查询记录语句
		queryInfo.setSelectString("from CotCurrency obj ");
		// 设置条件语句
		queryInfo.setQueryString(sql.toString());
		// 设置排序语句
		queryInfo.setOrderString("");
		int startIndex = Integer.parseInt(start);
		queryInfo.setStartIndex(startIndex);
		try {
			String json = this.getBaseDao().getJsonData(queryInfo);
			response.getWriter().write(json);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
