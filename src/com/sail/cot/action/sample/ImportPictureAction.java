package com.sail.cot.action.sample;
 

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
 

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping; 

import com.sail.cot.action.AbstractAction; 
import com.sail.cot.service.sample.ImportPictureService;
import com.sail.cot.util.ContextUtil;

public class ImportPictureAction extends AbstractAction {

	
	private ImportPictureService importPictureService;
	
	public ImportPictureService getImportPictureService() {
		if(importPictureService==null)
		{
			importPictureService=(ImportPictureService) super.getBean("ImportPictureService");
		}
		return importPictureService;
	}

	public void setImportPictureService(ImportPictureService importPictureService) {
		this.importPictureService = importPictureService;
	}

	private ContextUtil contextUtil;
	
	public ContextUtil getContextUtil() {
		return contextUtil;
	}

	public void setContextUtil(ContextUtil contextUtil) {
		this.contextUtil = contextUtil;
	}

	@Override
	public ActionForward add(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ActionForward modify(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ActionForward query(ActionMapping mapping, ActionForm form,
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

	 
	


	public ActionForward showBtn(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		int res=this.getImportPictureService().isExistFile();
		 
		request.setAttribute("res", res);
		 
		return mapping.findForward("importPicture");
	}
	
	
}
