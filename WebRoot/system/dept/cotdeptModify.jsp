<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%> 
<%
	String webapp=request.getContextPath();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"  >
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
		<title>Department to modify page</title>
		<jsp:include page="../../common.jsp"></jsp:include>
		<script type='text/javascript' src='<%=webapp %>/dwr/interface/cotDeptService.js'></script>
		<script type='text/javascript' src='<%=webapp %>/dwr/interface/cotCompanyService.js'></script>
		<link rel="stylesheet" type="text/css" href="<%=webapp %>/ecside/css/ecside_style.css" />
		<script type="text/javascript" src="<%=webapp %>/ecside/js/ecside.js"></script>
		<script type="text/javascript" src="<%=webapp %>/ecside/js/prototype_mini.js"></script>
		<script type="text/javascript" src="<%=webapp %>/ecside/js/ecside_msg_utf8_cn.js"></script>
		<script type="text/javascript">
  //权限判断变量，对应于cot_module表中的URL字段
var url = "cotdept.do?method=query"; 
	function  del()
	{   
	    var flag = window.confirm("Are you sure you want to delete this sector?");
	    if(flag){
	        var deptId=DWRUtil.getValue("deptid");
	        var isExist = false;
	        DWREngine.setAsync(false); 
		    //判断是否有关联员工信息
			cotDeptService.findEmpsRecordsCount(deptId,function(res){
				 isExist=res
		    });
			if(isExist){
				Ext.Msg.alert("Message","Remove failed! Department staff have been cited！");
				return;
			}
		    DWREngine.setAsync(true); 
	        
	       	var obj = DWRUtil.getValues("queryDeptForm");
		    var cotDept = new CotDept();
			for(var p in obj){
					cotDept[p] = obj[p];
				} 
		     cotDept.id =  DWRUtil.getValue("deptid");
			 cotDeptService.deleteCotDept(cotDept,function(res){
				Ext.Msg.alert("Message","Deleted successfully!");
				closeandreflashEC(true,"deptTable",false);
			})
	    }else{
	      return;
	    }
	     
	}

	 
   function mod()
	{
	       //表单验证
	    var form = document.forms["queryDeptForm"];
		var b = false;
		b = Validator.Validate(form,1);
		if(!b)
		{
			return;
		}
		//表单验证结束
	        var obj = DWRUtil.getValues("queryDeptForm");
	        var cotDept = new CotDept();
			var list = new Array();
			for(var p in obj)
			{
				cotDept[p] = obj[p];
			} 
		     cotDept.id =  DWRUtil.getValue("deptid");
		    
		     list.push(cotDept)
		     cotDeptService.updateCotDeptByList(list,function(res){
				          Ext.Msg.alert("Message","Successfully modified!");
						closeandreflashEC(true,"deptTable",false);
				 })
	}

	function initForm()
	{       
	      DWRUtil.useLoadingMessage("Loading data, please wait..........."); 
	      validForm();
	     
	      DWREngine.setAsync(false); 
	      bindSel();
		  var id = $("deptid").value;
		  cotDeptService.getDeptById(parseInt(id),function(res){
		  var obj = res;
		  DWRUtil.setValues(obj);
		  DWREngine.setAsync(true); 
		  });
	}

//加载所属节点下拉框数据
	function bindSel()
	    {
			cotCompanyService.getCompanyList(function(res){
			bindSelect('companyId',res,'id','companyShortName');
			})
		}
	
//初始化表单验证字段
	function validForm()
		{
			appendattribute("deptName","Department name can not be empty","Require");
			appendattribute("companyId","Please select the respective company!","Require");
			appendattribute("deptStatus","Select whether to enable!","Require");
	    }
	
	 
</script>
		<style type="text/css">
</style>
	</head>
	<%
		String id = request.getParameter("id");
	%>
 <body onload="initForm()">
		<table style="width: 100%;">
		<div class="navline">
			<label>
				&nbsp;
			</label>
		</div>	
			<tr>
				<td align="center">
					<form name="queryDeptForm" id="queryDeptForm" onsubmit="return false">
						<div style="width: 100%;margin-top:10px;">
								<label class="label_12" style="color: red">
									Department Name：
								</label>
								<input class="input_20" type="text" name="deptName"
									id="deptName" />

								<label class="label_12" style="color: red">
									Affiliated companies：
								</label>
								<select class="select_20" name="companyId" id="companyId">
								</select>

								<label class="label_12" style="color: red">
									Enabled：
								</label>
								<select class="select_20" name="deptStatus" id="deptStatus">
									<option value="">
										Please select
									</option>
									<option value="0">
										Disable
									</option>
									<option value="1">
										Enable
									</option>
								</select>
							</div>
							<div style="width: 100%;">
								<label class="label_12">
									Remarks：
								</label>
								<textarea name="deptRemark" id="deptRemark" rows="5" cols="50"
									class="input_84"></textarea>
							</div>
					</form>
				</td>
			</tr>
			<tr>
				<td align="center">
					<div style="margin-left:10%;margin-top:5px;">&nbsp;
						<a onclick="mod()" style="cursor: hand;"><img
								src="<%=webapp %>/common/images/_modi.gif" border="0"
								height="21px" width="61px"></a>	&nbsp;&nbsp;
						<a onclick="del()" style="cursor: hand;"><img
								src="<%=webapp %>/common/images/_del.gif" border="0"
								height="21px" width="61px"></a>	&nbsp;&nbsp;
						<a onclick="javascript:closeandreflashEC(true,'deptTable',false);" 
							style="cursor: hand;">
						<img src="<%=webapp %>/common/images/_cancel.gif" border="0"
								height="21px" width="61px"></a>																					
					<!--
						<button class="modBtn" onclick="mod()"/>修改</button>
				        <button class="delBtn" onclick="del()"/>删除</button>
						<button class="cancelBtn" 
						        onclick="javascript:closeandreflashEC(true,'deptTable',true);">关闭
						</button>
						-->
					</div>
				</td>
			</tr>	
		</table>
		<input type="hidden" name="deptid" id="deptid" value="<%=id%>" />
	</body>
</html>