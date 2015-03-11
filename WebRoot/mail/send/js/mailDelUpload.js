//删除文件方法
   function deleteMailAttachFile(fileKey){
   	  var formPanel = Ext.getCmp(EXT_MAIL_ID_OUTBOUND_FROMPANEL); 
   	  var requestConfig = {
		url :'uploadMailFile.action?m=deleteFile&name='+encodeURI(encodeURI(fileKey))+'&random='+formPanel.random,//请求的服务器地址
		callback : function(options,success,response){//回调函数
			var i=response.responseText
			var attachPanel = Ext.getDom('mail_send_attach_panel');
			attachPanel.attachCount--;
			if(attachPanel.attachCount==0){
			   Ext.getDom("mail_send_attach_panel").style.display="none";
			   formPanel.doLayout();
			}
			Ext.getDom(fileKey).style.display="none";	
		}
	  };
	 Ext.Ajax.request(requestConfig);//发送请求
   }
   function getMailAttachFile(fileKey){
   		var formPanel = Ext.getCmp(EXT_MAIL_ID_OUTBOUND_FROMPANEL); 
		var requestConfig = {
			url :'uploadMailFile.action?m=getFileUrl&name='+encodeURI(encodeURI(fileKey))+'&random='+formPanel.random,//请求的服务器地址
			callback : function(options,success,response){//回调函数
				var i=response.responseText;
				window.open('downLoadMailFile.down?'+Ext.urlEncode(Ext.decode(i)),"_blank");
			}
		  };
		 Ext.Ajax.request(requestConfig);//发送请求
   }
       
Ext.mail.SwfUploadPanel = Ext.extend(Ext.ux.SwfUploadPanel,{
	uploadComplete: function(file, result) {
		if (this.cancelled) {
			this.cancelled = false;
		} else {
			var o = Ext.decode(result);	
			this.store.getById(file.id).set('status', 2);
			this.store.getById(file.id).commit();
			this.progress_bar.reset();
			this.progress_bar.updateText('上传进度');
			
			if (this.remove_completed) {
				this.store.remove(this.store.getById(file.id));
			}
			this.fireEvent('fileUploadComplete', this, file, o);
	//上传成功返回指定文件名	
			var attachPanel = Ext.getDom('mail_send_attach_panel');
			attachPanel.style.display="block";
			var filename=file.name;
			attachPanel.attachCount = attachPanel.attachCount || 0;
			attachPanel.attachCount++;
			attachPanel.nameIndex = attachPanel.nameIndex || 0;
			attachPanel.nameIndex++;
			var sizeShow = '';
			if(file.size<1024){
				sizeShow = file.size+'字节';
			}else if(file.size<(1024*1024)){
				sizeShow = (file.size/1024).toFixed(2) + 'KB';
			}else{
				sizeShow = (file.size/1024/1024).toFixed(2) + 'MB';				
			}
			var fileKey = filename.replace(MAIL_SEND_ATTACH_UPLOAD_RG,"")+"*"+attachPanel.nameIndex;
            var file="<div  style='padding:3px;display:block;' id='"+fileKey+"'>"+filename+
            			"<span style='color:#888888;'>（"+sizeShow+"）</span>" +
            			"<span style='padding-left:15px;color:#8BB7F1;cursor:pointer;' onclick=getMailAttachFile('"+fileKey+"');>打开</span>" +
            			"<span style='padding-left:15px;color:#8BB7F1;cursor:pointer;' onclick=deleteMailAttachFile('"+fileKey+"');>删除</span>" +
            		  "</div>";
            Ext.DomHelper.insertHtml('beforeEnd',Ext.getDom('mail_send_attach_panel'),file);
            var formPanel = Ext.getCmp(EXT_MAIL_ID_OUTBOUND_FROMPANEL);
            formPanel.doLayout();
		}
	}
});