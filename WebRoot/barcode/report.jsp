<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
	String webapp=request.getContextPath();
%>
<%
 int pageIndex = 0;
 int lastPageIndex = 0;
 String pageStr = request.getParameter("pageIndex");
  lastPageIndex = Integer.parseInt(request.getAttribute("lastPageIndex").toString());
 try{
    if( pageStr != null)
        pageIndex = Integer.parseInt(pageStr);
 }catch(Exception e){
    //e.printStackTrace();
 }
%>

<html>
<head>
<title>Export barcodes and print preview page</title>
<meta http-equiv="Content-Type" content="text/html; charset=gbk">
<jsp:include page="/common.jsp"></jsp:include>
<link rel="stylesheet" type="text/css" href="CSS/style.css">
<style media=print> 
		.Noprint{display:none;} 
		.noshow{visibility:hidden} 
		.PageNext{page-break-after: always;} 
</style> 
<script type="text/javascript">

var HKEY_Root,HKEY_Path,HKEY_Key; 
HKEY_Root="HKEY_CURRENT_USER"; 
HKEY_Path="\\Software\\Microsoft\\Internet Explorer\\PageSetup\\"; 
		//设置网页打印的页眉页脚为空 
function PageSetup_Null() 
{ 
	//判断是否有样品数据
	var pageCount = document.getElementById("pageCount").value;
	if(pageCount==0){
		document.getElementById("previewDiv").style.display = 'none';
		document.getElementById("noData").style.top=document.body.offsetHeight/2;
		document.getElementById("noData").style.left=document.body.offsetWidth/2;
		document.getElementById("noData").style.display = 'block';
	}else{
		document.getElementById("previewDiv").style.display = 'block';
		document.getElementById("noData").style.display = 'none';
	}
 try 
 { 
  var Wsh=new ActiveXObject("WScript.Shell"); 
  HKEY_Key="header"; 
  Wsh.RegWrite(HKEY_Root+HKEY_Path+HKEY_Key,""); 
  HKEY_Key="footer"; 
  Wsh.RegWrite(HKEY_Root+HKEY_Path+HKEY_Key,""); 
 } 
 catch(e){
 	//printObject(e);
 } 
} 
function printContent()
{
	var obj = document.getElementById("WebBrowser");
	obj.ExecWB(7,1);
}
//判断是否有导出权限
function checkExpPopdeom()
{
	return false;
}
//选择页面
function changePage(value){
	window.location.reload('previewrpt.do?method=queryEleRpt&queryAgain=false&pageIndex='+value);
}
//本地打印
function localPrint(value){
	window.print();
}
</script>
</head>
<body onload="PageSetup_Null()">
<div id="previewDiv">
<table width="98%" cellpadding="0" cellspacing="0" border="0" height="22">
	<tr>
      <td>       
      	<div class="Noprint">
	      	<a href="<%=webapp %>/downloadRpt.action?queryAgain=false&printType=PDF"><img src="<%=webapp %>/common/images/tool_pdf.gif" border="0" alt="PDF格式"></a>
	      	<a href="<%=webapp %>/downloadRpt.action?queryAgain=false&printType=XLS"><img src="<%=webapp %>/common/images/tool_xls.gif" border="0" alt="Excel格式"></a>
	        <a href="<%=webapp %>/downloadRpt.action?queryAgain=false&printType=PRINT"><img src="<%=webapp %>/common/images/printIcon.gif" border="0" alt="打印"></a>
	        <a href="#"><img src="<%=webapp %>/common/images/previewIcon.gif" border="0" onclick="printContent()" alt="打印预览"></a>
	        <a onclick="localPrint()" style="text-decoration: underline; cursor: hand;"><img src="<%=webapp %>/common/images/printIcon.gif" border="0" alt="打印"></a>
	        <a href="#"></a>
        <%
          if (pageIndex > 0)
          {
            %>
            <a href="previewrpt.do?method=queryEleRpt&queryAgain=false&pageIndex=0"><img src="<%=webapp %>/common/images/first.gif" alt="第一页" border="0"></a>
            <a href="previewrpt.do?method=queryEleRpt&queryAgain=false&pageIndex=<%=pageIndex - 1%>"><img src="<%=webapp %>/common/images/previous.gif" alt="上一页" border="0"/></a>
            <%
          }
          else
          {
            %>
            <img src="<%=webapp %>/common/images/first_grey.gif" disabled="true" border="0"/>
            <img src="<%=webapp %>/common/images/previous_grey.gif" disabled="true" border="0"/>
            <%
          }
 
          if (pageIndex < lastPageIndex-1)
          {
            %>
            <a href="previewrpt.do?method=queryEleRpt&queryAgain=false&pageIndex=<%=pageIndex + 1%>"><img src="<%=webapp %>/common/images/next.gif" alt="下一页" border="0"></a>
            <a href="previewrpt.do?method=queryEleRpt&queryAgain=false&pageIndex=<%=lastPageIndex-1%>"><img src="<%=webapp %>/common/images/last.gif" alt="最后页" border="0"></a>
            <%
          }
          else
          {
            %>
            <img src="<%=webapp %>/common/images/next_grey.gif" border="0">
            <img src="<%=webapp %>/common/images/last_grey.gif" border="0">
            <%
            }
            %>
            <label>Current<%=pageIndex+1%>/<%=lastPageIndex%>Page,</label>
            <label>go to</label>
            <select onchange="changePage(this.value)" id="changeSel">
            	<%for(int i=0;i<lastPageIndex;i++){ %>
            		<%if(i==0){%>
            			<option value="">Pls. select</option>
            		<%} %>
	            	<%if(i<9){%>
	            		<option value="<%=i%>">0<%=i+1%></option>
	            	<%}else{ %>
	            		<option value="<%=i%>"><%=i+1%></option>
	            	<%} %>
            	<%} %>
            </select>
            <label>页</label>
            </div>
            </td>
   </tr>
        <tr>
		 <td align="left">
			 <%=request.getAttribute("preview")%>
			 </td>
		</tr>
</table>
</div>
<input type="hidden" value="<%=lastPageIndex%>" id="PageCount">
<div id="noData" style="display: none;position:absolute;"><H3>There are no sample data！<a href="#" onclick="javascript:window.close();">Close</a></H3></div>
<object id="WebBrowser" classid="CLSID:8856F961-340A-11D0-A96B-00C04FD705A2" height="0" width="0"></object>
</body>
</html>

