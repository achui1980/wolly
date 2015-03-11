package com.sail.cot.servlet;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import net.sf.json.JSONObject;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.log4j.Logger;

import com.sail.cot.domain.CotEmps;
import com.sail.cot.domain.CotMailAttach;
import com.sail.cot.email.util.Constants;
import com.sail.cot.email.util.MailLocalUtil;
import com.sail.cot.util.Log4WebUtil;
import com.sail.cot.util.SystemUtil;

@SuppressWarnings("serial")
public class UploadMailFileServlet extends BasicServlet{
	private Logger logger = Log4WebUtil.getLogger(UploadMailFileServlet.class);
	/**
	 * 上传文件方法
	 */
	@SuppressWarnings("unchecked")
	public void uploadFile(){
		logger.debug("上传邮件附件");
		HttpSession session=request.getSession();
		String random=request.getParameter("date");
		try {
			CotEmps cotEmps = (CotEmps) session.getAttribute("emp");
			String dirName = cotEmps.getEmpsId();
			request.setCharacterEncoding("UTF-8");
			FileItemFactory factory = new DiskFileItemFactory();
			ServletFileUpload upload = new ServletFileUpload(factory);
			String path =MailLocalUtil.getProPath()+Constants.MAIL_ATTACH_UPLOAD_PATH;
			List<FileItem> items = (List<FileItem>) upload.parseRequest(request);
			if(this.uploadFileExists(dirName, path)==false)
				return;
	    	File dir = new File(path+"/"+dirName);
			
			Iterator<FileItem> iter = items.iterator();
			while (iter.hasNext()) {
				FileItem fileItem = (FileItem) iter.next();
				if (fileItem.isFormField()) {
				} else {
					String value=fileItem.getName();
					int start=value.lastIndexOf("/");
					String fileName = value.substring(start+1);
					String tempFileName = RandomStringUtils.randomAlphanumeric(20);
					File uploadedFile = new File(dir.getAbsolutePath() , tempFileName);
					fileItem.write(uploadedFile);
					CotMailAttach attach = new CotMailAttach();
					attach.setName(fileName);
					attach.setUrl(Constants.MAIL_ATTACH_UPLOAD_PATH+"/"+dirName+"/"+tempFileName);
					Long size = uploadedFile.length();
					attach.setSize(size.intValue());
					response.getWriter().write("Success");	
					this.attachToSession(attach, session, fileName, random);
				}
			}
			
		} catch (Exception e) {
			logger.error("上传邮件附件失败："+e.getMessage(),e);
		}
	}
	/**
	 * 将附件信息保存到session中
	 * @param attach
	 * @param session
	 * @param fileName
	 * @param random 前后传进来的随机数，以防止不同发送页面存在同一个key中
	 */
	@SuppressWarnings("unchecked")
	private void attachToSession(CotMailAttach attach, HttpSession session,
			String fileName, String random) {
		Map<String, CotMailAttach> map;
		
		fileName = fileName.replaceAll(Constants.MAIL_SEND_ATTACH_UPLOAD_RG, "");
		if(session.getAttribute("map"+random)!=null){
			if(random.equals((String)session.getAttribute("random"+random))){
				int nameIndex = (Integer) session.getAttribute("nameIndex"+random);
				nameIndex++;
				map= (Map<String, CotMailAttach>) session.getAttribute("map"+random);
				map.put(fileName+"*"+nameIndex, attach);	
				session.setAttribute("nameIndex"+random, nameIndex);
			}else{			
				map =new HashMap<String,CotMailAttach>();
				map.put(fileName+"*"+1, attach);
				session.setAttribute("random"+random, random);
				session.setAttribute("nameIndex"+random, 1);
			}	
		}else{
			map = new HashMap<String,CotMailAttach>(); 
			map.put(fileName+"*"+1, attach);
			session.setAttribute("map"+random, map);
			session.setAttribute("random"+random, random);
			session.setAttribute("nameIndex"+random, 1); // 该参数只会一直增长与前台发送页面的附件面板一致
		}
	}
	/**
	 * 判断上传目录是否存在
	 * @param dirName
	 * @param path
	 * @return
	 */
	private boolean uploadFileExists(String dirName, String path) {
		String[] dirs = dirName.split("/");
		String dirPath ="";
		for(int i=0;i<dirs.length;i++){
			dirPath += "/"+dirs[i];
			File dir=new File(path+dirPath);  
			if(!dir.exists()){
				if(!dir.mkdir()){
					logger.warn("无法创建存储目录！");
					return false;
				}
			}
		}
		return true;
	}
	/**
	 * 删除文件
	 * @throws UnsupportedEncodingException 
	 * @throws IOException 
	 */
	@SuppressWarnings("unchecked")
	public void deleteFile() throws IOException{
		logger.debug("删除邮件附件");
		String name=request.getParameter("name");
		String random=request.getParameter("random");
		String filename=URLDecoder.decode(name,"UTF-8");
		
		HttpSession session=request.getSession();
		if(session.getAttribute("map"+random)!=null){
			Map<String,CotMailAttach> map=(Map<String, CotMailAttach>) session.getAttribute("map"+random);
			String url = map.get(filename).getUrl();
			SystemUtil.deleteRealFile(MailLocalUtil.getProPath()+url);
			map.remove(filename);
			response.getWriter().write("Success");
		}
	}
	/**
	 * 获得文件路径
	 * @throws IOException 
	 */
	@SuppressWarnings("unchecked")
	public void getFileUrl() throws IOException{
		logger.debug("获得邮件附件");
		String name=request.getParameter("name");
		String random=request.getParameter("random");
		String filename=URLDecoder.decode(name,"UTF-8");
		HttpSession session=request.getSession();
		if(session.getAttribute("map"+random)!=null){
			Map<String,CotMailAttach> map=(Map<String, CotMailAttach>) session.getAttribute("map"+random);
			CotMailAttach mailAttach = map.get(filename);
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("path", mailAttach.getUrl());
			jsonObject.put("name", mailAttach.getName());
			response.getWriter().write(jsonObject.toString());
		}
	}
}
