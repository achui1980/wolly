<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%> 
<%
	String webapp=request.getContextPath();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>报关行信息添加页面</title>
<jsp:include page="../../common.jsp"></jsp:include>
<script type='text/javascript' src='<%=webapp %>/dwr/interface/cotHsCompanyService.js'></script>
<link rel="stylesheet" type="text/css" href="<%=webapp %>/ecside/css/ecside_style.css" />
<script type="text/javascript" src="<%=webapp %>/ecside/js/ecside.js"></script>
<script type="text/javascript" src="<%=webapp %>/ecside/js/prototype_mini.js"></script>
<script type="text/javascript" src="<%=webapp %>/ecside/js/ecside_msg_utf8_cn.js"></script>
<script type="text/javascript">

	//权限判断变量，对应于cot_module表中的URL字段
	var url = "cothscompany.do?method=query"; 
	function add()
	{
	//表单验证
		var form = document.forms["queryHsCompanyForm"];
		var b = false;
		b = Validator.Validate(form,1);
		if(!b)
		{
			return;
		}
	//表单验证结束
	    var obj = DWRUtil.getValues("queryHsCompanyForm");
	    var cotHsCompany = new CotHsCompany();
	    var list = new Array();
	    for(var p in obj)
	    {
	    	cotHsCompany[p] = obj[p];
	    }
	    cotHsCompanyService.addHsCompany(cotHsCompany,function(res){
			alert("添加成功!");
			closeandreflashEC(true,"hscompanyTable",false);
		})
	}

 

    //初始化表单验证字段
	function validForm()
	{
		appendattribute("hsCompanyName","报关行名称不能为空！","Require");
	}

 
 
	function initForm()
	{
		validForm();
	}
 
 
</script>
	</head>
	<body onload="initForm()">
		 
		<div class="navline">
			<label>
				&nbsp;
			</label>
		</div>
		<div align="center">		
			<form name="queryHsCompanyForm" id="queryHsCompanyForm" >
				<div style="width: 100%;float:left;margin-top:10px;">
					<label class="label_25" style="color: red;text-align:right">
						中文名：
					</label>
					<input class="input_65" type="text" name="hsCompanyName" id="hsCompanyName" />
				</div>
				<div style="width: 100%;float:left;margin-top:5px;">
					<label class="label_25" style="color: red;text-align:right">
						英文名：
					</label>
					<input class="input_65" type="text" name="hsCompanyNameEn" id="hsCompanyNameEn" />
				</div>
				<div style="width: 100%;margin-top:5px;">
					<label class="label_25" style="text-align:right;color:#000;">
						联系电话：
					</label>
					<input class="input_65" type="text" name="hsContactNbr" id="hsContactNbr" />
				</div>
				<div style="width: 100%;margin-top:5px;">
					<label class="label_25" style="text-align:right;color:#000;">
						传  真：
					</label>
					<input class="input_65" type="text" name="hsFax" id="hsFax" />
				</div>
				<div style="width: 100%;margin-top:5px;">
					<label class="label_25" style="text-align:right;color:#000;">
						联系人：
					</label>
					<input class="input_65" type="text" name="hsContantPerson" id="hsContantPerson" />
				</div>
				<div style="width: 100%;margin-top:5px;">
					<label class="label_25" style="text-align:right;color:#000;">
						地  址：
					</label>
					<textarea rows="2" cols="" class="input_65" name="hsAdd" id="hsAdd" ></textarea>
				</div>
			</form>
		</div>
		<div align="center">
			<div style="margin-left:10%;margin-top:5px;">&nbsp;
				<a onclick="add()" style="cursor: hand;"><img
						src="<%=webapp %>/common/images/_save.gif" border="0"
						height="21px" width="61px"></a>	&nbsp;&nbsp;
				<a onclick="javascript:closeandreflashEC(true,'hscompanyTable',false);" style="cursor: hand;"><img
						src="<%=webapp %>/common/images/_cancel.gif" border="0"
						height="21px" width="61px"></a>	
			 
			</div>
		</div>
			
		<input type="hidden" name="hscompanyid" id="hscompanyid"   />
	</body>
</html>