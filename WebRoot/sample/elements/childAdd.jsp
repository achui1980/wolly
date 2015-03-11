<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%
	String webapp=request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+webapp+"/";
	//String empId = request.getSession().getAttribute("empId");
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" >
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
		<title>Sub-sample Add page</title>
		<jsp:include page="/extcommon.jsp"></jsp:include>
		<script type="text/javascript">
			getFacPodom('cotelements.do');
		</script>
		<link rel="stylesheet" type="text/css" href="<%=webapp %>/common/ext/resources/css/file-upload.css" />
		<script type="text/javascript" src="<%=webapp %>/common/ext/ux/FileUploadField.js"></script>
		<script type="text/javascript" src="<%=webapp %>/common/js/uploadpanel.js"></script>
		<script type='text/javascript' src='<%=webapp%>/dwr/interface/cotElementsService.js'></script>
		<script type='text/javascript' src='<%=webapp%>/sample/elements/js/childAdd.js'></script>
	</head>
<%
	String parentId = request.getParameter("parentId");//父id
	String PEleId = request.getParameter("PEleId");//Article No.
%>
<body>
<input type="hidden" name="parentId" id="parentId" value="<%=parentId%>" />
<input type="hidden" name="PEleId" id="PEleId" value="<%=PEleId%>" />
<input type="hidden" id="basePath" name="basePath" value="<%=basePath %>">
</body>
</html>