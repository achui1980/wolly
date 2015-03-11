/**
 * 
 */
package com.sail.cot.action.orderfac;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.sail.cot.action.AbstractAction;
import com.sail.cot.domain.CotArtWork;
import com.sail.cot.domain.CotEmps;
import com.sail.cot.domain.CotFinaceAccountdeal;
import com.sail.cot.domain.CotFinaceOther;
import com.sail.cot.domain.CotOrderDetail;
import com.sail.cot.domain.CotOrderFacdetail;
import com.sail.cot.domain.CotOrderPic;
import com.sail.cot.domain.CotOrderStatus;
import com.sail.cot.domain.CotOrderfacPic;
import com.sail.cot.domain.CotTypeLv1;
import com.sail.cot.query.QueryInfo;
import com.sail.cot.service.img.CotOpImgService;
import com.sail.cot.service.orderfac.CotOrderFacService;
import com.sail.cot.util.GridServerHandler;
import com.sail.cot.util.ReflectionUtils;
import com.sail.cot.util.SystemUtil;

public class CotOrderFacAction extends AbstractAction {

	private CotOrderFacService orderFacService;
	public CotOrderFacService getOrderFacService() {
		if (orderFacService == null) {
			orderFacService = (CotOrderFacService) super.getBean("CotOrderFacService");
		}
		return orderFacService;
	}

	public void setOrderFacService(CotOrderFacService orderFacService) {
		this.orderFacService = orderFacService;
	}
	
//	// 查询出货单数据
//	@Override
//	public ActionForward query(ActionMapping mapping, ActionForm form,
//			HttpServletRequest request, HttpServletResponse response) {
//		
//		String start = request.getParameter("start");
//		String limit = request.getParameter("limit");	
//		if(start == null || limit == null)
//			return mapping.findForward("querySuccess");
//		String orderNoFind = request.getParameter("orderNoFind"); //发票编号
//		String businessPerson = request.getParameter("businessPerson"); //业务员
//		String factoryId = request.getParameter("factoryId");//厂家
//		String currencyId = request.getParameter("currencyId");//币种
//		String startTime = request.getParameter("startTime"); //下单起始日期
//		String endTime = request.getParameter("endTime"); //下单结束日期
//		String status = request.getParameter("orderStatus");// 审核状态
//		StringBuffer queryString = new StringBuffer();
//		String con =request.getParameter("con");//有值表示从弹窗进入
//		String typeLv1Id =request.getParameter("typeLv1IdFind");
//		
//		String canOut = request.getParameter("canOut");//出货状态
//		// 获得登录人
//		CotEmps emp = (CotEmps) request.getSession().getAttribute("emp");
//		if ("admin".equals(emp.getEmpsId())) {
//			queryString.append(" where obj.businessPerson=e.id");
//			queryString.append(" and obj.orderId=fac.id");
//		} else {
//			// 判断是否有最高权限
//			boolean all = SystemUtil.isAction(request, "cotorderfac.do", "ALL");
//			if (all == true) {
//				queryString.append(" where obj.businessPerson=e.id");
//				queryString.append(" and obj.orderId=fac.id");
//			} else {
//				// 判断是否有查看该部门征样的权限
//				boolean dept = SystemUtil.isAction(request, "cotorderfac.do","DEPT");
//				if (dept == true) {
//					queryString.append(" where obj.businessPerson = e.id and e.deptId="+ emp.getDeptId());
//				} else {
//					queryString.append(" where obj.businessPerson = e.id and obj.businessPerson ="+ emp.getId());
//				}
//			}
//			if(con.trim().equals("No")){
//				queryString.append(" and obj.checkPerson =" +emp.getId());
//				queryString.append(" and obj.orderStatus=3");
//			}
//		}
//		
//		if (status != null && !status.trim().equals("")) {
//			if("1".equals(status)){
//				queryString.append(" and (obj.orderStatus=3 or obj.orderStatus=0)");
//			}else{
//				if("2".equals(status)){
//					queryString.append(" and obj.orderStatus=2");
//				}
//			}
//		}
//		 
//		if (orderNoFind != null && !orderNoFind.trim().equals("")) {
//			queryString.append(" and obj.orderNo like '%" + orderNoFind.trim() + "%'");
//		}
//		if (businessPerson != null && !businessPerson.trim().equals("")) {
//			queryString.append(" and obj.businessPerson=" + businessPerson.trim());
//		}
//		if (factoryId != null && !factoryId.trim().equals("")) {
//			queryString.append(" and obj.factoryId=" + factoryId.trim());
//		}
//		if (currencyId != null && !currencyId.trim().equals("")) {
//			queryString.append(" and obj.currencyId=" + currencyId.trim());
//		}
//		if (startTime!=null && !"".equals(startTime.trim())) {
//			queryString.append(" and obj.orderTime >='" + startTime + "'");
//		}
//		if (endTime!=null && !"".equals(endTime.trim())) {
//			queryString.append(" and obj.orderTime <='" + endTime + " 23:59:59'");
//		}
//		
//		if(canOut!=null){
//			if ("".equals(canOut) || "0".equals(canOut)) {
//				queryString.append(" and (fac.canOut is null or fac.canOut=1 or fac.canOut=0 or fac.canOut=3)");
//			}else{
//				queryString.append(" and fac.canOut=2");
//			}
//		}
//		//department
//		if (typeLv1Id!= null && !"".equals(typeLv1Id.trim())) {
//			queryString.append(" and obj.typeLv1Id =" +Integer.parseInt(typeLv1Id));
//		}
//		
//		// 设置查询参数
//		QueryInfo queryInfo = new QueryInfo();
//		// 设置每页显示多少行
//		int pageCount = 15;
//		// 取得页面选择的每页显示条数
//		pageCount = Integer.parseInt(limit);
//		// 设定每页显示记录数
//		queryInfo.setCountOnEachPage(pageCount);
//		// 设置查询记录总数语句
//		queryInfo.setCountQuery("select count(*) from CotOrderFac obj,CotEmps e,CotOrder fac" + queryString);
//		 
//		// 查询语句
//		queryInfo.setSelectString("select obj from CotOrderFac obj,CotEmps e,CotOrder fac");
//		// 设置条件语句
//		queryInfo.setQueryString(queryString.toString());
//		// 设置排序语句
//		queryInfo.setOrderString(" order by obj.orderNo asc");
//		//queryInfo.setOrderString(" order by obj.id asc");
//		int startIndex = Integer.parseInt(start);
//		queryInfo.setStartIndex(startIndex);
//		String excludes[] = {"orderMBImg","cotOrderFacdetails"};
//		queryInfo.setExcludes(excludes);
//		try {
//			
//			String json = this.getOrderFacService().getJsonData(queryInfo);
//			response.getWriter().write(json);
//			
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return null;
//	}
	
