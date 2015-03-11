var grid = null;
Ext.onReady(function() {

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
				name : "orderNo"
			}, {
				name : "poNo"
			}, {
				name : "eleId"
			}, {
				name : "eleName"
			}, {
				name : "orderPrice"
			}, {
				name : "currencyId"
			}, {
				name : "detailId"
			}, {
				name : "orderTime",
				sortType : timeSortType.createDelegate(this)
			}, {
				name : "customerShortName"
			}, {
				name : "empsName"
			}, {
				name : "priceFac"
			}, {
				name : "priceFacUint"
			}, {
				name : "boxCount"
			}, {
				name : "containerCount"
			}, {
				name : "unBoxCount"
			}, {
				name : "boxObCount"
			}, {
				name : "contactId"
			}

	]);
	// 创建数据源
	var ds = new Ext.data.Store({
				proxy : new Ext.data.HttpProxy({
							url : "cotqueryall.do?method=queryOrder"
						}),
				reader : new Ext.data.JsonReader({
							root : "data",
							totalProperty : "totalCount",
							idProperty : "detailId"
						}, roleRecord)
			});
	// 创建复选框列
	var sm = new Ext.grid.CheckboxSelectionModel();
	// 创建需要在表格显示的列
	var cm = new Ext.grid.ColumnModel({
		defaults : {
			sortable : true
		},
		columns : [sm, {
					header : "ID",
					dataIndex : "id",
					width : 50,
					hidden : true
				}, {
					header : "P.I No.",
					dataIndex : "orderNo",
					width : 150
				}, {
					header : "Date",
					dataIndex : "orderTime",
					width : 90,
					renderer : function(value, meta, rec, rowIdx, colIdx, ds) {
						if (value)
							return Ext.util.Format.date(new Date(value.time),
									"Y-m-d");
						else
							return value;
					}
				}, {
					header : "Client",
					dataIndex : "customerShortName",
					width : 110
				}, {
					//header : "PO#",
					header:'ClientP/O',
					dataIndex : "poNo",
					width : 90
				}, {
					header : "Art No.",
					dataIndex : "eleId",
					width : 100
				}, {
					header : "Description",
					dataIndex : "eleName",
					width : 130
				}, {
					header : "Unit price",
					dataIndex : "orderPrice",
					width : 80,
					renderer : function(value, meta, rec, rowIdx, colIdx, ds) {
						return Ext.util.Format.number(value, "0.000")
					}
				}, {
					header : "Currency",
					dataIndex : "currencyId",
					width : 60,
					renderer : function(value, meta, rec, rowIdx, colIdx, ds) {
						return parent.currencyMap[value]
					}
				}, {
					header : "P.I price",
					dataIndex : "priceFac",
					width : 70,
					renderer : function(value, meta, rec, rowIdx, colIdx, ds) {
						return Ext.util.Format.number(value, "0.000")
					}
				}, {
					header : "Currency",
					dataIndex : "priceFacUint",
					width : 80,
					renderer : function(value, meta, rec, rowIdx, colIdx, ds) {
						return parent.currencyMap[value]
					}
				}, {
					header : "QTY",
					dataIndex : "boxCount",
					width : 80
				}, {
					header : "CTNS",
					dataIndex : "containerCount",
					width : 80
				}, {
					header : "Remain QTY",
					dataIndex : "unBoxCount",
					width : 100
				}, {
					header : "Remain CTNS",
					dataIndex : "unBoxCount",
					width : 100,
					renderer : function(value, metaData, record, rowIndex,
							colIndex, store) {
						if (record.data.boxObCount == 0) {
							return 0;
						}
						if (value % record.data.boxObCount != 0) {
							return parseInt(value / record.data.boxObCount) + 1;
						} else {
							return value / record.data.boxObCount;
						}
					}
				}, {
					header : "Client Contact",
					dataIndex : "contactId",
					width : 130,
					renderer : function(value, meta, rec, rowIdx, colIdx, ds) {
						return parent.priMap[value]
					}
				}, {
					header : "Sales",
					hidden:true,
					dataIndex : "empsName",
					width : 80
				}, {
					header : "Operation",
					dataIndex : "id",
					renderer : function(value, metaData, record, rowIndex,
							colIndex, store) {
						var detailId = record.data.detailId;
						var eleId = record.data.eleId;
						var mod = '<a href="javascript:findOut(' + detailId
								+ ',\'' + eleId + '\')">Shipment</a>';
						return mod;
					}
				}]
	});
	var toolBar = new Ext.PagingToolbar({
				pageSize : 15,
				store : ds,
				displayInfo : true,
				// displayMsg : '显示第 {0} - {1}条记录 共{2}条记录',
				displaySize : '5|10|15|20|all'
				// emptyMsg : "//displayMsg : '显示第 {0} - {1}条记录 共{2}条记录'"
			});
	grid = new Ext.grid.GridPanel({
				id : "chekGrid",
				stripeRows : true,
				bodyStyle : 'width:100%',
				store : ds, // 加载数据源
				cm : cm, // 加载列
				sm : sm,
				loadMask : true, // 是否显示正在加载
				bbar : toolBar,
				border : false,
				viewConfig : {
					forceFit : false
				}
			});

	// 查看该货号导入的出货单据
	function showOrderOut() {
		var recs = sm.getSelections();
		if (recs.length == 0) {
			Ext.MessageBox.alert('Message', "Please select a record!");
			return;
		}
		if (recs.length > 1) {
			Ext.MessageBox.alert('Message', "Please select only one!");
			return;
		}
		var detailId = recs[0].data.detailId;
		var eleId = recs[0].data.eleId;
		var win = new OrderToOrderOutWin({
					'oId' : detailId,
					'eleId' : eleId
				});
		win.show();
	}

	// 右键菜单
	var rightMenu = new Ext.menu.Menu({
				id : "rightMenu",
				items : [{
							text : "Shipment",
							handler : showOrderOut
						}]
			});
	function rightClickFn(client, rowIndex, e) {
		e.preventDefault();
		rightMenu.showAt(e.getXY());
	}
	grid.on("rowcontextmenu", rightClickFn);

	var viewport = new Ext.Viewport({
				layout : "fit",
				items : [grid]
			});
	ds.on('beforeload', function() {
				ds.baseParams = parent.form.getForm().getValues();
			});
	// 分页基本参数
	ds.load({
				params : {
					start : 0,
					limit : 15
				}
			});
	grid.on("rowdblclick", showPWin)

	// 显示客户编辑页面
	function showPWin(gd, rowIndex) {
		// 添加权限判断
		var isPopedom = checkPopedoms('cotorder.do', "MOD");
		if (!isPopedom) {
			Ext.MessageBox.alert('Message', "'Sorry, you do not have Authority!");
			return;
		}
		var rec = gd.getStore().getAt(rowIndex);
		var obj = rec.get("id");
		openFullWindow('cotorder.do?method=addOrder&id=' + obj);
	}
});
// 查看出货数据
function findOut(detailId, eleId) {
	var win = new OrderToOrderOutWin({
				'oId' : detailId,
				'eleId' : eleId
			});
	win.show();
}