Ext.namespace('Ext.mail');
Ext.mail.sendPersonTo = function (field){
	var tos = [];
	if(field.hidden)
		return [];
	Ext.each(field.mailPersons,function(mailPersons){
		tos.push(mailPersons.toObj());
	});
	var lastValue = field.urlRange.lastValue;
	var mailPerson = new Ext.mail.MailPerson();
	if(mailPerson.setUrl(lastValue)){
		tos.push(mailPerson);
	}
	return tos;
}
/**
 * 发送或存草稿事件
 * @param {} sendType 1为发送，2为存草稿
 */
Ext.mail.sendHandler = function(sendType){
	var formPanel = this; 
	var form = formPanel.getForm();
	var to = [],cc = [],bcc = [];
	var mailStatus = MAIL_LOCAL_STATUS_NOSEND; // 用于标记邮件状态
	if(!formPanel.partingField.hidden){
		to = Ext.mail.sendPersonTo(form.findField('parting'));
		mailStatus = MAIL_LOCAL_STATUS_NOPARTINGSEND;
	}else{
		to = Ext.mail.sendPersonTo(form.findField('to')); 
		if(!formPanel.ccField.hidden)
			cc = Ext.mail.sendPersonTo(form.findField('cc')); 
		if(!formPanel.bccField.hidden)
			bcc = Ext.mail.sendPersonTo(form.findField('bcc')); 
	}
	if(sendType==1&&to.length ==0){
		Ext.example.msg('系统提示', '请正确填写邮件格式',formPanel.getEl());
		return ;
	}
	var id = form.findField('id').getValue(); 
	var custId = form.findField('custId').getValue(); 
	var sendTypeText = sendType==1?'发送':'存草稿';
	var sendStatus = Ext.getDom('sendStatus').value;
	var mailTag = '';
	if(sendStatus==MAIL_SEND_TYPE_STATUS_REPLAY||sendStatus==MAIL_SEND_TYPE_STATUS_REPLAYALL){
		mailTag = MAIL_TAG_REPALY;
	}else if(sendStatus==MAIL_SEND_TYPE_STATUS_FORWARD){
		mailTag = MAIL_TAG_FORWARD;
	}
		
    var loadMask = new Ext.LoadMask(formPanel.getEl(),{msg:sendTypeText+'中。。。'});
	var sendStatusV = Ext.getDom('sendStatus');
	loadMask.show();
	var mail = {
		id:id,
		msgId:id,
		sender:{
			name:Ext.getDom('sendEmpName').value,
			emailUrl:Ext.getDom('sendEmpEmail').value
		},
		custId:custId,
		mailTag:mailTag,
		mailStatus:mailStatus,
		mailType:sendStatusV.value=='null'?0:sendStatusV.value,
		to:to,
		cc:cc,
		bcc:bcc,
		isNotification:form.findField('isNotification').getValue(),
		subject:form.findField('subject').getValue(),
		body:form.findField('body').getValue()
	}
	formPanel.sending = true;
	var sendout = sendType == 1?mailSendService.addSendout:mailSendService.addDraft;
	sendout(mail,formPanel.random,function (result){
		loadMask.hide();
		//发送成功后，清空面板
		if(result!=MAIL_SEND_ERROR_STATUS){
			setTimeout(function(){closeandreflashEC(true, EXT_MAIL_ID_OUTBOUND_FROMPANEL, false);},1000);											
			if(self.opener!=null){
				var viewport = self.opener.Ext.getCmp(EXT_MAIL_ID_VIEWPORT);
				if(viewport){
					var card = viewport.mailCard;
					var grid = viewport.mailGrid; // 被击活的grid
					if (typeof(grid)!='undefined'&&grid.store) {
						card.layout.setActiveItem(0);
						grid.getStore().reload();
					}
				}
			}
		}
		Ext.mail.sendInfoFn(result,sendTypeText);
	});	 			
}
/**
 * 邮件发送面板
 * @class Ext.mail.OutBoundPanel
 * @extends Ext.FormPanel
 */
