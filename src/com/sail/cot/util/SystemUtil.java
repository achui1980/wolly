/**
 * 
 */
package com.sail.cot.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import net.sf.jasperreports.engine.JRElement;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRReport;
import net.sf.jasperreports.engine.design.JRDesignBand;
import net.sf.jasperreports.engine.design.JRDesignElement;
import net.sf.jasperreports.engine.design.JRDesignExpression;
import net.sf.jasperreports.engine.design.JRDesignStaticText;
import net.sf.jasperreports.engine.design.JRDesignTextField;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.engine.xml.JRXmlWriter;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.environment.EnvironmentUtils;
import org.apache.commons.exec.launcher.CommandLauncher;
import org.apache.commons.exec.launcher.CommandLauncherFactory;
import org.apache.commons.exec.launcher.WinNTCommandLauncher;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.directwebremoting.WebContextFactory;

import com.jason.core.Application;
import com.sail.cot.dao.CotBaseDao;
import com.sail.cot.dao.impl.CotExportRptDaoImpl;
import com.sail.cot.domain.CotElementsNew;
import com.sail.cot.domain.CotEmps;
import com.sail.cot.domain.CotGivenDetail;
import com.sail.cot.domain.CotOrderDetail;
import com.sail.cot.domain.CotPriceDetail;
import com.sail.cot.domain.CotSignDetail;

/**
 * <p>Title: 旗行办公自动化系统（OA）</p>
 * <p>Description:</p>
 * <p>Copyright: Copyright (c) 2008</p>
 * <p>Company: </p>
 * <p>Create Time: Feb 26, 2009 12:10:09 PM </p>
 * <p>Class Name: SystemUtil.java </p>
 * @author achui
 *
 */
public class SystemUtil implements java.io.Serializable {
	Logger log  = Log4WebUtil.getLogger(SystemUtil.class);
	public static String getDefaultSortSQL(String alias, Map map) {
		StringBuffer rs = new StringBuffer();
		if (map != null && !map.isEmpty()) {
			for (Iterator itor = map.keySet().iterator(); itor.hasNext();) {
				Object field = (String) itor.next();
				String ord = (String) map.get(field);
				rs.append(" ORDER BY ").append(alias).append(".").append(field)
						.append(" ").append(ord);
				break;
			}
		}

		return rs.toString();
	}

	//获取一个文件的扩展名
	public static String getFileSuffix(String filePath) {
		int index = filePath.lastIndexOf(".");
		String suffix = filePath.substring(index + 1);
		return suffix;
	}

	//获取一个路径下的文件名(带扩展名)
	public static String getFileName(String filePath) {
		if (filePath == null)
			return "";
		int index = filePath.lastIndexOf(File.separator);
		String fileName = "";
		if (index < 0)
			fileName = filePath;
		else
			fileName = filePath.substring(index + 1);
		return fileName;
	}

	//判断是否有对该模块进行操作的权限
	/**
	 * @param moduleUrl   模块权限的URL 如:cotemps.do
	 * @param action	  特定权限的动作,如 ADD,SEL等
	 * @return
	 * boolean	true:具有改操作权限 false:不具有改操作权限
	 */
	public static boolean isAction(HttpServletRequest request,
			String moduleUrl, String action) {
		HttpSession session = request.getSession();
		Map popedomOp = null;
		Map poepdomMap = (Map) session.getAttribute("popedomMap");
		if (poepdomMap != null) {
			Map map = (Map) poepdomMap.get(moduleUrl);
			if (map != null && map.get(action) != null) {
				return true;
			}
		}
		return false;
	}

	//将报价，订单，征样，送样对象转换为样品对象

