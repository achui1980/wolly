<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ taglib uri="/e3/table/E3Table.tld" prefix="e3t"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<jsp:include page="../../common.jsp"></jsp:include>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
	</head>
<script type="text/javascript">
function doAction(obj)
{
	var arr = obj.split("_");
	var cityid = "";
	var areaid = "";
	if(arr[0] == "root")
	{
		cityid = arr[1];
	}
	else
	{
		cityid = arr[0];
		areaid = arr[1];
	}
	parent.f2.getQuery(cityid,areaid);
}
</script>
<body>
	<%= request.getAttribute("treeScript") %>
</body>
</html>