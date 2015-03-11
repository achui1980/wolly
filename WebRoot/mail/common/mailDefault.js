Ext.namespace('Ext.mail');

Ext.mail.Default=Ext.extend(Ext.FormPanel ,{
	titleCollapse:true, // 不显示标题
	layout:"fit",
	mailType:'main', // 邮件视图类型，main为主界面，check为审核界面，cust为客户界面
	hideClose:false, // 是否显示关闭按钮
	hideReplay:false, // 是否显示快速回复
	iframeId:'',		
	initComponent: function(){
		if(!this.iframeId)
			this.iframeId = 'iframe'+ Math.floor(Math.random()*100000);
		var mailDefault = this;
		this.removeClass('x-panel-body');
		var tbarItems = [];
		if(this.mailType=='check')
			tbarItems.push([{
    			text:'审核通过',
    			iconCls:'page_check_ok',
    			scope:this,
    			handler:function(){
    				this.fireEvent('checkBtn',this,true);
    			}
    		},'-',{
    			text:'审核不通过',
    			iconCls:'page_check_error',
    			scope:this,
    			handler:function(){
    				this.fireEvent('checkBtn',this,false);
    			}
    		},{
    			xtype:'textfield',
    			emptyText:'审核不通过批注',
    			maxLength:250,
    			ref:'../checkMsg',
    			width:400
    		},'-']);
    	tbarItems.push([{
			text:'修改',
			ref:'../modifyBtn',
			iconCls:'email_edit',
			hidden:false,
			scope:this,
			handler:function(){
				this.fireEvent('modifyBtn',this);
			}
		},
		'->',{
			text:'上一封',
			ref:'../prevBtn',
			iconCls:'page_resultset_previous',
			scope:this,
			handler:function(){
				this.fireEvent('pageBtn',this,'prev');
			}
		},'-',{
			text:'下一封',
			ref:'../nextBtn',
			iconCls:'page_resultset_next',
			scope:this,
			handler:function(){
				this.fireEvent('pageBtn',this,'next');
			}
		},'-',{
			text:'打印',
			iconCls:'page_print',
			handler:showprint.createDelegate(this)
		},{
			xtype:'tbseparator',
			hidden:this.hideClose
		},{
			text:'关闭',
			iconCls:'gird_close',
			hidden:this.hideClose,
			scope:this,
			handler:function(){
				this.fireEvent('closeBtn',this);
			}
		}]);
		this.tbar = {
			xtype:'toolbar',
			enableOverflow:true,
			items:tbarItems
		};
		this.items=[{
			xtype:"panel",
			titleCollapse:true,
			autoScroll:true,
			ref:'allInfoPanel',
			border:false,
			items:[{
				ref:'../formPanel',
				labelWidth:60,
				labelAlign:"right",
				collapsible:true,
				titleCollapse:true,
				title:'<B>邮件头</B>',
				split:true,
				layout:"form",
				region:"north",
				frame:true
			},{
				layout:'fit',
				border:false,
				items:[{
					xtype:'panel',
					autoHeight:true,
					border:false,
					layout:'fit',
					style: 'padding: 15px;',
					ref:'../../bodyPanel',
					html:'<iframe frameborder="0" padding="0" margin="0" scrolling="no" width="100%" id="'+this.iframeId+'"></iframe>'
				},{
					xtype:'panel',
					autoHeight:true,
					title:'附件',
					style: 'padding: 15px;',
					ref:'../../attachPanel'
				}]
			},{
				titleCollapse:true,
				layout:"column",
				height:80,
				hidden:this.hideReplay,
				frame:true,
				items:[{
					labelWidth:70,
					labelAlign:"right",
					layout:"form",
					columnWidth:0.8,
					items:{
						xtype:"textarea",
						name:'rejuvenation',
						fieldLabel:"快速回复",
						emptyText:'快速回复',
						anchor:"100%",
						height:60
					}
				},{
					xtype:"button",
					text:"发送",
					width:60,
					style:'height:60px;',
					scope:this,
					handler:function(){
						var panel = this;
						var form = panel.getForm();
						var fPanel = panel.formPanel;
						var rej = form.findField('rejuvenation');
						var subject = fPanel.subject;
						var sender = fPanel.sender;
						var sendTime = fPanel.sendTime;
						var to = fPanel.to;
						var body = 
							'<div style="font-family: Verdana; font-size: 10px; color: rgb(0, 0, 0);">' +
								'<div>'+rej.getValue()+'</div>' +
								'<div style="color: rgb(0, 0, 0);">' +
									'<div>&nbsp;</div>' +
									'<div>&nbsp;</div>' +
									'<div style="font-size: 10px; font-family: Arial Narrow; padding: 2px 0pt;">---------------------------------------------------</div>' +
									'<div style="font-size: 10px;"><div><b>发件人:</b> "' +sender.initialConfig.html+'</div>' +
									'<div><b>发送时间:</b> '+sendTime.initialConfig.html+'</div>' +
									'<div><b>收件人:</b> '+to.initialConfig.html+'</div>' +
									'<div><b>主题:</b> '+subject.initialConfig.html+'</div>' +
								'</div>' +
							'<div>&nbsp;</div>' +
							'<div>'+panel.bodyPanel.data+'</div>';
						var mail = {
							to:[sender.data],
							subject:"Re: "+subject.data,
							body:body
						}
					    var loadMask = new Ext.LoadMask(panel.getEl(),{msg:'回复中。。。'});
						loadMask.show();
						mailSendService.addSendout(mail,null,function (result){
							loadMask.hide();
							Ext.mail.sendInfoFn(result,'回复');
							rej.setValue('');
						});	 
					}
				}]
			}]
		}];
		Ext.mail.Default.superclass.initComponent.call(this);
		this.addEvents(
			'closeBtn',// 点击关闭按钮事件，参数 本身
			'pageBtn', // 上一页，下一页按钮事件，参数 本身、上下页
			'modifyBtn', // 修改邮件事件 参数 本身
			'checkBtn' // 审核通过，审核不通过按钮事件，参数 本身、验证是否通过
		); 
	},
	listeners:{
   		afterrender:{
   			fn:function(grid){
				grid.mailLoadMask = new Ext.LoadMask(grid.getEl(),{msg:'邮件读取中。。。'});
			}
		}
	}
})
Ext.reg('maildefault',Ext.mail.Default);