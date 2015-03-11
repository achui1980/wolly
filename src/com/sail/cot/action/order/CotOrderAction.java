/**
 * 
 */
package com.sail.cot.action.order;

import java.io.IOException;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
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
import com.sail.cot.domain.CotElePic;
import com.sail.cot.domain.CotEmps;
import com.sail.cot.domain.CotFinaceOther;
import com.sail.cot.domain.CotGivenPic;
import com.sail.cot.domain.CotOrderDetail;
import com.sail.cot.domain.CotOrderEleprice;
import com.sail.cot.domain.CotOrderFittings;
import com.sail.cot.domain.CotOrderPic;
import com.sail.cot.domain.CotOrderStatus;
import com.sail.cot.domain.CotPanPic;
import com.sail.cot.domain.CotPricePic;
import com.sail.cot.domain.CotTypeLv1;
import com.sail.cot.query.QueryInfo;
import com.sail.cot.service.img.CotOpImgService;
import com.sail.cot.service.order.CotOrderService;
import com.sail.cot.service.sample.CotElementsService;
import com.sail.cot.util.GridServerHandler;
import com.sail.cot.util.ReflectionUtils;
import com.sail.cot.util.SystemUtil;

public class CotOrderAction extends AbstractAction {

	private CotOrderService orderService;
	private CotElementsService cotElementsService;

	@SuppressWarnings("deprecation")
	@Override
	// 添加订单明细
	public ActionForward add(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		// 内存数据
		HttpSession session = request.getSession();
		Map<String, CotOrderDetail> orderMap = this.getOrderService().getMapAction(session);

		List<CotOrderDetail> records = new ArrayList<CotOrderDetail>();
		List<CotOrderPic> imgList = new ArrayList<CotOrderPic>();
		CotOpImgService impOpService = (CotOpImgService) SystemUtil.getService("CotOpImgService");
		byte[] zwtpByte = this.getCotElementsService().getZwtpPic();
		String orderId = request.getParameter("orderPrimId");
		String currencyId = request.getParameter("currencyId");
		String customerId = request.getParameter("custId");
		Integer custId = Integer.parseInt(customerId);

		// 前台传递来的数据
		String json = request.getParameter("data");
		// 判断数据是一条还是多条
		boolean single = json.startsWith("{");
		if (single) {
			JSONObject jsonObject = getJsonObject(json);
			Object rdm = jsonObject.get("rdm");
			if (rdm != null && !"".equals(rdm.toString())) {
				CotOrderDetail cotOrderDetail = orderMap.get(rdm.toString());
				// 设置序列号
				Integer sortNo = (Integer) jsonObject.get("sortNo");
				cotOrderDetail.setSortNo(sortNo);
				// 设置是否锁定价格
				Object checkFlag = jsonObject.get("checkFlag");
				if (checkFlag == null || checkFlag.toString().equals("null")) {
					cotOrderDetail.setCheckFlag(0);
				} else {
					if (checkFlag instanceof Integer) {
						cotOrderDetail.setCheckFlag((Integer) checkFlag);
					}
					if (checkFlag instanceof Boolean) {
						if ((Boolean) checkFlag == false) {
							cotOrderDetail.setCheckFlag(0);
						} else {
							cotOrderDetail.setCheckFlag(1);
						}
					}
				}
				records.add(cotOrderDetail);
			}
		} else {
			JSONArray jarray = JSONArray.fromObject(json);
			for (int i = 0; i < jarray.size(); i++) {
				JSONObject jsonObject = jarray.getJSONObject(i);
				Object rdm = jsonObject.get("rdm");
				if (rdm != null && !"".equals(rdm.toString())) {
					CotOrderDetail cotOrderDetail = orderMap.get(rdm.toString());
					// 设置序列号
					Integer sortNo = (Integer) jsonObject.get("sortNo");
					cotOrderDetail.setSortNo(sortNo);
					// 设置是否锁定价格
					Object checkFlag = jsonObject.get("checkFlag");
					if (checkFlag == null || checkFlag.toString().equals("null")) {
						cotOrderDetail.setCheckFlag(0);
					} else {
						if (checkFlag instanceof Integer) {
							cotOrderDetail.setCheckFlag((Integer) checkFlag);
						}
						if (checkFlag instanceof Boolean) {
							if ((Boolean) checkFlag == false) {
								cotOrderDetail.setCheckFlag(0);
							} else {
								cotOrderDetail.setCheckFlag(1);
							}
						}
					}
					records.add(cotOrderDetail);
				}
			}
		}

		for (int i = 0; i < records.size(); i++) {
			CotOrderDetail detail = records.get(i);
			if (detail == null) {
				continue;
			}
			detail.setCurrencyId(Integer.parseInt(currencyId));
			detail.setOrderId(Integer.parseInt(orderId));
			detail.setEleAddTime(new Date(System.currentTimeMillis()));// 添加时间
			detail.setAddTime(new Date(System.currentTimeMillis()));// 编辑时间
			// 设置总数
			if (detail.getBoxCount() == null) {
				detail.setBoxCount(0l);
			}
			if (detail.getContainerCount() == null) {
				detail.setContainerCount(0l);
			}
			if (detail.getBoxCbm() == null) {
				detail.setTotalCbm(0f);
			} else {
				detail.setTotalCbm(detail.getContainerCount() * detail.getBoxCbm());
			}
			if (detail.getPriceFac() == null) {
				detail.setTotalFac(0f);
			} else {
				detail.setTotalFac(detail.getBoxCount() * detail.getPriceFac());
			}
			if (detail.getBoxGrossWeigth() == null) {
				detail.setTotalGrossWeigth(0f);
			} else {
				detail.setTotalGrossWeigth(detail.getContainerCount() * detail.getBoxGrossWeigth());
			}
			if (detail.getBoxNetWeigth() == null) {
				detail.setTotalNetWeigth(0f);
			} else {
				detail.setTotalNetWeigth(detail.getContainerCount() * detail.getBoxNetWeigth());
			}
			if (detail.getOrderPrice() == null) {
				detail.setTotalMoney(0.0);
			} else {
				detail.setTotalMoney(detail.getBoxCount() * detail.getOrderPrice());
			}
			// 初始化
			if (detail.getBoxCount() != null) {
				detail.setUnBoxCount(new Float(detail.getBoxCount())); // 未出货数量
				detail.setUnBoxCount4OrderFac(new Long(detail.getBoxCount())); // 未采购数量
			} else {
				detail.setUnBoxCount(0f); // 未出货数量
				detail.setUnBoxCount4OrderFac(0l); // 未采购数量
			}

			detail.setUnBoxSend(0); // 是否完全出货 0：否 1：是
			detail.setUnBoxSend4OrderFac(0); // 是否完全采购 0：否

			// 新建图片
			CotOrderPic orderPic = new CotOrderPic();
			if (detail.getType().equals("ele")) {
				CotElePic cotElePic = impOpService.getElePicImgByEleId(detail.getSrcId());
				detail.setPicName(cotElePic.getEleId());
				orderPic.setEleId(cotElePic.getEleId());
				orderPic.setPicImg(cotElePic.getPicImg());
				orderPic.setPicSize(cotElePic.getPicImg().length);
				orderPic.setPicName(cotElePic.getEleId());
			}
			if (detail.getType().equals("price")) {
				CotPricePic cotPricePic = impOpService.getPricePic(detail.getSrcId());
				detail.setPicName(cotPricePic.getEleId());
				orderPic.setEleId(cotPricePic.getEleId());
				orderPic.setPicImg(cotPricePic.getPicImg());
				orderPic.setPicSize(cotPricePic.getPicImg().length);
				orderPic.setPicName(cotPricePic.getEleId());
			}
			if (detail.getType().equals("order")) {
				CotOrderPic cotOrderPic = impOpService.getOrderPic(detail.getSrcId());
				// 新增的这行 引用的图片刚好是同个表格被删掉的行,先执行删除,已经删掉图片
				if (cotOrderPic != null) {
					detail.setPicName(cotOrderPic.getEleId());
					orderPic.setEleId(cotOrderPic.getEleId());
					orderPic.setPicImg(cotOrderPic.getPicImg());
					orderPic.setPicSize(cotOrderPic.getPicImg().length);
					orderPic.setPicName(cotOrderPic.getEleId());
				} else {
					detail.setPicName(detail.getEleId());
					orderPic.setEleId(detail.getEleId());
					orderPic.setPicImg(zwtpByte);
					orderPic.setPicSize(zwtpByte.length);
					orderPic.setPicName(detail.getEleId());
				}
			}
			if (detail.getType().equals("given")) {
				CotGivenPic cotGivenPic = impOpService.getGivenPic(detail.getSrcId());
				detail.setPicName(cotGivenPic.getEleId());
				orderPic.setEleId(cotGivenPic.getEleId());
				orderPic.setPicImg(cotGivenPic.getPicImg());
				orderPic.setPicSize(cotGivenPic.getPicImg().length);
				orderPic.setPicName(cotGivenPic.getEleId());
			}
			if (detail.getType().equals("panEle")) {
				CotPanPic cotPanPic = impOpService.getPanPic(detail.getSrcId());
				detail.setPicName(cotPanPic.getEleId());
				orderPic.setEleId(cotPanPic.getEleId());
				orderPic.setPicImg(cotPanPic.getPicImg());
				orderPic.setPicSize(cotPanPic.getPicImg().length);
				orderPic.setPicName(cotPanPic.getEleId());
			}
			if (detail.getType().equals("none")) {
				detail.setPicName(detail.getEleId());
				orderPic.setEleId(detail.getEleId());
				orderPic.setPicImg(zwtpByte);
				orderPic.setPicSize(zwtpByte.length);
				orderPic.setPicName(detail.getEleId());
			}
			// 添加到图片数组
			imgList.add(orderPic);
		}

		// 保存
		this.getOrderService().addOrderDetails(records);
//		List custPcs=this.getOrderService().getCustPcs(custId);
		// 保存报价图片
		for (int i = 0; i < records.size(); i++) {
			CotOrderDetail detail = (CotOrderDetail) records.get(i);
			CotOrderPic cotOrderPic = (CotOrderPic) imgList.get(i);
			cotOrderPic.setFkId(detail.getId());
			List picList = new ArrayList();
			picList.add(cotOrderPic);
			// 逐条添加，避免数据量大的时候，内存溢出
			impOpService.saveImg(picList);
			
//			for (int j = 0; j < custPcs.size(); j++) {
//				CotCustPc cotCustPc = (CotCustPc) custPcs.get(j);
//				if(detail.getBarcode()!=null && !detail.getBarcode().equals("") && detail.getEleTypeidLv3()!=null && detail.getEleTypeidLv3().intValue()==cotCustPc.getCategoryId().intValue()){
//					CotOrderPc orderPc=new CotOrderPc();
//					orderPc.setCategoryId(detail.getEleTypeidLv3());
//					orderPc.setOrderId(Integer.parseInt(orderId));
//					orderPc.setOrderDetailId(detail.getId());
//					orderPc.setPcRemark(cotCustPc.getPcRemark());
//					orderPc.setPhone(cotCustPc.getPhone());
//					List pcList = new ArrayList();
//					pcList.add(orderPc);
//					// 逐条添加，避免数据量大的时候，内存溢出
//					impOpService.saveImg(pcList);
//				}
//			}
		}

		// b货号调用历史数据时,如果刚好是同个主单的明细a,如果刚好这条明细需要被删除,
		// 则后台action会先调用删除方法,把a删掉,数据库的外键级联删除明细图片,那么b明细保存时,无法找到引用的a图片
		// 解决方法是,先判断要删除的订单明细是否被其他订单引用,如果有,则暂时不删除该明细,把该明细的type字段设置为'delete',在保存的action中的最后再删除
		List delIds = new ArrayList();
		Iterator<?> itAt = orderMap.keySet().iterator();
		while (itAt.hasNext()) {
			String key = (String) itAt.next();
			CotOrderDetail detail = orderMap.get(key);
			if (detail.getType() != null && detail.getType().equals("delete")) {
				delIds.add(detail.getId());
			}
		}
		this.getOrderService().deleteDetailByIds(delIds);

		// 获取货号、客号保存数据到客号表
		Iterator<?> it = records.iterator();
		Map<String, String> elecustMap = new HashMap<String, String>();
		Map<String, String> elenameenMap = new HashMap<String, String>();
		String type = "order";
		while (it.hasNext()) {
			CotOrderDetail detail = (CotOrderDetail) it.next();
			String eleId = detail.getEleId();
			String custNo = detail.getCustNo();
			String eleNameEn = detail.getEleNameEn();

			if (custNo != null && !custNo.trim().equals("") && !custNo.equals("null")) {
				elecustMap.put(eleId, custNo);
			}
			if (eleNameEn != null && !eleNameEn.trim().equals("")) {
				elenameenMap.put(eleId, eleNameEn);
			}
		}
		if (elecustMap.size() != 0 && elenameenMap.size() != 0) {
			this.getCotElementsService().saveEleCustNoByCustList(elecustMap, elenameenMap, custId, type);
		}
		return null;
	}

