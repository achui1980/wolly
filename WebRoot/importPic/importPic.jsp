<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
	String webapp=request.getContextPath();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
		<jsp:include page="/common.jsp"></jsp:include>
		<script type='text/javascript' src='<%=webapp %>/dwr/interface/importPicService.js'></script>
		
		<script type='text/javascript' src='<%=webapp %>/dwr/interface/contexUtil.js'></script>
		<script type="text/javascript" src="<%=webapp %>/common/js/swfupload_2.2.js"></script>
		<script type="text/javascript" src="<%=webapp %>/common/js/handlers.js"></script>
		<link href="<%=webapp %>/common/css/swfUpload.css" rel="stylesheet" type="text/css" />
		<script type="text/javascript">
		
	var uploadPath = "";
	
	var swfu ;
	
	function initUploadCompent()
	{
			// Max settings
			
		swfu = new SWFUpload({
		/*
			create_ui : true,
			debug : false,
			upload_script : "<%=webapp %>/servlet/TestServletUpload",	
			target : "SWFUploadTarget",
			flash_path : "<%=webapp %>/common/js/SWFUpload.swf",
			allowed_filesize : 102400,	// 30 MB
			allowed_filetypes : "*.*",
			allowed_filetypes_description : "All files...",
			browse_link_ID : "browse_link",
			upload_link_ID : "upload_link",
			flash_loaded_callback : 'swfu.flashLoaded',
			//upload_file_queued_callback : "fileQueued",
			upload_file_queued_callback : "fileQueuedByCustomize",
			upload_file_start_callback : 'uploadFileStart',
			upload_progress_callback : 'uploadProgress',
			upload_file_complete_callback : 'uploadFileComplete',
			upload_file_cancel_callback : 'uploadFileCancelled',
			upload_queue_complete_callback : 'uploadQueueComplete',
			upload_error_callback : 'uploadError',
			upload_cancel_callback : 'uploadCancel',
			auto_upload : false,
			upload_file_error_callback:"whenerror"
		*/
		// Backend Settings
			upload_url: "<%=webapp %>/servlet/PicUploadServlet",	// Relative to the SWF file (or you can use absolute paths)
			/*
			post_params : {
                   "JAVASESSID" : "<%=request.getSession().getId()%>"
               },
               */
			// File Upload Settings
			file_size_limit : "300",	// 200 kb
			file_types : "*.jpg;*.gif;*.png;*.bmp;*.img",
			file_types_description : "Image Files",
			file_upload_limit : "300",
			
			//设置上传按钮
			button_image_url : "<%=webapp %>/common/images/AttachButtonUploadText_61x22.png",
			button_width : 81,
			button_height : 22,
			button_action : SWFUpload.BUTTON_ACTION.SELECT_FILES,
			button_placeholder_id : "spanButtonPlaceholder",
			//button_text : '<span class="btnText">选择图片</span>',
			button_text_style : ".btnText { font-size: 12; font-family:宋体;margin-left:28%;}",
			button_text_top_padding : 3,
			button_text_left_padding : 0,
			//设置上传按钮结束
			//file_queue_limit : "0",
			//swfupload_element_id : "flashUI1",
			// Event Handler Settings (all my handlers are in the Handler.js file)
			file_dialog_start_handler : fileDialogStart,
			file_queued_handler : fileQueued,
			file_queue_error_handler : fileQueueError,
			file_dialog_complete_handler : fileDialogComplete,
			upload_start_handler : uploadStart,
			upload_progress_handler : uploadProgress,
			upload_error_handler : uploadError,
			upload_success_handler : uploadSuccess,
			upload_complete_handler : uploadComplete,
			
			// Flash Settings
			flash_url : "<%=webapp %>/common/js/swfupload_10f.swf",	// Relative to this file (or you can use absolute paths)
			// Debug Settings
			debug: false //调试信息框： true 打开；false 关闭；
		});
		//fix flash refresh;
		//swfu.loadUI();
		//swfu.loadFlash();
	}
	
	//导入图片
	function importPicture()
	{
	    DWRUtil.useLoadingMessage("正在导入数据，请等待..........."); 
	    DWREngine.setAsync(false);
	    //获得导入策略
		var id = $('id').value;
		if(id==''){
			alert("请选择单据编号！")
			return;
		} 
		//未导入图片数量
		var num = $('unImportPicName').options.length;
		if(num==0){
	     	alert("请上传图片后再执行导入图片！");
        	return;
	    }
	    var type = $('type').value;
		importPicService.updatePicture(parseInt(id),type,function(msgList){
		    for(var i=0;i<msgList.length;i++){
	   		   if(i==msgList.length-1&&i!=1){
		   		     var successSum = msgList[i].coverSum+msgList[i].addSum;
		   		     $("importMessage").innerHTML = "<br/>图片导入信息：<br/><br/>覆盖"+msgList[i].coverSum+"张图片<br/>共成功导入"+successSum+"张图片";
		   		   	 $("messageDiv").style.display = "block";
		   		   	 $("importMessageDiv").style.display = "block"; 
		   		   	 $("checkMessageDiv").style.display = "none";
	   		   }
	   		   if(i==1){
		   		     var successSum = msgList[0].coverSum+msgList[0].addSum;
		   		     $("importMessage").innerHTML = "<br/>图片导入信息：<br/><br/>覆盖"+msgList[0].coverSum+"张图片<br/>共成功导入"+successSum+"张图片";
		   		   	 $("messageDiv").style.display = "block";
		   		   	 $("importMessageDiv").style.display = "block"; 
		   		   	 $("checkMessageDiv").style.display = "none";
	   		   }
	   		}
		});
		DWREngine.setAsync(true); 
		bindSel();
	}
	
	 
	
	 
	//设置图片上传限制
	function setupUploadConfig()
	{
		var file_upload_limit = parseInt ($("uploadLimit").value);
	    if(isNaN(file_upload_limit) || file_upload_limit == 0)
	    {
	    	$("uploadLimit").value = "300";
	    	file_upload_limit = 300;
	    }  
	    if(file_upload_limit > 1000)
	    {
	    	alert("上传图片数过多，最多1000张")
	    	file_upload_limit = 1000;
	    	$("uploadLimit").value = "1000";
	    }
		swfu.setFileUploadLimit(file_upload_limit);
		//判断输入的内容是否违规
		var file_size_limit = parseInt ($("sizeLimit").value);
		if(isNaN(file_upload_limit) || file_size_limit == 0)
	    {
	    	$("sizeLimit").value = "300";
	    	file_size_limit = 300;
	    }  
	    if(file_size_limit > 1024)
	    {
	    	alert("每张最大不能大于1024KB（1MB），最多1024KB")
	    	file_size_limit = 1024;
	    	$("sizeLimit").value = "1024";
	    }
	    swfu.setFileSizeLimit(file_size_limit);
	    alert("设置成功");
	}
	
	
	//显示未导入图片信息
	function showUnImportPicName()
	{     
		DWREngine.setAsync(false); 
		initUploadCompent();
		bindSel();
	    //未导入图片数量
		var num = $('unImportPicName').options.length;
		//无图片可导入
	    if(num==0){
	     	$("aDiv").style.display = "block";
	    }else{
	     	//有图片尚未导入
	     	$("bDiv").style.display = "block";
	    }
	    DWREngine.setAsync(true); 
	}
	
	//加载未导入图片信息下拉列表
	function bindSel()
	{ 
		 importPicService.getUnImportPicList(function(res){
		 	bindSelect('unImportPicName',res,'picName','picName',false);
		 });
	} 
	
	
	//打开上传组件
	function uploadPic(){
	 	${"uploadwin"}.style.display = "block";
	}
	
	//关闭上传组件
	function closeuploadwin()
	{
		 ${"uploadwin"}.style.display = "none";
	}
	
	//刷新页面
	function refresh()
	{
		window.location.reload();
	}
	
	
	//显示查找单据编号的层
	function showGetNoDiv(e){
		var obj = $("getNoDiv").style.display;
		if(obj=='none'){
			//设置层显示位置
			var t=e.offsetTop;
			var l=e.offsetLeft;
			while(e=e.offsetParent){
				t+=e.offsetTop;
				l+=e.offsetLeft;
			}
			
			var type = $('type').value;
			if(type==''){
			 	alert("请先选择单据类型！");
			 	return;
			}
			if(type=="price"){
			    var frame = document.getNoFrame;
				frame.location.href = "<%=webapp %>/cotquery.do?method=queryPriceNo";
			}
			if(type=="order"){
				var frame = document.getNoFrame;
				frame.location.href = "<%=webapp %>/cotquery.do?method=queryOrderNo";
			}
			document.getElementById("getNoDiv").style.top=t+20;
			document.getElementById("getNoDiv").style.left=l-250;
			document.getElementById("getNoDiv").style.display ="block";
			
		}else{
			$("getNoDiv").style.display='none';
		}
	}
	
	//单据类型改变事件(重置单据编号表单)
	function resetNo(){
		$('id').value = '';
		$('No').value = '';
	} 
	 
	//根据选择的单据编号自动填写表单
	function setNoForm(id,No){
		//隐藏查询单据编号层
		$("getNoDiv").style.display='none';
		$('id').value = id;
		$('No').value = No;
	}
  
    //页面初始化 
    function initForm(){
    	//显示未导入图片信息
   		showUnImportPicName();
   		var getType = $("getType").value;
   		var getId = $("getId").value;
   		var getNo = $("getNo").value;
   		if(getId!="null"){
   			$("type").value = getType;
   			$("id").value = getId;
   			$("No").value = getNo;
   			$("type").disabled = "disabled";
   			$("No").disabled = "disabled";
   		}
    }
  
