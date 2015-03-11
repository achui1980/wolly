OrderfacdetailCopyWin = Ext.extend(Ext.Window,{
	width:700,
	height:500,
	closeAction:'close',
	initComponent:function(){
	var orderfacMap;
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
				name : "boxIbCount"
			}, {
				name : "boxMbCount"
			}, {
				name : "boxObCount"
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
				name : "boxCbm",
				convert : numFormat.createDelegate(this, [cbmStr], 3)
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
			}]);
	// 创建数据源
	var ds = new Ext.data.Store({
		autoSave : false,
		method : 'post',
		proxy : new Ext.data.HttpProxy({
					api : {
						read : "cotorderfac.do?method=queryDetail&orderId="
								+ $('pId').value + "&flag=orderFacDetail"
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
				}, {
					header : "Sort",
					align : 'center',
					dataIndex : "sortNo",
					menuDisabled : true,
					width : 40
				}, {
					header : "W&C Art No.",
					dataIndex : "eleId",
					width : 120
				}, {
					header : "Supplier's Art No.",
					dataIndex : "factoryNo",
					width : 140
				}, {
					header : "Client Art No.",
					dataIndex : "custNo",
					width : 100
				}, {
					header : "Barcode",
					dataIndex : "barcode",
					width : 120
				}, {}, {
					header : "Descripiton",
					dataIndex : "eleNameEn",
					editor : new Ext.form.TextArea({
								maxLength : 500
							}),
					width : 100
				},{
					header : "Size Description",
					dataIndex : "eleInchDesc",
					width : 100
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
				id : "orderfacGridCopy",
				stripeRows : true,
				margins : "0 5 0 0",
				bodyStyle : 'width:100%',
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
		width:200,
		fbar:[{
			text:'Save'
		}],
		items:[{
			xtype:'textfield',
			fieldLabel:'Size',
			name:'eleInchDesc'
		}]	
	})
	this.items = [{
		layout:'border',
		items : [grid,form]
	}]
		ds.load({
				params : {
					start : 0,
					limit : 1000
				}
			});

		OrderfacdetailCopyGrid.superclass.initComponent.call(this);
		// 行点击时加载右边折叠面板表单
		grid.on("rowclick", function(grid, rowIndex, e) {});
	}
//	getIds:function () {
//		var list = sm.getSelections();
//		var res = new Array();
//		Ext.each(list, function(item) {
//					res.push(item.id);
//				});
//		return res;
//	}
})
Ext.onReady(function() {
	
	

});