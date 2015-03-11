/**
 * 通用邮件GridPanel
 * @class Ext.mail.GridPanel
 * @extends Ext.grid.GridPanel
 */
Ext.mail.GridPanel = Ext.extend(Ext.grid.GridPanel,{
	autoExpandColumn:'subject',
	loadMask:{msg:'邮件加载中。。。'},
    showMailStatus:false, // 是否要显示邮件状态
    showEmp:false,	// 是否显示业务员
    showContact:false, // 是否显示客户厂家联系人
    showSend:false,	// 是否显示发件人
    showTo:false,	// 是否显示收件人
    showSize:false,	// 是否显示邮件大小
    showRightMenu:false, // 是否显示右击菜单
    enableDrag:false,	// 默认为false
    ddText:'选择了 {0} 封邮件', // enableDrag为true时，显示文本
    isSearch:false,  // 是否要查询
	showSearchStatus:false, // 是否要显示查询邮件状态
    isStatusCheck:false, // 查询邮件状态，true显示为审核状态，false显示为邮件状态
    isSendTime:false, // 显示时间 true为发送时间'sendTime',false为添加时间'addTime'
    mailType:'main', // 邮件视图类型，main为主界面，check为审核界面，cust为客户界面,
    readMailType:'into', // 读取邮件详情方式，into为进入读取详情，out为发送页面窗口
    mailInfoCard:'', // 所属的card
    initComponent : function(){
    	var grid = this;
		grid.store = grid.store || new Ext.mail.LocalInboxStore({mappingGroupDate:this.isSendTime?'sendTime':'addTime'});
		if(this.mailType == 'cust')
			grid.store.setBaseParam('custId',Ext.getDom('custId').value);
		else if(this.mailType == 'check')
			grid.store.setBaseParam('isCheckMail',"true");
    	grid.view = new Ext.grid.GroupingView({
    		groupMode:'display', // 指定分组ID为分组显示值相关
	        groupTextTpl: '{text} ({[values.rs.length]} {["封"]})'
	    })
	    if(this.mailType == 'mail')
		    grid.store.on('load',function(){
		    	// 工具栏
		    	Ext.mail.ToolbarControl();
		    	// 历史
				Ext.mail.ComeAndGoMailControl();
		    });
    	var sm = new Ext.grid.CheckboxSelectionModel({
		    onHdMouseDown : function(e, t){
		        if(t.className == 'x-grid3-hd-checker'){
		            e.stopEvent();
		            var hd = Ext.fly(t.parentNode);
		            var isChecked = hd.hasClass('x-grid3-hd-checker-on');
		            if(isChecked){
		                hd.removeClass('x-grid3-hd-checker-on');
		                this.clearSelections();
		            }else{
		                hd.addClass('x-grid3-hd-checker-on');
		                this.selectAll();
		            }
		            if(grid.mailType == 'main'){
			            Ext.mail.ToolbarControl();
						// 历史
						Ext.mail.ComeAndGoMailControl();
		            }
		        }
		    }
		});
		grid.sm = sm;
		var columns = [sm];
		if(this.showMailStatus){
			columns.push({header: '<span class="email_1">&nbsp;</span><span class="page_pencil_go_1">&nbsp;</span><span class="email_replay">&nbsp;</span>',menuDisabled:true,resizable:false,hideable:false, width: 85, dataIndex: 'mailStatus',renderer:Ext.mail.mailStatusRenderer});
			columns.push({header:'状态',hidden:true,hideable:false, renderer: Ext.mail.statusGroupingRenderer, dataIndex: 'groupStatus'});
		}
		if(this.showEmp)
			columns.push({header: '业务员', width: 120,dataIndex: 'empId',renderer : function(value) {return empsMap[value];}});
		if(this.showContact)
			columns.push({header: '<span style="color:blue">客户</span>/<span style="color:green">厂家</span>', width: 140,dataIndex: 'custId',renderer :Ext.mail.contactRenderer})
		if(this.showSend){
			columns.push({header: '发件人', width: 200,dataIndex: 'sender',renderer:Ext.mail.senderNameRenderer});
			columns.push({header:'发件人邮箱',hidden:true,hideable:false, renderer: Ext.mail.senderGroupingRenderer, dataIndex: 'groupSender'});
		}
		if(this.showTo)
			columns.push({header: '收件人',sortable: false, width: 200,dataIndex: 'to',renderer:Ext.mail.toNameRenderer});
		columns.push({id:'subject',header: "主题",width:100,renderer:Ext.mail.subjectRenderer, dataIndex: 'subject'});
		columns.push({header: '<span class="email_attach">&nbsp;</span>',renderer:Ext.mail.attachRenderer,hideable:false,menuDisabled:true,resizable:false, width: 39, dataIndex: 'isContainAttach'});
		columns.push({header: "时间", width: 190, renderer: Ext.mail.dateAllRenderer, dataIndex: this.isSendTime?'sendTime':'addTime'});
		columns.push({header:'主题',hidden:true,hideable:false, renderer: Ext.mail.subjectGroupingRenderer, dataIndex: 'groupSubject'});
		columns.push({header:'附件',hidden:true,hideable:false, renderer: Ext.mail.attachGroupingRenderer, dataIndex: 'groupAttach'});
		columns.push({header:'时间',hidden:true,hideable:false, renderer: Ext.mail.dateGroupingRenderer, dataIndex: 'groupDate'});
		if(this.showSize){
			columns.push({header: "大小", width: 80, renderer: Ext.mail.sizeRenderer, dataIndex: 'size'});
			columns.push({header:'大小',hidden:true,hideable:false, renderer: Ext.mail.sizeGroupingRenderer, dataIndex: 'groupSize'});
		}
	    grid.colModel = new Ext.grid.ColumnModel({
    		columns:columns,
	    	defaults:{
	            sortable: true,
             	groupable: false,
	            menuDisabled: false
        	}
	    });
    	grid.bbar = grid.bbar || new Ext.PagingToolbar({
//        	plugins:new Ext.ux.ProgressBarPager(),
        	pageSize:EXT_MAIL_PAGE_LIMIT,
        	store:grid.store,
        	displaySize:EXT_MAIL_PAGE_DISPLAY_SIZE,
        	displayInfo:true,
        	listeners:{
    		 	beforechange : function(pTbar,params){
    		 		grid.store.setBaseParam('limit',params.limit);
    		 	}
        	}
        });
        var tbarItems = [{
            iconCls: 'icon-expand-all',
			tooltip: '扩展所有邮件组',
            handler: function(){ this.getView().expandAllGroups(); },
            scope: this
        }, '-', {
	        iconCls: 'icon-collapse-all',
	        tooltip: '关闭所有邮件组',
	        handler: function(){ this.getView().collapseAllGroups(); },
	        scope: this
        },'->'];
        if(this.isSearch){
	        if(this.showSearchStatus)
	        	tbarItems.push({
		        	xtype:'combo',
		        	emptyText:'所有邮件',
		        	width:80,
		        	isSearchField:true,
		        	searchName:'status',
				    triggerAction: 'all',
				    editable:false,
					forceSelection:true,
				    mode: 'local',
				    store: new Ext.data.ArrayStore({
				        fields: ['id','name'],
				        data: this.isStatusCheck?[[0,'所有邮件'],[-9,'待审核'],[-8,'审核不通过']]:[[0,'所有邮件'],[1, '新邮件'],[2, '已读'],[3,'指派未读'],[4,'指派已读'],[5,'未接收邮件']]
				    }),
				    valueField: 'id',
				    displayField: 'name'
		        },' ');
			if(this.showEmp)
				tbarItems.push(new BindCombox({
					dataUrl : "servlet/DataSelvert?tbname=CotEmps&key=empsName&flag=filter",
					cmpId : 'eId',
					ref:'empField',
					fieldLabel : "业务员",
					editable : true,
					valueField : "id",
					displayField : "empsName",
					emptyText : '业务员',
					pageSize : 10,
					width:120,
					allowBlank : true,
					sendMethod : "post",
					selectOnFocus : false,
					isSearchField:true,
					searchName:'empId',
					minChars : 1, // 设置填充几个字节就去查一次,默认为4个字节
					listWidth : 200,// 下
					triggerAction : 'all'
				}),' ');
			tbarItems.push({
	        	xtype:'daterangefield',
				emptyText:'起始时间',
				ref:'../startDateField',
				width:90,
				isSearchField:true,
				searchName:'startDate',
				endDateField:grid,
				allowBlank:true
	        },' ');
	        tbarItems.push({
	        	xtype:'daterangefield',
				emptyText:'结束时间',
				width:90,
				isSearchField:true,
				searchName:'endDate',
				ref:'../endDateField',
				startDateField:grid,
				allowBlank:true
	        },' ');
	        tbarItems.push({
	        	xtype:'textfield',
	        	emptyText:'收发件人',
	        	width:90,
	        	isSearchField:true,
	        	searchName:'person'
	        },' ');
	        tbarItems.push({
	        	xtype:'searchcombo',
	        	width:150,
	        	store:grid.store,
//	        	queryStore:new Ext.mail.AutoCompleteStore({url:'cotmail.do?method=readAutoCompletMails'}),
//	        	autoComplet:true,
	        	displayField:'name',
	        	isSearchField:true,
	        	searchName:'subject',
	        	valueField:'name',
	        	emptyText:'主题名称'
	        },' ');
		    this.tbar = {
		    	xtype:'searchcombotoolbar',
		    	items:tbarItems
		    };
        }
        if(this.showRightMenu){
        	// 定义右键菜单
			var rightMenu = new Ext.menu.Menu({
				items: [{
					text:'移动到',
					ref:'moveItem',
					handler:function(){
						var mailIds = [];
						var records = grid.getSelectionModel().getSelections();
						Ext.each(records,function(record){
							mailIds.push(record.id);
						})
						var win = new Ext.mail.MailMoveTreeWindow({
							flag:grid.flag,
							mailIds:mailIds,
							empId:grid.empId,
							mailGrid:grid
						});
						win.show();
					}
				},{
					text:'标记为未读',
					ref:'noReadItem',
					handler:function(){
						var mailIds = [];
						var records = grid.getSelectionModel().getSelections();
						Ext.each(records,function(record){
							if(record.data.mailType==MAIL_LOCAL_TYPE_INBOX)
								mailIds.push(record.id);
						})
						if(mailIds.length>0){
							var loadMask = new Ext.LoadMask(grid.getEl(),{msg:'正在标记邮件。。。'});
							loadMask.show();
							mailLocalService.updateMailStatus(mailIds, true,function(){
								loadMask.hide();
								grid.getStore().reload();
							});
						}
					}
				},{
					text:'标记为已读',
					ref:'readItem',
					handler:function(){
						var mailIds = [];
						var records = grid.getSelectionModel().getSelections();
						Ext.each(records,function(record){
							if(record.data.mailType==MAIL_LOCAL_TYPE_INBOX)
								mailIds.push(record.id);
						})
						if(mailIds.length>0){
							var loadMask = new Ext.LoadMask(grid.getEl(),{msg:'正在标记邮件。。。'});
							loadMask.show();
							mailLocalService.updateMailStatus(mailIds, false,function(){
								loadMask.hide();
								grid.getStore().reload();
							});
						}
					}
				}]	
			});
			rightMenu.on('click',function(rightMenu){
				rightMenu.hide();
			});
			if(this.mailType=='main')
				grid.on('rowcontextmenu', 
			  		function(grid, rowIndex, e){
		  				e.preventDefault(); 
						var node = EXT_VIEWPORT.mailTree.getSelectionModel().getSelectedNode(); // 初选中的节点
						var nodeFlag = node?node.attributes.flag:'';
			  			var records = grid.getSelectionModel().getSelections();
			  			if(records.length>0){
							var isShow = false;
							var isRead = false;
							var isNoRead = false;
							Ext.each(records,function(record){
								if(record.get('mailType')==2){
									isShow = true;
								}
								var mailStatus = record.get('mailStatus');
								if(mailStatus==0||mailStatus==1||mailStatus==3||mailStatus==6){	// 未读邮件
									isNoRead = true;
								}else{
									isRead = true;
								}
							})
							if(isShow){
								if(isRead)
									rightMenu.noReadItem.enable();
								else
									rightMenu.noReadItem.disable();
								if(isNoRead)
									rightMenu.readItem.enable();
								else
									rightMenu.readItem.disable();
							}else{
								rightMenu.noReadItem.disable();
								rightMenu.readItem.disable();
							}
							if(nodeFlag.indexOf('A')==0)
								rightMenu.moveItem.disable();
							else
								rightMenu.moveItem.enable();
			  				rightMenu.showAt(e.getXY());
			  			}
			  		}
			  	);
        }
    	Ext.mail.GridPanel.superclass.initComponent.call(this);
    },
    listeners:{
   		afterrender:{
   			fn:function(grid){
				grid.mailLoadMask = new Ext.LoadMask(grid.getEl(),{msg:'邮件读取中。。。'});
			}
		},
		rowdblclick:{
			fn:function(grid,rowIndex,e){
				var record = grid.getStore().getAt(rowIndex);
				if(grid.readMailType=='into'){
					navHandler('this',grid,EXT_VIEWPORT.dbMailDefault);
					if(grid.mailType!='cust')
						hideSelDefault(EXT_VIEWPORT);
					EXT_VIEWPORT.mailCard.layout.setActiveItem(1); // 双击进去，设置dbMailDefault为击活项
				}else if(grid.readMailType=='out'){
					openWindowBase(500,800,'cotmailsend.do?method=query&mailId='+record.id); 
				}
			}
		},
		rowclick:{
			fn:function(grid,rowIndex,e){
				if(grid.mailType=='cust')
					return;
				if(getSelDatas(grid).length>0){
					showSelDefault(EXT_VIEWPORT);
					if(grid.mailType=='main'&&EXT_VIEWPORT.mailDefault.collapsed){
						Ext.mail.ToolbarControl();
						// 历史
						Ext.mail.ComeAndGoMailControl();
						return;
					}
					navHandler('this',grid,EXT_VIEWPORT.mailDefault);
				}else{
					if(grid.mailType=='main'){
						Ext.mail.ToolbarControl();
						// 历史
						Ext.mail.ComeAndGoMailControl();
					}
					hideSelDefault(EXT_VIEWPORT);
				}
			}
		}
    }
});
Ext.reg('mailgrid',Ext.mail.GridPanel);
/**
 * 本地收件箱
 * @class Ext.mail.LocalInboxCard
 * @extends Ext.mail.Card
 */
