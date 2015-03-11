Ext.namespace('Ext.mail');

/**
 * 通用邮件GridPanel
 * @class Ext.mail.GridPanel
 * @extends Ext.grid.GridPanel
 */
Ext.mail.GridPanel = Ext.extend(Ext.grid.GridPanel,{
	autoExpandColumn:'subject',
	loadMask:{msg:'Data Loading。。。'},
    initComponent : function(){
    	var grid = this;
    	if(!grid.store)
    		grid.store = new Ext.mail.LocalInboxStore({url:'cotfactory.do?method=readLocalMails'});
    	grid.store.setBaseParam('factoryId',$('factoryId').value);
    	grid.view = new Ext.grid.GroupingView({
	        groupTextTpl: '{text} ({[values.rs.length]} {["封"]})'
	    })
	    var empsMap;
		baseDataUtil.getBaseDicDataMap("CotEmps", "id", "empsName", function(res) {
			empsMap = res;
		});
	    grid.colModel = new Ext.grid.ColumnModel({
    		columns:[
	    		new Ext.grid.RowNumberer(),
	    		{header: '', width: 20, dataIndex: 'mailType',resizable:false,renderer:Ext.mail.mailTypeRenderer},
//				{header: 'Sales', width: 200,dataIndex: 'empId',renderer : function(value) {return empsMap[value];}},
				{id:'subject',header: "Topics",width:100,renderer:Ext.mail.subjectRenderer, dataIndex: 'subject'},
				{header: '<span class="email_attach">&nbsp;</span>',renderer:Ext.mail.attachRenderer,resizable:false, width: 20, dataIndex: 'isContainAttach'},
				{header: "Time", width: 190, renderer: Ext.mail.dateAllRenderer, dataIndex: 'sendTime'},
				{header:'Time',hidden:true,hideable:false, renderer: Ext.mail.dateGroupingRenderer, dataIndex: 'groupDate'}
	    	],
	    	defaults:{
	            menuDisabled: true 
        	}
	    });
    	grid.bbar = grid.bbar || new Ext.PagingToolbar({
//        	plugins:new Ext.ux.ProgressBarPager(),
        	pageSize:EXT_MAIL_PAGE_LIMIT,
        	store:grid.store,
        	displaySize:EXT_MAIL_PAGE_DISPLAY_SIZE,
        	displayInfo:true
        });
        grid.tbar = [{
	            iconCls: 'icon-expand-all',
				tooltip: 'Expand all mail groups',
	            handler: function(){ this.getView().expandAllGroups(); },
	            scope: this
	        }, '-', {
		        iconCls: 'icon-collapse-all',
		        tooltip: 'Close all e-mail group',
		        handler: function(){ this.getView().collapseAllGroups(); },
		        scope: this
	        },'->',{
        	xtype:'daterangefield',
			emptyText:'Start Date',
			ref:'../startDateField',
			width:90,
			endDateField:grid,
			allowBlank:true
        },{
        	xtype:'daterangefield',
			emptyText:'End Date',
			width:90,
			ref:'../endDateField',
			startDateField:grid,
			allowBlank:true
        },
//        new BindCombox({
//			dataUrl : "servlet/DataSelvert?tbname=CotEmps&key=empsName&flag=filter",
//			cmpId : 'eId',
//			ref:'../empField',
//			fieldLabel : "Sales",
//			editable : true,
//			valueField : "id",
//			displayField : "empsName",
//			emptyText : 'Sales',
//			pageSize : 10,
//			width:120,
//			allowBlank : true,
//			sendMethod : "post",
//			selectOnFocus : false,
//			minChars : 1, // 设置填充几个字节就去查一次,默认为4个字节
//			listWidth : 200,// 下
//			triggerAction : 'all'
//		}),
			{
        	xtype:'searchcombo',
        	width:150,
        	store:grid.store,
        	displayField:'name',
        	valueField:'name',
        	ref:'../seachField',
        	emptyText:'Topic name',
        	paramFn:function(v,searchField){
        		var empField = searchField.refOwner.empField;  // 业务员
        		var startDateField = searchField.refOwner.startDateField; // 起始
        		var endDateField = searchField.refOwner.endDateField;	// 结束
        		var startDate = startDateField.getValue();
        		var endDate = endDateField.getValue();
        		if(!Ext.isDate(startDate)){
    				startDateField.setValue('');
    				startDate = '';
        		}
        		if(!Ext.isDate(endDate)){
    				endDateField.setValue('');
    				endDate = '';
        		}
        	//	var empId = empField.getValue();
        		if(startDate||endDate||v)
					return {
					//	empId:empId,
						subject:v,
						startDate:startDate?startDate.format('Y-m-d')+" 00:00:00":'',
						endDate:endDate?endDate.format('Y-m-d')+" 23:59:59":''
					}
				else
					return '';
        	},
        	clearParamFn:function(searchField){
        		searchField.refOwner.empField.setValue('');
        		searchField.refOwner.startDateField.setValue('');
        		searchField.refOwner.endDateField.setValue('');
        	}
        }];
    	Ext.mail.GridPanel.superclass.initComponent.call(this);
    },
	listeners:{
		rowclick:{
			fn:function(grid,rowIndex,e){				
				navHandler(1,grid,rowIndex);
			}
		}
	}
});
Ext.reg('mailgrid',Ext.mail.GridPanel);