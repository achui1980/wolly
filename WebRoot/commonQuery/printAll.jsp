<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib uri="http://www.ecside.org" prefix="ec"%>
<%
	String webapp=request.getContextPath();
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" >
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
		<title>全部打印页面</title>
		<!-- 导入公共js和css -->
		<jsp:include page="/common.jsp"></jsp:include>
		<!-- 导入dwr -->
		<script type='text/javascript'
			src='<%=webapp %>/dwr/interface/cotElementsService.js'></script>
		<script type='text/javascript'
			src='<%=webapp %>/dwr/interface/cotBarcodeService.js'></script>
		<script type="text/javascript">
			 
			 
			//打印全部
			function printAll(){
			    //导出模板表单验证
			    var checkRes = parent.checkExportForm();
			    if(checkRes){
			    	var eleId = $('eleIdFind').value;
				    var eleTypeidLv1 = $('eleTypeidLv1Find').value;
				    var factoryId = $('factoryIdFind').value;
				    var eleCol = $('eleColFind').value;
				    var empId = ${emp.id}
				    DWREngine.setAsync(false);
				    cotBarcodeService.delBarcodeByAddEmp(parseInt(empId),function(res){
				    });
				    //默认打印数量
				    var defaultCount = parent.document.getElementById("defaultCount").value;
				    
				    //保存到数据库中
				    cotBarcodeService.saveAllBarcode(eleId,eleTypeidLv1,factoryId,eleCol,parseInt(defaultCount),parseInt(empId),function(res){
				    	if(!res){
				        	alert("对不起，没有符合条件的相关记录！");
				        }else{
				        	//打开预览页面
				    		parent.viewRpt(empId);
				        }
				    });
			        DWREngine.setAsync(true);
				}
			}
			
			 
			
			//关闭方法
			function closeDiv(){
				parent.document.getElementById('printAllDiv').style.display='none';
			}
			
			//加载下拉框数据
			function bindSel()
			{   
				sysdicutil.getDicListByName('factory',function(res){
					bindSelect('factoryIdFind',res,'id','shortName');
				});
				sysdicutil.getDicListByName('typelv1',function(res){
					bindSelect('eleTypeidLv1Find',res,'id','cnEnName');
				});
			}
			
			//重置
			function rset()
			{
				clearForm("queryForm");
			}				
		</script>
	</head>

	<body onload="bindSel()">
		<div style="width: 100%; margin-top: 10px" align="center">
			<fieldset style="width: 95%; height: 100%">
				<legend>
					打印样品条件：
				</legend>
				<form name="queryForm" id="queryForm"
					style="margin-top: 5px; margin-bottom: 10px">
					<div style="width: 100%;">
						<label class="label_13">
							货号：
						</label>
						<input class="input_36" name="eleIdFind" id="eleIdFind" />
						<label class="label_13">
							厂家：
						</label>
						<select class="select_36" name="factoryIdFind" id="factoryIdFind">
							<option value="">
								请选择
							</option>
						</select>
					</div>
					<div style="width: 100%;">
						<label class="label_13">
							颜色：
						</label>
						<input class="input_36" type="text" name="eleColFind"
							id="eleColFind" />
						<label class="label_13"">
							材质：
						</label>
						<select class="select_36" name="eleTypeidLv1Find"
							id="eleTypeidLv1Find">
							<option value="">
								请选择
							</option>
						</select>
					</div>
				</form>
			</fieldset>
			<div style="width: 100%" align="center">
				<label style="color: red">
					提示:如果要打印所有样品货号的条形码，请直接点击打印预览按钮！
				</label>
			</div>
			<div align="center" style="width: 100%; margin-top: 10px;">

				<a onclick="printAll()" style="cursor: hand;" id="printAllBtn"><img
						src="<%=webapp %>/common/images/new_preview.gif" border="0"
						height="21px" width="81px"> </a>
				<a onclick="rset()" style="cursor: hand;"> <img
						src="<%=webapp %>/common/images/query_rs.gif" border="0"
						height="21px" width="61px"> </a>
				<a onclick="closeDiv()" style="cursor: hand;"> <img
						src="<%=webapp %>/common/images/_toff.gif" border="0" height="21px"
						width="61px"> </a>

			</div>
		</div>

	</body>
</html>