Ext.mail.InboxGrid = Ext.extend(Ext.mail.GridPanel,{
	showMailStatus:true,
	showContact:true,
	showSend:true,
	showSize:true,
	showRightMenu:true,
	enableDrag:true,
	isSearch:true,
	showSearchStatus:true,
	isSendTime:true
});
Ext.mail.SendGrid = Ext.extend(Ext.mail.GridPanel,{
	showTo:true,
	showRightMenu:true,
	enableDrag:true,
	isSearch:true,
	isSendTime:true
});
Ext.mail.DraftGrid = Ext.extend(Ext.mail.GridPanel,{
	showTo:true,
	showRightMenu:true,
	enableDrag:true,
	readMailType:'out'
});
Ext.mail.DelGrid = Ext.extend(Ext.mail.GridPanel,{
	showMailStatus:true,
	showContact:true,
	showSend:true,
	showSize:true,
	showRightMenu:true,
	enableDrag:true,
	isSearch:true,
	showSearchStatus:true,
	isSendTime:true
});
Ext.mail.CheckGrid = Ext.extend(Ext.mail.GridPanel,{
	showTo:true,
	showRightMenu:true,
	enableDrag:true,
	isSearch:true,
	showSearchStatus:true,
	isStatusCheck:true
});
Ext.reg('checkgrid',Ext.mail.CheckGrid);
Ext.mail.ComeAndGoMail = Ext.extend(Ext.grid.GridPanel,{
	border:false,
	loadMask:{msg:'往来邮件加载中。。。'},
	autoExpandColumn:'subject',
	initComponent:function(){
		var grid = this;
		grid.store = new Ext.mail.ComeAndGoStore();
		grid.store.on('load',function(store){
			grid.setTitle('往来邮件('+store.getTotalCount()+')');		
		});
		grid.tbar = {
       		layout:'fit',
       		items:[{
	       		xtype:'searchTrigger',
	       		emptyText:'主题',
	       		ref:'../searchField',
	       		disabled:true,
	       		seachStore:grid.store
       		}]
       	};
		grid.cm = new Ext.grid.ColumnModel({
			columns:[
				{header: '', width: 20, dataIndex: 'mailType',renderer:Ext.mail.mailTypeRenderer},
				{id:'subject',header: "主题-时间",renderer:Ext.mail.subTimeRenderer},
				{header: '<span class="email_attach">&nbsp;</span>',renderer:Ext.mail.attachRenderer, width: 20, dataIndex: 'isContainAttach'}
	    	],
	    	defaults:{
	            menuDisabled: true,
	            resizable:false
        	}
		});
		grid.bbar = grid.bbar || new Ext.PagingToolbar({
        	pageSize:EXT_MAIL_HISTORY_PAGE_LIMIT,
        	store:grid.store,
        	displayInfo:true
        });
		Ext.mail.ComeAndGoMail.superclass.initComponent.call(this);
	},
	listeners:{
		rowclick:{
			fn:function(grid,rowIndex,e){
				var mailDefault = EXT_VIEWPORT.historyCard.mailDefault;
				navHandler('this',grid,mailDefault);
				EXT_VIEWPORT.historyCard.layout.setActiveItem(1);
			}
		}
	}
});
Ext.reg('comeandgomail',Ext.mail.ComeAndGoMail);

