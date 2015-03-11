<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib uri="http://www.ecside.org" prefix="ec"%>
<%
	String webapp=request.getContextPath();
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" >
<html>
	<head>
		<jsp:include page="/common.jsp"></jsp:include>
		<!-- 导入dwr -->
		<script type='text/javascript' src='<%=webapp %>/dwr/interface/cotOrderService.js'></script>
		<!-- 导入ECTable -->
	    <link rel="stylesheet" type="text/css" href="<%=webapp %>/ecside/css/ecside_style.css" />
	    <script type="text/javascript" src="<%=webapp %>/ecside/js/ecside.js"></script>
	    <script type="text/javascript" src="<%=webapp %>/ecside/js/prototype_mini.js"></script>
	    <script type="text/javascript" src="<%=webapp %>/ecside/js/ecside_msg_utf8_cn.js"></script>
		<script type="text/javascript">
		
			
			//导出方法
			function exportRpt(){
				var model = $('reportTemple').value;
				if(model==''){
					alert('请选择导出模板!');
					return;
				}
				var form = document.getElementById("printForm");
				form.action = "<%=webapp %>/downRpt.action"; 
				form.target = "";
				form.method = "post";
				form.submit();
			}
			
			//预览方法
			function viewRpt(){
				var _height = window.screen.height;
				var _width = window.screen.width;
				var model = $('reportTemple').value;
				if(model==''){
					alert('请选择导出模板!');
					return;
				}
				var form = document.getElementById("printForm");
				form.action = "<%=webapp %>/newpreviewrpt.do?method=queryRpt"; 
				//打开新页面  
				form.target = "new"; 
				form.method = "post";
				form.submit();
			}
			
			//打印按钮
			function openPrint(){
				var model = $('reportTemple').value;
				if(model==''){
					alert('请选择导出模板!');
					return;
				}
				var form = document.getElementById("printForm");
				form.action = "<%=webapp %>/downRpt.action?printType=PRINT"; 
				form.target = "";
				form.method = "post";
				form.submit();
			}
			
			//发邮件
			function sendMail(){
				var model = $('reportTemple').value;
				if(model==''){
					alert('请选择导出模板!');
					return;
				}
				var customerEmail = parent.$('customerEmail').value;
				if(customerEmail==''){
					alert('该客户还没有邮箱,请先去客户管理中添加!');
					return;
				}
				//设置层显示位置
				var width = window.screen.availWidth/3;
				var height = window.screen.availHeight/4;
				parent.$("mailDiv").style.top=height;
				parent.$("mailDiv").style.left=width;
				parent.$('mailDiv').style.display='block';
				setTimeout("openMail()",600);
			}
			
			//打开邮件发送页面
			function openMail(){
				//所需参数
				var mail = {};
				mail.custEmail = parent.$('customerEmail').value;
				mail.custId = parent.$('custId').value;
				mail.custName = parent.$('customerShortName').value;
				mail.orderId = parent.$('pId').value;
				mail.orderNo = parent.$('orderNo').value;
				mail.reportTemple = $('reportTemple').value;
				mail.printType = $('printType').value;
				DWREngine.setAsync(false);
				cotOrderService.saveMail(mail,function(res){
					parent.$('mailDiv').style.display='none';
					var id = res;
					if(id==null){
						alert('邮件配置失败,请联系管理员!');
					}else{
						openWindow('cotmail.do?method=modify&id='+id);
					}
					parent.$('mailDiv').style.display='none';
					parent.$('newPrintDiv').style.display='none';
				});
				DWREngine.setAsync(true);
			}
			
			
			
			//关闭
			function closeDiv(){
				parent.document.getElementById('newPrintDiv').style.display='none';
			}
			
			//加载下拉框数据
			function bindSel(){
				// 加载报表模版
				queryService.getRptFileList(5, function(res) {
					bindSelect('reportTemple', res, 'rptfilePath', 'rptName');
					//查询默认的模板
					queryService.getRptDefault(5, function(def) {
						if(def!=null){
							DWRUtil.setValue('reportTemple',def.rptfilePath);
						}
					});
				});
			}
			
			//初始化页面
			function initForm(){
				bindSel();
			}
			
			 
		</script>
	</head>
	 <%
		String mainId = request.getParameter("mainId");
	 %>
	<body onload="initForm()">
		<div class="toolbar">
			<div style="float: right;margin-right: 3px;">
			    <a onclick="closeDiv()" style="cursor: hand;margin-right:0px;"><img
					src="<%=webapp %>/common/images/turn_off.png" border="0"
					height="21px" width="15px"></a>							
			</div>
		</div>
		<form id="printForm" name="printForm" method="post" action="">
			<input type="hidden" name="mainId" id="mainId" value="<%= mainId %>"/>
			<div style="width: 100%;margin-top: 5px">
				<label class="label_18" style="text-align: right;color: black">
					导出模板：
				</label>
				<select class="select_30" name="reportTemple" id="reportTemple" >
					<option value="">
						请选择
					</option>
				</select>
				<label class="label_18" style="text-align: right;color: black">
					导出类型：
				</label>
				<select class="select_30" name="printType" id="printType" >
					<option value="XLS">
						XLS
					</option>
					<option value="PDF">
						PDF
					</option>
				</select>
			</div>
			<hr size="1px;">
			<div style="width: 100%;">
				<div style="width: 64%;float: left;">
					<div style="width: 100%;">
						<label class="label_16" style="color: black">
							每&nbsp;
						</label>
						<input class="input_12"  name="eleNum" id="eleNum" />
						<label class="label_48"  >
							&nbsp;个货号打印一张合同
						</label>
					</div>	
					<div style="width: 100%;margin-top: 5px;display:none">
						<input type="checkbox" name="HEADER_PER_PAGE" id="HEADER_PER_PAGE"
								style="float: left;margin-left: 30px; margin-top: 3px;"/>
						<label class="label_48" >
							&nbsp;&nbsp;表头分页显示
						</label>
					</div>
					<div style="width: 100%; display:none">
						<input type="checkbox" name="mHead" id="mHead"
								style="float: left;margin-left: 30px; margin-top: 3px;"/>
						<label class="label_48" >
							&nbsp;&nbsp;唛头资料打印
						</label>
					</div>
					<div style="width: 100%;">
						<input type="checkbox" name="eleDesc" id="eleDesc"
								style="float: left;margin-left: 30px; margin-top: 3px;"/>
						<label class="label_48" >
							&nbsp;&nbsp;货物描述部分作为附页
						</label>	
					</div>
					<div style="width: 100%;">
						<input type="checkbox" name="insure" id="insure"
								style="float: left;margin-left: 30px; margin-top: 3px;"/>
						<label class="label_48" >
							&nbsp;&nbsp;打印保险类别
						</label>	
					</div>
					<div style="width: 100%;">
						<input type="checkbox" name="bank" id="bank"
								style="float: left;margin-left: 30px; margin-top: 3px;"/>
						<label class="label_48" >
							&nbsp;&nbsp;打印银行资料
						</label>	
					</div>
					<div style="width: 100%;">
						<input type="checkbox" name="Sum" id="Sum"
								style="float: left;margin-left: 30px; margin-top: 3px;"/>
						<label class="label_48" >
							&nbsp;&nbsp;打印合计数
						</label>	
					</div>
					<div style="width: 100%;">
						<input type="checkbox" name="handlingFee" id="handlingFee"
								style="float: left;margin-left: 30px; margin-top: 3px;"/>
						<label class="label_48" >
							&nbsp;&nbsp;额外费用
						</label>	
					</div>
					<div style="width: 100%;">
						<input type="checkbox" name="isTran" id="isTran"
								style="float: left;margin-left: 30px; margin-top: 3px;"/>
						<label class="label_48" >
							&nbsp;&nbsp;允许分批装运和转运
						</label>	
					</div>
					<div style="width: 100%;">
						<input type="checkbox" name="clauseType" id="clauseType"
								style="float: left;margin-left: 30px; margin-top: 3px;"/>
						<label class="label_48" >
							&nbsp;&nbsp;付款条款
						</label>	
					</div>
					<div style="width: 100%;">
						<input type="checkbox" name="isGiven" id="isGiven"
								style="float: left;margin-left: 30px; margin-top: 3px;"/>
						<label class="label_48" >
							&nbsp;&nbsp;是否送样
						</label>	
					</div>
					<div style="width: 85%;margin-left: 30px;margin-top: 8px">
						<fieldset title="唛头信息">
							<legend>
								唛头信息
							</legend>
							<input type="checkbox" name="mHeadText" id="mHeadText"
									style="float: left;margin-left: 10px; margin-top: 3px;"/>
							<label class="label_28" style="color: black">
								&nbsp;文本唛头
							</label>	
							<input type="checkbox" name="mHeadPic" id="mHeadPic"
									style="float: left;margin-left: 20px; margin-top: 3px;"/>
							<label class="label_28" style="color: black">
								&nbsp;图片唛头
							</label>
						</fieldset>
					</div>
				</div>
				<br/>
				<div style="width: 35%;float: left;">
				    <fieldset style="width: 85%">
						<legend>
							输出排列顺序
						</legend>
						<div style="width: 100%;margin-top: 8px">
							<input type="checkbox" name="orderByOutEle" id="orderByOutEle"
									style="float: left;margin-left: 20px; margin-top: 3px;"/>
							<label class="label_48" >
								&nbsp;&nbsp;外商货号
							</label>	
						</div>
						<div style="width: 100%;margin-top: 8px">
							<input type="checkbox" name="orderByInEle" id="orderByInEle"
									style="float: left;margin-left: 20px; margin-top: 3px;"/>
							<label class="label_48" >
								&nbsp;&nbsp;我司货号
							</label>	
						</div>
						<div style="width: 100%;margin-top: 8px;margin-bottom: 8px;">
							<input type="checkbox" name="orderByEleType" id="orderByEleType"
									style="float: left;margin-left: 20px; margin-top: 3px;"/>
							<label class="label_48" >
								&nbsp;&nbsp;产品种类
							</label>	
						</div>
					</fieldset>
					<br/>
					<br/>
					<fieldset style="width: 85%;">
						<legend>
							输出货号选择
						</legend>
						<div style="width: 100%;margin-top: 8px">
							<input type="checkbox" name="outEle" id="outEle"
									style="float: left;margin-left: 20px; margin-top: 3px;"/>
							<label class="label_48" >
								&nbsp;&nbsp;外商货号
							</label>	
						</div>
						<div style="width: 100%;margin-top: 8px">
							<input type="checkbox" name="inEle" id="inEle"
									style="float: left;margin-left: 20px; margin-top: 3px;"/>
							<label class="label_48" >
								&nbsp;&nbsp;我司货号
							</label>	
						</div>
						<div style="width: 100%;margin-top: 8px;margin-bottom: 8px;">
							<input type="checkbox" name="facEle" id="facEle"
									style="float: left;margin-left: 20px; margin-top: 3px;"/>
							<label class="label_48" >
								&nbsp;&nbsp;供商货号
							</label>	
						</div>
					</fieldset>
				</div>
			</div>
		</form>
		<hr size="1px;">
		<div align="center">
			<a onclick="exportRpt()" style="cursor: hand; margin-right: 0px;"><img
					src="<%=webapp %>/common/images/query_output.gif" border="0"
					id="exportRptBtn" height="21px" width="61px"> </a>
			<!-- 
			<a onclick="viewRpt()" style="cursor: hand; margin-right: 0px;"><img
					src="<%=webapp %>/common/images/_preview.gif" border="0"
					id="viewRptBtn" height="21px" width="61px"> </a>
			 -->
			<a onclick="viewRpt()" style="cursor: hand; margin-right: 0px;"><img
					src="<%=webapp %>/common/images/_print.gif" border="0"
					id="viewRptBtn" height="21px" width="61px"> </a>
			<a onclick="sendMail()" style="cursor: hand; margin-right: 0px;"><img
					src="<%=webapp %>/common/images/_sendmail.gif" border="0"
					id="sendMBtn" height="21px" width="61px"> </a>
		</div>
		
	</body>
</html>
