<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8" %>
 <%
	String webapp=request.getContextPath();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" >
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>报表文件修改页面</title>
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
	
	//修改
	function  mod()
	{
		//添加权限判断
		var isPopedom = getPopedomByOpType(vaildUrl,"MOD");
		if(isPopedom == 0)//没有修改权限
		{
			Ext.Msg.alert("提示信息","您没有修改权限");
			return;
		}
		var obj = DWRUtil.getValues("queryRptFileForm");
		var cotRptFile = new CotRptFile();
	   
	    cotRptFile.rptType = DWRUtil.getValue("rptType");
		cotRptFile.rptName = rptName;
		cotRptFile.rptfilePath = rptfilePath;
		if(cotRptFile.rptName=="")
		{
		 	Ext.Msg.alert("提示信息","请上传报表文件！")
		 	return;
		}
	    cotRptFile.id = $("rptfileid").value;
	    var list = new Array();
	    list.push(cotRptFile);
	    cotRptFileService.modifyRptFile(list,function(res){
	    	Ext.Msg.alert("提示信息","修改成功！");
	    	self.opener.location.reload();
			window.close();
	    })
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
	
	
	var rName = "";
	//页面初始化
	function initForm()
	{
		DWREngine.setAsync(false); 
	　　 validForm();
	    bindSel();
	    var id = $("rptfileid").value;
        cotRptFileService.getRptFileById(parseInt(id),function(res){
	        DWRUtil.setValues(res);
	        rName = res.rptName;
		})
		uploadRptFile();
		DWREngine.setAsync(true); 
	}
	       
	//上传报表文件
	function uploadRptFile()
	{
	    var rptId = $("rptfileid").value;
	    var frame = document.uploadFile;
		frame.location.href = "cotrptfile.do?method=uploadFile"+"&rptId="+rptId;
	} 
	 	   
</script>

</head>
	<%
		String id = request.getParameter("id");
	%>
<body onload="initForm()">
		<input type="hidden" name="rptfileid" id="rptfileid" value="<%=id%>" />
		
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
							</form>
					    </div>
					   <div id="uploadFileDiv" style="width: 100%;margin-bottom: 0;margin-top: 0" >
							<iframe name="uploadFile" style="margin: 0" frameBorder="0" height="230px"
							        width=100%  scrolling=no  marginheight=0 marginwidth=0 ></iframe>
					    </div> 
				</fieldset>
			 </div>	
		</div>
		<br/>
		<div style="" align="center">
			<a onclick="mod()" style="cursor: hand;"><img
					src="<%=webapp %>/common/images/_save.gif" border="0"
					height="21px" width="61px"></a>	&nbsp;&nbsp;&nbsp;&nbsp;
			<a onclick="javascript:closeandreflashEC(true,'rptfileTable',false);" 
			 		style="cursor: hand;"><img
					src="<%=webapp %>/common/images/_cancel.gif" border="0"
					height="21px" width="61px"></a>	
	   </div>


	</body>
</html>