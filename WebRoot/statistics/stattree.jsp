<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<head>
	<title></title>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<script type="text/javascript">
	var globleFilePath = "";	
	var globleOrderBy = "";
	var globleName = "";
	function doAction(url,filePath,orderBy,name)
	{  
		parent.f2.location.href = url;
		globleFilePath = filePath;
		globleOrderBy = orderBy;
		globleName = name;
	}
</script>
</head>
<body>
<table width=150px>
	<tr>
		<td id="tree" style="width:150px;"><%= request.getAttribute("treeScript") %></td>
	</tr>
</table>
</body>