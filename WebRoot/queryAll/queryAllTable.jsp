<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
	String webapp=request.getContextPath();
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" >
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
		<title>General inquiries</title>
		<!-- 导入公共js和css -->
		<jsp:include page="/extcommon.jsp"></jsp:include>
		<!-- 导入dwr -->
		<script type='text/javascript'
			src='<%=webapp %>/dwr/interface/cotElementsService.js'></script>

		<script type='text/javascript' src='<%=webapp %>/queryAll/js/queryAllTable.js'></script>
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
					alert("Pls. select a record");
					return;
				}
				if(existEles!=''){
				    alert('Please note that Article No.'+existEles+"already exists and can not be added!");
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
			function bindSel()
			{
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
			function rset()
			{
				clearForm("queryForm");
			}
			
			//显示查找客户的层
			function showCustomerDiv(e){
				var obj = $("customerDiv").style.display;
				if(obj=='none'){
					//加载客户查询表格
					var frame = document.cusTabFrame;
					frame.location.href = "cotquery.do?method=queryCustomer";
					document.getElementById("customerDiv").style.top=$("customerShortName").offsetHeight+18;
					document.getElementById("customerDiv").style.left=$("customerShortName").offsetLeft;
					document.getElementById("customerDiv").style.display ="block";
					$("empDiv").style.display='none';
				}else{
					$("customerDiv").style.display='none';
				}
			}
			
			//显示查找员工的层
			function showEmpDiv(e){
				var obj = $("empDiv").style.display;
				if(obj=='none'){
					//设置层显示位置
					var t=e.offsetTop;
					var l=e.offsetLeft;
					while(e=e.offsetParent){
						t+=e.offsetTop;
						l+=e.offsetLeft;
					}
					//加载客户查询表格
					var frame = document.empTabFrame;
					frame.location.href = "cotquery.do?method=queryEmp";
					document.getElementById("empDiv").style.top=$("empsName").offsetHeight+18;
					document.getElementById("empDiv").style.left=$("empsName").offsetLeft-310;
					document.getElementById("empDiv").style.display ="block";
					$("customerDiv").style.display='none';
				}else{
					$("empDiv").style.display='none';
				}
				return false;
			}
			
			//根据选择的客户编号自动填写表单,供客户查询表格调用
			function setCustomerForm(cusId,val){
				//隐藏查询客户层
				//$("customerDiv").style.display='none';
				//var shortName = row.getElementsByTagName("TD")[1].innerText;
				$('customerShortName').value = val;
				$('custId').value = cusId;
			}
			
			//根据选择的员工编号自动填写表单,供员工查询表格调用
			function setEmpId(empId,empName){
				//隐藏查询客户层
				//$("empDiv").style.display='none';
				$('businessPerson').value=empId;
				$('empsName').value=empName;
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
			
			// 根据选择的厂家编码自动填写表单
			function setFactoryForm(id,shortName){
				// 隐藏查询加工厂家层
				//$("factoryDiv").style.display='none';
				$('factoryId').value = id;
				$('shortName').value = shortName;
			}
			
			//清空主单查询form
			function clearForm(){
				$('queryForm').reset();
			}
			
			// 点击选择模糊查询结果后方法
			function handleComon(id, val, fno, txtId) {
				if(txtId=='customerShortName'){
					setCustomerForm(id, val);
				}else if(txtId=='shortName'){
					setFactoryForm(id, val);
				}else{
					setEmpId(id, val);
				}
				$(txtId).style.background = '#E2F4FF';
				$("search").style.display = "none";// 隐藏列表
			}				
		</script>
	</head>

	<body>

	</body>

</html>