	// 查询出货单数据
	@Override
	public ActionForward query(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		
		String start = request.getParameter("start");
		String limit = request.getParameter("limit");	
		if(start == null || limit == null)
			return mapping.findForward("querySuccess");
		String orderNoFind = request.getParameter("orderNoFind"); //发票编号
		String businessPerson = request.getParameter("businessPerson"); //业务员
		String factoryId = request.getParameter("factoryId");//厂家
		String companyId = request.getParameter("companyId");//厂家
		String currencyId = request.getParameter("currencyId");//币种
		String startTime = request.getParameter("startTime"); //下单起始日期
		String endTime = request.getParameter("endTime"); //下单结束日期
		String status = request.getParameter("orderStatus");// 审核状态
		StringBuffer queryString = new StringBuffer();
		String con =request.getParameter("con");//有值表示从弹窗进入
		String typeLv1Id =request.getParameter("typeLv1IdFind");
		String startCheckTime = request.getParameter("startCheckTime");// 审核起始日期
		String endCheckTime = request.getParameter("endCheckTime");// 审核结束日期
		String canOut = request.getParameter("canOut");//出货状态
		String themeStr = request.getParameter("themeStr");
		
		queryString.append(" where 1=1");
		// 获得登录人
		CotEmps emp = (CotEmps) request.getSession().getAttribute("emp");
		if (!"admin".equals(emp.getEmpsId())) {
			// 判断是否有最高权限
			boolean all = SystemUtil.isAction(request, "cotorderfac.do", "ALL");
			if (all == false) {
				JSONObject json = SystemUtil.getEmpDataPopedom(emp.getEmpRight());
				//判断国家权限
				boolean nation = SystemUtil.isAction(request, "cotorderfac.do", "NATION");
				if(nation ==true){
					String nationStr = json.getString("nation");
					queryString.append(" and obj.nation_Id in(").append(nationStr).append(")");
				}
				// 判断是否有查看该部门报价的权限
				boolean dept = SystemUtil.isAction(request, "cotorderfac.do", "DEPT");
				if (dept == true) {
					String deptStr = json.getString("dept");
					queryString.append(" and obj.typeLv1_Id in(").append(deptStr).append(")");
				} else {
					//queryString.append(" and obj.BUSINESS_PERSON =" + emp.getId());
				}
			}
			if(con!=null && con.trim().equals("No")){
				queryString.append(" and obj.checkPerson =" +emp.getId());
				queryString.append(" and obj.ORDER_STATUS=3");
			}
		}
		
		if (status != null && !status.trim().equals("")) {
			if("1".equals(status)){
				queryString.append(" and (obj.ORDER_STATUS=3 or obj.ORDER_STATUS=0)");
			}else{
				if("2".equals(status)){
					queryString.append(" and obj.ORDER_STATUS=2");
				}
			}
		}
		 
		if (orderNoFind != null && !orderNoFind.trim().equals("")) {
			queryString.append(" and obj.ORDER_NO like '%" + orderNoFind.trim() + "%'");
		}
		if (businessPerson != null && !businessPerson.trim().equals("")) {
			queryString.append(" and obj.BUSINESS_PERSON=" + businessPerson.trim());
		}
		if (factoryId != null && !factoryId.trim().equals("")) {
			queryString.append(" and obj.factory_id=" + factoryId.trim());
		}
		if (companyId != null && !companyId.trim().equals("")) {
			queryString.append(" and obj.company_id=" + companyId.trim());
		}
		if (currencyId != null && !currencyId.trim().equals("")) {
			queryString.append(" and obj.CURRENCY_ID=" + currencyId.trim());
		}
		if (startTime!=null && !"".equals(startTime.trim())) {
			queryString.append(" and obj.ORDER_TIME >='" + startTime + "'");
		}
		if (endTime!=null && !"".equals(endTime.trim())) {
			queryString.append(" and obj.ORDER_TIME <='" + endTime + " 23:59:59'");
		}
		if (startCheckTime != null && !"".equals(startCheckTime.trim())) {
			queryString.append(" and obj.check_date >='" + startCheckTime + "'");
		}
		if (endCheckTime != null && !"".equals(endCheckTime.trim())) {
			queryString.append(" and obj.check_date <='" + endCheckTime + " 23:59:59'");
		}
		if(canOut!=null){
			if ("".equals(canOut) || "0".equals(canOut)) {
				queryString.append(" and (od.can_out is null or od.can_out=1 or od.can_out=0 or od.can_out=3)");
			}else{
				queryString.append(" and od.can_out=2");
			}
		}
		//department
		if (typeLv1Id!= null && !"".equals(typeLv1Id.trim())) {
			//String hql = "select id from CotTypeLv1 obj where obj.typeEnName like '"+typeLv1Id+"%'";
			List list = this.getOrderFacService().getObjList("CotTypeLv1");
			if(CollectionUtils.isEmpty(list)){
				queryString.append(" and obj.typeLv1_id = '0'");
			}else {
				List idsList = new ArrayList();
				for(int i = 0;i<list.size();i++){
					CotTypeLv1 lv1 = (CotTypeLv1)list.get(i);
					if(lv1.getTypeEnName().toLowerCase().indexOf(typeLv1Id.toLowerCase()) > -1){
						idsList.add(lv1.getId());
					}
				}
				String ids  = "0";
				if(idsList.size() > 0){
					ids = StringUtils.join(idsList.toArray(),",");
				}
				queryString.append(" and obj.typeLv1_id in("+ids+")");
			}
			//queryString.append(" and obj.typeLv1_id =" +Integer.parseInt(typeLv1Id));
		}
		if (themeStr!= null && !"".equals(themeStr.trim())) {
			queryString.append(" and obj.themeStr like '%" + themeStr.trim() + "%'");
			}
		
		// 设置查询参数
		QueryInfo queryInfo = new QueryInfo();
		// 设置每页显示多少行
		int pageCount = 15;
		// 取得页面选择的每页显示条数
		pageCount = Integer.parseInt(limit);
		// 设定每页显示记录数
		queryInfo.setCountOnEachPage(pageCount);
		// 设置查询记录总数语句
		queryInfo.setCountQuery("select count(*) from cot_order_fac obj left join cot_order od on obj.order_id=od.id " 
				+ "left join cot_emps e on obj.BUSINESS_PERSON=e.id "+ queryString);
		 
		// 查询语句
		queryInfo.setSelectString("select obj.id," +
				"obj.factory_id as factoryId," 
				+ "obj.ORDER_NO as orderNo," 
				+ "obj.CURRENCY_ID as currencyId," 
				+ "obj.ORDER_TIME as orderTime," 
				+ "obj.SEND_TIME as sendTime," 
				+ "obj.BUSINESS_PERSON as businessPerson, " 
				+ "obj.total_Money as totalMoney," 
				+ "obj.ORDER_STATUS as orderStatus," 
				+ "obj.order_id as orderId," 
				+ "obj.PO_NO as poNo," 
				+ "obj.all_pin_name as allPinName," 
				+ "obj.shipment_Date as shipmentDate," 
				+ "obj.pay_Type_Id as payTypeId," 
				+ "obj.clause_Type_Id as clauseTypeId," 
				+ "obj.order_lc_delay as orderLcDelay,"
				+ "obj.new_Remark as newRemark,"
				+ "obj.typeLv1_id as typeLv1Id,"
				+ "od.order_Status as piStatus,"
				+ "obj.checkPerson as checkPerson"
				+ " from cot_order_fac obj left join cot_order od on obj.order_id=od.id " 
				+ "left join cot_emps e on obj.BUSINESS_PERSON=e.id");
		// 设置条件语句
		queryInfo.setQueryString(queryString.toString());
		// 设置排序语句
		queryInfo.setOrderString(" order by obj.ORDER_NO asc");
		queryInfo.setQueryObjType("CotOrderFacVO");
		int count = this.getOrderFacService().getRecordCountJDBC(queryInfo);

		// 取得排序信息
		int startIndex = Integer.parseInt(start);
		queryInfo.setStartIndex(startIndex);

		GridServerHandler gd = new GridServerHandler();
		List res = this.getOrderFacService().getOrderFacVO(queryInfo);
		gd.setData(res);
		gd.setTotalCount(count);
		try {
			response.getWriter().write(gd.getLoadResponseText());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	// 查询该采购单的采购明细产品的信息
	public ActionForward queryDetail(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		String flag = request.getParameter("flag");// 用于区分是详细页面,还是编辑页面调用
		String start = request.getParameter("start");
		String limit = request.getParameter("limit");	
		
		if(start == null || limit == null)
			return mapping.findForward(flag);
		
		
		String orderId = request.getParameter("orderId");// 主采购单号

		if (orderId == null || "".equals(orderId)) {
			return null;
		}
		StringBuffer queryString = new StringBuffer();
		queryString.append(" where obj.orderId=" + orderId);
	
		
		
		// 设置查询参数
		QueryInfo queryInfo = new QueryInfo();
		// 设置每页显示多少行
		int pageCount = 15;
		// 取得页面选择的每页显示条数
		pageCount = Integer.parseInt(limit);

		// 设定每页显示记录数
		queryInfo.setCountOnEachPage(pageCount);
		// 设置查询记录总数语句
		queryInfo.setCountQuery("select count(*) from CotOrderFacdetail obj"
				+ queryString);
		// 设置查询记录语句
		queryInfo.setSelectString("from CotOrderFacdetail obj");
		// 设置条件语句
		queryInfo.setQueryString(queryString.toString());
		// 设置排序语句
		queryInfo.setOrderString(" order by obj.sortNo asc,obj.id asc");
		int startIndex = Integer.parseInt(start);
		queryInfo.setStartIndex(startIndex);

		List<?> list = this.getOrderFacService().getList(queryInfo);
		int totalCount = this.getOrderFacService().getRecordCount(queryInfo);
		//将当前明细list存到内存中的Map
		HttpSession session = request.getSession();
		Map<?, ?> facMap = this.getOrderFacService().getFactoryNameMapAction(request);
		if(flag.equals("orderFacDetail")){
			 for (int i = 0; i < list.size(); i++) {
			    CotOrderFacdetail detail = (CotOrderFacdetail) list.get(i);
			    detail.setPicImg(null);
			    if(detail.getFactoryId()!=null){
			    	detail.setFactoryShortName(facMap.get(detail.getFactoryId().toString()).toString());
				}
			    //用随机数作为key	
			    String rdm = "1" + RandomStringUtils.randomNumeric(8);
				detail.setRdm(rdm);
			    String eleNo = detail.getEleId();
			    this.getOrderFacService().setMapByRdmAction(session,rdm, detail);
			}
		}
		if(flag.equals("orderToOrderFac")){
			 for (int i = 0; i < list.size(); i++) {
			    CotOrderFacdetail detail = (CotOrderFacdetail) list.get(i);
			    detail.setPicImg(null);
			    
			    //用随机数作为key	
			    String rdm = "1" + RandomStringUtils.randomNumeric(8);
				detail.setRdm(rdm);
				this.getOrderFacService().setMapByRdmAction(session,rdm, detail);
			}
		}
		try{
			GridServerHandler gd = new GridServerHandler();
			gd.setData(list);
			gd.setTotalCount(totalCount);
			response.getWriter().write(gd.getLoadResponseText());	
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		return null;
	}
	
	// 查询该采购单的采购明细产品的信息
	public ActionForward queryDetailCopy(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		String flag = request.getParameter("flag");// 用于区分是详细页面,还是编辑页面调用
		String start = request.getParameter("start");
		String limit = request.getParameter("limit");	
		
		if(start == null || limit == null)
			return mapping.findForward(flag);
		
		
		String orderId = request.getParameter("orderId");// 主采购单号

		if (orderId == null || "".equals(orderId)) {
			return null;
		}
		StringBuffer queryString = new StringBuffer();
		queryString.append(" where obj.orderId=" + orderId);
	
		
		
		// 设置查询参数
		QueryInfo queryInfo = new QueryInfo();
		// 设置每页显示多少行
		int pageCount = 15;
		// 取得页面选择的每页显示条数
		pageCount = Integer.parseInt(limit);

		// 设定每页显示记录数
		queryInfo.setCountOnEachPage(pageCount);
		// 设置查询记录总数语句
		queryInfo.setCountQuery("select count(*) from CotOrderFacdetailCopy obj"
				+ queryString);
		// 设置查询记录语句
		queryInfo.setSelectString("from CotOrderFacdetailCopy obj");
		// 设置条件语句
		queryInfo.setQueryString(queryString.toString());
		// 设置排序语句
		queryInfo.setOrderString(" order by obj.sortNo asc,obj.id asc");
		int startIndex = Integer.parseInt(start);
		queryInfo.setStartIndex(startIndex);

		List<?> list = this.getOrderFacService().getList(queryInfo);
		int totalCount = this.getOrderFacService().getRecordCount(queryInfo);
		try{
			GridServerHandler gd = new GridServerHandler();
			gd.setData(list);
			gd.setTotalCount(totalCount);
			response.getWriter().write(gd.getLoadResponseText());	
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		return null;
	}
	
	//添加明细
	public ActionForward addDetail(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {

		//内存数据
		HttpSession session = request.getSession();
		Map<String, CotOrderFacdetail> orderfacMap = this.getOrderFacService().getMapByRdmAction(session);
		if (orderfacMap == null) {
			JSONObject error = new JSONObject();
			error.put("success", false);
			error.put("msg", "失败");
			try {
				response.getWriter().write(error.toString());
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		}

		List<CotOrderFacdetail> records = new ArrayList<CotOrderFacdetail>();
		List<CotOrderfacPic> imgList = new ArrayList<CotOrderfacPic>();
		CotOpImgService impOpService = (CotOpImgService) SystemUtil
				.getService("CotOpImgService");
		byte[] zwtpByte = this.getOrderFacService().getZwtpPic();
		
		String orderId = request.getParameter("orderId");		

		//前台传递来的数据
		String json = request.getParameter("data");
		//判断数据是一条还是多条
		boolean single = json.startsWith("{");
		if (single) {
			JSONObject jsonObject = JSONObject.fromObject(json);
			Object rdm = jsonObject.get("rdm");
			if(rdm!=null && !"".equals(rdm.toString())){
				CotOrderFacdetail cotOrderFacdetail = orderfacMap.get(rdm.toString());
	
				//设置总CBM
				if(cotOrderFacdetail.getContainerCount()==null || cotOrderFacdetail.getBoxCbm()==null){
					cotOrderFacdetail.setTotalCbm(0f);
				}else{
					cotOrderFacdetail.setTotalCbm(cotOrderFacdetail.getContainerCount()*cotOrderFacdetail.getBoxCbm());
				}
				//设置总毛重
				if(cotOrderFacdetail.getBoxCount()==null || cotOrderFacdetail.getBoxGrossWeigth()==null){
					cotOrderFacdetail.setTotalGrossWeigth(0f);
				}else{
					cotOrderFacdetail.setTotalGrossWeigth(cotOrderFacdetail.getBoxCount()*cotOrderFacdetail.getBoxGrossWeigth());
				}
				//设置总净重
				if(cotOrderFacdetail.getBoxCount()==null || cotOrderFacdetail.getBoxNetWeigth()==null){
					cotOrderFacdetail.setTotalNetWeigth(0f);
				}else{
					cotOrderFacdetail.setTotalNetWeigth(cotOrderFacdetail.getBoxCount()*cotOrderFacdetail.getBoxNetWeigth());
				}
				records.add(cotOrderFacdetail);
			}
		} else {
			JSONArray jarray = JSONArray.fromObject(json);
			for (int i = 0; i < jarray.size(); i++) {
				JSONObject jsonObject = jarray.getJSONObject(i);
				Object rdm = jsonObject.get("rdm");
				if(rdm!=null && !"".equals(rdm.toString())){
					CotOrderFacdetail cotOrderFacdetail = orderfacMap.get(rdm.toString());
					
					//设置总CBM
					if(cotOrderFacdetail.getContainerCount()==null || cotOrderFacdetail.getBoxCbm()==null){
						cotOrderFacdetail.setTotalCbm(0f);
					}else{
						cotOrderFacdetail.setTotalCbm(cotOrderFacdetail.getContainerCount()*cotOrderFacdetail.getBoxCbm());
					}
					//设置总毛重
					if(cotOrderFacdetail.getBoxCount()==null || cotOrderFacdetail.getBoxGrossWeigth()==null){
						cotOrderFacdetail.setTotalGrossWeigth(0f);
					}else{
						cotOrderFacdetail.setTotalGrossWeigth(cotOrderFacdetail.getBoxCount()*cotOrderFacdetail.getBoxGrossWeigth());
					}
					//设置总净重
					if(cotOrderFacdetail.getBoxCount()==null || cotOrderFacdetail.getBoxNetWeigth()==null){
						cotOrderFacdetail.setTotalNetWeigth(0f);
					}else{
						cotOrderFacdetail.setTotalNetWeigth(cotOrderFacdetail.getBoxCount()*cotOrderFacdetail.getBoxNetWeigth());
					}
					
					records.add(cotOrderFacdetail);
				}
			}
		}

		for (int i = 0; i < records.size(); i++) {
			CotOrderFacdetail detail = records.get(i);
			if (detail == null) {
				continue;
			}
			detail.setOrderId(Integer.parseInt(orderId));
			detail.setEleAddTime(new Date(System.currentTimeMillis()));// 添加时间
			//新建图片
		    CotOrderfacPic orderfacPic = new CotOrderfacPic();
		    
		    //从订单明细id获取订单明细对象
	    	CotOrderDetail orderDetail = this.getOrderFacService().getOrderDetailById(detail.getOrderDetailId());
	    	if(orderDetail!=null){
	    		
	    		//从订单明细id获取订单图片对象
		    	CotOrderPic orderPic = impOpService.getOrderPic(detail.getOrderDetailId());
		    	if(orderPic!=null){
		    		orderfacPic.setEleId(detail.getEleId());
		    		orderfacPic.setPicName(detail.getEleId());
		    		orderfacPic.setPicSize(orderPic.getPicSize());
		    		orderfacPic.setPicImg(orderPic.getPicImg());
		    	}else{
		    		orderfacPic.setEleId(detail.getEleId());
		    		orderfacPic.setPicName("zwtp");
		    		orderfacPic.setPicSize(zwtpByte.length);
		    		orderfacPic.setPicImg(zwtpByte);
		    	}
		    	
		    	imgList.add(orderfacPic);
	    		
	    		Long boxCount = detail.getBoxCount();//采购数量
	    		Long unBoxCount4OrderFac = orderDetail.getUnBoxCount4OrderFac() - boxCount;//未采购数量
	    		detail.setOutRemain(boxCount);
	    		if(unBoxCount4OrderFac > 0){
	    			orderDetail.setUnBoxCount4OrderFac(unBoxCount4OrderFac);
	    			orderDetail.setUnBoxSend4OrderFac(0);
	    		}else{
	    			orderDetail.setUnBoxCount4OrderFac(new Long(0));
	    			orderDetail.setUnBoxSend4OrderFac(1);
	    		}
	    		this.getOrderFacService().updateCotOrderDetail(orderDetail);
	    	} 		
		}   	
		// 保存
		this.getOrderFacService().addOrderFacDetails(records);
		
		//修改主单的总数量,总箱数,总体积,总金额
		this.getOrderFacService().modifyCotOrderFacTotal(Integer.parseInt(orderId));
		
		//插入采购图片信息表
		for(int i=0; i<records.size(); i++)
		{
			CotOrderFacdetail  orderFacdetail = (CotOrderFacdetail)records.get(i);
			CotOrderfacPic orderfacPic = (CotOrderfacPic)imgList.get(i);
			orderfacPic.setFkId(orderFacdetail.getId());
			List<CotOrderfacPic> picList = new ArrayList<CotOrderfacPic>();
			picList.add(orderfacPic);
			//逐条添加，避免数据量大的时候，内存溢出
			impOpService.saveImg(picList);
		}
		
		// 调用service更新记录
		//this.getOrderFacService().addOrderFacDetails(records);
		
		//将采购合同id及单号存入订单明细中
		this.getOrderFacService().saveIdAndNoToOrderDetail(Integer.parseInt(orderId));
		
		JSONObject suc = new JSONObject();
		suc.put("success", true);
		suc.put("msg", "成功");
		try {
			response.getWriter().write(suc.toString());
		} catch (IOException e) {
			e.printStackTrace();
			JSONObject error = new JSONObject();
			error.put("success", false);
			error.put("msg", "失败");
		}
		return null;
	}

    //修改明细
	public ActionForward modifyDetail(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
	
		//内存数据
		HttpSession session = request.getSession();
		Map<String, CotOrderFacdetail> orderfacMap = this.getOrderFacService().getMapByRdmAction(session);
		if (orderfacMap == null) {
//			JSONObject error = new JSONObject();
//			error.put("success", false);
//			error.put("msg", "失败");
//			try {
//				response.getWriter().write(error.toString());
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
			return null;
		}

		List<CotOrderFacdetail> records = new ArrayList<CotOrderFacdetail>();
		String orderId = request.getParameter("orderId");

		//前台传递来的数据
		String json = request.getParameter("data");
		//判断数据是一条还是多条
		boolean single = json.startsWith("{");
		if (single) {
			JSONObject jsonObject = JSONObject.fromObject(json);
			Object rdm = jsonObject.get("rdm");
			if(rdm!=null && !"".equals(rdm.toString())){
				CotOrderFacdetail newOrderFacdetail = orderfacMap.get(rdm.toString());				

				if(newOrderFacdetail!=null){
					//从订单明细id获取订单明细对象
			    	CotOrderDetail orderDetail = this.getOrderFacService().getOrderDetailById(newOrderFacdetail.getOrderDetailId());
			    	//从采购明细id获取修改前采购明细对象
			    	CotOrderFacdetail oldOrderFacdetail = this.getOrderFacService().getOrderFacDetailById(newOrderFacdetail.getId());
			    	
			    	if(orderDetail!=null){
			    		Long newBoxCount = newOrderFacdetail.getBoxCount();//new采购数量
			    		Long oldBoxCount = oldOrderFacdetail.getBoxCount();//old采购数量
			    		Long addBoxCount =  newBoxCount - oldBoxCount;//新增采购数量
			    		Long unBoxCount4OrderFac = orderDetail.getUnBoxCount4OrderFac() - addBoxCount;//未采购数量
			    		Long maxBoxCount = orderDetail.getBoxCount();//最大可采购数量
			    		Long realUnBoxCount = maxBoxCount - newBoxCount;//标准未采购数量
			    		
			    		if(unBoxCount4OrderFac > 0){
			    			if(unBoxCount4OrderFac > realUnBoxCount){
			    				if(realUnBoxCount>0){
			    					orderDetail.setUnBoxCount4OrderFac(realUnBoxCount);
			    					orderDetail.setUnBoxSend4OrderFac(0);
			    				}else{
			    					orderDetail.setUnBoxCount4OrderFac(new Long(0));
			    					orderDetail.setUnBoxSend4OrderFac(1);
			    				}
			    			}else{
			    				orderDetail.setUnBoxCount4OrderFac(unBoxCount4OrderFac);
			    				orderDetail.setUnBoxSend4OrderFac(0);
			    			}
			    		}else{
			    			orderDetail.setUnBoxCount4OrderFac(new Long(0));
			    			orderDetail.setUnBoxSend4OrderFac(1);
			    		}
			    		this.getOrderFacService().updateCotOrderDetail(orderDetail);
			    		
			    	} 
			    	//设置总CBM
					if(newOrderFacdetail.getContainerCount()==null || newOrderFacdetail.getBoxCbm()==null){
						newOrderFacdetail.setTotalCbm(0f);
					}else{
						newOrderFacdetail.setTotalCbm(newOrderFacdetail.getContainerCount()*newOrderFacdetail.getBoxCbm());
					}
					//设置总毛重
					if(newOrderFacdetail.getBoxCount()==null || newOrderFacdetail.getBoxGrossWeigth()==null){
						newOrderFacdetail.setTotalGrossWeigth(0f);
					}else{
						newOrderFacdetail.setTotalGrossWeigth(newOrderFacdetail.getBoxCount()*newOrderFacdetail.getBoxGrossWeigth());
					}
					//设置总净重
					if(newOrderFacdetail.getBoxCount()==null || newOrderFacdetail.getBoxNetWeigth()==null){
						newOrderFacdetail.setTotalNetWeigth(0f);
					}else{
						newOrderFacdetail.setTotalNetWeigth(newOrderFacdetail.getBoxCount()*newOrderFacdetail.getBoxNetWeigth());
					}
					newOrderFacdetail.setAddTime(new Date(System.currentTimeMillis()));//编辑时间
					newOrderFacdetail.setOutRemain(newOrderFacdetail.getBoxCount());
					
					//克隆对象,避免造成指针混用
			    	CotOrderFacdetail cloneObj = (CotOrderFacdetail)SystemUtil.deepClone(newOrderFacdetail);

					records.add(cloneObj);
				}
			}
		} else {
			JSONArray jarray = JSONArray.fromObject(json);
			for (int i = 0; i < jarray.size(); i++) {
				JSONObject jsonObject = jarray.getJSONObject(i);
				Object rdm = jsonObject.get("rdm");
				if(rdm!=null && !"".equals(rdm.toString())){
					CotOrderFacdetail newOrderFacdetail = orderfacMap.get(rdm.toString().toLowerCase());				
					if(newOrderFacdetail!=null){
						//从订单明细id获取订单明细对象
				    	CotOrderDetail orderDetail = this.getOrderFacService().getOrderDetailById(newOrderFacdetail.getOrderDetailId());
				    	//从采购明细id获取修改前采购明细对象
				    	CotOrderFacdetail oldOrderFacdetail = this.getOrderFacService().getOrderFacDetailById(newOrderFacdetail.getId());
				    	
				    	if(orderDetail!=null){
				    		Long newBoxCount = newOrderFacdetail.getBoxCount();//new采购数量
				    		Long oldBoxCount = oldOrderFacdetail.getBoxCount();//old采购数量
				    		Long addBoxCount =  newBoxCount - oldBoxCount;//新增采购数量
				    		Long unBoxCount4OrderFac = orderDetail.getUnBoxCount4OrderFac() - addBoxCount;//未采购数量
				    		Long maxBoxCount = orderDetail.getBoxCount();//最大可采购数量
				    		Long realUnBoxCount = maxBoxCount - newBoxCount;//标准未采购数量
				    		
				    		if(unBoxCount4OrderFac > 0){
				    			if(unBoxCount4OrderFac > realUnBoxCount){
				    				if(realUnBoxCount>0){
				    					orderDetail.setUnBoxCount4OrderFac(realUnBoxCount);
				    					orderDetail.setUnBoxSend4OrderFac(0);
				    				}else{
				    					orderDetail.setUnBoxCount4OrderFac(new Long(0));
				    					orderDetail.setUnBoxSend4OrderFac(1);
				    				}
				    			}else{
				    				orderDetail.setUnBoxCount4OrderFac(unBoxCount4OrderFac);
				    				orderDetail.setUnBoxSend4OrderFac(0);
				    			}
				    		}else{
				    			orderDetail.setUnBoxCount4OrderFac(new Long(0));
				    			orderDetail.setUnBoxSend4OrderFac(1);
				    		}
				    		this.getOrderFacService().updateCotOrderDetail(orderDetail);
				    		
				    	}
					}
				    //设置总CBM
					if(newOrderFacdetail.getContainerCount()==null || newOrderFacdetail.getBoxCbm()==null){
						newOrderFacdetail.setTotalCbm(0f);
					}else{
						newOrderFacdetail.setTotalCbm(newOrderFacdetail.getContainerCount()*newOrderFacdetail.getBoxCbm());
					}
					//设置总毛重
					if(newOrderFacdetail.getBoxCount()==null || newOrderFacdetail.getBoxGrossWeigth()==null){
						newOrderFacdetail.setTotalGrossWeigth(0f);
					}else{
						newOrderFacdetail.setTotalGrossWeigth(newOrderFacdetail.getBoxCount()*newOrderFacdetail.getBoxGrossWeigth());
					}
					//设置总净重
					if(newOrderFacdetail.getBoxCount()==null || newOrderFacdetail.getBoxNetWeigth()==null){
						newOrderFacdetail.setTotalNetWeigth(0f);
					}else{
						newOrderFacdetail.setTotalNetWeigth(newOrderFacdetail.getBoxCount()*newOrderFacdetail.getBoxNetWeigth());
					}
					newOrderFacdetail.setAddTime(new Date(System.currentTimeMillis()));//编辑时间
					newOrderFacdetail.setOutRemain(newOrderFacdetail.getBoxCount());
						
					//克隆对象,避免造成指针混用
				    CotOrderFacdetail cloneObj = (CotOrderFacdetail)SystemUtil.deepClone(newOrderFacdetail);

				    records.add(cloneObj);
				}
			}
		}

		// 保存
		this.getOrderFacService().modifyOrderFacDetails(records);
		
		//修改主单的总数量,总箱数,总体积,总金额
		this.getOrderFacService().modifyCotOrderFacTotal(Integer.parseInt(orderId));
		
//		JSONObject suc = new JSONObject();
//		suc.put("success", true);
//		suc.put("msg", "成功");
//		try {
//			response.getWriter().write(suc.toString());
//		} catch (IOException e) {
//			e.printStackTrace();
//			JSONObject error = new JSONObject();
//			error.put("success", false);
//			error.put("msg", "失败");
//		}
		return null;
	}
	
    //修改明细(订单分解页面)
	public ActionForward modifyOrderFacDetail(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		
//		String orderId = request.getParameter("orderId");
//		//获取有修改货号的id
//		String[] recordKeys = request.getParameterValues(TableConstants.RECORDKEY_NAME);
//		
//		//获取有修改货号的订单明细id
//		String[] orderDetailIdAry = request.getParameterValues("orderDetailId");
//		
//		//获取有修改货号的采购明细id
//		String[] rdmAry = request.getParameterValues("rdm");
//		
//		//获取有修改货号的eleId
//		//String[] modEleIdAry = request.getParameterValues("eleId");
//		
//		List<CotOrderFacdetail> records = new ArrayList<CotOrderFacdetail>();
//		
//		HttpSession session = request.getSession();
//		//Action获取Map
//		HashMap<String, CotOrderFacdetail> orderFacMap = this.getOrderFacService().getMapByRdmAction(session);
//		
//		if(orderFacMap!=null){
//			for (int i = 0; i < rdmAry.length; i++) {
//				//从orderFacMap获取修改后采购明细对象
//				CotOrderFacdetail newOrderFacdetail = orderFacMap.get(rdmAry[i]);
//				if(newOrderFacdetail!=null){
//					//从订单明细id获取订单明细对象
//			    	CotOrderDetail orderDetail = this.getOrderFacService().getOrderDetailById(Integer.parseInt(orderDetailIdAry[i]));
//			    	//从采购明细id获取修改前采购明细对象
//			    	CotOrderFacdetail oldOrderFacdetail = this.getOrderFacService().getOrderFacDetailById(Integer.parseInt(recordKeys[i]));
//			    	
//			    	if(orderDetail!=null){
//			    		Long newBoxCount = newOrderFacdetail.getBoxCount();//new采购数量
//			    		Long oldBoxCount = oldOrderFacdetail.getBoxCount();//old采购数量
//			    		Long addBoxCount =  newBoxCount - oldBoxCount;//新增采购数量
//			    		Long unBoxCount4OrderFac = orderDetail.getUnBoxCount4OrderFac() - addBoxCount;//未采购数量
//			    		Long maxBoxCount = orderDetail.getBoxCount();//最大可采购数量
//			    		Long realUnBoxCount = maxBoxCount - newBoxCount;//标准未采购数量
//			    		
//			    		if(unBoxCount4OrderFac > 0){
//			    			if(unBoxCount4OrderFac > realUnBoxCount){
//			    				if(realUnBoxCount>0){
//			    					orderDetail.setUnBoxCount4OrderFac(realUnBoxCount);
//			    					orderDetail.setUnBoxSend4OrderFac(0);
//			    				}else{
//			    					orderDetail.setUnBoxCount4OrderFac(new Long(0));
//			    					orderDetail.setUnBoxSend4OrderFac(1);
//			    				}
//			    			}else{
//			    				orderDetail.setUnBoxCount4OrderFac(unBoxCount4OrderFac);
//			    				orderDetail.setUnBoxSend4OrderFac(0);
//			    			}
//			    		}else{
//			    			orderDetail.setUnBoxCount4OrderFac(new Long(0));
//			    			orderDetail.setUnBoxSend4OrderFac(1);
//			    		}
//			    		this.getOrderFacService().updateCotOrderDetail(orderDetail);
//			    		
//			    	} 
//			    	//计算箱数
//			    	if (newOrderFacdetail.getBoxObCount() != null && newOrderFacdetail.getBoxObCount() != 0) {
//						if (newOrderFacdetail.getBoxCount() % newOrderFacdetail.getBoxObCount() != 0) {
//							newOrderFacdetail.setContainerCount(newOrderFacdetail.getBoxCount()/newOrderFacdetail.getBoxObCount() +1);
//						}else {
//							newOrderFacdetail.setContainerCount(newOrderFacdetail.getBoxCount()/newOrderFacdetail.getBoxObCount());
//						}
//					}
//			    	//计算总厂价
//			    	if (newOrderFacdetail.getPriceFac() != null) {
//						newOrderFacdetail.setTotalFac(newOrderFacdetail.getBoxCount()*newOrderFacdetail.getPriceFac());
//					}
//			    	//计算总外销价
//			    	if (newOrderFacdetail.getOrderPrice() != null) {
//						newOrderFacdetail.setTotalMoney(newOrderFacdetail.getBoxCount()*newOrderFacdetail.getOrderPrice());
//					}
//			    	//设置总CBM
//					if(newOrderFacdetail.getContainerCount()==null || newOrderFacdetail.getBoxCbm()==null){
//						newOrderFacdetail.setTotalCbm(0f);
//					}else{
//						newOrderFacdetail.setTotalCbm(newOrderFacdetail.getContainerCount()*newOrderFacdetail.getBoxCbm());
//					}
//					//设置总毛重
//					if(newOrderFacdetail.getBoxCount()==null || newOrderFacdetail.getBoxGrossWeigth()==null){
//						newOrderFacdetail.setTotalGrossWeigth(0f);
//					}else{
//						newOrderFacdetail.setTotalGrossWeigth(newOrderFacdetail.getBoxCount()*newOrderFacdetail.getBoxGrossWeigth());
//					}
//					//设置总净重
//					if(newOrderFacdetail.getBoxCount()==null || newOrderFacdetail.getBoxNetWeigth()==null){
//						newOrderFacdetail.setTotalNetWeigth(0f);
//					}else{
//						newOrderFacdetail.setTotalNetWeigth(newOrderFacdetail.getBoxCount()*newOrderFacdetail.getBoxNetWeigth());
//					}
//					newOrderFacdetail.setAddTime(new Date(System.currentTimeMillis()));//编辑时间
//					newOrderFacdetail.setOutRemain(newOrderFacdetail.getBoxCount());
//					
//					//克隆对象,避免造成指针混用
//			    	CotOrderFacdetail cloneObj = (CotOrderFacdetail)SystemUtil.deepClone(newOrderFacdetail);
//			    	//Integer detailId = cotOrderFacdetail.getId();
//			    	//byte[] picImg =  this.getOrderFacService().getPicImgByDetailId(detailId);
//			    	//cloneObj.setPicImg(picImg);
//					records.add(cloneObj);
//				}
//			}
//		}	
//		// 更新数据
//		int[] resultCodes = new int[records.size()];
//		for (int i = 0; i < resultCodes.length; i++) {
//			resultCodes[i] = 1;
//		}
//		String[] messages = null;
//
//		this.getOrderFacService().modifyOrderFacDetails(records);
//		//修改主单的总数量,总箱数,总体积,总金额
//		this.getOrderFacService().modifyCotOrderFacTotal(Integer.parseInt(orderId));
//
//		try {
//			super.defaultAjaxResopnse("orderToOrderFac", recordKeys, resultCodes,
//					messages, request, response);
//		} catch (ServletException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
		return null;
	}
	
	public ActionForward removeDetail(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String orderId = request.getParameter("orderId");
		
		String json = request.getParameter("data");
		List ids = null;
		//判断数据是一条还是多条
		boolean single = json.startsWith("[");
		if(!single){
			ids = new ArrayList();
			ids.add(new Integer(json));
		} else {
			JSONArray jarray = JSONArray.fromObject(json);
			ids = jarray.toList(jarray, ArrayList.class);
		}
		
		for (int i = 0; i < ids.size(); i++) {
			CotOrderFacdetail cotOrderFacdetail = this.getOrderFacService().getOrderFacDetailById((Integer)ids.get(i));
			
			//从订单明细id获取订单明细对象
	    	CotOrderDetail orderDetail = this.getOrderFacService().getOrderDetailById(cotOrderFacdetail.getOrderDetailId());
			if(orderDetail!=null){
	    		Long boxCount = cotOrderFacdetail.getBoxCount();//还原采购数量
	    		if(boxCount!=0){
	    			Long maxBoxCount = orderDetail.getBoxCount();//最大可采购数量
		    		Long oldUnBoxCount = orderDetail.getUnBoxCount4OrderFac();//old未采购数量
		    		Long newUnBoxCount = oldUnBoxCount + boxCount;//new未采购数量
		    		if(newUnBoxCount > maxBoxCount){
		    			orderDetail.setUnBoxCount4OrderFac(maxBoxCount);
		    		}else{
		    			orderDetail.setUnBoxCount4OrderFac(newUnBoxCount);
		    		}
		    		orderDetail.setUnBoxSend4OrderFac(0);

		    		this.getOrderFacService().updateCotOrderDetail(orderDetail);
	    		}
	    	} 
		}
		this.getOrderFacService().deleteDetailByIds(ids);
		
		//修改主单的总数量,总箱数,总体积,总金额
		this.getOrderFacService().modifyCotOrderFacTotal(Integer.parseInt(orderId));
		JSONObject suc = new JSONObject();
		suc.put("success", true);
		suc.put("msg", "成功");
		try {
			response.getWriter().write(suc.toString());
		} catch (IOException e) {
			e.printStackTrace();
			JSONObject error = new JSONObject();
			error.put("success", false);
			error.put("msg", "失败");
		}
		return null;	
	}
	
	
	public ActionForward openDetail(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		 
		return mapping.findForward("openDetail");
	}
	
	// 跳转到上传唛标页面
	public ActionForward showUpload(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		return mapping.findForward("showUpload");
	}
	
	@Override
	public ActionForward add(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		// 获得登录人
		CotEmps emp = (CotEmps) request.getSession().getAttribute("emp");
		request.setAttribute("emp", emp);
		//默认公司id
		request.setAttribute("defaultCompanyId", this.getOrderFacService().getDefaultCompanyId());
		//request.setAttribute("allFactoryName", this.getOrderFacService().getFactoryNameMap());
		return mapping.findForward("add");
	}
	
	public ActionForward assign(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		// 获得登录人
		CotEmps emp = (CotEmps) request.getSession().getAttribute("emp");
		request.setAttribute("emp", emp);
		//默认公司id
		request.setAttribute("defaultCompanyId", this.getOrderFacService().getDefaultCompanyId());
		//request.setAttribute("allFactoryName", this.getOrderFacService().getFactoryNameMap());
		return mapping.findForward("assign");
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
	
	// 查询明细产品的样品信息
	public ActionForward queryEleFrame(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		
		return mapping.findForward("queryEleFrame");
	}
	
	
	public ActionForward queryFinanceOther(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		
		String start = request.getParameter("start");
		String limit = request.getParameter("limit");	
		if(start == null || limit == null)
			return mapping.findForward("queryFinanceOther");
		
		String fkId = request.getParameter("fkId");// 外键
		String type = request.getParameter("type");// 类型 0:应收 1：应付
		
		StringBuffer queryString = new StringBuffer();
		queryString.append(" where 1=1 and obj.source ='orderfac'");

		if (fkId != null && !fkId.trim().equals("")) {
			queryString.append(" and obj.fkId=" + fkId.trim());
		}
		
		if (type != null && !type.trim().equals("")) {
			queryString.append(" and obj.type=" + type.trim());
		}
		
		// 设置查询参数
		QueryInfo queryInfo = new QueryInfo();
		// 设置每页显示多少行
		int pageCount  = Integer.parseInt(limit);
		// 设定每页显示记录数
		queryInfo.setCountOnEachPage(pageCount);
		// 设置查询记录总数语句
		queryInfo.setCountQuery("select count(*) from CotFinaceOther as obj"
				+ queryString);
		// 查询语句
		queryInfo.setSelectString("from CotFinaceOther AS obj");
		// 设置条件语句
		queryInfo.setQueryString(queryString.toString());
		// 设置排序语句
		queryInfo.setOrderString(" order by obj.id desc");
		int startIndex = Integer.parseInt(start);	
		queryInfo.setStartIndex(startIndex);
		try {
			String json = this.getOrderFacService().getJsonData(queryInfo);
			response.getWriter().write(json);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	// 保存
	public ActionForward addOther(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
		
		Integer empId = (Integer) request.getSession().getAttribute("empId");
//		String orderNo = request.getParameter("mainNo");// 订单单号
		String orderNo = new String(request.getParameter("mainNo").getBytes("ISO-8859-1"),"utf-8");
		String fkId = request.getParameter("mainId");// 订单主单编号
		String facId = request.getParameter("facId");//厂家id
		
		List<CotFinaceOther> records = new ArrayList<CotFinaceOther>();
		
		//前台传递来的数据
		String json = request.getParameter("data");
		//判断数据是一条还是多条
		boolean single = json.startsWith("{");
		if (single) {
			JSONObject jsonObject = JSONObject.fromObject(json);
			String name = jsonObject.getString("finaceName");
			if(!"".equals(name)){
				Object finaceName = jsonObject.get("finaceName");
				Object flag = jsonObject.get("flag");
				Object amount = jsonObject.get("amount");
				Object currencyId = jsonObject.get("currencyId");
				
				CotFinaceOther finaceOther = new CotFinaceOther();
				
				finaceOther.setFinaceName((String) finaceName);
				finaceOther.setFlag((String) flag);
				finaceOther.setAmount(Double.parseDouble(amount.toString()));
				finaceOther.setCurrencyId(new Integer(currencyId.toString()));
				finaceOther.setRemainAmount(Double.parseDouble(amount.toString()));
				
				finaceOther.setType(1);// 类型 0:应收 1：应付
				finaceOther.setSource("orderfac");
				finaceOther.setOrderNo(orderNo);
				finaceOther.setBusinessPerson(empId);
				finaceOther.setStatus(0);
				finaceOther.setFkId(Integer.parseInt(fkId));
				finaceOther.setFactoryId(Integer.parseInt(facId));
				
				records.add(finaceOther);
			}
			
		} else {
			JSONArray jarray = JSONArray.fromObject(json);
			for (int i = 0; i < jarray.size(); i++) {
				
				JSONObject jsonObject = JSONObject.fromObject(jarray.get(i));
				String name = jsonObject.getString("finaceName");
				if(!"".equals(name)){
					Object finaceName = jsonObject.get("finaceName");
					Object flag = jsonObject.get("flag");
					Object amount = jsonObject.get("amount");
					Object currencyId = jsonObject.get("currencyId");
					
					CotFinaceOther finaceOther = new CotFinaceOther();
					
					finaceOther.setFinaceName((String) finaceName);
					finaceOther.setFlag((String) flag);
					finaceOther.setAmount(Double.parseDouble(amount.toString()));
					finaceOther.setCurrencyId(Integer.parseInt(currencyId.toString()) );
					finaceOther.setRemainAmount(Double.parseDouble(amount.toString()));
					
					finaceOther.setType(1);// 类型 0:应收 1：应付
					finaceOther.setSource("orderfac");
					finaceOther.setOrderNo(orderNo);
					finaceOther.setBusinessPerson(empId);
					finaceOther.setStatus(0);
					finaceOther.setFkId(Integer.parseInt(fkId));
					finaceOther.setFactoryId(Integer.parseInt(facId));
					
					records.add(finaceOther);
				}
			}
		}

		// 保存
		this.getOrderFacService().addOtherList(records);
	
//		JSONObject suc = new JSONObject();
//		suc.put("success", true);
//		suc.put("msg", "成功");
//		try {
//			response.getWriter().write(suc.toString());
//		} catch (IOException e) {
//			e.printStackTrace();
//			JSONObject error = new JSONObject();
//			error.put("success", false);
//			error.put("msg", "失败");
//		}
		return null;
	}
	//修改
	public ActionForward modifyOther(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {

		Integer empId = (Integer) request.getSession().getAttribute("empId");
		String orderNo = request.getParameter("orderNo");// 单号

		// 前台传递来的数据
		String json = request.getParameter("data");
		// 判断数据是一条还是多条
		boolean single = json.startsWith("{");
		// 1.使用反射获取对象的所有属性名称
		String[] properties = ReflectionUtils
				.getDeclaredFields(CotFinaceOther.class);
		List<CotFinaceOther> records = new ArrayList<CotFinaceOther>();
		try {
			if (single) {
				JSONObject jsonObject = JSONObject.fromObject(json);
				CotFinaceOther finaceOther = this.getOrderFacService()
						.getFinaceOtherById(
								Integer.parseInt(jsonObject.getString("id")));
				for (int i = 0; i < properties.length; i++) {
					if (jsonObject.get(properties[i]) != null) {
						BeanUtils.setProperty(finaceOther, properties[i],
								jsonObject.get(properties[i]));
					}
				}
				finaceOther.setOrderNo(orderNo);
				finaceOther.setBusinessPerson(empId);
				records.add(finaceOther);
			} else {
				JSONArray jarray = JSONArray.fromObject(json);
				for (int i = 0; i < jarray.size(); i++) {
					JSONObject jsonObject = (JSONObject) jarray.get(i);
					CotFinaceOther finaceOther = this.getOrderFacService()
							.getFinaceOtherById(
									Integer
											.parseInt(jsonObject
													.getString("id")));
					for (int j = 0; j < properties.length; j++) {
						if (jsonObject.get(properties[j]) != null) {
							BeanUtils.setProperty(finaceOther, properties[j],
									jsonObject.get(properties[j]));
						}
					}
					finaceOther.setOrderNo(orderNo);
					finaceOther.setBusinessPerson(empId);
					records.add(finaceOther);
				}
			}
			this.getOrderFacService().updateOtherList(records);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	//删除
	public ActionForward removeOther(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		
		String json = request.getParameter("data");
		List ids = null;
		//判断数据是一条还是多条
		boolean single = json.startsWith("[");
		if(!single){
			ids = new ArrayList();
			ids.add(new Integer(json));
		} else {
			JSONArray jarray = JSONArray.fromObject(json);
			ids = jarray.toList(jarray, ArrayList.class);
		}
		
		this.getOrderFacService().deleteOtherList(ids);
//		JSONObject suc = new JSONObject();
//		suc.put("success", true);
//		suc.put("msg", "成功");
//		try {
//			response.getWriter().write(suc.toString());
//		} catch (IOException e) {
//			e.printStackTrace();
//			JSONObject error = new JSONObject();
//			error.put("success", false);
//			error.put("msg", "失败");
//		}
		return null;
	}
	
	//查询应付帐款
	public ActionForward queryDeal(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		 
		String start = request.getParameter("start");
		String limit = request.getParameter("limit");	
		if(start == null || limit == null)
			return mapping.findForward("queryDeal");
		
		String orderNo = request.getParameter("orderNo");// 单号
		String fkId = request.getParameter("fkId");// 外键
		String source = request.getParameter("source");// 金额源来
		String factoryId = request.getParameter("factoryId");// 客户
		
		StringBuffer queryString=new StringBuffer();
		queryString.append(" where 1=1");
		
		
		if (orderNo != null && !orderNo.trim().equals("")) {
			queryString.append(" and obj.orderNo='" + orderNo.trim()+"'");
		}
		if (fkId != null && !fkId.trim().equals("")) {
			queryString.append(" and obj.fkId=" + fkId.trim());
		}
		if (source != null && !source.trim().equals("")) {
			queryString.append(" and obj.source='" + source.trim()+"'");
		}
		if (factoryId != null && !factoryId.trim().equals("")) {
			queryString.append(" and obj.factoryId=" + factoryId.trim());
		}
		
		// 设置查询参数
		QueryInfo queryInfo = new QueryInfo();
		// 设置每页显示多少行
		int pageCount = Integer.parseInt(limit);
		// 设定每页显示记录数
		queryInfo.setCountOnEachPage(pageCount);
		//设置查询记录总数语句
		queryInfo.setCountQuery("select count(*) from CotFinaceAccountdeal obj"+queryString.toString());
		//设置查询记录语句
		queryInfo.setSelectString("select obj from CotFinaceAccountdeal obj");
		 
		//设置查询条件语句
		queryInfo.setQueryString(queryString.toString());
		//设置排序语句
		queryInfo.setOrderString(" order by obj.id desc");
		int startIndex = Integer.parseInt(start);	
		queryInfo.setStartIndex(startIndex);
		try {
			String json = this.getOrderFacService().getJsonData(queryInfo);
			response.getWriter().write(json);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}	
	
	//删除
	public ActionForward removeDeal(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
			
		String json = request.getParameter("data");
		List ids = null;
		//判断数据是一条还是多条
		boolean single = json.startsWith("[");
		if(!single){
			ids = new ArrayList();
			ids.add(new Integer(json));
		} else {
			JSONArray jarray = JSONArray.fromObject(json);
			ids = jarray.toList(jarray, ArrayList.class);
		}
		
		for (int j = 0; j < ids.size(); j++) {
			Integer id = (Integer) ids.get(j);
			CotFinaceAccountdeal finaceAccountdeal = this.getOrderFacService()
										.getGivenDealById(id);
			//修改其他费用生成状态 0：未生成 1：已生成
			this.getOrderFacService().modifyFinOtherStatus(finaceAccountdeal.getFkId(), 
											finaceAccountdeal.getFinaceName(), "remove");
		}
		
		this.getOrderFacService().deleteDealList(ids);
		JSONObject suc = new JSONObject();
		suc.put("success", true);
		suc.put("msg", "成功");
		try {
			response.getWriter().write(suc.toString());
		} catch (IOException e) {
			e.printStackTrace();
			JSONObject error = new JSONObject();
			error.put("success", false);
			error.put("msg", "失败");
		}
		return null;
	}
	
	//查询应付帐款
	public ActionForward queryDealDetail(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		 
		String start = request.getParameter("start");
		String limit = request.getParameter("limit");	
		if(start == null || limit == null)
			return mapping.findForward("queryDealDetail");
		
		String dealId = request.getParameter("dealId");//应付帐款id
		
		StringBuffer queryString=new StringBuffer();
		
		//最高权限
		boolean all = SystemUtil.isAction(request, "cotdeal.do", "ALL");
		//部门权限
		boolean dept = SystemUtil.isAction(request, "cotdeal.do","DEPT");		
		
		// 获得登录人
		CotEmps emp = (CotEmps) request.getSession().getAttribute("emp");
		if ("admin".equals(emp.getEmpsId())) {
			queryString.append(" where obj.businessPerson=e.id");
		} else {
			// 判断是否有最高权限
			if (all == true) {
				queryString.append(" where obj.businessPerson=e.id");
			} else {
				// 判断是否有查看该部门权限
				if (dept == true) {
					queryString .append(" where obj.businessPerson = e.id and e.deptId=" + emp.getDeptId());
				} else {
					queryString .append(" where obj.businessPerson = e.id and obj.businessPerson =" + emp.getId());
				}
			}
		}
		//queryString.append(" and obj.status = 0");
		
		if (dealId != null && !dealId.trim().equals("")) {
			queryString.append(" and obj.dealId=" + dealId.trim());
		}else {
			return null;
		}
		
		// 设置查询参数
		QueryInfo queryInfo = new QueryInfo();
		// 设置每页显示多少行
		int pageCount = 15;
		// 取得页面选择的每页显示条数
		pageCount = Integer.parseInt(limit);
		// 设定每页显示记录数
		queryInfo.setCountOnEachPage(pageCount);
		//设置查询记录总数语句
		queryInfo.setCountQuery("select count(*) from CotFinacegivenDetail obj,CotEmps e"+queryString.toString());
		//设置查询记录语句
		queryInfo.setSelectString("select obj from CotFinacegivenDetail obj,CotEmps e");
		 
		//设置查询条件语句
		queryInfo.setQueryString(queryString.toString());
		//设置排序语句
		queryInfo.setOrderString(" order by obj.id asc");
		
		int startIndex = Integer.parseInt(start);	
		queryInfo.setStartIndex(startIndex);
		try {
			String json = this.getOrderFacService().getJsonData(queryInfo);
			response.getWriter().write(json);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}	
	
	//从生产合同添加提醒消息
	public ActionForward addToMessage(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		return mapping.findForward("addToMessage");
	}
	
	public ActionForward queryOrderFac(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		
		String start = request.getParameter("start");
		String limit = request.getParameter("limit");	
		if(start == null || limit == null)
			return mapping.findForward("queryOrderFac");
		String ids = request.getParameter("facIds"); //主单id

		StringBuffer queryString = new StringBuffer();
		
		queryString.append(" where 1=1 ");
		 
		if (ids != null && !ids.trim().equals("")) {
			queryString.append(" and obj.id in("+ids.trim()+")");
		}else{
			return null;
		}

		// 设置查询参数
		QueryInfo queryInfo = new QueryInfo();
		// 设置每页显示多少行
		int pageCount = 15;
		// 取得页面选择的每页显示条数
		pageCount = Integer.parseInt(limit);
		// 设定每页显示记录数
		queryInfo.setCountOnEachPage(pageCount);
		// 设置查询记录总数语句
		queryInfo.setCountQuery("select count(*) from CotOrderFac obj" + queryString);
		 
		// 查询语句
		queryInfo.setSelectString("select obj from CotOrderFac obj");
		// 设置条件语句
		queryInfo.setQueryString(queryString.toString());
		// 设置排序语句
		queryInfo.setOrderString(" order by obj.id desc");
		int startIndex = Integer.parseInt(start);
		queryInfo.setStartIndex(startIndex);
		// 根据起始
		String excludes[] = {"orderMBImg","cotOrderFacdetails"};
		queryInfo.setExcludes(excludes);
		try {
			
			String json = this.getOrderFacService().getJsonData(queryInfo);
			response.getWriter().write(json);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	// 查询计划日期数据
	public ActionForward queryTime(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {

		String start = request.getParameter("start");
		String limit = request.getParameter("limit");

		if (start == null || limit == null)
			return mapping.findForward("queryTime");

		String orderId = request.getParameter("orderId");// 外键

		StringBuffer queryString = new StringBuffer();
		queryString.append(" where 1=1");

		if (orderId != null && !orderId.trim().equals("")) {
			queryString.append(" and obj.orderFacId=" + orderId.trim());
		}

		// 设置查询参数
		QueryInfo queryInfo = new QueryInfo();
		int startIndex = Integer.parseInt(start);
		queryInfo.setStartIndex(startIndex);
		// 设置每页显示多少行
		Integer pageCount = Integer.parseInt(limit);
		// 设定每页显示记录数
		queryInfo.setCountOnEachPage(pageCount);

		// 设置查询记录总数语句
		queryInfo.setCountQuery("select count(*) from CotOrderStatus as obj" + queryString);
		// 查询语句
		queryInfo.setSelectString("from CotOrderStatus AS obj");
		// 设置条件语句
		queryInfo.setQueryString(queryString.toString());
		// 设置排序语句
		queryInfo.setOrderString(" order by obj.id desc");

		try {
			String json = this.getOrderFacService().getJsonData(queryInfo);
			response.getWriter().write(json);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	

	// 修改计划日期
	public ActionForward modifyTime(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		List<CotOrderStatus> records = new ArrayList<CotOrderStatus>();
		// 前台传递来的数据
		String json = request.getParameter("data");
		String[] properties = ReflectionUtils.getDeclaredFields(CotOrderStatus.class);
		// 判断数据是一条还是多条
		boolean single = json.startsWith("{");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		try {
			if (single) {
				JSONObject jsonObject = JSONObject.fromObject(json);
				CotOrderStatus orderStatus = this.getOrderFacService().getOrderStatusById(Integer.parseInt(jsonObject.getString("id")));
				
				for (int i = 0; i < properties.length; i++) {
					if (properties[i].equals("planDate")) {
						String planDate = jsonObject.get(properties[i]).toString();
						if(planDate != null && !"".equals(planDate))
							orderStatus.setComleteDate(sdf.parse(planDate));
					} else if (properties[i].equals("comleteDate")) {
						String planDate = jsonObject.get(properties[i]).toString();
						if(planDate != null && !"".equals(planDate))
							orderStatus.setComleteDate(sdf.parse(planDate));
					} else {
						BeanUtils.setProperty(orderStatus, properties[i], jsonObject.get(properties[i]));
					}
				}
				orderStatus.setOrderType("ORDERFAC");
				records.add(orderStatus);

			} else {
				JSONArray jarray = JSONArray.fromObject(json);
				for (int i = 0; i < jarray.size(); i++) {
					JSONObject jsonObject = jarray.getJSONObject(i);
					CotOrderStatus orderStatus = this.getOrderFacService().getOrderStatusById(Integer.parseInt(jsonObject.getString("id")));
					
					for (int j = 0; j < properties.length; j++) {
						if (properties[j].equals("planDate")) {
							String planDate = jsonObject.get(properties[j]).toString();
							if(planDate != null && !"".equals(planDate))
								orderStatus.setComleteDate(sdf.parse(planDate));
						} else if (properties[j].equals("comleteDate")) {
							String planDate = jsonObject.get(properties[j]).toString();
							if(planDate != null && !"".equals(planDate))
								orderStatus.setComleteDate(sdf.parse(planDate));
						} else {
							BeanUtils.setProperty(orderStatus, properties[j], jsonObject.get(properties[j]));
						}
						
					}
					orderStatus.setOrderType("ORDERFAC");
					records.add(orderStatus);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		// 保存
		this.getOrderFacService().updateOtherList(records);
		return null;
	}
	
	// 查看客户图片信息
	public ActionForward queryOrderPc(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {

		String start = request.getParameter("start");
		String limit = request.getParameter("limit");
		
		if(start == null || limit == null)
			return mapping.findForward("queryOrderPc");
		
		String categoryId = request.getParameter("categoryIdFind");
		String orderDetailId = request.getParameter("orderDetailIdFind");
		StringBuffer queryString = new StringBuffer();
		queryString.append(" where obj.orderDetailId=d.id");
		
		String orderId = request.getParameter("orderId");

		if (orderId != null && !"".equals(orderId.trim())) {
			queryString.append(" and obj.orderId =" + orderId.trim());
		}else {
			return null;
		}
		
		if (categoryId != null && !"".equals(categoryId.trim())) {
			queryString.append(" and obj.categoryId =" + categoryId.trim());
		}
		
		if (orderDetailId != null && !"".equals(orderDetailId.trim())) {
			queryString.append(" and obj.orderDetailId =" + orderDetailId.trim());
		}
		
		QueryInfo queryInfo = new QueryInfo();
		// 设置每页显示多少行
		Integer pageCount = 15;
		pageCount = Integer.parseInt(limit);
		// 设定每页显示记录数
		queryInfo.setCountOnEachPage(pageCount);

		queryInfo.setCountQuery("select count(*) from CotOrderPc obj,CotOrderDetail d"+ queryString.toString());
		queryInfo.setSelectString("select new CotOrderPc(obj,d.barcode) from CotOrderPc obj,CotOrderDetail d");
		queryInfo.setQueryString(queryString.toString());
		queryInfo.setOrderString(" order by obj.id desc");
		
		int startIndex = Integer.parseInt(start);	
		queryInfo.setStartIndex(startIndex);
		try {
			String[] excludes={"phone"};
			queryInfo.setExcludes(excludes);
			String json = this.getOrderFacService().getJsonData(queryInfo);
			response.getWriter().write(json);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
		
	}
	
	// 进入客户图片编辑页面
	public ActionForward orderPcEdit(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {

		return mapping.findForward("orderPcEdit");
	}
	
	public ActionForward modifyArtWork(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		
		String json = request.getParameter("data");
		List list = null;
		//判断数据是一条还是多条
		boolean single = json.startsWith("{");
		if(single){
			JSONObject jsonObject = JSONObject.fromObject(json); 
			CotArtWork obj  = (CotArtWork)jsonObject.toBean(jsonObject, CotArtWork.class);
			list = new ArrayList();
			list.add(obj);
		}else{
			JSONArray jarray = JSONArray.fromObject(json);
			list = jarray.toList(jarray, CotArtWork.class);
		}
		//保存
		this.getOrderFacService().updateList(list);
		return null;
	}
	
	public ActionForward queryArtWork(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		
		String start = request.getParameter("start");
		String limit = request.getParameter("limit");
		
		if(start == null || limit == null)
			return mapping.findForward("queryArtWork");
		
		String orderId = request.getParameter("orderId");
		StringBuffer queryString = new StringBuffer();
		//设置查询参数
		queryString
		.append(" where obj.eleId=d.id");
		if (orderId != null && !orderId.trim().equals("")) {
			queryString.append(" and obj.orderId=" + orderId.trim());
		}
		QueryInfo queryInfo = new QueryInfo();
		//设置每页显示多少行
		int pageCount = 15;
		// 取得页面选择的每页显示条数
		pageCount = Integer.parseInt(limit);
		//设定每页显示记录数
		queryInfo.setCountOnEachPage(pageCount);
		//设置查询记录总数语句
		queryInfo.setCountQuery("select count(*) from CotArtWork obj,CotOrderFacdetail d"+ queryString);
		//查询语句
		queryInfo.setSelectString("select new CotArtWork(obj,d.eleNameEn,d.barcode,d.custNo) from CotArtWork obj,CotOrderFacdetail d");
		//设置条件语句
		queryInfo.setQueryString(queryString.toString());
		//设置排序语句
		queryInfo.setOrderString(" order by d.sortNo asc,d.id asc");
		//得到limit对象
		
		int startIndex = Integer.parseInt(start);
		//设置总行数和每页显示多少条
		queryInfo.setStartIndex(startIndex);
		//根据起始
		try {
			String json = this.getOrderFacService().getJsonData(queryInfo);
			response.getWriter().write(json);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
}
