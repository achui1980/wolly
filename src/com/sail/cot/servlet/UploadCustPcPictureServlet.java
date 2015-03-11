package com.sail.cot.servlet;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import javax.servlet.Servlet;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.log4j.Logger;

import com.bjinfotech.practice.ajax.Constant;
import com.bjinfotech.practice.ajax.Utils;
import com.jason.core.Application;
import com.sail.cot.service.customer.CotCustomerService;
import com.sail.cot.util.Log4WebUtil;

public class UploadCustPcPictureServlet extends HttpServlet implements Servlet {

	private static final long serialVersionUID = 946573126457064359L;
	private static final String DOC_DIR = "/sampleImg";
	Logger logger = Log4WebUtil.getLogger(UploadCustPcPictureServlet.class);

	public UploadCustPcPictureServlet() {
		super();
	}

	/**
	 * Initialization of the servlet. <br>
	 * 
	 * @throws ServletException
	 *             if an error occurs
	 */
	public void init() throws ServletException {
		ServletContext ctx = this.getServletContext();
		String realPath = ctx.getRealPath(DOC_DIR);
		File fDir = new File(realPath);
		if (!fDir.exists())
			fDir.mkdirs();
	}

	public void destroy() {
		super.destroy(); // Just puts "destroy" string in log
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response) {
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		String msg = "";
		response.setContentType("text/html");
		response.setCharacterEncoding("utf-8");
		response.setHeader("Cache-Control", "no-cache");
		if (request.getContentLength() > Constant.MAX_ONCE_UPLOAD_SIZE) {
			msg = "{success:false,msg:'上传文件时发生错误:文件大小超过上限"
					+ Constant.MAX_ONCE_UPLOAD_SIZE + " bytes'}";
		} else {
			DiskFileItemFactory factory = new DiskFileItemFactory();
			// 设置内存阀值，超过后写入临时文件
			factory.setSizeThreshold(Constant.UPLOAD_SIZE_THRESHOLD);
			// 设置临时文件存储位置
			factory.setRepository(new File(request.getRealPath(DOC_DIR)));
			ServletFileUpload upload = new ServletFileUpload(factory);
			// 设置单个文件的最大上传size:100M
			upload.setFileSizeMax(Constant.MAX_EACH_FILE_UPLOAD_SIZE);
			// 设置整个request的最大size:100M
			upload.setSizeMax(Constant.MAX_ONCE_UPLOAD_SIZE);
			upload.setHeaderEncoding("UTF-8");
			try {
				List<?> items = upload.parseRequest(request);
				if (items != null) {
					// 获取页面参数
					HashMap<String, String> map = new HashMap<String, String>();
					for (int i = 0; i < items.size(); i++) {
						FileItem fileItem = (FileItem) items.get(i);
						String fieldName = fileItem.getFieldName();
						if (fieldName != null) {
							map.put(fieldName, fileItem.getString("utf-8"));
						}
					}
					for (int i = 0; i < items.size(); i++) {
						FileItem fileItem = (FileItem) items.get(i);

						if (fileItem.getName() != null
								&& fileItem.getName().length() > 0) {
							String fileName = Utils.takeOutFileName(fileItem
									.getName());
							logger.info("处理文件[" + fileName + "]:保存路径为"
									+ request.getRealPath(DOC_DIR)
									+ File.separator + fileName);
							// 将图片后缀名改成png
							int point = fileName.lastIndexOf(".");
							String name = fileName.substring(0, point) + ".png";
							String fPath = request.getRealPath(DOC_DIR)
									+ File.separator + name;
							File uploadedFile = new File(fPath);
							fileItem.write(uploadedFile);
							String pEId = (String) map.get("pEId");
							String flag = (String) map.get("flag");
							// System.out.println(pEId);
							msg = "{'success':true,fileName:'"
									+ request.getContextPath() + DOC_DIR + "/"
									+ name + "'}";
							if (pEId != null && !"".equals(pEId)
									&& !"null".equals(pEId)) {
								// 保存到数据库(更新图片表和样品表)
								CotCustomerService cotCustomerService = (CotCustomerService) this
										.getService("CotCustomerService");
								try {
									cotCustomerService.updateCustPc(fPath,
											Integer.parseInt(pEId));
									msg = "{'success':true,fileName:'"
											+ DOC_DIR + File.separator + name
											+ "'}";
								} catch (Exception e) {
									msg = "{'success':false,msg:'保存文件异常'}";
									e.printStackTrace();
								}
							}
						}
					}
				}
			} catch (FileUploadException e) {
				msg = "{'success':false,msg:'上传文件时发生错误:" + e.getMessage()
						+ "'}";
				e.printStackTrace();
			} catch (Exception e) {
				msg = "{'success':false,msg:'解析multiPart HttpRequest时发生错误:"
						+ e.getMessage() + "'}";
				e.printStackTrace();
			}
		}
		response.getWriter().write(msg);
	}

	private Object getService(String strSerivce) {
		return Application.getInstance().getContainer()
				.getComponent(strSerivce);
	}

}
