
/* This is an example of how to cancel all the files queued up.  It's made somewhat generic.  Just pass your SWFUpload
object in to this method and it loops through cancelling the uploads. */
function cancelQueue(instance) {
	document.getElementById(instance.customSettings.cancelButtonId).disabled = true;
	instance.stopUpload();
	var stats;
	
	do {
		stats = instance.getStats();
		instance.cancelUpload();
	} while (stats.files_queued !== 0);
	
}

/* **********************
   Event Handlers
   These are my custom event handlers to make my
   web application behave the way I went when SWFUpload
   completes different tasks.  These aren't part of the SWFUpload
   package.  They are part of my application.  Without these none
   of the actions SWFUpload makes will show up in my application.
   ********************** */
function fileDialogStart() {
	/* I don't need to do anything here */
	//alert("fileDialogStart");
}
var totalSize=0;
function fileQueued(file) {
	try {
		// You might include code here that prevents the form from being submitted while the upload is in
		// progress.  Then you'll want to put code in the Queue Complete handler to "unblock" the form
		//alert("fileQueued");
		
		totalSize+=file.size;
		var listingfiles = document.getElementById("fileList");	
		var tr = listingfiles.insertRow();
		tr.id=file.id;
		
		var flag = tr.insertCell(); flag.className='flag2';
			var fileName = tr.insertCell();
			fileName.innerHTML=file.name;
			//fileName.width="300px";
			
			var progressTD = tr.insertCell(); progressTD.className='progressTD';
			//progressTD.width= "30px";
			progressTD.innerHTML='<span id="' + file.id +'progress" class="progressBar"></span><span style="width:47">'+getNiceFileSize(file.size)+'</span>';
			
			var del = tr.insertCell(); 
			//del.style="width : 300px";
			del.className='delectBtn';
			del.id=file.id +
		 'deleteGif'; 
			del.innerHTML='<a href="javascript:swfu.cancelUpload(\'' + file.id + '\')"><img src="common/images/_del-x.gif"  width="13" height="13" border="0" /></a>';
		showInfo(file, listingfiles.rows.length);

	} catch (ex) {
		alert(ex);
	}

}

function fileQueueError(file, errorCode, message) {
	try {
		//alert("fileQueueError");
		if (errorCode === SWFUpload.QUEUE_ERROR.QUEUE_LIMIT_EXCEEDED) {
			alert("Too many one-time upload files.\n" + (message == 0 ? "Has reached the maximum number of files uploaded." : "You can also choose " + (message > 1 ? "Upload " + message + " Files." : "A file.")));
			return;
		}

		//var progress = new FileProgress(file, this.customSettings.progressTarget);
		//progress.setError();
		//progress.toggleCancel(false);

		switch (errorCode) {
		case SWFUpload.QUEUE_ERROR.FILE_EXCEEDS_SIZE_LIMIT:
			//progress.setStatus("File is too big.");
			alert("Upload error: Upload file is too large, File Name: " + file.name + ", File Size: " + file.size + ", Error message: " + message);
			break;
		case SWFUpload.QUEUE_ERROR.ZERO_BYTE_FILE:
			//progress.setStatus("Cannot upload Zero Byte files.");
			alert("Upload error: 0 byte file exists, File Name: " + file.name + ", File Size:  " + file.size + ", Error message: " + message);
			break;
		case SWFUpload.QUEUE_ERROR.INVALID_FILETYPE:
			//progress.setStatus("Invalid File Type.");
			alert("Upload error: Invalid file type, File Name: " + file.name + ", File Size:  " + file.size + ", Error message: " + message);
			break;
		case SWFUpload.QUEUE_ERROR.QUEUE_LIMIT_EXCEEDED:
			alert("Upload error：The total number of files uploaded over the queue limits.  " +  (message > 1 ? "Can only add " +  message + " Files" : "Upload queue is full, can not continue to add."));
			break;
		default:
			if (file !== null) {
				progress.setStatus("Unhandled Error");
			}
			alert("Upload error: Code：" + errorCode + ", File Name: " + file.name + ", File Size: " + file.size + ", Error message: " + message);
			break;
		}
	} catch (ex) {
        this.debug(ex);
    }
}
/**
	�@ʾ��Ϣ
**/
function showInfo(file,queuelength){
	var fileCount = document.getElementById("fileCount");
	fileCount.innerText=queuelength;
	var fileTotleSize = document.getElementById("fileTotleSize");
	fileTotleSize.innerText = getNiceFileSize(totalSize);
}
function fileDialogComplete(numFilesSelected, numFilesQueued) {
	try {
		//alert("fileDialogComplete");
		//alert(numFilesSelected);
		//alert(numFilesQueued);
		if (this.getStats().files_queued > 0) {
			//document.getElementById(this.customSettings.cancelButtonId).disabled = false;
		}
		
		/* I want auto start and I can do that here */
		//this.startUpload();
	} catch (ex)  {
        this.debug(ex);
	}
}

