var SESSION_TIME_TO_LIVE=3*60*1000;
var sessionLastActiveTime=-1;
var POLL_PERIOD=2*1000;
var FIRST_POLL_PERIOD=500;
var sessionId='';
var FILEPATH_SEPARATOR='\\';

//获取文件上传状态bean，并增加相关连接
function fetchFileUpdataStatus(sessionId){
	if (sessionId!=null){
		$('sessionIdHint').innerHTML='Current session id:'+sessionId;
		var ajaxW = new AjaxWrapper(false);
		ajaxW.putRequest(
			'uploadCommand.action',
			'listFile=_&sessionId='+sessionId,
			function(responseText){
				
				var uploadInfo=new XMLDomForAjax(false).deserializedBeanFromXML(responseText);
				refreshUploadList(uploadInfo);
			}
		);
	}
	else{
		alert('Session Id to get upload failed!');
	}
}
//根据IFrame中response field的内容更新FUS 列表
function fetchUploadListFromIFrameIO(){
	if (frames['hiddenIFrame'] && frames['hiddenIFrame'].document){
		var responseXML=new OSUtil().getInputValue(frames['hiddenIFrame'].document,'textarea','response');
		if (responseXML!=null){
			var uploadInfo=new XMLDomForAjax(false).deserializedBeanFromXML(responseXML);
			refreshUploadList(uploadInfo);
		}
	}
}
//刷新上传文件列表
function refreshUploadList(uploadInfo){
	//如果是FUSBean
	if (new ObjectUtil().hasProperty(uploadInfo,'length')==false){
		if (uploadInfo.cancel=='true'){
			$('progressStatusText').innerHTML+='<div id="normalMessageArea"><span>Users upload cancel</span></div>';
			enableUploadFunction();
		}
		else if (uploadInfo.status && uploadInfo.status.indexOf("Error")>=0){
			$('progressStatusText').innerHTML+='<div id="errorArea"><span>Error:'+uploadInfo.status+'</span></div>';
			enableUploadFunction();
		}
	}
	//如果是FUSBean列表
	else if (new ObjectUtil().hasProperty(uploadInfo,'length')){
		var divContent='';
		for(var i=0;i<uploadInfo.length;i++){
			divContent+='<div id="fileField_'+i+'">';
			divContent+='<a href="uploadCommand.action?downFile=_&sessionId='+sessionId+'&fileId='+i+'">'+
				uploadInfo[i].uploadFileURL+'</a>';
			divContent+='&nbsp;&nbsp;';
			
			divContent+='<a href="javascript:deleteFile('+i+')">Del</a>';
			
			divContent+='</div>';
		}
		$('uploadList').innerHTML=divContent;
		enableUploadFunction();
	}
}
//刷新上传状态
function refreshUploadStatus(){
	var ajaxW = new AjaxWrapper(false);
	ajaxW.putRequest(
		'uploadCommand.action',
		'uploadStatus=_&sessionId='+sessionId,
		function(responseText){
			
			var uploadInfo=new XMLDomForAjax(false).deserializedBeanFromXML(responseText);
			//刷新进度条
			var progressPercent = Math.ceil(uploadInfo.readTotalSize / uploadInfo.uploadTotalSize * 100);
			if (isNaN(progressPercent)){
				progressPercent=0;
			}
		    $('progressBarText').innerHTML = ' The progress: '+progressPercent+'% Time-consuming: '+uploadInfo.processRunningTime+' ms';
		    $('progressStatusText').innerHTML=' State feedback: '+uploadInfo.status;
		    $('totalProgressBarBoxContent').style.width = parseInt(progressPercent * 3) + 'px';
		    //一次上传完成后获取FUS列表、并更新文件列表
		    if (uploadInfo.status=='Completion of file uploads'){
		    	enableUploadFunction();
		    	uploadSuccessFlag = 1;
		    }
		    else if (uploadInfo.status=='Cancel the deal completed'){
		    	enableUploadFunction();
		    	uploadSuccessFlag = 2;
		    }
		    else if (uploadInfo.status.indexOf('Error')>0){
		    	alert(uploadInfo.status);
		    	enableUploadFunction();
		    	uploadSuccessFlag = 3;
		    }
		    else if (uploadInfo.status.indexOf('Virus infection')>0){
		    	alert(uploadInfo.status);
		    	enableUploadFunction();
		    	uploadSuccessFlag = 4;
		    }
		    else{
		    	setTimeout(refreshUploadStatus,POLL_PERIOD);
		    }
		}
	);
}

//-------------------------------------------------------------------
//2009-8-20 为了在上传图片后即时刷新,加个标识返回
var uploadSuccessFlag= 0;
//图片显示定时器
var loadInterval;
/*
 * imgId为图片Id
 * src为要显示地址
 * rdmFlag为是否加随机数(0不加,1要加)
 * parentFlag图片是否是父页面的图片
 */
function startShow(imgId,src,rdmFlag,parentFlag){
	loadInterval = setInterval("loadPic('"+imgId+"','"+src+"',"+rdmFlag+","+parentFlag+")", 500);
}
function loadPic(imgId,src,rdmFlag,parentFlag){
	if(uploadSuccessFlag!=0){
		if(uploadSuccessFlag==1){
			if(rdmFlag!=0){
				var rdm = Math.round(Math.random() * 10000);
				src+="&tmp="+rdm;
			}
			if(parentFlag==true){
				parent.document.getElementById(imgId).src = src;
			}else{
				document.getElementById(imgId).src = src;
			}
		}
		uploadSuccessFlag = 0;
		clearInterval(loadInterval); 
	}
}
/*
 *传递上传后要执行的函数名称(字符串) 
 */
 function afterUpload(fun){
	loadInterval = setInterval('checkAfter("'+fun+'")', 500);
}
function checkAfter(fun){
	if(uploadSuccessFlag!=0){
		if(uploadSuccessFlag==1){
			eval(fun)
		}
		uploadSuccessFlag = 0;
		clearInterval(loadInterval); 
	}
}
//-------------------------------------------------------------------