Ext.mail.OutBoundPanel=Ext.extend(Ext.FormPanel ,{
	title:"",
	bodyBorder:false,
	border:false,
	initComponent: function(){
		var formPanel = this;
		this.random=new Date().getTime();
		function partinFn(btn){
			var personPanel = formPanel.personPanel;
			var ccBtn = formPanel.ccBtn;
			var bccBtn = formPanel.bccBtn;
			var partingField = formPanel.partingField;
			if(partingField.hidden){
				btn.setText('取消群发');
		        personPanel.hide();   
		        ccBtn.disable();
		        bccBtn.disable();
		       	partingField.show();   
			}else{
				btn.setText('使用群发');
		       	personPanel.show();   
		        ccBtn.enable();
		        bccBtn.enable();
		        partingField.hide();   
			}
			formPanel.doLayout();
		}
		this.tbar=[{
				text:"发送",
				iconCls:'email_go',
				handler:Ext.mail.sendHandler.createDelegate(this,[1])
			},"-",{
				text:"存草稿",
				scope:this,
				handler:Ext.mail.sendHandler.createDelegate(this,[2])
			},'-',{
				text:'添加抄送',
				ref:'../ccBtn',
				handler:function(btn){
					var ccField = formPanel.ccField;
					if(ccField.hidden){
						btn.setText('删除抄送');
						ccField.show();
					}else{
						btn.setText('添加抄送');
						ccField.hide();
					}
					formPanel.doLayout();
				}
			},'-',{
				text:'添加密送',
				ref:'../bccBtn',
				handler:function(btn){
					var bccField = formPanel.bccField;
					if(bccField.hidden){
						btn.setText('删除密送');
				       	bccField.show();   
					}else{
						btn.setText('添加密送');
				        bccField.hide();   
					}
					formPanel.doLayout();
				}
			},'-',{
				text:'使用群发',
				ref:'../partingBtn',
				handler:function(btn){
					var personPanel = formPanel.personPanel;
					var ccBtn = formPanel.ccBtn;
					var bccBtn = formPanel.bccBtn;
					var partingField = formPanel.partingField;
					if(partingField.hidden){
						btn.setText('取消群发');
				        personPanel.hide();   
				        ccBtn.disable();
				        bccBtn.disable();
				       	partingField.show();   
					}else{
						btn.setText('使用群发');
				       	personPanel.show();   
				        ccBtn.enable();
				        bccBtn.enable();
				        partingField.hide();   
					}
					formPanel.doLayout();
				}
			},'-',{
				text:'设置签名',
				ref:'../signature',
				handler:function(btn){
					var empsSign=Ext.getDom('empsSign').value;
					var empsId=Ext.getDom('empsId').value;
					window.close();
		            openWindowBase(580,800,'cotmailsend.do?method=querySignature');        		
					
	        	}

			}
		]
		this.items=[
			{
				title:"",
				labelWidth:60,
				labelAlign:"right",
				layout:"form",
				frame:true,
				margins:"",
				enctype : 'multipart/form-data', //上传类型
	            fileUpload:true,
				items:[
					{
						xtype:'hidden',
						name:'id'
					},
					{
						xtype:'hidden',
						name:'custId'
					},
					{
						xtype:"textfield",
						fieldLabel:"发件人",
						readOnly:true,
						value:"",
						anchor:"99%",
						allowBlank:false,
						name:"sender"
					},
					{
						ref:'../personPanel',
						items:[{
							xtype:'personfield',
							textLabel:'收件人',
							name:'to',
							id :'toField',
							ref:'../../toField',
							listeners:{
							  'fieldchange':function(field,mailPersons,newValue){
							  	//alert('chufa');
								var form = formPanel.getForm();
								//隐藏值为空，不执行下面代码
								if(!Ext.isEmpty($('fckField').value)){
									
									if(Ext.isEmpty(mailPersons)){
										//获取HtmlEditor内容
										var fck=form.findField('body').getValue();
										var start =fck.indexOf("<span tage=");
										var tmp=fck.substring(start);
										var end =start + tmp.indexOf("</span>");
										var str;
										if(tmp.indexOf("display:none")==-1){
											str=fck.substring(start+51,end);
										}else{
											str=fck.substring(start+49,end);
										}
										//组件上没值
										if(Ext.isEmpty(newValue)){
											tmp=tmp.replace(str,"&nbsp;");
											var indexStr=fck.substring(0,start);
											if(tmp.indexOf("display:none")>0){
												tmp=tmp.replace("display:none","display:inline");
											}
											var value=indexStr+tmp;
											form.findField('body').setValue(value);
										}else{
											if(newValue.indexOf('@')==-1){
												tmp=tmp.replace(str,newValue);
												var indexStr=fck.substring(0,start);
												if(tmp.indexOf("display:none")>0){
													tmp=tmp.replace("display:none","display:inline");
												}
												var value=indexStr+tmp;
												form.findField('body').setValue(value);
											}else{
												var i=newValue.indexOf('@');
												var name=newValue.substring(0,i);
												var indexStr=fck.substring(0,start);
												tmp=tmp.replace(str,name);
												if(tmp.indexOf("display:none")>0){
													tmp=tmp.replace("display:none","display:inline");
												}
												var value=indexStr+tmp;
												form.findField('body').setValue(value);
											}
										}
									}else{
										//联系人名称
										var objName=mailPersons[0].getName();
										//邮箱地址
										var objUrl=mailPersons[0].getUrl();
										//获取HtmlEditor内容
										var fck=form.findField('body').getValue();
											//如果没有联系人名称
										if(Ext.isEmpty(objName)&&!Ext.isEmpty(objUrl)){
											var i=objUrl.indexOf('@');
											var name=objUrl.substring(0,i);
											var str;
											var value;
											var start =fck.indexOf("<span tage=");
											var tmp=fck.substring(start);
											var end =start + tmp.indexOf("</span>");
											if(tmp.indexOf("display:none")>0){
												str=fck.substring(start+49,end);
												tmp=tmp.replace("display:none","display:inline");
												var indexStr=fck.substring(0,start);
												tmp=tmp.replace(str,name);
												value=indexStr+tmp;
											}else{
												str=fck.substring(start+51,end);
												var indexStr=fck.substring(0,start);
												tmp=tmp.replace(str,name);
												value=indexStr+tmp;
											}
										    form.findField('body').setValue(value);
										}else{
											var str;
											var value;
											var start =fck.indexOf("<span tage=");
											var tmp=fck.substring(start);
											var end =start+tmp.indexOf("</span>");
											if(tmp.indexOf("display:none")>0){
												str=fck.substring(start+49,end);
												tmp=tmp.replace("display:none","display:inline");
												var indexStr=fck.substring(0,start);
												tmp=tmp.replace(str,objName);
												value=indexStr+tmp;
											}else{
												str=fck.substring(start+51,end);
												var indexStr=fck.substring(0,start);
												tmp=tmp.replace(str,objName);
												value=indexStr+tmp;
											}
										    form.findField('body').setValue(value);
										}
									}
								}
								}
							}
						},{
							xtype:'personfield',
							textLabel:'抄送',
							name:'cc',
							ref:'../../ccField',
							hidden:true
						},{
							xtype:'personfield',
							textLabel:'密送',
							name:'bcc',
							ref:'../../bccField',
							hidden:true
						}]
					},
					{
						xtype:'personfield',
						textLabel:'群发',
						name:'parting',
						ref:'../partingField',
						hidden:true,
						listeners:{
							  'fieldchange':function(field,mailPersons,newValue){
								var form = formPanel.getForm();
								if(!Ext.isEmpty($('fckField').value)){
									if(Ext.isEmpty(mailPersons)){
										var fck=form.findField('body').getValue();
										var start =fck.indexOf("<span tage=");
										var tmp=fck.substring(start);
										var end =start + tmp.indexOf("</span>");
										if(Ext.isEmpty(newValue)){
											var str;
											if(tmp.indexOf("display:none")>0){
											    str=fck.substring(start+49,end);
												tmp=tmp.replace("display:none","display:inline");
											}else{
												str=fck.substring(start+51,end);
											}
											tmp=tmp.replace(str,"&nbsp;");
											var indexStr =fck.substring(0,start);
											var value=indexStr+tmp;
											form.findField('body').setValue(value);
										}else{
											tmp=tmp.replace(str,newValue);
											if(tmp.indexOf("display:none")>0){
												tmp=tmp.replace("display:none","display:inline");
											}
											var indexStr =fck.substring(0,start);
											var value=indexStr+tmp;
											form.findField('body').setValue(value);
										}
									}else{
										var fck=form.findField('body').getValue();
										var str;
										var value;
										var start =fck.indexOf("<span tage=");
										var tmp=fck.substring(start);
										var end =start + tmp.indexOf("</span>");
										if(tmp.indexOf("display:none")>0){
											str=fck.substring(start+49,end);
											tmp =tmp.replace(str,"xxx");
											tmp=tmp.replace("display:none","display:inline");
											var indexStr =fck.substring(0,start);
											value=indexStr+tmp;
										}else{
											str=fck.substring(start+51,end);
											tmp =tmp.replace(str,"xxx");
											var indexStr =fck.substring(0,start);
											value=indexStr+tmp;
										}
										form.findField('body').setValue(value);
									}
								}
								}
							}
					},
					{
						xtype:"textfield",
						fieldLabel:"主题",
						anchor:"99%",
						name:"subject"
					},
					{ 
						xtype:"panel",
						title:"",
						html:"<div onclick='mailShowUploadPanel();' style='padding:5px 0 5px 70px;cursor:pointer;color:#8BB7F1'>添加附件</div>"	
					},
					{
			        	id:'panel_1',
			        	layout:"form",
			        	style:"padding:3px 0 0 65px",
			        	html:'<div id="mail_send_attach_panel" style="border:1px #8BB7F1 solid;width:98%;display:none;padding:3px;margin-bottom:5px;"></div>'  	
			        },	
					{
//						xtype:'fckeditor',
						xtype:'htmleditor',
						fieldLabel:"正文",
						height:408,
						anchor:"99%",
						name:"body",
						fontFamilies : FontFamilies,//字体设置在extcommon.jsp
						enableSourceEdit:false,//源代码
						defaultFont : '宋体',//默认字体
						listeners:{
							render:function(bodyField){
								var formPanel = Ext.getCmp(EXT_MAIL_ID_OUTBOUND_FROMPANEL);
								var sendName = Ext.getDom('sendEmpName').value; // 登录者邮件地址
								var address = Ext.getDom('sendEmpEmail').value; // 登录者邮件地址
							    formPanel.getForm().findField('sender').setValue(sendName+'<'+address+'>');
							    

								var mailId = Ext.getDom('mailId');
								var excelKey = Ext.getDom('excelKey');
								var sendStatusV = Ext.getDom('sendStatus');
								if(excelKey.value != 'null'|| mailId.value != 'null' ||sendStatusV.value != 'null'){
									
									var sendStatus = sendStatusV.value;
									
									var getMailMethod; // 访问方法
									var methodValue = mailId.value;	// 访问值
									if(excelKey.value != 'null'){ // excel
										getMailMethod = mailLocalService.getExcel;
										methodValue = excelKey.value;
									}else if(sendStatus==MAIL_SEND_TYPE_STATUS_REPLAY||sendStatus==MAIL_SEND_TYPE_STATUS_REPLAYALL){ // 回复，回复全部
										getMailMethod = mailLocalService.getSendTypeInfo;
									}else{ // 再次发送，转发，修改，草稿
										getMailMethod = mailLocalService.getSendTypeInfoAndAttach;
									}
									var loadMask = new Ext.LoadMask(formPanel.getEl(),{msg:'数据加载中。。。'});	
									loadMask.show();
									getMailMethod(methodValue,formPanel.random,function(data){
										loadMask.hide();
										if(Ext.isEmpty(data))
											return ;
										var form = formPanel.getForm();
										var sender = data.sender;
//										if(!Ext.isEmpty(sender)&&replayId.value=='null') //当发件人不为空，并且不是回复 TODO:目前以登录者发送
//											form.findField('sender').setValue(sender.emailUrl);
											
										form.findField('isNotification').setValue(data.isNotification); // 是否需要回复
										form.findField('custId').setValue(data.custId); // 客户ID
										// 主题
										var subject = data.subject;
										if(sendStatus!='null'){
											// 回复，转发
											if(sendStatus==MAIL_SEND_TYPE_STATUS_REPLAY||sendStatus==MAIL_SEND_TYPE_STATUS_REPLAYALL){
												subject = 'Re: '+subject;
											}else if(sendStatus == MAIL_SEND_TYPE_STATUS_FORWARD){
												subject = 'Fw: '+subject;
											}
										}
										form.findField('subject').setValue(subject);
//										// ID
//										if(mailId.value == 'null'&&forward.value == 'null'){ // 草稿,再次发送,转发
//										}
										form.findField('id').setValue(data.id);
										
										// 设置内容
										
										if(sendStatus==MAIL_SEND_TYPE_STATUS_DRATF){ // 撰写
											var logo='New';
											mailTemplateService.getNewMailTemplate($('empsId').value,logo,function(rs){
												if(rs){
													$('fckField').value=rs;
													form.findField('body').setValue(rs);
												}
											});
										}else if(sendStatus==MAIL_SEND_TYPE_STATUS_FORWARD){ // 转发
											var logo='Forward';
											mailTemplateService.getMailTemplate($('empsId').value,logo,data,function(rs){
												if(rs){
													if(rs.indexOf('<span tage=')==-1){
														$('fckField').value="";
													}else{
														$('fckField').value=rs;
													}
													form.findField('body').setValue(rs);
													toField.setValue(urlsStr);
												}
											});
										}else if(sendStatus==MAIL_SEND_TYPE_STATUS_REPLAY||sendStatus==MAIL_SEND_TYPE_STATUS_REPLAYALL){ // 回复
											
											var logo='Reply';
											mailTemplateService.getMailTemplate($('empsId').value,logo,data,function(rs){
												if(rs){
													form.findField('body').setValue(rs);
													if(rs.indexOf('<span tage=')==-1){
														$('fckField').value="";
													}else{
														$('fckField').value=rs;
													}
													toField.setValue(urlsStr);
												}
											});
										}else if(sendStatus==MAIL_CONNECT_ERROR_SMTP_HOST_STATUS){//再次发送
											var rs=data.body;
											if(rs.indexOf('<span tage=')==-1){
														$('fckField').value="";
													}else{
														$('fckField').value=rs;
													}
											form.findField('body').setValue(data.body);
													//toField.setValue(urlsStr);
										}else{
											form.findField('body').setValue(data.body);
										}
									
										// 设置接收人，回复只设置发送人，回复全部设置所有人，转发，不设置
										var toField = form.findField('to');
										var status = data.mailStatus;
										if(status==MAIL_LOCAL_STATUS_NOPARTINGSEND||status==MAIL_LOCAL_STATUS_PARTINGCHECKNOTGO){ // 群发
											toField = form.findField('parting');
											partinFn(formPanel.partingBtn);
										}
										var urlsStr = '';
										var tos = [];
										// 回复或转发
										if(sendStatus==MAIL_SEND_TYPE_STATUS_REPLAY||sendStatus==MAIL_SEND_TYPE_STATUS_REPLAYALL||sendStatus == MAIL_SEND_TYPE_STATUS_FORWARD){
											if(sendStatus==MAIL_SEND_TYPE_STATUS_REPLAYALL){ // 回复所有,发送人，接收人，抄送人
												if(sender&&sender.emailUrl!=address)
													tos.push(sender);
												Ext.each(data.to,function(person){
													if(person.emailUrl!=address)
														tos.push(person);
												})
												Ext.each(data.cc,function(person){
													if(person.emailUrl!=address)
														tos.push(person);
												})
											}else if(sendStatus == MAIL_SEND_TYPE_STATUS_FORWARD){	// 转发，不做处理
											}else{ // 回复，设置原邮件发送人为接收人
												tos.push(sender);
											}
										}else{	
											tos = data.to;
										}
										Ext.each(tos,function(person){
											var mailPerson = new Ext.mail.MailPerson();
											mailPerson.setUrl(person.emailUrl);
											mailPerson.setName(person.name);
											toField.mailPersons.push(mailPerson);
											urlsStr += mailPerson.toString()+";";
										})
										toField.urlRange.saveValue(urlsStr);
										toField.setValue(urlsStr);
										//toField.setValue.defer(2000,this,[urlsStr])
										
										// 设置抄送,不为回复，回复全部，转发
										if(!Ext.isEmpty(data.cc)&&sendStatus!=MAIL_SEND_TYPE_STATUS_REPLAY&&sendStatus!=MAIL_SEND_TYPE_STATUS_REPLAYALL&&sendStatus != MAIL_SEND_TYPE_STATUS_FORWARD){
											var ccBtn = formPanel.ccBtn;
											var ccField = form.findField('cc');
											urlsStr = '';
											Ext.each(data.cc,function(person){
												var mailPerson = new Ext.mail.MailPerson();
												mailPerson.setUrl(person.emailUrl);
												mailPerson.setName(person.name);
												ccField.mailPersons.push(mailPerson);
												urlsStr += mailPerson.toString()+";";
											})
											ccField.urlRange.saveValue(urlsStr);
											ccField.setValue(urlsStr);
											ccBtn.setText('删除抄送');
									       	formPanel.ccField.show();  
										}
										
										// 设置暗送，不为回复，回复全部，转发
										if(!Ext.isEmpty(data.bcc)&&sendStatus!=MAIL_SEND_TYPE_STATUS_REPLAY&&sendStatus!=MAIL_SEND_TYPE_STATUS_REPLAYALL&&sendStatus != MAIL_SEND_TYPE_STATUS_FORWARD){
											var bccBtn = formPanel.bccBtn;
											var bccField = form.findField('bcc');
											urlsStr = '';
											Ext.each(data.bcc,function(person){
												var mailPerson = new Ext.mail.MailPerson();
												mailPerson.setUrl(person.emailUrl);
												mailPerson.setName(person.name);
												bccField.mailPersons.push(mailPerson);
												urlsStr += mailPerson.toString()+";";
											})
											bccField.urlRange.saveValue(urlsStr);
											bccField.setValue(urlsStr);
											bccBtn.setText('删除密送');
									       	formPanel.bccField.show();   
										}
										
										// 设置附件
										var attachPanel = Ext.getDom("mail_send_attach_panel");  // 附件面板
										attachPanel.style.display="block"; // 设置为显示
										if(Ext.isEmpty(data.attachs))
											attachPanel.style.display="none";
										else{
											attachPanel.attachCount = attachPanel.attachCount || 0;
											attachPanel.nameIndex = attachPanel.nameIndex || 0;
											Ext.each(data.attachs,function(attach){
												attachPanel.attachCount++;
												attachPanel.nameIndex++;
												var sizeShow = '';
												if(attach.size<1024){
													sizeShow = attach.size+'字节';
												}else if(attach.size<(1024*1024)){
													sizeShow = (attach.size/1024).toFixed(2) + 'KB';
												}else{
													sizeShow = (attach.size/1024/1024).toFixed(2) + 'MB';				
												}	
												var fileKey = attach.name.replace(MAIL_SEND_ATTACH_UPLOAD_RG,"")+"*"+attachPanel.nameIndex;
												 var file="<div  style='padding:3px;display:block;' id='"+fileKey+"'>"+attach.name+
							            			"<span style='color:#888888;'>（"+sizeShow+"）</span>" +
							            			"<span style='padding-left:15px;color:#8BB7F1;cursor:pointer;' onclick=getMailAttachFile('"+fileKey+"');>打开</span>" +
							            			"<span style='padding-left:15px;color:#8BB7F1;cursor:pointer;' onclick=deleteMailAttachFile('"+fileKey+"');>删除</span>" +
							            		  "</div>";
									        	Ext.DomHelper.insertHtml('beforeEnd',Ext.getDom('mail_send_attach_panel'),file);
											});
										}
										formPanel.doLayout();
									})
								}else{
									var logo='New';
									mailTemplateService.getNewMailTemplate($('empsId').value,logo,function(rs){
										if(rs){
											formPanel.getForm().findField('body').setValue(rs);
											$('fckField').value=rs;
											//alert(rs);
										}
									});

								}
								
							}
						}
					},{
						layout:'column',
						style:'padding-left:65',
						items:[{
							xtype:'checkbox',
							name:'isNotification',
							boxLabel:'需要回执'
						}]
					}
				]
			}
		]
		Ext.mail.OutBoundPanel.superclass.initComponent.call(this);
	}
});

Ext.reg('mailOuntbound',Ext.mail.OutBoundPanel);