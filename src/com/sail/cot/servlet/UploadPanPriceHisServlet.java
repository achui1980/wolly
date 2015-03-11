/**
 * 
 */
package com.sail.cot.servlet;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;

import com.bjinfotech.practice.ajax.Constant;
import com.bjinfotech.practice.ajax.MyServletFileUpload;
import com.bjinfotech.practice.ajax.Utils;
import com.jason.core.Application;
import com.sun.org.apache.bcel.internal.generic.NEW;

/**
 * <p>Title: 旗航外贸管理软件V8.0</p>
 * <p>Description:</p>
 * <p>Copyright: Copyright (c) 2011</p>
 * <p>Company: 厦门市旗航软件有限公司</p>
 * <p>Create Time: Dec 14, 2012 2:53:48 PM </p>
 * <p>Class Name: UploadPanPriceHisServlet.java </p>
 * @author achui
 *
 */
public class UploadPanPriceHisServlet extends HttpServlet {

	private static final String DOC_DIR = "/panelepricehis";
	/**
	 * The doGet method of the servlet. <br>
	 *
	 * This method is called when a form has its tag value method equals to get.
	 * 
	 * @param request the request send by the client to the server
	 * @param response the response send by the server to the client
	 * @throws ServletException if an error occurred
	 * @throws IOException if an error occurred
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

	}

	/**
	 * The doPost method of the servlet. <br>
	 *
	 * This method is called when a form has its tag value method equals to post.
	 * 
	 * @param request the request send by the client to the server
	 * @param response the response send by the server to the client
	 * @throws ServletException if an error occurred
	 * @throws IOException if an error occurred
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response){
		//返回一个json数据
		JSONObject json=new JSONObject();
		String flag=request.getParameter("flag");
		try {
			DiskFileItemFactory factory = new DiskFileItemFactory();
			//设置内存阀值，超过后写入临时文件
			factory.setSizeThreshold(Constant.UPLOAD_SIZE_THRESHOLD);
			//设置临时文件存储位置
			factory.setRepository(new File(request.getRealPath(DOC_DIR)));
			MyServletFileUpload upload = new MyServletFileUpload(factory);
			//设置单个文件的最大上传size:100M
			upload.setFileSizeMax(Constant.MAX_EACH_FILE_UPLOAD_SIZE);
			//设置整个request的最大size:100M
			upload.setSizeMax(Constant.MAX_ONCE_UPLOAD_SIZE);
			upload.setHeaderEncoding("UTF-8");
			SimpleDateFormat fm = new SimpleDateFormat("yyyyMMHHmmss");
			String subfix = fm.format(new Date(System.currentTimeMillis()));
			List<?> items = upload.parseRequest(request);
			if (items!=null){
				for(int i=0;i<items.size();i++){
					FileItem fileItem=(FileItem)items.get(i);
					if (fileItem.getName()!=null && fileItem.getName().length()>0){
						String fPath= request.getRealPath(DOC_DIR)+File.separator+subfix+"_"+fileItem.getName();
						File uploadedFile = new File(fPath);
						fileItem.write(uploadedFile);
						json.put("success", true);
						json.put("fileName", fileItem.getName());
						json.put("filePath", DOC_DIR+File.separator+subfix+"_"+fileItem.getName());
						json.put("msg", "Upload successful");
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			json.put("success", false);
			json.put("msg", "Upload error, the file may exceed the maximum!");
		}
		try {
			response.getWriter().write(json.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	/**
	 * Initialization of the servlet. <br>
	 *
	 * @throws ServletException if an error occurs
	 */
	public void init() throws ServletException {
		ServletContext ctx = this.getServletContext();
		String realPath = ctx.getRealPath(DOC_DIR);
		File fDir = new File(realPath);
		if(!fDir.exists())
			fDir.mkdir();		
	}
	
	private Object getService(String strSerivce){
		return Application.getInstance().getContainer().getComponent(strSerivce);
	}

}
