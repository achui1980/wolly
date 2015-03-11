/**
 * 邮件工具栏按钮
 * 按钮是否可用,在click Tree,rowSelect grid中处理
 * @class Ext.mail.Toolbar
 * @extends Ext.Toolbar
 */
Ext.mail.Toolbar = Ext.extend(Ext.Toolbar,{
	mailTree:'', // 视图邮件树
	mailCard:'', // 视图邮件卡片
	enableOverflow: true,
	initComponent:function(){
		this.items = ['-',{
			text:'接收',
	       	tooltip:'手动接收新邮件',
        	iconCls:'email_link',
        	ref:'emailRecive',
        	disabled:true,
        	scope:this,
        	handler:function(){
        		var mailToolbar = this;
        		var node = EXT_VIEWPORT.mailTree.getSelectionModel().getSelectedNode(); // 初选中的节点
        		var grid = EXT_VIEWPORT.mailGrid; // 被击活的grid
        		var loadMask = new Ext.LoadMask(grid.getEl(),{msg:'正在接收邮件。。。'});
				loadMask.show();
        		mailLocalService.saveMailToEmpNode(node.attributes.empId,node.id,function(){
        			loadMask.hide();
	        		EXT_VIEWPORT.mailCard.layout.setActiveItem(0);
	        		grid.getStore().load({params:{start:0,limit:EXT_MAIL_PAGE_LIMIT}});
        		});
        	}
			
		},'-',{
			text:'撰写',
			tooltip:'以当前登录者的邮箱发送',
        	iconCls:'email_add',
        	ref:'emailSend',
        	scope:this,
        	handler:function(){
        		openWindowBase(580,800,'cotmailsend.do?method=query');        		
        	}
		},{
			text:'回复',
			tooltip:'回复发件人',
			iconCls:'email_go',
			scope:this,
			ref:'emailReply',
			disabled:true,
			handler:function(){
				var grid = EXT_VIEWPORT.mailGrid; // 被击活的grid
				var records = getSelDatas(grid) // 获得被选中的数据
				if(records.length>0)
					openWindowBase(500,800,'cotmailsend.do?method=query&mailId='+records[0].id+'&status='+MAIL_SEND_TYPE_STATUS_REPLAY); 
			}
		},{
			text:'全部回复',
			tooltip:'回复所有人',
			iconCls:'page_script_go',
			scope:this,
			ref:'emailAllReply',
			disabled:true,
			handler:function(){
				var grid = EXT_VIEWPORT.mailGrid; // 被击活的grid
				var records = getSelDatas(grid) // 获得被选中的数据
				if(records.length>0)
					openWindowBase(500,800,'cotmailsend.do?method=query&mailId='+records[0].id+'&status='+MAIL_SEND_TYPE_STATUS_REPLAYALL); 
			}
		},{
			text:'再次发送',
			tooltip:'再次发送邮件',
			iconCls:'page_key_go',
			scope:this,
			ref:'emailAgainSend',
			disabled:true,
			handler:function(){
				var grid = EXT_VIEWPORT.mailGrid; // 被击活的grid
				var records = getSelDatas(grid) // 获得被选中的数据
				if(records.length>0)
					openWindowBase(500,800,'cotmailsend.do?method=query&mailId='+records[0].id+'&status='+MAIL_SEND_TYPE_STATUS_REPEAT); 
			}
		},{
			text:'转发',
			tooltip:'转发邮件',
			iconCls:'page_newspaper_go',
			scope:this,
			ref:'emailForward',
			disabled:true,
			handler:function(){
				var grid = EXT_VIEWPORT.mailGrid; // 被击活的grid
				var records = getSelDatas(grid) // 获得被选中的数据
				if(records.length>0)
					openWindowBase(500,800,'cotmailsend.do?method=query&mailId='+records[0].id+'&status='+MAIL_SEND_TYPE_STATUS_FORWARD); 
			}
		},'-',{
			text:'指派',
			cls:"SYSOP_ASSIGN",
        	tooltip:'将选中的邮件指派到指定的员工',
        	iconCls:'page_pencil_go',
        	disabled:true,
        	scope:this,
        	ref:'emailAssign',
        	handler:function(){
        		var card = EXT_VIEWPORT.mailCard;
				var grid = EXT_VIEWPORT.mailGrid; // 被击活的grid
        		var selectionModel = grid.getSelectionModel();
        		var formPanel = new Ext.FormPanel({
        			frame:true,
        			labelWidth:40,
        			buttonAlign:'center',
        			items:[{
        				xtype:'bindCombo',
        				fieldLabel:'员工',
        				dataUrl : "servlet/DataSelvert?tbname=CotEmps&key=empsName",
						editable : true,
						name:'assignCombo',
						valueField : "id",
						displayField : "empsName",
						emptyText : '请选择',
						pageSize : 10,
						anchor : "100%",
						allowBlank : false,
						blankText : "请选择员工！",
						sendMethod : "post",
						selectOnFocus : true,
			
						listWidth : 350// 下
        			}],
        			buttons:[{
        				text:'指派',
        				handler:function(){
        					var form = formPanel.getForm();
        					if(!form.isValid()){
        						return;
        					}
        					var empId = form.findField('assignCombo').getValue();
        					win.close();
        					var records = getSelDatas(grid) // 获得被选中的数据
							var ids = [];
							Ext.each(records,function(record){
								if(record.data.mailType==MAIL_LOCAL_TYPE_INBOX)
									ids.push(record.id);
							});
							if(ids.length>0){
	    						card.layout.setActiveItem(0);
								var loadMask = new Ext.LoadMask(card.getEl(),{msg:'正在指派邮件。。。'});
								loadMask.show();
	        					mailLocalService.moveAssignMail(empId,ids,function(result){
	        						loadMask.hide();
	        						grid.getStore().reload();
									if(result){
										Ext.example.msg('系统提示', "指派成功",card.getEl());
									}else{
										Ext.example.msg('系统提示', "操作失败",card.getEl());	        				
									}
	        					});
							}
        				}
        			},{
        				text:'取消',
        				handler:function(){
        					win.close();
        				}
        			}]
        		});
        		var win =new Ext.Window({
        			title:'指派邮件',
        			width:250,
        			shadow:true,
        			constrain:true,
        			resizable:true,
        			modal:true,
        			items:[formPanel]
        		});
        		win.show();
        	}
		},{
			text:'移动到',
			tooltip:'在当前邮箱下移动，只能对应类型邮箱下移动',
			iconCls:'page_drive_go',
			scope:this,
			ref:'emailMove',
			disabled:true,
			handler:function(){
				var grid = EXT_VIEWPORT.mailGrid; // 被击活的grid
				var win = new Ext.mail.MailMoveTreeWindow({
					empId:grid.empId
				});
				win.show();
			}
		},'-',{
			text:'删除',
			tooltip:'将邮件删除到废件箱',
			iconCls:'email_delete',
			disabled:true,
			empId:'',
			mailIds:'',
			scope:this,
			ref:'emailDel',
			handler:function(){
				var mailToolbar = this;
				var card = EXT_VIEWPORT.mailCard; // 被击活的card
				var grid = EXT_VIEWPORT.mailGrid; // 被击活的grid
				var records = getSelDatas(grid) // 获得被选中的数据
				var node = EXT_VIEWPORT.mailTree.getSelectionModel().getSelectedNode(); // 初选中的节点
				var ids = [];
				Ext.each(records,function(record){
					ids.push(record.id);
				});
				var loadMask = new Ext.LoadMask(card.getEl(),{msg:'正在删除邮件。。。'});
				loadMask.show();
				var delMethod = mailTreeService.moveMailToDel;
				var delType = '删除';
				if(grid.flag == 'D'){
					delMethod = mailTreeService.moveDelMailToRevert
					delType = '还原';
				}
				delMethod(node.attributes.empId,ids,function(result){
					loadMask.hide();
					// 删除成功则重新加载
					if(result){
						Ext.example.msg('系统提示', delType+"成功",card.getEl());
						card.layout.setActiveItem(0);
						grid.getStore().reload();
					}else{
						Ext.example.msg('系统提示', delType+"失败",card.getEl());
					}
					
				});
			}
		},{
			text:'彻底删除',
			cls:"SYSOP_DEL",
			tooltip:'将邮件彻底删除,将无法恢复!',
			iconCls:'page_del',
			disabled:true,
			scope:this,
			ref:'emailEveryInchDel',
			handler:function(){
				var mailToolbar = this;
				var card = EXT_VIEWPORT.mailCard; // 被击活的card
				var grid = EXT_VIEWPORT.mailGrid; // 被击活的grid
				var records = getSelDatas(grid) // 获得被选中的数据
				var ids = [];
				Ext.each(records,function(record){
					ids.push(record.id);
				});
				Ext.Msg.show({buttons:Ext.Msg.YESNO,icon:Ext.Msg.INFO,title:'系统提示',msg:'确认彻底删除?',
					fn:function(btnId){
						if(btnId == 'yes'){
							card.layout.setActiveItem(0);
							var loadMask = new Ext.LoadMask(card.getEl(),{msg:'正在彻底删除邮件。。。'});
							loadMask.show();
							mailLocalService.deleteMails(ids,function(result){
								loadMask.hide();
								// 删除成功则重新加载
								if(result){
									Ext.example.msg('系统提示', "彻底删除成功",card.getEl());
									reloadSelData(grid);
								}else{
									Ext.example.msg('系统提示', "彻底删除失败",card.getEl());
								}
								
							});
						}
					}
				});
			}
		}];
		Ext.mail.Toolbar.superclass.initComponent.call(this);
	}
});
Ext.reg('mailtoolbar',Ext.mail.Toolbar);
/**
 * 对工具栏的是否可用控制
 */
