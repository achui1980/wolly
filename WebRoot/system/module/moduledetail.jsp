<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib uri="/e3/table/E3Table.tld" prefix="e3t"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" >
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
		<title>菜单编辑页面</title>
		<!-- 导入公共js和css -->
		<jsp:include page="/common.jsp"></jsp:include>
		<!-- 导入dwr -->
		<script type='text/javascript' src='dwr/interface/cotModuelService.js'></script>
		<script type="text/javascript">
			//添加
			function add()
			{	
				var obj = DWRUtil.getValues("cotModuleForm");
				//验证表单
				var form = document.forms['cotModuleForm'];
				var b = false;
				b = Validator.Validate(form,1);
				if(!b)
				{
					return;
				}
				//如果菜单父类是"请选择"时,给它null值
				if(obj.moudleParent==''){
					obj.moudleParent  = null;
				}
				var list = new Array();
				list.push(obj);
				cotModuelService.addModule(list,function(res){
					if(res){
						alert("添加成功！");
						closeandreflash(true,"moduleTable",true);
					}else{
						alert("添加失败,菜单名称已存在,请重新输入！");
						$('moduleName').select();
					}
				})
			}
			//修改
			function mod()
			{
				var obj = DWRUtil.getValues("cotModuleForm");
				//验证表单
				var form = document.forms['cotModuleForm'];
				var b = false;
				b = Validator.Validate(form,1);
				if(!b)
				{
					return;
				}
				obj.id = $("moduleid").value;
				//如果菜单父类是"请选择"时,给它null值
				if(obj.moudleParent==''){
					obj.moudleParent = null;
				}
				var list = new Array();
				list.push(obj)
				cotModuelService.modifyModule(list,function(res){
					alert("修改成功！");
					closeandreflash(true,"moduleTable",true);
				})
				
			}
			//删除
			function del()
			{
				var id = $("moduleid").value;
				var obj = new CotModule();
				obj.id = id;
				var list = new Array();
				list.push(obj);
				var flag = window.confirm("确定删除此模块吗？同时也会删除它的子模块！");
				if(flag){
					cotModuelService.deleteModule(list,function(res){
						alert("删除成功");
						closeandreflash(true,"moduleTable",true);
					})
				}
				
			}
			//加载所属节点下拉框数据
			function bindSel(id)
			{
				cotModuelService.getParentModuleList(id,function(res){
					bindSelect('moudleParent',res,'id','moduleName');
				})
			}
			
			//显示"添加"按钮层
			function showSaveBtn()
			{	
				//显示"保存"和"取消"按钮
				$("showBtn").style.display='';
				//清空表单
				clearForm('cotModuleForm');
				//重新加载所属节点下拉框数据(显示所有级别是0－1的菜单)
				bindSel(0);
			}
			
			//初始化表单验证字段
			function validForm()
			{
				appendattribute("moduleName","菜单名称不能为空","Require");
			}
			
			//页面加载调用
			function initForm()
			{
				DWREngine.setAsync(false);
			  var id = $("moduleid").value;
			   //加载下拉列表
  			  bindSel(id);
  			  //表单验证
			 validForm();
			  if(id == "0") //无记录，进行添加
				{
				  	//显示保存按钮
				  	$("showBtn").style.display = "block";
					$("modBtn").style.display = "none";
					$("delBtn").style.display = "none";
					clearForm('cotModuleForm');
				 }else{
					  //同步
					   
		  			  //加载表单
					  cotModuelService.getModuleById(parseInt(id),function(res){
					  	//printObject(res);
					  	DWRUtil.setValues(res);
					  });
					  
				 }
				 DWREngine.setAsync(true); 
			}
		</script>
	</head>
	<%
		String id = request.getParameter("id");
	%>
	<body onload="initForm()">
		<table style="width: 100%;">
			<tr>
				<td>
					<div style="margin-left: 30%">
						<button class="formButton" onclick="showSaveBtn()">
							添加
						</button>
						<button class="formButton" id="modBtn" onclick="mod()">
							修改
						</button>
						<button class="formButton" id="delBtn" onclick="del()">
							删除
						</button>
					</div>
				</td>
			</tr>
			<tr>
				<td align="center">
					<form name="cotModuleForm" id="cotModuleForm"
						action="/budgetmanager/executeRecord.do">
						<div style="width: 100%;">
							<div>
								<label class="label_13">
									菜单名称
								</label>
								<input class="input_85" type="text" name="moduleName"
									id="moduleName" />
							</div>
							<div>
								<label class="label_13">
									菜单级别
								</label>
								<select class="select_85" name="moduleLv" id="moduleLv">
									<option value="0">
										0
									</option>
									<option value="1">
										1
									</option>
									<option value="2">
										2
									</option>
									<option value="3">
										3
									</option>
									<option value="4">
										4
									</option>
									<option value="5">
										5
									</option>
									<option value="6">
										6
									</option>
									<option value="7">
										7
									</option>
									<option value="8">
										8
									</option>
									<option value="9">
										9
									</option>
								</select>
							</div>
							<div>
								<label class="label_13">
									菜单地址
								</label>
								<input class="input_85" type="text" name="moduleUrl"
									id="moduleUrl" />
							</div>
							<div>
								<label class="label_13">
									图片地址
								</label>
								<input class="input_85" type="text" name="moduleImgurl"
									id="moduleImgurl" />
							</div>
							<div>
								<label class="label_13">
									所属节点
								</label>
								<select class="select_85" id="moudleParent" name="moudleParent">
									<option value="">
										请选择
									</option>
								</select>
							</div>
							<div>
								<label class="label_13">
									菜单类型
								</label>
								<select class="select_85" name="moudleType" id="moudleType">
									<option value="MODULE">
										MODULE
									</option>
									<option value="MENU">
										MENU
									</option>
									<option value="FUN">
										FUN
									</option>
								</select>
								<input type="hidden" name="moudleOrder" id="moudleOrder" />
							</div>
						</div>
					</form>
				</td>
			</tr>
			<tr>
				<td align="center">
					<div id="showBtn" style="display: none;">
						<button class="formButton" onclick="add()">
							保存
						</button>
						<button class="formButton"
							onclick="javascript:closeandreflash(true,'moduleTable',true);">
							取消
						</button>
						<input type="hidden" name="moduleid" id="moduleid"
							value="<%=id%>">
					</div>
				<td>
		</table>

	</body>
</html>
