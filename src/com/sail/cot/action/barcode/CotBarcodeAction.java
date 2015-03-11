package com.sail.cot.action.barcode;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.sail.cot.action.AbstractAction;
import com.sail.cot.domain.CotBarcode;
import com.sail.cot.domain.CotEmps;
import com.sail.cot.service.barcode.CotBarcodeService;
import com.sail.cot.util.GridServerHandler;

public class CotBarcodeAction extends AbstractAction {

	private CotBarcodeService cotBarcodeService;

	public CotBarcodeService getCotBarcodeService() {
		if (cotBarcodeService == null) {
			cotBarcodeService = (CotBarcodeService) super
					.getBean("CotBarcodeService");
		}
		return cotBarcodeService;
	}

	public void setCotBarcodeService(CotBarcodeService cotBarcodeService) {
		this.cotBarcodeService = cotBarcodeService;
	}

	@Override
	public ActionForward add(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		Integer empId = (Integer) request.getSession().getAttribute("empId");
		boolean chk = this.getCotBarcodeService().delBarcodeByAddEmp(empId);
		
		String json = request.getParameter("data");
		List list = new ArrayList();
		//判断数据是一条还是多条
		boolean single = json.startsWith("{");
		if(single){
			JSONObject jsonObject = JSONObject.fromObject(json);
			String eleId = (String) jsonObject.get("eleId");
			Integer count = (Integer) jsonObject.get("count");
			for (int i = 0; i < count; i++) {
				CotBarcode barcode = new CotBarcode();
				barcode.setEleId(eleId);
				barcode.setAddEmp(empId);
				barcode.setId(null);
				list.add(barcode);
			}
		}else{
			JSONArray jarray = JSONArray.fromObject(json);
			for (int i = 0; i < jarray.size(); i++) {
				JSONObject jsonObject = jarray.getJSONObject(i);
				String eleId = (String) jsonObject.get("eleId");
				Integer count = (Integer) jsonObject.get("count");
				for (int j = 0; j < count; j++) {
					CotBarcode barcode = new CotBarcode();
					barcode.setEleId(eleId);
					barcode.setAddEmp(empId);
					barcode.setId(null);
					list.add(barcode);
				}
			}
		}
		try {
			if(list.size()>0){
				this.getCotBarcodeService().addBarcode(list);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return null;
	}

	@Override
	public ActionForward modify(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {

		return null;
	}

	@Override
	public ActionForward query(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
//		// 获得登录人
//		CotEmps emp = (CotEmps) request.getSession().getAttribute("emp");
//		request.setAttribute("emp", emp);
		
		GridServerHandler gd = new GridServerHandler();
		gd.setData(null);
		gd.setTotalCount(0);
		String json = gd.getLoadResponseText();
		try {
			response.getWriter().write(json);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return mapping.findForward("querySuccess");
	}

	@Override
	public ActionForward remove(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {

		return null;
	}

}
