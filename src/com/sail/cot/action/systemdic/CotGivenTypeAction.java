/**
 * 
 */
package com.sail.cot.action.systemdic;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
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
import com.sail.cot.domain.CotGivenType;
import com.sail.cot.query.QueryInfo;
import com.sail.cot.service.systemdic.CotGivenTypeService;
import com.sail.cot.util.ReflectionUtils;
import com.sail.cot.util.SystemUtil;

/**
 * 送样方式管理
 * @author qh-chzy
 *
 */
public class CotGivenTypeAction extends AbstractAction {

	private CotGivenTypeService cotGivenTypeService;

	public CotGivenTypeService getCotGivenTypeService() {
		if (cotGivenTypeService == null) {
			cotGivenTypeService = (CotGivenTypeService) super.getBean("CotGivenTypeService");
		}
		return cotGivenTypeService;
	}

	@Override
	public ActionForward add(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
//		//1.使用反射获取对象的所有属性名称
//		String[] properties = ReflectionUtils.getDeclaredFields(CotGivenType.class);
//		//2.获取页面传入的所有属性的值
//		String[] recordKeys = request.getParameterValues(TableConstants.RECORDKEY_NAME);
//		List<CotGivenType> records = new ArrayList<CotGivenType>();
//		//声明需要多少个类
//		for(int i=0; i<properties.length; i++)
//		{
//			String[] values = request.getParameterValues(properties[i]);
//			if(values != null)
//			{
//				for(int j=0; j<values.length; j++)
//				{
//					CotGivenType cotGivenType = new CotGivenType();
//					records.add(cotGivenType);
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
//		this.getCotGivenTypeService().addGivenTypes(records);
//		
//		try {
//			super.defaultAjaxResopnse("givenTypeTable", recordKeys, resultCodes, messages, request, response);
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
//		String[] properties = ReflectionUtils.getDeclaredFields(CotGivenType.class);
//		//String[] ids = request.getParameterValues("id");
//		String[] recordKeys = request.getParameterValues(TableConstants.RECORDKEY_NAME);
//		List records = new ArrayList();
//		//Integer iVer = new Integer(request.getParameter("ver"));
//		for(int i=0; i<recordKeys.length; i++)
//		{
//			CotGivenType givenType= new CotGivenType();
//			givenType.setId(new Integer(recordKeys[i]));
//			records.add(givenType);
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
//		this.getCotGivenTypeService().modifyGivenTypes(records);
//		try {
//			super.defaultAjaxResopnse("givenTypeTable", recordKeys, resultCodes, messages, request, response);
//		} catch (ServletException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		return null;
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
//				CotGivenType givenType= new CotGivenType();
//				givenType.setId(new Integer(recordKeys[i]));
//				list.add(givenType);
//				
//			}
//			this.getCotGivenTypeService().deleteGivenTypes(list);
//			resultCodes = new int[recordKeys.length];
//			for (int i=0; i<resultCodes.length; i++) {
//				resultCodes[i] = 1;
//			}
//		}
//		try {
//			super.defaultAjaxResopnse("givenTypeTable", recordKeys, resultCodes, messages, request, response);
//		} catch (ServletException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
		return null;
	}
	
	@Override
	public ActionForward query(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		
		String start = request.getParameter("start");
		String limit = request.getParameter("limit");
		
		if(start == null || limit == null)
			return mapping.findForward("querySuccess");
		
		String givenName = request.getParameter("givenName");

		StringBuffer queryString = new StringBuffer();
		queryString.append(" where 1=1");
		if(givenName!=null && !givenName.trim().equals("")){
			queryString.append(" and obj.givenName like '%"+givenName.trim()+"%'");
		}
		
		QueryInfo queryInfo = new QueryInfo();
		 
		// 设置每页显示多少行
		int pageCount = 15;
		pageCount = Integer.parseInt(limit);
		// 设定每页显示记录数
		queryInfo.setCountOnEachPage(pageCount);
		//设置查询记录总数语句
		queryInfo.setCountQuery("select count(*) from CotGivenType obj"+queryString);
		//设置查询记录语句
		queryInfo.setSelectString("from CotGivenType obj");
		//设置条件语句
		queryInfo.setQueryString(queryString.toString());
		//设置排序语句
		queryInfo.setOrderString("");
		int startIndex = Integer.parseInt(start);
		queryInfo.setStartIndex(startIndex);
		String json;
		try {
			json = this.getCotGivenTypeService().getJsonData(queryInfo);
			response.getWriter().write(json);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;		
	}
}