Ext.mail.ComeAndGoAttach = Ext.extend(Ext.grid.GridPanel,{
	border:false,
	loadMask:{msg:'往来附件加载中。。。'},
	autoExpandColumn:'name',
	initComponent:function(){
		var grid = this;
		grid.store = new Ext.mail.ComeAndGoAttachStore();
		grid.store.on('load',function(store){
			grid.setTitle('往来附件('+store.getTotalCount()+')');		
		});
		grid.cm = new Ext.grid.ColumnModel({
			columns:[
				{id:'name',header: "附件-时间",dataIndex: 'name',renderer:Ext.mail.nameTimeRenderer},
				{header: "大小", width: 80, renderer: Ext.mail.sizeRenderer, dataIndex: 'size'}
	    	],
	    	defaults:{
	            menuDisabled: true,
	            resizable:false
        	}
		});
		grid.bbar = new Ext.PagingToolbar({
        	pageSize:EXT_MAIL_HISTORY_PAGE_LIMIT,
        	store:grid.store,
        	displayInfo:true
        });
        grid.sm = new Ext.grid.RowSelectionModel({
    		singleSelect:true	
       	});
       	grid.tbar = {
       		layout:'fit',
       		items:[{
	       		xtype:'searchTrigger',
	       		emptyText:'附件名',
	       		ref:'../searchField',
	       		disabled:true,
	       		seachStore:grid.store
       		}]
       	};
            // 定义右键菜单
		var rightMenu = new Ext.menu.Menu({
			items: [{
				text:'打开附件',
				handler:function(){
					var data = grid.getSelectionModel().getSelected().data; 
					window.open('downLoadMailFile.down?'+Ext.urlEncode({path:data.url,name:data.name}),"_blank");
				}
			},{
				text:'下载附件',
				handler:function(){
					var data = grid.getSelectionModel().getSelected().data; 
					location.href = 'downLoadMailFile.down?'+Ext.urlEncode({path:data.url,name:data.name})+'&t=down';
				}
			},{
				text:'打开邮件',
				scope:grid,
				handler:function(){
					var record = grid.getSelectionModel().getSelected();
					var mailType = record.get('mailType');
					var gridDetail;
					mailLocalService.readMailAllInfo(record.data.mailId,function(mailObj){
						mailObj.isRead = true;
						if(mailType==0)
							mailObj.isNotification=false;
						var mailDefault = EXT_VIEWPORT.historyCard.mailDefault;
						mailDefault.prevBtn.disable();
						mailDefault.nextBtn.disable();
						Ext.mail.detailPanelInsertData(mailDefault,mailObj);
						EXT_VIEWPORT.historyCard.layout.setActiveItem(1);
					});
				}
			}]	
		});
		rightMenu.on('click',function(rightMenu){
			rightMenu.hide();
		});
		grid.on('rowcontextmenu', 
	  		function(grid, rowIndex, e){
	  			e.preventDefault();
	  			grid.getSelectionModel().selectRow(rowIndex);
	  			rightMenu.showAt(e.getXY());
	  		}
	  	);
	  	grid.on('rowdblclick',
	  		function(grid, rowIndex, e){
	  				var data = grid.getSelectionModel().getSelected().data; 
					window.open('downLoadMailFile.down?'+Ext.urlEncode({path:data.url,name:data.name}),"_blank");
	  		}
	  	);
		Ext.mail.ComeAndGoMail.superclass.initComponent.call(this);
	}
});
Ext.reg('comeandgoattach',Ext.mail.ComeAndGoAttach);
/**
 * 对历史GRID的控件,所有有关历史的操作,直接调用该方法
 */
