package com.sail.cot.servlet;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;

import javax.servlet.Servlet;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUpload;
import org.apache.commons.fileupload.FileUploadBase;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.log4j.Logger;

import com.bjinfotech.practice.ajax.Constant;
import com.bjinfotech.practice.ajax.FileUploadListener;
import com.bjinfotech.practice.ajax.FileUploadStatus;
import com.bjinfotech.practice.ajax.MyServletFileUpload;
import com.bjinfotech.practice.ajax.UploadSessionImpl;
import com.bjinfotech.practice.ajax.UploadSessionManager;
import com.bjinfotech.practice.ajax.Utils;
import com.bjinfotech.practice.ajax.XmlUnSerializer;

import com.sail.cot.util.Log4WebUtil;

/**
@author:achui
Description:
Company:厦门纵横科技
Apr 7, 2008
 */
public class UploadReportImgServlet extends HttpServlet implements Servlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 946573126457064359L;
	private static final String DOC_DIR = "/reportImg";
	 
	Logger logger = Log4WebUtil.getLogger(UploadServlet.class);
	/**
	 * Constructor of the object.
	 */
	public UploadReportImgServlet() {
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
	public void processFileUpload(HttpServletRequest request, HttpServletResponse response) throws IOException
	{

		String uSessionId=(String)request.getSession(false).getAttribute(Constant.SESSION_KEY);
		if (request.getContentLength()>Constant.MAX_ONCE_UPLOAD_SIZE){
			uploadExceptionHandle(request,"Uploading Error:The file size exceeds limit["+Constant.MAX_ONCE_UPLOAD_SIZE+"bytes]",uSessionId);
		}
		else{
			//更新开始处理文件上传时间
			updateProcessStatus(uSessionId,System.currentTimeMillis(),true);
			
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
			
			upload.setHeaderEncoding("utf-8");
			
			upload.setProgressListener(new FileUploadListener(uSessionId));
			UploadSessionImpl uSession=null;
			
			try {
				List items = upload.parseRequest(request);
				if (items!=null){
					logger.debug("处理文件部分总数为"+upload.getFileItemCount());
					
					//获取页面参数
					HashMap map = new HashMap();;
					for (int i=0; i<items.size(); i++) {
						FileItem fileItem=(FileItem)items.get(i);
						
						String fieldName = fileItem.getFieldName();
						if (fieldName != null) {
							map.put(fieldName, fileItem.getString("utf-8"));
						}
					}
					
					for(int i=0;i<items.size();i++){
						//取消上传:当parseRequest时成功取消上传，则上传文件队列中没有文件
						uSession=UploadSessionManager.getInstance().getUploadSession(uSessionId);
						if (uSession.getActiveFUSBean().getCancel()){
							uSession.getActiveFUSBean().setStatus("正在进行取消处理...");
							UploadSessionManager.getInstance().addOrUpdateUploadSession(uSession);
							break;
						}
						FileItem fileItem=(FileItem)items.get(i);
					
						if (fileItem.getName()!=null && fileItem.getName().length()>0){
							String fileName=Utils.takeOutFileName(fileItem.getName());
							logger.info("处理文件["+fileName+"]:保存路径为"
									+request.getRealPath(DOC_DIR)+File.separator+fileName);
							
							// 如果文件夹不存在 则建立新文件夹
							File uploadForder = new File(request.getRealPath(DOC_DIR));
							if(!uploadForder.exists())
								uploadForder.mkdirs();
							
							File uploadedFile = new File(request.getRealPath(DOC_DIR)+File.separator+fileName);
							fileItem.write(uploadedFile);
							System.out.println("-------");
							fileItem.write(uploadedFile);
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
							uSession=UploadSessionManager.getInstance().getUploadSession(uSessionId);
							uSession.getActiveFUSBean().setUploadFileURL(fileName);
							//补充上文件大小<1024byte的文件数据
							if (uSession.getActiveFUSBean().getUploadTotalSize()==0){
								uSession.getActiveFUSBean().setUploadTotalSize(uploadedFile.length());
							}
							if (uSession.getActiveFUSBean().getReadTotalSize()==0){
								uSession.getActiveFUSBean().setReadTotalSize(uploadedFile.length());
							}
							uSession.addFUSBeanIntoList(uSession.getActiveFUSBean());
							UploadSessionManager.getInstance().addOrUpdateUploadSession(uSession);
							
							//TODO:在这里做上传后的操作
							//获取页面参数：
							 
							String rptImgPath="reportImg"+File.separator+fileName;
							map.put("rptImgPath", rptImgPath);
							request.setAttribute("rptImgPath", rptImgPath);
							String rptId = map.get("rptId").toString().trim();
							if(!rptId.equals(""))
							{
								UploadReportImgService service = new UploadReportImgService();
								service.doPorcess(map);
							}
						}
					}
				}
			} catch (FileUploadException e) {
				logger.error("Uploading Error:"+e.getMessage());
				uploadExceptionHandle(request,"Uploading Error:"+e.getMessage(),uSessionId);
				e.printStackTrace();
			} catch (Exception e) {
				logger.error("Failed to connect to server:"+e.getMessage());
				uploadExceptionHandle(request,"Uploading Error:"+e.getMessage(),uSessionId);
				e.printStackTrace();
			}
		}
		//更新开始处理文件上传时间
		updateProcessStatus(uSessionId,System.currentTimeMillis(),false);

		try {
			//等待一段时间，希望用户能够看到完成状态
			Thread.sleep(100);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		response.setContentType("text/html");
		response.setCharacterEncoding("utf-8");
		response.setHeader("Cache-Control", "no-cache");
		UploadSessionImpl uSession=UploadSessionManager.getInstance().getUploadSession(uSessionId);
		StringBuffer sb=new StringBuffer();
		sb.append("<html>\n<body>\n")
			.append("<textArea id='response'>\n")
			.append(XmlUnSerializer.serializeBeanList(uSession.getFUSBeanList()))
			.append("</textArea>\n")
			.append("<script>\n")
			.append("try{")
			.append("parent.fetchUploadListFromIFrameIO();")
			.append("}catch(e){ alert(e);}")
			.append("</script>\n")
			.append("</body>\n</html>");
		response.getWriter().write(sb.toString());
	}

}
