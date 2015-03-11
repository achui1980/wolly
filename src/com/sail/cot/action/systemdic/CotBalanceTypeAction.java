/**
 * 
 */
package com.sail.cot.action.systemdic;

import java.io.IOException;

import com.sail.cot.util.ReflectionUtils;
import com.sail.cot.util.SystemUtil;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import com.sail.cot.action.AbstractAction;
import com.sail.cot.domain.CotBalanceType;
import com.sail.cot.mail.service.MailRecvService;
import com.sail.cot.query.QueryInfo;
import com.sail.cot.service.systemdic.CotBalanceTypeService;

/**
 * 费用结算方式管理
 * @author qh-chzy
 *
 */
public class CotBalanceTypeAction extends AbstractAction {

	private CotBalanceTypeService cotBalanceTypeService;
	
	private MailRecvService mailRecvService;

	public CotBalanceTypeService getCotBalanceTypeService() {
		if (cotBalanceTypeService == null) {
			cotBalanceTypeService = (CotBalanceTypeService) super.getBean("CotBalanceTypeService");
		}
		return cotBalanceTypeService;
	}
	
	public MailRecvService getMailRecvService() {
		if (mailRecvService == null) {
			mailRecvService = (MailRecvService) super.getBean("recvMailService");
		}
		return mailRecvService;
	}

	@Override
	public ActionForward add(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
//		1.使用反射获取对象的所有属性名称
//		String[] properties = ReflectionUtils.getDeclaredFields(CotBalanceType.class);
//		//2.获取页面传入的所有属性的值
//		String[] recordKeys = request.getParameterValues(TableConstants.RECORDKEY_NAME);
//		List records = new ArrayList();
//		java.util.Calendar cal = Calendar.getInstance();
//		int iYear = cal.get(Calendar.YEAR);
//		//声明需要多少个类
//		for(int i=0; i<properties.length; i++)
//		{
//			String[] values = request.getParameterValues(properties[i]);
//			if(values != null)
//			{
//				for(int j=0; j<values.length; j++)
//				{
//					CotBalanceType balanceType = new CotBalanceType();
//					//balanceType.setEditionId(allotEdit.getId());
//					records.add(balanceType);
//				}
//				break;
//			}
//		}
//		for(int i=0 ; i< properties.length ; i++)
//		{
//			//获取页面传过来的属性值
//			
//			String[] values = request.getParameterValues(properties[i]);
//			if(values != null)
//			{
//				for(int j=0; j<values.length; j++)
//				{
//					try {
//						//设置对象个属性
//						BeanUtils.setProperty(records.get(j), properties[i], values[j]);
//					} catch (IllegalAccessException e) {
//						e.printStackTrace();
//					} catch (InvocationTargetException e) {
//						e.printStackTrace();
//					}
//				}
//			}
//		}
//		
////		返回的结果数组，数组长度一般为增加对象的个数,1为成功，0为失败 
//		//暂时都设为成功
//		int[] resultCodes = new int[records.size()];
//		for (int i=0; i<resultCodes.length; i++) {
//			resultCodes[i] = 1;
//		}
////		返回的信息数组，暂时设为空
//		String[] messages = null;
//		
//		//调用service更新记录
//		this.getCotBalanceTypeService().addBalanceTypes(records);
//		
//		try {
//			super.defaultAjaxResopnse(null, recordKeys, resultCodes, messages, request, response);
//		} catch (ServletException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
		return null;
	}

	@Override
	public ActionForward modify(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
//		String[] properties = ReflectionUtils.getDeclaredFields(CotBalanceType.class);
//		//String[] ids = request.getParameterValues("id");
//		String[] recordKeys = request.getParameterValues(TableConstants.RECORDKEY_NAME);
//		List records = new ArrayList();
//		//Integer iVer = new Integer(request.getParameter("ver"));
//		for(int i=0; i<recordKeys.length; i++)
//		{
//			CotBalanceType balanceType= new CotBalanceType();
//			balanceType.setId(new Integer(recordKeys[i]));
//			records.add(balanceType);
//		}
//		//设置对象属性信息
//		for(int i=0; i<properties.length; i++)
//		{
//			String[] values = request.getParameterValues(properties[i]);
//			if(values != null)
//			{
//				for(int j=0; j<values.length; j++)
//				{
//					try {
//						BeanUtils.setProperty(records.get(j), properties[i], values[j]);
//					} catch (IllegalAccessException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					} catch (InvocationTargetException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
//				}
//			}
//		}
//		
//		//更新数据
//		int[] resultCodes = new int[records.size()];
//		for(int i=0; i<resultCodes.length; i++)
//		{
//			resultCodes[i] = 1;
//		}
//		String[] messages = null;
//		
//		this.getCotBalanceTypeService().modifyBalanceTypes(records);
//		try {
//			super.defaultAjaxResopnse(null, recordKeys, resultCodes, messages, request, response);
//		} catch (ServletException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		return null;
	}
	
	//跳转到添加页面
	public ActionForward addBalanceType(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		return mapping.findForward("add");
	}
	
	public ActionForward queryBalanceType(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		return mapping.findForward("modify");
	}
	
