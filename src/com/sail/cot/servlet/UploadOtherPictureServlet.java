package com.sail.cot.servlet;

import java.io.File;
import java.io.IOException;
import java.sql.Date;
import java.util.HashMap;
import java.util.List;

import javax.servlet.Servlet;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.log4j.Logger;

import com.bjinfotech.practice.ajax.Constant;
import com.bjinfotech.practice.ajax.FileUploadListener;
import com.bjinfotech.practice.ajax.FileUploadStatus;
import com.bjinfotech.practice.ajax.MyServletFileUpload;
import com.bjinfotech.practice.ajax.UploadSessionImpl;
import com.bjinfotech.practice.ajax.UploadSessionManager;
import com.bjinfotech.practice.ajax.Utils;
import com.bjinfotech.practice.ajax.XmlUnSerializer;
import com.jason.core.Application;
import com.sail.cot.domain.CotSyslog;
import com.sail.cot.service.sample.CotElementsService;
import com.sail.cot.service.system.CotSysLogService;
import com.sail.cot.util.Log4WebUtil;

/**
样品编辑页面上传图片
 */
public class UploadOtherPictureServlet extends HttpServlet implements Servlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 946573126457064359L;
	private static final String DOC_DIR = "/sampleImg";
	Logger logger = Log4WebUtil.getLogger(UploadOtherPictureServlet.class);
	/**
	 * Constructor of the object.
	 */
	public UploadOtherPictureServlet() {
		super();
	}

	/**
	 * Destruction of the servlet. <br>
	 */
	public void destroy() {
		super.destroy(); // Just puts "destroy" string in log
		// Put your code here
	}

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

		this.processFileUpload(request, response);
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
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String param1 = request.getParameter("test");
		if(param1 != null)
		{
			System.out.println("-------------"+param1);
		}
		
		this.processFileUpload(request, response);
	}

	/**
	 * Initialization of the servlet. <br>
	 *
	 * @throws ServletException if an error occurs
	 */
	public void init() throws ServletException {
		// Put your code here
		ServletContext ctx = this.getServletContext();
		String realPath = ctx.getRealPath(DOC_DIR);
		File fDir = new File(realPath);
		//如果上传目录不存在，创建之
		if(!fDir.exists())
			fDir.mkdir();		
	}
	
	/**
	 * 上传过程中出错处理
	 * @param request
	 * @param errMsg
	 * @param uSessionId
	 * @throws IOException 
	 * @throws ServletException 
	 */
	protected void uploadExceptionHandle(
			HttpServletRequest request,
			String errMsg,
			String uSessionId){
		UploadSessionImpl uSession=UploadSessionManager.getInstance().getUploadSession(uSessionId);
		uSession.getActiveFUSBean().setStatus(errMsg);
		UploadSessionManager.getInstance().addOrUpdateUploadSession(uSession);
	}
	/**
	 * 更新开始/结束处理文件上传时间
	 * @param uSessionId
	 * @param time
	 * @param isStart 是否为开始时间，false表示为结束时间
	 */
	protected void updateProcessStatus(String uSessionId,long time,boolean isStart){
		UploadSessionImpl uSession=UploadSessionManager.getInstance().getUploadSession(uSessionId);
		FileUploadStatus fUploadStatus=uSession.getActiveFUSBean();
		
		if (isStart){
			fUploadStatus.setProcessStartTime(System.currentTimeMillis());
		}
		//更新结束时间时修改传输状态
		else{
			logger.debug("文件上传状态:"+fUploadStatus.getStatus());
			if (fUploadStatus.getCancel()){
				fUploadStatus.setStatus("完成取消处理");
			}
			else if (fUploadStatus.getStatus().indexOf("错误")<0 && fUploadStatus.getStatus().indexOf("上传文件已被病毒感染")<0){
				fUploadStatus.setStatus("完成文件上传处理");
			}
			fUploadStatus.setProcessEndTime(System.currentTimeMillis());
			fUploadStatus.setProcessRunningTime(fUploadStatus.getProcessEndTime()-fUploadStatus.getProcessStartTime());
		}
		uSession.setActiveFUSBean(fUploadStatus);
		UploadSessionManager.getInstance().addOrUpdateUploadSession(uSession);
	}
	@SuppressWarnings("deprecation")
	public void processFileUpload(HttpServletRequest request, HttpServletResponse response) throws IOException
	{

		JSONObject json=new JSONObject();
		String msg = "";
		if (request.getContentLength()>Constant.MAX_ONCE_UPLOAD_SIZE){
			
			json.put("success", false);
			json.put("msg", "Uploading Error:The file size exceeds limit["+Constant.MAX_ONCE_UPLOAD_SIZE+"bytes]");
		}
		else{
			//更新开始处理文件上传时间
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
			try {
				List<?> items = upload.parseRequest(request);
				if (items!=null){
					
					//获取页面参数
					HashMap<String, String> map = new HashMap<String, String>();;
					for (int i=0; i<items.size(); i++) {
						FileItem fileItem=(FileItem)items.get(i);
						
						String fieldName = fileItem.getFieldName();
						if (fieldName != null) {
							map.put(fieldName, fileItem.getString("utf-8"));
						}
					}
					
					for(int i=0;i<items.size();i++){
						//取消上传:当parseRequest时成功取消上传，则上传文件队列中没有文件
						FileItem fileItem=(FileItem)items.get(i);
					
						if (fileItem.getName()!=null && fileItem.getName().length()>0){
							String fileName=Utils.takeOutFileName(fileItem.getName());
							
							logger.info("处理文件["+fileName+"]:保存路径为"+request.getRealPath(DOC_DIR)+File.separator+fileName);
							//将图片后缀名改成png
							int point = fileName.lastIndexOf(".");
							String name = fileName.substring(0,point)+".png";
							String fPath = request.getRealPath(DOC_DIR)+File.separator+name;
							File uploadedFile = new File(fPath);
							fileItem.write(uploadedFile);
							//保存到数据库(更新图片表)
							CotElementsService cotElementsService = (CotElementsService)this.getService("CotElementsService");
							CotSysLogService sysLogService = (CotSysLogService)this.getService("CotSysLogService");
							String pEId=(String) map.get("pEId");
							Integer empId = (Integer) request.getSession().getAttribute("empId");
							try {
								cotElementsService.saveOtherPic(fPath, Integer.parseInt(pEId));
								json.put("success", "true");
								json.put("fileName", fileName);
								json.put("msg", "上传成功");
								//保存到系统日记表
								CotSyslog cotSyslog = new CotSyslog();
								cotSyslog.setEmpId(empId);
								cotSyslog.setOpMessage("上传样品其他图片成功");
								cotSyslog.setOpModule("elements");
								cotSyslog.setOpTime(new Date(System.currentTimeMillis()));
								cotSyslog.setItemNo(pEId);
								cotSyslog.setOpType(1);
								sysLogService.addSysLogByObj(cotSyslog);
							} catch (Exception e) {
								//保存到系统日记表
								json.put("success", false);
								json.put("fileName", fileName);
								json.put("msg", "上传失败"+e.getMessage());
								CotSyslog cotSyslog = new CotSyslog();
								cotSyslog.setEmpId(empId);
								cotSyslog.setOpMessage("上传样品其他图片失败,失败原因:"+e.getMessage());
								cotSyslog.setOpModule("elements");
								cotSyslog.setOpTime(new Date(System.currentTimeMillis()));
								cotSyslog.setItemNo(pEId);
								cotSyslog.setOpType(1);
								sysLogService.addSysLogByObj(cotSyslog);
							}
							
							/*
							if (Constant.IS_SCAN_VIRUS){
								uSession.getActiveFUSBean().setStatus("正在病毒扫描...");
								UploadSessionManager.getInstance().addOrUpdateUploadSession(uSession);
								if (Utils.scanInfectedFileWithCallClamAV(uploadedFile.getAbsolutePath())==false){
									logger.info("清除染毒文件:"+uploadedFile.getAbsolutePath());
									uSession.getActiveFUSBean().setStatus("上传文件已被病毒感染");
									UploadSessionManager.getInstance().addOrUpdateUploadSession(uSession);
									uploadedFile.delete();
								}
							}
							*/
							//更新当前激活的FUS和FUS列表
							logger.debug("更新上传文件列表");
						}
					}
				}
			} catch (FileUploadException e) {
				logger.error("Uploading Error:"+e.getMessage());
				json.put("success", false);
				json.put("msg", "Uploading Error:"+e.getMessage());
				e.printStackTrace();
			} catch (Exception e) {
				json.put("success", false);
				json.put("msg", "Failed to connect to server:"+e.getMessage());
				e.printStackTrace();
			}
		}
		//更新开始处理文件上传时间
		System.out.println("json:"+json.toString());
		response.getWriter().write(json.toString());
	}
	
	private Object getService(String strSerivce)
	{
		return Application.getInstance().getContainer().getComponent(strSerivce);
	}

}
