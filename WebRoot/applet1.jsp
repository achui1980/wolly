<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>


<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    
    <title>Report Preview</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->

  </head>
  <%
  	//String webPath = request.getContextPath();
  	//String url = request.getRequestURL().toString();
  	//String servletPath = request.getServletPath();
  	//url = url.replaceAll(servletPath,"");
  	//String queryString = String.valueOf(request.getAttribute("strSql"));
  	//String flag = String.valueOf(request.getAttribute("headerFlag"));
  	//String reportTemple = String.valueOf(request.getAttribute("reportTemple"));
  	//String webPath = request.getContextPath();
  	String url = request.getRequestURL().toString();
  	String servletPath = request.getServletPath();
  	url = url.replaceAll(servletPath,"");
  	String rndFlag = (String)request.getAttribute("rndFlag");
  	String printFlag = (String)request.getAttribute("printFlag");
  	//out.println("printFlag"+printFlag);
   %>
  <body>
   <OBJECT classid="clsid:8AD9C840-044E-11D1-B3E9-00805F499D93"  
        WIDTH = 100% HEIGHT = "100%"  codebase="<%=url%>/jre-6u14-windows-i586.exe">  
           
        <PARAM NAME = CODE VALUE  = "EmbeddedViewerApplet.class" >  
        <PARAM NAME = CODEBASE VALUE  = "applets" >  
        <PARAM NAME = ARCHIVE VALUE  = "jasperreports-3.0.0-applet.jar,jasperreports-3.0.0.jar,jcommon-1.0.0.jar,jfreechart-1.0.0.jar" >  
        <PARAM NAME = "type" VALUE ="application/x-java-applet;version=1.5.0">  
        <PARAM NAME = "scriptable" VALUE ="false">  
        <PARAM NAME = "REPORT_URL" VALUE  ="<%=url%>/servlet/AppletServlet?rndFlag=<%=rndFlag %>">  
        <PARAM NAME = "PRINT_FLAG" VALUE  ="<%=printFlag%>">  
           
        <COMMENT>  
            <EMBED    
                    type = "application/x-java-applet;version=1.5.0"    
                    CODE = "EmbeddedViewerApplet.class"    
                    CODEBASE = "applets"  
                    ARCHIVE = "jasperreports-3.0.0-applet.jar,jasperreports-3.0.0.jar,jcommon-1.0.0.jar,jfreechart-1.0.0.jar"  
                    scriptable = "false"    
                    REPORT_URL = "<%=url%>/servlet/AppletServlet?rndFlag=<%=rndFlag %>" 
                    PRINT_FLAG =  "<%=printFlag%>" 
                    pluginspage = "<%=url%>/jre-6u14-windows-i586.exe" width="100%" height="100%"> 
            </EMBED>  
        </COMMENT>  
       
    </OBJECT>  
  </body>
</html>
