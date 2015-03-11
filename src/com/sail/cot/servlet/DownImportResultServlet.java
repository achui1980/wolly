package com.sail.cot.servlet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jason.core.Application;
 
import com.sail.cot.domain.CotImportresult;
import com.sail.cot.service.sample.CotImportResultService;

public class DownImportResultServlet extends HttpServlet implements Servlet {

	private void downImportResult(HttpServletRequest request,HttpServletResponse response) throws IOException
	{
		
		String ImportResultId = request.getParameter("ImportResultId");
		System.out.println("ImportResultId="+ImportResultId);
		CotImportResultService cotImportResultService = (CotImportResultService)this.getService("CotImportResultService");
		CotImportresult cotImportresult = cotImportResultService.getImportresultById(Integer.parseInt(ImportResultId));
		
		setImportresultDownloadResponseHeader(response,cotImportresult.getImpFilename());
		try
		{
			if(cotImportresult != null)
			{
				String impFilepath =  cotImportresult.getImpFilepath();
				//获得tomcat路径
				String classPath = DownImportResultServlet.class.getResource("/").toString();
				String systemPath = classPath.substring(6, classPath.length()-16);
				File downImportResult = new File(systemPath+impFilepath);
 
				DataInputStream is=new DataInputStream(new FileInputStream(downImportResult));
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
			//ex.printStackTrace();
			response.getWriter().write("<script>window.parent.alert('下载文件失败：找不到下载文件');</script>");
		}
	}
	
	private Object getService(String strSerivce)
	{
		return Application.getInstance().getContainer().getComponent(strSerivce);
	}
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		downImportResult(request,response);
	}
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		downImportResult(request,response);
	}
	/**
	 * 设置文件下载的header
	 * @param response
	 * @param fileName
	 * @throws UnsupportedEncodingException 
	 */
	private void setImportresultDownloadResponseHeader(HttpServletResponse response,String fileName) throws UnsupportedEncodingException{
		//response.setHeader("Cache-Control", "no-cache");
		response.setContentType("application/octet-stream; CHARSET=utf8");
		response.setHeader("Content-Disposition","attachment; filename="+
					java.net.URLEncoder.encode(fileName,"UTF-8"));
		//response.setHeader("Content-Disposition","filename="+java.net.URLEncoder.encode(fileName,"UTF-8"));   
	}
}
