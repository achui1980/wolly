package com.sail.cot.action.fittings;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.sail.cot.action.AbstractAction;
import com.sail.cot.domain.CotEleFittings;
import com.sail.cot.domain.CotFittings;
import com.sail.cot.query.QueryInfo;
import com.sail.cot.service.fittings.CotFittingsService;
import com.sail.cot.util.GridServerHandler;
import com.sail.cot.util.SystemUtil;
 
 

public class CotFittingsAction extends AbstractAction {

	private CotFittingsService cotFittingsService;
	public CotFittingsService getCotFittingsService()
	{
		if(cotFittingsService==null)
		{
			cotFittingsService=(CotFittingsService) super.getBean("CotFittingsService");
		}
		return cotFittingsService;
	}
	@Override
	public ActionForward query(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		
		String fitNo = request.getParameter("fitNoFind");// 材料编号
		String fitName = request.getParameter("fitNameFind");// 配件名称
		String startTime = request.getParameter("startTime");
		String endTime = request.getParameter("endTime");
		String facId = request.getParameter("facIdFind");// 供应商
		String typeLv3Id = request.getParameter("typeLv3IdFind");// 配件类别
		
		String start = request.getParameter("start");
		String limit = request.getParameter("limit");
		
		if(start == null || limit == null)
			return mapping.findForward("querySuccess");
		
		StringBuffer queryString=new StringBuffer();
		queryString.append(" where 1=1");
		
		QueryInfo queryInfo = new QueryInfo();
		
		if (fitNo != null && !fitNo.trim().equals("")) {
			queryString.append(" and obj.fitNo like '%" + fitNo.trim()+"%'");
		}
		if (fitName != null && !fitName.trim().equals("")) {
			queryString.append(" and obj.fitName like '%" + fitName.trim()+"%'");
		}
		if (startTime != null && !"".equals(startTime.trim())) {
			queryString.append(" and obj.addTime >='" + startTime + "'");
		}
		if (endTime != null && !"".equals(endTime.trim())) {
			queryString.append(" and obj.addTime <='" + endTime
					+ " 23:59:59'");
		}
		if (facId != null && !facId.trim().equals("")) {
			queryString.append(" and obj.facId=" + facId.trim());
		}
		if (typeLv3Id != null && !typeLv3Id.trim().equals("")) {
			queryString.append(" and obj.typeLv3Id=" + typeLv3Id.trim());
		}
		
		// 设置每页显示多少行
		int pageCount = 15;
		// 取得页面选择的每页显示条数
		pageCount = Integer.parseInt(limit);
		// 设定每页显示记录数
		queryInfo.setCountOnEachPage(pageCount);
		//设置查询记录总数语句
		queryInfo.setCountQuery("select count(*) from CotFittings obj"+queryString.toString());
		//设置查询记录语句
		queryInfo.setSelectString("select obj from CotFittings obj");
		 
		//设置查询条件语句
		queryInfo.setQueryString(queryString.toString());
		//设置排序语句
		queryInfo.setOrderString(" order by obj.id desc");
		int startIndex = Integer.parseInt(start);	
		
		queryInfo.setStartIndex(startIndex);
		
		try {
			String json = this.getCotFittingsService().getJsonData(queryInfo);
			response.getWriter().write(json);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	// 跳转到标签添加页面
	public ActionForward addFittings(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		return mapping.findForward("addFittings");
	}
	
	// 查询配件历史报价
	public ActionForward loadPriceInfo(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String start = request.getParameter("start");
		String limit = request.getParameter("limit");
		
		if(start == null || limit == null)
			return mapping.findForward("loadPriceInfo");
		
		String fitId = request.getParameter("fitId");
		String startTime = request.getParameter("startTime");
		String endTime = request.getParameter("endTime");
		String facId = request.getParameter("facIdFind");// 供应商
		
		StringBuffer queryString=new StringBuffer();
		queryString.append(" where 1=1");
		
		QueryInfo queryInfo = new QueryInfo();
		
		if (fitId != null && !fitId.trim().equals("")) {
			queryString.append(" and obj.fitId=" + fitId.trim());
		}
		if (startTime != null && !"".equals(startTime.trim())) {
			queryString.append(" and obj.addTime >='" + startTime + "'");
		}
		if (endTime != null && !"".equals(endTime.trim())) {
			queryString.append(" and obj.addTime <='" + endTime
					+ " 23:59:59'");
		}
		if (facId != null && !facId.trim().equals("")) {
			queryString.append(" and obj.facId=" + facId.trim());
		}
		
		// 设置每页显示多少行
		int pageCount = 15;
		// 取得页面选择的每页显示条数
		pageCount = Integer.parseInt(limit);
		// 设定每页显示记录数
		queryInfo.setCountOnEachPage(pageCount);
		//设置查询记录总数语句
		queryInfo.setCountQuery("select count(*) from CotPriceOut obj"+queryString.toString());
		//设置查询记录语句
		queryInfo.setSelectString("from CotPriceOut obj"); 
		//设置查询条件语句
		queryInfo.setQueryString(queryString.toString());
		//设置排序语句
		queryInfo.setOrderString(" order by obj.addTime desc,obj.id desc");
		int startIndex = Integer.parseInt(start);
		queryInfo.setStartIndex(startIndex);
		try {
			String json = this.getCotFittingsService().getJsonData(queryInfo);
			response.getWriter().write(json);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	// 跳转到配件分发页面
	public ActionForward goFitEle(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		return mapping.findForward("goFitEle");
	}
	
	public ActionForward queryBlank(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		GridServerHandler gd = new GridServerHandler();
		gd.setData(null);
		gd.setTotalCount(0);
		String json = gd.getLoadResponseText();
		try {
			response.getWriter().write(json);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return mapping.findForward("goFitEle");
	}
	
	// 查询不含有该配件的样品
	public ActionForward queryEle(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		
		String start = request.getParameter("start");
		String limit = request.getParameter("limit");
		
		if(start == null || limit == null)
			return mapping.findForward("queryEle");
		
		String eleId = request.getParameter("eleIdFind");// 样品编号
		String eleTypeidLv1 = request.getParameter("eleTypeidLv1Find");// 材质
		String factoryId = request.getParameter("factoryIdFind");// 厂家
		String eleCol = request.getParameter("eleColFind");// 颜色
		String fitId = request.getParameter("fitId");// 配件编号

		StringBuffer queryString = new StringBuffer();
		queryString.append(" where 1=1");
		
		if (eleId != null && !eleId.toString().trim().equals("")) {
			queryString.append(" and ele.eleId like '%"
					+ eleId.toString().trim() + "%'");
		}
		if (eleTypeidLv1 != null && !eleTypeidLv1.toString().equals("")) {
			queryString.append(" and ele.eleTypeidLv1="
					+ eleTypeidLv1.toString());
		}
		if (factoryId != null && !factoryId.toString().equals("")) {
			queryString.append(" and ele.factoryId=" + factoryId.toString());
		}
		if (eleCol != null && !eleCol.toString().trim().equals("")) {
			queryString.append(" and ele.eleCol like '%"
					+ eleCol.toString().trim() + "%'");
		}
		//查找含有该配件的样品编号
		String ids = this.getCotFittingsService().getEleFitIds(Integer.parseInt(fitId));
		if(!"".equals(ids)){
			queryString.append(" and ele.id not in ("+ ids.substring(0,ids.length()-1)+ ")");
		}
		
		QueryInfo queryInfo = new QueryInfo();
		// 设置每页显示多少行
		int pageCount = 15;
		// 取得页面选择的每页显示条数
		pageCount = Integer.parseInt(limit);
		// 设定每页显示记录数
		queryInfo.setCountOnEachPage(pageCount);
		// 设置查询记录总数语句
		queryInfo.setCountQuery("select count(*) from CotElementsNew ele"
				+ queryString);
		String sql = "from CotElementsNew ele";

		queryInfo.setSelectString(sql);
		// 设置查询参数
		// 设置条件语句
		queryInfo.setQueryString(queryString.toString());
		// 设置排序语句
		queryInfo.setOrderString(" order by ele.id desc");
		int startIndex = Integer.parseInt(start);
		queryInfo.setStartIndex(startIndex);
		try {
			String excludes[] = {"cotPictures","childs","cotFile","cotPriceFacs","cotEleFittings",
					"cotElePrice","cotElePacking"};
			queryInfo.setExcludes(excludes);
			String json = this.getCotFittingsService().getJsonData(queryInfo);
			response.getWriter().write(json);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	//保存配件到样品档案
	@Override
	public ActionForward add(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String json = request.getParameter("data");
		String fitPrice = request.getParameter("fitPri");// 单价
		String fitId = request.getParameter("fitId");// 配件编号
		
		CotFittings fittings = this.getCotFittingsService().getFittingById(Integer.parseInt(fitId));

		//判断数据是一条还是多条
		List<CotEleFittings> records = new ArrayList<CotEleFittings>();
		boolean single = json.startsWith("{");
		if(single){
			JSONObject jsonObject = JSONObject.fromObject(json); 
			CotEleFittings eleFittings  = new CotEleFittings();
			eleFittings.setFitNo(fittings.getFitNo());
			eleFittings.setFitName(fittings.getFitName());
			Object fitDesc = jsonObject.get("fitDesc");
			if (fitDesc == null || fitDesc.toString().equals("null")) {
				eleFittings.setFitDesc("");
			} else {
				eleFittings.setFitDesc(fitDesc.toString());
			}
			eleFittings.setFitUseUnit(fittings.getUseUnit());
			eleFittings.setFitPrice(Double.parseDouble(fitPrice));
			eleFittings.setFittingId(Integer.parseInt(fitId));
			eleFittings.setFacId(fittings.getFacId());
			eleFittings.setFitRemark(fittings.getFitRemark());
			eleFittings.setEleId((Integer)jsonObject.get("eleId"));
			
			Object fitCount=jsonObject.get("fitCount");
			if(fitCount instanceof Integer){
				Integer temp = (Integer) fitCount;
				eleFittings.setFitCount(temp.doubleValue());
			}else{
				eleFittings.setFitCount((Double)fitCount);
			}
			
			Object fitUsedCount=jsonObject.get("fitUsedCount");
			if(fitUsedCount instanceof Integer){
				Integer temp = (Integer) fitUsedCount;
				eleFittings.setFitUsedCount(temp.doubleValue());
			}else{
				eleFittings.setFitUsedCount((Double)fitUsedCount);
			}
			records.add(eleFittings);
		}else{
			JSONArray jarray = JSONArray.fromObject(json);
			for (int i = 0; i < jarray.size(); i++) {
				JSONObject jsonObject = jarray.getJSONObject(i);
				CotEleFittings eleFittings = new CotEleFittings();
				eleFittings.setFitNo(fittings.getFitNo());
				eleFittings.setFitName(fittings.getFitName());
				
				Object fitDesc = jsonObject.get("fitDesc");
				if (fitDesc == null || fitDesc.toString().equals("null")) {
					eleFittings.setFitDesc("");
				} else {
					eleFittings.setFitDesc(fitDesc.toString());
				}
				
				eleFittings.setFitUseUnit(fittings.getUseUnit());
				eleFittings.setFitPrice(Double.parseDouble(fitPrice));
				eleFittings.setFittingId(Integer.parseInt(fitId));
				eleFittings.setFacId(fittings.getFacId());
				eleFittings.setFitRemark(fittings.getFitRemark());
				eleFittings.setEleId((Integer)jsonObject.get("eleId"));
				Object fitCount=jsonObject.get("fitCount");
				if(fitCount instanceof Integer){
					Integer temp = (Integer) fitCount;
					eleFittings.setFitCount(temp.doubleValue());
				}else{
					eleFittings.setFitCount((Double)fitCount);
				}
				
				Object fitUsedCount=jsonObject.get("fitUsedCount");
				if(fitUsedCount instanceof Integer){
					Integer temp = (Integer) fitUsedCount;
					eleFittings.setFitUsedCount(temp.doubleValue());
				}else{
					eleFittings.setFitUsedCount((Double)fitUsedCount);
				}
				records.add(eleFittings);
			}
		}
		//计算总价格
		for (int i = 0; i < records.size(); i++) {
			CotEleFittings eleFittings = records.get(i);
			double total = eleFittings.getFitPrice()*eleFittings.getFitCount()*eleFittings.getFitUsedCount();
			eleFittings.setFitTotalPrice(total);
		}
		JSONObject json1 = new JSONObject();
		try {
			if(records.size()>0){
				this.getCotFittingsService().addList(records);
			}
			json1.put("success", true);
			json1.put("msg", "成功");
			response.getWriter().write(json1.toString());
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return null;
	}
	
	//查看该配件分发了哪些货号
	public ActionForward findEleFen(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {

		String start = request.getParameter("start");
		String limit = request.getParameter("limit");
		
		if(start == null || limit == null)
			return null;
		
		String eleId = request.getParameter("eleIdFind");// 样品编号
		String eleTypeidLv1 = request.getParameter("eleTypeidLv1Find");// 材质
		String factoryId = request.getParameter("factoryIdFind");// 厂家
		String eleCol = request.getParameter("eleColFind");// 颜色
		String fitId = request.getParameter("fitId");// 配件编号

		StringBuffer queryString = new StringBuffer();
		queryString.append(" where obj.eleId=ele.id");
		
		if (eleId != null && !eleId.toString().trim().equals("")) {
			queryString.append(" and ele.eleId like '%"
					+ eleId.toString().trim() + "%'");
		}
		if (eleTypeidLv1 != null && !eleTypeidLv1.toString().equals("")) {
			queryString.append(" and ele.eleTypeidLv1="
					+ eleTypeidLv1.toString());
		}
		if (factoryId != null && !factoryId.toString().equals("")) {
			queryString.append(" and ele.factoryId=" + factoryId.toString());
		}
		if (eleCol != null && !eleCol.toString().trim().equals("")) {
			queryString.append(" and ele.eleCol like '%"
					+ eleCol.toString().trim() + "%'");
		}
		if (fitId != null && !fitId.toString().equals("")) {
			queryString.append(" and obj.fittingId="
					+ fitId.toString());
		}
		
		QueryInfo queryInfo = new QueryInfo();
		// 设置每页显示多少行
		int pageCount = 15;
		// 取得页面选择的每页显示条数
		pageCount = Integer.parseInt(limit);
		// 设定每页显示记录数
		queryInfo.setCountOnEachPage(pageCount);
		// 设置查询记录总数语句
		queryInfo.setCountQuery("select count(*) from CotEleFittings obj,CotElementsNew ele"
				+ queryString);
		String sql = "select ele from CotEleFittings obj,CotElementsNew ele";

		queryInfo.setSelectString(sql);
		// 设置查询参数
		// 设置条件语句
		queryInfo.setQueryString(queryString.toString());
		// 设置排序语句
		queryInfo.setOrderString(" order by ele.eleId asc");
		// 得到limit对象
		int startIndex = Integer.parseInt(start);
		queryInfo.setStartIndex(startIndex);
		// 根据起始
		try {
			String excludes[] = {"cotPictures","childs","cotFile","cotPriceFacs","cotEleFittings",
					"cotElePrice","cotElePacking"};
			queryInfo.setExcludes(excludes);
			String json = this.getCotFittingsService().getJsonData(queryInfo);
			response.getWriter().write(json);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public ActionForward addPriceOut(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		return mapping.findForward("addPriceOut");
	}

	@Override
	public ActionForward modify(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ActionForward remove(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		return null;
	}
	
	//跳转到excel导入页面
	public ActionForward showExcel(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		return mapping.findForward("showExcel");
	}
	 
}
