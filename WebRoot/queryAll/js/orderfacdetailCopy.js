OrderfacdetailCopyWin = Ext.extend(Ext.Window,{
	width:800,
	height:550,
	closeAction:'close',
	orderFacId:0,
	initComponent:function(){
	var orderfacMap;
	var me = this;
	DWREngine.setAsync(false);
	// 加载模块列
	cotCustomizeService.getCotCustomizeFieldMap('CotOrderFacdetail', function(rs) {
				orderfacMap = rs;
			});
	DWREngine.setAsync(true);
	/** ******EXT创建grid步骤******** */
	/* 1、创建数据记录类型类型 Ext.data.Record.create */
	/* 2、创建数据存储对象(数据源) Ext.data.Store */
	/* 3、创建需要在表格显示的列 Ext.grid.ColumnModel */
	/* 4、创建表格对象，加载数据 Ext.grid.GridPanel */
	/** ************** */
	var roleRecord = new Ext.data.Record.create([{
				name : "id",
				type : "int"
			}, {
				name : "sortNo"
			}, {
				name : "barcode"
			}, {
				name : "eleId"
			},{
				name : "eleCol"
			}, {
				name : "factoryNo"
			}, {
				name : "custNo"
			}, {
				name : "eleName"
			}, {
				name : "eleNameEn"
			}, {
				name : "taoUnit"
			}, {
				name : "eleUnit"
			}, {
				name : "boxCount"
			}, {
				name : "containerCount"
			}, {
				name : "eleSizeDesc"
			}, {
				name : "eleInchDesc"
			}, {
				name : "eleDesc"		//smell
			}, {
				name : "eleComposetype"	//weight
			}, {
				name : "eleTypenameLv1"		//general
			},{
				name : "eleTypenameLv2"		//packing
			}, {
				name : "boxObL",
				convert : numFormat.createDelegate(this, ["0.00"], 3)
			}, {
				name : "boxObW",
				convert : numFormat.createDelegate(this, ["0.00"], 3)
			}, {
				name : "boxObH",
				convert : numFormat.createDelegate(this, ["0.00"], 3)
			}, {
				name : "boxGrossWeigth",
				convert : numFormat.createDelegate(this, ["0.000"], 3)
			}, {
				name : "boxNetWeigth",
				convert : numFormat.createDelegate(this, ["0.000"], 3)
			}, {
				name : "type"
			}, {
				name : "rdm"
			}, {
				name : "eleMod"
			},{
				name:'eleFlag'
			}]);
	// 创建数据源
	var ds = new Ext.data.Store({
		autoSave : false,
		method : 'post',
		proxy : new Ext.data.HttpProxy({
					api : {
						read : "cotorderfac.do?method=queryDetailCopy&orderId="
								+ me.orderFacId + "&flag=orderFacDetail"
					}
				}),

		reader : new Ext.data.JsonReader({
					root : "data",
					totalProperty : "totalCount",
					idProperty : "id"
				}, roleRecord)
	});

	// 创建复选框列
	var sm = new Ext.grid.CheckboxSelectionModel();
	// 创建需要在表格显示的列
	var cm = new HideColumnModel({
		defaults : {
			sortable : true,
			editable : false
		},
		hiddenCols : orderfacMap,
		columns : [
				sm,// 添加复选框列
				{
					header : "ID",
					dataIndex : "id",
					width : 50,
					hidden : true
				},{
					header:'Approve',
					dataIndex:'eleFlag',
					width:60,
					renderer:function(value){
						if(value == 1){
							return '<font color="green">Yes</font>'
						}else{
							return '<font color="red">No</font>'
						}
					}
				}, {
					header : "Sort",
					align : 'center',
					dataIndex : "sortNo",
					hidden:true,
					menuDisabled : true,
					width : 40
				}, {
					header : "W&C Art No.",
					dataIndex : "eleId",
					hidden:true,
					width : 120
				}, {
					header : "Client Art No.",
					dataIndex : "custNo",
					hidden:false,
					width : 100
				}, {
					header : "Barcode",
					dataIndex : "barcode",
					width : 100
				}, {
					header : "Descripiton",
					dataIndex : "eleNameEn",
					width : 140
				},{
					header : "Size",
					dataIndex : "eleInchDesc",
					width : 100
				},{
					header : "Color",
					hidden:false,
					dataIndex : "eleCol",
					width : 140
				},{
					header : "Weight",
					hidden:false,
					dataIndex : "eleComposetype",
					width : 140
				},{
					header : "Smell",
					hidden:false,
					dataIndex : "eleDesc",
					width : 140
				},{
					header : "Packing",
					hidden:false,
					dataIndex : "eleTypenameLv2",
					width : 140
				},{
					header : "General",
					hidden:false,
					dataIndex : "eleTypenameLv1",
					width : 140
				}]
	});

	var toolBar = new Ext.PagingToolbar({
				pageSize : 15,
				store : ds,
				displayInfo : true,
				displayMsg : 'Displaying {0} - {1} of {2}',
				displaySize : '5|10|15|20|all',
				emptyMsg : "No data to display"
			});
	// 寄样明细表格
	var grid = new Ext.grid.GridPanel({
				region : "center",
				flex:2,
				height:500,
				id : "orderfacGridCopy",
				stripeRows : true,
				//margins : "0 5 0 0",
				//bodyStyle : 'width:100%',
				store : ds, // 加载数据源
				cm : cm, // 加载列
				sm : sm,
				loadMask : true, // 是否显示正在加载
				bbar : toolBar,
				viewConfig : {
					forceFit : false
				}
			});
	var form = new Ext.form.FormPanel({
		region:'east',
		//anchor:'50%',
		height:500,
		id:'detailData',
		title:'Data',
		flex:1,
		frame:true,
		fbar:[{
			text:'Save',
			handler:me.saveCopy.createDelegate(me,[])
		}],
		defaults:{
			xtype:'textarea',
			height:50,
			anchor:'95%'
		},
		items:[{
			labelWidth:70,
			//xtype:'textfield',
			fieldLabel:'Size',
			name:'eleInchDesc'
		},{
			labelWidth:70,
			//xtype:'textfield',
			fieldLabel:'Barcode',
			name:'barcode',
			height:20
		},{
			labelWidth:70,
			//xtype:'textfield',
			fieldLabel:'Client Art No',
			name:'custNo'
		},{
			labelWidth:70,
			//xtype:'textfield',
			fieldLabel:'Color',
			name:'eleCol'
		},{
			labelWidth:70,
			//xtype:'textfield',
			fieldLabel:'Weight',
			name:'eleComposetype'
		},{
			labelWidth:70,
			//xtype:'textfield',
			fieldLabel:'Smell',
			name:'eleDesc'
		},{
			labelWidth:70,
			//xtype:'textfield',
			fieldLabel:'Packing',
			name:'eleTypenameLv1'
		},{
			labelWidth:70,
			//xtype:'textfield',
			fieldLabel:'General',
			name:'eleTypenameLv2'
		},{
			xtype:'combo',
			editable : false,
			triggerAction : 'all',
			selectOnFocus : true,
			store : new Ext.data.SimpleStore({
				fields : ["tp", "name"],
				data : [[0, 'No'], [1, 'Yes']]
			}),
			valueField : "tp",
			displayField : "name",
			mode : 'local',
			fieldLabel:'Approve',
			anchor : "98%",
			value:0,
			id:"eleFlag"
		},{
			xtype:'hidden',
			name:'id'
		}]	
	})
	this.items = [{
		layout:'hbox',
		items : [grid,form]
	}]
		ds.load({
				params : {
					start : 0,
					limit : 1000
				}
			});

		OrderfacdetailCopyWin.superclass.initComponent.call(this);
		// 行点击时加载右边折叠面板表单
		grid.on("rowclick", function(grid, rowIndex, e) {
			var row = grid.getStore().getAt(rowIndex);
			var form = Ext.getCmp('detailData');
			form.getForm().loadRecord(row);
		});
	},
	saveCopy:function(){
		var form = Ext.getCmp('detailData');
		var values = form.getForm().getFieldValues();
		if(Ext.isEmpty(values['id'])){
			Ext.Msg.alert('Info','Please choose a record.');
			return;
		}
		cotOrderFacService.saveCopy(Ext.encode(values),function(res){
			var grid = Ext.getCmp('orderfacGridCopy');
			grid.getStore().reload();
		})
	},
//	getIds:function () {
//		var list = sm.getSelections();
//		var res = new Array();
//		Ext.each(list, function(item) {
//					res.push(item.id);
//				});
//		return res;
//	}
	isShowCommentWin: function(){
		var grid = Ext.getCmp('orderfacGridCopy');
		var store = grid.getStore();
		var canShowComment = true;
		store.each(function(record){
			if(Ext.isEmpty(record.get('eleFlag'))|| record.get('eleFlag') == 0){
				canShowComment = false;
				return false;
			}
		});
		return canShowComment;
	}
})
Ext.onReady(function() {
	
	

});