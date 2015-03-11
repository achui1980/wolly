<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
	String webapp=request.getContextPath();
%>
<%@ taglib uri="/e3/table/E3Table.tld" prefix="e3t"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" >
<html>
	<head>
		<jsp:include page="../../common.jsp"></jsp:include>
		<script type='text/javascript'
			src='<%=webapp %>/dwr/interface/cotLoginInfoService.js'></script>
		<script type="text/javascript">
			//权限判断变量，对应于cot_module表中的URL字段
			var url = "cotlogininfo.do?method=query"; 
			//查询
			function query()
			{
				var btn = document.getElementById("btn")
				ajaxAnywhere.formName = "logininfoSearchForm";
				ajaxAnywhere.getZonesToReload = function (url){
				return "loginInfoTableAjaxZone,loginInfoTable";          
				};
				var params = DWRUtil.getValues("logininfoSearchForm");
				ajaxAnywhere.doSubmitAJAX("",params,btn);
			}
		</script>
	</head>
	<body>
		<form name="logininfoSearchForm" id="logininfoSearchForm"
			method="post" action="cotlogininfo.do?method=query">
			<div style="width: 100%;">
				<div style="width: 100%;">
					<div>
						<label>
							起始时间:
						</label>
						<input type="text" name="startTime" id="startTime" class="Wdate"
							onFocus="showPicker(this)" onclick="new WdatePicker(this)" MAXDATE="$endTime" />
						<label>&nbsp;&nbsp;
							结束时间:
						</label>
						<input type="text" name="endTime" id="endTime" class="Wdate"
							onFocus="showPicker(this)" onclick="new WdatePicker(this)" MINDATE="$startTime" />&nbsp;&nbsp;
						<button class="queryBtn" id="btn" onclick="query()">查询</button>
					</div>
				</div>
			</div>
		</form>
		<e3t:table id="loginInfoTable" items="cotLoginInfo" var="logininfo"
			uri="cotlogininfo.do?method=query" mode="ajax" noDataTip="没有记录">

			<e3t:param name="startTime" value="${param.startTime}"></e3t:param>
			<e3t:param name="endTime" value="${param.endTime}"></e3t:param>

			<e3t:column property="loginEmpid" title="登陆人" mappingItem="allEmpsName"  />
			<e3t:column property="loginName" title="登陆名称" style="text-align:center"/>
			<e3t:column property="loginIpaddr" title="登陆IP地址" style="text-align:center"/>
			<e3t:column property="loginTime" title="登陆时间" style="text-align:center"/>
			<e3t:row>
				<e3t:attribute name="onmouseover"
					value="this.style.backgroundColor = '#CCCCFF'" />
				<e3t:attribute name="onmouseout"
					value="this.style.backgroundColor = ''" />
			</e3t:row>
		</e3t:table>
	</body>
</html>
