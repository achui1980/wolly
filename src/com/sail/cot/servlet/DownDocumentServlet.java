package com.sail.cot.servlet;
 
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream; 
import java.io.FileNotFoundException;
import java.io.IOException; 
import java.io.UnsupportedEncodingException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jason.core.Application; 
import com.sail.cot.domain.CotFile;
import com.sail.cot.service.sample.CotFileService;
 
public class DownDocumentServlet extends javax.servlet.http.HttpServlet implements javax.servlet.Servlet  {
	 
	private void downFile(HttpServletRequest request,HttpServletResponse response) throws IOException
	{
		String fileId = request.getParameter("fileId");
		CotFileService cotFileService = (CotFileService)this.getService("CotFileService");
		byte[] fileContent = cotFileService.getFileContentById(Integer.parseInt(fileId));
		CotFile cotFile = cotFileService.getFileById(Integer.parseInt(fileId));
		setFileDownloadResponseHeader(response,cotFile.getFileName());
		try
		{
			if(fileContent != null)
			{
				DataInputStream is=new DataInputStream(new ByteArrayInputStream(fileContent));
				DataOutputStream os=new DataOutputStream(response.getOutputStream()); 
				byte[] readBytes=new byte[128];
				while(is.read(readBytes)!=-1){
					os.write(readBytes);
				}
				os.close();  
				is.close();  
			}
			else
			{
				response.getWriter().write("<script>alert('下载文件失败：找不到下载文件');</script>");
			}
		}
		catch(FileNotFoundException ex)
		{
			response.getWriter().write("<script>window.parent.alert('下载文件失败：找不到下载文件');</script>");
		}
	}
	
	private Object getService(String strSerivce)
	{
		return Application.getInstance().getContainer().getComponent(strSerivce);
	}
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		downFile(request,response);
	}
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		downFile(request,response);
	}
	/**
	 * 设置文件下载的header
	 * @param response
	 * @param fileName
	 * @throws UnsupportedEncodingException 
	 */
	private void setFileDownloadResponseHeader(HttpServletResponse response,String fileName) throws UnsupportedEncodingException{
		//response.setHeader("Cache-Control", "no-cache");
		response.setContentType("application/octet-stream; CHARSET=utf8");
		response.setHeader("Content-Disposition","attachment; filename="+
					java.net.URLEncoder.encode(fileName,"UTF-8"));
		//response.setHeader("Content-Disposition","filename="+java.net.URLEncoder.encode(fileName,"UTF-8"));   
	}
}
