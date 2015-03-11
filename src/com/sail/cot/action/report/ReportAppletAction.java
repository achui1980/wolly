/**
 * 
 */
package com.sail.cot.action.report;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.JasperPrint;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.sail.cot.action.AbstractAction;
import com.sail.cot.domain.CotCurrency;
import com.sail.cot.service.sample.CotExportRptService;
import com.sail.cot.util.SystemUtil;
import com.sail.cot.util.pdf.InService;
import com.sail.cot.util.pdf.PIService;
import com.sail.cot.util.pdf.POService;
import com.sail.cot.util.pdf.create.InPdf;
import com.sail.cot.util.pdf.create.PIPdf;
import com.sail.cot.util.pdf.create.POPdf;
import com.sail.cot.util.pdf.create.SSAPdf;
import com.sail.cot.util.pdf.impl.InServiceImpl;
import com.sail.cot.util.pdf.impl.PIServiceImpl;
import com.sail.cot.util.pdf.impl.POServiceImpl;

/**
 * <p>
 * Title: 旗行办公自动化系统（OA）
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2008
 * </p>
 * <p>
 * Company:
 * </p>
 * <p>
 * Create Time: Mar 13, 2009 10:59:27 AM
 * </p>
 * <p>
 * Class Name: ReportAppletAction.java
 * </p>
 * 
 * @author achui
 * 
 */
