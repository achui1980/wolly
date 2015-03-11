package com.sail.cot.action.report;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.JRHtmlExporter;
import net.sf.jasperreports.engine.export.JRHtmlExporterParameter;
import net.sf.jasperreports.j2ee.servlets.ImageServlet;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.sail.cot.action.AbstractAction;
import com.sail.cot.service.sample.CotExportRptService;

public class ReportAction extends AbstractAction {

	private CotExportRptService rptService;

	@SuppressWarnings("deprecation")
	public ActionForward queryEleRpt(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		JasperPrint jasperPrint = (JasperPrint) request.getSession()
				.getAttribute("JasperPrint");
		//页面预览翻页是保留参数
		String queryAgain = request.getParameter("queryAgain");
		String headerFlag = request.getParameter("headerFlag");
		if(headerFlag == null)
			headerFlag = "false";

		if (queryAgain == null) {
			//获得查询条件
			String ids = request.getParameter("ids");// 编号字符串
			String eleId = request.getParameter("eleIdFind");// 样品编号
			String factoryId = request.getParameter("factoryIdFind");// 厂家
			String eleCol = request.getParameter("eleColFind");// 颜色
			String startTime = request.getParameter("startTime");// 起始时间
			String endTime = request.getParameter("endTime");// 结束时间
			String eleTypeidLv1 = request.getParameter("eleTypeidLv1Find");// 大类
			String eleGrade = request.getParameter("eleGradeFind");// 等级
			String eleForPerson = request.getParameter("eleForPersonFind");// 开发对象

			String boxLS = request.getParameter("boxLS");// 产品起始长
			String boxLE = request.getParameter("boxLE");// 产品结束长
			String boxWS = request.getParameter("boxWS");// 产品起始宽
			String boxWE = request.getParameter("boxWE");// 产品结束宽
			String boxHS = request.getParameter("boxHS");// 产品起始高
			String boxHE = request.getParameter("boxHE");// 产品结束高

			String reportTemple = request.getParameter("reportTemple");//导出模板
			String priceId = request.getParameter("priceId");//报价单预览时的主报价单编号
			String orderId = request.getParameter("orderId");//订单,生产合同,出货预览时的主订单编号
			String signId = request.getParameter("signId");//征样预览时的主订单编号
			String givenId = request.getParameter("givenId");//送样预览时的主订单编号
			String labelId = request.getParameter("labelId");//标签预览时的主单编号
			String addEmp = request.getParameter("addEmp");//条形码预览时的添加人Id
			String fitId = request.getParameter("fitId");//配件预览时的主单编号

			StringBuffer queryString = new StringBuffer();
			queryString.append(" 1=1");
			//----------------样品预览条件-----------------------------------
			if (ids != null && !ids.toString().trim().equals("")
					&& !ids.toString().trim().equals("all")) {
				String temp = ids.toString();
				queryString.append(" and ele.ID in("
						+ temp.substring(0, temp.length() - 1) + ")");
			}

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
			if (eleGrade != null && !eleGrade.toString().equals("")) {
				queryString.append(" and ele.ELE_GRADE like '%"
						+ eleGrade.toString().trim() + "%'");
			}

			if (eleForPerson != null && !eleForPerson.toString().equals("")) {
				queryString.append(" and ele.ELE_FOR_PERSON like '%"
						+ eleForPerson.toString().trim() + "%'");
			}

			if (startTime != null && !"".equals(startTime.trim())) {
				queryString.append(" and ele.ELE_PRO_TIME >='" + startTime
						+ "'");
			}
			if (endTime != null && !"".equals(endTime.trim())) {
				queryString.append(" and ele.ELE_PRO_TIME <='" + endTime
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
			//----------------样品预览条件-----------------------------------
			//----------------报价预览条件(固定报表中sql中的明细表的别名为obj)-----------------------------------
			if (priceId != null && !priceId.trim().equals("")) {
				queryString.append(" and obj.PRICE_ID=" + priceId);
			}
			if (orderId != null && !orderId.trim().equals("")) {
				queryString.append(" and obj.ORDER_ID=" + orderId);
			}
			if (signId != null && !signId.trim().equals("")) {
				queryString.append(" and obj.SIGN_ID=" + signId);
			}
			if (givenId != null && !givenId.trim().equals("")) {
				queryString.append(" and obj.GIVEN_ID=" + givenId);
			}
			if (labelId != null && !labelId.trim().equals("")) {
				queryString.append(" and obj.label_id=" + labelId);
			}
			if (addEmp != null && !addEmp.trim().equals("")) {
				queryString.append(" and obj.ADD_EMP=" + addEmp);
			}
			if (fitId != null && !fitId.trim().equals("")) {
				queryString.append(" and obj.id=" + fitId);
			}
			//----------------报价预览条件(固定报表中sql中的明细表的别名为obj)-----------------------------------

			//报表模块文件的位置
			String rptXMLpath = request.getRealPath("/") + reportTemple;
			//设置模板的参数(查询条件)
			HashMap<String, String> paramMap = new HashMap<String, String>();
			paramMap.put("IMG_PATH", request.getRealPath("/"));
			paramMap.put("STR_SQL", queryString.toString());
			paramMap.put("HEADER_PER_PAGE", headerFlag);
			jasperPrint = this.getRptService().getJasperPrint(rptXMLpath,
					paramMap);
			request.getSession().setAttribute("JasperPrint", jasperPrint);
			request.getSession().setAttribute(
					ImageServlet.DEFAULT_JASPER_PRINT_SESSION_ATTRIBUTE,
					jasperPrint);
		}

		StringBuffer sbuffer = new StringBuffer();
		JRHtmlExporter exporter = new JRHtmlExporter();

		int pageIndex = 0;
		int lastPageIndex = 0;
		if (jasperPrint.getPages() != null) {
			lastPageIndex = jasperPrint.getPages().size();
		}
		request.setAttribute("lastPageIndex", lastPageIndex);
		if (lastPageIndex == 0) {
			return mapping.findForward("previewSuccess");
		}
		String pageStr = request.getParameter("pageIndex");
		try {
			if (pageStr != null)
				pageIndex = Integer.parseInt(pageStr);
		} catch (Exception e) {
		}

		if (pageIndex < 0) {
			pageIndex = 0;
		}

		if (pageIndex > lastPageIndex) {
			pageIndex = lastPageIndex;
		}

		exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
		exporter
				.setParameter(JRExporterParameter.OUTPUT_STRING_BUFFER, sbuffer);
		exporter.setParameter(JRHtmlExporterParameter.IMAGES_URI,
				//"/CotSystem/servlets/image?image=");
				"servlets/image?image=");
		exporter.setParameter(JRExporterParameter.PAGE_INDEX, new Integer(
				pageIndex));

		exporter.setParameter(JRHtmlExporterParameter.HTML_HEADER, "");
		exporter.setParameter(JRHtmlExporterParameter.BETWEEN_PAGES_HTML, "");
		exporter.setParameter(JRHtmlExporterParameter.HTML_FOOTER, "");

		try {
			exporter.exportReport();
			request.setAttribute("preview", sbuffer.toString());
		} catch (JRException e) {
			e.printStackTrace();
		}
		
		//样品预览跳转
		return mapping.findForward("previewSuccess");
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
	 * @see com.sail.cot.action.AbstractAction#query(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	public ActionForward query(ActionMapping mapping, ActionForm form,
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

	public CotExportRptService getRptService() {
		if (rptService == null) {
			rptService = (CotExportRptService) super.getBean("CotRptService");
		}
		return rptService;
	}

	public void setRptService(CotExportRptService rptService) {
		this.rptService = rptService;
	}
}
