<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
String path = request.getContextPath();
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" >
<html>
	<head>

		<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
		<title>样品其他图片演示</title>
		<!-- 导入公共js和css -->
		<jsp:include page="../../extcommon.jsp"></jsp:include>
		<!-- 导入dwr -->
		<link rel="stylesheet" type="text/css" href="<%=path %>/common/css/chooser.css" />
		<link rel="stylesheet" type="text/css" href="<%=path %>/common/ext/resources/css/file-upload.css" />
		<script type="text/javascript" src="<%=path %>/common/ext/ux/FileUploadField.js"></script>
		<!-- 图片鼠标拖拉选择框 -->
		<script type="text/javascript" src="<%=path %>/common/ext/ux/DataView-more.js"></script>
		<script type='text/javascript'
			src='<%=path%>/dwr/interface/cotElementsService.js'></script>
		<script type='text/javascript' src='<%=path%>/sample/elements/js/showpicture.js'></script>	
		<script type="text/javascript" src="<%=path %>/common/js/uploadpanel.js"></script>
		<script type="text/javascript" src="<%=path %>/common/js/popedom.js"></script>
		<script>
		
		
		
		//显示上传图片层
		function showUploadDiv(e){
			//设置权限为空
			mapPopedom = null;
			var isPopedom = getPopedomByOpType("cotpicture.do","ADD");
			if(isPopedom == 0) //没有添加图片权限
			{
				alert("您没有添加图片的权限!");
				return;
			}else{
				var obj =document.getElementById("uploadDiv").style.display;
				if(obj=='none' || obj==''){
					//设置层显示位置
					var t=e.offsetTop;
					var l=e.offsetLeft;
					while(e=e.offsetParent){
						t+=e.offsetTop;
						l+=e.offsetLeft;
					}
					//加载客户查询表格
					var frame = document.uploadTabFrame;
					var eId = document.getElementById("eId").value;
			 		if(eId!='null' && eId!=''){
						cotPicture.eleId = eId;
			 		}
					frame.location.href = "cotelements.do?method=showOtherPic&eId="+eId;
					document.getElementById("uploadDiv").style.top=t+20;
					document.getElementById("uploadDiv").style.left=l-280;
					document.getElementById("uploadDiv").style.display ="block";
				}else{
					document.getElementById("uploadDiv").style.display='none';
				}
			}
			
		}
		
		//关闭
		function closeDiv(){ 
			document.getElementById("uploadDiv").style.display ="none"; 
			query(true,true);
		}
		
		//点击图片上传层外的地方后隐藏该层
		function hideDiv()
		{
			var down = event.srcElement.id;
			if(down!="uploadTable" && down!="uploadChildPicBtn" 
			&& down!="showUploadBtn" && down!="controlPanel" 
			&& down!="file" && down!="uploadButton" && down!="uploadParentPicBtn")
				document.getElementById("uploadDiv").style.display ='none';
		}
		
	</script>
	</head>
	<%
	String eId = request.getParameter("eId");
	%>
	<body>
		<input type="hidden" id="eId" value="<%=eId%>"/>
	</body>
</html>
