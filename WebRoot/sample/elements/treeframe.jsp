<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
	String webapp=request.getContextPath();
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" >
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
		<title>Packing information page</title>
		<!-- 导入公共js和css -->
		<jsp:include page="../../common.jsp"></jsp:include>
		<!-- 导入dwr -->
		<script type='text/javascript' src='<%=webapp %>/dwr/interface/cotElementsService.js'></script>
		<script type="text/javascript">
		  function doAction(obj,queryType)
  		  {
  			var arr = obj.split("_");
  			var factoryId = "";
  			var typeId = "";
  			if(queryType == "factory")
  			{
	  			if(arr[0] == "root")
	  			{
	  				factoryId = arr[1];
	  			}
	  			else
	  			{
	  				factoryId = arr[0];
	  				typeId = arr[1];
	  			}
  			}
  			else if(queryType == "type")
  			{
  				if(arr[0] == "root")
	  			{
	  				typeId = arr[1];
	  			}
	  			else
	  			{
	  				typeId = arr[0];
	  				factoryId = arr[1];
	  			}
  			}
  			 parent.queryByFacAndType(factoryId,typeId);
  		  }
  		  function closeWin()
  		  {
  		  	parent.document.getElementById("overlayer").style.display = "none";
  		  }
  		  function getQueryTree(type)
  		  {
  		  	var frame = parent.document.typetree;
  		  	frame.location.href = "cotelements.do?method=query&type="+type;
  		  }
  		  function onpageload()
  		  {
  		  	
  		  	 var type = document.getElementById("displaytype").value;
  		  	 if(type == "factory")
  		  	 {
  		  	 	var radio = document.getElementById("factory");
  		  	 	radio.checked = "true";
  		  	 }
  		  	 if(type == "type")
  		  	 {
  		  	 	var radio = document.getElementById("type");
  		  	 	radio.checked = "true";
  		  	 }
  		  }
  		  function getHTML()
  		  {
  		  	//window.prompt(document.getElementById("tree").innerHTML,"")
  		  	return document.getElementById("tree").innerHTML;
  		  }
		</script>
	</head>
	<%
		String type = request.getParameter("type");
	 %>
<body >
<table width=150px>
	<tr>
		<td id="tree" style="width:150px;"><%= request.getAttribute("treeScript") %></td>
	</tr>
</table>
<input type="hidden" id="displaytype" value=<%=type %>> 
</body>
</html>