/*
 * Created on 2005-6-13
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.sail.cot.action;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONNull;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import net.sf.json.processors.DefaultValueProcessor;
import net.sf.json.processors.JsDateJsonBeanProcessor;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

import com.jason.core.Application;
import com.sail.cot.service.JsonFloatValueProcessor;


/**
 * Copyright: Copyright (c) 2005 Company: PTNetwork
 * 
 * @author zjs
 * @version 1.0 2005-6-13
 */
public abstract class AbstractAction extends DispatchAction {

    public abstract ActionForward add(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response);
   
    public abstract ActionForward modify(ActionMapping mapping,
            ActionForm form, HttpServletRequest request,
            HttpServletResponse response);

    public abstract ActionForward remove(ActionMapping mapping,
            ActionForm form, HttpServletRequest request,
            HttpServletResponse response);

    public abstract ActionForward query(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response);

    protected Object getBean(String name) {
        return Application.getInstance().getContainer().getComponent(name);
    }

    protected ActionForward processSuccess(ActionMapping mapping,
            HttpServletRequest request, String key, String path) {
        request.setAttribute("success",  key);
        if (!path.equals("")) {
            path = request.getContextPath()
//                    + RequestUtils.pageURL(request, path);
                      + path;
        }
        request.setAttribute("path", path);
        this.saveToken(request);
        return mapping.findForward("success");
    }

    protected ActionForward processFail(ActionMapping mapping,
            HttpServletRequest request, String key) {
        request.setAttribute("errorID", "");
        request.setAttribute("message", key);
        return mapping.findForward("failure");
    }
    
    public JSONObject getJsonObject(Object jsonStr){
    	JsDateJsonBeanProcessor beanProcessor = new JsDateJsonBeanProcessor();
		JsonFloatValueProcessor floatProcessor = new JsonFloatValueProcessor();
		JsonConfig config = new JsonConfig();
		config.registerJsonValueProcessor(Float.class, floatProcessor);
		config.registerJsonBeanProcessor(java.sql.Date.class, beanProcessor);
		config.registerDefaultValueProcessor(Integer.class,  
			    new DefaultValueProcessor(){  
			        public Object getDefaultValue(Class type){  
			            return JSONNull.getInstance();  
			        }  
			    });
		return JSONObject.fromObject(jsonStr,config);
    }
   
    
}
