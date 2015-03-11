package com.sail.cot.util;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONNull;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import net.sf.json.processors.DefaultValueProcessor;
import net.sf.json.processors.JsDateJsonBeanProcessor;

import com.sail.cot.service.JsonFloatValueProcessor;

/**
 * @author fins
 * 
 */
public class GridServerHandler {

	public static String CONTENTTYPE = "text/html; charset=UTF-8";
	public static String GT_JSON_NAME = "_gt_json";
	public static String DATA_ROOT = "data";
		
		private String action;
		private String exception;

		private List data;
		private String recordType;
		private String encoding=null;
		
		private boolean success;
		
		private HttpServletRequest request;
		private HttpServletResponse response;

		private JSONObject jsonObject;
		
		private Class dataBeanClass=null;
		private JSONArray jsonData =null;
		
		private Map parameters = new HashMap();
		
		private Map parameterMap;
		
		private Map writers = new HashMap();
		private int totalCount = 0;
		private String[] exclude = null;
		
		private  JsonConfig config = new JsonConfig();
		
		public int getTotalCount() {
			return totalCount;
		}
		
		public void setTotalCount(int totalCount) {
			this.totalCount = totalCount;
		}

		public GridServerHandler(){
			JsDateJsonBeanProcessor beanProcessor = new JsDateJsonBeanProcessor();
			JsonFloatValueProcessor floatProcessor = new JsonFloatValueProcessor();
			config.registerJsonValueProcessor(Float.class, floatProcessor);
			config.registerJsonBeanProcessor(java.sql.Date.class, beanProcessor);
			config.registerDefaultValueProcessor(Integer.class,  
				    new DefaultValueProcessor(){  
				        public Object getDefaultValue(Class type){  
				            return JSONNull.getInstance();  
				        }  
				    });  
		}
		
		public GridServerHandler(String gtJson){
			init(gtJson);
		}
		public GridServerHandler(String[] excludes){
			JsDateJsonBeanProcessor beanProcessor = new JsDateJsonBeanProcessor();
			JsonFloatValueProcessor floatProcessor = new JsonFloatValueProcessor();
			config.registerJsonBeanProcessor(java.sql.Date.class, beanProcessor);
			config.registerJsonValueProcessor(float.class, floatProcessor);
			//过滤某些属性，不生成json数据
			config.setExcludes(excludes);
		}
		public GridServerHandler(Map parameterMap){
			setParameterMap(parameterMap);
			init();
		}
		
		
		public GridServerHandler(HttpServletRequest request,HttpServletResponse response) {
			setRequest(request);
			setResponse(response);
			//init();
		}

		public void init(String gtJson) {
		}
		
		public void init() {
			init(getParameter(GT_JSON_NAME));
		}

		public String getSaveResponseText(){
			JSONObject json=new JSONObject();
			try {
				json.put("success", success);
				json.put("exception", exception);
			} catch (JSONException e) {
				//LogHandler.error(this,e);
			}
			return json.toString();
		}

		public JSONObject getLoadResponseJSON(){
			JSONObject json=new JSONObject();
			try {
				json.put(DATA_ROOT, jsonData);
				json.put("totalCount", this.getTotalCount());
				json.put("exception", exception);
			} catch (JSONException e) {
				//LogHandler.error(this,e);
			}
			return json;
		}
		public String getLoadResponseText(){
			JSONObject json=getLoadResponseJSON();
			String jstr=json==null?"":json.toString();
			//LogHandler.debug(" AJAX OUT : "+jstr);
			System.out.println("AJAX OUT:"+jstr);
			return jstr;
		}
	

		public void setData(List data) {
			this.data = data;
			this.dataBeanClass=null;
			setJsonData(JSONArray.fromObject(data,config));
			
		}

		public String[] getParameterValues(String name){
			return (String[])parameterMap.get(name);
		}
		public String getParameter(String name){
			String[] pv=getParameterValues(name);
			if (pv!=null && pv.length>0){
				return pv[0];
			}
			return null;
		}
		
		public void printResponseText(String text){
			try {
				response.setContentType(CONTENTTYPE);
				PrintWriter out=response.getWriter();
				out.println(text);
				out.flush();
				out.close();
			} catch (IOException e) {
				//LogHandler.error(this,e);
			}	
		}


		
		public void printSaveResponseText(){
			printResponseText(getSaveResponseText());
		}
	
		public void printLoadResponseText(){
			printResponseText(getLoadResponseText());
		}
		
		public Map getParameters() {
			return parameters;
		}

		public void setParameters(Map parameters) {
			this.parameters = parameters;
		}

		public String getException() {
			return exception;
		}

		public void setException(String exception) {
			this.exception = exception;
		}

		
		public List getData() {
			return data;
		}

		public String getRecordType() {
			return recordType;
		}


		public boolean isSuccess() {
			return success;
		}

		public void setSuccess(boolean success) {
			this.success = success;
		}

		public HttpServletRequest getRequest() {
			return request;
		}

		public void setRequest(HttpServletRequest request) {
			this.request = request;
			setParameterMap(request.getParameterMap());
		}

		public HttpServletResponse getResponse() {
			return response;
		}

		public void setResponse(HttpServletResponse response) {
			this.response = response;
		}
		public JSONObject getJsonObject() {
			return jsonObject;
		}
		public void setJsonObject(JSONObject jsonObject) {
			this.jsonObject = jsonObject;
		}
		public Class getDataBeanClass() {
			return dataBeanClass;
		}
		public void setDataBeanClass(Class dataBeanClass) {
			this.dataBeanClass = dataBeanClass;
		}
		public JSONArray getJsonData() {
			return jsonData;
		}
		public void setJsonData(JSONArray jsonData) {
			this.jsonData = jsonData;
		}

		public Map getParameterMap() {
			return parameterMap;
		}

		public void setParameterMap(Map parameterMap) {
			this.parameterMap = parameterMap;
		}
		public static int getInt(Object i){
			return getInt(i,-1);
		}
		public static int getInt(Object i,int defaultI){
			try {
				if (i!=null){
					return Integer.parseInt(String.valueOf(i));
				}
			} catch (Exception e) {	}
			return defaultI;
		}

		public String getEncoding() {
			return encoding;
		}

		public void setEncoding(String encoding) {
			this.encoding = encoding;
		}
		
		public Object json2Bean(String json,Class clzz){
			JSONObject jsonObject = JSONObject.fromObject( json );   
			Object obj  = jsonObject.toBean(jsonObject, clzz);
			return obj;
		}	
		public List json2BeanList(String json,Class clzz)
		{
			JSONArray jarray = JSONArray.fromObject(json);
			List list = jarray.toList(jarray, clzz);
			return list;
		}
		
		//flag为true时,清空id
		public List jsonToBeans(String json,Class clzz,boolean flag)
		{
			try {
				JSONObject jsonObject = JSONObject.fromObject( json ); 
				Object obj  = jsonObject.toBean(jsonObject, clzz);
				List list = new ArrayList();
				list.add(obj);
				return list;
			} catch (Exception e) {
				JSONArray jarray = JSONArray.fromObject(json);
				for (Object o : jarray) {
					JSONObject jsonObj = JSONObject.fromObject(o);
					if(flag)
						jsonObj.put("id", null);
				}
				List list = jarray.toList(jarray, clzz);
				return list;
			}
			
		}

}
