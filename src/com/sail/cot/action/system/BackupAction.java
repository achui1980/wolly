package com.sail.cot.action.system;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;
import net.sourceforge.jeval.function.string.IndexOf;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.environment.EnvironmentUtils;
import org.apache.commons.exec.launcher.CommandLauncher;
import org.apache.commons.exec.launcher.CommandLauncherFactory;
import org.apache.commons.exec.launcher.WinNTCommandLauncher;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.dom4j.io.SAXReader;

import com.sail.cot.action.AbstractAction;

public class BackupAction extends AbstractAction {

	@Override
	public ActionForward add(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ActionForward modify(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ActionForward query(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ActionForward remove(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public ActionForward backupPage(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		
		return mapping.findForward("backupPage");
	}
	
	@SuppressWarnings("unchecked")
	public ActionForward backup(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {

		JSONObject json=new JSONObject();
		
		
		
		String serverIp = request.getParameter("serverIp");
		String loginName = request.getParameter("loginName");
		String loginPassword = request.getParameter("loginPassword");
		String databaseName = request.getParameter("databaseName");
		String tableName = request.getParameter("tableName");
		String flag = request.getParameter("flag");
		
		long ls = System.currentTimeMillis();
		Map map = null;
		try {
			map = EnvironmentUtils.getProcEnvironment();
		} catch (IOException e) {
			e.printStackTrace();
		}
		//获取系统目录
		//String dir = System.getProperty("user.dir");
		String subPath = request.getRealPath("/");
		
		//String subPath = dir.substring(0, dir.indexOf("\\bin"));
		
		File dirFile = new File(subPath+"\\bak");
		if (!dirFile.exists()) {
			dirFile.mkdir();
		}
		//在指定目录生成批处理文件
		FileOutputStream os = null;
		try {
			os = new FileOutputStream(new File(subPath+"\\bak\\bak.bat"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		String currentDate = DateFormatUtils.ISO_DATE_FORMAT.format(new Date());
		String backupPath = null;
		//MySql数据库备份命令
		String strCmd = null;
		//备份数据库中的某一张表
		if ("true".equals(flag)) {
			backupPath = subPath+"\\bak\\"+tableName+"_"+currentDate+".sql";
			strCmd = "SET Path=%Path%;%MYSQL_HOME%\\bin;"+"\r\n";
			strCmd += "mysqldump  -u"+loginName+" -p"+loginPassword+" -h"+serverIp+" --default-character-set=utf8 --opt " +
					 "-C --extended-insert=false --single-transaction --triggers -R --hex-blob  "
					 +databaseName+" "+tableName+"> "+subPath+"\\bak\\"+tableName+"_"+currentDate+".sql";
		}else {
			//备份整个数据库
			backupPath = subPath+"\\bak\\"+databaseName+"_"+currentDate+".sql";
			strCmd = "SET Path=%Path%;%MYSQL_HOME%\\bin;"+"\r\n";
			strCmd += "mysqldump  -u"+loginName+" -p"+loginPassword+" -h"+serverIp+" --default-character-set=utf8 --opt " +
			 	     "-C --extended-insert=false --single-transaction --triggers -R --hex-blob  "
			         +databaseName+">"+subPath+"\\bak\\"+databaseName+"_"+currentDate+".sql";
		}
		
		try {
			os.write(strCmd.getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			os.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		//执行批处理
		CommandLine cmd = new CommandLine(subPath+"\\bak\\bak.bat");
		CommandLauncher cmdFac = CommandLauncherFactory.createVMLauncher();
		WinNTCommandLauncher win = new WinNTCommandLauncher(cmdFac);
		
		Process process = null;
		try {
			process = win.exec(cmd, map);
		} catch (IOException e) {
			e.printStackTrace();
		}
		InputStream in = process.getInputStream();
		BufferedReader br = new BufferedReader(new InputStreamReader(in));
		String tmp = null;
		try {
			while ((tmp=br.readLine())!=null) {
				System.out.println("--------+"+tmp);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		long le = System.currentTimeMillis();
		long useTime = le-ls;
		boolean isComplete = true;
		if (isComplete) {
			System.out.println("执行完毕，共耗时： "+(le-ls)+"毫秒");
		}
		String msg = "Backup Complete！consuming time："+useTime+"&nbsp;&nbsp;milliseconds；</font>"+
		"Backed up to the server："+backupPath+"&nbsp;&nbsp;";
		json.put("success", true);
		json.put("msg", msg);
		try {
			response.getWriter().write(json.toString());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