public class ReportAppletAction extends AbstractAction {

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sail.cot.action.AbstractAction#add(org.apache.struts.action.ActionMapping,
	 *      org.apache.struts.action.ActionForm,
	 *      javax.servlet.http.HttpServletRequest,
	 *      javax.servlet.http.HttpServletResponse)
	 */
	@Override
	public ActionForward add(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sail.cot.action.AbstractAction#modify(org.apache.struts.action.ActionMapping,
	 *      org.apache.struts.action.ActionForm,
	 *      javax.servlet.http.HttpServletRequest,
	 *      javax.servlet.http.HttpServletResponse)
	 */
	@Override
	public ActionForward modify(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sail.cot.action.AbstractAction#query(org.apache.struts.action.ActionMapping,
	 *      org.apache.struts.action.ActionForm,
	 *      javax.servlet.http.HttpServletRequest,
	 *      javax.servlet.http.HttpServletResponse)
	 */

	public ActionForward queryEleRpt(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		JasperPrint jasperPrint = null;
		// 页面预览翻页是保留参数
		String queryAgain = request.getParameter("queryAgain");
		String flag = request.getParameter("flag");
		String headerFlag = request.getParameter("headerFlag");
		String tbflag = request.getParameter("tbflag");
		if (headerFlag == null)
			headerFlag = "false";
		String printFlag = request.getParameter("printFlag");
		if (printFlag == null)
			printFlag = "1";
		if (queryAgain == null) {
			// 获得查询条件
			String ids = request.getParameter("ids");// 编号字符串
			String eleId = request.getParameter("eleIdFind");// 样品编号
			String child = request.getParameter("childFind");// 子货号标识
			String factoryId = request.getParameter("factoryIdFind");// 厂家
			String eleCol = request.getParameter("eleColFind");// 颜色
			String startTime = request.getParameter("startTime");// 起始时间
			String endTime = request.getParameter("endTime");// 结束时间
			String eleTypeidLv1 = request.getParameter("eleTypeidLv1Find");// 大类
			String eleTypeidLv2 = request.getParameter("eleTypeidLv2");// 中类
			String eleTypeidLv3 = request.getParameter("eleTypeidLv3");// 小类
			String eleGrade = request.getParameter("eleGradeFind");// 等级
			String eleForPerson = request.getParameter("eleForPersonFind");// 开发对象
			String eleHsid = request.getParameter("eleHsid");// 海关编码

			String boxLS = request.getParameter("boxLS");// 产品起始长
			String boxLE = request.getParameter("boxLE");// 产品结束长
			String boxWS = request.getParameter("boxWS");// 产品起始宽
			String boxWE = request.getParameter("boxWE");// 产品结束宽
			String boxHS = request.getParameter("boxHS");// 产品起始高
			String boxHE = request.getParameter("boxHE");// 产品结束高

			String reportTemple = request.getParameter("reportTemple");// 导出模板
			String priceId = request.getParameter("priceId");// 报价单预览时的主报价单编号
			String orderId = request.getParameter("orderId");// 订单预览时的主订单编号

			String signId = request.getParameter("signId");// 征样预览时的主订单编号
			String facId = request.getParameter("factoryId");// 厂家
			String gId = request.getParameter("gId");// 送样预览时的主订单编号

			String givenId = request.getParameter("givenId");// 送样预览时的主订单编号
			String splitId = request.getParameter("splitId");// 排载预览时的主排载编号
			String companyId = request.getParameter("companyId");// 排载预览时的主排载编号
			// String addEmp = request.getParameter("addEmp");//条形码预览时的添加人Id
			Integer addEmp = (Integer) request.getSession().getAttribute(
					"empId");
			String orderOutId = request.getParameter("orderOutId");// 出货单预览时的主单编号
			String orderFacId = request.getParameter("orderFacId");// 采购单预览时的主单编号
			String fitorderId = request.getParameter("fitorderId");// 配件采购单预览时的主单编号
			String packorderId = request.getParameter("packorderId");// 包材采购单预览时的主单编号

			String orPicId = request.getParameter("orPicId");// 订单包装图片单预览时的主单编号
			String panId = request.getParameter("panId");// 订单包装图片单预览时的主单编号
			String facsId = request.getParameter("facId");// 订单包装图片单预览时的主单编号
			String detailIds = request.getParameter("detailIds");// 询盘单编辑页面表格勾选的ids

			StringBuffer queryString = new StringBuffer();
			queryString.append(" 1=1");
			// ----------------样品预览条件-----------------------------------
			if (ids != null && !ids.toString().trim().equals("")
					&& !ids.toString().trim().equals("all")) {
				String temp = ids.toString();
				queryString.append(" and ele.ID in("
						+ temp.substring(0, temp.length() - 1) + ")");
			}

			else {
				if (child != null && child.toString().trim().equals("true")) {
					queryString.append(" and ele.ELE_FLAG=2");
				} else if (child != null && child.equals("none")) {

				}
				if (tbflag != null && tbflag.equals("sample")) {
					if (child != null && child.toString().trim().equals("true")) {
						queryString.append(" and ele.ELE_FLAG=2");
					} else
						queryString.append(" and ele.ELE_FLAG !=2");
				}
				// else
				// queryString.append(" and obj.ELE_FLAG!=2");

				if (eleId != null && !eleId.toString().trim().equals("")) {
					queryString.append(" and ele.ELE_ID like '%"
							+ eleId.toString().trim() + "%'");
				}
				if (factoryId != null && !factoryId.toString().equals("")) {
					queryString.append(" and ele.FACTORY_ID="
							+ factoryId.toString());
				}
				if (eleCol != null && !eleCol.toString().trim().equals("")) {
					queryString.append(" and ele.ELE_COL like '%"
							+ eleCol.toString().trim() + "%'");
				}
				if (eleTypeidLv1 != null && !eleTypeidLv1.toString().equals("")) {
					queryString.append(" and ele.ELE_TYPEID_LV1="
							+ eleTypeidLv1.toString());
				}
				if (eleTypeidLv2 != null && !eleTypeidLv2.toString().equals("")) {
					queryString.append(" and ele.ELE_TYPEID_LV2="
							+ eleTypeidLv2.toString());
				}
				if (eleTypeidLv3 != null && !eleTypeidLv3.toString().equals("")) {
					queryString.append(" and ele.ELE_TYPEID_LV3="
							+ eleTypeidLv3.toString());
				}
				if (eleHsid != null && !eleHsid.toString().equals("")) {
					queryString.append(" and ele.HS_ID=" + eleHsid.toString());
				}
				if (eleGrade != null && !eleGrade.toString().equals("")) {
					queryString.append(" and ele.ELE_GRADE like '%"
							+ eleGrade.toString().trim() + "%'");
				}
				if (eleForPerson != null && !eleForPerson.toString().equals("")) {
					queryString.append(" and ele.ELE_FOR_PERSON like '%"
							+ eleForPerson.toString().trim() + "%'");
				}
				if (startTime != null && !"".equals(startTime.trim())) {
					queryString.append(" and ele.ELE_ADD_TIME >='" + startTime
							+ "'");
				}
				if (endTime != null && !"".equals(endTime.trim())) {
					queryString.append(" and ele.ELE_ADD_TIME <='" + endTime
							+ " 23:59:59'");
				}
				if (boxLS != null && boxLE != null) {
					if (!"".equals(boxLS.trim()) && !"".equals(boxLE.trim())) {
						queryString.append(" and ele.BOX_L between " + boxLS
								+ " and " + boxLE);
					}
				}
				if (boxWS != null && boxWE != null) {
					if (!"".equals(boxWS.trim()) && !"".equals(boxWE.trim())) {
						queryString.append(" and ele.BOX_W between " + boxWS
								+ " and " + boxWE);
					}
				}
				if (boxHS != null && boxHE != null) {
					if (!"".equals(boxHS.trim()) && !"".equals(boxHE.trim())) {
						queryString.append(" and ele.BOX_H between " + boxHS
								+ " and " + boxHE);
					}
				}
			}
			// ----------------样品预览条件-----------------------------------
			// ----------------报价预览条件(固定报表中sql中的明细表的别名为obj)-----------------------------------
			String id = null;
			if (priceId != null && !priceId.trim().equals("")) {
				queryString.append(" and obj.PRICE_ID=" + priceId);
				id = priceId;
			}
			if (orderId != null && !orderId.trim().equals("")) {
				queryString.append(" and obj.ORDER_ID=" + orderId);
				id = orderId;
			}
			// if(signId!=null && !signId.trim().equals("")){
			// queryString.append(" and obj.ID="+signId);
			// }
			if (gId != null && !gId.trim().equals("")) {
				queryString.append(" and obj.GIVEN_ID=" + gId);
			}
			if (facId != null && !facId.trim().equals("")) {
				queryString.append(" and obj.FACTORY_ID=" + facId);
			}
			if (companyId != null && !companyId.trim().equals("")) {
				queryString.append(" and p.COMPANY_ID=" + companyId);
			}
			if (givenId != null && !givenId.trim().equals("")) {
				queryString.append(" and obj.GIVEN_ID=" + givenId);
			}
			if (splitId != null && !splitId.trim().equals("")) {
				queryString.append(" and obj.SPLIT_ID=" + splitId);
			}
			if (addEmp != null && "barcode".equals(headerFlag)) {
				queryString.append(" and obj.ADD_EMP=" + addEmp);
			}
			if (orderOutId != null && !orderOutId.trim().equals("")) {
				queryString.append(" and obj.ORDER_ID=" + orderOutId);
				id = orderOutId;
			}
			if (orderFacId != null && !orderFacId.trim().equals("")) {
				queryString.append(" and obj.ID=" + orderFacId);
				id = orderFacId;
			}
			if (fitorderId != null && !fitorderId.trim().equals("")) {
				queryString.append(" and obj.order_id=" + fitorderId);
			}
			if (packorderId != null && !packorderId.trim().equals("")) {
				queryString.append(" and obj.packing_orderId=" + packorderId);
			}
			if (orPicId != null && !orPicId.trim().equals("")) {
				queryString.append(" and obj.id=" + orPicId);
			}
			if (panId != null && !panId.trim().equals("")) {
				queryString.append(" and obj.panId=" + panId);
			}
			if (detailIds != null && !detailIds.trim().equals("")) {
				queryString.append(" and e.id in (" + detailIds+")");
			}
			if (facsId != null && !facsId.trim().equals("")) {
				queryString.append(" and obj.factoryId=" + facsId);
			}
			// ----------------报价预览条件(固定报表中sql中的明细表的别名为obj)-----------------------------------

			// 报表模块文件的位置
			String rptXMLpath = request.getRealPath("/") + reportTemple;
			// String exportPath =
			// request.getRealPath("/reportfile")+File.separator +
			// "elements_nopic.xls";
			// 设置模板的参数(查询条件)
			HashMap<String, String> paramMap = new HashMap<String, String>();
			paramMap.put("IMG_PATH", request.getRealPath("/"));
			paramMap.put("STR_SQL", queryString.toString());
			if (startTime != null && !"".equals(startTime)) {
				paramMap.put("startTime", startTime.toString());
			}
			if (endTime != null && !"".equals(endTime)) {
				paramMap.put("endTime", endTime.toString());
			}
			paramMap.put("HEADER_PER_PAGE", headerFlag);
			String sql = queryString.toString();
			System.out.println("STR_SQL:" + queryString.toString());
			String customPdf = request.getParameter("custom");// 自定义报表
			if (customPdf == null) {

				jasperPrint = this.getRptService().getJasperPrint(rptXMLpath,
						paramMap);
				String rndFlag = RandomStringUtils.randomAlphabetic(10);
				request.getSession().setAttribute("JasperPrint" + rndFlag,
						jasperPrint);
				request.setAttribute("rndFlag", rndFlag);
				request.setAttribute("printFlag", printFlag);
			} else {
				try {

					if (customPdf.equals("1")) {
						String isReport = request.getParameter("isReport");
						if ("true".equals(isReport)) {
							SSAPdf ssaPdf = new SSAPdf();
							POService poService = new POServiceImpl();
							ssaPdf.setVpOrder(poService.getCotPOVO(Integer
									.parseInt(orderId)));
							ssaPdf.setDetailList(poService
									.getDetailList(Integer.parseInt(orderId)));
							ssaPdf.createPIPDF(response, request);
						} else {
							PIPdf piPdf = new PIPdf();
							PIService piService = new PIServiceImpl();
							piPdf.setVpInvoice(piService.getCotPIVO(Integer
									.parseInt(orderId)));
							piPdf.setDetailList(piService.getDetailList(Integer
									.parseInt(orderId)));
							piPdf.createPIPDF(response, request);
						}

					} else if (customPdf.equals("2")) {
						POPdf poPdf = new POPdf();
						POService poService = new POServiceImpl();

						poPdf.setvpOrder(poService.getCotPOVO(Integer
								.parseInt(orderId)));
						poPdf.setDetailList(poService.getDetailList(Integer
								.parseInt(orderId)));
						poPdf.createPOPDF(response, request);
					} else if (customPdf.equals("3")) {
						InPdf inPdf = new InPdf();
						InService inService = new InServiceImpl();

						inPdf.setvInvoice(inService.getCotInVO(Integer
								.parseInt(orderId), true));
						inPdf.setDetailList(inService.getDetailList(Integer
								.parseInt(orderId), true));
						
						//判断出货主单或作废主单的公司是否是wollly 如果是则返回true
						boolean chk=inService.isWolly(Integer.parseInt(orderId),true);
						inPdf.createInPDF(response, request, true,chk);
					} else if (customPdf.equals("4")) {
						InPdf inPdf = new InPdf();
						InService inService = new InServiceImpl();
						inPdf.setHeaderTitle("Credit Note");

						inPdf.setvInvoice(inService.getCotInVO(Integer
								.parseInt(orderId), false));
						inPdf.setDetailList(inService.getDetailList(Integer
								.parseInt(orderId), false));
						//判断出货主单或作废主单的公司是否是wollly 如果是则返回true
						boolean chk=inService.isWolly(Integer.parseInt(orderId),false);
						inPdf.createInPDF(response, request, true,chk);
					}
					// 3,in,4,c
					return null;
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			String rndFlag = RandomStringUtils.randomAlphabetic(10);
			request.getSession().setAttribute("JasperPrint" + rndFlag,
					jasperPrint);
			request.setAttribute("rndFlag", rndFlag);
			request.setAttribute("printFlag", printFlag);

			// sql = sql.replaceAll("=", "_E_").replaceAll(" ", "_S_");
			// System.out.println("___SQL:"+sql);
			// request.setAttribute("strSql", sql);
			// request.setAttribute("flag", flag);
			// reportTemple = reportTemple.replaceAll("/", "_Anti_");
			// request.setAttribute("reportTemple", reportTemple);
			// request.setAttribute("headerFlag", headerFlag);

		}

		return mapping.findForward("queryApplet");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sail.cot.action.AbstractAction#remove(org.apache.struts.action.ActionMapping,
	 *      org.apache.struts.action.ActionForm,
	 *      javax.servlet.http.HttpServletRequest,
	 *      javax.servlet.http.HttpServletResponse)
	 */
	@Override
	public ActionForward remove(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sail.cot.action.AbstractAction#query(org.apache.struts.action.ActionMapping,
	 *      org.apache.struts.action.ActionForm,
	 *      javax.servlet.http.HttpServletRequest,
	 *      javax.servlet.http.HttpServletResponse)
	 */
	@Override
	public ActionForward query(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		JasperPrint jasperPrint = null;
		String printFlag = request.getParameter("printFlag");
		String reportTemple = request.getParameter("reportTemple");// 导出模板
		Enumeration<String> enumeration = request.getParameterNames();
		HashMap<String, String> paramMap = new HashMap<String, String>();
		while (enumeration.hasMoreElements()) {
			String name = enumeration.nextElement();
			if ("method".equals(name) || "random".equals(name)
					|| "reportTemple".equals(name))
				continue;
			String value = request.getParameter(name);
			if (value != null && !"".equals(value)) {
				if (name.toUpperCase().equals("ENDTIME") || name.toUpperCase().equals("COMPAREFROMEND") || name.toUpperCase().equals("COMPARETOEND")) {
					value += " 23:59:59";
				}
				System.out.println("-----name:value-----:" + name.toUpperCase()
						+ ":" + value);
				paramMap.put(name.toUpperCase(), value);
			}
		}
		// 报表模块文件的位置
		String rptXMLpath = request.getRealPath("/") + reportTemple;
		// 设置模板的参数(查询条件)

		paramMap.put("IMG_PATH", request.getRealPath("/"));

		jasperPrint = this.getRptService().getJasperPrint(rptXMLpath, paramMap);
		String rndFlag = RandomStringUtils.randomAlphabetic(10);
		request.getSession().setAttribute("JasperPrint" + rndFlag, jasperPrint);
		request.setAttribute("rndFlag", rndFlag);
		request.setAttribute("printFlag", printFlag);

		return mapping.findForward("queryApplet");
	}

	private CotExportRptService rptService;

	public CotExportRptService getRptService() {
		if (rptService == null) {
			rptService = (CotExportRptService) SystemUtil
					.getService("CotRptService");
		}
		return rptService;
	}

	public void setRptService(CotExportRptService rptService) {
		this.rptService = rptService;
	}

	public ActionForward queryPrsRpt(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		JasperPrint jasperPrint = null;
		// 页面预览翻页是保留参数
		String queryAgain = request.getParameter("queryAgain");
		String headerFlag = request.getParameter("headerFlag");
		if (headerFlag == null)
			headerFlag = "false";
		String printFlag = request.getParameter("printFlag");
		if (printFlag == null)
			printFlag = "1";
		if (queryAgain == null) {
			// 获得查询条件
			String ids = request.getParameter("ids");// 编号字符串
			String priceNo = request.getParameter("priceNo");
			String eleId = request.getParameter("eleId");
			String startTime = request.getParameter("startTime");
			String endTime = request.getParameter("endTime");
			String eleNameEn = request.getParameter("eleNameEn");
			String quality = request.getParameter("quality");
			String size = request.getParameter("size");
			String weight = request.getParameter("weight");
			String filling = request.getParameter("filling");
			String construction = request.getParameter("construction");
			String packing = request.getParameter("packing");
			String boxObcount = request.getParameter("boxObcount");
			String productTime = request.getParameter("productTime");
			String factoryId = request.getParameter("factoryIdFind");
			String groupId = request.getParameter("groupId");
			String category = request.getParameter("category");

			String reportTemple = request.getParameter("reportTemple");// 导出模板

			StringBuffer queryString = new StringBuffer();
			queryString.append(" obj.price is not null and obj.price!=0");
			// ----------------样品预览条件-----------------------------------
			if (ids != null && !ids.toString().trim().equals("")
					&& !ids.toString().trim().equals("all")) {
				String temp = ids.toString();
				queryString.append(" and obj.id in("
						+ temp.substring(0, temp.length() - 1) + ")");
			} else {
				if (priceNo != null && !priceNo.toString().trim().equals("")) {
					queryString.append(" and p.priceNo like '%"
							+ priceNo.toString().trim() + "%'");
				}
				if (eleId != null && !eleId.toString().trim().equals("")) {
					queryString.append(" and e.eleId like '%"
							+ eleId.toString().trim() + "%'");
				}
				if (startTime != null && !"".equals(startTime.trim())) {
					queryString.append(" and obj.valDate >='" + startTime + "'");
				}
				if (endTime != null && !"".equals(endTime.trim())) {
					queryString.append(" and obj.valDate <='" + endTime
							+ " 23:59:59'");
				}
				if (eleNameEn != null && !eleNameEn.toString().trim().equals("")) {
					queryString.append(" and e.eleNameEn like '%"
							+ eleNameEn.toString().trim() + "%'");
				}
				if (quality != null && !quality.toString().trim().equals("")) {
					queryString.append(" and e.quality like '%"
							+ quality.toString().trim() + "%'");
				}
				if (size != null && !size.toString().trim().equals("")) {
					queryString.append(" and e.size like '%"
							+ size.toString().trim() + "%'");
				}
				if (weight != null && !weight.toString().trim().equals("")) {
					queryString.append(" and obj.weight like '%"
							+ weight.toString().trim() + "%'");
				}
				if (filling != null && !filling.toString().trim().equals("")) {
					queryString.append(" and obj.filling like '%"
							+ filling.toString().trim() + "%'");
				}
				if (construction != null
						&& !construction.toString().trim().equals("")) {
					queryString.append(" and obj.construction like '%"
							+ construction.toString().trim() + "%'");
				}
				if (packing != null && !packing.toString().trim().equals("")) {
					queryString.append(" and obj.packing like '%"
							+ packing.toString().trim() + "%'");
				}
				if (boxObcount != null
						&& !boxObcount.toString().trim().equals("")) {
					queryString.append(" and obj.boxObcount="
							+ boxObcount.toString().trim());
				}
				if (productTime != null
						&& !productTime.toString().trim().equals("")) {
					queryString.append(" and obj.productTime like '%"
							+ productTime.toString().trim() + "%'");
				}
				if (factoryId != null && !factoryId.toString().equals("")) {
					queryString.append(" and obj.factoryId="
							+ factoryId.toString());
				}
				if (groupId != null && !groupId.toString().equals("")) {
					queryString
							.append(" and e.groupId=" + groupId.toString());
				}
				if (category != null && !category.toString().equals("")) {
					queryString.append(" and e.category="
							+ category.toString());
				}
			}
			// 报表模块文件的位置
			String rptXMLpath = request.getRealPath("/") + reportTemple;
			// 设置模板的参数(查询条件)
			HashMap<String, String> paramMap = new HashMap<String, String>();
			paramMap.put("IMG_PATH", request.getRealPath("/"));
			paramMap.put("STR_SQL", queryString.toString());
			paramMap.put("HEADER_PER_PAGE", headerFlag);

			jasperPrint = this.getRptService().getJasperPrint(rptXMLpath,
					paramMap);
			String rndFlag = RandomStringUtils.randomAlphabetic(10);
			request.getSession().setAttribute("JasperPrint" + rndFlag,
					jasperPrint);
			request.setAttribute("rndFlag", rndFlag);
			request.setAttribute("printFlag", printFlag);

		}

		return mapping.findForward("queryApplet");
	}

}