	// 编辑时添加参数
	public CotOrderDetail setOrderDetailVal(CotOrderDetail detail) {
		if (detail.getBoxCount() == null) {
			detail.setBoxCount(0l);
		}
		if (detail.getContainerCount() == null) {
			detail.setContainerCount(0l);
		}
		if (detail.getBoxCbm() != null) {
			detail.setTotalCbm(detail.getContainerCount() * detail.getBoxCbm());
		} else {
			detail.setTotalCbm(0f);
		}
		if (detail.getPriceFac() != null) {
			detail.setTotalFac(detail.getBoxCount() * detail.getPriceFac());
		} else {
			detail.setTotalFac(0f);
		}
		if (detail.getBoxGrossWeigth() != null) {
			detail.setTotalGrossWeigth(detail.getContainerCount() * detail.getBoxGrossWeigth());
		} else {
			detail.setTotalGrossWeigth(0f);
		}
		if (detail.getBoxNetWeigth() != null) {
			detail.setTotalNetWeigth(detail.getContainerCount() * detail.getBoxNetWeigth());
		} else {
			detail.setTotalNetWeigth(0f);
		}
		if (detail.getOrderPrice() != null) {
			detail.setTotalMoney( detail.getOrderPrice()*detail.getBoxCount());
		} else {
			detail.setTotalMoney(0.0);
		}
		// 初始化
		if (detail.getBoxCount() != null) {
			// 取得原先的数量值,用来判断新增了多少数量.好在出货和国内采购时维持未发货量正确
			CotOrderDetail oldOrderDetail = this.getOrderService().getOrderDetailById(detail.getId());
			long cha = detail.getBoxCount() - oldOrderDetail.getBoxCount();
			// detail.setUnBoxCount(detail.getUnBoxCount() + cha);
			detail.setUnBoxCount4OrderFac(detail.getUnBoxCount4OrderFac() + cha);
		} else {
			detail.setUnBoxCount(0f);
		}
		// 判断利润率是否超过数据库范围
		Float liRes = detail.getLiRun();
		if (liRes == null) {
			liRes = 0f;
			detail.setLiRun(liRes);
		}

		if (liRes <= -1000) {
			detail.setLiRun(-999f);
		}
		if (liRes >= 1000) {
			detail.setLiRun(999f);
		}
		detail.setAddTime(new Date(System.currentTimeMillis()));// 编辑时间
		// 克隆对象,避免造成指针混用
		CotOrderDetail cloneObj = (CotOrderDetail) SystemUtil.deepClone(detail);
		return cloneObj;
	}