	@Override
	public ActionForward query(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		
//		Limit limit=RequestUtils.getLimit(request,"balancetypeTable");
//		Map<?, ?> sortValueMap = limit.getSort().getSortValueMap();
//		Map<?, ?> filterPropertyMap = limit.getFilterSet().getPropertyValueMap();
//		
//		String balanceNameFind = request.getParameter("balanceNameFind");
//		StringBuffer queryString = new StringBuffer();
//		//不显示admin用户
//		queryString.append(" where 1=1");
//		if(balanceNameFind!=null && !balanceNameFind.trim().equals("")){
//			queryString.append(" and obj.balanceName like '%"+balanceNameFind.trim()+"%'");
//		}
//		
//		QueryInfo queryInfo = new QueryInfo();
//		 
//		// 设置每页显示多少行
//		int pageCount = 15;
//		// 取得页面选择的每页显示条数
//		String pc = request.getParameter("balancetypeTable_crd");
//		if (pc != null) {
//			pageCount = Integer.parseInt(pc);
//		}
//		// 设定每页显示记录数
//		queryInfo.setCountOnEachPage(pageCount);
//		//设置查询记录总数语句
//		queryInfo.setCountQuery("select count(*) from CotBalanceType obj"+queryString);
//		//设置查询记录语句
//		queryInfo.setSelectString("from CotBalanceType obj");
//		//设置条件语句
//		queryInfo.setQueryString(queryString.toString());
//		//设置排序语句
//		queryInfo.setOrderString("");
//		 
//		int totalCount = this.getCotBalanceTypeService().getRecordCount(queryInfo);
//		limit.setRowAttributes(totalCount, 15);
//		int startIndex = limit.getRowStart();
//		
//		queryInfo.setStartIndex(startIndex);
//		
//		if (sortValueMap != null && !sortValueMap.isEmpty()) {
//			System.out.println("EC___"+ECSideUtils.getDefaultSortSQL(sortValueMap));
//			queryInfo.setOrderString(SystemUtil.getDefaultSortSQL("obj",sortValueMap));
//		}
//		List<?> list = this.getCotBalanceTypeService().getList(queryInfo);
//		request.setAttribute("cotbalancetype", list);
		return mapping.findForward("querySuccess");
	}

	@Override
	public ActionForward remove(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		//获取页面传入的所有recordKey的属性值
//		String[] recordKeys = request.getParameterValues(TableConstants.RECORDKEY_NAME);
//		int[] resultCodes = new int[0];
//		String[] messages = null;
//		if (recordKeys != null) {
//			List list = new ArrayList();
//			for (int i=0; i<recordKeys.length; i++) {
//				CotBalanceType balanceType= new CotBalanceType();
//				balanceType.setId(new Integer(recordKeys[i]));
//				list.add(balanceType);
//				
//			}
//			this.getCotBalanceTypeService().deleteBalanceTypes(list);
//			resultCodes = new int[recordKeys.length];
//			for (int i=0; i<resultCodes.length; i++) {
//				resultCodes[i] = 1;
//			}
//		}
//		try {
//			super.defaultAjaxResopnse(null, recordKeys, resultCodes, messages, request, response);
//		} catch (ServletException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
		return null;
	}
	public ActionForward queryEC(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
//		Limit limit=RequestUtils.getLimit(request);
//		Map sortValueMap = limit.getSort().getSortValueMap();
//		Map filterPropertyMap = limit.getFilterSet().getPropertyValueMap();
//		
//		String balanceNameFind = request.getParameter("balanceNameFind");
//		String value = request.getParameter("ec_crd");
//		StringBuffer queryString = new StringBuffer();
//		//不显示admin用户
//		queryString.append(" where 1=1");
//		if(balanceNameFind!=null && !balanceNameFind.trim().equals("")){
//			queryString.append(" and obj.balanceName like '%"+balanceNameFind.trim()+"%'");
//		}
//		
//		
//		
//		QueryInfo queryInfo = new QueryInfo();
//		//设定每页显示记录数
//		queryInfo.setCountOnEachPage(Integer.parseInt(value));
//		//设置查询记录总数语句
//		queryInfo.setCountQuery("select count(*) from CotBalanceType obj"+queryString);
//		//设置查询记录语句
//		queryInfo.setSelectString("from CotBalanceType obj");
//		//设置条件语句
//		queryInfo.setQueryString(queryString.toString());
//		//设置排序语句
//		queryInfo.setOrderString("");
//		int totalCount = this.getCotBalanceTypeService().getRecordCount(queryInfo);
//		limit.setRowAttributes(totalCount, Integer.parseInt(value));
//		int startIndex = limit.getRowStart();
//		
//		queryInfo.setStartIndex(startIndex);
//		
//		if (sortValueMap != null && !sortValueMap.isEmpty()) {
//			System.out.println("EC___"+ECSideUtils.getDefaultSortSQL(sortValueMap));
//			queryInfo.setOrderString(SystemUtil.getDefaultSortSQL("obj",sortValueMap));
//		}
//		
//		request.setAttribute("balcanceec", this.getCotBalanceTypeService().getList(queryInfo));
		return mapping.findForward("querySuccess");
	}
	
	
}
