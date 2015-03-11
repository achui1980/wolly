Ext.onReady(function(){
	   
	  var root =new Ext.tree.TreeNode();
	
	  //新建邮件信纸
	  var newNode =new Ext.tree.TreeNode({
	  		text:'新邮件信纸'
	  });
	  var currentHtmlNode =new Ext.tree.TreeNode({
	  		text:'当前模版'
	  });
	  var txtNewHtml =new Ext.tree.TreeNode({
	  		text:'纯文本'
	  });
//	  var newHtmlNode =new Ext.tree.TreeNode({
//	  		text:'design'
//	  });
//	  var newClearnHtml =new Ext.tree.TreeNode({
//	  		text:'透明胶'
//	  });
//	  var newnoteHtml =new Ext.tree.TreeNode({
//	  		text:'记事本'
//	  });
//	   var newrolleHtml =new Ext.tree.TreeNode({
//	  		text:'打孔纸'
//	  });
//	  var newleafsHtml =new Ext.tree.TreeNode({
//	  		text:'枫叶'
//	  });
//	  var newpanelHtml =new Ext.tree.TreeNode({
//	  		text:'钢笔'
//	  });
//	  var newbirthHtml =new Ext.tree.TreeNode({
//	  		text:'生日蛋糕'
//	  });
//	  var newseaHtml =new Ext.tree.TreeNode({
//	  		text:'悠闲海'
//	  });
	  root.appendChild(newNode);
	  newNode.appendChild(currentHtmlNode);
	  newNode.appendChild(txtNewHtml);
//	  newNode.appendChild(newHtmlNode);
//	  newNode.appendChild(newClearnHtml);
//	  newNode.appendChild(newnoteHtml);
//	  newNode.appendChild(newrolleHtml);
//	  newNode.appendChild(newleafsHtml);
//	  newNode.appendChild(newpanelHtml);
//	  newNode.appendChild(newbirthHtml);
//	  newNode.appendChild(newseaHtml);
	  //回复邮件信纸
	   var replyNode =new Ext.tree.TreeNode({
	  		text:'回复邮件信纸'
	  });
	  var currentReplyHtml =new Ext.tree.TreeNode({
	  		text:'当前模版'
	  });
	  var txtReplyHtml =new Ext.tree.TreeNode({
	  		text:'纯文本'
	  });
//	  var replyHtmlNode =new Ext.tree.TreeNode({
//	  		text:'sky'
//	  });
	  root.appendChild(replyNode);
	  replyNode.appendChild(currentReplyHtml);
	  replyNode.appendChild(txtReplyHtml);
//	  replyNode.appendChild(replyHtmlNode);
	  
	  //转发邮件信纸
	   var forwardNode =new Ext.tree.TreeNode({
	  		text:'转发邮件信纸'
	  });
	  var currentForwardNode =new Ext.tree.TreeNode({
	  		text:'当前模版'
	  });
	  var txtForwardNode =new Ext.tree.TreeNode({
	  		text:'纯文本'
	  });
	   root.appendChild(forwardNode);
	   forwardNode.appendChild(currentForwardNode);
	   forwardNode.appendChild(txtForwardNode);
	  
	  var menu =new Ext.tree.TreePanel({
	   		border : false,
	   		root : root,
	   		rootVisible :false,
	   		listeners : {
	   			'click':function(node,e){
	   				//var mainPanel = Ext.getCmp('mainContent');
	   				if(node.text=='新邮件信纸'||node.text=='回复邮件信纸'|| node.text=='转发邮件信纸'){
	   					//mainPanel.body.dom.innerHTML ='';
	   					
	   				}else{
	   					//读取新邮件信纸模版
	   					if(node.parentNode.text=='新邮件信纸'){
	   						gridStore.removeAll();
   							var date = new gridStore.recordType({
									name : '$date',
									column:'当前时间'
							});
							var empsSign = new gridStore.recordType({
									name : '$empsSign',
									column:'签名'
							});
							var toNewName= new gridStore.recordType({
									name : '$sendNewMailName',
									column:'收件人'
							});
							gridStore.add(date);
							gridStore.add(empsSign);
							gridStore.add(toNewName);
	   						$('nodeText').value=node.parentNode.text;
	   						if(node.text=='当前模版'){
	   							Ext.getCmp('defaltBt').disable ();
	   							//获取当前模版
	   							mailTemplateService.getCurrentMailTemplate($('empsId').value,$(nodeText).value,function(rs){
	   								if(rs){
	   									mainPanel.getForm().findField('htmleditorBody').setValue(rs);
	   								}else{
	   									mainPanel.getForm().findField('htmleditorBody').setValue('新邮件没有当前模版');
	   								}
	   							})
	   						}else{
	   							Ext.getCmp('defaltBt').enable();
	   							var logo='New';
			   					mailTemplateService.getHtmFile(node.text,logo,function(rs){
			   						if(rs){
			   							mainPanel.getForm().findField('htmleditorBody').setValue(rs);
			   						}
			   					});
	   						}
	   					}
	   					//读取回复邮件信纸模版
	   					if(node.parentNode.text=='回复邮件信纸'){
	   						    gridStore.removeAll();
	   							var date = new gridStore.recordType({
										name : '$date',
										column:'当前时间'
								});
								var empsSign = new gridStore.recordType({
										name : '$empsSign',
										column:'签名'
								});
	   							var sendName = new gridStore.recordType({
										name : '$sendName',
										column:'发件人'
								});
								var sendTime = new gridStore.recordType({
										name : '$sendTime',
										column:'发送时间'
								});
								var toName= new gridStore.recordType({
										name: '$toName',
										column:'原邮件的收件人'
								});
								var ccName= new gridStore.recordType({
										name: '$ccName',
										column:'抄送人'
								});
								var subject= new gridStore.recordType({
										name: '$subject',
										column:'主题'
								});
								var body= new gridStore.recordType({
										name: '$body',
										column:'邮件内容'
								});
								var toNewName= new gridStore.recordType({
									name : '$sendNewMailName',
									column:'收件人'
						    	});
								gridStore.add(date);
								gridStore.add(empsSign);
								gridStore.add(sendName);
								gridStore.add(sendTime);
								gridStore.add(toName);
								gridStore.add(ccName);
								gridStore.add(subject);
								gridStore.add(body);
								gridStore.add(toNewName);
	   						$('nodeText').value=node.parentNode.text;
	   						if(node.text=='当前模版'){
	   							Ext.getCmp('defaltBt').disable ();
	   							//获取当前模版
	   							mailTemplateService.getCurrentMailTemplate($('empsId').value,$(nodeText).value,function(rs){
	   								if(rs){
	   									mainPanel.getForm().findField('htmleditorBody').setValue(rs);
	   								}else{
	   									mainPanel.getForm().findField('htmleditorBody').setValue('回复邮件没有当前模版');
	   								}
	   							})
	   						}else{
	   							Ext.getCmp('defaltBt').enable();
	   							var logo='Reply';
			   					mailTemplateService.getHtmFile(node.text,logo,function(rs){
			   						if(rs){
			   							mainPanel.getForm().findField('htmleditorBody').setValue(rs);
			   						}
			   					});
	   						}
	   					}
	   					//读取转发邮件信纸模版
	   					if(node.parentNode.text=='转发邮件信纸'){
	   						     gridStore.removeAll();
	   							var date = new gridStore.recordType({
										name : '$date',
										column:'当前时间'
								});
								var empsSign = new gridStore.recordType({
										name : '$empsSign',
										column:'签名'
								});
	   							var sendName = new gridStore.recordType({
										name : '$sendName',
										column:'发件人'
								});
								var sendTime = new gridStore.recordType({
										name : '$sendTime',
										column:'发送时间'
								});
								var toName= new gridStore.recordType({
										name: '$toName',
										column:'原邮件的收件人'
								});
								var ccName= new gridStore.recordType({
										name: '$ccName',
										column:'抄送人'
								});
								var subject= new gridStore.recordType({
										name: '$subject',
										column:'主题'
								});
								var body= new gridStore.recordType({
										name: '$body',
										column:'邮件内容'
								});
								var toNewName= new gridStore.recordType({
									name : '$sendNewMailName',
									column:'收件人'
						    	});
								gridStore.add(date);
								gridStore.add(empsSign);
								gridStore.add(sendName);
								gridStore.add(sendTime);
								gridStore.add(toName);
								gridStore.add(ccName);
								gridStore.add(subject);
								gridStore.add(body);
								gridStore.add(toNewName);
	   						$('nodeText').value=node.parentNode.text;
	   						if(node.text=='当前模版'){
	   							Ext.getCmp('defaltBt').disable ();
	   							//获取当前模版
	   							mailTemplateService.getCurrentMailTemplate($('empsId').value,$(nodeText).value,function(rs){
	   								if(rs){
	   									mainPanel.getForm().findField('htmleditorBody').setValue(rs);
	   								}else{
	   									mainPanel.getForm().findField('htmleditorBody').setValue('转发邮件没有当前模版');
	   								}
	   							})
	   						}else{
	   							Ext.getCmp('defaltBt').enable();
	   							var logo='Forward';
			   					mailTemplateService.getHtmFile(node.text,logo,function(rs){
			   						if(rs){
			   							mainPanel.getForm().findField('htmleditorBody').setValue(rs);
			   						}
			   					});
	   						}
	   					}
	   				}
	   			}
	   		}
	   });
	   
	   var treePanel = new Ext.Panel({
			region:'west',
			collapsible :true,
			margins:'5 5 0 5',
			title:'模版',
			width : 200,
			items: [menu]
		});
		var mainPanel =new Ext.form.FormPanel({
			region: 'center',
			margins:'5 5 0 0',
			id :'mainContent',
			layout:'fit',
			buttonAlign : 'center',
			items:[{
			    xtype:'htmleditor',
				//xtype:'textarea',
				fieldLabel:"正文",
				height:408,
				anchor:"99%",
				fontFamilies : FontFamilies,//字体设置在extcommon.jsp
				enableSourceEdit:false,//源代码
				defaultFont : '宋体',//默认字体
				name:"name",
				id:"htmleditorBody"
			}],
			buttons:[{
				text:'设为默认',
				id:'defaltBt',
				handler:function(){
					var template=$('htmleditorBody').value;
					var parentNode=$('nodeText').value;
					if(Ext.isEmpty(parentNode)){
						parentNode='新邮件信纸';
					}
					mailTemplateService.saveCotEmpsMailTemplate($('empsId').value,template,parentNode,function(){
						Ext.Msg.alert('提示消息','设置成功');
					});
				}
			}]
			
		});
	
		//配置参数说明
		var optionData = {
			records : [
				{ name : "$date", column : "当前时间" },
				{ name : "$empsSign", column : "签名" },
				{ name : "$sendNewMailName", column : "收件人" }
			]
		};
		
		var fields = [
		   {name: 'name', mapping : 'name'},
		   {name: 'column', mapping : 'column'}
		];
		var gridStore = new Ext.data.JsonStore({
	        fields : fields,
			data   : optionData,
			root   : 'records'
	    });
	    var cols = [
			{ id : 'name', header: "配置参数", sortable: true, dataIndex: 'name',width:'60%'},
			{header: "参数说明",  sortable: true, dataIndex: 'column'}
		];
		
		var grid = new Ext.grid.GridPanel({
				//ddGroup          : 'gridDDGroup',
			    collapsible :true,
		        store            : gridStore,
		        columns          : cols,
			//	enableDragDrop   : true,
		    //    stripeRows       : true,
		        width            : 250,
				region           : 'east',
		        title            : '配置参数说明',
				selModel         : new Ext.grid.RowSelectionModel({singleSelect : true})
		 });
		    
		var viewport = new Ext.Viewport({
				layout : 'border',
				items : [treePanel,mainPanel,grid]
			});
//	   viewport.doLayout();
	   
//	   var blankRecord =  Ext.data.Record.create(fields);
//	   
//	   var mainPanelEl = mainPanel.body.dom;
//	   var formPanelDropTarget = new Ext.dd.DropTarget(mainPanelEl, {
//		ddGroup : 'gridDDGroup',
//		notifyEnter : function(ddSource, e, data) {
//
//			//Add some flare to invitfe drop.
//			mainPanel.body.stopFx();
//			mainPanel.body.highlight();
//		},
//		notifyDrop  : function(ddSource, e, data){
//
//			// Reference the record (single selection) for readability
//			var selectedRecord = ddSource.dragData.selections[0];
//
//
//			// Load the record into the form
//			mainPanel.getForm().loadRecord(selectedRecord);
//            //fckSetValue.defer(1000,this,['fckeditorBody',selectedRecord]);
//              
//			// Delete record from the grid.  not really required.
//			//ddSource.grid.store.remove(selectedRecord);
//
//			return(true);
//		}
//	});
	   
	});