Ext.mail.ToolbarControl = function(node){
	// 对Toolbar操作
	var mailToolbar = EXT_VIEWPORT.mailToolbar;
	var node = node||EXT_VIEWPORT.mailTree.getSelectionModel().getSelectedNode(); // 初选中的节点
	var nodeFlag = node?node.attributes.flag:'';
	var grid = EXT_VIEWPORT.mailGrid; // 被击活的grid
	if(!grid.store)
		grid = '';
	// 对接收按钮操作
	var emailRecive = mailToolbar.emailRecive;
	if(nodeFlag)
		if(nodeFlag=='G'||nodeFlag=='R'){
			emailRecive.enable();
		}else{
			emailRecive.disable();
		}
	else{
		emailRecive.disable();
	}
	// 对回复、全部回复按钮操作
	var emailReply = mailToolbar.emailReply;
	var emailAllReply = EXT_VIEWPORT.mailToolbar.emailAllReply;
	if(grid&&grid.flag=='R'&&grid.getSelectionModel().getCount()==1){
		emailReply.enable();
		emailAllReply.enable();
	}else{
		emailReply.disable();
		emailAllReply.disable();
	}
	// 对转发按钮操作
	var emailForward = mailToolbar.emailForward;
	if(grid&&grid.getSelectionModel().getCount()==1){
		emailForward.enable();
	}else{
		emailForward.disable();
	}
	// 对指派按钮操作
	var emailAssign = mailToolbar.emailAssign;
	if(grid&&grid.flag=='R'&&grid.getSelectionModel().getCount()>0){
		emailAssign.enable();
	}else{
		emailAssign.disable();
	}
	// 对再次发送按钮操作
	var emailAgainSend = mailToolbar.emailAgainSend;
	if(grid&&grid.flag=='S'&&grid.getSelectionModel().getCount()>0){
		emailAgainSend.enable();
	}else{
		emailAgainSend.disable();
	}
	// 对移动按钮操作
	var emailMove = mailToolbar.emailMove;
	if(grid&&grid.getSelectionModel().getCount()>0){
		emailMove.enable();
	}else{
		emailMove.disable();
	}
	// 对删除按钮操作
	var delBtn = mailToolbar.emailDel;
	var eiDelBtn = mailToolbar.emailEveryInchDel;
	if(grid&&grid.getSelectionModel().getCount()>0){
		if(nodeFlag)
			if(nodeFlag=='G')
				delBtn.disable();
			else
				delBtn.enable();
		eiDelBtn.enable();
	}else{
		delBtn.disable();
		eiDelBtn.disable();
	}
	// 当为废件箱时,显示删除按键为还原
	if(grid&&grid.flag=='D'){
		delBtn.setText('还原');
		delBtn.setTooltip('将邮件还原到所属类型的邮件箱');
		delBtn.setIconClass('email');
	}else{
		delBtn.setText('删除');
		delBtn.setTooltip('将邮件删除到废件箱');
		delBtn.setIconClass('email_delete');
	}
	
	if(nodeFlag.indexOf('A')==0||nodeFlag=='P'){
		emailRecive.disable();
		emailReply.disable();
		emailAllReply.disable();
		emailForward.disable();
		emailAssign.disable();
		emailAgainSend.disable();
		emailMove.disable();
		delBtn.disable();
		eiDelBtn.disable();
	}
}