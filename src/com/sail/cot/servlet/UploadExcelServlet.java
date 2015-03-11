package com.sail.cot.servlet;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.servlet.Servlet;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.log4j.Logger;

import com.bjinfotech.practice.ajax.Constant;
import com.bjinfotech.practice.ajax.MyServletFileUpload;
import com.bjinfotech.practice.ajax.Utils;
import com.jason.core.Application;
import com.sail.cot.util.Log4WebUtil;


public class UploadExcelServlet extends HttpServlet implements Servlet {

	private static final long serialVersionUID = 946573126457064359L;
	//private static final String DOC_DIR = "/sampleImg";
	Logger logger = Log4WebUtil.getLogger(UploadExcelServlet.class);
	
	public UploadExcelServlet() {
		super();
	}

	public void destroy() {
		super.destroy(); // Just puts "destroy" string in log
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response){
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response){
		//返回一个json数据
		JSONObject json=new JSONObject();
		try {
			DiskFileItemFactory factory = new DiskFileItemFactory();
			//设置内存阀值，超过后写入临时文件
			factory.setSizeThreshold(Constant.UPLOAD_SIZE_THRESHOLD);
			//设置临时文件存储位置
			factory.setRepository(new File(request.getRealPath(Constant.UPLOAD_DIR)));
			MyServletFileUpload upload = new MyServletFileUpload(factory);
			//设置单个文件的最大上传size:100M
			upload.setFileSizeMax(Constant.MAX_EACH_FILE_UPLOAD_SIZE);
			//设置整个request的最大size:100M
			upload.setSizeMax(Constant.MAX_ONCE_UPLOAD_SIZE);
			upload.setHeaderEncoding("UTF-8");
			
			List<?> items = upload.parseRequest(request);
			if (items!=null){
				for(int i=0;i<items.size();i++){
					FileItem fileItem=(FileItem)items.get(i);
					if (fileItem.getName()!=null && fileItem.getName().length()>0){
						String fileName=Utils.takeOutFileName(fileItem.getName());
						//判断后缀名是不是xls
						int point = fileName.lastIndexOf(".");
						String fx = fileName.substring(point+1);
						if(fx.contains("xls")){
							//String name = fileName.substring(0,point)+".png";
							String fPath = request.getRealPath(Constant.UPLOAD_DIR)+File.separator+fileName;
							File uploadedFile = new File(fPath);
							fileItem.write(uploadedFile);
							json.put("success", true);
							json.put("fileName", fileName);
							json.put("msg", "上传成功");
						}else{
							json.put("success", false);
							json.put("msg", "上传格式错误,文件不是excel文件!");
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			json.put("success", false);
			json.put("msg", "上传错误,文件可能超过最大值!");
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
		String realPath = ctx.getRealPath(Constant.UPLOAD_DIR);
		File fDir = new File(realPath);
		if(!fDir.exists())
			fDir.mkdir();		
	}
	
	private Object getService(String strSerivce){
		return Application.getInstance().getContainer().getComponent(strSerivce);
	}

}
