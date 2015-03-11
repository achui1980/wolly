Ext.namespace('Ext.mail');

Ext.mail.sendInfoFn = function(result,sendTypeText){
	var msg = '';
	var fieldName = '';
	switch(result){
		case MAIL_SEND_NO_ERROR_STATUS:
			msg = sendTypeText+'成功!';
			break;
		case MAIL_SEND_ERROR_STATUS:
			msg = sendTypeText+'失败!';
			break;
		case MAIL_SEND_CHECK_STATUS:
			msg = '邮件待审核!';
			sendTypeText = '存入待审核';
			break;
	}
	Ext.Msg.show({
		buttons:Ext.Msg.OK,
		icon:result==MAIL_SEND_ERROR_STATUS?Ext.Msg.ERROR:Ext.Msg.INFO,
		title:sendTypeText+(result==MAIL_SEND_ERROR_STATUS?'失败':'成功'),
		msg:msg,
		fn:function(btn){
		}
	});
}
/**
 * 信箱的详情显示
 * @param {} hanType 操作动作 rowIndex,prev,next
 * @param {} mailGrid
 * @param {} mailDefault
 */
function navHandler(hanType,mailGrid,mailDefault){
	try{
		mailDefault.mailLoadMask.show();
		var selModel = mailGrid.getSelectionModel();
		if(hanType=='prev'){ // 上一页
			selModel.selectPrevious();
		}else if(hanType=='next'){ // 下一页
			selModel.selectNext();
		}
		// 翻页后
		if(selModel.hasPrevious()) // 不是第一条，显示上一页
			mailDefault.prevBtn.enable();
		else // 不显示上一页
			mailDefault.prevBtn.disable();
		if(selModel.hasNext()) // 不是最后一条，显示下一页
			mailDefault.nextBtn.enable();
		else  // 不显示下一页
			mailDefault.nextBtn.disable();
			
		if(mailGrid.mailType == 'main'){
			Ext.mail.ToolbarControl();
			// 历史
			Ext.mail.ComeAndGoMailControl();
		}	
		
		var record = getSelDatas(mailGrid)[0]; // 选中的数据
		var data = record.data;
		var status = data.mailStatus;
		// 根据邮件状态是否显示修改按钮
		if(status==MAIL_LOCAL_STATUS_CHECKNOTGO||status==MAIL_LOCAL_STATUS_PARTINGCHECKNOTGO){
			mailDefault.modifyBtn.show();
		}else{
			mailDefault.modifyBtn.hide();	
		}
		if(!record.readData){ // 数据详情未读取过
			mailLocalService.readMailAllInfo(record.id,function(mailData){
				record.readData = mailData; // 保存读取的详情 0：新邮件，1：新邮件、回执，2：已读，3：指派未读，4：指派已读，5：错误邮件 ，6:指派回执未读
				if(data.mailStatus==0||data.mailStatus==1||data.mailStatus==3)
					record.set('mailStatus',data.mailStatus<=2?2:4);
				if("ARG".indexOf(mailGrid.flag)==-1)// 不为收件箱类型
					mailData.isNotification = false;
				else
					mailData.isNotification = data.isNotification
				record.commit();
				Ext.mail.detailPanelInsertData(mailDefault,mailData);
				mailDefault.mailLoadMask.hide();
			});
			return;
		}else{ // 数据详情已读取过
			Ext.mail.detailPanelInsertData(mailDefault,record.readData);
			mailDefault.mailLoadMask.hide();
		}
	}catch(e){}
};
Ext.mail.detailPanelInsertData = function(mailDefault,data){
	Ext.each(data.attachs,function(attach){
		if(attach.size<1024){
			attach.sizeShow = attach.size+'字节';
		}else if(attach.size<(1024*1024)){
			attach.sizeShow = (attach.size/1024).toFixed(2) + 'KB';
		}else{
			attach.sizeShow = (attach.size/1024/1024).toFixed(2) + 'MB';				
		}	
		attach.encodeUrl = Ext.urlEncode({path:attach.url,name:attach.name})
	});
	Ext.getDom(mailDefault.iframeId).height = 0;
	var form = mailDefault.formPanel;
	var bodyPanel = mailDefault.bodyPanel;
	var attachPanel = mailDefault.attachPanel;
	var dataBody = data.body.replace(/<img/ig,'<img onload=javascript:dyniframesize("'+mailDefault.iframeId+'")');
	frames[mailDefault.iframeId].document.write(dataBody);
	frames[mailDefault.iframeId].document.write('<script type="text/javascript">function dyniframesize(down) {parent.dyniframesize(down);}</script>');
//	frames[mailDefault.iframeId].document.write('<script type="text/javascript">var timeCount=0;function dyniframesize(down) {if(timeCount>3)return;timeCount++;parent.dyniframesize(down);/*setTimeout(\'dyniframesize("'+mailDefault.iframeId+'")\',200);*/}</script>');
	frames[mailDefault.iframeId].document.close(); 
	dyniframesize(mailDefault.iframeId);
	
    if(Ext.isEmpty(data.attachs))
    	attachPanel.hide();
    else{
    	attachPanel.setTitle('附件 ('+data.attachs.length+'个)');
    	attachPanel.show();
    }
    tpl = new Ext.XTemplate(
    	'<div style="padding:10px;font-size:10px;">',
    	data.attachs&&data.attachs.length>1?'<p><B>附件</B>&nbsp;&nbsp;&nbsp;&nbsp;[<a href="downLoadMailAllFile.down?mailId='+data.id+'">打包下载</a>]<div style="font-size: 0px;line-height: 0;border-top: 1px solid;border-color: #9CB8CC;"></div></p>':'',
	    '<tpl for="attachs">',     // interrogate the kids property within the data
	        '<p>{name}<span style="color:#888888;">（{sizeShow}）</span><br />',
	        '<a href="downLoadMailFile.down?{encodeUrl}" target="_blank">打开</a>&nbsp;|&nbsp;',
	        '<a href="downLoadMailFile.down?{encodeUrl}&t=down" >下载</a></p>',
	    '</tpl>',
	    '</div>'
	);
	function personShow(person){
		var showStr = '';
		if(person){
			if(Ext.isEmpty(person.name))
				showStr = person.emailUrl+";&nbsp;";
			else
				showStr = person.name+'&nbsp;&lt;<span style="font-size:14px;text-decoration:underline;color:blue">'+person.emailUrl+'</span>&gt;;';
			showStr = "<span style='cursor:pointer;' onclick='javascript:personClick("+Ext.encode(person)+")'>"+showStr+"</span>";
		}
		return showStr;
	}
	tpl.overwrite(attachPanel.body, data); 
	form.removeAll();
	var senderShow = personShow(data.sender);
	var toShow = '';
	Ext.each(data.to,function(person){
		toShow += personShow(person);
	});
	if(toShow=='')
		toShow = '<span style="color:#888888">(无联系人)</span>';
	var ccShow = '';
	Ext.each(data.cc,function(person){
		ccShow += personShow(person);
	});
	var sendTime;
	if(data.sendTime){
		var date = data.sendTime;
		if(date.time)
			sendTime = new Date(date.year+1900,date.month,date.date,date.hours,date.minutes,date.seconds);
		else
			sendTime = date;
	}
	if(mailDefault.inboxType!=4){
		var rej = mailDefault.getForm().findField('rejuvenation');
		rej.setValue('');
//			rej.emptyText = '快速回复给：'+data.sender.name || data.sender.emailUrl;
	}
	form.add([{
		xtype:"label",
		ref:'subject',
		data:data.subject,
		html:'<span style="font-weight: bold;font-size:18px;">'+data.subject+'</span>',
		style:'position: relative; top: 3px;',
		fieldLabel:'<span style="font-weight: bold;font-size:18px;">主题</span>'
	}
	,{
		xtype:"label",
		ref:'sender',
		data:data.sender,
		html:senderShow,
		style:'position: relative; top: 3px;',
		fieldLabel:"发件人"
	}
	,{
		xtype:"label",
		ref:'sendTime',
		data:data.sendTime,
		html:sendTime?sendTime.format('Y年m月d日 H:i(星期D)'):'',
		style:'position: relative; top: 3px;',
		fieldLabel:"时间"
	}
	,
	{
		xtype:"label",
		ref:'to',
		data:data.to,
		style:'position: relative; top: 3px;',
		html:toShow,
		fieldLabel:data.mailStatus<=-4&&data.mailStatus>=-6||data.mailStatus<=-10?"群发给":"收件人"
	}
	,
	ccShow?{
		xtype:"label",
		ref:'cc',
		data:data.cc,
		style:'position: relative; top: 3px;',
		html:ccShow,
		fieldLabel:"抄送人"
	}:{
		hidden:true
	},mailDefault.inboxType==7&&!Ext.isEmpty(data.errMessage)?{
		xtype:'label',
		style:'position: relative; top: 3px;',
		fieldLabel:'批注',
		html:'<span style="color:red">'+data.errMessage+'</span>'
	}:{
		hidden:true	
	}
	]);
	if(!Ext.isEmpty(data.attachs)){
		var attachHtml = '<B>'+data.attachs.length+'</B>&nbsp;个&nbsp;';
		attachHtml += '<span style="color:#888888">('
		for(var i=0;i<data.attachs.length&&i<3;i++){
			var attach = data.attachs[i];
			attachHtml += '<a href="downLoadMailFile.down?'+attach.encodeUrl+'" style="font-weight:bold;color:#999999;text-decoration:none;" target="_blank">'+attach.name+'</a>&nbsp;;&nbsp;';
		}
		if(data.attachs.length>3)
			attachHtml += '等...';
		attachHtml += ')</span>&nbsp;';
		attachHtml += "&nbsp;&nbsp;<span style='color:#8BB7F1;cursor:pointer;' onclick=scrollToMember();>";
		if(data.attachs.length>3)
			attachHtml += '查看全部附件';
		else
			attachHtml += '查看附件';
		attachHtml += "</span>";
		if(data.attachs.length>=2){
			attachHtml += '&nbsp;&nbsp;[<a href="downLoadMailAllFile.down?mailId='+data.id+'">打包下载</a>]';
		}
		form.add({
			xtype:"label",
			html:attachHtml,
			style:'position: relative; top: 3px;',
			fieldLabel:"附件"
		});
	}
	form.doLayout();
	if(data.isNotification){
		Ext.Msg.show({
		   title:'邮件回执提示',
		   msg:'发件人希望得到您的回执，是否发送？',
		   buttons:Ext.Msg.YESNO,
		   icon:Ext.Msg.QUESTION,
		   fn:function(btnId){
		   		var body = '这是邮件收条, '+(sendTime?sendTime.format('Y年m月d日 H:i(星期D)'):'')
		   			+' 主题为 '+data.subject+' 的信件已被接收,此收条只表明收件人的计算机上曾显示过此邮件';
		   		if(btnId == 'yes'){
		   			var mail = {
						to:[data.sender],
						subject:'已读:'+data.subject,
						body:body
					}
					mailSendService.addSendout(mail,null,function(result){
						Ext.mail.sendInfoFn(result,'回执');
					});	 
		   		}
		   		data.isNotification = false;
		   }
		});
	}
}
function personClick(person){
	var menu = Ext.getCmp(EXT_MAIL_PERSON_MENU);
	menu.person = person;
	menu.addCustContact.hide();
	menu.addFactoryContact.hide();
	menu.editCustContact.hide();
	menu.editFactoryContact.hide();
	cotCustContactService.findExistByEMail(person.emailUrl,function(id){
		if(id){
			menu.editCustContact.show();
			menu.personId = id;
			menu.showAt(Ext.EventObject.getXY());
		}else{
			cotContactService.findExistByEMail(person.emailUrl,function(id){
				if(id){
					menu.editFactoryContact.show();
					menu.personId = id;
				}else{
					menu.addCustContact.show();
					menu.addFactoryContact.show();
				}
				menu.showAt(Ext.EventObject.getXY());
			});
		}
	});
}
function scrollToMember(){
	var mailDefault = EXT_VIEWPORT.mailDefault;
	if(!mailDefault||mailDefault.hidden||mailDefault.collapsed){
		mailDefault = EXT_VIEWPORT.dbMailDefault;
	}
	
	var allInfoPanel = mailDefault.allInfoPanel;
	var attachPanel = mailDefault.attachPanel;
    var top = (attachPanel.el.getOffsetsTo(allInfoPanel.body)[1]) + allInfoPanel.body.dom.scrollTop;
    allInfoPanel.body.scrollTo('top', top-25, {duration:0.50, callback: hlMember.createDelegate(attachPanel)});
}
function hlMember(){
    if(this.el)
   		this.el.highlight('#cadaf9');
}


