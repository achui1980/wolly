/**
 * 
 */
package com.sail.cot.servlet;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import com.bjinfotech.practice.ajax.Utils;
import com.sail.cot.util.ImageCompressUtil;

/**
 * <p>Title: 工艺品管理系统</p>
 * <p>Description:</p>
 * <p>Copyright: Copyright (c) 2008</p>
 * <p>Company: </p>
 * <p>Create Time: Jan 9, 2009 3:22:28 PM </p>
 * <p>Class Name: TestServletUpload.java </p>
 * @author achui
 *
 */
public class TestServletUpload extends HttpServlet {
	
	private ImageCompressUtil imageUtil = new ImageCompressUtil();

	public void uploadfile(HttpServletRequest request, HttpServletResponse response)
	{
		System.out.println("xxx");
		String scale = request.getParameter("scale");
		try {
			request.setCharacterEncoding("UTF-8");
			FileItemFactory factory = new DiskFileItemFactory();
			ServletFileUpload upload = new ServletFileUpload(factory);
			//upload.setHeaderEncoding(request.getCharacterEncoding());
			
			List items = (List) upload.parseRequest(request);
			String addString = request.getParameter("test");
			System.out.println("addString:"+addString);
			Iterator iter = items.iterator();
			while (iter.hasNext()) {
				FileItem fileItem = (FileItem) iter.next();
				System.out.println(fileItem.getFieldName());
				//System.out.println(fileItem.getString("utf-8"));
				if (fileItem.isFormField()) {/*
					String name = item.getFieldName();
					String value = item.getString();
					System.out.println("name = " + name);
					System.out.println("value = " + value);
					System.out.println("/r/n");*/
				} else {
					String fieldName = fileItem.getFieldName();
					String fileName=Utils.takeOutFileName(fileItem.getName());
					String path = request.getRealPath("temp")+File.separator;
					File uploadedFile = new File(path+fileName);
					fileItem.write(uploadedFile);
					//response.setStatus(200);
					response.getWriter().write("上传完毕");
					//如果需要压缩图片
					if(scale!=null){
						String height = request.getParameter("height");
						String width = request.getParameter("width");
						//一张一张上传,上传后压缩,压缩后覆盖旧的大图片
						imageUtil.imageCompress(path, fileName, fileName, Float.parseFloat(scale), Integer.parseInt(width), Integer.parseInt(height));
					}
				}
			}
			System.out.println("-------- final--------");
		} catch (Exception ex) {
			ex.printStackTrace();
		}
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
		uploadfile(request,response);
	
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

		uploadfile(request,response);
	}
	
}
