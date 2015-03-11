<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String webapp = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+webapp+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" >
<html>
	<head>
		<base href="<%=basePath%>">

		<title>My JSP 'uploadMB.jsp' starting page</title>

		<meta http-equiv="pragma" content="no-cache">
		<meta http-equiv="cache-control" content="no-cache">
		<meta http-equiv="expires" content="0">
		<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
		<meta http-equiv="description" content="This is my page">
		<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
		<!-- 导入上传js和css -->
		<script type="text/javascript" src="<%=webapp %>/common/js/prototype.js"></script>
		<script type="text/javascript"
			src="<%=webapp %>/common/js/newAjaxWrapper.js"></script>
		<script type="text/javascript" src="<%=webapp %>/common/js/upload.js"></script>
		<link href="<%=webapp %>/common/css/fileUpload.css" type="text/css"
			rel="stylesheet" />
		<style>
			.cus_button {
			    font-family: "tahoma", "宋体";
			    font-size:9pt; 
			    color: #003399;
			    color:#006699;
			    background-color: #e8f4ff;
			    cursor: hand;
			    width:40px;
			    height:21px;
			    border:2px;
			}
		</style>

		<script type="text/javascript">
		//图片上传　
		function upload(){
		  	var name = uploadCheck($("file").value);
			if(name.charAt(0)!='_'){
				alert(name);
				return;
			}
		 	join(false);
		  	afterUpload("parent.closeDiv()");
		}
		
		//关闭页面
		function closeWindow(){
			parent.document.getElementById('uploadDiv').style.display = 'none';
		}
	</script>
	</head>
	<%
		String id = request.getParameter("eId");
	%>
	<body style="margin:0px;">
		<div id="uploadTable" class="overlayer">
			<div style="display: inline;"
				id="aazone.fileTableConditionFormAjaxZone">
				<div id="controlPanel"
					style="width: 320px;background: #99CCFF;margin-top: 0px;margin-bottom: 0px;">
					<div id="flowHint" style="display: none">
						<img src="<%=webapp %>/common/images/loading.gif" />
					</div>
					<div>
						只能上传图片，单张图片最大100M
					</div>
					<div id="progressStatusText"></div>
					<div id="sessionIdHint"></div>
					<div id="uploadFormDiv01">
						<form id="fileUploadForm" name="fileUploadForm"
							action="./uploadLabelPicture.action"
							enctype="multipart/form-data" method="post" target="hiddenIFrame">
							<input type="file" name="file" id="file" size="10" />
							<input type="hidden" name="pEId" id="pEId" value="<%=id%>"/>
							<input type="button" name="uploadButton" id="uploadButton"  class="cus_button"
								value="上传" />
							<input type="button" name="closeButton" id="closeButton"
							onclick="closeWindow()" value="关闭" class="cus_button"/>
							<input type="button" name="cancelButton" id="cancelButton"
								value="取消" />
							<div id="progressBar">
								<div id="totalProgressBarBox">
									<div id="totalProgressBarBoxContent"></div>
									<label id="progressBarText"></label>
								</div>
							</div>
						</form>
					</div>
				</div>
			</div>
			<div id="resultPanel" style="display: none;">
				<span>上传文件列表:</span>
				<div id="uploadList"></div>
			</div>
		</div>
		<iframe name="hiddenIFrame" id="hiddenIFrame"></iframe>
		<script>
		Event.observe('uploadButton','click',function(){upload()},false);
		Event.observe('cancelButton','click',cancelProgress,false);
		Event.observe(window,'load',initSession,false);
		</script>
	</body>
</html>