</script>
	</head>
	<% 
		String getType = request.getParameter("type");
		String id = request.getParameter("id");
		String No = request.getParameter("No");
	%>
	<body onload="initForm()">
		
		<input type="hidden" name="res" id="res" value="${res}" />
		<input type="hidden" name="getType" id="getType" value="<%=getType%>" />
		<input type="hidden" name="getId" id="getId" value="<%=id%>" />
		<input type="hidden" name="getNo" id="getNo" value="<%=No%>" />
		<div class="detail_nav">
			&nbsp;&nbsp; Picture import
		</div>
		<br />
		<fieldset style="float: left; width: 45%;">
			<legend>
				Picture info
			</legend>
			<br />
			<div style="width: 100%" align="center">
				<label style="color: red">
					Note:Import file format must be jpg、gif、png、bmp、img！
				</label>
			</div>
			<br />
			<div id="aDiv" style="display: none" style="width:100%"
				align="center">
				<label>
					Not picture import
				</label>
			</div>
			<div id="bDiv" style="display: none" style="width:100%"
				align="center">
				<label>
					Not imported picture information:
				</label>
			</div>

			<div style="width: 100%;" align="center">
				<select class="select_80" name="unImportPicName"
					id="unImportPicName" size="20" style="margin-left: 40px">
				</select>
			</div>
			<br />
		</fieldset>

		<div style="float: left; width: 48%;">
			<fieldset>
				<legend>
					Operation
				</legend>
				<br />
				<div id="importDiv" align="center" style="width: 100%">
					<div style="width: 100%;" >
						<label class="label_26">
							Types:
						</label>
						<select class="select_20" name="type" id="type" onchange="resetNo()" >
							<option value="">Pls.select</option>
							<option value="price">Quote</option>
							<option value="order">Order</option>
						</select>
						<label class="label_14">
							No.：
						</label>
						<input type="hidden" name="id" id="id"/>
						<input class="input_30" name="No" id="No" onclick="showGetNoDiv(this)" readonly="readonly"/>
					</div>
					<div style="width: 100%" align="center">
						<br />
						<label style="color: red">
							Note:Pls. be patient！
						</label>
					</div>
					<div style="width: 100%; margin-top: 10px; float: left;">
						<div style="width: 100%; margin-bottom: 5px;">
							<div style="float: left; margin-left: 30%">
								<a onclick="uploadPic()" style="cursor: hand; margin-top: 4px;"><img
										id="uploadBtn" src="<%=webapp %>/common/images/_uppic.gif"
										border="0" height="21px" width="81px"> </a>
							</div>
							<div style="margin-left: 10px; float: left;">
								<button class="importBtn" id="importBtn" onclick="importPicture()">
									<font style="margin-top: 14px; margin-left: 20px;"
										color="black">  Picture import </font>
								</button>
							</div>
						</div>
					</div>
					<br />
			</fieldset>
			<div id="messageDiv" style="display: none">
				<fieldset>
					<legend>
						操作结果
					</legend>
					<div id="checkMessageDiv"
						style="display: none; width: 60%; margin-left: 100px" align="left">
						<label id="checkMessage"></label>
					</div>
					<div id="importMessageDiv"
						style="display: none; width: 60%; margin-left: 100px" align="left">
						<label id="importMessage"></label>
					</div>
					<%--<div style="margin-top: 50px; float: right;">
						<a href="sample/elements/elementsframe.jsp" style="cursor: hand;">【返回样品资料】</a>
					</div>
				--%>
				</fieldset>
			</div>
		</div>

		<!-- 图片上传组件 -->
		<div id="uploadwin" class="overlayer_execu_coll">

			<div class="but_01">
				<a id="browse_link" style="cursor: hand; margin-right: 1.2%;"><span
					id="spanButtonPlaceholder"></span> </a>
				<a onclick="swfu.startUpload();" id="upload_link"
					style="cursor: hand;"><img
						src="<%=webapp %>/common/images/_up.gif" border="0" height="21px"
						width="61px"> </a>
				<a onclick="closeuploadwin();" style="cursor: hand;"><img
						src="<%=webapp %>/common/images/_toff.gif" border="0" height="21px"
						width="61px"> </a>
				<a onclick="javascript:cancelQueue();" id="clear_all"
					style="cursor: hand;"><img
						src="<%=webapp %>/common/images/_clear.gif" border="0" height="21px"
						width="61px"> </a>
			</div>

			<div class="note">
				Upload limit：Not more than
				<input type="text" id="uploadLimit" value=300 style="width: 30px" />
				PIC，Image size does not exceed
				<input type="text" id="sizeLimit" value=300 style="width: 35px" />
				K/PC
				<button style="width: 30px" onclick="setupUploadConfig();">
					Set
				</button>
			</div>
			<div id="bg_02">
				<div class="file">
					<span class="font_red" id="fileCount">0</span> Picuture，Upload
					<span class="font_red" id="fileTotleSize">0K</span>
				</div>
				<div class="table" id="fsUploadProgress1">
					<table width="100%" border="0" cellpadding="5" cellspacing="1"
						bgcolor="#CCCCCC">
						<tr>
							<td bgcolor="#EAEAEA">
								<strong>Picture Name</strong>
							</td>
							<td width="150px" bgcolor="#EAEAEA">
								<strong>Picture Size</strong>
							</td>
							<td width="50px" bgcolor="#EAEAEA">
								<strong>Clear</strong>
							</td>
						</tr>
					</table>

					<div id="box"
						style="overflow: scroll; height: 280px; width: 100%; font-size: 12px; color: #999999; font-style: normal;">
						<table id="fileList" cellpadding="5" cellspacing="1">
						</table>
					</div>
				</div>
			</div>
		</div>
		<!-------------------------- 查找单据编号的隐藏层 ------------------------------------->
		<div id="getNoDiv"
			style="display: none; position: absolute; z-index: 2;width: 420px; background-color: #96CEE5; padding: 0.2%;"
			align="center">
			<iframe name="getNoFrame" style="margin: 0; z-index: 10;" frameBorder="0"
				width=100% height="280px" scrolling=yes marginheight=0 marginwidth=0></iframe>
		</div>
		<!-------------------------- 查找单据编号的隐藏层(结束) ------------------------------------->
	</body>


</html>
