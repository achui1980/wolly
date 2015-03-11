<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8" %>
 <%
	String webapp=request.getContextPath();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"  >
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<jsp:include page="../../common.jsp"></jsp:include>
<script type='text/javascript' src='<%=webapp %>/dwr/interface/cotFileService.js'></script> 
<script type="text/javascript" src="<%=webapp %>/common/js/prototype.js"></script>
<script type="text/javascript" src="<%=webapp %>/common/js/newAjaxWrapper.js"></script>
<script type="text/javascript" src="<%=webapp %>/common/js/upload.js"></script>
<script type="text/javascript" src="<%=webapp %>/common/js/tabpane.js"></script>
<link href="<%=webapp %>/common/css/fileUpload.css" type="text/css" rel="stylesheet" />
<link rel="stylesheet" type="text/css" href="<%=webapp %>/common/css/luna/tab.css" />
<script type="text/javascript">

	 
	//上传　
	function upload()
	{
	    var str1 = $("file").value;
	    if(str1==""){
	   		 alert("请选择文件, 再上传!");
	   		 return;
	    }
	    //截取出文件名
	    var s = str1.lastIndexOf("\\");
	    var filename=str1.substring(s+1,str1.length);
	    var isExist=false;
	    DWREngine.setAsync(false); 
	    cotFileService.findExistName(filename,function(res){
	   	 	 isExist = res;
	    });
	     //判断有无同名文件
	    if(isExist)
		{
		 	var aa =window.confirm("已存在同名文件,是否覆盖？")
		 	if(aa) 
		    {    
		         cotFileService.deleteByFileName(filename,function(res){
			    	  join(false);
			     });
		    }
		    return;
		}
		else{
		    join(false);
		}
		DWREngine.setAsync(true);
		alert("上传成功！");
		parent.Hide();
		//setTimeout(" alert('上传完成后，请刷新页面查看更新！')", 600); 
	}
	 
	function initForm(){
		clearFrom();
	}

</script>
</head>
	<%
		String eleId = request.getParameter("eleId");
	%>
<body onload="initForm()">
        <!-- 文件上传组件 -->
		<div id="uploadTable" >
			<div style="display: inline;"
				id="aazone.fileTableConditionFormAjaxZone">
				<div id="controlPanel">
					<div id="flowHint" style="display: none">
						<img src="<%=webapp %>/common/images/loading.gif" />
					</div>
					<div id="readme">
						<span>上传说明:&nbsp;&nbsp;最大上传量:5M，单个文件最大长度:5M</span>
					</div>
					<div id="progressStatusText"></div>
					<div id="sessionIdHint"></div>
					<div id="uploadFormDiv01">
						<form id="fileUploadForm" name="fileUploadForm"
							action="./uploadfile.action" enctype="multipart/form-data"
							method="post" target="hiddenIFrame">
							<input type="file" name="file" id="file" size="20" />
							<input type="hidden" name="eleId" id="eleId" value="<%=eleId%>" />
							<input type="button" name="uploadButton" id="uploadButton"
								value="上传" />
							<input type="button" name="cancelButton" id="cancelButton"
								value="取消" />
							<label class="label_20" style="text-align:right;color:#000;">
								文件描述：
							</label>
							<textarea name="fileDesc" id="fileDesc" rows="2" cols="31"
								class="input_77"></textarea>	
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
		<!----------------------- 文件上传组件(结束) ----------------------------------> 
		<form name="eleFileForm" id="eleFileForm" onsubmit="return false">
			
		</form>  
	  	 
	</body>
</html>