	@Override
	public ActionForward modify(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {

		// 内存数据
		HttpSession session = request.getSession();
		Map<String, CotOrderDetail> orderMap = this.getOrderService().getMapAction(session);

		List<CotOrderDetail> records = new ArrayList<CotOrderDetail>();
		String customerId = request.getParameter("custId");
		String currencyId = request.getParameter("currencyId");
		Integer custId = Integer.parseInt(customerId);

		// 前台传递来的数据
		String json = request.getParameter("data");
		// 判断数据是一条还是多条
		boolean single = json.startsWith("{");
		if (single) {
			JSONObject jsonObject = getJsonObject(json);
			Object rdm = jsonObject.get("rdm");
			if (rdm != null && !"".equals(rdm.toString())) {
				CotOrderDetail cotOrderDetail = orderMap.get(rdm.toString());
				cotOrderDetail.setCurrencyId(Integer.parseInt(currencyId));
				// 设置是否锁定价格
				Object checkFlag = jsonObject.get("checkFlag");
				if (checkFlag instanceof Integer) {
					cotOrderDetail.setCheckFlag((Integer) checkFlag);
				}
				if (checkFlag instanceof Boolean) {
					if ((Boolean) checkFlag == false) {
						cotOrderDetail.setCheckFlag(0);
					} else {
						cotOrderDetail.setCheckFlag(1);
					}
				}
				records.add(setOrderDetailVal(cotOrderDetail));
			}
		} else {
			JSONArray jarray = JSONArray.fromObject(json);
			for (int i = 0; i < jarray.size(); i++) {
				JSONObject jsonObject = jarray.getJSONObject(i);
				Object rdm = jsonObject.get("rdm");
				if (rdm != null && !"".equals(rdm.toString())) {
					CotOrderDetail cotOrderDetail = orderMap.get(rdm.toString());
					cotOrderDetail.setCurrencyId(Integer.parseInt(currencyId));
					// 设置是否锁定价格
					Object checkFlag = jsonObject.get("checkFlag");
					if (checkFlag instanceof Integer) {
						cotOrderDetail.setCheckFlag((Integer) checkFlag);
					}
					if (checkFlag instanceof Boolean) {
						if ((Boolean) checkFlag == false) {
							cotOrderDetail.setCheckFlag(0);
						} else {
							cotOrderDetail.setCheckFlag(1);
						}
					}
					records.add(setOrderDetailVal(cotOrderDetail));
				}
			}
		}

		// 保存
		this.getOrderService().modifyOrderDetails(records);

		// 获取货号、客号保存数据到客号表
		Iterator<?> it = records.iterator();
		Map<String, String> elecustMap = new HashMap<String, String>();
		Map<String, String> elenameenMap = new HashMap<String, String>();
		String type = "order";
		while (it.hasNext()) {
			CotOrderDetail detail = (CotOrderDetail) it.next();
			String eleId = detail.getEleId();
			String custNo = detail.getCustNo();
			String eleNameEn = detail.getEleNameEn();

			if (custNo != null && !custNo.trim().equals("")) {
				elecustMap.put(eleId, custNo);
			}
			if (eleNameEn != null && !eleNameEn.trim().equals("")) {
				elenameenMap.put(eleId, eleNameEn);
			}
		}
		if (elecustMap.size() != 0 && elenameenMap.size() != 0) {
			this.getCotElementsService().saveEleCustNoByCustList(elecustMap, elenameenMap, custId, type);
		}
		return null;
	}

	@SuppressWarnings("deprecation")
	@Override
	public ActionForward remove(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		String json = request.getParameter("data");
		List ids = null;
		// 判断数据是一条还是多条
		boolean single = json.startsWith("[");
		if (!single) {
			ids = new ArrayList();
			ids.add(new Integer(json));
		} else {
			JSONArray jarray = JSONArray.fromObject(json);
			ids = jarray.toList(jarray, ArrayList.class);
		}
		this.getOrderService().deleteOrderDetailsAction(request, ids);
		return null;
	}

	// 跳转到订单添加页面
	public ActionForward addOrder(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		request.setAttribute("allCurrencyName", this.getOrderService().getCurrencyMap(request));
		// request.setAttribute("allFactoryName", this.getOrderService()
		// .getFactoryNameMap(request));
		// 默认公司id
		// request.setAttribute("defaultCompanyId",
		// this.getOrderService().getDefaultCompanyId());
		return mapping.findForward("orderDetail");
	}

	// 跳转到订单明细编辑页面
	public ActionForward queryDetail(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		return mapping.findForward("detail");
	}

	// 查询订单单数据
	@Override
	public ActionForward query(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		
		String flag = request.getParameter("flag");// 用于客户页面调用

		String custId = request.getParameter("custId");// 客户ID
		String companyId = request.getParameter("companyId");// 客户ID
		String currencyId = request.getParameter("currencyId");// 币种ID
		String status = request.getParameter("orderStatus");// 审核状态
		String canOut = request.getParameter("canOut");//出货状态
		String orderNoFind = request.getParameter("orderNoFind");// 订单号
		String poNo = request.getParameter("poNo");// po#
		String businessPerson = request.getParameter("businessPerson");// 业务员
		String startTime = request.getParameter("startTime");// 起始日期
		String endTime = request.getParameter("endTime");// 结束日期
		String startCheckTime = request.getParameter("startCheckTime");// 审核起始日期
		String endCheckTime = request.getParameter("endCheckTime");// 审核结束日期
		String start = request.getParameter("start");
		String limit = request.getParameter("limit");
		String con =request.getParameter("con");//有值表示从弹窗进入
		String typeLv1Id =request.getParameter("typeLv1IdFind");
		if (start == null || limit == null) {
			if ("customerPage".equals(flag)) {
				return mapping.findForward("queryOrder");
			} else {
				return mapping.findForward("querySuccess");
			}
		}

		StringBuffer queryString = new StringBuffer();
		queryString.append(" where 1=1");//不显示出完货的
		if(canOut!=null){
			if ("".equals(canOut) || "0".equals(canOut)) {
				queryString.append(" and (obj.can_out is null or obj.can_out=1 or obj.can_out=0 or obj.can_out=3)");
			}else{
				queryString.append(" and obj.can_out=2");
			}
		}
		// 获得登录人
		CotEmps emp = (CotEmps) request.getSession().getAttribute("emp");
		if (!"admin".equals(emp.getEmpsId())) {
			// 判断是否有最高权限
			boolean all = SystemUtil.isAction(request, "cotorder.do", "ALL");
			if (all == false) {
				JSONObject json = SystemUtil.getEmpDataPopedom(emp.getEmpRight());
				//判断国家权限
				boolean nation = SystemUtil.isAction(request, "cotorder.do", "NATION");
				if(nation ==true){
					String nationStr = json.getString("nation");
					queryString.append(" and obj.nation_Id in(").append(nationStr).append(")");
				}
				// 判断是否有查看该部门报价的权限
				boolean dept = SystemUtil.isAction(request, "cotorder.do", "DEPT");
				if (dept == true) {
					String deptStr = json.getString("dept");
					queryString.append(" and obj.typeLv1_Id in(").append(deptStr).append(")");
				} else {
					//queryString.append(" and obj.BUSINESS_PERSON =" + emp.getId());
				}
			}
			if(con.trim().equals("No")){
				queryString.append(" and obj.checkPerson =" +emp.getId());
				queryString.append(" and obj.ORDER_STATUS=3");
			}
		}

		
		if (currencyId != null && !currencyId.trim().equals("")) {
			queryString.append(" and obj.CURRENCY_ID =" + currencyId.trim());
		}
		if (status != null && !status.trim().equals("")) {
			if("1".equals(status)){
				queryString.append(" and (obj.ORDER_STATUS=3 or obj.ORDER_STATUS=0)");
			}
			if("2".equals(status)){
				queryString.append(" and obj.ORDER_STATUS=2");
			}
		}
		if (custId != null && !custId.trim().equals("")) {
			queryString.append(" and obj.CUST_ID =" + custId.trim());
		}
		if (companyId != null && !companyId.trim().equals("")) {
			queryString.append(" and obj.COMPANY_ID =" + companyId.trim());
		}
		if (orderNoFind != null && !orderNoFind.trim().equals("")) {
			queryString.append(" and obj.ORDER_NO like '%" + orderNoFind.trim() + "%'");
		}
		if (poNo != null && !poNo.trim().equals("")) {
			queryString.append(" and obj.PO_NO like '%" + poNo.trim() + "%'");
		}
		if (businessPerson != null && !businessPerson.trim().equals("")) {
			queryString.append(" and obj.BUSINESS_PERSON=" + businessPerson.trim());
		}
		if (startTime != null && !"".equals(startTime.trim())) {
			queryString.append(" and obj.order_lc_date >='" + startTime + "'");
		}
		if (endTime != null && !"".equals(endTime.trim())) {
			queryString.append(" and obj.order_lc_date <='" + endTime + " 23:59:59'");
		}
		if (startCheckTime != null && !"".equals(startCheckTime.trim())) {
			queryString.append(" and obj.given_date >='" + startCheckTime + "'");
		}
		if (endCheckTime != null && !"".equals(endCheckTime.trim())) {
			queryString.append(" and obj.given_date <='" + endCheckTime + " 23:59:59'");
		}
		//department
		if (typeLv1Id!= null && !"".equals(typeLv1Id.trim())) {
			//String hql = "select id from CotTypeLv1 obj where obj.typeEnName like '"+typeLv1Id+"%'";
			List list = this.getOrderService().getList("CotTypeLv1");
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

		// 设置查询参数
		QueryInfo queryInfo = new QueryInfo();
		// 设置每页显示多少行
		Integer pageCount = Integer.parseInt(limit);
		// 设定每页显示记录数
		queryInfo.setCountOnEachPage(pageCount);
		// 设置查询记录总数语句
		queryInfo.setCountQuery("select count(*) from cot_order obj left join cot_order_fac fac on fac.order_id=obj.id " 
				+ "left join cot_emps e on obj.BUSINESS_PERSON=e.id " +
						"left join cot_customer c on obj.CUST_ID=c.id" + queryString);
		// 查询语句
		queryInfo.setSelectString("select obj.id," 
				+ "obj.ORDER_TIME as orderTime," 
				+ "obj.SEND_TIME as sendTime," 
				+ "c.CUSTOMER_SHORT_NAME as customerShortName," 
				+ "obj.ORDER_NO as orderNo," 
				+ "e.EMPS_NAME as empsName, " 
				+ "obj.CLAUSE_TYPE_ID as clauseTypeId," 
				+ "obj.CURRENCY_ID as currencyId," 
				+ "obj.PAY_TYPE_ID as payTypeId," 
				+ "obj.total_count as totalCount," 
				+ "obj.total_container as totalContainer," 
				+ "obj.total_cbm as totalCBM," 
				+ "obj.total_Money as totalMoney," 
				+ "obj.ORDER_STATUS as orderStatus," 
				+ "obj.PO_NO as poNo," 
				+ "obj.ORDER_RATE as orderRate," 
				+ "obj.new_remark as newRemark," 
				+ "obj.Order_Compose as orderCompose," 
				+ "obj.TOTAL_GROSS as totalGross," 
				+ "c.id as cId," +"obj.typeLv1_id as typeLv1Id,"
				+ "obj.all_pin_name as allPinName," 
				+ "obj.order_lc_date as orderLcDate," 
				+ "obj.order_lc_delay as orderLcDelay," +
						"obj.ORDER_EARNEST as orderEarnest," +
						"obj.factory_id as factoryId," +
						"obj.can_out as canOut,fac.TOTAL_MONEY as ftm" +
						",obj.SHIPPORT_ID as shipportId"+ 
						",obj.TARGETPORT_ID as targetportId" 
				+ " from cot_order obj left join cot_order_fac fac on fac.order_id=obj.id " 
				+ "left join cot_emps e on obj.BUSINESS_PERSON=e.id " +
						"left join cot_customer c on obj.CUST_ID=c.id");
		// 设置条件语句
		queryInfo.setQueryString(queryString.toString());
		// 设置排序语句
		queryInfo.setOrderString(" order by obj.ORDER_NO asc");
		//queryInfo.setOrderString(" order by obj.id asc");
		queryInfo.setQueryObjType("CotOrderVO");
		int count = this.getOrderService().getRecordCountJDBC(queryInfo);

		// 取得排序信息
		int startIndex = Integer.parseInt(start);
		queryInfo.setStartIndex(startIndex);

		GridServerHandler gd = new GridServerHandler();
		List res = this.getOrderService().getOrderVO(queryInfo);
		
		gd.setData(res);
		gd.setTotalCount(count);
		try {
			response.getWriter().write(gd.getLoadResponseText());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	// 查询该订单单的订单明细产品的信息
	public ActionForward queryOrderDetail(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {

		String start = request.getParameter("start");
		String limit = request.getParameter("limit");
		String ctPc = request.getParameter("ctPc");
		if (start == null || limit == null)
			return mapping.findForward("orderDetail");

		StringBuffer queryString = new StringBuffer();
		String orderId = request.getParameter("orderId");// 主报价单号
		if (orderId == null || "".equals(orderId)) {
			return null;
		}
		queryString.append(" where obj.orderId=" + orderId);
		// 设置查询参数
		QueryInfo queryInfo = new QueryInfo();
		int startIndex = Integer.parseInt(start);
		queryInfo.setStartIndex(startIndex);
		// 设置每页显示多少行
		int pageCount = Integer.parseInt(limit);
		// 设定每页显示记录数
		queryInfo.setCountOnEachPage(pageCount);
		// 设置查询记录总数语句
		queryInfo.setCountQuery("select count(*) from CotOrderDetail obj" + queryString);
		// 设置查询记录语句
		queryInfo.setSelectString("from CotOrderDetail obj");
		// 设置条件语句
		queryInfo.setQueryString(queryString.toString());
		// 设置排序语句
		queryInfo.setOrderString(" order by obj.sortNo asc,obj.eleId asc");

		try {
			String json = this.getOrderService().getListData(request, queryInfo,ctPc);
			response.getWriter().write(json);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	// 跳转到上传麦标页面
	public ActionForward showUpload(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		return mapping.findForward("showUpload");
	}

	// 查询明细产品的样品信息
	public ActionForward queryEleFrame(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {

		return mapping.findForward("queryEleFrame");
	}

	// 跳转到excel导入界面
	public ActionForward showExcel(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		return mapping.findForward("showExcel");
	}

	// 跳转到图片导入页面
	public ActionForward importPic(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {

		return mapping.findForward("importPic");
	}

	// 查询应收帐主单数据
	public ActionForward queryRecv(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {

		String start = request.getParameter("start");
		String limit = request.getParameter("limit");
		if (start == null || limit == null)
			return mapping.findForward("queryRecv");

		String orderNo = request.getParameter("orderNo");// 单号
		String fkId = request.getParameter("fkId");// 外键
		String source = request.getParameter("source");// 金额源来
		String custId = request.getParameter("custId");// 客户

		StringBuffer queryString = new StringBuffer();
		queryString.append(" where obj.source='order'");

		if (orderNo != null && !orderNo.trim().equals("")) {
			queryString.append(" and obj.orderNo='" + orderNo.trim() + "'");
		}
		if (fkId != null && !fkId.trim().equals("")) {
			queryString.append(" and obj.fkId=" + fkId.trim());
		}
		if (source != null && !source.trim().equals("")) {
			queryString.append(" and obj.source='" + source.trim() + "'");
		}
		if (custId != null && !custId.trim().equals("")) {
			queryString.append(" and obj.custId=" + custId.trim());
		}

		// 设置查询参数
		QueryInfo queryInfo = new QueryInfo();
		int startIndex = Integer.parseInt(start);
		queryInfo.setStartIndex(startIndex);
		// 设置每页显示多少行
		int pageCount = Integer.parseInt(limit);
		// 设定每页显示记录数
		queryInfo.setCountOnEachPage(pageCount);

		// 设置查询记录总数语句
		queryInfo.setCountQuery("select count(*) from CotFinaceAccountrecv as obj" + queryString);
		// 查询语句
		queryInfo.setSelectString("from CotFinaceAccountrecv AS obj");
		// 设置条件语句
		queryInfo.setQueryString(queryString.toString());
		// 设置排序语句
		queryInfo.setOrderString(" order by obj.id desc");

		try {
			String json = this.getOrderService().getJsonData(queryInfo);
			response.getWriter().write(json);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	// 查询收款记录数据
	public ActionForward queryRecvDetail(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {

		String start = request.getParameter("start");
		String limit = request.getParameter("limit");
		if (start == null || limit == null)
			return null;

		String recvId = request.getParameter("recvId");// 应收款ID

		StringBuffer queryString = new StringBuffer();
		queryString.append(" where detail.finaceRecvid=p.id");

		if (recvId != null && !recvId.trim().equals("")) {
			queryString.append(" and detail.recvId=" + recvId.trim());
		}

		// 设置查询参数
		QueryInfo queryInfo = new QueryInfo();
		int startIndex = Integer.parseInt(start);
		queryInfo.setStartIndex(startIndex);
		// 设置每页显示多少行
		int pageCount = Integer.parseInt(limit);
		// 设定每页显示记录数
		queryInfo.setCountOnEachPage(pageCount);
		// 设置查询记录总数语句
		queryInfo.setCountQuery("select count(*) from CotFinacerecv p,CotFinacerecvDetail detail" + queryString);
		// 查询语句
		queryInfo.setSelectString("select detail,p.finaceNo from CotFinacerecv p,CotFinacerecvDetail detail");
		// 设置条件语句
		queryInfo.setQueryString(queryString.toString());
		// 设置排序语句
		queryInfo.setOrderString(" order by detail.id desc");

		// 根据起始
		List<?> list = this.getOrderService().getList(queryInfo);

		try {
			GridServerHandler gd = new GridServerHandler();
			List res = this.getOrderService().getRecvDetailList(list);
			int count = this.getOrderService().getRecordCount(queryInfo);
			gd.setData(res);
			gd.setTotalCount(count);
			String json = gd.getLoadResponseText();
			response.getWriter().write(json);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	// 保存
	public ActionForward addRecv(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		// Integer empId = (Integer) request.getSession().getAttribute("empId");
		// String orderNo = request.getParameter("orNo");// 单号
		// String fkId = request.getParameter("fId");// 外键
		// String custId = request.getParameter("cId");// 客户编号
		// String businessPerson = request.getParameter("bPerson");// 业务员
		// String companyId = request.getParameter("comId");// 公司
		// // 获得操作人
		// // 1.使用反射获取对象的所有属性名称
		// String[] properties = ReflectionUtils
		// .getDeclaredFields(CotFinaceAccountrecv.class);
		// // 2.获取页面传入的所有属性的值
		// String[] recordKeys = request
		// .getParameterValues(TableConstants.RECORDKEY_NAME);
		// List<CotFinaceAccountrecv> records = new
		// ArrayList<CotFinaceAccountrecv>();
		// // 声明需要多少个类
		// for (int i = 0; i < properties.length; i++) {
		// String[] values = request.getParameterValues(properties[i]);
		// if (values != null) {
		// for (int j = 0; j < values.length; j++) {
		// CotFinaceAccountrecv finaceAccountrecv = new CotFinaceAccountrecv();
		// finaceAccountrecv.setSource("order");
		// finaceAccountrecv.setCustId(Integer.parseInt(custId));
		// finaceAccountrecv.setOrderNo(orderNo);
		// finaceAccountrecv.setFkId(Integer.parseInt(fkId));
		// finaceAccountrecv.setBusinessPerson(Integer
		// .parseInt(businessPerson));
		// finaceAccountrecv.setEmpId(empId);
		// finaceAccountrecv.setStatus(0);
		// finaceAccountrecv.setAddDate(new java.util.Date());
		// finaceAccountrecv.setIsImport(0);
		// finaceAccountrecv.setCompanyId(Integer.parseInt(companyId));
		// records.add(finaceAccountrecv);
		// }
		// break;
		// }
		// }
		// SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		// for (int i = 0; i < properties.length; i++) {
		// // 获取页面传过来的属性值
		// String[] values = request.getParameterValues(properties[i]);
		// if (values != null) {
		// for (int j = 0; j < values.length; j++) {
		// try {
		// CotFinaceAccountrecv cotFinaceAccountrecv = records
		// .get(j);
		// // 帐款时间转化
		// if (properties[i].equals("amountDate")) {
		// cotFinaceAccountrecv.setAmountDate(sdf
		// .parse(values[j]));
		// } else if (properties[i].equals("amount")) {
		// cotFinaceAccountrecv.setAmount(Double
		// .parseDouble(values[j]));
		// // 设置剩余金额等于当前总金额
		// cotFinaceAccountrecv.setRemainAmount(Double
		// .parseDouble(values[j]));
		// } else {
		// // 设置对象个属性
		// BeanUtils.setProperty(cotFinaceAccountrecv,
		// properties[i], values[j]);
		// }
		// } catch (IllegalAccessException e) {
		// e.printStackTrace();
		// } catch (InvocationTargetException e) {
		// e.printStackTrace();
		// } catch (ParseException e) {
		// e.printStackTrace();
		// }
		// }
		// }
		// }
		//
		// // 返回的结果数组，数组长度一般为增加对象的个数,1为成功，0为失败
		// // 暂时都设为成功
		// int[] resultCodes = new int[records.size()];
		// for (int i = 0; i < resultCodes.length; i++) {
		// resultCodes[i] = 1;
		// }
		// // 返回的信息数组，暂时设为空
		// String[] messages = null;
		// // 调用service更新记录
		// this.getOrderService().addRecvList(records);
		// try {
		// super.defaultAjaxResopnse("accountrecvTable", recordKeys,
		// resultCodes, messages, request, response);
		// } catch (Exception e) {
		// e.printStackTrace();
		// }
		return null;
	}

	// 删除
	public ActionForward removeRecv(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		// // 获取页面传入的所有recordKey的属性值
		// String[] recordKeys = request
		// .getParameterValues(TableConstants.RECORDKEY_NAME);
		// int[] resultCodes = new int[0];
		// String[] messages = null;
		//
		// // 获取其他费用编号
		// String[] finaceOtherIdAry =
		// request.getParameterValues("finaceOtherId");
		//
		// if (recordKeys != null) {
		// List<CotFinaceAccountrecv> list = new
		// ArrayList<CotFinaceAccountrecv>();
		// for (int i = 0; i < recordKeys.length; i++) {
		// CotFinaceAccountrecv finaceAccountrecv = new CotFinaceAccountrecv();
		// finaceAccountrecv.setId(new Integer(recordKeys[i]));
		// if (!"".equals(finaceOtherIdAry[i].trim())) {
		// finaceAccountrecv.setFinaceOtherId(Integer
		// .parseInt(finaceOtherIdAry[i]));
		// }
		// list.add(finaceAccountrecv);
		//
		// }
		// this.getOrderService().deleteRecvList(list);
		// resultCodes = new int[recordKeys.length];
		// for (int i = 0; i < resultCodes.length; i++) {
		// resultCodes[i] = 1;
		// }
		// }
		// try {
		// super.defaultAjaxResopnse("accountrecvTable", recordKeys,
		// resultCodes, messages, request, response);
		// } catch (ServletException e) {
		// e.printStackTrace();
		// } catch (IOException e) {
		// e.printStackTrace();
		// }
		return null;
	}

	// 查询其他费用主单数据
	public ActionForward queryOther(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {

		String start = request.getParameter("start");
		String limit = request.getParameter("limit");

		if (start == null || limit == null)
			return mapping.findForward("queryOther");

		String orderNo = request.getParameter("orderNo");// 单号
		String fkId = request.getParameter("fkId");// 外键
		String source = request.getParameter("source");// 金额源来
		String type = request.getParameter("type");// 类型 0:应收 1：应付

		StringBuffer queryString = new StringBuffer();
		queryString.append(" where 1=1");

		if (orderNo != null && !orderNo.trim().equals("")) {
			queryString.append(" and obj.orderNo='" + orderNo.trim() + "'");
		}
		if (fkId != null && !fkId.trim().equals("")) {
			queryString.append(" and obj.fkId=" + fkId.trim());
		}
		if (source != null && !source.trim().equals("")) {
			queryString.append(" and obj.source='" + source.trim() + "'");
		}
		if (type != null && !type.trim().equals("")) {
			queryString.append(" and obj.type=" + type.trim());
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
		queryInfo.setCountQuery("select count(*) from CotFinaceOther as obj" + queryString);
		// 查询语句
		queryInfo.setSelectString("from CotFinaceOther AS obj");
		// 设置条件语句
		queryInfo.setQueryString(queryString.toString());
		// 设置排序语句
		queryInfo.setOrderString(" order by obj.id desc");

		try {
			String json = this.getOrderService().getJsonData(queryInfo);
			response.getWriter().write(json);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	// 保存
	public ActionForward addOther(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {

		Integer empId = (Integer) request.getSession().getAttribute("empId");
		String orderNo = request.getParameter("mainNo");// 订单单号
		String fkId = request.getParameter("mainId");// 订单主单编号

		List<CotFinaceOther> records = new ArrayList<CotFinaceOther>();
		// 前台传递来的数据
		String json = request.getParameter("data");
		String[] properties = ReflectionUtils.getDeclaredFields(CotFinaceOther.class);
		// 判断数据是一条还是多条
		boolean single = json.startsWith("{");
		// 保存所有生产合同导入的编号
		String ids = "";
		try {
			if (single) {
				JSONObject jsonObject = getJsonObject(json);
				String name = jsonObject.getString("finaceName");
				if (!"".equals(name)) {
					CotFinaceOther finaceOther = new CotFinaceOther();
					for (int i = 0; i < properties.length; i++) {
						BeanUtils.setProperty(finaceOther, properties[i], jsonObject.get(properties[i]));
					}
					finaceOther.setType(0);// 类型 0:应收 1：应付
					finaceOther.setSource("order");
					finaceOther.setOrderNo(orderNo);
					finaceOther.setBusinessPerson(empId);
					finaceOther.setStatus(0);
					ids += finaceOther.getIsImport() + ",";
					finaceOther.setFkId(Integer.parseInt(fkId));
					records.add(finaceOther);
				}

			} else {
				JSONArray jarray = JSONArray.fromObject(json);
				for (int i = 0; i < jarray.size(); i++) {
					JSONObject jsonObject = jarray.getJSONObject(i);
					String name = jsonObject.getString("finaceName");
					if (!"".equals(name)) {
						CotFinaceOther finaceOther = new CotFinaceOther();
						for (int j = 0; j < properties.length; j++) {
							BeanUtils.setProperty(finaceOther, properties[j], jsonObject.get(properties[j]));
						}
						finaceOther.setType(0);// 类型 0:应收 1：应付
						finaceOther.setSource("order");
						finaceOther.setOrderNo(orderNo);
						finaceOther.setBusinessPerson(empId);
						finaceOther.setStatus(0);
						ids += finaceOther.getIsImport() + ",";
						finaceOther.setFkId(Integer.parseInt(fkId));
						records.add(finaceOther);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		// 保存
		this.getOrderService().addOtherList(records);
		// 判断需要更改其他费用的导入标识
		if (!ids.equals("")) {
			this.getOrderService().updateOrderFacIsImport(ids);
		}
		return null;
	}

	// 修改
	public ActionForward modifyOther(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {

		Integer empId = (Integer) request.getSession().getAttribute("empId");
		String orderNo = request.getParameter("orderNo");// 单号

		// 前台传递来的数据
		String json = request.getParameter("data");
		// 判断数据是一条还是多条
		boolean single = json.startsWith("{");
		// 1.使用反射获取对象的所有属性名称
		String[] properties = ReflectionUtils.getDeclaredFields(CotFinaceOther.class);
		List<CotFinaceOther> records = new ArrayList<CotFinaceOther>();
		try {
			if (single) {
				JSONObject jsonObject = getJsonObject(json);
				CotFinaceOther finaceOther = this.getOrderService().getFinaceOtherById(Integer.parseInt(jsonObject.getString("id")));
				for (int i = 0; i < properties.length; i++) {
					if (jsonObject.get(properties[i]) != null) {
						BeanUtils.setProperty(finaceOther, properties[i], jsonObject.get(properties[i]));
					}
				}
				finaceOther.setOrderNo(orderNo);
				finaceOther.setBusinessPerson(empId);
				records.add(finaceOther);
			} else {
				JSONArray jarray = JSONArray.fromObject(json);
				for (int i = 0; i < jarray.size(); i++) {
					JSONObject jsonObject = (JSONObject) jarray.get(i);
					CotFinaceOther finaceOther = this.getOrderService().getFinaceOtherById(Integer.parseInt(jsonObject.getString("id")));
					for (int j = 0; j < properties.length; j++) {
						if (jsonObject.get(properties[j]) != null) {
							BeanUtils.setProperty(finaceOther, properties[j], jsonObject.get(properties[j]));
						}
					}
					finaceOther.setOrderNo(orderNo);
					finaceOther.setBusinessPerson(empId);
					records.add(finaceOther);
				}
			}
			this.getOrderService().updateOtherList(records);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	// 删除
	public ActionForward removeOther(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		String json = request.getParameter("data");
		List ids = null;
		// 判断数据是一条还是多条
		boolean single = json.startsWith("[");
		if (!single) {
			ids = new ArrayList();
			ids.add(new Integer(json));
		} else {
			JSONArray jarray = JSONArray.fromObject(json);
			ids = jarray.toList(jarray, ArrayList.class);
		}
		this.getOrderService().deleteOtherList(ids);
		return null;
	}

	// 查询产品采购,配件采购,包材采购的其他费用
	public ActionForward queryOrderFacOther(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {

		String start = request.getParameter("start");
		String limit = request.getParameter("limit");

		if (start == null || limit == null)
			return null;

		String orderId = request.getParameter("orderId");// 订单编号
		String source = request.getParameter("source");// 类型
		String orderNo = request.getParameter("orderNoFind");// 生产合同号
		String finaceName = request.getParameter("finaceNameFind");// 费用名称

		StringBuffer queryString = new StringBuffer();

		if ("orderfac".equals(source)) {
			String facIds = this.getOrderService().checkIsHasFac(Integer.parseInt(orderId));
			if (facIds != null && !facIds.trim().equals("")) {
				String temp = facIds.trim();
				queryString.append(" where obj.fkId in (" + temp.substring(0, temp.length() - 1) + ")");
			} else {
				queryString.append(" where 1=0");
			}
		} else {
			queryString.append(" where d.orderId=" + Integer.parseInt(orderId));
		}

		queryString.append(" and obj.fkId=d.id and obj.flag='A' and obj.source='" + source + "' and (obj.isImport is null or obj.isImport=0)");

		if (orderNo != null && !orderNo.toString().trim().equals("")) {
			queryString.append(" and obj.orderNo like '%" + orderNo.toString().trim() + "%'");
		}

		if (finaceName != null && !finaceName.toString().trim().equals("")) {
			queryString.append(" and obj.finaceName like '%" + finaceName.toString().trim() + "%'");
		}

		// 设置查询参数
		QueryInfo queryInfo = new QueryInfo();
		int startIndex = Integer.parseInt(start);
		queryInfo.setStartIndex(startIndex);
		// 设置每页显示多少行
		Integer pageCount = Integer.parseInt(limit);
		// 设定每页显示记录数
		queryInfo.setCountOnEachPage(pageCount);

		// 查询语句
		if ("orderfac".equals(source)) {
			queryInfo.setSelectString("select obj from CotFinaceOther obj,CotOrderFac d");

			queryInfo.setCountQuery("select count(*) from CotFinaceOther obj,CotOrderFac d" + queryString);
		}
		if ("fitorder".equals(source)) {
			queryInfo.setSelectString("select obj from CotFinaceOther obj,CotFittingOrder d");
			queryInfo.setCountQuery("select count(*) from CotFinaceOther obj,CotFittingOrder d" + queryString);
		}
		if ("packorder".equals(source)) {
			queryInfo.setSelectString("select obj from CotFinaceOther obj,CotPackingOrder d");
			queryInfo.setCountQuery("select count(*) from CotFinaceOther obj,CotPackingOrder d" + queryString);
		}

		// 设置条件语句
		queryInfo.setQueryString(queryString.toString());
		// 设置排序语句
		queryInfo.setOrderString(" order by obj.id desc");

		try {
			String json = this.getOrderService().getJsonData(queryInfo);
			response.getWriter().write(json);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	// 添加提醒消息
	public ActionForward addToMessage(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		return mapping.findForward("addToMessage");
	}

	// 查询订单明细的配件信息
	public ActionForward queryOrderFittings(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		String start = request.getParameter("start");
		String limit = request.getParameter("limit");
		if (start == null || limit == null)
			return mapping.findForward("queryOrderFittings");
		StringBuffer queryString = new StringBuffer();
		String rdm = request.getParameter("rdm");// 报价明细
		// 获得内存map
		Map<String, CotOrderDetail> orderMap = this.getOrderService().getMapAction(request.getSession());
		CotOrderDetail detail = orderMap.get(rdm);
		String tableName = "";
		// 判断是否是新增,隐藏页面工具栏不让操作
		if (detail.getId() == null) {
			request.setAttribute("isHId", 0);
			// 判断货号来源(报价,样品,订单)
			String type = detail.getType();
			Integer srcId = detail.getSrcId();
			if ("ele".equals(type)) {
				tableName = "CotEleFittings";
				queryString.append(" where obj.eleId=" + srcId);
			}
			if ("given".equals(type)) {
				tableName = "CotEleFittings";
				// 用过货号查找样品id
				Integer eId = this.getOrderService().getEleIdByEleName(detail.getEleId());
				if (eId != 0) {
					queryString.append(" where obj.eleId=" + eId);
				} else {
					queryString.append(" where 1=0");
				}
			}
			if ("none".equals(type)) {
				tableName = "CotEleFittings";
				queryString.append(" where 1=0");
			}
			if ("price".equals(type)) {
				tableName = "CotPriceFittings";
				queryString.append(" where obj.priceDetailId=" + srcId);
			}
			if ("order".equals(type)) {
				tableName = "CotOrderFittings";
				queryString.append(" where obj.orderDetailId=" + srcId);
			}
		} else {
			tableName = "CotOrderFittings";
			request.setAttribute("isHId", detail.getId());
			queryString.append(" where obj.orderDetailId=" + detail.getId());
		}

		String facId = request.getParameter("facId");
		String fitNo = request.getParameter("fitNo");
		String fitName = request.getParameter("fitName");
		String fitDesc = request.getParameter("fitDesc");

		// 设置查询参数
		QueryInfo queryInfo = new QueryInfo();

		if (facId != null && !"".equals(facId)) {
			queryString.append(" and obj.facId=" + facId);
		}
		if (fitNo != null && !"".equals(fitNo)) {
			queryString.append(" and obj.fitNo like '%" + fitNo.trim() + "%'");
		}
		if (fitName != null && !"".equals(fitName)) {
			queryString.append(" and obj.fitName like '%" + fitName.trim() + "%'");
		}
		if (fitDesc != null && !"".equals(fitDesc)) {
			queryString.append(" and obj.fitDesc like '%" + fitDesc.trim() + "%'");
		}

		// 设置每页显示多少行
		int pageCount = 15;
		// 取得页面选择的每页显示条数
		pageCount = Integer.parseInt(limit);
		// 设定每页显示记录数
		queryInfo.setCountOnEachPage(pageCount);
		// 设置查询记录总数语句
		queryInfo.setCountQuery("select count(*) from " + tableName + " obj" + queryString);
		// 设置查询记录语句
		queryInfo.setSelectString("from " + tableName + " obj");
		// 设置条件语句
		queryInfo.setQueryString(queryString.toString());
		// 设置排序语句
		queryInfo.setOrderString(" order by obj.id desc");
		int count = this.getOrderService().getRecordCount(queryInfo);
		int startIndex = Integer.parseInt(start);
		queryInfo.setStartIndex(startIndex);

		List<?> list = this.getOrderService().getList(queryInfo);
		// 新增时
		if (detail.getId() == null) {
			// 如果是报价或订单的配件.需要判断该配件厂家或者采购价格是否和配件库一致,如果不同,用配件库的最新数据代替
			list = this.getOrderService().getNewList(tableName, list);
		}

		try {
			GridServerHandler gd = new GridServerHandler();
			gd.setData(list);
			gd.setTotalCount(count);
			String json = gd.getLoadResponseText();
			response.getWriter().write(json);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	// 查询订单明细的成本信息
	public ActionForward queryOrderElePrice(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		String start = request.getParameter("start");
		String limit = request.getParameter("limit");
		if (start == null || limit == null)
			return mapping.findForward("queryOrderElePrice");
		StringBuffer queryString = new StringBuffer();
		String rdm = request.getParameter("rdm");// 报价明细
		// 获得内存map
		Map<String, CotOrderDetail> orderMap = this.getOrderService().getMapAction(request.getSession());
		CotOrderDetail detail = orderMap.get(rdm);
		String tableName = "";
		// 判断是否是新增,隐藏页面工具栏不让操作
		if (detail.getId() == null) {
			request.setAttribute("isHId", false);
			// 判断货号来源(报价,样品,订单)
			String type = detail.getType();
			Integer srcId = detail.getSrcId();
			if ("ele".equals(type)) {
				tableName = "CotElePrice";
				queryString.append(" where obj.eleId=" + srcId);
			}
			if ("given".equals(type)) {
				tableName = "CotElePrice";
				// 用过货号查找样品id
				Integer eId = this.getOrderService().getEleIdByEleName(detail.getEleId());
				if (eId != 0) {
					queryString.append(" where obj.eleId=" + eId);
				} else {
					queryString.append(" where 1=0");
				}
			}
			if ("none".equals(type)) {
				tableName = "CotElePrice";
				queryString.append(" where 1=0");
			}
			if ("price".equals(type)) {
				tableName = "CotPriceEleprice";
				queryString.append(" where obj.priceDetailId=" + srcId);
			}
			if ("order".equals(type)) {
				tableName = "CotOrderEleprice";
				queryString.append(" where obj.orderDetailId=" + srcId);
			}
		} else {
			tableName = "CotOrderEleprice";
			request.setAttribute("isHId", true);
			queryString.append(" where obj.orderDetailId=" + detail.getId());
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
		queryInfo.setCountQuery("select count(*) from " + tableName + " obj" + queryString);
		// 设置查询记录语句
		queryInfo.setSelectString("from " + tableName + " obj");
		// 设置条件语句
		queryInfo.setQueryString(queryString.toString());
		// 设置排序语句
		queryInfo.setOrderString(" order by obj.id desc");
		int startIndex = Integer.parseInt(start);
		queryInfo.setStartIndex(startIndex);
		try {

			String json = this.getOrderService().getJsonData(queryInfo);
			response.getWriter().write(json);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	// 保存报价明细的配件
	public ActionForward addFitting(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		String detailId = request.getParameter("pFId");// 报价明细编号
		String priceId = request.getParameter("oId");// 报价编号
		// 前台传递来的数据
		String json = request.getParameter("data");
		// 判断数据是一条还是多条
		boolean single = json.startsWith("{");
		// 1.使用反射获取对象的所有属性名称
		String[] properties = ReflectionUtils.getDeclaredFields(CotOrderFittings.class);
		List<CotOrderFittings> records = new ArrayList<CotOrderFittings>();
		try {
			if (single) {
				JSONObject jsonObject = getJsonObject(json);
				CotOrderFittings orderFittings = new CotOrderFittings();
				for (int i = 0; i < properties.length; i++) {
					Object temp = jsonObject.get(properties[i]);
					if (temp instanceof JSONObject) {
						BeanUtils.setProperty(orderFittings, properties[i], null);
					} else {
						BeanUtils.setProperty(orderFittings, properties[i], jsonObject.get(properties[i]));
					}
				}
				orderFittings.setId(null);
				orderFittings.setOrderDetailId(Integer.parseInt(detailId));
				orderFittings.setOrderId(Integer.parseInt(priceId));
				records.add(orderFittings);
			} else {
				JSONArray jarray = JSONArray.fromObject(json);
				for (int i = 0; i < jarray.size(); i++) {
					CotOrderFittings orderFittings = new CotOrderFittings();
					JSONObject jsonObject = (JSONObject) jarray.get(i);
					for (int j = 0; j < properties.length; j++) {
						Object temp = jsonObject.get(properties[j]);
						if (temp instanceof JSONObject) {
							BeanUtils.setProperty(orderFittings, properties[j], null);
						} else {
							BeanUtils.setProperty(orderFittings, properties[j], jsonObject.get(properties[j]));
						}
					}
					orderFittings.setId(null);
					orderFittings.setOrderDetailId(Integer.parseInt(detailId));
					orderFittings.setOrderId(Integer.parseInt(priceId));
					records.add(orderFittings);
				}
			}
			this.getOrderService().addList(records);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	// 修改报价明细的配件
	public ActionForward modifyFitting(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {

		String detailId = request.getParameter("pFId");// 报价明细编号
		String priceId = request.getParameter("oId");// 报价编号
		// 前台传递来的数据
		String json = request.getParameter("data");
		// 判断数据是一条还是多条
		boolean single = json.startsWith("{");
		// 1.使用反射获取对象的所有属性名称
		String[] properties = ReflectionUtils.getDeclaredFields(CotOrderFittings.class);
		List<CotOrderFittings> records = new ArrayList<CotOrderFittings>();
		try {
			if (single) {
				JSONObject jsonObject = getJsonObject(json);
				CotOrderFittings orderFittings = this.getOrderService().getOrderFittingById(Integer.parseInt(jsonObject.getString("id")));
				for (int i = 0; i < properties.length; i++) {
					Object temp = jsonObject.get(properties[i]);
					if (temp instanceof JSONObject) {
						BeanUtils.setProperty(orderFittings, properties[i], null);
					} else {
						BeanUtils.setProperty(orderFittings, properties[i], jsonObject.get(properties[i]));
					}
				}
				// orderFittings.setId(null);
				orderFittings.setOrderDetailId(Integer.parseInt(detailId));
				orderFittings.setOrderId(Integer.parseInt(priceId));
				records.add(orderFittings);
			} else {
				JSONArray jarray = JSONArray.fromObject(json);
				for (int i = 0; i < jarray.size(); i++) {
					JSONObject jsonObject = (JSONObject) jarray.get(i);
					CotOrderFittings orderFittings = this.getOrderService().getOrderFittingById(Integer.parseInt(jsonObject.getString("id")));
					for (int j = 0; j < properties.length; j++) {
						Object temp = jsonObject.get(properties[j]);
						if (temp instanceof JSONObject) {
							BeanUtils.setProperty(orderFittings, properties[j], null);
						} else {
							BeanUtils.setProperty(orderFittings, properties[j], jsonObject.get(properties[j]));
						}
					}
					orderFittings.setOrderDetailId(Integer.parseInt(detailId));
					orderFittings.setOrderId(Integer.parseInt(priceId));
					records.add(orderFittings);
				}
			}
			this.getOrderService().modifyList(records);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;

	}

	// 删除报价明细的配件
	public ActionForward removeFitting(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		String json = request.getParameter("data");
		List ids = null;
		// 判断数据是一条还是多条
		boolean single = json.startsWith("[");
		if (!single) {
			ids = new ArrayList();
			ids.add(new Integer(json));
		} else {
			JSONArray jarray = JSONArray.fromObject(json);
			ids = jarray.toList(jarray, ArrayList.class);
		}
		this.getOrderService().deleteList(ids, "CotOrderFittings");

		return null;
	}

	// 保存报价明细的配件
	public ActionForward addElePrice(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		String detailId = request.getParameter("pFId");// 报价明细编号
		String priceId = request.getParameter("oId");// 报价编号

		// 前台传递来的数据
		String json = request.getParameter("data");
		// 判断数据是一条还是多条
		boolean single = json.startsWith("{");
		// 1.使用反射获取对象的所有属性名称
		String[] properties = ReflectionUtils.getDeclaredFields(CotOrderEleprice.class);
		List<CotOrderEleprice> records = new ArrayList<CotOrderEleprice>();
		try {
			if (single) {
				JSONObject jsonObject = getJsonObject(json);
				CotOrderEleprice orderEleprice = new CotOrderEleprice();
				for (int i = 0; i < properties.length; i++) {
					BeanUtils.setProperty(orderEleprice, properties[i], jsonObject.get(properties[i]));
				}
				orderEleprice.setId(null);
				orderEleprice.setOrderDetailId(Integer.parseInt(detailId));
				orderEleprice.setOrderId(Integer.parseInt(priceId));
				records.add(orderEleprice);
			} else {
				JSONArray jarray = JSONArray.fromObject(json);
				for (int i = 0; i < jarray.size(); i++) {
					CotOrderEleprice orderEleprice = new CotOrderEleprice();
					JSONObject jsonObject = (JSONObject) jarray.get(i);
					for (int j = 0; j < properties.length; j++) {
						BeanUtils.setProperty(orderEleprice, properties[j], jsonObject.get(properties[j]));
					}
					orderEleprice.setId(null);
					orderEleprice.setOrderDetailId(Integer.parseInt(detailId));
					orderEleprice.setOrderId(Integer.parseInt(priceId));
					records.add(orderEleprice);
				}
			}
			this.getOrderService().addElePriceList(records);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	// 修改报价明细的配件
	public ActionForward modifyElePrice(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {

		String detailId = request.getParameter("pFId");// 报价明细编号
		String priceId = request.getParameter("oId");// 报价编号

		// 前台传递来的数据
		String json = request.getParameter("data");
		// 判断数据是一条还是多条
		boolean single = json.startsWith("{");
		// 1.使用反射获取对象的所有属性名称
		String[] properties = ReflectionUtils.getDeclaredFields(CotOrderEleprice.class);
		List<CotOrderEleprice> records = new ArrayList<CotOrderEleprice>();
		try {
			if (single) {
				JSONObject jsonObject = getJsonObject(json);
				CotOrderEleprice orderEleprice = this.getOrderService().getElePriceById(Integer.parseInt(jsonObject.getString("id")));
				for (int i = 0; i < properties.length; i++) {
					BeanUtils.setProperty(orderEleprice, properties[i], jsonObject.get(properties[i]));
				}
				orderEleprice.setOrderDetailId(Integer.parseInt(detailId));
				orderEleprice.setOrderId(Integer.parseInt(priceId));
				records.add(orderEleprice);
			} else {
				JSONArray jarray = JSONArray.fromObject(json);
				for (int i = 0; i < jarray.size(); i++) {
					JSONObject jsonObject = (JSONObject) jarray.get(i);
					CotOrderEleprice orderEleprice = this.getOrderService().getElePriceById(Integer.parseInt(jsonObject.getString("id")));
					for (int j = 0; j < properties.length; j++) {
						BeanUtils.setProperty(orderEleprice, properties[j], jsonObject.get(properties[j]));
					}
					orderEleprice.setOrderDetailId(Integer.parseInt(detailId));
					orderEleprice.setOrderId(Integer.parseInt(priceId));
					records.add(orderEleprice);
				}
			}
			this.getOrderService().modifyList(records);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	// 删除报价明细的配件
	public ActionForward removeElePrice(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		// 获取页面传入的所有recordKey的属性值
		String json = request.getParameter("data");
		List ids = null;
		// 判断数据是一条还是多条
		boolean single = json.startsWith("[");
		if (!single) {
			ids = new ArrayList();
			ids.add(new Integer(json));
		} else {
			JSONArray jarray = JSONArray.fromObject(json);
			ids = jarray.toList(jarray, ArrayList.class);
		}
		this.getOrderService().deleteList(ids, "CotOrderEleprice");

		return null;
	}

	// 跳转到订单明细分解页面
	public ActionForward queryOrderToOrderFac(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		String start = request.getParameter("start");
		String limit = request.getParameter("limit");
		if (start == null || limit == null)
			return mapping.findForward("queryOrderToOrderFac");
		String orderId = request.getParameter("orderId");// 主订单单号

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
		queryInfo.setCountQuery("select count(*) from CotOrderDetail obj" + queryString);
		// 设置查询记录语句
		queryInfo.setSelectString("from CotOrderDetail obj");
		// 设置条件语句
		queryInfo.setQueryString(queryString.toString());
		// 设置排序语句
		queryInfo.setOrderString(" order by obj.eleId asc,obj.id asc");

		int startIndex = Integer.parseInt(start);
		queryInfo.setStartIndex(startIndex);

		List<?> list = this.getOrderService().getList(queryInfo);
		int totalCount = this.getOrderService().getRecordCount(queryInfo);

		// 将明细存到内存中的map
		HttpSession session = request.getSession();
		// 清空内存map
		session.removeAttribute("SessionORDER");
		Map<?, ?> facMap = this.getOrderService().getFactoryNameMap(request);

		for (int i = 0; i < list.size(); i++) {
			CotOrderDetail cotOrderDetail = (CotOrderDetail) list.get(i);
			cotOrderDetail.setPicImg(null);
			if (cotOrderDetail.getFactoryId() != null) {
				cotOrderDetail.setFactoryShortName(facMap.get(cotOrderDetail.getFactoryId().toString()).toString());
			}
			String rdm = "1" + RandomStringUtils.randomNumeric(8);
			cotOrderDetail.setRdm(rdm);
			this.getOrderService().setMapAction(session, rdm, cotOrderDetail);
		}
		try {

			GridServerHandler gd = new GridServerHandler();
			gd.setData(list);
			gd.setTotalCount(totalCount);
			String json = gd.getLoadResponseText();
			response.getWriter().write(json);

		} catch (Exception e) {
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
			queryString.append(" and obj.orderId=" + orderId.trim() + " and obj.orderType='ORDER'");
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
			String json = this.getOrderService().getJsonData(queryInfo);
			response.getWriter().write(json);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	// 保存计划日期
	public ActionForward addTime(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		List<CotOrderStatus> records = new ArrayList<CotOrderStatus>();
		// 前台传递来的数据
		String json = request.getParameter("data");
		String[] properties = ReflectionUtils.getDeclaredFields(CotOrderStatus.class);
		// 判断数据是一条还是多条
		boolean single = json.startsWith("{");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Integer orderId = 0;
		try {
			if (single) {
				JSONObject jsonObject = getJsonObject(json);
				CotOrderStatus orderStatus = new CotOrderStatus();
				for (int i = 0; i < properties.length; i++) {
					if (properties[i].equals("planDate")) {
						String planDate = jsonObject.get(properties[i]).toString();
						orderStatus.setPlanDate(sdf.parse(planDate));
					} else {
						BeanUtils.setProperty(orderStatus, properties[i], jsonObject.get(properties[i]));
					}
					orderStatus.setOrderFacId(null);
				}
				orderStatus.setOrderType("ORDER");
				orderId=orderStatus.getOrderId();
				records.add(orderStatus);

			} else {
				JSONArray jarray = JSONArray.fromObject(json);
				for (int i = 0; i < jarray.size(); i++) {
					JSONObject jsonObject = jarray.getJSONObject(i);
					CotOrderStatus orderStatus = new CotOrderStatus();
					for (int j = 0; j < properties.length; j++) {
						if (properties[j].equals("planDate")) {
							String planDate = jsonObject.get(properties[j]).toString();
							orderStatus.setPlanDate(sdf.parse(planDate));
						} else {
							BeanUtils.setProperty(orderStatus, properties[j], jsonObject.get(properties[j]));
						}
					}
					orderStatus.setOrderFacId(null);
					orderStatus.setOrderType("ORDER");
					orderId=orderStatus.getOrderId();
					records.add(orderStatus);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		// 保存
		this.getOrderService().addOtherList(records);
		this.getOrderService().saveTime(orderId);
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
				JSONObject jsonObject = getJsonObject(json);
				CotOrderStatus orderStatus = this.getOrderService().getOrderStatusById(Integer.parseInt(jsonObject.getString("id")));
				// orderStatus.setOrderType("ORDER");
				for (int i = 0; i < properties.length; i++) {
					if (properties[i].equals("planDate")) {
						String planDate = jsonObject.get(properties[i]).toString();
						orderStatus.setPlanDate(sdf.parse(planDate));
					} else if (properties[i].equals("orderFacId")) {
					} else {
						BeanUtils.setProperty(orderStatus, properties[i], jsonObject.get(properties[i]));
					}
				}
				orderStatus.setOrderType("ORDER");
				records.add(orderStatus);

			} else {
				JSONArray jarray = JSONArray.fromObject(json);
				for (int i = 0; i < jarray.size(); i++) {
					JSONObject jsonObject = jarray.getJSONObject(i);
					CotOrderStatus orderStatus = this.getOrderService().getOrderStatusById(Integer.parseInt(jsonObject.getString("id")));
					for (int j = 0; j < properties.length; j++) {
						if (properties[j].equals("planDate")) {
							String planDate = jsonObject.get(properties[j]).toString();
							orderStatus.setPlanDate(sdf.parse(planDate));
						} else if (properties[j].equals("orderFacId") || properties[j].equals("orderType")) {
						} else {
							BeanUtils.setProperty(orderStatus, properties[j], jsonObject.get(properties[j]));
						}
					}
					orderStatus.setOrderType("ORDER");
					records.add(orderStatus);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		// 保存
		this.getOrderService().updateOtherList(records);
		return null;
	}

	// 跳转到上传盘点机文件页面
	public ActionForward showMachineUpload(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		return mapping.findForward("showMachineUpload");
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
		String filePath = request.getParameter("filePathFind");
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
		if (filePath != null && !filePath.trim().equals("")) {
			queryString.append(" and obj.filePath like '%" + filePath.trim() + "%'");
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
			String json = this.getOrderService().getJsonData(queryInfo);
			response.getWriter().write(json);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
		
	}
	
	// 查询货号小类在客户小类图片中的图片
	public ActionForward queryCustPic(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {

		String start = request.getParameter("start");
		String limit = request.getParameter("limit");
		if (start == null || limit == null)
			return null;

		StringBuffer queryString = new StringBuffer();
		String categoryId = request.getParameter("categoryId");
		String custId = request.getParameter("custId");
		if (categoryId == null || "".equals(categoryId)) {
			return null;
		}
		if (custId == null || "".equals(custId)) {
			return null;
		}
		queryString.append(" where obj.categoryId=ty.id and obj.custId="+custId+" and obj.categoryId=" + categoryId);
		// 设置查询参数
		QueryInfo queryInfo = new QueryInfo();
		int startIndex = Integer.parseInt(start);
		queryInfo.setStartIndex(startIndex);
		// 设置每页显示多少行
		int pageCount = Integer.parseInt(limit);
		// 设定每页显示记录数
		queryInfo.setCountOnEachPage(pageCount);
		// 设置查询记录总数语句
		queryInfo.setCountQuery("select count(*) from CotCustPc obj,CotTypeLv3 ty"
				+ queryString);
		// 设置查询记录语句
		queryInfo.setSelectString("select new CotCustPc(obj,ty.typeName) from CotCustPc obj,CotTypeLv3 ty");
		// 设置条件语句
		queryInfo.setQueryString(queryString.toString());
		// 设置排序语句
		queryInfo.setOrderString(" order by obj.id desc");

		try {
			String excludes[] = {"phone"};
			queryInfo.setExcludes(excludes);
			String json = this.getOrderService().getJsonData(queryInfo);
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
	// 查看客户图片信息
	public ActionForward queryOrderRemark(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {

		String start = request.getParameter("start");
		String limit = request.getParameter("limit");
		
		
		StringBuffer queryString = new StringBuffer();
		
		String orderId = request.getParameter("orderId");

		if (orderId != null && !"".equals(orderId.trim())) {
			queryString.append(" where 1=1 and obj.orderId =" + orderId.trim());
		}
		QueryInfo queryInfo = new QueryInfo();
		// 设置每页显示多少行
		Integer pageCount = 15;
		pageCount = Integer.parseInt(limit);
		// 设定每页显示记录数
		queryInfo.setCountOnEachPage(pageCount);

		queryInfo.setCountQuery("select count(*) from CotOrderRemark obj "+ queryString.toString());
		queryInfo.setSelectString("select obj from CotOrderRemark obj ");
		queryInfo.setQueryString(queryString.toString());
		queryInfo.setOrderString(" order by obj.id desc");
		
		int startIndex = Integer.parseInt(start);	
		queryInfo.setStartIndex(startIndex);
		try {
			String json = this.getOrderService().getJsonData(queryInfo);
			response.getWriter().write(json);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
		
	}

	public CotOrderService getOrderService() {
		if (orderService == null) {
			orderService = (CotOrderService) super.getBean("CotOrderService");
		}
		return orderService;
	}

	public void setOrderService(CotOrderService orderService) {
		this.orderService = orderService;
	}

	public CotElementsService getCotElementsService() {
		if (cotElementsService == null) {
			cotElementsService = (CotElementsService) super.getBean("CotElementsService");
		}
		return cotElementsService;
	}

	public void setCotElementsService(CotElementsService cotElementsService) {
		this.cotElementsService = cotElementsService;
	}
}
