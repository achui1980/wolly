<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
	String webapp=request.getContextPath();
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" >
<html>
	<head>

		<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
		<title>Credite Note</title>
		<!-- 导入公共js和css -->
		<jsp:include page="/extcommon.jsp"></jsp:include>
		 <!-- 表格中的复选框列 -->
		<script type="text/javascript" src="<%=webapp %>/common/ext/ux/CheckColumn.js"></script>
		 <!-- 单个文件上传css -->
		<link rel="stylesheet" type="text/css" href="<%=webapp%>/common/ext/ux/css/file-upload.css" />
		<!-- 单个文件上传js -->
		<script type='text/javascript' src='<%=webapp%>/common/ext/ux/FileUploadField.js'></script>
		<!-- 合计行 -->
		<link rel="stylesheet" type="text/css" href="<%=webapp %>/common/ext/ux/css/GroupSummary.css" />
		<script type="text/javascript" src="<%=webapp %>/common/ext/ux/GroupSummary.js"></script>
		<style>
			.x-grid3-gridsummary-row-inner{overflow:hidden;width:100%;}/* IE6 requires width:100% for hori. scroll to work */
			.x-grid3-gridsummary-row-offset{width:10000px;}
			.x-grid-hide-gridsummary .x-grid3-gridsummary-row-inner{display:none;}
		</style>
		
		<!-- 图片鼠标拖拉选择框 -->
		<script type="text/javascript" src="<%=webapp %>/common/ext/ux/DataView-more.js"></script>
		
		<!-- 导入dwr -->
		<script type='text/javascript'
			src='<%=webapp %>/dwr/interface/cotOrderOutService.js'></script>
		<script type='text/javascript'
			src='<%=webapp %>/dwr/interface/cotElementsService.js'></script>
		<script type='text/javascript'
			src='<%=webapp %>/dwr/interface/sysdicutil.js'></script>
		<script type='text/javascript'
			src='<%=webapp %>/dwr/interface/setNoService.js'></script>
		<script type='text/javascript'
			src='<%=webapp %>/dwr/interface/queryService.js'></script>
		<!-- MAP -->
		<script type="text/javascript" src="<%=webapp %>/common/js/map.js"></script>
		
		<script type="text/javascript" src="<%=webapp %>/common/js/uploadpanel.js"></script>
		<script type="text/javascript" src="<%=webapp %>/common/js/printPanel.js"></script>
		<script type='text/javascript' src='<%=webapp %>/orderoutdel/js/orderOutAdd.js'></script>
		<script type="text/javascript" src="<%=webapp %>/commonQuery/js/eleQuery.js"></script>
		<script type="text/javascript" src="<%=webapp %>/common/js/popedom.js"></script>
		<style type="">
			.x-grid-record-green  tr{ 
			    /*background: darkgreen;*/
			    background:lightgreen ;
			}
			.x-grid-record-yellow  tr{ 
			    /*background: darkgreen;*/
			    background:khaki ;
			}
		</style>
		<script type="text/javascript">
			function addTax(){
				$('addTaxBtn').style.display='none';
				$('mudTaxBtn').style.display='';
				$('addSpan').style.display='';
				
				var total=$('totalLab').innerText;
				
				//设置值
				if(total=='' || isNaN(total)){
					total=0;
				}
				var tax=(total*0.25).toFixed('2');
				var totalMoney = Number(total).add(tax);
				$('taxLab').innerText=tax;
				$('totalTaxLab').innerText=totalMoney;
			}
			function mudTax(){
				$('addTaxBtn').style.display='';
				$('mudTaxBtn').style.display='none';
				$('addSpan').style.display='none';
				$('taxLab').innerText='0';
				$('totalTaxLab').innerText='0';
			}
		</script>
	</head>
	<%
		String id = request.getParameter("id");
	%>
	<body onload="mask('Loading')">
		<input type="hidden" id="pId" name="pId" value="<%=id%>" />
		<input type="hidden" id="con20" />
		<input type="hidden" id="con40" />
		<input type="hidden" id="con40H" />
		<input type="hidden" id="con45" />
		
		<input type="hidden" name="hsInfoId" id="hsInfoId" />
		<input type="hidden" name="symbolId" id="symbolId" />
		<input type="hidden" id="orderouthsRptId" name="orderouthsRptId" />
		
		<!-- 总金额 -->
		<div class="tab" id="talPriceDiv"
			style="position: absolute; top: 143px; left:700px;display: none;">
			<button id='addTaxBtn' style="" onclick="addTax()">＋</button>
			<button id='mudTaxBtn' style="display:none;" onclick="mudTax()">－</button>
			
			<label style="font-size: 10pt; color: #10418C; font: bold">
				Total Amount:
			</label>
			<label id="totalLab" style="color: red; font-size: 12; font: bold">
				0
			</label>
			<span id="addSpan" style="display: none;">
				<label
					style="font-size: 10pt; color: #10418C; font: bold; margin-left: 20;">
					+
				</label>
				<label id="taxLab" style="color: red; font-size: 12; font: bold;">
					0
				</label>
				<label
					style="font-size: 10pt; color: #10418C; font: bold; margin-left: 20;">
					=
				</label>
				<label id="totalTaxLab" style="color: red; font-size: 12; font: bold;">
					0
				</label>
			</span>
		</div>
		<!-- 上传麦标成功标识 -->
		<input type="hidden" name="uploadSuc" id="uploadSuc" />
	</body>
</html>