	/**
	 * @param objs 需要转换的目标对象
	 * @param type 目标对象的类型
	 * @return 样品对象数组
	 * List<CotElementsNew>
	 */
	public static List<CotElementsNew> changeToCotElementsNew(List objs,
			String type) {
		List<CotElementsNew> list = new ArrayList<CotElementsNew>();
		String[] properties = ReflectionUtils
				.getDeclaredFields(CotElementsNew.class);
		try {
			for (int k = 0; k < objs.size(); k++) {
				CotElementsNew cotElements = new CotElementsNew();
				for (int i = 0; i < properties.length; i++) {
					if ("cotpictures".equals(properties[i].toLowerCase())
							|| "cotfile".equals(properties[i].toLowerCase())
							|| "childs".equals(properties[i].toLowerCase())
							|| "id".equals(properties[i].toLowerCase()))
						continue;
					if (type.equals("order"))//从订单对象转换
					{
						CotOrderDetail orderDetail = (CotOrderDetail) objs
								.get(k);
						String value = BeanUtils.getProperty(orderDetail,
								properties[i]);
						if (value != null) {
							if ("picimg".equals(properties[i].toLowerCase())) {
								cotElements.setPicImg(orderDetail.getPicImg());
							} else {
								BeanUtils.setProperty(cotElements,
										properties[i], value);
							}
						}
					} else if (type.equals("price"))//从报价对象转换
					{
						CotPriceDetail priceDetail = (CotPriceDetail) objs
								.get(k);
						String value = BeanUtils.getProperty(priceDetail,
								properties[i]);
						if (value != null) {
							if ("picimg".equals(properties[i].toLowerCase())) {
								cotElements.setPicImg(priceDetail.getPicImg());
							} else {
								BeanUtils.setProperty(cotElements,
										properties[i], value);
							}
						}
					} else if (type.equals("given"))//从送样对象转换
					{
						CotGivenDetail givenDetail = (CotGivenDetail) objs
								.get(k);
						String value = BeanUtils.getProperty(givenDetail,
								properties[i]);
						if (value != null) {
							if ("picimg".equals(properties[i].toLowerCase())) {
								cotElements.setPicImg(givenDetail.getPicImg());
							} else {
								BeanUtils.setProperty(cotElements,
										properties[i], value);
							}
						}
					} else if (type.equals("sign"))////从征样对象转换
					{
						CotSignDetail signDetail = (CotSignDetail) objs.get(k);
						String value = BeanUtils.getProperty(signDetail,
								properties[i]);
						if (value != null) {
							if ("picimg".equals(properties[i].toLowerCase())) {
								cotElements.setPicImg(signDetail.getPicImg());
							} else {
								BeanUtils.setProperty(cotElements,
										properties[i], value);
							}
						}
					}
				}
				list.add(cotElements);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return list;
	}

	/**
	 * 为制定对象的指定属性设置指定的值
	 * @param obj 	需要被赋值的指定对象
	 * @param type	指定对象的类型
	 * @param property	需要赋值的指定属性
	 * @param value	需要赋值的指定值
	 * void
	 */
	public static Object setValueByType(Object obj, String type,
			String property, Object value) {
		try {
			if ("order".equals(type)) {
				CotOrderDetail orderDetail = (CotOrderDetail) obj;
				BeanUtils.setProperty(orderDetail, property, value);
			} else if ("price".equals(type)) {
				CotPriceDetail priceDetail = (CotPriceDetail) obj;
				BeanUtils.setProperty(priceDetail, property, value);
			} else if ("sign".equals(type)) {
				CotSignDetail signDetail = (CotSignDetail) obj;
				BeanUtils.setProperty(signDetail, property, value);
			} else if ("given".equals(type)) {
				CotGivenDetail signDetail = (CotGivenDetail) obj;
				BeanUtils.setProperty(signDetail, property, value);

			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return obj;
	}
	public static boolean copyRealFile(String realPath,String toRealPath){
		try {
			// 获得tomcat路径
			File inputFile = new File(realPath);// 定义读取源文件
			File outputFile = new File(toRealPath);// 定义拷贝目标文件
			int lastIndex = toRealPath.lastIndexOf("/");
			if(lastIndex!=-1){
				String dirName = toRealPath.substring(0,lastIndex);
				String[] dirs = dirName.split("/");
				String dirPath ="";
				for(int i=0;i<dirs.length;i++){
					dirPath += dirs[i]+"/";
					File dir=new File(dirPath);  
					if(!dir.exists()){
						if(!dir.mkdir()){
							throw new Exception("文件目录无法创建！");
						}
					}
				}
			}
			// 定义输入文件流
			FileInputStream in = new FileInputStream(inputFile);
			// 将文件输入流构造到缓存
			BufferedInputStream bin = new BufferedInputStream(in);
			// 定义输出文件流
			FileOutputStream out = new FileOutputStream(outputFile);
			// 将输出文件流构造到缓存
			BufferedOutputStream bout = new BufferedOutputStream(out);
			int c;
			// 循环读取文件和写入文件
			while ((c = bin.read()) != -1)
				bout.write(c);
			// 关闭输入输出流，释放资源
			bin.close();
			bout.close();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	//拷贝文件
	public static boolean copyFile(String srcPath, String toPath) {
		// 获得tomcat路径
		String classPath = SystemUtil.class.getResource("/").toString();
		String systemPath = classPath.substring(6, classPath.length() - 16);
		return SystemUtil.copyRealFile(systemPath+srcPath, systemPath+toPath);
	}

	//删除文件
	public static void deleteFile(String srcPath) {
		// 获得tomcat路径
		String classPath = SystemUtil.class.getResource("/").toString();
		String systemPath = classPath.substring(6, classPath.length() - 16);
		File file = new File(systemPath + srcPath);// 定义读取源文件
		if (file.exists()) {
			file.delete();
		}
	}
	public static String getRootPath(){
		String classPath = SystemUtil.class.getResource("/").toString();
		String systemPath = classPath.substring(6, classPath.length() - 16);
		return systemPath;
	}
	
	public static String getRptFilePath(){
		String path = ContextUtil.getProperty("remoteaddr.properties", "rpt_file_path");
		if(path == null || path.equals("")){
			path = SystemUtil.getRootPath()+"/saverptfile/";
			File fDir = new File(path);
			//如果上传目录不存在，创建之
			if(!fDir.exists())
				fDir.mkdir();	
		}
		return path;
	}
	/**
	 * 删除包含绝对路径的文件
	 * @param srcPath
	 */
	public static void deleteRealFile(String realFile) {
		// 获得tomcat路径
		File file = new File(realFile);// 定义读取源文件
		if (file.exists()) {
			file.delete();
		}
	}
	/**
	 * 删除指定文件夹下所有文件，不删除该指定文件夹
	 * @param srcPath 绝对路径
	 */
	public static void deleteAllFile(String srcPath){
		File file = new File(srcPath);
		if(!file.exists()&&!file.isDirectory())
			return;
		String[] tempList = file.list();
		File temp = null;
		for (String tempPath : tempList) {
			if(srcPath.endsWith(File.separator))
				temp = new File(srcPath + tempPath);
			else
				temp = new File(srcPath + File.separator + tempPath);
			if(temp.isFile())
				temp.delete();
			if(temp.isDirectory()){
				deleteAllFile(temp.getAbsolutePath());
				deleteFolder(temp.getAbsolutePath());
			}
		}
	}
	/**
	 * 删除文件夹，并删除文件夹下所有文件
	 * @param folderPath 绝对路径
	 */
	public static void deleteFolder(String folderPath){
		try {
			deleteAllFile(folderPath); // 删除文件件夹所有文件
			File myFilePath = new File(folderPath);
			myFilePath.delete();  // 删除空文件夹
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	

	//重命名文件(返回新地址)
	public static String renameFile(String srcPath, String newName) {
		// 获得tomcat路径
		String classPath = SystemUtil.class.getResource("/").toString();
		String systemPath = classPath.substring(6, classPath.length() - 16);
		File file1 = new File(systemPath + srcPath);
		int s = srcPath.lastIndexOf("/");
		if (s < 0) {
			return "";
		}
		String path = srcPath.substring(0, s + 1);
		File file2 = new File(systemPath + path + newName);
		if (file1.exists()) {
			if (file1.renameTo(file2) == false) { //判断file2是否存在
				return "";
			}
		}
		return path + newName;
	}

	public static Object getService(String serviceName) {
		return Application.getInstance().getContainer().getComponent(
				serviceName);
	}

	public static void main(String[] args)
	{
		String mac = SystemUtil.getMacAddr();
		System.out.println("mac:"+mac);
	}

	public static void setObjBySession(HttpSession httpSession, Object obj,
			String type) {
		HttpSession session = null;
		if (httpSession == null) {
			session = WebContextFactory.get().getSession();
		} else {
			session = httpSession;
		}
		session.setAttribute("Session" + type.toUpperCase(), obj);
	}

	public static Object getObjBySession(HttpSession httpSession, String type) {
		HttpSession session = null;
		if (httpSession == null) {
			session = WebContextFactory.get().getSession();
		} else {
			session = httpSession;
		}
		return session.getAttribute("Session" + type.toUpperCase());
	}

	public static void clearSessionByType(HttpSession httpSession, String type) {
		HttpSession session = null;
		if (httpSession == null) {
			session = WebContextFactory.get().getSession();
		} else {
			session = httpSession;
		}
		session.removeAttribute("Session" + type.toUpperCase());
	}

	// 用序列化与反序列化实现深克隆 
	public static Object deepClone(Object src) {
		Object o = null;
		try {
			if (src != null) {
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				ObjectOutputStream oos = new ObjectOutputStream(baos);
				oos.writeObject(src);
				oos.close();

				ByteArrayInputStream bais = new ByteArrayInputStream(baos
						.toByteArray());
				ObjectInputStream ois = new ObjectInputStream(bais);

				o = ois.readObject();
				ois.close();
			}

		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return o;
	}
    
    /**
     * @param filepath 文件参数，全路径 如：C:/WINDOWS/system32/demo.properties
     * @param key 需要获取的属性值
     * @return
     * String
     */
    public synchronized static String getProperty(String filepath,String key)
	{
		Properties p = null;
		p = new Properties();
		try {
			String path = filepath;
			InputStream in = new FileInputStream(path);
			p.load(in);
		} catch (IOException e) {
			e.printStackTrace();
		}
			
		return p.getProperty(key);
		
	}
    //更新配置文件
    
    /**
     * @param map 配置文件中值/键对应关系
     * @param filepath 文件参数，全路径 如：C:/WINDOWS/system32/demo.properties
     * void
     */
    public synchronized static void setPropertyFile(Map map,String filepath)
    {
    	Properties   iniFile   =   new   Properties();  
    	if(map == null) return;
        try   {  
        
        String path =  filepath;
		InputStream in = new FileInputStream(path);
		iniFile.load(in);
		
		Iterator iterator = map.keySet().iterator();
		while(iterator.hasNext())
		{
			
			String key = (String)iterator.next();
			iniFile.setProperty(key,(String)map.get(key));   		
		}  
        FileOutputStream   oFile   =   new   FileOutputStream(path);             
        iniFile.store(oFile,"");   
        }   
        catch   (Exception   ex)   {  
        	ex.printStackTrace();
        }   
    }
    //获取并设置配置文件
    public synchronized static String getAndsetPropertyFile(Map map,String filepath,String key)
    {
    	String sequence = SystemUtil.getProperty(filepath, key);
    	SystemUtil.setPropertyFile(map, filepath);
    	return sequence;
    }
	//格式化字符串,不足补一定字符
	public static String formatString(String targetStr, String addStr,
			int strLength) {
		if(targetStr == null || "".equals(targetStr)){
			return "";
		}
		if (targetStr.getBytes().length <= strLength) {
			String newString = "";
			int cutLength = strLength - targetStr.getBytes().length;
			for (int i = 0; i < cutLength; i++) {
				newString += addStr;
			}
			return targetStr + newString;
		} else {
			if(targetStr.length()<targetStr.getBytes().length){
				byte[] b = targetStr.getBytes();
				String temp="";
				for (int i = 0; i < strLength; i++) {
					temp+=b[i];
				}
				return temp;
			}else{
				return targetStr.substring(0, strLength);
			}
		}
	}
	public static String getMacAddr()
	{
		String mac = "";
		try {
			Map map = EnvironmentUtils.getProcEnvironment() ;
			InetAddress inet = InetAddress.getLocalHost();
			//获取本地IP地址
			String ip = inet.getHostAddress();
			String strcmd = "nbtstat -A " + ip;
			CommandLine cmd = new CommandLine(strcmd);
			CommandLauncher cmdFac = CommandLauncherFactory.createVMLauncher();
			WinNTCommandLauncher win = new WinNTCommandLauncher(cmdFac);
			Process process = win.exec(cmd, map,new File(map.get("WINDIR")+"/System32"));
			InputStream in = process.getInputStream(); 
			BufferedReader br = new BufferedReader(new InputStreamReader(in)); 
			String line = null; 
			while((line=br.readLine())!=null){ 
				System.out.println(line);
				 if (line.indexOf("MAC") > 0) {
                     mac = line.substring(line.indexOf("-") - 2);
                     mac = mac.replaceAll("-", "");
                     break;
                 }
			}
			if("".equals(mac))
				mac = "001CBF5F8A69";
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return mac;
	}
	public static String getMacAddrJDK6()
	{
		String macaddr = "001CBF5F8A69";
//		 try { 
//		       Enumeration<NetworkInterface> el = NetworkInterface.getNetworkInterfaces(); 
//		      while (el.hasMoreElements()) { 
//		        byte[] mac = el.nextElement().getHardwareAddress(); 
//		        if (mac == null) 
//		          continue; 
//		        System.out.println("mac:"+mac);
//
//		         StringBuilder builder = new StringBuilder(); 
//		        for (byte b : mac) { 
//		           builder.append(hexByte(b)); 
//		           //builder.append("-"); 
//		         } 
//		         builder.deleteCharAt(builder.length() - 1); 
//		         System.out.println(builder); 
//		         macaddr = builder.toString();
//		         break;
//		       }
//		     } catch (Exception exception) { 
//		       exception.printStackTrace(); 
//		     }
		     return macaddr;
	}
	public static String hexByte(byte b) { 
	     String s = "000000" + Integer.toHexString(b); 
	    return s.substring(s.length() - 2); 
	   } 
	/**
	 * 描述：
	 * @param filePath 原报表文件路径
	 * @param redesignMap 需要控制的参数，与报表中元素的key值相对应
	 * @param areaBand 发生作用的区域（目前只能针对同一个区域）
	 * @param detailMap   需要动态显示的detail列，格式为 key：报表中元素对应的可以 value：组合成报表可识别的表达式 如：$F{EMP_ID} +"/"+ $F{EMP_NAME} 
	 * @param pageSize 需要显示多条记录
	 * @return 生成新的报表文件，并返回其路径
	 * 返回值：String
	 * @throws JRException 
	 */
	public static String getRptReDesignFilePath(String filePath,HashMap redesignMap,String areaBand,HashMap detailMap,int pageSize) throws JRException
	{
		if(redesignMap == null || areaBand == null)
			return filePath;
		JasperDesign design = null;
		JRDesignBand band = null;
		design = JRXmlLoader.load(filePath);
		JRElement[] elements = null;
		if("Title".equals(areaBand))
		{
			elements = design.getTitle().getElements();
			band = (JRDesignBand)design.getTitle();
		}
		else if("PageHeader".equals(areaBand))
		{
			elements = design.getPageHeader().getElements();
			band = (JRDesignBand)design.getPageHeader();
		}
		else if("ColumnHeader".equals(areaBand))
		{
			elements = design.getColumnHeader().getElements();
			band = (JRDesignBand)design.getColumnHeader();
		}
		else if("ColumnFooter".equals(areaBand))
		{
			elements = design.getColumnFooter().getElements();
			band = (JRDesignBand)design.getColumnFooter();
		}
		else if("PageFooter".equals(areaBand))
		{
			elements = design.getPageFooter().getElements();
			band = (JRDesignBand)design.getPageFooter();
		}
		else if("LastPageFooter".equals(areaBand))
		{
			elements = design.getLastPageFooter().getElements();
			band = (JRDesignBand)design.getLastPageFooter();
		}
		else if("Summary".equals(areaBand))
		{
			elements = design.getSummary().getElements();
			band = (JRDesignBand)design.getSummary();
		}
		else if("NoData".equals(areaBand))
		{
			elements = design.getNoData().getElements();
			band = (JRDesignBand)design.getNoData();
		}
		else
			return filePath;
		
		int currentY = 0;
		for(JRElement element : elements)
		{
			String key = element.getKey();
			if(redesignMap.get(key) == null)//不需要显示的项
			{
				//去除不需要显示的项
				currentY = element.getY();
				band.setHeight(band.getHeight() - element.getHeight());
				band.removeElement((JRDesignElement)element);
				continue;
			}
			//后面的元素往上移
			int tmp = element.getY();
			JRDesignElement tmpElement = (JRDesignElement)element;
			tmpElement.setY(currentY);
			currentY = tmp;
			band.removeElement((JRDesignElement)element);
			band.addElement(tmpElement);		
		}
		if("Title".equals(areaBand))
			design.setTitle(band);
		else if("PageHeader".equals(areaBand))
			design.setPageHeader(band);
		else if("ColumnHeader".equals(areaBand))
			design.setColumnHeader(band);
		else if("ColumnFooter".equals(areaBand))
			design.setColumnFooter(band);
		else if("PageFooter".equals(areaBand))
			design.setPageFooter(band);
		else if("LastPageFooter".equals(areaBand))
			design.setLastPageFooter(band);
		else if("Summary".equals(areaBand))
			design.setSummary(band);
		else if("NoData".equals(areaBand))
			design.setNoData(band);
		//计算除了detail区外的文档高度
		int docHeightNodetail = 0;
		if(design.getTitle() != null)
			docHeightNodetail += design.getTitle().getHeight();
		if(design.getPageHeader() != null)
			docHeightNodetail += design.getPageHeader().getHeight();
		if(design.getColumnHeader() != null)
			docHeightNodetail += design.getColumnHeader().getHeight();
		if(design.getColumnFooter() != null)
			docHeightNodetail += design.getColumnFooter().getHeight();
		if(design.getPageFooter() != null)
			docHeightNodetail += design.getPageFooter().getHeight();
		if(design.getLastPageFooter() != null)
			docHeightNodetail += design.getLastPageFooter().getHeight();
		if(design.getSummary() != null)
			docHeightNodetail += design.getSummary().getHeight();
		if(design.getNoData() != null)
			docHeightNodetail += design.getNoData().getHeight();
		docHeightNodetail += design.getTopMargin() + design.getBottomMargin();
		//动态处理detail区域
		elements = design.getDetail().getElements();
		band = (JRDesignBand)design.getDetail();

		if(pageSize > 0)
		{
			//设置每条记录的高度
			int detailHeight = design.getDetail().getHeight();
			int realDetailHeight = design.getPageHeight() - docHeightNodetail;
			int detailPerHeight = realDetailHeight / pageSize;
			if(detailHeight >= detailPerHeight)
				detailPerHeight = detailHeight;
			band = (JRDesignBand)design.getDetail();
			band.setHeight(detailPerHeight);
			design.setDetail(band);
		}
		for(JRElement element : elements)
		{
			String key = element.getKey();
			JRDesignElement textField = null;
			if(element instanceof JRDesignTextField)
				textField = (JRDesignTextField)element;
			if(element instanceof JRDesignStaticText)
				textField = (JRDesignStaticText)element;
			if(detailMap != null && detailMap.get(key) != null)
			{	
				//更新表达式
				JRDesignExpression express = (JRDesignExpression)((JRDesignTextField)textField).getExpression();
				express.setText(String.valueOf(detailMap.get(key)));
				((JRDesignTextField)textField).setExpression(express);

			}
			band.removeElement((JRDesignElement)element);
			textField.setHeight(band.getHeight());
			band.addElement(textField);
			
		}
		design.setDetail(band);
		String classpath = CotExportRptDaoImpl.class.getResource("/").getPath();
		int index = classpath.indexOf("WEB-INF");
		classpath = classpath.substring(1,index) + "reportfile/tmp/" + System.currentTimeMillis() + ".jrxml";		
		JRXmlWriter.writeReport((JRReport)design, classpath, "UTF-8");
		return classpath;
	}
	
	/**  
    * @param h  
    * @return  
    * 实现对map按照value升序排序  
    */  
   @SuppressWarnings("unchecked")   
   public static Map.Entry[] getSortedByValue(Map h) {   
       Set set = h.entrySet();   
       Map.Entry[] entries = (Map.Entry[]) set.toArray(new Map.Entry[set   
               .size()]);   
       Arrays.sort(entries, new Comparator() {   
           public int compare(Object arg0, Object arg1) {   
               Long key1 = Long.valueOf(((Map.Entry) arg0).getValue().toString());   
               Long key2 = Long.valueOf(((Map.Entry) arg1).getValue().toString());   
               return key1.compareTo(key2);   
           }   
       });   
       
       return entries;   
   }  
   public static Double formatNumber(Double num)
   {
	   Double res = 0.00;
	   if(num == null)
		   return res;
	   double tmp = num.doubleValue();
	   DecimalFormat   format = new DecimalFormat("#0.00");
	   res = new Double(format.format(tmp));
	   return res;
   }
	/**
	 * 描述：截取字符串中某个指定符号中的字符串
	 * @param source
	 * @param open
	 * @param close
	 * @return
	 * 返回值：String[]
	 */
	public static String[] stringsBetween(String source,String open,String close)
	{
		return StringUtils.substringsBetween(source, open, close);
	}
	/**
	 * @see 功能描述（必填）：获取员工配置的国家和部门的权限值，转为json
	 * @see 处理流程（选填）：
	 * @see 调用例子（必填）：
	 * @see 相关说明文件：
	 * @see <p>Author:achui</p>
	 * @see <p>Create Time:Jun 19, 2013 10:48:05 PM</p>
	 * @return
	 * 返回值：JSONObject
	 */
	public static JSONObject getEmpDataPopedom(String data){
		JSONObject object = new JSONObject();
		object.put("nation", "0");
		object.put("dept", "0");
		if(StringUtils.isEmpty(data)) return object;
		JSONObject dataView = JSONObject.fromObject(data);
		JSONArray nataionArr = dataView.getJSONArray("nation");
		JSONArray deptArr = dataView.getJSONArray("dept");
		if(!CollectionUtils.isEmpty(nataionArr)){
			object.put("nation",StringUtils.join(nataionArr.iterator(), ","));
		}
		if(!CollectionUtils.isEmpty(deptArr)){
			object.put("dept",StringUtils.join(deptArr.iterator(), ","));
		}
		return object;
	}

}
