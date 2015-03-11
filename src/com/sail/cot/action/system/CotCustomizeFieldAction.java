package com.sail.cot.action.system;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;



import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.sail.cot.action.AbstractAction;
import com.sail.cot.domain.CotEmps;
import com.sail.cot.query.QueryInfo;
import com.sail.cot.service.system.CotCompanyService;
import com.sail.cot.service.system.CotCustomizeService;
import com.sail.cot.util.SystemUtil;

public class CotCustomizeFieldAction extends AbstractAction {

	private CotCustomizeService cotCustomizeService;

	public CotCustomizeService getCotCustomizeService() {
		if (cotCustomizeService == null) {
			cotCustomizeService = (CotCustomizeService) super
					.getBean("CotCustomizeService");
		}
		return cotCustomizeService;
	}

	@Override
	public ActionForward add(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		 
		return mapping.findForward("add");
	}

	@Override
	public ActionForward modify(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		
		return mapping.findForward("modify");
	}

	@Override
	public ActionForward query(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		CotEmps emp = (CotEmps) request.getSession().getAttribute("emp");
		request.setAttribute("empsId", emp.getId());
		return mapping.findForward("querySuccess");	
	}

	@Override
	public ActionForward remove(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		 
		return null;
	}
	public ActionForward queryTree(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response){
		String nodeId=request.getParameter("node");
		System.out.println(nodeId);
		Map map=this.getCotCustomizeService().readXML();
		if(map.size()==0) return null;
		StringBuffer tree = new StringBuffer();
		if(nodeId.equals("root") == true){
			tree.append("[");
			Iterator it = map.entrySet().iterator(); 
			while (it.hasNext()) 
			{ 
			Map.Entry pairs = (Map.Entry)it.next(); 
			String id=(String) pairs.getKey();
			String title=(String) pairs.getValue();
			tree.append("{id:'");
			tree.append(id);
			tree.append("',text:'");
			tree.append(title);
			tree.append("',leaf:true},");
			} 
			int end =tree.toString().lastIndexOf(",");
			String body=(String) tree.toString().substring(0, end);
			body=body+"]";
			System.out.println(body);
			try {
				response.getWriter().write(body);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}else{
			
		}
		return null;
	}
	

	 
}
