<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
	String webapp=request.getContextPath();
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" >
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
		<title>Comprehensive review</title>
		<!-- 导入公共js和css -->
		<jsp:include page="/extcommon.jsp"></jsp:include>
		<!-- 导入dwr -->
		<script type='text/javascript'
			src='<%=webapp %>/dwr/interface/cotElementsService.js'></script>
		<!-- 导入ECTable -->
		<script type='text/javascript'
			src='<%=webapp %>/check/js/checkAllTable.js'></script>
		
		<script type="text/javascript">
			//加载样品数据
			function loadElements(){
				var params = DWRUtil.getValues("queryForm");
		   	 	ECSideUtil.queryECForm("elementTable",params,true);
			}
			//父页面调用模糊查询方法(根据货号)
			function query(eleId){
				var params = {};
				params.eleId = eleId;
		   	 	ECSideUtil.queryECForm("elementTable",params,true);
			}
			
			//获得表格选择的记录
			function getEleIds()
			{ 
				var ids = '';
				var eleIds = '';
				var objs = document.getElementsByName("checkEleID");
				var pareType = parent.parentType;
				if(pareType=="barcode"){
				     for(var i=0; i<objs.length; i++)
					{
						var chk = objs[i];
						if(chk.checked)
						{
							//截取出id
						   	var s = chk.value.indexOf("-");
						   	var id=chk.value.substring(0,s);
						    var eleId=chk.value.substring(s+1,111111);
							eleIds+=eleId+",";
						}
					}
				}
				return eleIds;
			}
			
			//显示图片
			function showElePic(elementId){
				mapPopedom = null;
				var isSelPic = getPopedomByOpType("cotpicture.do","SEL");
				if(isSelPic==0){//没有查看图片信息权限
					$('elePicPath').src = "common/images/noElePicSel.png";
				}else{
					$("elePicPath").src = "./showPicture.action?elementId="+elementId+"&flag=ele";
			    }
			}
			 
			var existEles = '';
			//表格添加事件,调用父页面的方法
			function returnIds(){
			    DWREngine.setAsync(false); 
				var eleIds = getEleIds(); 
				if(eleIds.length == 0)
				{	
					
					Ext.Msg.alert("Message","Please select a record");
					return;
				}
				if(existEles!=''){
				    Ext.Msg.alert("Message",'Please note,Item No.'+existEles+"Already exists,Can not be added!");
				}
				existEles='';
				DWREngine.setAsync(true); 
				parent.insertSelect(eleIds);
			}
			
			//表格全部添加事件,调用父页面的方法
			function returnAll(){
				var eleId = $('eleIdFind').value;
			    var eleTypeidLv1 = $('eleTypeidLv1Find').value;
			    var factoryId = $('factoryIdFind').value;
			    var eleCol = $('eleColFind').value;
			    var eleIds = ''
			    DWREngine.setAsync(false); 
			    cotBarcodeService.findEleIds(eleId,eleTypeidLv1,factoryId,eleCol,function(res){
			    	for(var i=res.length-1; i>=0; i--)
					{
						var eleId = res[i];
						eleIds+=eleId+",";
					}
			    });
			    parent.insertSelect(eleIds);
			    DWREngine.setAsync(true);
			}
			
			//关闭方法
			function closeDiv(){
				parent.document.getElementById('elementsDiv').style.display='none';
				parent.document.getElementById('flagBtn').src = '<%=webapp %>/common/images/_selpro.gif';
				parent.document.getElementById('insertEleId').value = '';
			}
			
			//加载下拉框数据
			function bindSel(){
				sysdicutil.getDicListByName('factory',function(res){
					bindSelect('factoryId',res,'id','shortName');
				});
				sysdicutil.getDicListByName('typelv1',function(res){
					bindSelect('eleTypeidLv1',res,'id','cnEnName');
				});
				sysdicutil.getDicListByName('typelv2',function(res){
					bindSelect('eleTypeidLv2',res,'id','typeName');
				});
			}
			
			//重置
			function rset(){
				clearForm("queryForm");
			}
			
			//查询条件
			var params = {};
			//加载标签页
			function loadInfo(modelName,sel){
				params = DWRUtil.getValues("queryForm");
				var str = "document."+modelName;
				var frame = eval(str);
				frame.location.href = sel;
			}
			
			//清空主单查询form
			function clearForm(){
				$('queryForm').reset();
			}
			
			// 点击选择模糊查询结果后方法
			function handleComon(id, val, fno, txtId) {
				if(txtId=='customerShortName'){
					$('customerShortName').value = val;
					$('custId').value = id;
				}else if(txtId=='shortName'){
					$('factoryId').value = id;
					$('shortName').value = shortName;
				}else{
					$('businessPerson').value=id;
					$('empsName').value=empName;
				}
				$(txtId).style.background = '#E2F4FF';
				$("search").style.display = "none";// 隐藏列表
			}				
		</script>
	</head>

	<body>
		<select name="status" id="status" class="select_10">
						<option value="3">Need to be reviewed </option>
						<option value="0">Non-reviewed </option>
						<option value="1">Review without </option>
						<option value="2">reviewed</option>
						<option value="9">Not review</option>
		</select>

	</body>

</html>
