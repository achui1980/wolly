<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%
	String webapp=request.getContextPath();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" >
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
		<title>单号设置页面</title>
		<jsp:include page="../extcommon.jsp"></jsp:include>
		<script type='text/javascript' src='<%=webapp %>/dwr/interface/setNoService.js'></script>
		
		<!-- MAP -->
		<script type="text/javascript" src="<%=webapp %>/common/js/map.js"></script>
		<script type='text/javascript' src='<%=webapp %>/system/setNo.js'></script>
		<script type="text/javascript"> 

		

		
		

		
	    </script>
	</head>

	<body>
					
		<div style="display:none">
			<form action="" id = "hiddenForm">
			----------------------------------------------
			<div style="width: 100%; margin-top: 3px">
				<label class="label_14">
					退税单编号：
				</label>
				<textarea class="input_30" rows="1" name="backtaxNo" id="backtaxNo">TX</textarea>
				<label class="label_8">
					当前序列号：
				</label>
				<input class="input_5"  name="backtaxNoSeq" id="backtaxNoSeq" disabled="disabled" value="0"/> 
				
				<label class="label_5" >
					归零方式：
				</label>
				<select class="input_5" name="backtaxZeroType" id="backtaxZeroType">
					<option value="0">系统</option>
					<option value="1">按年</option>
					<option value="2">按月</option>
					<option value="3">按日</option>
				</select>
				<button onclick="toZero(backtaxNoSeq)" id="toZeroBtn7"   style="margin-top: 2px">
					归零
				</button> 
			</div>
			<div style="width: 100%; margin-top: 3px">
				<label class="label_14">
					核销单编号：
				</label>
				<textarea class="input_30" rows="1" name="auditNo" id="auditNo">HX</textarea>
				<label class="label_8">
					当前序列号：
				</label>
				<input class="input_5"  name="auditNoSeq" id="auditNoSeq" disabled="disabled" value="0"/> 
				
				<label class="label_5" >
					归零方式：
				</label>
				<select class="input_5" name="auditZeroType" id="auditZeroType">
					<option value="0">系统</option>
					<option value="1">按年</option>
					<option value="2">按月</option>
					<option value="3">按日</option>
				</select>
				<button onclick="toZero(auditNoSeq)" id="toZeroBtn7"   style="margin-top: 2px">
					归零
				</button> 
			</div>
			</form>
		</div>
	</body>
</html>
