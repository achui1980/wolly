package com.sail.cot.servlet;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.log4j.Logger;

import com.sail.cot.util.Log4WebUtil;

/**
 * 文件上传类，
 * 并将上传的普通表单字段的值存入map中，map中key为form字段中name属性值
 * map存入request中，key为"FormField"
 * @author 钊^_^
 *
 */
public class ServletFUpload {
	private Logger logger = Log4WebUtil.getLogger(ServletFUpload.class);
	private DiskFileItemFactory factory;
	private ServletFileUpload upload;
	private long size_max = 100*1024*1024;
	private int size_thresholod = 5*1024*1024;
	private String header_encoding = "UTF-8";
	private String repository_path = "C:\\temp";
	public String formfield_key = "FormField_key";
	public String uploadFile_key = "uploadFile_key";
	
	@SuppressWarnings("unchecked")
	public boolean upload(HttpServletRequest request,String directoryPath){
		File dir=new File(directoryPath);
		if(!ServletFileUpload.isMultipartContent(request)){
			logger.warn("请求消息中的内容不是'multipart/form-data'类型");
			return false;
		}
		upload = this.getUpload();
		List<FileItem> items=null;
		try {
			items = upload.parseRequest(request);
			Map<String, String> map=new HashMap<String, String>();
			List<File> uploadFiles = new ArrayList<File>();
			for (FileItem item : items) {
				if (!item.isFormField()) {
					//item.getContentType();
					SimpleDateFormat sdf2 = new SimpleDateFormat(
							"yyyyMMddHHmmssSSS");
					String fileName = sdf2.format(new java.util.Date());
					String extName = item.getName();
					extName = extName.substring(extName.lastIndexOf("."),
							extName.length());
					fileName = fileName + extName;
					File uploadFile  = new File(dir.getAbsolutePath() + "/" + fileName);
					item.write(uploadFile);
					uploadFiles.add(uploadFile);
				}else{
					map.put(item.getFieldName(), item.getString("UTF-8"));
				}
			}
			request.setAttribute(uploadFile_key, uploadFiles);
			request.setAttribute(formfield_key, map);
		} catch (Exception e) {
			logger.error("保存文件失败",e);
			return false;
		}finally{
			for (FileItem item : items) {
				if(!item.isFormField()&&!item.isInMemory()){
					item.delete();
				}
			}
		}
		return true;
	}

	public DiskFileItemFactory getFactory() {
		if(factory==null){
			factory=new DiskFileItemFactory();
			factory.setSizeThreshold(size_thresholod);
			File dir = new File(repository_path);
			if(!dir.exists()){
				if(!dir.mkdir()){
					logger.warn("无法创建存储目录！");
					return null;
				}
			}
			factory.setRepository(dir);
			
		}
		return factory;
	}

	public void setFactory(DiskFileItemFactory factory) {
		this.factory = factory;
	}

	public ServletFileUpload getUpload() {
		if(upload==null){
			upload=new ServletFileUpload(this.getFactory());
			upload.setSizeMax(size_max);
			upload.setHeaderEncoding(header_encoding);
		}
		return upload;
	}

	public void setUpload(ServletFileUpload upload) {
		this.upload = upload;
	}

	public Logger getLogger() {
		return logger;
	}

	public void setLogger(Logger logger) {
		this.logger = logger;
	}

	public long getSize_max() {
		return size_max;
	}

	public void setSize_max(long size_max) {
		this.size_max = size_max;
	}

	public int getSize_thresholod() {
		return size_thresholod;
	}

	public void setSize_thresholod(int size_thresholod) {
		this.size_thresholod = size_thresholod;
	}

	public String getHeader_encoding() {
		return header_encoding;
	}

	public void setHeader_encoding(String header_encoding) {
		this.header_encoding = header_encoding;
	}

	public String getRepository_path() {
		return repository_path;
	}

	public void setRepository_path(String repository_path) {
		this.repository_path = repository_path;
	}

	public String getFormfield_key() {
		return formfield_key;
	}

	public void setFormfield_key(String formfield_key) {
		this.formfield_key = formfield_key;
	}

	public String getUploadFile_key() {
		return uploadFile_key;
	}

	public void setUploadFile_key(String uploadFile_key) {
		this.uploadFile_key = uploadFile_key;
	}
}