// 获得选中的数据
function getSelDatas(grid){
	return grid.getSelectionModel().getSelections();// 选中的数据	
}
// 重新加载数据
function reloadSelData(grid){
	grid.getStore().reload(); // 重新加载数据	
}
// 隐藏面板
function hideSelDefault(c){
	c.mailDefault.hide(); // 隐藏面板
    c.doLayout();	
}
function showSelDefault(c){
	c.mailDefault.show(); // 隐藏面板
    c.doLayout();	
}

function showprint(){
	var mailDefault = this;
	var form = mailDefault.formPanel; // 面板下的放邮件头信息的Panel
	var bodyPanel = mailDefault.bodyPanel; // 面板下放邮件内容的Panel
	//alert(form.el.dom.innerHTML);
	$('printcontent').value = form.el.dom.innerHTML+"<br/>"
					+"<div style='margin-left:15px'>"
					+frames[mailDefault.iframeId].document.body.innerHTML;
					+"</div>"
	$('printForm').action = "cotmail.do?method=printcontent&rnd"+Math.random();
	$('printForm').method = "POST";
	$('printForm').target="_blank";
	$('printForm').submit();
}

function dyniframesize(down) { 
	var pTar = null; 
	if (document.getElementById){ 
		pTar = document.getElementById(down); 
	} 
	else{ 
		eval('pTar = ' + down + ';'); 
	} 
	if (pTar && !window.opera){ 
		//begin resizing iframe 
		pTar.style.display="block" ;
		if (pTar.contentDocument && pTar.contentDocument.body.offsetHeight){ 
			//ns6 syntax 
			pTar.height = pTar.contentDocument.body.offsetHeight +20; 
//			pTar.width = pTar.contentDocument.body.scrollWidth+20; 
		} 
		else if (pTar.Document && pTar.Document.body.scrollHeight){ 
			//ie5+ syntax 
			pTar.height = pTar.Document.body.scrollHeight; 
//			pTar.width = pTar.Document.body.scrollWidth; 
		} 
	} 
} 
