Ext.namespace('Ext.mail');

Ext.mail.GridPanel = Ext.extend(Ext.grid.GridPanel,{
	autoExpandColumn:'subject',
	loadMask:{msg:'邮件加载中。。。'},
    showMailStatus:false, // 是否要显示邮件状态
    initComponent : function(){
    	var grid = this;
    	if(!grid.store)
    		grid.store = new Ext.mail.LocalInboxStore({mappingGroupDate:'addTime'});
    	grid.store.setBaseParam('isCheckMail',"true");
    	grid.store.load({
    		params:{
				start:EXT_MAIL_PAGE_START,
				limit:EXT_MAIL_PAGE_LIMIT
			}
		});
    	grid.view = new Ext.grid.GroupingView({
    		groupMode:'display',
	        groupTextTpl: '{text} ({[values.rs.length]} {["封"]})'
	    })
	    grid.colModel = new Ext.grid.ColumnModel({
    		columns:[
				{header: '业务员', width: 120,dataIndex: 'empId',sortable: false,renderer : function(value) {return empsMap[value];}},
				{header: '收件人',sortable: false, width: 200,dataIndex: 'to',renderer:Ext.mail.toNameRenderer},
				{id:'subject',header: "主题",width:100,renderer:Ext.mail.subjectRenderer, dataIndex: 'subject'},
				{header: '<span class="email_attach">&nbsp;</span>',renderer:Ext.mail.attachRenderer,hideable:false,menuDisabled:true,resizable:false, width: 39, dataIndex: 'isContainAttach'},
				{header: "时间", width: 190, renderer: Ext.mail.dateAllRenderer, dataIndex: 'addTime'},
				{header:'发件人邮箱',hidden:true,hideable:false, renderer: Ext.mail.senderGroupingRenderer, dataIndex: 'groupSender'},
				{header:'主题',hidden:true,hideable:false, renderer: Ext.mail.subjectGroupingRenderer, dataIndex: 'groupSubject'},
				{header:'附件',hidden:true,hideable:false, renderer: Ext.mail.attachGroupingRenderer, dataIndex: 'groupAttach'},
				{header:'时间',hidden:true,hideable:false, renderer: Ext.mail.dateGroupingRenderer, dataIndex: 'groupDate'}
	    	],
	    	defaults:{
	            sortable: true,
             	groupable: false,
	            menuDisabled: false
        	}
	    });
    	grid.bbar =  new Ext.PagingToolbar({
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
        grid.tbar = {
        	xtype:'searchcombotoolbar',
        	items:[{
	            iconCls: 'icon-expand-all',
				tooltip: '扩展所有邮件组',
	            handler: function(){ this.getView().expandAllGroups(); },
	            scope: this
	        }, '-', {
		        iconCls: 'icon-collapse-all',
		        tooltip: '关闭所有邮件组',
		        handler: function(){ this.getView().collapseAllGroups(); },
		        scope: this
	        },'->',{
	        	xtype:'daterangefield',
				emptyText:'起始时间',
				ref:'../startDateField',
				width:90,
				isSearchField:true,
				searchName:'startDate',
				endDateField:grid,
				allowBlank:true
	        },' ',{
	        	xtype:'daterangefield',
				emptyText:'结束时间',
				width:90,
				isSearchField:true,
				searchName:'endDate',
				ref:'../endDateField',
				startDateField:grid,
				allowBlank:true
	        },' ',new BindCombox({
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
			}),' ',{
	        	xtype:'textfield',
	        	emptyText:'收发件人',
	        	width:90,
	        	isSearchField:true,
	        	searchName:'person'
	        },' ',{
	        	xtype:'searchcombo',
	        	width:150,
	        	store:grid.store,
	        	queryStore:new Ext.mail.AutoCompleteStore({url:'cotmail.do?method=readAutoCompletMails'}),
	        	displayField:'name',
	        	isSearchField:true,
	        	searchName:'subject',
	        	ref:'searchField',
	        	autoComplet:true,
	        	valueField:'name',
	        	emptyText:'主题名称'
	        }]
        }
    	Ext.mail.GridPanel.superclass.initComponent.call(this);
    },
	listeners:{
		rowdblclick:{
			fn:function(grid,rowIndex,e){				
				var record = grid.getStore().getAt(rowIndex);
				navHandler(1,grid,rowIndex);
				var viewport = Ext.getCmp(EXT_MAIL_ID_VIEWPORT);
				var mailDefault = viewport.mailDefault;
				mailDefault.hide();
				viewport.doLayout();
			}
		},
		rowclick:{
			fn:function(grid,rowIndex,e){
				var viewport = Ext.getCmp(EXT_MAIL_ID_VIEWPORT);
				var mailDefault = viewport.mailDefault;
				var count = grid.getSelectionModel().getCount();
				if(count>0){
					mailDefault.show();
					viewport.doLayout();
					var record = grid.getSelectionModel().getSelections()[0];
					var data = record.data;
					mailLocalService.readMailAllInfo(record.id,function(mailObj){
						if(mailObj){
							data.body = mailObj.body;
							data.attachs = mailObj.attachs;
						}
						data.isRead = true;
						if(data.mailStatus>=0&&data.mailStatus!=5){
							if(data.mailStatus==1||data.mailStatus==6)
								data.isNotification = true;
							else
								data.isNotification = false;
							record.set('mailStatus',data.mailStatus<=2?2:4);
							record.commit();
						}
						Ext.mail.detailPanelInsertData(mailDefault,data);
					});
				}else{
					mailDefault.hide();
					viewport.doLayout();
				}
			}
		}
	}
});
Ext.reg('mailgrid',Ext.mail.GridPanel);
