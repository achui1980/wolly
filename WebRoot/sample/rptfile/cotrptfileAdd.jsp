<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8" %>
 <%
	String webapp=request.getContextPath();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" >
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>报表文件添加页面</title>
<jsp:include page="../../common.jsp"></jsp:include>
<script type='text/javascript' src='<%=webapp %>/dwr/interface/cotRptFileService.js'></script>
<script type='text/javascript' src='<%=webapp %>/dwr/interface/cotRptTypeService.js'></script>
<script type="text/javascript" src="<%=webapp %>/common/js/prototype.js"></script>
<script type="text/javascript" src="<%=webapp %>/common/js/newAjaxWrapper.js"></script>
<script type="text/javascript" src="<%=webapp %>/common/js/upload.js"></script>
<script type="text/javascript" src="<%=webapp %>/common/js/tabpane.js"></script>
<link href="<%=webapp %>/common/css/fileUpload.css" type="text/css" rel="stylesheet" />
<link rel="stylesheet" type="text/css" href="<%=webapp %>/common/css/luna/tab.css" />
<script type="text/javascript">
	
	var rptName = "";
	var rptfilePath = "";
	//var rptImgPath = "";
 
	 //添加
	function add()
	{
	     //表单验证
	     var form = document.forms["queryRptFileForm"];
	     var b = false;
	     b = Validator.Validate(form,1);
	  	     if(!b)
	     {
		     return;
	     }
	     var cotRptFile = new CotRptFile();
		 cotRptFile.rptType = DWRUtil.getValue("rptType");
		 cotRptFile.rptName = rptName;
		 cotRptFile.rptfilePath = rptfilePath;
		 //cotRptFile.rptImgPath = rptImgPath;
		 if(cotRptFile.rptName=="")
		 {
		 	alert("请上传报表文件！")
		 	return;
		 }
		 //if(cotRptFile.rptImgPath=="")
		 //{
		 	//cotRptFile.rptImgPath = "common/images/zwtp.png";
		 //}
		 var list = new Array();
	     list.push(cotRptFile);
	     DWREngine.setAsync(false); 
	     cotRptFileService.addRptFile(list,function(res){
			alert("添加成功");
			self.opener.location.reload();
			window.close();
		 })	 
		 DWREngine.setAsync(true); 
	}
 
	//加载所属节点下拉框数据
	function bindSel()
	{
		cotRptTypeService.getRptTypeList(function(res){
		bindSelect('rptType',res,'id','rptName');
		})
	}
	
	//初始化表单验证字段
	function validForm()
	{
		appendattribute("rptType","请选择报表类型!","Require");
	}
	
	function initForm()
	{
	　　 validForm();
	    bindSel();
	    uploadRptFile();
	    //uploadRptImg();
	}
	       
	//上传报表文件
	function uploadRptFile()
	{
	    var rptId = $("rptfileid").value;
	    var frame = document.uploadFile;
		frame.location.href = "cotrptfile.do?method=uploadFile"+"&rptId="+rptId;
	} 
	
	//更改报表模板图片
	/*function uploadRptImg()
	{   
	    var rptId = $("rptfileid").value;
	 	var frame = document.uploadImg;
		frame.location.href = "cotrptfile.do?method=uploadImg"+"&rptId="+rptId;
	} */
	
	//加载图片
	/*function loadPic()
	{
		$('rptImgPath').src = rptImgPath;
	}*/
	 	   
</script>

</head>
<body onload="initForm()">

		<div style="width: 100%;">
		    <div style="float: left;width: 100%;margin-top: 10px"> 
				<fieldset>
						 <legend>
							 报表文件上传
						 </legend>	
						 <div style="width: 100%;">
							<form name="queryRptFileForm" id="queryRptFileForm">
								<div style="width: 100%;" align="left">
									<label class="label_22">
										报表类型名称:
									</label>
									<select class="select_38" name="rptType" id="rptType">
									</select>
									 
								</div>
								<input type="hidden" name="rptfileid" id="rptfileid" />
							</form>
					    </div>
					    <%--<label class="label_22"> 模板缩略图上传: </label>
						<div id="uploadImgDiv" style="width: 100%"  >
							<iframe name="uploadImg" style="margin: 0" frameBorder="0" height="200px"
							        width=100% scrolling=no marginheight=0 marginwidth=0 align="left" ></iframe>
					    </div>
					    <label class="label_22"> 报表文件上传: </label>
					    --%>
					    <div id="uploadFileDiv" style="width: 100%;margin-bottom: 0;margin-top: 0" >
							<iframe name="uploadFile" style="margin: 0" frameBorder="0" height="230px"
							        width=100%  scrolling=no  marginheight=0 marginwidth=0 ></iframe>
					    </div> 
				</fieldset>
			 </div>	
			 <%--<div style="float: left;width: 35%;margin-top: 10px"> 
				 <fieldset>
					 <legend>
						 报表文件模板缩略图
					 </legend>
					 <div align="center" style="margin-bottom: 10px;margin-top: 10px"> 
					 <img src="common/images/zwtp.png" id="rptImgPath" width="220" height="250" align="middle"
					 	  onload="javascript:DrawImage(this,220,250)"/>
					 </div>
				 </fieldset>
			</div> 
			--%>
		</div>
		 
		<br/>
			<div style="" align="center">
				<a onclick="add()" style="cursor: hand;"><img
						src="<%=webapp %>/common/images/_save.gif" border="0"
						height="21px" width="61px"></a>	&nbsp;&nbsp;&nbsp;&nbsp;
				<a onclick="javascript:closeandreflashEC(true,'rptfileTable',false);" 
				 		style="cursor: hand;"><img
						src="<%=webapp %>/common/images/_cancel.gif" border="0"
						height="21px" width="61px"></a>	
		   </div>


	</body>
</html>