function uploadStart(file) {
	try {
		//alert("uploadStart");
		/* I don't want to do any file validation or anything,  I'll just update the UI and return true to indicate that the upload should start */
		//var progress = new FileProgress(file, this.customSettings.progressTarget);
		//progress.setStatus("Uploading...");
		//progress.toggleCancel(true, this);
	}
	catch (ex) {
	}
	
	return true;
}

function uploadProgress(file, bytesLoaded, bytesTotal) {

	try {
		//alert("uploadProgress");
		//var percent = Math.ceil((bytesLoaded / bytesTotal) * 100);

		var progress = document.getElementById(file.id + "progress");	
		var percent = Math.ceil((bytesLoaded / file.size) * 100)
		progress.style.backgroundPositionX=(percent-100)+'px';
	} catch (ex) {
		this.debug(ex);
	}
}

function uploadSuccess(file, serverData) {
	try {
		//alert("uploadSuccess");
		//var progress = new FileProgress(file, this.customSettings.progressTarget);
		//progress.setComplete();
		//progress.setStatus("Complete.");
		//progress.toggleCancel(false);
		var listingfiles = document.getElementById("fileList");	
		//alert(file.id);
			var tR = document.getElementById(file.id);
			//alert(tr.innerHTML)
		tR.className += " fileUploading";
		tR.cells(0).className='flag1';
	
		//var progress = document.getElementById(file.id + "progress");	
		//progress.style.backgroundPositionX='0px';
		//cancelImage;
		var deleteImg = document .getElementById(file.id + 'deleteGif'); 
  		deleteImg.innerHTML = "";	

	} catch (ex) {
		this.debug(ex);
	}
}

function uploadComplete(file) {
	try {
		//alert("hello");
		//alert("uploadComplete");
		/*  I want the next upload to continue automatically so I'll call startUpload here */
		if (this.getStats().files_queued === 0) {
			alert("上传完毕");
			//调用图片导入页面刷新方法
			refresh();
		} else {	
			this.startUpload();
		}
	} catch (ex) {
		this.debug(ex);
	}

}