Ext.mail.ComeAndGoMailControl = function(){
	if(!EXT_VIEWPORT.mailGrid)
		return;
	var historyCard = EXT_VIEWPORT.historyCard;
	var cagGrid = historyCard.grid;
	var cagAttachGrid = historyCard.attachGrid;
	var cagStore = cagGrid.getStore();
	var cagAttachStore = cagAttachGrid.getStore();
	var grid = EXT_VIEWPORT.mailGrid; // 被击活的grid
	var records = grid&&grid.store?getSelDatas(grid):[];
	var searchField = cagGrid.searchField;
	var searchAttachField = cagAttachGrid.searchField;
	if(!historyCard.collapsed&&records.length>0&&grid.flag!='C'&&grid.flag!='P'){
		searchField.enable();
		searchAttachField.enable();
		var record = records[0];
		var emailUrl = '';
		if(grid.flag=='S'){
			to = record.get('to');
			if(!Ext.isEmpty(to))
				emailUrl= to[0].emailUrl;
		}else
			emailUrl = record.get('sender').emailUrl;
		if(cagStore.baseParams&&cagStore.baseParams['mailUrl']==emailUrl&&cagStore.baseParams['empId']==EXT_VIEWPORT.mailGrid.empId)
			return;
		historyCard.layout.setActiveItem(0); // 
		cagStore.setBaseParam('mailUrl',emailUrl);
		cagStore.setBaseParam('empId',EXT_VIEWPORT.mailGrid.empId);
		cagStore.load({
			params:{
				start:EXT_MAIL_HISTORY_PAGE_START,
				limit:EXT_MAIL_HISTORY_PAGE_LIMIT
			}
		});
		cagAttachStore.mailRecord = record; // 当前被选中邮件
		cagAttachStore.setBaseParam('mailUrl',emailUrl);
		cagAttachStore.setBaseParam('empId',EXT_VIEWPORT.mailGrid.empId);
		cagAttachStore.load({
			params:{
				start:EXT_MAIL_HISTORY_PAGE_START,
				limit:EXT_MAIL_HISTORY_PAGE_LIMIT
			}
		});
	}else{
		cagGrid.setTitle('往来邮件');
		cagAttachGrid.setTitle('往来附件');
		cagStore.setBaseParam('mailUrl','');
		cagStore.removeAll();
		cagAttachStore.removeAll();
		searchField.disable();
		searchAttachField.disable();
	}	
}