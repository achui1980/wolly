<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%
	String webapp=request.getContextPath();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"  >
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
		<title>""</title>
		<jsp:include page="../../common.jsp"></jsp:include>
		<script type='text/javascript'src='<%=webapp %>/dwr/interface/cotBoxPackingService.js'></script>
		<link rel="stylesheet" type="text/css" href="<%=webapp %>/ecside/css/ecside_style.css" />
		<script type="text/javascript" src="<%=webapp %>/ecside/js/ecside.js"></script>
		<script type="text/javascript" src="<%=webapp %>/ecside/js/prototype_mini.js"></script>
		<script type="text/javascript" src="<%=webapp %>/ecside/js/ecside_msg_utf8_cn.js"></script>
		<script type="text/javascript">
		
	 //添加或修改记录
	function saveOrUpdate()
	{   
		//验证公式是否正确
		DWREngine.setAsync(false);
		var str = DWRUtil.getValue("checkCalculation");
	    var checkRes;
	    cotBoxPackingService.checkCalculation(str,function(res){
			  checkRes = res
		})
	    if(checkRes==false){
			 alert("Formula has an error, please re-enter!");
			 return;
		}
	   var ids = $('boxpackingid').value;
	   var formulaIn = $("formulaIn").value;
	   var formulaOut = $("formulaOut").value;
	   var checkCalculation = $("checkCalculation").value;
		cotBoxPackingService.updateCalculator(ids,formulaIn,formulaOut,checkCalculation,function(res){
			parent.$('calDiv').style.display = 'none';
			alert("Success!");
		});
		DWREngine.setAsync(true);
	}
	  
	//外部表达式显示公式编辑层
	function showCalculatorDiv(e){
		var obj = $("calculatorDiv").style.display;
		if(obj=='none'){
			//设置层显示位置
			var t=e.offsetTop;
			var l=e.offsetLeft;
			while(e=e.offsetParent){
				t+=e.offsetTop;
				l+=e.offsetLeft;
			}
			//加载客户查询表格
			var frame = document.calculatorFrame;
			frame.location.href = "cotboxpacking.do?method=openCalculator";
			document.getElementById("calculatorDiv").style.top=t+20;
			document.getElementById("calculatorDiv").style.left=l+30;
			document.getElementById("calculatorDiv").style.display ="block";
		}else{
			$("calculatorDiv").style.display='none';
		}
	}
	//关闭公式编辑层
	function closeCalculatorDiv(){
	      $("calculatorDiv").style.display = "none";
	}
	//关闭层
	function closeDiv(){
		$("calculatorDiv").style.display = 'none';
	} 	
	
	//填写内部表达式表单
	function setCheckCalculation(e){
		  $("checkCalculation").value = $("checkCalculation").value + e;
	}
	
	//填写内部表达式表单
	function setExpessionInForm(e){
		  $("formulaIn").value = $("formulaIn").value + e;
	}
	
	//填写对外表达式表单
	function setExpressionOutForm(e){
		  $("formulaOut").value = $("formulaOut").value + e;
	}
	
	//清空内部表达式&对外表达式表单
	function clearForm(){
	      $("formulaIn").value = "";
		  $("formulaOut").value = "";
		  $("checkCalculation").value = "";
	}	
	//关闭
	function closeDiv(){
		parent.$('calDiv').style.display = 'none';  
	}		
</script>
		<style type="text/css">
</style>
	</head>
	<%
		String ids = request.getParameter("packIds");
	%>
	<body onload="">
	<!-------- 下拉隐藏层 ----------->
		<div id="search"
			style="display: none; position: absolute; z-index: 800;width: 240; background-color: #E2F4FF; border: solid 1px; border-color: black;"></div>
		<input type="hidden" name="boxpackingid" id="boxpackingid" value="<%=ids%>" />
		<div align="center">
			<form name="queryBoxPackingForm" id="queryBoxPackingForm" onsubmit="return false">
				<div style="width: 100%;" align="center">
					<textarea class="input_80" name="formulaOut" id="formulaOut"  style="width: 100%;"
						onclick="showCalculatorDiv(this)" readonly="readonly" rows="8" cols="50"></textarea>
					<input type="hidden" name="checkCalculation" id="checkCalculation"/>			 
					<input type="hidden" name="formulaIn" id="formulaIn"   />					
				</div>						
			</form>
		</div>

		<div style="width: 100%; margin-top: 10px" align="center">
			<div style="float: left;margin-left: 35%">
				<a onclick="saveOrUpdate()" style="cursor: hand;"><img
						src="<%=webapp %>/common/images/_save.gif" border="0" height="21px"
						width="61px"> </a>
			</div>
			<div style="float: left; margin-left: 5px">
				<a onclick="closeDiv()"
					style="cursor: hand;"><img
						src="<%=webapp %>/common/images/_cancel.gif" border="0"
						height="21px" width="61px"> </a>
			</div>
		</div>
		<!-------------------------- 公式编辑隐藏层 ------------------------------------->
		<div id="calculatorDiv" onmousedown="beginDrag(this,event);"
			style="display: none; position: absolute; width: 380px; background-color: #99CCFF;padding: 0.5%;"
			align="center">
			<div class="toolbar" style="width: 100%; cursor: hand;">
				<div style="float: left; margin-top: 3px; margin-left: 5px;">
					<font style="font-size: 9pt; color: #306885; font-weight: bold;">
						Formula set</font>
				</div>
				<div
					style="float: right; margin-top: 1px; margin-right: 0px; cursor: move;">
					<a onclick="closeDiv()" style="cursor: hand;"><img
							src="<%=webapp %>/common/images/close.gif" border="0" alt="关闭"
							height="13px" width="15px"> </a>
				</div>
			</div>
			<iframe name="calculatorFrame" style="margin: 0" frameBorder="0" 
					width=100% height="205px" scrolling=no marginheight=0 marginwidth=0></iframe>
		</div>
		<!-------------------------- 公式编辑隐藏层(结束) ------------------------------------->
	</body>
</html>