function uploadError(file, errorCode, message) {
	try {
		//alert("uploadError,errorCode:"+errorCode);
		//var progress = new FileProgress(file, this.customSettings.progressTarget);
		//progress.setError();
		//progress.toggleCancel(false);

		switch (errorCode) {
		case SWFUpload.UPLOAD_ERROR.HTTP_ERROR:
			//progress.setStatus("Upload Error: " + message);
			alert("Upload error: HTTP ERROR, file: " + file.name + ", Error Message: " + message);
			break;
		case SWFUpload.UPLOAD_ERROR.MISSING_UPLOAD_URL:
			//progress.setStatus("Configuration Error");
			alert("Upload error:Upload invalid address, File Size: " + file.name + ", Error: " + message);
			break;
		case SWFUpload.UPLOAD_ERROR.UPLOAD_FAILED:
			//progress.setStatus("Upload Failed.");alertdebug("Error Code: Upload Failed, File name: " + file.name + ", File size: " + file.size + ", Message: " + message);
			break;
		case SWFUpload.UPLOAD_ERROR.IO_ERROR:
			//progress.setStatus("Server (IO) Error");
			alert("Upload error: IO error occurred, File name: " + file.name + ", Error: " + message);
			break;
		case SWFUpload.UPLOAD_ERROR.SECURITY_ERROR:
			//progress.setStatus("Security Error");
			alert("Upload error:Security error occurred, File name: " + file.name + ", Error: " + message);
			break;
		case SWFUpload.UPLOAD_ERROR.UPLOAD_LIMIT_EXCEEDED:
			//progress.setStatus("Upload limit exceeded.");
			alert("Upload error: Upload limit exceeded, File name: " + file.name + ", File Size: " + file.size + ", Error: " + message);
			break;
		case SWFUpload.UPLOAD_ERROR.SPECIFIED_FILE_ID_NOT_FOUND:
			//progress.setStatus("File not found.");
			alert("Upload error: Can not find the corresponding file, File name: " + file.name + ", File Size: " + file.size + ", Error: " + message);
			break;
		case SWFUpload.UPLOAD_ERROR.FILE_VALIDATION_FAILED:
			//progress.setStatus("Failed Validation.  Upload skipped.");
			alert("Error Code: File Validation Failed, File name: " + file.name + ", File size: " + file.size + ", Message: " + message);
			break;
		case SWFUpload.UPLOAD_ERROR.FILE_CANCELLED:
		/*
			if (this.getStats().files_queued === 0) {
				document.getElementById(this.customSettings.cancelButtonId).disabled = true;
			}
			progress.setStatus("Cancelled");
			progress.setCancelled();
		*/
		uploadFileCancelled(file);
			break;
		case SWFUpload.UPLOAD_ERROR.UPLOAD_STOPPED:
			//progress.setStatus("Stopped");
			break;
		default:
			//progress.setStatus("Unhandled Error: " + error_code);
			alert("Upload error: Code：" + errorCode + ", Files name: " + file.name + ", File Size: " + file.size + ", Error: " + message);
			break;
		}
	} catch (ex) {
        this.debug(ex);
    }
    
}
function uploadFileComplete(file) {
	var tR = document.getElementById(file.id);
	tR.className += " fileUploading";
	tR.cells(0).className='flag1';
	
	//var progress = document.getElementById(file.id + "progress");	
	//progress.style.backgroundPositionX='0px';
	//cancelImage;
	var deleteImg = document .getElementById(file.id + 'deleteGif')
 ; 
  deleteImg.innerHTML = "";	
}
/**
	ĳ���ς��ļ���ȡ��
**/
function uploadFileCancelled(file) {   
	var listingfiles = document.getElementById("fileList");
	
	var rows = listingfiles.rows;
	for(var i=rows.length-1;i>=0;i--){
		if(file.id==rows[i].id){
			listingfiles.deleteRow(i);
			//�۳�Filesize 
			totalSize = totalSize -file.size;
			break;
		}
	}	
	showInfo(file, listingfiles.rows.length);
}

/**
��������ς�n��
**/
function cancelQueue() {
	
	isCancelAll  = true; 
	totalSize = 0;	
	clearhQueue();
}
/**
	̎��UI���h�����������
**/
function  clearhQueue(){
	var listingfiles = document.getElementById("fileList");
	var rowLength = listingfiles.rows.length;
	while(rowLength>0){
		swfu.cancelUpload();	
		listingfiles.deleteRow(0);
		rowLength--;
	}
	var fileCount = document.getElementById("fileCount");
	fileCount.innerText=0;
	var fileTotleSize = document.getElementById("fileTotleSize");
	fileTotleSize.innerText = getNiceFileSize(totalSize);

}

var _K = 1024;
var _M = _K*1024;
function getNiceFileSize(bitnum){
	if(bitnum<_M){
		if(bitnum<_K){
			return bitnum+'B';
		}else{
			return Math.ceil(bitnum/_K)+'K';
		}
		
	}else{
		return Math.ceil(100*bitnum/_M)/100+'M';
	}
	
}