//检测文件是否已经存在于上传文件列表
function checkSameFile(){
	var fileFields=Form.getInputs('fileUploadForm','file','file');
	for(var i=0;i<fileFields.length;i++){
		var fileName=fileFields[i].value;
		
		if (fileName!=''){
			var lastPos=fileName.lastIndexOf(FILEPATH_SEPARATOR);
			if (lastPos>0){
				fileName=fileName.substring(lastPos+1);
			}
		}
		if ($('uploadList').innerHTML.indexOf(fileName)>=0){
			return 	fileName;	
		}
	}
	return '';
}
/**
 * 申请加入Session
 * @param onlyNewSession 是否仅用来申请新的sessionId
 */
function join(onlyNewSession){
	if (checkSameFile()!=''){
		alert('Upload the same file exists in the queue['+checkSameFile()+']!');
		return false;
	}
	if (onlyNewSession){
		sessionId='';
	}
	var ajaxW = new AjaxWrapper(false);
	ajaxW.putRequest(
		'uploadCommand.action',
		'join=_'+'&&sessionId='+sessionId,
		function(responseText){
			var iteractiveMsg=new XMLDomForAjax(false).deserializedBeanFromXML(responseText);
			if (iteractiveMsg.msg!=null && iteractiveMsg.msg.length>0){
				sessionId=iteractiveMsg.msg;	
				$('sessionIdHint').innerHTML=' Get the session Id:'+iteractiveMsg.msg;
				
				if (!onlyNewSession){
					startProgress();
				}
				$('fileUploadForm').submit();
			}
			else{
				alert('Failed to get session id');
				$('sessionIdHint').innerHTML='Error';
			}
		}
	);
}
function enableUploadFunction(){
	$('uploadButton').disabled=false;
	Element.hide('cancelButton');
	Element.show('uploadButton');
	Element.hide('progressBar');
	Element.hide('flowHint');
}
function enableCancelFunction(){
	$('cancelButton').disabled=false;
	Element.hide('uploadButton');
	Element.show('cancelButton');
	Element.show('progressBar');
	Element.show('flowHint');
}
//上传处理
function startProgress(){
	//渲染进度条
    $('progressBarText').innerHTML = ' The progress: 0%';
    $('progressStatusText').innerHTML=' State feedback:';
    $('totalProgressBarBoxContent').style.width =0;

	if (sessionId!=''){
		enableCancelFunction();
		setTimeout(refreshUploadStatus,FIRST_POLL_PERIOD);
	}
	else{
		alert('Unable to get session id');
		$('progressStatusText').innerHTML=' Feedback status: Unable to get session id';
		enableUploadFunction()
	}
    return true;
}
//取消上传处理
function cancelProgress(){
	$('cancelButton').disabled = true;
	var ajaxW = new AjaxWrapper(false);
	ajaxW.putRequest(
		'uploadCommand.action',
		'cancelUpload=_&sessionId='+sessionId,
		//因为form的提交，这可能不会执行
		function(responseText){
			var uploadInfo=new XMLDomForAjax(false).deserializedBeanFromXML(responseText);
			$('progressStatusText').innerHTML=' Feedback status: '+uploadInfo.status;
		}
	);
}
//删除文件
function deleteFile(fileId){
	var ajaxW = new AjaxWrapper(false);
	Element.show('flowHint');
	ajaxW.putRequest(
		'uploadCommand.action',
		'deleteFile=_&sessionId='+sessionId+'&fileId='+fileId,
		function(responseText){
			
			var deserialor=new XMLDomForAjax(false);
			var iMsg=deserialor.deserializedBeanFromXML(responseText);
			if (iMsg.msg){
				if (iMsg.msg=='File list is empty'){
					$('uploadList').innerHTML='';
				}
				else{
					alert(iMsg.msg);
				}				
			}
			else{
				refreshUploadList(iMsg);
			}
			Element.hide('flowHint');
		}
	);
}
//初始化
function initSession(){
	enableUploadFunction();
}
//重置会话
function resetSession(){
	$('uploadList').innerHTML='';
	join(true);
}
//清空表单
function clearFrom()
{
	$('uploadList').innerHTML='';
	$('progressStatusText').innerHTML='';
	$('sessionIdHint').innerHTML='';
}
//上传判断,成功返回文件名
function uploadCheck(filePath){
   if(filePath==''){
   		return "Pls. select a picture, then upload!";
   }
   //截取出文件名
   var s = filePath.lastIndexOf("\\");
   var filename=filePath.substring(s+1,111111);
   //判断文件类型
   var sType = filename.lastIndexOf(".");
   if(sType<0){
   		return "Pls. select a picture, then upload!";
   }else{
	   var fType=filename.substring(sType+1,111111);
	   if(fType.toLowerCase()!='img' && fType.toLowerCase()!='jpg' && fType.toLowerCase()!='gif' && fType.toLowerCase()!='bmp'&& fType.toLowerCase()!='png'){
		   	return "Can only upload png、img、jpg、gif、bmp！";
	   }else{
	   		var name = filename.substring(0,sType);
	   		return "_"+name;
	   }
   }
}