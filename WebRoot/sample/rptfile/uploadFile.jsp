<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8" %>
<%
	String webapp=request.getContextPath();
%>
<%@ taglib uri="/e3/table/E3Table.tld" prefix="e3t" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<jsp:include page="../../common.jsp"></jsp:include>
<script type="text/javascript" src="<%=webapp %>/common/js/prototype.js"></script>
<script type="text/javascript" src="<%=webapp %>/common/js/newAjaxWrapper.js"></script>
<script type="text/javascript" src="<%=webapp %>/common/js/upload.js"></script>
<script type="text/javascript" src="<%=webapp %>/common/js/tabpane.js"></script>
<link href="<%=webapp %>/common/css/fileUpload.css" type="text/css" rel="stylesheet" />
<link rel="stylesheet" type="text/css" href="<%=webapp %>/common/css/luna/tab.css" />
<script type="text/javascript">
 
function upload()
{
	var oldFilePath = DWRUtil.getValue("file");
	var rptName = DWRUtil.getValue("rptName");
	var s = oldFilePath.lastIndexOf(".");
	if(s<0){
	   	alert("请选择文件, 再上传!");
		return;
	}else{
	 	var sType = oldFilePath.substring(s+1,oldFilePath.length);
	    if(sType.toLowerCase()!='jasper' && sType.toLowerCase()!='jrxml'){
				alert("只能上传格式为jasper或者jrxml的报表文件，请重新上传文件！");
				return;
		}
	}
	if(rptName==""){
		alert("请输入报表文件名, 再上传!");
		return;
	}
    var point = oldFilePath.lastIndexOf("\\");
    var fileName = oldFilePath.substring(point+1,oldFilePath.length);
    parent.rptName = rptName;
    parent.rptfilePath = "reportfile/"+fileName;
    join(false);
}
 
function loadName()
{
	DWRUtil.setValue("rptName",parent.rName);
}


</script>
</head>
	<%
		String rptId = request.getParameter("rptId");
	%>
<body onload="loadName()">
	  	<div id="uploadRptFileTable" >
			<div style="display: inline;"
				id="aazone.fileTableConditionFormAjaxZone">
				<div id="controlPanel" style="width: 85%;margin-left: 0px">
					<div id="flowHint" style="display: none">
						<img src="<%=webapp %>/common/images/loading.gif" />
					</div>
					<div id="readme">
					     <span>上传文件说明:&nbsp;&nbsp;只能上传格式为jrxml或jasper的报表文件</span>
					</div>
					<div id="progressStatusText"></div>
					<div id="sessionIdHint"></div>
					<div id="uploadFormDiv01">
						<form id="fileUploadForm" name="fileUploadForm"
							action="./uploadRptFile.action" enctype="multipart/form-data"
							method="post" target="hiddenIFrame">
							<input type="file" name="file" id="file" size="20" />
							<input type="hidden" name="rptId" id="rptId" value="<%=rptId%>" />
							<div style="width: 100%"> 
							    <label>请输入报表名称:</label><br/>
								<input type="text" name="rptName" id="rptName"  />
								<input type="button" name="uploadButton" id="uploadButton" value="上传" />
							    <input type="button" name="cancelButton" id="cancelButton" value="取消" />
							</div>
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
			<div id="resultPanel" style="display: none">
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